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

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

/**
 * This class represents RGB colors.
 * 
 * @author <a href="mailto:stephane@hillion.org">Stephane Hillion</a>
 * @version $Id: RGBColorValue.java,v 1.1 2005/11/11 06:26:44 wyan Exp $
 */
public class RGBColorValue extends Value implements RGBColor
{

	/**
	 * The red component.
	 */
	protected CSSPrimitiveValue red;

	/**
	 * The green component.
	 */
	protected CSSPrimitiveValue green;

	/**
	 * The blue component.
	 */
	protected CSSPrimitiveValue blue;

	/**
	 * Creates a new RGBColorValue.
	 */
	public RGBColorValue( CSSPrimitiveValue r, CSSPrimitiveValue g,
			CSSPrimitiveValue b )
	{
		red = r;
		green = g;
		blue = b;
	}

	/**
	 * The type of the value.
	 */
	public short getPrimitiveType( )
	{
		return CSSPrimitiveValue.CSS_RGBCOLOR;
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText( )
	{
		return "rgb(" + red.getCssText( ) + ", " + green.getCssText( ) + ", "
				+ blue.getCssText( ) + ")";
	}

	/**
	 * Implements {@link Value#getRed()}.
	 */
	public CSSPrimitiveValue getRed( ) throws DOMException
	{
		return red;
	}

	/**
	 * Implements {@link Value#getGreen()}.
	 */
	public CSSPrimitiveValue getGreen( ) throws DOMException
	{
		return green;
	}

	/**
	 * Implements {@link Value#getBlue()}.
	 */
	public CSSPrimitiveValue getBlue( ) throws DOMException
	{
		return blue;
	}

	/**
	 * Returns a printable representation of the color.
	 */
	public String toString( )
	{
		return getCssText( );
	}
}
