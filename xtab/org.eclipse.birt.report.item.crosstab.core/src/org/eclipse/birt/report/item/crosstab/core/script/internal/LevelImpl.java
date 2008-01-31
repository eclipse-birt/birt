/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.script.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.script.ILevel;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.simpleapi.IFilterConditionElement;
import org.eclipse.birt.report.model.api.simpleapi.ISimpleElementFactory;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * LevelImpl
 */
public class LevelImpl implements ILevel
{

	private LevelViewHandle lv;
	private LevelHandle lh;

	public LevelImpl( LevelViewHandle lv )
	{
		this.lv = lv;
		if ( lv != null )
		{
			lh = lv.getCubeLevel( );
		}
	}

	public String getDimensionName( )
	{
		if ( lh != null && lh.getContainer( ) != null )
		{
			DimensionHandle dh = (DimensionHandle) lh.getContainer( )
					.getContainer( );
			if ( dh != null )
			{
				return dh.getName( );
			}
		}
		return null;
	}

	public String getName( )
	{
		if ( lh != null )
		{
			return lh.getName( );
		}
		return null;
	}

	public void addFilterCondition( IFilterConditionElement filter )
	{
		// TODO Auto-generated method stub

	}

	public List getFilterConditions( )
	{
		List filters = new ArrayList( );
		ISimpleElementFactory factory = SimpleElementFactory.getInstance( );

		for ( Iterator itr = lv.filtersIterator( ); itr.hasNext( ); )
		{
			FilterConditionElementHandle feh = (FilterConditionElementHandle) itr.next( );

			filters.add( factory.getElement( feh ) );
		}

		if ( filters.size( ) > 0 )
		{
			return filters;
		}

		return Collections.EMPTY_LIST;
	}

	public void removeAllFilterConditions( )
	{
		// TODO Auto-generated method stub

	}

	public void removeFilterCondition( IFilterConditionElement filter )
	{
		// TODO Auto-generated method stub

	}

}
