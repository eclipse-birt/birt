/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.extension.internal;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.data.dte.QueryResultSet;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IRowMetaData;
import org.eclipse.birt.report.engine.extension.IRowSet;

/**
 * 
 * 
 */
public class RowSet implements IRowSet {
	protected IQueryResultSet rset;
	protected IRowMetaData metaData;
	protected ExecutionContext context;

	public RowSet(IQueryResultSet rset) throws BirtException {
		this.context = ((QueryResultSet) rset).getExecutionContext();

		this.rset = rset;
		metaData = new IRowMetaData() {

			public int getColumnCount() {
				return 0;
			}

			public String getColumnName(int index) throws BirtException {
				return null;
			}

			public int getColumnType(int index) throws BirtException {
				return -1;
			}
		};
		if (rset != null) {
			metaData = new RowMetaData(rset.getResultMetaData());
		}
	}

	public DataSetID getID() {
		return rset.getID();
	}

	/**
	 * returns the definition for the data row
	 * 
	 * @return the definition for the data row
	 */
	public IRowMetaData getMetaData() {
		return metaData;
	}

	public boolean next() {
		if (rset != null) {
			try {
				return rset.next();
			} catch (BirtException ex) {
				context.addException(ex);
				return false;
			}
		}
		return false;
	}

	public Object evaluate(String expr) {
		try {
			if (rset != null) {
				return rset.evaluate(expr);
			}
		} catch (BirtException ex) {
			context.addException(ex);
		}
		return null;
	}

	public Object evaluate(IBaseExpression expr) {
		try {
			if (rset != null) {
				return rset.evaluate(expr);
			}
		} catch (BirtException ex) {
			context.addException(ex);
		}
		return null;
	}

	/**
	 * Returns the value of a bound column by column index. So far it's a dummy
	 * implementation.
	 * 
	 * @param columnIndex
	 * @return
	 */
	public Object getValue(int columnIndex) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Returns the value of a bound column by column name.
	 * 
	 * @param name of bound column
	 * @return value of bound column
	 * @throws BirtException
	 */
	public Object getValue(String columnName) {
		try {
			if (rset != null) {
				return rset.getValue(columnName);
			}
		} catch (BirtException ex) {
			context.addException(ex);
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getEndingGroupLevel()
	 */
	public int getEndingGroupLevel() {
		if (rset != null) {
			try {
				return rset.getEndingGroupLevel();
			} catch (BirtException ex) {
				context.addException(ex);
			}
		}
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.extension.IRowSet#getStartingGroupLevel()
	 */
	public int getStartingGroupLevel() {
		if (rset != null) {
			try {
				return rset.getStartingGroupLevel();
			} catch (BirtException ex) {
				context.addException(ex);

			}
		}
		return 0;
	}

	public void close() {
		return;
	}

	public boolean isEmpty() throws BirtException {
		if (rset == null) {
			return true;
		}
		return rset.isEmpty();
	}
}
