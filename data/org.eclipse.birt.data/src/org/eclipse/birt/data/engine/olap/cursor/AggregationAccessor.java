/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.olap.cursor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.olap.OLAPException;
import javax.olap.cursor.DimensionCursor;
import javax.olap.cursor.EdgeCursor;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.api.IAggregationResultSet;
import org.eclipse.birt.data.engine.olap.data.api.IDimensionSortDefn;
import org.eclipse.birt.data.engine.olap.driver.IEdgeAxis;
import org.eclipse.birt.data.engine.olap.driver.IResultSet;
import org.eclipse.birt.data.engine.olap.query.view.BirtCubeView;
import org.eclipse.birt.data.engine.olap.query.view.BirtEdgeView;
import org.eclipse.birt.data.engine.olap.query.view.CalculatedMember;
import org.eclipse.birt.data.engine.olap.query.view.Relationship;
import org.eclipse.birt.data.engine.olap.util.CubeRunningNestAggrDefn;

/**
 * This class is to access all aggregation value's according to its result set
 * ID and its index. Aggregation with same aggrOn level list will be assigned
 * with same result set ID during preparation.
 * 
 * Firstly, we will do match on its member level. It's better to define one
 * aggregation in sequence of that in consideration of efficiency.It will match
 * values with its associated edge. If they are matched, return accessor's
 * current value, or move down/up to do the match again based on the logic of
 * sort direction on this level.
 * 
 * If there is no match find in cube cursor, 'null' value will be returned.
 */
public class AggregationAccessor extends Accessor {
	private BirtCubeView view;
	private IResultSet resultSet;
	private Map relationMap;
	private int[] currentPosition;

	private boolean firstNextMeasure = true;
	private IAggregationResultSet maxAggregationResultSet = null;
	private EdgeCursor rowEdgeCursor = null;
	private EdgeCursor columnEdgeCursor = null;
	private EdgeCursor pageEdgeCursor = null;
	private Relationship maxRelationship = null;
	private int[] rowLevelIndexs;
	private int[] columnLevelIndexs;
	private int[] pageLevelIndexs;
	private ComparableObject[] rowCursorObjs;
	private ComparableObject[] columnCursorObjs;
	private ComparableObject[] pageCursorObjs;
	private boolean dimensionPrepared = false;
	private Map dimensionCursorMap;
	private Map<String, Integer> aggregationResultSetIDMap;

	/**
	 * 
	 * @param view
	 * @param result
	 * @param relationMap
	 * @param manager
	 */
	public AggregationAccessor(BirtCubeView view, IResultSet result, Map relationMap) {
		this.resultSet = result;
		this.view = view;
		this.relationMap = relationMap;
		this.dimensionPrepared = false;
		this.dimensionCursorMap = new HashMap();
		this.aggregationResultSetIDMap = new HashMap<String, Integer>();

		if (result == null || result.getMeasureResult() == null)
			return;

		this.currentPosition = new int[this.resultSet.getMeasureResult().length];
		// initial aggregation resultset position to 0 if possible
		for (int i = 0; i < this.resultSet.getMeasureResult().length; i++) {
			try {
				if (this.resultSet.getMeasureResult()[i].getQueryResultSet().length() > 0) {
					this.resultSet.getMeasureResult()[i].getQueryResultSet().seek(0);
					currentPosition[i] = 0;
				} else {
					currentPosition[i] = -1;
				}
			} catch (IOException e) {
				// do nothing
			}
		}

		initMeasureNavigator();
	}

	/*
	 * 
	 */
	private void initMeasureNavigator() {
		IEdgeAxis[] edgeAxises = this.resultSet.getMeasureResult();
		int measureMaxSize = 0;
		for (int i = 0; i < edgeAxises.length; i++) {
			if (edgeAxises[i].getQueryResultSet().getAllLevels() != null
					&& edgeAxises[i].getQueryResultSet().getAllLevels().length > measureMaxSize) {
				maxAggregationResultSet = edgeAxises[i].getQueryResultSet();
				measureMaxSize = edgeAxises[i].getQueryResultSet().getAllLevels().length;
			}
		}

		Iterator iterator = relationMap.values().iterator();
		int relationMaxLevelSize = 0;
		maxRelationship = new Relationship(new ArrayList(), new ArrayList(), new ArrayList());
		while (iterator.hasNext()) {
			Relationship relation = (Relationship) iterator.next();
			int levelSize = relation.getLevelListOnColumn().size() + relation.getLevelListOnPage().size()
					+ relation.getLevelListOnRow().size();
			if (levelSize > relationMaxLevelSize) {
				relationMaxLevelSize = levelSize;
				maxRelationship = relation;
			}
		}
		DimLevel[] measureLevels = new DimLevel[0];
		if (maxAggregationResultSet != null) {
			measureLevels = maxAggregationResultSet.getAllLevels();
		}

		rowLevelIndexs = new int[maxRelationship.getLevelListOnRow().size()];
		for (int i = 0; i < rowLevelIndexs.length; i++) {
			DimLevel level = (DimLevel) maxRelationship.getLevelListOnRow().get(i);
			for (int j = 0; j < measureLevels.length; j++) {
				if (level.equals(measureLevels[j]))
					rowLevelIndexs[i] = j;
			}
		}

		columnLevelIndexs = new int[maxRelationship.getLevelListOnColumn().size()];
		for (int i = 0; i < columnLevelIndexs.length; i++) {
			DimLevel level = (DimLevel) maxRelationship.getLevelListOnColumn().get(i);
			for (int j = 0; j < measureLevels.length; j++) {
				if (level.equals(measureLevels[j]))
					columnLevelIndexs[i] = j;
			}
		}

		pageLevelIndexs = new int[maxRelationship.getLevelListOnPage().size()];
		for (int i = 0; i < pageLevelIndexs.length; i++) {
			DimLevel level = (DimLevel) maxRelationship.getLevelListOnPage().get(i);
			for (int j = 0; j < measureLevels.length; j++) {
				if (level.equals(measureLevels[j]))
					pageLevelIndexs[i] = j;
			}
		}
	}

	private void prepareDimensionCursor() throws OLAPException {
		if (this.view.getRowEdgeView() != null) {
			rowEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getRowEdgeView()).getEdgeCursor();
			if (rowEdgeCursor != null) {
				Iterator cursorIter = rowEdgeCursor.getDimensionCursor().iterator();
				while (cursorIter.hasNext()) {
					DimensionCursor cursor = (DimensionCursor) cursorIter.next();
					dimensionCursorMap.put(cursor.getName(), cursor);
				}
			}
		}
		if (this.view.getColumnEdgeView() != null) {
			columnEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getColumnEdgeView()).getEdgeCursor();
			if (columnEdgeCursor != null) {
				Iterator cursorIter = columnEdgeCursor.getDimensionCursor().iterator();
				while (cursorIter.hasNext()) {
					DimensionCursor cursor = (DimensionCursor) cursorIter.next();
					dimensionCursorMap.put(cursor.getName(), cursor);
				}
			}
		}
		if (this.view.getPageEdgeView() != null) {
			pageEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getPageEdgeView()).getEdgeCursor();
			if (pageEdgeCursor != null) {
				Iterator cursorIter = pageEdgeCursor.getDimensionCursor().iterator();
				while (cursorIter.hasNext()) {
					DimensionCursor cursor = (DimensionCursor) cursorIter.next();
					dimensionCursorMap.put(cursor.getName(), cursor);
				}
			}
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#close()
	 */
	public void close() throws OLAPException {
		if (this.resultSet == null || this.resultSet.getMeasureResult() == null)
			return;
		List errorList = new ArrayList();
		for (int i = 0; i < this.resultSet.getMeasureResult().length; i++) {
			try {
				this.resultSet.getMeasureResult()[i].getQueryResultSet().close();
			} catch (IOException e) {
				errorList.add(e);
			}
		}
		if (!errorList.isEmpty()) {
			throw new OLAPException(((IOException) errorList.get(0)).getLocalizedMessage());
		}
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(int)
	 */
	public Object getObject(int arg0) throws OLAPException {
		if (this.resultSet == null || this.resultSet.getMeasureResult() == null)
			return null;

		try {
			String aggrName = this.view.getAggregationRegisterTable().getAggrName(arg0);
			int index = 0, aggrIndex = 0;
			IAggregationResultSet rs = null;
			if (aggregationResultSetIDMap.containsKey(arg0)) {
				index = aggregationResultSetIDMap.get(arg0).intValue();
				rs = this.resultSet.getMeasureResult()[index].getQueryResultSet();
				aggrIndex = rs.getAggregationIndex(aggrName);
			} else {
				IEdgeAxis[] axis = this.resultSet.getMeasureResult();
				for (; index < axis.length; index++) {
					aggrIndex = axis[index].getQueryResultSet().getAggregationIndex(aggrName);
					if (aggrIndex >= 0) {
						rs = axis[index].getQueryResultSet();
						aggregationResultSetIDMap.put(aggrName, Integer.valueOf(index));
						break;
					}
				}
			}

			if (synchronizedWithEdge(index, rs, aggrName, getCurrentValueOnEdge(aggrName)))
				return rs.getAggregationValue(aggrIndex);
			else {
				return null;
			}
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		} catch (DataException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
	}

	/*
	 * @see
	 * org.eclipse.birt.data.engine.olap.cursor.Accessor#getObject(java.lang.String)
	 */
	public Object getObject(String arg0) throws OLAPException {
		if (this.resultSet == null || this.resultSet.getMeasureResult() == null)
			return null;

		try {
			int index = 0, aggrIndex = 0;
			IAggregationResultSet rs = null;
			if (aggregationResultSetIDMap.containsKey(arg0)) {
				index = aggregationResultSetIDMap.get(arg0).intValue();
				rs = this.resultSet.getMeasureResult()[index].getQueryResultSet();
				aggrIndex = rs.getAggregationIndex(arg0);
			} else {
				IEdgeAxis[] axis = this.resultSet.getMeasureResult();
				for (; index < axis.length; index++) {
					aggrIndex = axis[index].getQueryResultSet().getAggregationIndex(arg0);
					if (aggrIndex >= 0) {
						rs = axis[index].getQueryResultSet();
						aggregationResultSetIDMap.put(arg0, Integer.valueOf(index));
						break;
					}
				}
			}

			if (synchronizedWithEdge(index, rs, arg0, getCurrentValueOnEdge(arg0)))
				return rs.getAggregationValue(aggrIndex);
			else {
				return null;
			}
		} catch (IOException e) {
			throw new OLAPException(e.getLocalizedMessage());
		} catch (DataException e) {
			throw new OLAPException(e.getLocalizedMessage());
		}
	}

	/**
	 * 
	 * @param aggrIndex
	 * @throws OLAPException
	 * @throws IOException
	 * @throws DataException
	 */
	private boolean synchronizedWithEdge(int index, IAggregationResultSet rs, String aggrName, Map valueMap)
			throws OLAPException, IOException, DataException {
		if (rs == null || rs.length() <= 0)
			return false;

		if (valueMap == null)
			return true;

		List memberList = Arrays.asList(rs.getAllLevels());

		CalculatedMember member = this.view.getAggregationRegisterTable().getCalculatedMember(aggrName);
		if (member != null && member.getCubeAggrDefn() instanceof CubeRunningNestAggrDefn) {
			// AggregationResultSet for running aggregation
			return findValueMatcherOneByOne(rs, memberList, valueMap, index);
		} else {
			return findValueMatcher(rs, memberList, valueMap, index);
		}
	}

	/*
	 * 
	 */
	public boolean nextMeasure() throws IOException, OLAPException {
		int pos = maxAggregationResultSet.getPosition();
		if (firstNextMeasure) {
			if (pos >= maxAggregationResultSet.length())
				return false;
			firstNextMeasure = false;
		} else {
			if (pos + 1 >= maxAggregationResultSet.length())
				return false;
			maxAggregationResultSet.seek(pos + 1);
		}
		DimLevel[] levels = maxAggregationResultSet.getAllLevels();
		Object[] keyValues = new Object[levels.length];
		for (int i = 0; i < levels.length; i++) {
			keyValues[i] = maxAggregationResultSet.getLevelKeyValue(i)[0];
		}
		if (this.view.getRowEdgeView() != null && rowEdgeCursor == null) {
			rowEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getRowEdgeView()).getEdgeCursor();
			rowCursorObjs = fetcheObjects(rowEdgeCursor, maxRelationship.getLevelListOnRow());
		}
		if (this.view.getColumnEdgeView() != null && columnEdgeCursor == null) {
			columnEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getColumnEdgeView()).getEdgeCursor();
			columnCursorObjs = fetcheObjects(columnEdgeCursor, maxRelationship.getLevelListOnColumn());
		}
		if (this.view.getPageEdgeView() != null && pageEdgeCursor == null) {
			pageEdgeCursor = (EdgeCursor) ((BirtEdgeView) this.view.getPageEdgeView()).getEdgeCursor();
			pageCursorObjs = fetcheObjects(pageEdgeCursor, maxRelationship.getLevelListOnPage());
		}
		if (rowEdgeCursor != null) {
			moveEdgeCursor(rowEdgeCursor, rowLevelIndexs, rowCursorObjs, levels, keyValues);
		}
		if (columnEdgeCursor != null) {
			moveEdgeCursor(columnEdgeCursor, columnLevelIndexs, columnCursorObjs, levels, keyValues);
		}
		if (pageEdgeCursor != null) {
			moveEdgeCursor(pageEdgeCursor, pageLevelIndexs, pageCursorObjs, levels, keyValues);
		}

		return true;
	}

	private ComparableObject[] fetcheObjects(EdgeCursor edgeCursor, List edgeLevels) throws OLAPException {
		ArrayList<ComparableObject> objList = new ArrayList<ComparableObject>();

		if (!edgeCursor.first())
			return new ComparableObject[0];
		Object[] cursorValues = getCursorValues(edgeCursor, edgeLevels);
		objList.add(new ComparableObject(cursorValues, edgeCursor.getPosition()));
		while (edgeCursor.next()) {
			cursorValues = getCursorValues(edgeCursor, edgeLevels);
			objList.add(new ComparableObject(cursorValues, edgeCursor.getPosition()));
		}

		ComparableObject[] objArray = objList.toArray(new ComparableObject[0]);
		Arrays.sort(objArray);
		return objArray;
	}

	private boolean moveEdgeCursor(EdgeCursor cursor, int[] levelIndexs, ComparableObject[] cursorObjs,
			DimLevel[] levels, Object[] keyValues) throws OLAPException {
		Object[] cursorkeyValues = new Object[levelIndexs.length];
		for (int i = 0; i < cursorkeyValues.length; i++) {
			cursorkeyValues[i] = keyValues[levelIndexs[i]];
		}

		int index = Arrays.binarySearch(cursorObjs, new ComparableObject(cursorkeyValues, 0));

		if (index < 0) {
			return false;
		} else {
			cursor.setPosition(cursorObjs[index].getIndex());
			return true;
		}
	}

	private Object[] getCursorValues(EdgeCursor cursor, List edgeLevels) throws OLAPException {
		List dimCursors = cursor.getDimensionCursor();
		Object[] cursorValue = new Object[edgeLevels.size()];
		for (int i = 0; i < edgeLevels.size(); i++) {
			DimensionCursor dimCursor = (DimensionCursor) dimCursors.get(i);
			DimLevel level = (DimLevel) edgeLevels.get(i);
			cursorValue[i] = dimCursor.getObject(level.getLevelName());
		}
		return cursorValue;
	}

	private Map getCurrentValueOnEdge(String aggrName) throws OLAPException {
		if (!this.dimensionPrepared) {
			this.prepareDimensionCursor();
			this.dimensionPrepared = true;
		}

		Relationship relation = (Relationship) this.relationMap.get(aggrName);
		List pageLevelList = relation.getLevelListOnPage();
		List columnLevelList = relation.getLevelListOnColumn();
		List rowLevelList = relation.getLevelListOnRow();

		Map valueMap = new HashMap();
		if (columnLevelList.isEmpty() && rowLevelList.isEmpty() && pageLevelList.isEmpty())
			return null;

		for (int index = 0; index < pageLevelList.size(); index++) {
			DimLevel level = (DimLevel) pageLevelList.get(index);
			DimensionCursor cursor = (DimensionCursor) dimensionCursorMap
					.get(UniqueNamingUtil.getUniqueName(level.getDimensionName(), level.getLevelName()));
			Object value = cursor.getObject(level.getLevelName());
			valueMap.put(level, value);
		}

		for (int i = 0; i < columnLevelList.size(); i++) {
			DimLevel level = (DimLevel) columnLevelList.get(i);
			DimensionCursor cursor = (DimensionCursor) dimensionCursorMap
					.get(UniqueNamingUtil.getUniqueName(level.getDimensionName(), level.getLevelName()));
			Object value = cursor.getObject(level.getLevelName());
			valueMap.put(level, value);
		}

		for (int i = 0; i < rowLevelList.size(); i++) {
			DimLevel level = (DimLevel) rowLevelList.get(i);
			DimensionCursor cursor = (DimensionCursor) dimensionCursorMap
					.get(UniqueNamingUtil.getUniqueName(level.getDimensionName(), level.getLevelName()));
			Object value = cursor.getObject(level.getLevelName());
			valueMap.put(level, value);
		}
		return valueMap;
	}

	/**
	 * Find the value matcher in cube cursor. Based on sort direction and compared
	 * result, decide to move on/back along resultset.
	 * 
	 * @param rs
	 * @param levelList
	 * @param valueMap
	 * @param aggrIndex
	 * @return
	 * @throws IOException
	 */
	private boolean findValueMatcherOneByOne(IAggregationResultSet rs, List levelList, Map valueMap, int aggrIndex)
			throws IOException {
		int position = 0;
		if (rs.length() <= 0 || levelList.isEmpty())
			return true;
		while (position < rs.length()) {
			rs.seek(position);
			boolean match = true;
			for (int i = 0; i < levelList.size(); i++) {
				DimLevel level = (DimLevel) levelList.get(i);
				Object value1 = valueMap.get(level);
				Object value2 = null;
				int index = rs.getLevelIndex(level);
				Object[] keyValues = rs.getLevelKeyValue(index);
				if (keyValues != null)
					value2 = keyValues[rs.getLevelKeyColCount(index) - 1];
				;
				if (value1 == value2) {
					continue;
				}
				if (value1 == null || value2 == null || !value1.equals(value2)) {
					match = false;
					break;
				}
			}
			if (match) {
				return true;
			} else {
				++position;
			}
		}

		return false;
	}

	/**
	 * Find the value matcher in cube cursor. Based on sort direction and compared
	 * result, decide to move on/back along resultset.
	 * 
	 * @param rs
	 * @param levelList
	 * @param valueMap
	 * @param aggrIndex
	 * @return
	 */
	private boolean findValueMatcher(IAggregationResultSet rs, List levelList, Map valueMap, int aggrIndex) {
		if (levelList.isEmpty())
			return true;
		int start = 0, state = 0;
		boolean find = false;
		currentPosition[aggrIndex] = rs.getPosition();

		for (; start < levelList.size();) {
			DimLevel level = (DimLevel) levelList.get(start);

			Object value1 = valueMap.get(level);
			Object value2 = null;
			int index = rs.getLevelIndex(level);
			Object[] keyValues = rs.getLevelKeyValue(index);
			if (keyValues != null)
				value2 = keyValues[rs.getLevelKeyColCount(index) - 1];
			int sortType = rs.getSortType(index) == IDimensionSortDefn.SORT_DESC ? -1 : 1;
			int compare = compare(value1, value2);
			int direction = sortType * compare < 0 ? -1 : compare == 0 ? 0 : 1;
			if (direction < 0 && currentPosition[aggrIndex] > 0 && (state == 0 || state == direction)) {
				state = direction;
				try {
					rs.seek(--currentPosition[aggrIndex]);
				} catch (IOException e) {
					find = false;
				}
				start = 0;
				continue;
			} else if (direction > 0 && currentPosition[aggrIndex] < rs.length() - 1
					&& (state == 0 || state == direction)) {
				state = direction;
				try {
					rs.seek(++currentPosition[aggrIndex]);
				} catch (IOException e) {
					find = false;
				}
				start = 0;
				continue;
			} else if (direction == 0) {
				if (start == levelList.size() - 1) {
					find = true;
					break;
				} else {
					start++;
					continue;
				}
			} else if (currentPosition[aggrIndex] < 0 || currentPosition[aggrIndex] >= rs.length()) {
				return false;
			} else
				return false;
		}
		return find;
	}

	static int compare(Object value1, Object value2) {
		if (value1 == value2) {
			return 0;
		}
		if (value1 == null) {
			return -1;
		}
		if (value2 == null) {
			return 1;
		}
		if (value1 instanceof Comparable) {
			return ((Comparable) value1).compareTo(value2);
		}
		return value1.toString().compareTo(value2.toString());
	}

}

class ComparableObject implements Comparable {
	private Object[] fields;
	private long index;

	ComparableObject(Object[] fields, long index) {
		this.fields = fields;
		this.index = index;
	}

	public int compareTo(Object o) {
		Object[] oFields = ((ComparableObject) o).getFields();
		for (int i = 0; i < fields.length; i++) {
			int result = AggregationAccessor.compare(fields[i], oFields[i]);
			if (result != 0)
				return result;
		}
		return 0;
	}

	public Object[] getFields() {
		return fields;
	}

	public long getIndex() {
		return index;
	}

}
