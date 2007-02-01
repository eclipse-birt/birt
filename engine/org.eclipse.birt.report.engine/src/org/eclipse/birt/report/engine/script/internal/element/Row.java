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
import org.eclipse.birt.report.engine.api.script.element.IHighlightRule;
import org.eclipse.birt.report.engine.api.script.element.IRow;
import org.eclipse.birt.report.engine.script.internal.HideRuleMethodUtil;
import org.eclipse.birt.report.engine.script.internal.HighlightRuleMethodUtil;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

public class Row extends DesignElement implements IRow
{

	public Row( RowHandle handle )
	{
		super( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getHeight()
	 */

	public String getHeight( )
	{
		DimensionHandle height = ( (RowHandle) handle ).getHeight( );
		return ( height == null ? null : height.getStringValue( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#getBookmark()
	 */

	public String getBookmark( )
	{
		return ( (RowHandle) handle ).getBookmark( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.element.IRow#setBookmark(java.lang.String)
	 */

	public void setBookmark( String value ) throws ScriptException
	{
		try
		{
			( (RowHandle) handle ).setBookmark( value );
		}
		catch ( SemanticException e )
		{
			throw new ScriptException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#addHighlightRule(org.eclipse.birt.report.engine.api.script.element.IHighlightRule)
	 */

	public void addHighlightRule( IHighlightRule rule ) throws ScriptException
	{
		if ( rule == null )
			return;
		HighlightRuleMethodUtil.addHighlightRule( handle, rule );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#getHighlightRules()
	 */

	public IHighlightRule[] getHighlightRules( )
	{
		return HighlightRuleMethodUtil.getHighlightRules( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#removeHighlightRules()
	 */

	public void removeHighlightRules( ) throws ScriptException
	{
		HighlightRuleMethodUtil.removeHighlightRules( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.engine.api.script.IHighlightRuleMethod#removeHighlightRule(org.eclipse.birt.report.engine.api.script.element.IHighlightRule)
	 */

	public void removeHighlightRule( IHighlightRule rule )
			throws ScriptException
	{
		if ( rule == null )
			return;
		HighlightRuleMethodUtil.removeHighlightRule( handle, rule );
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
