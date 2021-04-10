/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public void close() {
		// do nothing in close( )
	}

	public DataSetID getID() {
		return rset.getID();
	}

	public IBaseQueryResults getQueryResults() {
		return rset.getQueryResults();
	}

	public IResultIterator getResultIterator() {
		IResultIterator resultIterator = new SingleRowIterator(rset.getResultIterator());
		return resultIterator;
	}

	public IResultMetaData getResultMetaData() throws BirtException {
		return rset.getResultMetaData();
	}

	public boolean next() throws BirtException {
		if (isFirstCalled) {
			isFirstCalled = false;
			return true;
		}
		return false;
	}

	public boolean isEmpty() throws BirtException {
		return rset.isEmpty();
	}

	public boolean isFirst() throws BirtException {
		return rset.isFirst();
	}

	public boolean isBeforeFirst() throws BirtException {
		return rset.isBeforeFirst();
	}

	public Object evaluate(String expr) throws BirtException {
		return rset.evaluate(expr);
	}

	public Object evaluate(String language, String expr) throws BirtException {
		return rset.evaluate(language, expr);
	}

	public Object evaluate(IBaseExpression expr) throws BirtException {
		return rset.evaluate(expr);
	}

	public IBaseResultSet getParent() {
		return rset;
	}

	public String getRawID() throws BirtException {
		return rset.getRawID();
	}

	public int getType() {
		return rset.getType();
	}

	public BigDecimal getBigDecimal(String name) throws BirtException {
		return rset.getBigDecimal(name);
	}

	public Blob getBlob(String name) throws BirtException {
		return rset.getBlob(name);
	}

	public Boolean getBoolean(String name) throws BirtException {
		return rset.getBoolean(name);
	}

	public byte[] getBytes(String name) throws BirtException {
		return rset.getBytes(name);
	}

	public Date getDate(String name) throws BirtException {
		return rset.getDate(name);
	}

	public Double getDouble(String name) throws BirtException {
		return rset.getDouble(name);
	}

	public int getEndingGroupLevel() throws BirtException {
		return -1;
	}

	public String getGroupId(int groupLevel) {
		return null;
	}

	public Integer getInteger(String name) throws BirtException {
		return rset.getInteger(name);
	}

	public long getRowIndex() {
		return rset.getRowIndex();
	}

	public int getStartingGroupLevel() throws BirtException {
		return -1;
	}

	public String getString(String name) throws BirtException {
		return rset.getString(name);
	}

	public Object getValue(String name) throws BirtException {
		return rset.getValue(name);
	}

	public boolean skipTo(long rowIndex) throws BirtException {
		return false;
	}

	public String getQueryResultsID() {
		if (rset instanceof QueryResultSet)
			return ((QueryResultSet) rset).getQueryResultsID();
		return null;
	}

	private static class SingleRowIterator implements IResultIterator {

		private boolean isFirstCalled = true;
		IResultIterator iter = null;

		public SingleRowIterator(IResultIterator iter) {
			this.iter = iter;
		}

		public boolean next() {
			if (isFirstCalled) {
				isFirstCalled = false;
				return true;
			}
			return false;
		}

		public void close() throws BirtException {
			// do nothing in close( )
		}

		public boolean findGroup(Object[] groupKeyValues) throws BirtException {
			return false;
		}

		public BigDecimal getBigDecimal(String name) throws BirtException {
			return iter.getBigDecimal(name);
		}

		public Blob getBlob(String name) throws BirtException {
			return iter.getBlob(name);
		}

		public Boolean getBoolean(String name) throws BirtException {
			return iter.getBoolean(name);
		}

		public byte[] getBytes(String name) throws BirtException {
			return iter.getBytes(name);
		}

		public Date getDate(String name) throws BirtException {
			return iter.getDate(name);
		}

		public Double getDouble(String name) throws BirtException {
			return iter.getDouble(name);
		}

		public int getEndingGroupLevel() throws BirtException {
			return -1;
		}

		public Integer getInteger(String name) throws BirtException {
			return iter.getInteger(name);
		}

		public IQueryResults getQueryResults() {
			return iter.getQueryResults();
		}

		public IResultMetaData getResultMetaData() throws BirtException {
			return iter.getResultMetaData();
		}

		public int getRowId() throws BirtException {
			return iter.getRowId();
		}

		public int getRowIndex() throws BirtException {
			return iter.getRowIndex();
		}

		public Scriptable getScope() {
			return iter.getScope();
		}

		public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
			return null;
		}

		public IResultIterator getSecondaryIterator(ScriptContext scriptContext, String subQueryName)
				throws BirtException {
			return null;
		}

		public int getStartingGroupLevel() throws BirtException {
			return -1;
		}

		public String getString(String name) throws BirtException {
			return iter.getString(name);
		}

		public Object getValue(String name) throws BirtException {
			return iter.getValue(name);
		}

		public boolean isEmpty() throws BirtException {
			return iter.isEmpty();
		}

		public void moveTo(int rowIndex) throws BirtException {
		}

		public void skipToEnd(int groupLevel) throws BirtException {
		}

		public boolean isBeforeFirst() throws BirtException {
			return this.iter.isBeforeFirst();
		}

		public boolean isFirst() throws BirtException {
			return this.iter.isFirst();
		}
	}

}
