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

package org.eclipse.birt.report.engine.api.impl;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.DocumentArchive;
import org.eclipse.birt.core.archive.IDocumentArchive;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IDataPreviewTask;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * a helper class that does most of the dirty work for report engine
 */
public class ReportEngineHelper
{

	/**
	 * logger used to log syntax errors.
	 */
	static protected Logger logger = Logger.getLogger( ReportEngineHelper.class
			.getName( ) );

	/**
	 * reference the the public report engine object
	 */
	private ReportEngine engine;

	/**
	 * extension manager
	 */
	private ExtensionManager extensionMgr;

	/**
	 * constructor
	 * 
	 * @param engine
	 *            the report engine
	 */
	public ReportEngineHelper( ReportEngine engine )
	{
		this.engine = engine;
		extensionMgr = ExtensionManager.getInstance( );
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
		ReportDesignHandle designHandle;
		File file = new File( designName );
		if ( !file.exists( ) )
		{
			logger
					.log( Level.SEVERE,
							"{0} not found!", file.getAbsolutePath( ) ); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.DESIGN_FILE_NOT_FOUND_EXCEPTION,
					designName );
		}

		try
		{
			designHandle = new ReportParser( ).getDesignHandle( designName,
					null );
		}
		catch ( DesignFileException e )
		{
			logger.log( Level.SEVERE,
					"invalid design file {0}", file.getAbsolutePath( ) ); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.INVALID_DESIGN_FILE_EXCEPTION, designName,
					e );
		}
		assert ( designHandle != null );
		ReportRunnable runnable = new ReportRunnable( designHandle );
		runnable.setReportName( designName );
		runnable.setReportEngine( engine );
		return runnable;
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
		ReportDesignHandle designHandle;
		String designName = "<stream>"; //$NON-NLS-1$
		try
		{
			designHandle = new ReportParser( ).getDesignHandle( designName,
					designStream );
		}
		catch ( DesignFileException e )
		{
			logger.log( Level.SEVERE, "invalid design file {0}", designName ); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.INVALID_DESIGN_FILE_EXCEPTION, designName,
					e );
		}
		assert ( designHandle != null );
		ReportRunnable runnable = new ReportRunnable( designHandle );
		runnable.setReportName( designName );
		runnable.setReportEngine( engine );
		return runnable;
	}

	/**
	 * creates a report design runnable based on a report design handle. From
	 * the ReportRunnable object, embedded images and parameter definitions can
	 * be retrieved. Constructing an engine task requires a report design
	 * runnable object.
	 * 
	 * @param designStream
	 *            the report design input stream
	 * @return a report design runnable object
	 * @throws EngineException
	 *             throwed when the input stream is null, or the stream does not
	 *             yield a valid report design
	 */
	public IReportRunnable openReportDesign( DesignElementHandle designHandle )
			throws EngineException
	{
		assert ( designHandle instanceof ReportDesignHandle );
		ReportRunnable ret = new ReportRunnable(
				(ReportDesignHandle) designHandle );
		ret
				.setReportName( ( (ReportDesignHandle) designHandle )
						.getFileName( ) );
		ret.setReportEngine( engine );
		return ret;
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
		return new RunAndRenderTask( engine, reportRunnable );
	}

	public IGetParameterDefinitionTask createGetParameterDefinitionTask(
			IReportRunnable reportRunnable )
	{
		return new GetParameterDefinitionTask( engine, reportRunnable );
	}

	public IDataPreviewTask createDataPreviewTask(
			IReportRunnable reportRunnable )
	{
		return new DataPreviewTask( engine, reportRunnable );
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
		return (String[])extensionMgr.getSupportedFormat().toArray(new String[0]);
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
		return extensionMgr.getMIMEType( format );
	}

	/**
	 * @param dest
	 *            log destination. It is the directory name for log file
	 * @param level
	 *            log level
	 */
	public void setupLogging( String dest, Level level )
	{
		EngineLogger.startEngineLogging( dest, level );
	}

	/**
	 * Stop engine logging
	 */
	public void stopLogging( )
	{
		EngineLogger.stopEngineLogging( );
	}

	/**
	 * Change the log level to the newLevel
	 * 
	 * @param newLevel -
	 *            new log level
	 */
	public void changeLogLevel( Level newLevel )
	{
		EngineLogger.changeLogLevel( newLevel );
	}

	public IReportDocument openReportDocument( String docArchiveName )
			throws EngineException
	{
		return openReportDocument( new DocumentArchive( docArchiveName ) );
	}

	public IReportDocument openReportDocument( IDocumentArchive archive )
			throws EngineException
	{
		try
		{
			archive.open( );
			return new ReportDocument( engine, archive );
		}
		catch ( IOException ex )
		{
			throw new EngineException( "Can't open archive", ex );
		}
	}

	public IRunTask createRunTask( IReportRunnable runnable )
	{
		return new RunTask( engine, runnable );
	}

	public IRenderTask createRenderTask( IReportDocument reportDoc )
	{
		try
		{
			IReportRunnable runnable = engine.openReportDesign( reportDoc
					.getDesignStream( ) );
			return new RenderTask( engine, runnable, (ReportDocument) reportDoc );
		}
		catch ( EngineException ex )
		{
			ex.printStackTrace( );
		}
		return null;
	}
}
