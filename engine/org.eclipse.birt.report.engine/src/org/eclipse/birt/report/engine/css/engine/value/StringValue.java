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

/**
 * This class represents string values.
 * 
 * @version $Id: StringValue.java,v 1.5 2005/11/22 09:59:57 wyan Exp $
 */
public class StringValue extends Value
{

	/**
	 * Returns the CSS text associated with the given type/value pair.
	 */
	public static String getCssText( short type, String value )
	{
		if ( value == null )
		{
			return null;
		}

		switch ( type )
		{
			case CSSPrimitiveValue.CSS_URI :
				return "url(" + value + ")";

			case CSSPrimitiveValue.CSS_STRING :
				return value;
		}
		return value;
	}

	/**
	 * The value of the string
	 */
	protected String value;

	/**
	 * The unit type
	 */
	protected short unitType;

	/**
	 * Creates a new StringValue.
	 */
	public StringValue( short type, String s )
	{
		unitType = type;
		value = s;
	}

	/**
	 * The type of the value.
	 */
	public short getPrimitiveType( )
	{
		return unitType;
	}

	/**
	 * Indicates whether some other object is "equal to" this one.
	 * 
	 * @param obj
	 *            the reference object with which to compare.
	 */
	public boolean equals( Object obj )
	{
		if ( obj == null || !( obj instanceof StringValue ) )
		{
			return false;
		}
		StringValue v = (StringValue) obj;
		if ( unitType != v.unitType )
		{
			return false;
		}
		if ( value != null )
		{
			return value.equals( v.value );
		}
		else
		{
			if ( v.value == null )
			{
				return true;
			}
		}
		return false;

	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText( )
	{
		return getCssText( unitType, value );
	}

	/**
	 * This method is used to get the string value.
	 * 
	 * @exception DOMException
	 *                INVALID_ACCESS_ERR: Raised if the value doesn't contain a
	 *                string value.
	 */
	public String getStringValue( ) throws DOMException
	{
		return value;
	}

	/**
	 * Returns a printable representation of this value.
	 */
	public String toString( )
	{
		return getCssText( );
	}
}
