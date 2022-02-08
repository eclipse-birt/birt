/*
 *************************************************************************
 * Copyright (c) 2005, 2011 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.sampledb;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory;

public class SampleDBJDBCConnectionFactory implements IConnectionFactory {
	private static final Logger logger = Logger.getLogger(SampleDBJDBCConnectionFactory.class.getName());
	private Driver derbyDriver;

	/**
	 * Creates a new JDBC connection to the embedded sample database.
	 * 
	 * @see org.eclipse.birt.report.data.oda.jdbc.IConnectionFactory#getConnection(java.lang.String,
	 *      java.lang.String, java.util.Properties)
	 */
	public Connection getConnection(String driverClass, String url, Properties connectionProperties)
			throws SQLException {
		if (!driverClass.equals(SampleDBConstants.DRIVER_CLASS)) {
			// This is unexpected; we shouldn't be getting this call
			logger.log(Level.SEVERE, "Unexpected driverClass: " + driverClass);
			throw new SQLException("Unexpected driverClass " + driverClass);
		}
		if (!url.equals(SampleDBConstants.DRIVER_URL)) {
			// Wrong url
			logger.log(Level.WARNING, "Unexpected url: " + url);
			throw new SQLException("Classic Models Inc. Sample Database Driver does not recognize url: " + driverClass);
		}

		String dbUrl = SampledbPlugin.getDBUrl();

		// Copy connection properties and replace user and password with fixed value
		Properties props;
		if (connectionProperties != null)
			props = (Properties) connectionProperties.clone();
		else
			props = new Properties();
		props.put("user", SampleDBConstants.SAMPLE_DB_SCHEMA);
		props.put("password", "");

		if (logger.isLoggable(Level.FINER)) {
			logger.fine("Getting Sample DB JDBC connection. DriverClass=" + SampleDBConstants.DERBY_DRIVER_CLASS
					+ ", Url=" + dbUrl);
		}

		return getDerbyDriver().connect(dbUrl, props);
	}

	void shutdownDerby() {
		try {
			getDerbyDriver().connect("jdbc:derby:;shutdown=true", null);
		} catch (SQLException e) {
			// A successful shutdown always results in an SQLException to indicate that
			// Derby has shut down and that there is no other exception.
		}
	}

	/**
	 * Gets a new instance of Derby JDBC Driver
	 */
	private synchronized Driver getDerbyDriver() throws SQLException {
		if (derbyDriver == null) {
			try {
				derbyDriver = (Driver) Class
						.forName(SampleDBConstants.DERBY_DRIVER_CLASS, true, this.getClass().getClassLoader())
						.newInstance();
			} catch (Exception e) {
				logger.log(Level.WARNING,
						"Failed to load Derby embedded driver: " + SampleDBConstants.DERBY_DRIVER_CLASS, e);
				throw new SQLException(e.getLocalizedMessage());
			}
		}
		return derbyDriver;
	}

	/**
	 * Class loader to delegate Derby class and resource loading to our loader, and
	 * others to the default context loader
	 */
	/*
	 * private static class ContextClassLoaderDelegator extends ClassLoader {
	 * private DerbyClassLoader derbyLoader; public ContextClassLoaderDelegator(
	 * ClassLoader defaultLoader, DerbyClassLoader derbyLoader) {
	 * super(defaultLoader); assert derbyLoader != null; assert
	 * derbyLoader.isGood(); this.derbyLoader = derbyLoader; }
	 * 
	 *//**
		 * @see java.lang.ClassLoader#loadClass(java.lang.String, boolean)
		 */
	/*
	 * protected synchronized Class loadClass(String name, boolean resolve) throws
	 * ClassNotFoundException { if ( DerbyClassLoader.isDerbyClass(name) ) { //
	 * Always delegate derby classes to derby class loader return
	 * derbyLoader.loadClass( name, resolve); } else { // Delegate to default
	 * implementation, which will use parent classloader return super.loadClass(
	 * name, resolve); } }
	 * 
	 *//**
		 * @see java.lang.ClassLoader#getResource(java.lang.String)
		 *//*
			 * public URL getResource(String name) { if (
			 * DerbyClassLoader.isDerbyResource(name) ) { // Always delegate derby resources
			 * to derby resource loader return derbyLoader.getResource(name); } else { //
			 * Delegate to default implementation, which will use parent classloader return
			 * super.getResource( name); } } }
			 */

	/**
	 * @return user name for db connection
	 */
	public static String getDbUser() {
		return SampleDBConstants.SAMPLE_DB_SCHEMA;
	}

}
