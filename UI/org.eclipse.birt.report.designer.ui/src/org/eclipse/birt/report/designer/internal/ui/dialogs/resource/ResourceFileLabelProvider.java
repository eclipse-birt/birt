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

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 * Tree viewer label provider adapter for resource browser.
 */

public class ResourceFileLabelProvider extends LabelProvider
{

	private File rootFile;

	private static final Image IMG_FOLDER = PlatformUI.getWorkbench( )
			.getSharedImages( )
			.getImage( ISharedImages.IMG_OBJ_FOLDER );

	private static final Image IMG_FILE = PlatformUI.getWorkbench( )
			.getSharedImages( )
			.getImage( ISharedImages.IMG_OBJ_FILE );

	public ResourceFileLabelProvider( )
	{
		this.rootFile = new File( getRootFilePath( ) );
	}

	private String getRootFilePath( )
	{
		return ReportPlugin.getDefault( ).getResourcePreference( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof File )
		{
			if ( ( (File) element ).isDirectory( ) )
			{
				return IMG_FOLDER;
			}
			else
			{
				return IMG_FILE;
			}
		}
		return super.getImage( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof File )
		{
			if ( element.equals( rootFile ) )
			{
				return ( (File) element ).getAbsolutePath( );
			}
			else
			{
				return ( (File) element ).getName( );
			}
		}
		return super.getText( element );
	}

}
