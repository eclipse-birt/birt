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
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportFlowLayoutEditPolicy;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateDimensionViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands.CreateMeasureViewCommand;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabHandleAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.VirtualCrosstabCellAdapter;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 */

public class VirtualCrosstabCellFlowLayoutEditPolicy extends ReportFlowLayoutEditPolicy
{
	protected Command getCreateCommand( CreateRequest request )
	{
		EditPart after = getInsertionReference( request );

		CreateCommand command = new CreateCommand( request.getExtendedData( ) );

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
}
