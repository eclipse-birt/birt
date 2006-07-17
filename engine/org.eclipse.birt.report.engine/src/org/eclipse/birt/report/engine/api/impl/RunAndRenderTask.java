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

import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.UnsupportedFormatException;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * an engine task that runs a report and renders it to one of the output formats
 * supported by the engine.
 */
public class RunAndRenderTask extends EngineTask implements IRunAndRenderTask
{

	/**
	 * specifies the emitter ID used for rendering the report
	 */
	protected String emitterID;
	
	protected IReportLayoutEngine layoutEngine;

	/**
	 * @param engine
	 *            reference to the report engine
	 * @param runnable
	 *            the runnable report design reference
	 */
	public RunAndRenderTask( IReportEngine engine, IReportRunnable runnable )
	{
		super( engine, runnable );
	}

	private IContentEmitter createContentEmitter( ) throws EngineException
	{

		String format = executionContext.getOutputFormat( );

		ExtensionManager extManager = ExtensionManager.getInstance( );
		boolean supported = false;
		Collection supportedFormats = extManager.getSupportedFormat( );
		Iterator iter = supportedFormats.iterator( );
		while ( iter.hasNext( ) )
		{
			String supportedFormat = (String) iter.next( );
			if ( supportedFormat != null
					&& supportedFormat.equalsIgnoreCase( format ) )
			{
				supported = true;
				break;
			}
		}
		if ( !supported )
		{
			log.log( Level.SEVERE,
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
			throw new UnsupportedFormatException(
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
		}

		IContentEmitter emitter = null;
		try
		{
			emitter = extManager.createEmitter( format, emitterID );
		}
		catch ( Throwable t )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.", //$NON-NLS-1$
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION, t );
		}
		if ( emitter == null )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.", //$NON-NLS-1$
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION );
		}

		return emitter;
	}

	private void initializeContentEmitter( IContentEmitter emitter,
			ReportExecutor executor )
	{
		// create the emitter services object that is needed in the emitters.
		EngineEmitterServices services = new EngineEmitterServices( this );

		EngineConfig config = engine.getConfig( );
		if ( config != null )
		{
			services.setEmitterConfig( config.getEmitterConfigs( ) );
		}
		services.setRenderOption( renderOptions );
		services.setExecutor( executor );
		services.setRenderContext( appContext );
		services.setReportRunnable( runnable );

		// emitter is not null
		emitter.initialize( services );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run( ) throws EngineException
	{
		setRunningFlag( true );
		ReportDesignHandle reportDesign = executionContext.getDesign( );

		// register default parameters and validate
		if ( !validateParameters( ) )
		{
			setRunningFlag( false );
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		loadDesign( );
		prepareDesign( );
		startFactory( );
		startRender( );
		try
		{
			IContentEmitter emitter = createContentEmitter( );
			ReportExecutor executor = new ReportExecutor( executionContext,
					reportDesign, null );
			IReportExecutor lExecutor = new LocalizedReportExecutor(
					executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );

			// if we need do the paginate, do the paginate.
			String format = executionContext.getOutputFormat( );
			boolean paginate = true;
			if ( "html".equalsIgnoreCase( format ) ) //$NON-NLS-1$
			{
				if ( renderOptions instanceof HTMLRenderOption )
				{
					HTMLRenderOption htmlOption = (HTMLRenderOption) renderOptions;
					paginate = htmlOption.getHtmlPagination( );
				}
			}

			synchronized ( this )
			{
				if ( !executionContext.isCanceled( ) )
				{
					layoutEngine = LayoutEngineFactory.createLayoutEngine( emitter.getOutputFormat( ) );
				}
			}
			
			if( layoutEngine != null )
			{
				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle( executionContext );
				layoutEngine.setPageHandler( handle );
				layoutEngine.layout( lExecutor, emitter , paginate);
			}

			closeRender( );
			closeFactory( );
		}
		catch ( EngineException e )
		{
			throw e;
		}
		catch ( Exception ex )
		{
			ex.printStackTrace( );
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex ); //$NON-NLS-1$
		}
		catch ( OutOfMemoryError err )
		{
			err.printStackTrace( );
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
		catch ( Throwable t )
		{
			log.log( Level.SEVERE,
					"Error happened while running the report.", t ); //$NON-NLS-1$
			new EngineException( "Error happened while running the report", t ); //$NON-NLS-1$
		}
		finally
		{
			setRunningFlag( false );
		}
	}

	public void setEmitterID( String id )
	{
		this.emitterID = id;

	}
	
	protected void doCancel()
	{
		super.doCancel( );
		if(layoutEngine!=null)
		{
			layoutEngine.cancel( );
		}
	}
}