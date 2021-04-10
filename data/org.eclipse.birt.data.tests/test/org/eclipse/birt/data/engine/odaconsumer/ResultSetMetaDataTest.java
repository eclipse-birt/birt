/*
 * ****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *  Actuate Corporation - initial API and implementation
 * 
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.math.BigDecimal;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;

import org.junit.Before;
import static org.junit.Assert.*;

public class ResultSetMetaDataTest extends QueryTest {

	private IResultClass m_metadata;

	@Before
	public void resultSetMetaDataSetUp() throws Exception {
		m_metadata = getStatement().getMetaData();
	}

	public final void testGetColumnCount() {
		checkColumnCount(m_metadata);
	}

	public final void testGetColumnCount1() throws DataException {
		IResultClass metadata = getMetaDataFromResult();
		checkColumnCount(metadata);
	}

	public final void testGetColumnName() throws Exception {
		checkColumnNames(m_metadata);
	}

	public final void testGetColumnName1() throws Exception {
		IResultClass metadata = getMetaDataFromResult();

		checkColumnNames(metadata);
	}

	public final void testGetColumnType() throws Exception {
		checkColumnTypes(m_metadata);
	}

	public final void testGetColumnType1() throws Exception {
		IResultClass metadata = getMetaDataFromResult();

		checkColumnTypes(metadata);
	}

	private IResultClass getMetaDataFromResult() throws DataException {
		getStatement().execute();
		ResultSet resultset = getStatement().getResultSet();
		IResultClass metadata = resultset.getMetaData();
		return metadata;
	}

	private void checkColumnCount(IResultClass metadata) {
		assertEquals(5, metadata.getFieldCount());
	}

	private void checkColumnNames(IResultClass metadata) throws Exception {
		for (int i = 1, n = metadata.getFieldCount(); i <= n; i++) {
			String colName = metadata.getFieldName(i);
			switch (i) {
			case 1:
				assertEquals("intColumn", colName);
				break;

			case 2:
				assertEquals("doubleColumn", colName);
				break;

			case 3:
				assertEquals("stringColumn", colName);
				break;

			case 4:
				assertEquals("dateColumn", colName);
				break;

			case 5:
				assertEquals("decimalColumn", colName);
				break;

			default:
				assertTrue(false);
			}
		}
	}

	private void checkColumnTypes(IResultClass metadata) throws Exception {
		for (int i = 1, n = metadata.getFieldCount(); i <= n; i++) {
			Class colType = metadata.getFieldValueClass(i);
			switch (i) {
			case 1:
				assertEquals(Integer.class, colType);
				break;

			case 2:
				assertEquals(Double.class, colType);
				break;

			case 3:
				assertEquals(String.class, colType);
				break;

			case 4:
				assertEquals(java.sql.Date.class, colType);
				break;

			case 5:
				assertEquals(BigDecimal.class, colType);
				break;

			default:
				assertTrue(false);
			}
		}
	}
}
