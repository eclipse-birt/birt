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
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabCellCreateCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CrosstabPasterCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
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
			EditPart parent = getHost( ).getParent( );
			CrosstabHandleAdapter adapter = ( (CrosstabTableEditPart) parent ).getCrosstabHandleAdapter( );
			int type = getAreaType( (CrosstabCellAdapter) model );
			String position = ( (CrosstabCellAdapter) model ).getPositionType( );
			if ( newObject instanceof DimensionHandle
					&& ( type == ICrosstabConstants.COLUMN_AXIS_TYPE || type == ICrosstabConstants.ROW_AXIS_TYPE ) )
			{
				return new AddDimensionViewHandleCommand( adapter,
						type,
						(DimensionHandle) newObject );
			}
			else if ( newObject instanceof MeasureHandle
					&& position.equals( ICrosstabCellAdapterFactory.CELL_MEASURE ) )
			{
				return new AddMeasureViewHandleCommand( adapter,
						(MeasureHandle) newObject );
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
		return new CrosstabPasterCommand( (DesignElementHandle) DNDUtil.unwrapToModel( source ),
				(DesignElementHandle) DNDUtil.unwrapToModel( parentObj ),
				(DesignElementHandle) DNDUtil.unwrapToModel( afterObj ) );
	}
}
