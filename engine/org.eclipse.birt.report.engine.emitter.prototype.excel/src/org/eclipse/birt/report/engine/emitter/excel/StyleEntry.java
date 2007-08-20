
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.Serializable;

public class StyleEntry implements StyleConstant,Serializable
{

	private static final long serialVersionUID = 6959747237392429540L;

	public StyleEntry( )
	{
		props = new String[StyleConstant.COUNT];
	}

	public void setProperty( int id, String value )
	{
		props[id] = value;
	}

	public String getProperty( int id )
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
	
	public boolean isStart()
	{
		return start;
	}
	public void setDatatype(String type)
	{
		this.dataType = type;
	}
	public String getDatatype()
	{
		return dataType;
	}
	public void setStart(boolean start)
	{
		this.start = start;
	}	 

	public int hashCode( )
	{
		int code = 0;

		for ( int i = 0; i < StyleConstant.COUNT; i++ )
		{
			String value = props[i] == null ? NULL : props[i];
			code += value.hashCode( ) * 2 + 1;
				
			if ( Integer.MAX_VALUE == code )
			{
				break;
			}
		}

		return code;
	}

	public static boolean isNull( String value )
	{
		return value == null ? true : StyleConstant.NULL
				.equalsIgnoreCase( value );
	}
	
	private String[] props = null;	
	
	private boolean start = false;
	private String dataType = "String";
}
