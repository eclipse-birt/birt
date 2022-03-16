/*
 *************************************************************************
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
 *************************************************************************
 */

package org.eclipse.birt.data.engine.odaconsumer;

import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.helper.OdaDriver;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * Each <code>Driver</code> maintains the state of a driver in the drivers home
 * directory.
 */
class Driver {
	private String m_dataSourceDriverId;
	private ExtensionManifest m_driverConfig;
	private IDriver m_driverHelper;

	// trace logging variables
	private static final String sm_className = Driver.class.getName();
	private static final String sm_loggerName = ConnectionManager.sm_packageName;
	private static LogHelper sm_logger = LogHelper.getInstance(sm_loggerName);

	Driver(String dataSourceElementId) {
		final String methodName = "Driver"; //$NON-NLS-1$
		sm_logger.entering(sm_className, methodName, dataSourceElementId);

		m_dataSourceDriverId = dataSourceElementId;

		sm_logger.exiting(sm_className, methodName, this);
	}

	/**
	 * Returns the manifest that should be passed to the ODA consumer helper to
	 * handle, either a DTP ODA driver or the DTP-to-BIRT adapter.
	 */
	ExtensionManifest getExtensionConfig() throws DataException {
		// get DTP ODA driver manifest
		ExtensionManifest driverManifest = getDriverExtensionConfig();
		assert (driverManifest != null); // otherwise, DataException should have been thrown

		return driverManifest; // manifest of a DTP ODA driver
	}

	/**
	 * Returns the manifest of a DTP ODA driver, or that of a BIRT ODA driver.
	 *
	 * @throws DataException
	 */
	ExtensionManifest getDriverExtensionConfig() throws DataException {
		if (m_driverConfig != null) {
			return m_driverConfig;
		}

		// do lazy initialization;
		// find the driver extension config and initializes member variables
		findDataSourceExtensionConfig();

		assert (m_driverConfig != null); // otherwise, DataException should have been thrown
		return m_driverConfig;
	}

	// gets the consumer manager helper for this driver
	IDriver getDriverHelper() throws DataException {
		// shared driver helper has no sharable appContext
		if (m_driverHelper == null) {
			m_driverHelper = createNewDriverHelper(null);
		}
		return m_driverHelper;
	}

	IDriver createNewDriverHelper(Map<?, ?> appContext) throws DataException {
		final String methodName = "createNewDriverHelper(Map)"; //$NON-NLS-1$
		try {
			return new OdaDriver(getExtensionConfig(), appContext);
		} catch (OdaException ex) {
			sm_logger.logp(Level.SEVERE, sm_className, methodName, "Cannot get ODA data source driver helper.", ex); //$NON-NLS-1$
			throw ExceptionHandler.newException(ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND,
					new Object[] { m_dataSourceDriverId }, ex);
		} catch (UnsupportedOperationException ex) {
			sm_logger.logp(Level.SEVERE, sm_className, methodName, "Cannot get ODA data source driver factory.", ex); //$NON-NLS-1$
			throw ExceptionHandler.newException(ResourceConstants.INIT_ENTRY_CANNOT_BE_FOUND,
					new Object[] { m_dataSourceDriverId }, ex);
		}
	}

	/*
	 * Sets the member variable(s) with value of the DTP ODA driver manifest, or the
	 * manifests of a BIRT ODA driver and its adapter
	 */
	private void findDataSourceExtensionConfig() throws DataException {
		// reset member variable
		m_driverConfig = null;

		// try find extension for org.eclipse.datatools.connectivity.oda.dataSource
		m_driverConfig = doGetDriverManifest(m_dataSourceDriverId, true /* useDtpExtPoint */,
				true /* throwsIfNotFound */ );
	}

	/*
	 * Finds and returns a driver manifest of either the DTP extension point, or the
	 * BIRT one. This methods takes care of catching all exceptions, and in turn
	 * throws a DataException only. The throwsIfNotFound flag, when set to true,
	 * throws a DataException if given driver manifest is not found; if the flag is
	 * set to false, returns null instead.
	 */
	private ExtensionManifest doGetDriverManifest(String dataSourceDriverId, boolean useDtpExtPoint,
			boolean throwsIfNotFound) throws DataException {
		final String methodName = "doGetDriverManifest"; //$NON-NLS-1$

		ManifestExplorer explorer = ManifestExplorer.getInstance();
		try {
			if (useDtpExtPoint) {
				return explorer.getExtensionManifest(dataSourceDriverId);
			}

			// no longer supports BIRT ODA extension point
			if (throwsIfNotFound) { // not found
				throw new IllegalArgumentException(dataSourceDriverId);
			}
			return null;
		} catch (Exception ex) {
			// dataSourceDriverId is not found as a DTP ODA driver
			if (useDtpExtPoint && ex instanceof IllegalArgumentException) {
				if (!throwsIfNotFound) {
					return null; // not an error
				}
			}

			// throws a DataException for driver configuration problem
			return throwConfigException(methodName, dataSourceDriverId, ex);
		}
	}

	private ExtensionManifest throwConfigException(String methodName, String dataSourceDriverId, Throwable cause)
			throws DataException {
		sm_logger.logp(Level.SEVERE, sm_className, methodName,
				"Cannot find or process the ODA data source extension configuration.", cause); //$NON-NLS-1$

		throw ExceptionHandler.newException(ResourceConstants.CANNOT_PROCESS_DRIVER_CONFIG,
				new Object[] { dataSourceDriverId }, cause);
	}

}
