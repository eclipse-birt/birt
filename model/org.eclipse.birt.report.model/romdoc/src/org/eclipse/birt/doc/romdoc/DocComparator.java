/*
 * Created on May 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.romdoc;

import java.util.Comparator;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DocComparator implements Comparator
{

	/* (non-Javadoc)
	 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
	 */
	public int compare( Object arg0, Object arg1 )
	{
		DocObject obj1 = (DocObject) arg0;
		DocObject obj2 = (DocObject) arg1;
		return obj1.getName( ).compareTo( obj2.getName( ) );
	}

}
