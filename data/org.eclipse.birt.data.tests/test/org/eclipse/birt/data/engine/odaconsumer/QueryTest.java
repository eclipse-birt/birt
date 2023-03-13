/*
 * ****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v2.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-2.0.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 * *****************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.datatools.connectivity.oda.SortSpec;
import org.junit.After;
import org.junit.Before;

import testutil.JDBCOdaDataSource;

public class QueryTest extends ConnectionTest {

	private PreparedStatement m_statement;

	PreparedStatement getStatement() {
		return m_statement;
	}

	@Before
	public void querySetUp() throws Exception {
		String command = "select * from \"testtable\"";
		m_statement = getConnection().prepareStatement(command, JDBCOdaDataSource.DATA_SET_TYPE);
	}

	@After
	public void queryTearDown() throws Exception {
		m_statement.close();
	}

	public final void testSortSpecMessages() {
		try {
			new SortSpec(100);
			fail();
		} catch (IllegalArgumentException ex) {
			assertEquals("Invalid sort mode specified: 100.", ex.getMessage());
		}
	}

	public final void testSetProperty() throws DataException {
		// Note that even though oda.jdbd driver supports queryTimeOut property, Derby
		// does
		// not actually support setQueryTimeout. So we can't test that property here

		/// Test with a bad property
		final String badPropertyName = "bad_prop_123";

		try {
			// Try a property not supported by oda.jdbc driver
			m_statement.setProperty(badPropertyName, "propValue");
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			// Expected exception; exception message should contain the bad property name
			assertTrue(ex.getCause().getMessage().indexOf(badPropertyName) >= 0);
		}
	}

	public final void testSetSortSpec() throws DataException {
		try {
			// ODA-JDBC doesn't support sort specifications
			m_statement.setSortSpec(null);
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.CANNOT_SET_SORT_SPEC);
			assertEquals("setSortSpec is not supported.", ex.getCause().getMessage());
		}
	}

	private void doTestSetMaxRows(int max, int expected, boolean setMax) throws DataException {
		if (setMax) {
			m_statement.setMaxRows(max);
		}

		assertTrue(m_statement.execute());
		ResultSet result = m_statement.getResultSet();
		assertNotNull(result);
		int count = 0;
		while (result.fetch() != null) {
			count++;
		}
		assertEquals(expected, count);
	}

	public final void testSetMaxRows0() throws DataException {
		doTestSetMaxRows(0, 5, false /* setMax */);
	}

	public final void testSetMaxRows1() throws DataException {
		doTestSetMaxRows(2, 2, true /* setMax */);
	}

	public final void testSetMaxRows2() throws DataException {
		doTestSetMaxRows(0, 5, true /* setMax */);
	}

	public final void testSetMaxRows3() throws DataException {
		doTestSetMaxRows(10, 5, true /* setMax */);
	}

	public final void testGetRSMetaData() throws DataException {
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
	}

	public final void testGetMetaDataString() throws DataException {
		try {
			// ODA-JDBC driver doesn't support named result sets
			m_statement.getMetaData("someResultName");
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.NAMED_RESULTSETS_UNSUPPORTED);
			assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		}
	}

	public final void testExecute0() throws DataException {
		assertTrue(m_statement.execute());
	}

	public final void testExecute1() {
		try {
			String command = "select * from \"testtable\" where \"intColumn\" = ?";
			PreparedStatement stmt = getConnection().prepareStatement(command, JDBCOdaDataSource.DATA_SET_TYPE);
			stmt.execute();
			assertTrue(false);
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.CANNOT_EXECUTE_STATEMENT);
			// assertEquals( "A JDBC Exception occured: Parameter #1 has not
			// been set.",
			// ex.getCause().getMessage() );
		}
	}

	public final void testExecute2() throws DataException {
		m_statement.execute();
		ResultSet result = m_statement.getResultSet();
		assertNotNull(result);
		IResultClass metadata = m_statement.getMetaData();
		assertNotNull(metadata);
		IResultClass metadata1 = result.getMetaData();
		assertSame(metadata, metadata1);

		int count = 0;
		while (result.fetch() != null) {
			count++;
		}
		assertEquals(5, count);
	}

	public final void testGetResultSet() throws DataException {
		assertTrue(m_statement.execute());
		ResultSet result = m_statement.getResultSet();
		assertNotNull(result);
	}

	public final void testGetResultSetString() throws DataException {
		try {
			// ODA-JDBC driver doesn't support named result sets
			m_statement.execute();
			m_statement.getResultSet("someResultName");
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.NAMED_RESULTSETS_UNSUPPORTED);
			assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		}
	}

	public final void testFindOutParameter() throws DataException {
		try {
			// ODA-JDBC driver doesn't support output parameters right now
			m_statement.findOutParameter("someParamName");
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED);
			assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		}
	}

	public final void testGetParameterTypeInt() {
		try {
			m_statement.getParameterType(1);
		} catch (DataException ex) {
			assertEquals(ResourceConstants.CANNOT_GET_PARAMETER_METADATA, ex.getErrorCode());
			// different driver will return different error msg
			// assertEquals( "A JDBC Exception occured: Invalid parameter index
			// 1.",
			// ex.getCause().getMessage() );
		}
	}

	public final void testGetParameterTypeString() throws DataException {
		boolean hasException = false;
		try {
			m_statement.getParameterType("someParamName");
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.CANNOT_GET_PARAMETER_TYPE);
			hasException = true;
		}
		assertTrue(hasException);
	}

	public final void testGetParameterValueInt() throws DataException {
		try {
			// ODA-JDBC driver doesn't support output parameters right now
			assertTrue(m_statement.execute());
			m_statement.getParameterValue(1);
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED);
			assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		}
	}

	public final void testGetParameterValueString() throws DataException {
		try {
			// ODA-JDBC driver doesn't support output parameters right now
			assertTrue(m_statement.execute());
			m_statement.getParameterValue("someParamName");
			assertTrue(false); // shouldn't get here
		} catch (DataException ex) {
			assertEquals(ex.getErrorCode(), ResourceConstants.OUTPUT_PARAMETERS_UNSUPPORTED);
			assertTrue(ex.getCause() instanceof UnsupportedOperationException);
		}
	}

	public final void testQueryClose() throws DataException {
		m_statement.close();
	}
}
