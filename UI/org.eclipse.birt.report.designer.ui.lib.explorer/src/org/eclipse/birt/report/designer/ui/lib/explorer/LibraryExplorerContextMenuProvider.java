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

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.ui.ContextMenuProvider;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddElementtoReport;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddLibraryAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.AddSelectedLibToCurrentReportDesignAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.DeleteLibraryAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.RefreshLibExplorerAction;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredViewer;
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

	private RefreshLibExplorerAction refreshExplorerAction;
	private AddLibraryAction addLibraryAction;
	private AddSelectedLibToCurrentReportDesignAction useLibraryAction;
	private DeleteLibraryAction deleteLibraryAction;

	/**
	 * constructor
	 * 
	 * @param page
	 *            the viewer
	 * @param registry
	 *            the registry
	 */
	public LibraryExplorerContextMenuProvider( LibraryExplorerTreeViewPage page )
	{
		super( page.getTreeViewer( ) );
		refreshExplorerAction = new RefreshLibExplorerAction( page );
		addLibraryAction = new AddLibraryAction( page.getTreeViewer( ) );
		useLibraryAction = new AddSelectedLibToCurrentReportDesignAction( page.getTreeViewer( ) );
		deleteLibraryAction = new DeleteLibraryAction( page.getTreeViewer( ) );
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
		if ( Policy.TRACING_MENU_SHOW )
		{
			System.out.println( "Menu(for Views) >> Shows for library" ); //$NON-NLS-1$
		}

		menu.add( new Separator( IWorkbenchActionConstants.MB_ADDITIONS ) );

		IStructuredSelection selection = (IStructuredSelection) getViewer( ).getSelection( );
		if ( selection != null && selection.getFirstElement( ) != null )
		{
			Object selected = selection.getFirstElement( );

			refreshExplorerAction.setSelectedElement( selected );
			menu.add( refreshExplorerAction );
			menu.add( new Separator( ) );

			if ( selected instanceof File )
			{
				if ( ( (File) selected ).isDirectory( ) )
				{
					addLibraryAction.setFolder( (File) selected );
					menu.add( addLibraryAction );
				}
				else
				{
					addLibraryAction.setFolder( ( (File) selected ).getParentFile( ) );
					menu.add( addLibraryAction );
					if ( useLibraryAction.isEnabled( ) )
					{
						menu.add( useLibraryAction );
					}
				}
			}
			else if ( canAddtoReport( selected ) )
			{
				if ( selection.size( ) == 1 )
				{
					AddElementtoReport addElementAction = new AddElementtoReport( (StructuredViewer) getViewer( ) );
					addElementAction.setSelectedElement( selected );
					menu.add( addElementAction );
				}

			}
		}
		else
		{
			refreshExplorerAction.setSelectedElement( null );
			menu.add( refreshExplorerAction );
			addLibraryAction.setFolder( null );
			menu.add( addLibraryAction );
		}
		if ( deleteLibraryAction.isEnabled( ) )
			menu.add( deleteLibraryAction );
	}

	protected boolean canAddtoReport( Object transfer )
	{
		if ( transfer instanceof ReportElementHandle
				|| transfer instanceof EmbeddedImageHandle )
		{
			if ( transfer instanceof ScalarParameterHandle
					&& ( (ScalarParameterHandle) transfer ).getContainer( ) instanceof CascadingParameterGroupHandle )
			{
				return false;
			}
			else if ( transfer instanceof StyleHandle
					&& ( (StyleHandle) transfer ).getContainer( ) instanceof ThemeHandle )
			{
				return false;
			}
			else if ( transfer instanceof ThemeHandle )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		return false;
	}

}
