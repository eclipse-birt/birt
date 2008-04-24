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

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.wizards.NewLibraryWizard;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;

/**
 * The action class for creating a libary in resource explorer.
 */
public class NewLibraryAction extends ResourceAction
{

	private LibraryExplorerTreeViewPage viewerPage;

	public NewLibraryAction( LibraryExplorerTreeViewPage page )
	{
		super( Messages.getString( "NewLibraryAction.Text" ) ); //$NON-NLS-1$
		this.viewerPage = page;
	}

	@Override
	public void run( )
	{
		Dialog dialog = new WizardDialog( viewerPage.getSite( ).getShell( ),
				new NewLibraryWizard( ) {

					@Override
					protected IPath getDefaultContainerPath( )
					{
						return NewLibraryAction.this.getContainer( );
					}
				} );

		if ( dialog.open( ) == Window.OK )
		{
			viewerPage.refreshRoot( );
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_NEW_LIBRARY );
	}

	/**
	 * Returns the container to hold the pasted resources.
	 * 
	 * @return the container to hold the pasted resources.
	 */
	private IPath getContainer( )
	{
		IPath path = new Path( getSelectedFile( viewerPage.getTreeViewer( ) ).getAbsolutePath( ) );
		IContainer container = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getContainerForLocation( path );

		IPath containerPath = container == null ? null
				: container.getFullPath( );

		return containerPath == null ? path : containerPath;
	}
}
