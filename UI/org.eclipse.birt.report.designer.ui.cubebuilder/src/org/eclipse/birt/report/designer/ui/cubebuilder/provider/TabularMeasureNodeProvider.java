/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.cubebuilder.provider;

import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.action.EditCubeMeasureAction;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.UIHelper;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with dataset node
 * 
 */
public class TabularMeasureNodeProvider extends DefaultNodeProvider
{

	private static final Image IMG_DERIVED_MEASURE = UIHelper.getImage( BuilderConstants.IMAGE_DERIVED_MEASURE );

	private static final Image IMG_MEASURE = UIHelper.getImage( BuilderConstants.IMAGE_MEASUREGROUP );

	/**
	 * Creates the context menu for the given object. Gets the action from the
	 * actionRegistry and adds the action to the menu.
	 * 
	 * @param menu
	 *            the menu
	 * @param object
	 *            the object
	 */
	public void createContextMenu( TreeViewer sourceViewer, Object object,
			IMenuManager menu )
	{
		super.createContextMenu( sourceViewer, object, menu );

		if ( ( (MeasureHandle) object ).canEdit( ) )
		{
			menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS,
					new EditCubeMeasureAction( object,
							Messages.getString( "CubeMeasureNodeProvider.menu.text" ) ) ); //$NON-NLS-1$
		}

		menu.insertBefore( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction( object ) );

		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new Separator( ) ); //$NON-NLS-1$
		IAction action = new RefreshAction( sourceViewer );
		if ( action.isEnabled( ) )
		{
			menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS
					+ "-refresh", action ); //$NON-NLS-1$
		}
	}

	public Object getParent( Object model )
	{
		MeasureHandle measure = (MeasureHandle) model;
		return measure.getContainer( );
	}

	public String getNodeDisplayName( Object model )
	{
		MeasureHandle handle = (MeasureHandle) model;
		if ( handle.getDisplayName( ) != null
				&& handle.getDisplayName( ).trim( ).length( ) > 0 )
		{
			return handle.getDisplayName( );
		}
		return handle.getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider
	 * #hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object object )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#
	 * getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit( ReportElementHandle handle )
	{
		MeasureHandle measureHandle = (MeasureHandle) handle;
		CubeBuilder dialog = new CubeBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ),
				(TabularCubeHandle) measureHandle.getContainer( )
						.getContainer( ) );
		dialog.showPage( CubeBuilder.GROUPPAGE );
		return dialog.open( ) == Dialog.OK;
	}

	public String getIconName( Object model )
	{
		return IReportGraphicConstants.ICON_DATA_COLUMN;
	}

	public Image getNodeIcon( Object model )
	{
		if ( model instanceof DesignElementHandle
				&& ( (DesignElementHandle) model ).getSemanticErrors( ).size( ) > 0 )
		{
			return ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
		}

		if ( model instanceof MeasureHandle )
		{
			return ( (MeasureHandle) model ).isCalculated( ) ? IMG_DERIVED_MEASURE
					: IMG_MEASURE;
		}

		return super.getNodeIcon( model );
	}
}
