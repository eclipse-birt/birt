package org.eclipse.birt.report.engine.api;

/**
 * RenderOptionDefn
 */
public class RenderOptionDefn
{
	private String key;
	private String value;
	private boolean enabled;
	
	public RenderOptionDefn( String key, String value, boolean enabled )
	{
		super( );
		this.key = key;
		this.value = value;
		this.enabled = enabled;
	}

	public String getKey( )
	{
		return key;
	}
	
	public String getValue( )
	{
		return value;
	}
	
	public boolean isEnabled( )
	{
		return enabled;
	}

	@Override
	public int hashCode( )
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ( enabled ? 1231 : 1237 );
		result = prime * result + ( ( key == null ) ? 0 : key.hashCode( ) );
		result = prime * result + ( ( value == null ) ? 0 : value.hashCode( ) );
		return result;
	}

	@Override
	public boolean equals( Object obj )
	{
		if ( this == obj )
			return true;
		if ( obj == null )
			return false;
		if ( getClass( ) != obj.getClass( ) )
			return false;
		RenderOptionDefn other = (RenderOptionDefn) obj;
		if ( enabled != other.enabled )
			return false;
		if ( key == null )
		{
			if ( other.key != null )
				return false;
		}
		else if ( !key.equals( other.key ) )
			return false;
		if ( value == null )
		{
			if ( other.value != null )
				return false;
		}
		else if ( !value.equals( other.value ) )
			return false;
		return true;
	}

	@Override
	public String toString( )
	{
		return "RenderOptionDefn [key="
				+ key + ", value=" + value + ", enabled=" + enabled + "]";
	}
}
