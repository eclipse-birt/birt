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

import java.util.List;

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;

/**
 * 
 */

public class CrosstabComputedMeasureExpressionProvider extends CrosstabExpressionProvider {

	public CrosstabComputedMeasureExpressionProvider(DesignElementHandle handle) {
		super(handle, null);
	}

	protected List getCategoryList() {
		List categoryList = super.getCategoryList();

		try {
			if (CrosstabUtil.isBoundToLinkedDataSet(getCrosstabReportItemHandle())) {
				categoryList.remove(ExpressionProvider.DATASETS);
			}
		} catch (ExtendedElementException e) {
		}

		return categoryList;
	}

//	protected void addFilterToProvider( )
//	{
//		this.addFilter( new ExpressionFilter( ) {
//
//			public boolean select( Object parentElement, Object element )
//			{
//				if ( ExpressionFilter.CATEGORY.equals( parentElement )
//						&& ExpressionProvider.CURRENT_CUBE.equals( element ) )
//				{
//					// return false;
//				}
//				if ( CURRENT_CUBE.equals( parentElement ) )
//				{
//					PropertyHandle handle = null;
//					if ( element instanceof PropertyHandle )
//						handle = (PropertyHandle) element;
//					else if ( element instanceof IAdaptable
//							&& ( (IAdaptable) element ).getAdapter( PropertyHandle.class ) instanceof PropertyHandle )
//						handle = (PropertyHandle) ( (IAdaptable) element ).getAdapter( PropertyHandle.class );
//
//					if ( handle != null
//							&& handle.getPropertyDefn( )
//									.getName( )
//									.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
//					{
//						return true;
//					}
//					return false;
//				}
//				return true;
//			}
//		} );
//	}

}
