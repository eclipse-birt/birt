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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.ir.DimensionType;
import org.w3c.dom.Element;

public abstract class PropertiesProcessor implements HTMLConstants
{

	/** the possible values for property SIZE of HTML element FONT */
	private static String[] FONT_SIZE = new String[]{"7.5pt", //$NON-NLS-1$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$//$NON-NLS-2$
			"7.5pt", "10pt", //$NON-NLS-1$ //$NON-NLS-2$
			"7.5pt", "7.5pt", //$NON-NLS-1$ //$NON-NLS-2$
			"10pt", "12pt", //$NON-NLS-1$ //$NON-NLS-2$
			"13.8pt", "18pt", //$NON-NLS-1$//$NON-NLS-2$
			"23pt", "36pt"}; //$NON-NLS-1$//$NON-NLS-2$

	public final static HashMap<String, String[]> tagPropertiesMap = new HashMap<String, String[]>( );

	abstract void process( String value, StyleProperties sp );
	private static Map<String, PropertiesProcessor> properties2Style = new HashMap<String, PropertiesProcessor>( );

	public static void process( String[] properties, Element ele,
			StyleProperties sp )
	{
		for ( int i = 0; i < properties.length; i++ )
		{
			PropertiesProcessor p2s = properties2Style.get( properties[i] );
			if ( p2s != null )
			{
				String value = ele.getAttribute( properties[i] );
				if ( value != null && value.length( ) > 0 )
				{
					p2s.process( value, sp );
				}
			}
		}
	}

	public static void process( String property, String value,
			StyleProperties sp )
	{
		PropertiesProcessor p2s = properties2Style.get( property );
		if ( p2s != null )
		{
			if ( value != null && value.length( ) > 0 )
			{
				p2s.process( value, sp );
			}
		}
	}

	static
	{
		properties2Style.put( "cellpadding", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						try
						{
							int size = Integer.parseInt( value ); //$NON-NLS-1$
							String padding = size + "px";
							IStyle style = sp.getStyle( );
							style.setPadding( padding );
						}
						catch ( Exception e )
						{

						}
					}
				} );
		properties2Style.put( "background", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setBackground( value );
					}
				} );
		properties2Style.put( "size", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						try
						{
							int size = Integer.parseInt( value ); //$NON-NLS-1$
							sp.getStyle( ).setFontSize(
									FONT_SIZE[size + 7] + "pt" );
						}
						catch ( Exception e )
						{

						}
					}
				} );

		properties2Style.put( "text", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setColor( value );
					}
				} );

		properties2Style.put( "color", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setColor( value );
					}
				} );

		properties2Style.put( "bgcolor", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setBackgroundColor( value );
					}
				} );
		properties2Style.put( "border", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						try
						{
							// FIXME
							int size = Integer.parseInt( value ); //$NON-NLS-1$
							String width = size + "px";
							IStyle style = sp.getStyle( );
							style.setBorderTopWidth( width );
							style.setBorderBottomWidth( width );
							style.setBorderLeftWidth( width );
							style.setBorderRightWidth( width );
							style.setBorderTopStyle( "solid" );
							style.setBorderBottomStyle( "solid" );
							style.setBorderLeftStyle( "solid" );
							style.setBorderRightStyle( "solid" );
						}
						catch ( Exception e )
						{

						}
					}
				} );
		properties2Style.put( "face", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setFontFamily( value );
					}
				} );
		properties2Style.put( "align", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setTextAlign( value );
					}
				} );

		properties2Style.put( "valign", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						sp.getStyle( ).setVerticalAlign( value );
					}
				} );

		properties2Style.put( "width", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						if ( value != null && value.length( ) > 0 )
						{
							DimensionType d = DimensionType.parserUnit( value );
							if ( d != null )
							{
								sp.addProperty( StyleProperties.WIDTH, d );
							}
						}
					}
				} );
		properties2Style.put( "height", //$NON-NLS-1$
				new PropertiesProcessor( ) {

					public void process( String value, StyleProperties sp )
					{
						if ( value != null && value.length( ) > 0 )
						{
							DimensionType d = DimensionType.parserUnit( value );
							if ( d != null )
							{
								sp.addProperty( StyleProperties.HEIGHT, d );
							}
						}
					}
				} );

	}

}
