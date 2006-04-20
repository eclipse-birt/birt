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

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;

/**
 * A dialog to select resource folder files or folder.
 */

public class ResourceFileFolderSelectionDialog extends ElementTreeSelectionDialog
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

	public ResourceFileFolderSelectionDialog( Shell parent,
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
	 * @return
	 */
	public String getPath( )
	{
		Object[] selected = getResult( );
		if ( selected.length > 0 && rootFile != null )
		{
			File file = (File) selected[0];
			return rootFile.toURI( ).relativize( file.toURI( ) ).toString( );
		}
		return null;
	}

}