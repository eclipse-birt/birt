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

import java.io.BufferedWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
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

	/** the print writer for outputting */
	protected PrintWriter printWriter;

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
			/*
			 * disable the autoflush, I remember there is some bug about the the
			 * autoflush, but just close it for performance.
			 */
			this.printWriter = new PrintWriter( new BufferedWriter(
					new OutputStreamWriter( outputStream, encoding ) ), false );
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
			printWriter.print('\n' );
			printWriter.print( indent( ) );
		}
		bPairedFlag = false;
		printWriter.print( '<');
		printWriter.print( tagName );
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
				printWriter.print( "/>" ); //$NON-NLS-1$
			}
			else
			{
				printWriter.print( "></");
				printWriter.print( tagName);
				printWriter.print( '>' ); //$NON-NLS-1$
			}
		}
		else
		{
			if ( bIndent && !bText )
			{
				printWriter.print( '\n' );
				printWriter.print( indent( ) );
			}
			printWriter.print( "</");
			printWriter.print( tagName);
			printWriter.print( '>' ); //$NON-NLS-1$
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
			printWriter.print( ' ' );
			printWriter.print( attrName );
			printWriter.print( "=\"" ); //$NON-NLS-1$
			printWriter.print( encodeAttr( attrValue ) );
			printWriter.print( '\"' );
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
			printWriter.print( ' ' );
			printWriter.print( attrName );
			printWriter.print( "=\"" ); //$NON-NLS-1$
			printWriter.print( encodeAttr( attrValue ) );
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
		printWriter.print( ' ' + attrName + "=\"" //$NON-NLS-1$
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
			printWriter.print( '>' );
			bPairedFlag = true;
		}

		String stringToPrint = encodeText( value );
		printWriter.print( stringToPrint );
		bText = true;
	}

	public void cdata( String value )
	{
		if ( !bPairedFlag )
		{
			printWriter.print( '>' );
			bPairedFlag = true;
		}
		String text = encodeCdata( value );
		printWriter.print( text );
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
		printWriter.print( value );
	}

	private static String[] indents = new String[]{
			"",
			"\t",
			"\t\t",
			"\t\t\t",
			"\t\t\t\t",
			"\t\t\t\t\t",
			"\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t",
			"\t\t\t\t\t\t\t\t"
	};
	
	/**
	 * Get the indent string
	 * 
	 * @return the indent content
	 */
	protected String indent( )
	{
		if ( !bIndent )
		{
			return ""; //$NON-NLS-1$
		}
		if (indentCount < 8)
		{
			return indents[indentCount];
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
}