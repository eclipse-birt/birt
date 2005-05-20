/*
 * Created on May 7, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.eclipse.birt.doc.legacy;

import java.util.ArrayList;

/**
 * @author Paul Rogers
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SpecProperty extends SpecObject
{
	public String shortDescrip;			// Doc
	public String jsType;				// rom.def
	public String defaultValue;			// rom.def
	public int runtimeSettable;			// rom.def
	public int isArray;					// rom.def
	public int hidden;					// rom.def
	public String romType;				// rom.def
	public int inherited;				// rom.def
	public String exprType;				// rom.def
	public String exprContext;			// rom.def
	public ArrayList choices = new ArrayList( );
	public int required;				// rom.def
	
	/**
	 * @param choice
	 */
	public void addChoice( SpecChoice choice )
	{
		choices.add( choice );
	}
}
