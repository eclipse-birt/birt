/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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
 * 
 * @version $Revision: 1.3 $ $Date: 2005/02/07 02:00:39 $
 */
public class MapDesign
{

	/**
	 * test expression
	 */
	protected String testExpression;

	/**
	 * rules defined in this map
	 */
	protected ArrayList rules = new ArrayList( );

	/**
	 * @return Returns the textExpression.
	 */
	public String getTestExpression( )
	{
		return testExpression;
	}

	/**
	 * @param textExpression
	 *            The textExpression to set.
	 */
	public void setTestExpression( String textExpression )
	{
		this.testExpression = textExpression;
	}

	/**
	 * get the rule count.
	 * 
	 * @return total rule count defined in this map.
	 */
	public int getRuleCount( )
	{
		return this.rules.size( );
	}

	/**
	 * add map rule.
	 * 
	 * @param rule
	 *            rule to be added
	 */
	public void addRule( MapRuleDesign rule )
	{
		this.rules.add( rule );
	}

	/**
	 * get rule at index.
	 * 
	 * @param index
	 *            rule index
	 * @return rule
	 */
	public MapRuleDesign getRule( int index )
	{
		assert ( index >= 0 && index < this.rules.size( ) );
		return (MapRuleDesign) this.rules.get( index );
	}

}
