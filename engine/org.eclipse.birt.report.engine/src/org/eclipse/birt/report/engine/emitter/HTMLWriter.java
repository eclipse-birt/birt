/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.util.logging.Level;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

/**
 * <code>HTMLWriter</code> is a concrete subclass of <code>XMLWriter</code>
 * that outputs the HTML content.
 * 
 */
public class HTMLWriter extends XMLWriter
{

	/**
	 * Creates a HTMLWriter using this constructor.
	 */
	public HTMLWriter( )
	{
		//set bImplicitCloseTag here, because IE will treat <div/> as <div>
		//we must use <div></div> as the empty DIV tag.
		bImplicitCloseTag = false;
	}

	/**
	 * Close the tag
	 * 
	 * @param tagName
	 *            tag name
	 */
	public void closeTag( String tagName )
	{
		if( tagName == null )
		{
			return;
		}
		super.closeTag( tagName );
	}

	/**
	 * Close the tag whose end tag is forbidden say, "br".
	 * 
	 * @param tagName
	 *            tag name
	 */
	public void closeNoEndTag( )
	{
		super.indentCount--;
		if ( !super.bPairedFlag )
		{
			super.printWriter.print( '>' );
		}
		else
		{
			assert false;
		}
		super.bPairedFlag = true;
	}

	/**
	 * Outputs the style.
	 * 
	 * @param name
	 *            The style name.
	 * @param value
	 *            The style values.
	 */
	public void style( String name, String value )
	{
		assert name != null && name.length( ) > 0;
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}

		if ( !super.bPairedFlag )
		{
			super.printWriter.print( '>' );
			super.bPairedFlag = true;
		}

		if ( super.bIndent )
		{
			super.indentCount++;
			super.printWriter.println( );
			super.printWriter.print( super.indent( ) );
			super.indentCount--;
		}

		super.printWriter.print( name );
		super.printWriter.print( " {" ); //$NON-NLS-1$
		super.printWriter.print( value );
		super.printWriter.print( '}' );
	}

	/**
	 * Outputs java script code.
	 * 
	 * @param code
	 *            a line of code
	 */
	public void writeCode( String code )
	{
		if ( !super.bPairedFlag )
		{
			super.printWriter.print( '>' );
			super.bPairedFlag = true;
		}

		if ( super.bIndent )
		{
			super.printWriter.println( );
			super.printWriter.print( super.indent( ) );
		}

		super.printWriter.print( code );
	}

	/**
	 * Output the document type.
	 */
	public void outputDoctype( )
	{
		printWriter
			.print( "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">" ); //$NON-NLS-1$
	}

	/**
	 * Replace the escape characters.
	 * 
	 * @param s
	 *            The string needs to be replaced.
	 * @param whiteespace
	 *            A
	 *            <code>boolean<code> value indicating if the white space character should be converted or not. 
	 * @return The replaced string.
	 */
	protected String getEscapedStr( String s, boolean whitespace )
	{
		StringBuffer result = null;
		int spacePos = 1;
		char[] s2char = s.toCharArray( );

		for ( int i = 0, max = s2char.length, delta = 0; i < max; i++ )
		{
			char c = s2char[i];
			String replacement = null;
			
			
			//The first and the last characters are converted to Entity.
			if ( whitespace && c == ' ' )
			{
				boolean replace = false;
				if(spacePos % 2 == 1)
				{
					replace = true;
				}
				else
				{
					char last = ( i - 1 >= 0 ? s2char[i - 1] : '\n' );
					char next = ( i + 1 < max ? s2char[i + 1] : '\n' );
					char nextNext = ( i + 2 < max ? s2char[i + 2] : '\n' );
					if(last=='\n' || next=='\n' ||(next=='\r' && nextNext=='\n')  )
					{
						replace = true;
					}
				}
				if(replace)
				{
					replacement = "&#xa0;"; //$NON-NLS-1$
				}
				spacePos++;
				
			}
			else
			{
				spacePos = 0;
			}
			
			// Filters the char not defined.
			if ( !( c == 0x9 || c == 0xA || c == 0xD
					|| ( c >= 0x20 && c <= 0xD7FF ) || ( c >= 0xE000 && c <= 0xFFFD ) ) )
			{
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
				log.log( Level.WARNING,
						"Ignore the illegal XML character: 0x{0};", Integer //$NON-NLS-1$
								.toHexString( c ) );
			}
			else if ( c == '&' )
			{
				replacement = "&amp;"; //$NON-NLS-1$
			}
			else if ( c == '<' )
			{
				replacement = "&lt;"; //$NON-NLS-1$
			}
			else if ( c == '>' )
			{
				replacement = "&gt;"; //$NON-NLS-1$
			}
			else if ( c == '\t' )
			{
				replacement = "&#xa0;"; //$NON-NLS-1$
			}
			else if ( c == '\r' )
			{
				int n = i + 1;
				if ( n < max && s2char[n] == '\n' )
				{
					replacement = ""; //$NON-NLS-1$
				}
				else
				{
					replacement = "<br>";
				}
			}
			else if ( c == '\n' )
			{
				replacement = "<br>"; //$NON-NLS-1$
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
	 * Output the encoded content in html text item
	 * 
	 * @param value
	 *            the content
	 */
	public void textForHtmlItem( String value )
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
		
		String stringToPrint = getEscapedStrForHtmlItem( value );
		printWriter.print( stringToPrint );
		bText = true;
	}
	
	public void commentForHtmlItem( String value )
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

		printWriter.print( "<!--" );
		printWriter.print( value );
		printWriter.print( "-->" );
		bText = true;// bText is useless.
	}
	
	/**
	 * Replace the escape characters.
	 * 
	 * @param s
	 *            The string needs to be replaced. 
	 * @return The replaced string.
	 */
	protected String getEscapedStrForHtmlItem( String s )
	{
		StringBuffer result = null;
		char[] s2char = s.toCharArray( );

		for ( int i = 0, max = s2char.length, delta = 0; i < max; i++ )
		{
			char c = s2char[i];
			String replacement = null;	
			
			// Filters the char not defined.
			if ( !( c == 0x9 || c == 0xA || c == 0xD
					|| ( c >= 0x20 && c <= 0xD7FF ) || ( c >= 0xE000 && c <= 0xFFFD ) ) )
			{
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
				log.log( Level.WARNING,
						"Ignore the illegal XML character: 0x{0};", Integer //$NON-NLS-1$
								.toHexString( c ) );
			}
			else if ( c == '&' )
			{
				replacement = "&amp;"; //$NON-NLS-1$
			}
			else if ( c == '<' )
			{
				replacement = "&lt;"; //$NON-NLS-1$
			}
			else if ( c == '>' )
			{
				replacement = "&gt;"; //$NON-NLS-1$
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
			printWriter.print( escapeAttrValue( attrValue ) );
			printWriter.print( '\"' );
		}
	}
	
	/**
	 * Replaces the escape character in attribute value.
	 * 
	 * @param s
	 *            The string needs to be replaced.
	 * @return the replaced string
	 */
	protected String escapeAttrValue( String s )
	{
		StringBuffer result = null;
		char[] s2char = s.toCharArray( );

		for ( int i = 0, max = s2char.length, delta = 0; i < max; i++ )
		{
			char c = s2char[i];
			String replacement = null;
			// Filters the char not defined.
			if ( !( c == 0x9 || c == 0xA || c == 0xD
					|| ( c >= 0x20 && c <= 0xD7FF ) || ( c >= 0xE000 && c <= 0xFFFD ) ) )
			{
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
				log.log( Level.WARNING,
						"Ignore the illegal XML character: 0x{0};", Integer //$NON-NLS-1$
								.toHexString( c ) );
			}
			if ( c == '&' )
			{
				replacement = "&amp;"; //$NON-NLS-1$
			}
			else if ( c == '"' )
			{
				replacement = "&#34;"; //$NON-NLS-1$
			}
			else if ( c == '\r' )
			{
				replacement = "&#13;"; //$NON-NLS-1$
			}
			else if ( c == '<' )
			{
				replacement = "&lt;"; //$NON-NLS-1$
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
}