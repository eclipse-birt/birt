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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.report.designer.internal.ui.actions.ProjectFilterAction;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseElementTreeSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FileContentProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FileLabelProvider;
import org.eclipse.birt.report.designer.internal.ui.dialogs.FilePathEntry;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.widget.TreeViewerBackup;
import org.eclipse.birt.report.model.api.util.URIUtil;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.Viewer;
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
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * ProjectFileDialog
 */
public class ProjectFileDialog extends BaseElementTreeSelectionDialog
{
	public static final String HELPER_PROPERTY_SELECTED_TYPE = "HELPER_PROPERTY_SELECTED_TYPE";//$NON-NLS-1$
	public static final String HELPER_PROPERTY_IS_RELATIVE_TO_PROJECT = "HELPER_PROPERTY_IS_RELATIVE_TO_PROJECT";//$NON-NLS-1$
	public static final String HELPER_PROPERTY_PREFIX = "HELPER_PROPERTY_PREFIX";//$NON-NLS-1$

	IDialogHelper helper = null;
	String selectedType;
	boolean isRelativeToProjectRoot;
	protected String newFileName = ""; //$NON-NLS-1$

	private Status OKStatus = new Status( IStatus.OK,
			ReportPlugin.REPORT_UI,
			IStatus.OK,
			"", null ); //$NON-NLS-1$
	private Status ErrorStatus = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			Messages.getString( "ProjectFileDialog.ErrorMessage" ), //$NON-NLS-1$
			null );
	private Status ErrorStatusNoSelection = new Status( IStatus.ERROR,
			ReportPlugin.REPORT_UI,
			IStatus.ERROR,
			"", //$NON-NLS-1$
			null );

	/**
	 * Validator
	 */
	private class Validator implements ISelectionStatusValidator
	{

		public IStatus validate( Object[] selection )
		{
			int nSelected = selection.length;
			if ( nSelected == 0 )
			{
				return ErrorStatusNoSelection;
			}
			else if ( nSelected > 1 )
			{
				return ErrorStatus;
			}
			else if ( selection[0] instanceof ResourceEntry
					&& ( (ResourceEntry) selection[0] ).isFile( ) )
			{
				return OKStatus;
			}
			else
				return ErrorStatus;
		}
	}

	public String getPath( )
	{
		Object[] selected = getResult( );
		if ( selected.length > 0 )
		{
			return getPath( 0 );
		}
		return null;
	}

	public String getPath( int index )
	{
		Object[] selected = getResult( );
		if ( index < 0 || index >= selected.length || input == null )
		{
			return null;
		}
		FilePathEntry entry = (FilePathEntry) selected[index];

		String path = entry.getURL( ).getFile( );
		if ( entry.getURL( ).getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
		{
			path = URIUtil.resolveAbsolutePath( input,
					URIUtil.getRelativePath( input, path ) );
		}

		if ( helper != null )
		{
			Object prefix = helper.getProperty( HELPER_PROPERTY_PREFIX );
			if ( prefix != null )
			{
				path = prefix + path;
			}
		}
		return path;

	}

	private String input;

	private String[] filePattern;

	private MenuManager menuManager;

	private ToolItem toolItem;

	private ToolBar toolBar;

	public ProjectFileDialog( String input, String selectedType, boolean isRelativeToProjectRoot )
	{
		this( input, null, selectedType, isRelativeToProjectRoot );
	}

	public void refreshRoot( )
	{
		if ( filePattern == null )
			getTreeViewer( ).setInput( new ResourceEntry[]{
				new FilePathEntry( input )
			} );
		else
			getTreeViewer( ).setInput( new ResourceEntry[]{
				new FilePathEntry( input, filePattern )
			} );
		handleTreeViewerRefresh( );
	}

	public ProjectFileDialog( String input, String[] filePattern, String selectedType, boolean isRelativeToProjectRoot )
	{
		super( UIUtil.getDefaultShell( ),
				new FileLabelProvider( input ),
				new FileContentProvider( true ) );
		this.input = input;
		this.selectedType = selectedType;
		this.isRelativeToProjectRoot = isRelativeToProjectRoot;

		if ( filePattern != null && filePattern.length > 0 )
		{
			this.filePattern = filePattern;
		}
		setInput( new ResourceEntry[]{
			new FilePathEntry( input, filePattern )
		} );
		setDoubleClickSelects( true );
		setValidator( new Validator( ) );
		setAllowMultiple( false );
		setTitle( Messages.getString( "ProjectFileDialog.Title" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "ProjectFileDialog.Message" ) ); //$NON-NLS-1$
		setSorter( new FileViewerSorter( ) );
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
		toolItem.setToolTipText( Messages.getString( "ProjectFileDialog.Text.Menu" ) ); //$NON-NLS-1$
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
		ProjectFilterAction action = new ProjectFilterAction( this );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.ui.dialogs.ElementTreeSelectionDialog#createDialogArea(org
	 * .eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite rt = (Composite) super.createDialogArea( parent );
		addToolTip( );
		rt.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		getTreeViewer( ).expandToLevel( 2 );
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

		initDialogHelper(rt);
		
		UIUtil.bindHelp( parent, IHelpContextIds.PROJECT_FILES_DIALOG_ID );
		return rt;
	}

	private void initDialogHelper(Composite rt)
	{
		// *********** try using a helper provider ****************
		IDialogHelperProvider helperProvider = (IDialogHelperProvider) ElementAdapterManager.getAdapter( this,
				IDialogHelperProvider.class );

		if ( helperProvider != null )
		{
			this.helper = helperProvider.createHelper( this, "");//$NON-NLS-1$
			this.helper.setProperty( HELPER_PROPERTY_SELECTED_TYPE, selectedType );
			this.helper.setProperty( HELPER_PROPERTY_IS_RELATIVE_TO_PROJECT, isRelativeToProjectRoot );
			this.helper.createContent(rt);
		}
		else
		{
			this.helper = null;
		}
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
	 * @see org.eclipse.ui.dialogs.SelectionStatusDialog#okPressed()
	 */
	protected void okPressed( )
	{
		super.okPressed( );
		Object[] selected = getResult( );
		if ( selected.length > 0 && !newFileName.equals( "" ) ) //$NON-NLS-1$
		{
			ResourceEntry entry = (ResourceEntry) selected[0];
			File file = new File( entry.getURL( ).getPath( ) );
			try
			{
				new File( file, newFileName ).createNewFile( );
			}
			catch ( IOException e )
			{
				ExceptionHandler.handle( e );
			}
		}
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
						if ( getTreeViewer( ).getLabelProvider( ) instanceof FileLabelProvider )
						{
							tree.setToolTipText( ( (FileLabelProvider) getTreeViewer( ).getLabelProvider( ) ).getToolTip( item.getData( ) ) );
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

	/**
	 * FileViewerSorter
	 */
	protected static class FileViewerSorter extends ViewerSorter
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * org.eclipse.jface.viewers.ViewerSorter#category(java.lang.Object)
		 */
		public int category( Object element )
		{
			if ( element instanceof File )
			{
				if ( ( (File) element ).isDirectory( ) )
				{
					return 0;
				}
				else
				{
					return 1;
				}
			}
			else if ( element instanceof ResourceEntry )
			{
				if ( ( (ResourceEntry) element ).isFile( ) ) // file, return 1;
				{
					return 1;
				}
				else
				// directory, return 0;
				{
					return 0;
				}
			}

			return 1;
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
						if ( b instanceof FragmentResourceEntry )
						{
							return FileViewerSorter.this.compare( viewer, a, b );
						}
						else
						{
							return -1;
						}
					}
					else if ( a instanceof PathResourceEntry )
					{
						if ( b instanceof FragmentResourceEntry )
						{
							return 1;
						}
						else if ( b instanceof PathResourceEntry )
						{
							return FileViewerSorter.this.compare( viewer, a, b );
						}
						else
						{
							return -1;
						}
					}
					else
					{
						return FileViewerSorter.this.compare( viewer, a, b );
					}

				}
			} );
		}
	}
}
