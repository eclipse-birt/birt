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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

/**
 * 
 */

public class AddComputedSummaryDialog extends BaseDialog
{

	private CrosstabReportItemHandle crosstab = null;
	private final static String TITLE = Messages.getString( "AddComputedSummaryDialog.Title" );

	private Button expressionBtn;
	private Text nameText, expressionText;
	private CLabel errorLabel;
	private ComputedMeasureViewHandle compustedMeasure;

	private String name;
	private String expression;

	public AddComputedSummaryDialog( Shell parentShell,
			CrosstabReportItemHandle crosstab )
	{
		super( parentShell, TITLE );
		this.crosstab = crosstab;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createDialogArea( Composite parent )

	{
		Composite parentComposite = (Composite) super.createDialogArea( parent );

		Composite composite = new Composite( parentComposite, SWT.NONE );
		GridLayout layout = new GridLayout( 3, false );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		composite.setLayoutData( gridData );
		composite.setLayout( layout );

		Label nameLabel = new Label( composite, SWT.NONE );
		nameLabel.setText( Messages.getString( "AddComputedSummaryDialog.Label.Name" ) );

		nameText = new Text( composite, SWT.BORDER );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.widthHint = 200;
		gridData.horizontalSpan = 2;
		nameText.setLayoutData( gridData );
		nameText.addModifyListener( modifyListener );

		Label expressionLabel = new Label( composite, SWT.NONE );
		expressionLabel.setText( Messages.getString( "AddComputedSummaryDialog.Label.Expression" ) );

		expressionText = new Text( composite, SWT.BORDER );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		expressionText.setLayoutData( gridData );
		expressionText.addModifyListener( modifyListener );

		expressionBtn = new Button( composite, SWT.NONE );
		UIUtil.setExpressionButtonImage( expressionBtn );
		expressionBtn.addSelectionListener( exprBuildListener );

		// Label space = new Label(composite, SWT.NONE);
		// gridData = new GridData();
		// gridData.heightHint = 25;
		// space.setLayoutData( gridData );

		Label seperator = new Label( parentComposite, SWT.SEPARATOR
				| SWT.HORIZONTAL );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		seperator.setLayoutData( gridData );

		errorLabel = new CLabel( parentComposite, SWT.NONE );
		errorLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		return parentComposite;
	}

	protected Control createContents( Composite parent )
	{
		Control contents = super.createContents( parent );
		validate( );
		return contents;
	}

	protected SelectionListener exprBuildListener = new SelectionListener( ) {

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}

		public void widgetSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub
			if ( e.widget != expressionBtn )
			{
				return;
			}
			ExpressionBuilder dialog = new ExpressionBuilder( expressionText.getText( ) );
			dialog.setExpressionProvier( new CrosstabComputedMeasureExpressionProvider( crosstab.getModelHandle( ) ) );
			if ( dialog.open( ) == Dialog.OK )
			{
				expressionText.setText( dialog.getResult( ) );
			}

		}
	};

	private ModifyListener modifyListener = new ModifyListener( ) {

		public void modifyText( ModifyEvent e )
		{
			// TODO Auto-generated method stub
			validate( );
		}
	};

	protected void validate( )
	{
		boolean ok = true;
		final String EMPTY_STRING = "";
		String errorMessage = EMPTY_STRING;
		String name = nameText.getText( ).trim( );
		String expression = expressionText.getText( ).trim( );
		if ( name.length( ) == 0 )
		{
			errorMessage = Messages.getString( "AddComputedSummaryDialog.ErrMsg.Msg1" );
		}
		else if ( crosstab.getMeasure( name ) != null )
		{
			errorMessage = Messages.getString( "AddComputedSummaryDialog.ErrMsg.Msg2" );
		}else if( expression.length( ) == 0)
		{
			errorMessage = Messages.getString( "AddComputedSummaryDialog.ErrMsg.Msg3" );
		}

		if ( !errorMessage.equals( EMPTY_STRING ) )
		{
			ok = false;
		}

		getOkButton( ).setEnabled( ok );

		if ( ( errorLabel != null ) && ( !errorLabel.isDisposed( ) ) )
		{
			errorLabel.setText( errorMessage );
			if ( ok )
			{
				errorLabel.setImage( null );
			}
			else
			{
				errorLabel.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
			}
		}

	}

	public String getName( )
	{
		return name;
	}

	public String getExpression( )
	{
		return expression;
	}

	protected void okPressed( )
	{
		name = nameText.getText( ).trim( );
		expression = expressionText.getText( );
		super.okPressed( );
	}

}
