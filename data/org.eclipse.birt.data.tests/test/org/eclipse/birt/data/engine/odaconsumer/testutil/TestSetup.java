/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.data.engine.odaconsumer.testutil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class TestSetup {
	private static String url = "jdbc:derby:DtETest";
	private static String username = "sa";
	private static String password = "sa";
	private static String driverclass = "org.apache.derby.jdbc.EmbeddedDriver";

	public final static String PROCEDURE_NAME_PREFIX = "testOdaConsmr";
	public final static String PROCEDURE_PARAM_NAME_PREFIX = "paramNativeName";
	private final static String PROCEDURE_ELEMENTS = "PARAMETER STYLE JAVA READS SQL DATA LANGUAGE JAVA";
	private final static String PROCEDURE_SET_INPUT =
//	    " EXTERNAL NAME 'org.eclipse.birt.data.engine.odaconsumer.util.TestSetup.setInputParamData'";
			" EXTERNAL NAME 'org.eclipse.birt.data.engine.api.StoredProcedureTest.selectData' ";
	private final static String PROCEDURE_SET_INOUT = " EXTERNAL NAME 'org.eclipse.birt.data.engine.odaconsumer.util.TestSetup.selectInOutParamData'";

	public static void createTestTable() throws Exception {
		Class.forName(driverclass); // load the specified driver

		Connection connection = DriverManager.getConnection(url + ";create=true", username, password);
		Statement statement = connection.createStatement();
		try {
			statement.execute("drop table \"testtable\"");
			statement.execute("drop table \"testtable_lob\"");
		} catch (SQLException e) {
		}
		statement.execute("CREATE TABLE \"testtable\"( \"intColumn\" INT, \"doubleColumn\" FLOAT(52), "
				+ "\"stringColumn\" VARCHAR(50), \"dateColumn\" DATE, \"decimalColumn\" DECIMAL(18,0) )");
		statement
				.execute("INSERT INTO \"testtable\" VALUES( 123, 1.212312, 'blah blah blah', " + "'2000-09-01', 600 )");
		statement.execute("INSERT INTO \"testtable\" VALUES( 14, 3.14, 'hahahahahahhahaha', " + "'1991-10-02', 10 )");
		statement.execute("INSERT INTO \"testtable\" VALUES( 0, 1.23, 'niem', " + "'1979-11-28', 10 )");
		statement.execute("INSERT INTO \"testtable\" VALUES( NULL, NULL, NULL, " + "NULL, NULL )");
		statement.execute("INSERT INTO \"testtable\" VALUES( 4, 12.3636, 'seven zero six', " + "'2004-01-01', 10000 )");

		statement.execute("CREATE TABLE \"testtable_lob\"( \"blob1\" BLOB, \"clob1\" CLOB )");
		statement.execute("INSERT INTO \"testtable_lob\" VALUES( NULL, 'abcdefg' )");

		statement.close();
		connection.close();
	}

	public static void createTestStoredProcedures() throws Exception {
		Class.forName(driverclass); // load the specified driver

		Connection connection = DriverManager.getConnection(url + ";create=true", username, password);
		Statement statement = connection.createStatement();
		String sqlText;
		try {
			sqlText = "drop procedure " + PROCEDURE_NAME_PREFIX + "In";
			statement.execute(sqlText);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sqlText = "CREATE PROCEDURE " + PROCEDURE_NAME_PREFIX + "In" + " (IN " + PROCEDURE_PARAM_NAME_PREFIX
				+ "1 INTEGER "
//            + " ,IN " + PROCEDURE_PARAM_NAME_PREFIX + "2 VARCHAR(10) "
//            + " ,OUT paramNativeName2 varchar(10) "
				+ " ) " + PROCEDURE_ELEMENTS + PROCEDURE_SET_INPUT;
		try {
			statement.execute(sqlText);
		} catch (SQLException ex) {
			ex.printStackTrace();
		}

		statement.close();
		connection.close();
	}

	public static void setInputParamData(int inputValue, String outputValue) {

	}

	public static void selectInOutParamData(int inputValue, String[] outputValues) {
		outputValues[0] = (new Integer(inputValue)).toString();
	}

}