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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceLocator;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.action.ResourceFileFolderSelectionAction;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * A dialog to select resource folder files or folder.
 */

public class ResourceFileFolderSelectionDialog extends
		ElementTreeSelectionDialog
{

	private File rootFile;

	protected static class FileViewerSorter extends ViewerSorter
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
		 */
		public int category( Object element )
		{
			if ( element instanceof File && !( (File) element ).isDirectory( ) )
			{
				return 1;
			}
			else if ( element instanceof ResourceEntry
					&& ( (ResourceEntry) element ).isFile( ) )
			{
				return 1;
			}
			return 0;
		}

		/**
		 * Sorts the given elements in-place, modifying the given array.
		 * <p>
		 * The default implementation of this method uses the
		 * java.util.Arrays#sort algorithm on the given array, calling
		 * <code>compare</code> to compare elements.
		 * </p>
		 * <p>
		 * Subclasses may reimplement this method to provide a more optimized
		 * implementation.
		 * </p>
		 * 
		 * @param viewer
		 *            the viewer
		 * @param elements
		 *            the elements to sort
		 */
		public void sort( final Viewer viewer, Object[] elements )
		{
			Arrays.sort( elements, new Comparator<Object>( ) {

				public int compare( Object a, Object b )
				{
					if ( a instanceof FragmentResourceEntry )
					{
						return -1;
					}
					else if ( b instanceof FragmentResourceEntry )
					{
						return 1;
					}
					else if ( a instanceof PathResourceEntry )
					{
						return -1;
					}
					else if ( b instanceof PathResourceEntry )
					{
						return 1;
					}
					else
						return FileViewerSorter.this.compare( viewer, a, b );
				}
			} );
		}
	}

	public ResourceFileFolderSelectionDialog( )
	{
		this( true, false, null );
	}

	public ResourceFileFolderSelectionDialog( boolean showFiles )
	{
		this( showFiles, false, null );
	}

	public ResourceFileFolderSelectionDialog( String[] fileNamePattern )
	{
		this( true, false, fileNamePattern );
	}

	public ResourceFileFolderSelectionDialog( boolean includeFragments,
			String[] fileNamePattern )
	{
		this( true, includeFragments, fileNamePattern );
	}

	public ResourceFileFolderSelectionDialog( boolean showFiles,
			boolean includeFragments, String[] fileNamePattern )
	{
		this( showFiles,
				includeFragments,
				fileNamePattern,
				new ResourceFileContentProvider( showFiles ) );
	}

	/**
	 * Constructs a resource folder selection dialog with the specified content
	 * provider.
	 * 
	 * @param showFiles
	 *            the flag if show files
	 * @param includeFragments
	 *            the flag if include fragments
	 * @param fileNamePattern
	 *            the file name pattern to show
	 * @param contentProvider
	 *            the specified content provider
	 */
	public ResourceFileFolderSelectionDialog( boolean showFiles,
			boolean includeFragments, String[] fileNamePattern,
			ITreeContentProvider contentProvider )
	{
		this( UIUtil.getDefaultShell( ),
				new ResourceFileLabelProvider( ),
				contentProvider );

		if ( includeFragments )
		{
			this.input = ResourceLocator.getRootEntries( fileNamePattern );
		}
		else
		{
			this.input = ResourceLocator.getResourceFolder( fileNamePattern );
		}
		setInput( input );
	}

	protected ResourceFileFolderSelectionDialog( Shell parent,
			ILabelProvider labelProvider, ITreeContentProvider contentProvider )
	{
		super( parent, labelProvider, contentProvider );
		setSorter( new FileViewerSorter( ) );
	}

	public void refreshRoot( )
	{
		getTreeViewer( ).remove( input );
		getTreeViewer( ).setInput( input );
		handleTreeViewerRefresh( );
	}

	private TreeViewerBackup treeViewerBackup;

	private void handleTreeViewerRefresh( )
	{
		if ( treeViewerBackup != null )
		{
			treeViewerBackup.restoreBackup( getTreeViewer( ) );
		}
		else
		{
			treeViewerBackup = new TreeViewerBackup( );
			getTreeViewer( ).expandToLevel( 2 );
			treeViewerBackup.updateStatus( getTreeViewer( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#setInput(java.lang.Object)
	 */

	private Object input;

	public void setInput( Object input )
	{
		rootFile = new File( input.toString( ) );
		this.input = input;
		super.setInput( input );
	}

	/**
	 * Get the relative path to BIRT resource folder.
	 * 
	 * @return
	 */
	public String getPath( )
	{
		Object[] selected = getResult( );
		if ( selected.length > 0 && rootFile != null )
		{
			ResourceEntry entry = (ResourceEntry) selected[0];
			if ( entry == null || entry.getURL( ) == null )
			{
				return null;
			}
			return ResourceLocator.relativize( entry.getURL( ) );
		}
		return null;
	}

	/**
	 * Get the relative path to BIRT resource folder.
	 * 
	 * @return
	 */
	public String getPath( int index )
	{
		Object[] selected = getResult( );
		if ( index < 0 || index >= selected.length || rootFile == null )
		{
			return null;
		}
		ResourceEntry entry = (ResourceEntry) selected[index];
		return ResourceLocator.relativize( entry.getURL( ) );

	}

	/*
	 * @see Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.RESOURCE_SELECT_DIALOG_ID );
		Control control = super.createDialogArea( parent );
		getTreeViewer( ).getTree( ).setFocus( );

		TreeListener treeListener = new TreeListener( ) {

			public void treeCollapsed( TreeEvent e )
			{
				Item item = (Item) e.item;
				if ( treeViewerBackup != null )
					treeViewerBackup.updateCollapsedStatus( getTreeViewer( ),
							item.getData( ) );

			}

			public void treeExpanded( TreeEvent e )
			{
				Item item = (Item) e.item;
				if ( treeViewerBackup != null )
					treeViewerBackup.updateExpandedStatus( getTreeViewer( ),
							item.getData( ) );
			}

		};
		getTreeViewer( ).getTree( ).addTreeListener( treeListener );
		addToolTip( );
		return control;

	}

	protected Label createMessageArea( Composite composite )
	{
		Composite infoContent = new Composite( composite, SWT.NONE );

		GridData data = new GridData( );
		data.grabExcessVerticalSpace = false;
		data.grabExcessHorizontalSpace = true;
		data.horizontalAlignment = GridData.FILL;
		data.verticalAlignment = GridData.BEGINNING;
		infoContent.setLayoutData( data );

		GridLayout layout = new GridLayout( );
		layout.marginTop = layout.marginBottom = layout.marginLeft = layout.marginRight = layout.marginHeight = layout.marginWidth = 0;
		layout.numColumns = 2;
		infoContent.setLayout( layout );

		Label label = new Label( infoContent, SWT.NONE );
		if ( getMessage( ) != null )
		{
			label.setText( getMessage( ) );
		}
		label.setFont( composite.getFont( ) );
		label.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createViewMenu( infoContent );

		return label;
	}

	private MenuManager menuManager;

	private ToolItem toolItem;

	private ToolBar toolBar;

	private void createViewMenu( Composite parent )
	{
		toolBar = new ToolBar( parent, SWT.FLAT );
		toolItem = new ToolItem( toolBar, SWT.PUSH, 0 );
		GridData data = new GridData( );
		data.horizontalAlignment = GridData.END;
		toolBar.setLayoutData( data );

		toolBar.addMouseListener( new MouseAdapter( ) {

			public void mouseDown( MouseEvent e )
			{
				showViewMenu( );
			}
		} );

		toolItem.setImage( ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_VIEW_MENU ) );
		toolItem.setToolTipText( Messages.getString( "ResourceFileFolderSelectionDialog.Text.Menu" ) ); //$NON-NLS-1$
		toolItem.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				showViewMenu( );
			}
		} );

		menuManager = new MenuManager( );
		fillViewMenu( menuManager );
	}

	/**
	 * Fills the menu of the dialog.
	 * 
	 * @param menuManager
	 *            the menu manager
	 */
	protected void fillViewMenu( IMenuManager menuManager )
	{
		ResourceFileFolderSelectionAction action = new ResourceFileFolderSelectionAction( this );
		menuManager.add( action );
	}

	private void showViewMenu( )
	{
		Menu menu = menuManager.createContextMenu( getShell( ) );
		Rectangle bounds = toolItem.getBounds( );
		Point topLeft = new Point( bounds.x, bounds.y + bounds.height );
		topLeft = toolBar.toDisplay( topLeft );
		menu.setLocation( topLeft.x, topLeft.y );
		menu.setVisible( true );
	}

	/**
	 * Add Tooltip for root TreeItem.
	 */
	protected void addToolTip( )
	{
		final Tree tree = getTreeViewer( ).getTree( );
		tree.addMouseTrackListener( new MouseTrackAdapter( ) {

			public void mouseHover( MouseEvent event )
			{
				Widget widget = event.widget;
				if ( widget == tree )
				{
					Point pt = new Point( event.x, event.y );
					TreeItem item = tree.getItem( pt );

					if ( item == null )
					{
						tree.setToolTipText( null );
					}
					else
					{
						if ( getTreeViewer( ).getLabelProvider( ) instanceof ResourceFileLabelProvider )
						{
							tree.setToolTipText( ( (ResourceFileLabelProvider) getTreeViewer( ).getLabelProvider( ) ).getToolTip( item.getData( ) ) );
						}
						else
						{
							tree.setToolTipText( null );
						}
					}
				}
			}
		} );
		refreshRoot( );
	}

}