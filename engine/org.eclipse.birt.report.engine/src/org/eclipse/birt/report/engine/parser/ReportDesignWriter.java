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
import org.eclipse.birt.report.engine.ir.ReportItemVisitorAdapter;
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
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
class ReportDesignWriter
{

	public void write( Writer writer, Report report, boolean hasStyle )
	{
		new ReportDumpVisitor( writer ).writeReport( report, hasStyle );
	}

	private class ReportDumpVisitor extends ReportItemVisitorAdapter
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
			attribute( "x", item.getX( ) );
			attribute( "y", item.getY( ) );
			attribute( "width", item.getWidth( ) );
			attribute( "height", item.getHeight( ) );
			/*if ( item.getDataSet( ) != null )
			{
				attribute( "dataset", item.getDataSet( ).getName( ) );
			}*/

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
					attribute( "style", item.getStyle( ).getName( ) );
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
			attribute( "name", elem.getName( ) );
			attribute( "extends", elem.getExtends( ) );
		}

		/**
		 * report contains
		 * 
		 * @param report
		 */
		public void writeReport( Report report, boolean hasStyle )
		{
			this.hasStyle = hasStyle;
			out.println( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" );
			pushTag( "report" );
			attribute( "units", report.getUnit( ) );
			/*if ( report.getDataSourceCount( ) > 0 )
			{
				pushTag( "data-sources" );
				for ( int i = 0; i < report.getDataSourceCount( ); i++ )
				{
					writeDataSource( report.getDataSource( i ) );
				}
				popTag( );
			}
			if ( report.getDataSetCount( ) > 0 )
			{
				pushTag( "data-sets" );
				for ( int i = 0; i < report.getDataSetCount( ); i++ )
				{
					writeDataSet( report.getDataSet( i ) );
				}
				popTag( );
			}
*/
			if ( hasStyle && report.getStyleCount( ) > 0 )
			{
				pushTag( "styles" );
				for ( int i = 0; i < report.getStyleCount( ); i++ )
				{
					writeStyle( report.getStyle( i ) );
				}
				popTag( );
			}

			writePageSetup( report.getPageSetup( ) );

			if ( report.getContentCount( ) > 0 )
			{
				pushTag( "body" );
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
		/*private void writeDataSource( DataSourceDesign ds )
		{
			assert ds instanceof JdbcDataSourceDesign;

			pushTag( "jdbc-data-source" );
			writeReportElement( ds );

			JdbcDataSourceDesign jdbcds = (JdbcDataSourceDesign) ds;
			attribute( "user-name", jdbcds.getUserName( ) );
			attribute( "password", jdbcds.getPassword( ) );
			text( jdbcds.getUrl( ) );

			popTag( );
		}*/

		/**
		 * write out the data set
		 * 
		 * @param ds
		 *            data set
		 */
		/*private void writeDataSet( DataSetDesign ds )
		{
			assert ds instanceof SqlQueryDataSetDesign;

			pushTag( "sql-query" );
			writeReportElement( ds );

			SqlQueryDataSetDesign sqlds = (SqlQueryDataSetDesign) ds;
			attribute( "data-source", sqlds.getDataSource( ).getName( ) );
			text( sqlds.getStatement( ) );

			popTag( );
		}*/

		/**
		 * write pagesetup
		 * 
		 * @param pageSetup
		 *            pagesetup
		 */
		private void writePageSetup( PageSetupDesign pageSetup )
		{
			pushTag( "page-setup" );
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
			attribute( "type", page.getPageType( ) );
			attribute( "width", page.getPageWidth( ) );
			attribute( "height", page.getPageHeight( ) );
			attribute( "orientation", page.getOrientation( ) );
			attribute( "top-marign", page.getTopMargin( ) );
			attribute( "bottom-marign", page.getBottomMargin( ) );
			attribute( "left-marign", page.getLeftMargin( ) );
			attribute( "right-marign", page.getRightMargin( ) );
		}

		private void writeGraphicMasterPage( GraphicMasterPageDesign page )
		{
			pushTag( "graphic-master-page" );

			writeBaseMasterPage( page );

			attribute( "columns", page.getColumns( ) );
			attribute( "column-spacing", page.getColumnSpacing( ) );

			pushTag( "contents" );

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
			pushTag( "simple-master-page" );

			writeBaseMasterPage( page );

			attribute( "show-header-on-first", page.isShowHeaderOnFirst( ) );
			attribute( "show-footer-on-last", page.isShowFooterOnLast( ) );
			attribute( "floating-footer", page.isFloatingFooter( ) );
			pushTag( "header" );
			for ( int i = 0; i < page.getHeaderCount( ); i++ )
			{
				page.getHeader( i ).accept( this );
			}
			popTag( );

			pushTag( "footer" );
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
				pushTag( "style" );
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
			pushTag( "group" );
			
			if ( group.getHeader( ) != null )
			{
				writeListBand( "header", group.getHeader( ) );
			}
			if ( group.getFooter( ) != null )
			{
				writeListBand( "footer", group.getFooter( ) );
			}
			popTag( );
		}

		void writeTableGroup( TableGroupDesign group )
		{
			pushTag( "group" );
			
			if ( group.getHeader( ) != null )
			{
				writeTableBand( "header", group.getHeader( ) );
			}
			if ( group.getFooter( ) != null )
			{
				writeTableBand( "footer", group.getFooter( ) );
			}
			popTag( );
		}

		public void visitTextItem( TextItemDesign text )
		{
			pushTag( "text" );
			writeReportItem( text );
			attribute( "content-type", text.getContentType( ) );
			attribute( "resource-key", text.getContentKey( ) );
			text( text.getContent( ) );
			popTag( );
		}

		public void visitMultiLineItem( MultiLineItemDesign multiLine )
		{
			pushTag( "multi-line" );
			writeReportItem( multiLine );
			pushTag( "content-type" );
			text( multiLine.getContentType( ).getExpr( ) );
			popTag( );
			pushTag( "content" );
			text( multiLine.getContent( ).getExpr( ) );
			popTag( );
			popTag( );

		}

		public void visitListItem( ListItemDesign list )
		{
			pushTag( "list" );
			writeReportItem( list );
			if ( list.getHeader( ) != null )
			{
				writeListBand( "header", list.getHeader( ) );
			}
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				writeListGroup( list.getGroup( i ) );
			}
			if ( list.getDetail( ) != null )
			{
				writeListBand( "detail", list.getDetail( ) );
			}
			if ( list.getFooter( ) != null )
			{
				writeListBand( "footer", list.getFooter( ) );
			}
			popTag( );
		}

		public void visitDataItem( DataItemDesign data )
		{
			pushTag( "data" );
			writeReportItem( data );
			text( data.getValue( ).getExpr( ) );
			popTag( );
		}

		public void visitLabelItem( LabelItemDesign label )
		{
			pushTag( "label" );
			writeReportItem( label );
			attribute( "resource-key", label.getTextKey( ) );
			text( label.getText( ) );
			popTag( );
		}

		public void visitGridItem( GridItemDesign grid )
		{
			pushTag( "grid" );
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
			pushTag( "column" );
			writeStyledElement( column );
			attribute( "repeat", column.getRepeat( ), 1.0 );
			attribute( "width", column.getWidth( ) );
			popTag( );
		}

		protected void writeRow( RowDesign row )
		{
			pushTag( "row" );
			writeStyledElement( row );
			attribute( "height", row.getHeight( ) );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				writeCell( row.getCell( i ) );
			}
			popTag( );
		}

		protected void writeCell( CellDesign cell )
		{
			pushTag( "cell" );
			writeStyledElement( cell );
			attribute( "column", cell.getColumn( ) );
			attribute( "col-span", cell.getColSpan( ), 1.0 );
			attribute( "row-span", cell.getRowSpan( ), 1.0 );
			attribute( "height", cell.getHeight( ) );
			attribute( "width", cell.getWidth( ) );
			attribute( "drop", cell.getDrop( ) );
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this );
			}
			popTag( );
		}

		public void visitTableItem( TableItemDesign table )
		{
			pushTag( "table" );
			writeReportItem( table );

			if ( table.getCaption( ) != null || table.getCaptionKey( ) != null )
			{
				pushTag( "caption" );
				attribute( "resource-key", table.getCaptionKey( ) );
				text( table.getCaption( ) );
				popTag( );
			}

			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				writeColumn( table.getColumn( i ) );
			}

			if ( table.getHeader( ) != null )
			{
				writeTableBand( "header", table.getHeader( ) );
			}
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				writeTableGroup( table.getGroup( i ) );
			}
			if ( table.getDetail( ) != null )
			{
				writeTableBand( "detail", table.getDetail( ) );
			}
			if ( table.getFooter( ) != null )
			{
				writeTableBand( "footer", table.getFooter( ) );
			}
			popTag( );

		}

		public void visitImageItem( ImageItemDesign image )
		{
			pushTag( "image" );
			writeReportItem( image );
			switch ( image.getImageSource( ) )
			{
				case ImageItemDesign.IMAGE_NAME :
					pushTag( "image-name" );
					text( image.getImageName( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_URI :
					pushTag( "uri" );
					text( image.getImageUri( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_FILE :
					pushTag( "uri" );
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
			pushTag( "action" );
			switch ( action.getActionType( ) )
			{
				case ActionDesign.ACTION_BOOKMARK :
					pushTag( "bookmark-link" );
					text( action.getBookmark( ).getExpr( ) );
					popTag( );
					break;
				case ActionDesign.ACTION_HYPERLINK :
					pushTag( "hyperlink" );
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
			pushTag( "free-form" );
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
			if ( value != null && !"".equals( value ) )
			{
				out.print( " " + name + "=\"" + value + "\"" );
			}
		}

		protected void attribute( String name, double value )
		{
			attribute( name, value, 0.0 );
		}

		protected DecimalFormat doubleFmt = new DecimalFormat( "##.##" );

		protected void attribute( String name, double value, double omitValue )
		{
			if ( value != omitValue )
			{
				attribute( name, doubleFmt.format( value ) );
			}
		}

		protected void attribute( String name, boolean value )
		{
			attribute( name, value ? "true" : "false" );
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
				out.println( ">" );
			}
			indent( );
			out.print( "<" + tag );
			endTag = false;
			tagStack.push( tag );
		}

		protected void text( String text )
		{
			if ( text == null || "".equals( text.trim( ) ) )
			{
				return;
			}
			if ( endTag == false )
			{
				out.println( ">" );
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
				out.print( ">" );
			}
			else
			{
				indent( );
			}
			out.println( "</" + tag + ">" );

		}

		protected void indent( )
		{
			for ( int i = 0; i < tagStack.size( ); i++ )
			{
				out.print( "    " );
			}
		}
	}
}