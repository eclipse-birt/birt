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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.GroupDialog;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.AbstractMultiPageLayoutEditor;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.DeferredGraphicalViewer;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.GridEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorInput;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.AbstractTreeViewer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.ScrollBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.ILayoutExtension;
import org.osgi.framework.Bundle;

/**
 * Utility class for UI related routines.
 */

public class UIUtil
{

	/**
	 * Returns if current active editor is reportEditor.
	 * 
	 * @return true if current active editor is reportEditor, or false else.
	 */
	public static boolean isReportEditorActivated( )
	{
		return getActiveReportEditor( ) != null;
	}

	/**
	 * Returns the current active report editor. The same as getActiveEditor(
	 * true ).
	 * 
	 * @return the current active report editor, or null if no report editor is
	 *         active.
	 */
	public static AbstractMultiPageLayoutEditor getActiveReportEditor( )
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
	 * @return the current active report editor, or null if no report editor is
	 *         active.
	 */
	public static AbstractMultiPageLayoutEditor getActiveReportEditor( boolean activePageOnly )
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
							&& editor.getEditorInput( ) instanceof IReportEditorInput )
					{
						if ( editor instanceof AbstractMultiPageLayoutEditor )
						{
							return (AbstractMultiPageLayoutEditor) editor;
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
								&& editor.getEditorInput( ) instanceof IReportEditorInput )
						{
							if ( editor instanceof AbstractMultiPageLayoutEditor )
							{
								return (AbstractMultiPageLayoutEditor) editor;
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
	 * @return the current active editor part, or null if no editor part is
	 *         active.
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
	 * @return the default project according to current selection.
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
	 * @return the active shell of the current display
	 */
	public static Shell getDefaultShell( )
	{
		Shell shell = null;
		try
		{
			shell = PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( );
			if ( shell == null )
			{
				shell = Display.getCurrent( ).getActiveShell( );
			}
			if ( shell == null )
			{
				shell = PlatformUI.getWorkbench( )
						.getActiveWorkbenchWindow( )
						.getShell( );
			}
		}
		catch ( Exception e )
		{
		}
		if ( shell == null )
		{
			return new Shell( );
		}
		return shell;
	}

	public static ElementFactory getElementFactory( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getElementFactory( );
	}

	/**
	 * Creates a new group under the given parent
	 * 
	 * @param parent
	 *            The parent of the new group, it should be a table or a list
	 *            and should not be null.
	 * @return true if the group created successfully, false if the creation is
	 *         cancelled or some error occurred.
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

	/**
	 * Creates a new group in the position under the given parent
	 * 
	 * @param parent
	 *            The parent of the new group, it should be a table or a list
	 *            and should not be null.
	 * @param position
	 *            insert position
	 * @return true if the group created successfully, false if the creation is
	 *         cancelled or some error occurred.
	 */
	public static boolean createGroup( DesignElementHandle parent, int position )
	{
		Assert.isNotNull( parent );
		try
		{
			return addGroup( parent, position );
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
		// ElementFactory factory = parent.getElementFactory( );
		DesignElementFactory factory = DesignElementFactory.getInstance( parent.getModuleHandle( ) );
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
			{// If data set can be found or a blank group will be inserted.
				GroupDialog dialog = new GroupDialog( getDefaultShell( ) );
				dialog.setDataSetList( DEUtil.getDataSetList( parent ) );
				dialog.setInput( groupHandle );
				if ( dialog.open( ) == Window.CANCEL )
				{// Cancel the action
					return false;
				}
			}
			return true;
		}
		return false;
	}

	/**
	 * Gets the first selected edit part in layout editor. Whenever the user has
	 * deselected all edit parts, the contents edit part should be returned.
	 * 
	 * @return the first selected EditPart or root edit part
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
	 * @return the EditPartViewer in layout editor, or null if not found.
	 */
	public static EditPartViewer getLayoutEditPartViewer( )
	{
		AbstractMultiPageLayoutEditor reportEditor = (AbstractMultiPageLayoutEditor) PlatformUI.getWorkbench( )
				.getActiveWorkbenchWindow( )
				.getActivePage( )
				.getActiveEditor( );
		if ( reportEditor == null
				|| !( reportEditor.getActiveEditor( ) instanceof GraphicalEditorWithFlyoutPalette ) )
		{
			return null;
		}
		return ( (GraphicalEditorWithFlyoutPalette) reportEditor.getActiveEditor( ) ).getGraphicalViewer( );
	}

	/**
	 * Creates a new grid layout without margins by default
	 * 
	 * @return the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin( )
	{
		GridLayout layout = new GridLayout( );
		layout.marginHeight = layout.marginWidth = 0;
		return layout;
	}

	/**
	 * Creates a new grid layout without margins with given the number of
	 * columns, and whether or not the columns should be forced to have the same
	 * width
	 * 
	 * @param numsColumn
	 *            the number of columns in the grid
	 * @param makeColumnsEqualWidth
	 *            whether or not the columns will have equal width
	 * 
	 * @return the layout created
	 */
	public static GridLayout createGridLayoutWithoutMargin( int numsColumn,
			boolean makeColumnsEqualWidth )
	{
		GridLayout layout = new GridLayout( numsColumn, makeColumnsEqualWidth );
		layout.marginHeight = layout.marginWidth = 0;
		return layout;
	}

	/**
	 * Convert the give string to GUI style, which cannot be null
	 * 
	 * @param string
	 *            the string to convert
	 * @return the string, or an empty string for null
	 */
	public static String convertToGUIString( String string )
	{
		if ( string == null )
		{
			string = ""; //$NON-NLS-1$
		}
		return string;
	}

	/**
	 * Convert the give string to Model style
	 * 
	 * @param string
	 *            the string to convert
	 * @param trim
	 *            specify if the string needs to be trimmed
	 * @return the string, or null for an empty string
	 */
	public static String convertToModelString( String string, boolean trim )
	{
		if ( string == null )
		{
			return null;
		}
		if ( trim )
		{
			string = string.trim( );
		}
		if ( string.length( ) == 0 )
		{
			string = null; //$NON-NLS-1$
		}
		return string;
	}

	/**
	 * Returns the width hint for the given control.
	 * 
	 * @param wHint
	 *            the width hint
	 * @param c
	 *            the control
	 * 
	 * @return the width hint
	 */
	public static int getWidthHint( int wHint, Control c )
	{
		boolean wrap = isWrapControl( c );
		return wrap ? wHint : SWT.DEFAULT;
	}

	/**
	 * Returns the height hint for the given control.
	 * 
	 * @param hHint
	 *            the width hint
	 * @param c
	 *            the control
	 * 
	 * @return the height hint
	 */
	public static int getHeightHint( int hHint, Control c )
	{
		if ( c instanceof Composite )
		{
			Layout layout = ( (Composite) c ).getLayout( );
			if ( layout instanceof ColumnLayout )
				return hHint;
		}
		return SWT.DEFAULT;
	}

	/**
	 * Updates the page scroll increment for given composite.
	 * 
	 * @param scomp
	 */
	public static void updatePageIncrement( ScrolledComposite scomp )
	{
		ScrollBar vbar = scomp.getVerticalBar( );
		if ( vbar != null )
		{
			Rectangle clientArea = scomp.getClientArea( );
			int increment = clientArea.height - 5;
			vbar.setPageIncrement( increment );
		}
	}

	private static boolean isWrapControl( Control c )
	{
		if ( c instanceof Composite )
		{
			return ( (Composite) c ).getLayout( ) instanceof ILayoutExtension;
		}
		return ( c.getStyle( ) & SWT.WRAP ) != 0;
	}

	/**
	 * Returns table editpart.
	 * 
	 * @param editParts
	 *            a list of editpart
	 * @return the current selected table editpart, null if no table editpart,
	 *         more than one table, or other non-table editpart. Cell editpart
	 *         is also a type of table editpart.
	 */
	public static TableEditPart getTableEditPart( List editParts )
	{
		if ( editParts == null || editParts.isEmpty( ) )
			return null;
		int size = editParts.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = editParts.get( i );

			TableEditPart currentEditPart = null;
			if ( obj instanceof TableEditPart )
			{
				currentEditPart = (TableEditPart) obj;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				currentEditPart = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
			}
			else if ( obj instanceof DummyEditpart )
			{
				continue;
			}
			if ( part == null )
			{
				part = currentEditPart;
			}
			// Check if select only one table
			if ( currentEditPart == null
					|| currentEditPart != null && part != currentEditPart )
			{
				return null;
			}
		}
		// Only table permitted
		if ( part instanceof GridEditPart )
			return null;
		return part;
	}

	/**
	 * Returns list editpart.
	 * 
	 * @param editParts
	 *            a list of editpart
	 * @return the current selected list editpart, null if no list editpart,
	 *         more than one list, or other list editpart. List band editpart is
	 *         also a type of list editpart.
	 */
	public static ListEditPart getListEditPart( List editParts )
	{
		if ( editParts == null || editParts.isEmpty( ) )
			return null;
		int size = editParts.size( );
		ListEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = editParts.get( i );

			ListEditPart currentEditPart = null;
			if ( obj instanceof ListEditPart )
			{
				currentEditPart = (ListEditPart) obj;
			}
			else if ( obj instanceof ListBandEditPart )
			{
				currentEditPart = (ListEditPart) ( (ListBandEditPart) obj ).getParent( );
			}
			if ( part == null )
			{
				part = currentEditPart;
			}
			// Check if select only one list
			if ( currentEditPart == null
					|| currentEditPart != null && part != currentEditPart )
			{
				return null;
			}
		}
		return part;
	}

	/**
	 * Tests if the specified element is on the given tree viewer
	 * 
	 * @param treeViewer
	 *            the tree viewer
	 * @param element
	 *            the element
	 * 
	 * @return true if the element is on the tree, or false else.
	 */
	public static boolean containElement( AbstractTreeViewer treeViewer,
			Object element )
	{
		ITreeContentProvider provider = (ITreeContentProvider) treeViewer.getContentProvider( );
		Object input = treeViewer.getInput( );
		if ( input instanceof Object[] )
		{
			Object[] inputs = (Object[]) input;
			for ( int i = 0; i < inputs.length; i++ )
			{
				if ( containElement( inputs[i], provider, element ) )
				{
					return true;
				}
			}
			return false;
		}
		return containElement( input, provider, element );
	}

	private static boolean containElement( Object parent,
			ITreeContentProvider provider, Object element )
	{
		if ( parent == null )
		{
			return false;
		}
		if ( parent == element || parent.equals( element ) )
		{
			return true;
		}

		if ( provider == null )
		{
			return false;
		}
		Object[] children = provider.getChildren( parent );
		for ( int i = 0; i < children.length; i++ )
		{
			if ( containElement( children[i], provider, element ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns the plug-in provider
	 * 
	 * @param pluginId
	 *            the identify of the plugin
	 * 
	 * @return the plug-in provider, or null if the plug-in is not found
	 */
	public static String getPluginProvider( String pluginId )
	{
		return getBundleValue( pluginId,
				org.osgi.framework.Constants.BUNDLE_VENDOR );
	}

	/**
	 * Returns the plug-in name
	 * 
	 * @param pluginId
	 *            the identify of the plugin
	 * 
	 * @return the plug-in name, or null if the plug-in is not found
	 */
	public static String getPluginName( String pluginId )
	{
		return getBundleValue( pluginId,
				org.osgi.framework.Constants.BUNDLE_NAME );
	}

	/**
	 * Returns the plug-in version
	 * 
	 * @param pluginId
	 *            the identify of the plugin
	 * 
	 * @return the plug-in version, or null if the plug-in is not found
	 */
	public static String getPluginVersion( String pluginId )
	{
		return getBundleValue( pluginId,
				org.osgi.framework.Constants.BUNDLE_VERSION );
	}

	private static String getBundleValue( String pluginId, String key )
	{
		Assert.isNotNull( pluginId );
		Bundle bundle = Platform.getBundle( pluginId );
		if ( bundle != null )
		{
			return (String) bundle.getHeaders( ).get( key );
		}
		return null;
	}

	public static void resetViewSelection( final EditPartViewer viewer,
			final boolean notofyToMedia )
	{
		final List list = new ArrayList( ( (StructuredSelection) viewer.getSelection( ) ).toList( ) );

		boolean hasColumnOrRow = false;
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( list.get( i ) instanceof TableEditPart.DummyRowEditPart
					|| list.get( i ) instanceof TableEditPart.DummyColumnEditPart )
			{
				hasColumnOrRow = true;
				break;
			}
		}

		if ( hasColumnOrRow )
		{
			int selectionType = 0;// 0 select row 1select colum
			TableEditPart part = null;
			int[] selectContents = new int[0];
			for ( int i = 0; i < list.size( ); i++ )
			{
				Object obj = list.get( i );
				int number = -1;
				if ( obj instanceof TableEditPart.DummyRowEditPart )
				{
					selectionType = 0;// select row
					number = ( (TableEditPart.DummyRowEditPart) obj ).getRowNumber( );
				}
				else if ( obj instanceof TableEditPart.DummyColumnEditPart )
				{
					selectionType = 1;// select column
					number = ( (TableEditPart.DummyColumnEditPart) obj ).getColumnNumber( );
				}
				else if ( obj instanceof TableCellEditPart )
				{
					part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
				}
				if ( number != -1 )
				{
					int lenegth = selectContents.length;
					int[] temp = new int[lenegth + 1];

					System.arraycopy( selectContents, 0, temp, 0, lenegth );
					temp[lenegth] = number;
					selectContents = temp;
				}
			}
			if ( part == null
					|| selectContents.length == 0
					|| !viewer.getControl( ).isVisible( ) )
			{
				return;
			}

			if ( selectionType == 0 )
			{
				part.selectRow( selectContents );
			}
			else if ( selectionType == 1 )
			{
				part.selectColumn( selectContents );
			}

		}
		else
		{
			if ( viewer.getControl( ).isVisible( ) )
			{
				if ( viewer instanceof DeferredGraphicalViewer )
					( (DeferredGraphicalViewer) viewer ).setSelection( new StructuredSelection( list ),
							notofyToMedia );
			}

		}
	}

	/**
	 * Creates a folder resource given the folder handle.
	 * 
	 * @param folderHandle
	 *            the folder handle to create a folder resource for
	 * @param monitor
	 *            the progress monitor to show visual progress with
	 * @exception CoreException
	 *                if the operation fails
	 * @exception OperationCanceledException
	 *                if the operation is canceled
	 */
	public static void createFolder( IFolder folderHandle,
			IProgressMonitor monitor ) throws CoreException
	{
		try
		{
			// Create the folder resource in the workspace
			// Update: Recursive to create any folders which do not exist
			// already
			if ( !folderHandle.exists( ) )
			{
				IPath path = folderHandle.getFullPath( );
				IWorkspaceRoot root = ResourcesPlugin.getWorkspace( ).getRoot( );
				int numSegments = path.segmentCount( );
				if ( numSegments > 2
						&& !root.getFolder( path.removeLastSegments( 1 ) )
								.exists( ) )
				{
					// If the direct parent of the path doesn't exist, try
					// to create the
					// necessary directories.
					for ( int i = numSegments - 2; i > 0; i-- )
					{
						IFolder folder = root.getFolder( path.removeLastSegments( i ) );
						if ( !folder.exists( ) )
						{
							folder.create( false, true, monitor );
						}
					}
				}
				folderHandle.create( false, true, monitor );
			}
		}
		catch ( CoreException e )
		{
			// If the folder already existed locally, just refresh to get
			// contents
			if ( e.getStatus( ).getCode( ) == IResourceStatus.PATH_OCCUPIED )
				folderHandle.refreshLocal( IResource.DEPTH_INFINITE,
						new SubProgressMonitor( monitor, 500 ) );
			else
				throw e;
		}

		if ( monitor.isCanceled( ) )
			throw new OperationCanceledException( );
	}

}