/*
 *************************************************************************
 * Copyright (c) 2011, 2013 Actuate Corporation.
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

import java.util.Properties;

import org.eclipse.datatools.connectivity.ConnectionProfileConstants;
import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCConnectionProfileConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;
import org.eclipse.datatools.connectivity.oda.profile.OdaProfileExplorer;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;

/**
 * Implementation of IPropertyProvider for the SampleDb profile definition type,
 * for use in the org.eclipse.datatools.oda.consumer.propertyProvider extension.
 */
public class SampleDbPropertyProvider implements IPropertyProvider {
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider#
	 * getDataSourceProperties(java.util.Properties, java.lang.Object)
	 */
	public Properties getDataSourceProperties(Properties candidateProperties, Object appContext) throws OdaException {
		// adjusts transient SampleDb profile properties;
		// expects specified candidateProperties to be in db profile property keys

		if (!isTransientSampleDbProfile(candidateProperties))
			return candidateProperties; // use the properties as is

		// Adopts the properties in the default persisted profile that has the local
		// SampleDb path

		/*
		 * BIRT SampleDB uses Embedded Derby, whose DerbyEmbeddedJDBCConnection
		 * implementation tracks connection reference count, which conflicts with how a
		 * shared connection profile instance tracks its connection state. Thus this
		 * will create a separate transient connection profile instance, instead of
		 * sharing the default persisted profile instance for BIRT SampleDB.
		 */
		// specifies the SampleDb profile name to lookup the default persisted profile
		// instance
		// for BIRT SampleDB
		Properties defaultProfileProps = new Properties();
		defaultProfileProps.setProperty(ConnectionProfileProperty.PROFILE_NAME_PROP_KEY,
				SampleDbFactory.getLocalizedSampleDbProfileName());
		IConnectionProfile sampleDbProfile = OdaProfileExplorer.getInstance().getProfileByName(defaultProfileProps,
				appContext);

		// if found, adopts its properties to create a new transient profile
		if (sampleDbProfile != null) {
			defaultProfileProps.clear();
			defaultProfileProps.putAll(candidateProperties);
			defaultProfileProps.putAll(sampleDbProfile.getBaseProperties()); // override with persisted profile
																				// properties
			return defaultProfileProps;
		}

		// not able to find the default persisted SampleDb profile
		return candidateProperties; // use the properties as is
	}

	/*
	 * Checks whether the specified database profile properties is for a transient
	 * instance of the SampleDb profile.
	 */
	private static boolean isTransientSampleDbProfile(Properties dbProfileProps) {
		String profileName = dbProfileProps.getProperty(ConnectionProfileProperty.PROFILE_NAME_PROP_KEY);
		if (profileName != null && profileName.length() > 0) // has profile name
			return false; // not for a transient profile

		// check if the transient profile properties contain reference of
		// the default SampleDb driver definition instance
		String driverDefnId = dbProfileProps.getProperty(ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID, null);
		if (driverDefnId == null || !(driverDefnId.equalsIgnoreCase(SampleDbFactory.SAMPLEDB_DEFAULT_DRIVER_DEFN_ID)
				|| driverDefnId.equalsIgnoreCase(SampleDbFactory.getLocalizedDriverDefinitionId())))
			return false;

		String dbUrl = dbProfileProps.getProperty(IJDBCConnectionProfileConstants.URL_PROP_ID, null);
		if (dbUrl == null || dbUrl.replace('\\', '/').indexOf(SampleDbFactory.SAMPLEDB_URL_RELATIVE_SUFFIX) == -1)
			return false;

		return true;
	}

}
