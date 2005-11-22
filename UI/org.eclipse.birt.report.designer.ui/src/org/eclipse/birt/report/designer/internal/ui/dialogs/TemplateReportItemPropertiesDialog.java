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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Template Report Item Properties Dialog.
 */

public class TemplateReportItemPropertiesDialog extends BaseDialog
{

	private static final String DIALOG_TITLE = Messages.getString("TemplateReportItemPropertiesDialog.title"); //$NON-NLS-1$

	private static final String GROUP_TITLE = Messages.getString("TemplateReportItemPropertiesDialog.group"); //$NON-NLS-1$

	private static final String LABEL_OBJECT_TYPE = Messages.getString("TemplateReportItemPropertiesDialog.objectType"); //$NON-NLS-1$

	private static final String LABEL_PROMPT_TEXT = Messages.getString("TemplateReportItemPropertiesDialog.promptText"); //$NON-NLS-1$

//	private static final String ERROR_TITLE = Messages.getString("TemplateReportItemPropertiesDialog.errorTitle"); //$NON-NLS-1$
//
//	private static final String ERROR_MESSAGE = Messages.getString("TemplateReportItemPropertiesDialog.errorMessage"); //$NON-NLS-1$

	private String objectType;
	private String defaultPromptText;
	private Text promptText;

	public TemplateReportItemPropertiesDialog( String objectType,
			String defaultPromptText )
	{
		super( DIALOG_TITLE );
		this.objectType = objectType;
		this.defaultPromptText = defaultPromptText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite container = new Composite( parent, SWT.NONE );
		GridLayout gridLayout = new GridLayout( );
		gridLayout.marginWidth = 10;
		//gridLayout.marginTop = 7;
		container.setLayout( gridLayout );
		GridData gridData = new GridData( );
		gridData.widthHint = 400;
		container.setLayoutData( gridData );

		Group group = new Group( container, SWT.NONE );
		group.setLayout( new GridLayout( ) );
		group.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		group.setText( GROUP_TITLE );

		Composite groupContainer = new Composite( group, SWT.NONE );
		gridLayout = new GridLayout( );
		gridLayout.numColumns = 2;
		gridLayout.marginWidth = 7;
		gridLayout.marginHeight = 7;
		gridLayout.verticalSpacing = 10;
		groupContainer.setLayout( gridLayout );
		groupContainer.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		new Label( groupContainer, SWT.NONE ).setText( LABEL_OBJECT_TYPE );
		new Label( groupContainer, SWT.NONE ).setText( objectType );
		new Label( groupContainer, SWT.NONE ).setText( LABEL_PROMPT_TEXT );

		promptText = new Text( groupContainer, SWT.BORDER | SWT.SINGLE );
		promptText.setText( defaultPromptText );
		promptText.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		return container;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
//		if ( promptText.getText( ) == null
//				|| promptText.getText( ).trim( ).length( ) == 0 )
//		{
//			ExceptionHandler.openErrorMessageBox(ERROR_TITLE,ERROR_MESSAGE);
//			promptText.forceFocus();
//		}
//		else
//		{
			setResult(promptText.getText( ));
			super.okPressed();
		//		}
		
	}

}
