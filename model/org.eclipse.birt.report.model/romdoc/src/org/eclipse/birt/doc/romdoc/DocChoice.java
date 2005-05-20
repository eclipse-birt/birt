/*
 * Created on May 13, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import org.eclipse.birt.report.model.api.metadata.IChoice;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocChoice
{
	IChoice choice;
	String description;
	
	public DocChoice( IChoice item )
	{
		choice = item;
	}
	
	public String getName( )
	{
		return choice.getName( );
	}
	
	public String getDisplayName( )
	{
		return choice.getDisplayName( );
	}
	
	public String getValue( )
	{
		if ( choice.getValue( ) == null )
			return null;
		return choice.getValue( ).toString( );
	}
	
	public String getDescription( )
	{
		return description;
	}

	public void setDescription( String descrip )
	{
		description = descrip;
	}
}
