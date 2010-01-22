/*
 *************************************************************************
 * Copyright (c) 2008, 2010 Actuate Corporation.
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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb.nls.Messages;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;

/**
 *  The factory class that creates the BIRT SampleDB Derby Embedded Database specified by the 
 *  sample database connection profile.
 *  It is specified as the connectionInitializer class in the extension that implements the
 *  org.eclipse.datatools.connectivity.ProfileManagerInitializationProvider extension point.
 */
public class SampleDbFactory implements IExecutableExtension
{
    private static final String PLUGIN_ID = "org.eclipse.birt.report.data.oda.jdbc.dbprofile.sampledb"; //$NON-NLS-1$
    private static final String SAMPLEDB_PLUGIN_ID = "org.eclipse.birt.report.data.oda.sampledb"; //$NON-NLS-1$
    private static final String SAMPLE_DB_HOME_SUBDIR = "db"; //$NON-NLS-1$
    private static final String SAMPLE_DB_NAME = "BirtSample"; //$NON-NLS-1$
    private static final String SAMPLE_DB_JAR_FILE = "BirtSample.jar"; //$NON-NLS-1$
    private static final String SAMPLE_DB_LOG = "log"; //$NON-NLS-1$
    private static final String SAMPLE_DB_SEG = "seg0"; //$NON-NLS-1$
    private static final String PATH_SEPARATOR = "/"; //$NON-NLS-1$
    
    // key and name of pre-defined driver definition, as specified in the 
    // org.eclipse.datatools.connectivity.ProfileManagerInitializationProvider extension
    private static final String DRIVER_DEFN_NAME_KEY = "%driver.definition.name"; //$NON-NLS-1$
    private static final String SAMPLEDB_DRIVER_DEFN_DEFAULT_NAME = "BIRT SampleDb Derby Embedded Driver"; //$NON-NLS-1$
    private static final String SAMPLEDB_DRIVER_DEFN_RESOURCE_KEY = 
        DRIVER_DEFN_NAME_KEY + " " + SAMPLEDB_DRIVER_DEFN_DEFAULT_NAME; //$NON-NLS-1$

    /* (non-Javadoc)
     * @see org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org.eclipse.core.runtime.IConfigurationElement, java.lang.String, java.lang.Object)
     */
    public void setInitializationData( IConfigurationElement config,
            String propertyName, Object data ) throws CoreException
    {
        Bundle sampledbBundle = Platform.getBundle( SAMPLEDB_PLUGIN_ID);
        String stateLocation = Platform.getStateLocation( Platform.getBundle( 
                config.getDeclaringExtension().getNamespaceIdentifier())).toOSString();

        try
        {
            initSampleDb( sampledbBundle, stateLocation );
            
            // remove the sampledb driver instance if its jar path is obsolete, so 
            // a new driver instance with the correct jarList will get created automatically 
            // by DTP profile manager
            removeObsoleteDriverDefinition();   
        }
        catch( RuntimeException ex )
        {
            ex.printStackTrace();
            throw new CoreException( (IStatus) new Status( 
                    IStatus.ERROR, PLUGIN_ID, ex.getLocalizedMessage(), ex ) );
        }
        catch( IOException ex )
        {
            ex.printStackTrace();
            throw new CoreException( (IStatus) new Status( 
                    IStatus.ERROR, PLUGIN_ID, ex.getLocalizedMessage(), ex ) );
        }
    }
    
    private void initSampleDb( Bundle sampledbBundle, String rootPath ) throws IOException, IllegalArgumentException
    {
        if( sampledbBundle == null )
            throw new IllegalArgumentException( "null sampledbBundle" ); //$NON-NLS-1$
        if( rootPath == null || rootPath.length() == 0 )
            throw new IllegalArgumentException( Messages.bind( Messages.sampleDbFactory_invalidDirectory, rootPath ));
            
        File dbDir = new File( rootPath + PATH_SEPARATOR + SAMPLE_DB_HOME_SUBDIR );
        if( dbDir.exists() )
        {
            // check if contains BirtSample sub-directory
            File[] subDir = dbDir.listFiles( new FilenameFilter()
            {
                public boolean accept( File dir, String name ) 
                {
                    return name.equals( SAMPLE_DB_NAME );
                }
            } );
            if( subDir == null || subDir.length > 1 )  // filter should have returned empty or one File in subDir
                throw new IllegalArgumentException( Messages.bind( Messages.sampleDbFactory_invalidDirectory, dbDir.toString() ));

            if( subDir.length == 1 ) 
            {
                File sampleDb = subDir[0];
                if( ! sampleDb.isDirectory() )
                    throw new IllegalArgumentException( Messages.bind( Messages.sampleDbFactory_invalidDirectory, sampleDb.toString() ));

                // check if BirtSample contains db content
                File[] sampleDbFiles = sampleDb.listFiles( new FilenameFilter()
                {
                    public boolean accept( File dir, String name ) 
                    {
                        return name.equals( SAMPLE_DB_LOG ) || name.equals( SAMPLE_DB_SEG );
                    }
                } );
                
                if( sampleDbFiles.length == 2 )
                    return;     // done; BirtSample already exists
            }
        }
          
        if( ! dbDir.exists() )
            dbDir.mkdir();
        
        // unpack copy of Sample DB under rootPath/db

        // Get an input stream to read DB Jar file from sampledb plugin
        String dbJarEntryName = SAMPLE_DB_HOME_SUBDIR + PATH_SEPARATOR + SAMPLE_DB_JAR_FILE;
        URL fileURL = sampledbBundle.getEntry( dbJarEntryName );
        if ( fileURL == null )
        {
            throw new RuntimeException( Messages.bind( Messages.sampleDbFactory_noSampleDbJarFile, dbJarEntryName ) );
        }

        // Copy entries in the DB jar file to corresponding location in db dir
        InputStream dbFileStream = new BufferedInputStream( fileURL.openStream( ) );
        ZipInputStream zipStream = new ZipInputStream( dbFileStream );
        ZipEntry entry;
        while ( (entry = zipStream.getNextEntry()) != null )
        {
            File entryFile = new File( dbDir, entry.getName() );
            if ( entry.isDirectory() )
            {
                entryFile.mkdir();
            }
            else
            {
                // Copy zip entry to local file
                OutputStream os = new FileOutputStream( entryFile );
                byte[] buf = new byte[4000];
                int len;
                while ( (len = zipStream.read(buf)) > 0) 
                {
                    os.write(buf, 0, len);
                }
                os.close();
            }
        }
            
        zipStream.close();
        dbFileStream.close();
    }
    
    private void removeObsoleteDriverDefinition()
    {
        Bundle myBundle = Platform.getBundle( PLUGIN_ID );
        String driverDefnName = Platform.getResourceString( myBundle, SAMPLEDB_DRIVER_DEFN_RESOURCE_KEY );

        // remove the driver definition instance if it is invalid
        ProfileDriverUtil.removeInvalidDriverDefinition( driverDefnName );
    }
    
}
