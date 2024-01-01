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

import java.util.Hashtable;
import java.util.Map;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;

/**
 * Package internal class. <code>DriverManager</code> manages a set of data
 * source drivers. Calling <code>getInstance</code> will return the singleton
 * instance of <code>DriverManager</code>.
 *
 * When the method <code>getDriverHelper</code> is initiated by the
 * <code>ConnectionManager</code>, the <code>DriverManager</code> will attempt
 * to load the specified driver and return a <code>IDriver</code> instance of
 * that driver.
 */
public class DriverManager {
	private static DriverManager sm_driverManager = null;
	private Hashtable<String, Driver> m_loadedDrivers;

	// trace logging variables
	private static final String sm_className = DriverManager.class.getName();
	private static LogHelper sm_logger;

	private DriverManager() {
	}

	/**
	 * Returns a <code>DriverManager</code> instance for loading drivers and
	 * handling driver-related tasks.
	 *
	 * @return a <code>DriverManager</code> instance.
	 */
	public static DriverManager getInstance() {
		if (sm_driverManager == null) {
			synchronized (DriverManager.class) {
				if (sm_driverManager == null) {
					sm_driverManager = new DriverManager();
				}
			}
		}
		return sm_driverManager;
	}

	/**
	 * Singleton instance release method.
	 */
	static void releaseInstance() {
		sm_driverManager = null;
		sm_logger = null;
	}

	private static LogHelper getLogger() {
		if (sm_logger == null) {
			synchronized (DriverManager.class) {
				if (sm_logger == null) {
					sm_logger = LogHelper.getInstance(ConnectionManager.sm_packageName);
				}
			}
		}

		return sm_logger;
	}

	/**
	 * Returns the <code>IDriver</code> based on driverName.
	 *
	 * @param dataSourceElementId the name of the driver.
	 * @return an <code>IDriver</code> instance.
	 */
	public IDriver getDriverHelper(String dataSourceElementId) throws DataException {
		return getDriverHelper(dataSourceElementId, true, null);
	}

	IDriver getNewDriverHelper(String dataSourceElementId, Map<?, ?> appContext) throws DataException {
		return getDriverHelper(dataSourceElementId, false, appContext);
	}

	private IDriver getDriverHelper(String dataSourceElementId, boolean reuseExisting, Map<?, ?> appContext)
			throws DataException {
		final String methodName = "getDriverHelper(String,boolean,Map)"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName,
				new Object[] { dataSourceElementId, Boolean.valueOf(reuseExisting) });

		Driver driver = getDriver(dataSourceElementId);
		IDriver ret = reuseExisting ? driver.getDriverHelper() : driver.createNewDriverHelper(appContext);

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	/**
	 * Returns the id of the type of ODA data source for use as an argument to
	 * <code>IDriver.getConnection</code>.
	 *
	 * @param dataSourceElementId the id of the data source element defined in a
	 *                            data source extension.
	 * @return the extension data source type id for
	 *         <code>IDriver.getConnection</code>, or null if no explicit data
	 *         source type was specified.
	 */
	public String getExtensionDataSourceId(String dataSourceElementId) throws DataException {
		final String methodName = "getExtensionDataSourceId"; //$NON-NLS-1$
		getLogger().entering(sm_className, methodName, dataSourceElementId);

		Driver driver = getDriver(dataSourceElementId);
		ExtensionManifest config = driver.getDriverExtensionConfig();
		String ret = config.getDataSourceElementID();

		getLogger().exiting(sm_className, methodName, ret);
		return ret;
	}

	private Driver getDriver(String dataSourceElementId) {
		assert (dataSourceElementId != null && dataSourceElementId.length() != 0);

		Driver driver = getLoadedDrivers().get(dataSourceElementId);
		if (driver == null) {
			driver = new Driver(dataSourceElementId);
			getLoadedDrivers().put(dataSourceElementId, driver);
		}

		return driver;
	}

	Hashtable<String, Driver> getLoadedDrivers() {
		if (m_loadedDrivers == null) {
			m_loadedDrivers = new Hashtable<>();
		}

		return m_loadedDrivers;
	}

}
