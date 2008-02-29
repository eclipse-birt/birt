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


package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;


/**
 * @author Administrator
 *
 */
public class CrosstabFilterExpressionProvider extends
		CrosstabBindingExpressionProvider
{

	public final static int LEVEL_FILTER = 0;
	public final static int MEASURE_FILTER = 1;	
	
	int filterId ;
	ExpressionFilter levelExpressionFilter;
	ExpressionFilter measureExpressionFilter;
	
	private void initializeFilters()
	{
		if(levelExpressionFilter == null)
		{
			levelExpressionFilter = new ExpressionFilter(){
				public boolean select( Object parentElement, Object element )
				{
					// TODO Auto-generated method stub
					if((parentElement instanceof String && ((String)parentElement).equals( CURRENT_CUBE ) )
					&& ( element instanceof PropertyHandle))
					{
						PropertyHandle handle = (PropertyHandle) element;
						if ( handle.getPropertyDefn( )
								.getName( )
								.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
						{
							return false;
						}
					}
					return true;
				}};
		}

		if(measureExpressionFilter == null)
		{
			measureExpressionFilter = new ExpressionFilter(){
				public boolean select( Object parentElement, Object element )
				{
					// TODO Auto-generated method stub
					if((parentElement instanceof String && ((String)parentElement).equals( CURRENT_CUBE ) )
					&& ( element instanceof PropertyHandle))
					{
						PropertyHandle handle = (PropertyHandle) element;
						if ( handle.getPropertyDefn( )
								.getName( )
								.equals( ICubeModel.DIMENSIONS_PROP ) )
						{
							return false;
						}
					}
					if(parentElement instanceof String)
					{
						if ( ExpressionFilter.CATEGORY.equals( parentElement )
								&& ExpressionProvider.COLUMN_BINDINGS.equals( element ) )
						{
							return false;
						}
					}
					return true;
				}};
		}

	}

	
	/**
	 * @param handle
	 */
	public CrosstabFilterExpressionProvider( DesignElementHandle handle,int filterId )
	{
		super( handle );
		this.filterId = filterId;
		initializeFilters();
		updateFilter(filterId);
		// TODO Auto-generated constructor stub
	}
	
	protected void addFilterToProvider()
	{
		super.addFilterToProvider( );
		updateFilter(filterId);		
	}
	
	public void updateFilter(int filterId)
	{
		this.filterId = filterId;
		if(filterId == LEVEL_FILTER)
		{
			removeFilter( measureExpressionFilter );
			addFilter(levelExpressionFilter);
		}else
		{
			removeFilter(levelExpressionFilter);
			addFilter(measureExpressionFilter);
		}
	}

}
