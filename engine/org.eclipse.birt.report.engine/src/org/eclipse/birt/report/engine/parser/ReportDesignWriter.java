/*******************************************************************************
 * Copyright (c) 2004,2005,2007 , 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.parser;

import java.io.OutputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.List;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.ActionDesign;
import org.eclipse.birt.report.engine.ir.BandDesign;
import org.eclipse.birt.report.engine.ir.CellDesign;
import org.eclipse.birt.report.engine.ir.ColumnDesign;
import org.eclipse.birt.report.engine.ir.DataItemDesign;
import org.eclipse.birt.report.engine.ir.DefaultReportItemVisitorImpl;
import org.eclipse.birt.report.engine.ir.DrillThroughActionDesign;
import org.eclipse.birt.report.engine.ir.DynamicTextItemDesign;
import org.eclipse.birt.report.engine.ir.FreeFormItemDesign;
import org.eclipse.birt.report.engine.ir.GridItemDesign;
import org.eclipse.birt.report.engine.ir.GroupDesign;
import org.eclipse.birt.report.engine.ir.HighlightDesign;
import org.eclipse.birt.report.engine.ir.HighlightRuleDesign;
import org.eclipse.birt.report.engine.ir.ImageItemDesign;
import org.eclipse.birt.report.engine.ir.LabelItemDesign;
import org.eclipse.birt.report.engine.ir.ListItemDesign;
import org.eclipse.birt.report.engine.ir.ListingDesign;
import org.eclipse.birt.report.engine.ir.MapDesign;
import org.eclipse.birt.report.engine.ir.MapRuleDesign;
import org.eclipse.birt.report.engine.ir.MasterPageDesign;
import org.eclipse.birt.report.engine.ir.PageSetupDesign;
import org.eclipse.birt.report.engine.ir.Report;
import org.eclipse.birt.report.engine.ir.ReportElementDesign;
import org.eclipse.birt.report.engine.ir.ReportItemDesign;
import org.eclipse.birt.report.engine.ir.RowDesign;
import org.eclipse.birt.report.engine.ir.SimpleMasterPageDesign;
import org.eclipse.birt.report.engine.ir.StyledElementDesign;
import org.eclipse.birt.report.engine.ir.TableItemDesign;
import org.eclipse.birt.report.engine.ir.TextItemDesign;
import org.eclipse.birt.report.engine.ir.VisibilityDesign;
import org.eclipse.birt.report.engine.ir.VisibilityRuleDesign;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import com.ibm.icu.text.DecimalFormat;

/**
 * visitor used to write the IR.
 * 
 */
public class ReportDesignWriter
{

	public void write( OutputStream out, Report report ) throws Exception
	{
		Document document = DocumentBuilderFactory
				.newInstance( ).newDocumentBuilder( ).newDocument( );

		new ReportDumpVisitor( document ).createDocument( report );

		Transformer tr = TransformerFactory.newInstance( ).newTransformer( );
		tr.setOutputProperty( OutputKeys.STANDALONE, "none" );
		tr.setOutputProperty( OutputKeys.INDENT, "yes" );
		tr.setOutputProperty( OutputKeys.METHOD, "xml" );
		tr.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "3" );

		tr.transform( new DOMSource( document ), new StreamResult( out ) );
	}

	private class ReportDumpVisitor extends DefaultReportItemVisitorImpl
	{

		Document document;
		Element element;

		/**
		 * constructor.
		 * 
		 * @param writer
		 */
		ReportDumpVisitor( Document document )
		{
			this.document = document;
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
			attribute( "bookmark", item.getBookmark( ) );
			attribute( "toc", item.getTOC( ) );
			attribute( "onCreate", item.getOnCreate( ) );
			attribute( "onRender", item.getOnRender( ) );
			attribute( "onPageBreak", item.getOnPageBreak( ) );
			writeAction( item.getAction( ) );
			writeVisibility( item.getVisibility( ) );
		}

		/**
		 * write attribute of styled element
		 * 
		 * @param item
		 */
		private void writeStyledElement( StyledElementDesign item )
		{
			writeReportElement( item );
			attribute( "style", item.getStyleName( ) ); //$NON-NLS-1$
			writeMap( item.getMap( ) );
			writeHighlight( item.getHighlight( ) );
		}

		protected void writeAction( ActionDesign action )
		{
			if ( action == null )
				return;
			pushTag( "action" ); //$NON-NLS-1$
			attribute( "target-window", action.getTargetWindow( ) );
			attribute("title",action.getTooltip( ));
			
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
					pushTag( "drill-though" );
					DrillThroughActionDesign drillThrough = action
							.getDrillThrough( );
					attribute( "report-name", drillThrough.getReportName( ) );
					attribute( "bookmark", drillThrough.getBookmark( ) );
					attribute( "bookmark-type", drillThrough.isBookmark( ) );
					attribute( "paramters", drillThrough.getParameters( ) );
					attribute( "search", drillThrough.getSearch( ) );
					attribute( "format", drillThrough.getFormat( ) );

					popTag( );
				default :
					assert false;
			}
			popTag( );
		}

		void writeVisibility( VisibilityDesign visibility )
		{
			if ( visibility == null )
				return;
			pushTag( "visibility" );
			for ( int i = 0; i < visibility.count( ); i++ )
			{
				VisibilityRuleDesign rule = visibility.getRule( i );
				pushTag( "rule" );
				attribute( "format", rule.getExpression( ) );
				text( rule.getExpression( ) );
				popTag( );
			}
			popTag( );

		}

		void writeMap( MapDesign map )
		{
			if ( map == null )
				return;
			pushTag( "map" );
			for ( int i = 0; i < map.getRuleCount( ); i++ )
			{
				MapRuleDesign rule = map.getRule( i );
				pushTag( "rule" );
				attribute( "expression", rule.getTestExpression( ) );
				attribute( "operator", rule.getOperator( ) );
				if ( rule.ifValueIsList( ) )
				{
					List valueList = rule.getValue1List( );
					for ( int index = 0; index < valueList.size( ); index++ )
					{
						attribute( "value" + index, valueList
								.get( index ) );
					}
				}
				else
				{
					attribute( "value1", rule.getValue1( ) );
					attribute( "value2", rule.getValue2( ) );
				}
				text( rule.getDisplayText( ) );
				popTag( );
			}
			popTag( );

		}

		void writeHighlight( HighlightDesign highlight )
		{
			if ( highlight == null )
				return;
			pushTag( "map" );
			for ( int i = 0; i < highlight.getRuleCount( ); i++ )
			{
				HighlightRuleDesign rule = highlight.getRule( i );
				pushTag( "rule" );
				attribute( "expression", rule.getTestExpression( ) );
				attribute( "operator", rule.getOperator( ) );
				if ( rule.ifValueIsList( ) )
				{
					List valueList = rule.getValue1List( );
					for ( int index = 0; index < valueList.size( ); index++ )
					{
						attribute( "value" + index, valueList
								.get( index ) );
					}
				}
				else
				{
					attribute( "value1", rule.getValue1( ) );
					attribute( "value2", rule.getValue2( ) );
				}
				text( rule.getStyle( ).getCssText( ) );
				popTag( );
			}
			popTag( );
		}

		/**
		 * write report element attribute
		 * 
		 * @param elem
		 *            the element to be writeed.
		 */
		private void writeReportElement( ReportElementDesign elem )
		{
			if ( elem.getID( ) > 0 )
			{
				attribute( "id", elem.getID( ) );
			}
			attribute( "name", elem.getName( ) ); //$NON-NLS-1$
			attribute( "extends", elem.getExtends( ) ); //$NON-NLS-1$
			attribute( "javaClass", elem.getJavaClass( ) );
			attribute( "properties", elem.getCustomProperties( ) );
			attribute( "expressions", elem.getNamedExpressions( ) );
		}

		/**
		 * report contains
		 * 
		 * @param report
		 */
		public void createDocument( Report report )
		{
			pushTag( "report" ); //$NON-NLS-1$
			
			Map styles = report.getStyles( );
			if ( styles.size( ) > 0 )
			{
				Iterator iter = styles.entrySet( ).iterator( );
				pushTag( "styles" ); //$NON-NLS-1$
				while ( iter.hasNext( ) )
				{
					pushTag( "style" );
					Map.Entry entry = (Map.Entry) iter.next( );
					String styleName = (String) entry.getKey( );
					IStyle style = (IStyle) entry.getValue( );
					attribute( "name", styleName );
					attribute( "css-text", style.getCssText( ) );
					popTag( );
				}
				popTag( );
			}

			pushTag( "page-setup" ); //$NON-NLS-1$
			PageSetupDesign pageSetup = report.getPageSetup( );
			for ( int i = 0; i < pageSetup.getMasterPageCount( ); i++ )
			{
				writeSimpleMasterPage( (SimpleMasterPageDesign) pageSetup
						.getMasterPage( i ) );
			}
			popTag( );

			if ( report.getContentCount( ) > 0 )
			{
				pushTag( "body" ); //$NON-NLS-1$
				for ( int i = 0; i < report.getContentCount( ); i++ )
				{
					report.getContent( i ).accept( this, null );
				}
				popTag( );
			}

			popTag( );
		}

		/**
		 * write listband content
		 * 
		 * @param band
		 *            band
		 */
		public Object visitBand( BandDesign band, Object value )
		{
			pushTag( "band" );
			writeReportItem( band );

			for ( int i = 0; i < band.getContentCount( ); i++ )
			{
				band.getContent( i ).accept( this, value );
			}
			popTag( );

			return value;
		}

		private void writeMasterPage( MasterPageDesign page )
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

		private void writeSimpleMasterPage( SimpleMasterPageDesign page )
		{
			pushTag( "simple-master-page" ); //$NON-NLS-1$

			writeMasterPage( page );

			attribute( "show-header-on-first", page.isShowHeaderOnFirst( ) ); //$NON-NLS-1$
			attribute( "show-footer-on-last", page.isShowFooterOnLast( ) ); //$NON-NLS-1$
			attribute( "floating-footer", page.isFloatingFooter( ) ); //$NON-NLS-1$
			pushTag( "header" ); //$NON-NLS-1$
			for ( int i = 0; i < page.getHeaderCount( ); i++ )
			{
				page.getHeader( i ).accept( this, null );
			}
			popTag( );

			pushTag( "footer" ); //$NON-NLS-1$
			for ( int i = 0; i < page.getFooterCount( ); i++ )
			{
				page.getFooter( i ).accept( this, null );
			}
			popTag( );

			popTag( );
		}

		protected void writeListing( ListingDesign listing )
		{

			attribute( "repeat-header", listing.isRepeatHeader( ) );
			attribute( "page-break-interval", listing.getPageBreakInterval( ) );

			BandDesign header = listing.getHeader( );
			if ( header != null )
			{
				header.accept( this, null );
			}

			for ( int i = 0; i < listing.getGroupCount( ); i++ )
			{
				listing.getGroup( i ).accept( this, null );
			}

			BandDesign detail = listing.getDetail( );
			if ( detail != null )
			{
				detail.accept( this, null );
			}

			BandDesign footer = listing.getFooter( );
			if ( footer != null )
			{
				footer.accept( this, null );
			}
		}

		public Object visitGroup( GroupDesign group, Object value )
		{
			pushTag( "group" ); //$NON-NLS-1$
			writeReportItem( group );

			if ( group.getHeader( ) != null )
			{
				group.getHeader( ).accept( this, value );
			}
			if ( group.getFooter( ) != null )
			{
				group.getFooter( ).accept( this, value );
			}
			popTag( );
			return value;
		}

		public Object visitTextItem( TextItemDesign text, Object value )
		{
			pushTag( "text" ); //$NON-NLS-1$

			writeReportItem( text );

			attribute( "type", text.getTextType( ) );
			attribute( "text-key", text.getTextKey( ) );
			text( text.getText( ) );
			popTag( );
			return value;
		}

		public Object visitDynamicTextItem( DynamicTextItemDesign dynamicText,
				Object value )
		{
			pushTag( "dynamic-text" ); //$NON-NLS-1$
			writeReportItem( dynamicText );
			attribute( "content-type", dynamicText.getContentType( ) );
			text( dynamicText.getContent( ) );

			popTag( );
			return value;

		}

		public Object visitListItem( ListItemDesign list, Object value )
		{
			pushTag( "list" ); //$NON-NLS-1$

			writeListing( list );

			popTag( );
			return value;
		}

		public Object visitDataItem( DataItemDesign data, Object value )
		{
			pushTag( "data" ); //$NON-NLS-1$
			writeReportItem( data );
			attribute( "supress-duplicate", data.getSuppressDuplicate( ) );
			attribute( "help-text", data.getHelpText( ) );
			attribute( "help-text-key", data.getHelpTextKey( ) );
			text( data.getBindingColumn( ) );
			popTag( );
			return value;
		}

		public Object visitLabelItem( LabelItemDesign label, Object value )
		{
			pushTag( "label" ); //$NON-NLS-1$
			writeReportItem( label );
			attribute( "help-text", label.getHelpText( ) );
			attribute( "help-text-key", label.getHelpTextKey( ) );
			attribute( "text-key", label.getTextKey( ) );
			text( label.getText( ) );
			popTag( );
			return value;
		}

		public Object visitGridItem( GridItemDesign grid, Object value )
		{
			pushTag( "grid" ); //$NON-NLS-1$
			writeReportItem( grid );

			pushTag( "columns" );
			for ( int i = 0; i < grid.getColumnCount( ); i++ )
			{
				writeColumn( grid.getColumn( i ) );
			}
			popTag( );
			for ( int i = 0; i < grid.getRowCount( ); i++ )
			{
				grid.getRow( i ).accept( this, value );
			}
			popTag( );
			return value;
		}

		protected void writeColumn( ColumnDesign column )
		{
			pushTag( "column" ); //$NON-NLS-1$
			writeStyledElement( column );
			attribute( "width", column.getWidth( ) ); //$NON-NLS-1$
			attribute( "supress-duplicate", column.getSuppressDuplicate( ) );
			attribute( "has-data-in-detail", column.hasDataItemsInDetail( ) );
			attribute( "visibility", column.getVisibility( ) );

			popTag( );
		}

		public Object visitRow( RowDesign row, Object value )
		{
			pushTag( "row" ); //$NON-NLS-1$
			writeReportItem( row );
			attribute( "start-of-group", row.isStartOfGroup( ) );
			for ( int i = 0; i < row.getCellCount( ); i++ )
			{
				row.getCell( i ).accept( this, value );
			}
			popTag( );
			return value;
		}

		public Object visitCell( CellDesign cell, Object value )
		{
			pushTag( "cell" ); //$NON-NLS-1$
			writeReportItem( cell );
			attribute( "column", cell.getColumn( ) ); //$NON-NLS-1$
			attribute( "col-span", cell.getColSpan( ), 1.0 ); //$NON-NLS-1$
			attribute( "row-span", cell.getRowSpan( ), 1.0 ); //$NON-NLS-1$
			attribute( "drop", cell.getDrop( ) ); //$NON-NLS-1$
			
			// since the cell's display-group-icon value setting arithmetic has been changed in EngineIRVisitor,
			// remove write the field to html to fix the unit test bug.
			// attribute( "display-group-icon", cell.getDisplayGroupIcon( ) ); //$NON-NLS-1$
			for ( int i = 0; i < cell.getContentCount( ); i++ )
			{
				cell.getContent( i ).accept( this, null );
			}
			popTag( );
			return value;
		}

		public Object visitTableItem( TableItemDesign table, Object value )
		{
			pushTag( "table" ); //$NON-NLS-1$

			pushTag( "columns" );
			for ( int i = 0; i < table.getColumnCount( ); i++ )
			{
				writeColumn( table.getColumn( i ) );
			}
			popTag( );

			writeListing( table );
			attribute( "caption", table.getCaption( ) );
			attribute( "caption-key", table.getCaptionKey( ) );

			popTag( );

			return value;
		}

		public Object visitImageItem( ImageItemDesign image, Object value )
		{
			pushTag( "image" ); //$NON-NLS-1$
			writeReportItem( image );

			switch ( image.getImageSource( ) )
			{
				case ImageItemDesign.IMAGE_NAME :
					attribute( "image-name", image.getImageName( ) );

					break;
				case ImageItemDesign.IMAGE_URI :
					attribute( "image-uri", image.getImageUri( ) );

					break;
				case ImageItemDesign.IMAGE_FILE :
					attribute( "image-file", image.getImageUri( ) );
					popTag( );
					break;
				case ImageItemDesign.IMAGE_EXPRESSION :
					attribute( "image-type", image.getImageFormat( ) );
					attribute( "image-expr", image.getImageExpression( ) );
				default :
					assert false;
			}
			attribute( "help-text", image.getHelpText( ) );
			attribute( "help-text-key", image.getHelpTextKey( ) );
			attribute( "alt-text", image.getAltText( ) );
			attribute( "alt-text-key", image.getAltTextKey( ) );

			popTag( );
			return value;
		}

		public Object visitFreeFormItem( FreeFormItemDesign free, Object value )
		{
			pushTag( "free-form" ); //$NON-NLS-1$
			writeReportItem( free );
			for ( int i = 0; i < free.getItemCount( ); i++ )
			{
				free.getItem( i ).accept( this, null );
			}
			popTag( );
			return value;
		}

		protected void attribute( String name, String value )
		{
			if ( value != null && !"".equals( value ) ) //$NON-NLS-1$
			{
				element.setAttribute( name, value );
			}
		}

		protected void attribute( String name, Map map )
		{
			if ( map != null && !map.isEmpty( ) )
			{
				StringBuffer buffer = new StringBuffer( );
				Iterator iter = map.entrySet( ).iterator( );
				while ( iter.hasNext( ) )
				{
					Map.Entry entry = (Map.Entry) iter.next( );
					buffer.append( entry.getKey( ) );
					buffer.append( ":" );
					buffer.append( entry.getValue( ) );
					buffer.append( ";" );
				}
				if ( buffer.length( ) != 0 )
				{
					buffer.setLength( buffer.length( ) - 1 );
				}
				attribute( name, buffer.toString( ) );
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
				attribute( name, doubleFmt.format( value ) ); //$NON-NLS-1$ //$NON-NLS-2$
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

		protected Stack elements = new Stack( );

		protected void pushTag( String tag )
		{
			elements.push( element );
			Element child = document.createElement( tag );
			if (element != null)
			{
				element.appendChild( child );
			}
			else
			{
				document.appendChild( child );
			}
			element = child;
		}

		protected void text( String text )
		{
			if ( text == null || "".equals( text.trim( ) ) ) //$NON-NLS-1$
			{
				return;
			}
			Text textNode = document.createTextNode( text );
			element.appendChild( textNode );
		}

		protected void popTag( )
		{
			element = (Element) elements.pop( );
		}
	}
}