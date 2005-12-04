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

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.core.util.BirtTimer;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

/**
 * A report engine provides an entry point for reporting functionalities. It is
 * where the report generation and rendering process are globally customized. It
 * is also the place where engine statistics are collected. Through report
 * engine, reports can be generated and rendered to different output formats.
 * Queries can also be executed for preview purpose without involving a full
 * report generation.
 * <p>
 * Engine supports running different types of tasks. Example tasks include
 * running a report design to generate a report instance file, rendering a
 * report instance to output format, running a report directly to output,
 * running a dataset for preview, seaching a report, etc.
 */

public class ReportEngine
{

	protected static Logger logger = Logger.getLogger( ReportEngine.class
			.getName( ) );

	/**
	 * engine configuration object
	 */
	protected EngineConfig config;

	/**
	 * A helper object to carry out most ReportEngine jobs
	 */
	protected ReportEngineHelper helper;

	/**
	 * root script scope. contains objects shared by the whole engine.
	 */
	protected ScriptableObject rootScope;

	/**
	 * Constructor. If config is null, engine derives BIRT_HOME from the
	 * location of the engine jar file, and derives data driver directory as
	 * $BIRT_HOME/drivers. For a simple report with no images and links, engine
	 * will run without complaining. If the report has image/chart defined, the
	 * engine has to be configured with relevant image and chart handlers.
	 * 
	 * @param config
	 *            an engine configuration object used to configure the engine
	 */
	public ReportEngine( EngineConfig config )
	{
		BirtTimer timer = new BirtTimer( );
		timer.start( );

		this.config = config;

		// Explicitly initialize Platform with the ServletContext (could be
		// null).
		Platform.initialize( config.context );
		this.helper = new ReportEngineHelper( this );
		setupLogging( );
		setupScriptScope( );

		timer.stop( );
		timer.logTimeTaken( logger, Level.INFO, "Engine startup" ); // $NON-NLS-1$
	}

	/**
	 * set up engine logging
	 */
	private void setupLogging( )
	{
		if ( config != null )
		{
			String dest = (String) config.configObjects
					.get( EngineConfig.LOG_DESTINATION );

			if ( dest != null )
			{
				Level level = (Level) config.configObjects
						.get( EngineConfig.LOG_LEVEL );
				if ( level == null )
					level = Level.WARNING;
				helper.setupLogging( dest, level );
			}
		}
	}

	/**
	 * register globally available script functions
	 */
	private void setupScriptScope( )
	{
		if ( config != null )
		{
			Context cx = Context.enter( );
			try
			{
				rootScope = cx.initStandardObjects( null, true );
				registerBeans( rootScope, config.getConfigMap( ) );
				registerBeans( rootScope, config.getScriptObjects( ) );
				IStatusHandler handler = config.getStatusHandler( );
				if ( handler != null )
				{
					handler.initialize( );
					rootScope.put( "_statusHandle", rootScope, handler );
					cx
							.evaluateString(
									rootScope,
									"function writeStatus(msg) { _statusHandle.showStatus(msg); }",
									"<inline>", 0, null );
				}
			}
			catch ( Exception ex )
			{
				rootScope = null;
				logger.log( Level.INFO,
						"Error occurs while initialze script scope", ex );
			}
			finally
			{
				Context.exit( );
			}
		}
	}

	/**
	 * get the root scope used by the engine
	 * 
	 * @return
	 */
	public ScriptableObject getRootScope( )
	{
		return rootScope;
	}

	/**
	 * register the map entry into script object.
	 * 
	 * @param scope
	 *            script scope to be added.
	 * @param map
	 *            map
	 */
	private void registerBeans( ScriptableObject scope, Map map )
	{
		Iterator iter = map.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			if ( entry.getKey( ) != null )
			{
				scope
						.put( entry.getKey( ).toString( ), scope, entry
								.getValue( ) );
			}
		}
	}

	/**
	 * Change the log level to newLevel
	 * 
	 * @param newLevel -
	 *            new log level
	 */
	public void changeLogLevel( Level newLevel )
	{
		if ( newLevel != null )
			helper.changeLogLevel( newLevel );
	}

	/**
	 * returns the engine configuration object
	 * 
	 * @return the engine configuration object
	 */
	public EngineConfig getConfig( )
	{
		return config;
	}

	/**
	 * opens a report design file and creates a report design runnable. From the
	 * ReportRunnable object, embedded images and parameter definitions can be
	 * retrieved. Constructing an engine task requires a report design runnable
	 * object.
	 * 
	 * @param designName
	 *            the full path of the report design file
	 * @return a report design runnable object
	 * @throws EngineException
	 *             throwed when the input file does not exist, or the file is
	 *             invalid
	 */
	public IReportRunnable openReportDesign( String designName )
			throws EngineException
	{
		return helper.openReportDesign( designName );
	}

	/**
	 * opens a report design stream and creates a report design runnable. From
	 * the ReportRunnable object, embedded images and parameter definitions can
	 * be retrieved. Constructing an engine task requires a report design
	 * runnableobject.
	 * 
	 * @param designStream
	 *            the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException
	 *             throwed when the input stream is null, or the stream does not
	 *             yield a valid report design
	 */
	public IReportRunnable openReportDesign( InputStream designStream )
			throws EngineException
	{
		return helper.openReportDesign( designStream );
	}

	/**
	 * creates an engine task for running and rendering report directly to
	 * output format
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a run and render report task
	 */
	public IRunAndRenderTask createRunAndRenderTask(
			IReportRunnable reportRunnable )
	{
		return helper.createRunAndRenderTask( reportRunnable );
	}

	/**
	 * creates an engine task for obtaining report parameter definitions
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a run and render report task
	 */
	public IGetParameterDefinitionTask createGetParameterDefinitionTask(
			IReportRunnable reportRunnable )
	{
		return helper.createGetParameterDefinitionTask( reportRunnable );
	}

	public IDataPreviewTask createDataPreviewTask(
			IReportRunnable reportRunnable )
	{
		return helper.createDataPreviewTask( reportRunnable );
	}

	/**
	 * returns all supported output formats through BIRT engine emitter
	 * extensions
	 * 
	 * @return all supported output formats through BIRT engine emitter
	 *         extensions
	 */
	public String[] getSupportedFormats( )
	{
		return helper.getSupportedFormats( );
	}

	/**
	 * the MIME type for the specific formatted supported by the extension.
	 * 
	 * @param format
	 *            the output format
	 * @param extensionID
	 *            the extension ID, which could be null if only one plugin
	 *            supports the output format
	 * @return the MIME type for the specific formatted supported by the
	 *         extension.
	 */
	public String getMIMEType( String format )
	{
		return helper.getMIMEType( format );
	}

	/**
	 * shuts down the report engine
	 */
	public void destroy( )
	{
		helper.stopLogging( );
		rootScope = null;
		helper = null;
		if ( config != null )
		{
			IStatusHandler handler = config.getStatusHandler( );
			if ( handler != null )
			{
				handler.finish( );
			}
		}
	}

	/**
	 * creates a task to run a report to generate a report document
	 * 
	 * @param reportRunnable
	 *            the runnable report design object
	 * @return a task that runs the report
	 */
	public IRunTask createRunTask( IReportRunnable reportRunnable )
	{
		return helper.createRunTask( reportRunnable );
	}

	/**
	 * creates a task that renders the report to a specific output format.
	 * 
	 * @param reportDocument
	 *            a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IRenderTask createRenderTask( IReportDocument reportDocument )
	{
		return helper.createRenderTask( reportDocument );
	}

	/**
	 * opens a report document and returns an IReportDocument object, from which
	 * further information can be retrieved.
	 * 
	 * @param fileName
	 *            the report document name. report document is an archive in
	 *            BIRT.
	 * @return A handle to the report document
	 * @throws EngineException
	 *             throwed when the report document archive does not exist, or
	 *             the file is not a valud report document
	 */
	public IReportDocument openReportDocument( String fileName )
			throws EngineException
	{
		return helper.openReportDocument( fileName );
	}

	/**
	 * creates a task that allows data extraction from a report document
	 * 
	 * @param reportDocument
	 *            a handle to an IReportDocument object
	 * @return a task that renders a report to an output format
	 */
	public IDataExtractionTask createDataExtractionTask( IReportDocument reportDocument )
	{
		return null;
	}

	/**
	 * shut down the engine, release all the resources.
	 */
	public void shutdown( )
	{

	}
}