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

package org.eclipse.birt.report.designer.internal.ui.util;

import org.eclipse.birt.report.designer.internal.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

/**
 * Utility class for UI related routines.
 */

public class UIUtil
{

	/**
	 * Returns if current active editor is reportEditor.
	 * 
	 * @return Returns if current active editor is reportEditor.
	 */
	public static boolean isReportEditorActivated( )
	{
		return getActiveEditor( ) != null;
	}

	/**
	 * Returns the current active report editor. The same as getActiveEditor(
	 * true ).
	 * 
	 * @return Returns the current active report editor, or null if no report
	 *         editor is active.
	 */
	public static ReportEditor getActiveEditor( )
	{
		return getActiveEditor( true );
	}

	/**
	 * Returns the current active report editor in current active page or
	 * current active workbench.
	 * 
	 * @param activePageOnly
	 *            If this is true, only search the current active page, or will
	 *            search all pages in current workbench, returns the first
	 *            active report or null if not found.
	 * @return Returns the current active report editor, or null if no report
	 *         editor is active.
	 */
	public static ReportEditor getActiveEditor( boolean activePageOnly )
	{
		IWorkbenchWindow window = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );

		if ( window != null )
		{
			if ( activePageOnly )
			{
				IWorkbenchPage pg = window.getActivePage( );

				if ( pg != null )
				{
					IEditorPart editor = pg.getActiveEditor( );

					if ( editor != null
							&& editor.getEditorInput( ) instanceof FileEditorInput )
					{
						if ( editor instanceof ReportEditor )
						{
							return (ReportEditor) editor;
						}
					}
				}
			}
			else
			{
				IWorkbenchPage[] pgs = window.getPages( );

				for ( int i = 0; i < pgs.length; i++ )
				{
					IWorkbenchPage pg = pgs[i];

					if ( pg != null )
					{
						IEditorPart editor = pg.getActiveEditor( );

						if ( editor != null
								&& editor.getEditorInput( ) instanceof FileEditorInput )
						{
							if ( editor instanceof ReportEditor )
							{
								return (ReportEditor) editor;
							}
						}
					}
				}
			}
		}

		return null;

	}

	/**
	 * Returns current project according to current selection. 1. If current
	 * selection is editPart, get editor input and return associated project. 2.
	 * If current selection is not ediPart, use first selected element, query
	 * from its IAdaptable interface to get associated project. 3. If the above
	 * is not working, get the first accessible project in the current workspace
	 * and return it. 4. If none is accessible, returns null.
	 * 
	 * @return Returns the default project according to current selection.
	 */
	public static IProject getDefaultProject( )
	{
		IWorkbenchWindow benchWindow = PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( );
		IWorkbenchPart part = benchWindow.getPartService( ).getActivePart( );

		Object selection = null;
		if ( part instanceof IEditorPart )
		{
			selection = ( (IEditorPart) part ).getEditorInput( );
		}
		else
		{
			ISelection sel = benchWindow.getSelectionService( ).getSelection( );
			if ( ( sel != null ) && ( sel instanceof IStructuredSelection ) )
			{
				selection = ( (IStructuredSelection) sel ).getFirstElement( );
			}
		}

		if ( selection instanceof IAdaptable )
		{
			IResource resource = (IResource) ( (IAdaptable) selection ).getAdapter( IResource.class );

			if ( resource != null
					&& resource.getProject( ) != null
					&& resource.getProject( ).isAccessible( ) )
			{
				return resource.getProject( );
			}
		}

		IProject[] pjs = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getProjects( );

		for ( int i = 0; i < pjs.length; i++ )
		{
			if ( pjs[i].isAccessible( ) )
			{
				return pjs[i];
			}
		}

		return null;
	}

	/**
	 * Returns the default shell used by dialogs
	 * 
	 * @return Returns the active shell of the current display
	 */
	public static Shell getDefaultShell( )
	{
		return PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( );
	}

	/**
	 * Creates a new group under the given parent
	 * 
	 * @param parent
	 *            The parent of the new group, it should be a table or a list
	 *            and should not be null.
	 * @return Returns true if the group created successfully, false if the
	 *         creation is cancelled or some error occurred.
	 */
	public static boolean createGroup( DesignElementHandle parent )
	{
		Assert.isNotNull( parent );
		ElementFactory factory = parent.getElementFactory( );
		GroupHandle groupHandle = null;
		SlotHandle slotHandle = null;
		try
		{

			if ( parent instanceof TableHandle )
			{
				groupHandle = factory.newTableGroup( );
				slotHandle = ( (TableHandle) parent ).getGroups( );
				int columnCount = ( (TableHandle) parent ).getColumnCount( );
				groupHandle.getHeader( )
						.add( factory.newTableRow( columnCount ) );
				groupHandle.getFooter( )
						.add( factory.newTableRow( columnCount ) );
			}
			else if ( parent instanceof ListHandle )
			{
				groupHandle = factory.newListGroup( );
				slotHandle = ( (ListHandle) parent ).getGroups( );
			}
			if ( groupHandle != null )
			{
				GroupDialog dialog = new GroupDialog( getDefaultShell( ) );
				dialog.setDataSetList( DEUtil.getDataSetList( parent ) );
				dialog.setInput( groupHandle );
				if ( dialog.open( ) == Window.OK )
				{
					slotHandle.add( groupHandle );
					return true;
				}
			}
			return false;
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
	}
}