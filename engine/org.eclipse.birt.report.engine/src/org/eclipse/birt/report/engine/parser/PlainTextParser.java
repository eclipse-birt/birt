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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

/**
 * Parses the content of plain text to get the DOM tree.
 * 
 * <p>
 * During parsing the plain text, only the control codes '\r','\n' or '\r\n'
 * that format the layout are converted to "p" element in HTML and others are
 * preserved without change to be passed on to Emitter for outputting.
 * <p>
 * After parsing, the DOM tree has a <code>Document</code> node that has a
 * <code>Element</code> child node whose tag name is body. All other nodes
 * that need to be processed to output are the descendant nodes of "body" node.
 * 
 * 
 * @version $Revision: #1 $ $Date: 2005/01/21 $
 */
public class PlainTextParser
{

	/**
	 * logger used to log syntax errors.
	 */
	protected static Log logger = LogFactory.getLog( PlainTextParser.class );

	/**
	 * Parses the character stream to get the DOM tree.
	 * 
	 * @param reader
	 *            the Reader for providing the character stream
	 * @return DOM tree whose top element node is named "body" if no error
	 *         exists, otherwise null.
	 */
	private Document parsePlainText( Reader reader )
	{
		try
		{
			Document doc = DocumentBuilderFactory.newInstance( )
					.newDocumentBuilder( ).newDocument( );
			Element body = doc.createElement( "body" );
			doc.appendChild( body );

			LineNumberReader lReader = new LineNumberReader( reader );
			String content;
			//Only the control code that formats the layout is done here and
			// others are preserved without change to be passed on to Emitter
			// for processing.			
			while ( ( content = lReader.readLine( ) ) != null )
			{
				appendChild( doc, body, content );
				body.appendChild( doc.createElement( "br" ) );
			}
			//Removes the last BR element.
			//TODO The last BR element is removed even if there is a CR/LF
			// control code at the end of the file.
			if ( body.getLastChild( ) != null )
			{
				body.removeChild( body.getLastChild( ) );
			}
			return doc;
		}
		catch ( Exception e )
		{
			logger.error( e );
		}
		return null;
	}

	/**
	 * Appends the child text node if the content is not empty.
	 * 
	 * @param doc
	 *            Document node for creating other node
	 * @param body
	 *            the appending node
	 * @param buf
	 *            the content of the appended node
	 */
	private void appendChild( Document doc, Node body, String buf )
	{
		if ( buf.length( ) > 0 )
		{
			Text textNode = doc.createTextNode( buf );
			body.appendChild( textNode );
		}
	}

	/**
	 * Parses the plain text to get the DOM tree
	 * 
	 * @param text
	 *            the plain text
	 * @return DOM tree if no error exists, otherwise null.
	 */
	Document parsePlainText( String text )
	{
		return parsePlainText( new StringReader( text ) );
	}

	/**
	 * Parses the plain text to get the DOM tree
	 * 
	 * @param in
	 *            the plain text input stream
	 * @return DOM tree if no error exists,otherwise null.
	 */
	Document parsePlainText( InputStream in )
	{
		try
		{
			return parsePlainText( new BufferedReader( new InputStreamReader(
					in, "UTF-8" ) ) );
		}
		catch ( UnsupportedEncodingException e )
		{
			logger.error( e );
		}
		return null;
	}
}