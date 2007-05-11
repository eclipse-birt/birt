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

package org.eclipse.birt.report.designer.internal.ui.views.outline.providers;

import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;

/**
 * 
 */

public class CssSharedStyleNodeProvider extends StyleNodeProvider
{
	/**
	 * Creates the context menu for the given object.
	 * 
	 * @param object
	 *            the object
	 * @param menu
	 *            the menu
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		return;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit( ReportElementHandle handle )
	{
		return false;
	}

	public Image getNodeIcon( Object model )
	{
		CssSharedStyleHandle handle = (CssSharedStyleHandle) model;
		if ( handle.getCssStyleSheetHandle( ) != null
				&& handle.getCssStyleSheetHandle( ).getContainerHandle( ) != null )
		{
			return ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_ELEMENT_STYLE_LINK );
		}
		return super.getNodeIcon( model );
	}
	
}
