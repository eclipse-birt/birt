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

import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ReportEngine;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.presentation.DefaultPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.HTMLPaginationEmitter;
import org.eclipse.birt.report.engine.presentation.LocalizedEmitter;
import org.eclipse.birt.report.engine.presentation.ReportContentLoader;
import org.eclipse.birt.report.engine.script.internal.ReportScriptExecutor;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class RenderTask extends EngineTask implements IRenderTask
{

	ReportDocumentReader reportDoc;
	String emitterID;

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable object
	 * @param reportDoc
	 *            the report document instance
	 */
	public RenderTask( ReportEngine engine, IReportRunnable runnable,
			ReportDocumentReader reportDoc )
	{
		super( engine, runnable );

		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		// load report design
		loadDesign( );

		// open the report document
		openReportDocument( reportDoc );
	}

	protected void openReportDocument( ReportDocumentReader reportDoc )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor.handleBeforeOpenDoc( reportDesign,
				executionContext );

		this.reportDoc = reportDoc;
		executionContext.setReportDocument( reportDoc );

		// load the informationf rom the report document
		setParameterValues( reportDoc.getParameterValues( ) );
		executionContext.registerGlobalBeans( reportDoc
				.getGlobalVariables( null ) );

		ReportScriptExecutor
				.handleAfterOpenDoc( reportDesign, executionContext );
	}

	protected void closeReportDocument( )
	{
		ReportDesignHandle reportDesign = executionContext.getDesign( );
		ReportScriptExecutor.handleBeforeCloseDoc( reportDesign,
				executionContext );
		reportDoc.close( );
		ReportScriptExecutor.handleAfterCloseDoc( reportDesign,
				executionContext );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render(long)
	 */
	public void render( long pageNumber ) throws EngineException
	{
		long totalPage = reportDoc.getPageCount( );
		if ( pageNumber <= 0 || pageNumber > totalPage )
		{
			throw new EngineException( "Can't find page hints :{0}", new Long(
					pageNumber ) );
		}

		if ( renderOptions == null )
		{
			throw new EngineException(
					"Render options have to be specified to render a report." );
		}

		doRender( pageNumber );
	}

	protected IContentEmitter createContentEmitter( ReportExecutor executor )
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
			format = "html";
		}
		ExtensionManager extManager = ExtensionManager.getInstance( );
		if ( !extManager.getSupportedFormat( ).contains( format ) )
		{
			log.log( Level.SEVERE,
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
			throw new EngineException(
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
		}

		IContentEmitter emitter = extManager.createEmitter( format, emitterID );
		if ( emitter == null )
		{
			log.log( Level.SEVERE, "Report engine can not create {0} emitter.",
					format ); // $NON-NLS-1$
			throw new EngineException(
					MessageConstants.CANNOT_CREATE_EMITTER_EXCEPTION );
		}

		// localized emitter
		emitter = new LocalizedEmitter( executionContext, emitter );

		// if we need do the paginate, do the paginate.
		if ( format.equalsIgnoreCase( "html" ) )
		{
			emitter = new HTMLPaginationEmitter( executor, null, emitter );
		}
		else if ( format.equalsIgnoreCase( "fo" )
				|| format.equalsIgnoreCase( "fop" ) )
		{
			emitter = new DefaultPaginationEmitter( executor, null, emitter );
		}

		// emitter is not null
		emitter.initialize( services );

		return emitter;
	}

	/**
	 * @param pageNumber
	 *            the page to be rendered
	 * @throws EngineException
	 *             throws exception if there is a rendering error
	 */
	protected void doRender( long pageNumber ) throws EngineException
	{
		try
		{
			// start the render

			ReportContentLoader loader = new ReportContentLoader(
					executionContext );
			ReportExecutor executor = new ReportExecutor( executionContext );
			executionContext.setExecutor( executor );
			IContentEmitter emitter = createContentEmitter( executor );
			startRender( );
			loader.loadPage( pageNumber, emitter );
			closeRender( );
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setEmitterID(java.lang.String)
	 */
	public void setEmitterID( String id )
	{
		this.emitterID = id;
	}

	/**
	 * @return the emitter ID to be used to render this report. Could be null,
	 *         in which case the engine will choose one emitter that matches the
	 *         requested output format.
	 */
	public String getEmitterID( )
	{
		return this.emitterID;
	}

	public void close( )
	{
		closeReportDocument( );
		super.close( );
	}
}
