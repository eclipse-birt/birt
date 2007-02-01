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

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IColumn;
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.engine.script.internal.HideRuleMethodUtil;
import org.eclipse.birt.report.model.api.ColumnHandle;

/**
 * Column script. Implements of <code>IColumn</code>
 * 
 */

public class Column extends DesignElement implements IColumn
{

	/**
	 * Constructor.
	 * 
	 * @param columnHandle
	 */

	public Column( ColumnHandle columnHandle )
	{
		super( columnHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#addHideRule(org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */

	public void addHideRule( IHideRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;
		HideRuleMethodUtil.addHideRule( handle, rule );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#getHideRules()
	 */

	public IHideRule[] getHideRules( )
	{
		return HideRuleMethodUtil.getHideRules( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#removeHideRule(org.eclipse.birt.report.engine.api.script.element.IHideRule)
	 */

	public void removeHideRule( IHideRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;
		HideRuleMethodUtil.removeHideRule( handle, rule );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IHideRuleStructure#removeHideRules()
	 */

	public void removeHideRules( ) throws ScriptException
	{
		HideRuleMethodUtil.removeHideRules( handle );
	}

}
