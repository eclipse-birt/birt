/*******************************************************************************
 * Copyright (c) 2004,2007 Actuate Corporation.
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

package org.eclipse.birt.report.engine.data.dte;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;

public class QueryResultSet implements IQueryResultSet {

	protected IBaseResultSet parent;

	protected DataSetID id;

	// FIXME: See if we can remove it and use dTE's getRowIndex().
	protected long rowId = -1;

	/**
	 * result iterator object
	 */
	protected IResultIterator rs = null;

	/**
	 * holding the start row index of each group.
	 */
	protected long[] rowIdOfGroups;

	/**
	 * data engine
	 */
	protected IDataEngine dataEngine = null;

	private ExecutionContext context;

	private IBaseQueryDefinition queryDefn;

	private IQueryResults queryResults;

	private static IResultMetaData emptyResultMetaData = new EmptyResultMetaData();

	/**
	 * DTE's QueryResults's ID.
	 */
	private String queryResultsID;

	/**
	 *
	 */
	protected static Logger logger = Logger.getLogger(QueryResultSet.class.getName());

	// constructor for subclass only
	protected QueryResultSet() {

	}

	// Top level query results
	public QueryResultSet(IDataEngine dataEngine, ExecutionContext context, IQueryDefinition queryDefn,
			IQueryResults rsets) throws BirtException {
		this.parent = null;
		this.context = context;
		this.dataEngine = dataEngine;
		this.queryDefn = queryDefn;
		this.id = new DataSetID(rsets.getID());
		this.rs = rsets.getResultIterator();
		this.queryResults = rsets;
		this.queryResultsID = rsets.getID();
		initializeRowIdOfGroups(getGroupCount());
	}

	// Nest query
	public QueryResultSet(IDataEngine dataEngine, ExecutionContext context, IBaseResultSet parent,
			IQueryDefinition queryDefn, IQueryResults rsets) throws BirtException {
		assert parent != null;
		this.parent = parent;
		this.id = new DataSetID(rsets.getID());
		this.context = context;
		this.dataEngine = dataEngine;
		this.queryDefn = queryDefn;
		this.rs = rsets.getResultIterator();
		this.queryResults = rsets;
		this.queryResultsID = rsets.getID();
		initializeRowIdOfGroups(getGroupCount());
	}

	// subquery results
	public QueryResultSet(QueryResultSet parent, ISubqueryDefinition queryDefn, IResultIterator ri) // subqury
	{
		assert parent != null;
		assert queryDefn != null;
		this.parent = parent;
		// FIXME: why use parent's getRowIndex() not getRawId()?
		this.id = new DataSetID(parent.getID(), parent.getRowIndex(), queryDefn.getName());
		this.context = parent.context;
		this.dataEngine = parent.dataEngine;
		this.queryDefn = queryDefn;
		this.rs = ri;
		this.queryResults = this.rs.getQueryResults();
		int rowid = -1;
		try {
			rowid = parent.getResultIterator().getRowId();
		} catch (Exception ex) {
			// dont handle this exception because this works in most cases
		}
		StringBuilder sb = new StringBuilder();
		sb.append("{").append(parent.getQueryResultsID()).append("}.").append(rowid).append(".")
				.append(queryDefn.getName());
		this.queryResultsID = sb.toString();
		initializeRowIdOfGroups(queryDefn.getGroups().size());
	}

	public String getQueryResultsID() {
		return queryResultsID;
	}

	private void initializeRowIdOfGroups(int groupCount) {
		this.rowIdOfGroups = new long[groupCount + 2];
	}

	private int getGroupCount() {
		if (queryDefn instanceof IQueryDefinition) {
			List groups = ((IQueryDefinition) queryDefn).getGroups();
			assert groups != null;
			return groups.size();
		}
		return 0;
	}

	@Override
	public IBaseQueryResults getQueryResults() {
		return queryResults;
	}

	@Override
	public IResultIterator getResultIterator() {
		return rs;
	}

	@Override
	public long getRowIndex() {
		return rowId;
	}

	@Override
	public boolean next() throws BirtException {
		boolean flag;
		flag = rs == null ? false : rs.next();
		if (flag) {
			rowId++;
			updateRowIdOfGroups();
		}
		return flag;
	}

	private void updateRowIdOfGroups() {
		try {
			int startingGroup = rs.getStartingGroupLevel();
			for (int i = startingGroup; i < rowIdOfGroups.length; i++) {
				rowIdOfGroups[i] = rowId;
			}
		} catch (BirtException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}
	}

	@Override
	public boolean skipTo(long rowIndex) throws BirtException {
		if (rs == null) {
			return false;
		}
		long oldRowId = rowId;
		rs.moveTo((int) rowIndex);
		rowId = rowIndex;
		updateRowIdOfGroupsAfterSkip(oldRowId);
		return true;
	}

	private void updateRowIdOfGroupsAfterSkip(long oldRowId) {
		long incremetal = rowId - oldRowId;
		if (incremetal == 1) {
			updateRowIdOfGroups();
		} else if (incremetal > 1) {
			resetRowIdOfGroups();
		}
	}

	// FIXME: need find another solution to reset row ids.
	private void resetRowIdOfGroups() {
		for (int i = 0; i < rowIdOfGroups.length; i++) {
			rowIdOfGroups[i] = rowId;
		}
	}

	@Override
	public String getGroupId(int groupLevel) {
		return String.valueOf(groupLevel) + "." + getRowId(groupLevel); //$NON-NLS-1$
	}

	private String getRowId(int groupLevel) {
		assert rowIdOfGroups.length > 0;
		int level = groupLevel >= rowIdOfGroups.length ? rowIdOfGroups.length - 1 : groupLevel;
		level = groupLevel < 0 ? 0 : groupLevel;
		return String.valueOf(rowIdOfGroups[level]);
	}

	String baseRSetID;

	public void setBaseRSetID(String id) {
		baseRSetID = id;
	}

	public String getBaseRSetID() {
		if (baseRSetID == null && parent instanceof QueryResultSet) {
			return ((QueryResultSet) parent).getBaseRSetID();
		}
		return baseRSetID;
	}

	@Override
	public void close() {
		// FIXME: use try-catch for each close.
		// remove the data set from the data set list
		try {
			if (rs != null) {
				rs.close();
			}
		} catch (BirtException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			// context.addException( ex );
		}
		try {
			if (queryResults != null) {
				queryResults.close();
			}
		} catch (BirtException ex) {
			logger.log(Level.SEVERE, ex.getMessage(), ex);
			// context.addException( ex );
		}
	}

	@Override
	public Object evaluate(String expr) throws BirtException {
		if (expr == null) {
			return null;
		}
		IBaseResultSet oldRSet = context.getResultSet();
		if (oldRSet != this) {
			context.setResultSet(this);
		}

		Object result = context.evaluate(expr);

		if (oldRSet != this) {
			context.setResultSet(oldRSet);
		}
		return result;
	}

	@Override
	public Object evaluate(String language, String expr) throws BirtException {
		if (expr == null) {
			return null;
		}
		IBaseResultSet oldRSet = context.getResultSet();
		if (oldRSet != this) {
			context.setResultSet(this);
		}

		Object result = context.evaluateInlineScript(language, expr);

		if (oldRSet != this) {
			context.setResultSet(oldRSet);
		}
		return result;
	}

	@Override
	public Object evaluate(IBaseExpression expr) throws BirtException {
		IBaseResultSet oldRSet = context.getResultSet();
		if (oldRSet != this) {
			context.setResultSet(this);
		}

		Object result = null;
		if (expr instanceof IScriptExpression) {
			IScriptExpression scriptExpression = (IScriptExpression) expr;
			result = context.evaluate(scriptExpression.getText());
		} else if (expr instanceof IConditionalExpression) {
			result = context.evaluateCondExpr((IConditionalExpression) expr);
		}

		if (oldRSet != this) {
			context.setResultSet(oldRSet);
		}
		return result;
	}

	@Override
	public DataSetID getID() {
		return id;
	}

	@Override
	public IBaseResultSet getParent() {
		return parent;
	}

	@Override
	public String getRawID() throws BirtException {
		// getRowId() returns rawId, while getRowIndex() return the row index.
		return String.valueOf(rs.getRowId());
	}

	@Override
	public int getType() {
		return QUERY_RESULTSET;
	}

	@Override
	public Object getValue(String column) throws BirtException {
		return rs.getValue(column);
	}

	@Override
	public int getEndingGroupLevel() throws BirtException {
		return rs.getEndingGroupLevel();
	}

	@Override
	public int getStartingGroupLevel() throws BirtException {
		return rs.getStartingGroupLevel();
	}

	@Override
	public Boolean getBoolean(String name) throws BirtException {
		return rs.getBoolean(name);
	}

	@Override
	public Integer getInteger(String name) throws BirtException {
		return rs.getInteger(name);
	}

	@Override
	public Double getDouble(String name) throws BirtException {
		return rs.getDouble(name);
	}

	@Override
	public String getString(String name) throws BirtException {
		return rs.getString(name);
	}

	@Override
	public BigDecimal getBigDecimal(String name) throws BirtException {
		return rs.getBigDecimal(name);
	}

	@Override
	public Date getDate(String name) throws BirtException {
		return rs.getDate(name);
	}

	@Override
	public Blob getBlob(String name) throws BirtException {
		return rs.getBlob(name);
	}

	@Override
	public byte[] getBytes(String name) throws BirtException {
		return rs.getBytes(name);
	}

	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		if (null == rs) {
			return emptyResultMetaData;
		} else {
			return rs.getResultMetaData();
		}
	}

	@Override
	public boolean isEmpty() throws BirtException {
		if (rs == null) {
			return true;
		}
		return rs.isEmpty();
	}

	@Override
	public boolean isFirst() throws BirtException {
		if (rs == null) {
			return false;
		}
		return rs.isFirst();
	}

	@Override
	public boolean isBeforeFirst() throws BirtException {
		if (rs == null) {
			return false;
		}
		return rs.isBeforeFirst();
	}

	public ExecutionContext getExecutionContext() {
		return this.context;
	}

	private static class EmptyResultMetaData implements IResultMetaData {

		/**
		 * Returns the number of columns in a detail row of the result set.
		 *
		 * @return the number of columns in a detail row.
		 */
		@Override
		public int getColumnCount() {
			return 0;
		}

		/**
		 * Returns the column name at the specified index.
		 *
		 * @param index The projected column index.
		 * @return The name of the specified column.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public String getColumnName(int index) throws BirtException {
			return null;
		}

		/**
		 * Returns the column alias at the specified index. An alias is given to a
		 * column as a programmatic convenience. A column can be referred using a name
		 * or an alias interchangeably.
		 *
		 * @param index The projected column index.
		 * @return The alias of the specified column. Null if none is defined.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public String getColumnAlias(int index) throws BirtException {
			return null;
		}

		/**
		 * Returns the data type of the column at the specified index.
		 *
		 * @param index The projected column index.
		 * @return The data type of the specified column, as an integer defined in
		 *         org.eclipse.birt.data.engine.api.DataType.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public int getColumnType(int index) throws BirtException {
			return 0;
		}

		/**
		 * Returns the Data Engine data type name of the column at the specified index.
		 *
		 * @param index The projected column index.
		 * @return The Data Engine data type name of the specified column.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public String getColumnTypeName(int index) throws BirtException {
			return null;
		}

		/**
		 * Returns the data provider specific data type name of the specified column.
		 *
		 * @return the data type name as defined by the data provider.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public String getColumnNativeTypeName(int index) throws BirtException {
			return null;
		}

		/**
		 * Gets the label or display name of the column at the specified index.
		 *
		 * @param index The projected column index.
		 * @return The label of the specified column.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public String getColumnLabel(int index) throws BirtException {
			return null;
		}

		/**
		 * Indicates whether the specified projected column is defined as a computed
		 * column. A computed column is one that is not retrieved from the underlying
		 * data provider. Only those computed columns declared explicitly in a data set
		 * design are considered as "computed" columns.
		 *
		 * @param index The projected column index.
		 * @return true if the given column is a computed column; false otherwise.
		 * @throws BirtException if given index is invalid.
		 */
		@Override
		public boolean isComputedColumn(int index) throws BirtException {
			return false;
		}
	}

}
