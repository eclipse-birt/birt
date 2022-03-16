/*
 *****************************************************************************
 * Copyright (c) 2004, 2010 Actuate Corporation.
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
 ******************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;
import org.eclipse.datatools.connectivity.oda.profile.Constants;

/**
 * ConnectionManager manages a set of data source connections. Calling
 * <code>getInstance</code> will return an instance of
 * <code>ConnectionManager</code>. When the method <code>openConnection</code>
 * is called, the <code>ConnectionManager</code> will attempt to open and return
 * a <code>Connection</code> instance of the data source extension supported by
 * that driver.
 */
public class ConnectionManager {
	/**
	 * volatile modifier is used here to ensure the ConnectionManager, when being
	 * constructed by JVM, will be locked by current thread until the finish of
	 * construction.
	 */
	private static volatile ConnectionManager sm_instance = null;

	private static final String DTP_CONN_PROFILE_APPL_ID = Constants.CONN_PROFILE_APPL_ID;

	// trace logging variables
	private static final String sm_className = ConnectionManager.class.getName();
	static final String sm_packageName = ExceptionHandler.sm_packageName;
	private static LogHelper sm_logger = null;

	protected ConnectionManager() {
	}

	/**
	 * Returns a <code>ConnectionManager</code> instance for getting opened
	 * <code>Connections</code>.
	 *
	 * @return a <code>ConnectionManager</code> instance.
	 */
	public static ConnectionManager getInstance() {
		final String methodName = "getInstance"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName);

		if (sm_instance == null) {
			synchronized (ConnectionManager.class) {
				if (sm_instance == null) {
					sm_instance = new ConnectionManager();
				}
			}
		}

		getLogger().exiting(sm_className, methodName, sm_instance);
		return sm_instance;
	}

	/**
	 * Singleton instance release method.
	 */
	public static void releaseInstance() {
		DriverManager.releaseInstance();
		sm_instance = null;
		sm_logger = null;
	}

	private static LogHelper getLogger() {
		if (sm_logger == null) {
			synchronized (ConnectionManager.class) {
				if (sm_logger == null) {
					sm_logger = LogHelper.getInstance(sm_packageName);
				}
			}
		}

		return sm_logger;
	}

	/**
	 * Returns an opened <code>Connection</code> that is supported by the specified
	 * data source extension using the specified connection properties.
	 *
	 * @param dataSourceElementId  id of the data source element defined in the data
	 *                             source extension.
	 * @param connectionProperties connection properties to open the underlying
	 *                             connection.
	 * @return an opened <code>Connection</code> instance.
	 * @throws DataException if data source error occurs.
	 * @deprecated since 2.2
	 */
	@Deprecated
	public Connection openConnection(String dataSourceElementId, Properties connectionProperties) throws DataException {
		return openConnection(dataSourceElementId, connectionProperties, null);
	}

	/**
	 * Same functionality as the first openConnection method, but with an additional
	 * argument to pass in an application context to the underlying ODA driver.
	 *
	 * @param appContext Application context map to pass thru to the underlying ODA
	 *                   driver.
	 * @return an opened <code>Connection</code> instance.
	 * @throws DataException if data source error occurs
	 */
	public Connection openConnection(String dataSourceElementId, Properties connectionProperties, Map appContext)
			throws DataException {
		final String methodName = "openConnection"; //$NON-NLS-1$

		if (getLogger().isLoggingEnterExitLevel()) {
			getLogger().entering(sm_className, methodName, new Object[] { dataSourceElementId, connectionProperties });
		}

		try {
			DriverManager driverMgr = DriverManager.getInstance();

			// gets the driver helper to get a connection to open;
			// a driver helper instance for the same dataSourceElementId can normally be
			// shared,
			// except when appContext exists, in which case a dedicated driver helper would
			// be needed
			IDriver driverHelper = (appContext != null && !appContext.isEmpty())
					? driverMgr.getNewDriverHelper(dataSourceElementId, appContext)
					: driverMgr.getDriverHelper(dataSourceElementId);

			// specifies default connection profile property provider service
			appContext = addProfileProviderService(appContext);

			// before calling getConnection, passes application context
			// to the oda driver helper, which in turn takes care of
			// passing thru to the driver's connection(s) and quer(ies);
			// locale setting in appContext is handled by DTP ODA consumer component during
			// #open
			driverHelper.setAppContext(appContext);

			String dataSourceId = driverMgr.getExtensionDataSourceId(dataSourceElementId);
			IConnection connection = driverHelper.getConnection(dataSourceId);
			connection.open(connectionProperties);

			Connection ret = new Connection(connection, dataSourceElementId);

			getLogger().exiting(sm_className, methodName, ret);
			return ret;
		} catch (OdaException | UnsupportedOperationException ex) {
			getLogger().logp(Level.SEVERE, sm_className, methodName, "Unable to open connection.", ex); //$NON-NLS-1$

			throw ExceptionHandler.newException(ResourceConstants.CANNOT_OPEN_CONNECTION,
					new Object[] { dataSourceElementId }, ex);
		}
	}

	/**
	 * Adds default connection profile property provider service, if none is already
	 * defined in the appContext object. This will trigger the use of the DTP ODA
	 * framework service to apply the connection property values defined in an
	 * external connection profile store, for opening a connection.
	 *
	 * @param appContext application context object passed thru into the data engine
	 * @return updated application context object for passing thru to the DTP
	 *         oda.consumer
	 */
	@SuppressWarnings("unchecked")
	static Map addProfileProviderService(Map appContext) {
		Map providerAppContext = appContext;
		if (providerAppContext == null) {
			providerAppContext = new HashMap();
		}

		// if externally-provided appContext has not specified own consumer id for
		// a property provider extension, add the default ODA provider extension
		// to use a linked connection profile's properties
		if (!providerAppContext.containsKey(IPropertyProvider.ODA_CONSUMER_ID)) {
			providerAppContext.put(IPropertyProvider.ODA_CONSUMER_ID, DTP_CONN_PROFILE_APPL_ID);
			if (getLogger().isLoggable(Level.FINER)) {
				getLogger().logp(Level.FINER, sm_className, "addProfileProviderService( Map )", //$NON-NLS-1$
						"Added default property service: " + DTP_CONN_PROFILE_APPL_ID); //$NON-NLS-1$
			}
		}

		return providerAppContext;
	}

	/**
	 * Returns the maximum number of active connections that the driver can support.
	 *
	 * @return the maximum number of connections that can be opened concurrently, or
	 *         0 if there is no limit or the limit is unknown.
	 * @throws DataException if data source error occurs.
	 */
	public int getMaxConnections(String driverName) throws DataException {
		final String methodName = "getMaxConnections"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, driverName);

		int maxConnections = 0; // default to unknown limit
		try {
			IDriver driverHelper = DriverManager.getInstance().getDriverHelper(driverName);
			if (driverHelper != null) {
				maxConnections = driverHelper.getMaxConnections();
			}
		} catch (OdaException ex) {
			getLogger().logp(Level.WARNING, sm_className, methodName, "Cannot get max connections.", ex); //$NON-NLS-1$
			maxConnections = 0;
		} catch (UnsupportedOperationException ex) {
			getLogger().logp(Level.INFO, sm_className, methodName, "Cannot get max connections.", ex); //$NON-NLS-1$
			maxConnections = 0;
		}

		getLogger().exiting(sm_className, methodName, maxConnections);
		return maxConnections;
	}

}
