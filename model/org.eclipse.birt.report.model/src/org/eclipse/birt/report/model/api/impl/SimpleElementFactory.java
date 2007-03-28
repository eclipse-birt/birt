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

import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.TextDataHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.HideRule;
import org.eclipse.birt.report.model.api.elements.structures.HighlightRule;
import org.eclipse.birt.report.model.api.elements.structures.SortKey;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;
import org.eclipse.birt.report.model.api.simpleapi.IDesignElement;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IHideRule;
import org.eclipse.birt.report.model.api.simpleapi.IHighlightRule;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.simpleapi.DataBindingImpl;
import org.eclipse.birt.report.model.simpleapi.DataItem;
import org.eclipse.birt.report.model.simpleapi.DynamicText;
import org.eclipse.birt.report.model.simpleapi.ExtendedItem;
import org.eclipse.birt.report.model.simpleapi.FilterConditionImpl;
import org.eclipse.birt.report.model.simpleapi.Grid;
import org.eclipse.birt.report.model.simpleapi.HideRuleImpl;
import org.eclipse.birt.report.model.simpleapi.HighlightRuleImpl;
import org.eclipse.birt.report.model.simpleapi.Image;
import org.eclipse.birt.report.model.simpleapi.Label;
import org.eclipse.birt.report.model.simpleapi.List;
import org.eclipse.birt.report.model.simpleapi.ReportDesign;
import org.eclipse.birt.report.model.simpleapi.ReportElement;
import org.eclipse.birt.report.model.simpleapi.SortConditionImpl;
import org.eclipse.birt.report.model.simpleapi.Table;
import org.eclipse.birt.report.model.simpleapi.TextItem;

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

	public IDesignElement getElement( DesignElementHandle element )
	{
		if ( element == null )
			return null;

		if ( element instanceof ReportDesignHandle )
			return new ReportDesign( (ReportDesignHandle) element );

		if ( !( element instanceof ReportElementHandle ) )
			return null;

		if ( element instanceof DataItemHandle )
			return new DataItem( (DataItemHandle) element );

		if ( element instanceof GridHandle )
			return new Grid( (GridHandle) element );

		if ( element instanceof ImageHandle )
			return new Image( (ImageHandle) element );

		if ( element instanceof LabelHandle )
			return new Label( (LabelHandle) element );

		if ( element instanceof ListHandle )
			return new List( (ListHandle) element );

		if ( element instanceof TableHandle )
			return new Table( (TableHandle) element );

		if ( element instanceof TextDataHandle )
			return new DynamicText( (TextDataHandle) element );

		if ( element instanceof TextItemHandle )
			return new TextItem( (TextItemHandle) element );

		if ( element instanceof ExtendedItemHandle )
		{
			org.eclipse.birt.report.model.api.simpleapi.IReportItem item = null;
			try
			{
				IReportItem extensionItem = ( (ExtendedItemHandle) element )
						.getReportItem( );

				if ( extensionItem != null )
					item = extensionItem.getSimpleElement( );
			}
			catch ( ExtendedElementException e )
			{
				// do thing.
			}

			if ( item == null )
				item = new ExtendedItem( (ExtendedItemHandle) element );

			return item;
		}

		return new ReportElement( (ReportElementHandle) element );
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
