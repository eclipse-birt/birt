/*************************************************************************************
 * Copyright (c) 2004-2008 Actuate Corporation and others.
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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.preference.IPreferenceChangeListener;
import org.eclipse.birt.core.preference.PreferenceChangeEvent;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceLocator;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.outline.ItemSorter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.lib.explorer.action.ResourceAction;
import org.eclipse.birt.report.designer.ui.lib.explorer.dnd.LibraryDragListener;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.DesignElementEntry;
import org.eclipse.birt.report.designer.ui.lib.explorer.resource.ResourceEntryWrapper;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ParameterGroupHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.ResourceChangeEvent;
import org.eclipse.birt.report.model.api.core.IResourceChangeListener;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.validators.IValidationListener;
import org.eclipse.birt.report.model.api.validators.ValidationEvent;
import org.eclipse.core.runtime.Platform;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IOpenListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.OpenEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;

/**
 * This class represents the tree view page of the data view
 * 
 */
public class LibraryExplorerTreeViewPage extends LibraryExplorerViewPage implements
		IValidationListener,
		IPreferenceChangeListener,
		IResourceChangeListener
{

	// private static final String LABEL_DOUBLE_CLICK = Messages.getString(
	// "DataViewTreeViewerPage.tooltip.DoubleClickToEdit" ); //$NON-NLS-1$

	private static final String BUNDLE_PROTOCOL = "bundleresource://"; //$NON-NLS-1$

	private TreeViewerBackup libraryBackup;

	private LibraryExplorerContextMenuProvider menuManager = null;

	public LibraryExplorerTreeViewPage( )
	{
		super( );
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.addResourceChangeListener( this );
	}

	@Override
	public void createControl( Composite parent )
	{
		super.createControl( parent );
		initPage( );
		refreshRoot( );
	}

	/**
	 * Creates the tree view
	 * 
	 * @param parent
	 *            the parent
	 */
	protected TreeViewer createTreeViewer( Composite parent )
	{
		TreeViewer treeViewer = new TreeViewer( parent, SWT.SINGLE
				| SWT.H_SCROLL
				| SWT.V_SCROLL );

		treeViewer.setSorter( new ItemSorter( ) {

			@Override
			public int compare( Viewer viewer, Object e1, Object e2 )
			{
				if ( e1 instanceof ResourceEntry && e2 instanceof ResourceEntry )
				{
					// check same type
					if ( ( ( (ResourceEntry) e1 ).isFile( ) ^ ( (ResourceEntry) e2 ).isFile( ) ) )
					{
						// place folder first
						return ( (ResourceEntry) e1 ).isFile( ) ? 1 : -1;
					}
				}

				if ( e1 instanceof DesignElementEntry )
					e1 = ( (DesignElementEntry) e1 ).getReportElement( );
				if ( e2 instanceof DesignElementEntry )
					e2 = ( (DesignElementEntry) e2 ).getReportElement( );
				return super.compare( viewer, e1, e2 );
			}

		} );
		configTreeViewer( treeViewer );
		return treeViewer;
	}

	/**
	 * Configures the tree viewer.
	 * 
	 * @param treeViewer
	 *            the tree viewer to config.
	 */
	protected void configTreeViewer( final TreeViewer treeViewer )
	{
		ViewsTreeProvider provider = new LibraryExplorerProvider( );

		treeViewer.setContentProvider( provider );
		treeViewer.setLabelProvider( provider );

		// Adds drag and drop support
		int ops = DND.DROP_COPY | DND.DROP_LINK;
		Transfer[] transfers = new Transfer[]{
			TemplateTransfer.getInstance( )
		};
		treeViewer.addDragSupport( ops,
				transfers,
				new LibraryDragListener( treeViewer ) );

		treeViewer.getControl( ).addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{

			}

			public void keyReleased( KeyEvent e )
			{
				if ( e.keyCode == SWT.F5 )
				{
					treeViewer.refresh( );
				}
			}
		} );

		treeViewer.getTree( ).addDisposeListener( new DisposeListener( ) {

			public void widgetDisposed( DisposeEvent e )
			{
				Object input = treeViewer.getInput( );
				if ( input instanceof Object[] )
				{
					Object[] array = (Object[]) input;
					for ( int i = 0; i < array.length; i++ )
					{
						if ( array[i] instanceof ResourceEntry )
							( (ResourceEntry) array[i] ).dispose( );
					}

				}
			}

		} );

		TreeListener libraryTreeListener = new TreeListener( ) {

			public void treeCollapsed( TreeEvent e )
			{
				Item item = (Item) e.item;
				if ( libraryBackup != null )
					libraryBackup.updateCollapsedStatus( treeViewer,
							item.getData( ) );

			}

			public void treeExpanded( TreeEvent e )
			{
				Item item = (Item) e.item;
				if ( libraryBackup != null )
					libraryBackup.updateExpandedStatus( treeViewer,
							item.getData( ) );
			}

		};
		treeViewer.getTree( ).addTreeListener( libraryTreeListener );
		treeViewer.addDoubleClickListener( new IDoubleClickListener( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
			 */
			public void doubleClick( DoubleClickEvent event )
			{
				handleDoubleClick( event );
			}
		} );

		treeViewer.addOpenListener( new IOpenListener( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.jface.viewers.IDoubleClickListener#doubleClick(org.eclipse.jface.viewers.DoubleClickEvent)
			 */
			public void open( OpenEvent event )
			{
				try
				{
					handleOpen( event );
				}
				catch ( IOException e )
				{
					ExceptionHandler.handle( e );
				}
			}
		} );
	}

	/**
	 * Handles a double-click event from the viewer.
	 * 
	 * @param event
	 *            the double-click event
	 */
	protected void handleDoubleClick( DoubleClickEvent event )
	{
		IStructuredSelection selection = (IStructuredSelection) event.getSelection( );
		Object element = selection.getFirstElement( );
		TreeViewer viewer = getTreeViewer( );

		if ( element instanceof ResourceEntryWrapper )
		{
			switch ( ( (ResourceEntryWrapper) element ).getType( ) )
			{
				case ResourceEntryWrapper.LIBRARY :
					return;

				case ResourceEntryWrapper.CSS_STYLE_SHEET :
				default :
					break;
			}
		}

		if ( viewer.isExpandable( element ) )
		{
			viewer.setExpandedState( element,
					!viewer.getExpandedState( element ) );
		}
	}

	/**
	 * Handles an open event from the viewer. Opens an editor on the selected
	 * library.
	 * 
	 * @param event
	 *            the open event
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	protected void handleOpen( OpenEvent event ) throws IOException
	{
		IStructuredSelection selection = (IStructuredSelection) event.getSelection( );
		Object element = selection.getFirstElement( );

		if ( element instanceof ResourceEntryWrapper
				&& ( (ResourceEntryWrapper) element ).isFile( ) )
		{
			switch ( ( (ResourceEntryWrapper) element ).getType( ) )
			{
				case ResourceEntryWrapper.LIBRARY :
					File file = null;
					URL url = ( (ResourceEntryWrapper) element ).getURL( );

					if ( ( (ResourceEntryWrapper) element ).getEntry( ) instanceof FragmentResourceEntry )
					{
						file = ResourceAction.convertToFile( Platform.getBundle( IResourceLocator.FRAGMENT_RESOURCE_HOST )
								.getEntry( url.getPath( ) ) );
					}
					else
					{
						file = ResourceAction.convertToFile( url );
					}
					ResourceAction.openLibrary( this, file );
					break;

				case ResourceEntryWrapper.CSS_STYLE_SHEET :
				default :
					break;
			}
		}
	}

	/**
	 * Initializes the data view page.
	 */
	protected void initPage( )
	{
		createContextMenus( );

		// !remove sorter to keep same order with outline view
		// treeViewer.setSorter( new ViewerSorter( ) {
		//
		// public int category( Object element )
		// {
		// if ( element instanceof LibraryHandle )
		// {
		// return 1;
		// }
		// return super.category( element );
		// }
		//
		// } );
		
		final Tree tree = getTreeViewer( ).getTree( );

		tree.addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == tree )
				{
					Point pt = new Point( event.x, event.y );
					TreeItem item = tree.getItem( pt );

					try
					{
						tree.setToolTipText( getTooltip( item ) );
					}
					catch ( IOException e )
					{
						// Does nothing
					}
				}
			}
		} );
	}

	/**
	 * Creates the context menu
	 */
	private void createContextMenus( )
	{
		menuManager = new LibraryExplorerContextMenuProvider( this );

		Control control = getTreeViewer( ).getControl( );
		Menu menu = menuManager.createContextMenu( control );

		control.setMenu( menu );
	}

	private String getTooltip( TreeItem item ) throws IOException
	{
		if ( item != null )
		{
			Object object = item.getData( );
			if ( object instanceof DataSourceHandle
					|| object instanceof DataSetHandle )
			{
				return Messages.getString( "LibraryExplorerTreeViewPage.toolTips.DragAndDropOutline" ); //$NON-NLS-1$
			}
			else if ( object instanceof ThemeHandle )
			{
				return Messages.getString( "LibraryExplorerTreeViewPage.toolTips.DragAndDropLayout" ); //$NON-NLS-1$
			}
			else if ( object instanceof ParameterHandle
					|| object instanceof ParameterGroupHandle
					|| object instanceof EmbeddedImageHandle
					|| object instanceof ReportItemHandle )
			{
				return Messages.getString( "LibraryExplorerTreeViewPage.toolTips.DragAndDropToOutlineORLayout" ); //$NON-NLS-1$
			}
			else if ( object instanceof LibraryHandle )
			{
				return ( (LibraryHandle) object ).getFileName( );
			}
			else if ( object instanceof CssStyleSheetHandle )
			{
				CssStyleSheetHandle CssStyleSheetHandle = (CssStyleSheetHandle) object;
				if ( CssStyleSheetHandle.getFileName( )
						.startsWith( BUNDLE_PROTOCOL ) )
				{
					return CssStyleSheetHandle.getFileName( );
				}
				else
				{
					ModuleHandle moudleHandle = CssStyleSheetHandle.getModule( )
							.getModuleHandle( );
					URL url = moudleHandle.findResource( CssStyleSheetHandle.getFileName( ),
							IResourceLocator.CASCADING_STYLE_SHEET );

					if ( url != null )
					{
						return ResourceAction.convertToFile( url )
								.getAbsolutePath( );
					}
				}
			}
			else if ( object instanceof ResourceEntryWrapper )
			{
				URL url = ( (ResourceEntryWrapper) object ).getURL( );
				File file = null;

				if ( ( (ResourceEntryWrapper) object ).getParent( ) instanceof FragmentResourceEntry )
				{
					file = ResourceAction.convertToFile( Platform.getBundle( IResourceLocator.FRAGMENT_RESOURCE_HOST )
							.getEntry( url.getPath( ) ) );
				}
				else
				{
					file = ResourceAction.convertToFile( url );
				}
				return file == null ? null : file.getAbsolutePath( );
			}
			else if ( object instanceof ResourceEntryWrapper
					&& ( (ResourceEntryWrapper) object ).getType( ) == ResourceEntryWrapper.LIBRARY )
			{
				LibraryHandle libHandle = (LibraryHandle) ( (ResourceEntryWrapper) object ).getAdapter( LibraryHandle.class );

				return libHandle.getFileName( );
			}
			else if ( object instanceof ResourceEntryWrapper
					&& ( (ResourceEntryWrapper) object ).getType( ) == ResourceEntryWrapper.CSS_STYLE_SHEET )
			{
				CssStyleSheetHandle cssHandle = (CssStyleSheetHandle) ( (ResourceEntryWrapper) object ).getAdapter( CssStyleSheetHandle.class );

				if ( cssHandle.getFileName( ).startsWith( BUNDLE_PROTOCOL ) )
				{
					return cssHandle.getFileName( );
				}
				else
				{
					ModuleHandle moudleHandle = cssHandle.getModule( )
							.getModuleHandle( );
					URL url = moudleHandle.findResource( cssHandle.getFileName( ),
							IResourceLocator.CASCADING_STYLE_SHEET );
					if ( url != null )
					{
						return ResourceAction.convertToFile( url )
								.getAbsolutePath( );
					}
				}
			}
			else if ( object instanceof PathResourceEntry )
			{
				URL url = ( (PathResourceEntry) object ).getURL( );

				return ResourceAction.convertToFile( url ).getAbsolutePath( );
			}
			else if ( object instanceof FragmentResourceEntry )
			{
				URL url = ( (FragmentResourceEntry) object ).getURL( );

				return ResourceAction.convertToFile( Platform.getBundle( IResourceLocator.FRAGMENT_RESOURCE_HOST )
						.getEntry( url.getPath( ) ) )
						.getAbsolutePath( );
			}
		}
		return null;
	}

	/**
	 * The <code>Page</code> implementation of this <code>IPage</code>
	 * method disposes of this page's control (if it has one and it has not
	 * already been disposed). Disposes the visitor of the element
	 */
	public void dispose( )
	{
		SessionHandleAdapter.getInstance( )
				.getSessionHandle( )
				.removeResourceChangeListener( this );
		libraryBackup.dispose( );

		if ( menuManager != null )
		{
			menuManager.dispose( );
			menuManager = null;
		}
		super.dispose( );
	}

	protected boolean isDisposed( )
	{
		Control ctrl = getControl( );
		return ( ctrl == null || ctrl.isDisposed( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.validators.IValidationListener#elementValidated(org.eclipse.birt.report.model.api.DesignElementHandle,
	 *      org.eclipse.birt.report.model.api.validators.ValidationEvent)
	 */
	public void elementValidated( DesignElementHandle targetElement,
			ValidationEvent ev )
	{
		TreeViewer treeViewer = getTreeViewer( );
		
		if ( treeViewer != null && !treeViewer.getTree( ).isDisposed( ) )
		{
			treeViewer.refresh( );
			treeViewer.setInput( ResourceLocator.getRootEntries( ) );
			handleTreeViewerRefresh( );
		}
	}

	private void handleTreeViewerRefresh( )
	{
		TreeViewer treeViewer = getTreeViewer( );

		if ( libraryBackup != null )
		{
			libraryBackup.restoreBackup( treeViewer );
		}
		else
		{
			libraryBackup = new TreeViewerBackup( );
			treeViewer.expandToLevel( 2 );
			libraryBackup.updateStatus( treeViewer );
		}
	}

	public void refreshRoot( )
	{
		TreeViewer treeViewer = getTreeViewer( );

		if ( treeViewer != null && !treeViewer.getTree( ).isDisposed( ) )
		{
			ISelection selection = getSelection( );

			treeViewer.setSelection( null );
			treeViewer.setInput( ResourceLocator.getRootEntries( ) );
			handleTreeViewerRefresh( );
			if ( selection != null )
			{
				setSelection( selection );
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.IEclipsePreferences$IPreferenceChangeListener#preferenceChange(org.eclipse.core.runtime.preferences.IEclipsePreferences.PreferenceChangeEvent)
	 */
	public void preferenceChange( PreferenceChangeEvent event )
	{
		if ( event.getKey( ).equals( PreferenceChangeEvent.SPECIALTODEFAULT )
				|| ReportPlugin.RESOURCE_PREFERENCE.equals( event.getKey( ) ) )
			Display.getDefault( ).asyncExec( new Runnable( ) {

				public void run( )
				{
					refreshRoot( );
				}
			} );
	}

	public void resourceChanged( ModuleHandle module, ResourceChangeEvent event )
	{
		if ( isDisposed( ) )
		{
			return;
		}

		String path = event.getChangedResourcePath( );

		if ( path == null )
		{
			refreshRoot( );
		}
		else
		{
			selectPath( new String[]{
				path
			} );
		}
	}

	/**
	 * Sets selections for the specified tree viewer and optionally makes it
	 * visible.
	 * 
	 * @param treeViewer
	 *            the specified tree viewer to select.
	 * @param paths
	 *            the specified paths to select.
	 */
	public void selectPath( final String[] paths )
	{
		if ( paths == null || paths.length <= 0 )
		{
			return;
		}

		Display display = getSite( ).getShell( ).getDisplay( );

		display.asyncExec( new Runnable( ) {

			public void run( )
			{
				TreeViewer treeViewer = getTreeViewer( );
				boolean needSelect = false;

				refreshRoot( );
				for ( String path : paths )
				{
					File file = new File( path );

					if ( !file.exists( ) )
					{
						continue;
					}

					needSelect = true;
					String parent = file.getParent( );
					List<String> folders = new ArrayList<String>( );

					while ( parent != null )
					{
						folders.add( parent );
						parent = new File( parent ).getParent( );
					}

					for ( int i = folders.size( ) - 1; i >= 0; i-- )
					{
						treeViewer.expandToLevel( folders.get( i ), 1 );
					}
				}
				if ( needSelect )
				{
					treeViewer.setSelection( new StructuredSelection( paths ) );
					treeViewer.reveal( paths[0] );
				}
			}
		} );
	}
}