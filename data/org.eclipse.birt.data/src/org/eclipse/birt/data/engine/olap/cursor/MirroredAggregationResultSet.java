/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.olap.OLAPException;

import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.AggregationResultRow;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.AggrSortDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;
import org.eclipse.birt.data.engine.olap.util.sort.DimensionSortEvalHelper;

/**
 * This class is a wrapper class of AggregationResultSet in the case of using
 * mirror feature. The mirror is divided into 2 types: one is breakHierarchy,
 * another is no breakHierarchy. For the first one, we just use MemberTreeNode
 * for those axis who is not mirrored. For the second case, we just map all
 * members into one search tree
 *
 */
public class MirroredAggregationResultSet implements IAggregationResultSet {

	private IAggregationResultSet rs = null;
	private int mirrorLevel;
	private int length = 0, position = -1, nodeLength, valueMapLength;
	private long predictLength = 0;
	private boolean breakHierarchy = false;
	private Object[] resultObject;
	private MemberTreeNode rootNode;
	private List sortList;

	private List[] breakHierarchyList;
	private Map valueMap;
	private boolean isTimeMirror = false;
	private MirrorMetaInfo service;

	public MirroredAggregationResultSet(IAggregationResultSet rs, MirrorMetaInfo service, List sortList)
			throws IOException, OLAPException {
		this.mirrorLevel = service.getMirrorStartPosition();
		this.breakHierarchy = service.isBreakHierarchy();
		this.service = service;
		Member member = new Member();
		member.setKeyValues(new Object[] { "#ROOT#" });
		this.rootNode = new MemberTreeNode(member);
		this.resultObject = new Object[rs.getLevelCount()];
		this.rs = rs;
		this.sortList = sortList;
		this.isTimeMirror = TimeMemberUtil.containsTimeMirror(rs, service);

		if (!isTimeMirror && breakHierarchy) {
			this.breakHierarchyList = new ArrayList[rs.getLevelCount() - mirrorLevel];

			for (int j = 0; j < breakHierarchyList.length; j++) {
				breakHierarchyList[j] = new ArrayList();
			}
			populateMirror();
		} else {
			if (!isTimeMirror && !breakHierarchy) {
				this.mirrorLevel = this.mirrorLevel - 1;
			}
			populateTimeMirror();
			nodeLength = getLength(this.rootNode);
			valueMapLength = 0;
			Iterator iter = this.valueMap.entrySet().iterator();
			while (iter.hasNext()) {
				valueMapLength += getLength((MemberTreeNode) ((Entry) iter.next()).getValue());
			}
			this.length = nodeLength * valueMapLength;
		}
	}

	/**
	 *
	 * @param levelIndex
	 * @return
	 */
	private int findAggregationSort(int levelIndex) {
		int sortType = IDimensionSortDefn.SORT_UNDEFINED;

		if (this.sortList != null) {
			DimLevel level = this.rs.getLevel(levelIndex);
			for (int i = 0; i < this.sortList.size(); i++) {
				if (sortList.get(i) instanceof AggrSortDefinition) {
					AggrSortDefinition defn = (AggrSortDefinition) sortList.get(i);
					if (level.equals(defn.getTargetLevel())) {
						if (defn.getAxisQualifierLevel().length == 0) {
							sortType = defn.getSortDirection();
						} else {
							sortType = IDimensionSortDefn.SORT_UNDEFINED;
						}
						return sortType;
					}
				} else if (sortList.get(i) instanceof DimensionSortEvalHelper) {
					DimensionSortEvalHelper dimSort = (DimensionSortEvalHelper) sortList.get(i);
					if (dimSort.getTargetLevel().equals(level)) {
						return IDimensionSortDefn.SORT_UNDEFINED;
					}
				}
			}
		}
		sortType = this.rs.getSortType(levelIndex);
		if (sortType == IDimensionSortDefn.SORT_UNDEFINED) {
			sortType = IDimensionSortDefn.SORT_ASC;
		}
		return sortType;
	}

	private void populateTimeMirror() throws IOException {
		MemberTreeNode parent;
		MemberTreeNode child;
		Object[] preValue = new Object[rs.getLevelCount()];
		Object[] currValue = new Object[rs.getLevelCount()];

		final int sortType = this.getSortTypeOnMirroredLevel(this.mirrorLevel);
		if (sortType != IDimensionSortDefn.SORT_UNDEFINED) {
			valueMap = new TreeMap(new Comparator() {

				@Override
				public int compare(final Object arg0, final Object arg1) {
					if (sortType == IDimensionSortDefn.SORT_ASC) {
						return ((Comparable) arg0).compareTo(arg1);
					} else {
						return ((Comparable) arg0).compareTo(arg1) * -1;
					}
				}
			});
		} else {
			valueMap = new HashMap();
		}

		for (int i = 0; i < rs.length(); i++) {
			rs.seek(i);
			parent = this.rootNode;

			for (int j = 0; j < rs.getLevelCount(); j++) {
				if (rs.getLevelKeyValue(j) != null) {
					currValue[j] = rs.getLevelKeyValue(j)[0];
				} else {
					currValue[j] = null;
				}
			}

			for (int j = 0; j < this.mirrorLevel; j++) {
				if (!isEqualObject(preValue[j], currValue[j])) {
					Member member = new Member();
					member.setKeyValues(new Object[] { currValue[j] });
					member.setAttributes(rs.getLevelAttributesValue(j));
					child = new MemberTreeNode(member);
					parent.insertNode(child);
					child.parentNode = parent;
					parent = child;
				} else if (parent.childNodesList.size() > 0) {
					parent = (MemberTreeNode) parent.childNodesList.get(parent.childNodesList.size() - 1);
				} else {
					Member member = new Member();
					member.setKeyValues(new Object[] { currValue[j] });
					member.setAttributes(rs.getLevelAttributesValue(j));
					child = new MemberTreeNode(member);
					parent.insertNode(child);
					child.parentNode = parent;
					parent = child;
				}
			}

			Member mirrorMember = new Member();
			mirrorMember.setKeyValues(new Object[] { currValue[this.mirrorLevel] });
			mirrorMember.setAttributes(rs.getLevelAttributesValue(this.mirrorLevel));
			if (valueMap.containsKey(mirrorMember)) {
				MemberTreeNode node = (MemberTreeNode) valueMap.get(mirrorMember);
				for (int j = this.mirrorLevel + 1; j < this.rs.getLevelCount(); j++) {
					Member member = new Member();
					member.setKeyValues(new Object[] { currValue[j] });
					member.setAttributes(rs.getLevelAttributesValue(j));

					if (!node.containsChild(member)) {
						if (TimeMemberUtil.isTimeMirror(rs, j, service)) {
							MemberTreeNode[] nodes = TimeMemberUtil.getDateTimeNodes(rs.getAllLevels(),
									rs.getLevelAttribute(j, 0), j, service);
							for (int k = 0; k < nodes.length; k++) {
								node.insertNode(nodes[k]);
								nodes[k].parentNode = node;
							}
							break;
						} else {
							MemberTreeNode childNode = new MemberTreeNode(member);
							node.insertNode(childNode);
							childNode.parentNode = node;
							node = childNode;
						}
					} else {
						node = node.getChild(member);
					}
				}
			} else {
				MemberTreeNode parentNode = null;
				for (int j = this.mirrorLevel; j < this.rs.getLevelCount(); j++) {
					if (TimeMemberUtil.isTimeMirror(rs, j, service)) {
						MemberTreeNode[] nodes = TimeMemberUtil.getDateTimeNodes(rs.getAllLevels(),
								rs.getLevelAttribute(j, 0), j, service);
						for (int k = 0; k < nodes.length; k++) {
							if (parentNode == null) {
								valueMap.put(nodes[k].key, nodes[k]);
							} else {
								parentNode.insertNode(nodes[k]);
							}
							nodes[k].parentNode = parentNode;
						}
						break;
					} else if (parentNode == null) {
						Member member = new Member();
						member.setKeyValues(new Object[] { currValue[j] });
						member.setAttributes(rs.getLevelAttributesValue(j));
						parentNode = new MemberTreeNode(member);
						valueMap.put(mirrorMember, parentNode);
					} else {
						Member member = new Member();
						member.setKeyValues(new Object[] { currValue[j] });
						member.setAttributes(rs.getLevelAttributesValue(j));
						MemberTreeNode childNode = new MemberTreeNode(member);
						parentNode.insertNode(childNode);
						childNode.parentNode = parentNode;
						parentNode = childNode;
					}
				}

			}

			for (int k = 0; k < rs.getLevelCount(); k++) {
				preValue[k] = currValue[k];
			}
		}

		int level = mirrorLevel + 1;
		List nodeList1 = new ArrayList(valueMap.values());
		for (int k = mirrorLevel + 1; k < rs.getLevelCount(); k++) {
			final int childSortType = this.getSortTypeOnMirroredLevel(k);
			List nodeList2 = new ArrayList();

			if (sortType != IDimensionSortDefn.SORT_UNDEFINED) {
				while (k > level) {
					for (int j = 0; j < nodeList1.size(); j++) {
						nodeList2.addAll(((MemberTreeNode) nodeList1.get(j)).childNodesList);

					}
					nodeList1.clear();
					nodeList1.addAll(nodeList2);
					nodeList2.clear();
					level++;
				}

				for (int j = 0; j < nodeList1.size(); j++) {
					MemberTreeNode node = (MemberTreeNode) nodeList1.get(j);

					Collections.sort(node.childNodesList, new Comparator() {

						@Override
						public int compare(final Object arg0, final Object arg1) {
							if (childSortType == IDimensionSortDefn.SORT_ASC) {
								return ((Comparable) ((MemberTreeNode) arg0).key)
										.compareTo(((MemberTreeNode) arg1).key);
							} else {
								return ((Comparable) ((MemberTreeNode) arg0).key).compareTo(((MemberTreeNode) arg1).key)
										* -1;
							}
						}
					});
				}
			}
		}
	}

	private void populateMirror() throws IOException, OLAPException {
		MemberTreeNode parent;
		MemberTreeNode child;
		Object[] preValue = new Object[mirrorLevel];
		Object[] currValue = new Object[mirrorLevel];

		for (int i = 0; i < rs.length(); i++) {
			rs.seek(i);
			parent = this.rootNode;

			for (int j = 0; j < mirrorLevel; j++) {
				currValue[j] = rs.getLevelKeyValue(j)[0];
			}
			for (int j = 0; j < mirrorLevel; j++) {
				if (!isEqualObject(preValue[j], currValue[j])) {
					Member member = new Member();
					member.setKeyValues(new Object[] { currValue[j] });
					member.setAttributes(rs.getLevelAttributesValue(j));
					child = new MemberTreeNode(member);
					parent.insertNode(child);
					child.parentNode = parent;
					parent = child;
				} else if (parent.childNodesList.size() > 0) {
					parent = (MemberTreeNode) parent.childNodesList.get(parent.childNodesList.size() - 1);
				} else {
					Member member = new Member();
					member.setKeyValues(new Object[] { currValue[j] });
					member.setAttributes(rs.getLevelAttributesValue(j));
					child = new MemberTreeNode(member);
					parent.insertNode(child);
					child.parentNode = parent;
					parent = child;
				}
			}

			for (int j = 0; j < breakHierarchyList.length; j++) {
				Member temp = new Member();
				temp.setKeyValues(rs.getLevelKeyValue(j + mirrorLevel));
				if (!breakHierarchyList[j].contains(temp)) {
					Member member = new Member();
					member.setKeyValues(rs.getLevelKeyValue(j + mirrorLevel));
					member.setAttributes(rs.getLevelAttributesValue(j + mirrorLevel));
					breakHierarchyList[j].add(member);
				}
			}

			for (int k = 0; k < mirrorLevel; k++) {
				preValue[k] = currValue[k];
			}
		}
		this.length = getLength(this.rootNode);
		this.predictLength = this.length;
		for (int k = 0; k < breakHierarchyList.length; k++) {
			final int sortType = getSortTypeOnMirroredLevel(k + mirrorLevel);
			if (sortType != IDimensionSortDefn.SORT_UNDEFINED) {
				Collections.sort(breakHierarchyList[k], new Comparator() {

					@Override
					public int compare(final Object arg0, final Object arg1) {
						if (sortType == IDimensionSortDefn.SORT_ASC) {
							return ((Comparable) arg0).compareTo(arg1);
						} else {
							return ((Comparable) arg0).compareTo(arg1) * -1;
						}
					}
				});
			}
			this.predictLength *= breakHierarchyList[k].size();
			if (this.predictLength > Integer.MAX_VALUE) {
				throw new OLAPException(ResourceConstants.RESULT_LENGTH_EXCEED_LIMIT);
			}
			this.length = (int) predictLength;
		}
	}

	private int getSortTypeOnMirroredLevel(int level) {
		return this.findAggregationSort(level);
	}

	private int getLength(MemberTreeNode node) {
		int length = 0;
		if (node.childNodesList.size() == 0) {
			length++;
			return length;
		}

		for (int i = 0; i < node.childNodesList.size(); i++) {
			MemberTreeNode child = (MemberTreeNode) node.childNodesList.get(i);
			length += getLength(child);
		}
		return length;
	}

	private boolean isEqualObject(Object preValue, Object currentValue) {
		if (preValue == currentValue) {
			return true;
		}
		if (preValue == null || currentValue == null) {
			return false;
		}
		return preValue.equals(currentValue);
	}

	@Override
	public void clear() throws IOException {
		rs.clear();
	}

	@Override
	public void close() throws IOException {
		this.rs.close();
	}

	@Override
	public int getAggregationCount() {
		return this.rs.getAggregationCount();
	}

	@Override
	public int getAggregationDataType(int aggregationIndex) throws IOException {
		return this.rs.getAggregationDataType(aggregationIndex);
	}

	@Override
	public AggregationDefinition getAggregationDefinition() {
		return this.rs.getAggregationDefinition();
	}

	@Override
	public int getAggregationIndex(String name) throws IOException {
		return this.rs.getAggregationIndex(name);
	}

	@Override
	public String getAggregationName(int index) {
		return this.rs.getAggregationName(index);
	}

	@Override
	public Object getAggregationValue(int aggregationIndex) throws IOException {
		return this.rs.getAggregationValue(aggregationIndex);
	}

	@Override
	public String[][] getAttributeNames() {
		return this.rs.getAttributeNames();
	}

	@Override
	public DimLevel[] getAllLevels() {
		return this.rs.getAllLevels();
	}

	@Override
	public IAggregationResultRow getCurrentRow() throws IOException {
		Member[] member = new Member[resultObject.length];
		for (int i = 0; i < resultObject.length; i++) {
			member[i] = (Member) resultObject[i];
		}
		IAggregationResultRow row = new AggregationResultRow(member, null);
		return row;
	}

	@Override
	public String[][] getKeyNames() {
		return this.rs.getKeyNames();
	}

	@Override
	public DimLevel getLevel(int levelIndex) {
		return this.rs.getLevel(levelIndex);
	}

	@Override
	public Object getLevelAttribute(int levelIndex, int attributeIndex) {
		if (levelIndex < 0 || resultObject.length < levelIndex) {
			return null;
		}
		return ((Member) this.resultObject[levelIndex]).getAttributes()[attributeIndex];
	}

	@Override
	public int getLevelAttributeColCount(int levelIndex) {
		return this.rs.getLevelAttributeColCount(levelIndex);
	}

	@Override
	public int getLevelAttributeDataType(DimLevel level, String attributeName) {
		return this.rs.getLevelAttributeDataType(level, attributeName);
	}

	@Override
	public int getLevelAttributeDataType(int levelIndex, String attributeName) {
		return this.rs.getLevelAttributeDataType(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(int levelIndex, String attributeName) {
		return this.rs.getLevelAttributeIndex(levelIndex, attributeName);
	}

	@Override
	public int getLevelAttributeIndex(DimLevel level, String attributeName) {
		return this.rs.getLevelAttributeIndex(level, attributeName);
	}

	@Override
	public String[] getLevelAttributes(int levelIndex) {
		return this.rs.getLevelAttributes(levelIndex);
	}

	@Override
	public int getLevelCount() {
		return this.rs.getLevelCount();
	}

	@Override
	public int getLevelIndex(DimLevel level) {
		return this.rs.getLevelIndex(level);
	}

	@Override
	public int getLevelKeyColCount(int levelIndex) {
		return this.rs.getLevelKeyColCount(levelIndex);
	}

	@Override
	public int getLevelKeyDataType(DimLevel level, String keyName) {
		return this.rs.getLevelKeyDataType(level, keyName);
	}

	@Override
	public int getLevelKeyDataType(int levelIndex, String keyName) {
		return this.rs.getLevelKeyDataType(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(int levelIndex, String keyName) {
		return this.rs.getLevelKeyIndex(levelIndex, keyName);
	}

	@Override
	public int getLevelKeyIndex(DimLevel level, String keyName) {
		return this.rs.getLevelKeyIndex(level, keyName);
	}

	@Override
	public String getLevelKeyName(int levelIndex, int keyIndex) {
		return this.rs.getLevelKeyName(levelIndex, keyIndex);
	}

	@Override
	public Object[] getLevelKeyValue(int levelIndex) {
		return ((Member) this.resultObject[levelIndex]).getKeyValues();
	}

	@Override
	public int getPosition() {
		return this.position;
	}

	@Override
	public int getSortType(int levelIndex) {
		return this.rs.getSortType(levelIndex);
	}

	@Override
	public int length() {
		return length;
	}

	@Override
	public void seek(int index) throws IOException {
		this.position = index;
		if (!isTimeMirror && breakHierarchy) {
			int remainder = 0, number, mirrorPlus = 1;
			for (int j = 0; j < this.breakHierarchyList.length; j++) {
				mirrorPlus *= breakHierarchyList[j].size();
			}
			number = (int) Math.floor(index / mirrorPlus);
			remainder = index % mirrorPlus;

			MemberTreeNode node = findOuterMostChild(this.rootNode, number + 1, 0);

			for (int j = mirrorLevel - 1; j >= 0; j--) {
				this.resultObject[j] = node.key;
				node = node.parentNode;
			}

			for (int i = this.mirrorLevel; i < this.rs.getLevelCount(); i++) {
				mirrorPlus = 1;
				if (i < rs.getLevelCount() - 1) {
					for (int j = i + 1; j < rs.getLevelCount(); j++) {
						mirrorPlus *= breakHierarchyList[j - mirrorLevel].size();
					}
					number = (int) Math.floor((double) remainder / (double) mirrorPlus);
				} else {
					number = remainder;
				}
				this.resultObject[i] = this.breakHierarchyList[i - this.mirrorLevel].get(number);
				remainder = remainder % mirrorPlus;
			}
		} else {
			int nodePos = (int) Math.floor((double) index / (double) this.valueMapLength);
			int remainder = index % this.valueMapLength;

			MemberTreeNode node = findOuterMostChild(this.rootNode, nodePos + 1, 0);

			Iterator iter = this.valueMap.entrySet().iterator();
			int currentLength = 0;
			MemberTreeNode findNode = null;
			while (iter.hasNext()) {
				MemberTreeNode n1 = (MemberTreeNode) ((Entry) iter.next()).getValue();
				int len = getLength(n1);
				if (currentLength + len <= remainder) {
					currentLength += len;
				} else {
					findNode = findOuterMostChild(n1, remainder - currentLength + 1, 0);
					break;
				}
			}

			if (node != null) {
				for (int i = this.mirrorLevel - 1; i >= 0; i--) {
					this.resultObject[i] = node.key;
					node = node.parentNode;
				}
			}
			if (findNode != null) {
				for (int i = this.rs.getLevelCount() - 1; i >= this.mirrorLevel; i--) {
					this.resultObject[i] = findNode.key;
					findNode = findNode.parentNode;
				}
			}
		}
	}

	private MemberTreeNode findOuterMostChild(MemberTreeNode node, int index, int startIndex) {
		int temp = startIndex;
		if (node.childNodesList.size() == 0) {
			temp++;
			if (index == temp) {
				return node;
			}
		}
		for (int i = 0; i < node.childNodesList.size(); i++) {
			MemberTreeNode child = (MemberTreeNode) node.childNodesList.get(i);
			MemberTreeNode find = findOuterMostChild(child, index, temp);
			if (find != null) {
				return find;
			} else {
				temp += this.getLength(child);
			}
		}
		return null;
	}

	@Override
	public int[] getAggregationDataType() {
		return this.rs.getAggregationDataType();
	}

	@Override
	public int[][] getLevelAttributeDataType() {
		return this.rs.getLevelAttributeDataType();
	}

	@Override
	public String[][] getLevelAttributes() {
		return this.rs.getLevelAttributes();
	}

	@Override
	public int[][] getLevelKeyDataType() {
		return this.rs.getLevelKeyDataType();
	}

	@Override
	public String[][] getLevelKeys() {
		return this.rs.getLevelKeys();
	}

	@Override
	public int[] getSortType() {
		return this.rs.getSortType();
	}

	@Override
	public Object[] getLevelAttributesValue(int levelIndex) {
		return ((Member) this.resultObject[levelIndex]).getAttributes();
	}

}
