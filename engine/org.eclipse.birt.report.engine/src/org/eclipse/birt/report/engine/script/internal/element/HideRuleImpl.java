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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;

/**
 * Implements of Hide Rule.
 */

public class HideRuleImpl implements IHideRule
{

	private HideRule rule;

	/**
	 * Constructor
	 * 
	 * @param ruleHandle
	 */
	public HideRuleImpl( HideRuleHandle ruleHandle )
	{

		if ( ruleHandle == null )
		{
			rule = createHideRule( );
		}
		else
		{
			rule = (HideRule) ruleHandle.getStructure( );
		}
	}

	/**
	 * Constructor
	 * 
	 * @param rule
	 */
	public HideRuleImpl( HideRule rule )
	{
		if ( rule == null )
		{
			rule = createHideRule( );
		}
		else
		{
			this.rule = rule;
		}
	}

	/**
	 * Create instance of <code>HideRule</code>
	 * 
	 * @return instance
	 */
	private HideRule createHideRule( )
	{
		HideRule r = new HideRule( );
		return r;
	}

	public String getFormat( )
	{
		return rule.getFormat( );
	}

	public String getValueExpr( )
	{
		return rule.getExpression( );
	}

	public void setFormat( String format )
	{
		rule.setFormat( format );
	}

	public void setValueExpr( String valueExpr )
	{
		rule.setExpression( valueExpr );
	}

	public IStructure getStructure( )
	{
		return rule;
	}

}
