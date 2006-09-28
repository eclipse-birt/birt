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
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * 
 */

public class DataSetParameterBindingInputDialog extends BaseDialog
{

	private static final String LABEL_NAME = Messages.getString( "DataSetParameterBindingInputDialog.Label.Name" ); //$NON-NLS-1$
	private static final String LABEL_DATA_TYPE = Messages.getString( "DataSetParameterBindingInputDialog.Label.DataType" ); //$NON-NLS-1$
	private static final String LABEL_VALUE = Messages.getString( "DataSetParameterBindingInputDialog.Label.Value" ); //$NON-NLS-1$
	private static final String DIALOG_TITLE = Messages.getString( "DataSetParameterBindingInputDialog.Title" ); //$NON-NLS-1$

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( DataSetParameter.STRUCT_NAME )
			.getMember( DataSetParameter.DATA_TYPE_MEMBER )
			.getAllowedChoices( );

	private Label nameLabel, typeLabel;
	private Text valueEditor;
	private Button expButton;
	private String value;
	private DataSetParameterHandle handle;
	private IExpressionProvider provider;

	public DataSetParameterBindingInputDialog( Shell parentShell,
			DataSetParameterHandle handle, IExpressionProvider provider )
	{
		super( parentShell, DIALOG_TITLE );
		this.handle = handle;
		this.provider = provider;
	}

	public DataSetParameterBindingInputDialog( DataSetParameterHandle handle,
			IExpressionProvider provider )
	{
		this( UIUtil.getDefaultShell( ), handle, provider );
	}

	protected boolean initDialog( )
	{
		nameLabel.setText( handle.getName( ) );
		typeLabel.setText( DATA_TYPE_CHOICE_SET.findChoice( handle.getParameterDataType( ) )
				.getDisplayName( ) );
		if ( value == null )
		{
			value = ""; //$NON-NLS-1$
		}
		valueEditor.setText( value );
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		composite.setLayout( new GridLayout( 2, false ) );
	
		UIUtil.bindHelp( composite, IHelpContextIds.DATA_SET_PARAMETER_BINDING_DIALOG );
		
		new Label( composite, SWT.NONE ).setText( LABEL_NAME );
		nameLabel = new Label( composite, SWT.NONE );
		nameLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		new Label( composite, SWT.NONE ).setText( LABEL_DATA_TYPE );
		typeLabel = new Label( composite, SWT.NONE );
		typeLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		new Label( composite, SWT.NONE ).setText( LABEL_VALUE );
		Composite valueComposite = new Composite( composite, SWT.NONE );
		valueComposite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2,
				false ) );
		valueComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		valueEditor = new Text( valueComposite, SWT.BORDER | SWT.SINGLE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.widthHint = 150;
		valueEditor.setLayoutData( gd );
		expButton = new Button( valueComposite, SWT.PUSH );
		expButton.setText( "..." ); //$NON-NLS-1$
		expButton.setLayoutData( new GridData( ) );
		expButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder dialog = new ExpressionBuilder( valueEditor.getText( ) );
				dialog.setExpressionProvier( provider );
				if ( dialog.open( ) == OK )
				{
					valueEditor.setText( dialog.getResult( ) );
				}
			}
		} );
		return composite;
	}

	protected void okPressed( )
	{
		setResult( valueEditor.getText( ) );
		super.okPressed( );
	}

	public void setValue( String value )
	{
		this.value = value;
	}
}
