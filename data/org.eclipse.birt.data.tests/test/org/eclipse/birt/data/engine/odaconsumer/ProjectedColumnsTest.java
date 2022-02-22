/*
 * ****************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-2.0.html
 *
 * Contributors: Actuate Corporation - initial API and implementation
 *
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Properties;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.DataResourceHandle;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;
import org.junit.Before;
import org.junit.Test;

import testutil.JDBCOdaDataSource;

public class ProjectedColumnsTest extends ConnectionTest {

	private static String[] RESULTS = { "blah blah blah, 1.212312", "hahahahahahhahaha, 3.14", "niem, 1.23",
			"null, null", "seven zero six, 12.3636" };

	private static Double[] DOUBLE_RESULTS = { new Double(1.212312), new Double(3.14), new Double(1.23), null,
			new Double(12.3636) };

	private PreparedStatement m_statement;

	private DataResourceHandle resourceHandle = DataResourceHandle.getInstance();

	@Before
	public void projectedColumnsSetUp() throws Exception {
		String command = "select * from \"testtable\"";
		m_statement = getConnection().prepareStatement(command, JDBCOdaDataSource.DATA_SET_TYPE);
	}

	public final void testProjectedColumns() throws Exception {
		String[] projectedColumns = { "stringColumn", "doubleColumn" };

		m_statement.setColumnsProjection(projectedColumns);
		IResultClass metadata = m_statement.getMetaData();

		checkMetaData(metadata);

		assertTrue(m_statement.execute());
		ResultSet resultset = m_statement.getResultSet();
		assertNotNull(resultset);

		assertEquals(metadata, m_statement.getMetaData());
		assertEquals(metadata, resultset.getMetaData());

		IResultObject resultObject = null;
		int count = 1;
		while ((resultObject = resultset.fetch()) != null) {
			assertEquals(metadata, resultObject.getResultClass());

			testFields(resultObject, metadata, count, false);
			testFields(resultObject, metadata, count, true);
			count++;
		}
	}

	public final void testProjectedColumnValidation() throws Exception {
		try {
			String[] projectedColumns = { "MadeUpColumn" };
			m_statement.setColumnsProjection(projectedColumns);
			m_statement.getMetaData();
			fail();
		} catch (DataException ex) {
			assertEquals("Unrecognized projected column name: MadeUpColumn.", ex.getMessage());
		}
	}

	public final void testAlias() throws Exception {
		String[] projectedColumns = { "stringColumn", "doubleColumn" };

		ColumnHint columnHint = new ColumnHint("doubleColumn");
		columnHint.setAlias("Column2");
		m_statement.addColumnHint(columnHint);

		m_statement.setColumnsProjection(projectedColumns);
		IResultClass metadata = m_statement.getMetaData();

		checkMetaData(metadata);

		assertEquals(2, metadata.getFieldIndex("Column2"));
		assertEquals(Double.class, metadata.getFieldValueClass("Column2"));

		assertTrue(m_statement.execute());
		ResultSet resultset = m_statement.getResultSet();
		assertNotNull(resultset);

		IResultObject resultObject = null;
		int count = 1;
		while ((resultObject = resultset.fetch()) != null) {
			assertEquals(metadata, resultObject.getResultClass());

			testFields(resultObject, metadata, count, false);
			testFields(resultObject, metadata, count, true);

			Object value = resultObject.getFieldValue("Column2");
			assertEquals(DOUBLE_RESULTS[count - 1], value);

			count++;
		}
	}

	@Test
	public void testAliasValidation1() throws Exception {
		// ok to set the alias to the same name as the column name
		ColumnHint columnHint = new ColumnHint("doubleColumn");
		columnHint.setAlias("doubleColumn");
		m_statement.addColumnHint(columnHint);
		m_statement.getMetaData();
	}

	@Test
	public void testAliasValidation2() throws Exception {
		// not ok to set the alias as some other column's name
		try {
			ColumnHint columnHint = new ColumnHint("doubleColumn");
			columnHint.setAlias("stringColumn");
			m_statement.addColumnHint(columnHint);
		} catch (DataException ex) {
			String msg = resourceHandle.getMessage(ResourceConstants.COLUMN_NAME_OR_ALIAS_ALREADY_USED,
					new Object[] { "stringColumn", new Integer(3) });
			assertEquals(msg, ex.getMessage());
		}
	}

	@Test
	public void testAliasValidation3() throws Exception {
		// not ok to set the alias as some other column's alias
		try {
			ColumnHint columnHint = new ColumnHint("doubleColumn");
			columnHint.setAlias("MyColumn");
			m_statement.addColumnHint(columnHint);

			columnHint = new ColumnHint("stringColumn");
			columnHint.setAlias("MyColumn");
			m_statement.addColumnHint(columnHint);
		} catch (DataException ex) {
			String msg = resourceHandle.getMessage(ResourceConstants.COLUMN_NAME_OR_ALIAS_ALREADY_USED,
					new Object[] { "MyColumn", new Integer(2) });
			assertEquals(msg, ex.getMessage());
		}
	}

	private void checkMetaData(IResultClass metadata) throws DataException {
		assertEquals(2, metadata.getFieldCount());
		String[] fieldNames = metadata.getFieldNames();

		assertEquals("stringColumn", fieldNames[0]);
		assertEquals("doubleColumn", fieldNames[1]);

		assertEquals("stringColumn", metadata.getFieldName(1));
		assertEquals("doubleColumn", metadata.getFieldName(2));

		assertEquals(1, metadata.getFieldIndex("stringColumn"));
		assertEquals(2, metadata.getFieldIndex("doubleColumn"));

		assertEquals(String.class, metadata.getFieldValueClass(1));
		assertEquals(Double.class, metadata.getFieldValueClass(2));

		assertEquals(String.class, metadata.getFieldValueClass("stringColumn"));
		assertEquals(Double.class, metadata.getFieldValueClass("doubleColumn"));

		assertEquals("stringColumn", metadata.getFieldLabel(1));
		assertEquals("doubleColumn", metadata.getFieldLabel(2));
	}

	private void testFields(IResultObject resultObject, IResultClass resultClass, int rowPosition, boolean useFieldName)
			throws DataException {
		StringBuilder row = new StringBuilder();
		for (int i = 1; i <= resultClass.getFieldCount(); i++) {
			Object value = null;

			if (!useFieldName) {
				value = resultObject.getFieldValue(i);
			} else {
				value = (i == 1) ? resultObject.getFieldValue("stringColumn")
						: resultObject.getFieldValue("doubleColumn");
			}

			if (i > 1) {
				row.append(", ");
			}
			row.append((value == null) ? "null" : value.toString());
		}
		assertEquals(RESULTS[rowPosition - 1], row.toString());
	}

	public final void testGetAllDataByAlias() throws Exception {
		ColumnHint columnHint = new ColumnHint("doubleColumn");
		columnHint.setAlias("Column2");
		m_statement.addColumnHint(columnHint);

		columnHint = new ColumnHint("intColumn");
		columnHint.setAlias("Column1");
		m_statement.addColumnHint(columnHint);

		columnHint = new ColumnHint("stringColumn");
		columnHint.setAlias("Column3");
		m_statement.addColumnHint(columnHint);

		columnHint = new ColumnHint("dateColumn");
		columnHint.setAlias("Column4");
		m_statement.addColumnHint(columnHint);

		columnHint = new ColumnHint("decimalColumn");
		columnHint.setAlias("Column5");
		m_statement.addColumnHint(columnHint);

		assertTrue(m_statement.execute());
		ResultSet resultset = m_statement.getResultSet();
		assertNotNull(resultset);

		IResultObject resultObject = null;
		int count = 0;
		while ((resultObject = resultset.fetch()) != null) {
			Object value1 = resultObject.getFieldValue("Column1");
			Object value2 = resultObject.getFieldValue("Column2");
			Object value3 = resultObject.getFieldValue("Column3");
			Object value4 = resultObject.getFieldValue("Column4");
			Object value5 = resultObject.getFieldValue("Column5");
			String row = value1 + ", " + value2 + ", " + value3 + ", " + value4 + ", " + value5;
			assertEquals(row, ResultSetTest.RESULTS[count++]);
		}
	}

	@Test
	public void testChangeMetadataWithNewProjection() throws Exception {
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);

		assertTrue(m_statement.execute());
		ResultSet resultSet = m_statement.getResultSet();
		assertSame(metadata, resultSet.getMetaData());

		String[] projectedColumns = { "stringColumn", "doubleColumn" };
		m_statement.setColumnsProjection(projectedColumns);
		IResultClass newMetadata1 = m_statement.getMetaData();
		assertNotNull(newMetadata1);
		assertFalse(metadata.equals(newMetadata1));
		checkMetaData(newMetadata1);

		ColumnHint columnHint = new ColumnHint("doubleColumn");
		columnHint.setAlias("Column2");
		columnHint.setDataType(String.class); // should have no impact
		m_statement.addColumnHint(columnHint);

		IResultClass newMetadata2 = m_statement.getMetaData();
		assertNotNull(newMetadata2);
		assertFalse(metadata.equals(newMetadata2));
		assertSame(newMetadata1, newMetadata2);

		checkMetaData(newMetadata2);

		assertTrue(m_statement.execute());
		resultSet = m_statement.getResultSet();
		assertSame(newMetadata2, resultSet.getMetaData());
	}

	@Test
	public void testChangeMetadataWithAdditionalCustomColumns() throws Exception {
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(5, metadata.getFieldCount());

		// added a custom column, expect to get a new metadata with 6
		// columns instead of 5
		m_statement.declareCustomColumn("MyColumn", BigDecimal.class);
		metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(6, metadata.getFieldCount());

		assertEquals(6, metadata.getFieldIndex("MyColumn"));
		assertEquals(BigDecimal.class, metadata.getFieldValueClass(6));
		assertEquals(BigDecimal.class, metadata.getFieldValueClass("MyColumn"));
		assertEquals("MyColumn", metadata.getFieldLabel(6));
		assertEquals("MyColumn", metadata.getFieldName(6));
		assertTrue(metadata.isCustomField(6));
		assertTrue(metadata.isCustomField("MyColumn"));
	}

	@Test
	public void testCustomColumnValidation1() throws Exception {
		// can't declare a custom column with same name as existing column name
		try {
			m_statement.declareCustomColumn("intColumn", Integer.class);
		} catch (DataException ex) {
			String msg = resourceHandle.getMessage(ResourceConstants.COLUMN_NAME_OR_ALIAS_ALREADY_USED,
					new Object[] { "intColumn", new Integer(1) });
			assertEquals(msg, ex.getMessage());
		}
	}

	@Test
	public void testCustomColumnValidation2() throws Exception {
		// can't declare a custom column with same name as existing alias
		try {
			ColumnHint columnHint = new ColumnHint("decimalColumn");
			columnHint.setAlias("My Decimal Column");
			m_statement.addColumnHint(columnHint);

			m_statement.declareCustomColumn("My Decimal Column", Integer.class);
		} catch (DataException ex) {
			String msg = resourceHandle.getMessage(ResourceConstants.COLUMN_NAME_OR_ALIAS_ALREADY_USED,
					new Object[] { "My Decimal Column", new Integer(5) });
			assertEquals(msg, ex.getMessage());
		}
	}

	@Test
	public void testCustomColumnWithProjection1() throws Exception {
		// custom columns should show up in the ResultClass even it isn't
		// projected
		String[] projectedColumns = { "stringColumn", "doubleColumn" };
		m_statement.setColumnsProjection(projectedColumns);
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(2, metadata.getFieldCount());

		m_statement.declareCustomColumn("My Decimal Column", BigDecimal.class);
		metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(3, metadata.getFieldCount());
		assertEquals(3, metadata.getFieldIndex("My Decimal Column"));
	}

	@Test
	public void testCustomColumnWithProjection2() throws Exception {
		// make it work even if the caller projects a custom column
		String[] projectedColumns = { "stringColumn", "My Decimal Column", "doubleColumn" };

		m_statement.declareCustomColumn("My Decimal Column", null);
		m_statement.setColumnsProjection(projectedColumns);
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(3, metadata.getFieldCount());

		// make sure that the custom column still appears at the end as
		// if it weren't projected
		assertEquals(3, metadata.getFieldIndex("My Decimal Column"));
	}

	@Test
	public void testWithClearParameters1() throws Exception {
		doTestWithClearParameters(getConnection());
	}

	@Test
	public void testWithClearParameters2() throws Exception {
		Properties connProperties = getJdbcConnProperties();
		Connection connection = getManager().openConnection(JDBCOdaDataSource.DATA_SOURCE_TYPE, connProperties, null); // no
																														// appContext

		// jdbc1 contains the oda-jdbc driver that doesn't implement
		// clearParameterValues()
		doTestWithClearParameters(connection);
	}

	private void doTestWithClearParameters(Connection connection) throws Exception {
		String command = "select * from \"testtable\" where \"stringColumn\" > ? OR \"stringColumn\" IS NULL";
		m_statement = connection.prepareStatement(command, JDBCOdaDataSource.DATA_SET_TYPE);
		String[] projectedColumns = { "stringColumn", "My Decimal Column", "doubleColumn" };

		// custom columns, column projection, and column hint set only once,
		// clearParameterValues() shouldn't lose this information
		m_statement.declareCustomColumn("My Decimal Column", null);
		m_statement.setColumnsProjection(projectedColumns);
		ColumnHint columnHint = new ColumnHint("doubleColumn");
		columnHint.setAlias("My Double Column");
		m_statement.addColumnHint(columnHint);

		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		assertEquals(3, metadata.getFieldCount());
		m_statement.setParameterValue(1, "c");

		assertTrue(m_statement.execute());
		ResultSet resultset = m_statement.getResultSet();
		assertNotNull(resultset);

		IResultObject resultObject = null;
		int index = 1;
		while ((resultObject = resultset.fetch()) != null) {
			Object value = resultObject.getFieldValue("My Double Column");
			assertEquals(DOUBLE_RESULTS[index], value);

			index++;
		}

		m_statement.clearParameterValues();
		metadata = m_statement.getMetaData();
		assertEquals(3, metadata.getFieldCount());
		m_statement.setParameterValue(1, "i");

		assertTrue(m_statement.execute());
		resultset = m_statement.getResultSet();
		assertNotNull(resultset);

		resultObject = null;
		index = 2;
		while ((resultObject = resultset.fetch()) != null) {
			Object value = resultObject.getFieldValue("My Double Column");
			assertEquals(DOUBLE_RESULTS[index], value);

			index++;
		}
	}

	// test changing the data type of a projected column
	@Test
	public void testChangeColumnTypeWithHint() throws Exception {
		String customColumnName = "My null Column";
		m_statement.declareCustomColumn(customColumnName, null);
		IResultClass metadata = m_statement.getMetaData();
		Class fieldType = metadata.getFieldValueClass(customColumnName);
		assertNotNull(fieldType);
		// default to String type
		assertEquals(String.class, fieldType);

		// change the custom column type via a ColumnHint
		ColumnHint columnHint = new ColumnHint(customColumnName);
		columnHint.setDataType(Integer.class);
		m_statement.addColumnHint(columnHint);

		metadata = m_statement.getMetaData();
		fieldType = metadata.getFieldValueClass(customColumnName);
		assertNotNull(fieldType);
		assertEquals(Integer.class, fieldType);

		// change the custom column type again via a ColumnHint
		columnHint = new ColumnHint(customColumnName);
		columnHint.setDataType(Double.class);
		m_statement.addColumnHint(columnHint);

		metadata = m_statement.getMetaData();
		fieldType = metadata.getFieldValueClass(customColumnName);
		assertEquals(Double.class, fieldType);

		// change the custom column type again via a ColumnHint
		columnHint = new ColumnHint(customColumnName);
		columnHint.setDataType(java.sql.Date.class);
		m_statement.addColumnHint(columnHint);

		metadata = m_statement.getMetaData();
		fieldType = metadata.getFieldValueClass(customColumnName);
		assertEquals(java.sql.Date.class, fieldType);

		// change the custom column type again via a ColumnHint
		columnHint = new ColumnHint(customColumnName);
		columnHint.setDataType(Boolean.class);
		m_statement.addColumnHint(columnHint);

		metadata = m_statement.getMetaData();
		fieldType = metadata.getFieldValueClass(customColumnName);
		assertEquals(Boolean.class, fieldType);
	}

	@Test
	public void testChangeColumnTypeWithNativeTypeHint() throws Exception {
		String customColumnName = "My custom Column";
		m_statement.declareCustomColumn(customColumnName, null);
		IResultClass metadata = m_statement.getMetaData();
		Class fieldType = metadata.getFieldValueClass(customColumnName);
		assertNotNull(fieldType);
		// default to String type
		assertEquals(String.class, fieldType);

		// change the custom column type via a ColumnHint
		ColumnHint columnHint = new ColumnHint(customColumnName);
		columnHint.setDataType(Integer.class);
		columnHint.setNativeDataType(Types.DATE);
		m_statement.addColumnHint(columnHint);

		metadata = m_statement.getMetaData();
		fieldType = metadata.getFieldValueClass(customColumnName);
		assertNotNull(fieldType);
		assertEquals(java.sql.Date.class, fieldType); // maps from native type
	}

}
