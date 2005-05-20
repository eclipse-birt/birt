/*
 * Created on May 9, 2005
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
public class SpecSlot extends SpecObject
{
	public static final int UNKNOWN = 0;
	public static final int SINGLE = 1;
	public static final int MULTIPLE = 2;
	
	public String shortDescrip;				// Doc
	public String contents;					// Doc
	public String xmlElement;				// rom.def
	public int cardinality = UNKNOWN;		// rom.def
	public String styleNames;				// rom.def
}
