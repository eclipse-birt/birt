/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * 
 */

public class CrosstabBindingExpressionProvider extends CrosstabExpressionProvider {

	protected void addFilterToProvider() {
		this.addFilter(new ExpressionFilter() {

			public boolean select(Object parentElement, Object element) {
				// bug 220714
				// ychen 2008/03/13
				// CrosstabBindingExpressionProvider now use in common Binding dialog, not
				// aggregation binding dialog.
				// we can use available column binding, reference dimensions in the crosstab and
				// all measures
//				if(parentElement instanceof String )
//				{
//					String parent = (String)parentElement;
//					if(ExpressionFilter.CATEGORY.equals( parent ))
//					{
//						if(element instanceof String)
//						{
//							String elementString = (String)element;
//							if(COLUMN_BINDINGS.equals( elementString ))
//							{
//								return false;
//							}
//						}
//					}

//					if(CURRENT_CUBE.equals( parent ))
//					{
//						if(element instanceof PropertyHandle)
//						{
//							PropertyHandle handle = (PropertyHandle) element;
//							if ( handle.getPropertyDefn( )
//									.getName( )
//									.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
//							{
//								return true;
//							}
//							return false;
//						}
//					}
//				}
				if (parentElement instanceof PropertyHandle) {
					PropertyHandle handle = (PropertyHandle) parentElement;
					if (handle.getPropertyDefn().getName().equals(ICubeModel.DIMENSIONS_PROP)) {
						try {
							CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle();
							if (xtabHandle.getDimension(((DimensionHandle) element).getName()) == null)
								return false;
							return true;
						} catch (ExtendedElementException e) {
							return false;
						}
					}
					// Bug 211024
					// else if ( handle.getPropertyDefn( )
					// .getName( )
					// .equals( ICubeModel.MEASURE_GROUPS_PROP ) )
					// {
					//
					// try
					// {
					// CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
					// MeasureGroupHandle mgHandle = (MeasureGroupHandle) element;
					// for ( int i = 0; i < xtabHandle.getMeasureCount( ); i++ )
					// {
					// if ( xtabHandle.getMeasure( i )
					// .getCubeMeasure( )
					// .getContainer( )
					// .equals( mgHandle ) )
					// return true;
					// }
					// return false;
					// }
					// catch ( ExtendedElementException e )
					// {
					// return false;
					// }
					// }
				}
				// Bug 211024
				// if ( element instanceof MeasureHandle )
				// {
				// try
				// {
				// CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
				// for ( int i = 0; i < xtabHandle.getMeasureCount( ); i++ )
				// {
				// if ( xtabHandle.getMeasure( i )
				// .getCubeMeasure( )
				// .equals( element ) )
				// return true;
				// }
				// return false;
				// }
				// catch ( ExtendedElementException e )
				// {
				// return false;
				// }
				// }
				return true;
			}
		});
	}

	public CrosstabBindingExpressionProvider(DesignElementHandle handle, ComputedColumnHandle computedColumnHandle) {
		super(handle, computedColumnHandle);
	}

}
