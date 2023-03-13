/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.mozilla.javascript.Scriptable;

public class SingleQueryResultSet extends QueryResultSet {

	private IQueryResultSet rset;
	boolean isFirstCalled = true;

	public SingleQueryResultSet(IQueryResultSet rsets) {
		this.rset = rsets;
	}

	@Override
	public void close() {
		// do nothing in close( )
	}

	@Override
	public DataSetID getID() {
		return rset.getID();
	}

	@Override
	public IBaseQueryResults getQueryResults() {
		return rset.getQueryResults();
	}

	@Override
	public IResultIterator getResultIterator() {
		IResultIterator resultIterator = new SingleRowIterator(rset.getResultIterator());
		return resultIterator;
	}

	@Override
	public IResultMetaData getResultMetaData() throws BirtException {
		return rset.getResultMetaData();
	}

	@Override
	public boolean next() throws BirtException {
		if (isFirstCalled) {
			isFirstCalled = false;
			return true;
		}
		return false;
	}

	@Override
	public boolean isEmpty() throws BirtException {
		return rset.isEmpty();
	}

	@Override
	public boolean isFirst() throws BirtException {
		return rset.isFirst();
	}

	@Override
	public boolean isBeforeFirst() throws BirtException {
		return rset.isBeforeFirst();
	}

	@Override
	public Object evaluate(String expr) throws BirtException {
		return rset.evaluate(expr);
	}

	@Override
	public Object evaluate(String language, String expr) throws BirtException {
		return rset.evaluate(language, expr);
	}

	@Override
	public Object evaluate(IBaseExpression expr) throws BirtException {
		return rset.evaluate(expr);
	}

	@Override
	public IBaseResultSet getParent() {
		return rset;
	}

	@Override
	public String getRawID() throws BirtException {
		return rset.getRawID();
	}

	@Override
	public int getType() {
		return rset.getType();
	}

	@Override
	public BigDecimal getBigDecimal(String name) throws BirtException {
		return rset.getBigDecimal(name);
	}

	@Override
	public Blob getBlob(String name) throws BirtException {
		return rset.getBlob(name);
	}

	@Override
	public Boolean getBoolean(String name) throws BirtException {
		return rset.getBoolean(name);
	}

	@Override
	public byte[] getBytes(String name) throws BirtException {
		return rset.getBytes(name);
	}

	@Override
	public Date getDate(String name) throws BirtException {
		return rset.getDate(name);
	}

	@Override
	public Double getDouble(String name) throws BirtException {
		return rset.getDouble(name);
	}

	@Override
	public int getEndingGroupLevel() throws BirtException {
		return -1;
	}

	@Override
	public String getGroupId(int groupLevel) {
		return null;
	}

	@Override
	public Integer getInteger(String name) throws BirtException {
		return rset.getInteger(name);
	}

	@Override
	public long getRowIndex() {
		return rset.getRowIndex();
	}

	@Override
	public int getStartingGroupLevel() throws BirtException {
		return -1;
	}

	@Override
	public String getString(String name) throws BirtException {
		return rset.getString(name);
	}

	@Override
	public Object getValue(String name) throws BirtException {
		return rset.getValue(name);
	}

	@Override
	public boolean skipTo(long rowIndex) throws BirtException {
		return false;
	}

	@Override
	public String getQueryResultsID() {
		if (rset instanceof QueryResultSet) {
			return ((QueryResultSet) rset).getQueryResultsID();
		}
		return null;
	}

	private static class SingleRowIterator implements IResultIterator {

		private boolean isFirstCalled = true;
		IResultIterator iter = null;

		public SingleRowIterator(IResultIterator iter) {
			this.iter = iter;
		}

		@Override
		public boolean next() {
			if (isFirstCalled) {
				isFirstCalled = false;
				return true;
			}
			return false;
		}

		@Override
		public void close() throws BirtException {
			// do nothing in close( )
		}

		@Override
		public boolean findGroup(Object[] groupKeyValues) throws BirtException {
			return false;
		}

		@Override
		public BigDecimal getBigDecimal(String name) throws BirtException {
			return iter.getBigDecimal(name);
		}

		@Override
		public Blob getBlob(String name) throws BirtException {
			return iter.getBlob(name);
		}

		@Override
		public Boolean getBoolean(String name) throws BirtException {
			return iter.getBoolean(name);
		}

		@Override
		public byte[] getBytes(String name) throws BirtException {
			return iter.getBytes(name);
		}

		@Override
		public Date getDate(String name) throws BirtException {
			return iter.getDate(name);
		}

		@Override
		public Double getDouble(String name) throws BirtException {
			return iter.getDouble(name);
		}

		@Override
		public int getEndingGroupLevel() throws BirtException {
			return -1;
		}

		@Override
		public Integer getInteger(String name) throws BirtException {
			return iter.getInteger(name);
		}

		@Override
		public IQueryResults getQueryResults() {
			return iter.getQueryResults();
		}

		@Override
		public IResultMetaData getResultMetaData() throws BirtException {
			return iter.getResultMetaData();
		}

		@Override
		public int getRowId() throws BirtException {
			return iter.getRowId();
		}

		@Override
		public int getRowIndex() throws BirtException {
			return iter.getRowIndex();
		}

		@Override
		public Scriptable getScope() {
			return iter.getScope();
		}

		@Override
		public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
			return null;
		}

		@Override
		public IResultIterator getSecondaryIterator(ScriptContext scriptContext, String subQueryName)
				throws BirtException {
			return null;
		}

		@Override
		public int getStartingGroupLevel() throws BirtException {
			return -1;
		}

		@Override
		public String getString(String name) throws BirtException {
			return iter.getString(name);
		}

		@Override
		public Object getValue(String name) throws BirtException {
			return iter.getValue(name);
		}

		@Override
		public boolean isEmpty() throws BirtException {
			return iter.isEmpty();
		}

		@Override
		public void moveTo(int rowIndex) throws BirtException {
		}

		@Override
		public void skipToEnd(int groupLevel) throws BirtException {
		}

		@Override
		public boolean isBeforeFirst() throws BirtException {
			return this.iter.isBeforeFirst();
		}

		@Override
		public boolean isFirst() throws BirtException {
			return this.iter.isFirst();
		}
	}

}
