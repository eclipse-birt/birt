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

package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.cache.Constants;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.ISelection;
import org.eclipse.birt.data.engine.olap.data.document.DocumentObjectUtil;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentManager;
import org.eclipse.birt.data.engine.olap.data.document.IDocumentObject;
import org.eclipse.birt.data.engine.olap.data.impl.OneKeySelection;

/**
 * A disk based index. The index is n-tree which can be used to find element
 * quickly.
 */

public class DiskIndex {
	private static final int VERSION = 10000;
	private String name;
	private int degree;
	private IDocumentObject documentObject = null;
	private IDocumentObject offsetDocumentObject = null;
	private IDocumentManager documentManager = null;
	private int[] keyDataType;
	private int keyCount;
	private int rootNodeOffset;
	private int numberOfLevel;
	private int currentVersion = 1;

	/**
	 * 
	 * @param name
	 * @param sortedStack
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static DiskIndex createIndex(IDocumentManager documentManager, String name, int sonNumber,
			IDiskArray keyList, boolean isSorted) throws IOException, DataException {
		assert sonNumber <= 127;
		assert keyList.size() > 0;
		DiskIndex indexTree = new DiskIndex(documentManager, name, sonNumber, keyList, isSorted);
		return indexTree;
	}

	/**
	 * 
	 * @param name
	 * @param sortedStack
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static DiskIndex createIndex(IDocumentManager documentManager, String name, IDiskArray keyList,
			boolean isSorted) throws IOException, DataException {
		return createIndex(documentManager, name, 3, keyList, isSorted);
	}

	/**
	 * 
	 * @param name
	 * @param sortedStack
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public static DiskIndex loadIndex(IDocumentManager documentManager, String name) throws IOException, DataException {
		return new DiskIndex(documentManager, name);
	}

	/**
	 * 
	 * @param documentManager
	 * @param name
	 * @param granularity
	 * @param keyList
	 * @throws IOException
	 * @throws DataException
	 */
	DiskIndex(IDocumentManager documentManager, String name, int granularity, IDiskArray keyList, boolean isSorted)
			throws IOException, DataException {
		this.name = name;
		this.degree = granularity;
		this.documentManager = documentManager;
		produce(keyList, isSorted);
	}

	/**
	 * 
	 * @param documentManager
	 * @param name
	 * @throws IOException
	 * @throws DataException
	 */
	DiskIndex(IDocumentManager documentManager, String name) throws IOException, DataException {
		this.name = name;
		this.documentManager = documentManager;
		loadFromDisk();
	}

	/**
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws DataException
	 */
	private void loadFromDisk() throws IOException, DataException {
		openReadDocumentObject();

		keyDataType = new int[documentObject.readInt()];
		for (int i = 0; i < keyDataType.length; i++) {
			keyDataType[i] = documentObject.readInt();
		}
		keyCount = documentObject.readInt();
		degree = documentObject.readInt();
		if (degree > 10000) {
			this.currentVersion = degree / 10000;
		} else {
			this.currentVersion = 0;
		}
		rootNodeOffset = documentObject.readInt();
		numberOfLevel = documentObject.readShort();
		if (numberOfLevel < 1 || numberOfLevel > 1000 || Math.pow(degree, numberOfLevel) < keyCount) {
			throw new DataException(ResourceConstants.OLAPFILE_FORMAT_INVALID, name);
		}
	}

	private static String getOffsetDocName(String name) {
		return name + "_offset";
	}

	/**
	 * 
	 * @param keyList
	 * @throws IOException
	 * @throws DataException
	 */
	private void produce(IDiskArray keyList, boolean isSorted) throws IOException, DataException {
		createDocumentObject();
		if (keyList.size() == 0)
			return;
		IndexKey indexKey = (IndexKey) keyList.get(0);
		keyDataType = new int[indexKey.getKey().length];
		for (int i = 0; i < indexKey.getKey().length; i++) {
			keyDataType[i] = DataType.getDataType(((IndexKey) keyList.get(0)).getKey()[i].getClass());
		}

		IDiskArray sortedKeyArray = null;
		if (!isSorted)
			sortedKeyArray = sortKeys(keyList);
		else
			sortedKeyArray = keyList;
		keyCount = sortedKeyArray.size();

		int rootOffsetPos = saveIndexHeader() - 6;
		IDiskArray sonStartOffset = writeLeafNode(sortedKeyArray, degree);

		numberOfLevel = 1;
		int sonLevelTotalNumber = sortedKeyArray.size();
		int lastLevelTotalNumber = 0;

		while (sonStartOffset != null && sonStartOffset.size() > 1) {
			lastLevelTotalNumber = sonStartOffset.size();
			sonStartOffset = writeNonLeafNode(sortedKeyArray, sonStartOffset, numberOfLevel, sonLevelTotalNumber);
			numberOfLevel++;
			sonLevelTotalNumber = lastLevelTotalNumber;
		}
		assert sonStartOffset.size() == 1;
		// write root node
		sonStartOffset = writeNonLeafNode(sortedKeyArray, sonStartOffset, numberOfLevel, sonLevelTotalNumber);
		rootNodeOffset = ((Integer) sonStartOffset.get(0)).intValue();
		documentObject.seek(rootOffsetPos);
		documentObject.writeInt(rootNodeOffset);
		documentObject.writeShort(numberOfLevel);
		documentObject.flush();
		offsetDocumentObject.flush();
		closeWriteDocumentObject();
		openReadDocumentObject();
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void closeWriteDocumentObject() throws IOException {
		if (documentObject != null) {
			documentObject.close();
			documentObject = null;
		}
		if (offsetDocumentObject != null) {
			offsetDocumentObject.close();
			offsetDocumentObject = null;
		}
	}

	/**
	 * 
	 * @throws IOException
	 */
	private void openReadDocumentObject() throws IOException {
		if (documentObject == null) {
			documentObject = documentManager.openDocumentObject(name);
			documentObject.seek(0);

		}
		if (offsetDocumentObject == null) {
			offsetDocumentObject = documentManager.openDocumentObject(getOffsetDocName(name));
			documentObject.seek(0);
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private int saveIndexHeader() throws IOException {
		documentObject.writeInt(keyDataType.length);
		for (int i = 0; i < keyDataType.length; i++) {
			documentObject.writeInt(keyDataType[i]);
		}
		documentObject.writeInt(keyCount);
		documentObject.writeInt(degree + VERSION);
//		documentObject.skipBytes( 6 );
		byte[] b = new byte[6];
		documentObject.write(b, 0, 6);
		return (int) (documentObject.getFilePointer());
	}

	/**
	 * 
	 * @param sortedKeyArray
	 * @param interval
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IDiskArray writeLeafNode(IDiskArray sortedKeyArray, int interval) throws IOException, DataException {
		BufferedPrimitiveDiskArray offset = new BufferedPrimitiveDiskArray(
				Math.min(sortedKeyArray.size(), Constants.MAX_LIST_BUFFER_SIZE));
		for (int i = 0; i < sortedKeyArray.size(); i++) {
			if (i % interval == 0) {
				offset.add(Integer.valueOf((int) documentObject.getFilePointer()));
			}
			offsetDocumentObject.writeInt((int) documentObject.getFilePointer());
			writeKeyObject((IndexKey) sortedKeyArray.get(i));

		}
		return offset;
	}

	/**
	 * 
	 * @param sortedKeyArray
	 * @param startOffset
	 * @param level
	 * @param sonLevelTotalNumber
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	private IDiskArray writeNonLeafNode(IDiskArray sortedKeyArray, IDiskArray startOffset, int level,
			int sonLevelTotalNumber) throws IOException, DataException {
		int interval = pow(degree, level);
		BufferedPrimitiveDiskArray sonStartOffset = new BufferedPrimitiveDiskArray(
				Math.min(Constants.MAX_LIST_BUFFER_SIZE, startOffset.size() / degree + 1));

		for (int i = 0; i < startOffset.size(); i++) {
			if (i % degree == 0) {
				sonStartOffset.add(Integer.valueOf((int) documentObject.getFilePointer()));
			}
			if (i != startOffset.size() - 1) {
				documentObject.writeByte(degree);
				// min child key object of the node
				DocumentObjectUtil.writeValue(documentObject, keyDataType,
						((IndexKey) sortedKeyArray.get(i * interval)).getKey());
				// max child key object of the node
				DocumentObjectUtil.writeValue(documentObject, keyDataType,
						((IndexKey) sortedKeyArray.get((i + 1) * interval - 1)).getKey());
			} else {
				documentObject.writeByte(sonLevelTotalNumber - (startOffset.size() - 1) * degree);
				// min son key object of the node
				DocumentObjectUtil.writeValue(documentObject, keyDataType,
						((IndexKey) sortedKeyArray.get(i * interval)).getKey());
				// max son key object of the node
				DocumentObjectUtil.writeValue(documentObject, keyDataType,
						((IndexKey) sortedKeyArray.get(sortedKeyArray.size() - 1)).getKey());
			}
			documentObject.writeInt(((Integer) startOffset.get(i)).intValue());
		}

		return sonStartOffset;
	}

	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	private int pow(int a, int b) {
		int reValue = a;
		for (int i = 0; i < b - 1; i++) {
			reValue *= a;
		}
		return reValue;
	}

	/**
	 * 
	 * @param keyObject
	 * @throws IOException
	 * @throws DataException
	 */
	private void writeKeyObject(IndexKey keyObject) throws IOException, DataException {
		documentObject.writeInt(keyObject.getDimensionPos().length);
		for (int i = 0; i < keyObject.getDimensionPos().length; i++) {
			documentObject.writeInt(keyObject.getDimensionPos()[i]);
		}
		for (int i = 0; i < keyDataType.length; i++) {
			DocumentObjectUtil.writeValue(documentObject, keyDataType[i], keyObject.getKey()[i]);
		}
		documentObject.writeInt(keyObject.getOffset().length);
		for (int i = 0; i < keyObject.getOffset().length; i++) {
			documentObject.writeInt(keyObject.getOffset()[i]);
		}
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private NonLeafNode readNonLeafNode() throws IOException {
		NonLeafNode node = new NonLeafNode();

		node.numberOfSon = documentObject.readByte();
		node.minKeyValue = DocumentObjectUtil.readValue(documentObject, keyDataType);
		node.maxKeyValue = DocumentObjectUtil.readValue(documentObject, keyDataType);
		node.offset = documentObject.readInt();

		return node;
	}

	/**
	 * 
	 * @return
	 * @throws IOException
	 */
	private IndexKey readKeyObject() throws IOException {
		IndexKey keyObject = new IndexKey();
		int[] dimensionPos = null;
		if (currentVersion > 0) {
			dimensionPos = new int[documentObject.readInt()];
			for (int i = 0; i < dimensionPos.length; i++) {
				dimensionPos[i] = documentObject.readInt();
			}
			keyObject.setDimensionPos(dimensionPos);
		} else {
			dimensionPos = new int[1];
			dimensionPos[0] = documentObject.readInt();
			keyObject.setDimensionPos(dimensionPos);
		}

		keyObject.setKey(DocumentObjectUtil.readValue(documentObject, keyDataType));

		int[] offset = null;

		if (currentVersion > 0) {
			offset = new int[documentObject.readInt()];
			for (int i = 0; i < offset.length; i++) {
				offset[i] = documentObject.readInt();
			}
			keyObject.setOffset(offset);
		} else {
			offset = new int[1];
			offset[0] = documentObject.readInt();
			keyObject.setOffset(offset);
		}
		return keyObject;
	}

	/**
	 * 
	 * @throws IOException
	 * @throws DataException
	 */
	private void createDocumentObject() throws IOException, DataException {
		documentObject = documentManager.createDocumentObject(name);
		if (documentObject == null) {
			// throw new OlapException(
			// ResourceConstants.DOCUMENTOJBECT_ALWAYS_EXIST,
			// name );
		}

		offsetDocumentObject = documentManager.createDocumentObject(getOffsetDocName(name));
		if (offsetDocumentObject == null) {
			// throw new OlapException(
			// ResourceConstants.DOCUMENTOJBECT_ALWAYS_EXIST,
			// name );
		}
	}

	/**
	 * 
	 * @param keyList
	 * @return
	 * @throws IOException
	 */
	private IDiskArray sortKeys(IDiskArray keyList) throws IOException {
		DiskSortedStack sortStack = new DiskSortedStack(Math.min(keyList.size(), Constants.MAX_LIST_BUFFER_SIZE), false,
				IndexKey.getKeyComparator(), IndexKey.getCreator());
		for (int i = 0; i < keyList.size(); i++) {
			sortStack.push((IComparableStructure) (keyList.get(i)));
		}
		BufferedStructureArray reList = new BufferedStructureArray(IndexKey.getCreator(),
				Math.min(keyList.size(), Constants.MAX_LIST_BUFFER_SIZE));
		IndexKey curIndexKey = null;
		List<Integer> dimPos = new ArrayList<Integer>();
		List<Integer> dimOffset = new ArrayList<Integer>();
		for (int i = 0; i < keyList.size(); i++) {
			IndexKey indexKey = (IndexKey) sortStack.pop();
			if (curIndexKey == null) {
				curIndexKey = indexKey;
				dimPos.add(new Integer(curIndexKey.getDimensionPos()[0]));
				dimOffset.add(new Integer(curIndexKey.getOffset()[0]));
			} else if (indexKey.compareTo(curIndexKey) == 0) {
				dimPos.add(new Integer(indexKey.getDimensionPos()[0]));
				dimOffset.add(Integer.valueOf(indexKey.getOffset()[0]));
			} else {
				addIndex(reList, curIndexKey, dimPos, dimOffset);
				curIndexKey = indexKey;
				dimPos.clear();
				dimPos.add(Integer.valueOf(curIndexKey.getDimensionPos()[0]));
				dimOffset.clear();
				dimOffset.add(Integer.valueOf(curIndexKey.getOffset()[0]));
			}
		}
		addIndex(reList, curIndexKey, dimPos, dimOffset);
		return reList;
	}

	private void addIndex(BufferedStructureArray reList, IndexKey curIndexKey, List<Integer> dimPos,
			List<Integer> dimOffset) throws IOException {
		int[] iDimPos = new int[dimPos.size()];
		for (int j = 0; j < iDimPos.length; j++) {
			iDimPos[j] = dimPos.get(j);
		}
		int[] iDimOffset = new int[dimOffset.size()];
		for (int j = 0; j < iDimOffset.length; j++) {
			iDimOffset[j] = dimOffset.get(j);
		}
		curIndexKey.setDimensionPos(iDimPos);
		curIndexKey.setOffset(iDimOffset);
		reList.add(curIndexKey);
	}

	/**
	 * 
	 * @param value
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray find(Object[] value) throws IOException, DataException {
		ISelection[] selections = new ISelection[1];
		selections[0] = new OneKeySelection(value);
		return find(selections);
	}

	/**
	 * 
	 * @param selectionMark
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IndexKey findFirst(Object[] key) throws IOException, DataException {
		documentObject.seek(rootNodeOffset);
		NonLeafNode currentNode = readNonLeafNode();
		NonLeafNode tempNode = null;

		if (!checkValid(currentNode)) {
			throw new DataException(ResourceConstants.OLAPFILE_DATA_ERROR, name);
		}
		if (!isBetween(currentNode, key)) {
			return null;
		}

		boolean find;

		for (int i = 0; i < numberOfLevel - 1; i++) {
			find = false;
			documentObject.seek(currentNode.offset);
			for (int j = 0; j < currentNode.numberOfSon; j++) {

				tempNode = readNonLeafNode();
				if (isBetween(tempNode, key)) {
					find = true;
					break;
				}
			}
			if (!find) {
				return null;
			}
			currentNode = tempNode;
		}

		// check leaf nodes
		documentObject.seek(currentNode.offset);
		for (int i = 0; i < currentNode.numberOfSon; i++) {
			IndexKey indexKey = readKeyObject();
			if (CompareUtil.compare(indexKey.getKey(), key) == 0) {
				return indexKey;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param node
	 * @param key
	 * @return
	 */
	private boolean isBetween(NonLeafNode node, Object[] key) {
		if (CompareUtil.compare(node.minKeyValue, key) > 0 || CompareUtil.compare(node.maxKeyValue, key) < 0) {
			return false;
		} else {
			return true;
		}
	}

	public IDiskArray findAll() throws IOException, DataException {
		return topN(this.keyCount);
	}

	/**
	 * 
	 * @param selections
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public IDiskArray find(ISelection[] selections) throws IOException, DataException {
		documentObject.seek(rootNodeOffset);
		NonLeafNode currentNode = readNonLeafNode();

		if (!checkValid(currentNode)) {
			throw new DataException(ResourceConstants.OLAPFILE_DATA_ERROR, name);
		}

		boolean find = false;
		NodeSelection nodeSelection = new NodeSelection(currentNode, selections.length);

		for (int i = 0; i < selections.length; i++) {
			if (match(currentNode, selections[i])) {
				nodeSelection.addSelection(i);
				find = true;
			}
		}
		if (!find) {
			return null;
		}
		BufferedStructureArray validNodeList = new BufferedStructureArray(NodeSelection.getCreator(),
				Math.min(keyCount, Constants.MAX_LIST_BUFFER_SIZE));
		validNodeList.add(nodeSelection);
		BufferedStructureArray validSonNode = new BufferedStructureArray(NodeSelection.getCreator(),
				Math.min(keyCount, Constants.MAX_LIST_BUFFER_SIZE));
		for (int i = 0; i < numberOfLevel - 1; i++) {
			for (int j = 0; j < validNodeList.size(); j++) {
				processNonLeafNode((NodeSelection) validNodeList.get(j), selections, validSonNode);
			}
			if (validSonNode.size() <= 0) {
				return null;
			}
			BufferedStructureArray tempList = validNodeList;
			validNodeList = validSonNode;
			validSonNode = tempList;
			validSonNode.clear();
		}
		// check leaf nodes
		BufferedStructureArray resultList = new BufferedStructureArray(IndexKey.getCreator(),
				Math.min(keyCount, Constants.MAX_LIST_BUFFER_SIZE));
		for (int i = 0; i < validNodeList.size(); i++) {
			currentNode = ((NodeSelection) validNodeList.get(i)).node;
			boolean[] selectionMark = ((NodeSelection) validNodeList.get(i)).selectionMark;
			documentObject.seek(currentNode.offset);
			for (int j = 0; j < currentNode.numberOfSon; j++) {
				IndexKey indexKey = readKeyObject();
				for (int k = 0; k < selectionMark.length; k++) {
					if (selectionMark[k]) {
						if (selections[k].isSelected(indexKey.getKey())) {
							resultList.add(indexKey);
							break;
						}
					}
				}
			}
		}
		return resultList;
	}

	/**
	 * 
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public IDiskArray topN(int n) throws IOException {
		BufferedStructureArray resultList = new BufferedStructureArray(IndexKey.getCreator(),
				Math.min(n, Constants.MAX_LIST_BUFFER_SIZE));
		offsetDocumentObject.seek((keyCount - n) * 4);
		documentObject.seek(offsetDocumentObject.readInt());
		for (int i = 0; i < n; i++) {
			resultList.add(readKeyObject());
		}
		return resultList;
	}

	/**
	 * 
	 * @param percent
	 * @return
	 * @throws IOException
	 */
	public IDiskArray topPercent(double percent) throws IOException {
		return topN((int) (keyCount * percent));
	}

	/**
	 * 
	 * @param n
	 * @return
	 * @throws IOException
	 */
	public IDiskArray bottomN(int n) throws IOException {
		BufferedStructureArray resultList = new BufferedStructureArray(IndexKey.getCreator(),
				Math.min(n, Constants.MAX_LIST_BUFFER_SIZE));
		offsetDocumentObject.seek(0);
		documentObject.seek(offsetDocumentObject.readInt());
		for (int i = 0; i < n; i++) {
			resultList.add(readKeyObject());
		}
		return resultList;
	}

	/**
	 * 
	 * @param percent
	 * @return
	 * @throws IOException
	 */
	public IDiskArray bottomPercent(double percent) throws IOException {
		return bottomN((int) (keyCount * percent));
	}

	/**
	 * Check
	 * 
	 * @param node
	 * @return
	 */
	private boolean checkValid(NonLeafNode node) {
		if (node.maxKeyValue == null || node.minKeyValue == null
				|| CompareUtil.compare(node.maxKeyValue, node.minKeyValue) < 0) {
			return false;
		}
		if (node.numberOfSon > degree || node.numberOfSon <= 0) {
			return false;
		}
		if (node.offset < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param nodeSelection
	 * @param selections
	 * @param resultList
	 * @throws IOException
	 */
	private void processNonLeafNode(NodeSelection nodeSelection, ISelection[] selections, IDiskArray resultList)
			throws IOException {
		NonLeafNode tempNode = null;
		NodeSelection tempNodeSelection = null;
		boolean find = false;
		documentObject.seek(nodeSelection.node.offset);
		for (int i = 0; i < nodeSelection.node.numberOfSon; i++) {
			tempNode = readNonLeafNode();
			tempNodeSelection = new NodeSelection(tempNode, selections.length);
			find = false;
			for (int j = 0; j < nodeSelection.selectionMark.length; j++) {
				if (nodeSelection.selectionMark[j])
					if (match(tempNode, selections[j])) {
						tempNodeSelection.addSelection(j);
						find = true;
					}
			}
			if (find) {
				resultList.add(tempNodeSelection);
			}
		}
	}

	/**
	 * 
	 * @param node
	 * @param selection
	 * @return
	 */
	private boolean match(NonLeafNode node, ISelection selection) {
		if (selection.getMin() != null && CompareUtil.compare(node.maxKeyValue, selection.getMin()) < 0) {
			return false;
		}
		if (selection.getMax() != null && CompareUtil.compare(node.minKeyValue, selection.getMax()) > 0) {
			return false;
		}
		return true;
	}

	public void close() throws IOException {
		if (documentObject != null) {
			documentObject.close();
			documentObject = null;
		}
		if (offsetDocumentObject != null) {
			offsetDocumentObject.close();
			offsetDocumentObject = null;
		}
	}
}

class NonLeafNode {
	int numberOfSon;
	Object[] maxKeyValue;
	Object[] minKeyValue;
	int offset;
}

class NodeSelection implements IStructure {

	NonLeafNode node;
	boolean[] selectionMark;

	/**
	 * 
	 * @param node
	 * @param selectionNumber
	 */
	NodeSelection(NonLeafNode node, int selectionNumber) {
		this.node = node;
		selectionMark = new boolean[selectionNumber];
	}

	/**
	 * 
	 * @param selectionIndex
	 */
	void addSelection(int selectionIndex) {
		selectionMark[selectionIndex] = true;
		;
	}

	/**
	 * 
	 */
	public Object[] getFieldValues() {
		Object[][] objects = new Object[3][];
		objects[0] = new Object[3];
		objects[0][0] = Integer.valueOf(node.numberOfSon);
		objects[0][1] = Integer.valueOf(node.offset);
		objects[0][2] = toString(selectionMark);
		;
		objects[1] = node.maxKeyValue;
		objects[2] = node.minKeyValue;
		return ObjectArrayUtil.convert(objects);
	}

	/**
	 * 
	 * @param b
	 * @return
	 */
	private static String toString(boolean[] b) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			if (b[i]) {
				buffer.append('1');
			} else {
				buffer.append('0');
			}
		}
		return buffer.toString();
	}

	/**
	 * 
	 * @return
	 */
	public static IStructureCreator getCreator() {
		return new NodeSelectionCreator();
	}
}

class NodeSelectionCreator implements IStructureCreator {

	public IStructure createInstance(Object[] fields) {
		NonLeafNode node = new NonLeafNode();
		Object[][] objects = ObjectArrayUtil.convert(fields);
		node.numberOfSon = ((Integer) objects[0][0]).intValue();
		node.offset = ((Integer) objects[0][1]).intValue();

		node.maxKeyValue = objects[1];
		node.minKeyValue = objects[2];

		String selections = (String) objects[0][2];
		NodeSelection nodeSelection = new NodeSelection(node, selections.length());
		for (int i = 0; i < selections.length(); i++) {
			if (selections.charAt(i) == '1') {
				nodeSelection.addSelection(i);
			}
		}
		return nodeSelection;
	}
}
