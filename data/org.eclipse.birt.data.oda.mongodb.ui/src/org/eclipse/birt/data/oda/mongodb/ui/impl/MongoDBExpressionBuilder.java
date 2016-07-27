/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.oda.mongodb.ui.impl;

import java.io.IOException;

import org.eclipse.birt.data.oda.mongodb.ui.i18n.Messages;
import org.eclipse.birt.data.oda.mongodb.ui.util.CommandExpressionUtil;
import org.eclipse.birt.data.oda.mongodb.ui.util.UIHelper;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.internal.ui.dialogs.ExceptionHandler;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.StatusDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

import org.eclipse.birt.data.oda.mongodb.internal.impl.QueryModel;

public class MongoDBExpressionBuilder extends StatusDialog
{

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	private String dialogTitle = Messages
			.getString( "MongoDBExpressionBuilder.dialogTitle.default" ); //$NON-NLS-1$

	protected String expression;

	private Button validateSyntaxBtn, importBtn, exportBtn;
	private Text exprText;

	public MongoDBExpressionBuilder( Shell parent )
	{
		super( parent );
		initDialogTitle( );
	}

	/**
	 * Initialize the dialog title which needs to override
	 * 
	 */
	protected void initDialogTitle( )
	{
		setTitle( dialogTitle );
	}

	public void create( )
	{
		super.create( );

		Point pt = getShell( ).computeSize( -1, -1 );
		pt.x = Math.max( pt.x, 450 );
		pt.y = Math.max( pt.y, 350 );
		getShell( ).setSize( pt );

		validateStatus( );
	}

	protected boolean isResizable( )
	{
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginHeight = 20;
		layout.marginWidth = 10;
		layout.horizontalSpacing = 15;
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Label exprTextLabel = new Label( composite, SWT.NONE );
		exprTextLabel.setText( Messages
				.getString( "MongoDBExpressionBuilder.ExprssionText.label" ) ); //$NON-NLS-1$
		GridData gd = new GridData( );
		gd.horizontalIndent = 6;
		exprTextLabel.setLayoutData( gd );

		createValidateSyntaxButton( composite );

		createExprEditorArea( composite );

		createDialogHelper( composite );

		initDialogControls( );

		return composite;

	}

	protected void createDialogHelper( Composite composite )
	{
	}

	private void createExprEditorArea( Composite composite )
	{
		Composite editorArea = new Composite( composite, SWT.NONE );
		editorArea.setLayout( new GridLayout( 2, false ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.horizontalSpan = 2;
		editorArea.setLayoutData( gd );

		exprText = new Text( editorArea, SWT.NONE | SWT.MULTI | SWT.BORDER
				| SWT.V_SCROLL | SWT.H_SCROLL );
		exprText.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		exprText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				expression = exprText.getText( ).trim( );
				validateSyntaxBtn.setEnabled( expression.length( ) > 0 );
				exportBtn.setEnabled( expression.length( ) > 0 );
				validateStatus( );
			}

		} );

		Composite btnArea = new Composite( editorArea, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginTop = 10;
		layout.marginBottom = 10;
		layout.verticalSpacing = 20;
		btnArea.setLayout( layout );
		btnArea.setLayoutData( new GridData( ) );

		importBtn = new Button( btnArea, SWT.PUSH );
		importBtn.setText( Messages
				.getString( "MongoDBExpressionBuilder.Button.import" ) ); //$NON-NLS-1$
		importBtn.setToolTipText( Messages.getString(
				"MongoDBExpressionBuilder.Button.tooltip.import" ) ); //$NON-NLS-1$

		importBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				doImportCommandExpression( );

			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

		exportBtn = new Button( btnArea, SWT.PUSH );
		exportBtn.setText( Messages
				.getString( "MongoDBExpressionBuilder.Button.export" ) ); //$NON-NLS-1$
		exportBtn.setToolTipText( Messages.getString(
				"MongoDBExpressionBuilder.Button.tooltip.export" ) ); //$NON-NLS-1$

		exportBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				FileDialog dialog = new FileDialog( getShell( ),
						SWT.SINGLE | SWT.SAVE );
				dialog.setFilterExtensions( new String[]{"*.*" //$NON-NLS-1$ ,
																// $NON-NLS-2$
				} );
				dialog.setOverwrite( true );

				String fileName = dialog.open( );
				if ( fileName != null )
				{
					handleExportToFile( fileName );
				}
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );

	}

	private void createValidateSyntaxButton( Composite btnArea )
	{
		validateSyntaxBtn = new Button( btnArea, SWT.PUSH );
		try
		{
			validateSyntaxBtn.setImage( UIHelper.getSyntaxValidationImage( ) );
		}
		catch ( IOException e1 )
		{
			e1.printStackTrace( );
		}

		GridData btnGd = new GridData( );
		if ( !Platform.getOS( ).equals( Platform.OS_MACOSX ) )
		{
			btnGd.widthHint = 20;
			btnGd.heightHint = 20;
		}
		validateSyntaxBtn.setLayoutData( btnGd );

		validateSyntaxBtn.setToolTipText( Messages.getString(
				"MongoDBExpressionBuilder.Button.tooltip.ValidateSyntax" ) ); //$NON-NLS-1$

		validateSyntaxBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetSelected( SelectionEvent e )
			{
				validateExpressionSyntax( );

			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}

		} );
	}

	private void handleExportToFile( String fileName )
	{
		try
		{
			CommandExpressionUtil.exportToFile( fileName, expression );
		}
		catch ( IOException e1 )
		{
			e1.printStackTrace( );
		}
	}

	private void initDialogControls( )
	{
		if ( expression == null )
		{
			expression = EMPTY_STRING;
		}
		exprText.setText( expression );
		validateSyntaxBtn.setEnabled( expression.length( ) > 0 );
		exportBtn.setEnabled( expression.length( ) > 0 );

		resetButtonsWidth( );
	}

	private void resetButtonsWidth( )
	{
		int width = getMaxWidth( importBtn, 60 );
		width = getMaxWidth( exportBtn, width );
		width = getMaxWidth( validateSyntaxBtn, width ) + 10;

		GridData btnGd = new GridData( );
		btnGd.widthHint = width;

		importBtn.setLayoutData( btnGd );
		exportBtn.setLayoutData( btnGd );

	}

	private int getMaxWidth( Control control, int width )
	{
		int size = control.computeSize( -1, -1 ).x;
		return size > width ? size : width;
	}

	private void doImportCommandExpression( )
	{
		FileDialog dialog = new FileDialog( getShell( ), SWT.SINGLE );
		dialog.setFilterExtensions( new String[]{"*.*" //$NON-NLS-1$ ,
														// $NON-NLS-2$
		} );

		String fileName = dialog.open( );
		if ( fileName != null )
		{
			try
			{
				expression = CommandExpressionUtil
						.getCommandExpressionText( fileName );

				exprText.setText( expression );

			}
			catch ( IOException e )
			{
				e.printStackTrace( );
			}

		}

	}

	/**
	 * Method to validate the dialog status and need to be overwritten
	 * 
	 */
	protected void validateStatus( )
	{

	}

	protected Status getMiscStatus( int severity, String message )
	{
		return new Status( severity, PlatformUI.PLUGIN_ID, severity, message,
				null );
	}

	protected Status getOKStatus( )
	{
		return getMiscStatus( IStatus.OK, EMPTY_STRING );
	}

	protected void validateExpressionSyntax( )
	{
		String msgDialogTitle = Messages.getString(
				"MongoDBExpressionBuilder.InfoDialog.title.validateSyntax" ); //$NON-NLS-1$
		try
		{
			doValidateExpressionSyntax( );

			String infoMsg = Messages.getString(
					"MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.IsValid" );//$NON-NLS-1$
			MessageDialog.openInformation( getShell( ), msgDialogTitle,
					infoMsg );
		}
		catch ( OdaException e1 )
		{
			String errorMsg = UIHelper.getUserErrorMessage(
					"MongoDBExpressionBuilder.Expression.DialogMessage.ValidateSyntax.Invalid", //$NON-NLS-1$
					e1 );
			ExceptionHandler.showException( getShell( ), msgDialogTitle,
					errorMsg, e1 );
		}
	}

	/**
	 * Validate the syntax of the expression, which needs to override
	 * 
	 * @throws OdaException
	 */
	protected void doValidateExpressionSyntax( ) throws OdaException
	{
		QueryModel.validateQuerySyntax( expression );
	}

	protected void setExpressionText( String expr )
	{
		this.expression = expr;
	}

	protected String getExprText( )
	{
		return expression;
	}

}
