
package org.eclipse.birt.report.engine.emitter.excel;

import java.io.Serializable;

class Data implements Serializable, Cloneable
{
	
	public final static int INVALID = -1;

	private static final long serialVersionUID = -316995334044186083L;

	private static int ID = 0;

	String txt;

	int styleId, id;

	StyleEntry style;

	Span span;

	final String url;

	boolean isTxtData = true;

	Data( final String txt, final int styleId, final Span span,
			final String url, final StyleEntry s )
	{
		this.txt = txt;
		this.span = span;
		this.styleId = styleId;
		this.url = url;
		this.style = s;
		id = ID++;
	}

	protected void setNotTxtData( )
	{
		this.isTxtData = false;
	}

	public int hashCode( )
	{
		return id;
	}

	// shallow copy is necessary and sufficient
	protected Object clone( )
	{
		Object o = null;
		try
		{
			o = super.clone( );
		}
		catch ( final CloneNotSupportedException e )
		{
			e.printStackTrace( );
		}
		return o;
	}

	public boolean equals( final Object o )
	{
		if ( o == this )
		{
			return true;
		}
		if ( !( o instanceof Data ) )
		{
			return false;
		}
		final Data data = (Data) o;
		if ( data.id == id )
		{
			return true;
		}
		return false;
	}
}
