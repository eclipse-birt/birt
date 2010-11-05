/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter.excel;


public class StyleEntry implements StyleConstant
{
	public StyleEntry( StyleEntry entry )
	{
		this( );
		if ( entry == null )
		{
			return;
		}
		for ( int i = 0; i < props.length; i++ )
		{
			props[i] = entry.props[i];
		}
		if ( entry.hashCode != null )
		{
			hashCode = new Integer( entry.hashCode );
		}
	}

	public StyleEntry( )
	{
		props = new Object[StyleConstant.COUNT];
	}

	public void setProperty( int id, Object value )
	{
		props[id] = value;
		hashCode = null;
	}

	public Object getProperty( int id )
	{
		return props[id];
	}

	public boolean equals( Object obj )
	{
		if ( obj == this )
		{
			return true;
		}

		if ( !( obj instanceof StyleEntry ) )
		{
			return false;
		}

		StyleEntry tar = (StyleEntry) obj;

		for ( int i = 0; i < StyleConstant.COUNT; i++ )
		{
			if ( props[i] != null )
			{
				if ( !props[i].equals( tar.getProperty( i ) ) )
				{
					return false;
				}
			}
			else
			{
				if ( props[i] != tar.getProperty( i ) )
				{
					return false;
				}
			}
		}

		return true;
	}
	
	public int hashCode( )
	{
		if ( hashCode == null )
		{
			int code = 0;

			for ( int i = 0; i < StyleConstant.COUNT; i++ )
			{
				int hashCode = props[i] == null ? 0 : props[i].hashCode( );
				code += hashCode * 2 + 1;
			}

			hashCode = new Integer( code );;
		}
		return hashCode.intValue( );
	}

	public static boolean isNull( Object value )
	{
		if ( value == null )
			return true;
		if ( value instanceof String )
			return StyleConstant.NULL.equalsIgnoreCase( (String) value );
		return false;
	}

	public void setIsHyperlink( boolean isHyperlink )
	{
		this.isHyperlink = isHyperlink;
	}

	public boolean isHyperlink( )
	{
		return isHyperlink;
	}

	private boolean isHyperlink = false;

	private Object[] props = null;
	private Integer hashCode;
	
}
