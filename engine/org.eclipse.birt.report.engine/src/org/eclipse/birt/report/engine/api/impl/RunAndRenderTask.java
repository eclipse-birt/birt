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
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.presentation.HTMLPaginationBuilder;
import org.eclipse.birt.report.engine.presentation.HTMLPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.LocalizedEmitter;
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

	private IContentEmitter createContentEmitter( ReportExecutor executor )
			throws EngineException
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

		String format = executionContext.getOutputFormat( );
		if ( format == null )
		{
			format = "html"; //$NON-NLS-1$
		}
		
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
			throw new EngineException(
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

		// localized emitter
		emitter = new LocalizedEmitter( executionContext, emitter );

		// if we need do the paginate, do the paginate.
		if ( format.equalsIgnoreCase( "html" ) ) //$NON-NLS-1$
		{
			boolean paginate = true;
			if ( renderOptions instanceof HTMLRenderOption )
			{
				HTMLRenderOption htmlOption = (HTMLRenderOption) renderOptions;
				paginate = htmlOption.getHtmlPagination( );
			}
			if ( paginate )
			{
				HTMLPaginationBuilder paginationBuilder = new HTMLPaginationBuilder(executor);
				paginationBuilder.setOutputEmitter( emitter );
				emitter = paginationBuilder.getInputEmitter( );
			}
		}

		// emitter is not null
		emitter.initialize( services );

		return emitter;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api2.IRunAndRenderTask#run()
	 */
	public void run( ) throws EngineException
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );

		// register default parameters and validate
		if ( !validateParameters( ) )
		{
			throw new EngineException(
					MessageConstants.INVALID_PARAMETER_EXCEPTION ); //$NON-NLS-1$
		}

		loadDesign( );
		prepareDesign( );
		startFactory( );
		startRender( );
		try
		{
			ReportExecutor executor = new ReportExecutor( executionContext );
			executionContext.setExecutor( executor );
			IContentEmitter emitter = createContentEmitter( executor );
			executor.execute( reportDesign, emitter );

			closeRender( );
			closeFactory( );

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
	}

	public void setEmitterID( String id )
	{
		this.emitterID = id;

	}
}