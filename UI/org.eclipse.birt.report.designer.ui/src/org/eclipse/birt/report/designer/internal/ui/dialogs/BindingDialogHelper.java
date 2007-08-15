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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class BindingDialogHelper extends AbstractBindingDialogHelper
{

	protected static final String NAME = Messages.getString( "BindingDialogHelper.text.Name" ); //$NON-NLS-1$
	protected static final String DATA_TYPE = Messages.getString( "BindingDialogHelper.text.DataType" ); //$NON-NLS-1$
	protected static final String FUNCTION = Messages.getString( "BindingDialogHelper.text.Function" ); //$NON-NLS-1$
	protected static final String DATA_FIELD = Messages.getString( "BindingDialogHelper.text.DataField" ); //$NON-NLS-1$
	protected static final String FILTER_CONDITION = Messages.getString( "BindingDialogHelper.text.Filter" ); //$NON-NLS-1$
	protected static final String AGGREGATE_ON = Messages.getString( "BindingDialogHelper.text.AggOn" ); //$NON-NLS-1$
	protected static final String TABLE = Messages.getString( "BindingDialogHelper.text.Table" ); //$NON-NLS-1$
	protected static final String LIST = Messages.getString( "BindingDialogHelper.text.List" ); //$NON-NLS-1$
	protected static final String GROUP = Messages.getString( "BindingDialogHelper.text.Group" ); //$NON-NLS-1$
	protected static final String EXPRESSION = Messages.getString( "BindingDialogHelper.text.Expression" ); //$NON-NLS-1$
	protected static final String DISPLAY_NAME = Messages.getString( "BindingDialogHelper.text.displayName" ); //$NON-NLS-1$

	protected static final String DEFAULT_ITEM_NAME = Messages.getString( "BindingDialogHelper.bindingName.dataitem" ); //$NON-NLS-1$
	protected static final String DEFAULT_AGGREGATION_NAME = Messages.getString( "BindingDialogHelper.bindingName.aggregation" ); //$NON-NLS-1$

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( );
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices( null );
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET );

	private Text txtName, txtFilter, txtExpression;
	private Combo cmbType, cmbFunction, cmbDataField, cmbGroup;
	private Button btnTable, btnGroup;
	private Composite argsComposite;

	private String name;
	private String typeSelect;
	private String expression;
	private String functionName;
	private Map argsMap = new HashMap( );

	private Composite composite;
	private Text txtDisplayName;
	private ComputedColumn newBinding;
	private CLabel messageLine;
	private Combo cmbName;

	private boolean isCreate;
	private boolean isRef;

	public void createContent( Composite parent )
	{

		isCreate = getBinding( ) == null;
		isRef = getBindingHolder( ).getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF;
		composite = parent;

		( (GridLayout) composite.getLayout( ) ).numColumns = 3;

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 380;
		if ( isAggregate( ) )
		{
			gd.heightHint = 320;
		}
		else
		{
			gd.heightHint = 150;
		}
		composite.setLayoutData( gd );

		new Label( composite, SWT.NONE ).setText( NAME );

		if ( isRef )
		{
			cmbName = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
			cmbName.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL ) );
			cmbName.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					String bindingName = cmbName.getItem( cmbName.getSelectionIndex( ) );

					for ( Iterator iterator = getBindingHolder( ).getDataBindingReference( )
							.getColumnBindings( )
							.iterator( ); iterator.hasNext( ); )
					{
						ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
						if ( computedColumn.getName( ).equals( bindingName ) )
						{
							setBinding( computedColumn );
							initDialog( );
							return;
						}
					}
				}
			} );
		}
		else
		{
			txtName = new Text( composite, SWT.BORDER );
			txtName.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
					| GridData.GRAB_HORIZONTAL ) );
			txtName.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					validate( );
				}

			} );
		}
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label( composite, SWT.NONE ).setText( DISPLAY_NAME );
		txtDisplayName = new Text( composite, SWT.BORDER );
		txtDisplayName.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label( composite, SWT.NONE ).setText( DATA_TYPE );
		cmbType = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbType.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		if ( isAggregate( ) )
		{
			createAggregateSection( composite );
		}
		else
		{
			createCommonSection( composite );
		}
		createMessageSection( composite );

	}

	public void initDialog( )
	{
		if ( isCreate )//create
		{
			if ( isRef )
			{
				if ( getBinding( ) == null )
				{
					for ( Iterator iterator = getBindingHolder( ).getDataBindingReference( )
							.getColumnBindings( )
							.iterator( ); iterator.hasNext( ); )
					{
						ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
						if ( isAggregate( ) )
						{
							if ( computedColumn.getAggregateFunction( ) == null
									|| computedColumn.getAggregateFunction( )
											.equals( "" ) )
								continue;
						}
						else
						{
							if ( computedColumn.getAggregateFunction( ) != null
									&& !computedColumn.getAggregateFunction( )
											.equals( "" ) )
								continue;
						}
						cmbName.add( computedColumn.getName( ) );
					}
				}
				else
				{
					setDisplayName( getBinding( ).getDisplayName( ) );
					for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
					{
						if ( DATA_TYPE_CHOICES[i].getName( )
								.equals( getBinding( ).getDataType( ) ) )
						{
							setTypeSelect( DATA_TYPE_CHOICES[i].getDisplayName( ) );
							break;
						}
					}
					setDataFieldExpression( getBinding( ).getExpression( ) );
				}
			}
			else
			{
				setTypeSelect( dataTypes[0] );
				this.newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
						isAggregate( ) ? DEFAULT_AGGREGATION_NAME
								: DEFAULT_ITEM_NAME );
				setName( this.newBinding.getName( ) );
			}
		}
		else
		{
			if ( isRef )
			{
				int i = 0;
				for ( Iterator iterator = getBindingHolder( ).getDataBindingReference( )
						.getColumnBindings( )
						.iterator( ); iterator.hasNext( ); )
				{
					ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
					if ( isAggregate( ) )
					{
						if ( computedColumn.getAggregateFunction( ) == null
								|| computedColumn.getAggregateFunction( )
										.equals( "" ) )
							continue;
					}
					else
					{
						if ( computedColumn.getAggregateFunction( ) != null
								&& !computedColumn.getAggregateFunction( )
										.equals( "" ) )
							continue;
					}
					cmbName.add( computedColumn.getName( ) );
					if ( getBinding( ).getName( )
							.equals( computedColumn.getName( ) ) )
						cmbName.select( i );
					i++;
				}
				setDisplayName( getBinding( ).getDisplayName( ) );
				for ( i = 0; i < DATA_TYPE_CHOICES.length; i++ )
				{
					if ( DATA_TYPE_CHOICES[i].getName( )
							.equals( getBinding( ).getDataType( ) ) )
					{
						setTypeSelect( DATA_TYPE_CHOICES[i].getDisplayName( ) );
						break;
					}
				}
				setDataFieldExpression( getBinding( ).getExpression( ) );
			}
			else
			{
				setName( getBinding( ).getName( ) );
				setDisplayName( getBinding( ).getDisplayName( ) );
				setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( getBinding( ).getDataType( ) )
						.getDisplayName( ) );
				setDataFieldExpression( getBinding( ).getExpression( ) );
			}
		}

		if ( !isCreate )
		{
			if ( isRef )
			{
				this.cmbName.setEnabled( false );
			}
			else
			{
				this.txtName.setEnabled( false );
			}
		}

		if ( isAggregate( ) )
		{
			initFunction( );
			initDataFields( );
			initFilter( );
			initGroups( );
		}
	}

	private void initFilter( )
	{
		if ( binding != null && binding.getFilterExpression( ) != null )
		{
			txtFilter.setText( binding.getFilterExpression( ) );
		}
	}

	private void initFunction( )
	{
		cmbFunction.setItems( getFunctionDisplayNames( ) );
		//		cmbFunction.add( NULL, 0 );
		if ( binding == null )
		{
			cmbFunction.select( 0 );
			handleFunctionSelectEvent( );
			return;
		}
		String functionString = getFunctionDisplayName( binding.getAggregateFunction( ) );
		int itemIndex = getItemIndex( getFunctionDisplayNames( ),
				functionString );
		cmbFunction.select( itemIndex );
		handleFunctionSelectEvent( );
		//		List args = getFunctionArgs( functionString );
		//		bindingColumn.argumentsIterator( )
		for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
		{
			AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next( );
			String argDisplayName = getArgumentDisplayNameByName( binding.getAggregateFunction( ),
					arg.getName( ) );
			if ( argsMap.containsKey( argDisplayName ) )
			{
				if ( arg.getValue( ) != null )
				{
					Text txtArg = (Text) argsMap.get( argDisplayName );
					txtArg.setText( arg.getValue( ) );
				}
			}
		}
	}

	private String[] getFunctionDisplayNames( )
	{
		//		List functions = DEUtil.getMetaDataDictionary( ).getFunctions( );
		//		String[] displayNames = new String[functions.size( )];
		//		for ( int i = 0; i < displayNames.length; i++ )
		//		{
		//			displayNames[i] = ( (IMethodInfo) functions.get( i ) ).getName( );
		//		}
		//		return displayNames;
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

	private String getFunctionByDisplayName( String displayName )
	{
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return null;

		for ( int i = 0; i < choices.length; i++ )
		{
			if ( choices[i].getDisplayName( ).equals( displayName ) )
			{
				return choices[i].getName( );
			}
		}
		return null;
	}

	private String getFunctionDisplayName( String function )
	{
		IChoice[] choices = getFunctions( );
		if ( choices == null )
			return null;

		for ( int i = 0; i < choices.length; i++ )
		{
			if ( choices[i].getName( ).equals( function ) )
			{
				return choices[i].getDisplayName( );
			}
		}
		return null;
	}

	private IChoice[] getFunctions( )
	{
		return DEUtil.getMetaDataDictionary( )
				.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
				.getMember( ComputedColumn.AGGREGATEON_FUNCTION_MEMBER )
				.getAllowedChoices( )
				.getChoices( );
	}

	/**
	 * fill the cmbDataField with binding holder's bindings
	 */
	private void initDataFields( )
	{
		cmbDataField.setItems( getColumnBindings( ) );
		if ( binding != null && binding.getExpression( ) != null )
		{
			cmbDataField.setText( binding.getExpression( ) );
		}
	}

	private String[] getColumnBindings( )
	{
		List elementsList = DEUtil.getVisiableColumnBindingsList( getBindingHolder( ) );
		String[] bindings = new String[elementsList.size( )];
		for ( int i = 0; i < bindings.length; i++ )
		{
			bindings[i] = ( (ComputedColumnHandle) elementsList.get( i ) ).getName( );
		}
		return bindings;
	}

	private void initGroups( )
	{
		String[] groups = getGroups( );
		if ( groups.length > 0 )
		{
			cmbGroup.setItems( groups );
			if ( binding != null && binding.getAggregateOn( ) != null )
			{
				btnGroup.setSelection( true );
				for ( int i = 0; i < groups.length; i++ )
				{
					if ( groups[i].equals( binding.getAggregateOn( ) ) )
					{
						cmbGroup.select( i );
						return;
					}
				}
				cmbGroup.setEnabled( true );
			}
			else
			{
				btnTable.setSelection( true );
				cmbGroup.select( 0 );
				cmbGroup.setEnabled( false );
			}
		}
		else
		{
			btnGroup.setEnabled( false );
			cmbGroup.setEnabled( false );
			btnTable.setSelection( true );
		}
	}

	private String[] getGroups( )
	{
		if ( getBindingHolder( ) instanceof ListingHandle )
		{
			ListingHandle listingHandle = (ListingHandle) getBindingHolder( );
			String[] groups = new String[listingHandle.getGroups( ).getCount( )];
			for ( int i = 0; i < groups.length; i++ )
			{
				groups[i] = ( (GroupHandle) listingHandle.getGroups( ).get( i ) ).getName( );
			}
			return groups;
		}
		return new String[0];
	}

	private void setDataFieldExpression( String expression )
	{
		this.expression = expression;
		if ( expression != null )
		{
			if ( cmbDataField != null && !cmbDataField.isDisposed( ) )
			{
				cmbDataField.setText( expression );
			}
			if ( txtExpression != null && !txtExpression.isDisposed( ) )
			{
				txtExpression.setText( expression );
			}
		}
	}

	private void setName( String name )
	{
		this.name = name;
		if ( name != null && txtName != null )
			txtName.setText( name );
	}

	private void setDisplayName( String displayName )
	{
		if ( displayName != null && txtDisplayName != null )
			txtDisplayName.setText( displayName );
	}

	private void setTypeSelect( String typeSelect )
	{
		this.typeSelect = typeSelect;
		if ( dataTypes != null && cmbType != null )
		{
			cmbType.setItems( dataTypes );
			if ( typeSelect != null )
				cmbType.select( getItemIndex( cmbType.getItems( ), typeSelect ) );
			else
				cmbType.select( 0 );
		}
	}

	private int getItemIndex( String[] items, String item )
	{
		for ( int i = 0; i < items.length; i++ )
		{
			if ( items[i].equals( item ) )
				return i;
		}
		return -1;
	}

	private void createAggregateSection( Composite composite )
	{

		new Label( composite, SWT.NONE ).setText( FUNCTION );
		cmbFunction = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbFunction.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbFunction.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleFunctionSelectEvent( );
			}
		} );

		new Label( composite, SWT.NONE ).setText( DATA_FIELD );
		cmbDataField = new Combo( composite, SWT.BORDER );
		cmbDataField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		createExpressionButton( composite, cmbDataField );

		cmbDataField.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}
		} );

		cmbDataField.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String expr = getColumnBindingExpressionByName( cmbDataField.getText( ) );
				if ( expr != null )
				{
					cmbDataField.setText( expr );
				}
			}
		} );

		argsComposite = new Composite( composite, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL );
		gridData.horizontalSpan = 3;
		gridData.exclude = true;
		argsComposite.setLayoutData( gridData );
		GridLayout layout = new GridLayout( );
		//		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 3;
		argsComposite.setLayout( layout );

		new Label( composite, SWT.NONE ).setText( FILTER_CONDITION );
		txtFilter = new Text( composite, SWT.BORDER );
		txtFilter.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		createExpressionButton( composite, txtFilter );

		txtFilter.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{

			}
		} );

		Label lblAggOn = new Label( composite, SWT.NONE );
		lblAggOn.setText( AGGREGATE_ON );
		gridData = new GridData( );
		gridData.verticalAlignment = GridData.BEGINNING;
		lblAggOn.setLayoutData( gridData );

		Composite aggOnComposite = new Composite( composite, SWT.NONE );
		gridData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL );
		gridData.horizontalSpan = 2;
		aggOnComposite.setLayoutData( gridData );

		layout = new GridLayout( );
		layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		aggOnComposite.setLayout( layout );

		btnTable = new Button( aggOnComposite, SWT.RADIO );
		btnTable.setText( getBindingHolder( ) instanceof TableHandle ? TABLE
				: LIST );
		btnTable.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				cmbGroup.setEnabled( false );
			}
		} );

		WidgetUtil.createGridPlaceholder( aggOnComposite, 1, false );

		btnGroup = new Button( aggOnComposite, SWT.RADIO );
		btnGroup.setText( GROUP );
		btnGroup.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				cmbGroup.setEnabled( true );
			}
		} );
		cmbGroup = new Combo( aggOnComposite, SWT.BORDER | SWT.READ_ONLY );
		cmbGroup.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		if ( isRef )
		{
			txtDisplayName.setEnabled( false );
			cmbType.setEnabled( false );
			cmbFunction.setEnabled( false );
			cmbDataField.setEnabled( false );
			txtFilter.setEnabled( false );
			aggOnComposite.setEnabled( false );
			argsComposite.setEnabled( false );
		}
	}

	private void createCommonSection( Composite composite )
	{
		new Label( composite, SWT.NONE ).setText( EXPRESSION );
		txtExpression = new Text( composite, SWT.BORDER );
		txtExpression.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		createExpressionButton( composite, txtExpression );
		txtExpression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}

		} );
		if ( isRef )
		{
			txtDisplayName.setEnabled( false );
			cmbType.setEnabled( false );
			txtExpression.setEnabled( false );
		}
	}

	private void createMessageSection( Composite composite )
	{
		messageLine = new CLabel( composite, SWT.NONE );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 3;
		messageLine.setLayoutData( layoutData );
	}

	private void verifyInput( )
	{
		if ( isRef
				&& ( cmbName.getText( ) == null || cmbName.getText( )
						.equals( "" ) ) )
		{
			dialog.getOkButton( ).setEnabled( false );
			return;
		}

		if ( txtName != null
				&& ( txtName.getText( ) == null || txtName.getText( )
						.trim( )
						.equals( "" ) ) ) //$NON-NLS-1$
		{
			if ( dialog.getOkButton( ) != null )
				dialog.getOkButton( ).setEnabled( false );
		}
		else if ( txtExpression != null
				&& ( txtExpression.getText( ) == null || txtExpression.getText( )
						.trim( )
						.equals( "" ) ) ) //$NON-NLS-1$
		{
			if ( dialog.getOkButton( ) != null )
				dialog.getOkButton( ).setEnabled( false );
		}
		else
		{
			if ( this.binding == null )//create bindnig, we should check if the binding name already exists.
			{
				for ( Iterator iterator = this.bindingHolder.getColumnBindings( )
						.iterator( ); iterator.hasNext( ); )
				{
					ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
					if ( computedColumn.getName( ).equals( txtName.getText( ) ) )
					{
						if ( dialog.getOkButton( ) != null )
							dialog.getOkButton( ).setEnabled( false );
						this.messageLine.setText( Messages.getFormattedString( "BindingDialogHelper.error.nameduplicate", //$NON-NLS-1$
								new Object[]{
									txtName.getText( )
								} ) );
						this.messageLine.setImage( PlatformUI.getWorkbench( )
								.getSharedImages( )
								.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
						return;
					}
				}
			}
			if ( dialog.getOkButton( ) != null )
				dialog.getOkButton( ).setEnabled( true );
			this.messageLine.setText( "" ); //$NON-NLS-1$
			this.messageLine.setImage( null );
		}
	}

	protected void handleFunctionSelectEvent( )
	{
		Control[] children = argsComposite.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].dispose( );
		}

		String function = getFunctionByDisplayName( cmbFunction.getText( ) );
		if ( function != null )
		{
			argsMap.clear( );
			List args = getFunctionArgNames( function );
			if ( args.size( ) > 0 )
			{
				( (GridData) argsComposite.getLayoutData( ) ).exclude = false;
				( (GridData) argsComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;
				for ( Iterator iterator = args.iterator( ); iterator.hasNext( ); )
				{
					String argName = (String) iterator.next( );
					Label lblArg = new Label( argsComposite, SWT.NONE );
					lblArg.setText( argName + ":" ); //$NON-NLS-1$
					lblArg.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
							| GridData.GRAB_HORIZONTAL ) );

					Text txtArg = new Text( argsComposite, SWT.BORDER );
					GridData gridData = new GridData( );

					gridData.widthHint = txtFilter.getBounds( ).width - 9;

					txtArg.setLayoutData( gridData );
					createExpressionButton( argsComposite, txtArg );
					argsMap.put( argName, txtArg );
				}
			}
			else
			{
				( (GridData) argsComposite.getLayoutData( ) ).heightHint = 0;
				//						( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			}
		}
		else
		{
			( (GridData) argsComposite.getLayoutData( ) ).heightHint = 0;
			//					( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			//					new Label( argsComposite, SWT.NONE ).setText( "no args" );
		}
		argsComposite.layout( );
		composite.layout( );
	}

	private void createExpressionButton( final Composite parent, final Text text )
	{
		Button expressionButton = new Button( parent, SWT.PUSH );

		if ( expressionProvider == null )
			expressionProvider = new BindingExpressionProvider( this.bindingHolder );

		UIUtil.setExpressionButtonImage( expressionButton );
		expressionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder expression = new ExpressionBuilder( text.getText( ) );
				expression.setExpressionProvier( expressionProvider );

				if ( expression.open( ) == Window.OK )
				{
					if ( expression.getResult( ) != null )
						text.setText( expression.getResult( ) );
				}
			}
		} );
	}

	private void createExpressionButton( final Composite parent,
			final Combo combo )
	{
		Button expressionButton = new Button( parent, SWT.PUSH );

		if ( expressionProvider == null )
			expressionProvider = new BindingExpressionProvider( this.bindingHolder );

		UIUtil.setExpressionButtonImage( expressionButton );
		expressionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				ExpressionBuilder expression = new ExpressionBuilder( combo.getText( ) );
				expression.setExpressionProvier( expressionProvider );

				if ( expression.open( ) == Window.OK )
				{
					if ( expression.getResult( ) != null )
						combo.setText( expression.getResult( ) );
				}
			}
		} );
	}

	protected void setExpressionButtonImage( Button button )
	{
		String imageName;
		if ( button.isEnabled( ) )
		{
			imageName = IReportGraphicConstants.ICON_ENABLE_EXPRESSION_BUILDERS;
		}
		else
		{
			imageName = IReportGraphicConstants.ICON_DISABLE_EXPRESSION_BUILDERS;
		}
		Image image = ReportPlatformUIImages.getImage( imageName );

		GridData gd = new GridData( );
		gd.widthHint = 20;
		gd.heightHint = 20;
		button.setLayoutData( gd );

		button.setImage( image );
		if ( button.getImage( ) != null )
		{
			button.getImage( ).setBackground( button.getBackground( ) );
		}

	}

	private String getColumnBindingExpressionByName( String name )
	{
		List elementsList = DEUtil.getVisiableColumnBindingsList( this.bindingHolder );
		for ( Iterator iterator = elementsList.iterator( ); iterator.hasNext( ); )
		{
			ComputedColumnHandle binding = (ComputedColumnHandle) iterator.next( );
			if ( binding.getName( ).equals( name ) )
				return binding.getExpression( );
		}
		return null;
	}

	private List getFunctionArgNames( String function )
	{
		List functions = DEUtil.getMetaDataDictionary( ).getFunctions( );
		List argList = new ArrayList( );
		for ( Iterator iterator = functions.iterator( ); iterator.hasNext( ); )
		{
			IMethodInfo method = (IMethodInfo) iterator.next( );
			if ( method.getName( ).equals( function ) )
			{
				Iterator argumentListIter = method.argumentListIterator( );
				IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next( );
				for ( Iterator iter = arguments.argumentsIterator( ); iter.hasNext( ); )
				{
					IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
					argList.add( argInfo.getDisplayName( ) );
				}
				break;
			}
		}
		return argList;
	}

	public void save( ) throws Exception
	{
		if ( isRef )
			return;
		if ( txtName.getText( ) != null
				&& txtName.getText( ).trim( ).length( ) > 0 )
		{

			if ( isAggregate( ) )
			{
				saveAggregate( );
			}
			else
			{
				if ( isCreate )
				{
					for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
					{
						if ( DATA_TYPE_CHOICES[i].getDisplayName( )
								.equals( cmbType.getText( ) ) )
						{
							newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
							break;
						}
					}
					this.newBinding.setName( txtName.getText( ) );
					this.newBinding.setDisplayName( txtDisplayName.getText( ) );
					this.newBinding.setExpression( txtExpression.getText( ) );
					this.binding = DEUtil.addColumn( getBindingHolder( ),
							this.newBinding,
							true );
				}
				else
				{
					for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
					{
						if ( DATA_TYPE_CHOICES[i].getDisplayName( )
								.equals( cmbType.getText( ) ) )
						{
							this.binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
							break;
						}
					}
					this.binding.setDisplayName( txtDisplayName.getText( ) );
					this.binding.setExpression( txtExpression.getText( ) );
				}
			}
		}
	}

	private void saveAggregate( ) throws Exception
	{
		if ( isCreate )
		{
			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					this.newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}
			this.newBinding.setName( txtName.getText( ) );
			this.newBinding.setDisplayName( txtDisplayName.getText( ) );
			this.newBinding.setExpression( cmbDataField.getText( ) );
			this.newBinding.setAggregateFunction( getFunctionByDisplayName( cmbFunction.getText( ) ) );
			this.newBinding.setFilterExpression( txtFilter.getText( ) );

			if ( btnTable.getSelection( ) )
			{
				this.newBinding.setAggregateOn( null );
			}
			else
			{
				this.newBinding.setAggregateOn( cmbGroup.getText( ) );
			}

			this.binding = DEUtil.addColumn( getBindingHolder( ),
					this.newBinding,
					true );

			for ( Iterator iterator = argsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
				argHandle.setName( getArgumentByDisplayName( this.binding.getAggregateFunction( ),
						arg ) );
				argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
				this.binding.addArgument( argHandle );
			}

		}
		else
		{
			if ( cmbDataField.getText( ) != null
					&& cmbDataField.getText( ).trim( ).length( ) == 0 )
			{
				this.binding = null;
				return;
			}

			if ( !( this.binding.getName( ) != null && this.binding.getName( )
					.equals( txtName.getText( ).trim( ) ) ) )
				this.binding.setName( txtName.getText( ) );
			this.binding.setDisplayName( txtDisplayName.getText( ) );

			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					this.binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}

			this.binding.setExpression( cmbDataField.getText( ) );
			this.binding.setAggregateFunction( getFunctionByDisplayName( cmbFunction.getText( ) ) );
			this.binding.setFilterExpression( txtFilter.getText( ) );

			if ( btnTable.getSelection( ) )
			{
				this.binding.setAggregateOn( null );
			}
			else
			{
				this.binding.setAggregateOn( cmbGroup.getText( ) );
			}

			this.binding.clearArgumentList( );

			for ( Iterator iterator = argsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
				argHandle.setName( getArgumentByDisplayName( this.binding.getAggregateFunction( ),
						arg ) );
				argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
				this.binding.addArgument( argHandle );
			}
		}
	}

	private String getArgumentByDisplayName( String function, String argument )
	{
		List functions = DEUtil.getMetaDataDictionary( ).getFunctions( );
		for ( Iterator iterator = functions.iterator( ); iterator.hasNext( ); )
		{
			IMethodInfo method = (IMethodInfo) iterator.next( );
			if ( method.getName( ).equals( function ) )
			{
				Iterator argumentListIter = method.argumentListIterator( );
				IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next( );
				for ( Iterator iter = arguments.argumentsIterator( ); iter.hasNext( ); )
				{
					IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
					if ( argInfo.getDisplayName( ).equals( argument ) )
						return argInfo.getName( );
				}
			}
		}
		return null;
	}

	private String getArgumentDisplayNameByName( String function,
			String argument )
	{
		List functions = DEUtil.getMetaDataDictionary( ).getFunctions( );
		for ( Iterator iterator = functions.iterator( ); iterator.hasNext( ); )
		{
			IMethodInfo method = (IMethodInfo) iterator.next( );
			if ( method.getName( ).equals( function ) )
			{
				Iterator argumentListIter = method.argumentListIterator( );
				IArgumentInfoList arguments = (IArgumentInfoList) argumentListIter.next( );
				for ( Iterator iter = arguments.argumentsIterator( ); iter.hasNext( ); )
				{
					IArgumentInfo argInfo = (IArgumentInfo) iter.next( );
					if ( argInfo.getName( ).equals( argument ) )
						return argInfo.getDisplayName( );
				}
			}
		}
		return null;
	}

	public void validate( )
	{
		verifyInput( );
	}
}
