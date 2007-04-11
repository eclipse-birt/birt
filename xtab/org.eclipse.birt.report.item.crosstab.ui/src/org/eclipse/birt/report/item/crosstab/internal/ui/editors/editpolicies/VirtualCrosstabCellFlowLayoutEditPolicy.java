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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.ChangeAreaCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateDimensionViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateMeasureViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.FirstLevelHandleDataItemEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.UnexecutableCommand;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 */

public class VirtualCrosstabCellFlowLayoutEditPolicy extends ReportFlowLayoutEditPolicy
{
	protected Command getCreateCommand( CreateRequest request )
	{
		//EditPart after = getInsertionReference( request );

		//CreateCommand command = new CreateCommand( request.getExtendedData( ) );

		Object model = this.getHost( ).getModel( );
		Object newObject = request.getExtendedData( ).get( DesignerConstants.KEY_NEWOBJECT );

		if (model instanceof VirtualCrosstabCellAdapter)
		{
			EditPart parent = getHost( ).getParent( );
			CrosstabHandleAdapter adapter  = ((CrosstabTableEditPart)parent).getCrosstabHandleAdapter( );
			int type = ((VirtualCrosstabCellAdapter)model).getType( );
			if (newObject instanceof DimensionHandle)
			{
				return new CreateDimensionViewCommand(adapter, type, (DimensionHandle)newObject);
			}
			if (newObject instanceof LevelHandle)
			{
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDeDimensionHandle( (LevelHandle)newObject );
				CreateDimensionViewCommand command = new CreateDimensionViewCommand(adapter, type, dimensionHandle);
				command.setLevelHandle( (LevelHandle)newObject );
				return command;
			}
			else if (newObject instanceof MeasureHandle && type == VirtualCrosstabCellAdapter.MEASURE_TYPE)
			{
				return new CreateMeasureViewCommand(adapter, (MeasureHandle)newObject);
			}
		}
		// No previous edit part
//		if ( after != null )
//		{
//			command.setAfter( after.getModel( ) );
//		}		
		return super.getCreateCommand( request );
		//return null;
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
		//Object source = child.getModel( );
		Object afterObj = after == null ? null : after.getModel( );
		Object childParent = child.getParent( ).getModel( );
		if (parentObj instanceof VirtualCrosstabCellAdapter && childParent instanceof CrosstabCellAdapter)
		{
			CrosstabCellAdapter childAdapter = (CrosstabCellAdapter)childParent;
			VirtualCrosstabCellAdapter parentAdapter = (VirtualCrosstabCellAdapter)parentObj;
			if (parentAdapter.getType( ) == VirtualCrosstabCellAdapter.IMMACULATE_TYPE
					|| parentAdapter.getType( ) == VirtualCrosstabCellAdapter.MEASURE_TYPE)
			{
				return UnexecutableCommand.INSTANCE;
			}
			if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals( childAdapter.getPositionType( )))
			{
				if (!(after instanceof FirstLevelHandleDataItemEditPart) )
				{
					afterObj = null;
				}
				ChangeAreaCommand command = new ChangeAreaCommand(parentAdapter.getDesignElementHandle( ), 
						childAdapter.getDesignElementHandle( ),(DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );
				
				command.setType( parentAdapter.getType( ) );
				return command;
			}
		}
		return UnexecutableCommand.INSTANCE;
	}
}
