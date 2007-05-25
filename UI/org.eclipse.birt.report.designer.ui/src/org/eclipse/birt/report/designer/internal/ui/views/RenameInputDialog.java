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

package org.eclipse.birt.report.designer.internal.ui.views;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;

/**
 * The dialog used to rename action's input
 */
public class RenameInputDialog extends InputDialog
{

	public RenameInputDialog( Shell shell, String dialogTitle,
			String dialogMessage, String initialValue, IInputValidator validator )
	{
		super( shell, dialogTitle, dialogMessage, initialValue, validator );
	}

	public int open( )
	{
		getText( ).addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				if ( getText( ).getText( ).trim( ).length( ) == 0 )
					getButton( IDialogConstants.OK_ID ).setEnabled( false );
				else
					getButton( IDialogConstants.OK_ID ).setEnabled( true );
			}

		} );
		if ( getText( ).getText( ).trim( ).length( ) == 0 )
			getButton( IDialogConstants.OK_ID ).setEnabled( false );
		return super.open( );
	}
}