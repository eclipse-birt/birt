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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;


/**
 * 
 */

public class DeleteStyleAction extends DynamicItemAction
{
	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteStyleAction"; //$NON-NLS-1$

	private SharedStyleHandle handle;
	
	private DeleteAction action = null;

	/**
	 * @param handle
	 */
	public DeleteStyleAction( SharedStyleHandle handle )
	{
		this.handle = handle;
		setId( ID );
		if ( handle.getContainerSlotHandle( ).getElementHandle( ) instanceof ThemeHandle )
		{
			setText( ( (ThemeHandle) handle.getContainerSlotHandle( )
					.getElementHandle( ) ).getName( ) + "." //$NON-NLS-1$
					+ DEUtil.getEscapedMenuItemText( handle.getDisplayLabel( ) ) );
		}
		else
		{
			setText( DEUtil.getEscapedMenuItemText( handle.getDisplayLabel( ) ) );
		}
		action = new DeleteAction(handle);
	}

	/*
	 * (non-Javadoc) Method declared on IAction.
	 */
	public boolean isEnabled() {
		return action.isEnabled( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		if ( Policy.TRACING_ACTIONS )
		{
			System.out.println( "Edit style action >> Run ..." ); //$NON-NLS-1$
		}
		
		action.run( );

	}

}
