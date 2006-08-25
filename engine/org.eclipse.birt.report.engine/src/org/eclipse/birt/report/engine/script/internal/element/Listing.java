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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.engine.api.script.element.IListing;
import org.eclipse.birt.report.engine.api.script.element.ISortCondition;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;

/**
 * Implements of Listing
 * 
 */
public class Listing extends ReportItem implements IListing
{

	public Listing( ListingHandle listing )
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
			FilterConditionImpl f = new FilterConditionImpl( conditionHandle,
					(ListingHandle) handle );
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
			SortConditionImpl s = new SortConditionImpl( sortHandle, handle );
			rList.add( s );
			++count;
		}

		return (ISortCondition[]) rList.toArray( new ISortCondition[count] );
	}

}
