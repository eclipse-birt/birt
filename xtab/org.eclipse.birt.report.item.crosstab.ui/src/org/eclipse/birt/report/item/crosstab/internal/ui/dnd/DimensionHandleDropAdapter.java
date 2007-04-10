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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.gef.EditPart;

/**
 * 
 */

public class DimensionHandleDropAdapter implements IDropAdapter
{

	public int canDrop( Object transfer, Object target, int operation,
			int location )
	{
		if ( target instanceof EditPart )
		{
			EditPart editPart = (EditPart) target;
			if ( editPart.getModel( ) instanceof IVirtualValidator )
			{
				if ( ( (IVirtualValidator) editPart.getModel( ) ).handleValidate( transfer ) )
					return DNDService.LOGIC_TRUE;
				else
					return DNDService.LOGIC_FALSE;
			}
		}
		else if ( target instanceof PropertyHandle )
		{
			if ( ( (PropertyHandle) target ).getPropertyDefn( )
					.getName( )
					.equals( ICrosstabReportItemConstants.COLUMNS_PROP )
					|| ( (PropertyHandle) target ).getPropertyDefn( )
							.getName( )
							.equals( ICrosstabReportItemConstants.ROWS_PROP ) )
				return DNDService.LOGIC_TRUE;
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			int location )
	{
		if ( transfer instanceof Object[] )
		{
			Object[] objects = (Object[]) transfer;
			for ( int i = 0; i < objects.length; i++ )
			{
				if ( !performDrop( objects[i], target, operation, location ) )
					return false;
			}
			return true;
		}

		DimensionHandle dimensionHandle = (DimensionHandle) transfer;
		CrosstabReportItemHandle xtabHandle = null;
		int axisType = 0;

		if ( target instanceof EditPart )//drop on layout
		{
			EditPart editPart = (EditPart) target;
			CrosstabTableEditPart parent = (CrosstabTableEditPart) editPart.getParent( );
			CrosstabHandleAdapter handleAdpter = parent.getCrosstabHandleAdapter( );
			axisType = ( (VirtualCrosstabCellAdapter) editPart.getModel( ) ).getType( );
			xtabHandle = (CrosstabReportItemHandle) handleAdpter.getCrosstabItemHandle( );
		}
		else if ( target instanceof PropertyHandle )//drop on outline
		{
			PropertyHandle property = (PropertyHandle) target;
			Object handle = property.getElementHandle( );
			if ( handle instanceof CrosstabReportItemHandle )
			{
				xtabHandle = (CrosstabReportItemHandle) handle;
			}
			else if ( handle instanceof DesignElementHandle )
			{
				xtabHandle = (CrosstabReportItemHandle) CrosstabUtil.getReportItem( (DesignElementHandle) handle );
			}
			if ( property.getPropertyDefn( )
					.getName( )
					.equals( ICrosstabReportItemConstants.COLUMNS_PROP ) )
			{
				axisType = ICrosstabConstants.COLUMN_AXIS_TYPE;
			}
			else
			{
				axisType = ICrosstabConstants.ROW_AXIS_TYPE;
			}
		}
		return createDimensionViewHandle( xtabHandle, dimensionHandle, axisType );
	}

	private boolean createDimensionViewHandle(
			CrosstabReportItemHandle xtabHandle,
			DimensionHandle dimensionHandle, int type )
	{

		SessionHandleAdapter.getInstance( )
				.getCommandStack( )
				.startTrans( "DimensionHandleDropAdapter" );
		try
		{
			DimensionViewHandle viewHandle = xtabHandle.insertDimension( dimensionHandle,
					type,
					0 );
			HierarchyHandle hierarchyHandle = dimensionHandle.getDefaultHierarchy( );
			int count = hierarchyHandle.getLevelCount( );
			if ( count == 0 )
			{
				SessionHandleAdapter.getInstance( )
						.getCommandStack( )
						.rollback( );
				return false;
			}
			LevelHandle levelHandle = hierarchyHandle.getLevel( 0 );
			//new a bing
			ComputedColumn bindingColumn = CrosstabAdaptUtil.createComputedColumn( (ExtendedItemHandle) xtabHandle.getModelHandle( ),
					levelHandle );

			ComputedColumnHandle bindingHandle = ( (ExtendedItemHandle) xtabHandle.getModelHandle( ) ).addColumnBinding( bindingColumn,
					false );

			LevelViewHandle levelViewHandle = CrosstabUtil.insertLevel( viewHandle,
					levelHandle,
					0 );

			CrosstabCellHandle cellHandle = levelViewHandle.getCell( );

			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( levelHandle.getName( ) );
			dataHandle.setResultSetColumn( bindingHandle.getName( ) );

			cellHandle.addContent( dataHandle );
			SessionHandleAdapter.getInstance( ).getCommandStack( ).commit( );
		}
		catch ( SemanticException e )
		{
			SessionHandleAdapter.getInstance( ).getCommandStack( ).rollback( );
			ExceptionHandler.handle( e );
			return false;
		}
		return true;

	}

}
