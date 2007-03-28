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

package org.eclipse.birt.report.model.simpleapi;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IMultiRowItem;
import org.eclipse.birt.report.model.api.simpleapi.ISortCondition;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Implements of Listing
 * 
 */
public class MultiRowItem extends ReportItem implements IMultiRowItem
{

	/**
	 * Constructor
	 * 
	 * @param listing
	 */
	public MultiRowItem( ListingHandle listing )
	{
		super( listing );
	}

	public IFilterCondition[] getFilterConditions( )
	{
		PropertyHandle propHandle = ( (ListingHandle) handle )
				.getPropertyHandle( IListingElementModel.FILTER_PROP );
		Iterator iterator = propHandle.iterator( );

		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			FilterConditionHandle conditionHandle = (FilterConditionHandle) iterator
					.next( );
			FilterConditionImpl f = new FilterConditionImpl( conditionHandle );
			rList.add( f );
			++count;
		}

		return (IFilterCondition[]) rList.toArray( new IFilterCondition[count] );
	}

	public ISortCondition[] getSortConditions( )
	{
		PropertyHandle propHandle = ( (ListingHandle) handle )
				.getPropertyHandle( IListingElementModel.SORT_PROP );
		Iterator iterator = propHandle.iterator( );

		List rList = new ArrayList( );
		int count = 0;

		while ( iterator.hasNext( ) )
		{
			SortKeyHandle sortHandle = (SortKeyHandle) iterator.next( );
			SortConditionImpl s = new SortConditionImpl( sortHandle );
			rList.add( s );
			++count;
		}

		return (ISortCondition[]) rList.toArray( new ISortCondition[count] );
	}

	/**
	 * Add FilterCondition
	 * 
	 * @param condition
	 * @throws SemanticException
	 */

	public void addFilterCondition( IFilterCondition condition )
			throws SemanticException
	{
		if ( condition == null )
			return;
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.FILTER_PROP );
		propHandle.addItem( condition.getStructure( ) );
	}

	/**
	 * Add SortCondition
	 * 
	 * @param condition
	 * @throws SemanticException
	 */

	public void addSortCondition( ISortCondition condition )
			throws SemanticException
	{
		if ( condition == null )
			return;
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.SORT_PROP );

		propHandle.addItem( condition.getStructure( ) );
	}

	public void removeFilterCondition( IFilterCondition condition )
			throws SemanticException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.FILTER_PROP );

		propHandle.removeItem( condition.getStructure( ) );
	}

	public void removeFilterConditions( ) throws SemanticException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.FILTER_PROP );

		propHandle.clearValue( );
	}

	public void removeSortCondition( ISortCondition condition )
			throws SemanticException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.SORT_PROP );
		propHandle.removeItem( condition.getStructure( ) );

	}

	public void removeSortConditions( ) throws SemanticException
	{
		PropertyHandle propHandle = handle
				.getPropertyHandle( IListingElementModel.SORT_PROP );
		propHandle.clearValue( );

	}

}
