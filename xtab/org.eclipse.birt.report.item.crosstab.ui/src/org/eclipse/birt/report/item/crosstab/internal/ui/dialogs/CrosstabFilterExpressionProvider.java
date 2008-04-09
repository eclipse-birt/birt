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

	/**
	 * @param handle
	 */
	public CrosstabFilterExpressionProvider( DesignElementHandle handle )
	{
		super( handle, null );
		// TODO Auto-generated constructor stub
	}

	protected void addFilterToProvider( )
	{
		addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{
				// TODO Auto-generated method stub
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
							if ( xtabHandle.getDimension( ( (TabularDimensionHandle) element ).getName( ) ) == null )
								return false;
							return true;
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
