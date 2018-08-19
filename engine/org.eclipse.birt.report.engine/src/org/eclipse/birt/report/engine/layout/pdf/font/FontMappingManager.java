/*******************************************************************************
 * Copyright (c) 2004,2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.font;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import com.itextpdf.text.pdf.BaseFont;

public class FontMappingManager
{

	/** all fonts key */
	public static final String FONT_NAME_ALL_FONTS = "all-fonts";

	/** default fonts */
	public static final String DEFAULT_FONT = BaseFont.TIMES_ROMAN;

	private FontMappingManagerFactory factory;

	private FontMappingManager parent;

	private Map fontEncodings = new HashMap( );

	private Map searchSequences = new HashMap( );

	/** The font-family replacement */
	private Map fontAliases = new HashMap( );

	/**
	 * composite fonts
	 */
	private Map compositeFonts = new HashMap( );

	FontMappingManager( FontMappingManagerFactory factory,
			FontMappingManager parent, FontMappingConfig config, Locale locale )
	{
		this.factory = factory;
		this.parent = parent;
		if ( parent != null )
		{
			this.searchSequences.putAll( parent.getSearchSequences( ) );
			this.fontAliases.putAll( parent.getFontAliases( ) );
			this.fontEncodings.putAll( parent.getFontEncodings( ) );
			this.compositeFonts.putAll( parent.getCompositeFonts( ) );
		}
		this.fontEncodings.putAll( config.fontEncodings );
		this.searchSequences.putAll( config.searchSequences );
		this.fontAliases.putAll( config.fontAliases );

		String[] sequence = getSearchSequence( locale );
		Iterator iter = config.compositeFonts.entrySet( ).iterator( );
		while ( iter.hasNext( ) )
		{
			Map.Entry entry = (Map.Entry) iter.next( );
			String fontName = (String) entry.getKey( );
			CompositeFontConfig fontConfig = (CompositeFontConfig) entry
					.getValue( );
			CompositeFont font = factory.createCompositeFont( this, fontConfig,
					sequence );
			compositeFonts.put( fontName, font );
		}
	}

	public FontMappingManager getParent( )
	{
		return parent;
	}

	public Map getFontEncodings( )
	{
		return fontEncodings;
	}

	public Map getFontAliases( )
	{
		return fontAliases;
	}

	public Map getSearchSequences( )
	{
		return searchSequences;
	}

	public Map getCompositeFonts( )
	{
		return compositeFonts;
	}

	protected String[] getSearchSequence( Locale locale )
	{
		StringBuffer sb = new StringBuffer( );
		String[] localeKeys = new String[3];
		localeKeys[2] = sb.append( locale.getLanguage( ) ).toString( );
		localeKeys[1] = sb.append( '_' ).append( locale.getCountry( ) )
				.toString( );
		localeKeys[0] = sb.append( '_' ).append( locale.getVariant( ) )
				.toString( );
		for ( int i = 0; i < localeKeys.length; i++ )
		{
			String[] sequence = (String[]) searchSequences.get( localeKeys[i] );
			if ( sequence != null )
			{
				return sequence;
			}
		}
		return null;
	}

	public CompositeFont getCompositeFont( String name )
	{
		return (CompositeFont) compositeFonts.get( name );
	}

	public String getDefaultPhysicalFont( char c )
	{
		CompositeFont compositeFont = (CompositeFont) compositeFonts
				.get( FONT_NAME_ALL_FONTS );
		if ( compositeFont != null )
		{
			String font = compositeFont.getUsedFont( c );
			if ( font != null )
			{
				return font;
			}
			return compositeFont.getDefaultFont( );
		}
		return null;
	}

	public String getAliasedFont( String fontAlias )
	{
		String alias = (String) fontAliases.get( fontAlias );
		if ( alias != null )
		{
			return alias;
		}
		return fontAlias;
	}

	/**
	 * Creates iText BaseFont with the given font family name.
	 * 
	 * @param fontFamily
	 *            the specified font family name.
	 * @param style
	 *            font style
	 * @return the created BaseFont.
	 */
	public BaseFont createFont( String fontFamily, int fontStyle )
	{
		return factory.createFont( fontFamily, fontStyle );
	}
}