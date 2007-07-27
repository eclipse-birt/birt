/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation. All rights reserved. This
 * program and the accompanying materials are made available under the terms of
 * the Eclipse Public License v1.0 which accompanies this distribution, and is
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

// @FIXME: 2.1.3

package org.eclipse.birt.report.engine.api.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.internal.document.ReportPageExecutor;
import org.eclipse.birt.report.engine.internal.document.ReportletExecutor;
import org.eclipse.birt.report.engine.internal.document.v4.PageRangeIterator;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.presentation.IPageHint;

public class RenderTask extends EngineTask implements IRenderTask
{

	IReportDocument reportDoc;
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
		super( engine, runnable, IEngineTask.TASK_RENDER );

		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		// open the report document
		openReportDocument( reportDoc );

		// load report design
		loadDesign( );

		innerRender = new AllPageRender( new long[]{1,
				this.reportDoc.getPageCount( )} );
	}

	protected void openReportDocument( IReportDocument reportDoc )
	{
		this.reportDoc = reportDoc;
		executionContext.setReportDocument( reportDoc );

		// load the informationf rom the report document
		setParameterValues( reportDoc.getParameterValues( ) );
		setParameterDisplayTexts( reportDoc.getParameterDisplayTexts( ) );
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
			changeStatusToRunning( );
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
		catch ( Throwable t )
		{
			log.log( Level.SEVERE,
					"Error happened while running the report.", t ); //$NON-NLS-1$
			throw new EngineException(
					"Error happened while running the report", t ); //$NON-NLS-1$
		}
		finally
		{
			changeStatusToStopped( );
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
		innerRender = new PageRangeRender( new long[]{pageNumber, pageNumber} );
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
		if ( null == pageRange
				|| "".equals( pageRange ) || pageRange.toUpperCase( ).indexOf( "ALL" ) >= 0 ) //$NON-NLS-1$ //$NON-NLS-2$
		{
			innerRender = new AllPageRender( new long[] {1, reportDoc.getPageCount( )} );
		}
		else
		{		
			innerRender = new PageRangeRender( parsePageSequence( pageRange,
					reportDoc.getPageCount( ) ) );
		}
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
		innerRender = new PageRangeRender( new long[]{pageNumber, pageNumber} );
	}

	public void setReportlet( String bookmark ) throws EngineException
	{
		innerRender = new ReportletRender( bookmark );
	}

	private abstract class InnerRender
	{

		void render( ) throws Exception
		{
			setupRenderOption( );
			IContentEmitter emitter = createContentEmitter( );
			String format = executionContext.getOutputFormat( );
			IReportExecutor executor = createExecutor( );
			executor = new SuppressDuplciateReportExecutor( executor );
			executor = new LocalizedReportExecutor( executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );

			IReportLayoutEngine layoutEngine = createReportLayoutEngine(
					pagination, renderOptions );

			layoutEngine.setLocale( executionContext.getLocale( ) );
			OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
					executionContext );
			layoutEngine.setPageHandler( handle );

			CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
					format );
			outputEmitters.addEmitter( emitter );
			outputEmitters.addEmitter( handle.getEmitter( ) );

			startRender( );
			boolean paged = true;
			if ( ExtensionManager.PAGE_BREAK_PAGINATION.equals( pagination ) )
			{
				IRenderOption renderOption = executionContext.getRenderOption( );
				HTMLRenderOption htmlRenderOption = new HTMLRenderOption(
						renderOption );
				boolean htmlPagination = htmlRenderOption.getHtmlPagination( );
				if ( !htmlPagination )
				{
					paged = false;
				}
			}
			else if ( ExtensionManager.NO_PAGINATION.equals( pagination ) )
			{
				paged = false;
			}
			IReportContent report = executor.execute( );
			outputEmitters.start( report );
			layoutEngine.layout( executor, report, outputEmitters, paged );
			outputEmitters.end( report );
			closeRender( );
			executor.close( );
		}

		IReportExecutor createExecutor( ) throws Exception
		{
			return null;
		}
	}
	
	/**
	 * Renders a range of pages.
	 */
	private class PageRangeRender extends InnerRender
	{

		protected List pageSequences;

		public PageRangeRender( long[] arrayRange )
		{
			this.pageSequences = new ArrayList( );
			pageSequences.add( arrayRange );
		}

		public PageRangeRender( List pageRange )
		{
			this.pageSequences = pageRange;
		}

		protected boolean isPagedExecutor( )
		{
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				return !needPaginate();
			}
			boolean paged = true;
			IRenderOption renderOption = executionContext.getRenderOption( );
			HTMLRenderOption htmlRenderOption = new HTMLRenderOption(
					renderOption );
			boolean htmlPagination = htmlRenderOption.getHtmlPagination( );
			if ( !htmlPagination )
			{
				paged = false;
			}
			return paged;
		}

		//identify if layout engine need do paginate
		protected boolean needPaginate( )
		{
			return false;
		}

		protected IPageHint getPageHint( ReportPageExecutor executor,
				long pageNumber )
		{
			try
			{
				return executor.getLayoutPageHint( pageNumber );
			}
			catch ( IOException ex )
			{
				executionContext.addException( new EngineException(
						"can't load the page hint", ex ) );
				return null;
			}
		}

		void render( ) throws Exception
		{
			// start the render
			setupRenderOption( );
			IContentEmitter emitter = createContentEmitter( );
			String format = executionContext.getOutputFormat( );
			boolean paged = isPagedExecutor( );
			ReportPageExecutor pagesExecutor = new ReportPageExecutor(
					executionContext, pageSequences, paged );
			IReportExecutor executor = new SuppressDuplciateReportExecutor(
					pagesExecutor );
			executor = new LocalizedReportExecutor( executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );

			IReportLayoutEngine layoutEngine = createReportLayoutEngine(
					pagination, renderOptions );

			layoutEngine.setLocale( executionContext.getLocale( ) );

			PageRangeIterator iter = new PageRangeIterator( pageSequences );

			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				
				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
						executionContext );
				layoutEngine.setPageHandler( handle );

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
						format );
				outputEmitters.addEmitter( emitter );
				outputEmitters.addEmitter( handle.getEmitter( ) );
				startRender( );
				IReportContent report = executor.execute( );
				outputEmitters.start( report );
				if ( needPaginate() )
				{
					long pageNumber = iter.next( );
					layoutEngine.setLayoutPageHint( getPageHint( pagesExecutor,
							pageNumber ) );
					layoutEngine
							.layout( executor, report, outputEmitters, true );
				}
				else
				{
					while ( iter.hasNext( ) )
					{
						long pageNumber = iter.next( );
						IReportItemExecutor pageExecutor = executor
								.getNextChild( );
						if ( pageExecutor != null )
						{
							IReportExecutor pExecutor = new ReportExecutorWrapper(
									pageExecutor, executor );
							layoutEngine.setLayoutPageHint( getPageHint(
									pagesExecutor, pageNumber ) );
							layoutEngine.layout( pExecutor, report, emitter,
									false );
						}
					}
				}
				outputEmitters.end( report );

				closeRender( );
				executor.close( );
			}
			else if ( ExtensionManager.PAGE_BREAK_PAGINATION
					.equals( pagination ) )
			{
				startRender( );
				IReportContent report = executor.execute( );
				emitter.start( report );
				if ( paged )
				{
					// FIXME test it
					while ( iter.hasNext( ) )
					{
						long pageNumber = iter.next( );
						// here the pageExecutor will returns a report.root.
						IReportItemExecutor pageExecutor = executor
								.getNextChild( );
						if ( pageExecutor != null )
						{
							IReportExecutor pExecutor = new ReportExecutorWrapper(
									pageExecutor, executor );
							layoutEngine.setLayoutPageHint( getPageHint(
									pagesExecutor, pageNumber ) );
							layoutEngine.layout( pExecutor, report, emitter,
									false );
						}
					}
				}
				else
				{
					long pageNumber = iter.next( );
					layoutEngine.setLayoutPageHint( getPageHint( pagesExecutor,
							pageNumber ) );
					layoutEngine.layout( executor, report, emitter,
							false );
				}

				emitter.end( report );
				closeRender( );
				executor.close( );
			}
			else if ( ExtensionManager.NO_PAGINATION.equals( pagination ) )
			{
				startRender( );
				IReportContent report = executor.execute( );
				emitter.start( report );
				long pageNumber = iter.next( );
				layoutEngine.setLayoutPageHint( getPageHint( pagesExecutor,
						pageNumber ) );
				layoutEngine.layout( executor, report, emitter, false );
				emitter.end( report );
				closeRender( );
				executor.close( );
			}
		}
	}

	private class ReportletRender extends InnerRender
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

		void render( ) throws Exception
		{
			// start the render
			setupRenderOption( );
			IContentEmitter emitter = createContentEmitter( );
			String format = executionContext.getOutputFormat( );
			IReportExecutor executor = new ReportletExecutor( executionContext,
					offset );
			executor = new SuppressDuplciateReportExecutor( executor );
			executor = new LocalizedReportExecutor( executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );
			IReportLayoutEngine layoutEngine = createReportLayoutEngine(
					pagination, renderOptions );

			layoutEngine.setLocale( executionContext.getLocale( ) );

			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{

				OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
						executionContext );
				layoutEngine.setPageHandler( handle );

				CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
						format );
				outputEmitters.addEmitter( emitter );
				outputEmitters.addEmitter( handle.getEmitter( ) );
				emitter = outputEmitters;
			}

			startRender( );
			IReportContent report = executor.execute( );
			emitter.start( report );
			layoutEngine.layout( executor, report, emitter, false );
			emitter.end( report );
			closeRender( );
			executor.close( );
		}
	}
	
	private static class ReportExecutorWrapper implements IReportExecutor
	{
		IReportItemExecutor executor;
		IReportExecutor reportExecutor;

		ReportExecutorWrapper( IReportItemExecutor itemExecutor,
				IReportExecutor reportExecutor )
		{
			executor = itemExecutor;
			this.reportExecutor = reportExecutor;
		}

		public void close( )
		{
			executor.close( );
			
		}

		public IReportItemExecutor createPageExecutor( long pageNumber,
				MasterPageDesign pageDesign )
		{
			return reportExecutor.createPageExecutor( pageNumber, pageDesign );
		}

		public IReportContent execute( )
		{
			// FIXME: create the report content only once.
			return reportExecutor.execute( );
		}

		public IReportItemExecutor getNextChild( )
		{
			return executor.getNextChild( );
		}

		public boolean hasNextChild( )
		{
			return executor.hasNextChild( );
		}
		
	}

	public void setInstanceID( String iid ) throws EngineException
	{
		setInstanceID( InstanceID.parse( iid ) );
	}

	private class AllPageRender extends PageRangeRender
	{

		public AllPageRender( long[] arrayRange )
		{
			super( arrayRange );
		}

		protected boolean needPaginate( )
		{
			return true;
		}

	}
}