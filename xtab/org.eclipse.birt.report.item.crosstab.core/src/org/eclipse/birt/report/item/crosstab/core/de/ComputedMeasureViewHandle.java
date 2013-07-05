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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.IComputedMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * ComputedMeasureViewHandle
 */
public class ComputedMeasureViewHandle extends MeasureViewHandle implements
		IComputedMeasureViewConstants
{

	/**
	 * 
	 * @param handle
	 */
	ComputedMeasureViewHandle( DesignElementHandle handle )
	{
		super( handle );
	}

	public String getName( )
	{
		return handle.getName( );
	}

	@Override
	public MeasureHandle getCubeMeasure( )
	{
		return null;
	}

	@Override
	public String getCubeMeasureName( )
	{
		return handle.getName( );
	}
	
	@Override
	public String getDataType( )
	{
		String dataType = null;
		CrosstabReportItemHandle crosstabItem = getCrosstab();
		if( CrosstabUtil.isBoundToLinkedDataSet( crosstabItem ) )
		{
			CrosstabCellHandle cell = getCell( );
			if( cell != null )
			{
				List contents = cell.getContents( );
				for( Object obj : contents )
				{
					if( obj != null && obj instanceof DataItemHandle )
					{
						String bindingName = ((DataItemHandle)obj).getResultSetColumn( );
						ComputedColumnHandle column = CrosstabUtil.getColumnHandle( crosstabItem, bindingName );
						dataType = (column!= null) ? column.getDataType( ) : null;
						if( CrosstabUtil.validateBinding( column, null ) )
						{
							break;
						}
					}
				}
			}
		}
		
		return dataType;
	}
}
