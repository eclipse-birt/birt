
package org.eclipse.birt.report.engine.api;

/**
 * RenderOptionDefn
 */
public class RenderOptionDefn
{

	private String key;
	private String value;
	private boolean enabled;

	/**
	 * Constructor
	 * 
	 * @param key
	 *            the key to this option
	 * @param value
	 *            the value of the option
	 */
	public RenderOptionDefn( String key, String value )
	{
		this( key, value, true );
	}

	/**
	 * Constructor
	 * 
	 * @param key
	 *            the key to this option
	 * @param value
	 *            the value of the option
	 * @param enabled
	 *            whether this option is enabled
	 */
	public RenderOptionDefn( String key, String value, boolean enabled )
	{
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
	public String toString( )
	{
		return "RenderOptionDefn [key="
				+ key + ", value=" + value + ", enabled=" + enabled + "]";
	}
}
