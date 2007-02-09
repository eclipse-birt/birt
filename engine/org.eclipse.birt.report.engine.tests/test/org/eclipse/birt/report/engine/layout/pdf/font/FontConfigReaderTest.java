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

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import junit.framework.TestCase;

import org.eclipse.birt.report.engine.css.engine.value.ListValue;
import org.eclipse.birt.report.engine.css.engine.value.StringValue;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValueList;
import org.xml.sax.SAXException;

import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;

public class FontConfigReaderTest extends TestCase
{

	private FontMappingManager fontMappingManager = null;

	public void testFont( ) throws IOException, FactoryConfigurationError,
			ParserConfigurationException, SAXException
	{
		String configFile = "fontsConfig.xml";
		fontMappingManager = getFontMappingManager( configFile );

		// Tests an exist font which is not mapped in fontmapping should not be
		// mapped.
		assertTrue( isMappedTo( '1', "Symbol", "Symbol" ) );

		// Tests an unexist font which is not mapped in fontmapping should not be
		// mapped to all-fonts-mapped font.
		assertTrue( isMappedTo( '1', "Not exist", "Times Roman" ) );

		// Tests an unexist font which is not mapped in fontmapping should not be
		// mapped to all-fonts-mapped font.
		assertTrue( isDefaultMappedTo( (char)0xe81, "Symbol", "ITC Zapf Dingbats" ) );

		// Tests a font in font-mapping.
		assertTrue( isMappedTo( '1', "sans-serif", "Helvetica" ) );

		// Tests a font which is not in font-mapping but in all-fonts mapping by
		// font name.
		assertTrue( isMappedTo( '1', "block", "Courier" ) );

		// Tests a font which is not in font-mapping but in all-fonts mapping by
		// "all-fonts".
		assertTrue( isMappedTo( '1', "allfonts", "Times Roman" ) );

		// Tests a character which is not defined by any block is mapped to
		// default.
		assertTrue( isDefaultMappedTo( (char) 0x501, "onlyInDefault",
				"Helvetica" ) );

		// Tests a character which is not defined by any block is mapped to
		// default.
		assertTrue( isDefaultMappedTo( (char) 0x501, "defaultAllFonts",
				"ITC Zapf Dingbats" ) );
	}

	private boolean isDefaultMappedTo( char c, String from, String to )
	{
		CSSValueList fonts = createCssValueList( from );
		int style = Font.NORMAL;
		if ( fontMappingManager.getMappedFont( c, fonts, style ) != null )
			return false;
		BaseFont font = fontMappingManager.getDefaultFont( fonts, style );
		return isIn( to, font.getFullFontName( ) );
	}

	private boolean isMappedTo( char c, String from, String to )
	{
		BaseFont font = fontMappingManager.getMappedFont( c,
				createCssValueList( from ), Font.NORMAL );
		return isIn( to, font.getFullFontName( ) );
	}

	private CSSValueList createCssValueList( String fontName )
	{
		return createCssValueList( new String[]{fontName} );
	}

	private CSSValueList createCssValueList( String[] fontNames )
	{
		ListValue result = new ListValue( );
		for ( int i = 0; i < fontNames.length; i++ )
		{
			StringValue value = new StringValue( CSSPrimitiveValue.CSS_STRING,
					fontNames[i] );
			result.append( value );
		}
		return result;
	}

	private boolean isIn( String fontName, String[][] familyFontName )
	{
		assert ( fontName != null );
		for ( int i = 0; i < familyFontName.length; i++ )
		{
			for ( int j = 0; j < familyFontName[i].length; j++ )
			{
				if ( fontName.equals( familyFontName[i][j] ) )
					return true;
			}
		}
		return false;
	}

	private FontMappingManager getFontMappingManager( String configFile )
			throws IOException, FactoryConfigurationError,
			ParserConfigurationException, SAXException
	{
		URL fileURL = getURL( configFile );
		FontConfigReader reader = new FontConfigReader( );
		reader.parseConfigFile( fileURL );
		FontMappingManager fontMappingManager = reader.getFontMappingManager( );
		return fontMappingManager;
	}

	private URL getURL( String configFile )
	{
		URL fileURL = getClass( )
				.getResource(
						"/org/eclipse/birt/report/engine/layout/pdf/font/"
								+ configFile );
		return fileURL;
	}
}
