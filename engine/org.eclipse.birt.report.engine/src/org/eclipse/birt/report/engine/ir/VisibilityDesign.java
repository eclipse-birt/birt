/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.ir;

import java.util.ArrayList;

/**
 * Visibility Design.
 * 
 * 
 * @version $Revision: 1.4 $ $Date: 2005/05/08 06:08:26 $
 */
public class VisibilityDesign
{

	/**
	 * Stores the VisibilityRuleDesign items.
	 */
	protected ArrayList rules = new ArrayList( );
	/** All the renders */
	public static int FORMAT_TYPE_ALL = 1;
	/** HTML render */
	public static int FORMAT_TYPE_VIEWER = 2;
	/** FO and PDF render */
	public static int FORMAT_TYPE_PDF = 4;
	/** Email render */
	public static int FORMAT_TYPE_EMAIL = 8;
	/** Print render */
	public static int FORMAT_TYPE_PRINT = 16;
	/** Excel render */
	public static int FORMAT_TYPE_EXCEL = 32;
	/** Word render */
	public static int FORMAT_TYPE_WORD = 64;
	/** Powerpoint Render */
	public static int FORMAT_TYPE_POWERPOINT = 128;
	/** Reportlet Render */
	public static int FORMAT_TYPE_REPORTLET = 256;
	/** RTF render */
	public static int FORMAT_TYPE_RTF = 512;

	/**
	 * Adds the VisibilityRuleDesign
	 * 
	 * @param rule
	 *            the VisibilityRuleDesign
	 */
	public void addRule( VisibilityRuleDesign rule )
	{
		assert rule != null;
		rules.add( rule );
	}

	/**
	 * Gets the count of items
	 * 
	 * @return the count of the VisibilityRuleDesign
	 */
	public int count( )
	{
		return rules.size( );
	}

	/**
	 * Gets the VisibilityRuleDesign according to the specified index.
	 * 
	 * @param index
	 *            the specified index
	 * @return the VisibilityRuleDesign at the specified index
	 */
	public VisibilityRuleDesign getRule( int index )
	{
		assert index >= 0 && index < rules.size( );
		return (VisibilityRuleDesign) rules.get( index );
	}
}