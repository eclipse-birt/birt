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

package org.eclipse.birt.report.engine.executor.css;

import java.io.StringReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Converts the deprecated element according to the HTML 4.0 specification and
 * parses the style attribute of the HTML element.
 * 
 * @version $Revision: 1.4 $ $Date: 2005/02/25 06:02:24 $
 */
public class HTMLProcessor
{

	/** the logger */
	private Logger logger = Logger.getLogger( HTMLProcessor.class.getName() );

	/** the execution context */
	ExecutionContext context;

	/** the CSS2.0 Parser */
	private CssParser cssParser;

	/** the possible values for property SIZE of HTML element FONT */
	private static String[] FONT_SIZE = new String[]{"xx-small", "x-small",
			"small", "medium", "large", "x-large", "xx-large", "xxx-large"};

	/**
	 * Constructor
	 * 
	 * @param context
	 *            the execution context
	 */
	public HTMLProcessor( ExecutionContext context )
	{
		this.context = context;
		//Takes the zero-length string as parameter just for keeping to the
		// interface of constructor
		cssParser = new CssParser( new StringReader( "" ) );
	}

	/**
	 * Parses the style attribute of the element node and converts the
	 * deprecated element node in HTML 4.0, and calls it on its children element
	 * nodes recursively
	 * 
	 * @param ele
	 *            the element node in the DOM tree
	 * @param text
	 *            the text content object
	 */
	public void execute( Element ele, ITextContent text )
	{
		HashMap cssStyle = null;
		if ( !ele.hasAttribute( "style" ) )
		{
			cssStyle = new HashMap( );
		}
		else
		{
			cssParser.ReInit( new StringReader( ele.getAttribute( "style" ) ) );
			cssParser.setCssStatement( ele.getAttribute( "style" ) );
			try
			{
				cssParser.parse( );
			}
			catch ( Exception e )
			{
				logger.log(Level.SEVERE,"The css statement is:"
						+ ele.getAttribute( "style" ), e );
			}
			cssStyle = cssParser.getCssProperties( );
			ele.removeAttribute( "style" );
			//If the background image is a local resource, then get its global
			// URI.
			String src = (String) cssStyle.get( "background-image" );
			if ( src != null )
			{
				//The resource is surrounded with "url(" and ")", or "\"", or
				// "\'". Removes them.
				if ( src.startsWith( "url(" ) && src.length( ) > 5 )
				{
					src = src.substring( 4, src.length( ) - 1 );
				}
				else if ( ( src.startsWith( "\"" ) || src.startsWith( "\'" ) )
						&& src.length( ) > 2 )
				{
					src = src.substring( 1, src.length( ) - 1 );
				}
				//Checks if the resource is local
				if ( FileUtil.isLocalResource( src ) )
				{
					src = FileUtil.getAbsolutePath( context.getReport( )
							.getBasePath( ), src );
					// src = FileUtil.getURI( src );
				}
				if ( src != null )
				{
					//Puts the modified URI of the resource
					cssStyle.put( "background-image", src );
				}
				else
				{
					//If the resource does not exist, then removes this item.
					cssStyle.remove( "background-image" );
				}
			}
		}

		//FOR HTML 4.0 COMPATIBILITY
		if ( "b".equals( ele.getTagName( ) ) )
		{
			addToStyle( cssStyle, "font-weight", "bold" );
			//Re-points to the element node in the tree
			ele = replaceElement( ele, "span" );
		}
		else if ( "center".equals( ele.getTagName( ) ) )
		{
			addToStyle( cssStyle, "text-align", "center" );
			ele = replaceElement( ele, "div" );
		}
		else if ( "font".equals( ele.getTagName( ) ) )
		{
			addToStyle( cssStyle, "color", ele.getAttribute( "color" ) );
			addToStyle( cssStyle, "font-family", ele.getAttribute( "face" ) );
			if ( ele.hasAttribute( "size" ) )
			{
				try
				{
					int size = Integer.parseInt( ele.getAttribute( "size" ) );
					addToStyle( cssStyle, "font-size", FONT_SIZE[size - 1] );
				}
				catch ( Exception e )
				{
				    logger.log(Level.SEVERE, "There is a invalid value for property SIZE of element FONT in the HTML." );
				}
			}
			//Removes these attributes to avoid for being copied again.
			ele.removeAttribute( "color" );
			ele.removeAttribute( "face" );
			ele.removeAttribute( "size" );
			ele = replaceElement( ele, "span" );
		}
		else if ( "i".equals( ele.getTagName( ) ) )
		{
			addToStyle( cssStyle, "font-style", "italic" );
			ele = replaceElement( ele, "span" );
		}
		else if ( "u".equals( ele.getTagName( ) ) )
		{
			String decoration = (String) cssStyle.get( "text-decoration" );
			//The property "text-decoration" is made of more than one token.
			if ( decoration != null && decoration.indexOf( "underline" ) == -1
					&& decoration.indexOf( "none" ) == -1
					&& decoration.indexOf( "inherit" ) == -1 )
			{
				decoration = decoration + " underline";
			}
			else if ( decoration == null )
			{
				decoration = "underline";
			}
			cssStyle.put( "text-decoration", decoration );
			ele = replaceElement( ele, "span" );
		}
		text.addCssStyle( ele, cssStyle );

		//Walks on its children nodes recursively
		for ( int i = 0; i < ele.getChildNodes( ).getLength( ); i++ )
		{
			Node child = ele.getChildNodes( ).item( i );
			if ( child.getNodeType( ) == Node.ELEMENT_NODE )
			{
				execute( (Element) child, text );
			}
		}
	}

	/**
	 * Replaces the previous element with the new tag name in the same position
	 * and return it
	 * 
	 * @param oldEle
	 *            the replaced element
	 * @param tag
	 *            the tag name of the new HTML element
	 * @return the new HTML element
	 */
	private Element replaceElement( Element oldEle, String tag )
	{
		Element newEle = oldEle.getOwnerDocument( ).createElement( tag );
		//Copies the attributes
		for ( int i = 0; i < oldEle.getAttributes( ).getLength( ); i++ )
		{
			String attrName = oldEle.getAttributes( ).item( i ).getNodeName( );
			newEle.setAttribute( attrName, oldEle.getAttribute( attrName ) );
		}
		//Copies the children nodes
		//Note: After the child node is moved to another parent node, then
		// relationship between it and its sibling is removed. So here calls
		// <code>Node.getFirstChild()</code>again and again till it is null.
		for ( Node child = oldEle.getFirstChild( ); child != null; child = oldEle
				.getFirstChild( ) )
		{
			newEle.appendChild( child );
		}
		oldEle.getParentNode( ).replaceChild( newEle, oldEle );
		return newEle;
	}

	/**
	 * Adds the attribute name and value to the style if attribute value is not
	 * null and a zero-length string and the added attribute is not in the
	 * style.
	 * 
	 * @param style
	 *            the style attribute for HTML element
	 * @param attrName
	 *            the added attribute name
	 * @param attrValue
	 *            the added attribute value
	 */

	private void addToStyle( HashMap style, String attrName, String attrValue )
	{
		if ( attrValue == null || attrValue.trim( ).length( ) == 0 )
		{
			return;
		}
		if ( style.get( attrName ) == null )
		{
			style.put( attrName, attrValue );
		}
	}
}