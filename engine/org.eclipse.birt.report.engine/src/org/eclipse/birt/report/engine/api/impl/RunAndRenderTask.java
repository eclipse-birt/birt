/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ContextPageBreakHandler;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.layout.CompositeLayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask
{

	protected IReportLayoutEngine layoutEngine;

	/**
	 * @param engine
	 *            reference to the report engine
	 * @param runnable
	 *            the runnable report design reference
	 */
	public RunAndRenderTask( ReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable, IEngineTask.TASK_RUNANDRENDER );
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run( ) throws EngineException
	{
		try
		{
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			doRun( );
		}
		finally
		{
			changeStatusToStopped( );
			switchClassLoaderBack( );
		}
	}

	void doRun( ) throws EngineException
	{
		// register default parameters and validate
		doValidateParameters( );

		setupRenderOption( );
		loadDesign( );
		prepareDesign( );
		startFactory( );
		startRender( );
		try
		{
			IContentEmitter emitter = createContentEmitter( );
			IReportExecutor executor = new ReportExecutor( executionContext );
			executor = new SuppressDuplciateReportExecutor( executor );
			executor = new LocalizedReportExecutor(
					executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );

			// if we need do the paginate, do the paginate.
			String format = executionContext.getOutputFormat( );
			boolean paginate = true;
			if ( FORMAT_HTML.equalsIgnoreCase( format ) ) //$NON-NLS-1$
			{
				HTMLRenderOption htmlOption = new HTMLRenderOption(
						renderOptions );
				paginate = htmlOption.getHtmlPagination( );
			}
			
			if ( ExtensionManager.NO_PAGINATION.equals( pagination ) )
			{
				paginate = false;
			}

			synchronized ( this )
			{
				if ( !executionContext.isCanceled( ) )
				{
					layoutEngine = createReportLayoutEngine( pagination,
							renderOptions );
				}
			}

			if ( layoutEngine != null )
			{
				layoutEngine.setLocale( executionContext.getLocale( ) );

				CompositeLayoutPageHandler layoutPageHandler = new CompositeLayoutPageHandler( );
				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
						executionContext );
				layoutPageHandler.addPageHandler( handle );
				layoutPageHandler.addPageHandler( new ContextPageBreakHandler(
						executionContext ) );

				layoutEngine.setPageHandler( layoutPageHandler );

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
						format );
				outputEmitters.addEmitter( emitter );
				outputEmitters.addEmitter( handle.getEmitter( ) );

				IReportContent report = executor.execute( );
				outputEmitters.start( report );
				layoutEngine.layout( executor, report, outputEmitters, paginate );
				layoutEngine.close( );
				outputEmitters.end( report );
			}
			closeRender( );
			executionContext.closeDataEngine( );
			closeFactory( );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					MessageConstants.REPORT_RUN_ERROR, ex ); //$NON-NLS-1$
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
	}

	public void cancel( )
	{
		super.cancel( );
		if ( layoutEngine != null )
		{
			layoutEngine.cancel( );
		}
	}
	
	public void setMaxRowsPerQuery( int maxRows )
	{
		executionContext.setMaxRowsPerQuery( maxRows );
	}
}