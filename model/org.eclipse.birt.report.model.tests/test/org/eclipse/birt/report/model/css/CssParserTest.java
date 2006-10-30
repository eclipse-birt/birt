/*******************************************************************************
 * Copyright (c) {DATE} Actuate Corporation {ADD OTHER COPYRIGHT OWNERS}.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  {ADD SUBSEQUENT AUTHOR & CONTRIBUTION}
 *******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.io.StringReader;
import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.report.model.css.property.ParseException;
import org.eclipse.birt.report.model.css.property.PropertyParser;

/**
 * Test case
 * 
 * 
 * @version $Revision: 1.1 $ $Date: 2005/09/29 03:50:22 $
 */
public class CssParserTest extends TestCase
{

	public void testBackground( ) throws Exception
	{
		String style = "url(\"chess.png\") gray 50% repeat fixed ";
		PropertyParser parser = new PropertyParser( new StringReader( style ) );
		parser.parseBackground( );
		HashMap result = parser.getCssProperties( );
		assertEquals( result.get( "background-image" ), "url(\"chess.png\")" );
		assertEquals( result.get( "background-color" ), "gray" );
		assertEquals( result.get( "background-position" ), "50%" );
		assertEquals( result.get( "background-repeat" ), "repeat" );
		assertEquals( result.get( "background-attachment" ), "fixed" );
		
		style = "#ccc";
		parser.ReInit( new StringReader( style ) );
		parser.parseBackground( );
		result = parser.getCssProperties( );
		assertEquals( "#ccc", result.get( "background-color" ) );
	}

	public void testFont( ) throws Exception
	{
		String style = "12px/14px sans-serif ";
		PropertyParser parser = new PropertyParser( new StringReader( style ) );
		parser.parseFont( );
		HashMap result = parser.getCssProperties( );
		//		assertEquals( result.get( "font-weight" ), "bold" );
		//		assertEquals( result.get( "font-style" ), "italic" );
		assertEquals( result.get( "font-size" ), "12px" );
		assertEquals( result.get( "line-height" ), "14px" );
		assertEquals( result.get( "font-family" ), "sans-serif" );

		parser.ReInit( new StringReader( " 80% sans-serif " ) );
		parser.parseFont( );
		result = parser.getCssProperties( );
		//		assertEquals( result.get( "font-weight" ), "bold" );
		//		assertEquals( result.get( "font-style" ), "italic" );
		assertEquals( result.get( "font-size" ), "80%" );
		assertEquals( result.get( "font-family" ), "sans-serif" );

		parser.ReInit( new StringReader(
				" x-large/110% \"New Century Schoolbook\", serif " ) );
		parser.parseFont( );
		result = parser.getCssProperties( );
		assertEquals( result.get( "font-size" ), "x-large" );
		assertEquals( result.get( "line-height" ), "110%" );
		assertEquals( result.get( "font-family" ),
				"\"New Century Schoolbook\",serif" );

		parser
				.ReInit( new StringReader(
						" bold italic large Palatino, serif " ) );
		parser.parseFont( );
		result = parser.getCssProperties( );
		assertEquals( result.get( "font-weight" ), "bold" );
		assertEquals( result.get( "font-style" ), "italic" );
		assertEquals( result.get( "font-size" ), "large" );
		assertEquals( result.get( "font-family" ), "Palatino,serif" );

		parser.ReInit( new StringReader(
				" normal small-caps 120%/120% fantasy " ) );
		parser.parseFont( );
		result = parser.getCssProperties( );
		assertEquals( result.get( "font-weight" ), "normal" );
		assertEquals( result.get( "font-style" ), "normal" );
		assertEquals( result.get( "font-variant" ), "small-caps" );
		assertEquals( result.get( "font-size" ), "120%" );
		assertEquals( result.get( "line-height" ), "120%" );
		assertEquals( result.get( "font-family" ), "fantasy" );

	}

	public void testBorderWidth( ) throws Exception
	{
		String style = "medium thin";
		PropertyParser parser = new PropertyParser( new StringReader( style ) );
		parser.parseBorderWidth( );
		HashMap result = parser.getCssProperties( );
		assertEquals( result.get( "border-top-width" ), "medium" );
		assertEquals( result.get( "border-right-width" ), "thin" );
		assertEquals( result.get( "border-bottom-width" ), "medium" );
		assertEquals( result.get( "border-left-width" ), "thin" );
		
		style = "1em";
		parser.ReInit( new StringReader( style ) );
		parser.parsePadding( );
		result = parser.getCssProperties( );
		assertEquals( result.get( "padding-top" ), "1em" );
		assertEquals( result.get( "padding-right" ), "1em" );
		assertEquals( result.get( "padding-bottom" ), "1em" );
		assertEquals( result.get( "padding-left" ), "1em" );
	}	

	public void testwithException1( ) throws Exception
	{
		try
		{
			String style = "1p red double";
			PropertyParser parser = new PropertyParser( new StringReader( style ) );
			parser.parseBorder( );
			fail( );
		}
		catch ( ParseException e )
		{
			assertNotNull( e );
		}
	}
	
	public void testFont1( ) throws Exception
	{
		// \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif 2em/120% italic small-caps  bold
		
		String style = "italic small-caps  bold 2em/120% \"Bitstream Vera Sans\", Tahoma, Verdana, \"Myriad Web\", Syntax, sans-serif";
		PropertyParser parser = new PropertyParser( style );
		parser.parseFont( );
		HashMap result = parser.getCssProperties( );
		assertEquals( "\"Bitstream Vera Sans\",Tahoma,Verdana,\"Myriad Web\",Syntax,sans-serif", result.get( "font-family" ) );
	}
}