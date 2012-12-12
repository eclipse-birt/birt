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

	private static final String NULL_VALUE_STR = ParameterUtil.LABEL_NULL;

	private Composite contentPane;
	private ScrolledComposite scrollPane;

	private List params;
	private Map paramValues = new HashMap( );
	private List<IParameterAdapter> paramAdatpers = new ArrayList( );
	private List isRequiredParameters = new ArrayList( );
	private List dataTypeCheckList = new ArrayList( );

	private boolean performed = false;

	private Map<IParameter, Control> postParamLists = new HashMap<IParameter, Control>( );

	private static IParameterSelectionChoice nullValueChoice = new IParameterSelectionChoice( ) {

		public String getLabel( )
		{
			return NULL_VALUE_STR;
		}

		public Object getValue( )
		{
			return null;
		}

	};

	private static IParameterSelectionChoice blankValueChoice = new IParameterSelectionChoice( ) {

		public String getLabel( )
		{
			return ""; //$NON-NLS-1$
		}

		public Object getValue( )
		{
			return ""; //$NON-NLS-1$
		}
	};

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

	@Override
	protected void okPressed( )
	{
		Iterator itr = isRequiredParameters.iterator( );

		Map paramValues = getParameters( );

		while ( itr.hasNext( ) )
		{
			String paramName = (String) itr.next( );
			Object paramValue = paramValues.get( paramName );

			if ( paramValue == null
					|| ( paramValue instanceof String && ( (String) paramValue ).trim( )
							.length( ) == 0 )
					|| ( paramValue instanceof Object[] && ( (Object[]) paramValue ).length == 0 ) )
			{
				MessageDialog.openError( getShell( ),
						"Error", Messages.getFormattedString( "InputParameterDialog.err.requiredParam", new String[]{paramName} ) ); //$NON-NLS-1$ //$NON-NLS-2$
				return;
			}
		}

		itr = dataTypeCheckList.iterator( );
		while ( itr.hasNext( ) )
		{
			ScalarParameter scalarParameter = (ScalarParameter) itr.next( );
			Object paramValue = paramValues.get( scalarParameter.getHandle( )
					.getName( ) );
			try
			{
				paramValues.put( scalarParameter.getHandle( ).getName( ),
						scalarParameter.converToDataType( paramValue ) );
			}
			catch ( BirtException e )
			{
				MessageDialog.openError( getShell( ),
						Messages.getString( "InputParameterDialog.err.invalidValueTitle" ), //$NON-NLS-1$
						Messages.getFormattedString( "InputParameterDialog.err.invalidValue",
								new String[]{
										paramValue.toString( ),
										scalarParameter.getHandle( )
												.getDataType( )
								} ) );
				return;
			}
		}

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
				return;
			}
		}

		super.okPressed( );
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
		performed = true;

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

		// contentPane.setSize( contentPane.computeSize( SWT.DEFAULT,
		// SWT.DEFAULT ) );
		scrollPane.setMinSize( contentPane.computeSize( 400, SWT.DEFAULT ) );
	}

	private void createParametersSection( List children, Composite parent )
	{
		Iterator iterator = children.iterator( );
		while ( iterator.hasNext( ) )
		{
			Object obj = iterator.next( );
			if ( ( obj instanceof ScalarParameter && !( (ScalarParameter) obj ).getHandle( )
					.isHidden( ) )
					|| ( obj instanceof ScalarParameter && !( (ScalarParameter) obj ).getHandle( )
							.isHidden( ) ) )
			{
				ScalarParameter param = (ScalarParameter) obj;
				createParamSection( param, parent );
			}
			else if ( obj instanceof IParameterAdapter )
			{
				( (IParameterAdapter) obj ).createControl( parent );
				if ( ( (IParameterAdapter) obj ).getHandle( ).isRequired( ) )
				{
					isRequiredParameters.add( ( (IParameterAdapter) obj ).getName( ) );
				}
				paramAdatpers.add( (IParameterAdapter) obj );
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
		boolean isRequired = param.getHandle( ).isRequired( );
		// boolean isStringType = param.getHandle( )
		// .getDataType( )
		// .equals( DesignChoiceConstants.PARAM_TYPE_STRING );
		if ( isRequired )
		{
			isRequiredParameters.add( param.getHandle( ).getName( ) );
		}

		Composite container = new Composite( parent, SWT.NONE );
		container.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout layout = new GridLayout( );
		// layout.numColumns = 2;
		container.setLayout( layout );

		Label label = new Label( container, SWT.NONE );
		label.setText( param.getHandle( ).getDisplayLabel( )
				+ ( isRequired ? ": *" : ":" ) ); //$NON-NLS-1$ //$NON-NLS-2$

		if ( param instanceof StaticTextParameter )
		{
			createStaticTextParameter(container, param);
		}
		else if ( param instanceof RadioParameter )
		{
			createRadioParameter(container, param, isRequired);
		}
		else if ( param instanceof CheckBoxParameter )
		{
			createCheckBoxParameter(container, param, label);
		}
		else if ( param instanceof ListingParameter )
		{
			final ListingParameter listParam = (ListingParameter) param;
			if ( DesignChoiceConstants.SCALAR_PARAM_TYPE_MULTI_VALUE.equals( listParam.getHandle( )
					.getParamType( ) ) )
			{
				createList( container, listParam );
				GridData labelLayout = new GridData( GridData.VERTICAL_ALIGN_BEGINNING );
				label.setLayoutData( labelLayout );
			}
			else
			{
				createCombo( container, listParam );
			}

		}

		return container;
	}
	
	private void createCheckBoxParameter(Composite container,ScalarParameter param,Label label)
	{
		final CheckBoxParameter cbParameter = (CheckBoxParameter) param;

//		paramValues.put( cbParameter.getHandle( ).getName( ), false );
		Object value = getPreSetValue(cbParameter);
//		if ( paramValues.containsKey( cbParameter.getHandle( ).getName( ) ) )
//		{
//			value = getStringFormat( paramValues.get( cbParameter.getHandle( ).getName( ) ) );
//		}

		container.setLayout( GridLayoutFactory.fillDefaults( )
				.numColumns( 2 )
				.create( ) );
		label.setLayoutData( GridDataFactory.fillDefaults( )
				.span( 2, 1 )
				.create( ) );

		Button btnCheck = new Button( container, SWT.CHECK );
		btnCheck.setText( Messages.getString( "InputParameterDialog.boolean.checked" ) );
		btnCheck.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				Button button = (Button) e.getSource( );
				paramValues.put( cbParameter.getHandle( ).getName( ),
						button.getSelection( ) );
			}
		} );
		
		if(value != null ) 
		{
			if((value instanceof Boolean &&  (Boolean)value == true ) || "true".equals(value))
			{
				btnCheck.setSelection(true);
			}
		}
		paramValues.put( cbParameter.getHandle( ).getName( ),	btnCheck.getSelection( ) );
	}
	
	private void createRadioParameter(Composite container,ScalarParameter param,boolean isRequired )
	{
		final RadioParameter radioParameter = (RadioParameter) param;
		Object value = null;
		dataTypeCheckList.add( radioParameter );

		if ( radioParameter.getDefaultValue( ) != null )
		{
			value = radioParameter.getDefaultObject( );
		}

		if ( paramValues.containsKey( radioParameter.getHandle( ).getName( ) ) )
		{
			value = paramValues.get( radioParameter.getHandle( ).getName( ) );
		}

		List list = radioParameter.getValueList( );
		if ( !isRequired )
		{
			list.add( InputParameterDialog.nullValueChoice );
		}
		List<Button> radioItems = new ArrayList<Button>();
		for ( int i = 0; i < list.size( ); i++ )
		{
			if ( i > 0 )
			{
				new Label( container, SWT.NONE );
			}

			IParameterSelectionChoice choice = (IParameterSelectionChoice) list.get( i );
			Button button = new Button( container, SWT.RADIO );
			String choiceLabel = choice.getLabel( );
			if ( choiceLabel == null )
			{
//				choiceLabel = choice.getValue( ) == null ? NULL_VALUE_STR
//						: String.valueOf( choice.getValue( ) );
				choiceLabel = choice.getValue( ) == null ? NULL_VALUE_STR
						: getLabelString(choice,param);
			}
			button.setText( choiceLabel );
			button.setData( choice.getValue( ) );

			if ( choice.getValue( ) != null
					&& choice.getValue( ).equals( value ) )
			{
				button.setSelection( true );
				paramValues.put( radioParameter.getHandle( ).getName( ),
						button.getData( ) );
				clearSelectRadio(radioItems);
			}
			else if ( value == null && choiceLabel.equals( NULL_VALUE_STR ) )
			{
				button.setSelection( true );
				paramValues.remove( radioParameter.getHandle( ).getName( ) );
				clearSelectRadio(radioItems);
			}
			radioItems.add(button);
			
			button.addSelectionListener( new SelectionListener( ) {

				public void widgetDefaultSelected( SelectionEvent e )
				{
				}

				public void widgetSelected( SelectionEvent e )
				{
					Button button = (Button) e.getSource( );
					paramValues.put( radioParameter.getHandle( ).getName( ),
							button.getData( ) );
				}
			} );
		}
	}
	
	private void clearSelectRadio(List<Button> radioItems)
	{
		for(Button b : radioItems)
		{
			b.setSelection(false);
		}
	}
	
	private void createStaticTextParameter(Composite container,ScalarParameter param)
	{
		final StaticTextParameter textParam = (StaticTextParameter) param;
		String value = textParam.getDefaultValue( );
		dataTypeCheckList.add( textParam );

		Text input = new Text( container, param.getHandle( )
				.isConcealValue( ) ? SWT.BORDER | SWT.PASSWORD : SWT.BORDER );
		input.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		input.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				Text input = (Text) e.getSource( );
				paramValues.put( textParam.getHandle( ).getName( ),
						input.getText( ) );
			}
		} );

		if ( paramValues.containsKey( textParam.getHandle( ).getName( ) ) )
		{
			value = getStringFormat( paramValues.get( textParam.getHandle( ).getName( ) ) );
		}

		if ( value != null )
		{
			input.setText( formatString( value, param ) );
		}
	}
	
	private String getStringFormat(Object obj)
	{
		if (obj instanceof Date) {
			try {
				return DataTypeUtil.toString(obj);
			} catch (BirtException e) {
				//do nothing
			}
		}
		return obj.toString();
	}

	private void checkParam( final String defaultValueLabel,
			final Object defaultValue, List list )
	{
		if ( !performed )
		{
			boolean contains = false;
			for ( int i = 0; i < list.size( ); i++ )
			{
				try
				{
					Object obj = ( (IParameterSelectionChoice) ( list.get( i ) ) ).getValue( );
					if ( obj == null )
					{
						continue;
					}
					if ( obj.equals( defaultValue ) )
					{
						contains = true;
						break;
					}
				}
				catch ( Exception e )
				{
				}
			}
			if ( !contains && defaultValue != null )
			{
				IParameterSelectionChoice choice = new IParameterSelectionChoice( ) {

					public String getLabel( )
					{
						return defaultValueLabel;
					}

					public Object getValue( )
					{
						return defaultValue;
					}
				};
				list.add( choice );
			}
		}
	}

	private void createCombo( Composite container,
			final ListingParameter listParam )
	{
		boolean isRequired = listParam.getHandle( ).isRequired( );
		boolean isStringType = listParam.getHandle( )
				.getDataType( )
				.equals( DesignChoiceConstants.PARAM_TYPE_STRING );
		boolean containsBlank = false;
		boolean containsNull = false;

		//get the value when initiate the combo
		Object value = getPreSetValue(listParam);
		int selectIndex = -1;

		int style = SWT.BORDER;
		if ( !( listParam instanceof ComboBoxParameter ) )
		{
			style |= SWT.READ_ONLY;
		}
		Combo combo = new Combo( container, style );
		combo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		combo.setVisibleItemCount( 30 );

		for ( Object o : listParam.getValueList( ) )
		{
			Object choiceValue = ( (IParameterSelectionChoice) o ).getValue( );
			if ( blankValueChoice.getValue( ).equals( choiceValue ) )
				containsBlank = true;
			if ( null == choiceValue )
				containsNull = true;
		}

		List list = new ArrayList( );
		if ( isStringType && !isRequired && !containsBlank )
		{
			list.add( blankValueChoice );
		}
		list.addAll( listParam.getValueList( ) );

		boolean isCascade = isCascadeCombo(listParam);
		if(!isCascade)
		{
			checkParam( formatString( listParam.getDefaultValue( ), listParam ),
					listParam.getDefaultObject( ),
					list );
		}

		if ( !isRequired && !containsNull )
		{

			list.add( InputParameterDialog.nullValueChoice );
		}
		boolean nullAdded = false;
		for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
//			String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
//					: choice.getLabel( ) );
			String label = getLabelString(choice,listParam);
			if ( choice.getValue( ) == null && choice.getLabel( ) == null )
			{
				if ( !isRequired && !nullAdded )
				{
					combo.add( NULL_VALUE_STR );
					combo.setData( String.valueOf(combo.getItemCount() - 1), null );
					nullAdded = true;
				}
			}
			else
			{
				combo.add( label );
				combo.setData( String.valueOf(combo.getItemCount() - 1), choice.getValue( ) );
				if(choice.getValue( ) != null && choice.getValue( ).equals(value) && choice.getLabel() != null && !choice.getLabel().equals(blankValueChoice.getValue()) )
				{
//					value = choice.getLabel();
					selectIndex = combo.getItemCount() -1 ;
				}
			}
		}

		if ( value == null )
		{
			if ( !isRequired )
			{
				combo.select( combo.getItemCount( ) - 1 );
			}
			listParam.setSelectionValue( null );
			paramValues.put( listParam.getHandle( ).getName( ), null );
		}
		else
		{
			 setSelectValueAfterInitCombo(selectIndex,value, combo, listParam,isCascade,list);
		}
		combo.addFocusListener( new FocusListener( ) {

			public void focusGained( FocusEvent e )
			{

			}

			public void focusLost( FocusEvent e )
			{
				if ( !( listParam instanceof ComboBoxParameter ) )
				{
					return;
				}
				Combo combo = (Combo) e.getSource( );
				if ( combo.indexOf( combo.getText( ) ) < 0 )
				{
					try {
						paramValues.put( listParam.getHandle( ).getName( ),
								listParam.converToDataType( combo.getText( ) ) );
					}
					catch (BirtException e1) {
						MessageDialog.openError( getShell( ),
								Messages.getString( "InputParameterDialog.err.invalidValueTitle" ), //$NON-NLS-1$
								Messages.getFormattedString( "InputParameterDialog.err.invalidValue", //$NON-NLS-1$
										new String[]{
												combo.getText( ),
												listParam.getHandle( ).getDataType( )
										} ) );
						return;
					}
				}
				else
				{
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getData( String.valueOf( combo.indexOf( combo.getText( ) ) ) ) );
				}

				if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
				{
					CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
					if ( group.getPostParameter( listParam ) != null )
					{
						cascadingParamValueChanged( group, listParam );
					}
				}
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
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getData( String.valueOf(combo.getSelectionIndex( )) ));
				}
				if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
				{
					CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
					if ( group.getPostParameter( listParam ) != null )
					{
						// try
						// {
						// createParameters( );
						// }
						// catch ( RuntimeException e1 )
						// {
						// e1.printStackTrace( );
						// }
						cascadingParamValueChanged( group, listParam );
					}
				}
			}
		} );

		if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
		{
			CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
			if ( group.getPreParameter( listParam ) != null )
			{
				postParamLists.put( group.getPreParameter( listParam ), combo );
			}
		}
	}
	
	private void setSelectValueAfterInitCombo(int selectIndex,Object value,Combo combo,ListingParameter listParam ,boolean isCascade,List comboDataList)
	{
		boolean found = dealWithValueInComboList(selectIndex,value, combo, listParam);
		if(!found)
		{
//			if ( listParam instanceof ComboBoxParameter )
//			{
				dealWithValueNotInComboList(value, combo, listParam, isCascade, comboDataList);
//			}
//			else
//			{
//				listParam.setSelectionValue( null );
//				paramValues.put( listParam.getHandle( ).getName( ), null );
//			}
		}
	}
	
	//if is cascade combo and the value in the combo data list,then select it
	//if is cascade combo and the value not in the combo data list,then select the first item if items exists
	//if not cascade then set the value data to combo
	private void dealWithValueNotInComboList(Object value,Combo combo,ListingParameter listParam ,boolean isCascade,Object comboData)
	{
		if(!isCascade || (isCascade && isCanSet2Combo(listParam.getDefaultObject( ), comboData)))
		{
			combo.setText( value == null ? NULL_VALUE_STR :value.toString( ) );
			listParam.setSelectionValue( combo.getText( ) );
			paramValues.put( listParam.getHandle( ).getName( ),
					combo.getText( ) );
		}else{
			if(combo.getItemCount()>0)
			{
				combo.select(0);
				listParam.setSelectionValue(  combo.getText( ) );
				paramValues.put( listParam.getHandle( ).getName( ),
						combo.getData( String.valueOf(combo.getSelectionIndex( )) ));
			}else
			{
				paramValues.put( listParam.getHandle( ).getName( ),null);
			}
			
		}
	}
	
	//if value in combo data list ,then select it and return true
	//else do nothing and return false
	private boolean dealWithValueInComboList(int selectIndex,Object value,Combo combo,ListingParameter listParam)
	{
		boolean found = false;
		if(selectIndex > 0)
		{
			combo.select(selectIndex);
			paramValues.put( listParam.getHandle( ).getName( ),
					combo.getData( String.valueOf(combo.getSelectionIndex( )) ));
			listParam.setSelectionValue(value == null ? null : value.toString( ) );
			found = true;
			return found;
		}
		for ( int i = 0; i < combo.getItemCount( ); i++ )
		{
			Object data = combo.getData( String.valueOf(i) );
			if (value == data || ( value != null && value.equals( data ) ) )
			{
				combo.select( i );
				paramValues.put( listParam.getHandle( ).getName( ),
						combo.getData( String.valueOf(combo.getSelectionIndex( )) ));
				listParam.setSelectionValue(value == null ? null : value.toString( ) );
				found = true;
				break;
			}
		}
		return found;		
	}
	
	private Object getPreSetValue(final  ScalarParameter param )
	{
		Object value = null;
		try
		{
			if ( param.getDefaultValue( ) != null )
				value = param.converToDataType( param.getDefaultValue( ) );
			
			if(param.getDefaultValues().size()>0)
			{
				if(param.getDefaultValues().get(0) instanceof Date)
				{
					value = param.getDefaultValues().get(0);
				}
			}
		}
		catch ( BirtException e )
		{
		}

		if ( paramValues.containsKey( param.getHandle( ).getName( ) ) )
		{
			value = paramValues.get( param.getHandle( ).getName( ) );

			if ( value != null )
			{
				param.setSelectionValue( value.toString( ) );
			}
		}
		
		return value;
	}
	
	//used for cascade combo
	private boolean isCascadeCombo(ListingParameter listParam)
	{
		boolean result = false;
		IParameterGroup group = listParam.getParentGroup();
		if(group != null && group instanceof CascadingParameterGroup)
		{
			List child = listParam.getParentGroup().getChildren();
			if(child != null && child.size()>1)
			{
				for(int i =1;i<child.size();i++)
				{
					if(listParam.equals(child.get(i)))
					{
						result = true;
						break;
					}
				}
			}
		}
		return result;
		
	}
	
	//used fo cascade combo
	private boolean isCanSetComboxText(final Object defaultValue, List list )
	{
		boolean result = false;
		for (int i = 0; i < list.size(); i++) {
			try {
				Object obj = ((IParameterSelectionChoice) (list.get(i)))
						.getValue();
				if (obj == null) {
					continue;
				}
				if (obj.equals(defaultValue)) {
					result = true;
					break;
				}
			} catch (Exception e) {
			}
		}
		return result;
	}
	
	private boolean isCanSetComboxText(Object defaultValue,String[] comboItems)
	{
		for(int i = 0;i<comboItems.length;i++)
		{
			
			if ( defaultValue == comboItems[i] ||  defaultValue.equals( comboItems[i] ) ) 
			{
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isCanSet2Combo(Object value,Object comboData)
	{
		boolean result = false;
		if(comboData instanceof List)
		{
			result = isCanSetComboxText(value, (List)comboData);
		}else if(comboData instanceof String[])
		{
			result = isCanSetComboxText(value, (String[])comboData);
		}
		return result;
			
	}

	private void createList( Composite container,
			final ListingParameter listParam )
	{
		boolean isRequired = listParam.getHandle( ).isRequired( );
		boolean isStringType = listParam.getHandle( )
				.getDataType( )
				.equals( DesignChoiceConstants.PARAM_TYPE_STRING );
		boolean containsBlank = false;
		boolean containsNull = false;

		Object value = null;
		try
		{
			value = listParam.converToDataType( listParam.getDefaultValues( ) == null ? new Object[0]
					: listParam.getDefaultValues( ).toArray( ) );
		}
		catch ( BirtException e )
		{
		}

		if ( paramValues.containsKey( listParam.getHandle( ).getName( ) ) )
		{
			value = paramValues.get( listParam.getHandle( ).getName( ) );

			if ( value != null )
			{
				listParam.setSelectionValue( value );
			}
		}

		ListViewer listViewer = new ListViewer( container );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = 70;
		listViewer.getList( ).setLayoutData( gd );

		for ( Object o : listParam.getValueList( ) )
		{
			Object choiceValue = ( (IParameterSelectionChoice) o ).getValue( );
			if ( blankValueChoice.getValue( ).equals( choiceValue ) )
				containsBlank = true;
			if ( null == choiceValue )
				containsNull = true;
		}

		List list = new ArrayList( );
		if ( isStringType && !isRequired && !containsBlank )
		{
			list.add( blankValueChoice );
		}
		list.addAll( listParam.getValueList( ) );
		checkParam( formatString( listParam.getDefaultValue( ), listParam ),
				listParam.getDefaultObject( ),
				list );

		if ( !isRequired && !containsNull )
		{
			list.add( InputParameterDialog.nullValueChoice );
		}

		for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
//			String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
//					: choice.getLabel( ) );
			String label = getLabelString(choice,listParam);
			if ( label != null )
			{
				listViewer.getList( ).add( label );
				listViewer.getList( ).setData( label, choice.getValue( ) );
			}
		}

		if ( value == null && !isRequired )
		{
			listViewer.getList( )
					.select( listViewer.getList( ).getItemCount( ) - 1 );
		}
		else
		{
			initListValue(value, listViewer.getList(), listParam);

		}

		if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
		{
			CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
			if ( group.getPreParameter( listParam ) != null )
			{
				postParamLists.put( group.getPreParameter( listParam ),
						listViewer.getList( ) );
			}
		}

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
					List array = new ArrayList( );
					for ( int i = 0; i < strs.length; i++ )
					{
						Object obj = list.getData( strs[i] );
						array.add( obj );
					}
					Object[] objs = new Object[array.size( )];
					array.toArray( objs );
					paramValues.put( listParam.getHandle( ).getName( ), objs );
				}
				else
				{
					paramValues.remove( listParam.getHandle( ).getName( ) );
				}

				if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
				{
					CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
					if ( group.getPostParameter( listParam ) != null )
					{
						// try
						// {
						// createParameters( );
						// }
						// catch ( RuntimeException e1 )
						// {
						// e1.printStackTrace( );
						// }
						cascadingParamValueChanged( group, listParam );
					}
				}
			}
		} );
	}
	
	private void initListValue(Object value,org.eclipse.swt.widgets.List list,ListingParameter listParam)
	{
		List newValueList = new ArrayList( );
		List oldvalueList = new ArrayList( );

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
		paramValues.put( listParam.getHandle( ).getName( ),
				newValueList.toArray( new Object[newValueList.size( )] ) );
	}

	private void cascadingParamValueChanged( CascadingParameterGroup group,
			ListingParameter listParam )
	{

		clearPostParamList( group, listParam );
		if ( postParamLists.containsKey( listParam )
				&& paramValues.containsKey( listParam.getHandle( ).getName( ) ) )
		{
			Object value = paramValues.get( listParam.getHandle( ).getName( ) );
			listParam.setSelectionValue( value );
			ListingParameter postParam = (ListingParameter) group.getPostParameter( listParam );
			if ( postParam == null )
			{
				return;
			}
			Control control = postParamLists.get( listParam );
			setControlItems( control, new String[0], true );
			int itemIndex = 0;
			for ( Iterator iterator = postParam.getValueList( ).iterator( ); iterator.hasNext( ); )
			{
				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
				if ( choice.getValue( ) == null )
				{
					continue;
				}
//				String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
//						: choice.getLabel( ) );
				String label = getLabelString(choice,listParam);
				if ( label != null )
				{
					addControlItem( control, label );
					if(control instanceof Combo)
					{
						control.setData(String.valueOf(itemIndex),choice.getValue( ) );
						itemIndex++;
					}else
					{						
						control.setData( label, choice.getValue( ) );
					}
				}
			}

			processPostParator( postParam, control );

			cascadingParamValueChanged( group, postParam );
		}
	}

	private void setControlItems( Control control, String[] items,
			boolean copyEmptyValues )
	{

		List list = new ArrayList( );
		list.addAll( Arrays.asList( items ) );

		if ( control instanceof Combo )
		{
			Combo combo = (Combo) control;
			if ( copyEmptyValues )
			{
				if ( combo.indexOf( NULL_VALUE_STR ) > -1 )
				{
					list.add( 0, NULL_VALUE_STR );
				}
				if ( combo.indexOf( blankValueChoice.getLabel( ) ) > -1 )
				{
					list.add( 0, blankValueChoice.getLabel( ) );
				}
			}
			( (Combo) control ).setItems( (String[]) list.toArray( new String[0] ) );
		}
		if ( control instanceof org.eclipse.swt.widgets.List )
		{
			org.eclipse.swt.widgets.List listControl = (org.eclipse.swt.widgets.List) control;
			if ( copyEmptyValues )
			{
				if ( listControl.indexOf( NULL_VALUE_STR ) > -1 )
				{
					list.add( 0, NULL_VALUE_STR );
				}
				if ( listControl.indexOf( blankValueChoice.getLabel( ) ) > -1 )
				{
					list.add( 0, blankValueChoice.getLabel( ) );
				}
			}
			( (org.eclipse.swt.widgets.List) control ).setItems( (String[]) list.toArray( new String[0] ) );
		}
	}

	private void addControlItem( Control control, String item )
	{
		if ( control instanceof Combo )
		{
			( (Combo) control ).add( item );
		}
		if ( control instanceof org.eclipse.swt.widgets.List )
		{
			( (org.eclipse.swt.widgets.List) control ).add( item );
		}
	}

	private void processPostParator( ListingParameter listParam, Control control )
	{
		Object value = paramValues.get( listParam.getHandle( ).getName( ) );

		boolean found = false;
		if ( control instanceof Combo )
		{
			Combo combo = (Combo) control;
			
			found = dealWithValueInComboList(-1,value, combo, listParam);
			
			if ( !found )
			{
				try
				{
					Object obj = listParam.converToDataType( listParam.getDefaultValue( ) );
					if ( obj == null )
					{
						listParam.setSelectionValue( null );
						paramValues.put( listParam.getHandle( ).getName( ),
								null );
					}
					else
					{
						boolean isCascade = isCascadeCombo(listParam);
						dealWithValueNotInComboList(obj, combo, listParam, isCascade, combo.getItems());
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
			initListValue(value, list, listParam);
		}
	}

	private void clearPostParamList( CascadingParameterGroup group,
			IParameter param )
	{
		if ( postParamLists.containsKey( param ) )
		{
			setControlItems( postParamLists.get( param ), new String[0], true );
			clearPostParamList( group, group.getPostParameter( param ) );
		}
	}

	public Map getParameters( )
	{
		for ( IParameterAdapter adapter : this.paramAdatpers )
			this.paramValues.put( adapter.getName( ), adapter.getValue( ) );
		return this.paramValues;
	}

	private String formatString( String str, ScalarParameter para )
	{
		if(str == null || "".equals(str.trim()))//$NON-NLS-1$
		{
			return "";//$NON-NLS-1$
		}
		ScalarParameterHandle paraHandle = para.getHandle( );
		String formatCategroy = paraHandle.getCategory( );
		String formatPattern = paraHandle.getPattern( );
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
		String type = paraHandle.getDataType( );
		formatPattern = isCustom( formatCategroy ) ? formatPattern
				: formatCategroy;
		if ( formatPattern == null )
		{
			return str;
		}
		String formatStr = ""; //$NON-NLS-1$
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
	
	private String getLabelString(IParameterSelectionChoice choice,ScalarParameter para)
	{
		Object value = choice.getValue( );
		String label = choice.getLabel( );
		if(label == null && value != null)
		{
			if(value instanceof Date)
			{
				 try {
					 label = DataTypeUtil.toString( value );
				} catch (BirtException e) {
					// TODO Auto-generated catch block
				}
			}else
			{
				label = String.valueOf( value );
			}
		}
		label = formatString(label, para);
		return label; 
	}
}
