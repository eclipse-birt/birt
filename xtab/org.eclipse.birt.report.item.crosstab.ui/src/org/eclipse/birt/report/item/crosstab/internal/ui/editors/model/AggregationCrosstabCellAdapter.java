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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.model;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;

/**
 * Adapter for the AggregationCrosstabCell
 */

public class AggregationCrosstabCellAdapter extends CrosstabCellAdapter
{

	/**Constructor
	 * @param handle
	 */
	public AggregationCrosstabCellAdapter( AggregationCellHandle handle )
	{
		super( handle );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.BaseCrosstabAdapter#hashCode()
	 */
	public int hashCode( )
	{
		return getCrosstabItemHandle( ).hashCode( );
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.BaseCrosstabAdapter#equals(java.lang.Object)
	 */
	public boolean equals( Object obj )
	{
//		if (obj == getCrosstabItemHandle( ))
//		{
//			return true;
//		}	
//		if (obj instanceof CrosstabCellAdapter)
//		{
//			return getCrosstabItemHandle( ) == ((CrosstabCellAdapter)obj).getCrosstabItemHandle();
//		}
		return super.equals( obj );
	}
}
