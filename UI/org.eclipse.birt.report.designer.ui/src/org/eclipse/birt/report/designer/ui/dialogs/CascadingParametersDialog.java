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

package org.eclipse.birt.report.designer.ui.dialogs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * Cascading Parameter Dialog.
 */

public class CascadingParametersDialog extends BaseDialog
{

	private static final String LABEL_PROMPT_TEXT = Messages.getString( "CascadingParametersDialog.Label.promptText" ); //$NON-NLS-1$

	private static final String LABEL_VALUES = Messages.getString( "CascadingParametersDialog.Label.values" ); //$NON-NLS-1$

	private static final String LABEL_LIST_LIMIT = Messages.getString( "CascadingParametersDialog.Label.listLimit" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_GENERAL = Messages.getString( "CascadingParametersDialog.Label.group.general" ); //$NON-NLS-1$

	private static final String LABEL_CASCADING_PARAMETER_NAME = Messages.getString( "CascadingParametersDialog.Label.cascadingParam.name" ); //$NON-NLS-1$

	// private static final String LABEL_GROUP_PROMPT_TEXT = Messages.getString(
	// "CascadingParametersDialog.Label.promptText" ); //$NON-NLS-1$

	// private static final String LABEL_DATA_SETS = Messages.getString(
	// "CascadingParametersDialog.Label.dataSets" ); //$NON-NLS-1$

	// private static final String LABEL_BUTTON_CREATE_NEW_DATASET =
	// Messages.getString(
	// "CascadingParametersDialog.Label.button.createNew.dataset" );
	// //$NON-NLS-1$

	private static final String LABEL_PARAMETERS = Messages.getString( "CascadingParametersDialog.Label.parameters" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_PROPERTIES = Messages.getString( "CascadingParametersDialog.Label.group.properties" ); //$NON-NLS-1$

	private static final String LABEL_PARAM_NAME = Messages.getString( "CascadingParametersDialog.Label.param.name" ); //$NON-NLS-1$

	private static final String LABEL_DATA_TYPE = Messages.getString( "CascadingParametersDialog.Label.dataType" ); //$NON-NLS-1$

	private static final String LABEL_DISPLAY_TYPE = Messages.getString( "CascadingParametersDialog.Label.displayType" ); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages.getString( "CascadingParametersDialog.Label.defaultValue" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_MORE_OPTIONS = Messages.getString( "CascadingParametersDialog.Label.group.moreOptions" ); //$NON-NLS-1$

	private static final String LABEL_HELP_TEXT = Messages.getString( "CascadingParametersDialog.Label.helpText" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT_AS = Messages.getString( "CascadingParametersDialog.Label.formatAs" ); //$NON-NLS-1$

	private static final String LABEL_CHANGE_FORMAT_BUTTON = Messages.getString( "CascadingParametersDialog.Label.button.changeFormat" ); //$NON-NLS-1$

	private static final String LABEL_PREVIEW_WITH_FORMAT = Messages.getString( "CascadingParametersDialog.Label.preview" ); //$NON-NLS-1$

	private static final String LABEL_CREATE_NEW_PARAMETER = Messages.getString( "CascadingParametersDialog.Label.createNewParam" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DATA_SET = Messages.getString( "CascadingParametersDialog.Label.selectDataSet" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DISPLAY_COLUMN = Messages.getString( "CascadingParametersDialog.Label.selectDisplayColumn" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_VALUE_COLUMN = Messages.getString( "CascadingParametersDialog.Label.selectValueColumn" ); //$NON-NLS-1$

	private static final String LABEL_NO_COLUMN_AVAILABLE = Messages.getString( "CascadingParametersDialog.Label.NoColumnAvailable" ); //$NON-NLS-1$

	private static final String BUTTON_ALLOW_NULL_VALUE = Messages.getString( "CascadingParametersDialog.Button.AllowNull" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DATA_SET_MODE = Messages.getString( "CascadingParametersDialog.Label.SelectDataSetMode" ); //$NON-NLS-1$

	private static final String RADIO_SINGLE = Messages.getString( "CascadingParametersDialog.Radio.Single" ); //$NON-NLS-1$

	private static final String RADIO_MULTIPLE = Messages.getString( "CascadingParametersDialog.Radio.Mutli" ); //$NON-NLS-1$

	private static final String COLUMN_NAME = Messages.getString( "CascadingParametersDialog.Label.column.name" ); //$NON-NLS-1$

	private static final String COLUMN_DATA_SET = Messages.getString( "CascadingParametersDialog.Label.column.dataSet" ); //$NON-NLS-1$

	private static final String COLUMN_VALUE = Messages.getString( "CascadingParametersDialog.Label.column.value" ); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT = Messages.getString( "CascadingParametersDialog.Label.column.displayText" ); //$NON-NLS-1$

	private static final String PARAM_CONTROL_LIST = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
			+ "/List"; //$NON-NLS-1$

	private static final String PARAM_CONTROL_COMBO = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
			+ "/Combo"; //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_LIST = Messages.getString( "CascadingParametersDialog.display.controlType.listBox" ); //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_COMBO = Messages.getString( "CascadingParametersDialog.display.controlType.comboBox" ); //$NON-NLS-1$

	private static final double DEFAULT_PREVIEW_NUMBER = Double.parseDouble( "1234.56" ); //$NON-NLS-1$

	private static final String DEFAULT_PREVIEW_STRING = Messages.getString( "CascadingParametersDialog.default.preview.string" ); //$NON-NLS-1$

	private static final String ERROR_TITLE_INVALID_LIST_LIMIT = Messages.getString( "ParameterDialog.ErrorTitle.InvalidListLimit" ); //$NON-NLS-1$

	private static final String ERROR_MSG_INVALID_LIST_LIMIT = Messages.getString( "ParameterDialog.ErrorMessage.InvalidListLimit" ); //$NON-NLS-1$

	private Group optionsGroup;
	private Group propertiesGroup;

	private Text cascadingNameEditor;
	private Text promptTextEditor;
	private Text paramNameEditor;
	private Text helpTextEditor;
	private Text defaultValueEditor;
	private Text formatField;

	private Text listLimit;
	private Text promptText;

	private Combo dataTypeChooser;
	private Combo displayTypeChooser;

	private Button changeFormat;

	private Button singleDataSet, multiDataSet;

	private Label previewLable;

	private Table table;
	private TableViewer valueTable;
	private CellEditor[] cellEditors;

	private static IChoiceSet dataType = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_TYPE );

	private CascadingParameterGroupHandle inputParameterGroup;

	private ScalarParameterHandle selectedParameter;

	// private ArrayList inputParameters = new ArrayList( );

	protected ScalarParameterHandle newParameter = null;

	private String defaultValue;

	private String formatPattern;

	private String formatCategroy;

	private boolean loading = true;

	private Button allowNull;

	private int maxStrLengthProperty;

	private int maxStrLengthOption;

	private String PROPERTY_LABEL_STRING[] = {
			LABEL_PARAM_NAME,
			LABEL_PROMPT_TEXT,
			LABEL_DATA_TYPE,
			LABEL_DISPLAY_TYPE,
			LABEL_DEFAULT_VALUE
	};

	private String OPTION_LABEL_STRING[] = {
			LABEL_HELP_TEXT, LABEL_FORMAT_AS, LABEL_LIST_LIMIT
	};

	protected int getMaxStrLength( String string[], Control control )
	{
		int len = UIUtil.getMaxStringWidth( string, control );
		return len;
	}

	/**
	 * 
	 * Constructor.
	 * 
	 * @param parentShell
	 * @param title
	 */
	public CascadingParametersDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	/**
	 * Constructor.
	 * 
	 * @param title
	 */
	public CascadingParametersDialog( String title )
	{
		super( title );
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		GridData data = new GridData( );
		data.widthHint = 600;

		maxStrLengthProperty = getMaxStrLength( PROPERTY_LABEL_STRING,
				composite );

		maxStrLengthOption = getMaxStrLength( OPTION_LABEL_STRING, composite );

		composite.setLayoutData( data );

		createGeneralPart( composite );

		createChoicePart( composite );

		createDynamicParamsPart( composite );

		createPropertiesPart( composite );

		createOptionsPart( composite );

		return composite;
	}

	private void createGeneralPart( Composite parent )
	{
		Group group = new Group( parent, SWT.NULL );
		group.setText( LABEL_GROUP_GENERAL );
		group.setLayout( new GridLayout( 2, false ) );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( group, SWT.NULL ).setText( LABEL_CASCADING_PARAMETER_NAME );

		cascadingNameEditor = new Text( group, SWT.BORDER );
		cascadingNameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( group, SWT.NULL ).setText( LABEL_PROMPT_TEXT );
		promptTextEditor = new Text( group, SWT.BORDER );
		promptTextEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

	}

	private void createChoicePart( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( new GridLayout( 3, true ) );

		new Label( composite, SWT.NONE ).setText( LABEL_SELECT_DATA_SET_MODE );

		singleDataSet = new Button( composite, SWT.RADIO );
		singleDataSet.setText( RADIO_SINGLE );
		singleDataSet.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshValueTable( );
				updateButtons( );
			}
		} );

		multiDataSet = new Button( composite, SWT.RADIO );
		multiDataSet.setText( RADIO_MULTIPLE );
		multiDataSet.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				refreshValueTable( );
				updateButtons( );
			}
		} );

	}

	private void createDynamicParamsPart( Composite parent )
	{
		Composite comp = new Composite( parent, SWT.NULL );
		comp.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		comp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label label = new Label( comp, SWT.NULL );
		label.setText( LABEL_PARAMETERS );

		table = new Table( comp, SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION
				| SWT.BORDER );

		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 100;
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		table.addKeyListener( new KeyAdapter( ) {

			public void keyReleased( KeyEvent e )
			{
				// If Delete pressed, delete the selected row
				if ( e.keyCode == SWT.DEL )
				{
					deleteRow( );
				}
			}
		} );

		int[] columnWidths = new int[]{
				160, 115, 135, 145,
		};
		String[] columns = new String[]{
				COLUMN_NAME, COLUMN_DATA_SET, COLUMN_VALUE, COLUMN_DISPLAY_TEXT
		};

		cellEditors = new CellEditor[]{
				new TextCellEditor( table ),
				new ComboBoxCellEditor( table,
						ChoiceSetFactory.getDataSets( ),
						SWT.READ_ONLY ),
				new ComboBoxCellEditor( table, new String[0], SWT.READ_ONLY ),
				new ComboBoxCellEditor( table, new String[0], SWT.READ_ONLY ),
		};

		for ( int i = 0; i < columns.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setResizable( true );
			column.setText( columns[i] );
			column.setWidth( columnWidths[i] );
		}
		table.setLayoutData( data );

		valueTable = new TableViewer( table );

		valueTable.setCellEditors( cellEditors );
		valueTable.setColumnProperties( columns );
		valueTable.setContentProvider( contentProvider );
		valueTable.setLabelProvider( labelProvider );
		valueTable.setCellModifier( cellModifier );

		valueTable.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				ISelection selection = event.getSelection( );
				Object param = ( (StructuredSelection) selection ).getFirstElement( );
				if ( param != selectedParameter )
				{
					if ( param instanceof ScalarParameterHandle )
					{
						try
						{
							saveParameterProperties( );
							selectedParameter = (ScalarParameterHandle) param;
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
							valueTable.setSelection( new StructuredSelection( selectedParameter ) );
						}
						refreshParameterProperties( );
						updateButtons( );
					}
				}
			}
		} );
	}

	private void createPropertiesPart( Composite parent )
	{
		propertiesGroup = new Group( parent, SWT.NULL );
		propertiesGroup.setText( LABEL_GROUP_PROPERTIES );
		propertiesGroup.setLayout( new GridLayout( 2, false ) );
		propertiesGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( propertiesGroup, LABEL_PARAM_NAME, maxStrLengthProperty );

		paramNameEditor = new Text( propertiesGroup, SWT.BORDER );
		paramNameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		paramNameEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				valueTable.refresh( selectedParameter );
			}

		} );

		createLabel( propertiesGroup, LABEL_PROMPT_TEXT, maxStrLengthProperty );

		promptText = new Text( propertiesGroup, SWT.BORDER );
		promptText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( propertiesGroup, LABEL_DATA_TYPE, maxStrLengthProperty );
		dataTypeChooser = new Combo( propertiesGroup, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		dataTypeChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		dataTypeChooser.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( dataType ) );
		dataTypeChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( selectedParameter != null
						&& selectedParameter != newParameter )
				{
					changeDataType( dataType.findChoiceByDisplayName( dataTypeChooser.getText( ) )
							.getName( ) );
					try
					{
						selectedParameter.setDataType( dataType.findChoiceByDisplayName( dataTypeChooser.getText( ) )
								.getName( ) );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}
			}
		} );

		createLabel( propertiesGroup, LABEL_DISPLAY_TYPE, maxStrLengthProperty );
		displayTypeChooser = new Combo( propertiesGroup, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		displayTypeChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		displayTypeChooser.setItems( new String[]{
				DISPLAY_NAME_CONTROL_LIST, DISPLAY_NAME_CONTROL_COMBO
		} );
		displayTypeChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( selectedParameter != null
						&& selectedParameter != newParameter )
				{
					try
					{
						String newControlType = getSelectedDisplayType( );
						if ( PARAM_CONTROL_COMBO.equals( newControlType ) )
						{
							newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
							selectedParameter.setMustMatch( true );
						}
						else if ( PARAM_CONTROL_LIST.equals( newControlType ) )
						{
							newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
							selectedParameter.setMustMatch( false );
						}
						else
						{
							selectedParameter.setProperty( ScalarParameterHandle.MUCH_MATCH_PROP,
									null );
						}
						selectedParameter.setControlType( newControlType );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}
			}
		} );

		createLabel( propertiesGroup, LABEL_DEFAULT_VALUE, maxStrLengthProperty );
		defaultValueEditor = new Text( propertiesGroup, SWT.BORDER );
		defaultValueEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

	}

	private void createOptionsPart( Composite parent )
	{
		optionsGroup = new Group( parent, SWT.NULL );
		optionsGroup.setText( LABEL_GROUP_MORE_OPTIONS );
		optionsGroup.setLayout( new GridLayout( 2, false ) );
		optionsGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( optionsGroup, LABEL_HELP_TEXT, maxStrLengthOption );

		helpTextEditor = new Text( optionsGroup, SWT.BORDER );
		helpTextEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Label lable = new Label( optionsGroup, SWT.NULL );
		lable.setText( LABEL_FORMAT_AS );
		lable.setLayoutData( new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING ) );

		Composite formatArea = new Composite( optionsGroup, SWT.NONE );
		formatArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		formatArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatField = new Text( formatArea, SWT.BORDER
				| SWT.SINGLE
				| SWT.READ_ONLY );
		formatField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		changeFormat = new Button( formatArea, SWT.PUSH );
		changeFormat.setText( LABEL_CHANGE_FORMAT_BUTTON );
		setButtonLayoutData( changeFormat );
		changeFormat.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				popupFormatBuilder( true );
			}

		} );

		Group preview = new Group( formatArea, SWT.NULL );
		preview.setText( LABEL_PREVIEW_WITH_FORMAT );
		preview.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		preview.setLayout( new GridLayout( ) );
		previewLable = new Label( preview, SWT.CENTER
				| SWT.HORIZONTAL
				| SWT.VIRTUAL );
		previewLable.setText( "" ); //$NON-NLS-1$
		previewLable.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( optionsGroup, LABEL_LIST_LIMIT, maxStrLengthOption );

		Composite composite = new Composite( optionsGroup, SWT.NULL );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite limitArea = new Composite( composite, SWT.NULL );
		limitArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		limitArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		listLimit = new Text( limitArea, SWT.BORDER );
		GridData gridData = new GridData( );
		gridData.widthHint = 80;
		listLimit.setLayoutData( gridData );

		listLimit.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = ( "0123456789\0\b\u007f".indexOf( e.character ) != -1 ); //$NON-NLS-1$
			}
		} );
		listLimit.addModifyListener( new ModifyListener( ) {

			private String oldValue = ""; //$NON-NLS-1$

			public void modifyText( ModifyEvent e )
			{
				try
				{
					if ( !StringUtil.isBlank( listLimit.getText( ) ) )
					{
						Integer.parseInt( listLimit.getText( ) );
						oldValue = listLimit.getText( );
					}
				}
				catch ( NumberFormatException e1 )
				{
					ExceptionHandler.openErrorMessageBox( ERROR_TITLE_INVALID_LIST_LIMIT,
							MessageFormat.format( ERROR_MSG_INVALID_LIST_LIMIT,
									new Object[]{
										Integer.toString( Integer.MAX_VALUE )
									} ) );
					listLimit.setText( oldValue );
				}
			}
		} );
		new Label( limitArea, SWT.NONE ).setText( LABEL_VALUES );

		allowNull = new Button( composite, SWT.CHECK );
		allowNull.setText( BUTTON_ALLOW_NULL_VALUE );
		allowNull.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		allowNull.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( selectedParameter != null
						&& selectedParameter != newParameter )
				{
					try
					{
						selectedParameter.setAllowNull( allowNull.getSelection( ) );
					}
					catch ( SemanticException e1 )
					{
						ExceptionHandler.handle( e1 );
					}
				}

			}

		} );
	}

	private void createLabel( Composite parent, String content, int width )
	{
		Label label = new Label( parent, SWT.NONE );
		setLabelLayoutData( label, width );
		if ( content != null )
		{
			label.setText( content );
		}
	}

	private void setLabelLayoutData( Control control, int width )
	{
		GridData gd = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		gd.widthHint = width;
		control.setLayoutData( gd );
	}

	// set input for dialog
	public void setInput( Object input )
	{
		Assert.isNotNull( input );
		Assert.isLegal( input instanceof CascadingParameterGroupHandle );
		inputParameterGroup = (CascadingParameterGroupHandle) input;
	}

	// initiate dialog
	protected boolean initDialog( )
	{
		cascadingNameEditor.setText( inputParameterGroup.getName( ) );
		promptTextEditor.setText( UIUtil.convertToGUIString( inputParameterGroup.getPromptText( ) ) );

		if ( DesignChoiceConstants.DATA_SET_MODE_MULTIPLE.equals( inputParameterGroup.getDataSetMode( ) ) )
		{
			multiDataSet.setSelection( true );
		}
		else
		{
			singleDataSet.setSelection( true );
		}

		valueTable.setInput( inputParameterGroup );

		refreshParameterProperties( );
		updateButtons( );
		return true;
	}

	// ok pressed
	protected void okPressed( )
	{
		try
		{
			saveParameterProperties( );
			inputParameterGroup.setName( UIUtil.convertToModelString( cascadingNameEditor.getText( ),
					true ) );
			inputParameterGroup.setPromptText( promptTextEditor.getText( ) );

			if ( isSingle( ) )
			{
				inputParameterGroup.setDataSetMode( DesignChoiceConstants.DATA_SET_MODE_SINGLE );
			}
			else
			{
				inputParameterGroup.setDataSetMode( DesignChoiceConstants.DATA_SET_MODE_MULTIPLE );
			}
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			refreshParameterProperties( );
			return;
		}
		setResult( inputParameterGroup );
		super.okPressed( );

	}

	private void deleteRow( )
	{
		int index = valueTable.getTable( ).getSelectionIndex( );
		ScalarParameterHandle choice = (ScalarParameterHandle) ( (IStructuredSelection) valueTable.getSelection( ) ).getFirstElement( );

		if ( choice == null )
		{
			return;
		}
		try
		{
			inputParameterGroup.getParameters( ).drop( choice );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
			return;
		}
		refreshValueTable( );

		index--;
		if ( index < 0 && valueTable.getTable( ).getItemCount( ) > 1 )
		{
			index = 0;
		}
		StructuredSelection selection = null;
		if ( index != -1 )
		{
			selection = new StructuredSelection( valueTable.getTable( )
					.getItem( index )
					.getData( ) );
		}
		else
		{
			selection = StructuredSelection.EMPTY;
		}
		valueTable.setSelection( selection );
	}

	private String[] getDataSetColumns( ScalarParameterHandle handle,
			boolean needFilter )
	{
		DataSetHandle dataSet = getDataSet( handle );
		if ( dataSet == null )
		{
			return new String[0];
		}
		CachedMetaDataHandle metaHandle = dataSet.getCachedMetaDataHandle( );
		if ( metaHandle == null )
		{
			try
			{
				metaHandle = DataSetUIUtil.getCachedMetaDataHandle( dataSet );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
				return new String[0];
			}
		}
		if ( metaHandle == null || metaHandle.getResultSet( ) == null )
		{
			return new String[0];
		}
		ArrayList valueList = new ArrayList( );
		for ( Iterator iter = metaHandle.getResultSet( ).iterator( ); iter.hasNext( ); )
		{
			ResultSetColumnHandle columnHandle = (ResultSetColumnHandle) iter.next( );
			if ( !needFilter || matchDataType( handle, columnHandle ) )
			{
				valueList.add( columnHandle.getColumnName( ) );
			}
		}
		return (String[]) valueList.toArray( new String[0] );
	}

	private DataSetHandle getDataSet( ScalarParameterHandle handle )
	{
		if ( !isSingle( ) && handle.getDataSetName( ) != null )
		{
			return getDataSet( handle.getDataSetName( ) );
		}
		return inputParameterGroup.getDataSet( );
	}

	private DataSetHandle getDataSet( String name )
	{
		return inputParameterGroup.getModuleHandle( ).findDataSet( name );
	}

	private void setCellEditorItems( )
	{
		( (ComboBoxCellEditor) cellEditors[2] ).setItems( getDataSetColumns( selectedParameter,
				true ) );
		( (ComboBoxCellEditor) cellEditors[3] ).setItems( getDataSetColumns( selectedParameter,
				false ) );
	}

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public Object[] getElements( Object inputElement )
		{
			ArrayList elementsList = new ArrayList( inputParameterGroup.getParameters( )
					.getContents( ) );
			for ( Iterator iter = elementsList.iterator( ); iter.hasNext( ); )
			{
				ScalarParameterHandle handle = (ScalarParameterHandle) iter.next( );

				String[] columns = getDataSetColumns( handle, false );
				boolean found = false;
				for ( int i = 0; i < columns.length; i++ )
				{
					if ( DEUtil.getColumnExpression( columns[i] )
							.equals( handle.getValueExpr( ) ) )
					{
						found = true;
						break;
					}
				}
				if ( !found )
				{
					try
					{
						handle.setValueExpr( null );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
					}
				}
			}
			if ( newParameter == null )
			{
				newParameter = DesignElementFactory.getInstance( )
						.newScalarParameter( null ); //$NON-NLS-1$
				try
				{
					newParameter.setControlType( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX );
					newParameter.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			elementsList.add( newParameter );
			return elementsList.toArray( );
		}

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			String value = null;
			ScalarParameterHandle paramHandle = null;

			if ( element instanceof ScalarParameterHandle )
			{
				paramHandle = (ScalarParameterHandle) element;
			}

			if ( paramHandle == newParameter )
			{
				if ( columnIndex == 0 )
				{
					value = LABEL_CREATE_NEW_PARAMETER;
				}
				else if ( columnIndex == 1 )
				{
					if ( !isSingle( ) || paramHandle == getFirstParameter( ) )
					{
						value = LABEL_SELECT_DATA_SET;
					}
				}
				else if ( columnIndex == 2 )
				{
					if ( isSingle( ) && paramHandle != getFirstParameter( ) )
					{
						value = LABEL_SELECT_VALUE_COLUMN;
					}
				}
			}
			else
			{
				switch ( columnIndex )
				{
					case 0 :
					{
						if ( paramHandle != newParameter )
						{
							String paramName;
							if ( paramHandle != selectedParameter )
							{
								paramName = paramHandle.getName( );
							}
							else
							{
								paramName = paramNameEditor.getText( ).trim( );
							}
							value = getDummyText( paramHandle ) + paramName;
						}
						break;
					}
					case 1 :
					{
						DataSetHandle dataSet = null;
						if ( isSingle( ) )
						{
							if ( paramHandle != getFirstParameter( ) )
							{
								break;
							}
							dataSet = inputParameterGroup.getDataSet( );
						}
						else
						{
							dataSet = getDataSet( paramHandle );
						}
						if ( dataSet == null )
						{
							value = LABEL_SELECT_DATA_SET;
						}
						else
						{
							value = dataSet.getName( );
						}
						break;
					}
					case 2 :
					{
						if ( paramHandle.getValueExpr( ) != null )
						{
							value = getColumnName( paramHandle, COLUMN_VALUE );
						}
						else if ( getDataSetColumns( paramHandle, true ).length > 0 )
						{
							value = LABEL_SELECT_VALUE_COLUMN;
						}
						else
						{
							value = LABEL_NO_COLUMN_AVAILABLE;
						}
						break;
					}
					case 3 :
					{
						value = getColumnName( paramHandle, COLUMN_DISPLAY_TEXT );
						if ( value == null )
						{
							if ( getDataSetColumns( paramHandle, false ).length > 0 )
							{
								value = LABEL_SELECT_DISPLAY_COLUMN;
							}
							else
							{
								value = LABEL_NO_COLUMN_AVAILABLE;
							}

						}
						break;
					}
				}
			}

			if ( value == null )
			{
				value = ""; //$NON-NLS-1$
			}
			return value;
		}

		private String getDummyText( Object element )
		{
			String dummyText = ""; //$NON-NLS-1$
			int index = getTableIndex( element );
			for ( int i = 0; i < index; i++ )
			{
				dummyText += "    "; //$NON-NLS-1$
			}

			return dummyText;
		}

		public void addListener( ILabelProviderListener listener )
		{
		}

		public void dispose( )
		{
		}

		public boolean isLabelProperty( Object element, String property )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
		}

	};

	private ICellModifier cellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( element != selectedParameter || property.equals( COLUMN_NAME ) )
			{
				return false;
			}
			if ( COLUMN_DATA_SET.equals( property ) )
			{
				if ( isSingle( ) && getFirstParameter( ) != element )
				{
					return false;
				}
			}
			if ( element == newParameter )
			{
				if ( isSingle( ) )
				{
					if ( element == getFirstParameter( ) )
					{
						if ( !COLUMN_DATA_SET.equals( property ) )
						{
							return false;
						}
					}
					else
					{
						if ( !COLUMN_VALUE.equals( property ) )
						{
							return false;
						}
					}
				}
				else if ( !COLUMN_DATA_SET.equals( property ) )
				{
					return false;
				}
			}
			if ( COLUMN_VALUE.equals( property ) )
			{
				if ( getDataSetColumns( (ScalarParameterHandle) element, true ).length == 0 )
				{
					return false;
				}
			}
			setCellEditorItems( );
			return true;
		}

		public Object getValue( Object element, String property )
		{
			if ( element instanceof ScalarParameterHandle )
			{
				ScalarParameterHandle parameter = (ScalarParameterHandle) element;
				String value = null;

				if ( COLUMN_NAME.equals( property ) )
				{
					if ( element == newParameter )
					{
						value = ""; //$NON-NLS-1$
					}
					else
					{
						value = parameter.getName( );
					}
				}
				else if ( COLUMN_DATA_SET.equals( property ) )
				{
					if ( isSingle( )
							&& inputParameterGroup.getDataSet( ) != null )
					{
						value = inputParameterGroup.getDataSet( ).getName( );
					}
					else if ( getDataSet( parameter ) != null )
					{
						value = getDataSet( parameter ).getName( );
					}
				}
				else if ( COLUMN_VALUE.equals( property ) )
				{
					value = getColumnName( parameter, COLUMN_VALUE );

				}
				else if ( COLUMN_DISPLAY_TEXT.equals( property ) )
				{
					value = getColumnName( parameter, COLUMN_DISPLAY_TEXT );
				}

				if ( value == null )
				{
					value = ""; //$NON-NLS-1$
				}
				return value;
			}
			return ""; //$NON-NLS-1$

		}

		public void modify( Object element, String property, Object value )
		{
			Object actualElement = ( (TableItem) element ).getData( );

			try
			{
				saveParameterProperties( );
			}
			catch ( SemanticException e1 )
			{
				e1.printStackTrace( );
			}
			if ( COLUMN_NAME.equals( property ) )
			{
				try
				{
					if ( actualElement == newParameter )
					{
						( (ScalarParameterHandle) actualElement ).setName( newParameter.getName( ) );
					}
					else
					{
						( (ScalarParameterHandle) actualElement ).setName( (String) value );
					}
				}
				catch ( NameException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			if ( COLUMN_DATA_SET.equals( property ) )
			{
				try
				{
					if ( isSingle( ) )
					{
						inputParameterGroup.setDataSet( getDataSet( (String) value ) );
					}
					else
					{
						( (ScalarParameterHandle) actualElement ).setDataSetName( (String) value );
					}
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else if ( COLUMN_VALUE.equals( property ) )
			{
				try
				{
					( (ScalarParameterHandle) actualElement ).setValueExpr( DEUtil.getColumnExpression( (String) value ) );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
			else if ( COLUMN_DISPLAY_TEXT.equals( property ) )
			{
				try
				{
					( (ScalarParameterHandle) actualElement ).setLabelExpr( DEUtil.getColumnExpression( (String) value ) );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}

			if ( actualElement == newParameter )
			{
				if ( isSingle( ) || newParameter.getDataSetName( ) != null )
				{
					try
					{
						inputParameterGroup.getParameters( ).add( newParameter );
					}
					catch ( SemanticException e )
					{
						ExceptionHandler.handle( e );
						return;
					}
					clearNewParameter( );
				}
			}
			refreshValueTable( );
			refreshParameterProperties( );
			updateButtons( );
		}
	};

	private void clearNewParameter( )
	{
		newParameter = null;
	}

	protected int getTableIndex( Object element )
	{
		Object[] input = ( (IStructuredContentProvider) valueTable.getContentProvider( ) ).getElements( valueTable.getInput( ) );

		int index = 0;
		if ( element != newParameter )
		{
			for ( int i = 0; i < input.length; i++ )
			{
				if ( element == input[i] )
				{
					index = i;
					break;
				}
			}
		}
		return index;
	}

	private void refreshValueTable( )
	{
		if ( valueTable != null && !valueTable.getTable( ).isDisposed( ) )
		{
			valueTable.refresh( );
		}
	}

	private void refreshParameterProperties( )
	{
		if ( selectedParameter == null || selectedParameter == newParameter )
		{
			clearParamProperties( );
			setControlEnabled( false );
			return;
		}
		loading = true;

		setControlEnabled( true );

		paramNameEditor.setText( selectedParameter.getName( ) );

		if ( selectedParameter.getPromptText( ) == null )
		{
			promptText.setText( "" ); //$NON-NLS-1$
		}
		else
		{
			promptText.setText( selectedParameter.getPromptText( ) );
		}

		dataTypeChooser.setText( dataType.findChoice( selectedParameter.getDataType( ) )
				.getDisplayName( ) );

		if ( getInputDisplayName( ) == null )
		{
			displayTypeChooser.clearSelection( ); //$NON-NLS-1$
		}
		else
		{
			displayTypeChooser.setText( getInputDisplayName( ) );
		}

		defaultValue = selectedParameter.getDefaultValue( );

		if ( defaultValue == null )
		{
			defaultValueEditor.setText( "" ); //$NON-NLS-1$			
		}
		else
		{
			defaultValueEditor.setText( defaultValue );
		}

		helpTextEditor.setText( UIUtil.convertToGUIString( selectedParameter.getHelpText( ) ) );

		if ( selectedParameter.getPropertyHandle( ScalarParameterHandle.LIST_LIMIT_PROP )
				.isSet( ) )
		{
			listLimit.setText( String.valueOf( selectedParameter.getListlimit( ) ) );
		}
		else
		{
			listLimit.setText( "" ); //$NON-NLS-1$
		}

		allowNull.setSelection( selectedParameter.allowNull( ) );

		changeDataType( selectedParameter.getDataType( ) );

		loading = false;
	}

	private void clearParamProperties( )
	{
		paramNameEditor.setText( "" ); //$NON-NLS-1$
		promptText.setText( "" ); //$NON-NLS-1$
		dataTypeChooser.select( -1 );
		displayTypeChooser.select( -1 );
		defaultValueEditor.setText( "" ); //$NON-NLS-1$
		helpTextEditor.setText( "" ); //$NON-NLS-1$
		formatField.setText( "" ); //$NON-NLS-1$
		listLimit.setText( "" ); //$NON-NLS-1$

		previewLable.setText( "" ); //$NON-NLS-1$
		allowNull.setSelection( false );
	}

	private void setControlEnabled( boolean enable )
	{
		paramNameEditor.setEnabled( enable );
		promptText.setEnabled( enable );
		dataTypeChooser.setEnabled( enable );
		displayTypeChooser.setEnabled( enable );
		defaultValueEditor.setEnabled( enable );
		helpTextEditor.setEnabled( enable );
		formatField.setEnabled( enable );
		listLimit.setEnabled( enable );
		changeFormat.setEnabled( enable );
	}

	private void changeDataType( String type )
	{
		initFormatField( type );
		refreshValueTable( );
		updateButtons( );
	}

	private void initFormatField( String selectedDataType )
	{
		IChoiceSet choiceSet = getFormatChoiceSet( selectedDataType );
		if ( choiceSet == null )
		{
			formatCategroy = formatPattern = null;
		}
		else
		{
			if ( !loading
					|| ( selectedParameter.getCategory( ) == null && selectedParameter.getPattern( ) == null ) )
			{
				if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( selectedDataType ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( selectedDataType ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( selectedDataType )
						|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( selectedDataType ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				formatPattern = null;
			}
			else
			{
				formatCategroy = selectedParameter.getCategory( );
				if ( formatCategroy == null )
				{// back compatible
					formatCategroy = DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;
				}
				formatPattern = selectedParameter.getPattern( );
			}
		}
		updateFormatField( );
	}

	private String getInputDisplayName( )
	{
		String displayName = null;
		if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equals( selectedParameter.getControlType( ) ) )
		{
			if ( selectedParameter.isMustMatch( ) )
			{
				displayName = DISPLAY_NAME_CONTROL_COMBO;
			}
			else
			{
				displayName = DISPLAY_NAME_CONTROL_LIST;
			}
		}
		return displayName;
	}

	private IChoiceSet getFormatChoiceSet( String type )
	{
		IChoiceSet choiceSet = null;
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			choiceSet = DesignEngine.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			choiceSet = DesignEngine.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_DATETIME_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			choiceSet = DesignEngine.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_NUMBER_FORMAT_TYPE );
		}
		return choiceSet;
	}

	private String getSelectedDataType( )
	{
		String type = null;
		if ( StringUtil.isBlank( dataTypeChooser.getText( ) ) )
		{
			if ( selectedParameter != null )
			{
				type = selectedParameter.getDataType( );
			}
			else
			{
				type = DesignChoiceConstants.PARAM_TYPE_STRING;
			}
		}
		else
		{
			IChoice choice = dataType.findChoiceByDisplayName( dataTypeChooser.getText( ) );
			type = choice.getName( );
		}
		return type;
	}

	/**
	 * Gets the internal name of the control type from the display name
	 */
	private String getSelectedDisplayType( )
	{
		String displayText = displayTypeChooser.getText( );
		if ( displayText.length( ) == 0 )
		{
			return null;
		}
		if ( DISPLAY_NAME_CONTROL_COMBO.equals( displayText ) )
		{
			return PARAM_CONTROL_COMBO;
		}
		if ( DISPLAY_NAME_CONTROL_LIST.equals( displayText ) )
		{
			return PARAM_CONTROL_LIST;
		}
		return null;
	}

	private void popupFormatBuilder( boolean refresh )
	{
		String type = getSelectedDataType( );
		int style;
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			return;
		}
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			style = FormatBuilder.STRING;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			style = FormatBuilder.DATETIME;
		}
		else
		{
			style = FormatBuilder.NUMBER;
		}
		FormatBuilder formatBuilder = new FormatBuilder( style );
		formatBuilder.setInputFormat( formatCategroy, formatPattern );
		// formatBuilder.setPreviewText( defaultValue );
		if ( formatBuilder.open( ) == OK )
		{
			formatCategroy = ( (String[]) formatBuilder.getResult( ) )[0];
			formatPattern = ( (String[]) formatBuilder.getResult( ) )[1];
			updateFormatField( );
		}
	}

	private void updateFormatField( )
	{
		String displayFormat;
		IChoiceSet choiceSet = getFormatChoiceSet( getSelectedDataType( ) );
		if ( choiceSet == null )
		{// Boolean type;
			displayFormat = DesignEngine.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE )
					.findChoice( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
					.getDisplayName( );
		}
		else
		{
			displayFormat = choiceSet.findChoice( formatCategroy )
					.getDisplayName( );
			if ( formatCategroy != DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM )
			{
				displayFormat += ":  " + formatPattern; //$NON-NLS-1$
			}
		}
		formatField.setText( "" + displayFormat ); //$NON-NLS-1$
		changeFormat.setEnabled( choiceSet != null );

		if ( selectedParameter != null )
		{
			doPreview( formatCategroy != DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM ? formatCategroy
					: formatPattern );
		}
	}

	private void doPreview( String pattern )
	{
		String type = getSelectedDataType( );

		String formatStr = ""; //$NON-NLS-1$
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			formatStr = new StringFormatter( pattern, ULocale.getDefault( ) ).format( DEFAULT_PREVIEW_STRING );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			formatStr = new DateFormatter( pattern ).format( new Date( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			formatStr = new NumberFormatter( pattern ).format( DEFAULT_PREVIEW_NUMBER );
		}

		previewLable.setText( formatStr );
	}

	private String getCategroy( String formatString )
	{
		if ( formatString != null )
		{
			return formatString.split( ":" )[0]; // NON-NLS-1$ //$NON-NLS-1$
		}
		return null;
	}

	private String getPattern( String formatString )
	{
		if ( formatString != null )
		{
			int index = formatString.indexOf( ':' ); // NON-NLS-1$
			if ( index == -1 )
			{
				return null;
			}
			return formatString.substring( index + 1 );
		}
		return null;
	}

	private String getColumnName( ScalarParameterHandle handle, String column )
	{
		CachedMetaDataHandle cmdh = null;
		try
		{
			DataSetHandle dataSet = getDataSet( handle );
			if ( dataSet == null )
			{
				return null;
			}
			cmdh = DataSetUIUtil.getCachedMetaDataHandle( dataSet );
		}
		catch ( SemanticException e )
		{
		}
		String value = null;
		if ( COLUMN_VALUE.equals( column ) )
		{
			value = handle.getValueExpr( );
		}
		else
		{
			value = handle.getLabelExpr( );
		}
		if ( cmdh != null )
		{
			for ( Iterator iter = cmdh.getResultSet( ).iterator( ); iter.hasNext( ); )
			{
				ResultSetColumnHandle element = (ResultSetColumnHandle) iter.next( );
				if ( DEUtil.getColumnExpression( element.getColumnName( ) )
						.equalsIgnoreCase( value ) )
				{
					return element.getColumnName( );
				}
			}
		}

		return null;
	}

	private void updateButtons( )
	{
		boolean okEnable = true;

		Iterator iter = inputParameterGroup.getParameters( ).iterator( );
		if ( !iter.hasNext( ) )
		{
			okEnable = false;
		}
		else
		{
			int count = 0;
			while ( iter.hasNext( ) )
			{
				Object obj = iter.next( );
				if ( obj instanceof ScalarParameterHandle )
				{
					ScalarParameterHandle param = (ScalarParameterHandle) obj;
					count++;
					if ( !checkParameter( param ) )
					{
						okEnable = false;
						break;
					}
				}
			}
			okEnable &= ( count != 0 );
		}

		getOkButton( ).setEnabled( okEnable );

	}

	private boolean matchDataType( ScalarParameterHandle handle,
			ResultSetColumnHandle columnHandle )
	{
		String type = handle.getDataType( );
		if ( handle == selectedParameter )
		{
			type = getSelectedDataType( );
		}
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( columnHandle.getDataType( ) ) )
		{
			return true;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
		{
			return DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( columnHandle.getDataType( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals( columnHandle.getDataType( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) )
		{
			return DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( columnHandle.getDataType( ) );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
		{
			return DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( columnHandle.getDataType( ) );
		}
		return false;
	}

	private void saveParameterProperties( ) throws SemanticException
	{
		if ( selectedParameter != null && selectedParameter != newParameter )
		{
			selectedParameter.setPromptText( UIUtil.convertToModelString( promptText.getText( ),
					false ) );
			selectedParameter.setHelpText( UIUtil.convertToModelString( helpTextEditor.getText( ),
					true ) );
			selectedParameter.setDefaultValue( UIUtil.convertToModelString( defaultValueEditor.getText( ),
					true ) );
			if ( StringUtil.isBlank( listLimit.getText( ) ) )
			{
				selectedParameter.setProperty( ScalarParameterHandle.LIST_LIMIT_PROP,
						null );
			}
			else
			{
				selectedParameter.setListlimit( Integer.parseInt( listLimit.getText( ) ) );
			}
			selectedParameter.setAllowNull( allowNull.getSelection( ) );
			selectedParameter.setName( UIUtil.convertToModelString( paramNameEditor.getText( ),
					true ) );

			selectedParameter.setCategory( formatCategroy );
			selectedParameter.setPattern( formatPattern );
			refreshValueTable( );
		};
	}

	private boolean isSingle( )
	{
		return singleDataSet.getSelection( );
	}

	private ScalarParameterHandle getFirstParameter( )
	{
		if ( inputParameterGroup.getParameters( ).getCount( ) > 0 )
		{
			return (ScalarParameterHandle) inputParameterGroup.getParameters( )
					.get( 0 );
		}
		return newParameter;
	}

	private boolean checkParameter( ScalarParameterHandle paramHandle )
	{
		if ( paramHandle.getValueExpr( ) == null
				|| getColumnName( paramHandle, COLUMN_VALUE ) == null )
		{
			return false;
		}
		return true;
	}
}