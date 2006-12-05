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

import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * Dialog to add and edit selection choice for parameter
 */

public class SelectionChoiceDialog extends BaseDialog
{

	public static interface ISelectionChoiceValidator
	{

		String validate( String displayLabel, String value );
	}

	private Text labelEditor, valueEditor;

	private SelectionChoice selectionChoice;

	private CLabel messageLine;

	private ISelectionChoiceValidator validator;

	public SelectionChoiceDialog( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	public SelectionChoiceDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	protected boolean initDialog( )
	{
		Assert.isNotNull( selectionChoice );
		labelEditor.setText( UIUtil.convertToGUIString( selectionChoice.getLabel( ) ) );
		valueEditor.setText( UIUtil.convertToGUIString( selectionChoice.getValue( ) ) );
		if ( validator != null )
		{
			updateStatus( );
		}
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		composite.setLayout( new GridLayout( 2, false ) );
		new Label( composite, SWT.NONE ).setText( "Display Text:" );
		labelEditor = new Text( composite, SWT.BORDER );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 150;
		labelEditor.setLayoutData( gd );
		new Label( composite, SWT.NONE ).setText( "Value:" );
		valueEditor = new Text( composite, SWT.BORDER );
		valueEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		messageLine = new CLabel( composite, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		messageLine.setLayoutData( gd );
		if ( validator != null )
		{
			ModifyListener listener = new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateStatus( );
				}

			};
			labelEditor.addModifyListener( listener );
			valueEditor.addModifyListener( listener );

		}
		UIUtil.bindHelp( composite,
				IHelpContextIds.SELECTION_CHOICE_DIALOG );
		return composite;
	}

	protected void okPressed( )
	{
		selectionChoice.setLabel( UIUtil.convertToModelString( labelEditor.getText( ),
				false ) );
		selectionChoice.setValue( UIUtil.convertToModelString( valueEditor.getText( ),
				false ) );
		setResult( selectionChoice );
		super.okPressed( );
	}

	private void updateStatus( )
	{
		String erroeMessage = validator.validate( UIUtil.convertToModelString( labelEditor.getText( ),
				false ),
				UIUtil.convertToModelString( valueEditor.getText( ), false ) );
		if ( erroeMessage != null )
		{
			messageLine.setText( erroeMessage );
			messageLine.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
			getOkButton( ).setEnabled( false );
		}
		else
		{
			messageLine.setText( "" );
			messageLine.setImage( null );
			getOkButton( ).setEnabled( true );
		}
	}

	public void setInput( SelectionChoice selectionChoice )
	{
		this.selectionChoice = selectionChoice;
	}

	public void setValidator( ISelectionChoiceValidator validator )
	{
		this.validator = validator;
	}
}
