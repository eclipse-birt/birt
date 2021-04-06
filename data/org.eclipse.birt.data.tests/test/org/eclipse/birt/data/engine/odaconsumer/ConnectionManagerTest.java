/*
 *****************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odaconsumer.testutil.TraceLogTesterUtil;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.consumer.helper.OdaConsumerPlugin;
import org.eclipse.datatools.connectivity.oda.consumer.helper.OdaDriver;
import org.eclipse.datatools.connectivity.oda.util.logging.Level;

import testutil.JDBCOdaDataSource;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class ConnectionManagerTest extends OdaconsumerTestCase {
	private ConnectionManager sm_manager;

	private LogConfiguration m_testerLogConfig;
	private File m_logDir;

	ConnectionManager getManager() {
		if (sm_manager == null)
			sm_manager = ConnectionManager.getInstance();
		return sm_manager;
	}

	@Before
	public void connectionManagerSetUp() throws Exception {
		// setup below only when running the test case from own test class
		if (this.getTestName().equals("testPassThruContext")
				&& getClass().getName().endsWith("ConnectionManagerTest")) {
			// set up test log config; clean up files in logdir
			m_testerLogConfig = getOdaHelperTestLogConfig(JDBCOdaDataSource.DATA_SOURCE_TYPE);
			m_logDir = new File(m_testerLogConfig.getLogDirectory());
			TraceLogTesterUtil.getInstance().clearDirectory(m_logDir);
		}
	}

	@After
	public void connectionManagerTearDown() throws Exception {
		// tear down below only when running the test case from own test class
		if (this.getTestName().equals("testPassThruContext")
				&& getClass().getName().endsWith("ConnectionManagerTest")) {
			// turn off OdaDriver's logging
			LogConfiguration offLogConfig = new LogConfiguration(Level.OFF, m_testerLogConfig.getLogDirectory(), "",
					"");
			setOdaHelperLogConfig(JDBCOdaDataSource.DATA_SOURCE_TYPE, offLogConfig);

			// clean up logdir
			try {
				TraceLogTesterUtil.getInstance().clearDirectory(m_logDir);
				m_logDir.delete();
			} catch (IOException e) {
				fail("Problem with tearDown for test 4: " + e.toString());
			}

			// re-initalize for subsequent test cases
			m_testerLogConfig = null;
			m_logDir = null;
		}
	}

	public final void testGetInstance() {
		assertNotNull(getManager());
	}

	public final void testOpenConnection() throws Exception {
		Properties connProperties = getJdbcConnProperties();
		Connection conn = getManager().openConnection(JDBCOdaDataSource.DATA_SOURCE_TYPE, connProperties, null);
		assertNotNull(conn);
		conn.close();
	}

	public final void testGetMaxConnections() throws DataException {
		assertEquals(getManager().getMaxConnections(JDBCOdaDataSource.DATA_SOURCE_TYPE), 0);
	}

	public final void testPassThruContext() throws Exception {
		// do not run this test when triggered by a sub-class
		String testClassName = this.getClass().getName();
		if (!testClassName.endsWith("ConnectionManagerTest"))
			return;

		// assure the test number matches that used in connectionManagerSetUp()
		assertNotNull(m_testerLogConfig);
		assertNotNull(m_logDir);

		// override OdaDriver logging with given test log config setting
		setOdaHelperLogConfig(JDBCOdaDataSource.DATA_SOURCE_TYPE, m_testerLogConfig);

		Properties connProperties = getJdbcConnProperties();
		Map dummyAppContext = new Properties();
		Connection conn = getManager().openConnection(JDBCOdaDataSource.DATA_SOURCE_TYPE, connProperties,
				dummyAppContext);
		assertNotNull(conn);

		String queryText = "select * from \"testtable\" where \"intColumn\" > ?";
		conn.prepareStatement(queryText, JDBCOdaDataSource.DATA_SET_TYPE);
		conn.close();

		// now check the log file for expected log entries
		final String emptyParen = "\\(\\)\t";
		String[] expectedLogs = new String[] {
				"OdaDriver.setAppContext" + emptyParen + "Passing thru application context ",
				"OdaConnection.setAppContext" + emptyParen + "Passing thru application context ",
				"OdaQuery.setAppContext" + emptyParen + "Passing thru application context " };

		String[] logFiles = m_logDir.list();
		assertNotNull(logFiles);
		assertEquals(1, logFiles.length);
		String logFileName = m_logDir + "/" + logFiles[0];

		boolean hasExpectedLogs = TraceLogTesterUtil.getInstance().matchLogPatternsInFile(logFileName, expectedLogs);
		assertTrue(hasExpectedLogs);
	}

	private void sanityCheckProperties() throws Exception {
		if (System.getProperty("odaJdbcUrl") == null || System.getProperty("odaJdbcUser") == null
				|| System.getProperty("odaJdbcPassword") == null || System.getProperty("odaJdbcDriver") == null) {
			System.setProperty("odaJdbcUrl", "jdbc:derby:DtETest");
			System.setProperty("odaJdbcUser", "sa");
			System.setProperty("odaJdbcPassword", "sa");
			System.setProperty("odaJdbcDriver", "org.apache.derby.jdbc.EmbeddedDriver");
		}
	}

	protected Properties getJdbcConnProperties() throws Exception {
		sanityCheckProperties();

		Properties connProperties = new Properties();
		connProperties.setProperty("odaURL", System.getProperty("odaJdbcUrl"));
		connProperties.setProperty("odaUser", System.getProperty("odaJdbcUser"));
		connProperties.setProperty("odaPassword", System.getProperty("odaJdbcPassword"));
		connProperties.setProperty("odaDriverClass", System.getProperty("odaJdbcDriver"));
		return connProperties;
	}

	private LogConfiguration getOdaHelperTestLogConfig(String dataSourceId) throws Exception {
		String logDir = getOdaHelperLogDir(dataSourceId);
		LogConfiguration testLogConfig = new LogConfiguration(Level.FINE, logDir, "", "");
		return testLogConfig;
	}

	private String getOdaHelperLogDir(String dataSourceId) {
		return OdaConsumerPlugin.getDefault().getStateLocation().append("logs").append("ConnectionManagerTest").toFile()
				.getPath();
	}

	private void setOdaHelperLogConfig(String dataSourceId, LogConfiguration testLogConfig) throws Exception {
		IDriver driverHelper = DriverManager.getInstance().getDriverHelper(dataSourceId);

		// set odaDriverHelper log directory to ensure it
		// accepts the log configuration
		assertTrue(driverHelper instanceof OdaDriver);
		OdaDriver odaDrvHelper = (OdaDriver) driverHelper;
		odaDrvHelper.setLogDirectory(testLogConfig.getLogDirectory());

		driverHelper.setLogConfiguration(testLogConfig);
	}
}
