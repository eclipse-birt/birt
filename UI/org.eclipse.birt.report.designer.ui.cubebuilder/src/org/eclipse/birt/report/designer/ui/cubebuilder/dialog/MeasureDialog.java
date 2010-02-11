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

package org.eclipse.birt.report.designer.ui.cubebuilder.dialog;

import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.helper.IDialogHelperProvider;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.util.BuilderConstants;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.ElementAdapterManager;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;

public class MeasureDialog extends TitleAreaDialog
{

	private boolean isEdit = false;
	private Combo typeCombo;
	private Text expressionText;
	private Combo functionCombo;

	private TabularMeasureHandle input;
	private Text nameText;
	private static IChoice[] dataTypes = DEUtil.getMetaDataDictionary( )
			.getElement( ReportDesignConstants.MEASURE_ELEMENT )
			.getProperty( IMeasureModel.DATA_TYPE_PROP )
			.getAllowedChoices( )
			.getChoices( );
	private Object result;
	private IDialogHelper helper;

	public MeasureDialog( boolean newOrEdit )
	{
		super( UIUtil.getDefaultShell( ) );
		setShellStyle( getShellStyle( ) | SWT.RESIZE | SWT.MAX );
		this.isEdit = !newOrEdit;
	}

	private String[] getDataTypeNames( )
	{
		IChoice[] choices = dataTypes;
		if ( choices == null )
			return new String[0];

		String[] names = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			names[i] = choices[i].getName( );
		}
		return names;
	}

	private String getDataTypeDisplayName( String name )
	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				DEUtil.getMetaDataDictionary( )
						.getElement( ReportDesignConstants.MEASURE_ELEMENT )
						.getProperty( IMeasureModel.DATA_TYPE_PROP )
						.getAllowedChoices( ) );
	}

	private String[] getDataTypeDisplayNames( )
	{
		IChoice[] choices = dataTypes;
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;
	}

	private String[] getFunctionDisplayNames( )
	{
		IAggrFunction[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;
	}

	private IAggrFunction getFunctionByDisplayName( String displayName )
	{
		IAggrFunction[] choices = getFunctions( );
		if ( choices == null )
			return null;

		for ( int i = 0; i < choices.length; i++ )
		{
			if ( choices[i].getDisplayName( ).equals( displayName ) )
			{
				return choices[i];
			}
		}
		return null;
	}

	private String getFunctionDisplayName( String function )
	{
		try
		{
			return DataUtil.getAggregationManager( )
					.getAggregation( function )
					.getDisplayName( );
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
			return null;
		}
	}

	private IAggrFunction[] getFunctions( )
	{
		try
		{
			List aggrInfoList = DataUtil.getAggregationManager( )
					.getAggregations( AggregationManager.AGGR_MEASURE );
			return (IAggrFunction[]) aggrInfoList.toArray( new IAggrFunction[0] );
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
			return new IAggrFunction[0];
		}
	}

	public void setInput( TabularMeasureHandle input )
	{
		this.input = input;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.MEASURE_DIALOG );
		setTitle( Messages.getString( "MeasureDialog.Title.Property" ) ); //$NON-NLS-1$

		Composite area = (Composite) super.createDialogArea( parent );

		Composite contents = new Composite( area, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.verticalSpacing = 0;
		layout.marginWidth = 20;
		contents.setLayout( layout );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = convertWidthInCharsToPixels( 70 );
		contents.setLayoutData( data );

		createMeasureArea( contents );
		WidgetUtil.createGridPlaceholder( contents, 1, true );

		initMeasureDialog( );

		return contents;
	}

	private void initMeasureDialog( )
	{
		typeCombo.setItems( getDataTypeDisplayNames( ) );
		functionCombo.setItems( getFunctionDisplayNames( ) );
		if ( !isEdit )
		{
			if ( typeCombo.getItemCount( ) > 0 )
			{
				typeCombo.select( 0 );
			}
			if ( functionCombo.getItemCount( ) > 0 )
			{
				functionCombo.select( 0 );
			}
			handleFunctionSelectEvent( );
		}
		else
		{
			try
			{
				functionCombo.setText( getFunctionDisplayName( DataAdapterUtil.adaptModelAggregationType( input.getFunction( ) ) ) == null ? "" //$NON-NLS-1$
						: getFunctionDisplayName( DataAdapterUtil.adaptModelAggregationType( input.getFunction( ) ) ) );
			}
			catch ( AdapterException e )
			{
				ExceptionUtil.handle( e );
			}

			ExpressionButtonUtil.initExpressionButtonControl( expressionText,
					input,
					MeasureHandle.MEASURE_EXPRESSION_PROP );

			nameText.setText( input.getName( ) == null ? "" : input.getName( ) ); //$NON-NLS-1$
			handleFunctionSelectEvent( );
			typeCombo.setText( getDataTypeDisplayName( input.getDataType( ) ) == null ? "" //$NON-NLS-1$
					: getDataTypeDisplayName( input.getDataType( ) ) );
		}

	}

	public Object getResult( )
	{
		return result;
	}

	protected void okPressed( )
	{
		try
		{
			if ( !isEdit )
			{
				TabularMeasureHandle measure;
				if ( input == null )
					measure = DesignElementFactory.getInstance( )
							.newTabularMeasure( nameText.getText( ) );
				else
				{
					measure = input;
					input.setName( nameText.getText( ) );
				}

				measure.setFunction( getFunctions( )[functionCombo.getSelectionIndex( )].getName( ) );
				measure.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				if ( expressionText.isEnabled( ) )
				{
					ExpressionButtonUtil.saveExpressionButtonControl( expressionText,
							measure,
							MeasureHandle.MEASURE_EXPRESSION_PROP );
				}
				if ( helper != null )
				{
					measure.setExpressionProperty( MeasureHandle.ACL_EXPRESSION_PROP,
							(Expression) helper.getProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY ) );
				}
				result = measure;
			}
			else
			{
				input.setName( nameText.getText( ) );
				input.setFunction( getFunctions( )[functionCombo.getSelectionIndex( )].getName( ) );
				input.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				if ( expressionText.isEnabled( ) )
				{
					ExpressionButtonUtil.saveExpressionButtonControl( expressionText,
							input,
							MeasureHandle.MEASURE_EXPRESSION_PROP );
				}
				else
					input.setMeasureExpression( null );
				if ( helper != null )
				{
					input.setExpressionProperty( MeasureHandle.ACL_EXPRESSION_PROP,
							(Expression) helper.getProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY ) );
				}
				result = input;
			}

		}
		catch ( SemanticException e )
		{
			ExceptionUtil.handle( e );
			return;
		}
		super.okPressed( );
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		checkOkButtonStatus( );
	}

	private Composite createMeasureArea( Composite parent )
	{
		getShell( ).setText( Messages.getString( "MeasureDialog.Title.Property" ) ); //$NON-NLS-1$
		Group group = new Group( parent, SWT.NONE );
		GridData gd = new GridData( );
		gd.grabExcessHorizontalSpace = true;
		gd.horizontalAlignment = SWT.FILL;
		group.setLayoutData( gd );

		GridLayout layout = new GridLayout( );
		layout.numColumns = 3;
		group.setLayout( layout );

		Label nameLabel = new Label( group, SWT.NONE );
		nameLabel.setText( Messages.getString( "MeasureDialog.Label.Name" ) ); //$NON-NLS-1$
		nameText = new Text( group, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		nameText.setLayoutData( gd );
		nameText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		Label functionLabel = new Label( group, SWT.NONE );
		functionLabel.setText( Messages.getString( "MeasureDialog.Label.Function" ) ); //$NON-NLS-1$
		functionCombo = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		functionCombo.setLayoutData( gd );
		functionCombo.setVisibleItemCount( 30 );
		functionCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleFunctionSelectEvent( );
				checkOkButtonStatus( );
			}

		} );

		Label typeLabel = new Label( group, SWT.NONE );
		typeLabel.setText( Messages.getString( "MeasureDialog.Label.DataType" ) ); //$NON-NLS-1$
		typeCombo = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		typeCombo.setLayoutData( gd );
		typeCombo.setVisibleItemCount( 30 );
		typeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleTypeSelectEvent( );
				checkOkButtonStatus( );
			}

		} );

		Label expressionLabel = new Label( group, SWT.NONE );
		expressionLabel.setText( Messages.getString( "MeasureDialog.Label.Expression" ) ); //$NON-NLS-1$
		expressionText = new Text( group, SWT.SINGLE | SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		expressionText.setLayoutData( gd );
		expressionText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );

			}

		} );

		ExpressionButtonUtil.createExpressionButton( group,
				expressionText,
				new CubeExpressionProvider( input ),
				input );

		createSecurityPart( group );
		createHyperLinkPart( group );
		return group;
	}

	private IDialogHelper createHyperLinkPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null )
				{
					final IDialogHelper hyperLinkHelper = helperProvider.createHelper( this,
							BuilderConstants.HYPERLINK_HELPER_KEY );
					if ( hyperLinkHelper != null )
					{
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_LABEL,
								Messages.getString( "MeasureDialog.Label.LinkTo" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_BUTTON_TEXT,
								Messages.getString( "MeasureDialog.Button.Text.Edit" ) ); //$NON-NLS-1$
						hyperLinkHelper.setProperty( BuilderConstants.HYPERLINK_REPORT_ITEM_HANDLE,
								input );
						hyperLinkHelper.createContent( parent );
						hyperLinkHelper.addListener( SWT.Modify,
								new Listener( ) {

									public void handleEvent( Event event )
									{
										hyperLinkHelper.update( false );
									}
								} );
						hyperLinkHelper.update( true );
						return hyperLinkHelper;
					}
				}
			}
		}
		return null;
	}

	private void createSecurityPart( Composite parent )
	{
		Object[] helperProviders = ElementAdapterManager.getAdapters( input,
				IDialogHelperProvider.class );
		if ( helperProviders != null )
		{
			for ( int i = 0; i < helperProviders.length; i++ )
			{
				IDialogHelperProvider helperProvider = (IDialogHelperProvider) helperProviders[i];
				if ( helperProvider != null && helper == null )
				{
					helper = helperProvider.createHelper( this,
							BuilderConstants.SECURITY_HELPER_KEY );
					if ( helper != null )
					{
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_LABEL,
								Messages.getString( "MeasureDialog.Access.Control.List.Expression" ) ); //$NON-NLS-1$
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_CONTEXT,
								input );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROVIDER,
								new CubeExpressionProvider( input ) );
						helper.setProperty( BuilderConstants.SECURITY_EXPRESSION_PROPERTY,
								input.getACLExpression( ) );
						helper.createContent( parent );
						helper.addListener( SWT.Modify, new Listener( ) {

							public void handleEvent( Event event )
							{
								helper.update( false );
							}
						} );
						helper.update( true );
					}
				}
			}
		}
	}

	protected void handleTypeSelectEvent( )
	{
		IAggrFunction function = getFunctionByDisplayName( functionCombo.getText( ) );
		if ( function == null )
			return;
		try
		{
			String recommendType = getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationManager( )
					.getAggregation( function.getName( ) )
					.getDataType( ) ) );
			if ( !typeCombo.getText( ).equals( recommendType )
					&& typeCombo.indexOf( recommendType ) != -1 )
			{
				if ( !MessageDialog.openQuestion( getShell( ),
						Messages.getString( "MeasureDialog.MessageDialog.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "MeasureDialog.MessageDialog.Message", //$NON-NLS-1$
								new Object[]{
									recommendType
								} ) ) )
					typeCombo.setText( recommendType );
			}
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
		}
	}

	private void handleFunctionSelectEvent( )
	{
		IAggrFunction function = getFunctionByDisplayName( functionCombo.getText( ) );
		if ( function == null )
			return;
		try
		{
			typeCombo.setText( getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationManager( )
					.getAggregation( function.getName( ) )
					.getDataType( ) ) ) );
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
		}
		int parameterLength = function.getParameterDefn( ).length;
		expressionText.setEnabled( parameterLength > 0 );
		( (ExpressionButton) expressionText.getData( ExpressionButtonUtil.EXPR_BUTTON ) ).setEnabled( parameterLength > 0 );
	}

	protected void checkOkButtonStatus( )
	{
		if ( nameText.getText( ) == null
				|| nameText.getText( ).trim( ).equals( "" ) //$NON-NLS-1$
				|| functionCombo.getSelectionIndex( ) == -1
				|| typeCombo.getSelectionIndex( ) == -1 )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setMessage( null );
				setErrorMessage( Messages.getString( "MeasureDialog.Message.BlankName" ) ); //$NON-NLS-1$
				return;
			}
		}
		else if ( !UIUtil.validateDimensionName( nameText.getText( ) ) )
		{
			if ( getButton( IDialogConstants.OK_ID ) != null )
			{
				getButton( IDialogConstants.OK_ID ).setEnabled( false );
				setMessage( null );
				setErrorMessage( Messages.getString( "MeasureDialog.Message.NumericName" ) ); //$NON-NLS-1$
				return;
			}
		}

		IAggrFunction function = getFunctionByDisplayName( functionCombo.getText( ) );
		if ( function != null && function.getParameterDefn( ).length > 0 )
		{

			IParameterDefn param = function.getParameterDefn( )[0];
			if ( !param.isOptional( ) )
			{
				if ( expressionText.getText( ) == null
						|| expressionText.getText( ).trim( ).length( ) == 0 )
				{
					if ( getButton( IDialogConstants.OK_ID ) != null )
					{
						getButton( IDialogConstants.OK_ID ).setEnabled( false );
						setErrorMessage( null );
						setMessage( null );
						return;
					}
				}
			}
		}

		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			getButton( IDialogConstants.OK_ID ).setEnabled( true );
			setErrorMessage( null );
			setMessage( null );
		}
	}
}
