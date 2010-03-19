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

import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.TabularDimensionHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * @author Administrator
 * 
 */
public class CrosstabFilterExpressionProvider extends
		CrosstabExpressionProvider
{

	private boolean isDetail = false;

	public void setDetail( boolean isDetail )
	{
		this.isDetail = isDetail;
	}

	/**
	 * @param handle
	 */
	public CrosstabFilterExpressionProvider( DesignElementHandle handle )
	{
		super( handle, null );
	}

	protected void addFilterToProvider( )
	{
		addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{

				if ( ExpressionFilter.CATEGORY.equals( parentElement )
						&& ExpressionProvider.MEASURE.equals( element ) )
				{
					return false;
				}

				if ( ( parentElement instanceof String && ( (String) parentElement ).equals( CURRENT_CUBE ) )
						&& ( element instanceof PropertyHandle ) )
				{
					PropertyHandle handle = (PropertyHandle) element;
					if ( handle.getPropertyDefn( )
							.getName( )
							.equals( ICubeModel.MEASURE_GROUPS_PROP ) )
					{
						return false;
					}
				}

				if ( parentElement instanceof PropertyHandle )
				{
					PropertyHandle handle = (PropertyHandle) parentElement;
					if ( handle.getPropertyDefn( )
							.getName( )
							.equals( ICubeModel.DIMENSIONS_PROP ) )
					{
						try
						{
							CrosstabReportItemHandle xtabHandle = getCrosstabReportItemHandle( );
							boolean result;
							if ( xtabHandle.getDimension( ( (TabularDimensionHandle) element ).getName( ) ) != null )
								result = true;
							else
								result = false;
							if ( isDetail )
								result = !result;
							return result;
						}
						catch ( ExtendedElementException e )
						{
							return false;
						}
					}
				}
				return true;
			}
		} );
	}

}
