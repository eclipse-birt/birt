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

package org.eclipse.birt.report.designer.ui.lib.explorer;

import org.eclipse.birt.report.designer.internal.ui.lib.explorer.model.LibDirectoryNodeModel;
import org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * LibraryExplorerProvider LibraryExplorer tree viewer label and content
 * provider adapter. this provider will list all library files in BIRT resource
 * folder.
 */
public class LibraryExplorerProvider extends ViewsTreeProvider
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getChildren(java.lang.Object)
	 */
	public Object[] getChildren( Object parentElement )
	{
		if ( parentElement instanceof LibDirectoryNodeModel )
		{
			return ( (LibDirectoryNodeModel) parentElement ).getChildren( );
		}
		return super.getChildren( parentElement );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof LibDirectoryNodeModel )
		{

				return PlatformUI.getWorkbench( )
						.getSharedImages( )
						.getImage( ISharedImages.IMG_OBJ_FOLDER );
		}

		return super.getImage( element );
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof LibDirectoryNodeModel )
		{
			return ( (LibDirectoryNodeModel) element ).getText( );
		}
		else if ( element instanceof String )
		{
			return element.toString( );
		}
		return super.getText( element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.ViewsTreeProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object element )
	{
		if ( element instanceof LibDirectoryNodeModel )
		{
				return true;
		}
		return super.hasChildren( element );
	}
	
	public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
	{
		if(oldInput instanceof LibDirectoryNodeModel)
		{
			((LibDirectoryNodeModel)oldInput).dispose( );
		}
		super.inputChanged( viewer, oldInput, newInput );
	}

}
