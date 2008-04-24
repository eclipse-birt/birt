/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishResourceWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * The action class for adding a resource in resource explorer.
 */
public class AddResourceAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	public AddResourceAction( LibraryExplorerTreeViewPage viewer )
	{
		super( Messages.getString( "AddResourceAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = viewer;
	}

	@Override
	public boolean isEnabled( )
	{
		ISelection selection = viewerPage.getTreeViewer( ).getSelection( );

		if ( selection != null
				&& ( (IStructuredSelection) selection ).toList( ).size( ) == 1 )
		{
			Object resource = ( (IStructuredSelection) selection ).toList( )
					.iterator( )
					.next( );

			return ( resource instanceof PathResourceEntry ) ? !( (PathResourceEntry) resource ).isFile( )
					: false;
		}
		return false;
	}

	@Override
	public void run( )
	{

		File folder = getSelectedFile( viewerPage.getTreeViewer( ) );
		PublishResourceWizard publishLibrary = new PublishResourceWizard( folder.getAbsolutePath( ) );

		WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
				publishLibrary );

		dialog.setPageSize( 500, 250 );
		if ( dialog.open( ) == Window.OK )
		{
			viewerPage.getTreeViewer( ).refresh( );
		}
	}
}
