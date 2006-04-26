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
import java.util.logging.Level;

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
	 */
	public void setConfigurationVariable( String name, String value )
	{
		setProperty( name, value );
	}

	/**
	 * returns a hash map that contains all the configuration objects
	 * 
	 * @return the configuration object map
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
	 * returns a hash map that contains all the app-specific, app-wide
	 * scriptable Java objects
	 * 
	 * @return a hash map with all the app-specific, app-wide scriptable Java
	 *         objects
	 */
	public HashMap getScriptObjects( )
	{
		HashMap scriptObjects = (HashMap) getProperty( SCRIPT_OBJECTS );
		if ( scriptObjects == null )
		{
			scriptObjects = new HashMap( );
			setProperty( SCRIPT_OBJECTS, scriptObjects );
		}
		return scriptObjects;
	}

	/**
	 * defines an additional Java object that is exposed to BIRT scripting
	 * 
	 * @param jsName
	 *            the name that the object is referenced in JavaScript
	 * @param obj
	 *            the Java object that is wrapped and scripted
	 */
	public void addScriptableJavaObject( String jsName, Object obj )
	{
		getScriptObjects( ).put( jsName, obj );
	}

	/**
	 * sets configuration for a specific extension to engine, i.e., an emitter
	 * extension
	 * 
	 * @param extensionID
	 *            identifier for the emitter
	 * @param extensionConfig
	 *            configuration object for the emitter
	 */
	public void setEmitterConfiguration( String format, Object emitterConfig )
	{
		getEmitterConfigs( ).put( format, emitterConfig );
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
}
