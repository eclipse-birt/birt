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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;

/**
 * Parses the content of a text-related item.
 * <p>
 * During parsing a plain text string, only control codes 
 * '\r', '\n', '\r\n' '\n\r' are converted to "p" element in HTML,
 * space chars are treated as non-breaking space, i.e., &nbsp; tab
 * characters are treated as spaces, and others characters are 
 * preserved without change to be passed on to Emitter for outputting.
 * <p>
 * After parsing, the root of the DOM tree is a <code>Document</code> node 
 * with an <code>Element</code> child node whose tag name is body. All 
 * other nodes that need to be processed are descendant nodes of "body" 
 * node.
 * <p>
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class TextParser
{
    public static String TEXT_TYPE_AUTO 	= "auto"; 	// $NON-NLS-1$ 
    public static String TEXT_TYPE_PLAIN = "plain"; 	// $NON-NLS-1$
    public static String TEXT_TYPE_HTML 	= "html"; 	// $NON-NLS-1$
    public static String TEXT_TYPE_RTF 	= "rtf"; 	// $NON-NLS-1$
    
    public static String HTML_PREFIX = "<html>";		// $NON-NLS-1$
    public static String RTF_PREFIX = "\\rtf";			// $NON-NLS-1$
    
	/**
	 * logs syntax errors.
	 */
	protected static Log logger = LogFactory.getLog( TextParser.class );

	/**
	 * Parse the input text to get a DOM tree.
	 * 
	 * @param text the text to be parsed
	 * @param textType the text type (case-insensitive). Valid types includes
	 *            auto,plain,html. If null, it is regarded as auto; if set to any
	 *            other value, treat the text as plain text.
	 * @return DOM tree if no error exists,otherwise null.
	 */
	public Document parse( String text, String textType )
	{
		// Handle null case
		if ( text == null || text.length( ) == 0 )
			return null;
		
		//If the type is null or auto, resets the text type based on the content prefix
		if ( null == textType || TEXT_TYPE_AUTO.equalsIgnoreCase( textType ) )
		{
			int index = 0;
			int len = text.length( );
			
			// remove white spaces
			while ( index < len && Character.isWhitespace( text.charAt( index ) ) )
			{
				index++;
			}
			
			// Checks if the first six characters are "<html>"
			if ( ( len - index ) >= 6 && text.substring( index, index + 6 ).equalsIgnoreCase(HTML_PREFIX ) )
				textType = TEXT_TYPE_HTML;
			else if ( ( len - index ) >= 4 && text.substring( index, index + 4 ).equalsIgnoreCase(RTF_PREFIX ) )
				textType = TEXT_TYPE_RTF;
			else
				textType = TEXT_TYPE_PLAIN;		// Assume plain text in any other cases
		}

		if ( TEXT_TYPE_HTML.equalsIgnoreCase( textType ) )
		{
			try
			{
				//Convert input string to an input stream because JTidy accepts a stream only
				return new HTMLTextParser( ).parseHTML( new ByteArrayInputStream(text.getBytes( "UTF-8" ) ) );
			}
			catch ( UnsupportedEncodingException e )
			{
				logger.error( e );
				return null;
			}
		}
		else if ( TEXT_TYPE_RTF.equalsIgnoreCase( textType ) )
		{
		    assert false;		// Not supported yet
		    return null;
		}
		else 
		{
		    if ( !TEXT_TYPE_PLAIN.equalsIgnoreCase( textType ) )
		        logger.warn( "Invalid text type. The content is treated as plain text." );
			return new PlainTextParser( ).parsePlainText( text );		    			
		}
	}

	/**
	 * Parse the input stream to get a DOM tree.
	 * 
	 * @param in the input stream
	 * @param textType the text type (case-insensitive). Valid types includes
	 *            auto,plain,html. If null, it is regarded as auto; if set to any
	 *            other value, treat the text as plain text.
	 * @return DOM tree if no error exists,otherwise null.
	 */
	public Document parse( InputStream in, String textType )
	{
		// Handle the null case
		if ( in == null )
			return null;
		
		InputStream tmpInputStream = in;
		
		//If the type is null or auto, resets the text type based on the content prefix
		if ( null == textType || TEXT_TYPE_AUTO.equalsIgnoreCase( textType ) )
		{
			StringBuffer buf = new StringBuffer( );
			int chr;
			try
			{
				// Skips the white space
				while ( ( chr = in.read( ) ) != -1 && Character.isWhitespace( (char) chr ) )
					buf.append( (char) chr );

				// Reads the next (up-to) six characters
				for ( int headLen = 0; headLen < 6 && chr != -1; headLen++ )
				{
					buf.append( (char) chr );
					chr = in.read( );
				}
				
				//Checks the type of text
				if ( buf.toString( ).toLowerCase( ).startsWith( HTML_PREFIX ) )
					textType = TEXT_TYPE_HTML;
				else if ( buf.toString( ).toLowerCase( ).startsWith( RTF_PREFIX ) )
					textType = TEXT_TYPE_RTF;
				else
				    textType = TEXT_TYPE_PLAIN;
				
				if ( chr != -1 )
					buf.append( (char) chr );
				
				// Pushes back the characters that are read for text type detection
				byte[] head = buf.toString( ).getBytes( );
				PushbackInputStream pin = new PushbackInputStream( in,
						head.length );
				//Push back these bytes to the stream to ensure that the stream
				// is complete.
				pin.unread( head, 0, head.length );
				tmpInputStream = pin;
			}
			catch ( IOException e )
			{
				logger.error( e );
				return null;
			}
		}
		
		if ( TEXT_TYPE_HTML.equalsIgnoreCase( textType ) )
			return new HTMLTextParser( ).parseHTML( tmpInputStream );
		else if (TEXT_TYPE_RTF.equals( textType ))
		{
		    assert false; // not supported
		    return null;
		}
		else
		{		
		    if ( !TEXT_TYPE_PLAIN.equalsIgnoreCase( textType ) )
		        logger.warn( "Invalid text type. The content is treated as plain text." );
	        // All other types are considered as the plain text.
	        return new PlainTextParser( ).parsePlainText( tmpInputStream );
		}
	}
}