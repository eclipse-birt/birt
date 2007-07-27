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

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.PDFConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValueList;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

/**
 * the font handler, which maps fontFamily, fontStyle, fontWeight properties to
 * the TrueType font.
 */
public class FontHandler
{

	//FIXME: code review : create font manager factory to manage the font managers. 
	/** The font-family mapping manager without format related configuration. */
	private static FontMappingManager formatUnrelatedManager = null;

	/**
	 * The font-family mapping managers with format related configuration. Each
	 * entry map a format name to a font mapping manger;
	 */
	private static Map formatRelatedManagers = new HashMap( );

	/** The font family names */
	private CSSValueList fontFamilies = null;

	/** the style of the font, should be BOLD, ITALIC, BOLDITALIC or NORMAL */
	private int fontStyle = Font.NORMAL;

	/** the font-size property */
	private float fontSize = 0f;

	/** the selected BaseFont */
	private BaseFont bf = null;

	/** the flag to show if the BaseFont has been changed */
	private boolean isFontChanged = false;

	/** the flag to show whether we need to simulate bold/italic font or not */
	private boolean simulation = false;

	/** the flag to show whether we have prepared the font or not */
	private static boolean prepared = false;

	private static FontConfigReader fontConfigReader;

	private FontMappingManager fontManager = null;
	
	private static boolean needDefaultConfig = true;
	
	private Map fonts = new HashMap( );
	
	FontHandler( )
	{
	}
	
	/**
	 * The constructor
	 * 
	 * @param textContent
	 *            the textContent whose font need to be handled
	 */
	public FontHandler( ITextContent textContent, String format )
	{
		IStyle style = textContent.getComputedStyle( );

		// splits font-family list
		this.fontFamilies = (CSSValueList) style
				.getProperty( StyleConstants.STYLE_FONT_FAMILY );

		if ( CSSConstants.CSS_OBLIQUE_VALUE.equals( style.getFontStyle( ) )
				|| CSSConstants.CSS_ITALIC_VALUE.equals( style.getFontStyle( ) ) )
		{
			this.fontStyle |= Font.ITALIC;
		}

		if ( PropertyUtil.isBoldFont( style
				.getProperty( StyleConstants.STYLE_FONT_WEIGHT ) ) )
		{
			this.fontStyle |= Font.BOLD;
		}

		this.fontSize = PropertyUtil.getDimensionValue( style
				.getProperty( StyleConstants.STYLE_FONT_SIZE ) )
				/ PDFConstants.LAYOUT_TO_PDF_RATIO;
		setFormat( format );
	}

	/**
	 * Registers fonts in some probable directories. It usually works in
	 * Windows, Linux and Solaris.
	 * 
	 * @return the number of fonts registered
	 */
	private static void registerDirectories( )
	{
		FontFactory.registerDirectory( "C:/windows/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "d:/windows/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "e:/windows/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "f:/windows/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "g:/windows/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "C:/WINNT/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "d:/WINNT/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "e:/WINNT/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "f:/WINNT/fonts" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "g:/WINNT/fonts" ); //$NON-NLS-1$

		FontFactory.registerDirectory( "/usr/X/lib/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "/usr/share/fonts/default/TrueType" ); //$NON-NLS-1$

		FontFactory.registerDirectory( "/usr/openwin/lib/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/euro_fonts/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_2/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_5/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_7/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_8/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_9/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_13/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_15/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ar/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/hi_IN.UTF-8/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ja/X11/fonts/TT" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ko/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ko.UTF-8/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/KOI8-R/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ru.ansi-1251/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/th_TH/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_TW/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_TW.BIG5/X11/fonts/TT" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_HK.BIG5HK/X11/fonts/TT" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_CN.GB18030/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh/X11/fonts/TrueType" ); //$NON-NLS-1$
		FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh.GBK/X11/fonts/TrueType" ); //$NON-NLS-1$

		FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/TrueType" ); //$NON-NLS-1$ //RH 7.1+
		FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/truetype" ); //$NON-NLS-1$ // SuSE
		FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/tt" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/TTF" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/OTF" ); //$NON-NLS-1$ //RH 9.0 (but empty!)
		FontFactory.registerDirectory( "/usr/share/fonts/ja/TrueType" ); //$NON-NLS-1$ //RH 7.2+
		FontFactory.registerDirectory( "/usr/share/fonts/truetype" ); //$NON-NLS-1$
		FontFactory.registerDirectory( "/usr/share/fonts/ko/TrueType" ); //$NON-NLS-1$ //RH 9.0
		FontFactory.registerDirectory( "/usr/share/fonts/zh_CN/TrueType" ); //$NON-NLS-1$ //RH 9.0
		FontFactory.registerDirectory( "/usr/share/fonts/zh_TW/TrueType" ); //$NON-NLS-1$ //RH 9.0
		FontFactory
				.registerDirectory( "/var/lib/defoma/x-ttcidfont-conf.d/dirs/TrueType" ); //$NON-NLS-1$ // Debian

	}

	/**
	 * Registers the fonts path according the config file. If no config file is
	 * found, uses the most probable directories.
	 */
	public synchronized static void prepareFonts( )
	{
		if ( prepared )
			return;

		fontConfigReader = new FontConfigReader( );
		fontConfigReader.initialize( );
		formatUnrelatedManager = fontConfigReader.getFontMappingManager( );
		Set fontPaths = formatUnrelatedManager.getFontPaths( );
		// FIXME: code review : path should be registered when format related
		// config has no paths defined.
		if ( !fontPaths.isEmpty( ) )
		{
			registerPaths( fontPaths );
			needDefaultConfig = false;
		}
		
		fontConfigReader.reset( );
		prepared = true;
	}

	public FontMappingManager getFontManager( String format )
	{
		if ( format == null )
		{
			return formatUnrelatedManager;
		}

		FontMappingManager result = null;
		//FIXME: code review : synchonize the whole block.
		result = (FontMappingManager) formatRelatedManagers.get( format );
		if ( result != null )
		{
			return result;
		}
		synchronized ( formatRelatedManagers )
		{
			result = (FontMappingManager) formatRelatedManagers.get( format );
			if ( result == null )
			{
				if ( fontConfigReader.parseFormatRelatedConfigFile( format ) )
				{
					FontMappingManager fontManager = fontConfigReader
							.getFontMappingManager( );
					result = formatUnrelatedManager.merge( fontManager );
					registerPaths(fontManager);
				}
				else
				{
					result = formatUnrelatedManager;
				}
				formatRelatedManagers.put( format, result );
				fontConfigReader.reset( );
			}
		}
		return result;
	}

	/**
	 * Gets the FontInfo Object.
	 */
	public FontInfo getFontInfo( )
	{
		//FIXME: code review : do this check only when bf is changed.
		checkFontAvailability( );
		return new FontInfo( bf, fontSize, fontStyle, simulation );
	}

	private void setFormat( String format )
	{
		if ( format != null )
		{
			fontManager = getFontManager( format.toLowerCase( ) );
		}
		else
		{
			//FIXME: code review : should use formatUnrelatedManager instead of null.
			fontManager = null;
		}
	}

	/**
	 * Selects a proper font for a character.
	 */
	public boolean selectFont( char character )
	{
		assert ( fontManager != null );
		// FIXME: code review : return null when no mapped font defined the
		// character so that charExist only need to be invoked once.
		BaseFont candidateFont = getMappedFont( character );
		assert ( candidateFont != null );
		checkFontStatus( candidateFont );
		
		return candidateFont.charExists( character );
	}
	
	public BaseFont getMappedFont(char c )
	{
		return getMappedFont( c, fontManager, fontFamilies, fontStyle );
	}

	/**
	 * Gets the BaseFont object to display the given character. It will
	 * <li> traverse the customer defined font list by the specified order, try
	 * to display the character with the BaseFont using the font family name. If
	 * the font family name is a generic font, or an alias of another font
	 * family, the font family name will be replaced according to the mapping
	 * defined in the fontMapping object. </li>
	 * <li> If the above fails, the unicode block containing the given character
	 * will be retrived. Then we will try each font defined for this block to
	 * display the character. </li>
	 * <li> If the above also fails, we can not find a font to display the given
	 * character. null will be returned. </li>
	 * 
	 * @param c
	 *            the given character.
	 * @param fontFamilies
	 *            the customer defined font list.
	 * @param fontStyle
	 *            the style of the font.
	 * @return the BaseFont. If we fail to find one, return null.
	 */
	BaseFont getMappedFont( char c, FontMappingManager fontManager,
			CSSValueList fontFamilies, int fontStyle )
	{
//		Set fonts = FontFactory.getRegisteredFonts( );
//		Iterator iterator = fonts.iterator( );
//		while( iterator.hasNext( ) )
//		{
//			String fontName = (String)iterator.next( );
//			String encoding = fontManager.getEncoding( fontName );
//			BaseFont font = null;
//			try
//			{
//				// FIXME: code view verify if BaseFont.NOT_EMBEDDED or
//				// BaseFont.EMBEDDED should be used.
//				Font f = FontFactory.getFont( fontName, encoding,
//						BaseFont.NOT_EMBEDDED, 14, fontStyle );
//				font = f.getBaseFont( );
//				if ( font == null ) continue;
//			}
//			catch ( Throwable de )
//			{
//			}
//
//			if ( isCharDefinedInFont( c, font ))
//			{
//				System.out.println( fontName );
//			}
//		}
		for ( int i = 0; i < fontFamilies.getLength( ); i++ )
		{
			String fontFamilyName = fontFamilies.item( i ).getCssText( );
			String logicalFont = fontManager.getLogicalFont( fontFamilyName );

			String physicalFont = fontManager.getPhysicalFont( c, logicalFont,
					logicalFont );

			BaseFont font = getPhysicalFont( fontManager, physicalFont,
					fontStyle );
			if ( isCharDefinedInFont( c, font ) )
			{
				return font;
			}
		}
		String physicalFont = fontManager.getDefaultPhysicalFont( c );
		BaseFont defaultFont = getPhysicalFont( fontManager, physicalFont,
				fontStyle );
		if ( defaultFont == null )
		{
			defaultFont = fontManager.createFont(
					FontMappingManager.DEFAULT_FONT, fontStyle );
		}
		return defaultFont;
	}

	private BaseFont getPhysicalFont( FontMappingManager fontManager,
			String physicalFont, int fontStyle )
	{
		BaseFont font = (BaseFont) fonts.get( physicalFont );
		if ( font == null )
		{
			font = fontManager.createFont( physicalFont, fontStyle );
			if ( font != null )
			{
				fonts.put( physicalFont, font );
			}
		}
		return font;
	}

	private boolean isCharDefinedInFont( char c, BaseFont bf )
	{
		return null != bf && bf.charExists( c );
	}

	private void registerPaths( FontMappingManager fontManager )
	{
		Set fontPaths = fontManager.getFontPaths( );
		if ( fontPaths.isEmpty( ) )
		{
			if ( needDefaultConfig )
			{
				registerDirectories( );
				needDefaultConfig = false;
			}
		}
		else
		{
			registerPaths( fontPaths );
		}
	}

	private static void registerPaths( Set fontPaths )
	{
		for ( Iterator i = fontPaths.iterator( ); i.hasNext( ); )
		{
			String fontPath = (String) i.next( );
			File file = new File( fontPath );
			if ( file.exists( ) )
			{
				if ( file.isDirectory( ) )
				{
					FontFactory.registerDirectory( fontPath );
				}
				else
				{
					FontFactory.register( fontPath );
				}
			}
		}
	}

	//FIXME: code review : provide a more expressive name.
	private void checkFontStatus( BaseFont candidateFont )
	{
		assert candidateFont != null;
		if ( bf == candidateFont )
		{
			isFontChanged = false;
		}
		else
		{
			isFontChanged = true;
			bf = candidateFont;
		}
	}

	public boolean isFontChanged( )
	{
		return isFontChanged;
	}

	/**
	 * Gets the English font name or font family name from the given naming
	 * array
	 * 
	 * @param names
	 *            the naming array
	 * @return the English name
	 */
	private String getEnglishName( String[][] names )
	{
		String tmp = null;
		for ( int i = 0; i < names.length; i++ )
		{
			if ( "0".equals( names[i][2] ) ) //$NON-NLS-1$
			{
				return names[i][3];
			}
			//FIXME: code review : check the logic.
			if ( "1033".equals( names[i][2] ) ) //$NON-NLS-1$
			{
				tmp = names[i][3];
			}
			if ( "".equals( names[i][2] ) ) //$NON-NLS-1$
			{
				tmp = names[i][3];
			}
		}

		return tmp;
	}

	/**
	 * If the BaseFont can NOT find the correct physical glyph, we need to
	 * simulate the proper style for the font. The "simulate" flag will be set
	 * if we need to simulate it.
	 */
	//FIXME: code review : merged into checkFontStatus.
	private void checkFontAvailability( )
	{
		if ( fontStyle == Font.NORMAL )
		{
			simulation = false;
		}
		else
		{
			simulation = getEnglishName( bf.getFamilyFontName( ) ).equals(
					getEnglishName( bf.getFullFontName( ) ) );
		}
	}

}