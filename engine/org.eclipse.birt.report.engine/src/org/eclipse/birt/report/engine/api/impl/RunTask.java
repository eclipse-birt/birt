/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.data.dte.DocumentDataSource;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.emitter.ReportEmitterExecutor;
import org.eclipse.birt.report.engine.internal.presentation.ReportDocumentInfo;
import org.eclipse.birt.report.engine.presentation.ReportDocumentBuilder;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask
{

	private String documentName;
	private IDocArchiveWriter archive;
	private ReportDocumentWriter writer;
	private ReportDocumentBuilder documentBuilder;

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable instance
	 */
	public RunTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable, IEngineTask.TASK_RUN );
		executionContext.setFactoryMode( true );
		executionContext.setPresentationMode( false );
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
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			if ( reportDocName == null || reportDocName.length( ) == 0 )
			{
				throw new EngineException(
						MessageConstants.REPORT_DOCNAME_NOT_SPECIFIED_ERROR ); //$NON-NLS-1$
			}
			this.documentName = reportDocName;
			doRun( );
		}
		finally
		{
			changeStatusToStopped( );
			switchClassLoaderBack( );
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
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			if ( archive == null )
			{
				throw new EngineException(
						MessageConstants.REPORT_ARCHIVE_ERROR ); //$NON-NLS-1$
			}
			this.archive = archive;
			doRun( );
		}
		finally
		{
			changeStatusToStopped( );
			switchClassLoaderBack( );
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
			String[] exts = executionContext.getEngineExtensions( );
			writer = new ReportDocumentWriter( engine, archive, exts );
			executionContext.setReportDocWriter( writer );
			DocumentDataSource ds = executionContext.getDataSource( );
			if ( ds != null )
			{
				writer.saveReportletDocument( ds.getBookmark( ), ds
						.getInstanceID( ) );
			}
		}
		catch ( IOException ex )
		{
			throw new EngineException(
					MessageConstants.REPORT_ARCHIVE_OPEN_ERROR, ex );
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
		if ( progressMonitor != null )
		{
			progressMonitor.onProgress( IProgressMonitor.START_TASK, TASK_RUN );
		}
		loadDataSource( );
		doValidateParameters( );
		loadDesign( );
		prepareDesign( );
		startFactory( );
		openReportDocument( );
		try
		{
			ReportRunnable newRunnable = writer.saveDesign( executionContext
					.getRunnable( ), executionContext.getOriginalRunnable( ) );
			executionContext.updateRunnable( newRunnable );
			writer.saveReportIR( executionContext.getReport( ) );
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
				// prepare the extension executor
				executor = createReportExtensionExecutor( executor );
				executor = new ReportEmitterExecutor( executor, emitter );
				executor = new SuppressDuplciateReportExecutor( executor );
//				IReportExecutor lExecutor = new LocalizedReportExecutor( executionContext,
//						executor );
				executionContext.setExecutor( executor );

				initializeContentEmitter( emitter, executor );

				documentBuilder.build( );
			}
						
			executionContext.closeDataEngine( );
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.REPORT_RUN_ERROR, ex );
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
			throw new EngineException( MessageConstants.REPORT_RUN_ERROR, t ); //$NON-NLS-1$
		}
		finally
		{
			documentBuilder = null;
			closeFactory();
			writer.savePersistentObjects( executionContext.getGlobalBeans( ) );
			closeReportDocument();

			// notify that the document has been finished
			if ( pageHandler != null && !executionContext.isCanceled( ) )
			{
				int totalPage = (int) executionContext.getTotalPage( );
				IReportDocumentInfo docInfo = new ReportDocumentInfo(
						executionContext, totalPage, true );
				pageHandler.onPage( totalPage, true, docInfo );
			}
			if ( progressMonitor != null )
			{
				progressMonitor.onProgress( IProgressMonitor.END_TASK, TASK_RUN );
			}
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
	
	public void setMaxRowsPerQuery( int maxRows )
	{
		executionContext.setMaxRowsPerQuery( maxRows );
	}
	
	public void enableProgressiveViewing( boolean enabled )
	{
		executionContext.enableProgressiveViewing( enabled );
	}
}