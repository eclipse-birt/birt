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

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Output the content following the XML specification. Only when the events of
 * endding the writer and closing the tag come, the stream is flushed.
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class XMLWriter
{

	/** logger */
	protected Log log = LogFactory.getLog( XMLWriter.class );

	/** the print writer for outputting */
	protected PrintWriter printWriter;

	/** character encoding */
	protected String encoding = "UTF-8"; //$NON-NLS-1$

	/** the indent count */
	protected int indentCount;

	/** whether or not the tag is paired */
	protected boolean bPairedFlag = true;

	/** whether or not the content is indented. */
	protected boolean bIndent = true;

	/**
	 * whether or not using implicit closing tag.
	 */
	protected boolean bImplicitCloseTag = true;

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

	public void setLogger( Log log )
	{
		this.log = log;
	}

	public void open( OutputStream outputStream, String encoding )
	{
		assert ( encoding != null );
		assert ( outputStream != null );

		this.encoding = encoding;
		try
		{
			this.printWriter = new PrintWriter( new OutputStreamWriter(
					outputStream, encoding ), true );
		}
		catch ( UnsupportedEncodingException e )
		{
			log.error( "the character encoding " + encoding //$NON-NLS-1$
					+ " unsupported !" ); //$NON-NLS-1$
		}
	}

	public void open( OutputStream outputStream )
	{
		open( outputStream, "UTF-8" ); //$NON-NLS-1$
	}

	public void close( )
	{
		this.printWriter.close( );
	}

	/**
	 * Output the xml header
	 */
	public void startWriter( )
	{
		printWriter.print( "<?xml version=\"1.0\" encoding=\"" + encoding //$NON-NLS-1$
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
			printWriter.print( '>' );
			if ( bIndent )
			{
				printWriter.println( );
			}
		}
		printWriter.flush( );
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
			printWriter.print( '>' );
		}
		if ( bIndent )
		{
			printWriter.println( );
		}
		bPairedFlag = false;
		printWriter.print( indent( ) + '<' + tagName );
		indentCount++;
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
				printWriter.print( "/>" ); //$NON-NLS-1$
			}
			else
			{
				printWriter.print( "></" + tagName + '>' ); //$NON-NLS-1$
			}
		}
		else
		{
			if ( bIndent )
			{
				printWriter.println( );
			}
			printWriter.print( indent( ) + "</" + tagName + '>' ); //$NON-NLS-1$
		}
		bPairedFlag = true;
		printWriter.flush( );
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
			printWriter.print( ' ' );
			printWriter.print( attrName );
			printWriter.print( "=\"" ); //$NON-NLS-1$
			printWriter.print( getEscapedStr( attrValue, false ) );
			printWriter.print( '\"' );
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
		if ( attrValue != null && attrValue.toString( ).length( ) > 0 )
		{
			printWriter.print( ' ' );
			printWriter.print( attrName );
			printWriter.print( "=\"" ); //$NON-NLS-1$
			printWriter.print( getEscapedStr( attrValue.toString( ), false ) );
			printWriter.print( '\"' );
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
		printWriter.print( ' ' + attrName
				+ "=\"" + Float.toString( attrValue ) + '\"' ); //$NON-NLS-1$ 
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
		printWriter.print( ' ' + attrName
				+ "=\"" + Double.toString( attrValue ) + '\"' ); //$NON-NLS-1$
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
		printWriter.print( ' ' + attrName + "=\""
				+ Integer.toString( attrValue ) + '\"' ); //$NON-NLS-1$
	}

	/**
	 * Output the encoded content
	 * 
	 * @param value
	 *            the content
	 */
	public void text( String value )
	{
		text( value, false );
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
	public void text( String value, boolean whitespace )
	{
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}
		indentCount++;
		if ( !bPairedFlag )
		{
			printWriter.print( '>' );
		}
		if ( bIndent )
		{
			printWriter.println( );
		}
		bPairedFlag = true;
		printWriter.print( indent( ) + getEscapedStr( value, whitespace ) );
		indentCount--;
	}

	/**
	 * the content does not need to be encoded
	 * 
	 * @param value
	 *            the literal content
	 */
	public void literal( String value )
	{
		printWriter.print( value );
	}

	/**
	 * @param value
	 */
	public void style( String name, String value )
	{
		assert name != null && name.length( ) > 0;
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}

		if ( !bPairedFlag )
		{
			printWriter.print( '>' );
			printWriter.println( );
			bPairedFlag = true;
		}

		indentCount++;
		printWriter.print( indent( ) );
		printWriter.print( '.' );
		printWriter.print( name );
		printWriter.print( " {" ); //$NON-NLS-1$
		printWriter.print( value );
		printWriter.print( '}' );
		printWriter.println( );
		indentCount--;
	}

	/**
	 * Get the indent string
	 * 
	 * @return the indent content
	 */
	private String indent( )
	{
		if ( !bIndent )
		{
			return "";
		}
		StringBuffer indentContent = new StringBuffer( 64 );
		for ( int i = 0; i < indentCount; i++ )
		{
			indentContent.append( '\t' ); //$NON-NLS-1$
		}
		return indentContent.toString( );
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
	protected String getEscapedStr( String s, boolean whitespace )
	{
		StringBuffer result = null;
		for ( int i = 0, max = s.length( ), delta = 0; i < max; i++ )
		{
			char c = s.charAt( i );
			String replacement = null;
			if ( c == '&' )
			{
				replacement = "&amp;"; //$NON-NLS-1$
			}
			else if ( c == '<' )
			{
				replacement = "&lt;"; //$NON-NLS-1$
			}
			else if ( c == '\r' )
			{
				replacement = "&#13;"; //$NON-NLS-1$
			}
			else if ( c == '>' )
			{
				replacement = "&gt;"; //$NON-NLS-1$
			}
			else if ( c == '"' )
			{
				replacement = "&quot;"; //$NON-NLS-1$
			}
			else if ( c == '\'' )
			{
				replacement = "&apos;"; //$NON-NLS-1$
			}
			else if ( whitespace && c == ' ' )
			{
				replacement = "&#160;"; //$NON-NLS-1$
			}
			else if ( c >= 0x80 )
			{
				replacement = "&#x" + Integer.toHexString( c ) + ';'; //$NON-NLS-1$ 
			}
			if ( replacement != null )
			{
				if ( result == null )
				{
					result = new StringBuffer( s );
				}
				result.replace( i + delta, i + delta + 1, replacement );
				delta += ( replacement.length( ) - 1 );
			}
		}
		if ( result == null )
		{
			return s;
		}
		return result.toString( );
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
}