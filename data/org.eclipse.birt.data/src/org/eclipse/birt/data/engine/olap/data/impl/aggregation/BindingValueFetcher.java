
/*******************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeCursor;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.IBindingValueFetcher;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationRowAccessor;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * This class is used to fetch binding values from cube cursor
 *
 * @author Administrator
 *
 */
public class BindingValueFetcher implements IBindingValueFetcher {
	//
	private Node node;
	private List<String> bindingNames;
	private List<String> levels;

	private AggregationRowAccessor currentRow;
	private int currentRowIndex;
	private List currentBindingValues;
	private List<Set<String>> dimLevelOfInteresting;
	private static Dummy DUMMYOBJ = new Dummy();

	public BindingValueFetcher(ICubeCursor cursor, ICubeQueryDefinition queryDefn, List<String> bindingOfInteresting,
			List<Set<String>> dimLevelOfInteresting) throws DataException {
		this.node = new Node(null);
		this.bindingNames = bindingOfInteresting;
		this.levels = new ArrayList<>();
		this.currentBindingValues = new ArrayList();
		this.dimLevelOfInteresting = dimLevelOfInteresting;
		populateOrderedLvls(cursor);
		populateBindingValueTree(cursor, bindingOfInteresting);
	}

	/**
	 *
	 * @param cursor
	 * @param bindingOfInteresting
	 * @throws DataException
	 */
	private void populateBindingValueTree(ICubeCursor cursor, List<String> bindingOfInteresting) throws DataException {
		List memberValue = new ArrayList();
		List edges = cursor.getOrdinateEdge();
		if (edges.size() > 0) {
			this.populateNode(edges, cursor, memberValue, bindingOfInteresting);
		}
	}

	/**
	 *
	 * @param edgeCursors
	 * @param cursor
	 * @param memberValue
	 * @param bindingOfInteresting
	 * @throws DataException
	 */
	private void populateNode(List<EdgeCursor> edgeCursors, ICubeCursor cursor, List memberValue,
			List<String> bindingOfInteresting) throws DataException {
		EdgeCursor edge = edgeCursors.get(0);
		edge.beforeFirst();
		while (edge.next()) {
			List temp = new ArrayList(memberValue);
			List<DimensionCursor> dimCursors = edge.getDimensionCursor();
			for (DimensionCursor dim : dimCursors) {
				temp.add(dim.getObject(0));
			}
			if (edgeCursors.size() == 1) {
				List bindingValues = new ArrayList();
				for (String bindingName : bindingOfInteresting) {
					bindingValues.add(cursor.getObject(bindingName));
				}
				this.node.add(temp, bindingValues);
			} else {
				this.populateNode(edgeCursors.subList(1, edgeCursors.size()), cursor, temp, bindingOfInteresting);
			}
		}
	}

	/**
	 *
	 * @param cursor
	 * @throws OLAPException
	 */
	private void populateOrderedLvls(ICubeCursor cursor) throws OLAPException {
		for (EdgeCursor edge : (List<EdgeCursor>) cursor.getOrdinateEdge()) {
			for (DimensionCursor dim : (List<DimensionCursor>) edge.getDimensionCursor()) {
				String dimLvl = dim.getName();
				dimLvl += "/" + dimLvl.split("/")[1];
				this.levels.add(dimLvl);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.IBindingValueFetcher
	 * #getValue(java.lang.String,
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation
	 * .filter.AggregationRowAccessor)
	 */
	@Override
	public Object getValue(String bindingName, AggregationRowAccessor row, int rowIndex) throws DataException {
		int index = this.bindingNames.indexOf(bindingName);

		if (index != -1) {
			Set<String> involvedLvl = this.dimLevelOfInteresting.get(index);
			if (this.currentRow != row || this.currentRowIndex != rowIndex) {
				List memberValues = new ArrayList();
				memberValues.add(DUMMYOBJ);
				for (String lvlName : this.levels) {
					if (involvedLvl.contains(lvlName) || row.isAxisLevel(lvlName)) {
						memberValues.add(row.getFieldValue(lvlName));
					} else {
						memberValues.add(DUMMYOBJ);
					}
				}

				this.currentBindingValues = node.getBindingValue(memberValues);
				this.currentRow = row;
				this.currentRowIndex = rowIndex;
			}
			return this.currentBindingValues == null ? null : this.currentBindingValues.get(index);
		}
		return null;
	}

	private static class Dummy {
	}

	/**
	 * The class is used to build a tree that contains binding values
	 *
	 * @author Administrator
	 *
	 */
	private class Node {

		private Object value;
		private Set<Node> sub;
		private List bindingValues;

		Node(Object value) {
			this.value = value;
			this.sub = new HashSet<>();
		}

		private boolean valueEqual(Object o) throws DataException {
			// is drilled element
			if ((o == DUMMYOBJ) || (this.value == null)) {
				return true;
			}
			return ScriptEvalUtil.compare(this.value, o) == 0;
		}

		public Node find(List memberValue) throws DataException {
			if (!valueEqual(memberValue.get(0))) {
				return null;
			}
			if (memberValue.size() == 1 && valueEqual(memberValue.get(0))) {
				return this;
			}
			for (Node subNode : sub) {
				Node result = subNode.find(memberValue.subList(1, memberValue.size()));
				if (result != null) {
					return result;
				}
			}
			return null;
		}

		@Override
		public String toString() {
			String result = "";
			for (Node subNode : sub) {
				result += (subNode.value + ",");
			}
			return result;
		}

		public List getBindingValue(List memberValues) throws DataException {
			Node result = find(memberValues);
			if (result == null) {
				return null;
			}
			return result.bindingValues;
		}

		public void add(List memberValues, List bindingValues) throws DataException {
			if (!memberValues.isEmpty()) {
				Object o = memberValues.get(0);
				boolean addNewNode = true;
				for (Node subNode : sub) {
					if (subNode.valueEqual(o)) {
						subNode.add(memberValues.size() == 1 ? new ArrayList()
								: memberValues.subList(1, memberValues.size()), bindingValues);
						addNewNode = false;
					}
					if (!addNewNode) {
						break;
					}
				}
				if (addNewNode) {
					Node newNode = new Node(o);
					this.sub.add(newNode);
					newNode.add(
							memberValues.size() == 1 ? new ArrayList() : memberValues.subList(1, memberValues.size()),
							bindingValues);
				}
			} else {
				this.bindingValues = bindingValues;
			}

		}
	}
}
