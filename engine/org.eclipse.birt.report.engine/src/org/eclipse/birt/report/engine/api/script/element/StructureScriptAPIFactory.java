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

package org.eclipse.birt.report.engine.api.script.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.script.internal.element.DataBindingImpl;
import org.eclipse.birt.report.engine.script.internal.element.FilterConditionImpl;
import org.eclipse.birt.report.engine.script.internal.element.HideRuleImpl;
import org.eclipse.birt.report.engine.script.internal.element.HighLightRuleImpl;
import org.eclipse.birt.report.engine.script.internal.element.SortConditionImpl;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;

/**
 * Structure Factory for script API
 * 
 */

public class StructureScriptAPIFactory
{

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public static IHideRule createHideRule( )
	{
		HideRule r = new HideRule( );
		IHideRule rule = new HideRuleImpl( r );
		return rule;
	}

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @param rule
	 * @return instance
	 * @throws ScriptException
	 */

	public static IHideRule createHideRule( HideRule rule )
	{
		IHideRule r = new HideRuleImpl( rule );
		return r;
	}

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	public static IFilterCondition createFilterCondition( )
	{
		FilterCondition c = new FilterCondition( );
		IFilterCondition condition = new FilterConditionImpl( c );
		return condition;
	}

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @param c
	 * @return instance
	 */

	public static IFilterCondition createFilterCondition( FilterCondition c )
	{
		IFilterCondition condition = new FilterConditionImpl( c );
		return condition;
	}

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	public static IDataBinding createComputedColumn( )
	{
		ComputedColumn c = new ComputedColumn( );
		IDataBinding binding = new DataBindingImpl( c );
		return binding;
	}

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @param c
	 * @return instance
	 */

	public static IDataBinding createComputedColumn( ComputedColumn c )
	{
		IDataBinding binding = new DataBindingImpl( c );
		return binding;
	}

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @param h
	 * @return instance
	 */

	public static IHighLightRule createHighLightRule( HighlightRule h )
	{
		IHighLightRule rule = new HighLightRuleImpl( h );
		return rule;
	}

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @return instance
	 */
	public static IHighLightRule createHighLightRule( )
	{
		HighlightRule h = new HighlightRule( );
		IHighLightRule rule = new HighLightRuleImpl( h );
		return rule;
	}

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @return instance
	 */
	public static ISortCondition createSortKey( )
	{
		SortKey s = new SortKey( );
		ISortCondition sort = new SortConditionImpl( s );
		return sort;
	}

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @param s
	 * @return instance
	 */
	public static ISortCondition createSortKey( SortKey s )
	{
		ISortCondition sort = new SortConditionImpl( s );
		return sort;
	}

}
