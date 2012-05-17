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
import org.eclipse.birt.report.model.api.DesignElementHandle;

/**
 * 
 */

public class CrosstabComputedMeasureExpressionProvider extends
		CrosstabExpressionProvider
{

	public CrosstabComputedMeasureExpressionProvider( DesignElementHandle handle )
	{
		super( handle, null );
	}

	protected void addFilterToProvider( )
	{
		this.addFilter( new ExpressionFilter( ) {

			public boolean select( Object parentElement, Object element )
			{
				if ( ExpressionFilter.CATEGORY.equals( parentElement )
						&& ExpressionProvider.CURRENT_CUBE.equals( element ) )
				{
					return false;
				}
				return true;
			}
		} );
	}

}
