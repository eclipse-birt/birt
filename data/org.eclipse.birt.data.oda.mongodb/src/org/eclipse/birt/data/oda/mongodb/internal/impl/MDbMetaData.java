/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;

import org.bson.BSON;
import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb.impl.MDbConnection;
import org.eclipse.birt.data.oda.mongodb.impl.MDbQuery;
import org.eclipse.birt.data.oda.mongodb.impl.MongoDBDriver;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties.CommandOperationType;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

import com.mongodb.Bytes;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

/**
 * Utility class to retrieve the metadata of a MongoDB database and collections.
 */
public class MDbMetaData {
	public static final int DEFAULT_META_DATA_SEARCH_LIMIT = 1;
	public static int count = 0;

	private static final MDbMetaData sm_factory = new MDbMetaData();
	private static final DocumentsMetaData sm_emptyFields = sm_factory.new DocumentsMetaData();
	private static final FieldMetaData sm_emptyFieldMetaData = sm_factory.new FieldMetaData(DriverUtil.EMPTY_STRING);

	private static final String SYSTEM_NAMESPACE_PREFIX = "system."; //$NON-NLS-1$
	static final String FIELD_FULL_NAME_SEPARATOR = "."; //$NON-NLS-1$

	private static final Integer NULL_NATIVE_DATA_TYPE = Integer.valueOf(BSON.NULL);
	private static final Integer STRING_NATIVE_DATA_TYPE = Integer.valueOf(BSON.STRING);
	private static final Integer BOOLEAN_NATIVE_DATA_TYPE = Integer.valueOf(BSON.BOOLEAN);
	private static final Integer NUMBER_NATIVE_DATA_TYPE = Integer.valueOf(BSON.NUMBER);
	private static final Integer NUMBER_INT_NATIVE_DATA_TYPE = Integer.valueOf(BSON.NUMBER_INT);
	private static final Integer DATE_NATIVE_DATA_TYPE = Integer.valueOf(BSON.DATE);
	private static final Integer TIMESTAMP_NATIVE_DATA_TYPE = Integer.valueOf(BSON.TIMESTAMP);
	private static final Integer BINARY_NATIVE_DATA_TYPE = Integer.valueOf(BSON.BINARY);
	private static final Integer ARRAY_NATIVE_DATA_TYPE = Integer.valueOf(BSON.ARRAY);
	private static final Integer OBJECT_NATIVE_DATA_TYPE = Integer.valueOf(BSON.OBJECT);

	private MongoDatabase m_connectedDB;
	private ArrayList<String> collectionNames = new ArrayList<String>();

	public MDbMetaData(Properties connProperties) throws OdaException {
		m_connectedDB = MDbConnection.getMongoDatabase(connProperties);
	}

	public MDbMetaData(MongoDatabase connectedDB) {
		if (connectedDB == null)
			throw new IllegalArgumentException("null"); //$NON-NLS-1$
		m_connectedDB = connectedDB;
	}

	private MDbMetaData() {
	}

	/**
	 * Returns the name of the connected database.
	 */
	public String getDatabaseName() {
		return m_connectedDB.getName();
	}

	/**
	 * Returns the sorted list of collection names found in the connected database
	 * of this instance. The returned list excludes system collections, by default.
	 */
	public List<String> getCollectionsList() {
		return getCollectionsList(true);
	}

	/**
	 * Returns the sorted list of collection names found in the connected database
	 * of this instance.
	 * 
	 * @param excludeSystemCollections true indicates to exclude system collections
	 *                                 from the returned list; false to include.
	 */
	public List<String> getCollectionsList(boolean excludeSystemCollections) {
		if (collectionNames.isEmpty()) {
			try {
				MongoIterable<String> collectionNamesIterable = m_connectedDB.listCollectionNames();
				for (final String collectionName : collectionNamesIterable) {
					collectionNames.add(collectionName);
				}
			} catch (MongoException ex) {
				// log and ignore
				DriverUtil.getLogger().log(Level.INFO, "Ignoring error to get collection names from database.", ex); //$NON-NLS-1$
				return Collections.emptyList();
			}
		}
		if (excludeSystemCollections) {
			List<String> filteredNames = new ArrayList<String>(collectionNames.size());
			for (String collectionName : collectionNames) {
				if (!collectionName.startsWith(SYSTEM_NAMESPACE_PREFIX))
					filteredNames.add(collectionName);
			}
			return filteredNames;
		}

		// return the complete list returned by mongoDB, which is already sorted
		List<String> collectionNamesCopy = new ArrayList<String>(Collections.nCopies(collectionNames.size(), " "));
		Collections.copy(collectionNamesCopy, collectionNames);
		return new ArrayList<String>(collectionNamesCopy);
	}

	public boolean collectionExists(String collectionName) {
		if (collectionNames.isEmpty()) {
			getCollectionsList();
		}
		for (String name : collectionNames) {
			if (name.equalsIgnoreCase(collectionName)) {
				return true;
			}
		}
		return false;
	}

	public MongoCollection<Document> getCollection(String collectionName) {
		if (!collectionExists(collectionName))
			return null;
		return m_connectedDB.getCollection(collectionName);
	}

	/**
	 * Returns all fields' name and corresponding metadata found in the specified
	 * collection.
	 * 
	 * @param collectionName name of MongoDB collection (i.e. table)
	 * @param searchLimit    maximum number of documents, i.e. rows to search for
	 *                       available fields; a zero or negative value would adopt
	 *                       the default limit
	 * @param runtimeProps   an instance of QueryProperties containing the data set
	 *                       runtime property values; may be null to apply all
	 *                       default values in finding the available fields metadata
	 * @return the DocumentsMetaData object that contains the list of available
	 *         field names and corresponding metadata; an empty list is returned if
	 *         no available fields are found, or if the specified collection does
	 *         not exist
	 * @throws OdaException
	 */
	public DocumentsMetaData getAvailableFields(String collectionName, int searchLimit, QueryProperties runtimeProps)
			throws OdaException {
		MongoCollection<Document> collection = getCollection(collectionName);
		if (collection == null && !runtimeProps.hasRunCommand()) {
			if (runtimeProps.getOperationType() == CommandOperationType.RUN_DB_COMMAND
					&& runtimeProps.getOperationExpression().isEmpty())
				throw new OdaException(Messages.bind(Messages.mDbMetaData_missingCmdExprText,
						runtimeProps.getOperationType().displayName()));
			else
				throw new OdaException(Messages.bind(Messages.mDbMetaData_invalidCollectionName, collectionName));
		}

		if (searchLimit <= 0) // no limit specified, applies meta data design-time default
			searchLimit = DEFAULT_META_DATA_SEARCH_LIMIT;

		// handle optional command operation
		if (runtimeProps.hasValidCommandOperation()) {
			QueryModel.validateCommandSyntax(runtimeProps.getOperationType(), runtimeProps.getOperationExpression());

			Iterable<Document> commandResults = null;
			if (runtimeProps.hasAggregateCommand())
				commandResults = MDbOperation.callAggregateCmd(collection, runtimeProps);
			else if (runtimeProps.hasMapReduceCommand()) {
				commandResults = MDbOperation.callMapReduceCmd(collection, runtimeProps);
				// skip running $query on output collection in discovering metadata
			} else if (runtimeProps.hasRunCommand())
				commandResults = MDbOperation.callDBCommand(m_connectedDB, runtimeProps);

			if (commandResults != null)
				return getMetaData(commandResults, searchLimit);
			return sm_emptyFields;
		}

		// run search query operation by default
		FindIterable<Document> rowsCursor = collection.find();

		if (searchLimit > 0)
			rowsCursor.limit(searchLimit);

		QueryProperties mdCursorProps = runtimeProps != null ? runtimeProps : QueryProperties.defaultValues();
		MDbOperation.applyPropertiesToCursor(rowsCursor, mdCursorProps, false);

		return getMetaData(rowsCursor);
	}

	/**
	 * Returns the default database port.
	 */
	public static int defaultPort() {
		return ServerAddress.defaultPort();
	}

	static String[] splitFieldName(String fieldFullName) {
		if (fieldFullName == null || fieldFullName.isEmpty())
			return new String[0];
		return fieldFullName.split('\\' + FIELD_FULL_NAME_SEPARATOR);
	}

	static String getSimpleName(String fieldFullName) {
		String[] nameFragments = splitFieldName(fieldFullName);
		if (nameFragments.length == 0)
			return DriverUtil.EMPTY_STRING; // something is wrong; not able to find simple name
		return nameFragments[nameFragments.length - 1];
	}

	static String stripParentName(String fieldFullName, String parentName) {
		if (parentName == null || parentName.isEmpty())
			return fieldFullName; // nothing applicable to strip
		int stripFromIndex = parentName.length() + FIELD_FULL_NAME_SEPARATOR.length();
		if (stripFromIndex > fieldFullName.length()) // out of bound index
			return fieldFullName; // n/a in fieldFullName to strip
		return fieldFullName.substring(stripFromIndex);
	}

	static String formatFieldLevelNames(String[] fieldLevelNames, int fromIndex, int toIndex) {
		if (fromIndex < 0 || toIndex >= fieldLevelNames.length || fromIndex > toIndex)
			throw new IllegalArgumentException("MDbMetaData#formatFieldLevelNames: Index argument(s) out of range."); //$NON-NLS-1$

		StringBuffer fieldName = new StringBuffer();
		for (int i = fromIndex; i <= toIndex; i++) {
			if (fieldName.length() > 0)
				fieldName.append(FIELD_FULL_NAME_SEPARATOR);
			fieldName.append(fieldLevelNames[i]);
		}
		return fieldName.toString();
	}

	/**
	 * Find and return the metadata of the specified field.
	 * 
	 * @param fieldFullName   the full name of a field
	 * @param fromDocMetaData the metadata of documents found in a collection
	 * @return the FieldMetaData instance of the specified field full name
	 */
	public static FieldMetaData findFieldByFullName(String fieldFullName, DocumentsMetaData fromDocMetaData) {
		String[] nameFragments = splitFieldName(fieldFullName);
		if (nameFragments.length == 0)
			return null; // something is wrong; not able to find a match

		FieldMetaData firstLevelMd = fromDocMetaData.getFieldMetaData(nameFragments[0]);
		if (nameFragments.length == 1) // specified field has only 1 level
			return firstLevelMd;

		// expects the first level to be a parent field
		if (!firstLevelMd.hasChildDocuments())
			return null; // does not match metadata; not able to find a match
		// remove the parent name to get the next level child's full name
		String childFullName = stripParentName(fieldFullName, nameFragments[0]);
		return findFieldByFullName(childFullName, firstLevelMd.getChildMetaData());
	}

	/**
	 * Indicates whether the specified field has flattening support.
	 * 
	 * @param fieldMd       field metadata; may be that of top-level array field or
	 *                      a child field
	 * @param topLevelDocMD top level document metadata returned by
	 *                      {@link #getAvailableFields(String, int, QueryProperties)}
	 */
	public static boolean isFlattenableNestedField(FieldMetaData fieldMd, DocumentsMetaData topLevelDocMD) {
		if (fieldMd == null)
			return false;
		DocumentsMetaData containingDocMD = fieldMd.getContainingMetaData();
		if (containingDocMD == null) // top-level field
		{
			containingDocMD = topLevelDocMD; // use top-level metadata
		}
		String cachedAncestorName = containingDocMD.getFlattenableFieldName();
		if (cachedAncestorName != null && cachedAncestorName.equals(fieldMd.getFullName()))
			return true;
		if (fieldMd.isChildField())
			return isFlattenableNestedField(fieldMd.getParentMetaData(), topLevelDocMD);
		return false;
	}

	/**
	 * Find the FieldMetaData of each field specified by its full name.
	 * 
	 * @param fieldFullNames  a list of fields in their full name.
	 * @param fromDocMetaData the metadata of documents found in a collection
	 * @return a flattened Map of each field's full name with its corresponding
	 *         FieldMetaData. If a field name is not found in the specified
	 *         DocumentsMetaData, no entry is put in the returned Map.
	 */
	public static Map<String, FieldMetaData> flattenFieldsMetaData(List<String> fieldFullNames,
			DocumentsMetaData fromDocMetaData) {
		if (fieldFullNames.isEmpty())
			return Collections.emptyMap(); // done; no fields to find

		Map<String, FieldMetaData> resultFieldsMD = new LinkedHashMap<String, FieldMetaData>(fieldFullNames.size());
		for (String fieldFullName : fieldFullNames) {
			FieldMetaData fieldMD = findFieldByFullName(fieldFullName, fromDocMetaData);
			if (fieldMD != null)
				resultFieldsMD.put(fieldFullName, fieldMD);
		}
		return resultFieldsMD;
	}

	/**
	 * Flatten all the fields, including nested ones, in the specified
	 * DocumentsMetaData into the specified Map.
	 * 
	 * @param fromDocMetaData  the metadata of documents found in a collection
	 * @param toResultFieldsMD the Map to which append the entries for all fields in
	 *                         the specified DocumentsMetaData
	 * @return a flattened Map of each field's full name with its corresponding
	 *         FieldMetaData.
	 */
	public static Map<String, FieldMetaData> flattenFieldsMetaData(DocumentsMetaData fromDocMetaData,
			Map<String, FieldMetaData> toResultFieldsMD) {
		if (toResultFieldsMD == null)
			toResultFieldsMD = new LinkedHashMap<String, FieldMetaData>();
		for (FieldMetaData fieldMD : fromDocMetaData.m_fieldsMetaData.values()) {
			toResultFieldsMD.put(fieldMD.getFullName(), fieldMD);
			if (fieldMD.hasChildDocuments())
				toResultFieldsMD = flattenFieldsMetaData(fieldMD.getChildMetaData(), toResultFieldsMD);
		}
		return toResultFieldsMD;
	}

	/**
	 * An internal utility method. Discover and return the metadata of one or more
	 * documents in the specified MongoDB cursor, up to the searchLimit count.
	 * 
	 * @param iterable an inactive MongoDB cursor, before having executed query, to
	 *                 iterate the results expects caller to have already set
	 *                 searchLimit and other options on the result cursor
	 * @return a DocumentsMetaData representing all the fields and corresponding
	 *         metadata found in the specified iterated cursor
	 */
	public static DocumentsMetaData getMetaData(Iterable<Document> iterable) {
		if (iterable == null)
			return sm_emptyFields;

		DocumentsMetaData newMetaData = sm_factory.new DocumentsMetaData();
		for (Document document : iterable) {
			newMetaData.addDocumentMetaData(document, null); // top-level doc
																// has no parent
		}
		return newMetaData;
	}

	public static DocumentsMetaData getMetaData(Iterable<Document> iterable, int searchLimit) {
		if (iterable == null)
			return sm_emptyFields;

		DocumentsMetaData newMetaData = sm_factory.new DocumentsMetaData();
		// iterate thru searchLimit documents to discover metadata
		int count = 1;
		for (Document document : iterable) {
			if (!(searchLimit <= 0 || count <= searchLimit)) {
				break;
			}
			newMetaData.addDocumentMetaData(document, null); // top-level doc has no parent
			count++;
		}
		return newMetaData;
	}

	private static Integer getPreferredScalarNativeDataType(Set<Integer> nativeDataTypes) {
		if (nativeDataTypes.isEmpty())
			return NULL_NATIVE_DATA_TYPE; // none available
		if (nativeDataTypes.size() == 1)
			return nativeDataTypes.iterator().next(); // return the only data type available

		// more than one native data types in field

		if (nativeDataTypes.contains(STRING_NATIVE_DATA_TYPE))
			return STRING_NATIVE_DATA_TYPE; // String data type takes precedence over other scalar types

		// check if any of the native data types map to an ODA String
		Set<Integer> nonStringNativeDataTypes = new HashSet<Integer>(nativeDataTypes.size());
		for (Integer nativeDataType : nativeDataTypes) {
			if (nativeDataType == NULL_NATIVE_DATA_TYPE || nativeDataType == ARRAY_NATIVE_DATA_TYPE
					|| nativeDataType == OBJECT_NATIVE_DATA_TYPE)
				continue; // skip non-scalar data types
			int odaDataType = ManifestExplorer.getInstance().getDefaultOdaDataTypeCode(nativeDataType,
					MongoDBDriver.ODA_DATA_SOURCE_ID, MDbQuery.ODA_DATA_SET_ID);
			if (odaDataType == Types.CHAR) // maps to ODA String data type
				return nativeDataType; // String data type takes precedence over other scalar types

			nonStringNativeDataTypes.add(nativeDataType);
		}

		if (nonStringNativeDataTypes.isEmpty())
			return NULL_NATIVE_DATA_TYPE; // none available
		if (nonStringNativeDataTypes.size() == 1)
			return nonStringNativeDataTypes.iterator().next(); // return first element by default

		// more than one native data types in field are not mapped to ODA String;
		// check if they have mixed data type categories.
		boolean isNumeric = nonStringNativeDataTypes.contains(NUMBER_NATIVE_DATA_TYPE)
				|| nonStringNativeDataTypes.contains(NUMBER_INT_NATIVE_DATA_TYPE)
				|| nonStringNativeDataTypes.contains(BOOLEAN_NATIVE_DATA_TYPE);
		boolean isDatetime = nonStringNativeDataTypes.contains(DATE_NATIVE_DATA_TYPE)
				|| nonStringNativeDataTypes.contains(TIMESTAMP_NATIVE_DATA_TYPE);
		boolean isBinary = nonStringNativeDataTypes.contains(BINARY_NATIVE_DATA_TYPE);

		if (isNumeric && !isDatetime && !isBinary) // numeric only
		{
			if (nonStringNativeDataTypes.contains(NUMBER_NATIVE_DATA_TYPE))
				return NUMBER_NATIVE_DATA_TYPE; // Number takes precedence over other numeric data types
			return NUMBER_INT_NATIVE_DATA_TYPE; // Integer takes precedence over Boolean
		}

		if (!isNumeric && isDatetime && !isBinary) // Date and Timestamp data types only
		{
			return TIMESTAMP_NATIVE_DATA_TYPE; // Timestamp takes precedence over Date
		}

		// multiple non-String native data types must be of mixed data type categories
		return STRING_NATIVE_DATA_TYPE; // use String to handle mixed data types
	}

	/**
	 * The metadata of all fields discovered in one or more documents found in a
	 * collection.
	 */
	public class DocumentsMetaData {
		// an ordered Map w/ the field name as the key,
		// and its corresponding metadata as value
		private Map<String, FieldMetaData> m_fieldsMetaData = new LinkedHashMap<String, FieldMetaData>();

		// flattening of nested collection is supported for only one applicable field in
		// a document,
		// and is tracked in this variable to ensure consistency in metadata and
		// fetching result set
		private String m_nestedCollFieldName;

		@SuppressWarnings("unchecked")
		private void addDocumentMetaData(Object documentObj, FieldMetaData parentMd) {
			if (documentObj == null)
				return;
			Document doc = null;
			if (documentObj instanceof List<?>) {
				List<Document> docList = (List<Document>) documentObj;
				if (docList.size() > 0)
					doc = ((List<Document>) documentObj).get(0);
			} else {
				doc = (Document) documentObj;
			}

			if (doc != null) {
				Set<String> fieldNames = doc.keySet();
				for (String fieldName : fieldNames) {
					Object value = doc.get(fieldName);
					addDataTypeOfFieldValue(fieldName, value, parentMd);
				}
			}
		}

		private FieldMetaData addDataTypeOfFieldValue(String fieldName, Object fieldValue, FieldMetaData parentMd) {
			// add the specified data type to existing set, if exists, for the specified
			// field;
			// the same field name in a different doc may have a different data type
			FieldMetaData fieldMd = m_fieldsMetaData.get(fieldName);
			if (fieldMd == null) {
				fieldMd = new FieldMetaData(fieldName);
				fieldMd.setParentMetaData(parentMd);
			}
			fieldMd.addDataType(fieldValue);
			m_fieldsMetaData.put(fieldMd.getSimpleName(), fieldMd);
			return fieldMd;
		}

		@SuppressWarnings("unused")
		private void removeField(String fieldName) {
			if (fieldName != null)
				m_fieldsMetaData.remove(fieldName);
		}

		/**
		 * Returns the simple names of first level fields in the sequence that they were
		 * discovered from a collection.
		 */
		public List<String> getFieldNames() {
			// maintain the ordering of the fields that they were discovered
			List<String> docFields = new ArrayList<String>(m_fieldsMetaData.size());
			for (String fieldName : m_fieldsMetaData.keySet()) {
				docFields.add(fieldName);
			}
			return docFields;
		}

		/**
		 * Returns the simple names of first level fields in natural ascending order.
		 */
		public List<String> getSortedFieldNames() {
			Set<String> attributeNames = m_fieldsMetaData.keySet();
			return sortFieldNames(attributeNames);
		}

		private List<String> sortFieldNames(Set<String> fieldNames) {
			if (fieldNames == null || fieldNames.isEmpty())
				return Collections.emptyList();

			String[] attrNamesArray = (String[]) fieldNames.toArray(new String[fieldNames.size()]);
			Arrays.sort(attrNamesArray);

			List<String> sortedAttrList = new ArrayList<String>(attrNamesArray.length);
			sortedAttrList.addAll(Arrays.asList(attrNamesArray));
			return sortedAttrList;
		}

		public FieldMetaData getFieldMetaData(String fieldSimpleName) {
			FieldMetaData fieldMd = m_fieldsMetaData.get(fieldSimpleName);
			return fieldMd != null ? fieldMd : sm_emptyFieldMetaData;
		}

		public void setFlattenableFields(Map<String, FieldMetaData> resultFieldsMD, boolean isTopLevelDoc) {
			if (resultFieldsMD == null || resultFieldsMD.isEmpty())
				return; // no result set fields; nothing to set

			FieldMetaData flattenableFieldMD = null;
			if (m_nestedCollFieldName != null)
				flattenableFieldMD = resultFieldsMD.get(m_nestedCollFieldName);

			if (flattenableFieldMD == null) {
				// iterate in the sequence of selected fields in result set
				for (FieldMetaData resultFieldMD : resultFieldsMD.values()) {
					FieldMetaData fieldLevelMD = resultFieldMD;
					while (fieldLevelMD != null) {
						if (!m_fieldsMetaData.containsValue(fieldLevelMD)) // not a field in this document
						{
							fieldLevelMD = fieldLevelMD.getParentMetaData(); // check its parent field
							continue;
						}

						// is a field of this document;
						// determine the first field with nested collection of documents, whose result
						// set may be flattened
						if (fieldLevelMD.isArrayOfDocuments()) {
							m_nestedCollFieldName = fieldLevelMD.getFullName();
							flattenableFieldMD = fieldLevelMD;
						}
						// done checking on this field
						fieldLevelMD = null;
					}
					if (flattenableFieldMD != null) // done finding the flattenable field
						break;
				}
			}

			// set its child document's flattenable field
			if (flattenableFieldMD != null) {
				if (flattenableFieldMD.hasChildDocuments())
					flattenableFieldMD.getChildMetaData().setFlattenableFields(resultFieldsMD, false);
				return;
			}

			// no nested collection of documents exist;
			// determine the top-level's first array field of scalar values that may be
			// flattened
			if (m_nestedCollFieldName == null && isTopLevelDoc) {
				for (FieldMetaData resultFieldMD : resultFieldsMD.values()) {
					if (resultFieldMD.isArrayOfScalarValues()) {
						m_nestedCollFieldName = resultFieldMD.getFullName();
						break;
					}
				}
			}
		}

		public String getFlattenableFieldName() {
			return m_nestedCollFieldName;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer("\n " + getClass().getSimpleName() + ":"); //$NON-NLS-1$ //$NON-NLS-2$
			buf.append("; flattenableFieldName: " + m_nestedCollFieldName); //$NON-NLS-1$
			for (Entry<String, FieldMetaData> entry : m_fieldsMetaData.entrySet()) {
				buf.append("\n  field key: " + entry.getKey()); //$NON-NLS-1$
				buf.append("; metadata: " + entry.getValue()); //$NON-NLS-1$
			}
			return buf.toString();
		}
	}

	/**
	 * The metadata of an individual field found in one or more documents in a
	 * collection. A field is uniquely identified by name within a document. If a
	 * field value is a sub-document, this includes the sub-document's metadata.
	 */
	public class FieldMetaData {
		private static final String ARRAY_NOTATION = "[]"; //$NON-NLS-1$

		private String m_simpleName;
		private Set<Integer> m_nativeDataTypes;
		private DocumentsMetaData m_childDocMetaData;
		private FieldMetaData m_parentMd; // may be null if no parent doc field
		private String[] m_nameFragments; // cached fragments of field full name

		private FieldMetaData(String simpleName) {
			m_simpleName = simpleName;
		}

		public String getSimpleName() {
			return m_simpleName;
		}

		public String getSimpleDisplayName() {
			return hasArrayDataType() ? getSimpleName() + ARRAY_NOTATION : getSimpleName();
		}

		public String getFullName() {
			if (m_parentMd == null)
				return getSimpleName();

			StringBuffer fullName = new StringBuffer(m_parentMd.getFullName());
			fullName.append(FIELD_FULL_NAME_SEPARATOR);
			fullName.append(getSimpleName());
			return fullName.toString();
		}

		public String getFullDisplayName() {
			if (m_parentMd == null)
				return getSimpleDisplayName();

			StringBuffer fullName = new StringBuffer(m_parentMd.getFullDisplayName());
			fullName.append(FIELD_FULL_NAME_SEPARATOR);
			fullName.append(getSimpleDisplayName());
			return fullName.toString();
		}

		String[] getLevelNames() {
			if (m_nameFragments == null)
				m_nameFragments = splitFieldName(getFullName());
			return m_nameFragments;
		}

		private void setParentMetaData(FieldMetaData parentMd) {
			if (parentMd != null)
				m_parentMd = parentMd;
		}

		private FieldMetaData getParentMetaData() {
			return m_parentMd;
		}

		private DocumentsMetaData getContainingMetaData() {
			if (m_parentMd == null)
				return null;
			return m_parentMd.getChildMetaData();
		}

		/**
		 * Indicates whether this is a child field, contained by a parent document.
		 */
		public boolean isChildField() {
			return m_parentMd != null;
		}

		private void addDataType(Object fieldValue) {
			// add the data type of given fieldValue to existing set, if exists;
			// the same named field set in a different doc may have a different data type
			byte nativeBSonDataTypeCode = Bytes.getType(fieldValue);
			if (m_nativeDataTypes == null)
				m_nativeDataTypes = new HashSet<Integer>(2);
			if (nativeBSonDataTypeCode == -1) {
				if (fieldValue instanceof Document) {
					nativeBSonDataTypeCode = Bytes.OBJECT;
				}
			}
			m_nativeDataTypes.add(Integer.valueOf(nativeBSonDataTypeCode));

			// check if field value contains a document,
			// iteratively get field document's nested metadata
			if (nativeBSonDataTypeCode == BSON.ARRAY) {
				if (fieldValue instanceof java.util.List) {
					java.util.List<?> listOfObjects = (java.util.List<?>) fieldValue;
					if (listOfObjects.size() > 0) {
						// use first element in array to determine metadata
						addDataType(listOfObjects.get(0)); // handles nested arrays
						return;
					}
				}
			}

			Object fieldObjValue = ResultDataHandler.fetchFieldDocument(fieldValue, nativeBSonDataTypeCode);

			if (fieldObjValue != null) // contains nested document
			{
				if (m_childDocMetaData == null)
					m_childDocMetaData = sm_factory.new DocumentsMetaData();
				m_childDocMetaData.addDocumentMetaData(fieldObjValue, this);
			}
		}

		public Integer getPreferredNativeDataType(boolean isAutoFlattening) {
			Set<Integer> nativeDataTypes = getNativeDataTypes();
			if (nativeDataTypes.isEmpty())
				return NULL_NATIVE_DATA_TYPE; // none available

			// determine the preferred data type of a nested array
			if (hasArrayDataType()) {
				if (isAutoFlattening) {
					// if field is an array of scalar data elements,
					// return the scalar data type
					if (!hasDocumentDataType())
						return getScalarNativeDataType();
				}
				return ARRAY_NATIVE_DATA_TYPE;
			}

			if (hasDocumentDataType())
				return OBJECT_NATIVE_DATA_TYPE;

			return getPreferredScalarNativeDataType(nativeDataTypes);
		}

		/**
		 * Returns all the known data types of the specified field. The same named field
		 * could have a different data type in a separate document within a collection.
		 * 
		 * @param ofField field name
		 * @return a set of known MongoDB native data type codes
		 */
		public Set<Integer> getNativeDataTypes() {
			if (m_nativeDataTypes == null)
				return Collections.emptySet();
			return m_nativeDataTypes;
		}

		private Integer getScalarNativeDataType() {
			Set<Integer> nativeDataTypes = getNativeDataTypes();
			Set<Integer> scalarNativeDataTypes = new HashSet<Integer>(nativeDataTypes.size());
			for (Integer nativeDataType : nativeDataTypes) {
				if (nativeDataType == ARRAY_NATIVE_DATA_TYPE || nativeDataType == OBJECT_NATIVE_DATA_TYPE)
					continue; // skip complex types
				scalarNativeDataTypes.add(nativeDataType);
			}
			return getPreferredScalarNativeDataType(scalarNativeDataTypes);
		}

		public boolean isArrayOfScalarValues() {
			if (!hasArrayDataType())
				return false;
			return !hasDocumentDataType();
		}

		public boolean isArrayOfDocuments() {
			if (!hasArrayDataType())
				return false;
			return hasDocumentDataType();
		}

		public boolean hasDocumentDataType() {
			return getNativeDataTypes().contains(OBJECT_NATIVE_DATA_TYPE);
		}

		public boolean hasArrayDataType() {
			return getNativeDataTypes().contains(ARRAY_NATIVE_DATA_TYPE);
		}

		public boolean isDescendantOfArrayField() {
			if (!isChildField())
				return false;
			return m_parentMd.hasArrayDataType() || m_parentMd.isDescendantOfArrayField();
		}

		public String getArrayAncestorName() {
			if (!isChildField()) // has no parent
				return null;
			if (m_parentMd.hasArrayDataType())
				return m_parentMd.getFullName();
			return m_parentMd.getArrayAncestorName();
		}

		/**
		 * Indicates whether this is a parent field, containing nested documents.
		 */
		public boolean hasChildDocuments() {
			return m_childDocMetaData != null;
		}

		public DocumentsMetaData getChildMetaData() {
			return m_childDocMetaData != null ? m_childDocMetaData : sm_emptyFields;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			StringBuffer buf = new StringBuffer("name: " + m_simpleName); //$NON-NLS-1$
			buf.append("; full display name: " + getFullDisplayName()); //$NON-NLS-1$
			buf.append("; nativeDataTypes: " + m_nativeDataTypes); //$NON-NLS-1$
			String parentName = m_parentMd != null ? m_parentMd.getFullDisplayName() : "null"; //$NON-NLS-1$
			buf.append("; parent field: " + parentName); //$NON-NLS-1$
			buf.append("; child document metadata: " + m_childDocMetaData); //$NON-NLS-1$
			return buf.toString();
		}
	}

}
