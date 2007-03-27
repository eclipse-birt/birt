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

import java.util.List;

import org.eclipse.birt.report.designer.core.commands.DeleteCommand;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.gef.requests.GroupRequest;

/**
 * 
 */

public class CrosstabCellContainerEditPolicy extends ReportContainerEditPolicy
{
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org.eclipse.gef.requests.GroupRequest)
	 */
	public Command getOrphanChildrenCommand( GroupRequest request )
	{
		List parts = request.getEditParts( );
		CompoundCommand result = new CompoundCommand( "Move in layout" );//$NON-NLS-1$
		for ( int i = 0; i < parts.size( ); i++ )
		{
			Object model =  ((EditPart) parts.get( i ) ).getModel( ) ;
			Object parent = ((EditPart) parts.get( i ) ).getParent( ).getModel( ) ;
			if (parent instanceof CrosstabCellAdapter)
			{
				if (ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals( 
						((CrosstabCellAdapter)parent).getPositionType( ))
						||ICrosstabCellAdapterFactory.CELL_MEASURE.equals( 
								((CrosstabCellAdapter)parent).getPositionType( )))
				{
					return new Command(){};
				}
			}
			result.add( new DeleteCommand(model ));
		}
		return result.unwrap( );
	}
}
