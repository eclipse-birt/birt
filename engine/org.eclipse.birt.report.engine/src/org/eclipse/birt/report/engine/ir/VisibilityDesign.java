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
 * @version $Revision: 1.5 $ $Date: 2005/05/08 06:59:45 $
 */
public class VisibilityDesign
{
	/**
	 * Stores the VisibilityRuleDesign items.
	 */
	protected ArrayList rules = new ArrayList( );


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