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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editpolicies.ReportContainerEditPolicy;

/**
 * 
 */
public class CrosstabCellContainerEditPolicy extends ReportContainerEditPolicy {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.gef.editpolicies.ContainerEditPolicy#getOrphanChildrenCommand(org
	 * .eclipse.gef.requests.GroupRequest)
	 */
//	public Command getOrphanChildrenCommand( GroupRequest request )
//	{
//		List parts = request.getEditParts( );
//		int size = parts.size( );
//		CompoundCommand result = new CompoundCommand( "Move in layout" );//$NON-NLS-1$
//		for ( int i = 0; i < size; i++ )
//		{
//			Object model = ( (EditPart) parts.get( i ) ).getModel( );
//			Object parent = ( (EditPart) parts.get( i ) ).getParent( )
//					.getModel( );
//			if ( parent instanceof CrosstabCellAdapter )
//			{
//				if ( ICrosstabCellAdapterFactory.CELL_FIRST_LEVEL_HANDLE.equals( ( (CrosstabCellAdapter) parent ).getPositionType( ) )
//						|| ICrosstabCellAdapterFactory.CELL_MEASURE.equals( ( (CrosstabCellAdapter) parent ).getPositionType( ) ) )
//				{
//					if (model == ((CrosstabCellAdapter)parent).getFirstDataItem( ))
//					{
//						if (size == 1)
//						{
//							return new Command( ) {
//							};
//						}
//						else
//						{
//							return UnexecutableCommand.INSTANCE;
//						}
//					}
//					
//				}
//			}
//			result.add( new DeleteCommand( model ) );
//		}
//		return result.unwrap( );
//	}
}
