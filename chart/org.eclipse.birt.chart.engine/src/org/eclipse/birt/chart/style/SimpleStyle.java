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
import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * SimpleStyle
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
				setFont( (FontDefinition) EcoreUtil.copy( src.getFont( ) ) );
			}
			if ( src.getColor( ) != null )
			{
				setColor( (ColorDefinition) EcoreUtil.copy( src.getColor( ) ) );
			}
			if ( src.getBackgroundColor( ) != null )
			{
				setBackgroundColor( (ColorDefinition) EcoreUtil.copy( src.getBackgroundColor( ) ) );
			}
			if ( src.getBackgroundImage( ) != null )
			{
				setBackgroundImage( (Image) EcoreUtil.copy( src.getBackgroundImage( ) ) );
			}
			if ( src.getPadding( ) != null )
			{
				setPadding( (Insets) EcoreUtil.copy( src.getPadding( ) ) );
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
			ss.setFont( (FontDefinition) EcoreUtil.copy( font ) );
		}
		if ( color != null )
		{
			ss.setColor( (ColorDefinition) EcoreUtil.copy( color ) );
		}
		if ( backcolor != null )
		{
			ss.setBackgroundColor( (ColorDefinition) EcoreUtil.copy( backcolor ) );
		}
		if ( backimage != null )
		{
			ss.setBackgroundImage( (Image) EcoreUtil.copy( backimage ) );
		}
		if ( padding != null )
		{
			ss.setPadding( (Insets) EcoreUtil.copy( padding ) );
		}

		return ss;
	}

	/**
	 * @param font
	 */
	public void setFont( FontDefinition font )
	{
		this.font = font;
	}

	/**
	 * @param color
	 */
	public void setColor( ColorDefinition color )
	{
		this.color = color;
	}

	/**
	 * @param backcolor
	 */
	public void setBackgroundColor( ColorDefinition backcolor )
	{
		this.backcolor = backcolor;
	}

	/**
	 * @param backimage
	 */
	public void setBackgroundImage( Image backimage )
	{
		this.backimage = backimage;
	}

	/**
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

}
