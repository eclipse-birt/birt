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

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.w3c.tidy.Tidy;

/**
 * Parse the content of text according to the HTML specification to get the DOM
 * tree.
 * <p>
 * After parsing, the DOM tree has a <code>Document</code> node that has a
 * <code>Element</code> child node whose tag name is body. All other nodes
 * that need to be processed to output are the descendant nodes of "body" node.
 * <p>
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class HTMLTextParser
{

	/**
	 * logger used to log syntax errors.
	 */
	protected static Log logger = LogFactory.getLog( HTMLTextParser.class );

	/** Supported tags in HTML */
	protected static HashSet supportedTags = new HashSet( );
	/**
	 * Tidy instance
	 */
	protected Tidy tidy = new Tidy( );
	/**
	 * Initializes and sets configuration
	 */
	static
	{
		supportedTags.add( "a" );
		supportedTags.add( "b" );
		//		supportedTags.add("BODY");
		supportedTags.add( "br" );
		supportedTags.add( "center" );
		supportedTags.add( "code" );
		supportedTags.add( "dd" );
		supportedTags.add( "del" );
		supportedTags.add( "div" );
		supportedTags.add( "dl" );
		supportedTags.add( "dt" );
		supportedTags.add( "font" );
		supportedTags.add( "em" );
		//		supportedTags.add("HEAD");
		//		supportedTags.add("HN");
		//		supportedTags.add( "html" );
		supportedTags.add( "i" );
		supportedTags.add( "image" );
		supportedTags.add( "img" );
		supportedTags.add( "ins" );
		supportedTags.add( "li" );
		supportedTags.add( "ol" );
		supportedTags.add( "pre" );
		supportedTags.add( "p" );
		supportedTags.add( "span" );
		supportedTags.add( "strong" );
		supportedTags.add( "sub" );
		supportedTags.add( "sup" );
		//		supportedTags.add("TITLE");
		supportedTags.add( "ul" );
		supportedTags.add( "tt" );
		supportedTags.add( "u" );
		supportedTags.add( "value-of" );
	}
	/** For heading level */
	private static Pattern hn = Pattern.compile( "h[\\d]" );

	/**
	 * Constructor
	 *  
	 */
	public HTMLTextParser( )
	{
		try
		{
			Properties props = new Properties( );
			props.load( getClass( ).getResourceAsStream(
					"htmlparser.properties" ) );

			tidy.setConfigurationFromProps( props );
		}
		catch ( Exception ex )
		{
			logger.fatal( ex );
		}
	}

	/**
	 * Parse the HTML input stream.
	 * 
	 * @param in
	 *            the HTML input stream
	 * @return created DOM tree, null if any error exists.
	 */
	public Document parseHTML( InputStream in )
	{
		assert in != null;

		Document doc = tidy.parseDOM( in, null );
		Document desDoc = null;
		try
		{
			desDoc = DocumentBuilderFactory.newInstance( ).newDocumentBuilder( )
					.newDocument( );
			//After parsing with JTidy,normally the children nodes of the root
			// are
			// HTML entity, HTML element and comments node. And The children
			// nodes of the
			// element HTML are Head element and Body element. Only Body element
			// and its descendant nodes are preserved.
			//Entities in raw html are converted to text.
			Node html = getNodeByName( doc, "html" );
			Node body = null;
			if ( html != null )
			{
				body = getNodeByName( html, "body" );
			}
			//			doc.getLastChild( ).getLastChild( );
			Node desBody = desDoc.createElement( "body" );
			desDoc.appendChild( desBody );
			if ( body != null )
			{
				copyNode( body, desBody );
			}
		}
		catch ( ParserConfigurationException e )
		{
			logger.error( e );
			return null;
		}
		return desDoc;
	}

	/**
	 * Retrieves the child node by name
	 * 
	 * @param parent
	 *            the parent node
	 * @param childName
	 *            the name of the child node to retrieve
	 * @return null if such node does not exist, otherwise return the specified
	 *         node.
	 */
	private Node getNodeByName( Node parent, String childName )
	{
		for ( Node child = parent.getFirstChild( ); child != null; child = child
				.getNextSibling( ) )
		{
			if ( child.getNodeType( ) == Node.ELEMENT_NODE
					&& childName.equals( child.getNodeName( ) ) )
			{
				return child;
			}
		}
		return null;
	}

	/**
	 * Remove the unsupported tags and convert the JTidy DOM tree to W3C DOM
	 * tree recursively.
	 * 
	 * @param srcNode
	 *            the Node in the JTidy DOM tree
	 * @param desNode
	 *            the Node in the W3c DOM tree
	 * @see org.w3c.dom.Node
	 */
	private void copyNode( Node srcNode, Node desNode )
	{
		assert srcNode != null && desNode != null;

		//In the definition of <code>org.w3c.dom.Node<code>, there are 12 kinds
		// of nodes. Here only process the text, attribute and element types.
		for ( Node child = srcNode.getFirstChild( ); child != null; child = child
				.getNextSibling( ) )
		{
			//The child node is a text node or cdata section, and create it and
			// return
			if ( child.getNodeType( ) == Node.TEXT_NODE
					|| child.getNodeType( ) == Node.CDATA_SECTION_NODE )
			{
				Text txtNode = desNode.getOwnerDocument( ).createTextNode(
						child.getNodeValue( ) );
				desNode.appendChild( txtNode );
			}
			//The child node is an element node. If it is supported, then
			// create it and call this method on the child node recursively. If
			// it is unsupported, then skip it and call this method recursively.
			else if ( child.getNodeType( ) == Node.ELEMENT_NODE )
			{
				boolean bSupported = false;
				if ( supportedTags.contains( child.getNodeName( ) ) )
				{
					bSupported = true;
				}
				//Check if it is a heading level
				else if ( hn.matcher( child.getNodeName( ) ).matches( ) )
				{
					bSupported = true;
				}
				if ( bSupported )
				{
					//copy the element node
					Element ele = null;
					ele = desNode.getOwnerDocument( ).createElement(
							child.getNodeName( ) );

					// copy the attributes
					for ( int i = 0; i < child.getAttributes( ).getLength( ); i++ )
					{
						Node attr = child.getAttributes( ).item( i );
						ele.setAttribute( attr.getNodeName( ), attr
								.getNodeValue( ) );
					}

					desNode.appendChild( ele );
					copyNode( child, ele );
				}
				else
				{
					copyNode( child, desNode );
				}
			}
		}
	}
}