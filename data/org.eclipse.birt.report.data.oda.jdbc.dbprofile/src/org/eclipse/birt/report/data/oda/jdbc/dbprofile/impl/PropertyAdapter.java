/*
 *************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.data.oda.jdbc.dbprofile.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Map.Entry;

import org.eclipse.datatools.connectivity.IConnectionProfile;
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCConnectionProfileConstants;

/**
 * Internal adapter of this driver's ODA data source properties. For internal
 * use only.
 * 
 * @since 2.5.2
 */
public class PropertyAdapter {
	private static final String EMPTY_STRING = ""; //$NON-NLS-1$
	private volatile static Map<String, String> sm_adaptableNames;

	private PropertyAdapter() {
	}

	/**
	 * Adapts persisted data source properties to those needed to create a database
	 * profile.
	 * 
	 * @param connProperties data source properties in their ODA property names
	 * @return a copy of the adapted properties ready to use to create a database
	 *         profile instance
	 */
	public static Properties adaptToDbProfilePropertyNames(Properties connProperties) {
		// makes a copy of the specified properties
		Properties profileProps = new Properties();
		profileProps.putAll(connProperties);

		// adapt the property names to the complete names used by a DTP database profile
		adaptToFullPropertyNames(profileProps);

		/*
		 * works around the limitation with BIRT ROM that has converted an empty
		 * property value to null for private property
		 */
		String dbConnPropValues = profileProps
				.getProperty(IJDBCConnectionProfileConstants.CONNECTION_PROPERTIES_PROP_ID);
		if (dbConnPropValues == null)
			profileProps.setProperty(IJDBCConnectionProfileConstants.CONNECTION_PROPERTIES_PROP_ID, EMPTY_STRING);

		return profileProps;
	}

	/**
	 * Adapts profile properties to those for persisted data source properties.
	 * 
	 * @param profileProperties database profile properties in their full names
	 * @return a copy of the adapted properties ready to use to be saved in a data
	 *         source design
	 */
	public static Properties adaptToDataSourcePropertyNames(Properties profileProperties) {
		// makes a copy of the specified properties
		Properties sourceProps = new Properties();
		sourceProps.putAll(profileProperties);

		// adapts the property names from the complete names used by a DTP database
		// profile
		adaptToOdaPropertyNames(sourceProps);

		// removes the isTransient profile property, if exists
		sourceProps.remove(IConnectionProfile.TRANSIENT_PROPERTY_ID);

		return sourceProps;
	}

	private static void adaptToFullPropertyNames(Properties profilePros) {
		for (Entry<String, String> propertyNames : getAdaptableNames().entrySet()) {
			String currentName = propertyNames.getValue(); // oda prop name
			String newName = propertyNames.getKey(); // full prop name
			changePropertyName(profilePros, currentName, newName);
		}
	}

	private static void adaptToOdaPropertyNames(Properties profilePros) {
		for (Entry<String, String> propertyNames : getAdaptableNames().entrySet()) {
			String currentName = propertyNames.getKey(); // full prop name
			String newName = propertyNames.getValue(); // oda prop name
			changePropertyName(profilePros, currentName, newName);
		}
	}

	private static void changePropertyName(Properties profilePros, String currentName, String newName) {
		// remove the entry of the current property name; and if exists, add in new
		// entry with the new name
		Object propValue = profilePros.remove(currentName);
		if (propValue != null)
			profilePros.put(newName, propValue);
	}

	/*
	 * Returns a map of all adaptable property names, with the full property name as
	 * key, and Oda property name as value
	 */
	private static Map<String, String> getAdaptableNames() {
		/*
		 * BIRT ROM property names do not allow embedded special character such as a
		 * period. Thus this adapts all the database profile properties, which may be
		 * exposed as ROM properties, to use just the name suffix as the ODA driver
		 * property names in a data source design.
		 */
		if (sm_adaptableNames == null) {
			synchronized (PropertyAdapter.class) {
				if (sm_adaptableNames == null) {
					sm_adaptableNames = new HashMap<String, String>(6);

					String fullPropName = IJDBCConnectionProfileConstants.DATABASE_NAME_PROP_ID;
					sm_adaptableNames.put(fullPropName, convertToOdaPropertyName(fullPropName));
					fullPropName = IJDBCConnectionProfileConstants.USERNAME_PROP_ID;
					sm_adaptableNames.put(fullPropName, convertToOdaPropertyName(fullPropName));
					fullPropName = IJDBCConnectionProfileConstants.PASSWORD_PROP_ID;
					sm_adaptableNames.put(fullPropName, convertToOdaPropertyName(fullPropName));
					fullPropName = IJDBCConnectionProfileConstants.URL_PROP_ID;
					sm_adaptableNames.put(fullPropName, convertToOdaPropertyName(fullPropName));
					fullPropName = IJDBCConnectionProfileConstants.DRIVER_CLASS_PROP_ID;
					sm_adaptableNames.put(fullPropName, convertToOdaPropertyName(fullPropName));
				}
			}
		}
		return sm_adaptableNames;
	}

	private static String convertToOdaPropertyName(String fullPropName) {
		// the suffix of the full property name is the oda data source property name
		return fullPropName.substring(fullPropName.lastIndexOf('.') + 1);
	}

}
