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

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.birt.report.model.util.XMLWriter;

/**
 * 
 */
public class DocumentXMLWriter extends XMLWriter
{

	/**
	 * 
	 * @param outputFile
	 * @param signature
	 * @throws IOException
	 */
	public DocumentXMLWriter( File outputFile, String signature )
			throws IOException
	{
		super( outputFile, signature );
		markLineNumber = false;
	}

	/**
	 * 
	 * @param os
	 * @param signature
	 * @throws IOException
	 */
	public DocumentXMLWriter( OutputStream os, String signature )
			throws IOException
	{
		super( os, signature );
		markLineNumber = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLWriter#attribute(java.lang.String,
	 * java.lang.String)
	 */
	public void attribute( String attrName, String value )
	{
		if ( value == null )
			return;
		checkAttribute( );
		assert elementActive;
		out.print( " " ); //$NON-NLS-1$ 
		out.print( attrName );
		out.print( "=\"" ); //$NON-NLS-1$ 

		// Scan the string character-by-character to look for non-ASCII
		// characters that must be hex encoded.

		int len = value.length( );
		for ( int i = 0; i < len; i++ )
		{
			char c = value.charAt( i );

			if ( c == '&' )
				out.print( "&amp;" ); //$NON-NLS-1$ 
			else if ( c == '<' )
				out.print( "&lt;" ); //$NON-NLS-1$ 
			else if ( c == '"' )
				out.print( "&quot;" ); //$NON-NLS-1$
			else if ( c < 0x20 )
			{
				out.print( "&#x" ); //$NON-NLS-1$ 
				out.print( Integer.toHexString( c ) );
				out.print( ';' );
			}
			else
				out.print( c );
		}
		out.print( "\"" ); //$NON-NLS-1$ 
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLWriter#emitStartTag(java.lang.String
	 * )
	 */
	protected void emitStartTag( String tagName )
	{
		elementStack.push( tagName );
		elementActive = true;
		out.print( "<" ); //$NON-NLS-1$ 
		out.print( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#endElement()
	 */
	public void endElement( )
	{
		// Check if we never actually wrote the tag because it had
		// no content.

		if ( !pendingElementStack.isEmpty( ) )
		{
			pendingElementStack.pop( );
			return;
		}

		// Close a tag for which the start tag was written.

		assert elementStack.size( ) > 0;
		String tagName = elementStack.pop( );
		if ( elementActive )
		{
			out.print( "/>" ); //$NON-NLS-1$
		}
		else
		{
			out.print( "</" ); //$NON-NLS-1$ 
			out.print( tagName );
			out.print( ">" ); //$NON-NLS-1$
		}
		elementActive = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLWriter#literal(java.lang.String)
	 */
	public void literal( String text )
	{
		if ( !markLineNumber )
		{
			out.print( text );
			return;
		}

		int len = text.length( );
		for ( int i = 0; i < len; i++ )
		{
			char c = text.charAt( i );
			out.print( c );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#text(java.lang.String)
	 */
	public void text( String text )
	{
		closeTextTag( );
		if ( text == null )
			return;

		// Write the text character-by-character to encode special characters.

		int len = text.length( );
		for ( int i = 0; i < len; i++ )
		{
			char c = text.charAt( i );
			if ( c == '&' )
				out.print( "&amp;" ); //$NON-NLS-1$ 
			else if ( c == '<' )
				out.print( "&lt;" ); //$NON-NLS-1$ 			
			else
				out.print( c );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.XMLWriter#textCDATA(java.lang.String)
	 */
	public void textCDATA( String text )
	{
		closeTextTag( );
		if ( text == null )
			return;

		// Write the text character-by-character to encode special characters.
		out.print( "<![CDATA[" ); //$NON-NLS-1$		
		out.print( text );
		out.print( "]]>" ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#checkAttribute()
	 */
	protected void checkAttribute( )
	{
		// Write any conditional elements waiting for content. If we get
		// here, we're about to write an attribute, so the elements do
		// have content.

		flushPendingElements( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.XMLWriter#printLine()
	 */
	protected void printLine( )
	{
		// do nothing
	}
}
