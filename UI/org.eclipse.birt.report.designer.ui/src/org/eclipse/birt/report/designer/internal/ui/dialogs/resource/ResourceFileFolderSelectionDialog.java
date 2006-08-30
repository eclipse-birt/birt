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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import java.io.File;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceLocator;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseTrackAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
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

	private static class FileViewerSorter extends ViewerSorter
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
			return 0;
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
		this( UIUtil.getDefaultShell( ),
				new ResourceFileLabelProvider( ),
				new ResourceFileContentProvider( showFiles ) );
		if ( includeFragments )
			setInput( ResourceLocator.getRootEntries( fileNamePattern ) );
		else
			setInput( ResourceLocator.getResourceFolder( fileNamePattern ) );
	}

	private ResourceFileFolderSelectionDialog( Shell parent,
			ILabelProvider labelProvider, ITreeContentProvider contentProvider )
	{
		super( parent, labelProvider, contentProvider );
		setSorter( new FileViewerSorter( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.ElementTreeSelectionDialog#setInput(java.lang.Object)
	 */
	public void setInput( Object input )
	{
		rootFile = new File( input.toString( ) );
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
		addToolTip( );
		return control;

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
	}

}