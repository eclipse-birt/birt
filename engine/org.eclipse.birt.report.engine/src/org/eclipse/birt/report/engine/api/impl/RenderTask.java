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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.UnsupportedFormatException;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.EngineEmitterServices;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.executor.ReportExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.IReportContentLoader;
import org.eclipse.birt.report.engine.internal.document.ReportContentLoader;
import org.eclipse.birt.report.engine.internal.executor.doc.ReportPageReader;
import org.eclipse.birt.report.engine.internal.executor.doc.ReportletReader;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.engine.layout.html.HTMLTableLayoutNestEmitter;

public class RenderTask extends EngineTask implements IRenderTask
{

	IReportDocument reportDoc;
	String emitterID;
	int paginationType;

	private InnerRender innerRender;

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable object
	 * @param reportDoc
	 *            the report document instance
	 */
	public RenderTask( IReportEngine engine, IReportRunnable runnable,
			IReportDocument reportDoc )
	{
		super( engine, runnable );

		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		// open the report document
		openReportDocument( reportDoc );

		// load report design
		loadDesign( );

		innerRender = new PageRangeRender( new long[]{1,
				this.reportDoc.getPageCount( )} );
	}

	protected void openReportDocument( IReportDocument reportDoc )
	{
		this.reportDoc = reportDoc;
		executionContext.setReportDocument( reportDoc );

		// load the informationf rom the report document
		setParameterValues( reportDoc.getParameterValues( ) );
		// setParameterDisplayTexts( reportDoc.getParameterDisplayTexts( ) );
		usingParameterValues( );
		executionContext.registerGlobalBeans( reportDoc
				.getGlobalVariables( null ) );
	}

	protected void closeReportDocument( )
	{
		// the report document is shared by mutiple render task,
		// it is open by the caller, it should be closed by the caller.
		// reportDoc.close( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render(long)
	 */
	public void render( long pageNumber ) throws EngineException
	{
		setPageNumber( pageNumber );
		render( );
	}

	protected IContentEmitter createContentEmitter( ) throws EngineException
	{
		String format = executionContext.getOutputFormat( );

		if ( "html".equalsIgnoreCase( format ) )
		{
			paginationType = IReportContentLoader.MULTI_PAGE;
		}
		else
		{
			paginationType = IReportContentLoader.NO_PAGE;
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
			throw new UnsupportedFormatException(
					MessageConstants.FORMAT_NOT_SUPPORTED_EXCEPTION, format );
		}

		IContentEmitter emitter;
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

		// the output will be paginate.
		if ( paginationType != IReportContentLoader.NO_PAGE )
		{
			emitter = new HTMLTableLayoutNestEmitter( emitter );
		}

		return emitter;
	}

	private void initializeContentEmitter( IContentEmitter emitter,
			IReportExecutor executor )
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
			IContentEmitter emitter = createContentEmitter( );
			Report reportDesign = executionContext.getReport( );
			String format = executionContext.getOutputFormat( );
			if ( "pdf".equalsIgnoreCase( format ) ) //$NON-NLS-1$
			{
				IReportExecutor executor = new ReportPageReader(
						executionContext, pageNumber, paginationType );
				executor = new LocalizedReportExecutor( executionContext,
						executor );
				executionContext.setExecutor( executor );
				initializeContentEmitter( emitter, executor );
				IReportLayoutEngine layoutEngine = LayoutEngineFactory
						.createLayoutEngine( format );

				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
						executionContext );
				layoutEngine.setPageHandler( handle );

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
						format );
				outputEmitters.addEmitter( emitter );
				outputEmitters.addEmitter( handle.getEmitter( ) );

				startRender( );
				layoutEngine.layout( executor, outputEmitters, false );
				closeRender( );
				executor.close( );
			}
			else
			{
				IReportExecutor executor = new ReportExecutor(
						executionContext, reportDesign, null );
				executor = new LocalizedReportExecutor( executionContext,
						executor );
				executionContext.setExecutor( executor );
				initializeContentEmitter( emitter, executor );
				// start the render
				ReportContentLoader loader = new ReportContentLoader(
						executionContext );
				startRender( );
				loader.loadPage( pageNumber, paginationType, emitter );
				closeRender( );
				executor.close( );
			}
		}
		catch ( EngineException e )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", e ); //$NON-NLS-1$
			throw e;
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex ); //$NON-NLS-1$
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
	}

	/**
	 * @param pageNumber
	 *            the page to be rendered
	 * @throws EngineException
	 *             throws exception if there is a rendering error
	 */
	protected void doRender( List pageSequences ) throws EngineException
	{
		if ( pageSequences.size( ) == 0 )
		{
			return;
		}
		try
		{
			// start the render
			Report reportDesign = executionContext.getReport( );
			IContentEmitter emitter = createContentEmitter( );
			String format = executionContext.getOutputFormat( );
			if ( "pdf".equalsIgnoreCase( format ) ) //$NON-NLS-1$
			{
				IReportExecutor executor = new ReportPageReader(
						executionContext, pageSequences, paginationType );
				executor = new LocalizedReportExecutor( executionContext,
						executor );
				executionContext.setExecutor( executor );
				initializeContentEmitter( emitter, executor );

				IReportLayoutEngine layoutEngine = LayoutEngineFactory
						.createLayoutEngine( format );

				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
						executionContext );
				layoutEngine.setPageHandler( handle );

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
						format );
				outputEmitters.addEmitter( emitter );
				outputEmitters.addEmitter( handle.getEmitter( ) );

				startRender( );
				layoutEngine.layout( executor, outputEmitters, true );
				closeRender( );
				executor.close( );
			}
			else
			{
				IReportExecutor executor = new ReportExecutor(
						executionContext, reportDesign, null );
				executor = new LocalizedReportExecutor( executionContext,
						executor );
				executionContext.setExecutor( executor );
				initializeContentEmitter( emitter, executor );
				ReportContentLoader loader = new ReportContentLoader(
						executionContext );
				startRender( );
				IRenderOption renderOption = executionContext.getRenderOption( );
				if ( renderOption instanceof HTMLRenderOption )
				{
					boolean htmlPagination = ( (HTMLRenderOption) renderOption )
							.getHtmlPagination( );
					if ( !htmlPagination )
					{
						paginationType = IReportContentLoader.SINGLE_PAGE;
					}
				}
				loader.loadPageRange( pageSequences, paginationType, emitter );
				closeRender( );
				executor.close( );
			}
		}
		catch ( EngineException e )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", e ); //$NON-NLS-1$
			throw e;
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex ); //$NON-NLS-1$
		}
		catch ( OutOfMemoryError err )
		{
			log.log( Level.SEVERE,
					"An OutOfMemory error happened while running the report." ); //$NON-NLS-1$
			throw err;
		}
	}

	/**
	 * @param offset
	 *            the offset of the reportlet to be rendered
	 * @throws EngineException
	 *             throws exception if there is a rendering error
	 */
	protected void doRenderReportlet( long offset ) throws EngineException
	{
		try
		{
			if ( offset != -1 )
			{
				// start the render

				IContentEmitter emitter = createContentEmitter( );
				Report reportDesign = executionContext.getReport( );
				String format = executionContext.getOutputFormat( );
				if ( "pdf".equalsIgnoreCase( format ) ) //$NON-NLS-1$
				{
					IReportExecutor executor = new ReportletReader(
							executionContext, offset );
					executor = new LocalizedReportExecutor( executionContext,
							executor );
					executionContext.setExecutor( executor );
					initializeContentEmitter( emitter, executor );
					IReportLayoutEngine layoutEngine = LayoutEngineFactory
							.createLayoutEngine( format );

					OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
							executionContext );
					layoutEngine.setPageHandler( handle );

					CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
							format );
					outputEmitters.addEmitter( emitter );
					outputEmitters.addEmitter( handle.getEmitter( ) );

					startRender( );
					layoutEngine.layout( executor, outputEmitters, false );
					closeRender( );
					executor.close( );
				}
				else
				{
					ReportContentLoader loader = new ReportContentLoader(
							executionContext );
					IReportExecutor executor = new ReportExecutor(
							executionContext, reportDesign, null );
					executor = new LocalizedReportExecutor( executionContext,
							executor );
					executionContext.setExecutor( executor );

					initializeContentEmitter( emitter, executor );
					startRender( );
					loader.loadReportlet( offset, emitter );
					closeRender( );
					executor.close( );
				}
			}
		}
		catch ( EngineException e )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", e ); //$NON-NLS-1$
			throw e;
		}
		catch ( Exception ex )
		{
			log.log( Level.SEVERE,
					"An error happened while running the report. Cause:", ex ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", ex ); //$NON-NLS-1$
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

	public void render( String pageRange ) throws EngineException
	{
		setPageRange( pageRange );
		render( );
	}

	private List parsePageSequence( String pageRange, long totalPage )
	{
		ArrayList list = new ArrayList( );
		if ( null == pageRange
				|| "".equals( pageRange ) || pageRange.toUpperCase( ).indexOf( "ALL" ) >= 0 ) //$NON-NLS-1$ //$NON-NLS-2$
		{
			list.add( new long[]{1, totalPage} );
			return list;
		}
		String[] ps = pageRange.split( "," ); //$NON-NLS-1$
		for ( int i = 0; i < ps.length; i++ )
		{
			try
			{
				if ( ps[i].indexOf( "-" ) > 0 ) //$NON-NLS-1$
				{
					String[] psi = ps[i].split( "-" ); //$NON-NLS-1$
					if ( psi.length == 2 )
					{
						long start = Long.parseLong( psi[0].trim( ) );
						long end = Long.parseLong( psi[1].trim( ) );
						if ( end > start )
						{
							list.add( new long[]{Math.max( start, 1 ),
									Math.min( end, totalPage )} );
						}
					}
					else
					{
						log.log( Level.SEVERE,
								"error page number range: {0}", ps[i] ); //$NON-NLS-1$
					}
				}
				else
				{
					long number = Long.parseLong( ps[i].trim( ) );
					if ( number > 0 && number <= totalPage )
					{
						list.add( new long[]{number, number} );
					}
					else
					{
						log.log( Level.SEVERE,
								"error page number range: {0}", ps[i] ); //$NON-NLS-1$
					}

				}
			}
			catch ( NumberFormatException ex )
			{
				log.log( Level.SEVERE, "error page number rang:", ps[i] ); //$NON-NLS-1$
			}
		}
		return sort( list );
	}

	private List sort( List list )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			long[] currentI = (long[]) list.get( i );
			int minIndex = i;
			long[] min = currentI;
			for ( int j = i + 1; j < list.size( ); j++ )
			{
				long[] currentJ = (long[]) list.get( j );
				if ( currentJ[0] < min[0] )
				{
					minIndex = j;
					min = currentJ;
				}
			}
			if ( minIndex != i )
			{
				// swap
				list.set( i, min );
				list.set( minIndex, currentI );
			}
		}
		long[] current = null;
		long[] last = null;
		ArrayList ret = new ArrayList( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			current = (long[]) list.get( i );
			if ( last != null )
			{
				if ( current[1] <= last[1] )
					continue;
				if ( current[0] <= last[1] )
					current[0] = last[1];
				ret.add( current );
			}
			else
			{
				ret.add( current );
			}
			last = current;
		}
		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render(org.eclipse.birt.report.engine.api.InstanceID)
	 */
	public void render( InstanceID iid ) throws EngineException
	{
		setInstanceID( iid );
		render( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render()
	 */
	public void render( ) throws EngineException
	{

		try
		{
			runningStatus = RUNNING_STATUS_RUNNING;
			if ( renderOptions == null )
			{
				throw new EngineException(
						"Render options have to be specified to render a report." ); //$NON-NLS-1$
			}
			if ( runnable == null )
			{
				throw new EngineException(
						"Can not find the report design in the report document {0}.",
						new Object[]{reportDoc.getName( )} );
			}
			innerRender.render( );
		}
		finally
		{
			runningStatus = RUNNING_STATUS_STOP;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render()
	 */
	public void setPageNumber( long pageNumber ) throws EngineException
	{
		long totalPage = reportDoc.getPageCount( );
		if ( pageNumber <= 0 || pageNumber > totalPage )
		{
			throw new EngineException( "Page {0} is not found ", new Long( //$NON-NLS-1$
					pageNumber ) );
		}
		innerRender = new PageRender( pageNumber );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setInstanceID(
	 *      InstanceID iid )
	 */
	public void setInstanceID( InstanceID iid ) throws EngineException
	{
		innerRender = new ReportletRender( iid );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setPageRange( String
	 *      pageRange )
	 */
	public void setPageRange( String pageRange ) throws EngineException
	{
		innerRender = new PageRangeRender( parsePageSequence( pageRange,
				reportDoc.getPageCount( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#setBookmark( String
	 *      bookmark )
	 */
	public void setBookmark( String bookmark ) throws EngineException
	{
		long pageNumber = reportDoc.getPageNumber( bookmark );
		if ( pageNumber <= 0 )
		{
			throw new EngineException( "Can not find bookmark :{0}", bookmark ); //$NON-NLS-1$
		}
		innerRender = new PageRender( pageNumber );
	}

	public void setReportlet( String bookmark ) throws EngineException
	{
		innerRender = new ReportletRender( bookmark );
	}

	private interface InnerRender
	{

		void render( ) throws EngineException;
	}

	/**
	 * Renders a page with a page number.
	 */
	private class PageRender implements InnerRender
	{

		private long pageNumber;

		public PageRender( long pageNumber )
		{
			this.pageNumber = pageNumber;
		}

		public void render( ) throws EngineException
		{
			RenderTask.this.doRender( pageNumber );
		}
	}

	/**
	 * Renders a range of pages.
	 */
	private class PageRangeRender implements InnerRender
	{

		private List pageRange;

		public PageRangeRender( long[] arrayRange )
		{
			this.pageRange = new ArrayList( );
			pageRange.add( arrayRange );
		}

		public PageRangeRender( List pageRange )
		{
			this.pageRange = pageRange;
		}

		public void render( ) throws EngineException
		{
			RenderTask.this.doRender( pageRange );
		}
	}

	private class ReportletRender implements InnerRender
	{

		private long offset;

		ReportletRender( InstanceID iid ) throws EngineException
		{
			this.offset = reportDoc.getInstanceOffset( iid );
			if ( offset == -1 )
			{
				throw new EngineException( "Invalid instance id :" + iid ); //$NON-NLS-1$
			}
		}

		ReportletRender( String bookmark ) throws EngineException
		{
			this.offset = reportDoc.getBookmarkOffset( bookmark );
			if ( offset == -1 )
			{
				throw new EngineException( "Invalid bookmark :" + bookmark ); //$NON-NLS-1$
			}
		}

		public void render( ) throws EngineException
		{
			RenderTask.this.doRenderReportlet( offset );
		}
	}

	public void setInstanceID( String iid ) throws EngineException
	{
		setInstanceID( InstanceID.parse( iid ) );
	}
}