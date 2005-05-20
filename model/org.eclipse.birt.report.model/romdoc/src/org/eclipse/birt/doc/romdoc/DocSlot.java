/*
 * Created on May 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import org.eclipse.birt.report.model.api.metadata.ISlotDefn;
import org.eclipse.birt.report.model.metadata.ElementDefn;
import org.eclipse.birt.report.model.metadata.SlotDefn;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocSlot extends DocObject
{
	ISlotDefn defn;
	String contentInfo;
	
	public DocSlot( ISlotDefn slot )
	{
		defn = slot;
	}
	
	public String getName( )
	{
		return defn.getName( );
	}

	public String getCardinality( )
	{
		return defn.isMultipleCardinality( )? "Multiple" : "Single";
	}

	public String getDisplayName( )
	{
		return defn.getDisplayName( );
	}

	public String getContents( )
	{
		ArrayList list = new ArrayList( );
		
		// The following is a workaround because the meaning of getContentElements
		// was changed in an unfortunate way. See SCR 78805.
		
		list.addAll( ( (SlotDefn) defn).getRealContentElements( ) );
		Collections.sort( list, new ElementComparator( ) );
		Iterator iter = list.iterator( );
		StringBuffer contents = new StringBuffer( );
		while ( iter.hasNext( ) )
		{
			ElementDefn element = (ElementDefn) iter.next( );
			if ( contents.length( ) > 0 )
				contents.append( ", " );
			contents.append( "<a href=\"" );
			contents.append( element.getName( ) );
			contents.append( ".html\">" );
			contents.append( element.getName( ) );
			contents.append( "</a>" );
		}
		if ( defn.isMultipleCardinality( ) )
			contents.insert( 0, "List of " );
		return contents.toString( );
	}
	
	protected class ElementComparator implements Comparator
	{

		/* (non-Javadoc)
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare( Object arg0, Object arg1 )
		{
			ElementDefn e1 = (ElementDefn) arg0;
			ElementDefn e2 = (ElementDefn) arg1;
			return e1.getName( ).compareTo( e2.getName( ) );
		}
		
	}

	public void setContentInfo( String value )
	{
		contentInfo = value;
	}
	
	public String getContentInfo( )
	{
		return contentInfo;
	}

	public String getSince( )
	{
		return defn.getSince( );
	}

	public String getXmlName( )
	{
		return defn.getXmlName( );
	}
	
	public String getStyle( )
	{
		String style = defn.getSelector( );
		if ( style == null )
			return "None";
		
		String target = style;
		if ( style.endsWith( "-n" ) )
		{
			target = style.substring( 0, style.length( ) - 1 ) + "1";
			style = style.substring( 0, style.length( ) - 1 ) + "<i>n</i>";
		}
		
		StringBuffer link = new StringBuffer( );
		link.append( "<a href=\"../styles.html#" );
		link.append( target );
		link.append( "\">" );
		link.append( style );
		link.append( "</a>" );
		return link.toString( );
	}

	public boolean hasStyle( )
	{
		return defn.getSelector( ) != null;
	}

}
