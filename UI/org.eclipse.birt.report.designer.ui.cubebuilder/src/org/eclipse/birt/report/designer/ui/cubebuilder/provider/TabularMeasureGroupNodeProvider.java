/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeMeasureGroupAction;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;

/**
 * Provider for the data sets node
 * 
 */
public class TabularMeasureGroupNodeProvider extends DefaultNodeProvider
{

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the given menu.
	 * 
	 * @param menu
	 *            the menu
	 * @param object
	 *            the object
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		// WizardUtil.createNewCubeMenu( menu );
		super.createContextMenu( sourceViewer, object, menu );

		if ( ( (MeasureGroupHandle) object ).canEdit( ) )
		{
			menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
					new EditCubeMeasureGroupAction( object,
							Messages.getString( "CubeMeasureGroupNodeProvider.menu.text" ) ) );
		}

		menu.insertBefore( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction( object ) );

		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new Separator( ) ); //$NON-NLS-1$
		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new RefreshAction( sourceViewer ) ); //$NON-NLS-1$

	}

	public Object[] getChildren( Object model )
	{
		return ( (MeasureGroupHandle) model ).getContents( MeasureGroupHandle.MEASURES_PROP )
				.toArray( );
	}

	/**
	 * Gets the display name of the node.
	 * 
	 * @param model
	 *            the object
	 */
	public String getNodeDisplayName( Object object )
	{
		MeasureGroupHandle measures = (MeasureGroupHandle) object;
		if ( measures.getName( ) != null
				&& !measures.getName( ).trim( ).equals( "" ) )
			return measures.getName( ) + "(Measures)";
		else
			return "MEASURES(Measures)";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getIconName(java.lang.Object)
	 */
	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_CUBE_MEASUREGROUP;
	}
}