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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.core.format.NumberFormatter;
import org.eclipse.birt.core.format.StringFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.parameters.AbstractParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.CascadingParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.CheckBoxParameter;
import org.eclipse.birt.report.designer.ui.parameters.ComboBoxParameter;
import org.eclipse.birt.report.designer.ui.parameters.IParameter;
import org.eclipse.birt.report.designer.ui.parameters.IParameterAdapter;
import org.eclipse.birt.report.designer.ui.parameters.IParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.ListingParameter;
import org.eclipse.birt.report.designer.ui.parameters.ParameterUtil;
import org.eclipse.birt.report.designer.ui.parameters.RadioParameter;
import org.eclipse.birt.report.designer.ui.parameters.ScalarParameter;
import org.eclipse.birt.report.designer.ui.parameters.StaticTextParameter;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.model.api.FormatValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FormatValue;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.interfaces.IScalarParameterModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * The dialog for inputting report parameter values when previewing report in
 * new preview prototype
 */
public class InputParameterDialog extends BaseDialog
{

	private Composite contentPane;
	private ScrolledComposite scrollPane;

	private List params;
	private Map<String, Object> paramValues = new HashMap<String, Object>( );
	private List<IParameterAdapter> paramAdatpers = new ArrayList( );

	private List<IParameterControlHelper> controlHelpers = new ArrayList<IParameterControlHelper>( );
	private Map<IParameter, SelectionParameterControlHelper> postCascadeParamLists = new HashMap<IParameter, SelectionParameterControlHelper>( );

	public InputParameterDialog( Shell parentShell, List params, Map paramValues )
	{
		super( parentShell,
				Messages.getString( "InputParameterDialog.msg.title" ) ); //$NON-NLS-1$

		this.params = params;
		if ( paramValues != null )
		{
			this.paramValues.putAll( paramValues );
		}
	}

	protected void okPressed( )
	{

		if ( !validateParameters( ) )
		{
			return;
		}

		if ( !validateAdapters( ) )
		{
			return;
		}
		super.okPressed( );
	}

	private boolean validateParameters( )
	{
		for ( IParameterControlHelper helper : controlHelpers )
		{
			if ( !helper.validate( ) )
			{
				return false;
			}
		}
		return true;
	}

	private boolean validateAdapters( )
	{
		for ( IParameterAdapter adapter : this.paramAdatpers )
		{
			try
			{
				adapter.validate( );
			}
			catch ( BirtException e )
			{
				MessageDialog.openError( getShell( ),
						Messages.getString( "InputParameterDialog.err.invalidValueTitle" ), //$NON-NLS-1$
						e.getMessage( ) );
				return false;
			}
		}
		return true;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.marginHeight = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_MARGIN );
		layout.marginWidth = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_MARGIN );
		layout.verticalSpacing = convertVerticalDLUsToPixels( IDialogConstants.VERTICAL_SPACING );
		layout.horizontalSpacing = convertHorizontalDLUsToPixels( IDialogConstants.HORIZONTAL_SPACING );
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		applyDialogFont( composite );

		new Label( composite, SWT.NONE ).setText( Messages.getString( "InputParameterDialog.msg.requiredParam" ) ); //$NON-NLS-1$

		scrollPane = new ScrolledComposite( composite, SWT.H_SCROLL
				| SWT.V_SCROLL );
		scrollPane.setExpandHorizontal( true );
		scrollPane.setExpandVertical( true );

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 400;
		gd.heightHint = 400;
		scrollPane.setLayoutData( gd );

		createParameters( );

		UIUtil.bindHelp( parent, IHelpContextIds.INPUT_PARAMETERS_DIALOG_ID );

		return composite;
	}

	private void createParameters( )
	{
		if ( contentPane != null && !contentPane.isDisposed( ) )
		{
			contentPane.dispose( );
		}

		contentPane = new Composite( scrollPane, SWT.NONE );
		scrollPane.setContent( contentPane );
		contentPane.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contentPane.setLayout( new GridLayout( ) );

		createParametersSection( params, contentPane );

		scrollPane.setMinSize( contentPane.computeSize( 400, SWT.DEFAULT ) );
	}

	private void createParametersSection( List children, Composite parent )
	{
		Iterator iterator = children.iterator( );
		while ( iterator.hasNext( ) )
		{
			Object obj = iterator.next( );
			if ( ( obj instanceof ScalarParameter && !( (ScalarParameter) obj ).getHandle( )
					.isHidden( ) ) )
			{
				ScalarParameter param = (ScalarParameter) obj;
				createParamSection( param, parent );
			}
			else if ( obj instanceof IParameterAdapter )
			{
				IParameterAdapter adapterObj = (IParameterAdapter) obj;
				adapterObj.createControl( parent );
				paramAdatpers.add( adapterObj );
			}
			else if ( obj instanceof AbstractParameterGroup )
			{
				AbstractParameterGroup group = (AbstractParameterGroup) obj;
				createParametersSection( group.getChildren( ),
						createParamGroupSection( group, parent ) );
			}
		}
	}

	private Composite createParamGroupSection(
			AbstractParameterGroup paramGroup, Composite parent )
	{
		Group group = new Group( parent, SWT.NONE );
		group.setText( paramGroup.getHandle( ).getDisplayLabel( ) );
		group.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		group.setLayout( new GridLayout( ) );
		return group;
	}

	private Composite createParamSection( ScalarParameter param,
			Composite parent )
	{

		Composite container = createParamSectionContainer( parent );
		IParameterControlHelper helper = null;
		if ( param instanceof StaticTextParameter )
		{
			helper = new StaticTextParameterControlHelper( this );

		}
		else if ( param instanceof RadioParameter )
		{
			helper = new RadioParameterControlHelper( this );
		}
		else if ( param instanceof CheckBoxParameter )
		{
			helper = new CheckBoxParameterControlHelper( this );
		}
		else if ( param instanceof ListingParameter )
		{
			final ListingParameter listParam = (ListingParameter) param;
			if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals( listParam.getHandle( )
					.getParamType( ) ) )
			{
				helper = new ListParameterControlHelper( this );
			}
			else
			{
				helper = new ComboParameterControlHelper( this );
			}

		}

		helper.createControl( container,
				param,
				paramValues.get( param.getHandle( ).getName( ) ) );

		controlHelpers.add( helper );
		return container;
	}

	private Composite createParamSectionContainer( Composite parent )
	{
		Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout layout = new GridLayout( );
		container.setLayout( layout );

		return parent;
	}

	public Map getParameters( )
	{
		for ( IParameterControlHelper helper : controlHelpers )
		{
			this.paramValues.putAll( helper.getResults( ) );
		}

		for ( IParameterAdapter adapter : this.paramAdatpers )
		{
			this.paramValues.put( adapter.getName( ), adapter.getValue( ) );
		}
		return this.paramValues;
	}

	public Map<IParameter, SelectionParameterControlHelper> getPostParamLists( )
	{
		return this.postCascadeParamLists;
	}

	public void addPostParamter( IParameter parameter,
			SelectionParameterControlHelper helper )
	{
		this.postCascadeParamLists.put( parameter, helper );
	}
}

class InternalParameterSelectionChoice implements IParameterSelectionChoice
{

	private String _label;
	private Object _value;

	public InternalParameterSelectionChoice( String label, Object value )
	{
		this._label = label;
		this._value = value;
	}

	public Object getValue( )
	{
		return _value;
	}

	public String getLabel( )
	{
		return _label;
	}

}

enum InputParameterSelectionChoice implements IParameterSelectionChoice {
	NULLVALUECHOICE(IParameterControlHelper.NULL_VALUE_STR, null), BLANKVALUECHOICE(
			IParameterControlHelper.EMPTY_VALUE_STR,
			IParameterControlHelper.EMPTY_VALUE_STR);

	private final String _label;
	private final Object _value;

	InputParameterSelectionChoice( String label, Object value )
	{
		_label = label;
		_value = value;
	}

	public Object getValue( )
	{
		return _value;
	}

	public String getLabel( )
	{
		return _label;
	}
}

interface IParameterControlHelper
{

	public static final String NULL_VALUE_STR = ParameterUtil.LABEL_NULL;
	public static final String EMPTY_VALUE_STR = "";//$NON-NLS-1$

	void createControl( Composite parent, Object parameter, Object lastValue );

	Map<String, Object> getResults( );

	boolean validate( );
}

abstract class AbstractParameterControlHelper implements
		IParameterControlHelper
{

	protected Composite parent;
	protected ScalarParameter parameter;

	protected boolean isRequired = false;
	protected boolean needTypeCheck = false;
	protected String paramterHandleName;
	protected boolean isStringType = false;

	protected Map<String, Object> lastConfigValues = new HashMap<String, Object>( );
	protected Object defaultValue;

	protected Label controlLabel;

	protected InputParameterDialog parameterDialog;

	public AbstractParameterControlHelper( InputParameterDialog dialog )
	{
		this.parameterDialog = dialog;
	}

	public void createControl( Composite parent, Object para, Object lastValue )
	{
		this.parent = parent;
		init( para, lastValue );

		prepare( );

		createControlLabel( );
		createParameterControl( );
	}

	protected abstract void createParameterControl( );

	protected void prepare( )
	{

	}

	private void initLastValue( Object lastValue )
	{
		if ( lastValue != null )
		{
			putConfigValue( paramterHandleName, lastValue );
		}
	}

	public Map<String, Object> getResults( )
	{
		return lastConfigValues;
	}

	public boolean validate( )
	{
		return validateRequiredItem( ) && validateDataType( );
	}

	protected void putConfigValue( String key, Object value )
	{
		lastConfigValues.put( key, value );
	}

	protected void removeConfigValue( String key )
	{
		lastConfigValues.remove( key );
	}

	private boolean validateRequiredItem( )
	{
		if ( isRequired )
		{
			Object paramterValue = lastConfigValues.get( paramterHandleName );
			if ( paramterValue == null
					|| ( paramterValue instanceof String && StringUtil.isBlank( (String) paramterValue ) )
					|| ( paramterValue instanceof Object[] && ( (Object[]) paramterValue ).length == 0 ) )
			{
				MessageDialog.openError( parent.getShell( ),
						"Error",
						Messages.getFormattedString( "InputParameterDialog.err.requiredParam",
								new String[]{
									paramterHandleName
								} ) ); //$NON-NLS-1$ //$NON-NLS-2$
				return false;
			}
		}
		return true;
	}

	private boolean validateDataType( )
	{
		if ( needTypeCheck )
		{
			Object paramValue = lastConfigValues.get( paramterHandleName );
			try
			{
				Object obj = parameter.converToDataType( paramValue );
				putConfigValue( paramterHandleName, obj );
			}
			catch ( BirtException e )
			{
				MessageDialog.openError( parent.getShell( ),
						Messages.getString( "InputParameterDialog.err.invalidValueTitle" ), //$NON-NLS-1$
						Messages.getFormattedString( "InputParameterDialog.err.invalidValue",//$NON-NLS-1$
								new String[]{
										paramValue.toString( ),
										parameter.getHandle( ).getDataType( )
								} ) );
				return false;
			}
		}
		return true;
	}

	private void init( Object para, Object lastValue )
	{
		parameter = (ScalarParameter) para;
		paramterHandleName = parameter.getHandle( ).getName( );
		isRequired = parameter.getHandle( ).isRequired( );
		isStringType = parameter.getHandle( )
				.getDataType( )
				.equals( DesignChoiceConstants.PARAM_TYPE_STRING );

		initLastValue( lastValue );
		prepareControlDefaultValue( );
	}

	private void createControlLabel( )
	{
		controlLabel = new Label( parent, SWT.NONE );
		controlLabel.setText( parameter.getHandle( ).getDisplayLabel( )
				+ ( isRequired ? ": *" : ":" ) ); //$NON-NLS-1$ //$NON-NLS-2$
	}

	private void prepareControlDefaultValue( )
	{
		defaultValue = getPreSetValue( );
	}

	/**
	 * Only get first value,if default is Multiple values,should not use this
	 * 
	 * @param param
	 * @return
	 */
	private Object getPreSetValue( )
	{
		Object value = null;

		if ( parameter.getDefaultValues( ).size( ) > 0 )
		{
			value = parameter.getDefaultValues( ).get( 0 );
		}

		if ( lastConfigValues.containsKey( paramterHandleName ) )
		{
			value = lastConfigValues.get( paramterHandleName );

			if ( value != null )
			{
				parameter.setSelectionValue( value );
			}
		}

		return value;
	}

	protected String getFormatLabelString( IParameterSelectionChoice choice,
			ScalarParameter para )
	{
		Object value = choice.getValue( );
		String label = choice.getLabel( );
		if ( label == null && value != null )
		{
			if ( value instanceof Date )
			{
				label = formatDate2String( value );
			}
			else
			{
				label = String.valueOf( value );
			}
		}
		label = formatString( label, para );
		return label;
	}

	private String formatDate2String( Object value )
	{
		String result = value.toString( );
		try
		{
			result = DataTypeUtil.toString( value );
		}
		catch ( BirtException e )
		{
		}
		return result;
	}

	protected String formatString( String str, ScalarParameter para )
	{

		if ( StringUtil.isBlank( str ) )
		{
			return EMPTY_VALUE_STR;
		}

		ScalarParameterHandle paraHandle = para.getHandle( );

		String formatPattern = convertFormatPattern( paraHandle );
		ULocale formatLocale = cvonvertFormatLocale( paraHandle );
		String type = paraHandle.getDataType( );

		if ( formatPattern == null )
		{
			return str;
		}
		String formatStr = EMPTY_VALUE_STR;
		try
		{
			if ( DesignChoiceConstants.PARAM_TYPE_STRING.equals( type ) )
			{
				formatStr = new StringFormatter( formatPattern, formatLocale ).format( str );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DATETIME.equals( type ) )
			{
				formatPattern = formatPattern.equals( DesignChoiceConstants.DATETIEM_FORMAT_TYPE_UNFORMATTED ) ? DateFormatter.DATETIME_UNFORMATTED
						: formatPattern;
				formatStr = new DateFormatter( formatPattern, formatLocale ).format( (Date) para.converToDataType( str ) );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DATE.equals( type ) )
			{
				formatPattern = formatPattern.equals( DesignChoiceConstants.DATE_FORMAT_TYPE_UNFORMATTED ) ? DateFormatter.DATE_UNFORMATTED
						: formatPattern;
				formatStr = new DateFormatter( formatPattern, formatLocale ).format( (Date) para.converToDataType( str ) );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_TIME.equals( type ) )
			{
				formatPattern = formatPattern.equals( "Unformatted" ) ? DateFormatter.TIME_UNFORMATTED //$NON-NLS-1$
						: formatPattern;
				formatStr = new DateFormatter( formatPattern, formatLocale ).format( (Date) para.converToDataType( str ) );
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_DECIMAL.equals( type )
					|| DesignChoiceConstants.PARAM_TYPE_FLOAT.equals( type ) )
			{
				double value = Double.parseDouble( str );
				if ( Double.isInfinite( value ) )
					formatStr = str;
				else
				{
					if ( DesignChoiceConstants.NUMBER_FORMAT_TYPE_UNFORMATTED.equals( formatPattern ) )
					{
						formatPattern = null;
					}
					formatStr = new NumberFormatter( formatPattern,
							formatLocale ).format( value );
				}
			}
			else if ( DesignChoiceConstants.PARAM_TYPE_INTEGER.equals( type ) )
			{
				int value = Integer.parseInt( str );
				formatStr = new NumberFormatter( formatPattern, formatLocale ).format( value );
			}
		}
		catch ( Exception e )
		{
			formatStr = str;
		}
		if ( formatStr == null )
		{
			return str;
		}
		return UIUtil.convertToGUIString( formatStr );
	}

	private String convertFormatPattern( ScalarParameterHandle paraHandle )
	{
		String formatCategroy = paraHandle.getCategory( );
		String formatPattern = paraHandle.getPattern( );

		formatPattern = isCustom( formatCategroy ) ? formatPattern
				: formatCategroy;
		return formatPattern;
	}

	private ULocale cvonvertFormatLocale( ScalarParameterHandle paraHandle )
	{
		ULocale formatLocale = null;
		Object formatValue = paraHandle.getProperty( IScalarParameterModel.FORMAT_PROP );
		if ( formatValue instanceof FormatValue )
		{
			PropertyHandle propHandle = paraHandle.getPropertyHandle( IScalarParameterModel.FORMAT_PROP );
			FormatValue formatValueToSet = (FormatValue) formatValue;
			FormatValueHandle formatHandle = (FormatValueHandle) formatValueToSet.getHandle( propHandle );
			formatLocale = formatHandle.getLocale( );
		}
		if ( formatLocale == null )
		{
			formatLocale = ULocale.getDefault( );
		}
		return formatLocale;
	}

	private boolean isCustom( String formatCategroy )
	{
		if ( DesignChoiceConstants.STRING_FORMAT_TYPE_CUSTOM.equals( formatCategroy )
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CUSTOM.equals( formatCategroy )
				|| DesignChoiceConstants.DATETIEM_FORMAT_TYPE_CUSTOM.equals( formatCategroy )
				|| DesignChoiceConstants.DATE_FORMAT_TYPE_CUSTOM.equals( formatCategroy )
				|| DesignChoiceConstants.TIME_FORMAT_TYPE_CUSTOM.equals( formatCategroy )
				|| DesignChoiceConstants.NUMBER_FORMAT_TYPE_CURRENCY.equals( formatCategroy ) )
		{
			return true;
		}
		return false;
	}
}

class StaticTextParameterControlHelper extends AbstractParameterControlHelper
{

	public StaticTextParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	private Text input = null;

	protected void prepare( )
	{
		needTypeCheck = true;
	}

	protected void createParameterControl( )
	{
		createText( );
	}

	private void createText( )
	{
		input = new Text( parent,
				parameter.getHandle( ).isConcealValue( ) ? SWT.BORDER
						| SWT.PASSWORD : SWT.BORDER );
		input.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		input.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Text input = (Text) e.getSource( );
				putConfigValue( paramterHandleName, input.getText( ) );
			}
		} );
		input.setText( getTextDefaultValue( ) );
	}

	private String getTextDefaultValue( )
	{
		String result = EMPTY_VALUE_STR;
		try
		{
			result = DataTypeUtil.toString( defaultValue );
		}
		catch ( BirtException e )
		{
		}
		return formatString( result, parameter );
	}

}

class CheckBoxParameterControlHelper extends AbstractParameterControlHelper
{

	public CheckBoxParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	protected void createParameterControl( )
	{
		createCheckBox( );
	}

	private void createCheckBox( )
	{
		parent.setLayout( GridLayoutFactory.fillDefaults( )
				.numColumns( 2 )
				.create( ) );
		controlLabel.setLayoutData( GridDataFactory.fillDefaults( )
				.span( 2, 1 )
				.create( ) );

		Button btnCheck = new Button( parent, SWT.CHECK );
		btnCheck.setText( Messages.getString( "InputParameterDialog.boolean.checked" ) );
		btnCheck.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				Button button = (Button) e.getSource( );
				putConfigValue( paramterHandleName, button.getSelection( ) );
			}
		} );

		if ( isTrue( defaultValue ) )
		{
			btnCheck.setSelection( true );
		}

		putConfigValue( paramterHandleName, btnCheck.getSelection( ) );
	}

	private boolean isTrue( Object value )
	{
		if ( value == null )
		{
			return false;
		}
		return Boolean.valueOf( value.toString( ) );
	}
}

class RadioParameterControlHelper extends AbstractParameterControlHelper
{

	public RadioParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	protected void createParameterControl( )
	{
		doCreateRadioParameter( );
	}

	protected void prepare( )
	{
		needTypeCheck = true;
		list = parameter.getValueList( );

		boolean isContainNull = false;
		for ( IParameterSelectionChoice choice : list )
		{
			if ( InputParameterSelectionChoice.BLANKVALUECHOICE.getValue( )
					.equals( choice.getValue( ) ) )
			{
				isContainNull = true;
			}
		}

		if ( !isRequired && !isContainNull )
		{
			list.add( InputParameterSelectionChoice.NULLVALUECHOICE );
		}
	}

	private List<IParameterSelectionChoice> list;
	private List<Button> radioItems = new ArrayList<Button>( );

	private void doCreateRadioParameter( )
	{
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( i > 0 )
			{
				new Label( parent, SWT.NONE );
			}
			IParameterSelectionChoice choice = list.get( i );
			String choiceLabel = getChoiceLabel( choice );
			Button button = createRadioButton( choiceLabel, choice.getValue( ) );
			initRadioButton( button, choice, choiceLabel );

		}
	}

	private void initRadioButton( Button button,
			IParameterSelectionChoice choice, String choiceLabel )
	{
		if ( ( choice.getValue( ) == defaultValue )
				|| ( choice.getValue( ) != null && choice.getValue( )
						.equals( defaultValue ) ) )
		{
			button.setSelection( true );
			putConfigValue( paramterHandleName, button.getData( ) );
			clearSelectRadio( radioItems );
		}
		else if ( defaultValue == null
				&& choiceLabel.equals( InputParameterSelectionChoice.NULLVALUECHOICE ) )
		{
			button.setSelection( true );
			removeConfigValue( paramterHandleName );
			clearSelectRadio( radioItems );
		}
		radioItems.add( button );
	}

	private String getChoiceLabel( IParameterSelectionChoice choice )
	{
		String choiceLabel = choice.getLabel( );
		if ( choiceLabel == null )
		{
			choiceLabel = choice.getValue( ) == null ? NULL_VALUE_STR
					: getFormatLabelString( choice, parameter );
		}
		return choiceLabel;
	}

	private Button createRadioButton( String labelText, Object buttonData )
	{
		Button button = new Button( parent, SWT.RADIO );
		button.setText( labelText );
		button.setData( buttonData );
		button.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				Button button = (Button) e.getSource( );
				putConfigValue( paramterHandleName, button.getData( ) );
			}
		} );
		return button;
	}

	private void clearSelectRadio( List<Button> radioItems )
	{
		for ( Button b : radioItems )
		{
			b.setSelection( false );
		}
	}
}

abstract class SelectionParameterControlHelper extends
		AbstractParameterControlHelper
{

	private boolean performed = false;
	boolean isCascade = false;

	protected List<IParameterSelectionChoice> valueList = new ArrayList<IParameterSelectionChoice>( );
	private boolean isCascadeGroup;
	private CascadingParameterGroup cascadeGroup;

	protected List<String> controlResetItems = new ArrayList<String>( );

	public SelectionParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	abstract Control getControl( );

	protected void prepare( )
	{
		super.prepare( );
		prepareValueList( );
		initCascadeGroup( );
	}

	private void initCascadeGroup( )
	{
		if ( parameter.getParentGroup( ) instanceof CascadingParameterGroup )
		{
			isCascadeGroup = true;
			cascadeGroup = (CascadingParameterGroup) parameter.getParentGroup( );
		}
	}

	protected void cascadingParamValueChanged( CascadingParameterGroup group,
			ScalarParameter listParam )
	{

		if ( listParam == null )
		{
			return;
		}

		if ( getPostParamList( ).containsKey( listParam ) )
		{
			copeWithCurrentParameter( listParam );
			ScalarParameter postParam = copeWithNextParameter( group, listParam );
			cascadingParamValueChanged( group, postParam );
		}
	}

	private void copeWithCurrentParameter( ScalarParameter currentParam )
	{
		Object value = lastConfigValues.get( currentParam.getHandle( )
				.getName( ) );
		currentParam.setSelectionValue( value );
	}

	private ScalarParameter copeWithNextParameter(
			CascadingParameterGroup group, ScalarParameter listParam )
	{
		ScalarParameter postParam = (ListingParameter) group.getPostParameter( listParam );
		if ( postParam == null )
		{
			return null;
		}
		SelectionParameterControlHelper helper = getPostParamList( ).get( listParam );
		helper.emptyControlItem( );

		Control control = helper.getControl( );
		int itemIndex = 0;
		for ( Iterator iterator = postParam.getValueList( ).iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
			if ( choice.getValue( ) == null )
			{
				continue;
			}

			String label = getFormatLabelString( choice, listParam );
			if ( label != null )
			{
				itemIndex = addControlItem( control, label );
				if ( control instanceof Combo )
				{
					control.setData( String.valueOf( itemIndex ),
							choice.getValue( ) );
				}
				else
				{
					control.setData( label, choice.getValue( ) );
				}
			}
		}

		processPostParator( postParam, control );
		return postParam;
	}

	private int addControlItem( Control control, String item )
	{
		int itemIndex = 0;
		if ( control instanceof Combo )
		{
			( (Combo) control ).add( item );
			itemIndex = ( (Combo) control ).getItemCount( ) - 1;
		}
		if ( control instanceof org.eclipse.swt.widgets.List )
		{
			( (org.eclipse.swt.widgets.List) control ).add( item );
			itemIndex = ( (org.eclipse.swt.widgets.List) control ).getItemCount( ) - 1;
		}
		return itemIndex;
	}

	private void processPostParator( ScalarParameter listParam, Control control )
	{
		Object value = lastConfigValues.get( listParam.getHandle( ).getName( ) );
		boolean found = false;
		if ( control instanceof Combo )
		{
			Combo combo = (Combo) control;
			found = dealWithValueInComboList( -1, value, combo, listParam );

			if ( !found )
			{
				try
				{
					Object obj = listParam.converToDataType( listParam.getDefaultObject( ) );
					if ( obj == null )
					{
						listParam.setSelectionValue( null );
						putConfigValue( listParam.getHandle( ).getName( ), null );
					}
					else
					{
						boolean isCascade = isCascadeParameter( listParam );
						dealWithValueNotInComboList( obj,
								combo,
								listParam,
								isCascade,
								combo.getItems( ) );
					}
				}
				catch ( BirtException e )
				{
					//
				}

			}
		}
		if ( control instanceof org.eclipse.swt.widgets.List )
		{
			org.eclipse.swt.widgets.List list = (org.eclipse.swt.widgets.List) control;
			initListValue( value, list, listParam );
		}
	}

	/**
	 * If the combo is cascade then: If the value in the combo data list: then
	 * select the value else: then select the first item if items exists else :
	 * then set the value to combo
	 * 
	 * @param value
	 * @param combo
	 * @param listParam
	 * @param isCascade
	 * @param comboData
	 */
	private void dealWithValueNotInComboList( Object value, Combo combo,
			ScalarParameter listParam, boolean isCascade, Object comboData )
	{
		if ( !isCascade
				|| ( isCascade && isContainValue( listParam.getDefaultObject( ),
						comboData ) ) )
		{
			combo.setText( value == null ? NULL_VALUE_STR : value.toString( ) );
			listParam.setSelectionValue( combo.getText( ) );
			putConfigValue( listParam.getHandle( ).getName( ), combo.getText( ) );
		}
		else
		{
			if ( combo.getItemCount( ) > 0 )
			{
				combo.select( 0 );
				listParam.setSelectionValue( combo.getText( ) );
				putConfigValue( listParam.getHandle( ).getName( ),
						combo.getData( String.valueOf( combo.getSelectionIndex( ) ) ) );
			}
			else
			{
				putConfigValue( listParam.getHandle( ).getName( ), null );
			}

		}
	}

	/**
	 * 
	 * set the default selected item in combo
	 * 
	 * @param selectIndex
	 *            :indicate which item will be selected
	 * @param combo
	 *            : Combo
	 * 
	 */
	protected void setSelectValueAfterInitCombo( int selectIndex, Combo combo )
	{
		boolean found = dealWithValueInComboList( selectIndex,
				defaultValue,
				combo,
				parameter );
		if ( !found )
		{
			dealWithValueNotInComboList( defaultValue,
					combo,
					parameter,
					isCascade,
					valueList );
		}
	}

	/**
	 * If value in combo data list ,then select it and return true; else do
	 * nothing and return false
	 * 
	 * @param selectIndex
	 *            : indicate which item will be selected
	 * @param value
	 *            : the value which will be selected
	 * @param combo
	 * @param listParam
	 * @return
	 */

	private boolean dealWithValueInComboList( int selectIndex, Object value,
			Combo combo, ScalarParameter listParam )
	{
		boolean found = false;
		if ( selectIndex > 0 )
		{
			combo.select( selectIndex );
			putConfigValue( listParam.getHandle( ).getName( ),
					combo.getData( String.valueOf( combo.getSelectionIndex( ) ) ) );
			listParam.setSelectionValue( lastConfigValues.get( listParam.getHandle( ).getName( ) ) );
			found = true;
			return found;
		}
		for ( int i = 0; i < combo.getItemCount( ); i++ )
		{
			Object data = combo.getData( String.valueOf( i ) );
			if ( value == data || ( value != null && value.equals( data ) ) )
			{
				combo.select( i );
				putConfigValue( listParam.getHandle( ).getName( ),
						combo.getData( String.valueOf( combo.getSelectionIndex( ) ) ) );
				listParam.setSelectionValue( lastConfigValues.get( listParam.getHandle( ).getName( ) ) );
				found = true;
				break;
			}
		}
		return found;
	}

	protected void initListValue( Object value,
			org.eclipse.swt.widgets.List list, ScalarParameter listParam )
	{
		List<Object> newValueList = new ArrayList<Object>( );
		List<Object> oldvalueList = new ArrayList<Object>( );

		if ( value instanceof Object[] )
		{
			oldvalueList = Arrays.asList( (Object[]) value );
		}
		else
		{
			oldvalueList.add( value );
		}

		for ( int i = 0; i < list.getItemCount( ); i++ )
		{
			Object item = list.getData( list.getItem( i ) );
			if ( oldvalueList.indexOf( item ) >= 0 )
			{
				list.select( i );
				newValueList.add( list.getData( list.getItem( i ) ) );
			}
		}
		putConfigValue( listParam.getHandle( ).getName( ),
				newValueList.toArray( new Object[newValueList.size( )] ) );
	}

	protected void doWithCascadeGroup( )
	{
		if ( isCascadeGroup )
		{
			if ( cascadeGroup.getPostParameter( parameter ) != null )
			{
				cascadingParamValueChanged( cascadeGroup, parameter );
			}
		}
	}

	protected void prepareValueList( )
	{
		boolean containsBlank = false;
		boolean containsNull = false;

		for ( Object o : parameter.getValueList( ) )
		{
			Object choiceValue = ( (IParameterSelectionChoice) o ).getValue( );
			if ( InputParameterSelectionChoice.BLANKVALUECHOICE.getValue( )
					.equals( choiceValue ) )
				containsBlank = true;
			if ( null == choiceValue )
				containsNull = true;
		}

		if ( isStringType && !isRequired && !containsBlank )
		{
			valueList.add( InputParameterSelectionChoice.BLANKVALUECHOICE );
		}
		valueList.addAll( parameter.getValueList( ) );

		dealCascade( );

		if ( !isRequired && !containsNull )
		{
			valueList.add( InputParameterSelectionChoice.NULLVALUECHOICE );
		}
	}

	/**
	 * If parameter is in cascade group and the parameter not the first item in
	 * group : then do nothing; else then if default value not in value
	 * list,then add it to value list.
	 */
	private void dealCascade( )
	{
		isCascade = isCascadeParameter( parameter );
		if ( !isCascade )
		{
			addDefaultToValueList( formatString( parameter.getDefaultValue( ),
					parameter ),
					parameter.getDefaultObject( ),
					valueList );
		}
	}

	/**
	 * If defaultValue is not null and it is in the list, then construct a new
	 * SelectionChoice with defaultValue and add it to the list
	 * 
	 * @param defaultValueLabel
	 * @param defaultValue
	 * @param list
	 */
	private void addDefaultToValueList( final String defaultValueLabel,
			final Object defaultValue, List list )
	{
		if ( performed )
		{
			return;
		}
		boolean contains = isContainValue( defaultValue, list );

		if ( !contains && defaultValue != null )
		{
			IParameterSelectionChoice choice = new InternalParameterSelectionChoice( defaultValueLabel,
					defaultValue );
			list.add( choice );
		}
		performed = true;
	}

	/**
	 * To check that whether the defaultValue is in the value list If in
	 * list,return true;else return false.
	 * 
	 * @param defaultValue
	 * @param list
	 * @return
	 */
	private boolean isContainValue( Object defaultValue, List list )
	{
		boolean result = false;
		for ( int i = 0; i < list.size( ); i++ )
		{
			Object obj = ( (IParameterSelectionChoice) ( list.get( i ) ) ).getValue( );
			if ( obj != null && obj.equals( defaultValue ) )
			{
				result = true;
				break;
			}
		}
		return result;
	}

	private boolean isContainValue( Object defaultValue, String[] items )
	{
		for ( int i = 0; i < items.length; i++ )
		{
			if ( defaultValue == items[i] || defaultValue.equals( items[i] ) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * If the value is in the data,return true;else return false
	 * 
	 * @param value
	 * @param comboData
	 * @return
	 */
	protected boolean isContainValue( Object value, Object data )
	{
		boolean result = false;
		if ( data instanceof List )
		{
			result = isContainValue( value, (List) data );
		}
		else if ( data instanceof String[] )
		{
			result = isContainValue( value, (String[]) data );
		}
		return result;

	}

	/**
	 * test whether this ScalarParameter is used in cascade group If the param
	 * is the first item of group,then return false.
	 * 
	 * @param listParam
	 * @return
	 */
	private boolean isCascadeParameter( ScalarParameter param )
	{
		boolean result = false;
		IParameterGroup group = param.getParentGroup( );
		if ( group != null && group instanceof CascadingParameterGroup )
		{
			List child = param.getParentGroup( ).getChildren( );
			if ( child != null && child.size( ) > 1 )
			{
				for ( int i = 1; i < child.size( ); i++ )
				{
					if ( param.equals( child.get( i ) ) )
					{
						result = true;
						break;
					}
				}
			}
		}
		return result;
	}

	protected void addCascadeParamterRelation( )
	{
		if ( isCascadeGroup )
		{
			if ( cascadeGroup.getPreParameter( parameter ) != null )
			{
				parameterDialog.addPostParamter( cascadeGroup.getPreParameter( parameter ),
						this );
			}
		}
	}

	private Map<IParameter, SelectionParameterControlHelper> getPostParamList( )
	{
		return parameterDialog.getPostParamLists( );
	}

	protected void addItemForControlReset( String data )
	{
		if ( EMPTY_VALUE_STR.equals( data ) || NULL_VALUE_STR.equals( data ) )
		{
			controlResetItems.add( 0, data );
		}
	}

	private void emptyControlItem( )
	{
		if ( getControl( ) instanceof Combo )
		{
			( (Combo) getControl( ) ).setItems( controlResetItems.toArray( new String[controlResetItems.size( )] ) );
		}
		else if ( getControl( ) instanceof org.eclipse.swt.widgets.List )
		{
			( (org.eclipse.swt.widgets.List) getControl( ) ).setItems( controlResetItems.toArray( new String[controlResetItems.size( )] ) );
		}
	}
}

class ComboParameterControlHelper extends SelectionParameterControlHelper
{

	public ComboParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	private Combo combo;

	Control getControl( )
	{
		return combo;
	}

	protected void createParameterControl( )
	{
		createCombo( );
	}

	private void createCombo( )
	{
		createRawCombo( );
		initCombo( );
	}

	private void createRawCombo( )
	{
		int style = SWT.BORDER;
		if ( !( parameter instanceof ComboBoxParameter ) )
		{
			style |= SWT.READ_ONLY;
		}
		combo = new Combo( parent, style );
		combo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		combo.setVisibleItemCount( 30 );

		combo.addFocusListener( new FocusListener( ) {

			public void focusGained( FocusEvent e )
			{

			}

			public void focusLost( FocusEvent e )
			{
				if ( !( parameter instanceof ComboBoxParameter ) )
				{
					return;
				}
				Combo combo = (Combo) e.getSource( );
				if ( combo.indexOf( combo.getText( ) ) < 0 )
				{
					try
					{
						putConfigValue( paramterHandleName,
								parameter.converToDataType( combo.getText( ) ) );
					}
					catch ( BirtException e1 )
					{
						MessageDialog.openError( combo.getShell( ),
								Messages.getString( "InputParameterDialog.err.invalidValueTitle" ), //$NON-NLS-1$
								Messages.getFormattedString( "InputParameterDialog.err.invalidValue", //$NON-NLS-1$
										new String[]{
												combo.getText( ),
												parameter.getHandle( )
														.getDataType( )
										} ) );
						return;
					}
				}
				else
				{
					putConfigValue( paramterHandleName,
							combo.getData( String.valueOf( combo.indexOf( combo.getText( ) ) ) ) );
				}

				// doWithCascadeGroup();
			}
		} );

		combo.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				Combo combo = (Combo) e.getSource( );
				if ( combo.getSelectionIndex( ) != -1 )
				{
					putConfigValue( paramterHandleName,
							combo.getData( String.valueOf( combo.getSelectionIndex( ) ) ) );
				}
				doWithCascadeGroup( );
			}
		} );
	}

	private void initCombo( )
	{
		int selectIndex = -1;
		boolean nullAdded = false;

		// add data to combo,compare whether the default value in data list
		for ( IParameterSelectionChoice choice : valueList )
		{
			String label = getFormatLabelString( choice, parameter );
			if ( choice.getValue( ) == null && choice.getLabel( ) == null )
			{
				if ( !isRequired && !nullAdded )
				{
					combo.add( NULL_VALUE_STR );
					combo.setData( String.valueOf( combo.getItemCount( ) - 1 ),
							null );
					addItemForControlReset( NULL_VALUE_STR );
					nullAdded = true;
				}
			}
			else
			{
				combo.add( label );
				combo.setData( String.valueOf( combo.getItemCount( ) - 1 ),
						choice.getValue( ) );

				addItemForControlReset( label );
				// If this choice is the default value,mark current choice index
				if ( choice.getValue( ) != null
						&& choice.getValue( ).equals( defaultValue )
						&& choice.getLabel( ) != null
						&& !choice.getLabel( )
								.equals( InputParameterSelectionChoice.BLANKVALUECHOICE.getValue( ) ) )
				{
					selectIndex = combo.getItemCount( ) - 1;
				}
			}
		}

		if ( defaultValue == null )
		{
			if ( !isRequired )
			{
				combo.select( combo.getItemCount( ) - 1 );
			}
			parameter.setSelectionValue( null );
			putConfigValue( paramterHandleName, null );
		}
		else
		{
			setSelectValueAfterInitCombo( selectIndex, combo );
		}

		// deal cascade
		addCascadeParamterRelation( );
	}

}

class ListParameterControlHelper extends SelectionParameterControlHelper
{

	private ListViewer listViewer;

	Control getControl( )
	{
		return listViewer.getList( );
	}

	public ListParameterControlHelper( InputParameterDialog dialog )
	{
		super( dialog );
	}

	protected void createParameterControl( )
	{
		initControlLabelLayout( );
		createListParamer( );
	}

	public void createListParamer( )
	{
		createRawListViewer( );
		initListViewer( );
	}

	private void initControlLabelLayout( )
	{
		GridData labelLayout = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
		controlLabel.setLayoutData( labelLayout );
	}

	private void createRawListViewer( )
	{
		listViewer = new ListViewer( parent );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = 70;
		listViewer.getList( ).setLayoutData( gd );

		listViewer.getList( ).addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				org.eclipse.swt.widgets.List list = (org.eclipse.swt.widgets.List) e.getSource( );

				String[] strs = list.getSelection( );
				if ( strs != null && strs.length > 0 )
				{
					Object[] selectDatas = new Object[strs.length];
					for ( int i = 0; i < strs.length; i++ )
					{
						selectDatas[i] = list.getData( strs[i] );
					}
					putConfigValue( paramterHandleName, selectDatas );
				}
				else
				{
					removeConfigValue( paramterHandleName );
				}

				doWithCascadeGroup( );
			}
		} );
	}

	private void initListViewer( )
	{
		for ( Iterator iterator = valueList.iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
			String label = getFormatLabelString( choice, parameter );
			if ( label != null )
			{
				listViewer.getList( ).add( label );
				listViewer.getList( ).setData( label, choice.getValue( ) );
				addItemForControlReset( label );
			}
		}

		if ( defaultValue == null && !isRequired )
		{
			listViewer.getList( )
					.select( listViewer.getList( ).getItemCount( ) - 1 );
		}
		else
		{
			initListValue( defaultValue, listViewer.getList( ), parameter );
		}

		addCascadeParamterRelation( );
	}
}
