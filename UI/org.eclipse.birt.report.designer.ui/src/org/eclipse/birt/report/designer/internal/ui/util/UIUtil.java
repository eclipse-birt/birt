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

import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.internal.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.elements.ListItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.layout.GridLayout;
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
		return getActiveReportEditor( ) != null;
	}

	/**
	 * Returns the current active report editor. The same as getActiveEditor(
	 * true ).
	 * 
	 * @return Returns the current active report editor, or null if no report
	 *         editor is active.
	 */
	public static ReportEditor getActiveReportEditor( )
	{
		return getActiveReportEditor( true );
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
	public static ReportEditor getActiveReportEditor( boolean activePageOnly )
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
	 * Returns the current active editor part in current active page or current
	 * active workbench.
	 * 
	 * @param activePageOnly
	 *            If this is true, only search the current active page, or will
	 *            search all pages in current workbench, returns the first
	 *            active editor part or null if not found.
	 * @return Returns the current active editor part, or null if no editor part
	 *         is active.
	 */
	public static IEditorPart getActiveEditor( boolean activePageOnly )
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
					return pg.getActiveEditor( );
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

						if ( editor != null )
						{
							return editor;
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
		try
		{
			return PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( );
		}
		catch ( Exception e )
		{
			return new Shell( );
		}
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
		try
		{
			return addGroup( parent, -1 );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
	}

	public static boolean createTableGroup( RowHandle row )
	{
		Assert.isNotNull( row );
		try
		{
			TableHandle table = null;
			DesignElementHandle container = row.getContainer( );
			int slotId = row.getContainerSlotHandle( ).getSlotID( );
			int position = -1;

			if ( container instanceof TableGroupHandle )
			{
				table = (TableHandle) ( (TableGroupHandle) container ).getContainer( );
				position = DEUtil.findInsertPosition( table.getGroups( )
						.getElementHandle( ), container, table.getGroups( )
						.getSlotID( ) );
			}
			else if ( container instanceof TableHandle )
			{
				table = (TableHandle) container;
				if ( slotId == TableItem.DETAIL_SLOT )
				{
					position = -1;
				}
				else if ( slotId == TableItem.HEADER_SLOT
						|| slotId == TableItem.FOOTER_SLOT )
				{
					position = 0;
				}
			}
			else
			{
				return false;
			}

			return addGroup( table, position );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
	}

	public static boolean createListGroup( ListBandProxy listBand )
	{
		Assert.isNotNull( listBand );
		try
		{
			ListHandle list = null;
			DesignElementHandle container = listBand.getElemtHandle( );
			int slotId = listBand.getSlotId( );
			int position = -1;

			if ( container instanceof ListGroupHandle )
			{
				list = (ListHandle) ( (ListGroupHandle) container ).getContainer( );
				position = DEUtil.findInsertPosition( list.getGroups( )
						.getElementHandle( ), container, list.getGroups( )
						.getSlotID( ) );
			}
			else if ( container instanceof ListHandle )
			{
				list = (ListHandle) container;
				if ( slotId == ListItem.DETAIL_SLOT )
				{
					position = -1;
				}
				else if ( slotId == ListItem.HEADER_SLOT
						|| slotId == ListItem.FOOTER_SLOT )
				{
					position = 0;
				}
			}
			else
			{
				return false;
			}

			return addGroup( list, position );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}
	}

	private static boolean addGroup( DesignElementHandle parent, int position )
			throws SemanticException
	{
		GroupHandle groupHandle = null;
		SlotHandle slotHandle = null;
		ElementFactory factory = parent.getElementFactory( );
		if ( parent instanceof TableHandle )
		{
			groupHandle = factory.newTableGroup( );
			slotHandle = ( (TableHandle) parent ).getGroups( );
			int columnCount = ( (TableHandle) parent ).getColumnCount( );
			groupHandle.getHeader( ).add( factory.newTableRow( columnCount ) );
			groupHandle.getFooter( ).add( factory.newTableRow( columnCount ) );
		}
		else if ( parent instanceof ListHandle )
		{
			groupHandle = factory.newListGroup( );
			slotHandle = ( (ListHandle) parent ).getGroups( );
		}

		if ( groupHandle != null && slotHandle != null )
		{
			slotHandle.add( groupHandle, position );
			if ( !DEUtil.getDataSetList( parent ).isEmpty( ) )
			{//If data set can be found or a blank group will be inserted.
				GroupDialog dialog = new GroupDialog( getDefaultShell( ) );
				dialog.setDataSetList( DEUtil.getDataSetList( parent ) );
				dialog.setInput( groupHandle );
				if ( dialog.open( ) == Window.CANCEL )
				{//Cancel the action
					return false;
				}
				DataItemHandle dataItemHandle = factory.newDataItem( null );
				dataItemHandle.setValueExpr( groupHandle.getKeyExpr( ) );
				if ( parent instanceof ListHandle )
				{
					groupHandle.getHeader( ).add( dataItemHandle );
				}
				else if ( parent instanceof TableHandle )
				{
					if ( groupHandle.getHeader( ).getCount( ) != 0 )
					{
						RowHandle rowHandle = ( (RowHandle) groupHandle.getHeader( )
								.get( 0 ) );
						CellHandle cellHandle = (CellHandle) rowHandle.getCells( )
								.get( 0 );
						cellHandle.getContent( ).add( dataItemHandle );
					}
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the first selected editpart in layout editor. Whenever the user has
	 * deselected all editparts, the contents editpart should be returned.
	 * 
	 * @return the first selected EditPart or root editpart
	 */
	public static EditPart getCurrentEditPart( )
	{
		EditPartViewer viewer = getLayoutEditPartViewer( );
		if ( viewer == null )
			return null;
		IStructuredSelection targets = (IStructuredSelection) viewer.getSelection( );
		if ( targets.isEmpty( ) )
			return null;
		return (EditPart) targets.getFirstElement( );
	}

	/**
	 * Gets EditPartViewer in layout editor.
	 * 
	 * @return EditPartViewer in layout editor. Return null if not found.
	 */
	public static EditPartViewer getLayoutEditPartViewer( )
	{
		ReportEditor reportEditor = (ReportEditor) PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( )
				.getActivePage( )
				.getActiveEditor( );
		if ( !( reportEditor.getActiveEditor( ) instanceof GraphicalEditorWithFlyoutPalette ) )
			return null;
		return ( (GraphicalEditorWithFlyoutPalette) reportEditor.getActiveEditor( ) ).getGraphicalViewer( );
	}

	/**
	 * Creates a new grid layout without margins by default
	 * 
	 * @return Returns the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin( )
	{
		GridLayout layout = new GridLayout( );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		return layout;
	}

	/**
	 * Creates a new grid layout without margins with given the number of
	 * columns, and whether or not the columns should be forced to have the same
	 * width
	 * 
	 * @param numColumns
	 *            the number of columns in the grid
	 * @param makeColumnsEqualWidth
	 *            whether or not the columns will have equal width
	 * 
	 * @return Returns the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin( int numsColumn,
			boolean makeColumnsEqualWidth )
	{
		GridLayout layout = new GridLayout( numsColumn, makeColumnsEqualWidth );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		return layout;
	}
}