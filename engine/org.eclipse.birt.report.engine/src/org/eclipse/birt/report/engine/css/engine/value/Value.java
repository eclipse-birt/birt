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
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.Counter;
import org.w3c.dom.css.RGBColor;
import org.w3c.dom.css.Rect;

/**
 * This class provides an abstract implementation of the Value interface.
 * 
 * @version $Id: Value.java,v 1.2 2005/11/22 09:59:57 wyan Exp $
 */
public abstract class Value implements CSSValue, CSSPrimitiveValue
{

	/**
	 * Implements {@link Value#getCssValueType()}.
	 */
	public short getCssValueType( )
	{
		return CSSValue.CSS_PRIMITIVE_VALUE;
	}

	public void setCssText( String cssText ) throws DOMException
	{
		throw createDOMException( );
	}

	public short getPrimitiveType( )
	{
		throw createDOMException( );
	}

	public void setFloatValue( short unitType, float floatValue )
			throws DOMException
	{
		throw createDOMException( );
	}

	public float getFloatValue( short unitType ) throws DOMException
	{
		throw createDOMException( );
	}

	public float getFloatValue( ) throws DOMException
	{
		throw createDOMException( );
	}

	public void setStringValue( short stringType, String stringValue )
			throws DOMException
	{
		throw createDOMException( );
	}

	public String getStringValue( ) throws DOMException
	{
		throw createDOMException( );
	}

	public Counter getCounterValue( ) throws DOMException
	{
		throw createDOMException( );
	}

	public Rect getRectValue( ) throws DOMException
	{
		throw createDOMException( );
	}

	public RGBColor getRGBColorValue( ) throws DOMException
	{
		throw createDOMException( );
	}

	public String getCssText( )
	{
		throw createDOMException( );
	}

	/**
	 * Creates an INVALID_ACCESS_ERR exception.
	 */
	protected DOMException createDOMException( )
	{
		Object[] p = new Object[]{new Integer( getCssValueType( ) )};
		String s = Messages.formatMessage( "invalid.value.access", p );
		return new DOMException( DOMException.INVALID_ACCESS_ERR, s );
	}

}
