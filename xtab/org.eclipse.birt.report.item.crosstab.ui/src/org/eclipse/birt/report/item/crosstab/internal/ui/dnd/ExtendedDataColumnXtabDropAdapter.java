/*******************************************************************************
 * Copyright (c) 2004, 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataXtabAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataXtabAdapter;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;

public class ExtendedDataColumnXtabDropAdapter implements IDropAdapter
{
	private IExtendedDataXtabAdapter adapter = ExtendedDataXtabAdapterHelper.getInstance( ).getAdapter( );

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if (adapter == null || !adapter.isExtendedDataColumn( transfer ))
		{
			return DNDService.LOGIC_UNKNOW;
		}
		if ( target instanceof EditPart )
		{
			EditPart editPart = (EditPart) target;
			if ( editPart.getModel( ) instanceof IVirtualValidator )
			{
				if (handleValidate(editPart, transfer) != null)
					return DNDService.LOGIC_TRUE;
				else
					return DNDService.LOGIC_FALSE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( target instanceof EditPart)
		{
			EditPart targetPart = (EditPart) target;
			
			CrosstabReportItemHandle crosstab = getCrosstab( targetPart );
			if ( crosstab != null )
			{
				if(!adapter.getExtendedDataName( (ReportItemHandle) crosstab.getModelHandle( ) )
						.equals( ((ReportElementHandle) transfer).getContainer( ).getContainer( ).getName( ) ))
				{
					adapter.setExtendedData( (ReportItemHandle)crosstab.getModelHandle( ), 
							((ReportElementHandle) transfer).getContainer( ).getContainer( ));
				}
			}
			
			Object element = handleValidate(targetPart, transfer);
			
			if (element instanceof MeasureHandle)
			{
				return new MeasureHandleDropAdapter().performDrop( element, targetPart, operation, location );
			}
			else if (element instanceof DimensionHandle)
			{
				return new DimensionHandleDropAdapter().performDrop( element, targetPart, operation, location );
			}
		}

		return false;
	}

	private CrosstabReportItemHandle getCrosstab( EditPart editPart )
	{
		CrosstabReportItemHandle crosstab = null;
		Object tmp = editPart.getModel( );
		if ( !( tmp instanceof CrosstabCellAdapter ) )
		{
			return null;
		}
		if ( tmp instanceof VirtualCrosstabCellAdapter )
		{
			return ( (VirtualCrosstabCellAdapter) tmp ).getCrosstabReportItemHandle( );
		}

		CrosstabCellHandle handle = ( (CrosstabCellAdapter) tmp ).getCrosstabCellHandle( );
		if ( handle != null )
		{
			crosstab = handle.getCrosstab( );
		}

		return crosstab;

	}
	
	private Object handleValidate(EditPart editPart, Object transfer)
	{
		Object[] supportedTypes = adapter.getSupportedTypes( transfer, getCrosstab( editPart ).getCube( ) );
		
		for (Object type : supportedTypes)
		{
			if (( (IVirtualValidator) editPart.getModel( ) ).handleValidate( type ))
			{
				return type;
			}
		}
		
		return null;
	}
}