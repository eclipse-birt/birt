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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.parameters.AbstractParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.CascadingParameterGroup;
import org.eclipse.birt.report.designer.ui.parameters.ComboBoxParameter;
import org.eclipse.birt.report.designer.ui.parameters.IParameter;
import org.eclipse.birt.report.designer.ui.parameters.IParameterAdapter;
import org.eclipse.birt.report.designer.ui.parameters.ListingParameter;
import org.eclipse.birt.report.designer.ui.parameters.ParameterUtil;
import org.eclipse.birt.report.designer.ui.parameters.RadioParameter;
import org.eclipse.birt.report.designer.ui.parameters.ScalarParameter;
import org.eclipse.birt.report.designer.ui.parameters.StaticTextParameter;
import org.eclipse.birt.report.engine.api.IParameterSelectionChoice;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
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
	private Map dynamicParamValues = new HashMap( );
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
			String paramValue = (String) paramValues.get( scalarParameter.getHandle( )
					.getName( ) );
			try
			{
				paramValues.put( scalarParameter.getHandle( ).getName( ),
						scalarParameter.converToDataType( paramValue ) );
			}
			catch ( BirtException e )
			{
				MessageDialog.openError( getShell( ), "Invalid value type", //$NON-NLS-1$
						"The value \"" //$NON-NLS-1$
								+ paramValue
								+ "\" is invalid with type " //$NON-NLS-1$
								+ scalarParameter.getHandle( ).getDataType( ) );
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
			final StaticTextParameter textParam = (StaticTextParameter) param;
			String value = textParam.getDefaultValue( );
			dataTypeCheckList.add( textParam );

			Text input = new Text( container, SWT.BORDER );
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
				value = paramValues.get( textParam.getHandle( ).getName( ) )
						.toString( );
			}

			if ( value != null )
			{
				input.setText( value );
			}
		}
		else if ( param instanceof RadioParameter )
		{
			final RadioParameter radioParameter = (RadioParameter) param;
			Object value = null;

			try
			{
				value = radioParameter.converToDataType( radioParameter.getDefaultValue( ) );
			}
			catch ( BirtException e )
			{
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
					choiceLabel = choice.getValue( ) == null ? NULL_VALUE_STR
							: String.valueOf( choice.getValue( ) );
				}
				button.setText( choiceLabel );
				button.setData( choice.getValue( ) );

				if ( choice.getValue( ) != null
						&& choice.getValue( ).equals( value ) )
				{
					button.setSelection( true );
					paramValues.put( radioParameter.getHandle( ).getName( ),
							button.getData( ) );
				}
				else if ( value == null && choiceLabel.equals( NULL_VALUE_STR ) )
				{
					button.setSelection( true );
					paramValues.remove( radioParameter.getHandle( ).getName( ) );
				}

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

	private void checkParam( final String defaultValue, List list )
	{
		if ( !performed )
		{
			boolean contains = false;
			for ( int i = 0; i < list.size( ); i++ )
			{
				try
				{
					if ( ( (IParameterSelectionChoice) ( list.get( i ) ) ).getValue( )
							.toString( )
							.equals( defaultValue ) )
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
						return defaultValue;
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
		Object value = null;
		try
		{
			if ( listParam.getDefaultValues( ) != null )
				value = listParam.converToDataType( listParam.getDefaultValue( ) );
		}
		catch ( BirtException e )
		{
		}

		if ( paramValues.containsKey( listParam.getHandle( ).getName( ) ) )
		{
			value = paramValues.get( listParam.getHandle( ).getName( ) );

			if ( value != null )
			{
				listParam.setSelectionValue( value.toString( ) );
			}
		}
		int style = SWT.BORDER;
		if ( !( listParam instanceof ComboBoxParameter ) )
		{
			style |= SWT.READ_ONLY;
		}
		Combo combo = new Combo( container, style );
		combo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		combo.setVisibleItemCount( 30 );
		List list = new ArrayList( );
		if ( isStringType && !isRequired )
		{
			list.add( blankValueChoice );
		}
		list.addAll( listParam.getValueList( ) );
		checkParam( listParam.getDefaultValue( ), list );
		if ( !isRequired )
		{
			boolean hasNull = false;
			for ( int i = 0; i < list.size( ); i++ )
			{
				IParameterSelectionChoice choice = (IParameterSelectionChoice) list.get( i );
				if ( choice.getValue( ) == null )
				{
					hasNull = true;
				}
			}
			if ( !hasNull )
			{
				list.add( InputParameterDialog.nullValueChoice );
			}
		}
		boolean nullAdded = false;
		for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
			String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
					: choice.getLabel( ) );
			if ( choice.getValue( ) == null && choice.getLabel( ) == null )
			{
				if ( !isRequired && !nullAdded )
				{
					combo.add( NULL_VALUE_STR );
					combo.setData( NULL_VALUE_STR, null );
					nullAdded = true;
				}
			}
			else
			{
				combo.add( label );
				combo.setData( label, choice.getValue( ) );
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
			boolean found = false;
			for ( int i = 0; i < combo.getItemCount( ); i++ )
			{
				if ( value.equals( combo.getData( combo.getItem( i ) ) ) )
				{
					combo.select( i );
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getData( combo.getItem( i ) ) );
					listParam.setSelectionValue( value.toString( ) );
					found = true;
					break;
				}
			}
			if ( !found )
			{
				if ( listParam instanceof ComboBoxParameter )
				{
					combo.setText( value.toString( ) );
					listParam.setSelectionValue( combo.getText( ) );
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getText( ) );
				}
				else
				{
					listParam.setSelectionValue( null );
					paramValues.put( listParam.getHandle( ).getName( ), null );
				}

			}
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
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getText( ) );
				}
				else
				{
					paramValues.put( listParam.getHandle( ).getName( ),
							combo.getData( combo.getItem( combo.indexOf( combo.getText( ) ) ) ) );
				}

				if ( listParam.getParentGroup( ) instanceof CascadingParameterGroup )
				{
					CascadingParameterGroup group = (CascadingParameterGroup) listParam.getParentGroup( );
					if ( group.getPostParameter( listParam ) != null )
					{
						try
						{
							createParameters( );
						}
						catch ( RuntimeException e1 )
						{
							e1.printStackTrace( );
						}
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
				paramValues.put( listParam.getHandle( ).getName( ),
						combo.getData( combo.getItem( combo.getSelectionIndex( ) ) ) );

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

	private void createList( Composite container,
			final ListingParameter listParam )
	{
		boolean isRequired = listParam.getHandle( ).isRequired( );
		boolean isStringType = listParam.getHandle( )
				.getDataType( )
				.equals( DesignChoiceConstants.PARAM_TYPE_STRING );
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

		List list = new ArrayList( );
		if ( isStringType && !isRequired )
		{
			list.add( blankValueChoice );
		}
		list.addAll( listParam.getValueList( ) );
		checkParam( listParam.getDefaultValue( ), list );
		if ( !isRequired )
		{
			list.add( InputParameterDialog.nullValueChoice );
		}

		for ( Iterator iterator = list.iterator( ); iterator.hasNext( ); )
		{
			IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
			String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
					: choice.getLabel( ) );
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

			for ( int i = 0; i < listViewer.getList( ).getItemCount( ); i++ )
			{
				Object item = listViewer.getList( )
						.getData( listViewer.getList( ).getItem( i ) );
				if ( oldvalueList.indexOf( item ) >= 0 )
				{
					listViewer.getList( ).select( i );
					// paramValues.put( listParam.getHandle( ).getName( ),
					// new Object[]{
					// listViewer.getList( )
					// .getData( listViewer.getList( )
					// .getItem( i ) )
					// } );
					newValueList.add( listViewer.getList( )
							.getData( listViewer.getList( ).getItem( i ) ) );
				}
			}
			paramValues.put( listParam.getHandle( ).getName( ),
					newValueList.toArray( new Object[newValueList.size( )] ) );

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
			Control control = postParamLists.get( listParam );
			setControlItems( control, new String[0] );
			for ( Iterator iterator = postParam.getValueList( ).iterator( ); iterator.hasNext( ); )
			{
				IParameterSelectionChoice choice = (IParameterSelectionChoice) iterator.next( );
				String label = ( choice.getLabel( ) == null ? String.valueOf( choice.getValue( ) )
						: choice.getLabel( ) );
				if ( label != null )
				{
					addControlItem( control, label );
					control.setData( label, choice.getValue( ) );
				}
			}
		}
	}

	private void setControlItems( Control control, String[] items )
	{
		if ( control instanceof Combo )
		{
			( (Combo) control ).setItems( items );
		}
		if ( control instanceof org.eclipse.swt.widgets.List )
		{
			( (org.eclipse.swt.widgets.List) control ).setItems( items );
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

	private void clearPostParamList( CascadingParameterGroup group,
			IParameter param )
	{
		if ( postParamLists.containsKey( param ) )
		{
			setControlItems( postParamLists.get( param ), new String[0] );
			clearPostParamList( group, group.getPostParameter( param ) );
		}
	}

	public Map getParameters( )
	{
		for ( IParameterAdapter adapter : this.paramAdatpers )
			this.paramValues.put( adapter.getName( ), adapter.getValue( ) );
		return this.paramValues;
	}
}
