/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Apache - initial API and implementation
 *  Actuate Corporation - changed by Actuate
 *******************************************************************************/
/*

   Copyright 1999-2003  The Apache Software Foundation 

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.

*/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.RGBColor;

/**
 * This class represents RGB colors.
 * 
 * @version $Id: RGBColorValue.java,v 1.4 2005/12/06 03:12:42 gliu Exp $
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

	public RGBColor getRGBColorValue( ) throws DOMException
	{
		return this;
	}

	/**
	 * Returns a printable representation of the color.
	 */
	public String toString( )
	{
		return getCssText( );
	}

	public boolean equals( Object value )
	{
		if ( value instanceof RGBColorValue )
		{
			RGBColorValue color = (RGBColorValue) value;
			if ( red.equals( color.red ) && blue.equals( color.blue )
					&& green.equals( color.green ) )
			{
				return true;
			}
		}
		return false;

	}
}
