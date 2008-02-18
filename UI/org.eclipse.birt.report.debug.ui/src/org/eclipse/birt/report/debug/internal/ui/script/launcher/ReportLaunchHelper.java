/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.script.launcher;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.debug.internal.core.launcher.IReportLaunchConstants;
import org.eclipse.birt.report.debug.internal.core.launcher.LauncherEngineConfig;
import org.eclipse.birt.report.debug.internal.core.launcher.ReportLauncher;
import org.eclipse.birt.report.debug.internal.ui.script.util.ScriptDebugUtil;
import org.eclipse.birt.report.debug.ui.DebugUI;
import org.eclipse.birt.report.designer.ui.dialogs.InputParameterDialog;
import org.eclipse.birt.report.designer.ui.parameters.ParameterFactory;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jdt.launching.IRuntimeClasspathEntry;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;

/**
 * ReportLaunchHelper
 */
public class ReportLaunchHelper implements IReportLaunchConstants
{

	private Map paramValues = new HashMap( );

	String fileName;
	String engineHome;
	String tempFolder;
	String targetFormat;
	boolean isOpenTargetFile;
	boolean useDefaultEngineHome;
	int debugType;
	int taskType;
	int eventPort;
	int requestPort;

	void init( ILaunchConfiguration configuration ) throws CoreException
	{
		fileName = covertVariables( configuration.getAttribute( ATTR_REPORT_FILE_NAME,
				"" ) ); //$NON-NLS-1$
		engineHome = covertVariables( configuration.getAttribute( ATTR_ENGINE_HOME,
				"" ) ); //$NON-NLS-1$
		tempFolder = covertVariables( configuration.getAttribute( ATTR_TEMP_FOLDER,
				"" ) ); //$NON-NLS-1$

		useDefaultEngineHome = configuration.getAttribute( ATTR_USE_DEFULT_ENGINE_HOME,
				true );

		targetFormat = configuration.getAttribute( ATTR_TARGET_FORMAT,
				DEFAULT_TARGET_FORMAT );
		isOpenTargetFile = configuration.getAttribute( ATTR_OPEN_TARGET, false );
		debugType = configuration.getAttribute( ATTR_DEBUG_TYPE,
				DEFAULT_DEBUG_TYPE );
		taskType = configuration.getAttribute( ATTR_TASK_TYPE,
				DEFAULT_TASK_TYPE );
	}

	void addUserClassPath( List list, ILaunchConfiguration config )
	{
		list.add( "-D" + ATTR_USER_CLASS_PATH + "=" + convertClassPath( getUserClasspath( config ) ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void addPortArgs( List list )
	{
		requestPort = findFreePort( );
		eventPort = findFreePort( );

		list.add( "-D" + ATTR_REQUEST_PORT + "=" + requestPort ); //$NON-NLS-1$ //$NON-NLS-2$
		list.add( "-D" + ATTR_EVENT_PORT + "=" + eventPort ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void addParameterArgs( List list )
	{
		Iterator iterator = paramValues.keySet( ).iterator( );
		while ( iterator.hasNext( ) )
		{
			String key = (String) iterator.next( );
			String value = String.valueOf( paramValues.get( key ) );
			StringBuffer buff = new StringBuffer( );
			buff.append( "-D" ); //$NON-NLS-1$
			buff.append( ATTR_PARAMRTER );
			buff.append( key );
			buff.append( "=" ); //$NON-NLS-1$
			buff.append( value );

			list.add( buff.toString( ) );
		}
	}

	void addTypeArgs( List list )
	{
		list.add( "-D" + ATTR_DEBUG_TYPE + "=" + debugType ); //$NON-NLS-1$ //$NON-NLS-2$
		list.add( "-D" + ATTR_TASK_TYPE + "=" + taskType ); //$NON-NLS-1$ //$NON-NLS-2$
		list.add( "-D" + ATTR_TARGET_FORMAT + "=" + targetFormat ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void addTempFolder( List list )
	{
		list.add( "-D" + ATTR_TEMP_FOLDER + "=" + tempFolder ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void addFileNameArgs( List list )
	{
		list.add( "-D" + ATTR_REPORT_FILE_NAME + "=" + fileName ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	void addEngineHomeArgs( List list )
	{
		list.add( "-D" + ATTR_ENGINE_HOME + "=" + engineHome ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private static String covertVariables( String str )
	{
		try
		{
			return ScriptDebugUtil.getSubstitutedString( str );
		}
		catch ( CoreException e )
		{
			return str;
		}
	}

	private static String[] getUserClasspath( ILaunchConfiguration configuration )
	{
		try
		{
			ScriptDebugClasspathProvider provider = new ScriptDebugClasspathProvider( );
			IRuntimeClasspathEntry[] entries = provider.computeUserClasspath( configuration );
			entries = JavaRuntime.resolveRuntimeClasspath( entries,
					configuration );

			List userEntries = new ArrayList( );

			Set set = new HashSet( entries.length );

			for ( int i = 0; i < entries.length; i++ )
			{
				if ( entries[i].getClasspathProperty( ) == IRuntimeClasspathEntry.USER_CLASSES )
				{
					String location = entries[i].getLocation( );
					if ( location != null )
					{
						if ( !set.contains( location ) )
						{
							userEntries.add( location );
							set.add( location );
						}
					}
				}
			}

			return (String[]) userEntries.toArray( new String[userEntries.size( )] );
		}
		catch ( CoreException e )
		{
			e.printStackTrace( );
		}

		return null;
	}

	private static String convertClassPath( String[] cp )
	{
		int pathCount = 0;
		StringBuffer buf = new StringBuffer( );
		if ( cp == null || cp.length == 0 )
		{
			return ""; //$NON-NLS-1$
		}
		for ( int i = 0; i < cp.length; i++ )
		{
			if ( pathCount > 0 )
			{
				buf.append( File.pathSeparator );
			}
			buf.append( cp[i] );
			pathCount++;
		}
		return buf.toString( );
	}

	private static int findFreePort( )
	{
		ServerSocket socket = null;
		try
		{
			socket = new ServerSocket( 0 );
			return socket.getLocalPort( );
		}
		catch ( IOException e )
		{
		}
		finally
		{
			if ( socket != null )
			{
				try
				{
					socket.close( );
				}
				catch ( IOException e )
				{
				}
			}
		}
		return -1;
	}

	private static void configEngine( EngineConfig engineConfig )
	{
		HTMLRenderOption emitterConfig = new HTMLRenderOption( );

		emitterConfig.setActionHandler( new HTMLActionHandler( ) {

			public String getURL( IAction actionDefn, Object context )
			{
				if ( actionDefn.getType( ) == IAction.ACTION_DRILLTHROUGH )
					return "birt://" //$NON-NLS-1$
							+ URLEncoder.encode( super.getURL( actionDefn,
									context ) );
				return super.getURL( actionDefn, context );
			}

		} );
		// emitterConfig.setImageHandler( new HTMLCompleteImageHandler( ) );
		// emitterConfig.setImageHandler( new HTMLImageHandler( ) );
		engineConfig.getEmitterConfigs( ).put( RenderOption.OUTPUT_FORMAT_HTML,
				emitterConfig );

	}

	/**
	 * Gets the parameter
	 * 
	 * @param params
	 * @return
	 */
	private static boolean getParameterValues( List params, Map paramValues )
	{
		if ( params != null && params.size( ) > 0 )
		{
			InputParameterDialog dialog = new InputParameterDialog( DebugUI.getShell( ),
					params,
					paramValues );
			if ( dialog.open( ) == Window.OK )
			{
				paramValues.clear( );
				paramValues.putAll( dialog.getParameters( ) );
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return true;
		}
	}

	private static List getInputParameters( String reportDesignFile,
			int taskType, IReportEngine engine )
	{
		IGetParameterDefinitionTask task = null;
		try
		{
			if ( taskType == TASK_TYPE_RENDER )
			{
				task = engine.createGetParameterDefinitionTask( engine.openReportDocument( reportDesignFile )
						.getReportRunnable( ) );
			}
			else
			{
				task = engine.createGetParameterDefinitionTask( engine.openReportDesign( reportDesignFile ) );
			}
			ParameterFactory factory = new ParameterFactory( task );
			List parameters = factory.getRootChildren( );
			task.close( );
			task = null;
			return parameters;
		}
		catch ( EngineException e )
		{
			if ( task != null )
			{
				task.close( );
			}
		}
		return null;
	}

	boolean finalLaunchCheck( final ILaunchConfiguration configuration,
			String mode, IProgressMonitor monitor ) throws CoreException
	{
		paramValues = new HashMap( );

		LauncherEngineConfig engineConfig = new LauncherEngineConfig( );

		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );

		final IReportEngine engine = factory.createReportEngine( engineConfig );
		engine.changeLogLevel( Level.WARNING );
		configEngine( engineConfig );

		final String fileName = covertVariables( configuration.getAttribute( ATTR_REPORT_FILE_NAME,
				"" ) ); //$NON-NLS-1$
		final int taskType = configuration.getAttribute( ATTR_TASK_TYPE,
				DEFAULT_TASK_TYPE );

		Display display = DebugUI.getStandardDisplay( );
		if ( display.getThread( ).equals( Thread.currentThread( ) ) )
		{
			return getParameterValues( getInputParameters( fileName,
					taskType,
					engine ), paramValues );
		}

		final Object[] result = new Object[]{
			Boolean.FALSE
		};

		Runnable r = new Runnable( ) {

			public void run( )
			{
				result[0] = new Boolean( getParameterValues( getInputParameters( fileName,
						taskType,
						engine ),
						paramValues ) );
			}
		};

		DebugUI.getStandardDisplay( ).syncExec( r );

		return ( (Boolean) result[0] ).booleanValue( );
	}

	static void handleProcessTermination( ILaunch launch,
			final IProcess process, final String fileName,
			final String outputFolder ) throws CoreException
	{
		if ( launch.getLaunchConfiguration( ).getAttribute( ATTR_OPEN_TARGET,
				false )
				&& launch.getLaunchConfiguration( )
						.getAttribute( ATTR_TASK_TYPE, DEFAULT_TASK_TYPE ) != TASK_TYPE_RUN )
		{
			final String suffix = launch.getLaunchConfiguration( )
					.getAttribute( ATTR_TARGET_FORMAT, DEFAULT_TARGET_FORMAT );

			Thread monitorThread = new Thread( new Runnable( ) {

				public void run( )
				{
					try
					{
						while ( !process.isTerminated( ) )
						{
							try
							{
								Thread.sleep( 100 );
							}
							catch ( InterruptedException e )
							{
								// donothing
							}
						}

						if ( process.getExitValue( ) == ReportLauncher.EXIT_OK )
						{
							File file = new File( fileName );

							String openName = ReportLauncher.getOutputFileName( outputFolder,
									file.getName( ),
									suffix );

							if ( openName != null
									&& new File( openName ).exists( ) )
							{
								Program.launch( openName );
							}
						}
					}
					catch ( Exception e )
					{
						// ignore
					}
				}
			},
					"Process Termination Monitor" ); //$NON-NLS-1$

			monitorThread.setDaemon( true );
			monitorThread.start( );
		}
	}
}
