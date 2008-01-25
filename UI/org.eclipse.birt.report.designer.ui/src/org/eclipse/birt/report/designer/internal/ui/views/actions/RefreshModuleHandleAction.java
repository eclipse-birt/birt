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

import java.util.logging.Level;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;

/**
 * 
 */

public class RefreshModuleHandleAction extends AbstractViewAction
{

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshModuleHandleAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString( "RefreshModuleHandleAction.Action.Text" ); //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public RefreshModuleHandleAction( Object selectedObject )
	{
		super( selectedObject, ACTION_TEXT );
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public RefreshModuleHandleAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled( )
	{
		if ( getSelection( ) instanceof ReportDesignHandle
				|| getSelection( ) instanceof LibraryHandle )
		{
			return true;
		}
		else
			return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		try
		{
			CommandUtils.executeCommand("org.eclipse.birt.report.designer.ui.command.refreshLibraryCommand", null); //$NON-NLS-1$
		}
		catch ( Exception e )
		{
			logger.log(Level.SEVERE, e.getMessage(),e);
		}
	}
}
