/*
 *************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *  
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb.internal.impl;

import org.eclipse.datatools.connectivity.drivers.DriverInstance;
import org.eclipse.datatools.connectivity.drivers.DriverManager;
import org.eclipse.datatools.connectivity.drivers.DriverValidator;

/**
 * An internal utility class for database profile-related driver management.
 * 
 * @since 2.5.2
 */
public class ProfileDriverUtil {
	/**
	 * Checks whether the specified driver definition is invalid.
	 * 
	 * @param driverDefnName name of a connectivity driver definition instance
	 * @return true if the specified driver definition exists but invalid; false
	 *         otherwise
	 */
	static boolean isInvalidDriverDefinition(String driverDefnName) {
		if (driverDefnName == null || driverDefnName.length() == 0)
			return false;
		DriverInstance driverInstance = DriverManager.getInstance().getDriverInstanceByName(driverDefnName);
		if (driverInstance == null)
			return false;

		// check if the driver instance is invalid
		return !new DriverValidator(driverInstance).isValid(false);
	}

	/**
	 * Removes the specified driver definition instance, if invalid. This may be
	 * used by a client to clean up its obsolete driver definition, so a new driver
	 * instance with the current jar paths will get created automatically by the
	 * connectivity profile management.
	 * 
	 * @param driverDefnName name of a connectivity driver definition instance
	 * @return true if the specified driver definition is found invalid and is
	 *         successfully removed; false otherwise
	 */
	static boolean removeInvalidDriverDefinition(String driverDefnName) {
		if (isInvalidDriverDefinition(driverDefnName)) {
			DriverManager driverMgr = DriverManager.getInstance();
			DriverInstance driverInstance = driverMgr.getDriverInstanceByName(driverDefnName);
			if (driverInstance != null)
				return driverMgr.removeDriverInstance(driverInstance.getId());
		}
		return false; // driver definition is valid and not removed
	}

}
