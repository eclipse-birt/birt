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
import java.util.logging.Level;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.FolderArchive;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.presentation.ReportDocumentBuilder;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask
{

	private String documentName;
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
		super( engine, runnable, IEngineTask.TASK_RUN );
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
			changeStatusToRunning( );
			if ( reportDocName == null || reportDocName.length( ) == 0 )
			{
				throw new EngineException(
						"Report document name is not specified when running a report." ); //$NON-NLS-1$
			}
			this.documentName = reportDocName;
			doRun( );
		}
		finally
		{
			changeStatusToStopped( );
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
			changeStatusToRunning( );
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
			changeStatusToStopped( );
		}
	}

	private void openArchive( ) throws IOException
	{
		File file = new File( documentName );
		if ( file.exists( ) )
		{
			if ( file.isDirectory( ) )
			{
				archive = new FolderArchiveWriter( documentName );
			}
			else
			{
				archive = new FileArchiveWriter( documentName );
			}
		}
		else
		{
			if ( documentName.endsWith( "\\" ) || documentName.endsWith( "/" ) )
			{
				archive = new FolderArchiveWriter( documentName );
			}
			else
			{
				archive = new FileArchiveWriter( documentName );
			}
		}
	}

	private void openReportDocument( ) throws EngineException
	{
		try
		{
			if ( archive == null )
			{
				openArchive( );
			}
			writer = new ReportDocumentWriter( engine, archive );
			executionContext.setReportDocWriter( writer );
		}
		catch ( IOException ex )
		{
			throw new EngineException( "Can not open the report archive.", ex ); //$NON-NLS-1$	
		}
	}

	private void closeReportDocument( )
	{
		writer.close( );
		writer = null;
		archive = null;
		documentName = null;
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

//		setupRenderOption( );
		
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
				ReportExecutor executor = new ReportExecutor( executionContext,
						report.getReportIR( ),
						emitter );
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
		catch ( Throwable t )
		{
			log.log( Level.SEVERE,
					"Error happened while running the report.", t ); //$NON-NLS-1$
			throw new EngineException( "Error happened while running the report", t ); //$NON-NLS-1$
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
			changeStatusToRunning( );
			setDataSource( fArchive );
			run( (IDocArchiveWriter) fArchive );
		}
		finally
		{
			changeStatusToStopped( );
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