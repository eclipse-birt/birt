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

package org.eclipse.birt.report.debug.internal.core.launcher;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.debug.internal.core.vm.ReportVMServer;
import org.eclipse.birt.report.debug.internal.core.vm.VMConstants;
import org.eclipse.birt.report.debug.internal.core.vm.VMContextData;
import org.eclipse.birt.report.debug.internal.core.vm.VMException;
import org.eclipse.birt.report.debug.internal.core.vm.VMListener;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.mozilla.javascript.Context;

/**
 * Run this class when debug the script.
 */
public class ReportLauncher implements VMListener, IReportLaunchConstants
{

	private static final Logger logger = Logger.getLogger( ReportLauncher.class.getName( ) );

	private static final String RPTDOC_SUFFIX = "rptdocument"; //$NON-NLS-1$

	private IReportEngine engine;
	private EngineConfig engineConfig;
	private Map paramValues = new HashMap( );

	private String reportDesignFile;
	private boolean debugScript;
	private String targetFormat;

	private ReportVMServer server;

	private IRunTask runTask;
	private IRenderTask renderTask;
	private IRunAndRenderTask runAndRenderTask;

	public ReportLauncher( )
	{
		reportDesignFile = getFileName( );
		debugScript = ( getDebugType( ) & DEBUG_TYPE_JAVA_SCRIPT ) == DEBUG_TYPE_JAVA_SCRIPT;
		targetFormat = getTargetFormat( );

		initParameters( );
	}

	public static void main( String[] args )
	{
		new ReportLauncher( ).run( );
	}

	private static int getListenPort( )
	{
		String str = System.getProperty( ATTR_LISTEN_PORT );
		if ( str == null )
		{
			throw new Error( "The request port value is absent." );//$NON-NLS-1$
		}

		return Integer.parseInt( str );
	}

	private static String getFileName( )
	{
		return System.getProperty( ATTR_REPORT_FILE_NAME );
	}

	private static String getEngineHome( )
	{
		return System.getProperty( ATTR_ENGINE_HOME );
	}

	private static String getOutputFolder( )
	{
		return System.getProperty( ATTR_TEMP_FOLDER );
	}

	private static String getTargetFormat( )
	{
		return System.getProperty( ATTR_TARGET_FORMAT );
	}

	private static int getTaskType( )
	{
		String str = System.getProperty( ATTR_TASK_TYPE );
		if ( str == null )
		{
			return DEFAULT_TASK_TYPE;
		}

		return Integer.parseInt( str );
	}

	private static int getDebugType( )
	{
		String str = System.getProperty( ATTR_DEBUG_TYPE );
		if ( str == null )
		{
			return DEFAULT_DEBUG_TYPE;
		}

		return Integer.parseInt( str );
	}

	private static String getUserClassPath( )
	{
		return System.getProperty( ATTR_USER_CLASS_PATH );
	}

	private void initParameters( )
	{
		Properties propertys = System.getProperties( );
		Iterator itor = propertys.keySet( ).iterator( );
		while ( itor.hasNext( ) )
		{
			String str = (String) itor.next( );
			if ( str.startsWith( ATTR_PARAMRTER ) )
			{
				addParameter( paramValues, str, propertys.getProperty( str ) );
			}
			else if ( str.startsWith( ATTR_MULPARAMRTER + "0" ) )
			{
				addMulitipleParameter( paramValues,
						str,
						propertys.getProperty( str ) );
			}
		}
	}

	private void addParameter( Map map, String key, String value )
	{
		String temp = key.substring( ATTR_PARAMRTER.length( ) );
		map.put( temp, value );
	}

	private void addMulitipleParameter( Map map, String key, String value )
	{
		List list = new ArrayList( );
		String temp = key.substring( ATTR_MULPARAMRTER.length( ) + 1 );
		list.add( value );

		int i = 1;
		Properties propertys = System.getProperties( );
		Set set = propertys.keySet( );
		while ( set.contains( ATTR_MULPARAMRTER + i + temp ) )
		{
			list.add( propertys.get( ATTR_MULPARAMRTER + i + temp ) );
			i++;
		}
		Object[] objs = new Object[list.size( )];
		list.toArray( objs );
		map.put( temp, objs );
	}

	private void run( )
	{
		init( );
		renderReport( );
		dispose( );
		System.exit( EXIT_OK );
	}

	private void init( )
	{
		if ( debugScript )
		{
			server = new ReportVMServer( );
			server.addVMListener( this );
			Context cx = Context.enter( );

			try
			{
				server.start( getListenPort( ), cx );
			}
			catch ( VMException e1 )
			{
				throw new Error( "Fail to start Debug Server." );//$NON-NLS-1$
			}
		}

		engineConfig = new LauncherEngineConfig( );
		engineConfig.setEngineHome( getEngineHome( ) );

		try
		{
			Platform.startup( engineConfig );
		}
		catch ( BirtException e )
		{
			throw new Error( "Fail to start Report Platform" );//$NON-NLS-1$
		}

		IReportEngineFactory factory = (IReportEngineFactory) Platform.createFactoryObject( IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY );

		configEngine( );
		
		this.engine = factory.createReportEngine( engineConfig );
		engine.changeLogLevel( Level.WARNING );
	}

	private void configEngine( )
	{
		String userClassPath = getUserClassPath( );

		logger.info( "User class path received: " + userClassPath ); //$NON-NLS-1$

		// clear dev user classpath state
		System.clearProperty( EngineConstants.PROJECT_CLASSPATH_KEY );

		if ( userClassPath != null )
		{
			// set user classpath for engine
			engineConfig.getAppContext( )
					.put( EngineConstants.PROJECT_CLASSPATH_KEY, userClassPath );

			// also set dev user classpath
			System.setProperty( EngineConstants.PROJECT_CLASSPATH_KEY,
					userClassPath );
		}
	}

	private void renderReport( )
	{
		String outputFolder = getOutputFolder( );

		// TODO validate parameters

		int taskType = getTaskType( );

		boolean isRunAndRenderTask = ( taskType & TASK_TYPE_RUN_AND_RENDER ) != 0;
		boolean isRunTask = ( taskType & TASK_TYPE_RUN ) != 0;
		boolean isRenderTask = ( taskType & TASK_TYPE_RENDER ) != 0;

		try
		{
			if ( isRunAndRenderTask )
			{
				createReport( reportDesignFile, outputFolder, paramValues );
			}
			else
			{
				String reportDocumentFile = reportDesignFile;

				if ( isRunTask )
				{
					reportDocumentFile = createReportDocument( reportDesignFile,
							outputFolder,
							paramValues );
				}

				if ( isRenderTask )
				{
					createReportOutput( reportDocumentFile, outputFolder );
				}
			}
		}
		catch ( EngineException e )
		{
			logger.log( Level.SEVERE, "Engine exception", e ); //$NON-NLS-1$
		}
		catch ( IOException e )
		{
			logger.log( Level.SEVERE, "IO exception", e ); //$NON-NLS-1$
		}
	}

	private void createReport( String reportDesignFile, String outputFolder,
			Map parameters ) throws IOException, EngineException
	{
		String outputFile = getOutputFileName( outputFolder,
				new File( reportDesignFile ).getName( ),
				targetFormat );

		IReportRunnable report = engine.openReportDesign( reportDesignFile );
		runAndRenderTask = engine.createRunAndRenderTask( report );

		IRenderOption renderOption = new HTMLRenderOption( );
		renderOption.setOutputFileName( outputFile );
		renderOption.setOutputFormat( targetFormat );

		try
		{
			if ( parameters != null )
			{
				runAndRenderTask.setParameterValues( parameters );
			}
			runAndRenderTask.setAppContext( Collections.EMPTY_MAP );
			runAndRenderTask.setRenderOption( renderOption );
			runAndRenderTask.run( );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		finally
		{
			runAndRenderTask.close( );
			report = null;
			runAndRenderTask = null;
		}
	}

	private String createReportDocument( String reportDesignFile,
			String outputFolder, Map parameters ) throws IOException,
			EngineException
	{
		String reportDocumentFile = getOutputFileName( outputFolder,
				new File( reportDesignFile ).getName( ),
				RPTDOC_SUFFIX );

		IDocArchiveWriter archive = new FileArchiveWriter( reportDocumentFile );
		IReportRunnable report = engine.openReportDesign( reportDesignFile );
		runTask = engine.createRunTask( report );
		try
		{
			if ( parameters != null )
			{
				runTask.setParameterValues( parameters );
			}
			runTask.setAppContext( Collections.EMPTY_MAP );
			runTask.run( archive );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		finally
		{
			runTask.close( );
			report = null;
			runTask = null;
		}
		return reportDocumentFile;
	}

	private void createReportOutput( String reportDocumentFile,
			String outputFolder ) throws EngineException, IOException
	{
		IReportDocument document = engine.openReportDocument( reportDocumentFile );
		renderTask = engine.createRenderTask( document );

		String outputFile = getOutputFileName( outputFolder,
				new File( reportDesignFile ).getName( ),
				targetFormat );

		IRenderOption renderOption = new HTMLRenderOption( );
		renderOption.setOutputFileName( outputFile );
		renderOption.setOutputFormat( targetFormat );

		try
		{
			renderTask.setRenderOption( renderOption );
			renderTask.render( );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		finally
		{
			renderTask.close( );
			renderTask = null;
			document.close( );
			document = null;
		}
	}

	public static String getOutputFileName( String outputFolder,
			String fileName, String suffix )
	{
		return outputFolder + File.separator + fileName + "." //$NON-NLS-1$
				+ suffix;
	}

	public void handleEvent( int eventCode, VMContextData context )
	{
		if ( eventCode == VMConstants.VM_TERMINATED )
		{
			dispose( );
		}
	}

	private void dispose( )
	{
		try
		{
			if ( runTask != null )
			{
				runTask.cancel( );
				runTask.close( );
				runTask = null;
			}
		}
		catch ( Throwable e )
		{
			// do nothing
		}

		try
		{
			if ( renderTask != null )
			{
				renderTask.cancel( );
				renderTask.close( );
				renderTask = null;
			}
		}
		catch ( Throwable e )
		{
			// do nothing
		}

		try
		{
			if ( runAndRenderTask != null )
			{
				runAndRenderTask.cancel( );
				runAndRenderTask.close( );
				runAndRenderTask = null;
			}
		}
		catch ( Throwable e )
		{
			// do nothing
		}

		if ( server != null )
		{
			server.shutdown( Context.enter( ) );
		}

		Platform.shutdown( );
	}

}
