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
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeMeasureGroupAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstancts;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
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

	public Object getParent( Object model )
	{
		MeasureGroupHandle measures = (MeasureGroupHandle) model;
		CubeHandle cube = (CubeHandle) measures.getContainer( );
		if ( cube != null )
			return cube.getPropertyHandle( ICubeModel.MEASURE_GROUPS_PROP );
		return null;
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
		return measures.getName( );
	}

	public Image getNodeIcon( Object model )
	{
		if ( model instanceof DesignElementHandle
				&& ( (DesignElementHandle) model ).getSemanticErrors( ).size( ) > 0 )
		{
			return ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
		}
		return UIHelper.getImage( BuilderConstancts.IMAGE_MEASUREGROUP );
	}
}