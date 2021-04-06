/*
 *************************************************************************
 * Copyright (c) 2004-2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.sampledb;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.birt.report.data.oda.jdbc.Connection;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;

/**
 * Implements the BIRT ODA connection factory interface.
 * 
 * @deprecated This class remains solely for backward compatibility. In BIRT
 *             2.0M3 and beyond, all SampleDB data sources are created as proper
 *             JDBC data sources. SampleDB data sources created using prior
 *             builds will continue to use this class to obtain runtime
 *             connectivity.
 */
public class SampleDBDriver extends OdaJdbcDriver {
	private static Logger logger = Logger.getLogger(SampleDBDriver.class.getName());

	/**
	 * @see org.eclipse.birt.data.oda.IDriver#getConnection(java.lang.String)
	 */
	public IConnection getConnection(String connectionClassName) throws OdaException {
		return new SampleDBConnection();
	}

	/**
	 * Implements the BIRT ODA connection interface. This class wraps the oda.jdbc
	 * driver's actual implementation of ODA. Connection properties as fixed for the
	 * Sample Database
	 */
	static private class SampleDBConnection extends Connection {

		/**
		 * @see org.eclipse.birt.data.oda.IConnection#open(java.util.Propertikes)
		 */
		public void open(Properties connProperties) throws OdaException {
			logger.entering(SampleDBConnection.class.getName(), "open");

			// Ignore all properties passed in (it's expected to be empty anyway)
			Properties props = new Properties();
			String driverClass = SampleDBConstants.DRIVER_CLASS;
			String url = SampleDBConstants.DRIVER_URL;
			String user = SampleDBJDBCConnectionFactory.getDbUser();
			props.setProperty(Connection.Constants.ODADriverClass, driverClass);
			props.setProperty(Connection.Constants.ODAURL, url);
			props.setProperty(Connection.Constants.ODAUser, user);

			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Opening SampleDB connection. DriverClass=" + driverClass + "; url=" + url);
			}

			super.open(props);

			logger.exiting(SampleDBConnection.class.getName(), "open");
		}
	}

}
