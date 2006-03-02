/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.data.oda.jdbc;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Timestamp;
import com.ibm.icu.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Logger;

import org.eclipse.datatools.connectivity.oda.IConnection;
import org.eclipse.datatools.connectivity.oda.IDriver;
import org.eclipse.datatools.connectivity.oda.LogConfiguration;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.logging.Level;
import org.eclipse.datatools.connectivity.oda.util.manifest.ExtensionManifest;
import org.eclipse.datatools.connectivity.oda.util.manifest.ManifestExplorer;

/**
 * This class implements IDriver, which is the entry point for the
 * ODA consumer.
 * 
 */
public class OdaJdbcDriver implements IDriver
{
    private static String className = OdaJdbcDriver.class.getName();
	private static Logger logger = Logger.getLogger( className );	

	public static final class Constants
	{
		/** Name of directory that contains user provided JDBC drivers */
		public static final String DRIVER_DIRECTORY = "drivers";
		
		/** ODA data source ID; must match value of dataSource.id attribute defined in extension */
		public static final String DATA_SOURCE_ID = "org.eclipse.birt.report.data.oda.jdbc";
		
		/** Full name of driverinfo extension */
		public static final String DRIVER_INFO_EXTENSION = "org.eclipse.birt.report.data.oda.jdbc.driverinfo";
		
		public static final String DRIVER_INFO_ATTR_NAME = "name";
		public static final String DRIVER_INFO_ATTR_DRIVERCLASS= "driverClass";
		public static final String DRIVER_INFO_ATTR_URLTEMPL = "urlTemplate";
		public static final String DRIVER_INFO_ATTR_CONNFACTORY = "connectionFactory";
		public static final String DRIVER_INFO_ELEM_JDBCDRIVER = "jdbcDriver";
		
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.datatools.connectivity.IDriver#getConnection(java.lang.String)
	 */
	public IConnection getConnection( String connectionClassName )
			throws OdaException
	{
		logger.logp( java.util.logging.Level.FINE,
		        className,
				"getConnection",
				"JDBCConnectionFactory.getConnection( ) connectionClassName=" + connectionClassName);
		return new Connection( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.datatools.connectivity.IDriver#getMaxConnections()
	 */
	public int getMaxConnections() throws OdaException
	{
		// The max # of connection is unknown to this Driver; each JDBC driver can have a limit
		// on the # of connection. This Driver can connect to mulitple JDBC drivers.
		return 0;
	}
	
	/*
	 * @see org.eclipse.datatools.connectivity.oda.IDriver#setAppContext(java.lang.Object)
	 */
	public void setAppContext( Object context ) throws OdaException
	{
	    // do nothing; no support for pass-through application context
	}	
	
	/**
	 * @see org.eclipse.datatools.connectivity.IDriver#setLogConfiguration(org.eclipse.birt.data.oda.LogConfiguration)
	 */
	public void setLogConfiguration( LogConfiguration logConfig )
			throws OdaException
	{
		final String methodName = "setLogConfiguration";
		
	    // Get logger for this driver package
		String className = OdaJdbcDriver.class.getName( );
		Logger pkgLogger = Logger.getLogger( className.substring( 0,
				className.lastIndexOf( "." ) ) );
        
        // determine driver package log level;
        // if a valid value is configured, set it in package logger
        switch( logConfig.getLogLevel() )
        {
        case Level.ALL:
            pkgLogger.setLevel( java.util.logging.Level.ALL );
            break;
        case Level.FINEST:
            pkgLogger.setLevel( java.util.logging.Level.FINEST );
            break;
        case Level.FINER:
            pkgLogger.setLevel( java.util.logging.Level.FINER );
            break;
        case Level.FINE:
            pkgLogger.setLevel( java.util.logging.Level.FINE );
            break;
        case Level.CONFIG:
            pkgLogger.setLevel( java.util.logging.Level.CONFIG );
            break;
        case Level.INFO:
            pkgLogger.setLevel( java.util.logging.Level.INFO );
            break;
        case Level.WARNING:
            pkgLogger.setLevel( java.util.logging.Level.WARNING );
            break;
        case Level.SEVERE:
            pkgLogger.setLevel( java.util.logging.Level.SEVERE );
            break;
        case Level.OFF:
            pkgLogger.setLevel( java.util.logging.Level.OFF );
            break;
        default:
        	{
            if( logConfig.getLogLevel() > Level.SEVERE )
                pkgLogger.setLevel( java.util.logging.Level.OFF );
            else
            	// preserve the existing log level
                logger.logp( java.util.logging.Level.WARNING, className,
                    methodName, logConfig.getLogLevel() + " is not a valid log level." );
            break;
        	}
        }
                
    	// if logging is OFF, no need to setup package handler or formatter
        if( pkgLogger.getLevel() == java.util.logging.Level.OFF )
            return;		// done

        // Create handler, if one doesn't already exist
        Handler handler = setLogHandler( pkgLogger, logConfig );
        if( handler == null )
        {
            logger.logp( java.util.logging.Level.WARNING, className,
                    methodName, "Cannot create log handler for package." );
            return;
        }

        // set handler log level to that of package logger
        if( pkgLogger.getLevel() != null )
            handler.setLevel( pkgLogger.getLevel() );
        
        // setup log formatter, if configured
        
        String formatterClassName = logConfig.getFormatterClassName();
        if( formatterClassName == null || formatterClassName.length() == 0 )
        {
            return; // done, no need to set log formatter
        }

        // if existing formatter is of the same type as
        // configured formatter class, we are done
        if( handler.getFormatter() != null &&
            formatterClassName.equals( 
                    handler.getFormatter().getClass().getName() ) )
        {
            return;
        }

        // assign new formatter to handler
        try
        {
            Class formatterClass = Class.forName( formatterClassName );
            handler.setFormatter( (Formatter) formatterClass.newInstance() );
        }
        catch( Exception ex )
        {
            logger.logp( java.util.logging.Level.WARNING, className,
                    methodName, "Cannot setup Formatter object.", ex );
        }
	}

	private static URL getInstallDirectory() throws OdaException, IOException
	{
		ExtensionManifest extMF = null;
		try
        {
            extMF = ManifestExplorer.getInstance().
            			getExtensionManifest( Constants.DATA_SOURCE_ID );
        }
        catch( IllegalArgumentException e )
        {
            // ignore and continue to return null
        }
		if ( extMF != null )
		    return extMF.getDriverLocation();
		return null;
	}
	
	/**
     * Gets the location of the "drivers" subdirectory of this plugin
     */
	public static File getDriverDirectory() throws OdaException, IOException
	{
	    URL url = getInstallDirectory();
		if ( url == null )
		    return null;

		File result = null;
	    try
		{
	    	URI uri = new URI( url.toString() );
			result = new File( uri.getPath(), Constants.DRIVER_DIRECTORY );
		}
		catch ( URISyntaxException e )
		{
			result = new File( url.getFile(), Constants.DRIVER_DIRECTORY );
		}
		
		return result;
	}
	
	/**
	 * Lists all possible driver files (those ending with .zip or .jar) in the drivers directory
	 */
	public static List getDriverFileList() throws OdaException, IOException
	{
		File driverHomeDir = getDriverDirectory();
		String files[] = driverHomeDir.list( new FilenameFilter() {
					public boolean accept( File dir,String name )
					{	return isDriverFile( name ); }
				} );
		List retList = new ArrayList();
		for ( int i = 0; i < files.length; i++ )
		{
			retList.add( new File( driverHomeDir, files[i] ) );
		}
		return retList;
	}
		
    /**
     * Check to see if a file has the correct extension for a JDBC driver. ZIP and JAR files
     * are accepted
     */
	static boolean isDriverFile( String fileName )
	{
		String lcName = fileName.toLowerCase();
		return lcName.endsWith(".jar") || lcName.endsWith(".zip");
	}
	
	/*
	 * Assigns an appropriate handler to the package logger
	 * for the given log configuration, using existing handler when possible. 
	 * If no existing handler is appropriate, add a new handler.
	 * Returns the assigned log handler.
	 */
	private static Handler setLogHandler( Logger pkgLogger, LogConfiguration logConfig )
	{
	    final String methodName = "setLogHandler";
	    
	    Handler handler = null;
        Handler[] handlers = pkgLogger.getHandlers();
	    final int numHandlers = handlers.length;
	    
	    // if insufficient log file info, use a consoleHandler
        String logDirectory = logConfig.getLogDirectory();
        String logPrefix = logConfig.getLogPrefix();
        if ( logDirectory == null || logDirectory.length() == 0 || 
             logPrefix == null || logPrefix.length() == 0 ) 
        {
            // look for an existing console handler
    		for( int i = 0; i < numHandlers; i++ )
    		{
    			handler = handlers[i];
    			if( handler instanceof ConsoleHandler )
    			    return handler;
    		}
    		
            handler = new ConsoleHandler(); 
            pkgLogger.addHandler( handler );
            return handler;
        } 
        
        // use a file handler instead;          
        // first look for an existing file handler
		for( int i = 0; i < numHandlers; i++ )
		{
			handler = handlers[i];
			if( handler instanceof FileHandler )
			    return handler;
		}

		// create a new file handler
		try
        {
            handler = new FileHandler( generateFileName( logDirectory, logPrefix ), true );
            pkgLogger.addHandler( handler );
        }
        catch( Exception ex )
        {
            logger.logp( java.util.logging.Level.WARNING, className,
                    methodName, "Cannot create FileHandler.", ex );
        }
        return handler;		// may be null
	}
       
	/* 
	 * Logic to generate the proper file name:
     * <logDirectory>/<logPrefix>-YYYYMMDD-HHmmss.log
     */
    private static String generateFileName( String logDirectory,
    										String logPrefix )
    {
        // if the log directory is a relative path, the working directory is
        // not necessarily the same as the plugin installation directory;
        // we must ensure that the log files are in the installation directory
		File logDir = new File( logDirectory );
        if ( ( logDir.isDirectory() && ! logDir.isAbsolute() ) ||
             logDirectory.startsWith( "." ) )
        {
            try
            {
        	    URL url = getInstallDirectory();
        	    if( url != null )
        	    {
	                String driverHomeDir = url.getPath();
	                logDir = new File( driverHomeDir, logDirectory );
	                logDirectory = logDir.getPath();
        	    }
            }
            catch( OdaException e )
            {
                // ignore and use original logDirectory
            }
            catch( IOException e )
            {
                // ignore and use original logDirectory
            }
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyyMMdd-HHmmss" );
    	String logfileName = ( logDirectory.endsWith( "/" ) ||
    						   logDirectory.endsWith( "\\" ) ) ?
    						 logDirectory : logDirectory + "/";

    	logfileName += logPrefix + "-";
    	
    	Timestamp timestamp = new Timestamp( System.currentTimeMillis() );
    	logfileName += dateFormat.format( timestamp ) + ".log";
    	
    	return logfileName;
    }
	
}