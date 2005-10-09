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
