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
import java.util.Iterator;
import java.util.Stack;

import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GraphicMasterPageDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListBandDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.MultiLineItemDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TableBandDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.w3c.dom.css.CSSStyleDeclaration;

import com.ibm.icu.text.DecimalFormat;

/**
 * visitor used to write the IR.
 * 
 * @version $Revision: 1.15 $ $Date: 2006/04/06 12:35:26 $
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
				if ( item.getStyleName( ) != null )
				{
					attribute( "style", item.getStyleName() ); //$NON-NLS-1$
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

			if ( hasStyle && report.getStyleCount( ) > 0 )
			{
				pushTag( "styles" ); //$NON-NLS-1$
				for ( int i = 0; i < report.getStyleCount( ); i++ )
				{
					writeStyle( report.getStyle( i ) );
				}
				popTag( );
			}

			writePageSetup( report.getPageSetup( ) );

			if ( report.getContentCount( ) > 0 )
			{
				pushTag( "body" ); //$NON-NLS-1$
				for ( int i = 0; i < report.getContentCount( ); i++ )
				{
					report.getContent( i ).accept( this , null);
				}
				popTag( );
			}

			popTag( );
			out.flush( );
		}

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
		public Object visitListBand( ListBandDesign band, Object value )
		{
			pushTag( "band" );
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this, value );
			}
			popTag( );
			return value;
		}

		public Object visitTableBand( TableBandDesign band, Object value )
		{
			pushTag( "band" );
			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this, value );
			}
			popTag( );
			return value;
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
			attribute( "top-margin", page.getTopMargin( ) ); //$NON-NLS-1$
			attribute( "bottom-margin", page.getBottomMargin( ) ); //$NON-NLS-1$
			attribute( "left-margin", page.getLeftMargin( ) ); //$NON-NLS-1$
			attribute( "right-margin", page.getRightMargin( ) ); //$NON-NLS-1$
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
				( (ReportItemDesign) iter.next( ) ).accept( this, null );
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
				page.getHeader( i ).accept( this , null);
			}
			popTag( );

			pushTag( "footer" ); //$NON-NLS-1$
			for ( int i = 0; i < page.getFooterCount( ); i++ )
			{
				page.getFooter( i ).accept( this , null);
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
		private void writeStyle( CSSStyleDeclaration style )
		{
			if ( style != null )
			{
				pushTag( "style" ); //$NON-NLS-1$
				text(style.getCssText());
				popTag( );
			}
		}

		public Object visitGroup( GroupDesign group, Object value )
		{
			pushTag( "group" ); //$NON-NLS-1$

			if ( group.getHeader( ) != null )
			{
				group.getHeader().accept(this, value);
			}
			if ( group.getFooter( ) != null )
			{
				group.getFooter().accept(this, value);
			}
			popTag( );
			return value;
		}

		public Object visitTextItem( TextItemDesign text, Object value )
		{
			pushTag( "text" ); //$NON-NLS-1$
			writeReportItem( text );
			attribute( "content-type", text.getTextType( ) ); //$NON-NLS-1$
			attribute( "resource-key", text.getTextKey( ) ); //$NON-NLS-1$
			text( text.getText( ) );
			popTag( );
			return value;
		}

		public Object visitMultiLineItem( MultiLineItemDesign multiLine , Object value)
		{
			pushTag( "multi-line" ); //$NON-NLS-1$
			writeReportItem( multiLine );
			pushTag( "content-type" ); //$NON-NLS-1$
			text( multiLine.getContentType( ) );
			popTag( );
			pushTag( "content" ); //$NON-NLS-1$
			text( multiLine.getContent( ) );
			popTag( );
			popTag( );
			return value;

		}

		public Object visitListItem( ListItemDesign list, Object value )
		{
			pushTag( "list" ); //$NON-NLS-1$
			writeReportItem( list );
			if ( list.getHeader( ) != null )
			{
				list.getHeader( ).accept( this, value );
			}
			for ( int i = 0; i < list.getGroupCount( ); i++ )
			{
				list.getGroup( i ).accept( this, value );
			}
			if ( list.getDetail( ) != null )
			{
				list.getDetail( ).accept( this, value );
			}
			if ( list.getFooter( ) != null )
			{
				list.getFooter( ).accept( this, value );
			}
			popTag( );
			return value;
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			pushTag( "data" ); //$NON-NLS-1$
			writeReportItem( data );
			text( data.getValue( ) );
			popTag( );
			return value;
		}

		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			pushTag( "label" ); //$NON-NLS-1$
			writeReportItem( label );
			attribute( "resource-key", label.getTextKey( ) ); //$NON-NLS-1$
			text( label.getText( ) );
			popTag( );
			return value;
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
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
			return value;
		}

		protected void writeColumn( ColumnDesign column )
		{
			pushTag( "column" ); //$NON-NLS-1$
			writeStyledElement( column );
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
				cell.getContent( i ).accept( this , null);
			}
			popTag( );
		}

		public Object visitTableItem( TableItemDesign table, Object value )
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
				table.getHeader( ).accept( this, value );
			}
			for ( int i = 0; i < table.getGroupCount( ); i++ )
			{
				table.getGroup( i ).accept( this, value );
			}
			if ( table.getDetail( ) != null )
			{
				table.getDetail( ).accept( this, value );
			}
			if ( table.getFooter( ) != null )
			{
				table.getFooter( ).accept( this, value );
			}
			popTag( );
			
			return value;

		}

		public Object visitImageItem( ImageItemDesign image , Object value)
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
					text( image.getImageUri() );
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
			return value;
		}

		protected void writeAction( ActionDesign action )
		{
			pushTag( "action" ); //$NON-NLS-1$
			switch ( action.getActionType( ) )
			{
				case ActionDesign.ACTION_BOOKMARK :
					pushTag( "bookmark-link" ); //$NON-NLS-1$
					text( action.getBookmark( ) );
					popTag( );
					break;
				case ActionDesign.ACTION_HYPERLINK :
					pushTag( "hyperlink" ); //$NON-NLS-1$
					text( action.getHyperlink( ) );
					popTag( );
					break;
				case ActionDesign.ACTION_DRILLTHROUGH :
				default :
					assert false;
			}
			popTag( );
		}

		public Object visitFreeFormItem( FreeFormItemDesign free, Object value )
		{
			pushTag( "free-form" ); //$NON-NLS-1$
			writeReportItem( free );
			for ( int i = 0; i < free.getItemCount( ); i++ )
			{
				free.getItem( i ).accept( this , null);
			}
			popTag( );
			return value;
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