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

package org.eclipse.birt.report.engine.presentation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.CompositeContentEmitter;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.executor.IReportExecutor;
import org.eclipse.birt.report.engine.executor.OnPageBreakLayoutPageHandle;
import org.eclipse.birt.report.engine.internal.document.DocumentExtension;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.internal.document.IReportContentWriter;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintWriterV2;
import org.eclipse.birt.report.engine.internal.document.v3.ReportContentWriterV3;
import org.eclipse.birt.report.engine.internal.presentation.ReportDocumentInfo;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.layout.CompositeLayoutPageHandler;
import org.eclipse.birt.report.engine.layout.ILayoutPageHandler;
import org.eclipse.birt.report.engine.layout.IReportLayoutEngine;
import org.eclipse.birt.report.engine.layout.LayoutEngineFactory;
import org.eclipse.birt.report.engine.layout.html.HTMLLayoutContext;
import org.eclipse.birt.report.engine.toc.TOCTree;

public class ReportDocumentBuilder
{

	protected static Logger logger = Logger
			.getLogger( ReportDocumentBuilder.class.getName( ) );

	/**
	 * execution context used to execute the report
	 */
	protected ExecutionContext executionContext;
	/**
	 * current page number
	 */
	protected long pageNumber;
	/**
	 * the offset of the page content
	 */
	protected long pageOffset;

	/**
	 * bookmark index, contains bookmark, page pair.
	 */
	protected HashMap bookmarks = new HashMap( );

	/**
	 * Reportlets index by instanceID, contains instanceId, offset pair.
	 */
	protected HashMap reportletsIndexById = new HashMap( );

	/**
	 * Reportlets index by bookmark, contains bookmark, offset pair.
	 */
	protected HashMap reportletsIndexByBookmark = new HashMap( );

	/**
	 * report document used to save the informations.
	 */
	protected ReportDocumentWriter document;
	/**
	 * used to write the content stream
	 */
	protected IContentEmitter contentEmitter;
	/**
	 * used to write the page content stream.
	 */
	protected CompositeContentEmitter outputEmitters;
	/**
	 * use the write the page hint stream.
	 */
	protected IPageHintWriter pageHintWriter;

	/**
	 * page handler used to recevie the document page events.
	 */
	protected IPageHandler pageHandler;

	protected IReportLayoutEngine engine;

	/**
	 * handle used to recive the layout page events
	 */
	protected CompositeLayoutPageHandler layoutPageHandler;

	public ReportDocumentBuilder( ExecutionContext context,
			ReportDocumentWriter document )
	{
		this.executionContext = context;
		this.document = document;
		OnPageBreakLayoutPageHandle onPageBreakHandler = new OnPageBreakLayoutPageHandle(
				context );
		outputEmitters = new CompositeContentEmitter( );
		outputEmitters.addEmitter( new PageEmitter( ) );
		outputEmitters.addEmitter( onPageBreakHandler.getEmitter( ) );

		layoutPageHandler = new CompositeLayoutPageHandler( );
		layoutPageHandler.addPageHandler( new LayoutPageHandler( ) );
		layoutPageHandler.addPageHandler( onPageBreakHandler );

		contentEmitter = new ContentEmitter( );
	}

	public IContentEmitter getContentEmitter( )
	{
		return contentEmitter;
	}

	public void build( )
	{
		IReportExecutor executor = executionContext.getExecutor( );
		engine = LayoutEngineFactory.createLayoutEngine( "html" );
		engine.setPageHandler( layoutPageHandler );
		engine.layout( executor, outputEmitters, true );
		engine = null;
	}

	public void cancel( )
	{
		if ( engine != null )
		{
			engine.cancel( );
		}
	}

	public void setPageHandler( IPageHandler handler )
	{
		pageHandler = handler;
	}

	/**
	 * emitter used to save the report content into the content stream
	 * 
	 */
	class ContentEmitter extends ContentEmitterAdapter
	{

		IReportContentWriter writer;

		protected void open( )
		{
			try
			{
				writer = new ReportContentWriterV3( document );
				writer.open( ReportDocumentConstants.CONTENT_STREAM );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "failed to open the content writers",
						ex );
				close( );
			}
		}

		protected void close( )
		{
			if ( writer != null )
			{
				writer.close( );
			}
			writer = null;
		}

		public void start( IReportContent report )
		{
			open( );
		}

		public void end( IReportContent report )
		{
			close( );
			// save the toc stream
			ITOCTree tocTree = report.getTOCTree( null, null );
			if ( tocTree instanceof TOCTree )
			{
				document.saveTOC( (TOCTree) report.getTOCTree( null, null ) );
			}
			// save the instance id to the report document
			document.saveReportletsIdIndex( reportletsIndexById );
			document.saveReprotletsBookmarkIndex( reportletsIndexByBookmark );
		}

		public void startContent( IContent content )
		{
			if ( writer != null )
			{
				try
				{
					// save the contents into the content stream.
					long offset = writer.writeContent( content );

					// save the reportlet index
					Object generateBy = content.getGenerateBy( );
					if ( generateBy instanceof TableItemDesign
							|| generateBy instanceof ListItemDesign
							|| generateBy instanceof ExtendedItemDesign )
					{
						InstanceID iid = content.getInstanceID( );
						if ( iid != null )
						{
							String strIID = iid.toString( );
							if ( reportletsIndexById.get( strIID ) == null )
							{
								reportletsIndexById.put( strIID, new Long(
										offset ) );
							}
						}
					}

					String bookmark = content.getBookmark( );
					if ( bookmark != null )
					{
						if ( reportletsIndexByBookmark.get( bookmark ) == null )
						{
							reportletsIndexByBookmark.put( bookmark, new Long(
									offset ) );
						}
					}
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, "Write content error" );
					close( );
				}
			}
		}
	}

	/**
	 * emitter used to save the master page.
	 * 
	 */
	class PageEmitter extends ContentEmitterAdapter
	{

		IReportContentWriter writer;

		protected void open( )
		{
			try
			{
				writer = new ReportContentWriterV3( document );
				writer.open( ReportDocumentConstants.PAGE_STREAM );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "failed to open the content writers",
						ex );
				close( );
			}
		}

		protected void close( )
		{
			if ( writer != null )
			{
				writer.close( );
			}
			writer = null;
		}

		public void start( IReportContent report )
		{
			open( );
		}

		public void end( IReportContent report )
		{
			close( );
			// save the bookmark stream
			document.saveBookmarks( bookmarks );
		}

		public void startPage( IPageContent page )
		{
			// write the page content into the disk
			pageNumber = page.getPageNumber( );

			// write the page contents
			try
			{
				pageOffset = writer.writeFullContent( page );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "write page content failed", ex );
				close( );
			}
		}

		public void startContent( IContent content )
		{
			// save the bookmark index
			if ( content.getBookmark( ) != null )
			{
				if ( !bookmarks.containsKey( content.getBookmark( ) ) )
				{
					bookmarks.put( content.getBookmark( ),
							new Long( pageNumber ) );
				}
			}
		}
	}

	class LayoutPageHandler implements ILayoutPageHandler
	{

		IPageHintWriter writer;

		LayoutPageHandler( )
		{
		}

		boolean ensureOpen( )
		{
			if ( writer != null )
			{
				return true;
			}
			writer = new PageHintWriterV2( document );
			try
			{
				writer.open( );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "Can't open the hint stream", ex );
				close( );
				return false;
			}
			return true;
		}

		protected void close( )
		{
			if ( writer != null )
			{
				writer.close( );
			}
			writer = null;
		}

		void writeTotalPage( long pageNumber )
		{
			if ( ensureOpen( ) )
			{
				try
				{
					writer.writeTotalPage( pageNumber );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, "Failed to save the page number",
							ex );
					close( );
				}
			}
		}

		void writePageHint( PageHint pageHint )
		{
			if ( ensureOpen( ) )
			{
				try
				{
					writer.writePageHint( pageHint );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, "Failed to save the page hint",
							ex );
					close( );
				}
			}
		}

		protected long getOffset( IContent content )
		{
			DocumentExtension docExt = (DocumentExtension) content
					.getExtension( IContent.DOCUMENT_EXTENSION );
			if ( docExt != null )
			{
				return docExt.getIndex( );
			}
			return -1;
		}

		public void onPage( long pageNumber, Object context )
		{
			if ( context instanceof HTMLLayoutContext )
			{
				HTMLLayoutContext htmlContext = (HTMLLayoutContext) context;
				document.setPageCount( pageNumber );

				boolean checkpoint = false;
				// check points for page 1, 10, 50, 100, 200 ...
				// the end of report should also be check point.
				if ( pageNumber == 1 || pageNumber == 10 || pageNumber == 50
						|| pageNumber % 100 == 0 )
				{
					checkpoint = true;
				}

				boolean reportFinished = htmlContext.isFinished( );
				if ( reportFinished )
				{
					checkpoint = true;
				}
				else
				{
					ArrayList pageHint = htmlContext.getPageHint( );
					PageHint hint = new PageHint( pageNumber, pageOffset );
					for ( int i = 0; i < pageHint.size( ); i++ )
					{
						IContent[] range = (IContent[]) pageHint.get( i );
						long startOffset = getOffset( range[0] );
						long endOffset = getOffset( range[1] );
						hint.addSection( startOffset, endOffset );
					}
					writePageHint( hint );
				}

				if ( checkpoint )
				{
					try
					{
						IDocArchiveWriter archive = document.getArchive( );
						Object lock = archive
								.lock( ReportDocumentConstants.CORE_STREAM );
						try
						{
							writeTotalPage( pageNumber );
							document.saveCoreStreams( );
							archive.flush( );
						}
						finally
						{
							archive.unlock( lock );
						}
					}
					catch ( Exception e )
					{
						logger.log( Level.WARNING, " check point failed ", e );
					}

					if ( reportFinished )
					{
						close( );
					}
				}
				// notify the page handler
				if ( pageHandler != null )
				{
					// if user has canceled the task, we should not do onPage.
					if ( !htmlContext.getCancelFlag( ) )
					{
						IReportDocumentInfo docInfo = new ReportDocumentInfo(
							executionContext, pageNumber, reportFinished );
						pageHandler.onPage( (int) pageNumber, checkpoint, docInfo );
					}
				}
			}
		}
	}
}