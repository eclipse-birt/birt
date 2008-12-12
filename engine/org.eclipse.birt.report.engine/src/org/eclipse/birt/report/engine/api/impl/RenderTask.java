/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation. All rights reserved. This
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
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.executor.ReportExtensionExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.extension.engine.IRenderExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.ReportPageExecutor;
import org.eclipse.birt.report.engine.internal.document.ReportletExecutor;
import org.eclipse.birt.report.engine.internal.document.v4.PageRangeIterator;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.pdf.emitter.PDFLayoutEmitterProxy;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCView;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class RenderTask extends EngineTask implements IRenderTask
{

	private IReportDocument reportDocument;
	private IReportRunnable reportRunnable;
	private InnerRender innerRender;
	private long outputPageCount;

	private boolean designLoaded = false;

	/**
	 * @param engine
	 *            the report engine
	 * @param reportDoc
	 *            the report document instance
	 */
	public RenderTask( ReportEngine engine, IReportDocument reportDocument )
	{
		this( engine, null, reportDocument );
	}

	/**
	 * @param engine
	 *            the report engine
	 * @param runnable
	 *            the report runnable object
	 * @param reportDoc
	 *            the report document instance
	 */
	public RenderTask( ReportEngine engine, IReportRunnable runnable,
			IReportDocument reportDoc )
	{
		super( engine, IEngineTask.TASK_RENDER );
		this.reportDocument = reportDoc;
		this.reportRunnable = runnable;

		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		executionContext.setReportDocument( reportDocument );

		assert ( reportDocument instanceof IInternalReportDocument );
		IInternalReportDocument internalReportDoc = (IInternalReportDocument) reportDocument;
		if ( reportRunnable == null )
		{
			// load the report runnable from the document
			IReportRunnable documentRunnable = getOnPreparedRunnable( reportDocument );
			setReportRunnable( documentRunnable );
			Report reportIR = internalReportDoc
					.getReportIR( (ReportDesignHandle) documentRunnable
							.getDesignHandle( ) );
			executionContext.setReport( reportIR );
		}
		else
		{
			// the report runnable is set by the user
			setReportRunnable( reportRunnable );
			Report reportIR = new ReportParser( )
					.parse( (ReportDesignHandle) reportRunnable
							.getDesignHandle( ) );
			executionContext.setReport( reportIR );
		}

		ClassLoader documentLoader = internalReportDoc.getClassLoader( );
		executionContext.setApplicationClassLoader( documentLoader );

		// load the information from the report document
		setParameterValues( reportDocument.getParameterValues( ) );
		setParameterDisplayTexts( reportDocument.getParameterDisplayTexts( ) );
		usingParameterValues( );
		executionContext.registerGlobalBeans( reportDocument
				.getGlobalVariables( null ) );
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
		designLoaded = false;
		unloadRenderExtensions( );
		unloadVisiblePages( );
		super.close( );
	}

	public void render( String pageRange ) throws EngineException
	{
		setPageRange( pageRange );
		render( );
	}

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
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			if ( renderOptions == null )
			{
				throw new EngineException( MessageConstants.RENDER_OPTION_ERROR ); //$NON-NLS-1$
			}
			IReportRunnable runnable = executionContext.getRunnable( );
			if ( runnable == null )
			{
				throw new EngineException(
						MessageConstants.REPORT_DESIGN_NOT_FOUND_ERROR,
						new Object[]{reportDocument.getName( )} );
			}

			if ( !designLoaded )
			{
				updateRtLFlag( );
				// load report design
				loadDesign( );
				// synchronize the design ir's version with the document
				String version = reportDocument.getVersion( );
				Report report = executionContext.getReport( );
				report.updateVersion( version );

				designLoaded = true;
			}

			if ( innerRender == null )
			{
				innerRender = new AllPageRender( new long[]{1,
						reportDocument.getPageCount( )} );
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
			throw new EngineException( MessageConstants.REPORT_RUN_ERROR, ex ); //$NON-NLS-1$
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
			changeStatusToStopped( );
			switchClassLoaderBack( );
		}
	}

	public long getPageCount( ) throws EngineException
	{
		if ( runningStatus != STATUS_SUCCEEDED )
		{
			throw new EngineException(
					MessageConstants.RENDERTASK_NOT_FINISHED_ERROR );
		}
		return Math.max( outputPageCount, executionContext.getPageCount( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.IRenderTask#render()
	 */
	public void setPageNumber( long pageNumber ) throws EngineException
	{
		long totalVisiblePageCount = getTotalPage( );
		if ( pageNumber <= 0 || pageNumber > totalVisiblePageCount )
		{
			throw new EngineException( MessageConstants.PAGE_NOT_FOUND_ERROR,
					new Long( //$NON-NLS-1$
							pageNumber ) );
		}
		innerRender = new PageRangeRender( new long[]{pageNumber, pageNumber} );
	}

	public void setInstanceID( String iid ) throws EngineException
	{
		setInstanceID( InstanceID.parse( iid ) );
	}

	public void setInstanceID( InstanceID iid ) throws EngineException
	{
		long offset = reportDocument.getInstanceOffset( iid );
		if ( offset == -1 )
		{
			throw new EngineException(
					MessageConstants.INVALID_INSTANCE_ID_ERROR, iid );
		}

		innerRender = new ReportletRender( offset );
	}

	public void setReportlet( String bookmark ) throws EngineException
	{
		long offset = reportDocument.getBookmarkOffset( bookmark );
		if ( offset == -1 )
		{
			throw new EngineException( MessageConstants.INVALID_BOOKMARK_ERROR,
					bookmark );
		}

		innerRender = new ReportletRender( offset );
	}

	public void setPageRange( String pageRange ) throws EngineException
	{
		long totalVisiblePageCount = RenderTask.this.getTotalPage( );
		List list = PageSequenceParse.parsePageSequence( pageRange,
				totalVisiblePageCount );
		if ( list.size( ) == 1 )
		{
			long[] range = (long[]) list.get( 0 );
			long totalPageCount = reportDocument.getPageCount( );
			if ( range[0] == 1 && range[1] == totalPageCount )
			{
				innerRender = new AllPageRender( new long[]{1,
						totalPageCount} );				
				return;
			}
		}
		innerRender = new PageRangeRender( list );
	}

	public void setBookmark( String bookmark ) throws EngineException
	{
		long pageNumber = getPageNumber( bookmark );
		if ( pageNumber <= 0 )
		{
			throw new EngineException(
					MessageConstants.BOOKMARK_NOT_FOUND_ERROR, bookmark ); //$NON-NLS-1$
		}
		innerRender = new PageRangeRender( new long[]{pageNumber, pageNumber} );
	}

	private interface InnerRender
	{

		void render( ) throws Exception;
	}

	/**
	 * Renders a range of pages.
	 */
	private class PageRangeRender implements InnerRender
	{

		protected ArrayList<long[]> pageSequences;

		public PageRangeRender( long[] arrayRange )
		{
			this.pageSequences = new ArrayList<long[]>( );
			pageSequences.add( arrayRange );
		}

		public PageRangeRender( List<long[]> pageRange )
		{
			this.pageSequences = new ArrayList<long[]>( pageRange );
		}

		protected boolean isPagedExecutor( )
		{
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				return !needPaginate( );
			}
			int pageCount = getPageCount( );
			if ( pageCount == 1 )
			{
				return true;
			}
			IRenderOption renderOption = executionContext.getRenderOption( );
			HTMLRenderOption htmlRenderOption = new HTMLRenderOption(
					renderOption );
			boolean htmlPagination = htmlRenderOption.getHtmlPagination( );
			if ( !htmlPagination )
			{
				return false;
			}
			return true;
		}

		protected int getPageCount( )
		{
			int pageCount = 0;
			for ( long[] pageSeg : pageSequences )
			{
				long start = pageSeg[0];
				long end = pageSeg[1];
				pageCount += ( end - start ) + 1;
			}
			return pageCount;
		}

		// identify if layout engine need do paginate
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
						MessageConstants.PAGE_HINT_LOADING_ERROR, ex ) );
				return null;
			}
		}

		protected void supportHtmlPagination( )
		{
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				Object htmlPaginationObj = renderOptions
						.getOption( IHTMLRenderOption.HTML_PAGINATION );
				if ( htmlPaginationObj != null
						&& htmlPaginationObj instanceof Boolean )
				{
					boolean htmlPagination = ( (Boolean) htmlPaginationObj )
							.booleanValue( );
					if ( htmlPagination )
					{
						if ( renderOptions
								.getOption( IPDFRenderOption.FIT_TO_PAGE ) == null )
						{
							renderOptions.setOption(
									IPDFRenderOption.FIT_TO_PAGE, Boolean.TRUE );
						}
						renderOptions.setOption(
								IPDFRenderOption.PAGEBREAK_PAGINATION_ONLY,
								Boolean.TRUE );
					}
				}
			}
		}

		public void render( ) throws Exception
		{
			// start the render
			setupRenderOption( );
			IContentEmitter emitter = createContentEmitter( );
			supportHtmlPagination( );
			String format = executionContext.getOutputFormat( );
			boolean paged = isPagedExecutor( );

			// setup the page sequences
			List<long[]> physicalPageSequences = getPhysicalPageSequence( pageSequences );
			long filteredTotalPage = getTotalPage( );
			long totalPage = reportDocument.getPageCount( );
			if ( filteredTotalPage != totalPage )
			{
				executionContext.setFilteredTotalPage( filteredTotalPage );
			}

			ReportPageExecutor pagesExecutor = new ReportPageExecutor(
					executionContext, physicalPageSequences, paged );
			
			IReportExecutor executor = createRenderExtensionExecutor( pagesExecutor );
			executor = new SuppressDuplciateReportExecutor( executor );
			executor = new LocalizedReportExecutor( executionContext, executor );
			executionContext.setExecutor( executor );
			initializeContentEmitter( emitter, executor );

			IReportLayoutEngine layoutEngine = createReportLayoutEngine(
					pagination, renderOptions );

			layoutEngine.setLocale( executionContext.getLocale( ) );

			PageRangeIterator iter = new PageRangeIterator( physicalPageSequences );

			if ( ExtensionManager.PAGE_BREAK_PAGINATION.equals( pagination )
					|| ExtensionManager.PAPER_SIZE_PAGINATION
							.equals( pagination ) )
			{
				if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
				{
					OnPageBreakLayoutPageHandle handle = new OnPageBreakLayoutPageHandle(
							executionContext );
					layoutEngine.setPageHandler( handle );

					CompositeContentEmitter outputEmitters = new CompositeContentEmitter(
							format );
					outputEmitters.addEmitter( new PDFLayoutEmitterProxy(
							executor, emitter, renderOptions, executionContext,
							getTotalPage( ) ) );
					outputEmitters.addEmitter( handle.getEmitter( ) );
					emitter = outputEmitters;
					if ( needPaginate( ) )
					{
						startRender( );
						IReportContent report = executor.execute( );
						outputEmitters.start( report );
						long pageNumber = iter.next( );
						layoutEngine.setTotalPageCount( getTotalPage( ) );
						if( pageNumber!=1 )
						{
							layoutEngine.setLayoutPageHint( getPageHint(
									pagesExecutor, pageNumber ) );
						}
						layoutEngine.layout( executor, report, outputEmitters,
								true );
						layoutEngine.close( );
						outputEmitters.end( report );

						closeRender( );
						executor.close( );
						outputPageCount = layoutEngine.getPageCount( );
						return;
					}

				}
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
							if ( filteredTotalPage != totalPage )
							{
								long filteredPageNumber = getLogicalPageNumber( pageNumber );
								executionContext
										.setFilteredPageNumber( filteredPageNumber );
							}
							IReportExecutor pExecutor = new ReportExecutorWrapper(
									pageExecutor, executor );
							layoutEngine.setLayoutPageHint( getPageHint(
									pagesExecutor, pageNumber ) );
							layoutEngine.layout( pExecutor, report, emitter,
									false );
						}
					}
					layoutEngine.close( );
				}
				else
				{
					if ( iter.hasNext( ) )
					{
						long pageNumber = iter.next( );
						layoutEngine.setLayoutPageHint( getPageHint(
								pagesExecutor, pageNumber ) );
						layoutEngine.layout( executor, report, emitter, false );
						layoutEngine.close( );
					}
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
				layoutEngine.layout( executor, report, emitter, false );
				layoutEngine.close( );
				emitter.end( report );
				closeRender( );
				executor.close( );
			}
			outputPageCount = layoutEngine.getPageCount( );
		}
	}

	private class ReportletRender implements InnerRender
	{

		private long offset;

		ReportletRender( long offset )
		{
			this.offset = offset;
		}

		public void render( ) throws Exception
		{
			// start the render
			setupRenderOption( );
			IContentEmitter emitter = createContentEmitter( );
			String format = executionContext.getOutputFormat( );
			IReportExecutor executor = new ReportletExecutor( executionContext,
					offset );
			executor = createRenderExtensionExecutor( executor );
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
				outputEmitters.addEmitter( new PDFLayoutEmitterProxy( executor,
						emitter, renderOptions, executionContext,
						getTotalPage( ) ) );
				outputEmitters.addEmitter( handle.getEmitter( ) );
				emitter = outputEmitters;
			}

			startRender( );
			IReportContent report = executor.execute( );
			emitter.start( report );
			layoutEngine.layout( executor, report, emitter, false );
			layoutEngine.close( );
			emitter.end( report );
			closeRender( );
			executor.close( );
			outputPageCount = layoutEngine.getPageCount( );
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

	public long getPageNumber( String bookmark ) throws EngineException
	{
		int physicalPageNumber = (int) executionContext.getReportDocument( )
				.getPageNumber( bookmark );
		return getLogicalPageNumber( physicalPageNumber );
	}

	public ITOCTree getTOCTree( ) throws EngineException
	{
		IReportDocument document = executionContext.getReportDocument( );
		String format = IRenderOption.OUTPUT_FORMAT_HTML;
		if ( renderOptions != null )
		{
			String renderFormat = renderOptions.getOutputFormat( );
			if ( renderFormat != null )
			{
				format = renderFormat;
			}
		}

		ULocale ulocale = getULocale( );
		TimeZone timeZone = getTimeZone( );
		ReportDesignHandle design = executionContext.getDesign( );

		if ( document instanceof IInternalReportDocument )
		{
			ITreeNode tocTree = ( (IInternalReportDocument) document )
					.getTOCTree( );
			if ( tocTree != null )
			{
				LogicalPageSequence visiblePages = loadVisiblePages( );
				if ( visiblePages != null )
				{
					return new TOCView( tocTree, design, ulocale, timeZone,
							format, new VisiblePageFilter( document,
									visiblePages ) );
				}
				else
				{
					return new TOCView( tocTree, design, ulocale, timeZone,
							format );
				}
			}
		}
		return TOCView.EMPTY_TOC_VIEW;
	}

	public long getTotalPage( ) throws EngineException
	{
		LogicalPageSequence visiblePages = loadVisiblePages( );
		if ( visiblePages != null )
		{
			return visiblePages.getTotalVisiblePageCount( );
		}
		return reportDocument.getPageCount( );
	}

	private long getLogicalPageNumber( long physicalPageNumber )
			throws EngineException
	{
		LogicalPageSequence visiblePages = loadVisiblePages( );
		if ( visiblePages != null )
		{
			return visiblePages.getLogicalPageNumber( physicalPageNumber );
		}
		return physicalPageNumber;
	}

	private ArrayList<long[]> getPhysicalPageSequence(
			ArrayList<long[]> logicalPages ) throws EngineException
	{
		LogicalPageSequence visiblePages = loadVisiblePages( );
		if ( visiblePages != null )
		{
			long[][] pages = visiblePages.getPhysicalPageNumbers( logicalPages
					.toArray( new long[logicalPages.size( )][] ) );
			ArrayList<long[]> physicalPages = new ArrayList<long[]>(
					pages.length );
			for ( int i = 0; i < pages.length; i++ )
			{
				physicalPages.add( pages[i] );
			}
			return physicalPages;
		}
		return logicalPages;
	}

	boolean renderExtensionLoaded;
	ArrayList<IRenderExtension> renderExtensions;
	boolean visiblePageLoaded;
	LogicalPageSequence logicalPageSequence;

	private ArrayList<IRenderExtension> loadRenderExtensions( )
			throws EngineException
	{
		if ( renderExtensionLoaded == false )
		{
			String[] extensions = executionContext.getEngineExtensions( );
			if ( extensions != null )
			{
				renderExtensions = new ArrayList<IRenderExtension>( );
				EngineExtensionManager manager = executionContext
						.getEngineExtensionManager( );

				for ( String extName : extensions )
				{
					IRenderExtension renderExtension = manager
							.getRenderExtension( extName );
					if ( renderExtension != null )
					{
						renderExtensions.add( renderExtension );
					}
				}
			}
		}
		return renderExtensions;
	}

	private void unloadRenderExtensions( )
	{
		if ( renderExtensions != null )
		{
			for ( IRenderExtension renderExtension : renderExtensions )
			{
				renderExtension.close( );
			}
			renderExtensions = null;
		}
		renderExtensionLoaded = false;
	}

	private LogicalPageSequence loadVisiblePages( ) throws EngineException
	{
		if ( visiblePageLoaded == false )
		{
			ArrayList<IRenderExtension> renderExtensions = loadRenderExtensions( );
			if ( renderExtensions != null )
			{
				ArrayList<long[][]> pages = new ArrayList<long[][]>( );
				for ( IRenderExtension renderExtension : renderExtensions )
				{
					long[][] visiblePages = renderExtension.getVisiblePages( );
					if ( visiblePages != null )
					{
						pages.add( visiblePages );
					}
				}
				if ( !pages.isEmpty( ) )
				{
					long physicalTotalPage = reportDocument.getPageCount( );
					logicalPageSequence = new LogicalPageSequence( pages,
							physicalTotalPage );
				}
			}
			visiblePageLoaded = true;
		}
		return logicalPageSequence;
	}

	private void unloadVisiblePages( )
	{
		visiblePageLoaded = false;
		logicalPageSequence = null;
	}
	
	protected IReportExecutor createRenderExtensionExecutor(
			IReportExecutor executor ) throws EngineException
	{
		// prepare the extension executor
		ArrayList<IRenderExtension> renderExtensions = loadRenderExtensions( );
		if ( renderExtensions != null )
		{
			ArrayList<IContentProcessor> processors = new ArrayList<IContentProcessor>( );
			for ( IRenderExtension extension : renderExtensions )
			{
				IContentProcessor processor = extension.getRenderProcessor( );
				if ( processor != null )
				{
					processors.add( processor );
				}
			}
			if ( !processors.isEmpty( ) )
			{
				return new ReportExtensionExecutor( executionContext, executor,
						processors.toArray( new IContentProcessor[processors
								.size( )] ) );
			}
		}
		return executor;
	}
	
}