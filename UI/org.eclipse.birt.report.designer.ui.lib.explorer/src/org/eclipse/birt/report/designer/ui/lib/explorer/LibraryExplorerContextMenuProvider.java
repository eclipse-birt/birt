/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddLibraryAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddSelectedLibToCurrentReportDesignAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.RefreshLibExplorerAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.RemoveLibraryAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * This class provides the context menu for the single selection and multiple
 * selection
 * 
 * 
 */
public class LibraryExplorerContextMenuProvider extends ContextMenuProvider
{

	/**
	 * constructor
	 * 
	 * @param viewer
	 *            the viewer
	 * @param registry
	 *            the registry
	 */
	public LibraryExplorerContextMenuProvider( ISelectionProvider viewer )
	{
		super( viewer );
	}

	/**
	 * Builds the context menu. Single selection menu and multiple selection
	 * menu are created while selecting just single element or multiple elements
	 * 
	 * 
	 * @param menu
	 *            the menu
	 */
	public void buildContextMenu( IMenuManager menu )
	{
		TreeViewer treeViewer = (TreeViewer) getViewer( );

		if ( Policy.TRACING_MENU_SHOW )
		{
			System.out.println( "Menu(for Views) >> Shows for library" ); //$NON-NLS-1$
		}
		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );
		IAction addAction = new AddSelectedLibToCurrentReportDesignAction( treeViewer );
		if ( addAction.isEnabled( ) )
		{
			menu.add( addAction );
		}
		IAction refreshAction = new RefreshLibExplorerAction( treeViewer );
		if ( refreshAction.isEnabled( ) )
		{
			menu.add( refreshAction );
		}

		IAction addLibraryAction = new AddLibraryAction( );
		menu.add( addLibraryAction );
		
		IAction removeLibraryAction = new RemoveLibraryAction( treeViewer );
		if ( removeLibraryAction.isEnabled( ) )
		{
			menu.add( removeLibraryAction );
		}
	}
}
