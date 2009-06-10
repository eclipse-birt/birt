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
		super.printWriter.print( encodeAttr( value ) );
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

	public void comment( String value )
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
		printWriter.print( HTMLEncodeUtil.encodeCdata( value ) );
		printWriter.print( "-->" );
		bText = true;// bText is useless.
	}

	public void text(String value) {
		text(value, true);
	}

	public void text( String value, boolean whitespace )
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

		String stringToPrint = HTMLEncodeUtil.encodeText( value, whitespace );
		printWriter.print( stringToPrint );
		bText = true;
	}

	protected String encodeText( String text )
	{
		return HTMLEncodeUtil.encodeText( text, false );
	}

	protected String encodeText( String text, boolean whitespace )
	{
		return HTMLEncodeUtil.encodeText( text, whitespace );
	}
}