/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.eclipse.birt.report.engine.css.engine.value.IdentifierManager;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.InputSource;
import org.w3c.css.sac.LexicalUnit;

public abstract class ShortHandProcessor
{

	abstract void process( StringBuffer buffer, String value, CSSEngine engine );

	private static Map<String, ShortHandProcessor> processorMap = new HashMap<String, ShortHandProcessor>( );

	static String EMPTY_VALUE = "";
	static String COLON = ":";
	static String SEMICOLON = ";";

	static
	{
		processorMap.put( "text-decoration", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						for ( int i = 0; i < vs.length; i++ )
						{
							if ( CSSConstants.CSS_UNDERLINE_VALUE
									.equals( vs[i] ) )
							{
								appendStyle( buffer,
										IStyle.BIRT_TEXT_UNDERLINE_PROPERTY,
										"true" );
							}
							else if ( CSSConstants.CSS_LINE_THROUGH_VALUE
									.equals( vs[i] ) )
							{
								appendStyle( buffer,
										IStyle.BIRT_TEXT_LINETHROUGH_PROPERTY,
										"true" );
							}
							else if ( CSSConstants.CSS_OVERLINE_VALUE
									.equals( vs[i] ) )
							{
								appendStyle( buffer,
										IStyle.BIRT_TEXT_OVERLINE_PROPERTY,
										"true" );
							}

						}
					}
				} );
		processorMap.put( "margin", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						switch ( vs.length )
						{
							case 1 :
								buildMargin( buffer, vs[0], vs[0], vs[0], vs[0] );
								break;
							case 2 :
								buildMargin( buffer, vs[0], vs[1], vs[0], vs[1] );
								break;
							case 3 :
								buildMargin( buffer, vs[0], vs[1], vs[2], vs[1] );
								break;
							case 4 :
								buildMargin( buffer, vs[0], vs[1], vs[2], vs[3] );
								break;
						}
					}
				} );
		processorMap.put( "padding", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						switch ( vs.length )
						{
							case 1 :
								buildPadding( buffer, vs[0], vs[0], vs[0],
										vs[0] );
								break;
							case 2 :
								buildPadding( buffer, vs[0], vs[1], vs[0],
										vs[1] );
								break;
							case 3 :
								buildPadding( buffer, vs[0], vs[1], vs[2],
										vs[1] );
								break;
							case 4 :
								buildPadding( buffer, vs[0], vs[1], vs[2],
										vs[3] );
								break;
						}
					}
				} );
		processorMap.put( "border", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						buildBorder( buffer, getBorderWidth( vs, engine ),
								getBorderColor( vs, engine ), getBorderStyle(
										vs, engine ) );
					}
				} );
		processorMap.put( "border-style", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						buildBorder( buffer, null, null, value );
					}
				} );
		processorMap.put( "border-width", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						buildBorder( buffer, value, null, null );
					}
				} );
		processorMap.put( "border-color", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						buildBorder( buffer, null, value, null );
					}
				} );
		processorMap.put( "border-left", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						buildLeftBorder( buffer, getBorderWidth( vs, engine ),
								getBorderColor( vs, engine ), getBorderStyle(
										vs, engine ) );
					}
				} );
		processorMap.put( "border-right", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						buildRightBorder( buffer, getBorderWidth( vs, engine ),
								getBorderColor( vs, engine ), getBorderStyle(
										vs, engine ) );
					}
				} );
		processorMap.put( "border-top", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						buildTopBorder( buffer, getBorderWidth( vs, engine ),
								getBorderColor( vs, engine ), getBorderStyle(
										vs, engine ) );
					}
				} );

		processorMap.put( "border-bottom", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						buildBottomBorder( buffer,
								getBorderWidth( vs, engine ), getBorderColor(
										vs, engine ), getBorderStyle( vs,
										engine ) );
					}
				} );

		processorMap.put( "background", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						String[] vs = value.split( " " );
						String url = getBackgroundImage( vs, engine );
						if ( !EMPTY_VALUE.equals( url ) )
						{
							appendStyle( buffer,
									CSSConstants.CSS_BACKGROUND_IMAGE_PROPERTY,
									url );
						}
						String color = getBackgroundColor( vs, engine );
						if ( color != null )
						{
							appendStyle( buffer,
									CSSConstants.CSS_BACKGROUND_COLOR_PROPERTY,
									color );
						}

					}
				} );

		processorMap.put( "font", //$NON-NLS-1$
				new ShortHandProcessor( ) {

					public void process( StringBuffer buffer, String value,
							CSSEngine engine )
					{
						ArrayList<String> vl = new ArrayList<String>( );
						Pattern pattern = Pattern
								.compile( "((?:(?:\"[^\",]+\")|(?:[^\",\\s]+))(?:,\\s*(?:(?:\"[^\",]+\")|(?:[^\",\\s]+)))*)" );
						Matcher matcher = pattern.matcher( value );
						while ( matcher.find( ) )
						{
							vl.add( matcher.group( 1 ) );
						}
						String[] vs = new String[vl.size( )];
						vl.toArray( vs );
						switch ( vs.length )
						{
							case 0 :
								break;
							case 1 :
								appendStyle( buffer,
										CSSConstants.CSS_FONT_FAMILY_PROPERTY,
										vs[0] );
								break;
							case 2 :
								appendStyle( buffer,
										CSSConstants.CSS_FONT_FAMILY_PROPERTY,
										vs[0] );
								buildFontSize( buffer, vs[1], engine );
								break;
							default :
								appendStyle( buffer,
										CSSConstants.CSS_FONT_FAMILY_PROPERTY,
										vs[vs.length - 1] );
								buildFontSize( buffer, vs[vs.length - 2],
										engine );
								String[] values = new String[vs.length - 2];
								System.arraycopy( vs, 0, values, 0,
										vs.length - 2 );
								buildFontStyle( buffer, values, engine );
								break;

						}
					}
				} );

	}

	protected static void appendStyle( StringBuffer buffer, String name,
			String value )
	{
		if ( value == null )
		{
			return;
		}
		buffer.append( name );
		buffer.append( COLON );
		buffer.append( value );
		buffer.append( SEMICOLON );
	}

	protected void buildFontStyle( StringBuffer buffer, String[] vs,
			CSSEngine engine )
	{
		for ( int i = 0; i < vs.length; i++ )
		{
			LexicalUnit u = getUnit( vs[i], engine );
			if ( u != null )
			{
				if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
				{
					if ( isIdentifier( vs[i], IStyle.STYLE_FONT_STYLE, engine ) )
					{
						appendStyle( buffer,
								CSSConstants.CSS_FONT_STYLE_PROPERTY, vs[i] );
						continue;
					}
					if ( isIdentifier( vs[i], IStyle.STYLE_FONT_WEIGHT, engine ) )
					{
						appendStyle( buffer,
								CSSConstants.CSS_FONT_WEIGHT_PROPERTY, vs[i] );
						continue;
					}
				}
				else if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_INTEGER )
				{
					if ( CSSConstants.CSS_100_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_200_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_300_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_400_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_500_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_600_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_700_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_800_VALUE.equals( vs[i] )
							|| CSSConstants.CSS_900_VALUE.equals( vs[i] ) )
					{
						appendStyle( buffer,
								CSSConstants.CSS_FONT_WEIGHT_PROPERTY, vs[i] );
						continue;
					}
				}

			}
		}
	}

	protected void buildFontSize( StringBuffer buffer, String value,
			CSSEngine engine )
	{
		if ( value != null && value.length( ) > 0 )
		{
			String[] ss = value.split( "/" );
			if ( ss.length == 1 )
			{
				appendStyle( buffer, CSSConstants.CSS_FONT_SIZE_PROPERTY, value );
			}
			else
			{
				appendStyle( buffer, CSSConstants.CSS_FONT_SIZE_PROPERTY, ss[0] );
				appendStyle( buffer, CSSConstants.CSS_LINE_HEIGHT_PROPERTY,
						ss[1] );
			}
		}
	}

	protected LexicalUnit getUnit( String value, CSSEngine engine )
	{
		LexicalUnit u = null;
		try
		{
			u = engine.getParser( ).parsePropertyValue(
					new InputSource( new StringReader( value ) ) );
		}
		catch ( CSSException e )
		{
			e.printStackTrace( );
		}
		catch ( IOException e )
		{
			e.printStackTrace( );
		}
		return u;
	}

	protected String getBackgroundColor( String[] values, CSSEngine engine )
	{
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );
			if ( u != null )
			{
				if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_RGBCOLOR )
				{
					return values[i];
				}
				else if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
				{
					if ( isIdentifier( values[i], IStyle.STYLE_COLOR, engine ) )
					{
						return values[i];
					}
				}
			}
		}
		return null;
	}

	protected String[] getBackgroundRepeat( String[] values, CSSEngine engine )
	{
		String[] result = new String[]{CSSConstants.CSS_REPEAT_VALUE,
				CSSConstants.CSS_REPEAT_VALUE};
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );
			if ( u != null && u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
			{
				if ( isIdentifier( values[i], IStyle.STYLE_BACKGROUND_REPEAT,
						engine ) )
				{
					result[0] = values[i];
					if ( i < values.length - 1 )
					{
						u = getUnit( values[i + 1], engine );
						if ( u != null
								&& u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
						{
							if ( isIdentifier( values[i + 1],
									IStyle.STYLE_BACKGROUND_REPEAT, engine ) )
							{
								result[1] = values[i + 1];
								return result;
							}
						}
					}
					result[1] = values[i];
				}
			}
		}
		return result;

	}

	protected boolean isIdentifier( String value, int index, CSSEngine engine )
	{
		if ( value != null )
		{
			IdentifierManager im = (IdentifierManager) engine
					.getPropertyManagerFactory( ).getValueManager( index );
			if ( im.getIdentifiers( ).get( value.toLowerCase( ).intern( ) ) != null )
			{
				return true;
			}
		}
		return false;
	}

	protected String getBackgroundImage( String[] values, CSSEngine engine )
	{
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );
			if ( u != null && u.getLexicalUnitType( ) == LexicalUnit.SAC_URI )
			{
				return values[i];
			}
		}
		return EMPTY_VALUE;
	}

	protected String getBorderStyle( String[] values, CSSEngine engine )
	{
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );

			if ( u != null && u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
			{
				if ( isIdentifier( values[i], IStyle.STYLE_BORDER_TOP_STYLE,
						engine ) )
				{
					return values[i];
				}
			}
		}
		return CSSConstants.CSS_SOLID_VALUE;
	}

	protected String getBorderColor( String[] values, CSSEngine engine )
	{
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );
			if ( u != null )
			{
				if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_RGBCOLOR )
				{
					return values[i];
				}
				else if ( u.getLexicalUnitType( ) == LexicalUnit.SAC_IDENT )
				{
					if ( isIdentifier( values[i], IStyle.STYLE_COLOR, engine ) )
					{
						return values[i];
					}
				}
			}
		}
		return CSSConstants.CSS_BLACK_VALUE;
	}

	protected String getBorderWidth( String[] values, CSSEngine engine )
	{
		for ( int i = 0; i < values.length; i++ )
		{
			LexicalUnit u = getUnit( values[i], engine );
			if ( u != null )
			{
				int type = u.getLexicalUnitType( );
				if ( type >= LexicalUnit.SAC_EM
						&& type <= LexicalUnit.SAC_PERCENTAGE )
				{
					return values[i];
				}
				else if ( type == LexicalUnit.SAC_IDENT )
				{
					if ( CSSConstants.CSS_MEDIUM_VALUE.equals( values[i] )
							|| CSSConstants.CSS_THICK_VALUE.equals( values[i] )
							|| CSSConstants.CSS_THIN_VALUE.equals( values[i] ) )
					{
						return values[i];
					}
				}
			}
		}
		return CSSConstants.CSS_MEDIUM_VALUE;
	}

	public ShortHandProcessor getProcessor( String name )
	{
		return processorMap.get( name );
	}

	protected void buildMargin( StringBuffer buffer, String top, String right,
			String bottom, String left )
	{
		appendStyle( buffer, CSSConstants.CSS_MARGIN_TOP_PROPERTY, top );
		appendStyle( buffer, CSSConstants.CSS_MARGIN_RIGHT_PROPERTY, right );
		appendStyle( buffer, CSSConstants.CSS_MARGIN_BOTTOM_PROPERTY, bottom );
		appendStyle( buffer, CSSConstants.CSS_MARGIN_LEFT_PROPERTY, left );
	}

	protected void buildPadding( StringBuffer buffer, String top, String right,
			String bottom, String left )
	{
		appendStyle( buffer, CSSConstants.CSS_PADDING_TOP_PROPERTY, top );
		appendStyle( buffer, CSSConstants.CSS_PADDING_RIGHT_PROPERTY, right );
		appendStyle( buffer, CSSConstants.CSS_PADDING_BOTTOM_PROPERTY, bottom );
		appendStyle( buffer, CSSConstants.CSS_PADDING_LEFT_PROPERTY, left );
	}

	protected void buildBorderWidth( StringBuffer buffer, String width )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_WIDTH_PROPERTY, width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_WIDTH_PROPERTY,
				width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_WIDTH_PROPERTY, width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_WIDTH_PROPERTY,
				width );
	}

	protected void buildBorderStyle( StringBuffer buffer, String style )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_STYLE_PROPERTY, style );
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_STYLE_PROPERTY,
				style );
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_STYLE_PROPERTY, style );
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_STYLE_PROPERTY,
				style );
	}

	protected void buildBorderColor( StringBuffer buffer, String color )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_COLOR_PROPERTY, color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_COLOR_PROPERTY,
				color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_COLOR_PROPERTY, color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_COLOR_PROPERTY,
				color );
	}

	protected void buildRightBorder( StringBuffer buffer, String width,
			String color, String style )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_WIDTH_PROPERTY,
				width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_COLOR_PROPERTY,
				color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_RIGHT_STYLE_PROPERTY,
				style );
	}

	protected void buildBottomBorder( StringBuffer buffer, String width,
			String color, String style )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_WIDTH_PROPERTY,
				width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_COLOR_PROPERTY,
				color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_BOTTOM_STYLE_PROPERTY,
				style );
	}

	protected void buildLeftBorder( StringBuffer buffer, String width,
			String color, String style )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_WIDTH_PROPERTY, width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_COLOR_PROPERTY, color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_LEFT_STYLE_PROPERTY, style );
	}

	protected void buildTopBorder( StringBuffer buffer, String width,
			String color, String style )
	{
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_WIDTH_PROPERTY, width );
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_COLOR_PROPERTY, color );
		appendStyle( buffer, CSSConstants.CSS_BORDER_TOP_STYLE_PROPERTY, style );
	}

	protected void buildBorder( StringBuffer buffer, String width,
			String color, String style )
	{
		buildTopBorder( buffer, width, color, style );
		buildBottomBorder( buffer, width, color, style );
		buildLeftBorder( buffer, width, color, style );
		buildRightBorder( buffer, width, color, style );
	}

	public static void process( StringBuffer buffer, String name, String value,
			CSSEngine engine )
	{
		ShortHandProcessor p = processorMap.get( name );
		if ( p != null )
		{
			p.process( buffer, value, engine );
		}
		else
		{
			appendStyle( buffer, name, value );
		}
	}

}
