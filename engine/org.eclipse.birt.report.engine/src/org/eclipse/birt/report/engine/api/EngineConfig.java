/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.IPlatformContext;
import org.eclipse.birt.core.framework.PlatformConfig;
import org.eclipse.birt.report.model.api.IResourceLocator;

/**
 * Wraps around configuration settings for report engine. Allows developers to
 * specify where to look for engine plugins, data drivers, and where to write
 * image files. Allows users to customize data-related properties (i.e., data
 * engine). Also allows engine to provide customized implementations for image
 * handling, hyperlink handling and font handling, etc.
 */
public class EngineConfig extends PlatformConfig implements IEngineConfig
{

	/**
	 * constructor
	 */
	public EngineConfig( )
	{
		// set default configruation
		HTMLEmitterConfig emitterConfig = new HTMLEmitterConfig( );
		emitterConfig.setActionHandler( new HTMLActionHandler( ) );
		emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		getEmitterConfigs( ).put( "html", emitterConfig ); //$NON-NLS-1$
	}

	/**
	 * set the BIRT_HOME system property
	 * 
	 * @param birtHome
	 *            the value for the BIRT_HOMT configuration variable
	 * @deprecated, use setBIRTHome instead.
	 */
	public void setEngineHome( String birtHome )
	{
		setProperty( BIRT_HOME, birtHome );
	}

	/**
	 * sets a configuration variable that is available through scripting in
	 * engine
	 * 
	 * @param name
	 *            configuration variable name
	 * @param value
	 *            configuration variable value
	 * @deprecated use setProperty() instead.
	 */
	public void setConfigurationVariable( String name, String value )
	{
		setProperty( name, value );
	}

	/**
	 * returns a hash map that contains all the configuration objects
	 * 
	 * @return the configuration object map
	 * @deprecated use getProperties() instead.
	 */
	public HashMap getConfigMap( )
	{
		return properties;
	}

	/**
	 * set log configuration, i.e., log file name prefix and log level
	 * 
	 * @param directoryName -
	 *            the directory name of the log file(e.g C:\Log). Engine appends
	 *            a file name with date and time to the directory name (e.g.
	 *            C:\Log\BIRT_Engine_2005_02_26_11_26_56.log).
	 * @param level
	 *            the engine log level
	 */
	public void setLogConfig( String directoryName, Level level )
	{
		setProperty( LOG_DESTINATION, directoryName );
		setProperty( LOG_LEVEL, level );
	}

	/**
	 * set the logger used by the report engine.
	 * 
	 * the logger will overides the setLogConfig().
	 * 
	 * @param logger
	 */
	public void setLogger( Logger logger )
	{
		if ( logger == null )
		{
			throw new NullPointerException( "logger can't be NULL" );
		}
		setProperty( ENGINE_LOGGER, logger );
	}

	/**
	 * return the user's logger set through setLogger.
	 * 
	 * @return the logger setted by user. NULL if the user doesn't set it.
	 */
	public Logger getLogger( )
	{
		Object logger = getProperty( ENGINE_LOGGER );
		if ( logger instanceof Logger )
		{
			return (Logger) logger;
		}
		return null;
	}

	/**
	 * returns a hash map that contains all the app-specific, app-wide
	 * scriptable Java objects
	 * 
	 * @return a hash map with all the app-specific, app-wide scriptable Java
	 *         objects
	 * @deprecated use getAppContext() instead.
	 */
	public HashMap getScriptObjects( )
	{
		return getAppContext( );
	}

	/**
	 * defines an additional Java object that is exposed to BIRT scripting
	 * 
	 * @param jsName
	 *            the name that the object is referenced in JavaScript
	 * @param obj
	 *            the Java object that is wrapped and scripted
	 * @deprecated the user need add the object to the appContext directly.
	 */
	public void addScriptableJavaObject( String jsName, Object obj )
	{
		getScriptObjects( ).put( jsName, obj );
	}
	
	public HashMap getAppContext()
	{
		HashMap appContext = (HashMap) getProperty( SCRIPT_OBJECTS );
		if ( appContext == null )
		{
			appContext = new HashMap( );
			setProperty( SCRIPT_OBJECTS, appContext );
		}
		return appContext;
	}
	
	public void setAppContext(HashMap appContext)
	{
		setProperty( SCRIPT_OBJECTS, appContext );
	}

	/**
	 * sets configuration for a specific extension to engine, i.e., an emitter
	 * extension
	 * 
	 * @param extensionID
	 *            identifier for the emitter
	 * @param extensionConfig
	 *            configuration object for the emitter, which must be an
	 *            instance of HashMap or IRenderOption
	 */
	public void setEmitterConfiguration( String format, Object emitterConfig )
	{
		if ( emitterConfig instanceof HashMap )
		{
			getEmitterConfigs( ).put( format,
					new RenderOption( (HashMap) emitterConfig ) );
		}
		else if ( emitterConfig instanceof IRenderOption )
		{
			getEmitterConfigs( ).put( format, emitterConfig );
		}
		else
		{
			throw new IllegalArgumentException(
					"the agr1 must be an instance of HashMap or IRenderOption" );
		}
	}

	/**
	 * gets a map for emitter configuration objects
	 * 
	 * @return emitter configuration
	 */
	public HashMap getEmitterConfigs( )
	{
		HashMap emitterConfigs = (HashMap) getProperty( EMITTER_CONFIGS );
		if ( emitterConfigs == null )
		{
			emitterConfigs = new HashMap( );
			setProperty( EMITTER_CONFIGS, emitterConfigs );
		}
		return emitterConfigs;
	}

	/**
	 * returns the status handler
	 * 
	 * @return the status handler
	 */
	public IStatusHandler getStatusHandler( )
	{
		IStatusHandler statusHandler = (IStatusHandler) getProperty( STATUS_HANDLER );
		if ( statusHandler == null )
		{
			statusHandler = new DefaultStatusHandler( );
			setProperty( STATUS_HANDLER, statusHandler );
		}
		return statusHandler;
	}

	/**
	 * sets the handler for reporting report running status.
	 * 
	 * @param handler
	 *            status handler
	 */
	public void setStatusHandler( IStatusHandler handler )
	{
		setProperty( STATUS_HANDLER, handler );
	}

	public Level getLogLevel( )
	{
		Level level = (Level) getProperty( LOG_LEVEL );

		return level;
	}

	public String getLogDirectory( )
	{
		String logDestination = (String) getProperty( LOG_DESTINATION );
		return logDestination;
	}
	
	public String getLogFile( )
	{
		String logFile = (String) getProperty( LOG_FILE );
		return logFile;
	}
	
	public void setLogFile( String filename )
	{
		setProperty( LOG_FILE, filename );
	}

	/**
	 * sets the directory for temporary files
	 * 
	 * @param tmpDir
	 *            the directory for temporary files
	 */
	public void setTempDir( String tmpDir )
	{
		setProperty( TEMP_DIR, tmpDir );
	}

	/**
	 * returns engine temporary directory for temporary files
	 * 
	 * @return Returns the Temp Directory for engine to write temp files
	 */
	public String getTempDir( )
	{
		String tempDir = (String) getProperty( TEMP_DIR );
		if (tempDir == null)
		{
			tempDir = System.getProperty( "java.io.tmpdir" );
		}
		return tempDir;
	}

	/**
	 * return a lock manager. The lock manager is used to lock the report
	 * document opened by this engine.
	 * 
	 * @return lock manager, NULL if not set.
	 */
	public IReportDocumentLockManager getReportDocumentLockManager( )
	{
		Object manager = getProperty( REPORT_DOCUMENT_LOCK_MANAGER );
		if ( manager instanceof IReportDocumentLockManager )
		{
			return (IReportDocumentLockManager) manager;
		}
		return null;
	}

	public void setReportDocumentLockManager( IReportDocumentLockManager manager )
	{
		setProperty( REPORT_DOCUMENT_LOCK_MANAGER, manager );
	}

	/**
	 * set the framework context
	 * 
	 * @param context
	 *            the platform context
	 * @deprecated use setPlatformContext instead.
	 */
	public void setEngineContext( IPlatformContext context )
	{
		setPlatformContext( context );
	}

	/**
	 * set the framework context
	 * 
	 * @return context, the framework context
	 * @deprecated use getPlatformContext instead
	 */
	public IPlatformContext getServletContext( )
	{
		return getPlatformContext( );
	}

	/**
	 * @return the resourceLocator
	 */
	public IResourceLocator getResourceLocator( )
	{
		Object locator = getProperty( RESOURCE_LOCATOR );
		if ( locator instanceof IResourceLocator )
		{
			return (IResourceLocator) locator;
		}
		return null;
	}
	
	/**
	 * @param resourceLocator the resourceLocator to set
	 */
	public void setResourceLocator( IResourceLocator resourceLocator )
	{
		setProperty( RESOURCE_LOCATOR, resourceLocator );
	}

	/**
	 * @return the resourcePath
	 */
	public String getResourcePath( )
	{
		Object resourcePath = getProperty( RESOURCE_PATH );
		if ( resourcePath instanceof String )
		{
			return (String) resourcePath;
		}
		return null;
	}
	
	/**
	 * @param resourcePath the resourcePath to set
	 */
	public void setResourcePath( String resourcePath )
	{
		setProperty( RESOURCE_PATH, resourcePath );
	}
	
	/**
	 * Set the max rows per query
	 * @param maxRows: max rows
	 */
	public void setMaxRowsPerQuery( int maxRows )
	{
		setProperty( MAX_ROWS_PER_QUERY, new Integer(maxRows) );
	}
	
	/**
	 * Get the max rows per query
	 * @return the max rows per query
	 */
	public int getMaxRowsPerQuery()
	{
		Object maxRows = getProperty( MAX_ROWS_PER_QUERY );
		if ( maxRows instanceof Integer )
		{
			return ( (Integer) maxRows ).intValue( );
		}
		return 0;
	}
	
	/**
	 * Output properties for debug tracing
	 * @see java.lang.Object#toString()
	 */
	public String toString()
	{
		String str = "EngineConfig: ";
		if ( properties == null )
		{
			str += "null";
		}
		else
		{
			Iterator entryIt = properties.entrySet().iterator();
			while ( entryIt.hasNext() )
			{
				Map.Entry entry = (Map.Entry)entryIt.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				str += key == null ? "<null>" : key.toString();
				str += "=";
				str += value == null? "<null>" : value.toString();
				str += ";";
			}
		}
		return str;
	}
}
