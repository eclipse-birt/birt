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

package testutil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.DriverPropertyInfo;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Used to create table create stored procedure insert data into table drop
 * table drop stored procedure Property: DTETest.driver DTETest.url
 * DTETest.database DTETest.user DTETest.password
 *
 * DTETest.otherDB (whether derby is not used)
 */
public class JDBCDataSourceUtil {
	private Connection conn;
	private PreparedStatement prepStmt;
	private List tableNameList = new ArrayList();
	private List procedureNameList = new ArrayList();

	/**
	 * Constructor
	 *
	 * @throws Exception
	 */
	JDBCDataSourceUtil() throws Exception {
		conn = createDBConnection();

		System.out.println("Test against JDBC database: url=" + getURL() + " user=" + getUser() + " driverClass="
				+ getDriverClassName());
	}

	/**
	 * Create test table according to the passed arguments
	 *
	 * @param tableName
	 * @param metaInfo
	 * @param dropTable
	 * @throws SQLException
	 */
	public void createTable(String tableName, String createSql, boolean dropTable) throws SQLException {
		if (tableName != null) {
			if (dropTable) {
				dropTable(tableName);
			}
			statementExecute(createSql);
			tableNameList.add(tableName);
		}
	}

	/**
	 * Create test procedure according to the passed arguments
	 *
	 * @param proName
	 * @param metaInfo
	 * @param dropProc
	 * @throws SQLException
	 */
	public void createStoredProcedure(String proName, String createSql, boolean dropProc) throws SQLException {
		if (proName != null) {
			if (dropProc) {
				dropStoredProcedure(proName);
			}
			statementExecute(createSql);
			procedureNameList.add(proName);
		}
	}

	/**
	 * Populate data for test table
	 *
	 * @param testTableName
	 * @param testTableDataFile
	 * @throws SQLException
	 * @throws IOException
	 */
	public void populateTable(String testTableName, InputStream testTableDataFile) throws SQLException, IOException {
		InputStreamReader inputFile = new InputStreamReader(testTableDataFile);
		BufferedReader lineReader = new BufferedReader(inputFile);
		// skip first two lines which store metadata information
		lineReader.readLine();
		String metaData = lineReader.readLine();
		String str;

		while ((str = lineReader.readLine()) != null) {
			// skip comment line
			if (str.charAt(0) == '#') {
				continue;
			}
			String sql = "insert into " + testTableName + " values(" + str + ")";

			// blob support in sql statement
			boolean isBlob = false;
			String[] dataType = metaData.split(",");
			for (int i = 0; i < dataType.length; i++) {
				dataType[i] = dataType[i].trim();
				if (dataType[i].toUpperCase().equals("BLOB")) {
					isBlob = true;
					break;
				}
			}

			if (!isBlob) {
				statementExecute(sql);
			} else {
				Object[] ob = getPreparedData(testTableName, dataType, str);
				statementExecute((String) ob[0], (Object[]) ob[1]);
			}
		}

		lineReader.close();
		inputFile.close();
		testTableDataFile.close();
	}

	/**
	 * Used to insert Blob data
	 *
	 * @return Object[], SQL statement and data object array
	 */
	private Object[] getPreparedData(String testTableName, String[] dataType, String dataStr) {
		StringBuilder replaceStr = new StringBuilder();
		for (int i = 0; i < dataType.length; i++) {
			replaceStr.append("?");
			if (i < dataType.length - 1) {
				replaceStr.append(",");
			}
		}
		String insertSql = "insert into " + testTableName + " values(" + replaceStr.append(")").toString();
		Object[] value = new Object[dataType.length];

		String[] data = dataStr.split(",");
		for (int i = 0; i < dataType.length; i++) {
			data[i] = data[i].trim();

			if (dataType[i].toUpperCase().equals("INT")) {
				value[i] = Integer.valueOf(data[i]);
			} else if (dataType[i].toUpperCase().equals("CLOB")) {
				value[i] = data[i];
			} else if (dataType[i].toUpperCase().equals("BLOB")) {
				value[i] = data[i].toString().getBytes();
			} else {
				throw new IllegalArgumentException("");
			}
		}

		return new Object[] { insertSql, value };
	}

	/**
	 * Drop test table
	 *
	 * @param tableName
	 * @throws SQLException
	 */
	public void dropTable(String tableName) {
		String sql = "drop table " + tableName;
		try {
			statementExecute(sql);
			tableNameList.remove(tableName);
		} catch (SQLException e) {
			// Assume table does not exist
		}
	}

	/**
	 * Drop test procedure
	 *
	 * @param proName
	 * @throws SQLException
	 */
	public void dropStoredProcedure(String proName) {
		String sql = "drop procedure " + proName;
		try {
			statementExecute(sql);
			procedureNameList.remove(proName);
		} catch (SQLException e) {
			// Assume table does not exist
		}
	}

	/**
	 * Execute sql statement
	 *
	 * @param exeStr
	 * @throws SQLException
	 */
	private void statementExecute(String exeStr) throws SQLException {
		prepStmt = conn.prepareStatement(exeStr);
		prepStmt.execute();
	}

	/**
	 * Execute sql statement, to support BLOB insert
	 *
	 * @param exeStr
	 * @throws SQLException
	 */
	private void statementExecute(String exeStr, Object[] value) throws SQLException {
		prepStmt = conn.prepareStatement(exeStr);

		for (int i = 0; i < value.length; i++) {
			int pos = i + 1;
			if (value[i] instanceof Integer) {
				prepStmt.setInt(pos, ((Integer) value[i]).intValue());
			} else if (value[i] instanceof String) {
				prepStmt.setString(pos, (String) value[i]);
			} else if (value[i] instanceof byte[]) {
				prepStmt.setBytes(pos, (byte[]) value[i]);
			} else {
				throw new IllegalArgumentException("not support data type" + value[i].getClass().getName());
			}
		}

		prepStmt.execute();
	}

	/**
	 * Close test table
	 *
	 * @param droptable
	 * @throws SQLException
	 */
	public void close(boolean droptable) throws SQLException {
		if (conn != null) {
			if (prepStmt != null) {
				if (droptable) {
					for (int i = 0; i < tableNameList.size(); i++) {
						prepStmt = conn.prepareStatement("drop table " + tableNameList.get(i));
						prepStmt.executeUpdate();
					}
				}
				prepStmt.close();
			}
			conn.close();
		}
	}

	/**
	 * Create db connection
	 *
	 * @return
	 * @throws Exception
	 */
	private Connection createDBConnection() throws Exception {
		if (getURL().startsWith("jdbc:derby")) {
			return createDerbyConnection();
		}

		loadJdbcDrivers();

		java.util.Properties props = new java.util.Properties();
		props.put("user", getUser());
		props.put("password", getPassword());
		return DriverManager.getConnection(getURL(), props);
	}

	/**
	 * Create derby db connection
	 *
	 * @return
	 * @throws Exception
	 */
	private Connection createDerbyConnection() throws Exception {
		Class.forName(getDriverClassName());

		String nsURL = getURL() + ";create=true";
		java.util.Properties props = new java.util.Properties();
		props.put("user", getUser());
		props.put("password", getPassword());
		return DriverManager.getConnection(nsURL, props);
	}

	/**
	 * Return driver class name for test table
	 *
	 * @return
	 */
	static String getDriverClassName() {
		String clsName = System.getProperty("DTETest.driver");
		if (clsName == null) {
			clsName = "org.apache.derby.jdbc.EmbeddedDriver";
		}
		return clsName;
	}

	/**
	 * Return URL for test table
	 *
	 * @return
	 */
	public static String getURL() {
		String url = System.getProperty("DTETest.url");
		if (url == null) {
			url = "jdbc:derby:" + System.getProperty("java.io.tmpdir") + File.separator + "DTETest";
		}

		return url;
//		try{
//			new File( System.getProperty("java.io.tmpdir")+File.separator + "HELLO.txt").createNewFile( );
//		}catch(Exception e)
//		{
//
//		}
//		throw new NullPointerException( url +"                " +  System.getProperty("java.io.tmpdir"));
	}

	/**
	 * Return database name for test table
	 *
	 * @return
	 */
	/*
	 * private static String getDatabase( ) { String database = System.getProperty(
	 * "DTETest.database" ); if ( database != null ) return database; else return
	 * System.getProperty("java.io.temp")+File.pathSeparator+ "DTETest"; }
	 */

	/**
	 * Return user for test table
	 *
	 * @return
	 */
	static String getUser() {
		String user = System.getProperty("DTETest.user");
		if (user != null) {
			return user;
		} else {
			return "user";
		}
	}

	/**
	 * Return password for test table
	 *
	 * @return
	 */
	static String getPassword() {
		String pwd = System.getProperty("DTETest.password");
		if (pwd != null) {
			return pwd;
		} else {
			return "password";
		}
	}

	/**
	 * Load JDBC drivers
	 *
	 * @throws Exception
	 */
	private void loadJdbcDrivers() throws Exception {
		try {
			if ("true".equals(System.getProperty("DTETest.otherDB"))) {
				File driverHomeDir = getDriverHomeDir();
				URL[] urlList = getDriverFileURLs(driverHomeDir);
				URLClassLoader urlClassLoader = new URLClassLoader(urlList, this.getClass().getClassLoader());
				Class c = urlClassLoader.loadClass(getDriverClassName());
				DriverExt driverExt = new DriverExt((Driver) c.newInstance());
				DriverManager.registerDriver(driverExt);
			} else {
				Class.forName(getDriverClassName());
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Due to licence issue, Driver lib for " + getDriverClassName() + " can not be ");
			System.out.println("checked into Eclipse CVS. Please manually download this driver lib and put it ");
			System.out.println("into test\\plugins\\org.eclipse.birt.report.data.oda.jdbc\\drivers directory.");
			throw e;
		}
	}

	/**
	 * Gets driver home dir
	 */
	private File getDriverHomeDir() throws OdaException, IOException {
		File driverHomeDir = null;
		ExtensionManifest extMF = ManifestExplorer.getInstance()
				.getExtensionManifest("org.eclipse.birt.report.data.oda.jdbc");
		if (extMF != null) {
			URL url = extMF.getRuntimeInterface().getLibraryLocation();
			try {
				URI uri = new URI(url.toString());
				driverHomeDir = new File(uri.getPath(), "drivers");
			} catch (URISyntaxException e) {
				driverHomeDir = new File(url.getFile(), "drivers");
			}
		}
		return driverHomeDir;
	}

	/**
	 * Gets URL array of driver files
	 */
	private URL[] getDriverFileURLs(File driverHomeDir) throws MalformedURLException {
		String files[] = driverHomeDir.list(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return isDriverFile(name);
			}
		});

		URL[] urls = new URL[files.length];
		for (int i = 0; i < files.length; i++) {
			urls[i] = new URL("file", null, (new File(driverHomeDir, files[i])).getAbsolutePath());
		}
		return urls;
	}

	/**
	 * Checks whether the file is a jar file
	 */
	private boolean isDriverFile(String fileName) {
		String lcName = fileName.toLowerCase();
		return lcName.endsWith(".jar") || lcName.endsWith(".zip");
	}

	/**
	 * Support dynamically load JDBC drivers.
	 */
	private class DriverExt implements Driver {
		private Driver driver;

		DriverExt(Driver d) {
			this.driver = d;
		}

		@Override
		public boolean acceptsURL(String u) throws SQLException {
			return this.driver.acceptsURL(u);
		}

		@Override
		public Connection connect(String u, Properties p) throws SQLException {
			return this.driver.connect(u, p);
		}

		@Override
		public int getMajorVersion() {
			return this.driver.getMajorVersion();
		}

		@Override
		public int getMinorVersion() {
			return this.driver.getMinorVersion();
		}

		@Override
		public DriverPropertyInfo[] getPropertyInfo(String u, Properties p) throws SQLException {
			return this.driver.getPropertyInfo(u, p);
		}

		@Override
		public boolean jdbcCompliant() {
			return this.driver.jdbcCompliant();
		}

		@Override
		public Logger getParentLogger() throws SQLFeatureNotSupportedException {
			throw new SQLFeatureNotSupportedException();
		}
	}

}
