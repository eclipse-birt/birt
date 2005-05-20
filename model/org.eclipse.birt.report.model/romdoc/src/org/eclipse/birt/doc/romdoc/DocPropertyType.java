/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocPropertyType
{
	PropertyType defn;
	String summary;
	String description;
	String seeAlso;

	public DocPropertyType( PropertyType type )
	{
		defn = type;
	}
	
	public void setSummary( String string )
	{
		summary = string;
	}

	public void setSeeAlso( String string )
	{
		seeAlso = string;
	}

	public void setDescription( String string )
	{
		description = string;
	}

	public String getName( )
	{
		return defn.getName( );
	}

	public String getSummary( )
	{
		return summary;
	}

	public String getDisplayName( )
	{
		return defn.getDisplayName( );
	}

	public String getXmlName( )
	{
		return defn.getName( );
	}

	public String getJSDesignType( )
	{
		// TODO: Get this from the model when available.
		
		return null;
	}

	public String getJSRuntimeType( )
	{
		// TODO: Get this from the model when available.
		
		return null;
	}

	public String getDescription( )
	{
		return description;
	}

	public String getSeeAlso( )
	{
		return seeAlso;
	}

	public String getSince( )
	{
		// TODO: Get this from the model when it is supported.
		
		String name = getName( );
		if ( name.equals( "column" )  ||  name.equals( "variant" ) )
			return "reserved";
		
		return "1.0";
	}
}
