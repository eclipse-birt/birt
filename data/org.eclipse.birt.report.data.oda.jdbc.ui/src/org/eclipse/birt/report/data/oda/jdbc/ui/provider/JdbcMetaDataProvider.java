/*
 *************************************************************************
 * Copyright (c) 2006, 2013 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *
 *************************************************************************
 */
package org.eclipse.birt.report.data.oda.jdbc.ui.provider;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.bidi.utils.core.BidiConstants;
import org.eclipse.birt.report.data.bidi.utils.core.BidiTransform;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.Constants;
import org.eclipse.birt.report.data.oda.jdbc.ui.util.DriverLoader;
import org.eclipse.birt.report.data.oda.jdbc.utils.ResourceLocator;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.design.DataSetDesign;
import org.eclipse.datatools.connectivity.oda.design.DataSourceDesign;
import org.eclipse.datatools.connectivity.oda.design.ResourceIdentifiers;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DesignSessionUtil;

public class JdbcMetaDataProvider {
	private String userName;
	private String url;
	private String driverClass;
	private String password;
	private Properties props;
	private Connection connection;
	private boolean connect_fail = false;

	private static Logger logger = Logger.getLogger(JdbcMetaDataProvider.class.getName());

	private static JdbcMetaDataProvider instance = null;

	private String getCatalog() throws SQLException {
		// if the data source has no Catalog to store table, and the table is
		// stored in the root(seldom),the connection.getCatalog( ) will return
		// "". in this case if the user want to get all the 'root' table, they
		// needs to use Catalog= null to search. as a result here is the
		// connection.getCatalog( ) is "", to make user convenient to do it,
		// just set it as null.
		if (connection.getCatalog() != null && connection.getCatalog().trim().length() == 0) {
			return null;
		} else {
			return connection.getCatalog();
		}
	}

	private JdbcMetaDataProvider(String driverClass, String url, String userName, String password, Properties props) {
		this.driverClass = driverClass;
		this.url = url;
		this.userName = userName;
		this.password = password;
		this.props = props;
	}

	public static void createInstance(DataSetDesign dataSetDesign, ResourceIdentifiers resourceIdentifiers) {
		release();
		DataSourceDesign dataSourceDesign = dataSetDesign.getDataSourceDesign();
		Properties props = new Properties();
		try {
			props = DesignSessionUtil.getEffectiveDataSourceProperties(dataSourceDesign);
		} catch (OdaException e) {
			logger.log(Level.WARNING, e.getMessage(), e);
		}

		String userName = props.getProperty(Constants.ODAUser);
		String password = props.getProperty(Constants.ODAPassword);
		String url = props.getProperty(Constants.ODAURL);
		String driverClass = props.getProperty(Constants.ODADriverClass);

		Map appContext = new HashMap();
		if (resourceIdentifiers != null) {
			appContext.put(
					org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS,
					DesignSessionUtil.createRuntimeResourceIdentifiers(resourceIdentifiers));
		}
		try {
			ResourceLocator.resolveConnectionProperties(props, driverClass, appContext);
		} catch (OdaException e) {
		}

		// bidi_hcg: if Bidi format is defined - perform required Bidi transformations
		String metadataBidiFormatStr = props.getProperty(BidiConstants.METADATA_FORMAT_PROP_NAME);
		if (metadataBidiFormatStr != null) {
			userName = BidiTransform.transform(userName, BidiConstants.DEFAULT_BIDI_FORMAT_STR, metadataBidiFormatStr);
			password = BidiTransform.transform(password, BidiConstants.DEFAULT_BIDI_FORMAT_STR, metadataBidiFormatStr);
			url = BidiTransform.transformURL(url, BidiConstants.DEFAULT_BIDI_FORMAT_STR, metadataBidiFormatStr);
		}
		instance = new JdbcMetaDataProvider(driverClass, url, userName, password, props);
	}

	public static void release() {
		if (instance != null) {
			instance.closeConnection();
			instance = null;
		}
	}

	public void reconnect() throws SQLException, OdaException {
		if (connect_fail) {
			return;
		}
		closeConnection();
		try {
			connection = DriverLoader.getConnection(driverClass, url, userName, password, props);
		} catch (SQLException | OdaException odaException) {
			connect_fail = true;
			throw odaException;
		}
	}

	private void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// just ignore it
			}
		}
	}

	public static JdbcMetaDataProvider getInstance() {
		return instance;
	}

	public String getIdentifierQuoteString() {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return ""; //$NON-NLS-1$
			}
			try {
				return connection.getMetaData().getIdentifierQuoteString();
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return ""; //$NON-NLS-1$
			} catch (Exception ex) {
				return ""; //$NON-NLS-1$
			}
		}
		try {
			return connection.getMetaData().getIdentifierQuoteString();
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getIdentifierQuoteString();
			} catch (Exception e1) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return ""; //$NON-NLS-1$
			}
		}
	}

	public boolean isSupportProcedure() {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return false;
			}
			try {
				return connection.getMetaData().supportsStoredProcedures();
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return false;
			} catch (Exception ex) {
				return false;
			}
		}
		try {
			return connection.getMetaData().supportsStoredProcedures();
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().supportsStoredProcedures();
			} catch (Exception e1) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return true;
			}
		}
	}

	public boolean isSupportSchema() {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return false;
			}
			try {
				return connection.getMetaData().supportsSchemasInTableDefinitions();
			} catch (SQLException e) {
				try {
					ResultSet rs = connection.getMetaData().getSchemas();
					if (rs != null) {
						return true;
					} else {
						return false;
					}
				} catch (SQLException e1) {
					logger.log(Level.WARNING, e.getMessage(), e);
					return false;
				}
			} catch (Exception ex) {
				return false;
			}
		}
		try {
			return connection.getMetaData().supportsSchemasInTableDefinitions();
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().supportsSchemasInTableDefinitions();
			} catch (Exception e1) {
				try {
					ResultSet rs = connection.getMetaData().getSchemas();
					if (rs != null) {
						return true;
					} else {
						return false;
					}
				} catch (SQLException e2) {
					logger.log(Level.WARNING, e.getMessage(), e1);
					return false;
				}
			}
		}
	}

	public ResultSet getTableColumns(String schemaPattern, String tableNamePattern, String columnNamePattern) {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getColumns(getCatalog(), schemaPattern, tableNamePattern,
						columnNamePattern);
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getColumns(getCatalog(), schemaPattern, tableNamePattern,
					columnNamePattern);
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getColumns(getCatalog(), schemaPattern, tableNamePattern,
						columnNamePattern);
			} catch (SQLException | OdaException e1) {
				logger.log(Level.WARNING, e1.getMessage(), e1);
				return null;
			}
		}
	}

	public ResultSet getProcedures(String schemaPattern, String procedureNamePattern) {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getProcedures(getCatalog(), schemaPattern, procedureNamePattern);
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getProcedures(getCatalog(), schemaPattern, procedureNamePattern);
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getProcedures(getCatalog(), schemaPattern, procedureNamePattern);
			} catch (Exception e1) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
		}
	}

	public String[] getTableTypeNames(long milliSeconds) {
		class TempThread extends Thread {

			private List<String> names = new ArrayList<>();

			@Override
			public void run() {
				ResultSet rs = JdbcMetaDataProvider.this.getTableTypes();
				if (rs != null) {
					try {
						while (rs.next()) {
							names.add(rs.getString("TABLE_TYPE")); //$NON-NLS-1$
						}
					} catch (SQLException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
					}
				}
			}

			public String[] getResult() {
				return names.toArray(new String[0]);
			}
		}
		TempThread tt = new TempThread();
		tt.start();
		try {
			tt.join(milliSeconds);
		} catch (InterruptedException e) {
		}
		return tt.getResult();
	}

	public ResultSet getTableTypes() {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getTableTypes();
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getTableTypes();
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getTableTypes();
			} catch (SQLException | OdaException ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
				return null;
			}
		}
	}

	public ResultSet getProcedureColumns(String schemaPattern, String procedureNamePattern, String columnNamePattern) {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getProcedureColumns(getCatalog(), schemaPattern, procedureNamePattern,
						columnNamePattern);
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getProcedureColumns(getCatalog(), schemaPattern, procedureNamePattern,
					columnNamePattern);
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getProcedureColumns(getCatalog(), schemaPattern, procedureNamePattern,
						columnNamePattern);
			} catch (SQLException | OdaException e1) {
				logger.log(Level.WARNING, e1.getMessage(), e1);
				return null;
			}
		}
	}

	public ResultSet getAlltables(String schemaPattern, String namePattern, String[] types) {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getTables(getCatalog(), schemaPattern, namePattern, types);
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getTables(getCatalog(), schemaPattern, namePattern, types);
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getTables(getCatalog(), schemaPattern, namePattern, types);
			} catch (SQLException | OdaException ex) {
				logger.log(Level.WARNING, ex.getMessage(), ex);
				return null;
			}
		}
	}

	public ResultSet getAllSchemas() {
		if (connection == null) {
			try {
				reconnect();
			} catch (Exception e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
			try {
				return connection.getMetaData().getSchemas();
			} catch (SQLException e) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			} catch (Exception ex) {
				return null;
			}
		}
		try {
			return connection.getMetaData().getSchemas();
		} catch (SQLException e) {
			try {
				reconnect();
				return connection.getMetaData().getSchemas();
			} catch (Exception e1) {
				logger.log(Level.WARNING, e.getMessage(), e);
				return null;
			}
		}
	}

	public String[] getAllSchemaNames(long milliSeconds) {
		class TempThread extends Thread {
			private List<String> names = new ArrayList<>();

			@Override
			public void run() {
				ResultSet rs = JdbcMetaDataProvider.this.getAllSchemas();
				if (rs != null) {
					try {
						while (rs.next()) {
							names.add(rs.getString("TABLE_SCHEM")); //$NON-NLS-1$
						}
					} catch (SQLException e) {
						logger.log(Level.WARNING, e.getMessage(), e);
					}
				}
			}

			public String[] getResult() {
				return names.toArray(new String[0]);
			}
		}
		TempThread tt = new TempThread();
		tt.start();
		try {
			tt.join(milliSeconds);
		} catch (InterruptedException e) {
		}
		return tt.getResult();
	}
}
