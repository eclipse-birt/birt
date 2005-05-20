/*
 * Created on May 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocInheritedProperty extends DocObject
{

	private String name;
	public void setName( String propName )
	{
		name = propName;
	}

	public void setDescription( String string )
	{
		description = string;
	}

	public String getName( )
	{
		return name;
	}

	public String getDescription( )
	{
		return description;
	}

	public boolean isDefined( DocElement element )
	{
		return element.getDefn( ).findProperty( name ) != null;
	}
	
	public boolean isReserved( DocElement element )
	{
		IPropertyDefn prop = element.getDefn( ).findProperty( name );
		if ( prop == null )
			return true;
		String since = ( (PropertyDefn) prop ).getSince( );
		if ( since == null )
			return false;
		return since.equals( "reserved" );
	}

}
