/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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
import org.eclipse.datatools.connectivity.drivers.jdbc.IJDBCConnectionProfileConstants;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider;
import org.eclipse.datatools.connectivity.oda.util.manifest.ConnectionProfileProperty;

/**
 * Implementation of IPropertyProvider for the SampleDb profile definition type,
 * for use in the org.eclipse.datatools.oda.consumer.propertyProvider extension.
 */
public class SampleDbPropertyProvider implements IPropertyProvider
{
    /* (non-Javadoc)
     * @see org.eclipse.datatools.connectivity.oda.consumer.services.IPropertyProvider#getDataSourceProperties(java.util.Properties, java.lang.Object)
     */
    public Properties getDataSourceProperties( Properties candidateProperties,
            Object appContext ) throws OdaException
    {
        // adjusts transient SampleDb profile properties;
        // expects specified candidateProperties to be in db profile property keys
        if( ! isTransientSampleDbProfile( candidateProperties ) )
            return candidateProperties;     // use the properties as is

        // adds the default persisted SampleDb profile name to the profile properties,
        // so it will use the default persisted profile for BIRT SampleDB
        Properties effectiveProps = new Properties();
        effectiveProps.putAll( candidateProperties );
        effectiveProps.setProperty( ConnectionProfileProperty.PROFILE_NAME_PROP_KEY,
                                         SampleDbFactory.SAMPLEDB_DEFAULT_PROFILE_NAME );
        return effectiveProps;  // returns a new instance for the adjusted profile properties
    }

    /*
     * Checks whether the specified database profile properties is for
     * a transient instance of the SampleDb profile.
     */
    private static boolean isTransientSampleDbProfile( Properties dbProfileProps )
    {
        String profileName =
            dbProfileProps.getProperty( ConnectionProfileProperty.PROFILE_NAME_PROP_KEY );
        if( profileName != null && profileName.length() > 0 )   // has profile name
            return false;   // not for a transient profile

        // check if the transient profile properties contain reference of
        // the default SampleDb driver definition instance
        String driverDefnId = dbProfileProps.getProperty(
                ConnectionProfileConstants.PROP_DRIVER_DEFINITION_ID, null );
        if( driverDefnId == null ||
            ! driverDefnId.equalsIgnoreCase( SampleDbFactory.SAMPLEDB_DRIVER_DEFN_ID ) )
            return false;

        String dbUrl = dbProfileProps.getProperty( IJDBCConnectionProfileConstants.URL_PROP_ID, null );
        if( dbUrl == null || ! dbUrl.endsWith( SampleDbFactory.SAMPLEDB_URL_RELATIVE_SUFFIX ) )
            return false;

        return true;
    }

}
