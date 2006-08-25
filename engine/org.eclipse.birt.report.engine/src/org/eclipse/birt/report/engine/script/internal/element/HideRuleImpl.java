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
import org.eclipse.birt.report.engine.api.script.element.IHideRule;
import org.eclipse.birt.report.model.api.HideRuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;

/**
 * Implements of Hide Rule.
 */

public class HideRuleImpl implements IHideRule
{

	private HideRuleHandle rule;

	private ReportItemHandle handle;

	public HideRuleImpl( HideRuleHandle rule, ReportItemHandle handle )
	{
		this.handle = handle;
		this.rule = rule;
	}

	public String getFormat( )
	{
		return rule.getFormat( );
	}

	public String getValueExpr( )
	{
		return rule.getExpression( );
	}

	public void setFormat( String format ) throws ScriptException
	{
		checkHandle( );
		try
		{
			rule.setFormat( format );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	public void setValueExpr( String valueExpr ) throws ScriptException
	{
		checkHandle( );
		rule.setExpression( valueExpr );
	}

	private void checkHandle( ) throws ScriptException
	{
		if ( rule != null )
			return;

		HideRule r = new HideRule( );

		PropertyHandle propHandle = handle
				.getPropertyHandle( IReportItemModel.VISIBILITY_PROP );
		try
		{
			rule = (HideRuleHandle) propHandle.addItem( r );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}

	}

}
