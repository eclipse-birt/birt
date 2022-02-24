/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.aggregation.Accumulator;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.ComparatorUtil;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.data.impl.AggregationFunctionDefinition;
import org.eclipse.birt.data.engine.olap.data.impl.dimension.Member;

public class MergeRow4Aggregation {
	private Map<RowHashKey, Node> rowMap;
	private int cacheSize;
	private int nodeSize;
	private AggregationFunctionDefinition aggregation;
	private IAggrFunction aggrFunc;
	private Node firstNode;
	private Node lastNode;

	private int measureIndexes;
	private int parameterColIndex;

	MergeRow4Aggregation(int cacheSize, AggregationFunctionDefinition aggregation, int measureIndexes,
			int parameterColIndex) throws DataException {
		this.cacheSize = cacheSize;
		this.aggregation = aggregation;

		if (aggregation != null) {
			aggrFunc = AggregationManager.getInstance().getAggregation(aggregation.getFunctionName());
			if (aggrFunc == null) {
				throw new DataException(
						DataResourceHandle.getInstance().getMessage(ResourceConstants.UNSUPPORTED_FUNCTION)
								+ aggregation.getFunctionName());
			}

			this.parameterColIndex = parameterColIndex;
			this.measureIndexes = measureIndexes;

		}
		this.rowMap = new HashMap<RowHashKey, Node>();
		this.nodeSize = 0;
	}

	public Row4Aggregation push(Row4Aggregation row) throws DataException {
		RowHashKey hashKey = new RowHashKey(row.getLevelMembers(), row.getParameterValues());
		Node mapNode = rowMap.get(hashKey);
		if (mapNode == null) {
			addNode(row);
			if (nodeSize > cacheSize) {
				Node popNode = popNode();
				return popNode.row;
			}
		} else {
			if (mapNode.accumulator == null) {
				while (row.nextMeasures()) {
					mapNode.row.addMeasure(row.getMeasures());
				}
			} else {
				while (row.nextMeasures()) {
					mapNode.accumulator.onRow(getAccumulatorParameter(row));
				}
			}
		}

		return null;
	}

	private Object[] getAccumulatorParameter(Row4Aggregation row) {
		Object[] parameters = null;
		if (parameterColIndex == -1) {
			parameters = new Object[1];
			if (measureIndexes < 0) {
				return null;
			} else {
				parameters[0] = row.getMeasures()[measureIndexes];
			}
		} else {
			parameters = new Object[2];
			if (measureIndexes < 0) {
				parameters[0] = null;
			} else {
				parameters[0] = row.getMeasures()[measureIndexes];
			}
			parameters[1] = row.getParameterValues()[parameterColIndex];
		}
		return parameters;
	}

	private void addNode(Row4Aggregation row) throws DataException {
		Node node = new Node();
		node.row = row;
		if (aggrFunc != null) {
			node.accumulator = aggrFunc.newAccumulator();
			node.accumulator.start();
			while (row.nextMeasures()) {
				node.accumulator.onRow(getAccumulatorParameter(row));
			}
			node.row.clearMeasure();
		}
		if (lastNode != null) {
			node.preNode = lastNode;
			lastNode.nextNode = node;
			lastNode = node;
		} else {
			firstNode = node;
			lastNode = node;
		}
		RowHashKey hashKey = new RowHashKey(row.getLevelMembers(), node.row.getParameterValues());
		rowMap.put(hashKey, node);
		nodeSize++;
	}

	private Node popNode() throws DataException {
		Node node = firstNode;
		firstNode = node.nextNode;
		if (firstNode != null)
			firstNode.preNode = null;
		node.nextNode = null;
		RowHashKey hashKey = new RowHashKey(node.row.getLevelMembers(), node.row.getParameterValues());
		rowMap.remove(hashKey);
		nodeSize--;
		if (node.accumulator != null) {
			node.accumulator.finish();
			node.row.getMeasures()[measureIndexes] = node.accumulator.getValue();
		}
		node.row.resetPosition();
		return node;
	}

	public List<Row4Aggregation> getAll() throws DataException {
		List<Row4Aggregation> rowList = new ArrayList<Row4Aggregation>();
		Iterator<Node> nodes = this.rowMap.values().iterator();
		while (nodes.hasNext()) {
			Node node = nodes.next();
			if (node.accumulator != null) {
				node.accumulator.finish();
				node.row.getMeasures()[measureIndexes] = node.accumulator.getValue();
				node.row.resetPosition();
			}
			rowList.add(node.row);
		}
		return rowList;
	}
}

class Node {
	Row4Aggregation row;
	Node preNode;
	Node nextNode;
	Accumulator accumulator;
}

class RowHashKey {
	Member[] levelMembers;
	Object[] paraValues;

	RowHashKey(Member[] levelMembers, Object[] paraValues) {
		this.levelMembers = levelMembers;
		this.paraValues = paraValues;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		Member[] oMembers = ((RowHashKey) o).levelMembers;
		for (int i = 0; i < levelMembers.length; i++) {
			if ((levelMembers[i] == null && oMembers[i] != null) || (levelMembers[i] != null && oMembers[i] == null)) {
				return false;
			}
			if (levelMembers[i] == null && oMembers[i] == null)
				continue;
			if (!levelMembers[i].equals(oMembers[i])) {
				return false;
			}
		}
		Object[] oParaValues = ((RowHashKey) o).paraValues;
		if (oParaValues == null && paraValues == null)
			return true;
		if (oParaValues == null || paraValues == null)
			return false;
		for (int i = 0; i < paraValues.length; i++) {
			if (!ComparatorUtil.isEqualObject(paraValues[i], oParaValues[i]))
				return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		int hashCode = 1;
		for (int j = 0; j < levelMembers.length; j++) {
			for (int i = 0; i < levelMembers[j].getKeyValues().length; i++) {
				hashCode = 31 * hashCode + (levelMembers[j].getKeyValues()[i] == null ? 0
						: levelMembers[j].getKeyValues()[i].hashCode());
			}
		}
		if (this.paraValues == null)
			return hashCode;
		for (int i = 0; i < paraValues.length; i++) {
			hashCode = 31 * hashCode + (paraValues[i] == null ? 0 : paraValues[i].hashCode());
		}
		return hashCode;
	}
}
