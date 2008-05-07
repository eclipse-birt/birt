/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.dialog;

import org.eclipse.birt.report.designer.internal.ui.dialogs.resource.ResourceFileFolderSelectionDialog;
import org.eclipse.birt.report.designer.nls.Messages;

/**
 * A dialog for moving reource in resource explorer. User can select a folder to
 * move reource.
 */
public class MoveResourceDialog extends ResourceFileFolderSelectionDialog
{

	/**
	 * Constructs a dialog for moving resource.
	 */
	public MoveResourceDialog( )
	{
		super( false, false, null, new ResourceFolderProvider( ) );
		setTitle( Messages.getString( "MoveResourceDialog.Title" ) );
		setMessage( Messages.getString( "MoveResourceDialog.Message" ) );
		setDoubleClickSelects( true );
		setAllowMultiple( false );
		setHelpAvailable( false );
	}
}
