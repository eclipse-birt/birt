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

package org.eclipse.birt.report.engine.internal.document.v2;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.report.engine.api.DataID;
import org.eclipse.birt.report.engine.api.DataSetID;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IPageContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.ITableBandContent;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.content.impl.DataContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.data.IDataEngine;
import org.eclipse.birt.report.engine.data.IResultSet;
import org.eclipse.birt.report.engine.emitter.ContentDOMVisitor;
import org.eclipse.birt.report.engine.emitter.DOMBuilderEmitter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.internal.document.IReportContentLoader;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class ReportContentLoaderV2 implements IReportContentLoader
{

	protected static Logger logger = Logger
			.getLogger( IReportContentLoader.class.getName( ) );

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected IContentEmitter emitter;
	protected ReportContentReaderV2 reader;
	protected ReportContentReaderV2 pageReader;
	protected PageHintReaderV2 hintReader;
	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;
	/**
	 * the offset of current read object. The object has been read out, setted
	 * in loadContent();
	 */
	protected long currentOffset;

	protected Stack resultSets = new Stack( );

	public ReportContentLoaderV2( ExecutionContext context )
	{
		this.context = context;
		dataEngine = context.getDataEngine( );
		ReportDesignHandle reportDesign = context.getDesign( );
		report = new ReportParser( context ).parse( reportDesign );
		context.setReport( report );

		reportContent = (ReportContent) ContentFactory
				.createReportContent( report );
		context.setReportContent( reportContent );

		reportDoc = context.getReportDocument( );
		dataEngine.prepare( report, context.getAppContext( ) );
	}

	protected void openReaders( )
	{
		try
		{
			reader = new ReportContentReaderV2( reportContent, reportDoc );
			reader.open( ReportDocumentConstants.CONTENT_STREAM );
			pageReader = new ReportContentReaderV2( reportContent, reportDoc );
			pageReader.open( ReportDocumentConstants.PAGE_STREAM );
			hintReader = new PageHintReaderV2( reportDoc );
			hintReader.open( );
		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "Failed to open the content reader", ex );
			closeReaders( );
		}
	}

	protected void closeReaders( )
	{
		if ( reader != null )
		{
			reader.close( );
			reader = null;
		}
		if ( pageReader != null )
		{
			pageReader.close( );
			pageReader = null;
		}
		if ( hintReader != null )
		{
			hintReader.close( );
			hintReader = null;
		}
	}

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPage( long pageNumber, boolean bodyOnly,
			IContentEmitter emitter )
	{
		emitter.start( reportContent );
		this.emitter = emitter;
		try
		{
			openReaders( );
			excutePage( pageNumber, bodyOnly );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page", ex );
			ex.printStackTrace( );
		}
		finally
		{
			emitter.end( reportContent );
			closeReaders( );
		}
	}

	public void loadReportlet( long offset, IContentEmitter emitter )
	{
		emitter.start( reportContent );
		this.emitter = emitter;
		try
		{
			openReaders( );
			excuteReportlet( offset );
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page", ex );
			ex.printStackTrace( );
		}
		finally
		{
			emitter.end( reportContent );
			closeReaders( );
		}

	}

	/**
	 * stack used to control the output contents.
	 */
	protected Stack contents = new Stack( );

	/**
	 * load the page content and output to the emitter.
	 * 
	 * @param pageNumber
	 *            page number
	 * @param bodyOnly
	 *            only output the page body.
	 */
	private void excutePage( long pageNumber, boolean bodyOnly )
	{
		IPageHint pageHint = hintReader.getPageHint( pageNumber );
		IPageContent pageContent = null;
		if ( !bodyOnly )
		{
			long pageOffset = pageHint.getOffset( );
			try
			{
				pageContent = loadPageContent( pageOffset );
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "Can't load the page content", ex );
			}
			if ( pageContent == null )
			{
				return;
			}

			Object generateBy = pageContent.getGenerateBy( );
			if ( generateBy instanceof SimpleMasterPageDesign )
			{
				SimpleMasterPageDesign pageDesign = (SimpleMasterPageDesign) generateBy;
				if ( !pageDesign.isShowHeaderOnFirst( ) )
				{
					if ( pageNumber == 1 )
					{
						pageContent.getHeader( ).clear( );
					}
				}
				if ( !pageDesign.isShowFooterOnLast( ) )
				{
					if ( pageNumber == reportDoc.getPageCount( ) )
					{
						pageContent.getFooter( ).clear( );
					}
				}
			}
			emitter.startPage( pageContent );
		}

		for ( int i = 0; i < pageHint.getSectionCount( ); i++ )
		{
			long start = pageHint.getSectionStart( i );
			long end = pageHint.getSectionEnd( i );
			if ( start != -1 && end != -1 )
			{
				try
				{
					outputPageRegion( start, end );
				}
				catch ( IOException ex )
				{
					logger.log( Level.SEVERE, "Can't load the page content" );
				}
			}
		}

		while ( !contents.isEmpty( ) )
		{
			IContent content = (IContent) contents.pop( );
			endContent( content, emitter );
		}

		if ( !bodyOnly )
		{
			emitter.endPage( pageContent );
		}
	}

	/**
	 * load the page content and output to the emitter.
	 * 
	 * @param pageNumber
	 *            page number
	 * @param bodyOnly
	 *            only output the page body.
	 */
	private void excuteReportlet( long offset )
	{
		try
		{
			reader.setOffset( offset );

			// FIXME We needn't output the parent of the reportlet.

			// start all the parent of the content
			IContent root = reader.readContent( );

			IContent parent = (IContent) root.getParent( );
			if ( parent != null )
			{
				outputParent( parent );
			}
			// output this content
			initializeContent( root );
			startContent( root, emitter );
			contents.push( root );

			parent = root;
			IContent next = reader.readContent( );
			while ( next != null )
			{
				if ( next.getParent( ) == parent )
				{
					initializeContent( next );
					startContent( next, emitter );
					contents.push( next );

					parent = next;
					next = reader.readContent( );
				}
				else
				{
					if ( parent == root )
					{
						break;
					}
					endContent( parent, emitter );
					contents.pop( );
					parent = (IContent) parent.getParent( );
				}
			}

		}
		catch ( IOException ex )
		{
			logger.log( Level.SEVERE, "Can't load the page content", ex );
		}

		while ( !contents.isEmpty( ) )
		{
			IContent content = (IContent) contents.pop( );
			endContent( content, emitter );
		}
	}

	/**
	 * output the contents from start to end.
	 * 
	 * @param start
	 * @param end
	 * @throws IOException
	 */
	private void outputPageRegion( long start, long end ) throws IOException
	{
		long offset = start;
		reader.setOffset( offset );
		while ( offset <= end )
		{
			IContent content = reader.readContent( );
			// first end all the content which is not
			// the ancestor of this contentutil the stack is empty
			while ( !contents.isEmpty( ) )
			{
				IContent parent = (IContent) contents.peek( );
				if ( parent != content.getParent( ) )
				{
					endContent( parent, emitter );
					contents.pop( );
					continue;
				}
				break;
			}
			// output all the ancestor of this content
			if ( contents.isEmpty( ) )
			{
				long curOffset = reader.getOffset( );
				IContent parent = (IContent) content.getParent( );
				if ( parent != null )
				{
					outputParent( parent );
				}
				reader.setOffset( curOffset );
			}
			// now the contents contains the parent of this content.
			initializeContent( content );
			startContent( content, emitter );
			contents.push( content );
			offset = reader.getOffset( );
		}
	}

	/**
	 * output the parents of the content.
	 * 
	 * @param content
	 */
	private void outputParent( IContent content ) throws IOException
	{
		IContent parent = (IContent) content.getParent( );
		if ( parent != null )
		{
			outputParent( parent );
		}
		initializeContent( content );
		startContent( content, emitter );
		contents.push( content );
		if ( content instanceof ITableContent )
		{
			ITableContent table = (ITableContent) content;
			if ( table.isHeaderRepeat( ) )
			{
				ITableBandContent header = table.getHeader( );
				if (header == null)
				{
					//try to load the header content
					reader.setOffset( table.getOffset( ) );
					//skip the table object
					reader.readContent( );
					//read the header object
					IContent headerContent = reader.readContent( );
					//read the contents in the header
					loadFullContent( headerContent, reader);
					//add the header into the table
					table.getChildren( ).add( headerContent );
					header = table.getHeader( );
				}
				//output the table header
				if ( header != null )
				{
					new ContentDOMVisitor( ).emit( header, emitter );
				}
			}
		}
	}

	/**
	 * load the page from the content stream and output it to the emitter
	 * 
	 * @param pageNumber
	 * @param emitter
	 */
	public void loadPageRange( List pageList, boolean bodyOnly,
			IContentEmitter emitter )
	{
		emitter.start( reportContent );
		this.emitter = emitter;
		try
		{
			openReaders( );
			for ( int m = 0; m < pageList.size( ); m++ )
			{
				long[] ps = (long[]) pageList.get( m );
				for ( long i = ps[0]; i <= ps[1]; i++ )
				{
					excutePage( i, bodyOnly );
				}
			}
		}
		catch ( Exception ex )
		{
			logger.log( Level.SEVERE, "Failed to load the page", ex ); //$NON-NLS-1$
			ex.printStackTrace( );
		}
		finally
		{
			emitter.end( reportContent );
			closeReaders( );
		}
	}

	/**
	 * load the page content from the page content stream.
	 * 
	 * @param offset
	 * @return
	 * @throws IOException
	 */
	protected IPageContent loadPageContent( long offset ) throws IOException
	{
		pageReader.setOffset( offset );
		IPageContent pageContent = (IPageContent) pageReader.readContent( );
		initializeContent( pageContent );
		if ( pageContent == null )
		{
			return null;
		}

		SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageContent
				.getGenerateBy( );

		if ( masterPage.getHeaderCount( ) > 0
				|| masterPage.getFooterCount( ) > 0 )
		{
			IContent nextContent = null;
			IContent content = pageReader.readContent( );
			for ( int i = 0; i < masterPage.getHeaderCount( ); i++ )
			{
				nextContent = loadFullContent( content, pageReader );
				pageContent.getHeader( ).add( content );
				content = nextContent;
			}
			for ( int i = 0; i < masterPage.getFooterCount( ); i++ )
			{
				nextContent = loadFullContent( content, pageReader );
				pageContent.getFooter( ).add( content );
				content = nextContent;
			}
		}
		return pageContent;
	}

	/**
	 * load all the children of the root from the reader and output them into
	 * emitter.
	 * 
	 * @param root
	 *            content to be loaded.
	 * @param reader
	 *            reader
	 * @param emitter
	 *            output emitter.
	 * @return the next content after the root. NULL if at the end of stream.
	 */
	protected IContent loadFullContent( IContent root,
			ReportContentReaderV2 reader )
	{
		IContentEmitter emitter = new DOMBuilderEmitter( root );
		IContent parent = root;

		initializeContent( root );
		openQuery(root);
		try
		{
			IContent next = reader.readContent( );
			while ( next != null )
			{
				if ( next.getParent( ) == parent )
				{
					initializeContent( next );
					startContent( next, emitter );
					parent = next;
					next = reader.readContent( );
				}
				else
				{
					if ( parent == root )
					{
						closeQuery(root);
						return next;
					}
					endContent( parent, emitter );
					parent = (IContent) parent.getParent( );
				}
			}
		}
		catch ( IOException ex )
		{
		}

		while ( parent != root )
		{
			endContent( parent, emitter );
			parent = (IContent) parent.getParent( );
		}
		closeQuery(root);

		return null;
	}

	protected void initializeContent( IContent content )
	{
		// set up the report content
		content.setReportContent( reportContent );
		// set up the design object
		InstanceID id = content.getInstanceID( );
		if ( id != null )
		{
			long designId = id.getComponentID( );
			if ( designId != -1 )
			{
				Object generateBy = findReportItem( designId );
				content.setGenerateBy( generateBy );
				if ( generateBy instanceof ReportItemDesign )
				{
					ReportItemDesign design = (ReportItemDesign) generateBy;
					content.setX( design.getX( ) );
					content.setY( design.getY( ) );
					content.setWidth( design.getWidth( ) );
					content.setHeight( design.getHeight( ) );
					content.setStyleClass( design.getStyleName( ) );
				}
				if ( generateBy instanceof TemplateDesign )
				{
					TemplateDesign design = (TemplateDesign) generateBy;
					if ( content instanceof ILabelContent )
					{
						ILabelContent labelContent = (ILabelContent) content;
						labelContent.setLabelKey( design.getPromptTextKey( ) );
						labelContent.setLabelText( design.getPromptText( ) );
					}
				}
			}
		}
	}

	/**
	 * find the report element by the design id. we need get the engine's IR
	 * from the design id, so we can't use the mode's getElementByID().
	 * 
	 * @param designId
	 *            design id
	 * @return design object (engine)
	 */
	protected ReportElementDesign findReportItem( long designId )
	{
		return report.getReportItemByID( designId );
	}

	protected void openQuery( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		// open the query associated with the current report item
		if ( generateBy instanceof ReportItemDesign )
		{
			ReportItemDesign design = (ReportItemDesign) generateBy;
			IBaseQueryDefinition query = design.getQuery( );
			if ( query != null )
			{
				InstanceID iid = content.getInstanceID( );
				if ( iid != null )
				{
					// To the current report item,
					// if the dataId exist and it's deteSet id is not null,
					// and we can find it has parent,
					// we'll try to skip to the current row of the parent
					// query.
					DataID dataId = iid.getDataID( );
					if ( dataId != null )
					{
						DataSetID dataSetId = dataId.getDataSetID( );
						if ( dataSetId != null )
						{
							DataSetID parentSetId = dataSetId.getParentID( );
							long parentRowId = dataSetId.getRowID( );
							if ( parentSetId != null && parentRowId != -1 )
							{
								// the parent exist.
								if ( !resultSets.isEmpty( ) )
								{
									IResultSet rset = (IResultSet) resultSets
											.peek( );
									if ( rset != null )
									{
										// the parent query's result set is
										// not null, skip to the right row
										// according row id.
										if ( parentRowId != rset
												.getCurrentPosition( ) )
										{
											rset.skipTo( parentRowId );
										}
									}
								}
							}
						}
					}
				}
				// execute query
				IResultSet rset = dataEngine.execute( query );
				resultSets.push( rset );
			}
		}
		// locate the row position to the current position
		InstanceID iid = content.getInstanceID( );
		if ( iid != null )
		{
			DataID dataId = iid.getDataID( );
			while ( dataId == null && iid.getParentID( ) != null )
			{
				iid = iid.getParentID( );
				dataId = iid.getDataID( );
			}
			if ( dataId != null )
			{
				if ( !resultSets.isEmpty( ) )
				{
					IResultSet rset = (IResultSet) resultSets.peek( );
					if ( rset != null )
					{
						long rowId = dataId.getRowID( );
						if ( rowId != -1 && rowId != rset.getCurrentPosition( ) )
						{
							rset.skipTo( rowId );
						}
					}
				}
			}
		}
		if ( generateBy instanceof DataItemDesign
				&& content instanceof DataContent )
		{
			DataItemDesign design = (DataItemDesign) generateBy;
			DataContent data = (DataContent) content;
			if ( design.getMap( ) == null )
			{
				String valueExpr = design.getValue( );
				if ( valueExpr != null )
				{
					Object value = context.evaluate( valueExpr );
					data.setValue( value );
				}
			}
		}
	}

	protected void checkDataSet( DataID dataId, IResultSet rset )
	{
		DataSetID dsetId = rset.getID( );
		DataSetID rsetId = dataId.getDataSetID( );
		assert dsetId != null;
		assert rsetId != null;
		assert dsetId.toString( ).equals( rsetId.toString( ) );
	}

	protected void closeQuery( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		if ( generateBy instanceof ReportItemDesign )
		{
			ReportItemDesign design = (ReportItemDesign) generateBy;
			IBaseQueryDefinition query = design.getQuery( );
			if ( query != null )
			{
				IResultSet rset = (IResultSet) resultSets.pop( );
				if ( rset != null )
				{
					rset.close( );
				}
			}
		}
	}

	/**
	 * push the content into stack
	 * 
	 * @param content
	 */
	protected void pushContent( IContent content )
	{
		context.pushContent( content );
	}

	/**
	 * pop the content from the stack
	 */
	protected IContent popContent( )
	{
		return context.popContent( );
	}

	/**
	 * get the first content from the content stack.
	 * 
	 * @return the first content in the stack.
	 */
	protected IContent peekContent( )
	{
		return context.getContent( );
	}

	/**
	 * output the content to emitter
	 * 
	 * @param content
	 *            output content
	 */
	protected void startContent( IContent content, IContentEmitter emitter )
	{
		// open the query used by the content, locate the resource
		openQuery( content );
		outputStartVisitor.visit( content, emitter );
	}

	/**
	 * output the content to emitter.
	 * 
	 * @param content
	 *            output content
	 */
	protected void endContent( IContent content, IContentEmitter emitter )
	{
		outputEndVisitor.visit( content, emitter );
		// close the query used by the content
		closeQuery( content );
	}

	protected IContentVisitor outputStartVisitor = new IContentVisitor( ) {

		public void visit( IContent content, Object value )
		{
			content.accept( this, value );
		}

		public void visitContent( IContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContent( content );
		}

		public void visitPage( IPageContent page, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startPage( page );
		}

		public void visitContainer( IContainerContent container, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startContainer( container );
		}

		public void visitTable( ITableContent table, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startTable( table );
		}

		public void visitTableBand( ITableBandContent tableBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			switch ( tableBand.getType( ) )
			{
				case ITableBandContent.BAND_HEADER :
					emitter.startTableHeader( tableBand );
					break;
				case ITableBandContent.BAND_FOOTER :
					emitter.startTableFooter( tableBand );
					break;
				default :
					emitter.startTableBody( tableBand );
					break;
			}
		}

		public void visitRow( IRowContent row, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startRow( row );
		}

		public void visitCell( ICellContent cell, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startCell( cell );
		}

		public void visitText( ITextContent text, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startText( text );
		}

		public void visitLabel( ILabelContent label, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startLabel( label );
		}
		
		public void visitAutoText( IAutoTextContent autoText, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			if ( autoText.getType() == IAutoTextContent.TOTAL_PAGE )
			{
				autoText.setText(String.valueOf(reportDoc.getPageCount()));	
			}
			emitter.startAutoText( autoText );
		}

		public void visitData( IDataContent data, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startData( data );
		}

		public void visitImage( IImageContent image, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startImage( image );
		}

		public void visitForeign( IForeignContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.startForeign( content );
		}

	};

	protected IContentVisitor outputEndVisitor = new IContentVisitor( ) {

		public void visit( IContent content, Object value )
		{
			content.accept( this, value );
		}

		public void visitContent( IContent content, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContent( content );
		}

		public void visitPage( IPageContent page, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endPage( page );
		}

		public void visitContainer( IContainerContent container, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endContainer( container );
		}

		public void visitTable( ITableContent table, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endTable( table );
		}

		public void visitTableBand( ITableBandContent tableBand, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			switch ( tableBand.getType( ) )
			{
				case ITableBandContent.BAND_HEADER :
					emitter.endTableHeader( tableBand );
					break;
				case ITableBandContent.BAND_FOOTER :
					emitter.endTableFooter( tableBand );
					break;
				default :
					emitter.endTableBody( tableBand );
					break;
			}
		}

		public void visitRow( IRowContent row, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endRow( row );
		}

		public void visitCell( ICellContent cell, Object value )
		{
			IContentEmitter emitter = (IContentEmitter) value;
			emitter.endCell( cell );
		}

		public void visitText( ITextContent text, Object value )
		{
		}

		public void visitLabel( ILabelContent label, Object value )
		{
		}
		
		public void visitAutoText( IAutoTextContent autoText, Object value )
		{
		}

		public void visitData( IDataContent data, Object value )
		{
		}

		public void visitImage( IImageContent image, Object value )
		{
		}

		public void visitForeign( IForeignContent content, Object value )
		{
		}
	};
}
