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

package org.eclipse.birt.report.model.api.impl;

import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.simpleapi.DataBindingImpl;
import org.eclipse.birt.report.model.simpleapi.FilterConditionImpl;
import org.eclipse.birt.report.model.simpleapi.HideRuleImpl;
import org.eclipse.birt.report.model.simpleapi.HighlightRuleImpl;
import org.eclipse.birt.report.model.simpleapi.MultiRowItem;
import org.eclipse.birt.report.model.simpleapi.SortConditionImpl;

/**
 * The factory class to create scriptable objects.
 * 
 */

public class SimpleElementFactory implements ISimpleElementFactory
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.script.IScriptElementFactory#getElement(org.eclipse.birt.report.model.api.DesignElementHandle)
	 */

	public IReportItem wrapExtensionElement( ExtendedItemHandle handle )
	{
		if (handle == null)
			return null;
		
		return new MultiRowItem( handle );
	}

	/**
	 * Create <code>IHideRule</code> instance
	 * 
	 * @return IHideRule
	 */

	public IHideRule createHideRule( )
	{
		HideRule r = new HideRule( );
		IHideRule rule = new HideRuleImpl( r );
		return rule;
	}

	/**
	 * Create <code>IFilterCondition</code>
	 * 
	 * @return instance
	 */

	public IFilterCondition createFilterCondition( )
	{
		FilterCondition c = new FilterCondition( );
		IFilterCondition condition = new FilterConditionImpl( c );
		return condition;
	}

	/**
	 * Create <code>IDataBinding</code>
	 * 
	 * @return instance
	 */

	public IDataBinding createDataBinding( )
	{
		ComputedColumn c = new ComputedColumn( );
		IDataBinding binding = new DataBindingImpl( c );
		return binding;
	}

	/**
	 * Create <code>IHighLightRule</code>
	 * 
	 * @return instance
	 */

	public IHighlightRule createHighLightRule( )
	{
		HighlightRule h = new HighlightRule( );
		IHighlightRule rule = new HighlightRuleImpl( h );
		return rule;
	}

	/**
	 * Create <code>ISortCondition</code>
	 * 
	 * @return instance
	 */

	public ISortCondition createSortCondition( )
	{
		SortKey s = new SortKey( );
		ISortCondition sort = new SortConditionImpl( s );
		return sort;
	}

}
