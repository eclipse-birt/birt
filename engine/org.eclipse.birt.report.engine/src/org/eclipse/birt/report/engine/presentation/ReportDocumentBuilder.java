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
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.api.IPageHandler;
import org.eclipse.birt.report.engine.api.IReportDocumentInfo;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentWriter;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.internal.document.IPageHintWriter;
import org.eclipse.birt.report.engine.internal.document.IReportContentWriter;
import org.eclipse.birt.report.engine.internal.document.v2.PageHintWriterV2;
import org.eclipse.birt.report.engine.internal.document.v2.ReportContentWriterV2;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;

public class ReportDocumentBuilder
{

	protected static Logger logger = Logger
			.getLogger( ReportDocumentBuilder.class.getName( ) );

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
	 * bookmark index, contains instanceId, offset pair.
	 */
	protected HashMap reportlets = new HashMap( );

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
	protected IContentEmitter pageEmitter;
	/**
	 * use the write the page hint stream.
	 */
	protected IPageHintWriter pageHintWriter;

	protected IPageHandler pageHandler;

	public ReportDocumentBuilder( ReportDocumentWriter document )
	{
		this.document = document;
		contentEmitter = new ContentEmitter( );
		pageEmitter = new PageEmitter( );
		pageHandler = new PageHandler( );
	}

	public IContentEmitter getContentEmitter( )
	{
		return contentEmitter;
	}

	public IContentEmitter getPageEmitter( )
	{
		return pageEmitter;
	}

	public IPageHandler getPageHandler( )
	{
		return pageHandler;
	}

	/**
	 * emitter used to save the report content into the content stream
	 * 
	 * @version $Revision:$ $Date:$
	 */
	class ContentEmitter extends ContentEmitterAdapter
	{

		IReportContentWriter writer;

		protected void open( )
		{
			try
			{
				writer = new ReportContentWriterV2( document );
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
			document.saveTOC( report.getTOC( ) );
			// save the instance id to the report document
			document.saveReportlets( reportlets );
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
							if ( reportlets.get( strIID ) == null )
							{
								reportlets.put( strIID, new Long( offset ) );
							}
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
	 * @version $Revision:$ $Date:$
	 */
	class PageEmitter extends ContentEmitterAdapter
	{

		IReportContentWriter writer;

		protected void open( )
		{
			try
			{
				writer = new ReportContentWriterV2( document );
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

		public void endPage( IPageContent page )
		{
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

	class PageHandler implements IPageHandler
	{

		IPageHintWriter writer;

		PageHandler( )
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
					logger.log( Level.SEVERE, "Can't save the page number", ex );
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
					logger.log( Level.SEVERE, "Can't save the page hint", ex );
					close( );
				}
			}
		}

		public void onPage( int pageNumber, boolean checkpoint,
				IReportDocumentInfo docInfo )
		{
			if ( docInfo instanceof ReportDocumentInfo )
			{
				ReportDocumentInfo info = (ReportDocumentInfo) docInfo;
				long startOffset = info.getStart( );
				long endOffset = info.getEnd( );
				boolean reportFinished = info.isFinsihed( );
				if ( reportFinished )
				{
					writeTotalPage( pageNumber );
					close( );
				}
				else
				{
					PageHint hint = new PageHint( pageNumber, pageOffset,
							startOffset, endOffset );
					writePageHint( hint );
					if ( checkpoint )
					{
						writeTotalPage( pageNumber );
					}
				}
			}
			if ( checkpoint )
			{
				document.saveCoreStreams( );
			}
		}
	}
}
