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

import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Util class 
 */
//TODO form DimensionViewHandle you can get index
public class CrosstabAdaptUtil
{
	/**Gets the row or column count  after the handel, now include the current handle 
	 * @param handle
	 * @return
	 */
	public static int getNumberAfterDimensionViewHandle(DimensionViewHandle handle)
	{
		CrosstabReportItemHandle reportItem = (CrosstabReportItemHandle)CrosstabUtil.getReportItem( handle.getCrosstabHandle( ) );
		int index  = handle.getIndex( );
		int type = handle.getAxisType( );
		int count = reportItem.getDimensionCount( type);
		int retValue = 0;
		for (int i=index; i<count; i++)
		{
			retValue = retValue + reportItem.getDimension( type, index ).getLevelCount( );
		}
		return retValue;
	}
	
	/**
	 * @param owner
	 * @param levelHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn(ReportItemHandle owner, LevelHandle levelHandle)
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				levelHandle.getName( ));
		
		bindingColumn.setDataType( DesignChoiceConstants. COLUMN_DATA_TYPE_ANY);
		bindingColumn.setExpression( DEUtil.getExpression( levelHandle ) );
		
		return bindingColumn;
	}
	
	/**
	 * @param owner
	 * @param measureHandle
	 * @return
	 */
	public static ComputedColumn createComputedColumn(ReportItemHandle owner, MeasureHandle measureHandle)
	{
		ComputedColumn bindingColumn = StructureFactory.newComputedColumn( owner,
				measureHandle.getName( ));
		
		bindingColumn.setDataType( DesignChoiceConstants. COLUMN_DATA_TYPE_ANY);
		bindingColumn.setExpression( DEUtil.getExpression( measureHandle ) );
		
		return bindingColumn;
	}
	
}
