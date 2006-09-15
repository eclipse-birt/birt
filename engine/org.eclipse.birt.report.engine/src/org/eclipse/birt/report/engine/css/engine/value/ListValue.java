/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - modification of Batik's ListValue.java to support BIRT's CSS rules
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine.value;

import org.w3c.dom.DOMException;
import org.w3c.dom.css.CSSPrimitiveValue;
import org.w3c.dom.css.CSSValue;
import org.w3c.dom.css.CSSValueList;

/**
 * This class represents a list of values.
 * 
 * @version $Id: ListValue.java,v 1.3.14.1 2006/09/15 05:33:32 lyu Exp $
 */
public class ListValue extends Value implements CSSValueList
{

	/**
	 * The length of the list.
	 */
	protected int length;

	/**
	 * The items.
	 */
	protected CSSValue[] items = new CSSValue[5];

	/**
	 * The list separator.
	 */
	protected char separator = ',';

	/**
	 * Creates a ListValue.
	 */
	public ListValue( )
	{
	}

	/**
	 * Creates a ListValue with the given separator.
	 */
	public ListValue( char s )
	{
		separator = s;
	}

	/**
	 * Returns the separator used for this list.
	 */
	public char getSeparatorChar( )
	{
		return separator;
	}

	/**
	 * Implements {@link Value#getCssValueType()}.
	 */
	public short getCssValueType( )
	{
		return CSSValue.CSS_VALUE_LIST;
	}

	/**
	 * A string representation of the current value.
	 */
	public String getCssText( )
	{
		StringBuffer sb = new StringBuffer( );
		if ( length > 0 )
		{
			sb.append( getCssText( items[0] ) );
		}
		for ( int i = 1; i < length; i++ )
		{
			sb.append( separator );
			sb.append( getCssText( items[i] ) );
		}
		return sb.toString( );
	}

	protected String getCssText( CSSValue value )
	{
		String cssText = value.getCssText( );
		if ( value.getCssValueType( ) == CSSValue.CSS_PRIMITIVE_VALUE )
		{
			CSSPrimitiveValue pvalue = (CSSPrimitiveValue) value;
			if ( pvalue.getPrimitiveType( ) == CSSPrimitiveValue.CSS_STRING )
			{
				char q = ( cssText.indexOf( '"' ) != -1 ) ? '\'' : '"';
				return q + cssText + q;
			}
		}
		return cssText;
	}

	/**
	 * Implements {@link Value#getLength()}.
	 */
	public int getLength( ) throws DOMException
	{
		return length;
	}

	/**
	 * Implements {@link Value#item(int)}.
	 */
	public CSSValue item( int index ) throws DOMException
	{
		return items[index];
	}

	/**
	 * Returns a printable representation of this value.
	 */
	public String toString( )
	{
		return getCssText( );
	}

	/**
	 * Appends an item to the list.
	 */
	public void append( CSSValue v )
	{
		if ( length == items.length )
		{
			CSSValue[] t = new CSSValue[length * 2];
			for ( int i = 0; i < length; i++ )
			{
				t[i] = items[i];
			}
			items = t;
		}
		items[length++] = v;
	}

	public boolean equals( Object value )
	{
		if ( value instanceof ListValue )
		{
			ListValue l = (ListValue) value;
			if ( l.length == length )
			{
				for ( int i = 0; i < length; i++ )
				{
					CSSValue i1 = items[i];
					CSSValue i2 = l.items[i];
					if ( i1 != i2 )
					{
						if ( i1 == null || !i1.equals( i2 ) )
						{
							return false;
						}
					}
				}
				return true;
			}
		}
		return false;
	}
}
