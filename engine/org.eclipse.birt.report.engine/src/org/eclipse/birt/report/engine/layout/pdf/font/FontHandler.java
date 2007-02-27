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
import java.util.Iterator;
import java.util.List;

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

	/** The font-family mapping manager */
	private static FontMappingManager fontMappingManager = null;

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

	/**
	 * The constructor
	 * 
	 * @param textContent
	 *            the textContent whose font need to be handled
	 */
	public FontHandler( ITextContent textContent )
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

	}

	/**
	 * Registers fonts in some probable directories. It usually works in
	 * Windows, Linux and Solaris.
	 * 
	 * @return the number of fonts registered
	 */
	private static int registerDirectories( )
	{
		int count = 0;
		count += FontFactory.registerDirectory( "C:/windows/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "d:/windows/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "e:/windows/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "f:/windows/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "g:/windows/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "C:/WINNT/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "d:/WINNT/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "e:/WINNT/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "f:/WINNT/fonts" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "g:/WINNT/fonts" ); //$NON-NLS-1$

		count += FontFactory
				.registerDirectory( "/usr/X/lib/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/share/fonts/default/TrueType" ); //$NON-NLS-1$

		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/euro_fonts/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_2/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_5/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_7/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_8/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_9/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_13/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/iso_8859_15/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ar/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/hi_IN.UTF-8/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ja/X11/fonts/TT" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ko/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ko.UTF-8/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/KOI8-R/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/ru.ansi-1251/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/th_TH/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_TW/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_TW.BIG5/X11/fonts/TT" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_HK.BIG5HK/X11/fonts/TT" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh_CN.GB18030/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh/X11/fonts/TrueType" ); //$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/openwin/lib/locale/zh.GBK/X11/fonts/TrueType" ); //$NON-NLS-1$

		count += FontFactory
				.registerDirectory( "/usr/X11R6/lib/X11/fonts/TrueType" ); /*
																			 * RH
																			 * 7.1+
																			 *///$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/X11R6/lib/X11/fonts/truetype" ); /* SuSE *///$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/tt" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/TTF" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/X11R6/lib/X11/fonts/OTF" ); /*
																					 * RH
																					 * 9.0
																					 * (but
																					 * empty!)
																					 *///$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/share/fonts/ja/TrueType" ); /*
																					 * RH
																					 * 7.2+
																					 *///$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/share/fonts/truetype" ); //$NON-NLS-1$
		count += FontFactory.registerDirectory( "/usr/share/fonts/ko/TrueType" ); /*
																					 * RH
																					 * 9.0
																					 *///$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/share/fonts/zh_CN/TrueType" ); /*
																			 * RH
																			 * 9.0
																			 *///$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/usr/share/fonts/zh_TW/TrueType" ); /*
																			 * RH
																			 * 9.0
																			 *///$NON-NLS-1$
		count += FontFactory
				.registerDirectory( "/var/lib/defoma/x-ttcidfont-conf.d/dirs/TrueType" ); /* Debian *///$NON-NLS-1$

		return count;
	}

	/**
	 * Registers the fonts path according the config file. If no config file is
	 * found, uses the most probable directories.
	 */
	public synchronized static void prepareFonts( )
	{
		if ( prepared )
			return;

		FontConfigReader fcr = new FontConfigReader( );

		// read the embeded font path.
		String embededFontPath = fcr.getEmbededFontPath( );
		if ( null != embededFontPath )
		{
			FontFactory.registerDirectory( embededFontPath );
		}

		fontMappingManager = fcr.getFontMappingManager( );

		if ( fcr.parseConfigFile( ) )
		{
			// read font path from config file.
			List fontPaths = fcr.getTrueTypeFontPaths( );
			if ( fontPaths.isEmpty( ) )
			{
				registerDirectories( );
			}
			else
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
		}
		else
		{
			registerDirectories( );
		}

		prepared = true;
	}

	/**
	 * Gets the FontInfo Object.
	 */
	public FontInfo getFontInfo( )
	{
		checkFontAvailability( );
		return new FontInfo( bf, fontSize, fontStyle, simulation );
	}

	/**
	 * Selects a proper font for a character.
	 */
	public boolean selectFont( char character )
	{
		BaseFont candidateFont = fontMappingManager.getMappedFont( character,
				fontFamilies, fontStyle );
		assert( candidateFont != null );
		checkFontStatus( candidateFont );
		return candidateFont.charExists( character );
	}

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