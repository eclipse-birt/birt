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

package org.eclipse.birt.chart.style;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;

/**
 * A default implementaitn for IStyle.
 */
public final class SimpleStyle implements IStyle
{

	private FontDefinition font;

	private ColorDefinition color;

	private ColorDefinition backcolor;

	private Image backimage;

	private Insets padding;

	/**
	 * The constructor.
	 */
	public SimpleStyle( )
	{
		super( );
	}

	/**
	 * The constructor.
	 * 
	 * @param font
	 * @param backcolor
	 * @param backimage
	 * @param padding
	 */
	public SimpleStyle( FontDefinition font, ColorDefinition color,
			ColorDefinition backcolor, Image backimage, Insets padding )
	{
		super( );

		setFont( font );
		setColor( color );
		setBackgroundColor( backcolor );
		setBackgroundImage( backimage );
		setPadding( padding );
	}

	/**
	 * The constructor.
	 * 
	 * @param src
	 */
	public SimpleStyle( IStyle src )
	{
		super( );

		if ( src != null )
		{
			if ( src.getFont( ) != null )
			{
				setFont( src.getFont( ).copyInstance( ) );
			}
			if ( src.getColor( ) != null )
			{
				setColor( src.getColor( ).copyInstance( ) );
			}
			if ( src.getBackgroundColor( ) != null )
			{
				setBackgroundColor( src.getBackgroundColor( ).copyInstance( ) );
			}
			if ( src.getBackgroundImage( ) != null )
			{
				setBackgroundImage( src.getBackgroundImage( ).copyInstance( ) );
			}
			if ( src.getPadding( ) != null )
			{
				setPadding( src.getPadding( ).copyInstance( ) );
			}
		}
	}

	/**
	 * Returns a copy of current instance.
	 * 
	 * @return
	 */
	public SimpleStyle copy( )
	{
		SimpleStyle ss = new SimpleStyle( );

		if ( font != null )
		{
			ss.setFont( font.copyInstance( ) );
		}
		if ( color != null )
		{
			ss.setColor( color.copyInstance( ) );
		}
		if ( backcolor != null )
		{
			ss.setBackgroundColor( backcolor.copyInstance( ) );
		}
		if ( backimage != null )
		{
			ss.setBackgroundImage( backimage.copyInstance( ) );
		}
		if ( padding != null )
		{
			ss.setPadding( padding.copyInstance( ) );
		}

		return ss;
	}

	/**
	 * Sets the font of current style.
	 * 
	 * @param font
	 */
	public void setFont( FontDefinition font )
	{
		this.font = font;
	}

	/**
	 * Sets the color of current style.
	 * 
	 * @param color
	 */
	public void setColor( ColorDefinition color )
	{
		this.color = color;
	}

	/**
	 * Sets the background color of current style.
	 * 
	 * @param backcolor
	 */
	public void setBackgroundColor( ColorDefinition backcolor )
	{
		this.backcolor = backcolor;
	}

	/**
	 * Sets the background image of current style.
	 * 
	 * @param backimage
	 */
	public void setBackgroundImage( Image backimage )
	{
		this.backimage = backimage;
	}

	/**
	 * Sets the padding of current style.
	 * 
	 * @param padding
	 */
	public void setPadding( Insets padding )
	{
		this.padding = padding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyle#getFont()
	 */
	public FontDefinition getFont( )
	{
		return font;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyle#getBackgroundColor()
	 */
	public ColorDefinition getBackgroundColor( )
	{
		return backcolor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyle#getBackgroundImage()
	 */
	public Image getBackgroundImage( )
	{
		return backimage;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyle#getPadding()
	 */
	public Insets getPadding( )
	{
		return padding;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.style.IStyle#getColor()
	 */
	public ColorDefinition getColor( )
	{
		return color;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode( )
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ( ( backcolor == null ) ? 0 : backcolor.hashCode( ) );
		result = prime
				* result
				+ ( ( backimage == null ) ? 0 : backimage.hashCode( ) );
		result = prime * result + ( ( color == null ) ? 0 : color.hashCode( ) );
		result = prime * result + ( ( font == null ) ? 0 : font.hashCode( ) );
		result = prime
				* result
				+ ( ( padding == null ) ? 0 : padding.hashCode( ) );
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass( ) != obj.getClass( ) )
			return false;
		SimpleStyle other = (SimpleStyle) obj;
		if ( backcolor == null )
		{
			if ( other.backcolor != null )
				return false;
		}
		else if ( !backcolor.equals( other.backcolor ) )
			return false;
		if ( backimage == null )
		{
			if ( other.backimage != null )
				return false;
		}
		else if ( !backimage.equals( other.backimage ) )
			return false;
		if ( color == null )
		{
			if ( other.color != null )
				return false;
		}
		else if ( !color.equals( other.color ) )
			return false;
		if ( font == null )
		{
			if ( other.font != null )
				return false;
		}
		else if ( !font.equals( other.font ) )
			return false;
		if ( padding == null )
		{
			if ( other.padding != null )
				return false;
		}
		else if ( !padding.equals( other.padding ) )
			return false;
		return true;
	}

}
