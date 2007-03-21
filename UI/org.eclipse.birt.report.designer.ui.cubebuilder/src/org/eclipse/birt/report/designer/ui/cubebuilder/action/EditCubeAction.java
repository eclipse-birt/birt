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

package org.eclipse.birt.report.designer.ui.cubebuilder.action;

import org.eclipse.birt.report.designer.data.ui.util.CubeModel;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.CubeBuilder;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.SimpleCubeBuilder;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.PlatformUI;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.1 $ $Date: 2007/03/07 08:40:38 $
 */
public class EditCubeAction extends AbstractElementAction
{

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditCubeAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditCubeAction( String text )
	{
		super( text );
		setId( ID );
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public EditCubeAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		setId( ID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{
		if ( Policy.TRACING_ACTIONS )
		{
			System.out.println( "Edit cube action >> Runs ..." ); //$NON-NLS-1$
		}
		CubeHandle cubeHandle = null;
		if ( getSelection( ) instanceof CubeHandle )
			cubeHandle = (CubeHandle) getSelection( );
		else if ( getSelection( ) instanceof CubeModel )
			cubeHandle = ( (CubeModel) getSelection( ) ).getModel( );
		CubeBuilder dialog = new CubeBuilder( PlatformUI.getWorkbench( )
				.getDisplay( )
				.getActiveShell( ), cubeHandle );
		if ( getSelection( ) instanceof CubeHandle ){
			dialog.showPage(CubeBuilder.DATASETSELECTIONPAGE);
		}
		else if ( getSelection( ) instanceof CubeModel ){
			dialog.showPage(CubeBuilder.GROUPPAGE);
		}
		return ( dialog.open( ) == IDialogConstants.OK_ID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		if ( getSelection( ) instanceof CubeHandle )
			return ( (CubeHandle) getSelection( ) ).canEdit( );
		else if ( getSelection( ) instanceof CubeModel )
			return true;
		return super.isEnabled( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#getTransactionLabel()
	 */
	protected String getTransactionLabel( )
	{
		if ( getSelection( ) instanceof CubeHandle )
			return Messages.getFormattedString( "cube.edit", new String[]{( (CubeHandle) getSelection( ) ).getName( )} ); //$NON-NLS-1$
		return super.getTransactionLabel( );
	}
}
