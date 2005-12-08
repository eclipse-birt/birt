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
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.data.DataSetItemModel;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.DataSetManager;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ComboBoxCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CascadingParameterGroupHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
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

/**
 * Cascading Parameter Dialog.
 */

public class CascadingParametersDialog extends BaseDialog
{

	private static final String LABEL_PROMPT_TEXT = Messages.getString( "CascadingParametersDialog.label.promptText" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DISPLAY_COLUMN = Messages.getString( "CascadingParametersDialog.label.select.displayColumn" ); //$NON-NLS-1$

	private static final String LABEL_VALUES = Messages.getString( "CascadingParametersDialog.label.values" ); //$NON-NLS-1$

	private static final String LABEL_LIST_LIMIT = Messages.getString( "CascadingParametersDialog.label.listLimit" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_GENERAL = Messages.getString( "CascadingParametersDialog.label.group.general" ); //$NON-NLS-1$

	private static final String LABEL_CASCADING_PARAMETER_NAME = Messages.getString( "CascadingParametersDialog.label.cascadingParam.name" ); //$NON-NLS-1$

	private static final String LABEL_DATA_SETS = Messages.getString( "CascadingParametersDialog.label.dataSets" ); //$NON-NLS-1$

	private static final String LABEL_BUTTON_CREATE_NEW_DATASET = Messages.getString( "CascadingParametersDialog.label.button.createNew.dataset" ); //$NON-NLS-1$

	private static final String LABEL_PARAMETERS = Messages.getString( "CascadingParametersDialog.label.parameters" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_PROPERTIES = Messages.getString( "CascadingParametersDialog.label.group.properties" ); //$NON-NLS-1$

	private static final String LABEL_PARAM_NAME = Messages.getString( "CascadingParametersDialog.label.param.name" ); //$NON-NLS-1$

	private static final String LABEL_DATA_TYPE = Messages.getString( "CascadingParametersDialog.label.dataType" ); //$NON-NLS-1$

	private static final String LABEL_DISPLAY_TYPE = Messages.getString( "CascadingParametersDialog.label.displayType" ); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages.getString( "CascadingParametersDialog.label.defaultValue" ); //$NON-NLS-1$

	private static final String LABEL_GROUP_MORE_OPTIONS = Messages.getString( "CascadingParametersDialog.label.group.moreOptions" ); //$NON-NLS-1$

	private static final String LABEL_HELP_TEXT = Messages.getString( "CascadingParametersDialog.label.helpText" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT_AS = Messages.getString( "CascadingParametersDialog.label.formatAs" ); //$NON-NLS-1$

	private static final String LABEL_CHANGE_FORMAT_BUTTON = Messages.getString( "CascadingParametersDialog.label.button.changeFormat" ); //$NON-NLS-1$

	private static final String LABEL_PREVIEW_WITH_FORMAT = Messages.getString( "CascadingParametersDialog.label.preview" ); //$NON-NLS-1$

	private static final String LABEL_CREATE_NEW_PARAMETER = Messages.getString( "CascadingParametersDialog.label.createNewParam" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_A_VALUE_COLUMN = Messages.getString( "CascadingParametersDialog.label.selecteValueColumn" ); //$NON-NLS-1$

	private static final String LABEL_NO_COLUMN_AVAILABLE = Messages.getString( "CascadingParametersDialog.Label.NoColumnAvailable" ); //$NON-NLS-1$

	private static final String BUTTON_ALLOW_NULL_VALUE = Messages.getString( "CascadingParametersDialog.Button.AllowNull" ); //$NON-NLS-1$

	private static final String COLUMN_NAME = "Name"; //$NON-NLS-1$

	private static final String COLUMN_VALUE = "Value"; //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT = "Display Text"; //$NON-NLS-1$

	private static final String COLUMN_NAME_LABEL = Messages.getString( "CascadingParametersDialog.label.column.name" ); //$NON-NLS-1$

	private static final String COLUMN_VALUE_LABEL = Messages.getString( "CascadingParametersDialog.label.column.value" ); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT_LABEL = Messages.getString( "CascadingParametersDialog.label.column.displayText" ); //$NON-NLS-1$

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
	private Text paramNameEditor;
	private Text helpTextEditor;
	private Text defaultValueEditor;
	private Text formatField;

	private Text listLimit;
	private Text promptText;

	private Combo dataSetsCombo;
	private Combo dataTypeChooser;
	private Combo displayTypeChooser;

	private Button createDSButton;
	private Button changeFormat;

	private Label previewLable;

	private Table table;
	private TableViewer valueTable;
	private CellEditor[] cellEditors;

	private static IChoiceSet dataType = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_TYPE );

	private CascadingParameterGroupHandle inputParameterGroup;

	private DataSetHandle dataSet;

	private ScalarParameterHandle selectedParameter;

	// private ArrayList inputParameters = new ArrayList( );

	protected ScalarParameterHandle newParameter = null;

	private String defaultValue;

	private String formatPattern;

	private String formatCategroy;

	private boolean loading = true;

	private Button allowNull;

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
		data.widthHint = 550;
		composite.setLayoutData( data );

		createGeneralPart( composite );

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

		Label label = new Label( group, SWT.NULL );
		label.setText( LABEL_CASCADING_PARAMETER_NAME );

		cascadingNameEditor = new Text( group, SWT.BORDER );
		cascadingNameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	private void createDynamicParamsPart( Composite parent )
	{
		Composite comp = new Composite( parent, SWT.NULL );
		GridLayout layout = new GridLayout( 3, false );
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		comp.setLayout( layout );
		comp.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( comp, LABEL_DATA_SETS );

		dataSetsCombo = new Combo( comp, SWT.READ_ONLY );
		GridData data = new GridData( );
		data.widthHint = 120;
		dataSetsCombo.setLayoutData( data );
		dataSetsCombo.setItems( getDataSets( ) );
		dataSetsCombo.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				dataSet = getDataSet( dataSetsCombo.getText( ) );
				refreshValueTable( );
				updateButtons( );
			}
		} );

		createDSButton = new Button( comp, SWT.PUSH );
		createDSButton.setText( LABEL_BUTTON_CREATE_NEW_DATASET );
		createDSButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				new NewDataSetAction( ).run( );
				refreshDataSets( );
			}
		} );

		Label label = new Label( comp, SWT.NULL );
		label.setText( LABEL_PARAMETERS );
		data = new GridData( );
		data.horizontalSpan = 3;
		label.setLayoutData( data );

		table = new Table( comp, SWT.FULL_SELECTION
				| SWT.HIDE_SELECTION
				| SWT.BORDER );

		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 3;
		data.heightHint = 100;
		table.setLayoutData( data );
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
				180, 145, 145,
		};
		String[] columnProps = new String[]{
				COLUMN_NAME, COLUMN_VALUE, COLUMN_DISPLAY_TEXT
		};
		String[] columnLabels = new String[]{
				COLUMN_NAME_LABEL,
				COLUMN_VALUE_LABEL,
				COLUMN_DISPLAY_TEXT_LABEL
		};
		cellEditors = new CellEditor[]{
				new TextCellEditor( table ),
				new ComboBoxCellEditor( table, new String[0], SWT.READ_ONLY ),
				new ComboBoxCellEditor( table, new String[0], SWT.READ_ONLY ),
		};

		for ( int i = 0; i < columnProps.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setResizable( true );
			column.setText( columnLabels[i] );
			column.setWidth( columnWidths[i] );
		}

		valueTable = new TableViewer( table );

		valueTable.setCellEditors( cellEditors );
		valueTable.setColumnProperties( columnProps );
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

		createLabel( propertiesGroup, LABEL_PARAM_NAME );

		paramNameEditor = new Text( propertiesGroup, SWT.BORDER );
		paramNameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		paramNameEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				valueTable.refresh( selectedParameter );
			}

		} );

		createLabel( propertiesGroup, LABEL_PROMPT_TEXT );

		promptText = new Text( propertiesGroup, SWT.BORDER );
		promptText.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( propertiesGroup, LABEL_DATA_TYPE );
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

		createLabel( propertiesGroup, LABEL_DISPLAY_TYPE );
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

		createLabel( propertiesGroup, LABEL_DEFAULT_VALUE );
		defaultValueEditor = new Text( propertiesGroup, SWT.BORDER );
		defaultValueEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

	}

	private void createOptionsPart( Composite parent )
	{
		optionsGroup = new Group( parent, SWT.NULL );
		optionsGroup.setText( LABEL_GROUP_MORE_OPTIONS );
		optionsGroup.setLayout( new GridLayout( 2, false ) );
		optionsGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( optionsGroup, LABEL_HELP_TEXT );

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

		createLabel( optionsGroup, LABEL_LIST_LIMIT );

		Composite composite = new Composite( optionsGroup, SWT.NULL );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite limitArea = new Composite( composite, SWT.NULL );
		limitArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, true ) );
		limitArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		listLimit = new Text( limitArea, SWT.BORDER );
		listLimit.setLayoutData( new GridData( ) );

		listLimit.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = ( "0123456789\0\b\u007f".indexOf( e.character ) != -1 );
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

	private void createLabel( Composite parent, String content )
	{
		Label label = new Label( parent, SWT.NONE );
		setLabelLayoutData( label );
		if ( content != null )
		{
			label.setText( content );
		}
	}

	private void setLabelLayoutData( Control control )
	{
		GridData gd = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		gd.widthHint = 100;
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
		dataSet = inputParameterGroup.getDataSet( );

		if ( dataSet != null )
		{
			dataSetsCombo.setText( "" + dataSet.getName( ) ); //$NON-NLS-1$
		}
		else
		{
			dataSetsCombo.select( 0 );
			dataSet = getDataSet( dataSetsCombo.getText( ) );
		}
		cascadingNameEditor.setText( inputParameterGroup.getName( ) );

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
			inputParameterGroup.setName( cascadingNameEditor.getText( ) );
			inputParameterGroup.setDataSet( dataSet );
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

	private void refreshDataSets( )
	{
		String selectedDataSetName = dataSetsCombo.getText( );
		String[] oldList = dataSetsCombo.getItems( );
		String[] newDataSets = ChoiceSetFactory.getDataSets( );

		if ( !Arrays.asList( oldList ).equals( Arrays.asList( newDataSets ) ) )
		{
			dataSetsCombo.setItems( newDataSets );
			dataSetsCombo.setText( selectedDataSetName );
		}
	}

	private String[] getDataSets( )
	{
		ArrayList dataSetList = new ArrayList( );
		for ( Iterator iter = DEUtil.getDataSets( ).iterator( ); iter.hasNext( ); )
		{
			DataSetHandle dataSet = (DataSetHandle) iter.next( );
			if ( !dataSet.paramBindingsIterator( ).hasNext( ) )
			{
				dataSetList.add( dataSet.getQualifiedName( ) );
			}
		}
		return (String[]) dataSetList.toArray( new String[dataSetList.size( )] );
	}

	private DataSetHandle getDataSet( String name )
	{
		if ( name == null )
		{
			return null;
		}
		String value = name;

		DataSetHandle dataSet = null;
		if ( value != null )
		{
			dataSet = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.findDataSet( value );
		}
		return dataSet;
	}

	private String[] getDataSetColumns( )
	{
		return getDataSetColumns( null );
	}

	private String[] getDataSetColumns( ScalarParameterHandle handle )
	{
		if ( dataSet == null )
		{
			return new String[0];
		}
		DataSetItemModel[] models = DataSetManager.getCurrentInstance( )
				.getColumns( dataSet, true );
		if ( models == null )
		{
			return new String[0];
		}
		ArrayList valueList = new ArrayList( models.length );
		for ( int i = 0; i < models.length; i++ )
		{
			if ( handle == null || matchDataType( handle, models[i] ) )
			{
				valueList.add( models[i].getName( ) );
			}
		}
		return (String[]) valueList.toArray( new String[0] );
	}

	private void setCellEditorItems( )
	{
		( (ComboBoxCellEditor) cellEditors[1] ).setItems( getDataSetColumns( selectedParameter ) );
		( (ComboBoxCellEditor) cellEditors[2] ).setItems( getDataSetColumns( ) );
	}

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public Object[] getElements( Object inputElement )
		{
			ArrayList elementsList = new ArrayList( inputParameterGroup.getParameters( )
					.getContents( ) );
			for ( Iterator iter = elementsList.iterator( ); iter.hasNext( ); )
			{
				ScalarParameterHandle handle = (ScalarParameterHandle) iter.next( );
				String[] columns = getDataSetColumns( handle );
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
					value = LABEL_SELECT_A_VALUE_COLUMN;
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
						if ( paramHandle.getValueExpr( ) != null )
						{
							value = getColumnName( paramHandle.getValueExpr( ) );
						}
						else if ( getDataSetColumns( paramHandle ).length > 0 )
						{
							value = LABEL_SELECT_A_VALUE_COLUMN;
						}
						else
						{
							value = LABEL_NO_COLUMN_AVAILABLE;
						}
						break;
					}
					case 2 :
					{
						value = getColumnName( paramHandle.getLabelExpr( ) );
						if ( value == null )
						{
							if ( getDataSetColumns( ).length > 0 )
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
				dummyText += "        "; //$NON-NLS-1$
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
			if ( element != selectedParameter )
			{
				return false;
			}
			if ( element == newParameter )
			{
				if ( !property.equals( COLUMN_VALUE ) )
				{
					return false;
				}
			}
			else if ( property.equals( COLUMN_NAME ) )
			{
				return false;
			}
			if ( property.equals( COLUMN_VALUE ) )
			{
				if ( getDataSetColumns( (ScalarParameterHandle) element ).length == 0 )
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
				else if ( COLUMN_VALUE.equals( property ) )
				{
					value = getColumnName( parameter.getValueExpr( ) );

				}
				else if ( COLUMN_DISPLAY_TEXT.equals( property ) )
				{
					value = getColumnName( parameter.getLabelExpr( ) );

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
				if ( newParameter.getValueExpr( ) != null
						&& newParameter.getValueExpr( ).trim( ).length( ) > 0 )
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
			if ( !loading || selectedParameter.getFormat( ) == null )
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
				formatCategroy = getCategroy( selectedParameter.getFormat( ) );
				formatPattern = getPattern( selectedParameter.getFormat( ) );
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
			formatCategroy = getCategroy( (String) formatBuilder.getResult( ) );
			formatPattern = getPattern( (String) formatBuilder.getResult( ) );
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
			if ( formatPattern != null )
			{
				displayFormat += ":  " + formatPattern; //$NON-NLS-1$
			}
		}
		formatField.setText( "" + displayFormat ); //$NON-NLS-1$
		changeFormat.setEnabled( choiceSet != null );

		if ( selectedParameter != null )
		{
			try
			{
				doPreview( formatPattern == null ? formatCategroy
						: formatPattern );

				String format = formatCategroy;
				if ( formatPattern != null )
				{
					format += ":" + formatPattern; //$NON-NLS-1$
				}
				selectedParameter.setFormat( format );
			}
			catch ( SemanticException e1 )
			{
				ExceptionHandler.handle( e1 );
			}
		}
	}

	private void doPreview( String pattern )
	{
		String type = getSelectedDataType( );

		String formatStr = ""; //$NON-NLS-1$
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			formatStr = new StringFormatter( pattern, Locale.getDefault( ) ).format( DEFAULT_PREVIEW_STRING );
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

	private String getColumnName( String value )
	{
		DataSetItemModel[] models = DataSetManager.getCurrentInstance( )
				.getColumns( dataSet, true );
		if ( value != null )
		{
			for ( int i = 0; i < models.length; i++ )
			{
				if ( value.equalsIgnoreCase( DEUtil.getColumnExpression( models[i].getName( ) ) ) )
				{
					return models[i].getName( );
				}
			}
		}
		return null;
	}

	private void updateButtons( )
	{
		boolean okEnable = true;
		if ( dataSet == null )
		{
			okEnable = false;
		}
		else
		{
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
						if ( param.getValueExpr( ) == null )
						{
							okEnable = false;
							break;
						}
					}
				}
				okEnable &= ( count != 0 );
			}
		}
		getOkButton( ).setEnabled( okEnable );

	}

	private boolean matchDataType( ScalarParameterHandle handle,
			DataSetItemModel column )
	{
		if ( column.getDataType( ) == DataType.UNKNOWN_TYPE )
		{
			return false;
		}
		String type = handle.getDataType( );
		if ( handle == selectedParameter )
		{
			type = getSelectedDataType( );
		}
		if ( type.equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
		{
			return true;
		}
		switch ( column.getDataType( ) )
		{
			case DataType.BOOLEAN_TYPE :
				return type.equals( DesignChoiceConstants.PARAM_TYPE_BOOLEAN );
			case DataType.INTEGER_TYPE :
				return type.equals( DesignChoiceConstants.PARAM_TYPE_DECIMAL )
						|| type.equals( DesignChoiceConstants.PARAM_TYPE_FLOAT );
			case DataType.DATE_TYPE :
				return type.equals( DesignChoiceConstants.PARAM_TYPE_DATETIME );
			case DataType.DECIMAL_TYPE :
				return type.equals( DesignChoiceConstants.PARAM_TYPE_DECIMAL )
						|| type.equals( DesignChoiceConstants.PARAM_TYPE_FLOAT );
			case DataType.DOUBLE_TYPE :
				return type.equals( DesignChoiceConstants.PARAM_TYPE_FLOAT );
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
			refreshValueTable( );
		};
	}
}