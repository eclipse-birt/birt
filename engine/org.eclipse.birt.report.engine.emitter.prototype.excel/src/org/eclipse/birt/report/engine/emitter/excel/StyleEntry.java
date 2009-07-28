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

import java.io.Serializable;

public class StyleEntry implements StyleConstant,Serializable,Cloneable
{
	private static final long serialVersionUID = 6959747237392429540L;

	public static final String ENTRYNAME_HYPERLINK = "Hyperlink";

	public StyleEntry( )
	{
		props = new Object[StyleConstant.COUNT];
	}

	public void setProperty( int id, Object value )
	{
		props[id] = value;
	}

	public Object getProperty( int id )
	{
		return props[id];
	}

	public boolean equals( Object obj )
	{
		if ( obj == null )
		{
			return false;
		}

		if ( !( obj instanceof StyleEntry ) )
		{
			return false;
		}

		if ( obj == this )
		{
			return true;
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
		int code = 0;

		for ( int i = 0; i < StyleConstant.COUNT; i++ )
		{
			int hashCode = props[i] == null ? 0 : props[i].hashCode( );
			code += hashCode * 2 + 1;
				
			if ( Integer.MAX_VALUE == code )
			{
				break;
			}
		}

		return code;
	}

	public static boolean isNull( Object value )
	{
		if ( value == null )
			return true;
		if ( value instanceof String )
			return StyleConstant.NULL.equalsIgnoreCase( (String) value );
		return false;
	}

	public Object clone( ) 
	{
		StyleEntry o = null;
        try{
            o = (StyleEntry)super.clone();
        }catch(CloneNotSupportedException e){
           
        }
        
        for(int i = 0; i < props.length; i++) {
        	o.setProperty( i, getProperty(i) );
        }
        
        return o;        
	}


	public void setName( String name )
	{
		this.name = name;
	}

	public String getName( )
	{
		return name;
	}

	private String name = null;

	private Object[] props = null;
	
}
