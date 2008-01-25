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

package org.eclipse.birt.report.designer.internal.ui.views.data.providers;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.data.ui.dataset.DataSetEditor;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataSourceSelectionDialog;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.internal.ui.views.actions.RefreshAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.odadatasource.wizards.WizardUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.JointDataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
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
public class DataSetNodeProvider extends DefaultNodeProvider
{

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

		if ( ( (DataSetHandle) object ).canEdit( ) )
		{
			WizardUtil.createEditDataSetMenu( menu, object );
		}

		menu.insertBefore( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction( object ) );

		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new Separator( ) ); //$NON-NLS-1$
		menu.insertAfter( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", new RefreshAction( sourceViewer ) ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getNodeDisplayName(java.lang.Object)
	 */
	public String getNodeDisplayName( Object model )
	{
		return DEUtil.getDisplayLabel( model, false );
	}

	public Image getNodeIcon( Object model )
	{
		DataSetHandle handle = (DataSetHandle) model;
		if ( !( handle instanceof JointDataSetHandle )
				&& handle.getDataSource( ) == null )
		{
			return ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );
		}
		return super.getNodeIcon( model );
	}

	/**
	 * Gets the children element of the given model using visitor.
	 * 
	 * @param object
	 *            the handle
	 */
	public Object[] getChildren( Object object )
	{
		DataSetHandle handle = (DataSetHandle) object;

		ArrayList params = new ArrayList( 10 );

		CachedMetaDataHandle cmdh = null;
		try
		{
			cmdh = DataSetUIUtil.getCachedMetaDataHandle( handle );
		}
		catch ( SemanticException e )
		{
		}

		ArrayList columns = new ArrayList( 10 );

		if ( cmdh != null )
		{
			for ( Iterator iterator = cmdh.getResultSet( ).iterator( ); iterator.hasNext( ); )
			{
				ResultSetColumnHandle element = (ResultSetColumnHandle) iterator.next( );
				columns.add( element );
			}
		}

		PropertyHandle parameters = handle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );
		Iterator iter = parameters.iterator( );

		if ( iter != null )
		{
			while ( iter.hasNext( ) )
			{
				Object dataSetParameter = iter.next( );
				if ( ( (DataSetParameterHandle) dataSetParameter ).isOutput( ) == true )
				{
					params.add( dataSetParameter );
				}
			}
		}

		Object[] parametersArray = params.toArray( );
		Object[] both = new Object[columns.toArray( ).length
				+ parametersArray.length];
		System.arraycopy( columns.toArray( ),
				0,
				both,
				0,
				columns.toArray( ).length );
		System.arraycopy( parametersArray,
				0,
				both,
				columns.toArray( ).length,
				parametersArray.length );
		return both;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#hasChildren(java.lang.Object)
	 */
	public boolean hasChildren( Object object )
	{
		return true;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.INodeProvider#getNodeDisplayName(java.lang.Object)
	 */
	protected boolean performEdit( ReportElementHandle handle )
	{
		DataSetHandle dsHandle = (DataSetHandle) handle;
		if ( !( dsHandle instanceof JointDataSetHandle ) &&
				dsHandle.getDataSource( ) == null )
		{
			try
			{
				List dataSourceList = DEUtil.getDataSources( );
				String[] names = new String[dataSourceList.size( )];
				for ( int i = 0; i < names.length; i++ )
				{
					names[i] = ( (DataSourceHandle) dataSourceList.get( i ) ).getName( );
				}
				DataSourceSelectionDialog dataSorucedialog = new DataSourceSelectionDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						Messages.getString( "dataSourceSelectionPage.title" ), //$NON-NLS-1$
						names );
				if ( dataSorucedialog.open( ) == Dialog.CANCEL )
					return false;
				dsHandle.setDataSource( dataSorucedialog.getResult( )
						.toString( ) );
			}
			catch ( SemanticException e )
			{
			}
		}
		DataSetEditor dialog = new DataSetEditor( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ), (DataSetHandle) handle, false );

		return dialog.open( ) == Dialog.OK;
	}
}
