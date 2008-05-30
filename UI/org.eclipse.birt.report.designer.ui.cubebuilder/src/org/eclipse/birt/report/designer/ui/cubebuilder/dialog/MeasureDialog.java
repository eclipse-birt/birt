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
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.nls.Messages;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.CubeExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularMeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class MeasureDialog extends BaseDialog
{

	private TabularMeasureHandle input;
	private TabularCubeHandle cube;
	private Text nameText;
	private static IChoice[] dataTypes = DEUtil.getMetaDataDictionary( )
			.getElement( ReportDesignConstants.MEASURE_ELEMENT )
			.getProperty( IMeasureModel.DATA_TYPE_PROP )
			.getAllowedChoices( )
			.getChoices( );

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
			ExceptionHandler.handle( e );
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
			ExceptionHandler.handle( e );
			return new IAggrFunction[0];
		}
	}

	public MeasureDialog( boolean newOrEdit )
	{
		super( Messages.getString( "MeasureDialog.Title" ) ); //$NON-NLS-1$
		this.isEdit = !newOrEdit;
	}

	public void setInput( TabularCubeHandle cube, TabularMeasureHandle input )
	{
		this.input = input;
		this.cube = cube;
	}

	/*
	 * (non-Javadoc) Method declared on Dialog.
	 */
	protected Control createDialogArea( Composite parent )
	{
		createTitleArea( parent );
		UIUtil.bindHelp( parent, IHelpContextIds.MEASURE_DIALOG );

		Composite contents = new Composite( parent, SWT.NONE );
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
		}
		else
		{
			typeCombo.setText( getDataTypeDisplayName( input.getDataType( ) ) == null ? "" //$NON-NLS-1$
					: getDataTypeDisplayName( input.getDataType( ) ) );
			try
			{
				functionCombo.setText( getFunctionDisplayName( DataAdapterUtil.adaptModelAggregationType( input.getFunction( ) ) ) == null ? "" //$NON-NLS-1$
						: getFunctionDisplayName( DataAdapterUtil.adaptModelAggregationType( input.getFunction( ) ) ) );
			}
			catch ( AdapterException e )
			{
				ExceptionHandler.handle( e );
			}
			expressionText.setText( input.getMeasureExpression( ) == null ? "" //$NON-NLS-1$
					: input.getMeasureExpression( ) );
			nameText.setText( input.getName( ) == null ? "" : input.getName( ) ); //$NON-NLS-1$
		}
		handleFunctionSelectEvent( );
	}

	private Object result;

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
				TabularMeasureHandle measure = DesignElementFactory.getInstance( )
						.newTabularMeasure( nameText.getText( ) );

				measure.setFunction( getFunctions( )[functionCombo.getSelectionIndex( )].getName( ) );
				measure.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				if ( expressionText.isEnabled( ) )
					measure.setMeasureExpression( expressionText.getText( ) );
				result = measure;
			}
			else
			{
				input.setName( nameText.getText( ) );
				input.setFunction( getFunctions( )[functionCombo.getSelectionIndex( )].getName( ) );
				input.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				if ( expressionText.isEnabled( ) )
					input.setMeasureExpression( expressionText.getText( ) );
				else
					input.setMeasureExpression( null );
				result = input;
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
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

		expressionButton = new Button( group, SWT.PUSH );
		UIUtil.setExpressionButtonImage( expressionButton );
		expressionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				openExpression( );
			}
		} );

		return group;
	}

	protected void handleTypeSelectEvent( )
	{
		IAggrFunction function = getFunctionByDisplayName( functionCombo.getText( ) );
		try
		{
			String recommendType = getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationManager( )
					.getAggregation( function.getName( ) )
					.getDataType( ) ) );
			if ( !typeCombo.getText( ).equals( recommendType ) )
			{
				if ( !MessageDialog.openQuestion( getShell( ),
						Messages.getString( "MeasureDialog.MessageDialog.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( Messages.getString( "MeasureDialog.MessageDialog.Message" ), //$NON-NLS-1$
								new Object[]{
									recommendType
								} ) ) )
					typeCombo.setText( recommendType );
			}
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void handleFunctionSelectEvent( )
	{
		IAggrFunction function = getFunctionByDisplayName( functionCombo.getText( ) );
		try
		{
			typeCombo.setText( getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationManager( )
					.getAggregation( function.getName( ) )
					.getDataType( ) ) ) );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
		int parameterLength = function.getParameterDefn( ).length;
		expressionText.setEnabled( parameterLength > 0 );
		expressionButton.setEnabled( parameterLength > 0 );
	}

	protected void checkOkButtonStatus( )
	{
		if ( nameText.getText( ) == null
				|| nameText.getText( ).trim( ).equals( "" ) //$NON-NLS-1$
				|| functionCombo.getSelectionIndex( ) == -1
				|| typeCombo.getSelectionIndex( ) == -1 )
		{
			if ( getOkButton( ) != null )
			{
				getOkButton( ).setEnabled( false );
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
					if ( getOkButton( ) != null )
					{
						getOkButton( ).setEnabled( false );
						return;
					}
				}

			}
		}

		if ( getOkButton( ) != null )
			getOkButton( ).setEnabled( true );

	}

	private void openExpression( )
	{
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( expressionText.getText( ) );
		ExpressionProvider provider = new CubeExpressionProvider( cube );
		expressionBuilder.setExpressionProvier( provider );
		if ( expressionBuilder.open( ) == Window.OK )
		{
			expressionText.setText( expressionBuilder.getResult( ) );
		}
	}

	private Composite createTitleArea( Composite parent )
	{
		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayout( new GridLayout( ) );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		contents.setLayoutData( data );

		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite( contents, SWT.NONE );
		FormLayout layout = new FormLayout( );
		layout.marginHeight = heightMargins;
		layout.marginWidth = widthMargins;
		titleArea.setLayout( layout );

		Display display = parent.getDisplay( );
		Color background = JFaceColors.getBannerBackground( display );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20 + ( heightMargins * 2 );
		titleArea.setLayoutData( layoutData );
		titleArea.setBackground( background );

		titleArea.addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				e.gc.setForeground( titleArea.getDisplay( )
						.getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
				Rectangle bounds = titleArea.getClientArea( );
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle( bounds );
			}
		} );

		Label label = new Label( titleArea, SWT.NONE );
		label.setBackground( background );
		label.setFont( FontManager.getFont( label.getFont( ).toString( ),
				10,
				SWT.BOLD ) );
		label.setText( Messages.getString( "MeasureDialog.Title.Property" ) ); //$NON-NLS-1$
		return titleArea;

	}

	private boolean isEdit = false;
	private Combo typeCombo;
	private Text expressionText;
	private Combo functionCombo;
	private Button expressionButton;

}
