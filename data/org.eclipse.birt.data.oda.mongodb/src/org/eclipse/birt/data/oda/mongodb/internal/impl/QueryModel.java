/*
 *************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.data.oda.mongodb.internal.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bson.Document;
import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSet;
import org.eclipse.birt.data.oda.mongodb.impl.MDbResultSetMetaData;
import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryProperties.CommandOperationType;
import org.eclipse.birt.data.oda.mongodb.nls.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.spec.QuerySpecification;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

/**
 * Represents the model of a MongoDB query. It contains QueryProperties and
 * interacts with query spec defined by an ODA consumer to adjust a query.
 */
public class QueryModel {
	private static final int DEFAULT_RUNTIME_METADATA_SEARCH_LIMIT = QueryProperties.DEFAULT_RUNTIME_METADATA_SEARCH_LIMIT;

	static final String DOC_ID_FIELD_NAME = "_id"; //$NON-NLS-1$
	private static final String GROUP_AGGR_KEY = "$group"; //$NON-NLS-1$
	private static final String SORT_AGGR_KEY = "$sort"; //$NON-NLS-1$
	private static final String EVAL_KEY = "eval"; //$NON-NLS-1$
	private static final String NOLOCK_KEY = "nolock"; //$NON-NLS-1$

	static final String MAP_REDUCE_MAP_FUNCTION = "map";
	static final String MAP_REDUCE_REDUCE_FUNCTION = "reduce";
	private static final String[] REQUIRED_MAPREDUCE_KEYS = new String[] { MAP_REDUCE_MAP_FUNCTION,
			MAP_REDUCE_REDUCE_FUNCTION, "out" }; //$NON-NLS-1$ //$NON-NLS-2$
													// //$NON-NLS-3$
	static final String MAP_REDUCE_CMD_KEY = "mapreduce"; //$NON-NLS-1$
	static final String MAP_REDUCE_CMD_KEY2 = "mapReduce"; //$NON-NLS-1$
	private static final String[] SUPPORTED_DB_COMMANDS = new String[] { "buildInfo", "collStats", "connPoolStats", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			"count", "cursorInfo", "dataSize", "dbStats", "distinct", EVAL_KEY, //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"geoNear", "geoSearch", "getLastError", "getLog", "getPrevError", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$
			"group", "isMaster", "isdbgrid", "listCommands", "listDatabases", "listShards", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
			"ping", "printShardingStatus", "replSetGetStatus", "serverStatus" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	};

	private QueryProperties m_queryProps;
	private MongoDatabase m_connectedDB;
	private MongoCollection<Document> m_mongoCollection;
	private Integer m_metaDataSearchLimit;

	private MDbOperation m_operation;

	public QueryModel(QueryProperties queryProps, MongoDatabase connectedDB) throws OdaException {
		if (connectedDB == null)
			throw new OdaException(new IllegalArgumentException("null com.mongodb.DB")); //$NON-NLS-1$
		m_connectedDB = connectedDB;
		m_queryProps = queryProps;
		initialize();
	}

	public void addQuerySpec(QuerySpecification querySpec) {
		if (querySpec == null)
			return; // done; nothing to add

		// add/override with properties in query spec
		m_queryProps.setNonNullValues(querySpec.getProperties());
	}

	private void initialize() throws OdaException {
		if (m_queryProps == null || m_queryProps.getPropertiesMap().isEmpty())
			throw new OdaException(Messages.queryModel_missingQueryProps);

		// if query has specified no command type or a command that is not a
		// valid Run_DB_Command,
		// validate that collection name is defined and exists in connectedDB
		if (!m_queryProps.hasRunCommand()) {
			String collectionName = m_queryProps.getCollectionName();
			if (collectionName == null || collectionName.isEmpty())
				throw new OdaException(Messages.queryModel_missingCollectionName);

			MDbMetaData metadataUtil = new MDbMetaData(m_connectedDB);
			m_mongoCollection = metadataUtil.getCollection(collectionName);
			if (m_mongoCollection == null)
				throw new OdaException(Messages.bind(Messages.queryModel_invalidCollectionName, collectionName));
		}
	}

	public boolean isValid() {
		return m_mongoCollection != null || (m_queryProps.hasRunCommand() && m_connectedDB != null);
	}

	QueryProperties getQueryProperties() {
		return m_queryProps;
	}

	MongoDatabase getConnectedDB() {
		return m_connectedDB;
	}

	MongoCollection<Document> getCollection() {
		return m_mongoCollection;
	}

	private boolean isPrepared() {
		return m_operation != null;
	}

	public MDbResultSetMetaData getResultSetMetaData() throws OdaException {
		if (!isPrepared())
			prepare();

		return m_operation.getResultSetMetaData();
	}

	private void prepare() throws OdaException {
		if (!isValid())
			throw new OdaException(Messages.queryModel_invalidModelToPrepare);

		m_operation = MDbOperation.createQueryOperation(this);
		m_operation.prepare(m_mongoCollection);
	}

	public MDbResultSet execute() throws OdaException {
		if (!isValid())
			throw new OdaException(Messages.queryModel_invalidModelToExec);

		if (!isPrepared())
			prepare();

		return m_operation.execute();
	}

	private int getMetaDataSearchLimit() {
		if (m_metaDataSearchLimit == null)
			return DEFAULT_RUNTIME_METADATA_SEARCH_LIMIT;
		return m_metaDataSearchLimit;
	}

	int getEffectiveMDSearchLimit(QueryProperties queryProps) {
		// if data set prop setting is invalid or has default, override it with
		// local setting (that may be set by the designer at design time)
		Integer searchLimit = queryProps.getRuntimeMetaDataSearchLimit();
		if (searchLimit == null || searchLimit < 0 || searchLimit == DEFAULT_RUNTIME_METADATA_SEARCH_LIMIT)
			searchLimit = getMetaDataSearchLimit();
		return searchLimit;
	}

	public void setMetaDataSearchLimit(int searchLimit) {
		if (searchLimit > 0)
			m_metaDataSearchLimit = searchLimit;
	}

	public String getEffectiveQueryText() {
		if (!isPrepared())
			return DriverUtil.EMPTY_STRING;
		QueryProperties effectiveProps = m_operation.getEffectiveProperties();
		return effectiveProps != null ? effectiveProps.serialize() : DriverUtil.EMPTY_STRING;
	}

	private static final DBObject parseExprToDBObject(String jsonExpr) throws OdaException {
		return DriverUtil.parseExprToDBObject(jsonExpr);
	}

	/**
	 * Validates the syntax of the specified query expression text.
	 * 
	 * @param queryExpr
	 * @throws OdaException
	 *             throws OdaException if the specified expression has syntax
	 *             error
	 */
	public static void validateQuerySyntax(String queryExpr) throws OdaException {
		if (queryExpr == null)
			return; // nothing to validate
		parseExprToDBObject(queryExpr.trim());
	}

	/**
	 * Validates the syntax of the specified sort expression text.
	 * 
	 * @param sortExpr
	 * @throws OdaException
	 *             throws OdaException if the specified expression has syntax
	 *             error
	 */
	public static void validateSortExprSyntax(String sortExpr) throws OdaException {
		if (sortExpr == null)
			return; // nothing to validate
		DBObject parsedSortObj = parseExprToDBObject(sortExpr.trim());

		if (parsedSortObj instanceof BasicDBObject) {
			BasicDBObject sortExprObj = (BasicDBObject) parsedSortObj;
			for (Object sortKeySpec : sortExprObj.values()) {
				// a Boolean value, both true and false, are handled by Mongo as
				// ascending order;
				// but such behavior is unexpected, thus not allowed in this
				// context
				if (!(sortKeySpec instanceof Number))
					throw new OdaException(Messages.bind(Messages.queryModel_invalidQuerySortExpr, sortExpr));
			}
		}
	}

	/**
	 * Validates the syntax of the specified command expression for the
	 * specified command operation type.
	 * 
	 * @param cmdOp
	 * @param commandExpr
	 * @throws OdaException
	 *             throws OdaException if the specified command is not valid
	 */
	public static void validateCommandSyntax(CommandOperationType cmdOp, String commandExpr) throws OdaException {
		if (cmdOp != CommandOperationType.AGGREGATE && cmdOp != CommandOperationType.MAP_REDUCE
				&& cmdOp != CommandOperationType.RUN_DB_COMMAND)
			return; // nothing to validate

		if (commandExpr == null)
			return; // nothing to validate
		commandExpr = commandExpr.trim();
		if (commandExpr.isEmpty())
			return; // nothing to validate

		// Aggregate command accepts pipeline operations in an array
		if (cmdOp == CommandOperationType.AGGREGATE)
			commandExpr = QueryProperties.addArrayMarkers(commandExpr);

		DBObject parsedCmdObj = parseExprToDBObject(commandExpr);

		if (cmdOp == CommandOperationType.MAP_REDUCE) {
			// check that expected command keys exist
			validateMapReduceCommand(parsedCmdObj);
		} else if (cmdOp == CommandOperationType.AGGREGATE) {
			validateAggregateCommand(parsedCmdObj);
		} else if (cmdOp == CommandOperationType.RUN_DB_COMMAND) {
			validateDBCommand(parsedCmdObj);
		}
	}

	private static void validateMapReduceCommand(DBObject commandObj) throws OdaException {
		for (int i = 0; i < REQUIRED_MAPREDUCE_KEYS.length; i++) {
			String requiredKey = REQUIRED_MAPREDUCE_KEYS[i];
			if (!commandObj.containsField(requiredKey))
				throw new OdaException(Messages.bind(Messages.queryModel_missingMapReduceKey, requiredKey));
			if (commandObj.get(requiredKey) == null)
				throw new OdaException(Messages.bind(Messages.queryModel_missingMapReduceValue, requiredKey));
		}
	}

	private static void validateAggregateCommand(DBObject commandObj) throws OdaException {
		// validate a $group pipeline operation expression, if specified
		List<BasicDBObject> groupOps = findPipelineOperation(commandObj, GROUP_AGGR_KEY);
		for (BasicDBObject groupOp : groupOps) {
			if (!groupOp.containsField(DOC_ID_FIELD_NAME))
				throw new OdaException(Messages.bind(Messages.queryModel_missingGroupAggrKey,
						new Object[] { GROUP_AGGR_KEY, DOC_ID_FIELD_NAME, groupOp }));
		}

		// validate a $sort pipeline operation expression, if specified
		List<BasicDBObject> sortOps = findPipelineOperation(commandObj, SORT_AGGR_KEY);
		for (BasicDBObject sortOp : sortOps) {
			for (Object sortKeySpec : sortOp.values()) {
				if (sortKeySpec instanceof Number) {
					int sortKeyValue = ((Number) sortKeySpec).intValue();
					if (sortKeyValue == 1 || sortKeyValue == -1)
						continue; // is valid
				}
				throw new OdaException(Messages.bind(Messages.queryModel_invalidSortAggrValue, SORT_AGGR_KEY, sortOp));
			}
		}
	}

	private static List<BasicDBObject> findPipelineOperation(DBObject commandObj, String operator) throws OdaException {
		List<BasicDBObject> foundOps = new ArrayList<BasicDBObject>(2);
		if (commandObj instanceof BasicDBObject) {
			BasicDBObject foundOp = findOperation((BasicDBObject) commandObj, operator);
			if (foundOp != null)
				foundOps.add(foundOp);
		} else if (commandObj instanceof BasicDBList) {
			BasicDBList ops = (BasicDBList) commandObj;
			Iterator<Object> opsItr = ops.iterator();
			while (opsItr.hasNext()) {
				Object op = opsItr.next();
				if (!(op instanceof DBObject)) {
					if (String.valueOf(op).isEmpty())
						op = Messages.queryModel_emptyExprErrorMsg;
					throw new OdaException(Messages.bind(Messages.queryModel_invalidPipelineOp, op));
				}

				List<BasicDBObject> foundOpsInElement = findPipelineOperation((DBObject) op, operator);
				if (!foundOpsInElement.isEmpty())
					foundOps.addAll(foundOpsInElement);

				// continue to check the next op element in list
			}
		}

		return foundOps;
	}

	private static BasicDBObject findOperation(BasicDBObject opObj, String operator) {
		if (opObj == null)
			return null;
		Object op = opObj.get(operator);
		return op instanceof BasicDBObject ? (BasicDBObject) op : null;
	}

	private static void validateDBCommand(DBObject commandObj) throws OdaException {
		// check that the db command is one of the supported ones
		boolean hasSupportedCommand = false;
		for (int i = 0; i < SUPPORTED_DB_COMMANDS.length; i++) {
			String supportedCommand = SUPPORTED_DB_COMMANDS[i];
			if (commandObj.containsField(supportedCommand)
					|| commandObj.containsField(supportedCommand.toLowerCase())) {
				hasSupportedCommand = true;
				break;
			}
		}

		if (!hasSupportedCommand)
			throw new OdaException(Messages.bind(Messages.queryModel_nonSupportedDbCmd, commandObj.toString()));

		// only supports eval command w/ {nolock : true}
		if (commandObj.containsField(EVAL_KEY)) {
			boolean noLockValue = getBooleanValueOfKey(commandObj, NOLOCK_KEY, false);
			if (noLockValue != true)
				throw new OdaException(Messages.bind(Messages.queryModel_invalidDbCmdKeyValue, EVAL_KEY,
						"{" + NOLOCK_KEY + " : true}")); //$NON-NLS-1$ //$NON-NLS-2$
		}
	}

	private static boolean getBooleanValueOfKey(DBObject commandObj, String keyName, boolean defaultValue) {
		Object value = commandObj.get(keyName);
		if (value == null)
			return defaultValue;
		if (value instanceof Number)
			return ((Number) value).intValue() > 0;
		if (value instanceof Boolean)
			return ((Boolean) value).booleanValue();
		return defaultValue;
	}

}
