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
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.presentation.ReportDocumentBuilder;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * A task for running a report design to get a report document
 */
public class RunTask extends AbstractRunTask implements IRunTask
{

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
		if ( reportDocName == null || reportDocName.length( ) == 0 )
			throw new EngineException(
					"Report document name is not specified when running a report." ); //$NON-NLS-1$
		try
		{
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
		}
		catch ( IOException e )
		{
			throw new EngineException( e.getLocalizedMessage( ) );
		}

		run( archive );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRunTask#run(org.eclipse.birt.core.archive.IDocumentArchive)
	 */
	public void run( IDocArchiveWriter archive ) throws EngineException
	{
		if ( archive == null )
			throw new EngineException(
					"Report archive is not specified when running a report." ); //$NON-NLS-1$	

		this.archive = archive;
		doRun( );
	}

	private void openReportDocument( ) throws EngineException
	{
		try
		{
			archive.initialize( );
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
		setRunningFlag( true );
		ReportDesignHandle reportDesign = executionContext.getDesign( );

		// using paramters
		if ( !validateParameters( ) )
		{
			setRunningFlag( false );
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		loadDesign( );
		prepareDesign( );
		startFactory( );
		openReportDocument( );
		try
		{
			writer.saveDesign( reportDesign );
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
						reportDesign,
						emitter );
				IReportExecutor lExecutor = new LocalizedReportExecutor( executionContext,
						executor );
				executionContext.setExecutor( lExecutor );

				initializeContentEmitter( emitter, lExecutor );

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
					"Error happended while running the report", ex );
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
			setRunningFlag( false );
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
		setDataSource( fArchive );
		run( ( IDocArchiveWriter ) fArchive );
	}
	
	protected void doCancel( )
	{
		super.doCancel( );
		if ( documentBuilder != null )
		{
			documentBuilder.cancel( );
		}
	}
}