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

import org.eclipse.birt.report.designer.internal.ui.dialogs.StyleBuilderDialog;
import org.eclipse.birt.report.designer.ui.actions.MenuUpdateAction.DynamicItemAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.ui.PlatformUI;

/**
 * Edits style.
 */

public class EditStyleAction extends DynamicItemAction
{

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.EditStyleAction"; //$NON-NLS-1$

	private SharedStyleHandle handle;

	/**
	 * @param handle
	 */
	public EditStyleAction( SharedStyleHandle handle )
	{
		this.handle = handle;
		setId( ID );
		setText( DEUtil.getEscapedMenuItemText( handle.getDisplayLabel( ) ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		StyleBuilderDialog d = new StyleBuilderDialog( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				handle );
		d.open( );
	}
}