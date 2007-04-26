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

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.util.IVirtualValidator;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;

/**
 * Support drag the levelhandle to the crosstab.Gets the command from the editpart to execute.
 */

public class LevelHandleDropAdapter implements IDropAdapter
{

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#canDrop(java.lang.Object, java.lang.Object, int, org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( !isLevelHandle( transfer ) )
		{
			return DNDService.LOGIC_UNKNOW;
		}

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
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isLevelHandle( Object transfer )
	{
		return transfer instanceof LevelHandle;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter#performDrop(java.lang.Object, java.lang.Object, int, org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation)
	 */
	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
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
		
		if ( target instanceof EditPart )//drop on layout
		{
			EditPart editPart = (EditPart) target;

			if ( editPart != null )
			{
				CreateRequest request = new CreateRequest( );

				request.getExtendedData( )
						.put( DesignerConstants.KEY_NEWOBJECT, transfer );
				request.setLocation( location.getPoint( ) );
				Command command = editPart.getCommand( request );
				if ( command != null && command.canExecute( ) )
				{
					editPart.getViewer( )
							.getEditDomain( )
							.getCommandStack( )
							.execute( command );
					return true;
				}
				else
					return false;
			}
		}
		return false;
	}

}
