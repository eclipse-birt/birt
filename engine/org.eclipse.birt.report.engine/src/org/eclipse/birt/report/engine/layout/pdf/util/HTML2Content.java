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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;

import org.eclipse.birt.report.engine.content.IContainerContent;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IForeignContent;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.engine.content.impl.ContainerContent;
import org.eclipse.birt.report.engine.content.impl.ImageContent;
import org.eclipse.birt.report.engine.content.impl.LabelContent;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.content.impl.TextContent;
import org.eclipse.birt.report.engine.css.dom.StyleDeclaration;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.eclipse.birt.report.engine.ir.EngineIRConstants;
import org.eclipse.birt.report.engine.parser.TextParser;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.css.CSSValue;



public class HTML2Content
{
	protected static final HashMap tag2Style = new HashMap( );
	
	protected static final HashSet htmlDisplayMode = new HashSet( );
	
	protected static final HashSet supportedHTMLElementTags = new HashSet();
	
	protected static final HashMap textTypeMapping = new HashMap( );
	
	protected ActionContent action = null;
	
	protected Stack inlineContainerStack = new Stack();

	protected ReportDesignHandle report;
	
	static
	{
		tag2Style.put( "code", //$NON-NLS-1$
				"font-family: monospace"); //$NON-NLS-1$
		
		tag2Style.put( "em", //$NON-NLS-1$
				"font-style: italic"); //$NON-NLS-1$
		tag2Style.put( "h1", //$NON-NLS-1$
			"font-size: 2em; margin-top: 0.67em; margin-bottom:0.67em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
		tag2Style.put( "h2", //$NON-NLS-1$
			"font-size: 1.5em; margin-top: 0.75em; margin-bottom:0.75em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
		tag2Style.put( "h3", //$NON-NLS-1$
			"font-size: 1.17em; margin-top: 0.83em; margin-bottom:0.83em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
	
		tag2Style.put( "h4", //$NON-NLS-1$
			"font-size: 1.12em; margin-top: 1.12em; margin-bottom:1.12em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
	
		tag2Style.put( "h5", //$NON-NLS-1$
			"font-size: 0.83em; margin-top: 1.5em; margin-bottom:1.5em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
	
		tag2Style.put( "h6", //$NON-NLS-1$
			"font-size: 0.75em; margin-top: 1.67em; margin-bottom:1.67em; font-weight: bold; page-break-after: avoid"); //$NON-NLS-1$
		tag2Style.put( "pre", //$NON-NLS-1$
				"font-family: monospace; white-space: no-wrap; "); //$NON-NLS-1$
		tag2Style.put( "strong", //$NON-NLS-1$
				"font-weight: bold"); //$NON-NLS-1$
		tag2Style.put( "sub", //$NON-NLS-1$
				"vertical-align: bottom; font-size: 75%"); //$NON-NLS-1$
		
		tag2Style.put( "sup", //$NON-NLS-1$
			"vertical-align: top; font-size: 75%"); //$NON-NLS-1$
		tag2Style.put( "tt", //$NON-NLS-1$
				"font-family: monospace;"); //$NON-NLS-1$
		tag2Style.put( "center", //$NON-NLS-1$
			"text-align: center;"); //$NON-NLS-1$
		tag2Style.put( "i", //$NON-NLS-1$
			"font-style: italic;"); //$NON-NLS-1$
		
		tag2Style.put( "b", //$NON-NLS-1$
			"font-weight: bold;"); //$NON-NLS-1$
		tag2Style.put( "p", //$NON-NLS-1$
		"margin-top: 1.33em; margin-bottom: 1.33em"); //$NON-NLS-1$
		
		tag2Style.put( "u", //$NON-NLS-1$
		"text-decoration: underline;"); //$NON-NLS-1$
		
		tag2Style.put( "del", //$NON-NLS-1$
		"text-decoration: line-through;"); //$NON-NLS-1$
		
		supportedHTMLElementTags.add("H1"); //$NON-NLS-1$
		supportedHTMLElementTags.add("H2"); //$NON-NLS-1$
		supportedHTMLElementTags.add("H3"); //$NON-NLS-1$
		supportedHTMLElementTags.add("H4"); //$NON-NLS-1$
		supportedHTMLElementTags.add("H5"); //$NON-NLS-1$
		supportedHTMLElementTags.add("H6"); //$NON-NLS-1$
		supportedHTMLElementTags.add("A"); //$NON-NLS-1$
		supportedHTMLElementTags.add("B"); //$NON-NLS-1$
		supportedHTMLElementTags.add("BODY"); //$NON-NLS-1$
		supportedHTMLElementTags.add("BR"); //$NON-NLS-1$
		supportedHTMLElementTags.add("CENTER"); //$NON-NLS-1$
		supportedHTMLElementTags.add("CODE"); //$NON-NLS-1$
		supportedHTMLElementTags.add("DD"); //$NON-NLS-1$
		supportedHTMLElementTags.add("DEL"); //$NON-NLS-1$
		supportedHTMLElementTags.add("DIV"); //$NON-NLS-1$
		supportedHTMLElementTags.add("DL"); //$NON-NLS-1$
		supportedHTMLElementTags.add("DT"); //$NON-NLS-1$
		supportedHTMLElementTags.add("FONT"); //$NON-NLS-1$
		supportedHTMLElementTags.add("EM"); //$NON-NLS-1$
		supportedHTMLElementTags.add("HEAD"); //$NON-NLS-1$
		supportedHTMLElementTags.add("HTML"); //$NON-NLS-1$
		supportedHTMLElementTags.add("I"); //$NON-NLS-1$
		supportedHTMLElementTags.add("IMAGE"); //$NON-NLS-1$
		supportedHTMLElementTags.add("IMG"); //$NON-NLS-1$
		supportedHTMLElementTags.add("INS"); //$NON-NLS-1$
		supportedHTMLElementTags.add("LI"); //$NON-NLS-1$
		supportedHTMLElementTags.add("OL"); //$NON-NLS-1$
		supportedHTMLElementTags.add("PRE"); //$NON-NLS-1$
		supportedHTMLElementTags.add("P"); //$NON-NLS-1$
		supportedHTMLElementTags.add("SPAN"); //$NON-NLS-1$
		supportedHTMLElementTags.add("STRONG"); //$NON-NLS-1$
		supportedHTMLElementTags.add("SUB"); //$NON-NLS-1$
		supportedHTMLElementTags.add("SUP"); //$NON-NLS-1$
		supportedHTMLElementTags.add("TITLE"); //$NON-NLS-1$
		supportedHTMLElementTags.add("UL"); //$NON-NLS-1$
		supportedHTMLElementTags.add("TT"); //$NON-NLS-1$
		supportedHTMLElementTags.add("U"); //$NON-NLS-1$
		
		
		
		//block-level elements
		htmlDisplayMode.add( "dd" ); //$NON-NLS-1$
		htmlDisplayMode.add( "div" ); //$NON-NLS-1$
		htmlDisplayMode.add( "dl" ); //$NON-NLS-1$
		htmlDisplayMode.add( "dt" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h1" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h2" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h3" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h4" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h5" ); //$NON-NLS-1$
		htmlDisplayMode.add( "h6" ); //$NON-NLS-1$
		htmlDisplayMode.add( "hr" ); //$NON-NLS-1$
		htmlDisplayMode.add( "ol" ); //$NON-NLS-1$
		htmlDisplayMode.add( "p" ); //$NON-NLS-1$
		htmlDisplayMode.add( "pre" ); //$NON-NLS-1$
		htmlDisplayMode.add( "ul" ); //$NON-NLS-1$
		htmlDisplayMode.add( "li" ); //$NON-NLS-1$
		htmlDisplayMode.add( "body" ); //$NON-NLS-1$
		htmlDisplayMode.add( "center" ); //$NON-NLS-1$
		
		textTypeMapping.put( IForeignContent.HTML_TYPE, TextParser.TEXT_TYPE_HTML );
		textTypeMapping.put( IForeignContent.TEXT_TYPE, TextParser.TEXT_TYPE_PLAIN );
		textTypeMapping.put( IForeignContent.UNKNOWN_TYPE, TextParser.TEXT_TYPE_AUTO );

	}
	
	public HTML2Content(ReportDesignHandle report)
	{
		this.report = report;
	}
	
	public void html2Content( IForeignContent foreign)
	{
		processForeignData(foreign);
	}
	
	protected void processForeignData( IForeignContent foreign)
	{
		
		if(foreign.getChildren( )!=null && foreign.getChildren( ).size( )>0)
		{
			return;
		}
		
		HashMap styleMap = new HashMap( );
		HTMLStyleProcessor htmlProcessor = new HTMLStyleProcessor( report );
		Object rawValue = foreign.getRawValue();
		Document doc = null;
		if ( null != rawValue )
		{
			doc = new TextParser( ).parse( foreign.getRawValue( ).toString( ), 
					( String )textTypeMapping.get( foreign.getRawType( ) ) );	
		}
		
		Element body = null;
		if ( doc != null )
		{
			Node node = doc.getFirstChild( );
			//The following must be true
			if ( node instanceof Element )
			{
				body = (Element) node;
			}
		}
		if ( body != null )
		{
			htmlProcessor.execute( body, styleMap );
			IContainerContent container = new ContainerContent((ReportContent)foreign.getReportContent());
			
			// no style will be applied to <body>.
			// <body> is block.
			addChild(foreign, container);
			processNodes( body, checkEscapeSpace( doc ), styleMap, container );
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
	 * 			  the parent content of the element
	 * 		       
	 */
	private void processNodes( Element ele, boolean needEscape, HashMap cssStyles, IContent content )
	{
		int level=0;
		for ( Node node = ele.getFirstChild( ); node != null; node = node
				.getNextSibling( ) )
		{
			if ( node.getNodeName( ).equals( "value-of" ) ) //$NON-NLS-1$
			{
				if ( node.getFirstChild( ) instanceof Element )
				{
					processNodes( (Element) node.getFirstChild( ), checkEscapeSpace( node ), cssStyles, content );
				}
			}
			else if ( node.getNodeName( ).equals( "image" ) ) //$NON-NLS-1$
			{
				if ( node.getFirstChild( ) instanceof Element )
				{
					processNodes( (Element) node.getFirstChild( ), needEscape, cssStyles, content);
				}
			}
			else if(node.getNodeType() == Node.TEXT_NODE)
			{
				ILabelContent label = new LabelContent((ReportContent)content.getReportContent());
				addChild(content, label);
				label.setText(node.getNodeValue());
				StyleDeclaration inlineStyle = new StyleDeclaration(content.getCSSEngine());
				inlineStyle.setProperty( IStyle.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE );
				//support del, ins and u
				Node pNode = node.getParentNode();
				if(pNode!=null)
				{
					if("u".equalsIgnoreCase(pNode.getNodeName()) || "ins".equalsIgnoreCase(pNode.getNodeName()))  //$NON-NLS-1$//$NON-NLS-2$
					{
						inlineStyle.setProperty( IStyle.STYLE_TEXT_UNDERLINE, IStyle.UNDERLINE_VALUE );
					}
					else if("del".equalsIgnoreCase(pNode.getNodeName())) //$NON-NLS-1$
					{
						
						inlineStyle.setProperty( IStyle.STYLE_TEXT_LINETHROUGH, IStyle.LINE_THROUGH_VALUE );
					}
					else if("sub".equalsIgnoreCase(pNode.getNodeName())) //$NON-NLS-1$
					{
						inlineStyle.setProperty( IStyle.STYLE_VERTICAL_ALIGN, IStyle.BOTTOM_VALUE );
					}
					else if("sup".equalsIgnoreCase(pNode.getNodeName())) //$NON-NLS-1$
					{
						inlineStyle.setProperty( IStyle.STYLE_VERTICAL_ALIGN, IStyle.TOP_VALUE);
					}
				}
				label.setInlineStyle(inlineStyle);
				if(action!=null)
				{
					label.setHyperlinkAction(action);
				}
				
			}
			else if(supportedHTMLElementTags.contains(node.getNodeName().toUpperCase()) && node.getNodeType()== Node.ELEMENT_NODE)
			{
				handleElement((Element)node, needEscape, cssStyles, content, ++level);
				
			}
		}
	}
	
	private void handleElement(Element ele, boolean needEscape, HashMap cssStyles, IContent content, int index)
	{
		IStyle cssStyle = ( IStyle ) cssStyles.get( ele );
		if ( cssStyle != null )
		{
			if ( "none".equals( cssStyle.getDisplay() ) ) //$NON-NLS-1$
			{
				//Check if the display mode is none.
				return;
			}
		}
		
		String tagName = ele.getTagName();
		if ( tagName.toLowerCase().equals( "a" ) ) //$NON-NLS-1$
		{
			IContainerContent container = new ContainerContent((ReportContent)content.getReportContent());
			setInlineParent( content, container);
			handleStyle(ele, cssStyles, container);
			ActionContent oldAction = action;
			handleAnchor( ele, container );
			inlineContainerStack.push( container );
			processNodes( ele, needEscape, cssStyles, content );
			inlineContainerStack.pop();
			this.action = oldAction;
		}
		else if(tagName.toLowerCase().equals( "img" )) //$NON-NLS-1$
		{
			outputImg( ele, cssStyles, content);
		}
		else if ( tagName.toLowerCase().equals( "br" ) ) //$NON-NLS-1$
		{
			
			ILabelContent label = new LabelContent((ReportContent)content.getReportContent());
			addChild(content, label);
			label.setText("\n"); //$NON-NLS-1$
			StyleDeclaration inlineStyle = new StyleDeclaration(content.getCSSEngine());
			inlineStyle.setProperty( IStyle.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE );
			label.setInlineStyle( inlineStyle );
		}
		else if (tagName.toLowerCase().equals("li") //$NON-NLS-1$
				&& ele.getParentNode().getNodeType() == Node.ELEMENT_NODE)
		{
			StyleDeclaration style = new StyleDeclaration(content.getCSSEngine());
			style.setProperty( IStyle.STYLE_DISPLAY, CSSValueConstants.BLOCK_VALUE );
			style.setProperty( IStyle.STYLE_VERTICAL_ALIGN, CSSValueConstants.MIDDLE_VALUE );
			IContainerContent container = new ContainerContent((ReportContent)content.getReportContent());
			container.setInlineStyle(style);
			addChild(content, container);
			handleStyle(ele, cssStyles, container);
			
			//fix scr  157259In PDF <li> effect is incorrect when page break happens.
			//add a container to number serial, keep consistent page-break
			style = new StyleDeclaration(content.getCSSEngine());
			style.setProperty( IStyle.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE );
			style.setProperty( IStyle.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE );
			
			IContainerContent orderContainer = new ContainerContent((ReportContent)content.getReportContent());
			CSSValue fontSizeValue = content.getComputedStyle( ).getProperty( IStyle.STYLE_FONT_SIZE ); 
			orderContainer.setWidth( new DimensionType(2.1*PropertyUtil.getDimensionValue( fontSizeValue )/1000.0, EngineIRConstants.UNITS_PT) );
			orderContainer.setInlineStyle( style );
			addChild(container, orderContainer);
			TextContent text = new TextContent((ReportContent)content.getReportContent());
			addChild(orderContainer, text);
			if(ele.getParentNode().getNodeName().equals("ol")) //$NON-NLS-1$
			{
				text.setText(new Integer(index).toString()+"."); //$NON-NLS-1$
			}
			else if(ele.getParentNode().getNodeName().equals("ul")) //$NON-NLS-1$
			{
				text.setText(new String(new char[]{'\u2022'}));
			}

			text.setInlineStyle(style);
			
			IContainerContent childContainer = new ContainerContent((ReportContent)content.getReportContent());
			addChild(container, childContainer);
			childContainer.setInlineStyle(style);
			processNodes( ele, needEscape, cssStyles, childContainer );
		}
		else if (tagName.toLowerCase().equals("dd") || tagName.toLowerCase().equals("dt")) //$NON-NLS-1$ //$NON-NLS-2$
		{
			IContainerContent container = new ContainerContent((ReportContent)content.getReportContent());
			addChild(content, container);
			handleStyle(ele, cssStyles, container);
			
			if (tagName.toLowerCase().equals("dd")) //$NON-NLS-1$
			{
				StyleDeclaration style = new StyleDeclaration(content.getCSSEngine());
				style.setProperty( IStyle.STYLE_DISPLAY, CSSValueConstants.INLINE_VALUE );
				style.setProperty( IStyle.STYLE_VERTICAL_ALIGN, CSSValueConstants.TOP_VALUE );
				TextContent text = new TextContent((ReportContent) content
						.getReportContent());
				addChild(content, text);
				if (ele.getParentNode().getNodeName().equals("dl")) //$NON-NLS-1$
				{
					text.setText(""); //$NON-NLS-1$
				}
				style.setTextIndent("3em"); //$NON-NLS-1$
				text.setInlineStyle(style);
				
				IContainerContent childContainer = new ContainerContent((ReportContent)content.getReportContent());
				childContainer.setInlineStyle(style);
				addChild(container, childContainer);
				
				processNodes( ele, needEscape, cssStyles, container );
				
			}
			else
			{
				processNodes(ele, needEscape, cssStyles, container);
			}
			
		}
		else 
		{
			IContainerContent container = new ContainerContent((ReportContent)content.getReportContent());
			handleStyle(ele, cssStyles, container);
			if(htmlDisplayMode.contains(ele.getTagName()))
			{
				addChild(content, container);
				processNodes( ele, needEscape, cssStyles, container );
			}
			else
			{
				setInlineParent(content, container);
				//handleStyle(ele, cssStyles, container);
				inlineContainerStack.push( container );
				processNodes( ele, needEscape, cssStyles, content );
				inlineContainerStack.pop( );
			}
			
		}
	}
	
	/**
	 * Checks if the content inside the DOM should be escaped.
	 * 
	 * @param doc
	 *            the root of the DOM tree
	 * @return true if the content needs escaping, otherwise false.
	 */
	private boolean checkEscapeSpace( Node doc )
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
	protected void handleAnchor( Element ele, IContent content )
	{
		//			If the "id" attribute is not empty, then use it,
		// otherwise use the "name" attribute.
		if ( ele.getAttribute( "id" ).trim( ).length( ) != 0 ) //$NON-NLS-1$
		{
			content.setBookmark(ele.getAttribute( "id" )); //$NON-NLS-1$
		}
		else
		{
			content.setBookmark(ele.getAttribute( "name" ));//$NON-NLS-1$
		}
		
		if ( ele.getAttribute( "href" ).length( ) > 0 ) //$NON-NLS-1$
		{
			String href = ele.getAttribute( "href" ); //$NON-NLS-1$
			if(null!=href && !"".equals(href)) //$NON-NLS-1$
			{
				ActionContent action = new ActionContent();
				if(href.startsWith("#")) //$NON-NLS-1$
				{
					action.setBookmark(href.substring(1));
				}
				else
				{
					String target = ele.getAttribute("target");
					if ("".equals(target))
					{
						target = "_blank";
					}
					action.setHyperlink(href, target);
				}
				this.action = action;
			}
			
		}
	}
	
	private void handleStyle(Element ele, HashMap cssStyles, IContent content)
	{
		String tagName = ele.getTagName();
		StyleDeclaration style = new StyleDeclaration(content.getCSSEngine());
		
		if("font".equals(tagName)) //$NON-NLS-1$
		{
			String attr = ele.getAttribute("size"); //$NON-NLS-1$
			if(null!=attr && !"".equals(attr)) //$NON-NLS-1$
			{
				style.setFontSize(attr);
			}
			attr = ele.getAttribute("color"); //$NON-NLS-1$
			if(null!=attr && !"".equals(attr)) //$NON-NLS-1$
			{
				style.setColor(attr);
			}
			attr = ele.getAttribute("face"); //$NON-NLS-1$
			if(null!=attr && !"".equals(attr)) //$NON-NLS-1$
			{
				style.setFontFamily(attr);
			}
		}
		if(htmlDisplayMode.contains(tagName))
		{
			style.setDisplay("block"); //$NON-NLS-1$
		}
		else
		{
			style.setDisplay("inline"); //$NON-NLS-1$
		}
		IStyle inlineStyle = (IStyle)cssStyles.get(ele);
		if(inlineStyle!=null)
		{
			style.setProperties(inlineStyle);
		}
		if(tag2Style.containsKey(ele.getTagName()))
		{
			StyleDeclaration tagStyle = (StyleDeclaration)content.getCSSEngine().parseStyleDeclaration((String)tag2Style.get(ele.getTagName()));
			if(tagStyle!=null)
			{
				style.setProperties(tagStyle);
				
			}
		}
		content.setInlineStyle(style);
	}

	/**
	 * Outputs the image
	 * 
	 * @param ele
	 *            the IMG element instance
	 */
	protected void outputImg( Element ele, HashMap cssStyles, IContent content )
	{
	 	String src = ele.getAttribute( "src" ); //$NON-NLS-1$
		if(src!=null)
		{		
			IImageContent image = new ImageContent(content);
			addChild(content, image);
			handleStyle(ele, cssStyles, image);
			
			if( !FileUtil.isLocalResource( src ) )
			{
				image.setImageSource(IImageContent.IMAGE_URL);
				image.setURI(src);
			}
			else
			{
				ReportDesignHandle handle = content.getReportContent( ).getDesign( ).getReportDesign( );
				URL url = handle.findResource( src, IResourceLocator.IMAGE );
				if(url!=null)
				{
					src = url.toString( );
				}
				image.setImageSource(IImageContent.IMAGE_FILE);
				image.setURI(src);
			}
			
			if(null!=ele.getAttribute("width") && !"".equals(ele.getAttribute("width"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setWidth(DimensionType.parserUnit(ele.getAttribute("width"))); //$NON-NLS-1$
			}
			if(ele.getAttribute("height")!=null &&! "".equals(ele.getAttribute("height"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setWidth(DimensionType.parserUnit(ele.getAttribute("height"))); //$NON-NLS-1$
			}
			if(ele.getAttribute("alt")!=null && !"".equals(ele.getAttribute("alt"))) //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			{
				image.setAltText(ele.getAttribute("alt")); //$NON-NLS-1$
			}
			
			
		}
	}

	protected void addChild(IContent parent, IContent child)
	{
		if(parent!=null && child!=null)
		{
			Collection children = parent.getChildren( );
			if(!children.contains( child ))
			{
				children.add( child );
				if(inlineContainerStack.isEmpty( ))
				{
					child.setParent( parent );
				}
				else
				{
					child.setParent( ( IContent) inlineContainerStack.peek( ));
				}
			}
		}
	}
	
	protected void setInlineParent(IContent parent, IContent child)
	{
		if(parent!=null && child!=null)
		{
			if(inlineContainerStack.isEmpty( ))
			{
				child.setParent( parent );
			}
			else
			{
				child.setParent( ( IContent) inlineContainerStack.peek( ));
			}
		}
	}
	
	
}
