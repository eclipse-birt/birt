/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImportValueDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.SelectionChoiceDialog;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ITableAreaModifier;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.TableArea;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.parameters.ParameterUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.SelectionChoiceHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.SelectionChoice;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.util.ParameterValidationUtil;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;

import com.ibm.icu.util.ULocale;

/**
 * The dialog used to create or edit a parameter
 */

public class ParameterDialog extends BaseDialog
{

	private static final String CHOICE_NO_DEFAULT = Messages.getString( "ParameterDialog.Choice.NoDefault" ); //$NON-NLS-1$

	private static final String CHOICE_NONE = Messages.getString( "ParameterDialog.Choice.None" ); //$NON-NLS-1$

	private static final String CHOICE_DISPLAY_TEXT = Messages.getString( "ParameterDialog.Choice.DisplayText" ); //$NON-NLS-1$

	private static final String CHOICE_VALUE_COLUMN = Messages.getString( "ParameterDialog.Choice.ValueColumn" ); //$NON-NLS-1$

	private static final String CHOICE_ASCENDING = Messages.getString( "ParameterDialog.Choice.ASCENDING" ); //$NON-NLS-1$

	private static final String CHOICE_DESCENDING = Messages.getString( "ParameterDialog.Choice.DESCENDING" ); //$NON-NLS-1$

	private static final String CHOICE_SELECT_VALUE = Messages.getString( "ParameterDialog.Choice.SelectValue" ); //$NON-NLS-1$

	private static final String GROUP_MORE_OPTION = Messages.getString( "ParameterDialog.Group.MoreOption" ); //$NON-NLS-1$

	private static final String LABEL_NAME = Messages.getString( "ParameterDialog.Label.Name" ); //$NON-NLS-1$

	private static final String LABEL_DATETIME_PROMPT = Messages.getFormattedString( "ParameterDialog.datetime.prompt", new String[]{"yyyy-MM-dd HH:mm:ss.SSS"} ); //$NON-NLS-1$ //$NON-NLS-2$

	private static final String LABEL_DATE_PROMPT = Messages.getFormattedString( "ParameterDialog.date.prompt", new String[]{"yyyy-MM-dd"} ); //$NON-NLS-1$ //$NON-NLS-2$

	private static final String LABEL_TIME_PROMPT = Messages.getFormattedString( "ParameterDialog.time.prompt", new String[]{"hh:mm:ss"} ); //$NON-NLS-1$ //$NON-NLS-2$

	private static final String LABEL_PROMPT_TEXT = Messages.getString( "ParameterDialog.Label.PromptText" ); //$NON-NLS-1$

	private static final String LABEL_PARAM_DATA_TYPE = Messages.getString( "ParameterDialog.Label.DataType" ); //$NON-NLS-1$

	private static final String LABEL_DISPALY_TYPE = Messages.getString( "ParameterDialog.Label.DisplayType" ); //$NON-NLS-1$

	private static final String LABEL_DEFAULT_VALUE = Messages.getString( "ParameterDialog.Label.DefaultValue" ); //$NON-NLS-1$

	private static final String LABEL_HELP_TEXT = Messages.getString( "ParameterDialog.Label.HelpText" ); //$NON-NLS-1$

	private static final String LABEL_LIST_OF_VALUE = Messages.getString( "ParameterDialog.Label.ListOfValue" ); //$NON-NLS-1$

	private static final String LABEL_SORT_GROUP = Messages.getString( "ParameterDialog.Label.SortGroup" ); //$NON-NLS-1$

	private static final String LABEL_VALUES = Messages.getString( "ParameterDialog.Label.Value" ); //$NON-NLS-1$

	private static final String LABEL_FORMAT = Messages.getString( "ParameterDialog.Label.Format" ); //$NON-NLS-1$

	private static final String LABEL_LIST_LIMIT = Messages.getString( "ParameterDialog.Label.Listlimit" ); //$NON-NLS-1$

	private static final String LABEL_NULL = ParameterUtil.LABEL_NULL;

	private static final String LABEL_SELECT_DISPLAY_TEXT = Messages.getString( "ParameterDialog.Label.SelectDisplayText" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_VALUE_COLUMN = Messages.getString( "ParameterDialog.Label.SelectValueColumn" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DATA_SET = Messages.getString( "ParameterDialog.Label.SelectDataSet" ); //$NON-NLS-1$

	private static final String LABEL_PREVIEW = Messages.getString( "ParameterDialog.Label.Preview" ); //$NON-NLS-1$

	private static final String LABEL_SORT_KEY = Messages.getString( "ParameterDialog.Label.SortKey" ); //$NON-NLS-1$

	private static final String LABEL_SORT_DIRECTION = Messages.getString( "ParameterDialog.Label.SortDirection" ); //$NON-NLS-1$

	private static final String CHECKBOX_ISREQUIRED = Messages.getString( "ParameterDialog.CheckBox.IsRequired" ); //$NON-NLS-1$

	private static final String CHECKBOX_DO_NOT_ECHO = Messages.getString( "ParameterDialog.CheckBox.DoNotEchoInput" ); //$NON-NLS-1$

	private static final String CHECKBOX_HIDDEN = Messages.getString( "ParameterDialog.CheckBox.Hidden" ); //$NON-NLS-1$

	private static final String CHECKBOX_DISTINCT = Messages.getString( "ParameterDialog.CheckBox.Distinct" ); //$NON-NLS-1$

	private static final String BUTTON_LABEL_CHANGE_FORMAT = Messages.getString( "ParameterDialog.Button.ChangeFormat" ); //$NON-NLS-1$

	private static final String BUTTON_LABEL_IMPORT = Messages.getString( "ParameterDialog.Button.ImportValue" ); //$NON-NLS-1$

	private static final String BUTTON_LABEL_SET_DEFAULT = Messages.getString( "ParameterDialog.Button.SetDefault" ); //$NON-NLS-1$

	private static final String BUTTON_LABEL_REMOVE_DEFAULT = Messages.getString( "ParameterDialog.Button.RemoveDefault" ); //$NON-NLS-1$

	private static final String BUTTON_CREATE_DATA_SET = Messages.getString( "ParameterDialog.Button.CreateDataSet" ); //$NON-NLS-1$

	private static final String RADIO_DYNAMIC = Messages.getString( "ParameterDialog.Radio.Dynamic" ); //$NON-NLS-1$

	private static final String CHECK_ALLOW_MULTI = Messages.getString( "ParameterDialog.Check.AllowMulti" ); //$NON-NLS-1$

	private static final String RADIO_STATIC = Messages.getString( "ParameterDialog.Radio.Static" ); //$NON-NLS-1$

	private static final String ERROR_TITLE_INVALID_LIST_LIMIT = Messages.getString( "ParameterDialog.ErrorTitle.InvalidListLimit" ); //$NON-NLS-1$

	private static final String ERROR_MSG_CANNOT_BE_BLANK = Messages.getString( "ParameterDialog.ErrorMessage.CanootBeBlank" ); //$NON-NLS-1$

	private static final String ERROR_MSG_CANNOT_BE_NULL = Messages.getString( "ParameterDialog.ErrorMessage.CanootBeNull" ); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_VALUE = Messages.getString( "ParameterDialog.ErrorMessage.DuplicatedValue" ); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_LABEL = Messages.getString( "ParameterDialog.ErrorMessage.DuplicatedLabel" ); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_LABELKEY = Messages.getString( "ParameterDialog.ErrorMessage.DuplicatedLabelKey" ); //$NON-NLS-1$

	private static final String ERROR_MSG_MISMATCH_DATA_TYPE = Messages.getString( "ParameterDialog.ErrorMessage.MismatchDataType" ); //$NON-NLS-1$

	private static final String ERROR_MSG_DUPLICATED_NAME = Messages.getString( "ParameterDialog.ErrorMessage.DuplicatedName" ); //$NON-NLS-1$

	private static final String ERROR_MSG_NAME_IS_EMPTY = Messages.getString( "ParameterDialog.ErrorMessage.EmptyName" ); //$NON-NLS-1$

	private static final String ERROR_MSG_NO_DEFAULT_VALUE = Messages.getString( "ParameterDialog.ErrorMessage.NoDefaultValue" ); //$NON-NLS-1$

	private static final String ERROR_MSG_NO_AVAILABLE_COLUMN = Messages.getString( "ParameterDialog.ErrorMessage.NoAvailableColumn" ); //$NON-NLS-1$

	private static final String ERROR_MSG_INVALID_LIST_LIMIT = Messages.getString( "ParameterDialog.ErrorMessage.InvalidListLimit" ); //$NON-NLS-1$

	private static final String FLAG_DEFAULT = Messages.getString( "ParameterDialog.Flag.Default" ); //$NON-NLS-1$

	private static final String COLUMN_VALUE = Messages.getString( "ParameterDialog.Column.Value" ); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT = Messages.getString( "ParameterDialog.Column.DisplayText" ); //$NON-NLS-1$

	private static final String COLUMN_DISPLAY_TEXT_KEY = Messages.getString( "ParameterDialog.Column.DisplayTextKey" ); //$NON-NLS-1$

	private static final String COLUMN_IS_DEFAULT = Messages.getString( "ParameterDialog.Column.Default" ); //$NON-NLS-1$

	private static final String BOOLEAN_TRUE = Messages.getString( "ParameterDialog.Boolean.True" ); //$NON-NLS-1$

	private static final String BOOLEAN_FALSE = Messages.getString( "ParameterDialog.Boolean.False" ); //$NON-NLS-1$

	private static final String PARAM_CONTROL_LIST = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
			+ "/List"; //$NON-NLS-1$

	private static final String PARAM_CONTROL_COMBO = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX
			+ "/Combo"; //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_LIST = Messages.getString( "ParameterDialog.DisplayLabel.List" ); //$NON-NLS-1$

	private static final String DISPLAY_NAME_CONTROL_COMBO = Messages.getString( "ParameterDialog.DisplayLabel.Combo" ); //$NON-NLS-1$

	private static final String NONE_DISPLAY_TEXT = Messages.getString( "ParameterDialog.Label.None" ); //$NON-NLS-1$

	private static final Image DEFAULT_ICON = ReportPlatformUIImages.getImage( IReportGraphicConstants.ICON_DEFAULT );

	private static final Image ERROR_ICON = ReportPlatformUIImages.getImage( ISharedImages.IMG_OBJS_ERROR_TSK );

	private static final String STANDARD_DATE_TIME_PATTERN = ParameterUtil.STANDARD_DATE_TIME_PATTERN;

	private boolean allowMultiValueVisible = true;

	private HashMap dirtyProperties = new HashMap( 5 );

	private ArrayList choiceList = new ArrayList( );

	private static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getElement( ReportDesignConstants.SCALAR_PARAMETER_ELEMENT )
			.getProperty( ScalarParameterHandle.DATA_TYPE_PROP )
			.getAllowedChoices( );

	private static final IChoiceSet CONTROL_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_CONTROL );

	private ScalarParameterHandle inputParameter;

	private boolean loading = true;

	private Text nameEditor, promptTextEditor, helpTextEditor, formatField;

	// Prompt message line
	private Label promptMessageLine;

	// Error message line
	private CLabel errorMessageLine;

	// Check boxes
	private Button isRequired, doNotEcho, isHidden, distinct;

	// Push buttons
	private Button importValue, changeDefault, changeFormat, createDataSet;

	// Radio buttons
	private Button dynamicRadio, staticRadio;

	private Button allowMultiChoice;

	// Combo chooser for static
	private Combo dataTypeChooser, controlTypeChooser, defaultValueChooser;

	// Combo chooser for dynamic
	private Combo dataSetChooser, columnChooser, displayTextChooser,
			sortKeyChooser, sortDirectionChooser;

	private Button valueColumnExprButton;

	// Label
	private Label previewLabel, sortKeyLabel, sortDirectionLabel;

	private TableViewer valueTable;

	private String lastDataType, lastControlType;

	private String formatCategroy, formatPattern;

	private String defaultValue;

	private Composite valueArea, sorttingArea;

	private List columnList;

	private TableArea tableArea;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			ArrayList list = ( (ArrayList) inputElement );
			ArrayList elementsList = (ArrayList) list.clone( );
			return elementsList.toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			if ( valueTable.getColumnProperties( ).length == 5
					&& columnIndex == 1 )
			{
				SelectionChoice choice = ( (SelectionChoice) element );
				if ( isDefaultChoice( choice ) )
				{
					return DEFAULT_ICON;
				}
			}
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			SelectionChoice choice = ( (SelectionChoice) element );
			final int valueIndex = valueTable.getColumnProperties( ).length - 3;
			String text = null;
			if ( valueTable.getColumnProperties( ).length == 5
					&& columnIndex == 1 )
			{
				if ( isDefaultChoice( choice ) )
				{
					text = FLAG_DEFAULT;
				}
			}
			else if ( columnIndex == valueIndex )
			{
				text = choice.getValue( );
			}
			else if ( columnIndex == valueIndex + 1 )
			{
				text = choice.getLabel( );
				if ( text == null )
				{
					// text = format( choice.getValue( ) );
					text = ""; //$NON-NLS-1$
				}
			}
			else if ( columnIndex == valueIndex + 2 )
			{
				text = choice.getLabelResourceKey( );
			}
			if ( text == null )
			{
				text = ""; //$NON-NLS-1$
			}
			return text;
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

	private final ITableAreaModifier tableAreaModifier = new ITableAreaModifier( ) {

		public boolean editItem( final Object element )
		{
			final SelectionChoice choice = (SelectionChoice) element;
			boolean isDefault = isDefaultChoice( choice );
			SelectionChoiceDialog dialog = new SelectionChoiceDialog( Messages.getString( "ParameterDialog.SelectionDialog.Edit" ) ); //$NON-NLS-1$
			dialog.setInput( choice );
			dialog.setValidator( new SelectionChoiceDialog.ISelectionChoiceValidator( ) {

				public String validate( String displayLableKey,
						String displayLabel, String value )
				{
					return validateChoice( choice,
							displayLableKey,
							displayLabel,
							value );
				}

			} );
			if ( dialog.open( ) == Dialog.OK )
			{
				choice.setValue( convertToStandardFormat( choice.getValue( ) ) );
				if ( isDefault )
				{
					changeDefaultValue( choice.getValue( ) );
				}
				return true;
			}
			return false;
		}

		public boolean newItem( )
		{
			SelectionChoice choice = StructureFactory.createSelectionChoice( );
			SelectionChoiceDialog dialog = new SelectionChoiceDialog( Messages.getString( "ParameterDialog.SelectionDialog.New" ) ); //$NON-NLS-1$
			dialog.setInput( choice );
			dialog.setValidator( new SelectionChoiceDialog.ISelectionChoiceValidator( ) {

				public String validate( String displayLabelKey,
						String displayLabel, String value )
				{
					return validateChoice( null,
							displayLabelKey,
							displayLabel,
							value );
				}
			} );
			if ( dialog.open( ) == Dialog.OK )
			{
				choice.setValue( convertToStandardFormat( choice.getValue( ) ) );
				choiceList.add( choice );
				return true;
			}
			return false;
		}

		public boolean removeItem( Object[] elements )
		{
			for ( int i = 0; i < elements.length; i++ )
			{
				if ( isDefaultChoice( (SelectionChoice) elements[i] ) )
				{
					changeDefaultValue( null );
				}
				choiceList.remove( elements[i] );
			}
			return true;
		}

		public boolean removeItemAll( )
		{
			// TODO Auto-generated method stub
			choiceList.clear( );
			changeDefaultValue( null );
			return true;
		}
	};

	private Text listLimit;

	/**
	 * Create a new parameter dialog with given title under the active shell
	 * 
	 * @param title
	 *            the title of the dialog
	 */
	public ParameterDialog( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	/**
	 * Create a new parameter dialog with given title under the specified shell
	 * 
	 * @param parentShell
	 *            the parent shell of the dialog
	 * @param title
	 *            the title of the dialog
	 */
	public ParameterDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	public ParameterDialog( Shell parentShell, String title,
			boolean allowMultiValueVisible )
	{
		super( parentShell, title );
		this.allowMultiValueVisible = allowMultiValueVisible;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite parentComposite = (Composite) super.createDialogArea( parent );

		Composite topComposite = new Composite( parentComposite, SWT.NONE );
		topComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		topComposite.setLayout( new GridLayout( 2, false ) );

		createPropertiesSection( topComposite );
		createDisplayOptionsSection( topComposite );
		createValuesDefineSection( parentComposite );
		UIUtil.bindHelp( parent, IHelpContextIds.PARAMETER_DIALOG_ID );
		return parentComposite;
	}

	private void createPropertiesSection( Composite composite )
	{

		Composite propertiesSection = new Composite( composite, SWT.NONE );
		propertiesSection.setLayout( new GridLayout( ) );
		GridData gd = new GridData( );
		gd.widthHint = 200;
		propertiesSection.setLayoutData( gd );

		createLabel( propertiesSection, LABEL_NAME );
		nameEditor = new Text( propertiesSection, SWT.BORDER );
		nameEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		nameEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateMessageLine( );
			}
		} );
		createLabel( propertiesSection, LABEL_PROMPT_TEXT );
		promptTextEditor = new Text( propertiesSection, SWT.BORDER );
		promptTextEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createLabel( propertiesSection, LABEL_PARAM_DATA_TYPE );
		dataTypeChooser = new Combo( propertiesSection, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		dataTypeChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		dataTypeChooser.setItems( ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET ) );
		dataTypeChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				changeDataType( );
				updateCheckBoxArea( );
				refreshColumns( true );
			}
		} );
		createLabel( propertiesSection, LABEL_DISPALY_TYPE );
		controlTypeChooser = new Combo( propertiesSection, SWT.READ_ONLY
				| SWT.DROP_DOWN );
		controlTypeChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		controlTypeChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				changeControlType( );
			}
		} );

	}

	private void createDisplayOptionsSection( Composite composite )
	{
		Group displayOptionSection = new Group( composite, SWT.NONE );
		displayOptionSection.setText( GROUP_MORE_OPTION );
		displayOptionSection.setLayout( new GridLayout( 2, false ) );
		displayOptionSection.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createLabel( displayOptionSection, LABEL_HELP_TEXT );
		helpTextEditor = new Text( displayOptionSection, SWT.BORDER );
		helpTextEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		createLabel( displayOptionSection, LABEL_FORMAT );
		Composite formatSection = new Composite( displayOptionSection, SWT.NONE );
		formatSection.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		formatSection.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		formatField = new Text( formatSection, SWT.BORDER
				| SWT.SINGLE
				| SWT.READ_ONLY );
		formatField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		changeFormat = new Button( formatSection, SWT.PUSH );
		changeFormat.setText( BUTTON_LABEL_CHANGE_FORMAT );
		setButtonLayoutData( changeFormat );
		changeFormat.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				popupFormatBuilder( true );
			}

		} );
		createLabel( displayOptionSection, null );
		Group previewArea = new Group( displayOptionSection, SWT.NONE );
		previewArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		previewArea.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		previewArea.setText( LABEL_PREVIEW );
		previewLabel = new Label( previewArea, SWT.NONE );
		previewLabel.setAlignment( SWT.CENTER );
		previewLabel.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		// start create list limitation area
		createLabel( displayOptionSection, LABEL_LIST_LIMIT );

		Composite limitArea = new Composite( displayOptionSection, SWT.NULL );
		GridLayout layout = new GridLayout( 2, false );
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		limitArea.setLayout( layout );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.verticalSpan = 1;
		limitArea.setLayoutData( data );

		listLimit = new Text( limitArea, SWT.BORDER );
		data = new GridData( );
		data.widthHint = 80;
		listLimit.setLayoutData( data );
		listLimit.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				e.doit = ( "0123456789\0\b\u007f".indexOf( e.character ) != -1 ); //$NON-NLS-1$
			}
		} );
		Label values = new Label( limitArea, SWT.NULL );
		values.setText( Messages.getString( "ParameterDialog.Label.values" ) ); //$NON-NLS-1$
		values.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		// end

		createLabel( displayOptionSection, null ); // Dummy
		Composite checkBoxArea = new Composite( displayOptionSection, SWT.NONE );
		checkBoxArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		checkBoxArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		isRequired = new Button( checkBoxArea, SWT.CHECK );
		isRequired.setText( CHECKBOX_ISREQUIRED );
		addCheckBoxListener( isRequired, CHECKBOX_ISREQUIRED );

		doNotEcho = new Button( checkBoxArea, SWT.CHECK );
		doNotEcho.setText( CHECKBOX_DO_NOT_ECHO );
		addCheckBoxListener( doNotEcho, CHECKBOX_DO_NOT_ECHO );
		isHidden = new Button( checkBoxArea, SWT.CHECK );
		isHidden.setText( CHECKBOX_HIDDEN );
		addCheckBoxListener( isHidden, CHECKBOX_HIDDEN );

		distinct = new Button( checkBoxArea, SWT.CHECK );
		distinct.setText( CHECKBOX_DISTINCT );
		distinct.setSelection( false );
		addCheckBoxListener( distinct, CHECKBOX_DISTINCT );
	}

	private void createValuesDefineSection( Composite composite )
	{
		Group valuesDefineSection = new Group( composite, SWT.NONE );
		valuesDefineSection.setText( LABEL_LIST_OF_VALUE );
		valuesDefineSection.setLayout( new GridLayout( 2, false ) );
		valuesDefineSection.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite choiceArea = new Composite( valuesDefineSection, SWT.NONE );
		choiceArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		choiceArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 4, true ) );
		staticRadio = new Button( choiceArea, SWT.RADIO );
		staticRadio.setText( RADIO_STATIC );
		GridData gd = new GridData( GridData.HORIZONTAL_ALIGN_BEGINNING );
		staticRadio.setLayoutData( gd );
		staticRadio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchParamterType( );
			}

		} );
		dynamicRadio = new Button( choiceArea, SWT.RADIO );
		dynamicRadio.setText( RADIO_DYNAMIC );
		dynamicRadio.setLayoutData( gd );
		dynamicRadio.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switchParamterType( );
			}
		} );

		Label dummy = new Label( choiceArea, SWT.NONE );
		dummy.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		allowMultiChoice = new Button( choiceArea, SWT.CHECK );
		allowMultiChoice.setText( CHECK_ALLOW_MULTI );
		gd = new GridData( GridData.HORIZONTAL_ALIGN_END );
		staticRadio.setLayoutData( gd );

		valueArea = new Composite( valuesDefineSection, SWT.NONE );
		valueArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 300;
		gd.widthHint = 550;
		gd.horizontalSpan = 2;
		valueArea.setLayoutData( gd );

		createLabel( valuesDefineSection, null );
		errorMessageLine = new CLabel( valuesDefineSection, SWT.NONE );
		GridData msgLineGridData = new GridData( GridData.FILL_HORIZONTAL );
		msgLineGridData.horizontalSpan = 2;
		errorMessageLine.setLayoutData( msgLineGridData );
	}

	/**
	 * Set the input of the dialog, which cannot be null
	 * 
	 * @param input
	 *            the input of the dialog, which cannot be null
	 */
	public void setInput( Object input )
	{
		// Assert.isNotNull( input );
		// Assert.isLegal( input instanceof ScalarParameterHandle );
		inputParameter = (ScalarParameterHandle) input;
	}

	protected boolean initDialog( )
	{
		// Assert.isNotNull( inputParameter );
		nameEditor.setText( inputParameter.getName( ) );
		if ( !StringUtil.isBlank( inputParameter.getPromptText( ) ) )
		{
			promptTextEditor.setText( inputParameter.getPromptText( ) );
		}
		helpTextEditor.setText( UIUtil.convertToGUIString( inputParameter.getHelpText( ) ) );
		if ( inputParameter.getValueType( )
				.equals( DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC ) )
		{
			staticRadio.setSelection( true );

			for ( Iterator iter = inputParameter.getPropertyHandle( ScalarParameterHandle.SELECTION_LIST_PROP )
					.iterator( ); iter.hasNext( ); )
			{
				SelectionChoiceHandle choiceHandle = (SelectionChoiceHandle) iter.next( );
				choiceList.add( choiceHandle.getStructure( ) );
			}
		}
		else
		{
			dynamicRadio.setSelection( true );
		}
		defaultValue = inputParameter.getDefaultValue( );

		if ( PARAM_CONTROL_LIST.endsWith( getSelectedControlType( ) )
				&& allowMultiValueVisible )
		{
			allowMultiChoice.setVisible( true );
			if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.endsWith( inputParameter.getParamType( ) ) )
			{
				allowMultiChoice.setSelection( true );
			}
			else
			{
				allowMultiChoice.setSelection( false );
			}

		}
		else
		{
			allowMultiChoice.setVisible( false );
		}

		if ( inputParameter.getPropertyHandle( ScalarParameterHandle.LIST_LIMIT_PROP )
				.isSet( ) )
		{
			listLimit.setText( String.valueOf( inputParameter.getListlimit( ) ) );
		}

		isHidden.setSelection( inputParameter.isHidden( ) );
		isRequired.setSelection( inputParameter.isRequired( ) );
		doNotEcho.setSelection( inputParameter.isConcealValue( ) );
		distinct.setSelection( !inputParameter.distinct( ) );
		changeDataType( );
		dataTypeChooser.setText( DATA_TYPE_CHOICE_SET.findChoice( inputParameter.getDataType( ) )
				.getDisplayName( ) );
		switchParamterType( );
		loading = false;
		return true;
	}

	private void initValueArea( )
	{
		String controlType = getSelectedControlType( );
		if ( isStatic( ) )
		{
			if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals( controlType ) )
			{
				if ( isValidValue( defaultValue ) != null )
				{
					defaultValue = null;
					defaultValueChooser.select( 0 );
				}
				else
				{
					if ( defaultValue == null )
					{
						defaultValueChooser.select( defaultValueChooser.indexOf( CHOICE_NO_DEFAULT ) );
					}
					else if ( Boolean.valueOf( defaultValue ).booleanValue( ) )
					{
						defaultValueChooser.select( 1 );
					}
					else
					{
						defaultValueChooser.select( 2 );
					}
				}
			}
			else if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals( controlType ) )
			{
				if ( getSelectedDataType( ).equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
				{
					defaultValueChooser.setText( DEUtil.resolveNull( defaultValue ) );
				}
				else if ( defaultValue != null )
				{
					if ( ( defaultValue.equals( Boolean.toString( true ) ) || defaultValue.equals( Boolean.toString( false ) ) ) )
					{
						defaultValue = null;
					}
					else
					{
						defaultValueChooser.setText( defaultValue );
					}
				}
			}
			else if ( PARAM_CONTROL_COMBO.equals( controlType )
					|| PARAM_CONTROL_LIST.equals( controlType )
					|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.endsWith( controlType ) )
			{
				initSorttingArea( );

				// To fix bug Bugzilla 169927
				// Please also refer to Bugzilla 175788
				if ( lastControlType != null
						&& lastControlType.equals( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX ) )
				{
					defaultValue = null;
				}
			}
			refreshValueTable( );
		}
		else
		{
			refreshDataSets( );
			if ( inputParameter.getDataSetName( ) != null )
			{
				dataSetChooser.setText( inputParameter.getDataSetName( ) );
			}
			refreshColumns( false );
			String columnName = getColumnName( inputParameter.getValueExpr( ) );
			if ( columnName != null )
			{
				columnChooser.setText( columnName );
			}
			columnName = getColumnName( inputParameter.getLabelExpr( ) );
			if ( columnName != null )
			{
				displayTextChooser.setText( columnName );
			}
			if ( getSelectedDataType( ).equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
			{
				defaultValueChooser.setText( DEUtil.resolveNull( defaultValue ) );

			}
			else if ( defaultValue != null )
			{
				defaultValueChooser.setText( defaultValue );
			}

			initSorttingArea( );
		}
		updateMessageLine( );
	}

	private void initSorttingArea( )
	{
		if ( !inputParameter.isFixedOrder( ) )
		{
			sortKeyLabel.setEnabled( true );
			sortKeyChooser.setEnabled( true );
			sortDirectionLabel.setEnabled( true );
			sortDirectionChooser.setEnabled( true );
			distinct.setEnabled( true );

			distinct.setSelection( !inputParameter.distinct( ) );
			String sortKey = inputParameter.getSortBy( );
			if ( sortKey == null
					|| sortKey.equals( DesignChoiceConstants.PARAM_SORT_VALUES_LABEL ) )
			{
				sortKeyChooser.setText( CHOICE_DISPLAY_TEXT );
			}
			else
			{
				sortKeyChooser.setText( CHOICE_VALUE_COLUMN );
			}

			String sortDirection = inputParameter.getSortDirection( );
			if ( sortDirection == null
					|| sortDirection.equals( DesignChoiceConstants.SORT_DIRECTION_ASC ) )
			{
				sortDirectionChooser.setText( CHOICE_ASCENDING );
			}
			else
			{
				sortDirectionChooser.setText( CHOICE_DESCENDING );
			}
		}
		else
		{
			sortKeyLabel.setEnabled( true );
			sortKeyChooser.setEnabled( true );
			sortDirectionLabel.setEnabled( false );
			sortDirectionChooser.setEnabled( false );
			distinct.setEnabled( false );
		}
	}

	private void initFormatField( )
	{
		String type = getSelectedDataType( );
		if ( ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( lastControlType ) && DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
				|| ( DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( lastControlType ) && DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type ) ) )
		{
			return;
		}
		IChoiceSet choiceSet = getFormatChoiceSet( type );
		if ( choiceSet == null )
		{
			formatCategroy = formatPattern = null;
		}
		else
		{
			if ( !loading
					|| ( ( inputParameter.getCategory( ) == null && inputParameter.getPattern( ) == null ) ) )
			{
				if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
						|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type )
						|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
				{
					formatCategroy = choiceSet.findChoice( DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED )
							.getName( );
				}
				formatPattern = null;
			}
			else
			{
				formatCategroy = inputParameter.getCategory( );
				if ( formatCategroy == null )
				{
					formatCategroy = DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED;
				}
				formatPattern = inputParameter.getPattern( );
			}
		}
		updateFormatField( );
	}

	private List getColumnValueList( )
	{
		try
		{
			String queryExpr = getExpression( columnChooser.getText( ) );
			if ( queryExpr == null || queryExpr.equals( "" ) ) //$NON-NLS-1$
			{
				return Collections.EMPTY_LIST;
			}

			ArrayList valueList = new ArrayList( );

			valueList.addAll( SelectValueFetcher.getSelectValueList( queryExpr,
					getDataSetHandle( ) ) );

			return valueList;
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
			return Collections.EMPTY_LIST;
		}
	}

	private void refreshDataSets( )
	{
		String selectedDataSetName = dataSetChooser.getText( );
		String[] oldList = dataSetChooser.getItems( );

		List dataSetList = new ArrayList( );

		for ( Iterator iterator = inputParameter.getModuleHandle( )
				.getVisibleDataSets( )
				.iterator( ); iterator.hasNext( ); )
		{
			DataSetHandle DataSetHandle = (DataSetHandle) iterator.next( );
			dataSetList.add( DataSetHandle.getQualifiedName( ) );
		}
		if ( inputParameter.getDataSetName( ) != null
				&& !dataSetList.contains( inputParameter.getDataSetName( ) ) )
		{
			dataSetList.add( 0, inputParameter.getDataSetName( ) );
		}

		if ( oldList.length != dataSetList.size( ) ) // it means new data set
		// is created.
		{
			String newName = findNewDataSet( Arrays.asList( oldList ),
					dataSetList );
			if ( newName != null )
			{
				selectedDataSetName = newName;
			}

			dataSetChooser.setItems( (String[]) dataSetList.toArray( new String[]{} ) );
			if ( StringUtil.isBlank( selectedDataSetName ) )
			{
				dataSetChooser.select( 0 );
				refreshColumns( false );
			}
			else if ( selectedDataSetName != null
					&& dataSetChooser.indexOf( selectedDataSetName ) != -1 )
			{
				dataSetChooser.select( dataSetChooser.indexOf( selectedDataSetName ) );
				refreshColumns( false );
			}
		}
	}

	private String findNewDataSet( List existingDataSets, List newDataSets )
	{
		for ( int i = 0; i < newDataSets.size( ); i++ )
		{
			if ( !existingDataSets.contains( newDataSets.get( i ) ) )
			{
				return (String) newDataSets.get( i );
			}
		}
		return null;
	}

	private DataSetHandle getDataSetHandle( )
	{
		return inputParameter.getModuleHandle( )
				.findDataSet( dataSetChooser.getText( ) );
	}

	private void refreshColumns( boolean onlyFilter )
	{
		if ( columnChooser == null || columnChooser.isDisposed( ) )
		{
			return;
		}
		if ( !onlyFilter )
		{
			DataSetHandle dataSetHandle = getDataSetHandle( );

			try
			{
				columnList = DataUtil.getColumnList( dataSetHandle );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
			displayTextChooser.removeAll( );
			displayTextChooser.add( NONE_DISPLAY_TEXT );
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				displayTextChooser.add( ( (ResultSetColumnHandle) iter.next( ) ).getColumnName( ) );
			}
			displayTextChooser.setText( NONE_DISPLAY_TEXT );
		}
		String originalSelection = columnChooser.getText( );
		columnChooser.removeAll( );

		for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
		{
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next( );
			if ( matchDataType( cachedColumn ) )
			{
				columnChooser.add( cachedColumn.getColumnName( ) );
			}
		}
		if ( columnChooser.indexOf( originalSelection ) != -1 )
		{
			columnChooser.setText( originalSelection );
		}
		if ( columnChooser.getItemCount( ) == 0 )
		{
			columnChooser.add( "" ); //$NON-NLS-1$
		}
		// columnChooser.setEnabled( columnChooser.getItemCount( ) > 0 );
		// valueColumnExprButton.setEnabled( columnChooser.getItemCount( ) > 0
		// );
		updateMessageLine( );
	}

	private boolean matchDataType( ResultSetColumnHandle column )
	{
		String type = getSelectedDataType( );
		if ( type.equals( DesignChoiceConstants.PARAM_TYPE_STRING )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( column.getDataType( ) ) )
		{
			return true;
		}
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_DATETIME );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_DECIMAL );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_FLOAT );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_INTEGER );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_DATE );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.PARAM_TYPE_TIME );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN.equals( column.getDataType( ) ) )
		{
			return type.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN );
		}
		return false;
	}

	private String getInputControlType( )
	{
		String type = null;
		if ( inputParameter.getControlType( ) == null )
		{
			type = DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX;
		}
		else if ( DesignChoiceConstants.PARAM_CONTROL_LIST_BOX.equals( inputParameter.getControlType( ) ) )
		{
			if ( inputParameter.isMustMatch( ) )
			{
				type = PARAM_CONTROL_LIST;
			}
			else
			{
				type = PARAM_CONTROL_COMBO;
			}
		}
		else
		{
			type = inputParameter.getControlType( );
		}
		return type;
	}

	private String getSelectedDataType( )
	{
		String type = null;
		if ( StringUtil.isBlank( dataTypeChooser.getText( ) ) )
		{
			type = inputParameter.getDataType( );
		}
		else
		{
			IChoice choice = DATA_TYPE_CHOICE_SET.findChoiceByDisplayName( dataTypeChooser.getText( ) );
			type = choice.getName( );
		}
		return type;
	}

	/**
	 * Gets the internal name of the control type from the display name
	 */
	private String getSelectedControlType( )
	{
		String displayText = controlTypeChooser.getText( );
		if ( StringUtil.isBlank( displayText ) )
		{
			return getInputControlType( );
		}
		if ( DISPLAY_NAME_CONTROL_COMBO.equals( displayText ) )
		{
			return PARAM_CONTROL_COMBO;
		}
		if ( DISPLAY_NAME_CONTROL_LIST.equals( displayText ) )
		{
			return PARAM_CONTROL_LIST;
		}
		return CONTROL_TYPE_CHOICE_SET.findChoiceByDisplayName( displayText )
				.getName( );
	}

	private void changeDataType( )
	{
		String type = getSelectedDataType( );
		if ( type.equals( lastDataType ) )
		{
			return;
		}

		// When data type is changed, validate the default value first. if the
		// old default value is invalid,
		// then set the default value to null, else let in remain it unchanged.
		// -- Begin --
		try
		{
			validateValue( defaultValue );
		}
		catch ( BirtException e1 )
		{
			defaultValue = null;
		}
		// -- End --

		if ( buildControlTypeList( type ) )
		{
			changeControlType( );
		}

		initFormatField( );

		if ( type.equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
		{
			clearDefaultValueChooser( isRequired.getSelection( ) );
		}
		else if ( !type.equals( DesignChoiceConstants.PARAM_TYPE_BOOLEAN ) )
		{
			clearDefaultValueText( );
			clearDefaultValueChooserSelections( );
		}

		if ( ( isStatic( ) && !distinct.isEnabled( ) )
				|| ( distinct.isEnabled( ) && !distinct.getSelection( ) ) )
		{
			makeUniqueAndValid( );
			refreshValueTable( );
		}
		else
		{
			refreshColumns( true );
		}

		lastDataType = type;
		updateMessageLine( );
	}

	private boolean buildControlTypeList( String type )
	{
		String[] choices;
		if ( isStatic( ) )
		{
			if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
			{
				choices = new String[3];
			}
			else
			{
				choices = new String[4];
			}
		}
		else
		{
			choices = new String[2];
		}
		if ( controlTypeChooser.getItemCount( ) != choices.length )
		{
			String originalSelection = controlTypeChooser.getText( );
			if ( isStatic( ) )
			{
				if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( type ) )
				{
					choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX )
							.getDisplayName( );
					choices[1] = DISPLAY_NAME_CONTROL_COMBO;
				}
				else
				{
					choices[0] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX )
							.getDisplayName( );
					// choices[1] = DISPLAY_NAME_CONTROL_LIST;
					choices[1] = DISPLAY_NAME_CONTROL_COMBO;
					choices[2] = DISPLAY_NAME_CONTROL_LIST;
				}
				// choices[choices.length - 2] = DISPLAY_NAME_CONTROL_COMBO;
				// choices[choices.length - 2] = DISPLAY_NAME_CONTROL_LIST;
				choices[choices.length - 1] = CONTROL_TYPE_CHOICE_SET.findChoice( DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON )
						.getDisplayName( );

			}
			else
			{
				choices[0] = DISPLAY_NAME_CONTROL_COMBO;
				choices[1] = DISPLAY_NAME_CONTROL_LIST;
			}
			controlTypeChooser.setItems( choices );
			if ( originalSelection.length( ) == 0 )
			{// initialize
				controlTypeChooser.setText( getInputControlDisplayName( ) );
			}
			else
			{
				int index = controlTypeChooser.indexOf( originalSelection );
				if ( index == -1 )
				{// The original control type cannot be
					// supported
					controlTypeChooser.select( 0 );
					return true;
				}
				controlTypeChooser.setText( originalSelection );
			}
		}

		return false;
	}

	// false: change anything; true: remove duplicated
	private boolean makeUniqueAndValid( )
	{
		boolean change = false;
		try
		{

			Set set = new HashSet( );
			for ( Iterator iter = choiceList.iterator( ); iter.hasNext( ); )
			{
				SelectionChoice choice = (SelectionChoice) iter.next( );
				if ( isValidValue( choice.getValue( ) ) != null
						|| set.contains( validateValue( choice.getValue( ) ) ) )
				{
					iter.remove( );
					change = true;
				}
				else
				{

					set.add( validateValue( choice.getValue( ) ) );

				}
			}
		}
		catch ( BirtException e )
		{
			// TODO Auto-generated catch block
			e.printStackTrace( );
		}
		return change;
	}

	private void changeControlType( )
	{
		if ( isStatic( ) )
		{
			String type = getSelectedControlType( );
			if ( !type.equals( lastControlType ) )
			{
				if ( DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals( type ) )
				{
					clearArea( valueArea );
					switchToCheckBox( );
				}
				else if ( PARAM_CONTROL_COMBO.equals( type )
						|| PARAM_CONTROL_LIST.equals( type )
						|| DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals( type ) )
				{
					// Radio ,Combo and List has the same UI
					if ( !PARAM_CONTROL_COMBO.equals( lastControlType )
							&& !PARAM_CONTROL_LIST.equals( lastControlType )
							&& !DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals( lastControlType ) )
					{
						clearArea( valueArea );
						switchToList( );
					}
				}
				else if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals( type ) )
				{
					clearArea( valueArea );
					switchToText( );
				}
				valueArea.layout( );
				initValueArea( );
				lastControlType = type;
			}
		}

		updateCheckBoxArea( );
		updateMessageLine( );
		boolean radioEnable = false;
		if ( PARAM_CONTROL_COMBO.equals( getSelectedControlType( ) )
				|| PARAM_CONTROL_LIST.equals( getSelectedControlType( ) ) )
		{
			radioEnable = true;
		}
		if ( radioEnable != staticRadio.isEnabled( ) )
		{
			staticRadio.setEnabled( radioEnable );
			dynamicRadio.setEnabled( radioEnable );
		}

		if ( PARAM_CONTROL_LIST.equals( getSelectedControlType( ) )
				&& allowMultiValueVisible )
		{
			allowMultiChoice.setVisible( true );
		}
		else
		{
			allowMultiChoice.setVisible( false );
		}

	}

	private void switchParamterType( )
	{
		clearArea( valueArea );
		lastControlType = null;
		if ( isStatic( ) )
		{
			switchToStatic( );
		}
		else
		{
			switchToDynamic( );
		}
		buildControlTypeList( getSelectedDataType( ) );
		valueArea.layout( );
		initValueArea( );
		updateCheckBoxArea( );
	}

	private void switchToCheckBox( )
	{
		createLabel( valueArea, LABEL_DEFAULT_VALUE );
		defaultValueChooser = new Combo( valueArea, SWT.READ_ONLY | SWT.BORDER );
		defaultValueChooser.add( CHOICE_NO_DEFAULT );
		defaultValueChooser.add( BOOLEAN_TRUE );
		defaultValueChooser.add( BOOLEAN_FALSE );
		defaultValueChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		defaultValueChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				switch ( defaultValueChooser.getSelectionIndex( ) )
				{
					case 0 :
						defaultValue = null;
						break;
					case 1 :
						defaultValue = Boolean.toString( true );
						break;
					case 2 :
						defaultValue = Boolean.toString( false );
						break;
				}
				updateMessageLine( );
			}
		} );
	}

	private void switchToList( )
	{
		createLabel( valueArea, LABEL_VALUES );
		Composite tableAreaComposite = new Composite( valueArea, SWT.NONE );
		tableAreaComposite.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		tableAreaComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		tableArea = new TableArea( tableAreaComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER, tableAreaModifier );
		tableArea.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		Table table = tableArea.getTable( );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );

		String[] columns;
		int[] columnWidth;
		columns = new String[]{
				null,
				COLUMN_IS_DEFAULT,
				COLUMN_VALUE,
				COLUMN_DISPLAY_TEXT,
				COLUMN_DISPLAY_TEXT_KEY
		};
		columnWidth = new int[]{
				10, 70, 100, 100, 100
		};

		for ( int i = 0; i < columns.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setResizable( columns[i] != null );
			if ( columns[i] != null )
			{
				column.setText( columns[i] );
			}
			column.setWidth( columnWidth[i] );
		}

		valueTable = tableArea.getTableViewer( );
		valueTable.setColumnProperties( columns );
		valueTable.setContentProvider( contentProvider );
		valueTable.setLabelProvider( labelProvider );
		tableArea.setInput( choiceList );
		valueTable.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				updateTableButtons( );
			}

		} );
		Composite buttonBar = new Composite( tableAreaComposite, SWT.NONE );
		buttonBar.setLayout( UIUtil.createGridLayoutWithoutMargin( 4, false ) );
		buttonBar.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		importValue = new Button( buttonBar, SWT.PUSH );
		importValue.setText( BUTTON_LABEL_IMPORT );
		setButtonLayoutData( importValue );
		// Disabled when no date set defined
		importValue.setEnabled( !inputParameter.getModuleHandle( )
				.getVisibleDataSets( )
				.isEmpty( ) );
		importValue.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				boolean defaultValueRemoved = true;
				String type = getSelectedDataType( );
				List choices = new ArrayList( );
				Map labelMap = new HashMap( );
				for ( Iterator iter = choiceList.iterator( ); iter.hasNext( ); )
				{
					SelectionChoice choice = (SelectionChoice) iter.next( );
					choices.add( choice.getValue( ) );
					if ( choice.getLabel( ) != null )
					{
						labelMap.put( choice.getValue( ), choice.getLabel( ) );
					}
				}
				ImportValueDialog dialog = new ImportValueDialog( type, choices );
				dialog.setValidate( new ImportValueDialog.IAddChoiceValidator( ) {

					public String validateString( String value )
					{
						// TODO Auto-generated method stub
						String errorMessage = isValidValue( value );
						if ( errorMessage != null )
						{
							return errorMessage;
						}
						return null;
					}
				} );

				if ( dialog.open( ) == OK )
				{
					String[] importValues = (String[]) dialog.getResult( );
					choiceList.clear( );
					for ( int i = 0; i < importValues.length; i++ )
					{
						SelectionChoice choice = StructureFactory.createSelectionChoice( );
						choice.setValue( UIUtil.convertToModelString( importValues[i],
								false ) );
						if ( labelMap.get( UIUtil.convertToModelString( importValues[i],
								false ) ) != null )
						{
							choice.setLabel( (String) labelMap.get( importValues[i] ) );
						}
						choiceList.add( choice );
						if ( defaultValue != null
								&& defaultValue.equals( UIUtil.convertToModelString( importValues[i],
										false ) ) )
						{
							defaultValueRemoved = false;
						}
					}
					refreshValueTable( );

					if ( defaultValue != null && defaultValueRemoved )
					{
						changeDefaultValue( null );
					}
				}
			}
		} );
		changeDefault = new Button( buttonBar, SWT.TOGGLE );
		changeDefault.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				SelectionChoice choice = (SelectionChoice) ( (IStructuredSelection) valueTable.getSelection( ) ).getFirstElement( );
				if ( isDefaultChoice( choice ) )
				{
					changeDefaultValue( null );
				}
				else
				{
					changeDefaultValue( choice.getValue( ) );
				}
				refreshValueTable( );
				changeDefault.getParent( ).layout( );
			}
		} );

		int width1 = UIUtil.getStringWidth( BUTTON_LABEL_REMOVE_DEFAULT,
				changeDefault ) + 10;
		int width2 = UIUtil.getStringWidth( BUTTON_LABEL_SET_DEFAULT,
				changeDefault ) + 10;
		int width = width1 >= width2 ? width1 : width2;

		GridData gd = new GridData( );
		gd.widthHint = width;
		changeDefault.setLayoutData( gd );

		createPromptLine( tableAreaComposite );
		updateTableButtons( );
		createSortingArea( valueArea );
	}

	private void switchToText( )
	{
		createDefaultEditor( );
		createLabel( valueArea, null );
		createPromptLine( valueArea );
	}

	private void switchToStatic( )
	{
		changeControlType( );
		listLimit.setEditable( false );
	}

	private void switchToDynamic( )
	{
		Composite composite = new Composite( valueArea, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		composite.setLayoutData( gd );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( 3, false ) );
		createLabel( composite, LABEL_SELECT_DATA_SET );
		dataSetChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		dataSetChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		dataSetChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				refreshColumns( false );
			}

			public void widgetSelected( SelectionEvent e )
			{
				refreshColumns( false );
			}
		}

		);
		createDataSet = new Button( composite, SWT.PUSH );
		createDataSet.setText( BUTTON_CREATE_DATA_SET );
		setButtonLayoutData( createDataSet );
		createDataSet.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				new NewDataSetAction( ).run( );
				refreshDataSets( );
			}

		} );

		createLabel( composite, LABEL_SELECT_VALUE_COLUMN );
		// columnChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		columnChooser = new Combo( composite, SWT.BORDER | SWT.DROP_DOWN );
		columnChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		columnChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				updateButtons( );
			}
		} );

		valueColumnExprButton = new Button( composite, SWT.PUSH );
		// valueColumnExprButton.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( valueColumnExprButton );
		valueColumnExprButton.setToolTipText( Messages.getString( "ParameterDialog.toolTipText.OpenExprButton" ) ); //$NON-NLS-1$
		valueColumnExprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{

				ExpressionBuilder expressionBuilder = new ExpressionBuilder( getExpression( columnChooser.getText( ) ) );
				expressionBuilder.setExpressionProvier( new ParameterExpressionProvider( inputParameter,
						dataSetChooser.getText( ) ) );

				if ( expressionBuilder.open( ) == OK )
				{
					setExpression( columnChooser, expressionBuilder.getResult( )
							.trim( ) );
				}
			}
		} );

		// createLabel( composite, null );
		createLabel( composite, LABEL_SELECT_DISPLAY_TEXT );
		// displayTextChooser = new Combo( composite, SWT.BORDER | SWT.READ_ONLY
		// );
		displayTextChooser = new Combo( composite, SWT.BORDER | SWT.DROP_DOWN );
		displayTextChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Button displayTextExprButton = new Button( composite, SWT.PUSH );
		// displayTextExprButton.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( displayTextExprButton );
		displayTextExprButton.setToolTipText( Messages.getString( "ParameterDialog.toolTipText.OpenExprButton" ) ); //$NON-NLS-1$
		displayTextExprButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{

				ExpressionBuilder expressionBuilder = new ExpressionBuilder( getExpression( displayTextChooser.getText( ) ) );
				expressionBuilder.setExpressionProvier( new ParameterExpressionProvider( inputParameter,
						dataSetChooser.getText( ) ) );

				if ( expressionBuilder.open( ) == OK )
				{
					setExpression( displayTextChooser,
							expressionBuilder.getResult( ).trim( ) );
				}
			}
		} );

		createDefaultEditor( );
		createSortingArea( valueArea );
		createLabel( valueArea, null );
		createPromptLine( valueArea );
		listLimit.setEditable( true );
	}

	private void createSortingArea( Composite parent )
	{
		// Sorting conditions here
		sorttingArea = new Composite( parent, SWT.NONE );
		GridData sorttingAreaGridData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.VERTICAL_ALIGN_END );
		sorttingAreaGridData.horizontalSpan = 2;
		sorttingArea.setLayoutData( sorttingAreaGridData );
		sorttingArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 1, false ) );

		Group sortGroup = new Group( sorttingArea, SWT.NONE );
		sortGroup.setText( LABEL_SORT_GROUP );
		sortGroup.setLayout( new GridLayout( 2, false ) );
		sortGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite sortKeyArea = new Composite( sortGroup, SWT.NONE );
		sortKeyArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sortKeyArea.setLayout( new GridLayout( 2, false ) );
		// createLabel( sortKeyArea, LABEL_SORT_KEY );
		sortKeyLabel = new Label( sortKeyArea, SWT.NONE );
		sortKeyLabel.setText( LABEL_SORT_KEY );
		sortKeyChooser = new Combo( sortKeyArea, SWT.BORDER | SWT.READ_ONLY );
		sortKeyChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sortKeyChooser.add( CHOICE_NONE );
		sortKeyChooser.add( CHOICE_DISPLAY_TEXT );
		sortKeyChooser.add( CHOICE_VALUE_COLUMN );
		sortKeyChooser.setText( CHOICE_NONE );
		sortKeyChooser.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				if ( !( (Combo) e.widget ).getText( ).equals( CHOICE_NONE ) )
				{
					sortDirectionLabel.setEnabled( true );
					sortDirectionChooser.setEnabled( true );
				}
				else
				{
					sortDirectionLabel.setEnabled( false );
					sortDirectionChooser.setEnabled( false );
				}
			}

		} );

		Composite sortDirectionArea = new Composite( sortGroup, SWT.NONE );
		sortDirectionArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sortDirectionArea.setLayout( new GridLayout( 2, false ) );
		// createLabel( sortDirectionArea, LABEL_SORT_DIRECTION );
		sortDirectionLabel = new Label( sortDirectionArea, SWT.NONE );
		sortDirectionLabel.setText( LABEL_SORT_DIRECTION );
		sortDirectionChooser = new Combo( sortDirectionArea, SWT.BORDER
				| SWT.READ_ONLY );
		sortDirectionChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		sortDirectionChooser.add( CHOICE_ASCENDING );
		sortDirectionChooser.add( CHOICE_DESCENDING );
		sortDirectionChooser.setText( CHOICE_ASCENDING );
	}

	private void clearDefaultValueText( )
	{
		if ( defaultValueChooser == null || defaultValueChooser.isDisposed( ) )
			return;
		String textValue = defaultValueChooser.getText( );
		if ( textValue != null && textValue.length( ) == 0 )
		{
			defaultValueChooser.setText( "" ); //$NON-NLS-1$
		}
	}

	private void clearDefaultValueChooserSelections( )
	{
		if ( defaultValueChooser == null || defaultValueChooser.isDisposed( ) )
			return;
		if ( defaultValueChooser.getItemCount( ) > 1 )
		{
			defaultValueChooser.removeAll( );
		}
	}

	private void createDefaultEditor( )
	{
		createLabel( valueArea, LABEL_DEFAULT_VALUE );
		defaultValueChooser = new Combo( valueArea, SWT.BORDER );
		defaultValueChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		if ( !isStatic( ) )
		{
			defaultValueChooser.add( CHOICE_SELECT_VALUE );
		}
		// if ( getSelectedDataType( ).equals(
		// DesignChoiceConstants.PARAM_TYPE_STRING )
		// && !isRequired.getSelection( ) )
		// {
		// defaultValueChooser.add( CHOICE_NULL_VALUE );
		// defaultValueChooser.add( CHOICE_BLANK_VALUE );
		// }

		defaultValueChooser.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				// TODO Auto-generated method stub
				String selection = e.text;
				if ( defaultValueChooser.indexOf( selection ) == -1 )
				{
					e.doit = true;
					return;
				}

				if ( selection.equals( CHOICE_SELECT_VALUE ) )
				{
					e.doit = false;
				}
				else
				{
					e.doit = true;
				}
			}
		} );

		defaultValueChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( defaultValueChooser.getSelectionIndex( ) == -1 )
					return;
				String selection = defaultValueChooser.getItem( defaultValueChooser.getSelectionIndex( ) );
				if ( selection.equals( CHOICE_SELECT_VALUE ) )
				{

					List columnValueList = getColumnValueList( );
					if ( columnValueList.isEmpty( ) )
						return;
					SelectParameterDefaultValueDialog dialog = new SelectParameterDefaultValueDialog( Display.getCurrent( )
							.getActiveShell( ),
							Messages.getString( "SelectParameterDefaultValueDialog.Title" ) ); //$NON-NLS-1$
					dialog.setColumnValueList( columnValueList );
					int status = dialog.open( );
					if ( status == Window.OK )
					{
						String selectedValue = dialog.getSelectedValue( );
						defaultValueChooser.setText( DEUtil.resolveNull( selectedValue ) );
					}
				}
				else
				{
					// if ( selection.equals( CHOICE_NULL_VALUE ) )
					// {
					// changeDefaultValue( null );
					// }
					// else if ( selection.equals( CHOICE_BLANK_VALUE ) )
					// {
					// changeDefaultValue( "" ); //$NON-NLS-1$
					// }
					if ( isStatic( ) )
					{
						refreshValueTable( );
					}
				}
			}
		} );
		defaultValueChooser.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				String value = defaultValueChooser.getText( );
				// if ( value.equals( CHOICE_NULL_VALUE )
				// || value.equals( CHOICE_BLANK_VALUE ) )
				// return;
				changeDefaultValue( UIUtil.convertToModelString( value, false ) );
				if ( isStatic( ) )
				{
					refreshValueTable( );
				}
			}
		} );
	}

	private void createPromptLine( Composite parent )
	{
		promptMessageLine = new Label( parent, SWT.NONE );
		promptMessageLine.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	private Object validateValue( String value ) throws BirtException
	{
		String tempdefaultValue = value;

		if ( !( ( DesignChoiceConstants.PARAM_TYPE_STRING.endsWith( getSelectedDataType( ) ) ) || ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.endsWith( getSelectedDataType( ) ) ) ) )
		{
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( getSelectedDataType( ) ) )
			{
				tempdefaultValue = ParameterUtil.convertToStandardFormat( DataTypeUtil.toDate( tempdefaultValue ) );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( getSelectedDataType( ) ) )
			{
				tempdefaultValue = ParameterUtil.convertToStandardFormat( DataTypeUtil.toSqlDate( tempdefaultValue ) );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( getSelectedDataType( ) ) )
			{
				tempdefaultValue = ParameterUtil.convertToStandardFormat( DataTypeUtil.toSqlTime( tempdefaultValue ) );
			}

			return ParameterValidationUtil.validate( getSelectedDataType( ),
					STANDARD_DATE_TIME_PATTERN,
					tempdefaultValue,
					ULocale.getDefault( ) );

		}
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( getSelectedDataType( ) ) )
		{
			if ( tempdefaultValue != null
					&& tempdefaultValue.equals( CHOICE_NO_DEFAULT ) )
			{
				return DataTypeUtil.toBoolean( null );
			}
			return DataTypeUtil.toBoolean( tempdefaultValue );
		}
		else
			return tempdefaultValue;
	}

	protected void okPressed( )
	{
		// Validate the date first -- begin -- bug 164765
		try
		{
			validateValue( defaultValue );
		}
		catch ( BirtException e1 )
		{
			ExceptionHandler.handle( e1 );
			return;
		}
		// Validate the date first -- end --

		try
		{
			// Save the name and display name
			inputParameter.setName( nameEditor.getText( ) );
			inputParameter.setPromptText( UIUtil.convertToModelString( promptTextEditor.getText( ),
					true ) );

			inputParameter.setParamType( DesignChoiceConstants.SCALAR_PARAM_TYPE_SIMPLE );
			String newControlType = getSelectedControlType( );
			if ( PARAM_CONTROL_COMBO.equals( newControlType ) )
			{
				newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
				// inputParameter.setMustMatch( true );
				inputParameter.setMustMatch( false );
			}
			else if ( PARAM_CONTROL_LIST.equals( newControlType ) )
			{
				newControlType = DesignChoiceConstants.PARAM_CONTROL_LIST_BOX;
				// inputParameter.setMustMatch( false );
				inputParameter.setMustMatch( true );

				if ( allowMultiChoice.isVisible( )
						&& allowMultiChoice.getSelection( ) )
				{
					inputParameter.setParamType( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE );
				}
			}
			else
			{
				inputParameter.setProperty( ScalarParameterHandle.MUCH_MATCH_PROP,
						null );
			}

			// Save control type
			inputParameter.setControlType( newControlType );

			inputParameter.setDefaultValue( defaultValue );

			// Set data type
			inputParameter.setDataType( DATA_TYPE_CHOICE_SET.findChoiceByDisplayName( dataTypeChooser.getText( ) )
					.getName( ) );

			// Clear original choices list
			PropertyHandle selectionChioceList = inputParameter.getPropertyHandle( ScalarParameterHandle.SELECTION_LIST_PROP );
			selectionChioceList.setValue( null );

			if ( isStatic( ) )
			{
				// Save static choices list
				inputParameter.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_STATIC );
				if ( !DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals( newControlType )
						&& !DesignChoiceConstants.PARAM_CONTROL_CHECK_BOX.equals( newControlType ) )
				{
					for ( Iterator iter = choiceList.iterator( ); iter.hasNext( ); )
					{
						SelectionChoice choice = (SelectionChoice) iter.next( );
						if ( isValidValue( choice.getValue( ) ) == null )
						{
							selectionChioceList.addItem( choice );
						}
					}
				}
				inputParameter.setDataSetName( null );
				inputParameter.setValueExpr( null );
				inputParameter.setLabelExpr( null );
			}
			else
			{
				// Save dynamic settings
				inputParameter.setValueType( DesignChoiceConstants.PARAM_VALUE_TYPE_DYNAMIC );
				inputParameter.setDataSetName( dataSetChooser.getText( ) );
				inputParameter.setValueExpr( getExpression( columnChooser.getText( ) ) );

				if ( displayTextChooser.getText( ).equals( LABEL_NULL ) )
				{
					inputParameter.setLabelExpr( "" ); //$NON-NLS-1$
				}
				else
				{
					inputParameter.setLabelExpr( getExpression( displayTextChooser.getText( ) ) );
				}
			}

			// Save help text
			inputParameter.setHelpText( UIUtil.convertToModelString( helpTextEditor.getText( ),
					false ) );

			// Save format
			inputParameter.setCategory( formatCategroy );
			inputParameter.setPattern( formatPattern );

			if ( isStatic( )
					&& ( PARAM_CONTROL_COMBO.equals( getSelectedControlType( ) ) || DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals( getSelectedControlType( ) ) )
					&& !containValue( null, defaultValue, COLUMN_VALUE ) )
			{
				defaultValue = null;
			}

			// Save options
			if ( dirtyProperties.containsKey( CHECKBOX_HIDDEN ) )
			{
				inputParameter.setHidden( getProperty( CHECKBOX_HIDDEN ) );
			}

			if ( dirtyProperties.containsKey( CHECKBOX_ISREQUIRED ) )
			{
				inputParameter.setIsRequired( getProperty( CHECKBOX_ISREQUIRED ) );
			}

			if ( doNotEcho.isEnabled( ) )
			{
				if ( dirtyProperties.containsKey( CHECKBOX_DO_NOT_ECHO ) )
				{
					inputParameter.setConcealValue( getProperty( CHECKBOX_DO_NOT_ECHO ) );
				}
			}
			else
			{
				inputParameter.setProperty( ScalarParameterHandle.CONCEAL_VALUE_PROP,
						null );
			}

			if ( distinct.isEnabled( ) )
			{
				inputParameter.setDistinct( !distinct.getSelection( ) );
			}
			else
			{
				inputParameter.setDistinct( true );
			}

			if ( sorttingArea != null
					&& !sorttingArea.isDisposed( )
					&& sorttingArea.isVisible( ) )
			{
				if ( !sortKeyChooser.getText( ).equals( CHOICE_NONE ) )
				{
					inputParameter.setFixedOrder( false );
					if ( sortKeyChooser.getText( ).equals( CHOICE_DISPLAY_TEXT ) )
					{
						inputParameter.setSortBy( DesignChoiceConstants.PARAM_SORT_VALUES_LABEL );
					}
					else if ( sortKeyChooser.getText( )
							.equals( CHOICE_VALUE_COLUMN ) )
					{
						inputParameter.setSortBy( DesignChoiceConstants.PARAM_SORT_VALUES_VALUE );
					}

					if ( sortDirectionChooser.getText( )
							.equals( CHOICE_ASCENDING ) )
					{
						inputParameter.setSortDirection( DesignChoiceConstants.SORT_DIRECTION_ASC );
					}
					else if ( sortDirectionChooser.getText( )
							.equals( CHOICE_DESCENDING ) )
					{
						inputParameter.setSortDirection( DesignChoiceConstants.SORT_DIRECTION_DESC );
					}
				}
				else
				{
					inputParameter.setFixedOrder( true );
					inputParameter.setSortBy( null );
					inputParameter.setSortDirection( null );
				}
			}
			else
			{
				inputParameter.setProperty( ScalarParameterHandle.FIXED_ORDER_PROP,
						null );
			}

			// Save limits
			if ( !isStatic( ) && !StringUtil.isBlank( listLimit.getText( ) ) )
			{
				try
				{
					inputParameter.setListlimit( Integer.parseInt( listLimit.getText( ) ) );
				}
				catch ( NumberFormatException ex )
				{
					ExceptionHandler.openErrorMessageBox( ERROR_TITLE_INVALID_LIST_LIMIT,
							MessageFormat.format( ERROR_MSG_INVALID_LIST_LIMIT,
									new Object[]{
										Integer.toString( Integer.MAX_VALUE )
									} ) );
				}
			}
			else
			{
				inputParameter.setProperty( ScalarParameterHandle.LIST_LIMIT_PROP,
						null );
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
			return;
		}
		setResult( inputParameter );
		super.okPressed( );
	}

	private void createLabel( Composite parent, String content )
	{
		Label label = new Label( parent, SWT.NONE );
		if ( content != null )
		{
			label.setText( content );
		}
		setLabelLayoutData( label );
	}

	private void setLabelLayoutData( Label label )
	{
		GridData gd = new GridData( );
		if ( label.getText( ).equals( LABEL_VALUES ) )
		{
			gd.verticalAlignment = GridData.BEGINNING;
		}
		label.setLayoutData( gd );

	}

	private void addCheckBoxListener( final Button checkBox, final String key )
	{
		checkBox.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				checkBoxChange( checkBox, key );
			}
		} );
	}

	/**
	 * @param key
	 * @param checkBox
	 * 
	 */
	protected void checkBoxChange( Button checkBox, String key )
	{
		dirtyProperties.put( key, new Boolean( checkBox.getSelection( ) ) );
		if ( CHECKBOX_ISREQUIRED.equals( key )
				|| CHECKBOX_DISTINCT.equals( key ) )
		{
			if ( ( isStatic( ) && !distinct.isEnabled( ) )
					|| ( distinct.isEnabled( ) && !distinct.getSelection( ) ) )
			{
				boolean change = makeUniqueAndValid( );
				if ( change )
				{
					refreshValueTable( );
				}
			}
			if ( getSelectedDataType( ).equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
			{
				clearDefaultValueChooser( checkBox.getSelection( ) );
			}
			updateMessageLine( );
		}
	}

	private void clearDefaultValueChooser( boolean isChecked )
	{
		if ( isChecked )
		{
			clearDefaultValueText( );
			clearDefaultValueChooserSelections( );
		}
		else
		{
			if ( defaultValueChooser == null
					|| defaultValueChooser.isDisposed( )
					|| defaultValueChooser.getItemCount( ) > 1 )
				return;
			// defaultValueChooser.add( CHOICE_NULL_VALUE );
			// defaultValueChooser.add( CHOICE_BLANK_VALUE );
		}
	}

	private void clearArea( Composite area )
	{
		Control[] children = area.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].dispose( );
		}
	}

	private void updateTableButtons( )
	{
		tableArea.updateButtons( );
		boolean isEnable = true;
		SelectionChoice selectedChoice = null;

		if ( valueTable.getSelection( ).isEmpty( ) )
		{
			isEnable = false;
		}
		else
		{
			selectedChoice = (SelectionChoice) ( (IStructuredSelection) valueTable.getSelection( ) ).getFirstElement( );
			String value = selectedChoice.getValue( );
			try
			{
				validateValue( value );
			}
			catch ( BirtException e )
			{
				isEnable = false;
			}
		}
		boolean isDefault = isEnable && isDefaultChoice( selectedChoice );
		if ( isDefault )
		{
			changeDefault.setText( BUTTON_LABEL_REMOVE_DEFAULT );
		}
		else
		{
			changeDefault.setText( BUTTON_LABEL_SET_DEFAULT );
		}

		changeDefault.setSelection( isDefault );
		changeDefault.setEnabled( isEnable );
	}

	private void updateButtons( )
	{
		boolean canFinish = !StringUtil.isBlank( nameEditor.getText( ) );
		if ( canFinish )
		{
			if ( errorMessageLine != null && !errorMessageLine.isDisposed( ) )
			{
				canFinish = ( errorMessageLine.getImage( ) == null );
			}
			if ( columnChooser != null
					&& !columnChooser.isDisposed( )
					&& !isStatic( ) )
			{
				canFinish &= ( getExpression( columnChooser.getText( ) ) != null );
			}
		}
		getOkButton( ).setEnabled( canFinish );
	}

	private void updateCheckBoxArea( )
	{

		// Do not echo check
		if ( DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals( getSelectedControlType( ) ) )
		{
			doNotEcho.setEnabled( true );
			distinct.setEnabled( false );
		}
		else
		{
			doNotEcho.setEnabled( false );
			distinct.setEnabled( true );
		}
	}

	private void updateMessageLine( )
	{
		String errorMessage = validateName( );
		if ( errorMessage == null )
		{
			// 1. No available column error
			if ( !isStatic( )
					&& columnChooser != null
					&& !columnChooser.isDisposed( )
					&& columnChooser.getItemCount( ) == 0 )
			{
				errorMessage = ERROR_MSG_NO_AVAILABLE_COLUMN;
			}

			// 2. No default value error
//			if ( defaultValue == null
//					&& ( PARAM_CONTROL_COMBO.equals( getSelectedControlType( ) ) || DesignChoiceConstants.PARAM_CONTROL_RADIO_BUTTON.equals( getSelectedControlType( ) ) ) )
//			{
//				// if ( isStatic( ) )
//				// {
//				// errorMessage = ( !canBeNull( ) || !containValue( null,
//				// null,
//				// COLUMN_VALUE ) ) ? ERROR_MSG_NO_DEFAULT_VALUE
//				// : null;
//				// }
//				// else
//				// {
//				// errorMessage = canBeNull( ) ? null
//				// : ERROR_MSG_NO_DEFAULT_VALUE;
//				// }
//				errorMessage = canBeNull( ) ? null : ERROR_MSG_NO_DEFAULT_VALUE;
//			}
		}
		if ( errorMessage != null )
		{
			errorMessageLine.setText( errorMessage );
			errorMessageLine.setImage( ERROR_ICON );
		}
		else
		{
			errorMessageLine.setText( "" ); //$NON-NLS-1$
			errorMessageLine.setImage( null );
		}
		if ( promptMessageLine != null && !promptMessageLine.isDisposed( ) )
		{
			if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( getSelectedDataType( ) ) )
			{
				promptMessageLine.setText( LABEL_DATETIME_PROMPT );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( getSelectedDataType( ) ) )
			{
				promptMessageLine.setText( LABEL_DATE_PROMPT );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( getSelectedDataType( ) ) )
			{
				promptMessageLine.setText( LABEL_TIME_PROMPT );
			}
			else
			{
				promptMessageLine.setText( "" ); //$NON-NLS-1$
			}
		}
		updateButtons( );
	}

	private String validateName( )
	{
		String name = nameEditor.getText( ).trim( );
		if ( name.length( ) == 0 )
		{
			return ERROR_MSG_NAME_IS_EMPTY;
		}
		if ( !name.equals( inputParameter.getName( ) )
				&& inputParameter.getModuleHandle( ).findParameter( name ) != null )
		{
			return ERROR_MSG_DUPLICATED_NAME;
		}
		if ( defaultValueChooser != null
				&& ( !defaultValueChooser.isDisposed( ) )
				&& defaultValueChooser.getText( ).length( ) != 0 )
		{
			try
			{
				validateValue( defaultValueChooser.getText( ) );
			}
			catch ( BirtException e )
			{
				return ERROR_MSG_MISMATCH_DATA_TYPE;
			}
		}

		return null;
	}

	private void refreshValueTable( )
	{
		if ( valueTable != null && !valueTable.getTable( ).isDisposed( ) )
		{
			valueTable.refresh( );
			updateTableButtons( );
		}
	}

	private boolean getProperty( String key )
	{
		return ( (Boolean) dirtyProperties.get( key ) ).booleanValue( );
	}

	// private String format( String string )
	// {
	// return ParameterUtil.format( string,
	// getSelectedDataType( ),
	// formatCategroy,
	// formatPattern,
	// canBeNull( ) );
	// }

	/**
	 * Check if the specified value is valid
	 * 
	 * @param value
	 *            the value to check
	 * @return Returns the error message if the input value is invalid,or null
	 *         if it is valid
	 */
	private String isValidValue( String value )
	{
		if ( canBeNull( ) )
		{
			if ( value == null || value.length( ) == 0 )
			{
				return null;
			}
		}
		else
		{
			if ( value == null || value.length( ) == 0 )
			{
				return ERROR_MSG_CANNOT_BE_NULL;
			}
		}
		// bug 153405
		// if ( value == null || value.length( ) == 0 )
		// {
		// return ERROR_MSG_CANNOT_BE_NULL;
		// }
		if ( canBeBlank( ) )
		{
			if ( StringUtil.isBlank( value ) )
			{
				return null;
			}
		}
		else
		{
			if ( StringUtil.isBlank( value ) )
			{
				return ERROR_MSG_CANNOT_BE_BLANK;
			}
		}
		try
		{
			validateValue( value );
		}
		catch ( BirtException e )
		{
			return ERROR_MSG_MISMATCH_DATA_TYPE;
		}
		return null;
	}

	private boolean isEqual( String value1, String value2 )
	{
		Object v1 = null;
		Object v2 = null;
		if ( ( value1 == null && value2 != null )
				|| ( value1 != null && value2 == null ) )
		{
			return false;
		}

		try
		{
			v1 = validateValue( value1 );
			v2 = validateValue( value2 );
		}
		catch ( BirtException e )
		{
			return false;
		}
		if ( v1 == null )
		{
			return v2 == null;
		}
		if ( v1 instanceof Double && v2 instanceof Double )
		{
			return ( (Double) v1 ).compareTo( (Double) v2 ) == 0;
		}
		if ( v1 instanceof BigDecimal && v2 instanceof BigDecimal )
		{
			return ( (BigDecimal) v1 ).compareTo( (BigDecimal) v2 ) == 0;
		}
		if ( v1 instanceof Integer && v2 instanceof Integer )
		{
			return ( (Integer) v1 ).compareTo( (Integer) v2 ) == 0;
		}
		return v1.equals( v2 );
	}

	private IChoiceSet getFormatChoiceSet( String type )
	{
		IChoiceSet choiceSet = null;
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
		{
			choiceSet = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
		{
			choiceSet = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_DATETIME_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
		{
			choiceSet = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_DATE_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
		{
			choiceSet = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_TIME_FORMAT_TYPE );
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type )
				|| DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
		{
			choiceSet = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_NUMBER_FORMAT_TYPE );
		}
		return choiceSet;
	}

	private void updateFormatField( )
	{
		String displayFormat;
		String previewString;
		String type = getSelectedDataType( );
		IChoiceSet choiceSet = getFormatChoiceSet( type );
		if ( choiceSet == null )
		{ // Boolean type;
			displayFormat = DEUtil.getMetaDataDictionary( )
					.getChoiceSet( DesignChoiceConstants.CHOICE_STRING_FORMAT_TYPE )
					.findChoice( DesignChoiceConstants.STRING_FORMAT_TYPE_UNFORMATTED )
					.getDisplayName( );
			previewString = "True"; //$NON-NLS-1$
		}
		else
		{
			if ( formatCategroy == null )
			{
				return;
			}
			displayFormat = choiceSet.findChoice( formatCategroy )
					.getDisplayName( );
			if ( ParameterUtil.isCustomCategory( formatCategroy ) )
			{
				displayFormat += ": " + formatPattern; //$NON-NLS-1$
			}

			if ( type.equals( DesignChoiceConstants.PARAM_TYPE_DATETIME ) )
			{
				previewString = new DateFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: ( formatCategroy.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED ) ? DateFormatter.DATETIME_UNFORMATTED
								: formatCategroy ),
						ULocale.getDefault( ) ).format( new Date( ) );
			}
			else if ( type.equals( DesignChoiceConstants.PARAM_TYPE_DATE ) )
			{
				previewString = new DateFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: ( formatCategroy.equals( DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED ) ? DateFormatter.DATE_UNFORMATTED
								: formatCategroy ),
						ULocale.getDefault( ) ).format( new Date( ) );
			}
			else if ( type.equals( DesignChoiceConstants.PARAM_TYPE_TIME ) )
			{
				previewString = new DateFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: ( formatCategroy.equals( "Unformatted" ) ? DateFormatter.TIME_UNFORMATTED //$NON-NLS-1$
								: formatCategroy ),
						ULocale.getDefault( ) ).format( new Date( ) );
			}
			else if ( type.equals( DesignChoiceConstants.PARAM_TYPE_STRING ) )
			{
				previewString = new StringFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: formatCategroy,
						ULocale.getDefault( ) ).format( Messages.getString( "ParameterDialog.Label.Sample" ) ); //$NON-NLS-1$
			}
			else if ( type.equals( DesignChoiceConstants.PARAM_TYPE_INTEGER ) )
			{
				previewString = new NumberFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: formatCategroy,
						ULocale.getDefault( ) ).format( 1234567890 );
			}
			else
			{
				previewString = new NumberFormatter( ParameterUtil.isCustomCategory( formatCategroy ) ? formatPattern
						: formatCategroy,
						ULocale.getDefault( ) ).format( 123456789.01234 );
			}
		}
		// }
		formatField.setText( displayFormat );
		previewLabel.setText( convertNullString( previewString ) );
		changeFormat.setEnabled( choiceSet != null );
	}

	private String convertNullString( String str )
	{
		if ( str == null )
		{
			return "";//$NON-NLS-1$
		}
		return str;

	}

	private boolean containValue( SelectionChoice selectedChoice,
			String newValue, String property )
	{
		for ( Iterator iter = choiceList.iterator( ); iter.hasNext( ); )
		{
			SelectionChoice choice = (SelectionChoice) iter.next( );
			if ( choice != selectedChoice )
			{
				String value = null;
				if ( COLUMN_VALUE.equals( property ) )
				{
					value = choice.getValue( );
					if ( isEqual( value, newValue ) )
					{
						return true;
					}
				}
				if ( COLUMN_DISPLAY_TEXT_KEY.equals( property ) )
				{
					value = choice.getLabelResourceKey( );
					if ( value == null )
					{
						value = choice.getValue( );
					}
					if ( value == null )
					{
						value = LABEL_NULL;
					}
					if ( value.equals( newValue ) )
					{
						return true;
					}
				}
				if ( COLUMN_DISPLAY_TEXT.equals( property ) )
				{
					value = choice.getLabel( );
					if ( value == null )
					{
						value = choice.getValue( );
					}
					if ( value == null )
					{
						value = LABEL_NULL;
					}
					if ( value.equals( newValue ) )
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private void popupFormatBuilder( boolean refresh )
	{
		String dataType = getSelectedDataType( );
		int formatType;
		if ( DesignChoiceConstants.PARAM_TYPE_BOOLEAN.equals( dataType ) )
		{
			return;
		}
		if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( dataType ) )
		{
			formatType = FormatBuilder.STRING;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( dataType ) )
		{
			formatType = FormatBuilder.DATETIME;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( dataType ) )
		{
			formatType = FormatBuilder.DATE;
		}
		else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( dataType ) )
		{
			formatType = FormatBuilder.TIME;
		}
		else
		{
			formatType = FormatBuilder.NUMBER;
		}
		FormatBuilder formatBuilder = new FormatBuilder( formatType );
		formatBuilder.setInputFormat( formatCategroy, formatPattern );
		formatBuilder.setPreviewText( defaultValue );
		if ( formatBuilder.open( ) == OK )
		{
			formatCategroy = ( (String[]) formatBuilder.getResult( ) )[0];
			formatPattern = ( (String[]) formatBuilder.getResult( ) )[1];
			updateFormatField( );
			if ( refresh )
			{
				refreshValueTable( );
			}
		}
	}

	private boolean canBeBlank( )
	{
		boolean canBeBlank = false;
		// if ( PARAM_CONTROL_LIST.equals( getSelectedControlType( ) )
		// || DesignChoiceConstants.PARAM_CONTROL_TEXT_BOX.equals(
		// getSelectedControlType( ) ) )
		{
			if ( dirtyProperties.containsKey( CHECKBOX_ISREQUIRED ) )
			{
				canBeBlank = !( ( (Boolean) dirtyProperties.get( CHECKBOX_ISREQUIRED ) ).booleanValue( ) );
			}
			else
			{
				canBeBlank = !( inputParameter.isRequired( ) );
			}
		}
		return canBeBlank;
	}

	private boolean canBeNull( )
	{
		boolean canBeNull = true;
		if ( dirtyProperties.containsKey( CHECKBOX_ISREQUIRED ) )
		{
			canBeNull = !( ( (Boolean) dirtyProperties.get( CHECKBOX_ISREQUIRED ) ).booleanValue( ) );
		}
		else
		{
			canBeNull = !( inputParameter.isRequired( ) );
		}
		return canBeNull;
	}

	private boolean isDefaultChoice( SelectionChoice choice )
	{

		String choiceValue = choice.getValue( );
		String defaultValue = convertToStandardFormat( this.defaultValue );
		if ( canBeNull( ) && choiceValue == null && defaultValue == null )
		{
			return true;
		}
		return choiceValue != null
				&& defaultValue != null
				&& isEqual( choiceValue, defaultValue );
	}

	private boolean isStatic( )
	{
		return staticRadio.getSelection( );
	}

	private String convertToStandardFormat( String string )
	{
		if ( string != null
				&& DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( getSelectedDataType( ) ) )
		{
			try
			{
				string = ParameterUtil.convertToStandardFormat( DataTypeUtil.toDate( string,
						ULocale.US ) );
			}
			catch ( BirtException e )
			{
			}
		}
		return string;
	}

	private String getExpression( String columnName )
	{
		if ( columnName.equals( NONE_DISPLAY_TEXT ) )
		{
			return null;
		}
		for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
		{
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next( );
			if ( cachedColumn.getColumnName( ).equals( columnName ) )
			{
				return DEUtil.getExpression( cachedColumn );
			}
		}
		// return null;
		return columnName;
	}

	private String getColumnName( String expression )
	{
		for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
		{
			ResultSetColumnHandle cachedColumn = (ResultSetColumnHandle) iter.next( );
			if ( DEUtil.getExpression( cachedColumn ).equals( expression ) )
			{
				return cachedColumn.getColumnName( );
			}
		}
		// return null;
		return expression;
	}

	private String getInputControlDisplayName( )
	{
		String type = getInputControlType( );
		String displayName = null;
		if ( CONTROL_TYPE_CHOICE_SET.findChoice( type ) != null )
		{
			displayName = CONTROL_TYPE_CHOICE_SET.findChoice( type )
					.getDisplayName( );
		}
		else
		{
			if ( PARAM_CONTROL_COMBO.equals( type ) )
			{
				displayName = DISPLAY_NAME_CONTROL_COMBO;
			}
			else if ( PARAM_CONTROL_LIST.equals( type ) )
			{
				displayName = DISPLAY_NAME_CONTROL_LIST;
			}
		}
		return displayName;
	}

	private String validateChoice( SelectionChoice choice,
			String displayLabelKey, String displayLabel, String value )
	{
		String errorMessage = isValidValue( value );
		if ( errorMessage != null )
		{
			return errorMessage;
		}
		String newValue = convertToStandardFormat( value );
		if ( distinct.isEnabled( ) && distinct.getSelection( ) )
		{
			if ( containValue( choice, displayLabelKey, COLUMN_DISPLAY_TEXT_KEY ) )
			{
				return ERROR_MSG_DUPLICATED_LABELKEY;
			}
			else
				return null;
		}
		if ( containValue( choice, newValue, COLUMN_VALUE ) )
		{
			return ERROR_MSG_DUPLICATED_VALUE;
		}
		if ( ( displayLabel == null && containValue( choice,
				newValue,
				COLUMN_DISPLAY_TEXT ) )
				|| ( containValue( choice, displayLabel, COLUMN_DISPLAY_TEXT ) ) )
		{
			return ERROR_MSG_DUPLICATED_LABEL;
		}
		if ( containValue( choice, displayLabelKey, COLUMN_DISPLAY_TEXT_KEY ) )
		{
			return ERROR_MSG_DUPLICATED_LABELKEY;
		}
		return null;
	}

	private void changeDefaultValue( String value )
	{
		defaultValue = value;
		updateFormatField( );
		updateMessageLine( );
	}

	private void setExpression( Combo chooser, String key )
	{
		chooser.deselectAll( );
		key = StringUtil.trimString( key );
		if ( StringUtil.isBlank( key ) )
		{
			chooser.setText( "" ); //$NON-NLS-1$
			return;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			if ( key.equals( DEUtil.getExpression( columnList.get( i ) ) ) )
			{
				// chooser.select( i );
				chooser.setText( ( (ResultSetColumnHandle) columnList.get( i ) ).getColumnName( ) );
				return;
			}
		}
		chooser.setText( key );
	}

}