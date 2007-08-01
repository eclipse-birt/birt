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

	private String getDataTypeDisplaName( String name )
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
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;

	}

	private String[] getFunctionNames( )
	{
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getName( );
		}
		return displayNames;
	}

	private String getFunctionDisplayName( String name )

	{
		return ChoiceSetFactory.getDisplayNameFromChoiceSet( name,
				DEUtil.getMetaDataDictionary( )
						.getElement( ReportDesignConstants.MEASURE_ELEMENT )
						.getProperty( IMeasureModel.FUNCTION_PROP )
						.getAllowedChoices( ) );

	}

	private IChoice[] getFunctions( )

	{
		return DEUtil.getMetaDataDictionary( )
				.getElement( ReportDesignConstants.MEASURE_ELEMENT )
				.getProperty( IMeasureModel.FUNCTION_PROP )
				.getAllowedChoices( )
				.getChoices( );

	}

	public MeasureDialog( boolean newOrEdit )
	{
		super( Messages.getString( "MeasureDialog.Title" ) );
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
		UIUtil.bindHelp( parent, IHelpContextIds.MEASURE_DIALOG ); //$NON-NLS-1$

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
			typeCombo.setText( getDataTypeDisplaName( input.getDataType( ) ) == null ? ""
					: getDataTypeDisplaName( input.getDataType( ) ) );
			functionCombo.setText( getFunctionDisplayName( input.getFunction( ) ) == null ? ""
					: getFunctionDisplayName( input.getFunction( ) ) );
			expressionText.setText( input.getMeasureExpression( ) == null ? ""
					: input.getMeasureExpression( ) );
			nameText.setText( input.getName( ) == null ? "" : input.getName( ) );
		}
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

				measure.setFunction( getFunctionNames( )[functionCombo.getSelectionIndex( )] );
				measure.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				measure.setMeasureExpression( expressionText.getText( ) );
				result = measure;
			}
			else
			{
				input.setName( nameText.getText( ) );
				input.setFunction( getFunctionNames( )[functionCombo.getSelectionIndex( )] );
				input.setDataType( getDataTypeNames( )[typeCombo.getSelectionIndex( )] );
				input.setMeasureExpression( expressionText.getText( ) );
				result = input;
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
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
		nameLabel.setText( Messages.getString( "MeasureDialog.Label.Name" ) );
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
		functionLabel.setText( Messages.getString( "MeasureDialog.Label.Function" ) );
		functionCombo = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		functionCombo.setLayoutData( gd );
		functionCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		Label typeLabel = new Label( group, SWT.NONE );
		typeLabel.setText( Messages.getString( "MeasureDialog.Label.DataType" ) );
		typeCombo = new Combo( group, SWT.BORDER | SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		typeCombo.setLayoutData( gd );
		typeCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkOkButtonStatus( );
			}

		} );

		Label expressionLabel = new Label( group, SWT.NONE );
		expressionLabel.setText( Messages.getString( "MeasureDialog.Label.Expression" ) );
		expressionText = new Text( group, SWT.SINGLE | SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		expressionText.setLayoutData( gd );
		expressionText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkOkButtonStatus( );

			}

		} );

		Button expressionButton = new Button( group, SWT.PUSH );
		UIUtil.setExpressionButtonImage( expressionButton );
		expressionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				openExpression( );
			}
		} );

		return group;
	}

	protected void checkOkButtonStatus( )
	{
		if ( nameText.getText( ) == null
				|| nameText.getText( ).trim( ).equals( "" )
				|| functionCombo.getSelectionIndex( ) == -1
				|| typeCombo.getSelectionIndex( ) == -1
				|| expressionText.getText( ) == null
				|| expressionText.getText( ).trim( ).equals( "" ) )
		{
			if ( getOkButton( ) != null )
				getOkButton( ).setEnabled( false );
		}
		else
		{
			if ( getOkButton( ) != null )
				getOkButton( ).setEnabled( true );
		}

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

}
