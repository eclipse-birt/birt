/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.hive;

import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.Connection;
import org.eclipse.birt.report.data.oda.jdbc.OdaJdbcDriver;
import org.eclipse.birt.report.data.oda.jdbc.Statement;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;

/**
 * Implements the BIRT ODA connection factory interface.
 */
public class HiveDriver extends OdaJdbcDriver {
	private static Logger logger = Logger.getLogger(HiveDriver.class.getName());
	public static final String DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.hive";

	/**
	 * @see org.eclipse.birt.data.oda.IDriver#getConnection(java.lang.String)
	 */
	public IConnection getConnection(String connectionClassName) throws OdaException {
		return new HiveConnection();
	}

	/**
	 * Implements the BIRT ODA connection interface.
	 */
	static private class HiveConnection extends Connection {
		private String addFileStatement = null;

		/**
		 * @see org.eclipse.birt.data.oda.IConnection#open(java.util.Propertikes)
		 */
		public void open(Properties connProperties) throws OdaException {
			logger.entering(HiveConnection.class.getName(), "open");

			if (logger.isLoggable(Level.FINER)) {
				logger.log(Level.FINER, "Opening Hive connection. DriverClass=" + Connection.Constants.ODADriverClass);
			}
			if (connProperties.getProperty("addListFile") != null) {
				this.addFileStatement = connProperties.getProperty(HiveConstants.HIVE_ADD_FILE_PROPERTY);
			}
			super.open(connProperties);

			logger.exiting(HiveConnection.class.getName(), "open");
		}

		public IQuery newQuery(String query) throws OdaException {
			return new HiveQuery(this.jdbcConn, this.addFileStatement);

		}
	}

	static private class HiveQuery extends Statement {
		private String parentAddFileStatement;
		private boolean added = false;

		public HiveQuery(java.sql.Connection connection, String addfile) throws OdaException {
			super(connection);
			this.parentAddFileStatement = addfile;
		}

		public void prepare(String command) throws OdaException {
			if (!this.added) {
				String addFileStatement = null;
				if (this.getSpecification() != null) {
					Object temp = this.getSpecification().getProperty(HiveConstants.HIVE_ADD_FILE_PROPERTY);
					addFileStatement = temp == null ? null : temp.toString();
				}
				if (addFileStatement != null || this.parentAddFileStatement != null) {
					try {

						java.sql.Statement st = this.conn.createStatement();

						String addfilestatementstr = null;

						if (addFileStatement == null) {
							addfilestatementstr = this.parentAddFileStatement;

						} else {
							addfilestatementstr = addFileStatement;
						}

						String delimiter = ";";
						String[] aFiles;
						aFiles = addfilestatementstr.split(delimiter);
						for (int i = 0; i < aFiles.length; i++) {
							if (aFiles[i] != null && aFiles[i].length() > 0) {
								st.execute(aFiles[i]);
							}
						}
						st.close();
						this.added = true;

					} catch (SQLException e) {
						// e.printStackTrace();
						logger.logp(java.util.logging.Level.WARNING, HiveQuery.class.getName(), "executeQuery",
								"Add File Operation Failed", e);
					}
				}
			}
			super.prepare(command);
		}

		public void close() throws OdaException {
			// TODO Auto-generated method stub
			super.close();
			this.added = false;
		}

	}

}
