/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.parser;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GraphicMasterPageDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
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
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyleDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableGroupDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;

/**
 * visitor used to write the IR.
 * 
 * @version $Revision: 1.7 $ $Date: 2005/03/17 07:57:03 $
 */
class ReportDesignWriter
{

	public void write( Writer writer, Report report, boolean hasStyle )
	{
		new ReportDumpVisitor( writer ).writeReport( report, hasStyle );
	}

	private class ReportDumpVisitor extends DefaultReportItemVisitorImpl
	{

		/**
		 * print writer used to write the IR
		 */
		protected PrintWriter out;
		/**
		 * should we write style information
		 *  
		 */
		protected boolean hasStyle;

		/**
		 * constructor.
		 * 
		 * @param writer
		 */
		ReportDumpVisitor( Writer writer )
		{
			out = new PrintWriter( writer );
		}

		/**
		 * write attributes of Report Item.
		 * 
		 * @param item
		 */
		private void writeReportItem( ReportItemDesign item )
		{
			writeStyledElement( item );
			attribute( "x", item.getX( ) ); //$NON-NLS-1$
			attribute( "y", item.getY( ) ); //$NON-NLS-1$
			attribute( "width", item.getWidth( ) ); //$NON-NLS-1$
			attribute( "height", item.getHeight( ) ); //$NON-NLS-1$
			/*
			 * if ( item.getDataSet( ) != null ) { attribute( "dataset",
			 * item.getDataSet( ).getName( ) ); }
			 */

		}

		/**
		 * write attribute of styled element
		 * 
		 * @param item
		 */
		private void writeStyledElement( StyledElementDesign item )
		{
			writeReportElement( item );
			if ( hasStyle )
			{
				if ( item.getStyle( ) != null )
				{
					attribute( "style", item.getStyle( ).getName( ) ); //$NON-NLS-1$
				}
			}
		}

		/**
		 * write report element attribute
		 * 
		 * @param elem
		 *            the element to be writeed.
		 */
		private void writeReportElement( ReportElementDesign elem )
		{
			attribute( "name", elem.getName( ) ); //$NON-NLS-1$
			attribute( "extends", elem.getExtends( ) ); //$NON-NLS-1$
		}

		/**
		 * report contains
		 * 
		 * @param report
		 */
		public void writeReport( Report report, boolean hasStyle )
		{
			this.hasStyle = hasStyle;
			out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" ); //$NON-NLS-1$
			pushTag( "report" ); //$NON-NLS-1$
			attribute( "units", report.getUnit( ) ); //$NON-NLS-1$
			/*
			 * if ( report.getDataSourceCount( ) > 0 ) { pushTag( "data-sources" );
			 * for ( int i = 0; i < report.getDataSourceCount( ); i++ ) {
			 * writeDataSource( report.getDataSource( i ) ); } popTag( ); } if (
			 * report.getDataSetCount( ) > 0 ) { pushTag( "data-sets" ); for (
			 * int i = 0; i < report.getDataSetCount( ); i++ ) { writeDataSet(
			 * report.getDataSet( i ) ); } popTag( ); }
			 */
			if ( hasStyle && report.getStyleCount( ) > 0 )
			{
				pushTag( "styles" ); //$NON-NLS-1$
				for ( int i = 0; i < report.getStyleCount( ); i++ )
				{
					writeStyle( (StyleDesign) report.getStyle( i ) );
				}
				popTag( );
			}

			writePageSetup( report.getPageSetup( ) );

			if ( report.getContentCount( ) > 0 )
			{
				pushTag( "body" ); //$NON-NLS-1$
				for ( int i = 0; i < report.getContentCount( ); i++ )
				{
					report.getContent( i ).accept( this );
				}
				popTag( );
			}

			popTag( );
			out.flush( );
		}

		/**
		 * write the data source.
		 * 
		 * @param ds
		 *            datas ource
		 */
		/*
		 * private void writeDataSource( DataSourceDesign ds ) { assert ds
		 * instanceof JdbcDataSourceDesign;
		 * 
		 * pushTag( "jdbc-data-source" ); writeReportElement( ds );
		 * 
		 * JdbcDataSourceDesign jdbcds = (JdbcDataSourceDesign) ds; attribute(
		 * "user-name", jdbcds.getUserName( ) ); attribute( "password",
		 * jdbcds.getPassword( ) ); text( jdbcds.getUrl( ) );
		 * 
		 * popTag( ); }
		 */

		/**
		 * write out the data set
		 * 
		 * @param ds
		 *            data set
		 */
		/*
		 * private void writeDataSet( DataSetDesign ds ) { assert ds instanceof
		 * SqlQueryDataSetDesign;
		 * 
		 * pushTag( "sql-query" ); writeReportElement( ds );
		 * 
		 * SqlQueryDataSetDesign sqlds = (SqlQueryDataSetDesign) ds; attribute(
		 * "data-source", sqlds.getDataSource( ).getName( ) ); text(
		 * sqlds.getStatement( ) );
		 * 
		 * popTag( ); }
		 */

		/**
		 * write pagesetup
		 * 
		 * @param pageSetup
		 *            pagesetup
		 */
		private void writePageSetup( PageSetupDesign pageSetup )
		{
			pushTag( "page-setup" ); //$NON-NLS-1$
			for ( int i = 0; i < pageSetup.getMasterPageCount( ); i++ )
			{
				writeMasterPage( pageSetup.getMasterPage( i ) );
			}
			popTag( );
		}

		/**
		 * write listband content
		 * 
		 * @param band
		 *            band
		 */
		private void writeListBand( String tag, ListBandDesign band )
		{
			pushTag( tag );
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this );
			}
			popTag( );
		}

		private void writeTableBand( String tag, TableBandDesign band )
		{
			pushTag( tag );
			for ( int i = 0; i < band.getRowCount( ); i++ )
			{
				writeRow( band.getRow( i ) );
			}
			popTag( );
		}

		/**
		 * write master pages
		 * 
		 * @param page
		 *            master page
		 */
		private void writeMasterPage( MasterPageDesign page )
		{
			if ( page instanceof GraphicMasterPageDesign )
			{
				writeGraphicMasterPage( (GraphicMasterPageDesign) page );
			}
			else if ( page instanceof SimpleMasterPageDesign )
			{
				writeSimpleMasterPage( (SimpleMasterPageDesign) page );

			}
		}

		private void writeBaseMasterPage( MasterPageDesign page )
		{
			writeStyledElement( page );
			attribute( "type", page.getPageType( ) ); //$NON-NLS-1$
			attribute( "width", page.getPageWidth( ) ); //$NON-NLS-1$
			attribute( "height", page.getPageHeight( ) ); //$NON-NLS-1$
			attribute( "orientation", page.getOrientation( ) ); //$NON-NLS-1$
			attribute( "top-marign", page.getTopMargin( ) ); //$NON-NLS-1$
			attribute( "bottom-marign", page.getBottomMargin( ) ); //$NON-NLS-1$
			attribute( "left-marign", page.getLeftMargin( ) ); //$NON-NLS-1$
			attribute( "right-marign", page.getRightMargin( ) ); //$NON-NLS-1$
		}

		private void writeGraphicMasterPage( GraphicMasterPageDesign page )
		{
			pushTag( "graphic-master-page" ); //$NON-NLS-1$

			writeBaseMasterPage( page );

			attribute( "columns", page.getColumns( ) ); //$NON-NLS-1$
			attribute( "column-spacing", page.getColumnSpacing( ) ); //$NON-NLS-1$

			pushTag( "contents" ); //$NON-NLS-1$

			Iterator iter = page.getContents( ).iterator( );
			while ( iter.hasNext( ) )
			{
				( (ReportItemDesign) iter.next( ) ).accept( this );
			}

			popTag( );
			popTag( );
		}

		private void writeSimpleMasterPage( SimpleMasterPageDesign page )
		{
			pushTag( "simple-master-page" ); //$NON-NLS-1$

			writeBaseMasterPage( page );

			attribute( "show-header-on-first", page.isShowHeaderOnFirst( ) ); //$NON-NLS-1$
			attribute( "show-footer-on-last", page.isShowFooterOnLast( ) ); //$NON-NLS-1$
			attribute( "floating-footer", page.isFloatingFooter( ) ); //$NON-NLS-1$
			pushTag( "header" ); //$NON-NLS-1$
			for ( int i = 0; i < page.getHeaderCount( ); i++ )
			{
				page.getHeader( i ).accept( this );
			}
			popTag( );

			pushTag( "footer" ); //$NON-NLS-1$
			for ( int i = 0; i < page.getFooterCount( ); i++ )
			{
				page.getFooter( i ).accept( this );
			}
			popTag( );

			popTag( );
		}

		/**
		 * write styles
		 * 
		 * @param style
		 *            style
		 */
		private void writeStyle( StyleDesign style )
		{
			if ( style != null )
			{
				pushTag( "style" ); //$NON-NLS-1$
				writeReportElement( style );
				Iterator iter = style.entrySet( ).iterator( );
				while ( iter.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) iter.next( );
					attribute( entry.getKey( ).toString( ), entry.getValue( ) );
				}
				popTag( );
			}
		}

		void writeListGroup( ListGroupDesign group )
		{
			pushTag( "group" ); //$NON-NLS-1$

			if ( group.getHeader( ) != null )
			{
				writeListBand( "header", group.getHeader( ) ); //$NON-NLS-1$
			}
			if ( group.getFooter( ) != null )
			{
				writeListBand( "footer", group.getFooter( ) ); //$NON-NLS-1$
			}
			popTag( );
		}

		void writeTableGroup( TableGroupDesign group )
		{
			pushTag( "group" ); //$NON-NLS-1$

			if ( group.getHeader( ) != null )
			{
				writeTableBand( "header", group.getHeader( ) ); //$NON-NLS-1$
			}
			if ( group.getFooter( ) != null )
			{
				writeTableBand( "footer", group.getFooter( ) ); //$NON-NLS-1$
			}
			popTag( );
		}

		public void visitTextItem( TextItemDesign text )
		{
			pushTag( "text" ); //$NON-NLS-1$
			writeReportItem( text );
			attribute( "content-type", text.getTextType( ) ); //$NON-NLS-1$
			attribute( "resource-key", text.getTextKey( ) ); //$NON-NLS-1$
			text( text.getText( ) );
			popTag( );
		}

		public void visitMultiLineItem( MultiLineItemDesign multiLine )
		{
			pushTag( "multi-line" ); //$NON-NLS-1$
			writeReportItem( multiLine );
			pushTag( "content-type" ); //$NON-NLS-1$
			text( multiLine.getContentType( ).getExpr( ) );
			popTag( );
			pushTag( "content" ); //$NON-NLS-1$
			text( multiLine.getContent( ).getExpr( ) );
			popTag( );
			popTag( );

		}

		public void visitListItem( ListItemDesign list )
		{
			pushTag( "list" ); //$NON-NLS-1$
			writeReportItem( list );
			if ( list.getHeader( ) != null )
			{
				writeListBand( "header", list.getHeader( ) ); //$NON-NLS-1$
			}
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				writeListGroup( list.getGroup( i ) );
			}
			if ( list.getDetail( ) != null )
			{
				writeListBand( "detail", list.getDetail( ) ); //$NON-NLS-1$
			}
			if ( list.getFooter( ) != null )
			{
				writeListBand( "footer", list.getFooter( ) ); //$NON-NLS-1$
			}
			popTag( );
		}

		public void visitDataItem( DataItemDesign data )
		{
			pushTag( "data" ); //$NON-NLS-1$
			writeReportItem( data );
			text( data.getValue( ).getExpr( ) );
			popTag( );
		}

		public void visitLabelItem( LabelItemDesign label )
		{
			pushTag( "label" ); //$NON-NLS-1$
			writeReportItem( label );
			attribute( "resource-key", label.getTextKey( ) ); //$NON-NLS-1$
			text( label.getText( ) );
			popTag( );
		}

		public void visitGridItem( GridItemDesign grid )
		{
			pushTag( "grid" ); //$NON-NLS-1$
			writeReportItem( grid );
			for ( int i = 0; i < grid.getColumnCount( ); i++ )
			{
				writeColumn( grid.getColumn( i ) );
			}
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				writeRow( grid.getRow( i ) );
			}
			popTag( );
		}

		protected void writeColumn( ColumnDesign column )
		{
			pushTag( "column" ); //$NON-NLS-1$
			writeStyledElement( column );
			attribute( "repeat", column.getRepeat( ), 1.0 ); //$NON-NLS-1$
			attribute( "width", column.getWidth( ) ); //$NON-NLS-1$
			popTag( );
		}

		protected void writeRow( RowDesign row )
		{
			pushTag( "row" ); //$NON-NLS-1$
			writeStyledElement( row );
			attribute( "height", row.getHeight( ) ); //$NON-NLS-1$
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				writeCell( row.getCell( i ) );
			}
			popTag( );
		}

		protected void writeCell( CellDesign cell )
		{
			pushTag( "cell" ); //$NON-NLS-1$
			writeStyledElement( cell );
			attribute( "column", cell.getColumn( ) ); //$NON-NLS-1$
			attribute( "col-span", cell.getColSpan( ), 1.0 ); //$NON-NLS-1$
			attribute( "row-span", cell.getRowSpan( ), 1.0 ); //$NON-NLS-1$
			attribute( "height", cell.getHeight( ) ); //$NON-NLS-1$
			attribute( "width", cell.getWidth( ) ); //$NON-NLS-1$
			attribute( "drop", cell.getDrop( ) ); //$NON-NLS-1$
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this );
			}
			popTag( );
		}

		public void visitTableItem( TableItemDesign table )
		{
			pushTag( "table" ); //$NON-NLS-1$
			writeReportItem( table );

			if ( table.getCaption( ) != null || table.getCaptionKey( ) != null )
			{
				pushTag( "caption" ); //$NON-NLS-1$
				attribute( "resource-key", table.getCaptionKey( ) ); //$NON-NLS-1$
				text( table.getCaption( ) );
				popTag( );
			}

			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				writeColumn( table.getColumn( i ) );
			}

			if ( table.getHeader( ) != null )
			{
				writeTableBand( "header", table.getHeader( ) ); //$NON-NLS-1$
			}
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				writeTableGroup( table.getGroup( i ) );
			}
			if ( table.getDetail( ) != null )
			{
				writeTableBand( "detail", table.getDetail( ) ); //$NON-NLS-1$
			}
			if ( table.getFooter( ) != null )
			{
				writeTableBand( "footer", table.getFooter( ) ); //$NON-NLS-1$
			}
			popTag( );

		}

		public void visitImageItem( ImageItemDesign image )
		{
			pushTag( "image" ); //$NON-NLS-1$
			writeReportItem( image );
			switch ( image.getImageSource( ) )
			{
				case ImageItemDesign.IMAGE_NAME :
					pushTag( "image-name" ); //$NON-NLS-1$
					text( image.getImageName( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_URI :
					pushTag( "uri" ); //$NON-NLS-1$
					text( image.getImageUri( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_FILE :
					pushTag( "uri" ); //$NON-NLS-1$
					text( image.getImageFile( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_EXPRESSION :
				default :
					assert false;
			}

			if ( image.getAction( ) != null )
			{
				writeAction( image.getAction( ) );
			}
			popTag( );
		}

		protected void writeAction( ActionDesign action )
		{
			pushTag( "action" ); //$NON-NLS-1$
			switch ( action.getActionType( ) )
			{
				case ActionDesign.ACTION_BOOKMARK :
					pushTag( "bookmark-link" ); //$NON-NLS-1$
					text( action.getBookmark( ).getExpr( ) );
					popTag( );
					break;
				case ActionDesign.ACTION_HYPERLINK :
					pushTag( "hyperlink" ); //$NON-NLS-1$
					text( action.getHyperlink( ).getExpr( ) );
					popTag( );
					break;
				case ActionDesign.ACTION_DRILLTHROUGH :
				default :
					assert false;
			}
			popTag( );
		}

		public void visitFreeFormItem( FreeFormItemDesign free )
		{
			pushTag( "free-form" ); //$NON-NLS-1$
			writeReportItem( free );
			for ( int i = 0; i < free.getItemCount( ); i++ )
			{
				free.getItem( i ).accept( this );
			}
			popTag( );
		}

		protected boolean endTag = true;

		protected void attribute( String name, String value )
		{
			assert ( endTag == false );
			if ( value != null && !"".equals( value ) ) //$NON-NLS-1$
			{
				out.print( " " + name + "=\"" + value + "\"" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			}
		}

		protected void attribute( String name, double value )
		{
			attribute( name, value, 0.0 );
		}

		protected DecimalFormat doubleFmt = new DecimalFormat( "##.##" ); //$NON-NLS-1$

		protected void attribute( String name, double value, double omitValue )
		{
			if ( value != omitValue )
			{
				attribute( name, doubleFmt.format( value ) );
			}
		}

		protected void attribute( String name, boolean value )
		{
			attribute( name, value ? "true" : "false" ); //$NON-NLS-1$ //$NON-NLS-2$
		}

		protected void attribute( String name, Object value )
		{
			if ( value != null )
			{
				attribute( name, value.toString( ) );
			}
		}
		protected Stack tagStack = new Stack( );

		protected void pushTag( String tag )
		{
			if ( endTag == false )
			{
				out.println( ">" ); //$NON-NLS-1$
			}
			indent( );
			out.print( "<" + tag ); //$NON-NLS-1$
			endTag = false;
			tagStack.push( tag );
		}

		protected void text( String text )
		{
			if ( text == null || "".equals( text.trim( ) ) ) //$NON-NLS-1$
			{
				return;
			}
			if ( endTag == false )
			{
				out.println( ">" ); //$NON-NLS-1$
			}
			endTag = true;
			indent( );
			out.println( text );
		}

		protected void popTag( )
		{
			String tag = (String) tagStack.pop( );
			if ( endTag == false )
			{
				endTag = true;
				out.print( ">" ); //$NON-NLS-1$
			}
			else
			{
				indent( );
			}
			out.println( "</" + tag + ">" ); //$NON-NLS-1$ //$NON-NLS-2$

		}

		protected void indent( )
		{
			for ( int i = 0; i < tagStack.size( ); i++ )
			{
				out.print( "    " ); //$NON-NLS-1$
			}
		}
	}
}