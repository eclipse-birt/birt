/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation. All rights reserved. This
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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IHTMLRenderOption;
import org.eclipse.birt.report.engine.api.IPDFRenderOption;
import org.eclipse.birt.report.engine.api.IProgressMonitor;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.EngineExtensionManager;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.PageVariable;
import org.eclipse.birt.report.engine.executor.ReportExtensionExecutor;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.extension.engine.IContentProcessor;
import org.eclipse.birt.report.engine.extension.engine.IRenderExtension;
import org.eclipse.birt.report.engine.extension.internal.ExtensionManager;
import org.eclipse.birt.report.engine.i18n.MessageConstants;
import org.eclipse.birt.report.engine.internal.document.PageHintReader;
import org.eclipse.birt.report.engine.internal.document.ReportPageExecutor;
import org.eclipse.birt.report.engine.internal.document.ReportletExecutor;
import org.eclipse.birt.report.engine.internal.document.v4.PageRangeIterator;
import org.eclipse.birt.report.engine.internal.executor.dup.SuppressDuplciateReportExecutor;
import org.eclipse.birt.report.engine.internal.executor.l18n.LocalizedReportExecutor;
import org.eclipse.birt.report.engine.internal.presentation.ReportDocumentInfo;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.layout.html.HTMLReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.pdf.emitter.LayoutEngineContext;
import org.eclipse.birt.report.engine.nLayout.LayoutEngine;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.engine.toc.ITOCReader;
import org.eclipse.birt.report.engine.toc.ITreeNode;
import org.eclipse.birt.report.engine.toc.TOCView;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;

import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class RenderTask extends EngineTask implements IRenderTask
{

	protected IReportDocument reportDocument;
	protected IReportRunnable reportRunnable;
	protected InnerRender innerRender;
	protected long outputPageCount;

	protected ITOCReader tocReader;
	protected boolean designLoaded = false;
	protected boolean variablesLoaded = false;
	
	//the flag of render page by page
	protected boolean PDFRenderPageByPage = true;
	
	// the html layout engine
	private IReportLayoutEngine layoutEngine = null;

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
		initRenderTask();
	}
	
	/**
	 * @param engine
	 *            the report engine
	 * @param taskType
	 *            the task type
	 * @param runnable
	 *            the report runnable object
	 * @param reportDoc
	 *            the report document instance
	 */
	public RenderTask( ReportEngine engine, int taskType, IReportRunnable runnable,
			IReportDocument reportDoc )
	{
		super( engine, taskType );
		this.reportDocument = reportDoc;
		this.reportRunnable = runnable;
		initRenderTask();
	}
	
	protected void initRenderTask()
	{
		executionContext.setFactoryMode( false );
		executionContext.setPresentationMode( true );

		executionContext.setReportDocument( reportDocument );

		assert ( reportDocument instanceof IInternalReportDocument );
		IInternalReportDocument internalReportDoc = (IInternalReportDocument) reportDocument;
		if ( reportDocument != null && reportRunnable == null )
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

	}
	
	/**
	 * Loads parameters and global variables from report document. Since the
	 * application context is not available and application class loader can't
	 * be created when render task is initialized, loading parameters and global
	 * variables from document must be deferred until application context is
	 * available.
	 */
	protected void loadDocument()
	{
		if ( !variablesLoaded )
		{
			IInternalReportDocument documentReader = (IInternalReportDocument) reportDocument;
			try
			{
				// load the information from the report document
				ClassLoader classLoader = executionContext
						.getApplicationClassLoader( );
				setParameters( documentReader.loadParameters( classLoader ) );
				usingParameterValues( );
				executionContext.registerGlobalBeans( documentReader
						.loadVariables( classLoader ) );
				tocReader = documentReader.getTOCReader( classLoader );
			}
			catch ( EngineException e )
			{
				log.log( Level.SEVERE, e.getLocalizedMessage( ), e );
			}
			variablesLoaded = true;
		}
	}

	protected void loadReportVariable( ) throws IOException
	{
		PageHintReader hintsReader = new PageHintReader( reportDocument );
		try
		{
			// load the report variables
			Collection<PageVariable> vars = hintsReader.getPageVariables( );
			if ( vars != null )
			{
				executionContext.addPageVariables( vars );
			}
		}
		finally
		{
			hintsReader.close( );
		}
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
		if ( tocReader != null )
		{
			try
			{
				tocReader.close( );
			}
			catch ( IOException ignored )
			{
			}
		}
		dataSource = null;
		innerRender = null;
		reportDocument = null;
		reportRunnable = null;
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
		if ( progressMonitor != null )
		{
			progressMonitor.onProgress( IProgressMonitor.START_TASK,
					TASK_RENDER );
		}
		try
		{
			switchToOsgiClassLoader( );
			changeStatusToRunning( );
			if ( renderOptions == null )
			{
				throw new EngineException( MessageConstants.RENDER_OPTION_ERROR ); //$NON-NLS-1$
			}
			loadDocument();
			loadReportVariable( );
			IReportRunnable runnable = executionContext.getRunnable( );
			if ( runnable == null )
			{
				throw new EngineException(
						MessageConstants.REPORT_DESIGN_NOT_FOUND_ERROR,
						new Object[]{reportDocument.getName( )} );
			}

			if ( !designLoaded )
			{
				loadScripts( );
				// load report design
				loadDesign( );
				// synchronize the design ir's version with the document
				String version = reportDocument.getVersion( );
				Report report = executionContext.getReport( );
				report.updateVersion( version );

				designLoaded = true;
			}

			updateRtLFlag( );
			
			ReportDesignHandle design = executionContext.getReportDesign( );
			if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
					.equals( design.getLayoutPreference( ) ) )
			{
				executionContext.setFixedLayout( true );
			}

			if ( innerRender == null )
			{
				innerRender = new PageRangeRender( new long[]{
						1, getTotalPage( )
				} );
			}

			innerRender.render( );
		}
		catch ( Throwable t )
		{
			handleFatalExceptions( t );
		}
		finally
		{
			changeStatusToStopped( );
			switchClassLoaderBack( );
			if ( progressMonitor != null )
			{
				progressMonitor.onProgress( IProgressMonitor.END_TASK,
						TASK_RENDER );
			}
		}
	}

	public long getPageCount( ) throws EngineException
	{
		if ( runningStatus != STATUS_SUCCEEDED )
		{
			throw new EngineException(
					MessageConstants.RENDERTASK_NOT_FINISHED_ERROR );
		}
		return outputPageCount;
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
					Long.valueOf( pageNumber ) );
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
		if ( bookmark != null )
		{
			bookmark = bookmark.trim( );
		}
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
	
	public void cancel( )
	{
		super.cancel( );
		if ( layoutEngine != null )
		{
			layoutEngine.cancel( );
		}
	}

	private interface InnerRender
	{

		void render( ) throws Exception;
	}

	/**
	 * Renders a range of pages.
	 */
	protected class PageRangeRender implements InnerRender
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

		/**
		 * @return true if BIRT render the report page by page. false if BIRT
		 *         render the report as a whole page.
		 */
		protected boolean needPagedExecutor(List<long[]> pageSequences )
		{
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				/* if fixed-layout, need render page by page, or pagination may different with html in the following case:
				 * 1. element is set visibility to false in pdf format
				 * 2. element is set display to none
				 */
				Object repaginateForPDF = renderOptions
						.getOption( IPDFRenderOption.REPAGINATE_FOR_PDF );
				if ( repaginateForPDF != null
						&& repaginateForPDF instanceof Boolean )
				{
					if ( ( (Boolean) repaginateForPDF ).booleanValue( ) )
					{
						RenderTask.this.PDFRenderPageByPage = false;
					}
				}
				if ( RenderTask.this.PDFRenderPageByPage
						&& executionContext.isFixedLayout( ) )
				{
					return true;
				}
				
				// the output pages is sequential or there is no page sequence,
				// in this case, we can output the report content as a whole and
				// the HTML layout engine may re-paginate the content into
				// pages.
				if ( pageSequences == null )
				{
					return false;
				}
				if ( pageSequences != null && pageSequences.size( ) == 1 )
				{
					long[] pages = pageSequences.get( 0 );
					if ( pages[0] == 1
							&& pages[1] == reportDocument.getPageCount( ) )
					{
						return false;
					}
				}
				// the page sequence is defined by several segment, we can't
				// display the report as a whole as in this case the HTML layout
				// engine can't regenerate the pagination.
				return true;
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
			startRender( );
			IContentEmitter emitter = createContentEmitter( );
			supportHtmlPagination( );

			//prepare the layout engine
			synchronized ( this )
			{
				if ( !executionContext.isCanceled( ) )
				{
					layoutEngine = createReportLayoutEngine( pagination,
							renderOptions );
				}
			}
			
			if ( null == layoutEngine )
			{
				return;
			}
			
			layoutEngine.setLocale( executionContext.getLocale( ) );
			LayoutPageHandler layoutPageHandler = new LayoutPageHandler(
					( (HTMLReportLayoutEngine) layoutEngine ).getContext( ) );
			
			ReportDesignHandle design = executionContext.getReportDesign( );
			if ( DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT
					.equals( design.getLayoutPreference( ) ) )
			{
				( (HTMLReportLayoutEngine) layoutEngine ).getContext( ).setFixedLayout( true );
			}
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				LayoutEngine pdfEmitter = new LayoutEngine(
						( (HTMLReportLayoutEngine) layoutEngine ).getContext( ),
						emitter,
						renderOptions, executionContext,
						getDocumentTotalPage( ) );
				pdfEmitter.setPageHandler( layoutPageHandler );
				
				emitter = pdfEmitter;
			}
			else
			{
				layoutEngine.setPageHandler( layoutPageHandler );
			}
			
			//initialize the emitter,  the emitter may change the render options here.
			initializeContentEmitter( emitter );

            // setup the page sequences
            List<long[]> physicalPageSequences = getPhysicalPageSequence( pageSequences );
            long filteredTotalPage = getTotalPage( );
            long totalPage = reportDocument.getPageCount( );
            if ( filteredTotalPage != totalPage )
            {
                executionContext.setFilteredTotalPage( filteredTotalPage );
            }
            PageRangeIterator iter = new PageRangeIterator(
                    physicalPageSequences );

            boolean paged = needPagedExecutor( physicalPageSequences );

            //prepare the executor
            ReportPageExecutor pagesExecutor = new ReportPageExecutor(
                    executionContext, physicalPageSequences, paged );

            IReportExecutor executor = createRenderExtensionExecutor( pagesExecutor );
            executor = new SuppressDuplciateReportExecutor( executor );
            executor = new LocalizedReportExecutor( executionContext, executor );
            executionContext.setExecutor( executor );
            
            IReportContent report = executor.execute( );
			emitter.start( report );
			layoutEngine.setTotalPageCount( getTotalPage( ) );

			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
				if ( !paged )
				{
					long pageNumber = iter.next( );
					if ( pageNumber != 1 )
					{
						layoutEngine.setLayoutPageHint( getPageHint(
								pagesExecutor, pageNumber ) );
					}
					setFilteredPageNumber( filteredTotalPage,
							totalPage,
							pageNumber );
					layoutEngine.layout( executor, report, emitter, true );
				}
				else
				{
					while ( iter.hasNext( ) )
					{
						long pageNumber = iter.next( );
						IPageHint pageHint = getPageHint( pagesExecutor,
								pageNumber );
						layoutEngine.setLayoutPageHint( pageHint );
						// here the pageExecutor will returns a report.root.
						IReportItemExecutor pageExecutor = executor
								.getNextChild( );
						if ( pageExecutor != null )
						{
							setFilteredPageNumber( filteredTotalPage,
									totalPage,
									pageNumber );
							IReportExecutor pExecutor = new ReportExecutorWrapper(
									pageExecutor, executor );
							layoutEngine.layout( pExecutor, report, emitter,
									false );
						}
					}
				}
			}
			else if ( ExtensionManager.PAGE_BREAK_PAGINATION
					.equals( pagination ) )
			{
				if ( !paged )
				{
					long pageNumber = iter.next( );
					if ( pageNumber != 1 )
					{
						layoutEngine.setLayoutPageHint( getPageHint(
								pagesExecutor, pageNumber ) );
					}
					setFilteredPageNumber( filteredTotalPage,
							totalPage,
							pageNumber );
					layoutEngine.layout( executor, report, emitter, false );
				}
				else
				{
					while ( iter.hasNext( ) )
					{
						long pageNumber = iter.next( );
						IPageHint pageHint = getPageHint( pagesExecutor,
								pageNumber );
						layoutEngine.setLayoutPageHint( pageHint );
						// here the pageExecutor will returns a report.root.
						IReportItemExecutor pageExecutor = executor
								.getNextChild( );
						if ( pageExecutor != null )
						{
							setFilteredPageNumber( filteredTotalPage,
									totalPage,
									pageNumber );
							IReportExecutor pExecutor = new ReportExecutorWrapper(
									pageExecutor, executor );
							layoutEngine.layout( pExecutor, report, emitter,
									false );
						}
					}
				}
			}
			else if ( ExtensionManager.NO_PAGINATION.equals( pagination ) )
			{
				layoutEngine.layout( executor, report, emitter, false );
			}
			outputPageCount = layoutEngine.getPageCount( );

			layoutEngine.close( );
			layoutEngine = null;
			emitter.end( report );
			closeRender( );
			executor.close( );

		}

		private void setFilteredPageNumber( long filteredTotalPage,
				long totalPage, long pageNumber ) throws EngineException
		{
			if ( filteredTotalPage != totalPage )
			{
				long filteredPageNumber = getLogicalPageNumber( pageNumber );
				executionContext.setFilteredPageNumber( filteredPageNumber );
			}
		}
	}

	protected class ReportletRender implements InnerRender
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
			startRender( );
			IContentEmitter emitter = createContentEmitter( );

			synchronized ( this )
			{
				if ( !executionContext.isCanceled( ) )
				{
					layoutEngine = createReportLayoutEngine( pagination,
							renderOptions );
				}
			}
			
			if ( null == layoutEngine )
			{
				return;
			}
			layoutEngine.setPageHandler( new LayoutPageHandler(
					( (HTMLReportLayoutEngine) layoutEngine ).getContext( ) ) );

			layoutEngine.setLocale( executionContext.getLocale( ) );

			// paper size output need re-paginate
			if ( ExtensionManager.PAPER_SIZE_PAGINATION.equals( pagination ) )
			{
                emitter = new LayoutEngine(
                        ( (HTMLReportLayoutEngine) layoutEngine ).getContext( ),
                        emitter, renderOptions, executionContext,
                        getDocumentTotalPage( ) );
			}
			
            initializeContentEmitter( emitter );
			
            IReportExecutor executor = new ReportletExecutor( executionContext,
                    offset );
            executor = createRenderExtensionExecutor( executor );
            executor = new SuppressDuplciateReportExecutor( executor );
            executor = new LocalizedReportExecutor( executionContext, executor );
            executionContext.setExecutor( executor );

			IReportContent report = executor.execute( );
			emitter.start( report );
			// output the reportlet without pagination
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

		public void close( ) throws BirtException
		{
			executor.close( );

		}

		public IReportItemExecutor createPageExecutor( long pageNumber,
				MasterPageDesign pageDesign ) throws BirtException
		{
			return reportExecutor.createPageExecutor( pageNumber, pageDesign );
		}

		public IReportContent execute( ) throws BirtException
		{
			// FIXME: create the report content only once.
			return reportExecutor.execute( );
		}

		public IReportItemExecutor getNextChild( ) throws BirtException
		{
			return executor.getNextChild( );
		}

		public boolean hasNextChild( ) throws BirtException
		{
			return executor.hasNextChild( );
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
		loadDocument( );
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
		ReportDesignHandle design = executionContext.getReportDesign( );

		if ( document instanceof IInternalReportDocument )
		{
			ITreeNode tocTree = null;
			if ( tocReader != null )
			{
				try
				{
					tocTree = tocReader.readTree( );
				}
				catch ( IOException e )
				{
					throw new EngineException( MessageConstants.FAILED_TO_LOAD_TOC_TREE_EXCEPTION, e );
				}
			}
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

	public ITreeNode getRawTOCTree( )
	{
		loadDocument( );
		ITreeNode tocTree = null;
		if ( tocReader != null )
		{
			try
			{
				tocTree = tocReader.readTree( );
			}
			catch ( IOException e )
			{
				log.log( Level.SEVERE, e.getLocalizedMessage( ), e );
			}
		}
		return tocTree;
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
	
	public HashMap getParameterValues( )
	{
		loadDocument();
		return (HashMap)executionContext.getParameterValues( );
	}

	public Object getParameterDisplayText( String name )
	{
		loadDocument();
		return executionContext.getParameterDisplayText(name);
	}

	private long getDocumentTotalPage( )
	{
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
	
	private class LayoutPageHandler implements ILayoutPageHandler
	{
		private HTMLLayoutContext context;
		
		public LayoutPageHandler(HTMLLayoutContext context )
		{
			this.context = context;
		}
		
		public void onPage( long pageNumber, Object context )
		{
			if ( pageHandler != null )
			{
				long totalPage = reportDocument.getPageCount( );
				boolean finished = false;
				if ( context instanceof HTMLLayoutContext )
				{
					HTMLLayoutContext layoutContext = (HTMLLayoutContext) context;
					finished = layoutContext.isFinished( );
				}
				else if ( context instanceof LayoutEngineContext )
				{
					LayoutEngineContext layoutEngineContext = (LayoutEngineContext) context;
					finished = this.context.isFinished( ) && layoutEngineContext.isFinished( );
				}
				IReportDocumentInfo reportDocumentInfo = new ReportDocumentInfo(
						executionContext, totalPage, finished );
				pageHandler.onPage( (int) pageNumber, false, reportDocumentInfo );
			}
			executionContext.getProgressMonitor( ).onProgress(
					IProgressMonitor.END_PAGE, (int) pageNumber );
		}
	}
}
