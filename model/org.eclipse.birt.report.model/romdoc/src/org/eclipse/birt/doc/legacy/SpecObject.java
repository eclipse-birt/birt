/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.legacy;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecObject
{
	public static final int TRI_UNKNOWN = 0;
	public static final int TRI_TRUE = 1;
	public static final int TRI_FALSE = 2;
	
	public String name;				// Keys docs, rom.def
	public String displayName;		// Doc
	public String description;		// Doc
	public String since;			// rom.def
	public String seeAlso;			// Doc
	public String summary;			// Doc
	public String issues;			// Doc
	
	public void addIssue( String issue )
	{
		if ( issues == null )
			issues = "";
		issues += "<p>" + issue + "</p>\n";
	}
}
