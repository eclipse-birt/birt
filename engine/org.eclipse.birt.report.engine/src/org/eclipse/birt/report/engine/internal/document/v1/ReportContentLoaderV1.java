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

package org.eclipse.birt.report.engine.internal.document.v1;

import java.io.IOException;
import java.util.HashMap;
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
import org.eclipse.birt.report.engine.api.impl.ReportDocumentReader;
import org.eclipse.birt.report.engine.content.ContentFactory;
import org.eclipse.birt.report.engine.content.IAutoTextContent;
import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IContentVisitor;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IElement;
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
import org.eclipse.birt.report.engine.emitter.ContentEmitterAdapter;
import org.eclipse.birt.report.engine.emitter.IContentEmitter;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.internal.document.IReportContentLoader;
import org.eclipse.birt.report.engine.ir.AutoTextItemDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.ExtendedItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.IReportItemVisitor;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListGroupDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TemplateDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.parser.ReportParser;
import org.eclipse.birt.report.engine.presentation.IPageHint;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

public class ReportContentLoaderV1 implements IReportContentLoader
{

	protected static Logger logger = Logger
			.getLogger( ReportContentLoaderV1.class.getName( ) );

	protected ExecutionContext context;
	protected IDataEngine dataEngine;
	protected IContentEmitter emitter;
	protected ReportContentReaderV1 reader;
	protected ReportContentReaderV1 pageReader;
	protected PageHintReaderV1 hintReader;
	protected Report report;
	protected IReportDocument reportDoc;
	protected ReportContent reportContent;
	/**
	 * the offset of current read object. The object has been read out, setted
	 * in loadContent();
	 */
	protected long currentOffset;

	protected Stack resultSets = new Stack( );

	public ReportContentLoaderV1( ExecutionContext context )
	{
		this.context = context;
		dataEngine = context.getDataEngine( );
		ReportDesignHandle reportDesign = context.getDesign( );
		Report report = new ReportParser( context ).parse( reportDesign );
		context.setReport( report );

		reportContent = (ReportContent) ContentFactory
				.createReportContent( report );
		context.setReportContent( reportContent );

		reportDoc = (ReportDocumentReader) context.getReportDocument( );
		dataEngine.prepare( report, context.getAppContext( ) );
	}

	protected void openReaders( )
	{
		try
		{
			reader = new ReportContentReaderV1( reportContent, reportDoc );
			reader.open( ReportDocumentConstants.CONTENT_STREAM );
			pageReader = new ReportContentReaderV1( reportContent, reportDoc );
			pageReader.open( ReportDocumentConstants.PAGE_STREAM );
			hintReader = new PageHintReaderV1( reportDoc );
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
			// we don't support this feature for V1
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

	private void excutePage( long pageNumber, boolean bodyOnly )
	{
		IPageHint pageHint = hintReader.getPageHint( pageNumber );
		IPageContent pageContent = null;
		if ( !bodyOnly )
		{
			pageContent = loadPageContent( pageHint.getOffset( ) );
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

		long startOffset = pageHint.getSectionStart( 0 );
		long endOffset = pageHint.getSectionEnd( 0 );
		if ( startOffset != -1 && endOffset != -1 )
		{
			if ( reader != null )
			{
				reader.setOffset( startOffset );
				IContent content = loadContent( reader );
				while ( content != null && currentOffset <= endOffset )
				{
					content = loadFullContent( content, reader, emitter );
				}
			}
		}

		if ( !bodyOnly )
		{
			emitter.endPage( pageContent );
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

	protected IPageContent loadPageContent( long offset )
	{

		if ( pageReader != null )
		{

			pageReader.setOffset( offset );
			IPageContent pageContent = (IPageContent) loadContent( pageReader );
			if ( pageContent == null )
			{
				return null;
			}

			SimpleMasterPageDesign masterPage = (SimpleMasterPageDesign) pageContent
					.getGenerateBy( );

			IContent content = loadContent( pageReader );
			for ( int i = 0; i < masterPage.getHeaderCount( ); i++ )
			{
				pageContent.getHeader( ).add( content );
				content = loadFullContent( content, pageReader,
						new DOMBuildingEmitter( ) );

			}
			for ( int i = 0; i < masterPage.getFooterCount( ); i++ )
			{
				pageContent.getFooter( ).add( content );
				content = loadFullContent( content, pageReader,
						new DOMBuildingEmitter( ) );
			}
			return pageContent;
		}
		return null;
	}

	private class DOMBuildingEmitter extends ContentEmitterAdapter
	{

		private IContent parent;

		public DOMBuildingEmitter( )
		{
		}

		public void startContent( IContent content )
		{
			if ( parent != null )
			{
				parent.getChildren( ).add( content );
			}
			parent = content;
		}

		public void endContent( IContent content )
		{
			if ( parent != null )
			{
				parent = (IContent) parent.getParent( );
			}
		}
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
			ReportContentReaderV1 reader, IContentEmitter emitter )
	{
		Stack contents = new Stack( );
		contents.push( root );

		startContent( root, emitter );

		IContent next = loadContent( reader );

		do
		{
			IContent parent = (IContent) contents.peek( );
			if ( isChildOf( next, parent ) )
			{
				contents.push( next );
				next.setParent( parent );
				startContent( next, emitter );
				next = loadContent( reader );
			}
			else
			{
				endContent( parent, emitter );
				contents.pop( );
			}
		} while ( !contents.isEmpty( ) );

		return next;
	}

	private boolean isChildOf( DesignElementHandle child,
			DesignElementHandle parent )
	{
		DesignElementHandle container = child.getContainer( );
		if ( container == parent )
		{
			return true;
		}
		if ( container == null )
		{
			return false;
		}
		return isChildOf( container, parent );
	}

	private boolean isChildOf( IContent content, IContent parent )
	{
		if ( content != null && parent != null )
		{
			DesignElementHandle handle = getDesignHandle( content );
			DesignElementHandle parentHandle = getDesignHandle( parent );
			if ( handle != null && parentHandle != null )
			{
				return isChildOf( handle, parentHandle );
			}
			else
			{
				// we may create empty band for table (table body)
				if ( parent instanceof ITableContent
						&& content instanceof ITableBandContent )
				{
					return true;
				}
				// we may create row for empty band (table body)
				if ( parent instanceof ITableBandContent
						&& content instanceof IRowContent )
				{
					return isRowInBand( (IRowContent) content,
							(ITableBandContent) parent );
				}
				// we may create a empty cell to fix the table layout.
				if ( parent instanceof IRowContent
						&& content instanceof ICellContent )
				{
					return true;
				}
			}
		}
		return false;
	}

	private boolean isRowInBand( IRowContent row, ITableBandContent band )
	{
		Object generateBy = row.getGenerateBy( );
		if ( generateBy instanceof RowDesign )
		{
			RowDesign rowDesign = (RowDesign) generateBy;
			IElement parent = band.getParent( );
			if ( parent instanceof ITableContent )
			{
				ITableContent table = (ITableContent) parent;
				generateBy = table.getGenerateBy( );
				if ( generateBy instanceof ReportItemDesign )
				{
					DesignElementHandle rowHandle = rowDesign.getHandle( );
					DesignElementHandle tableHandle = ( (ReportItemDesign) generateBy )
							.getHandle( );
					if ( rowHandle.getContainer( ) == tableHandle
							|| rowHandle.getContainer( ).getContainer( ) == tableHandle )
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	protected IContent loadContent( ReportContentReaderV1 reader )
	{
		if ( reader != null )
		{
			try
			{
				currentOffset = reader.getOffset( );
				IContent content = reader.readContent( );
				initializeContent( content );
				return content;
			}
			catch ( IOException ex )
			{
				logger.log( Level.SEVERE, "load content failed", ex );
			}
		}
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

	private DesignElementHandle getDesignHandle( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		if ( generateBy instanceof ReportElementDesign )
		{
			return ( (ReportElementDesign) generateBy ).getHandle( );
		}
		return null;
	}

	private HashMap generateByIDs;

	/**
	 * find the report element by the design id. we need get the engine's IR
	 * from the design id, so we can't use the mode's getElementByID().
	 * 
	 * @param designId
	 *            design id
	 * @return design object (engine)
	 */
	protected Object findReportItem( long designId )
	{
		if ( generateByIDs == null )
		{
			generateByIDs = new HashMap( );
			new GenerateIDMapVisitor( generateByIDs ).visitReport( report );
		}
		return generateByIDs.get( new Long( designId ) );
	}

	/**
	 * generate a hash map used to find the element by element id.
	 * 
	 * It visits the report design, add the element id and design object into
	 * the hash map.
	 * 
	 * @version $Revision: 1.6 $ $Date: 2006/05/09 08:41:50 $
	 */
	protected class GenerateIDMapVisitor implements IReportItemVisitor
	{

		/**
		 * map used to store the id, design object.
		 */
		private HashMap IDMaps;

		/**
		 * create a visitor, the maps should be stroe into map.
		 * 
		 * @param map
		 *            map used to store the id->design mapping.
		 */
		public GenerateIDMapVisitor( HashMap map )
		{
			IDMaps = map;
		}

		/**
		 * visit the report, store the id->design mapping.
		 * 
		 * It is the main entry of the class
		 * 
		 * @param report
		 *            the visited report
		 */
		public void visitReport( Report report )
		{
			PageSetupDesign pageSetup = report.getPageSetup( );
			for ( int i = 0; i < pageSetup.getMasterPageCount( ); i++ )
			{
				MasterPageDesign masterPage = (MasterPageDesign) pageSetup
						.getMasterPage( i );
				IDMaps.put( new Long( masterPage.getID( ) ), masterPage );
				if ( masterPage instanceof SimpleMasterPageDesign )
				{
					SimpleMasterPageDesign simpleMasterPage = (SimpleMasterPageDesign) masterPage;
					for ( int j = 0; j < simpleMasterPage.getHeaderCount( ); j++ )
					{
						simpleMasterPage.getHeader( j ).accept( this, null );
					}
					for ( int j = 0; j < simpleMasterPage.getFooterCount( ); j++ )
					{
						simpleMasterPage.getFooter( j ).accept( this, null );
					}
				}
			}

			for ( int i = 0; i < report.getContentCount( ); i++ )
			{
				report.getContent( i ).accept( this, null );
			}

		}

		public Object visitFreeFormItem( FreeFormItemDesign container,
				Object value )
		{
			IDMaps.put( new Long( container.getID( ) ), container );
			for ( int i = 0; i < container.getItemCount( ); i++ )
			{
				container.getItem( i ).accept( this, value );
			}
			return value;
		}

		public Object visitListItem( ListItemDesign list, Object value )
		{
			IDMaps.put( new Long( list.getID( ) ), list );
			visitListBand( list.getHeader( ), value );
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				visitListGroup( list.getGroup( i ), value );
			}
			visitListBand( list.getDetail( ), value );
			visitListBand( list.getFooter( ), value );
			return value;
		}

		protected Object visitListBand( ListBandDesign band, Object value )
		{
			if ( band != null )
			{
				IDMaps.put( new Long( band.getID( ) ), band );
				for ( int i = 0; i < band.getContentCount( ); i++ )
				{
					band.getContent( i ).accept( this, null );
				}
			}
			return value;
		}

		protected Object visitListGroup( ListGroupDesign group, Object value )
		{
			if ( group != null )
			{
				visitListBand( group.getHeader( ), value );
				visitListBand( group.getFooter( ), value );
			}
			return value;
		}

		protected Object visitReportItem( ReportItemDesign item, Object value )
		{
			IDMaps.put( new Long( item.getID( ) ), item );
			return value;
		}

		public Object visitTextItem( TextItemDesign text, Object value )
		{
			return visitReportItem( text, value );
		}

		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			return visitReportItem( label, value );
		}
		
		public Object visitAutoTextItem( AutoTextItemDesign autoText, Object value )
		{
			return visitReportItem( autoText, value );
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			return visitReportItem( data, value );
		}

		public Object visitMultiLineItem( MultiLineItemDesign multiLine,
				Object value )
		{
			return visitReportItem( multiLine, value );
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			IDMaps.put( new Long( grid.getID( ) ), grid );
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				grid.getRow( i ).accept( this, value );
			}
			return value;
		}

		public Object visitTableItem( TableItemDesign table, Object value )
		{
			visitReportItem( table, value );
			visitTableBand( table.getHeader( ), value );
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				visitTableGroup( table.getGroup( i ), value );
			}
			visitTableBand( table.getDetail( ), value );
			visitTableBand( table.getFooter( ), value );
			return null;
		}

		protected Object visitTableBand( TableBandDesign band, Object value )
		{
			if ( band != null )
			{
				IDMaps.put( new Long( band.getID( ) ), band );
				for ( int i = 0; i < band.getRowCount( ); i++ )
				{
					band.getRow( i ).accept( this, null );
				}
			}
			return value;
		}

		protected Object visitTableGroup( TableGroupDesign group, Object value )
		{
			if ( group != null )
			{
				visitTableBand( group.getHeader( ), value );
				visitTableBand( group.getFooter( ), value );
			}
			return value;
		}

		public Object visitRow( RowDesign row, Object value )
		{
			visitReportItem( row, value );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				row.getCell( i ).accept( this, value );
			}
			return value;
		}

		public Object visitCell( CellDesign cell, Object value )
		{
			visitReportItem( cell, value );
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this, value );
			}
			return value;
		}

		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			return visitReportItem( image, value );
		}

		public Object visitExtendedItem( ExtendedItemDesign item, Object value )
		{
			return visitReportItem( item, value );
		}

		public Object visitTemplate( TemplateDesign template, Object value )
		{
			return visitReportItem( template, value );
		}

	}

	protected void openQuery( IContent content )
	{
		Object generateBy = content.getGenerateBy( );
		if ( generateBy instanceof ReportItemDesign )
		{
			if ( !( generateBy instanceof ExtendedItemDesign ) )
			{
				ReportItemDesign design = (ReportItemDesign) generateBy;
				IBaseQueryDefinition query = design.getQuery( );
				if ( query != null )
				{
					IResultSet rset = dataEngine.execute( query );
					resultSets.push( rset );
				}
			}
		}
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
			if ( !( generateBy instanceof ExtendedItemDesign ) )
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
			if(autoText.getType()==IAutoTextContent.TOTAL_PAGE)
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
