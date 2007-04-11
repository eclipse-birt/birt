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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.editpolicies;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddDimensionViewHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.AddMeasureViewHandleCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeAreaCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeMeasureOrderCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabCellCreateCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabFlowMoveChildCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabPasterCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.FirstLevelHandleDataItemEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Crosstab cell police
 */

public class CrosstabCellFlowLayoutEditPolicy extends
		ReportFlowLayoutEditPolicy
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy#getCreateCommand(org.eclipse.gef.requests.CreateRequest)
	 */
	protected Command getCreateCommand( CreateRequest request )
	{
		EditPart after = getInsertionReference( request );

		// CreateCommand command ;

		Object model = this.getHost( ).getModel( );
		Object newObject = request.getExtendedData( )
				.get( DesignerConstants.KEY_NEWOBJECT );

		if ( model instanceof CrosstabCellAdapter )
		{
			//EditPart parent = getHost( ).getParent( );
			//CrosstabHandleAdapter adapter = ( (CrosstabTableEditPart) parent ).getCrosstabHandleAdapter( );
			int type = getAreaType( (CrosstabCellAdapter) model );
			String position = ( (CrosstabCellAdapter) model ).getPositionType( );
			if ( (newObject instanceof DimensionHandle || newObject instanceof LevelHandle) 
					&& ( type == ICrosstabConstants.COLUMN_AXIS_TYPE || type == ICrosstabConstants.ROW_AXIS_TYPE ) )
			{
				Object afterObj = null;
				if (after instanceof FirstLevelHandleDataItemEditPart )
				{
					afterObj = after.getModel( );
				}
				
				if (newObject instanceof LevelHandle)
				{
					DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDeDimensionHandle( (LevelHandle)newObject );
					AddDimensionViewHandleCommand command = new AddDimensionViewHandleCommand( (CrosstabCellAdapter) model,
							type,dimensionHandle , afterObj );
					command.setLevelHandle( (LevelHandle)newObject );
					return command;
				}
				
				return new AddDimensionViewHandleCommand( (CrosstabCellAdapter) model,
						type,
						(DimensionHandle) newObject, afterObj );
			}
			else if ( newObject instanceof MeasureHandle
					&& position.equals( ICrosstabCellAdapterFactory.CELL_MEASURE ) )
			{
				Object afterObj = null;
				if (after != null )
				{
					afterObj = after.getModel( );
				}
				return new AddMeasureViewHandleCommand( (CrosstabCellAdapter) model,
						(MeasureHandle) newObject, afterObj );
			}
			else
			{
				CrosstabCellCreateCommand command = new CrosstabCellCreateCommand( request.getExtendedData( ) );
				command.setParent( getHost( ).getModel( ) );
				command.setAfter( after == null ? null : after.getModel( ) );
				return command;
			}
		}
		// TODO there is a bug, include design ui
		// ReportFlowLayoutEditPolicy.code can't return null,
		// must call the super method.
		return super.getCreateCommand( request );
	}

	private int getAreaType( CrosstabCellAdapter cellAdapter )
	{
		AbstractCrosstabItemHandle handle = cellAdapter.getCrosstabItemHandle( )
				.getContainer( );
		while ( handle != null )
		{
			if ( handle instanceof DimensionViewHandle )
			{
				return ( (DimensionViewHandle) handle ).getAxisType( );

			}
			handle = handle.getContainer( );
		}
		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy#createAddCommand(org.eclipse.gef.EditPart,
	 *      org.eclipse.gef.EditPart, org.eclipse.gef.EditPart)
	 */
	protected Command createAddCommand( EditPart parent, EditPart child,
			EditPart after )
	{
		Object parentObj = parent.getModel( );
		Object source = child.getModel( );
		Object afterObj = after == null ? null : after.getModel( );
		Object childParent = child.getParent( ).getModel( );
		if (parentObj instanceof CrosstabCellAdapter && childParent instanceof CrosstabCellAdapter)
		{
			CrosstabCellAdapter childAdapter = (CrosstabCellAdapter)childParent;
			CrosstabCellAdapter parentAdapter = (CrosstabCellAdapter)parentObj;
			if (isFirstDataItem( childAdapter, child.getModel( ), ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE )
				&& (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals( parentAdapter.getPositionType( ) )
				|| ICrosstabCellAdapterFactory.CELL_LEVEL_HANDLE.equals( parentAdapter.getPositionType( )))
				)
			{
				if (afterObj != parentAdapter.getFirstDataItem( ))
				{
					afterObj = null;
				}
				return new ChangeAreaCommand(parentAdapter.getDesignElementHandle( ), 
						childAdapter.getDesignElementHandle( ),(DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );
			}
			else if (isFirstDataItem( childAdapter, child.getModel( ), ICrosstabCellAdapterFactory.CELL_MEASURE )
					&& ICrosstabCellAdapterFactory.CELL_MEASURE.equals( parentAdapter.getPositionType( ) ))
				{
					if (afterObj != parentAdapter.getFirstDataItem( ))
					{
						afterObj = null;
					}
					return new ChangeMeasureOrderCommand(parentAdapter.getDesignElementHandle( ), 
							childAdapter.getDesignElementHandle( ),(DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );
				}
		}
		if (childParent instanceof CrosstabCellAdapter)
		{	
			CrosstabCellAdapter childAdapter = (CrosstabCellAdapter)childParent;
			if (isFirstDataItem( childAdapter, child.getModel( ), ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE )
					|| isFirstDataItem( childAdapter, child.getModel( ), ICrosstabCellAdapterFactory.CELL_MEASURE ))
			{
				return UnexecutableCommand.INSTANCE;
			}
		}
		return new CrosstabPasterCommand( (DesignElementHandle) DNDUtil.unwrapToModel( source ),
				(DesignElementHandle) DNDUtil.unwrapToModel( parentObj ),
				(DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );
	}
	
	private boolean isFirstDataItem(CrosstabCellAdapter adapter, Object model, String type)
	{
		if (adapter.getPositionType( ).equals( type ))
		{
			return adapter.getFirstDataItem( ) == model;
		}
		return false;
	}
	
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editpolicies.OrderedLayoutEditPolicy#createMoveChildCommand(org.eclipse.gef.EditPart,
	 *      org.eclipse.gef.EditPart)
	 */
	protected Command createMoveChildCommand( EditPart child, EditPart after )
	{
		Object afterModel = null;
		if ( after != null )
		{
			afterModel = after.getModel( );
		}
		CrosstabFlowMoveChildCommand command = new CrosstabFlowMoveChildCommand( child.getModel( ),
				afterModel,
				child.getParent( ).getModel( ) );
		return command;
	}
}
