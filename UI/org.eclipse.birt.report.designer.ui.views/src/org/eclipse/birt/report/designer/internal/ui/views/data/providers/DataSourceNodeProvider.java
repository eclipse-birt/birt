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

import org.eclipse.birt.report.designer.data.ui.datasource.DataSourceEditor;
import org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider;
import org.eclipse.birt.report.designer.ui.actions.ShowPropertyAction;
import org.eclipse.birt.report.designer.ui.odadatasource.wizards.WizardUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;

/**
 * Deals with the data source node
 */
public class DataSourceNodeProvider extends DefaultNodeProvider
{

	/**
	 * Creates the context menu for the given object.
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
		if(((DataSourceHandle)object).canEdit())
		{
			WizardUtil.createEditDataSourceMenu( menu, object );
		}
		menu.insertBefore( IWorkbenchActionConstants.MB_ADDITIONS + "-refresh", //$NON-NLS-1$
				new ShowPropertyAction( object ) );
	}

	/**
	 * Gets the children list of the given model
	 * 
	 * @param model
	 *            the model
	 */
	public Object[] getChildren( Object model )
	{
		return new Object[]{};
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.DefaultNodeProvider#performEdit(org.eclipse.birt.model.api.ReportElementHandle)
	 */
	protected boolean performEdit( ReportElementHandle handle )
	{
		if ( handle instanceof OdaDataSourceHandle && handle.canEdit( ) )
		{
			DataSourceEditor dialog = new DataSourceEditor( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					(DataSourceHandle) handle );
			return dialog.open( ) == Dialog.OK;
		}
		return false;
	}
    
}