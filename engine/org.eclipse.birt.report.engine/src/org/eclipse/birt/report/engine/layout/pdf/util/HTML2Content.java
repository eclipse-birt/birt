/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.content.ICellContent;
import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITableContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.Column;
import org.eclipse.birt.report.engine.content.impl.ObjectContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.css.CSSValue;

/**
 * Class <code>HTML2Content</code> encapsulates the logic of converting a
 * section of HTML text to report content. Currently the supported tags are:
 * <table>
 * <tr>
 * <td><b>Tag Name</b></td>
 * <td><b>Supported Attributes</b></td>
 * </tr>
 * <tr>
 * <td>"a"</td>
 * <td>id, name, href, target</td>
 * </tr>
 * <tr>
 * <td>"address"</td>
 * </tr>
 * <tr>
 * <td>"b"</td>
 * </tr>
 * <tr>
 * <td>"body"</td>
 * </tr>
 * <tr>
 * <td>"center"</td>
 * </tr>
 * <tr>
 * <td>"code"</td>
 * </tr>
 * <tr>
 * <td>"col"</td>
 * <td>width</td>
 * </tr>
 * <tr>
 * <td>"del"</td>
 * </tr>
 * <tr>
 * <td>"dd"</td>
 * </tr>
 * <tr>
 * <td>"div"</td>
 * </tr>
 * <tr>
 * <td>"dl"</td>
 * </tr>
 * <tr>
 * <td>"dt"</td>
 * </tr>
 * <tr>
 * <td>"em"</td>
 * </tr>
 * <tr>
 * <td>"embed"</td>
 * <td>src, alt, height, width</td>
 * </tr>
 * <tr>
 * <td>"font"</td>
 * <td>size, color, face</td>
 * </tr>
 * <tr>
 * <td>"h1"</td>
 * </tr>
 * <tr>
 * <td>"h2"</td>
 * </tr>
 * <tr>
 * <td>"h3"</td>
 * </tr>
 * <tr>
 * <td>"h4"</td>
 * </tr>
 * <tr>
 * <td>"h5"</td>
 * </tr>
 * <tr>
 * <td>"h6"</td>
 * </tr>
 * <tr>
 * <td>"i"</td>
 * </tr>
 * <tr>
 * <td>"img"</td>
 * <td>src, alt, height, width</td>
 * </tr>
 * <tr>
 * <td>"ins"</td>
 * </tr>
 * <tr>
 * <td>"li"</td>
 * </tr>
 * <tr>
 * <td>"ol"</td>
 * </tr>
 * <tr>
 * <td>"p"</td>
 * </tr>
 * <tr>
 * <td>"pre"</td>
 * </tr>
 * <tr>
 * <td>"span"</td>
 * </tr>
 * <tr>
 * <td>"strong"</td>
 * </tr>
 * <tr>
 * <td>"strike"</td>
 * </tr>
 * <tr>
 * <td>"big"</td>
 * </tr>
 * <tr>
 * <td>"small"</td>
 * </tr>
 * <tr>
 * <td>"sub"</td>
 * </tr>
 * <tr>
 * <td>"sup"</td>
 * </tr>
 * <tr>
 * <td>"table"</td>
 * <td>width</td>
 * </tr>
 * <tr>
 * <td>"tbody"</td>
 * </tr>
 * <tr>
 * <td>"td"</td>
 * <td>rowspan, colspan</td>
 * </tr>
 * <tr>
 * <td>"tfoot"</td>
 * </tr>
 * <tr>
 * <td>"thead"</td>
 * </tr>
 * <tr>
 * <td>"tr"width</td>
 * </tr>
 * <tr>
 * <td>"tt"</td>
 * </tr>
 * <tr>
 * <td>"u"</td>
 * </tr>
 * <tr>
 * <td>"ul"</td>
 * </tr>
 * </table>
 * All the supported HTML tags can use attribute "style" to specify HTML inline
 * style information. The syntax of the style attribute value is CSS inline
 * style syntax. The supported CSS properties are listed below:<br> 
 * <b>Text and Fonts</b>
 * <ul>
 * <li>font-family</li>
 * <li>font-size</li>
 * <li>font-weight</li>
 * <li>font-style</li>
 * <li>line-height</li>
 * <li>letter-spacing</li>
 * <li>word-spacing</li>
 * <li>text-align</li>
 * <li>text-decoration</li>
 * <li>text-indent</li>
 * <li>text-transform</li>
 * <li>vertical-align</li>
 * </ul>
 * <b>Colors and Backgrounds</b>
 * <ul>
 * <li>background-color</li>
 * <li>background-image</li>
 * <li>background-repeat</li>
 * </ul>
 * <b>The Box Model - dimensions, padding, margin and borders</b>
 * <ul>
 * <li>padding-top, padding-right, padding-bottom, padding-left</li>
 * <li>border-top, border-right, border-bottom, border-left</li>
 * <li>border-top-style, border-right-style, border-bottom-style,
 * border-left-style</li>
 * <li>border-top-color, border-right-color, border-bottom-color,
 * border-left-color</li>
 * <li>border-top-width, border-right-width, border-bottom-width,
 * border-left-width</li>
 * <li>margin-top, margin-right, margin-bottom, margin-left</li>
 * </ul>
 * <b>Positioning and Display</b>
 * <ul>
 * <li>display</li>
 * <li>visibility</li>
 * </ul>
 * Please note: the CSS shorthand properties are not supported.
 */
public class HTML2Content
{

	protected static final HashSet htmlBlockDisplay = new HashSet( );

	protected static final HashSet htmlInlineDisplay = new HashSet( );

	protected static final HashMap textTypeMapping = new HashMap( );

	static
	{
		htmlInlineDisplay.add( "i" );
		htmlInlineDisplay.add( "font" );
		htmlInlineDisplay.add( "b" );
		htmlInlineDisplay.add( "a" );
		htmlInlineDisplay.add( "code" );
		htmlInlineDisplay.add( "em" );
		htmlInlineDisplay.add( "object" );
		htmlInlineDisplay.add( "img" );
		htmlInlineDisplay.add( "ins" );
		htmlInlineDisplay.add( "span" );
		htmlInlineDisplay.add( "strong" );
		htmlInlineDisplay.add( "sub" );
		htmlInlineDisplay.add( "sup" );
		htmlInlineDisplay.add( "tt" );
		htmlInlineDisplay.add( "u" );
		htmlInlineDisplay.add( "del" );
		htmlInlineDisplay.add( "strike" );
		htmlInlineDisplay.add( "s" );
		htmlInlineDisplay.add( "big" );
		htmlInlineDisplay.add( "small" );
	}

	static
	{
		// block-level elements
		htmlBlockDisplay.add( "dd" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "div" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "dl" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "dt" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h1" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h2" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h3" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h4" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h5" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "h6" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "hr" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "ol" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "p" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "pre" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "ul" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "li" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "address" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "body" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "center" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "table" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "td" ); //$NON-NLS-1$
		htmlBlockDisplay.add( "tr" ); //$NON-NLS-1$

		textTypeMapping.put( IForeignContent.HTML_TYPE,
				TextParser.TEXT_TYPE_HTML );
		textTypeMapping.put( IForeignContent.TEXT_TYPE,
				TextParser.TEXT_TYPE_PLAIN );
		textTypeMapping.put( IForeignContent.UNKNOWN_TYPE,
				TextParser.TEXT_TYPE_AUTO );

	}

	public static void html2Content( IForeignContent foreign )
	{
		processForeignData( foreign );
	}

	protected static void processForeignData( IForeignContent foreign )
	{
		if ( foreign.getChildren( ) != null
				&& foreign.getChildren( ).size( ) > 0 )
		{
			return;
		}

		HashMap styleMap = new HashMap( );
		ReportDesignHandle reportDesign = foreign.getReportContent( )
				.getDesign( ).getReportDesign( );
		HTMLStyleProcessor htmlProcessor = new HTMLStyleProcessor( reportDesign );
		Object rawValue = foreign.getRawValue( );
		Document doc = null;
		if ( null != rawValue )
		{
			doc = new TextParser( ).parse( foreign.getRawValue( ).toString( ),
					(String) textTypeMapping.get( foreign.getRawType( ) ) );
		}

		Element body = null;
		if ( doc != null )
		{
			Node node = doc.getFirstChild( );
			// The following must be true
			if ( node instanceof Element )
			{
				body = (Element) node;
			}
		}
		if ( body != null )
		{
			htmlProcessor.execute( body, styleMap, foreign.getReportContent( )
					.getReportContext( ) == null ? null : foreign
					.getReportContent( ).getReportContext( ).getAppContext( ) );
			IContainerContent container = foreign.getReportContent( )
					.createContainerContent( );

			IStyle parentStyle = foreign.getStyle( );
			if ( CSSValueConstants.INLINE_VALUE.equals( parentStyle
					.getProperty( IStyle.STYLE_DISPLAY ) ) )
			{
				container.getStyle( ).setProperty( IStyle.STYLE_DISPLAY,
						CSSValueConstants.INLINE_VALUE );
			}
			addChild( foreign, container );
			processNodes( body, styleMap, container, null );
			// formalizeInlineContainer( new ArrayList( ), foreign, container );
		}
	}

	/**
	 * Visits the children nodes of the specific node
	 * 
	 * @param ele
	 *            the specific node
	 * @param needEscape
	 *            the flag indicating the content needs escaping
	 * @param cssStyles
	 * @param content
	 *            the parent content of the element
	 * 
	 */
	static void processNodes( Element ele, Map cssStyles, IContent content,
			ActionContent action )
	{
		int level = 0;
		for ( Node node = ele.getFirstChild( ); node != null; node = node
				.getNextSibling( ) )
		{
			if ( node.getNodeName( ).equals( "value-of" ) ) //$NON-NLS-1$
			{
				if ( node.getFirstChild( ) instanceof Element )
				{
					processNodes( (Element) node.getFirstChild( ), cssStyles,
							content, action );
				}
			}
			else if ( node.getNodeName( ).equals( "image" ) ) //$NON-NLS-1$
			{
				if ( node.getFirstChild( ) instanceof Element )
				{
					processNodes( (Element) node.getFirstChild( ), cssStyles,
							content, action );
				}
			}
			else if ( node.getNodeName( ).equals( "script" ) ) //$NON-NLS-1$
			{
				continue;
			}
			else if ( node.getNodeType( ) == Node.TEXT_NODE )
			{
				ILabelContent label = content.getReportContent( )
						.createLabelContent( );
				addChild( content, label );
				label.setText( node.getNodeValue( ) );
				StyleDeclaration inlineStyle = new StyleDeclaration( content
						.getCSSEngine( ) );
				inlineStyle.setProperty( IStyle.STYLE_DISPLAY,
						CSSValueConstants.INLINE_VALUE );
				label.setInlineStyle( inlineStyle );
				if ( action != null )
				{
					label.setHyperlinkAction( action );
				}
			}
			else if ( // supportedHTMLElementTags.contains(node.getNodeName().
						// toUpperCase())
			// &&
			node.getNodeType( ) == Node.ELEMENT_NODE )
			{
				handleElement( (Element) node, cssStyles, content, action,
						++level );
			}
		}
	}

	static void handleElement( Element ele, Map cssStyles, IContent content,
			ActionContent action, int index )
	{
		IStyle cssStyle = (IStyle) cssStyles.get( ele );
		if ( cssStyle != null )
		{
			if ( "none".equals( cssStyle.getDisplay( ) ) ) //$NON-NLS-1$
			{
				// Check if the display mode is none.
				return;
			}
		}

		String lTagName = ele.getTagName( ).toLowerCase( );
		if ( lTagName.equals( "a" ) ) //$NON-NLS-1$
		{
			IContainerContent container = content.getReportContent( )
					.createContainerContent( );
			addChild( content, container );
			handleStyle( ele, cssStyles, container );
			ActionContent actionContent = handleAnchor( ele, container, action );
			processNodes( ele, cssStyles, content, actionContent );
		}
		else if ( lTagName.equals( "img" ) ) //$NON-NLS-1$
		{
			outputImg( ele, cssStyles, content );
		}
		else if ( lTagName.equals( "object" ) ) //$NON-NLS-1$
		{
			outputEmbedContent( ele, cssStyles, content );
		}
		else if ( lTagName.equals( "br" ) ) //$NON-NLS-1$
		{

			ILabelContent label = content.getReportContent( )
					.createLabelContent( );
			addChild( content, label );
			label.setText( "\n" ); //$NON-NLS-1$
			StyleDeclaration inlineStyle = new StyleDeclaration( content
					.getCSSEngine( ) );
			inlineStyle.setProperty( IStyle.STYLE_DISPLAY,
					CSSValueConstants.INLINE_VALUE );
			label.setInlineStyle( inlineStyle );
		}
		else if (lTagName.equals( "ul" ) || lTagName.equals( "ol" ) )//$NON-NLS-1$
		{
			IReportContent report = content.getReportContent( );
			ITableContent table = report.createTableContent( );
			addChild( content, table );
			Column column1 = new Column( report );
			column1.setWidth( new DimensionType( 2, "em" ) );
			table.addColumn( column1 );
			column1 = new Column( report );
			table.addColumn( column1 );
			handleStyle( ele, cssStyles, table );
			processNodes( ele, cssStyles, table, action );

		}
		else if ( lTagName.equals( "li" ) //$NON-NLS-1$
				&& ele.getParentNode( ).getNodeType( ) == Node.ELEMENT_NODE )
		{
			IReportContent report = content.getReportContent( );

			IRowContent row = report.createRowContent( );
			addChild( content, row );
			handleStyle( ele, cssStyles, row );

			// fix scr 157259In PDF <li> effect is incorrect when page break
			// happens.
			// add a container to number serial, keep consistent page-break

			StyleDeclaration style = new StyleDeclaration( content
					.getCSSEngine( ) );
			style.setProperty( IStyle.STYLE_VERTICAL_ALIGN,
					CSSValueConstants.TOP_VALUE );
			style.setProperty( IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0 );
			style.setProperty( IStyle.STYLE_PADDING_LEFT, IStyle.NUMBER_0 );
			style.setProperty( IStyle.STYLE_PADDING_RIGHT, IStyle.NUMBER_0 );
			style.setProperty( IStyle.STYLE_PADDING_TOP, IStyle.NUMBER_0 );
			ICellContent orderCell = report.createCellContent( );
			orderCell.setRowSpan( 1 );
			orderCell.setColumn( 0 );
			orderCell.setColSpan( 1 );
			orderCell.setInlineStyle( style );
			addChild( row, orderCell );
			TextContent text = (TextContent) report.createTextContent( );
			addChild( orderCell, text );
			if ( ele.getParentNode( ).getNodeName( ).equals( "ol" ) ) //$NON-NLS-1$
			{
				text.setText( new Integer( index ).toString( ) + "." ); //$NON-NLS-1$
			}
			else if ( ele.getParentNode( ).getNodeName( ).equals( "ul" ) ) //$NON-NLS-1$
			{
				text.setText( new String( new char[]{'\u2022'} ) );
			}

			ICellContent childCell = report.createCellContent( );
			childCell.setRowSpan( 1 );
			childCell.setColumn( 1 );
			childCell.setColSpan( 1 );
			childCell.setInlineStyle( style );
			addChild( row, childCell );

			processNodes( ele, cssStyles, childCell, action );
		}

		else if ( lTagName.equals( "dd" ) || lTagName.equals( "dt" ) ) //$NON-NLS-1$ //$NON-NLS-2$
		{
			IContainerContent container = content.getReportContent( )
					.createContainerContent( );
			addChild( content, container );
			handleStyle( ele, cssStyles, container );

			if ( lTagName.equals( "dd" ) ) //$NON-NLS-1$
			{
				StyleDeclaration style = new StyleDeclaration( content
						.getCSSEngine( ) );
				style.setProperty( IStyle.STYLE_DISPLAY,
						CSSValueConstants.INLINE_VALUE );
				style.setProperty( IStyle.STYLE_VERTICAL_ALIGN,
						CSSValueConstants.TOP_VALUE );
				TextContent text = (TextContent) content.getReportContent( )
						.createTextContent( );
				addChild( container, text );
				if ( ele.getParentNode( ).getNodeName( ).equals( "dl" ) ) //$NON-NLS-1$
				{
					text.setText( " " ); //$NON-NLS-1$
				}
				style.setTextIndent( "2em" ); //$NON-NLS-1$
				text.setInlineStyle( style );

				IContainerContent childContainer = content.getReportContent( )
						.createContainerContent( );
				childContainer.setInlineStyle( style );
				addChild( container, childContainer );

				processNodes( ele, cssStyles, container, action );

			}
			else
			{
				processNodes( ele, cssStyles, container, action );
			}

		}
		else if ( "table".equals( lTagName ) ) //$NON-NLS-1$
		{
			TableProcessor.processTable( ele, cssStyles, content, action );
		}
		else if ( htmlBlockDisplay.contains( lTagName )
				|| htmlInlineDisplay.contains( lTagName ) )
		{
			IContainerContent container = content.getReportContent( )
					.createContainerContent( );
			handleStyle( ele, cssStyles, container );
			addChild( content, container );
			// handleStyle(ele, cssStyles, container);
			processNodes( ele, cssStyles, container, action );
		}
		else
		{
			processNodes( ele, cssStyles, content, action );
		}
	}

	/**
	 * Checks if the content inside the DOM should be escaped.
	 * 
	 * @param doc
	 *            the root of the DOM tree
	 * @return true if the content needs escaping, otherwise false.
	 */
	private static boolean checkEscapeSpace( Node doc )
	{
		String textType = null;
		if ( doc != null && doc.getFirstChild( ) != null
				&& doc.getFirstChild( ) instanceof Element )
		{
			textType = ( (Element) doc.getFirstChild( ) )
					.getAttribute( "text-type" ); //$NON-NLS-1$
			return ( !TextParser.TEXT_TYPE_HTML.equalsIgnoreCase( textType ) );
		}
		return true;
	}

	/**
	 * Outputs the A element
	 * 
	 * @param ele
	 *            the A element instance
	 */
	protected static ActionContent handleAnchor( Element ele, IContent content,
			ActionContent defaultAction )
	{
		// If the "id" attribute is not empty, then use it,
		// otherwise use the "name" attribute.
		ActionContent result = defaultAction;
		if ( ele.getAttribute( "id" ).trim( ).length( ) != 0 ) //$NON-NLS-1$
		{
			content.setBookmark( ele.getAttribute( "id" ) ); //$NON-NLS-1$
		}
		else
		{
			content.setBookmark( ele.getAttribute( "name" ) );//$NON-NLS-1$
		}

		if ( ele.getAttribute( "href" ).length( ) > 0 ) //$NON-NLS-1$
		{
			String href = ele.getAttribute( "href" ); //$NON-NLS-1$
			if ( null != href && !"".equals( href ) ) //$NON-NLS-1$
			{
				ActionContent action = new ActionContent( );
				if ( href.startsWith( "#" ) ) //$NON-NLS-1$
				{
					action.setBookmark( href.substring( 1 ) );
				}
				else
				{
					String target = ele.getAttribute( "target" );
					if ( "".equals( target ) )
					{
						target = "_blank";
					}
					action.setHyperlink( href, target );
				}
				result = action;
			}

		}
		return result;
	}

	static void handleStyle( Element ele, Map cssStyles, IContent content )
	{
		String tagName = ele.getTagName( );
		StyleDeclaration style = new StyleDeclaration( content.getCSSEngine( ) );

		if ( "font".equals( tagName ) ) //$NON-NLS-1$
		{
			String attr = ele.getAttribute( "size" ); //$NON-NLS-1$
			if ( null != attr && !"".equals( attr ) ) //$NON-NLS-1$
			{
				style.setFontSize( attr );
			}
			attr = ele.getAttribute( "color" ); //$NON-NLS-1$
			if ( null != attr && !"".equals( attr ) ) //$NON-NLS-1$
			{
				style.setColor( attr );
			}
			attr = ele.getAttribute( "face" ); //$NON-NLS-1$
			if ( null != attr && !"".equals( attr ) ) //$NON-NLS-1$
			{
				style.setFontFamily( attr );
			}
		}
		if ( htmlBlockDisplay.contains( tagName ) )
		{
			style.setDisplay( "block" ); //$NON-NLS-1$
		}
		else if ( htmlInlineDisplay.contains( tagName ) )
		{
			style.setDisplay( "inline" ); //$NON-NLS-1$
		}
		IStyle inlineStyle = (IStyle) cssStyles.get( ele );
		if ( inlineStyle != null )
		{
			style.setProperties( inlineStyle );
		}
		StyleProcessor tag2Style = StyleProcessor.getStyleProcess( tagName );
		if ( tag2Style != null )
		{
			tag2Style.process( style );
		}
		content.setInlineStyle( style );
	}

	/**
	 * Outputs the embed content. Currently only support flash.
	 * 
	 * @param ele
	 * @param cssStyles
	 * @param content
	 */
	protected static void outputEmbedContent( Element ele, Map cssStyles,
			IContent content )
	{
		String classId = ele.getAttribute( "classid" );
		if ( "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000".equalsIgnoreCase( classId ) )
		{
			outputFlash( ele, cssStyles, content );
		}
	}

	/**
	 * Outputs the flash.
	 * 
	 * @param ele
	 * @param cssStyles
	 * @param content
	 */
	protected static void outputFlash( Element ele, Map cssStyles,
			IContent content )
	{
		String src = null;
		String flashVars = null;
		NodeList list = ele.getElementsByTagName( "param" );
		for ( int i = 0; i< list.getLength( ); i++ )
		{
			Node node = list.item( i );
			if ( node instanceof Element )
			{	
				if ( "movie".equalsIgnoreCase( ( (Element) node ).getAttribute( "name" ) ) )
				{
					src = ( (Element) node ).getAttribute( "value" );
				}
				else if ( "flashvars".equalsIgnoreCase( ( (Element) node ).getAttribute( "name" ) ) )
				{
					flashVars = ( (Element) node ).getAttribute( "value" );
				}
			}
		}
		if ( src != null )
		{
			ObjectContent flash = (ObjectContent) ( (ReportContent) ( content
					.getReportContent( ) ) ).createObjectContent( );	
			flash.setExtension( ".swf" );
			flash.setMIMEType( "application/x-shockwave-flash" );
			addChild( content, flash );
			handleStyle( ele, cssStyles, flash );

			if ( !FileUtil.isLocalResource( src ) )
			{
				flash.setImageSource( IImageContent.IMAGE_URL );
				flash.setURI( src );
			}
			else
			{
				ReportDesignHandle handle = content.getReportContent( )
						.getDesign( ).getReportDesign( );
				URL url = handle.findResource( src, IResourceLocator.IMAGE,
						content.getReportContent( ).getReportContext( ) == null
								? null
								: content.getReportContent( )
										.getReportContext( ).getAppContext( ) );
				if ( url != null )
				{
					src = url.toString( );
				}
				flash.setImageSource( IImageContent.IMAGE_FILE );
				flash.setURI( src );
			}

			IForeignContent foreign = getForeignRoot( content );
			if ( null != foreign )
			{
				flash.setWidth( foreign.getWidth( ) );
				flash.setHeight( foreign.getHeight( ) );
			}
			
			if ( flashVars != null && !"".equals( flashVars ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				flash.addParam( "FlashVars", flashVars ); //$NON-NLS-1$
			}
			String alt = ele.getAttribute( "alt" );
			if ( alt != null && !"".equals( alt ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				flash.setAltText( alt ); //$NON-NLS-1$
			}
		}
	}

	private static IForeignContent getForeignRoot( IContent content )
	{
		while ( !( content instanceof IForeignContent ) )
		{
			content = (IContent) content.getParent( );
			if ( content == null )
				return null;
		}
		return (IForeignContent) content;
	}

	/**
	 * Outputs the image
	 * 
	 * @param ele
	 *            the IMG element instance
	 */
	protected static void outputImg( Element ele, Map cssStyles,
			IContent content )
	{
		String src = ele.getAttribute( "src" ); //$NON-NLS-1$
		if ( src != null )
		{
			IImageContent image = content.getReportContent( )
					.createImageContent( );
			addChild( content, image );
			handleStyle( ele, cssStyles, image );

			if ( !FileUtil.isLocalResource( src ) )
			{
				image.setImageSource( IImageContent.IMAGE_URL );
				image.setURI( src );
			}
			else
			{
				ReportDesignHandle handle = content.getReportContent( )
						.getDesign( ).getReportDesign( );
				URL url = handle.findResource( src, IResourceLocator.IMAGE,
						content.getReportContent( ).getReportContext( ) == null
								? null
								: content.getReportContent( )
										.getReportContext( ).getAppContext( ) );
				if ( url != null )
				{
					src = url.toString( );
				}
				image.setImageSource( IImageContent.IMAGE_FILE );
				image.setURI( src );
			}

			if ( null != ele.getAttribute( "width" ) && !"".equals( ele.getAttribute( "width" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setWidth( PropertyUtil.getDimensionAttribute( ele,
						"width" ) ); //$NON-NLS-1$
			}
			if ( ele.getAttribute( "height" ) != null && !"".equals( ele.getAttribute( "height" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setHeight( PropertyUtil.getDimensionAttribute( ele,
						"height" ) ); //$NON-NLS-1$
			}
			if ( ele.getAttribute( "alt" ) != null && !"".equals( ele.getAttribute( "alt" ) ) ) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setAltText( ele.getAttribute( "alt" ) ); //$NON-NLS-1$
			}
		}
	}

	protected static void addChild( IContent parent, IContent child )
	{

		if ( parent != null && child != null )
		{
			Collection children = parent.getChildren( );
			if ( !children.contains( child ) )
			{
				children.add( child );
				child.setParent( parent );
			}
		}
	}

	protected static void formalizeInlineContainer( List parentChildren,
			IContent parent, IContent content )
	{
		IStyle style = content.getStyle( );

		CSSValue display = style.getProperty( IStyle.STYLE_DISPLAY );

		if ( CSSValueConstants.INLINE_VALUE.equals( display ) )
		{

			Iterator iter = content.getChildren( ).iterator( );
			ArrayList contentChildren = new ArrayList( );
			IContainerContent clonedBlock = null;
			while ( iter.hasNext( ) )
			{
				IContent child = (IContent) iter.next( );
				boolean isContainer = child.getChildren( ).size( ) > 0;
				if ( isContainer )
				{
					formalizeInlineContainer( contentChildren, content, child );
				}
				if ( clonedBlock == null )
				{
					CSSValue childDisplay = child.getStyle( ).getProperty(
							IStyle.STYLE_DISPLAY );
					if ( CSSValueConstants.BLOCK_VALUE.equals( childDisplay ) )
					{
						IReportContent report = content.getReportContent( );
						clonedBlock = report.createContainerContent( );
						IStyle clonedStyle = report.createStyle( );
						clonedStyle.setProperties( content.getStyle( ) );
						clonedStyle.setProperty( IStyle.STYLE_DISPLAY,
								CSSValueConstants.BLOCK_VALUE );
						clonedBlock.setInlineStyle( clonedStyle );
						clonedBlock.getChildren( ).add( child );
					}
					else
					{
						if ( !isContainer )
						{
							contentChildren.add( child );
						}
					}
				}
				else
				{
					iter.remove( );
					clonedBlock.getChildren( ).add( child );
				}
			}
			content.getChildren( ).clear( );
			if ( contentChildren.size( ) > 0 )
			{
				content.getChildren( ).addAll( contentChildren );
			}

			if ( content.getChildren( ).size( ) > 0 )
			{
				parentChildren.add( content );
			}

			if ( clonedBlock != null )
			{
				parentChildren.add( clonedBlock );
			}
		}
		else
		{
			Iterator iter = content.getChildren( ).iterator( );
			ArrayList newChildren = new ArrayList( );
			while ( iter.hasNext( ) )
			{
				IContent child = (IContent) iter.next( );
				boolean isContainer = child.getChildren( ).size( ) > 0;
				if ( isContainer )
				{
					formalizeInlineContainer( newChildren, content, child );

				}
				else
				{
					newChildren.add( child );
				}
			}
			content.getChildren( ).clear( );
			if ( newChildren.size( ) > 0 )
			{
				content.getChildren( ).addAll( newChildren );
				parentChildren.add( content );
			}

		}
	}

	public static void main( String[] args )
	{
		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent block = createBlockContent(
		 * report ); root.getChildren( ).add( block ); IContent inlineContent =
		 * createInlineContent( report ); block.getChildren( ).add(
		 * createBlockContent( report ) ); block.getChildren( ).add(
		 * inlineContent ); block.getChildren( ).add( createInlineContent(
		 * report ) ); inlineContent.getChildren( ).add( createInlineContent(
		 * report ) ); inlineContent.getChildren( ).add( createBlockContent(
		 * report ) ); inlineContent.getChildren( ).add( createInlineContent(
		 * report ) ); ArrayList list = new ArrayList( );
		 */

		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent inline = createInlineContent(
		 * report ); root.getChildren( ).add( inline ); IContent inlineContent =
		 * createInlineContent( report ); inlineContent.getChildren( ).add(
		 * createInlineContent( report ) ); inline.getChildren( ).add(
		 * inlineContent ); ArrayList list = new ArrayList( );
		 */

		/*
		 * ReportContent report = new ReportContent( ); IContent root =
		 * createBlockContent( report ); IContent inline = createInlineContent(
		 * report ); root.getChildren( ).add( inline ); IContent inlineContent =
		 * createInlineContent( report ); inline.getChildren( ).add(
		 * inlineContent ); inline.getChildren( ).add( createBlockContent(
		 * report ) ); ArrayList list = new ArrayList( );
		 */

		ReportContent report = new ReportContent( );
		IContent root = createBlockContent( report );
		IContent inline = createInlineContent( report );
		root.getChildren( ).add( inline );
		IContent inlineContent = createInlineContent( report );
		inline.getChildren( ).add( inlineContent );
		inline.getChildren( ).add( createBlockContent( report ) );
		ArrayList list = new ArrayList( );

		formalizeInlineContainer( list, root, inline );
		root.getChildren( ).clear( );
		if ( list.size( ) > 0 )
		{
			root.getChildren( ).addAll( list );
		}
		int i = 0;
	}

	protected static IContent createInlineContent( ReportContent report )
	{
		IContent content = report.createContainerContent( );
		content.getStyle( ).setProperty( IStyle.STYLE_DISPLAY,
				CSSValueConstants.INLINE_VALUE );
		return content;
	}

	protected static IContent createBlockContent( ReportContent report )
	{
		IContent content = report.createContainerContent( );
		content.getStyle( ).setProperty( IStyle.STYLE_DISPLAY,
				CSSValueConstants.BLOCK_VALUE );
		return content;
	}

}
