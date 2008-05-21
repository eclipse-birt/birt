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
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.FragmentResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.PathResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */

public class AddImageResourceFileFolderSelectionDialog extends
		ResourceFileFolderSelectionDialog
{

	private static final String[] IMAGE_FILTER = new String[]{
		"*.gif;*.jpg;*.jpeg;*.png;*.ico;*.bmp" //$NON-NLS-1$
	};

	private static final String[] IMAGE_TYPES = new String[]{
			".bmp", ".jpg", ".jpeg", ".gif", ".png", ".ico" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	};

	private ResourceEntry[] getAllRootEntries( String[] fileNamePattern )
	{
		ResourceEntry systemResource = new FragmentResourceEntry( fileNamePattern );
		ResourceEntry templateResource = new FragmentResourceEntry( fileNamePattern,
				Messages.getString( "FragmentTemplateResourceEntry.RootName" ),
				Messages.getString( "FragmentTemplateResourceEntry.RootDisplayName" ),
				FragmentResourceEntry.TEMPLATE_ROOT );
		ResourceEntry sharedResource = new PathResourceEntry( fileNamePattern );

		return new ResourceEntry[]{
				systemResource, templateResource, sharedResource
		};

	}

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public AddImageResourceFileFolderSelectionDialog( )
	{

		super( UIUtil.getDefaultShell( ),
				new ResourceFileLabelProvider( ),
				new ResourceFileContentProvider( true ) );

		Object input = getAllRootEntries( IMAGE_FILTER );
		setInput( input );

		setValidator( new ResourceSelectionValidator( false, false, IMAGE_TYPES ) );
		setAllowMultiple( false );
		setTitle( Messages.getString( "ReportPage.title.setPrewImg" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "AddImageResourceFileFolderSelectionDialog.Message" ) ); //$NON-NLS-1$

		setSorter( new FileViewerSorter( ) );
	}

	/*
	 * @see Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Control control = super.createDialogArea( parent );
		UIUtil.bindHelp( parent, IHelpContextIds.ADD_IMAGE_FILES_DIALOG_ID );
		return control;
	}

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
					if ( ( a instanceof FragmentResourceEntry )
							&& ( b instanceof FragmentResourceEntry ) )
					{
						return FileViewerSorter.this.compare( viewer, a, b );
					}
					else if ( a instanceof FragmentResourceEntry )
					{
						return -1;
					}
					else if ( b instanceof FragmentResourceEntry )
					{
						return 1;
					}
					else if ( a instanceof PathResourceEntry && b instanceof PathResourceEntry)
					{
						return FileViewerSorter.this.compare( viewer, a, b );
					}else if( a instanceof PathResourceEntry)
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

}
