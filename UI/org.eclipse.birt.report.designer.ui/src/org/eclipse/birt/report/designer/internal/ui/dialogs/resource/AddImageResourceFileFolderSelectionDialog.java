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

package org.eclipse.birt.report.designer.internal.ui.dialogs.resource;

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

/**
 * 
 */

public class AddImageResourceFileFolderSelectionDialog extends
		ResourceFileFolderSelectionDialog
{

	private static final String[] IMAGE_FILTER = new String[]{
		"*.gif;*.jpg;*.jpeg;*.png;*.ico;*.bmp" //$NON-NLS-1$
	};

	private static final String[] IMAGE_TYPES = new String[]{
		".bmp", ".jpg", ".jpeg", ".gif", ".png", ".ico" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
	};

	/**
	 * @param parent
	 * @param labelProvider
	 * @param contentProvider
	 */
	public AddImageResourceFileFolderSelectionDialog( )
	{
		super( true, IMAGE_FILTER );
		setValidator( new ResourceSelectionValidator( false, false, IMAGE_TYPES ) );
		setAllowMultiple( false );
		setTitle( Messages.getString( "ReportPage.title.setPrewImg" ) ); //$NON-NLS-1$
		setMessage( Messages.getString( "AddImageResourceFileFolderSelectionDialog.Message" ) ); //$NON-NLS-1$
	}

	/*
	 * @see Dialog#createDialogArea(Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Control control = super.createDialogArea( parent );
		UIUtil.bindHelp( parent, IHelpContextIds.ADD_IMAGE_FILES_DIALOG_ID );
		return control;
	}

}
