/*******************************************************************************
 * Copyright (c) 2004, 2014 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.internal.rcp.actions;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseWizardDialog;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.internal.rcp.wizards.NewReportWizard;
import org.eclipse.birt.report.designer.ui.rcp.nls.DesignerWorkbenchMessages;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

/**
 * The action to create a new report
 */

public class NewReportAction extends Action implements IWorkbenchAction
{

	private IWorkbenchWindow fWindow;

	public NewReportAction( IWorkbenchWindow window )
	{
		init( window );
		setId( "org.eclipse.birt.report.designer.rcp.internal.ui.actions.NewReportAction" ); //$NON-NLS-1$
		setText( DesignerWorkbenchMessages.Action_newReport );
		setToolTipText( DesignerWorkbenchMessages.Action_newReport );
		setImageDescriptor( ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_NEW_REPORT ) );
	}

	/*
	 * @see org.eclipse.ui.IWorkbenchWindowActionDelegate#init(org.eclipse.ui.IWorkbenchWindow)
	 */
	public void init( IWorkbenchWindow window )
	{
		if ( window == null )
		{
			throw new IllegalArgumentException( );
		}
		fWindow = window;
	}

	public void run( )
	{
		Dialog dialog = new BaseWizardDialog( fWindow.getShell( ),
				new NewReportWizard( ) );
		dialog.open( );
	}

	public void dispose( )
	{
		fWindow = null;
	}
}
