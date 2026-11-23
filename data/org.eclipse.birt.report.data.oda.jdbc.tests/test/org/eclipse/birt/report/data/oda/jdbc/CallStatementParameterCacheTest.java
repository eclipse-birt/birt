/*******************************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors: See git history
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IParameterMetaData;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.junit.Before;
import org.junit.Test;

/**
 * Test case for the parameters of a stored procedure call that are kept for the
 * life of the ODA connection. A data set bound to a table row runs once per
 * row and must not look the procedure up in the database dictionary every time.
 */
public class CallStatementParameterCacheTest {

	private static final String SP_DATA_SET = "org.eclipse.birt.report.data.oda.jdbc.SPSelectDataSet";
	private static final String CALL = "{call GetPartDetails(?,?)}";

	private static final Object[] IN_PART_NO = { "P_PARTNO", DatabaseMetaData.procedureColumnIn, Types.NUMERIC,
			"NUMBER" };
	private static final Object[] OUT_WEIGHT = { "P_WEIGHT", DatabaseMetaData.procedureColumnOut, Types.VARCHAR,
			"VARCHAR2" };

	/** the lookups made on all connections, as "catalog|schema|procedure" */
	private final List<String> lookups = new ArrayList<>();
	/** the parameters the database returns, empty while the procedure is not deployed */
	private List<Object[]> parameters = new ArrayList<>();
	private String currentSchema = "APP_SCHEMA";
	/** thrown by getSchema when set */
	private Throwable schemaLookupFailure = null;
	/** the result set fails after this many rows when not negative */
	private int failAfterRows = -1;
	/** the database only knows the procedure inside a package, as Oracle does */
	private boolean procedureOnlyInPackage = false;
	private int closedConnections = 0;

	@Before
	public void setUp() {
		lookups.clear();
		parameters = new ArrayList<>(Arrays.asList(IN_PART_NO, OUT_WEIGHT));
		currentSchema = "APP_SCHEMA";
		schemaLookupFailure = null;
		failAfterRows = -1;
		procedureOnlyInPackage = false;
		closedConnections = 0;
	}

	@Test
	public void testSecondStatementOnTheSameConnectionDoesNotLookUpAgain() throws Exception {
		IConnection connection = openConnection();

		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(1, lookups.size());
	}

	@Test
	public void testStatementsOfOneConnectionShareTheCacheEvenWhenOpenAtTheSameTime() throws Exception {
		IConnection connection = openConnection();
		IQuery first = connection.newQuery(SP_DATA_SET);
		IQuery second = connection.newQuery(SP_DATA_SET);
		first.prepare(CALL);
		second.prepare(CALL);

		assertEquals(2, first.getParameterMetaData().getParameterCount());
		assertEquals(2, second.getParameterMetaData().getParameterCount());

		assertEquals(1, lookups.size());
	}

	@Test
	public void testAnotherConnectionLooksUpAgain() throws Exception {
		assertEquals(2, parameterCount(openConnection(), CALL));
		assertEquals(2, parameterCount(openConnection(), CALL));

		assertEquals(2, lookups.size());
	}

	@Test
	public void testClosedAndReopenedConnectionLooksUpAgain() throws Exception {
		Map<String, Object> appContext = new HashMap<>();
		IConnection connection = openConnection(appContext);
		assertEquals(2, parameterCount(connection, CALL));

		connection.close();
		assertEquals(1, closedConnections);
		connection.open(new Properties());
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(2, lookups.size());
	}

	@Test
	public void testConnectionThatStaysOpenAfterCloseLooksUpAgain() throws Exception {
		Map<String, Object> appContext = new HashMap<>();
		appContext.put(IConnectionFactory.CLOSE_PASS_IN_CONNECTION, Boolean.FALSE);
		IConnection connection = openConnection(appContext);
		assertEquals(2, parameterCount(connection, CALL));

		connection.close();
		assertEquals(0, closedConnections);
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(2, lookups.size());
	}

	@Test
	public void testChangedCurrentSchemaLooksUpAgain() throws Exception {
		IConnection connection = openConnection();

		assertEquals(2, parameterCount(connection, CALL));
		currentSchema = "OTHER_SCHEMA";
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, lookups.size());

		// the result for the first schema is still known
		currentSchema = "APP_SCHEMA";
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, lookups.size());
	}

	@Test
	public void testDriverThatCannotTellTheSchemaStillCaches() throws Exception {
		for (Throwable failure : new Throwable[] { new java.sql.SQLException("no schema"),
				new UnsupportedOperationException("no schema"), new AbstractMethodError("no schema") }) {
			lookups.clear();
			schemaLookupFailure = failure;
			IConnection connection = openConnection();

			assertEquals(failure.toString(), 2, parameterCount(connection, CALL));
			assertEquals(failure.toString(), 2, parameterCount(connection, CALL));

			assertEquals(failure.toString(), 1, lookups.size());
		}
	}

	@Test
	public void testReopenedConnectionWithAnotherJdbcConnectionLooksUpAgain() throws Exception {
		Map<String, Object> appContext = new HashMap<>();
		IConnection connection = openConnection(appContext);
		assertEquals(2, parameterCount(connection, CALL));

		appContext.put(IConnectionFactory.PASS_IN_CONNECTION, jdbcConnection());
		connection.open(new Properties());
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(2, lookups.size());
	}

	@Test
	public void testFailedLookupDoesNotKeepThePartialList() throws Exception {
		failAfterRows = 1;
		IConnection connection = openConnection();

		// the dictionary fails after the first of two parameters
		assertNoParameters(connection);
		assertEquals(1, lookups.size());

		failAfterRows = -1;
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, lookups.size());
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, lookups.size());
	}

	@Test
	public void testPackageProcedureIsFoundByTheRetryAndKept() throws Exception {
		procedureOnlyInPackage = true;
		IConnection connection = openConnection();
		String call = "{call MY_PACKAGE.GetPartDetails(?,?)}";

		assertEquals(2, parameterCount(connection, call));
		int lookupsForTheFirstCall = lookups.size();
		assertEquals(2, lookupsForTheFirstCall);

		assertEquals(2, parameterCount(connection, call));
		assertEquals(2, parameterCount(connection, call));
		assertEquals(lookupsForTheFirstCall, lookups.size());
	}

	@Test
	public void testBidiCallStatementsShareTheCacheToo() throws Exception {
		Properties connectionProperties = new Properties();
		connectionProperties.put("contentBidiFormatStr", "ILYNN");
		Map<String, Object> appContext = new HashMap<>();
		appContext.put("connectionProperties", connectionProperties);
		IConnection connection = openConnection(appContext);

		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(1, lookups.size());
	}

	@Test
	public void testConnectionWithoutSchemaStillCaches() throws Exception {
		currentSchema = null;
		IConnection connection = openConnection();

		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(1, lookups.size());
	}

	@Test
	public void testDifferentProceduresAreLookedUpSeparately() throws Exception {
		IConnection connection = openConnection();

		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, "{call OtherProcedure(?,?)}"));
		assertEquals(2, parameterCount(connection, "{call OTHER_SCHEMA.GetPartDetails(?,?)}"));
		assertEquals(2, parameterCount(connection, CALL));

		assertEquals(3, lookups.size());
	}

	@Test
	public void testEmptyResultIsNotKept() throws Exception {
		parameters.clear();
		IConnection connection = openConnection();

		assertNoParameters(connection);
		assertNoParameters(connection);
		assertEquals(2, lookups.size());

		// the procedure is deployed while the report runs
		parameters.add(IN_PART_NO);
		parameters.add(OUT_WEIGHT);
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(2, parameterCount(connection, CALL));
		assertEquals(3, lookups.size());
	}

	private void assertNoParameters(IConnection connection) throws OdaException {
		try {
			parameterCount(connection, CALL);
			fail("The parameters of the procedure cannot be found");
		} catch (OdaException e) {
			// expected
		}
	}

	private int parameterCount(IConnection connection, String call) throws OdaException {
		IQuery query = connection.newQuery(SP_DATA_SET);
		query.prepare(call);
		IParameterMetaData metaData = query.getParameterMetaData();
		return metaData.getParameterCount();
	}

	private IConnection openConnection() throws OdaException {
		return openConnection(new HashMap<>());
	}

	/** an ODA connection on top of a stub JDBC connection, handed in through the app context */
	private IConnection openConnection(Map<String, Object> appContext) throws OdaException {
		appContext.put(IConnectionFactory.PASS_IN_CONNECTION, jdbcConnection());
		IConnection connection = new org.eclipse.birt.report.data.oda.jdbc.Connection();
		connection.setAppContext(appContext);
		connection.open(new Properties());
		return connection;
	}

	private Connection jdbcConnection() {
		CallableStatement callable = proxy(CallableStatement.class,
				(p, method, args) -> defaultValue(method.getReturnType()));
		DatabaseMetaData metaData = proxy(DatabaseMetaData.class, (p, method, args) -> {
			switch (method.getName()) {
			case "getIdentifierQuoteString":
				return "\"";
			case "getSearchStringEscape":
				return "\\";
			case "storesUpperCaseIdentifiers":
			case "storesUpperCaseQuotedIdentifiers":
				return true;
			case "getProcedureColumns":
				return procedureColumns(args);
			default:
				return defaultValue(method.getReturnType());
			}
		});
		return proxy(Connection.class, (p, method, args) -> {
			switch (method.getName()) {
			case "getMetaData":
				return metaData;
			case "prepareCall":
				return callable;
			case "getAutoCommit":
				return true;
			case "close":
				closedConnections++;
				return null;
			case "getSchema":
				if (schemaLookupFailure instanceof java.sql.SQLException) {
					throw (java.sql.SQLException) schemaLookupFailure;
				}
				if (schemaLookupFailure instanceof RuntimeException) {
					throw (RuntimeException) schemaLookupFailure;
				}
				if (schemaLookupFailure instanceof Error) {
					throw (Error) schemaLookupFailure;
				}
				return currentSchema;
			default:
				return defaultValue(method.getReturnType());
			}
		});
	}

	private ResultSet procedureColumns(Object[] args) {
		lookups.add(args[0] + "|" + args[1] + "|" + args[2]);
		List<Object[]> rows = new ArrayList<>(parameters);
		if (procedureOnlyInPackage && args[0] == null) {
			rows.clear();
		}
		int[] position = { -1 };
		return proxy(ResultSet.class, (p, method, a) -> {
			switch (method.getName()) {
			case "next":
				if (failAfterRows >= 0 && position[0] + 1 >= failAfterRows) {
					throw new java.sql.SQLException("dictionary lookup failed");
				}
				return ++position[0] < rows.size();
			case "getString":
				return column(rows.get(position[0]), (String) a[0]);
			case "getInt":
				Object value = column(rows.get(position[0]), (String) a[0]);
				return value == null ? 0 : value;
			default:
				return defaultValue(method.getReturnType());
			}
		});
	}

	private static Object column(Object[] row, String name) {
		switch (name) {
		case "COLUMN_NAME":
			return row[0];
		case "COLUMN_TYPE":
			return row[1];
		case "DATA_TYPE":
			return row[2];
		case "TYPE_NAME":
			return row[3];
		default:
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private static <T> T proxy(Class<T> type, InvocationHandler handler) {
		return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class<?>[] { type }, handler);
	}

	private static Object defaultValue(Class<?> type) {
		if (type == boolean.class) {
			return false;
		}
		if (type == int.class) {
			return 0;
		}
		if (type == long.class) {
			return 0L;
		}
		return null;
	}
}
