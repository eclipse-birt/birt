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
import java.util.HashMap;
import java.util.logging.Level;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.FolderArchive;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.emitter.ReportEmitterExecutor;
import org.eclipse.birt.report.engine.presentation.ReportDocumentBuilder;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask
{

	private String reportDocName;
	private IDocArchiveWriter archive;
	private ReportDocumentWriter writer;
	private IPageHandler pageHandler;
	private ReportDocumentBuilder documentBuilder;

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable instance
	 */
	public RunTask( IReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );
		executionContext.setFactoryMode( true );
		executionContext.setPresentationMode( false );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#setPageHandler(org.eclipse.birt.report.engine.api.IPageHandler)
	 */
	public void setPageHandler( IPageHandler callback )
	{
		this.pageHandler = callback;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(java.lang.String)
	 */
	public void run( String reportDocName ) throws EngineException
	{
		try
		{
			runningStatus = RUNNING_STATUS_RUNNING;
			if ( reportDocName == null || reportDocName.length( ) == 0 )
			{
				throw new EngineException(
						"Report document name is not specified when running a report." ); //$NON-NLS-1$
			}
			this.reportDocName = reportDocName;
			doRun( );
		}
		finally
		{
			runningStatus = RUNNING_STATUS_STOP;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(org.eclipse.birt.core.archive.IDocumentArchive)
	 */
	public void run( IDocArchiveWriter archive ) throws EngineException
	{
		try
		{
			runningStatus = RUNNING_STATUS_RUNNING;
			if ( archive == null )
			{
				throw new EngineException(
						"Report archive is not specified when running a report." ); //$NON-NLS-1$
			}
			this.archive = archive;
			doRun( );
		}
		finally
		{
			runningStatus = RUNNING_STATUS_STOP;
		}
	}

	private IDocArchiveWriter openArchive( String reportDocName )
			throws IOException
	{
		IDocArchiveWriter archive;
		File file = new File( reportDocName );
		if ( file.exists( ) )
		{
			if ( file.isDirectory( ) )
			{
				archive = new FolderArchiveWriter( reportDocName );
			}
			else
			{
				archive = new FileArchiveWriter( reportDocName );
			}
		}
		else
		{
			if ( reportDocName.endsWith( "\\" ) || reportDocName.endsWith( "/" ) )
			{
				archive = new FolderArchiveWriter( reportDocName );
			}
			else
			{
				archive = new FileArchiveWriter( reportDocName );
			}
		}
		return archive;
	}

	private void openReportDocument( ) throws EngineException
	{
		try
		{
			if ( archive == null )
			{
				archive = openArchive( reportDocName );
			}
			archive.initialize( );
		}
		catch ( IOException ex )
		{
			if ( archive != null )
			{
				try
				{
					archive.finish( );
				}
				catch ( IOException e )
				{
					log.log( Level.WARNING, " error in close archive ", e );
				}
			}
			throw new EngineException( "Can not open the report archive.", ex ); //$NON-NLS-1$	
		}
		writer = new ReportDocumentWriter( engine, archive );
		executionContext.setReportDocWriter( writer );
	}

	private void closeReportDocument( )
	{
		if ( writer != null )
		{
			writer.close( );
			writer = null;
		}
		// the archive will be closed in the writer's close.
		archive = null;
		reportDocName = null;
	}

	private void initializeContentEmitter(IContentEmitter emitter, IReportExecutor executor)
	{
		// create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices( this );

		EngineConfig config = engine.getConfig( );
		if ( config != null )
		{
			HashMap emitterConfigs = config.getEmitterConfigs( );
			services.setEmitterConfig( emitterConfigs );
		}
		services.setRenderOption( renderOptions );
		services.setExecutor( executor );
		services.setRenderContext( appContext );
		services.setReportRunnable( runnable );

		// emitter is not null
		emitter.initialize( services );
	}

	/**
	 * runs the report
	 * 
	 * @throws EngineException
	 *             throws exception when there is a run error
	 */
	protected void doRun( ) throws EngineException
	{
		// using paramters
		if ( !validateParameters( ) )
		{
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		loadDesign( );
		prepareDesign( );
		startFactory( );
		openReportDocument( );
		try
		{
			ReportRunnable report = executionContext.getRunnable( );

			writer.saveDesign( report );
			writer.saveParamters( inputValues );

			executionContext.openDataEngine( );

			synchronized(this)
			{
				if (!executionContext.isCanceled( ))
				{
					documentBuilder = new ReportDocumentBuilder(
							executionContext, writer );
				}
			}
			
			if ( documentBuilder != null )
			{
				if ( pageHandler != null )
				{
					documentBuilder.setPageHandler( pageHandler );
				}

				IContentEmitter emitter = documentBuilder.getContentEmitter( );
				IReportExecutor executor = new ReportExecutor( executionContext );
				executor = new ReportEmitterExecutor(executor, emitter);
				executor = new SuppressDuplciateReportExecutor( executor );
//				IReportExecutor lExecutor = new LocalizedReportExecutor( executionContext,
//						executor );
				executionContext.setExecutor( executor );

				initializeContentEmitter( emitter, executor );

				documentBuilder.build( );
			}
			
			
			executionContext.closeDataEngine( );

			writer.savePersistentObjects( executionContext.getGlobalBeans( ) );

		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex );
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
		finally
		{
			documentBuilder = null;
			closeReportDocument();
			closeFactory();
		}
	}

	public void close( )
	{
		super.close( );
	}

	/**
	 * @deprecated
	 */
	public void run( FolderArchive fArchive ) throws EngineException
	{
		try
		{
			runningStatus = RUNNING_STATUS_RUNNING;
			setDataSource( fArchive );
			run( (IDocArchiveWriter) fArchive );
		}
		finally
		{
			runningStatus = RUNNING_STATUS_STOP;
		}
	}
	
	public void cancel( )
	{
		super.cancel( );
		if ( documentBuilder != null )
		{
			documentBuilder.cancel( );
		}
	}
}