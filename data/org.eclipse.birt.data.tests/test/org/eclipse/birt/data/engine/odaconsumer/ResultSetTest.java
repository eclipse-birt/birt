/*
 * ****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-2.0.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 * 
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;
import java.sql.Types;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class ResultSetTest extends QueryTest {

	static String[] RESULTS = { "123, 1.212312, blah blah blah, 2000-09-01, 600",
			"14, 3.14, hahahahahahhahaha, 1991-10-02, 10", "0, 1.23, niem, 1979-11-28, 10",
			"null, null, null, null, null", "4, 12.3636, seven zero six, 2004-01-01, 10000" };

	private ResultSet m_resultSet;

	ResultSet getResultSet() {
		return m_resultSet;
	}

	@Before
	public void resultSetSetUp() throws Exception {
		getStatement().execute();
		m_resultSet = getStatement().getResultSet();
	}

	@After
	public void resultSetTearDown() throws Exception {
		m_resultSet.close();
	}

	public final void testGetResultSetMetaData() throws DataException {
		IResultClass metadata = m_resultSet.getMetaData();
		assertNotNull(metadata);

		IResultClass metadata1 = m_resultSet.getMetaData();
		assertSame(metadata, metadata1);
	}

	public final void testSetMaxRows() throws DataException {
		m_resultSet.setMaxRows(3);
		int count = 0;
		while (m_resultSet.fetch() != null)
			count++;
		assertEquals(3, count);
	}

	public final void testFetch() throws DataException {
		int rowPosition = 0;
		IResultObject resultObject = null;
		IResultClass resultClass = null;
		while ((resultObject = m_resultSet.fetch()) != null) {
			rowPosition++;
			if (rowPosition == 1) // check the ResultClass the first time
			{
				resultClass = testResultClass(resultObject);
			}

			testFields(resultObject, resultClass, rowPosition);
		}
	}

	private IResultClass testResultClass(IResultObject resultObject) throws DataException {
		IResultClass resultClass = resultObject.getResultClass();
		assertNotNull(resultClass);
		assertEquals(5, resultClass.getFieldCount());

		String[] fieldNames = resultClass.getFieldNames();
		assertNotNull(fieldNames);
		for (int i = 0; i < 5; i++) {
			String fieldName = fieldNames[i];
			Class fieldClass = resultClass.getFieldValueClass(fieldName);
			assertNotNull(fieldClass);

			switch (i) {
			case 0:
				assertEquals("intColumn", fieldName);
				assertEquals(Integer.class, fieldClass);
				break;

			case 1:
				assertEquals("doubleColumn", fieldName);
				assertEquals(Double.class, fieldClass);
				break;

			case 2:
				assertEquals("stringColumn", fieldName);
				assertEquals(String.class, fieldClass);
				break;

			case 3:
				assertEquals("dateColumn", fieldName);
				assertEquals(java.sql.Date.class, fieldClass);
				break;

			case 4:
				assertEquals("decimalColumn", fieldName);
				assertEquals(BigDecimal.class, fieldClass);
				break;
			}
		}

		return resultClass;
	}

	private void testFields(IResultObject resultObject, IResultClass resultClass, int rowPosition)
			throws DataException {
		String[] fieldNames = resultClass.getFieldNames();
		String row = "";
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			assertEquals(resultClass.getFieldName(i), fieldNames[i - 1]);

			Object value = resultObject.getFieldValue(i);
			if (i > 1)
				row += ", ";
			row += (value == null) ? "null" : value.toString();
		}

		assertEquals(RESULTS[rowPosition - 1], row);
	}

	public final void testGetRowPosition() throws DataException {
		int count = 0;
		while (m_resultSet.fetch() != null) {
			count++;
			assertEquals(count, m_resultSet.getRowPosition());
		}
	}

	public final void testRSClose() throws DataException {
		m_resultSet.close();
	}

	public final void testFetchWithHint() throws DataException, Exception {
		// dont' have a test jdbc driver that does not have runtime result set metadata;
		// so use negative test here to check that a native data type gets ignored
		getStatement().close(); // do not use default setup
		// prepare statement

		ColumnHint columnHint = new ColumnHint("intColumn");
		columnHint.setDataType(Integer.class);
		// specify invalid native data type, expects it to be ignored since
		// driver has runtime metadata
		columnHint.setNativeDataType(Types.TIMESTAMP);
		getStatement().addColumnHint(columnHint);

		getStatement().execute();
		m_resultSet = getStatement().getResultSet();

		int rowPosition = 0;
		IResultObject resultObject = null;
		IResultClass resultClass = null;
		while ((resultObject = m_resultSet.fetch()) != null) {
			rowPosition++;
			if (rowPosition == 1) // check the ResultClass the first time
			{
				resultClass = testResultClass(resultObject);
			}

			testFields(resultObject, resultClass, rowPosition);
		}
	}
}
