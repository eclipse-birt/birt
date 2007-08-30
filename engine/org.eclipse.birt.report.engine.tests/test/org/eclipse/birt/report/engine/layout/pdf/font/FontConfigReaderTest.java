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
import java.util.Locale;
import java.util.Map;

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
	FontHandler handler = FontHandlerUtil.getInstance( );

	public void testFontMapWhenAllFontsNotDefined( ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		fontMappingManager = getFontMappingManager( "fontsConfig2.xml" );

		// alias: defined; compsoite-font: defined; block: defined; character:
		// defined by the block font.
		assertTrue( isMappedTo( '1', "alias1", "Courier" ) );
		// alias: defined; compsoite-font: defined; block: defined; character:
		// not defined by the block font.
		assertTrue( isMappedTo( (char) 0xe81, "alias1", "Times Roman" ) );
		// alias: defined; compsoite-font: defined; block: not; character:
		// not defined by the logical font.
		assertTrue( isMappedTo( (char) 0x80, "alias1", "Times Roman" ) );
		// alias: defined; compsoite-font: not; character: defined by the
		// logical font.
		assertTrue( isMappedTo( '1', "alias2", "Helvetica" ) );

		// alias: not; compsoite-font: defined; block: defined; character:
		// defined by the block font.
		assertTrue( isMappedTo( '1', "Symbol", "Courier" ) );
		// alias: not; compsoite-font: defined; block: defined; character:
		// not defined by the block font.
		assertTrue( isMappedTo( (char) 0xe81, "Symbol", "Times Roman" ) );
		// alias: not; compsoite-font: defined; block: not; character:
		// not defined by the logical font.
		assertTrue( isMappedTo( (char) 0x80, "Symbol", "Times Roman" ) );
		// alias: not; compsoite-font: not; character: defined by the logical
		// font.
		assertTrue( isMappedTo( '1', "Helvetica", "Helvetica" ) );
	}

	public void testFontMapWhenAllFontsDefined( ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		fontMappingManager = getFontMappingManager( "fontsConfig1.xml" );

		// alias defined, composite font defined, block defined, and character
		// is defined by the block font.
		assertTrue( isMappedTo( '1', "alias1", "Courier" ) );
		// alias defined, composite font not defined, and character
		// is defined by the logical font.
		assertTrue( isMappedTo( '1', "alias2", "Helvetica" ) );

		// alias not defined, composite font defined, and character
		// is defined by the block font.
		assertTrue( isMappedTo( '1', "Symbol", "Courier" ) );
		// alias not defined, composite font not defined, and character
		// is defined by the logical font.
		assertTrue( isMappedTo( '1', "Helvetica", "Helvetica" ) );

		// alias: defined; compsoite-font: defined; block: defined; character:
		// not defined by the block font.
		// all-fonts:define the block
		assertTrue( isMappedTo( (char) 0xe81, "Symbol", "Helvetica" ) );
		// alias: defined; compsoite-font: defined; block: defined; character:
		// not defined by the block font.
		// all-fonts:not define the block
		assertTrue( isMappedTo( (char) 0xf01, "Symbol", "Times Roman" ) );
	}

	public void testCompatibleParser( ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		fontMappingManager = getFontMappingManager( "fontsConfigCompatible.xml" );
		Map fontAliases = fontMappingManager.getFontAliases( );
		assertEquals( 7, fontAliases.size( ) );
		assertEquals( "font alias in font-aliases", fontAliases
				.get( "font alias overridden" ) );
		assertEquals( "font alias in font-mappings", fontAliases
				.get( "font alias not overridden" ) );

		Map compositeFonts = fontMappingManager.getCompositeFonts( );
		assertEquals( 3, compositeFonts.size( ) );
		CompositeFont overriddenFont = (CompositeFont) compositeFonts.get( "font overridden" );
		assertEquals( 1, overriddenFont.getBlockCount( ) );
		assertEquals( "font in composite-font", overriddenFont.getBlockFont( 0 ) );
		CompositeFont notOverriddenFont = (CompositeFont) compositeFonts
				.get( "font not overridden" );
		assertEquals( 1, notOverriddenFont.getBlockCount( ) );
		assertEquals( "font in all-fonts", notOverriddenFont.getBlockFont( 0) );
		CompositeFont allFonts = (CompositeFont) compositeFonts.get( "all-fonts" );
		assertEquals( 2, allFonts.getBlockCount( ) );
		assertEquals( "Times-Roman", allFonts.getBlockFont( 0 ) );
		assertEquals( "Helvetica", allFonts.getBlockFont( 1 ) );
		assertEquals( "Helvetica", allFonts.getDefaultFont( ) );
	}

	public void testFontConfigParser( ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		fontMappingManager = getFontMappingManager( "fontsConfigParser.xml" );
		Map fontAliases = fontMappingManager.getFontAliases( );
		assertEquals( 6, fontAliases.size( ) );
		assertEquals( "Times-Roman", fontAliases.get( "test alias" ) );

		Map compositeFonts = fontMappingManager.getCompositeFonts( );
		assertEquals( 1, compositeFonts.size( ) );
		CompositeFont testFont = (CompositeFont) compositeFonts.get( "test font" );
		assertEquals( 1, testFont.getBlockCount( ) );
		assertEquals( "Symbol", testFont.getBlockFont( 0 ) );
		assertEquals( "Helvetica", testFont.getDefaultFont( ) );
	}

	public void testConfigFilePriority( )
	{
		String osName = System.getProperty( "os.name" );
		Locale locale = Locale.getDefault( );
		if ( "Windows XP".equals( osName ) && locale.equals( Locale.SIMPLIFIED_CHINESE ) )
		{
			testPriority( "default_os" );
			testPriority( "os_language" );
			testPriority( "language_country" );
		}
	}

	public void testCharacterFontMapping( ) throws IOException,
			FactoryConfigurationError, ParserConfigurationException,
			SAXException
	{
		fontMappingManager = getFontMappingManager( "fontsConfig_character.xml" );
		String testFontForCharacterMapping = "testForCharacterMapping";
		assertTrue( isMappedTo( 'a', testFontForCharacterMapping, "Helvetica" ));
		assertTrue( isMappedTo( 'b', testFontForCharacterMapping, "Times Roman" ));
		assertTrue( isMappedTo( 'c', testFontForCharacterMapping, "Courier" ));
		assertTrue( isMappedTo( 'a', "testForDefaultFont", "Symbol" ));
	}
	
	private void testPriority( final String testDir )
	{
		testPriority( testDir, null );
	}
	
	private void testPriority( final String testDir, String format )
	{
		FontConfigReader reader = new FontConfigReader( ) {

			protected URL getURL( String configFile )
			{
				URL fileURL = getClass( ).getResource(
						"/org/eclipse/birt/report/engine/layout/pdf/font/"
								+ testDir + "/" + configFile );
				return fileURL;
			}
		};
		reader.initialize( );
		FontMappingManager manager = null;
		if ( format == null )
		{
			manager = reader.getFontMappingManager( );
		}
		else
		{
			reader.parseFormatRelatedConfigFile( format );
			manager = reader.getFontMappingManager( );
		}
		testMergedProperty( manager.getFontAliases( ), 8 );
		testMergedProperty( manager.getFontEncodings( ), 9 );

		Map compositeFonts = manager.getCompositeFonts( );
		assertEquals( 3, compositeFonts.size( ) );
		String fontName = "common";
		CompositeFont compositeFont = (CompositeFont) compositeFonts.get( fontName );
		assertEquals( 1, compositeFont.getBlockCount( ) );
		assertTrue( compositeFonts.containsKey( "higher priority" ) );
		assertTrue( compositeFonts.containsKey( "lower priority" ) );
	}

	private void testMergedProperty( Map map, int size )
	{
		assertEquals( size, map.size( ) );
		assertEquals( "higher priority", map.get( "common" ) );
		assertEquals( "higher priority", map.get( "higher priority" ) );
		assertEquals( "lower priority", map.get( "lower priority" ) );
	}

	private boolean isMappedTo( char c, String from, String to )
	{
		BaseFont font = FontHandlerUtil.getMappedFont( handler, c,
				fontMappingManager, createCssValueList( from ), Font.NORMAL );
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
