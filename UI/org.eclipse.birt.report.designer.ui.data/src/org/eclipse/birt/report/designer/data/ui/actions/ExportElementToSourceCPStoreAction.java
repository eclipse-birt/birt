/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.actions;

import org.eclipse.birt.report.designer.data.ui.datasource.ExportDataSourceDialog;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractViewAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.OdaDataSourceHandle;
import org.eclipse.datatools.connectivity.oda.design.DesignSessionRequest;
import org.eclipse.datatools.connectivity.oda.design.ui.designsession.DataSourceDesignSession;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class ExportElementToSourceCPStoreAction extends AbstractViewAction
{
	private static final String DISPLAY_TEXT = Messages.getString( "ExportToCPSouceAction.action.text" ); //$NON-NLS-1$

	public ExportElementToSourceCPStoreAction( Object selectedObject )
	{
		super( selectedObject, DISPLAY_TEXT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see isEnabled()
	 */
	public boolean isEnabled( )
	{
		// will implement it later.
		Object selection = getSelection( );
		if ( selection instanceof StructuredSelection )
		{
			if ( ( (StructuredSelection) selection ).size( ) > 1 )
			{
				return false;
			}
			selection = ( (StructuredSelection) selection ).getFirstElement( );
		}
		if ( selection instanceof OdaDataSourceHandle )
		{
			return !isSampleDB( (OdaDataSourceHandle)selection );
		}
		return false;
	}
	
	private boolean isSampleDB( OdaDataSourceHandle handle )
	{
		if ( "org.eclipse.birt.report.data.oda.jdbc".equals( handle.getExtensionID( ) ) )
		{
			Object driverClass = handle.getProperty( "odaDriverClass" );
			if ( driverClass != null
					&& "org.eclipse.birt.report.data.oda.sampledb.Driver".equals( driverClass.toString( ) ) )
				return true;
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		Object selection = getSelection( );
		if ( selection instanceof StructuredSelection )
		{
			selection = ( (StructuredSelection) selection ).getFirstElement( );
		}
		if ( selection instanceof OdaDataSourceHandle )
		{
			ExportDataSourceDialog dialog = new ExportDataSourceDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					Messages.getString( "datasource.exprotToCP.title" ),
					(DataSourceHandle) selection );
			if ( dialog.open( ) == Dialog.OK )
			{
				try
				{
					DesignSessionRequest designSessionRequest = DTPUtil.getInstance( )
							.createDesignSessionRequest( (OdaDataSourceHandle) selection );
					DataSourceDesignSession.convertDesignToLinkedProfile( designSessionRequest,
					        null, false,    // TODO UI enhancement to prompt user for their values
							( (Boolean) dialog.getResult( ) ).booleanValue( ),
							PlatformUI.getWorkbench( )
									.getDisplay( )
									.getActiveShell( ) );
				}
				catch ( Exception ex )
				{
					ExceptionHandler.handle( ex );
				}
			}
		}
	}
}
