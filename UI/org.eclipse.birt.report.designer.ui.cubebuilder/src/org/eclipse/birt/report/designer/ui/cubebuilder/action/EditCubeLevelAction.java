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

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.dialog.LevelDialog;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.jface.dialogs.IDialogConstants;

/**
 * TODO: Please document
 * 
 * @version $Revision: 1.1 $ $Date: 2007/03/06 05:13:52 $
 */
public class EditCubeLevelAction extends AbstractElementAction
{

	public static final String ID = "org.eclipse.birt.report.designer.ui.actions.EditCubeLevelAction"; //$NON-NLS-1$

	/**
	 * @param selectedObject
	 */
	public EditCubeLevelAction( Object selectedObject )
	{
		super( selectedObject );
		setId( ID );
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public EditCubeLevelAction( Object selectedObject, String text )
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
			System.out.println( "Edit Level action >> Runs ..." ); //$NON-NLS-1$
		}
		LevelHandle LevelHandle = (LevelHandle) getSelection( );
		LevelDialog dialog = new LevelDialog( false );
		dialog.setInput( LevelHandle );
		return ( dialog.open( ) == IDialogConstants.OK_ID );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		return ( (LevelHandle) getSelection( ) ).canEdit( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#getTransactionLabel()
	 */
	protected String getTransactionLabel( )
	{
		return Messages.getFormattedString( "cube.level.edit", new String[]{( (LevelHandle) getSelection( ) ).getName( )} ); //$NON-NLS-1$
	}
}
