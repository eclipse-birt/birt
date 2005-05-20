/*
 * Created on May 10, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocMethod extends DocProperty
{
	String returnText;

	public DocMethod( ElementPropertyDefn propDefn )
	{
		super( propDefn );
	}

	public void setReturnText( String value )
	{
		returnText = value;
	}
	
	public String getReturnText( )
	{
		return returnText;
	}
}
