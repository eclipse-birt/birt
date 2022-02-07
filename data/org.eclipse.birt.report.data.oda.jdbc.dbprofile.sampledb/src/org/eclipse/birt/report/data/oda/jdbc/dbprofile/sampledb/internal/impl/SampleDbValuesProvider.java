/*
 *************************************************************************
 * Copyright (c) 2010, 2011 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb.internal.impl;

import org.eclipse.datatools.connectivity.apache.internal.derby.driver.DerbyDriverValuesProvider101;
import org.eclipse.datatools.connectivity.drivers.IDriverValuesProvider;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCDriverDefinitionConstants;

/**
 * Provider of property values for BIRT SampleDB Driver definition. It is
 * specified as the driverTemplate element's valuesProvider class in the
 * extension that implements the
 * org.eclipse.datatools.connectivity.driverExtension extension point.
 */
public class SampleDbValuesProvider extends DerbyDriverValuesProvider101 implements IDriverValuesProvider {
	private static final String PLUGIN_STATE_LOCATION = "Plugin_State_Location"; //$NON-NLS-1$

	public SampleDbValuesProvider() {
		super();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.apache.internal.derby.driver.
	 * DerbyDriverValuesProvider101#createDefaultValue(java.lang.String)
	 */
	@Override
	public String createDefaultValue(String key) {
		if (key.equals(IJDBCDriverDefinitionConstants.URL_PROP_ID)) {
			// substitute keyword in the default URL with the BIRT Sampledb URL path
			return getSampleDbURL();
		} else if (key.equals(IDriverValuesProvider.VALUE_CREATE_DEFAULT)) {
			// hold off creating default driver definition until the SampleDB connection
			// profile is created,
			// so the default driver definition will adopt the profile's properites
			return Boolean.toString(false);
		} else if (key.equals(IDriverValuesProvider.VALUE_DEFAULT_DEFINITION_NAME)) {
			// exclude the "Default" suffix added by DriverManager
			String defaultDefnName = getDriverTemplate().getDefaultDefinitionName();
			if (defaultDefnName != null)
				return defaultDefnName;
		}

		return super.createDefaultValue(key);
	}

	private String getSampleDbURL() {
		String dbURL = getDriverTemplate().getPropertyValueFromId(IJDBCDriverDefinitionConstants.URL_PROP_ID);

		int index = dbURL.indexOf(PLUGIN_STATE_LOCATION);
		if (index != -1) // found keyword, substitute it with the sampledb location path
		{
			String sampleDbLocation = SampleDbFactory.getSampleDbRootPath(SampleDbFactory.PLUGIN_ID);
			if (sampleDbLocation != null) {
				dbURL = dbURL.substring(0, index) + sampleDbLocation
						+ dbURL.substring(index + PLUGIN_STATE_LOCATION.length());
			}
		}

		return dbURL;
	}

}
