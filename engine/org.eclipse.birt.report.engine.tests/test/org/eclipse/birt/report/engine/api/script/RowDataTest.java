/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
package org.eclipse.birt.report.engine.api.script;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

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
import org.eclipse.birt.report.engine.script.internal.RowData;
import org.mozilla.javascript.Scriptable;

/**
 * JUnit test for the RowData class
 * 
 */
public class RowDataTest extends TestCase {

	private Map testExpressions;

	private IRowData rowData;

	protected void setUp() {
		testExpressions = new HashMap();
		testExpressions.put("row[9999]", "1");
		testExpressions.put("row[\"test\"]", "2");
		testExpressions.put("test", "3");
		testExpressions.put("row[\"test1\"]", "4");
		testExpressions.put("ROW[\"test\"]", "5");
		testExpressions.put("ROw[\"test\"]", "6");
		testExpressions.put("row[\"test_1\"]", "7");
		testExpressions.put("row[\"test\"] + test", "8");
		testExpressions.put("row[\"test\"] + row[\"testit\"]", "9");

		IResultIterator iterator = new FakeResultIteratorTest(testExpressions);
		IQueryResultSet rset = new FakeResultSetTest(iterator);
		List expressions = new ArrayList();
		expressions.addAll(testExpressions.keySet());
		rowData = new RowData(rset, null);
	}

	public void testIndex() throws ScriptException {
	}

	public void testWithQuotes() throws ScriptException {
	}

	public void testWithoutQuotes() throws ScriptException {
	}

	public void testWithoutRow() throws ScriptException {
		assertEquals("3", rowData.getColumnValue("test"));
	}

	public void testWithNumber() throws ScriptException {
	}

	public void testWithCapital() throws ScriptException {
	}

	public void testWithMixed() throws ScriptException {
	}

	public void testWithUnderscore() throws ScriptException {
	}

	public void testWithMultipleParts1() throws ScriptException {
	}

	public void testWithMultipleParts2() throws ScriptException {
	}

	// Fake a result set
	private class FakeResultSetTest implements IQueryResultSet {
		protected IResultIterator rs = null;

		public FakeResultSetTest(IResultIterator rs) {
			this.rs = rs;
		}

		public IBaseResultSet getParent() {
			return null;
		}

		public DataSetID getID() {
			// TODO Auto-generated method stub
			return null;
		}

		public long getCurrentPosition() {
			// TODO Auto-generated method stub
			return 0;
		}

		public boolean skipTo(long rows) {
			// TODO Auto-generated method stub
			return false;
		}

		public boolean next() {
			// TODO Auto-generated method stub
			return false;
		}

		public int getStartingGroupLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		public int getEndingGroupLevel() {
			// TODO Auto-generated method stub
			return 0;
		}

		public IResultMetaData getResultMetaData() throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getValue(String name) throws BirtException {
			return rs.getValue(name);
		}

		public Boolean getBoolean(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Integer getInteger(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Double getDouble(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getString(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public BigDecimal getBigDecimal(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Date getDate(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Blob getBlob(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public byte[] getBytes(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public void close() {
			// TODO Auto-generated method stub

		}

		public Object evaluate(String expr) {
			// TODO Auto-generated method stub
			try {
				return rs.getValue(expr);
			} catch (BirtException e) {
			}
			return null;
		}

		public Object evaluate(String language, String expr) throws BirtException {
			// TODO Auto-generated method stub
			try {
				return rs.getValue(expr);
			} catch (BirtException e) {
			}
			return null;
		}

		public Object evaluate(IBaseExpression expr) {
			// TODO Auto-generated method stub
			return null;
		}

		public String getGroupId(int groupLevel) {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEmpty() throws BirtException {
			return rs.isEmpty();
		}

		public boolean isFirst() throws BirtException {
			return rs.isFirst();
		}

		public boolean isBeforeFirst() throws BirtException {
			return rs.isBeforeFirst();
		}

		public String getBaseRSetID() {
			// TODO Auto-generated method stub
			return null;
		}

		public IResultIterator getResultIterator() {
			// TODO Auto-generated method stub
			return null;
		}

		public long getRowIndex() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void setBaseRSetID(String id) {
			// TODO Auto-generated method stub

		}

		public IBaseQueryResults getQueryResults() {
			// TODO Auto-generated method stub
			return null;
		}

		public String getRawID() throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public int getType() {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	// Fake a result iterator
	private class FakeResultIteratorTest implements IResultIterator {

		private Map expressions;

		public FakeResultIteratorTest(Map expressions) {
			this.expressions = expressions;
		}

		public IQueryResults getQueryResults() {
			return null;
		}

		public Scriptable getScope() {
			return null;
		}

		public IResultMetaData getResultMetaData() throws BirtException {
			return null;
		}

		public boolean next() throws BirtException {
			return false;
		}

		public Boolean getBoolean(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public Integer getInteger(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public Double getDouble(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public String getString(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public BigDecimal getBigDecimal(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public Date getDate(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public Blob getBlob(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public byte[] getBytes(IBaseExpression dataExpr) throws BirtException {
			return null;
		}

		public void skipToEnd(int groupLevel) throws BirtException {

		}

		public int getStartingGroupLevel() throws BirtException {
			return 0;
		}

		public int getEndingGroupLevel() throws BirtException {
			return 0;
		}

		public IResultIterator getSecondaryIterator(String subQueryName, Scriptable scope) throws BirtException {
			return null;
		}

		public void close() throws BirtException {

		}

		public boolean findGroup(Object[] groupKeyValues) throws BirtException {
			return false;
		}

		public int getRowId() throws BirtException {
			return 0;
		}

		public int getRowIndex() throws BirtException {
			return 0;
		}

		public void moveTo(int rowIndex) throws BirtException {

		}

		public Object getValue(String name) throws BirtException {
			return expressions.get(name);
		}

		public Boolean getBoolean(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Integer getInteger(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Double getDouble(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public String getString(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public BigDecimal getBigDecimal(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Date getDate(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Blob getBlob(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public byte[] getBytes(String name) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public Object getValue(IBaseExpression dataExpr) throws BirtException {
			// TODO Auto-generated method stub
			return null;
		}

		public boolean isEmpty() throws BirtException {
			return true;
		}

		public boolean isFirst() throws BirtException {
			return false;
		}

		public boolean isBeforeFirst() throws BirtException {
			return false;
		}

		public IResultIterator getSecondaryIterator(ScriptContext scriptContext, String subQueryName)
				throws BirtException {
			return null;
		}
	}
}
