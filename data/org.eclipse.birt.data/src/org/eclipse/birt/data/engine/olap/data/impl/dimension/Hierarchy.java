
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.dimension;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.StopSign;
import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDatasetIterator;
import org.eclipse.birt.data.engine.olap.data.api.cube.IHierarchy;
import org.eclipse.birt.data.engine.olap.data.api.cube.ILevelDefn;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.NamingUtil;
import org.eclipse.birt.data.engine.olap.data.util.BufferedStructureArray;
import org.eclipse.birt.data.engine.olap.data.util.DiskIndex;
import org.eclipse.birt.data.engine.olap.data.util.DiskSortedStack;
import org.eclipse.birt.data.engine.olap.data.util.IDiskArray;
import org.eclipse.birt.data.engine.olap.data.util.IndexKey;

/**
 * Describes a hierarchy. A hierarchy is composed of multi-levels.
 */

public class Hierarchy implements IHierarchy {
	private IDocumentManager documentManager = null;
	private IDocumentObject documentObj = null;
	private IDocumentObject offsetDocObj = null;
	protected Level[] levels = null;
	private String name = null;
	private Map levelMap = new HashMap();
	private String dimensionName;

	public Hierarchy(IDocumentManager documentManager, String dimensionName, String hierarchyName) {
		this.documentManager = documentManager;
		this.dimensionName = dimensionName;
		this.name = hierarchyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#getLevels()
	 */
	public ILevel[] getLevels() {
		return levels;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#getName()
	 */
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#close()
	 */
	public void close() throws IOException {
		for (int i = 0; i < levels.length; i++) {
			levels[i].close();
		}
		if (documentObj != null) {
			documentObj.close();
			documentObj = null;
		}
		if (offsetDocObj != null) {
			offsetDocObj.close();
			offsetDocObj = null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.olap.data.api.IHierarchy#size()
	 */
	public int size() {
		return levels[levels.length - 1].size();
	}

	/**
	 * 
	 * @param datasetIterator
	 * @param levelDefs
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	public void createAndSaveHierarchy(IDatasetIterator datasetIterator, ILevelDefn[] levelDefs, StopSign stopSign)
			throws IOException, BirtException {
		documentObj = createHierarchyDocumentObject();
		offsetDocObj = createLevelOffsetDocumentObject();

		DiskSortedStack sortedDimensionSet = getSortedDimRows(datasetIterator, levelDefs);

		documentObj.seek(4);
		saveHierarchyMetadata(datasetIterator, levelDefs);

		int[][] keyDataType = new int[levelDefs.length][];
		int[][] attributesDataType = new int[levelDefs.length][];
		for (int i = 0; i < levelDefs.length; i++) {
			keyDataType[i] = new int[levelDefs[i].getKeyColumns().length];
			for (int j = 0; j < levelDefs[i].getKeyColumns().length; j++) {
				keyDataType[i][j] = datasetIterator.getFieldType(levelDefs[i].getKeyColumns()[j]);
			}
			if (levelDefs[i].getAttributeColumns() != null) {
				attributesDataType[i] = new int[levelDefs[i].getAttributeColumns().length];
				for (int j = 0; j < levelDefs[i].getAttributeColumns().length; j++) {
					attributesDataType[i][j] = datasetIterator.getFieldType(levelDefs[i].getAttributeColumns()[j]);
				}
			}
		}

		int size = saveHierarchyRows(levelDefs, keyDataType, attributesDataType, sortedDimensionSet, stopSign);
		// save dimension member size
		int savedPointer = (int) documentObj.getFilePointer();
		documentObj.seek(0);
		documentObj.writeInt(size);
		documentObj.seek(savedPointer);

		closeWriteDocuemntObject();

		openReadDocuemntObject();

	}

	/**
	 * 
	 * @throws IOException
	 */
	private void closeWriteDocuemntObject() throws IOException {
		if (documentObj != null) {
			documentObj.close();
			documentObj = null;
		}
		if (offsetDocObj != null) {
			offsetDocObj.close();
			offsetDocObj = null;
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void openReadDocuemntObject() throws IOException {
		if (documentObj == null)
			documentObj = documentManager.openDocumentObject(NamingUtil.getHierarchyDocName(dimensionName, name));
		if (offsetDocObj == null)
			offsetDocObj = documentManager
					.openDocumentObject(NamingUtil.getHierarchyOffsetDocName(dimensionName, name));
	}

	/**
	 * 
	 * @throws IOException
	 * @throws DataException
	 */
	public void loadFromDisk() throws IOException, DataException {
		openReadDocuemntObject();
		int size = documentObj.readInt();
		levels = new Level[documentObj.readInt()];
		for (int i = 0; i < levels.length; i++) {
			String levelName = documentObj.readString();
			String[] keyColName = new String[documentObj.readInt()];
			int[] keyDataType = new int[keyColName.length];
			for (int j = 0; j < keyColName.length; j++) {
				keyColName[j] = documentObj.readString();
				keyDataType[j] = documentObj.readInt();
			}
			int attributeNumber = documentObj.readInt();
			String[] attributeColNames = null;
			int[] attributeDataTypes = null;
			if (attributeNumber > 0) {
				attributeColNames = new String[attributeNumber];
				attributeDataTypes = new int[attributeNumber];
				for (int j = 0; j < attributeNumber; j++) {
					attributeColNames[j] = documentObj.readString();
					attributeDataTypes[j] = documentObj.readInt();
				}
			}

			levels[i] = new Level(documentManager, new LevelDefinition(levelName, keyColName, attributeColNames),
					keyDataType, attributeDataTypes, size,
					size == 0 ? null
							: DiskIndex.loadIndex(documentManager,
									NamingUtil.getLevelIndexDocName(dimensionName, levelName)));
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private IDocumentObject createHierarchyDocumentObject() throws IOException {
		return documentManager.createDocumentObject(NamingUtil.getHierarchyDocName(dimensionName, name));
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private IDocumentObject createLevelOffsetDocumentObject() throws IOException {
		return documentManager.createDocumentObject(NamingUtil.getHierarchyOffsetDocName(dimensionName, name));
	}

	/**
	 * 
	 * @param iterator
	 * @param levelDefs
	 * @throws IOException
	 * @throws BirtException
	 */
	private void saveHierarchyMetadata(IDatasetIterator iterator, ILevelDefn[] levelDefs)
			throws IOException, BirtException {
		documentObj.writeInt(levelDefs.length);
		for (int i = 0; i < levelDefs.length; i++) {
			saveLevelMetadata(iterator, levelDefs[i]);
		}
	}

	/**
	 * 
	 * @param iterator
	 * @param levelDef
	 * @throws IOException
	 * @throws BirtException
	 */
	private void saveLevelMetadata(IDatasetIterator iterator, ILevelDefn levelDef) throws IOException, BirtException {
		documentObj.writeString(levelDef.getLevelName());
		documentObj.writeInt(levelDef.getKeyColumns().length);
		for (int i = 0; i < levelDef.getKeyColumns().length; i++) {
			documentObj.writeString(levelDef.getKeyColumns()[i]);
			documentObj.writeInt(iterator.getFieldType(levelDef.getKeyColumns()[i]));
		}
		String[] attributes = levelDef.getAttributeColumns();
		if (attributes != null) {
			documentObj.writeInt(attributes.length);
			for (int j = 0; j < attributes.length; j++) {
				documentObj.writeString(attributes[j]);
				documentObj.writeInt(iterator.getFieldType(attributes[j]));
			}
		} else {
			documentObj.writeInt(0);
		}
	}

	/**
	 * 
	 * @param levelDefs
	 * @param keyDataType
	 * @param attributesDataType
	 * @param sortedDimensionSet
	 * @param stopSign
	 * @throws IOException
	 * @throws BirtException
	 */
	private int saveHierarchyRows(ILevelDefn[] levelDefs, int[][] keyDataType, int[][] attributesDataType,
			DiskSortedStack sortedDimensionSet, StopSign stopSign) throws IOException, BirtException {
		DiskSortedStack sortedDimMembers = new DiskSortedStack(
				Math.min(sortedDimensionSet.size(), Constants.MAX_DIMENSION_LENGTH), true, false, Member.getCreator());

		IDiskArray[] indexKeyLists = new IDiskArray[keyDataType.length];
		for (int i = 0; i < indexKeyLists.length; i++) {
			indexKeyLists[i] = new BufferedStructureArray(IndexKey.getCreator(),
					Math.min(sortedDimensionSet.size(), Constants.MAX_LIST_BUFFER_SIZE));
		}

		Object obj = sortedDimensionSet.pop();
		int currentIndex = 0;
		IndexKey indexKey = null;
		while (obj != null) {
			if (stopSign.isStopped())
				break;
			DimensionRow dimRows = (DimensionRow) obj;
			Member[] levelMembers = dimRows.getMembers();
			for (int i = 0; i < indexKeyLists.length; i++) {
				indexKey = new IndexKey();
				indexKey.setKey(levelMembers[i].getKeyValues());
				indexKey.setOffset(new int[] { (int) documentObj.getFilePointer() });
				indexKey.setDimensionPos(new int[] { currentIndex });
				indexKeyLists[i].add(indexKey);
			}
			// write row offset
			offsetDocObj.writeInt((int) documentObj.getFilePointer());
			// write hierarchy rows
			sortedDimMembers.push(dimRows.getMembers()[levelDefs.length - 1]);
			writeDimensionRow(dimRows, keyDataType, attributesDataType);

			obj = sortedDimensionSet.pop();
			currentIndex++;
		}
		validateDimensionMembers(sortedDimMembers);
		DiskIndex[] diskIndex = new DiskIndex[indexKeyLists.length];
		for (int i = 0; i < indexKeyLists.length; i++) {
			// create index for this level
			diskIndex[i] = DiskIndex.createIndex(documentManager,
					NamingUtil.getLevelIndexDocName(dimensionName, levelDefs[i].getLevelName()), indexKeyLists[i],
					false);
		}
		levels = new Level[levelDefs.length];
		for (int i = 0; i < levels.length; i++) {
			levels[i] = new Level(documentManager, levelDefs[i], keyDataType[i], attributesDataType[i], currentIndex,
					diskIndex[i]);
			levels[i].setLevelType(levelDefs[i].getTimeType());
		}
		for (int i = 0; i < levels.length; i++) {
			this.levelMap.put(levels[i].getName(), levels[i]);
		}
		return currentIndex;
	}

	/**
	 * 
	 * @param sortedDimMembers
	 * @throws IOException
	 * @throws DataException
	 */
	private void validateDimensionMembers(DiskSortedStack sortedDimMembers) throws IOException, DataException {
		Object obj = sortedDimMembers.pop();
		Member lastMember = null;
		while (obj != null) {
			Member currentMember = (Member) obj;
			if (lastMember != null && lastMember.equals(currentMember)) {
				throw new DataException(ResourceConstants.DETAIL_MEMBER_HAVE_MULTI_PARENT,
						lastMember.getKeyValues()[0]);
			}
			lastMember = currentMember;
			obj = sortedDimMembers.pop();
		}
	}

	/**
	 * 
	 * @param dimensionMember
	 * @param keyDataType
	 * @param attributesDataType
	 * @throws IOException
	 * @throws DataException
	 */
	private void writeDimensionRow(DimensionRow dimensionMember, int[][] keyDataType, int[][] attributesDataType)
			throws IOException, DataException {
		Member[] levelMembers = dimensionMember.getMembers();
		for (int i = 0; i < levelMembers.length; i++) {
			writeLevelMember(levelMembers[i], keyDataType[i], attributesDataType[i]);
		}
	}

	/**
	 * 
	 * @param levelMember
	 * @param keyDataType
	 * @param attributesDataType
	 * @throws IOException
	 * @throws DataException
	 */
	private void writeLevelMember(Member levelMember, int keyDataType[], int[] attributesDataType)
			throws IOException, DataException {
		for (int i = 0; i < levelMember.getKeyValues().length; i++) {
			DocumentObjectUtil.writeValue(documentObj, keyDataType[i], levelMember.getKeyValues()[i]);
		}
		if (levelMember.getAttributes() != null) {
			for (int i = 0; i < levelMember.getAttributes().length; i++) {
				DocumentObjectUtil.writeValue(documentObj, attributesDataType[i], levelMember.getAttributes()[i]);
			}
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private DimensionRow readDimensionRow() throws IOException {
		Member[] levelMembers = populateLevelMembers();
		return new DimensionRow(levelMembers);
	}

	protected Member[] populateLevelMembers() throws IOException {
		Member[] levelMembers = new Member[levels.length];
		for (int i = 0; i < levelMembers.length; i++) {
			levelMembers[i] = readLevelMember(levels[i]);
		}
		return levelMembers;
	}

	/**
	 * 
	 * @param level
	 * @return
	 * @throws IOException
	 */
	protected Member readLevelMember(Level level) throws IOException {
		Member levelMember = new Member();
		levelMember.setKeyValues(new Object[level.getKeyColNames().length]);
		for (int i = 0; i < level.getKeyColNames().length; i++) {
			levelMember.getKeyValues()[i] = DocumentObjectUtil.readValue(documentObj, level.getKeyDataType()[i]);
		}
		if (level.getAttributeDataTypes() != null && level.getAttributeDataTypes().length > 0) {
			levelMember.setAttributes(new Object[level.getAttributeDataTypes().length]);
			for (int i = 0; i < level.getAttributeDataTypes().length; i++) {
				levelMember.getAttributes()[i] = DocumentObjectUtil.readValue(documentObj,
						level.getAttributeDataTypes()[i]);
			}
		}
		return levelMember;
	}

	/**
	 * @param stopSign
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray readAllRows(StopSign stopSign) throws IOException, DataException {
		if (documentObj == null) {
			loadFromDisk();
		}
		documentObj.seek(0);
		int size = documentObj.readInt();
		BufferedStructureArray resultArray = new BufferedStructureArray(DimensionRow.getCreator(), size + 1);
		if (size == 0)
			return resultArray;
		offsetDocObj.seek(0);
		documentObj.seek(offsetDocObj.readInt());
		for (int i = 0; i < size; i++) {
			if (stopSign.isStopped())
				break;
			resultArray.add(readDimensionRow());
		}

		return resultArray;
	}

	/**
	 * 
	 * @param dimPosition
	 * @return
	 * @throws IOException
	 */
	public DimensionRow readRowByPosition(int dimPosition) throws IOException {
		offsetDocObj.seek(dimPosition * 4L);

		return readRowByOffset(offsetDocObj.readInt());
	}

	/**
	 * 
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	public DimensionRow readRowByOffset(int offset) throws IOException {
		documentObj.seek(offset);

		return readDimensionRow();
	}

	/**
	 * 
	 * @param iterator
	 * @param levelDefs
	 * @return
	 * @throws BirtException
	 * @throws IOException
	 */
	private static DiskSortedStack getSortedDimRows(IDatasetIterator iterator, ILevelDefn[] levelDefs)
			throws BirtException, IOException {
		DiskSortedStack result = new DiskSortedStack(Constants.MAX_DIMENSION_LENGTH / 2, true, true,
				DimensionRow.getCreator());

		int[][] levelKeyColumnIndex = new int[levelDefs.length][];
		int[][] levelAttributesIndex = new int[levelDefs.length][];
		for (int i = 0; i < levelDefs.length; i++) {
			levelKeyColumnIndex[i] = new int[levelDefs[i].getKeyColumns().length];
			for (int j = 0; j < levelDefs[i].getKeyColumns().length; j++) {
				levelKeyColumnIndex[i][j] = iterator.getFieldIndex(levelDefs[i].getKeyColumns()[j]);
			}
			String[] attributeColumns = levelDefs[i].getAttributeColumns();
			if (attributeColumns != null) {
				levelAttributesIndex[i] = new int[attributeColumns.length];
				for (int j = 0; j < attributeColumns.length; j++) {
					levelAttributesIndex[i][j] = iterator.getFieldIndex(attributeColumns[j]);
				}
			}
		}
		Member[] levelMembers = null;
		while (iterator.next()) {
			levelMembers = new Member[levelDefs.length];
			for (int i = 0; i < levelDefs.length; i++) {
				levelMembers[i] = getLevelMember(iterator, levelKeyColumnIndex[i], levelAttributesIndex[i],
						levelDefs[i]);
			}
			result.push(new DimensionRow(levelMembers));
		}
		return result;
	}

	/**
	 * 
	 * @param iterator
	 * @param IDColumn
	 * @param attributeCols
	 * @return
	 * @throws BirtException
	 */
	private static Member getLevelMember(IDatasetIterator iterator, int[] keyCols, int[] attributeCols,
			ILevelDefn levelDefn) throws BirtException {
		Member levelMember = new Member();
		levelMember.setKeyValues(new Object[keyCols.length]);
		for (int i = 0; i < keyCols.length; i++) {
			levelMember.getKeyValues()[i] = iterator.getValue(keyCols[i]);
			if (levelMember.getKeyValues()[i] == null) {
				throw new DataException(ResourceConstants.KEY_VALUE_CANNOT_BE_NULL, levelDefn.getKeyColumns()[i]);
			}
		}
		if (attributeCols != null) {
			levelMember.setAttributes(new Object[attributeCols.length]);
			for (int i = 0; i < attributeCols.length; i++) {
				levelMember.getAttributes()[i] = iterator.getValue(attributeCols[i]);
			}
		}
		return levelMember;
	}

}
