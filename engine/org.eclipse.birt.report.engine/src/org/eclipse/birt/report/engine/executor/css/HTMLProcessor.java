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

package org.eclipse.birt.report.engine.executor.css;

import java.io.StringReader;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.impl.TextItemContent;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.engine.util.FileUtil;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Converts the deprecated element according to the HTML 4.0 specification and
 * parses the style attribute of the HTML element.
 * 
 * @version $Revision: 1.8 $ $Date: 2005/04/27 05:35:36 $
 */
public class HTMLProcessor
{

	/** the logger */
	private static Logger logger = Logger.getLogger( HTMLProcessor.class.getName() );

	/** the execution context */
	ExecutionContext context;

	/** the CSS2.0 Parser */
	private CssParser cssParser;

	/** the possible values for property SIZE of HTML element FONT */
	private static String[] FONT_SIZE = new String[]{"7.5pt", "8.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"10pt", "12pt",  //$NON-NLS-1$ //$NON-NLS-2$
			"14.4pt", "19pt",  //$NON-NLS-1$//$NON-NLS-2$
			"23pt", "36pt"};  //$NON-NLS-1$//$NON-NLS-2$
//	private static String[] FONT_SIZE = new String[]{"xx-small", "x-small", //$NON-NLS-1$ //$NON-NLS-2$
//			"small", "medium",  //$NON-NLS-1$ //$NON-NLS-2$
//			"large", "x-large",  //$NON-NLS-1$//$NON-NLS-2$
//			"xx-large", "xxx-large"};  //$NON-NLS-1$//$NON-NLS-2$

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
		cssParser = new CssParser( new StringReader( "" ) ); //$NON-NLS-1$
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
	public void execute( Element ele, TextItemContent text )
	{
		HashMap cssStyle = null;
		if ( !ele.hasAttribute( "style" ) ) //$NON-NLS-1$
		{
			cssStyle = new HashMap( );
		}
		else
		{
			cssParser.ReInit( new StringReader( ele.getAttribute( "style" ) ) ); //$NON-NLS-1$
			cssParser.setCssStatement( ele.getAttribute( "style" ) ); //$NON-NLS-1$
			try
			{
				cssParser.parse( );
			}
			catch ( Exception e )
			{
				logger.log(Level.SEVERE,"The css statement is:" //$NON-NLS-1$
						+ ele.getAttribute( "style" ), e ); //$NON-NLS-1$
			}
			cssStyle = cssParser.getCssProperties( );
			ele.removeAttribute( "style" ); //$NON-NLS-1$
			//If the background image is a local resource, then get its global
			// URI.
			String src = (String) cssStyle.get( "background-image" ); //$NON-NLS-1$
			if ( src != null )
			{
				//The resource is surrounded with "url(" and ")", or "\"", or
				// "\'". Removes them.
				if ( src.startsWith( "url(" ) && src.length( ) > 5 ) //$NON-NLS-1$
				{
					src = src.substring( 4, src.length( ) - 1 );
				}
				else if ( ( src.startsWith( "\"" ) || src.startsWith( "\'" ) ) //$NON-NLS-1$ //$NON-NLS-2$
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
					cssStyle.put( "background-image", src ); //$NON-NLS-1$
				}
				else
				{
					//If the resource does not exist, then removes this item.
					cssStyle.remove( "background-image" ); //$NON-NLS-1$
				}
			}
		}

		//FOR HTML 4.0 COMPATIBILITY
		if ( "b".equals( ele.getTagName( ) ) ) //$NON-NLS-1$
		{
			addToStyle( cssStyle, "font-weight", "bold" );  //$NON-NLS-1$//$NON-NLS-2$
			//Re-points to the element node in the tree
			ele = replaceElement( ele, "span" ); //$NON-NLS-1$
		}
		else if ( "center".equals( ele.getTagName( ) ) ) //$NON-NLS-1$
		{
			addToStyle( cssStyle, "text-align", "center" ); //$NON-NLS-1$ //$NON-NLS-2$
			ele = replaceElement( ele, "div" ); //$NON-NLS-1$
		}
		else if ( "font".equals( ele.getTagName( ) ) ) //$NON-NLS-1$
		{
			addToStyle( cssStyle, "color", ele.getAttribute( "color" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			addToStyle( cssStyle, "font-family", ele.getAttribute( "face" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			if ( ele.hasAttribute( "size" ) ) //$NON-NLS-1$
			{
				try
				{
					int size = Integer.parseInt( ele.getAttribute( "size" ) ); //$NON-NLS-1$
					addToStyle( cssStyle, "font-size", FONT_SIZE[size - 1] ); //$NON-NLS-1$
				}
				catch ( Exception e )
				{
				    logger.log(Level.SEVERE, "There is a invalid value for property SIZE of element FONT in the HTML." ); //$NON-NLS-1$
				}
			}
			//Removes these attributes to avoid for being copied again.
			ele.removeAttribute( "color" ); //$NON-NLS-1$
			ele.removeAttribute( "face" ); //$NON-NLS-1$
			ele.removeAttribute( "size" ); //$NON-NLS-1$
			ele = replaceElement( ele, "span" ); //$NON-NLS-1$
		}
		else if ( "i".equals( ele.getTagName( ) ) ) //$NON-NLS-1$
		{
			addToStyle( cssStyle, "font-style", "italic" ); //$NON-NLS-1$ //$NON-NLS-2$
			ele = replaceElement( ele, "span" ); //$NON-NLS-1$
		}
		else if ( "u".equals( ele.getTagName( ) ) ) //$NON-NLS-1$
		{
			String decoration = (String) cssStyle.get( "text-decoration" ); //$NON-NLS-1$
			//The property "text-decoration" is made of more than one token.
			if ( decoration != null && decoration.indexOf( "underline" ) == -1 //$NON-NLS-1$
					&& decoration.indexOf( "none" ) == -1 //$NON-NLS-1$
					&& decoration.indexOf( "inherit" ) == -1 ) //$NON-NLS-1$
			{
				decoration = decoration + " underline"; //$NON-NLS-1$
			}
			else if ( decoration == null )
			{
				decoration = "underline"; //$NON-NLS-1$
			}
			cssStyle.put( "text-decoration", decoration ); //$NON-NLS-1$
			ele = replaceElement( ele, "span" ); //$NON-NLS-1$
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