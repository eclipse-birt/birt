/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public abstract class DocObject
{
	protected String description;
	protected String summary;
	protected String seeAlso;

	public abstract String getName( );
	
	public String makeElementLink( String elementName, String dir )
	{
		StringBuffer link = new StringBuffer( );
		link.append( "<a href=\"" );
		if ( dir == null )
			link.append( "elements/" );
		else if ( ! dir.equals( "elements" ) )
			link.append( "../elements/" );
		link.append( elementName );
		link.append( ".html\">" );
		link.append( elementName );
		link.append( "</a>" );
		return link.toString( );
	}
	
	public String makeStructureLink( IStructureDefn struct, String dir )
	{
		StringBuffer link = new StringBuffer( );
		link.append( "<a href=\"" );
		if ( dir == null )
			link.append( "structs/" );
		else if ( ! dir.equals( "structs" ) )
			link.append( "../structs/" );
		link.append( struct.getName( ) );
		link.append( ".html\">" );
		link.append( struct.getName( ) );
		link.append( "</a>" );
		return link.toString( );
	}
	
	public String makeTypeLink( PropertyType type, String dir )
	{
		StringBuffer link = new StringBuffer( );
		link.append( "<a href=\"" );
		if ( dir != null )
			link.append( "../" );
		link.append( "types.html#" );
		link.append( type.getName( ) );
		link.append( "\">" );
		link.append( type.getName( ) );
		link.append( "</a>" );
		return link.toString( );
	}
	
	public String yesNo( boolean flag )
	{
		return flag ? "Yes" : "No";
	}

	public String getDescription( )
	{
		return description;
	}

	public String getSummary( )
	{
		return summary;
	}

	public String getSeeAlso( )
	{
		return seeAlso;
	}

	public void setDescription( String string )
	{
		description = string;
	}

	public void setSeeAlso( String string )
	{
		seeAlso = string;
	}

	public void setSummary( String string )
	{
		summary = string;
	}
}
