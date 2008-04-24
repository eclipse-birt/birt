/*******************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation.
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
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerView;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;

/**
 * The action class for refreshing all contents in resource explorer.
 */
public class RefreshResourceExplorerAction extends Action
{

	private LibraryExplorerTreeViewPage viewer;
	private LibraryExplorerView explorerView;
	private static final String ACTION_TEXT = Messages.getString( "RefreshLibExplorerAction.Text" ); //$NON-NLS-1$

	public RefreshResourceExplorerAction( LibraryExplorerTreeViewPage page )
	{
		super( ACTION_TEXT );
		this.viewer = page;
	}

	public RefreshResourceExplorerAction( LibraryExplorerView explorerView )
	{
		super( ACTION_TEXT );
		setImageDescriptor( ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_REFRESH ) );
		setDisabledImageDescriptor( ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_REFRESH_DISABLE ) );
		this.explorerView = explorerView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		Display.getDefault( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				if ( viewer != null )
				{
					viewer.refreshRoot( );
				}
			}
		} );
	}

	public void updateStatus( )
	{
		if ( explorerView != null
				&& explorerView.getCurrentPage( ) instanceof LibraryExplorerTreeViewPage
				&& ( (LibraryExplorerTreeViewPage) explorerView.getCurrentPage( ) ).getTreeViewer( ) != null )
		{
			viewer = ( (LibraryExplorerTreeViewPage) explorerView.getCurrentPage( ) );
			setEnabled( true );
		}
		else
		{
			viewer = null;
			setEnabled( false );
		}
	}

	@Override
	public ImageDescriptor getImageDescriptor( )
	{
		return ReportPlatformUIImages.getImageDescriptor( IReportGraphicConstants.ICON_REFRESH );
	}
}
