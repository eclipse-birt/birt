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

package org.eclipse.birt.report.designer.ui.actions;

import java.io.File;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

/**
 * 
 */

public class PublishLibraryAction implements IWorkbenchWindowActionDelegate
{

	public void dispose( )
	{
		// TODO Auto-generated method stub

	}

	public void init( IWorkbenchWindow window )
	{
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{

		if ( isEnable( ) == false )
		{
			return;
		}

		String filePath = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		String fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );

		PublishLibraryWizard publishLibrary = new PublishLibraryWizard( (LibraryHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ),
				fileName,
				ReportPlugin.getDefault( ).getResourcePreference( ) );

		WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
				publishLibrary );

		dialog.setPageSize( 500, 250 );
		dialog.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		action.setEnabled( isEnable( ) ); //$NON-NLS-1$

	}

	private boolean isEnable( )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		if ( editor != null )
		{
			return ( editor.getEditorInput( ).getName( ).endsWith( ".rptlibrary" ) ); //$NON-NLS-1$
		}
		return false;

	}

}
