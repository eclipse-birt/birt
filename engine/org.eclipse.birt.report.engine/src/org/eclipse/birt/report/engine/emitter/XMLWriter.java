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

package org.eclipse.birt.report.engine.emitter;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Output the content following the XML specification. Only when the events of
 * endding the writer, the stream is flushed explictly.
 * 
 */
public class XMLWriter
{

	/** logger */
	protected static Logger log = Logger.getLogger( XMLWriter.class.getName( ) );

	protected static final int MAX_BUFFER_SIZE = 1024;
	protected char[] buffer = new char[MAX_BUFFER_SIZE];
	protected int bufferSize;

	/** the print writer for outputting */
	protected Writer writer;

	/** character encoding */
	protected String encoding = "UTF-8"; //$NON-NLS-1$

	/** the indent count */
	protected int indentCount;

	/** whether or not the tag is paired */
	protected boolean bPairedFlag = true;

	/** whether or not we have text before the end tag */
	protected boolean bText = false;

	/** whether or not the content is indented. */
	protected boolean bIndent = true;

	/**
	 * whether or not using implicit closing tag.
	 */
	protected boolean bImplicitCloseTag = true;
	
	protected boolean enableCompactMode = false;

	/**
	 * Constructor
	 * 
	 * create a XML writer without the stream associated with it.
	 * 
	 * To use such a XML writer, you must open it first.
	 * 
	 * @param encoding
	 *            character encoding
	 * @param outputStream
	 *            outputStream for outputting
	 */
	public XMLWriter( )
	{
	}

	public void open( OutputStream outputStream, String encoding )
	{
		assert ( encoding != null );
		assert ( outputStream != null );

		this.encoding = encoding;
		try
		{
			writer = new OutputStreamWriter( outputStream, encoding );
		}
		catch ( UnsupportedEncodingException e )
		{
			log.log( Level.SEVERE,
					"the character encoding {0} unsupported !", encoding ); //$NON-NLS-1$
		}
	}

	public void open( OutputStream outputStream )
	{
		open( outputStream, "UTF-8" ); //$NON-NLS-1$
	}

	public void close( )
	{
		flush( );
		try
		{
			writer.close( );
		}
		catch ( IOException ex )
		{
			log.log( Level.SEVERE, ex.getMessage( ) );
		}
	}

	/**
	 * Output the xml header
	 */
	public void startWriter( )
	{
		print( "<?xml version=\"1.0\" encoding=\"" + encoding //$NON-NLS-1$
				+ "\"?>" ); //$NON-NLS-1$
	}

	/**
	 * the event of endding the writer
	 */
	public void endWriter( )
	{
		// it should not happen but test case
		if ( !bPairedFlag )
		{
			print( '>' );
			if ( bIndent )
			{
				println( );
			}
		}
		flush( );
	}

	/**
	 * @return Returns the encoding.
	 */
	public String getEncoding( )
	{
		return encoding;
	}

	/**
	 * Open the tag
	 * 
	 * @param tagName
	 *            tag name
	 */
	public void openTag( String tagName )
	{
		if ( !bPairedFlag )
		{
			print( '>' );
		}
		if ( bIndent )
		{
			println( );
			print( indent( ) );
		}
		bPairedFlag = false;
		print( '<' );
		print( tagName );
		indentCount++;
		bText = false;
	}

	/**
	 * Close the tag
	 * 
	 * @param tagName
	 *            tag name
	 */
	public void closeTag( String tagName )
	{
		indentCount--;
		if ( !bPairedFlag )
		{
			if ( bImplicitCloseTag )
			{
				print( "/>" ); //$NON-NLS-1$
			}
			else
			{
				print( "></" );
				print( tagName );
				print( '>' ); //$NON-NLS-1$
			}
		}
		else
		{	
		  //Ignoring formatting for a tag </a> because additional hidden symbols before a tag </a> are underlined on a html page
			if ( bIndent && !bText && !HTMLTags.TAG_A.equals(tagName))
			{
				println( );
				print( indent( ) );
			}
			print( "</" );
			print( tagName );
			print( '>' ); //$NON-NLS-1$
		}
		bPairedFlag = true;
		bText = false;
	}

	/**
	 * Output the attribute whose value is not null
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attribute( String attrName, String attrValue )
	{
		if ( attrValue != null && attrValue.length( ) > 0 )
		{
			print( ' ' );
			print( attrName );
			print( "=\"" ); //$NON-NLS-1$
			print( encodeAttr( attrValue ) );
			print( '\"' );
		}
	}

	/**
	 * Output the attribute whose value is not null but can be ""
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attributeAllowEmpty( String attrName, String attrValue )
	{
		if ( attrValue != null )
		{
			print( ' ' );
			print( attrName );
			print( "=\"" ); //$NON-NLS-1$
			print( encodeAttr( attrValue ) );
			print( '\"' );
		}
	}

	/**
	 * Output the attribute when the attrValue is not null
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attribute( String attrName, Object attrValue )
	{
		if ( attrValue != null )
		{
			attribute( attrName, attrValue.toString( ) );
		}
	}

	/**
	 * Output the attribute
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attribute( String attrName, float attrValue )
	{
		print( ' ' + attrName + "=\"" + Float.toString( attrValue ) + '\"' ); //$NON-NLS-1$ 
	}

	/**
	 * Output the attribute
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attribute( String attrName, double attrValue )
	{
		print( ' ' + attrName + "=\"" + Double.toString( attrValue ) + '\"' ); //$NON-NLS-1$
	}

	/**
	 * Output the attribute
	 * 
	 * @param attrName
	 *            attribute name
	 * @param attrValue
	 *            attribute value
	 */
	public void attribute( String attrName, int attrValue )
	{
		print( ' ' + attrName + "=\"" //$NON-NLS-1$
				+ Integer.toString( attrValue ) + '\"' ); //$NON-NLS-1$
	}

	/**
	 * Output the encoded content
	 * 
	 * @param value
	 *            the content
	 * @param whiteespace
	 *            A
	 *            <code>boolean<code> indicating if the white space character should be converted or not.
	 */
	public void text( String value )
	{
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}
		if ( !bPairedFlag )
		{
			print( '>' );
			bPairedFlag = true;
		}

		String stringToPrint = encodeText( value );
		print( stringToPrint );
		bText = true;
	}

	public void cdata( String value )
	{
		if ( !bPairedFlag )
		{
			print( '>' );
			bPairedFlag = true;
		}
		String text = encodeCdata( value );
		print( text );
		if ( bPairedFlag )
		{
			bText = true;
		}
	}

	/**
	 * the content does not need to be encoded
	 * 
	 * @param value
	 *            the literal content
	 */
	public void literal( String value )
	{
		print( value );
	}

	private static String[] INDENTS = new String[]{
			"",
			"\t",
			"\t\t",
			"\t\t\t",
			"\t\t\t\t",
			"\t\t\t\t\t",
			"\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"};

	/**
	 * Get the indent string
	 * 
	 * @return the indent content
	 */
	protected String indent( )
	{
		if ( enableCompactMode )
		{
			return INDENTS[0];
		}
		if ( indentCount < INDENTS.length )
		{
			return INDENTS[indentCount];
		}
		return INDENTS[INDENTS.length - 1];
	}

	/**
	 * Replace the escape character
	 * 
	 * @param s
	 *            The string needs to be replaced.
	 * @param whiteespace
	 *            A
	 *            <code>boolean<code> value indicating if the white space character should be converted or not.
	 * @return the replaced string
	 */
	protected String encodeText( String s )
	{
		return XMLEncodeUtil.encodeText( s );
	}

	/**
	 * Replaces the escape character in attribute value.
	 * 
	 * @param s
	 *            The string needs to be replaced.
	 * @return the replaced string
	 */
	protected String encodeAttr( String s )
	{
		return XMLEncodeUtil.encodeAttr( s );
	}

	protected String encodeCdata( String s )
	{
		return XMLEncodeUtil.encodeCdata( s );
	}

	/**
	 * @return Returns the indent.
	 */
	public boolean isIndent( )
	{
		return bIndent;
	}

	/**
	 * @param indent
	 *            The indent to set.
	 */
	public void setIndent( boolean indent )
	{
		this.bIndent = indent;
	}

	/**
	 * @return the enableCompactMode
	 */
	public boolean isEnableCompactMode( )
	{
		return enableCompactMode;
	}

	
	/**
	 * @param enableCompactMode
	 *            the enableCompactMode to set
	 */
	public void setEnableCompactMode( boolean enableCompactMode )
	{
		this.enableCompactMode = enableCompactMode;
	}

	/**
	 * @return Returns the bImplicitCloseTag.
	 */
	public boolean isImplicitCloseTag( )
	{
		return bImplicitCloseTag;
	}

	/**
	 * @param bImplicitCloseTag
	 *            The bImplicitCloseTag to set.
	 */
	public void setImplicitCloseTag( boolean bImplicitCloseTag )
	{
		this.bImplicitCloseTag = bImplicitCloseTag;
	}

	public void print( String s )
	{
		int length = s.length( );
		if ( bufferSize + length >= MAX_BUFFER_SIZE )
		{
			try
			{
				writer.write( buffer, 0, bufferSize );
				writer.write( s );
				bufferSize = 0;
			}
			catch ( IOException ex )
			{
				log.log( Level.SEVERE, ex.getMessage( ) );
			}
		}
		else
		{
			s.getChars( 0, length, buffer, bufferSize );
			bufferSize += length;
		}
	}

	public void println( )
	{
		if ( !enableCompactMode )
		{
			print( '\n' );
		}
	}

	public void println( String s )
	{
		print( s );
		println( );
	}

	public void print( char c )
	{
		if ( bufferSize >= MAX_BUFFER_SIZE )
		{
			flush( );
		}
		buffer[bufferSize++] = c;
	}

	protected void flush( )
	{
		if ( bufferSize > 0 )
		{
			try
			{
				writer.write( buffer, 0, bufferSize );
			}
			catch ( IOException ex )
			{
				log.log( Level.SEVERE, ex.getMessage( ) );
			}
			bufferSize = 0;
		}
	}
}