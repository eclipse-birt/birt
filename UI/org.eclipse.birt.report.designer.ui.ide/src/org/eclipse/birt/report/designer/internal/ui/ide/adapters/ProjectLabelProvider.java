/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ide.IDE;

/**
 * Provider the project label and image.
 */

public class ProjectLabelProvider implements ILabelProvider
{

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage( Object element )
	{
		if ( element instanceof IJavaElement )
		{
			return getBaseImage( (IJavaElement) element );
		}

		return null;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
	 */
	public String getText( Object element )
	{
		if ( element instanceof IJavaElement )
		{
			return ( (IJavaElement) element ).getElementName( );
		}
		return ""; //$NON-NLS-1$
	}

	public void addListener( ILabelProviderListener listener )
	{
		// do nothing now
	}

	public void dispose( )
	{
		// do nothing now

	}

	public boolean isLabelProperty( Object element, String property )
	{
		return true;
	}

	public void removeListener( ILabelProviderListener listener )
	{
		// do nothing now
	}

	public Image getBaseImage( IJavaElement element )
	{

		switch ( element.getElementType( ) )
		{

			case IJavaElement.JAVA_PROJECT :
				IJavaProject jp = (IJavaProject) element;
				if ( jp.getProject( ).isOpen( ) )
				{
					return ReportPlugin.getDefault( )
							.getWorkbench( )
							.getSharedImages( )
							.getImage( IDE.SharedImages.IMG_OBJ_PROJECT );
				}
				return ReportPlugin.getDefault( )
						.getWorkbench( )
						.getSharedImages( )
						.getImage( IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED );

			default :
				return null;
		}

	}

}
