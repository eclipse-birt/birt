/*
 *************************************************************************
 * Copyright (c) 2008, 2011 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.nls.Messages;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.IManagedConnection;
import org.eclipse.datatools.connectivity.drivers.IDriverMgmtConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IQuery;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;
import org.eclipse.datatools.connectivity.oda.consumer.services.impl.ProviderUtil;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.connectivity.oda.profile.internal.OdaConnectionProfile;
import org.eclipse.datatools.connectivity.oda.profile.internal.OdaProfileFactory;

/**
 * Extends the behavior of the oda.jdbc runtime driver to use a database
 * connection profile.
 */
public class Connection extends org.eclipse.birt.report.data.oda.jdbc.Connection implements IConnection {
	protected static final String SQB_DATA_SET_TYPE = "org.eclipse.birt.report.data.oda.jdbc.dbprofile.sqbDataSet"; //$NON-NLS-1$
	private static final String JDBC_CONN_TYPE = "java.sql.Connection"; //$NON-NLS-1$

	private static final String sm_className = Connection.class.getName();
	private static final String sm_packageName = Connection.class.getPackage().getName();
	private static final Logger sm_logger = Logger.getLogger(sm_packageName);

	private IConnectionProfile m_dbProfile;

	/*
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#open(java.util.Properties)
	 */
	@Override
	public void open(Properties connProperties) throws OdaException {
		OdaException originalEx = null;
		try {
			// find and load the db profile defined in connection properties
			IConnectionProfile dbProfile = getProfile(connProperties);
			open(dbProfile);
		} catch (OdaException ex) {
			// log warning with connect status
			sm_logger.logp(Level.WARNING, sm_className, "open(Properties)", //$NON-NLS-1$
					Messages.connection_openFailed, ex);
			originalEx = ex;
		}

		// check if no DTP managed JDBC connection available, try use local properties
		// to connect
		try {
			if (!isOpen()) {
				openJdbcConnection(connProperties);
			}
			if (this.jdbcConn != null && this.appContext != null) {
				// Set the app context with this connection to be used
				this.appContext.put(Constants.ODACurrentOpenConnection, this.jdbcConn);
			}
		} catch (OdaException ex) {
			// not able to open with local properties; throw the original exception if
			// exists
			if (originalEx != null) {
				throw originalEx;
			}
			throw new OdaException(Messages.connection_openFailed);
		}
	}

	protected IConnectionProfile getProfile(Properties connProperties) throws OdaException {
		return loadProfileFromProperties(connProperties, getAppContextMap());
	}

	protected void openJdbcConnection(Properties profileProperties) throws OdaException {
		// TODO - adapt db profile properties to oda.jdbc properties
		super.open(profileProperties);
	}

	/**
	 * Internal method to open a connection based on the specified database
	 * connection profile.
	 *
	 * @param dbProfile
	 * @throws OdaException
	 */
	public void open(IConnectionProfile dbProfile) throws OdaException {
		super.jdbcConn = null;
		m_dbProfile = dbProfile;
		if (m_dbProfile == null) {
			throw new OdaException(Messages.connection_nullProfile);
		}

		// connect via the db profile
		IStatus connectStatus = openWithProfile(m_dbProfile);

		if (connectStatus == null || connectStatus.getSeverity() > IStatus.INFO) {
			throw new OdaException(getStatusException(connectStatus));
		}

		super.jdbcConn = getJDBCConnection(m_dbProfile);
	}

	/**
	 * For internal use only. An utility method to open a connection based on the
	 * properties defined in the specified connection profile.
	 *
	 * @since 3.7.2
	 */
	public static IStatus openWithProfile(IConnectionProfile connProfile) {
		if (connProfile instanceof OdaConnectionProfile) {
			return ((OdaConnectionProfile) connProfile).connectSynchronously(); // handles re-connection
		}

		return connProfile.connect();
	}

	private java.sql.Connection getJDBCConnection(IConnectionProfile dbProfile) {
		if (dbProfile == null) {
			return null;
		}

		IManagedConnection mgtConn = dbProfile.getManagedConnection(JDBC_CONN_TYPE);
		if (mgtConn == null) {
			return null;
		}

		org.eclipse.datatools.connectivity.IConnection connObj = mgtConn.getConnection();
		if (connObj == null) {
			return null;
		}

		java.sql.Connection jdbcConn = (java.sql.Connection) connObj.getRawConnection();
		return jdbcConn;
	}

	/**
	 * Returns a connection profile based on the specified connection properties. If
	 * a profile store file is specified, load the referenced profile instance from
	 * the profile store. Otherwise, create a transient profile instance if profile
	 * base properties are available.
	 *
	 * @param connProperties
	 * @return the loaded connection profile; may be null if properties are invalid
	 *         or insufficient
	 * @throws OdaException
	 */
	public static IConnectionProfile loadProfileFromProperties(Properties connProperties) throws OdaException {
		return loadProfileFromProperties(connProperties, null);
	}

	/**
	 * Returns a connection profile based on the specified connection properties and
	 * application context. If a profile store file is specified, load the
	 * referenced profile instance from the profile store. Otherwise, create a
	 * transient profile instance if profile base properties are available.
	 *
	 * @param connProperties
	 * @param appContext
	 * @return the loaded connection profile; may be null if properties are invalid
	 *         or insufficient
	 * @throws OdaException
	 * @since 3.7.2
	 */
	public static IConnectionProfile loadProfileFromProperties(Properties connProperties, Map<?, ?> appContext)
			throws OdaException {
		// adjust the effective db profile properties to use for loading a profile
		connProperties = adjustDbProfileProperties(connProperties);

		// find and load the db profile defined in connection properties;
		// note: driver class path specified in appContext, if exists, is not relevant
		// when connecting with the properties defined in a connection profile instance
		// (i.e. a profile instance uses its own jarList property)
		IConnectionProfile dbProfile = OdaProfileExplorer.getInstance().getProfileByName(connProperties, appContext);

		// If we have ManagedConnection whose key is IConnection, that means
		// it is a ConnectionProfile to ConnectionProfile repository, rather
		// than the wanted one to a database, so try to get the wanted
		// profile inside.
		if (dbProfile != null && dbProfile.getManagedConnection(IConnection.class.getName()) != null) {
			connProperties = dbProfile.getBaseProperties();
			dbProfile = Connection.loadProfileFromProperties(connProperties);
		}

		if (dbProfile != null) {
			return dbProfile; // found referenced external profile instance
		}

		// no external profile instance is specified or available;
		// try create a transient profile if the connection properties contains profile
		// properties
		return createTransientProfile(connProperties);
	}

	private static IConnectionProfile createTransientProfile(Properties connProperties) throws OdaException {
		Properties profileProps = PropertyAdapter.adaptToDbProfilePropertyNames(connProperties);

		return OdaProfileFactory.createTransientProfile(profileProps);
	}

	private static Properties adjustDbProfileProperties(Properties dataSourceProperties) throws OdaException {
		// first adapts to db profile properties
		Properties dbProfileProps = PropertyAdapter.adaptToDbProfilePropertyNames(dataSourceProperties);

		// use the oda.consumer.propertyProvider extension implemented for the profile's
		// definition type,
		// to get the effective properties to open a connection
		String defnType = dbProfileProps.getProperty(IDriverMgmtConstants.PROP_DEFN_TYPE);
		Map<String, String> appContext = new HashMap<>();
		appContext.put(IPropertyProvider.ODA_CONSUMER_ID, defnType);

		Properties adjustedDbProfileProps = ProviderUtil.getEffectiveProperties(dbProfileProps, appContext);
		if (dbProfileProps.equals(adjustedDbProfileProps)) {
			return dataSourceProperties; // no adjustment is applicable
		}

		// adapt adjusted db profile properties back to data source properties
		return PropertyAdapter.adaptToDataSourcePropertyNames(adjustedDbProfileProps);
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#close()
	 */
	@Override
	public void close() throws OdaException {
		if (m_dbProfile != null) {
			closeProfile(m_dbProfile);
			m_dbProfile = null;
			super.jdbcConn = null;
			return;
		}

		super.close();
	}

	/**
	 * Close the specified connection profile.
	 *
	 * @param dbProfile
	 * @deprecated As of 2.5.2, replaced by
	 *             {@link #closeProfile(IConnectionProfile)}
	 */
	@Deprecated
	protected static void close(IConnectionProfile dbProfile) {
		closeProfile(dbProfile);
	}

	/**
	 * Utility method to close the specified connection profile.
	 *
	 * @param connProfile
	 * @since 2.5.2
	 */
	public static void closeProfile(IConnectionProfile connProfile) {
		if (connProfile == null) {
			return; // nothing to close
		}

		if (connProfile instanceof OdaConnectionProfile) {
			((OdaConnectionProfile) connProfile).close();
		} else {
			connProfile.disconnect(null); // does nothing if already disconnected
		}
	}

	/*
	 * @see org.eclipse.datatools.connectivity.oda.IConnection#isOpen()
	 */
	@Override
	public boolean isOpen() throws OdaException {
		if (m_dbProfile != null) {
			return (m_dbProfile.getConnectionState() == IConnectionProfile.CONNECTED_STATE);
		}

		return super.isOpen();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.datatools.connectivity.oda.IConnection#newQuery(java.lang.String)
	 */
	@Override
	public IQuery newQuery(String dataSetType) throws OdaException {
		// ignores the specified dataSetType,
		// as this driver currently supports only one data set type, and
		// the SQB data set type supports Select statements only
		return new DBProfileStatement(getRawConnection());
	}

	/**
	 * Returns the connection profile instance for this db connection.
	 *
	 * @return
	 */
	protected IConnectionProfile getDbProfile() {
		return m_dbProfile;
	}

	protected java.sql.Connection getRawConnection() {
		return super.jdbcConn;
	}

	/**
	 * Internal method to collect the first exception from the specified status.
	 *
	 * @param status may be null
	 */
	public static Throwable getStatusException(IStatus status) {
		if (status == null) {
			return null;
		}
		Throwable ex = status.getException();
		if (ex != null) {
			return ex;
		}

		// find first exception from its children
		IStatus[] childrenStatus = status.getChildren();
		for (int i = 0; i < childrenStatus.length && ex == null; i++) {
			ex = childrenStatus[i].getException();
		}
		return ex;
	}

}
