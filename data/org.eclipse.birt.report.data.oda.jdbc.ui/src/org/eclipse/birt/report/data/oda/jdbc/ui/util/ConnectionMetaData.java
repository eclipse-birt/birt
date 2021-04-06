/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc.ui.util;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This is a utility class for maintaining the meta data information for a
 * particular JDBC connection.
 * 
 * @version $Revision: 1.9 $ $Date: 2008/08/04 07:55:18 $
 */

public class ConnectionMetaData implements Serializable {

	/**
	 * serial version id
	 */
	private static final long serialVersionUID = -4370317661507339132L;
	private String classname = null;
	private String url = null;
	private String username = null;
	private String password = null;
	private Properties properties = null;
	private String catalogname = null;
	private ArrayList schemas = null;
	private transient Connection connection = null;
	private transient DatabaseMetaData metadata = null;
	private static Logger logger = Logger.getLogger(ConnectionMetaData.class.getName());
	private long timeout; // milliseconds

	/**
	 *  
	 */
	public ConnectionMetaData() {
		super();
	}

	/**
	 * @return Returns the classname.
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * @param classname The classname to set.
	 */
	public void setClassname(String classname) {
		this.classname = classname;
	}

	/**
	 * @return Returns the password.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password The password to set.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return Returns the properties.
	 */
	public Properties getProperties() {
		return properties;
	}

	/**
	 * @param properties The properties to set.
	 */
	public void setProperties(Properties properties) {
		this.properties = properties;
	}

	/**
	 * @return Returns the url.
	 */
	public String getUrl() {
		return url;
	}

	public void setTimeout(long timeout) {
		this.timeout = timeout;
	}

	/**
	 * @param url The url to set.
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return Returns the username.
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @param username The username to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the catalogname.
	 * @throws SQLException
	 */
	public String getCatalogname() throws SQLException {
		if (catalogname == null) {
			// Get the name of the catalog for this connection from the data
			// base
			catalogname = getConnection().getCatalog();
		}
		return catalogname;
	}

	public String getDatabaseProductName() throws SQLException {
		return getMetaData().getDatabaseProductName();
	}

	public String getDatabaseProductVersion() throws SQLException {
		return getMetaData().getDatabaseProductVersion();
	}

	/**
	 * @return Returns the schemas.
	 * @throws SQLException
	 */
	public ArrayList getSchemas() throws SQLException {
		if (schemas == null) {
			Thread h = new Thread() {
				@Override
				public void run() {
					try {
						retrieveSchemas();
					} catch (SQLException e) {
					}
				}
			};
			h.start();
			try {
				h.join(timeout);
			} catch (InterruptedException e) {
			}
			if (schemas == null) {
				schemas = new ArrayList();
			}
		}
		return schemas;
	}

	public Schema getSchema(String schemaName) throws SQLException {
		Iterator iter = getSchemas().iterator();
		Schema schema = null;
		while (iter.hasNext()) {
			schema = (Schema) iter.next();
			if (schemaName.equals(schema.getName())) {
				return schema;
			}
		}

		return null;
	}

	private Connection getConnection() throws SQLException {
		if (connection == null) {
			connect();
		}

		return connection;
	}

	DatabaseMetaData getMetaData() throws SQLException {
		if (metadata == null) {
			metadata = getConnection().getMetaData();
		}

		return metadata;
	}

	private synchronized void connect() throws SQLException {
		if (connection == null || connection.isClosed()) {
			if (getClassname() != null && getUrl() != null) {
				if (getPassword() == null) {
					setPassword("");
				}

				connection = DriverLoader.getConnectionWithExceptionTip(classname, url, username, password, properties);
			}
		}
	}

	private synchronized void retrieveSchemas() throws SQLException {
		if (schemas == null) {
			schemas = new ArrayList();
			Schema schema = null;
			// Get the meta data and find out whether it support schemas
			if (getMetaData().supportsSchemasInTableDefinitions()) {
				// If it does then get the schemas from the database
				ResultSet resultSet = getMetaData().getSchemas();
				while (resultSet.next()) {
					String schemaName = resultSet.getString("TABLE_SCHEM");//$NON-NLS-1$
					schema = new Schema(this, timeout);
					schema.setName(schemaName);
					schemas.add(schema);
				}
			} else {
				// Add a default schema to the list with no name
				// to indicate that this data base doesn't support schemas
				schema = new Schema(this, timeout);
				schemas.add(schema);
			}
		}
	}

	public void clearCache() {
		try {
			if (connection != null && !connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.log(Level.FINE, e.getMessage(), e);
		}
		if (schemas != null) {
			schemas.clear();
			schemas = null;
		}
	}

	/**
	 * Returns <code>true</code> if this <code>ConnectionMetaData</code> is the same
	 * as the o argument.
	 * 
	 * @return <code>true</code> if this <code>ConnectionMetaData</code> is the same
	 *         as the o argument.
	 */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (o.getClass() != getClass()) {
			return false;
		}
		ConnectionMetaData castedObj = (ConnectionMetaData) o;
		return ((this.classname == null ? castedObj.classname == null : this.classname.equals(castedObj.classname))
				&& (this.url == null ? castedObj.url == null : this.url.equals(castedObj.url))
				&& (this.username == null ? castedObj.username == null : this.username.equals(castedObj.username))
				&& (this.password == null ? castedObj.password == null : this.password.equals(castedObj.password))
				&& (this.properties == null ? castedObj.properties == null
						: this.properties.equals(castedObj.properties)));
	}

	/**
	 * Override hashCode.
	 * 
	 * @return the Objects hashcode.
	 */
	public int hashCode() {
		int hashCode = 1;
		hashCode = 31 * hashCode + (classname == null ? 0 : classname.hashCode());
		hashCode = 31 * hashCode + (url == null ? 0 : url.hashCode());
		hashCode = 31 * hashCode + (username == null ? 0 : username.hashCode());
		hashCode = 31 * hashCode + (password == null ? 0 : password.hashCode());
		hashCode = 31 * hashCode + (properties == null ? 0 : properties.hashCode());
		return hashCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#finalize()
	 */
	protected void finalize() throws Throwable {
		clearCache();
		super.finalize();
	}
}