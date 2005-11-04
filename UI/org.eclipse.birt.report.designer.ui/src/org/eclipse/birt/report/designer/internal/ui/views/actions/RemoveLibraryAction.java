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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class RemoveLibraryAction extends AbstractElementAction
{

	private static final String TEXT = "Remove library";
	private static final String CONFIRM_LIBRARY_REMOVE_TITLE = "Confirm library remove";
	private static final String CONFIRM_LIBRARY_REMOVE_MESSAGE = "Do you really want to remove this library?";

	public RemoveLibraryAction( Object selectedObject )
	{
		super( selectedObject, TEXT );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{
		if ( MessageDialog.openQuestion( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				CONFIRM_LIBRARY_REMOVE_TITLE,
				CONFIRM_LIBRARY_REMOVE_MESSAGE ) )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.dropLibrary( (LibraryHandle) getSelection( ) );
			return true;
		}
		return false;
	}

}
