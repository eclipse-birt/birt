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
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.pdf.BaseFont;

public class FontMappingManager
{

	/** The possible generic font names */
	private static final String SERIF = "serif"; //$NON-NLS-1$
	private static final String SANS_SERIF = "sans-serif"; //$NON-NLS-1$
	private static final String CURSIVE = "cursive"; //$NON-NLS-1$
	private static final String MONOSPACE = "monospace"; //$NON-NLS-1$
	private static final String FANTASY = "fantasy"; //$NON-NLS-1$

	/** default font for generic family "serif" */
	private static final String DEFAULT_SERIF_FONT = BaseFont.TIMES_ROMAN;

	/** default fonts for generic family "sans-serif" */
	private static final String DEFAULT_SANS_SERIF_FONT = BaseFont.HELVETICA;

	/** default fonts for generic family "cursive" */
	private static final String DEFAULT_CURSIVE_FONT = BaseFont.ZAPFDINGBATS;

	/** default fonts for generic family "monospace" */
	private static final String DEFAULT_MONOSPACE_FONT = BaseFont.COURIER;

	/** default fonts for generic family "fantasy" */
	private static final String DEFAULT_FANTASY_FONT = BaseFont.TIMES_ROMAN;

	/** all fonts key */
	public static final String FONT_NAME_ALL_FONTS = "all-fonts";

	/** default fonts */
	public static final String DEFAULT_FONT = BaseFont.TIMES_ROMAN;

	private static final String BLOCK_DEFAULT = "default";

	protected static Logger logger = Logger.getLogger( FontMappingManager.class
			.getName( ) );

    /** The font-family replacement */
	private Map fontAliases = new HashMap( );;

	/** The encoding for the fonts */
	private Map fontEncodings = new HashMap( );;

	/**
	 * The array maintaining the font list which can display the character in
	 * the given unicode block.
	 */
	private Map compositeFonts = new HashMap( );

	private Set fontPaths = new HashSet( );
	
	FontMappingManager( )
	{
		initializeFontMapping( );
		initializeFontEncoding( );
	}

	FontMappingManager(FontMappingManager fontManager)
	{
		fontAliases.putAll( fontManager.fontAliases );
		fontEncodings.putAll( fontManager.fontEncodings );
		compositeFonts.putAll( fontManager.compositeFonts );
		fontPaths.addAll( fontManager.fontPaths );
	}
	
	public FontMappingManager merge(FontMappingManager fontManager)
	{
		//FIXME: code review : verify if the priority is correct.
		FontMappingManager result = new FontMappingManager(this);
		result.fontAliases.putAll( fontManager.fontAliases );
		result.fontEncodings.putAll( fontManager.fontEncodings );
		result.compositeFonts.putAll( fontManager.compositeFonts );
		result.fontPaths.addAll( fontManager.fontPaths );
		return result;
	}
	
	protected void initializeFontMapping( )
	{
		fontAliases.put( SERIF, DEFAULT_SERIF_FONT );
		fontAliases.put( SANS_SERIF, DEFAULT_SANS_SERIF_FONT );
		fontAliases.put( CURSIVE, DEFAULT_CURSIVE_FONT );
		fontAliases.put( MONOSPACE, DEFAULT_MONOSPACE_FONT );
		fontAliases.put( FANTASY, DEFAULT_FANTASY_FONT );
	}

	protected void initializeFontEncoding( )
	{
		fontEncodings.put( BaseFont.TIMES_ROMAN, BaseFont.WINANSI );
		fontEncodings.put( BaseFont.HELVETICA, BaseFont.WINANSI );
		fontEncodings.put( BaseFont.COURIER, BaseFont.WINANSI );
		fontEncodings.put( BaseFont.SYMBOL, BaseFont.WINANSI );
		fontEncodings.put( BaseFont.ZAPFDINGBATS, BaseFont.WINANSI );
		//FIXME: code review : move it to fontsConfig.xml
		fontEncodings.put( "Times", BaseFont.WINANSI ); //$NON-NLS-1$
	}

	public String getDefaultPhysicalFont( char c )
	{
		return getPhysicalFont( c, FONT_NAME_ALL_FONTS, DEFAULT_FONT );
	}

	public void addFontPath( String path )
	{
		File file = new File(path);
		if ( file.exists( ) )
		{
			try
			{
				fontPaths.add( file.getCanonicalPath( ) );
			}
			catch ( IOException e )
			{
				logger.log( Level.WARNING, e.getMessage( ) );
			}
		}
	}
	
	public Set getFontPaths()
	{
		return fontPaths;
	}
	
	public String getPhysicalFont( char c, String logicalFont,
			String defaultFont )
	{
		if ( isCompositeFont( logicalFont ) )
		{
			CompositeFont compositeFont = (CompositeFont) compositeFonts.get( logicalFont );
			String font = compositeFont.getCharacterFont( c );
			if ( font != null )
			{
				return font;
			}
		}
		return defaultFont;
	}

	private boolean isCompositeFont( String logicalFont )
	{
		return compositeFonts.containsKey( logicalFont );
	}

	public String getLogicalFont( String fontFamilyName )
	{
		//FIXME: code review : remove last parameter of getMappedFontName.
		String fontName = getMappedFontName( fontFamilyName, fontAliases );
		return fontName == null ? fontFamilyName : fontName;
	}

	public void addCompositeFonts( String fontName, CompositeFont compositeFont )
	{
		compositeFonts.put( fontName, compositeFont );
	}

	/**
	 * Adds font mapping for a block into a composite font.
	 * 
	 * @param fontName
	 *            name of the composite font.
	 * @param blockIndex
	 *            index of the block.
	 * @param fontMappedTo
	 *            fonts for the block.
	 */
	public void addBlockToCompositeFont( String fontName, int blockIndex,
			String fontMappedTo )
	{
		CompositeFont compositeFont = getCompositFont( fontName );
		compositeFont.setBlockFont( blockIndex, fontMappedTo );
	}

	/**
	 * Sets default font for a composite font.
	 * 
	 * @param fontName
	 *            name of the composite font.
	 * @param defaultFont
	 *            default font.
	 */
	public void setDefaultFont( String fontName, String defaultFont )
	{
		CompositeFont compositeFont = getCompositFont( fontName );
		compositeFont.setDefaultFont( defaultFont );
	}

	private CompositeFont getCompositFont( String fontName )
	{
		CompositeFont compositeFont = (CompositeFont) compositeFonts.get( fontName );
		if ( compositeFont == null )
		{
			compositeFont = new CompositeFont( fontName );
			compositeFonts.put( fontName, compositeFont );
		}
		return compositeFont;
	}

	public Map getFontAliases( )
	{
		return fontAliases;
	}

	public void addFontEncoding( HashMap fontEncoding )
	{
		this.fontEncodings.putAll( fontEncoding );
	}

	public void addFontMapping( HashMap fontMapping )
	{
		this.fontAliases.putAll( fontMapping );
	}

	public Map getCompositeFonts( )
	{
		return compositeFonts;
	}

	public Map getFontEncodings( )
	{
		return fontEncodings;
	}
	
	public void reset()
	{
		fontAliases.clear( );
		fontEncodings.clear( );
		compositeFonts.clear( );
		fontPaths.clear( );
	}
	
	private String getMappedFontName( String fontFamilyName, Map fontMap )
	{
		return (String) fontMap.get( fontFamilyName );
	}

	public String getEncoding( String fontFamilyName )
	{
		String encoding = (String) fontEncodings.get( fontFamilyName );
		return ( null == encoding ) ? BaseFont.IDENTITY_H : encoding;
	}

	private HashMap baseFonts = new HashMap( );

	/**
	 * Creates iText BaseFont with the given font family name.
	 * 
	 * @param ffn
	 *            the specified font family name.
	 * @param encoding
	 *            the encoding for the font.
	 * @return the created BaseFont.
	 */
	public BaseFont createFont( String ffn, int fontStyle )
	{
		String key = ffn + fontStyle;
		synchronized ( baseFonts )
		{
			BaseFont font = (BaseFont) baseFonts.get( key );
			if ( font == null )
			{
				String encoding = getEncoding( ffn );
				Font f = null;
				try
				{
					// FIXME: code view verify if BaseFont.NOT_EMBEDDED or
					// BaseFont.EMBEDDED should be used.
					f = FontFactory.getFont( ffn, encoding,
							BaseFont.NOT_EMBEDDED, 14, fontStyle );
					font = f.getBaseFont( );
					if ( font != null )
					{
						baseFonts.put( key, font );
					}
				}
				catch ( Throwable de )
				{
					logger.log( Level.WARNING, de.getLocalizedMessage( ) );
				}
			}
			return font;
		}
	}

	//FIXME: code review : remove it.
	public boolean isCharDefinedInFont( char c, String fontName, int fontStyle )
	{
		BaseFont bf = createFont( fontName, fontStyle );
		return null != bf && bf.charExists( c );
	}

}