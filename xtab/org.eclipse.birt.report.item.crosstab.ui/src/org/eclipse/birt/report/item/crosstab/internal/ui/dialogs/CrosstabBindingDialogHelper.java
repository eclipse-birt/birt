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

package org.eclipse.birt.report.item.crosstab.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.aggregation.IAggregationFactory;
import org.eclipse.birt.data.engine.api.aggregation.IAggregationInfo;
import org.eclipse.birt.data.engine.api.aggregation.IParameterInfo;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractBindingDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
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

public class CrosstabBindingDialogHelper extends AbstractBindingDialogHelper
{

	protected static final String NAME = Messages.getString( "BindingDialogHelper.text.Name" ); //$NON-NLS-1$
	protected static final String DATA_TYPE = Messages.getString( "BindingDialogHelper.text.DataType" ); //$NON-NLS-1$
	protected static final String FUNCTION = Messages.getString( "BindingDialogHelper.text.Function" ); //$NON-NLS-1$
	protected static final String DATA_FIELD = Messages.getString( "BindingDialogHelper.text.DataField" ); //$NON-NLS-1$
	protected static final String FILTER_CONDITION = Messages.getString( "BindingDialogHelper.text.Filter" ); //$NON-NLS-1$
	protected static final String AGGREGATE_ON = Messages.getString( "BindingDialogHelper.text.AggOn" ); //$NON-NLS-1$
	protected static final String EXPRESSION = Messages.getString( "BindingDialogHelper.text.Expression" ); //$NON-NLS-1$
	protected static final String ALL = Messages.getString( "CrosstabBindingDialogHelper.AggOn.All" ); //$NON-NLS-1$
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
	private Combo cmbType, cmbFunction, cmbDataField, cmbAggOn;
	private Composite argsComposite;

	private String name;
	private String typeSelect;
	private String expression;
	private Map argsMap = new HashMap( );

	private Composite composite;
	private Text txtDisplayName;
	private ComputedColumn newBinding;
	private CLabel messageLine;
	private Label lbName;

	public void createContent( Composite parent )
	{
		composite = parent;

		( (GridLayout) composite.getLayout( ) ).numColumns = 3;

		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.widthHint = 380;
		if ( isAggregate( ) )
		{
			gd.heightHint = 300;
		}
		else
		{
			gd.heightHint = 150;
		}
		composite.setLayoutData( gd );

		lbName = new Label( composite, SWT.NONE );
		lbName.setText( NAME );

		txtName = new Text( composite, SWT.BORDER );

		gd = new GridData( GridData.FILL_HORIZONTAL | GridData.GRAB_HORIZONTAL );
		gd.horizontalSpan = 2;
		txtName.setLayoutData( gd );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		txtName.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}

		} );

		new Label( composite, SWT.NONE ).setText( DISPLAY_NAME );
		txtDisplayName = new Text( composite, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		txtDisplayName.setLayoutData( gd );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		new Label( composite, SWT.NONE ).setText( DATA_TYPE );
		cmbType = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbType.setLayoutData( gd );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

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
		if ( getBinding( ) == null )// create
		{
			setTypeSelect( dataTypes[0] );
			this.newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
					isAggregate( ) ? DEFAULT_AGGREGATION_NAME
							: DEFAULT_ITEM_NAME );
			setName( this.newBinding.getName( ) );
		}
		else
		{
			setName( getBinding( ).getName( ) );
			setDisplayName( getBinding( ).getDisplayName( ) );
			setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( getBinding( ).getDataType( ) )
					.getDisplayName( ) );
			setDataFieldExpression( getBinding( ).getExpression( ) );
		}

		if ( this.getBinding( ) != null )
		{
			this.txtName.setEnabled( false );
		}

		if ( isAggregate( ) )
		{
			initFunction( );
			initDataFields( );
			initFilter( );
			initAggOn( );
		}
		validate( );
	}

	private void initAggOn( )
	{
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );
			String[] aggOns = getAggOns( xtabHandle );
			cmbAggOn.setItems( aggOns );

			String aggstr = "";
			if ( getBinding( ) != null )
			{
				List aggOnList = getBinding( ).getAggregateOnList( );
				int i = 0;
				for ( Iterator iterator = aggOnList.iterator( ); iterator.hasNext( ); )
				{
					if ( i > 0 )
						aggstr += ",";
					String name = (String) iterator.next( );
					aggstr += name;
					i++;
				}
			}
			else if ( getDataItemContainer( ) instanceof AggregationCellHandle )
			{
				AggregationCellHandle cellHandle = (AggregationCellHandle) getDataItemContainer( );
				if ( cellHandle.getAggregationOnRow( ) != null )
				{
					aggstr += cellHandle.getAggregationOnRow( ).getFullName( );
					if ( cellHandle.getAggregationOnColumn( ) != null )
					{
						aggstr += ",";
					}
				}
				if ( cellHandle.getAggregationOnColumn( ) != null )
				{
					aggstr += cellHandle.getAggregationOnColumn( )
							.getFullName( );
				}
			}
			for ( int j = 0; j < aggOns.length; j++ )
			{
				if ( aggOns[j].equals( aggstr ) )
				{
					cmbAggOn.select( j );
					return;
				}
			}
			cmbAggOn.select( 0 );
		}
		catch ( ExtendedElementException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private String[] getAggOns( CrosstabReportItemHandle xtabHandle )
	{

		List rowLevelList = getCrosstabViewHandleLevels( xtabHandle,
				ICrosstabConstants.ROW_AXIS_TYPE );
		List columnLevelList = getCrosstabViewHandleLevels( xtabHandle,
				ICrosstabConstants.COLUMN_AXIS_TYPE );
		List aggOnList = new ArrayList( );
		aggOnList.add( ALL );
		for ( Iterator iterator = rowLevelList.iterator( ); iterator.hasNext( ); )
		{
			String name = (String) iterator.next( );
			aggOnList.add( name );
		}
		for ( Iterator iterator = columnLevelList.iterator( ); iterator.hasNext( ); )
		{
			String name = (String) iterator.next( );
			aggOnList.add( name );
		}
		for ( Iterator iterator = rowLevelList.iterator( ); iterator.hasNext( ); )
		{
			String name = (String) iterator.next( );
			for ( Iterator iterator2 = columnLevelList.iterator( ); iterator2.hasNext( ); )
			{
				String name2 = (String) iterator2.next( );
				aggOnList.add( name + "," + name2 );
			}
		}
		return (String[]) aggOnList.toArray( new String[aggOnList.size( )] );
	}

	private List getCrosstabViewHandleLevels( CrosstabReportItemHandle xtab,
			int type )
	{
		List levelList = new ArrayList( );
		CrosstabViewHandle viewHandle = xtab.getCrosstabView( type );
		if ( viewHandle != null )
		{
			int dimensions = viewHandle.getDimensionCount( );
			for ( int i = 0; i < dimensions; i++ )
			{
				DimensionViewHandle dimension = viewHandle.getDimension( i );
				int levels = dimension.getLevelCount( );
				for ( int j = 0; j < levels; j++ )
				{
					LevelViewHandle level = dimension.getLevel( j );
					levelList.add( level.getCubeLevel( ).getFullName( ) );
				}
			}
		}
		return levelList;
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
		// cmbFunction.add( NULL, 0 );
		if ( binding == null )
		{
			cmbFunction.select( 0 );
			handleFunctionSelectEvent( );
			return;
		}
		try
		{
			String functionString = getFunctionDisplayName( DataAdapterUtil.adaptModelAggregationType( binding.getAggregateFunction( ) ) );
			int itemIndex = getItemIndex( getFunctionDisplayNames( ),
					functionString );
			cmbFunction.select( itemIndex );
			handleFunctionSelectEvent( );
		}
		catch ( AdapterException e )
		{
			ExceptionHandler.handle( e );
		}
		// List args = getFunctionArgs( functionString );
		// bindingColumn.argumentsIterator( )
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
		IAggregationInfo[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;
	}

	private IAggregationInfo getFunctionByDisplayName( String displayName )
	{
		IAggregationInfo[] choices = getFunctions( );
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
			return DataUtil.getAggregationFactory( )
					.getAggrInfo( function )
					.getDisplayName( );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
			return null;
		}
	}

	private IAggregationInfo[] getFunctions( )
	{
		try
		{
			List aggrInfoList = DataUtil.getAggregationFactory( )
					.getAggrInfoList( IAggregationFactory.AGGR_XTAB );
			return (IAggregationInfo[]) aggrInfoList.toArray( new IAggregationInfo[0] );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
			return new IAggregationInfo[0];
		}
	}
	
	private String getDataTypeDisplayName( String dataType )
	{
		for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
		{
			if ( dataType.equals( DATA_TYPE_CHOICES[i].getName( ) ) )
			{
				return DATA_TYPE_CHOICES[i].getDisplayName( );
			}
		}

		return "";
	}

	/**
	 * fill the cmbDataField with binding holder's bindings
	 */
	private void initDataFields( )
	{
		String[] items = getMesures( );
		cmbDataField.setItems( items );
		if ( binding != null )
		{
			for ( int i = 0; i < items.length; i++ )
			{
				if ( items[i].equals( binding.getExpression( ) ) )
				{
					cmbDataField.select( i );
				}
			}
		}
	}

	private String[] getMesures( )
	{
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );
			String[] mesures = new String[xtabHandle.getMeasureCount( ) + 1];
			mesures[0] = ""; //$NON-NLS-1$
			for ( int i = 1; i < mesures.length; i++ )
			{
				mesures[i] = DEUtil.getExpression( xtabHandle.getMeasure( i - 1 )
						.getCubeMeasure( ) );
			}
			return mesures;
		}
		catch ( ExtendedElementException e )
		{
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
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		cmbFunction.setLayoutData( gd );

		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbFunction.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleFunctionSelectEvent( );
				validate( );
			}
		} );

		new Label( composite, SWT.NONE ).setText( DATA_FIELD );
		cmbDataField = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbDataField.setLayoutData( gd );

		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbDataField.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}
		} );

		argsComposite = new Composite( composite, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL );
		gridData.horizontalSpan = 3;
		gridData.exclude = true;
		argsComposite.setLayoutData( gridData );
		GridLayout layout = new GridLayout( );
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 3;
		argsComposite.setLayout( layout );

		new Label( composite, SWT.NONE ).setText( FILTER_CONDITION );
		txtFilter = new Text( composite, SWT.BORDER );
		gridData = new GridData( GridData.FILL_HORIZONTAL ) ;
		txtFilter.setLayoutData( gridData );

		createExpressionButton( composite, txtFilter );

		Label lblAggOn = new Label( composite, SWT.NONE );
		lblAggOn.setText( AGGREGATE_ON );
		gridData = new GridData( );
		gridData.verticalAlignment = GridData.BEGINNING;
		lblAggOn.setLayoutData( gridData );

		cmbAggOn = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		gridData = new GridData( GridData.FILL_HORIZONTAL ); 
		gridData.horizontalSpan = 2;
		cmbAggOn.setLayoutData( gridData );

	}

	private void createCommonSection( Composite composite )
	{
		new Label( composite, SWT.NONE ).setText( EXPRESSION );
		txtExpression = new Text( composite, SWT.BORDER );
		txtExpression.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		createExpressionButton( composite, txtExpression );
		txtExpression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}

		} );
	}

	private void createMessageSection( Composite composite )
	{
		messageLine = new CLabel( composite, SWT.NONE );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 3;
		messageLine.setLayoutData( layoutData );
	}

	protected void handleFunctionSelectEvent( )
	{
		Control[] children = argsComposite.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].dispose( );
		}

		IAggregationInfo function = getFunctionByDisplayName( cmbFunction.getText( ) );
		if ( function != null )
		{
			argsMap.clear( );
			List args = getFunctionArgNames( function.getName( ) );
			if ( args.size( ) > 0 )
			{
				( (GridData) argsComposite.getLayoutData( ) ).exclude = false;
				( (GridData) argsComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;
				for ( Iterator iterator = args.iterator( ); iterator.hasNext( ); )
				{
					String argName = (String) iterator.next( );
					Label lblArg = new Label( argsComposite, SWT.NONE );
					lblArg.setText( argName + ":" );

					GridData gd = new GridData( );
					gd.widthHint = lbName.getBounds( ).width
							- lbName.getBorderWidth( );
					lblArg.setLayoutData( gd );

					Text txtArg = new Text( argsComposite, SWT.BORDER );
					GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
					gridData.horizontalIndent = 0;

					txtArg.setLayoutData( gridData );
					createExpressionButton( argsComposite, txtArg );
					argsMap.put( argName, txtArg );
				}
			}
			else
			{
				( (GridData) argsComposite.getLayoutData( ) ).heightHint = 0;
				// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			}
			this.cmbDataField.setEnabled( function.needDataField( ) );
			try
			{
				cmbType.setText( getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationFactory( )
						.getAggregation( function.getName( ) )
						.getDataType( ) ) ) );
			}
			catch ( BirtException e )
			{
				ExceptionHandler.handle( e );
			}
		}
		else
		{
			( (GridData) argsComposite.getLayoutData( ) ).heightHint = 0;
			// ( (GridData) argsComposite.getLayoutData( ) ).exclude = true;
			// new Label( argsComposite, SWT.NONE ).setText( "no args" );
		}
		argsComposite.layout( );
		composite.layout( );
		dialog.getShell( ).layout( );
	}

	private void createExpressionButton( final Composite parent, final Text text )
	{
		Button expressionButton = new Button( parent, SWT.PUSH );

		if ( expressionProvider == null
				|| ( !( expressionProvider instanceof CrosstabBindingExpressionProvider ) ) )
		{
			expressionProvider = new CrosstabBindingExpressionProvider( this.bindingHolder );
		}

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

	private List getFunctionArgNames( String function )
	{
		List argList = new ArrayList( );
		try
		{
			IAggregationInfo aggregationInfo = DataUtil.getAggregationFactory( )
					.getAggrInfo( function );
			Iterator argumentListIter = aggregationInfo.getParameters( )
					.iterator( );
			for ( ; argumentListIter.hasNext( ); )
			{
				IParameterInfo argInfo = (IParameterInfo) argumentListIter.next( );
				argList.add( argInfo.getDisplayName( ) );
			}
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
		return argList;
	}

	// public void save( ) throws Exception
	// {
	// if ( txtName.getText( ) != null
	// && txtName.getText( ).trim( ).length( ) > 0 )
	// {
	//
	// if ( isAggregate( ) )
	// {
	// saveAggregate( );
	// }
	// else
	// {
	// if ( getBinding( ) == null )
	// {
	// for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
	// {
	// if ( DATA_TYPE_CHOICES[i].getDisplayName( )
	// .equals( cmbType.getText( ) ) )
	// {
	// newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
	// break;
	// }
	// }
	// this.newBinding.setName( txtName.getText( ) );
	// this.newBinding.setExpression( txtExpression.getText( ) );
	// this.newBinding.setDisplayName( txtDisplayName.getText( ) );
	// this.binding = DEUtil.addColumn( getBindingHolder( ),
	// newBinding,
	// true );
	// }
	// else
	// {
	// for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
	// {
	// if ( DATA_TYPE_CHOICES[i].getDisplayName( )
	// .equals( cmbType.getText( ) ) )
	// {
	// this.binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
	// break;
	// }
	// }
	// this.binding.setDisplayName( txtDisplayName.getText( ) );
	// this.binding.setExpression( txtExpression.getText( ) );
	// }
	// }
	// }
	// }
	//
	// private void saveAggregate( ) throws Exception
	// {
	// if ( getBinding( ) == null )
	// {
	// this.newBinding.setName( txtName.getText( ) );
	// this.newBinding.setDisplayName( txtDisplayName.getText( ) );
	// for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
	// {
	// if ( DATA_TYPE_CHOICES[i].getDisplayName( )
	// .equals( cmbType.getText( ) ) )
	// {
	// newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
	// break;
	// }
	// }
	// this.newBinding.setExpression( cmbDataField.getText( ) );
	// this.newBinding.setAggregateFunction( getFunctionByDisplayName(
	// cmbFunction.getText( ) ) );
	// this.newBinding.setFilterExpression( txtFilter.getText( ) );
	//
	// this.newBinding.clearAggregateOnList( );
	// String aggStr = cmbAggOn.getText( );
	// StringTokenizer token = new StringTokenizer( aggStr, "," );
	//
	// while ( token.hasMoreTokens( ) )
	// {
	// String agg = token.nextToken( );
	// if ( !agg.equals( ALL ) )
	// newBinding.addAggregateOn( agg );
	// }
	//
	// this.binding = DEUtil.addColumn( getBindingHolder( ),
	// newBinding,
	// true );
	//
	// for ( Iterator iterator = argsMap.keySet( ).iterator( );
	// iterator.hasNext( ); )
	// {
	// String arg = (String) iterator.next( );
	// AggregationArgument argHandle =
	// StructureFactory.createAggregationArgument( );
	// argHandle.setName( ( getArgumentByDisplayName(
	// this.binding.getAggregateFunction( ),
	// arg ) ) );
	// argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
	// this.binding.addArgument( argHandle );
	// }
	//
	// }
	// else
	// {
	// if ( cmbDataField.getText( ) != null
	// && cmbDataField.getText( ).trim( ).length( ) == 0 )
	// {
	// this.binding = null;
	// return;
	// }
	//
	// if ( !( this.binding.getName( ) != null && this.binding.getName( )
	// .equals( txtName.getText( ).trim( ) ) ) )
	// this.binding.setName( txtName.getText( ) );
	// this.binding.setDisplayName( txtDisplayName.getText( ) );
	//
	// for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
	// {
	// if ( DATA_TYPE_CHOICES[i].getDisplayName( )
	// .equals( cmbType.getText( ) ) )
	// {
	// this.binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
	// break;
	// }
	// }
	//
	// this.binding.setExpression( cmbDataField.getText( ) );
	// this.binding.setAggregateFunction( getFunctionByDisplayName(
	// cmbFunction.getText( ) ) );
	// this.binding.setFilterExpression( txtFilter.getText( ) );
	//
	// this.binding.clearAggregateOnList( );
	// String aggStr = cmbAggOn.getText( );
	// StringTokenizer token = new StringTokenizer( aggStr, "," );
	//
	// while ( token.hasMoreTokens( ) )
	// {
	// String agg = token.nextToken( );
	// if ( !agg.equals( ALL ) )
	// this.binding.addAggregateOn( agg );
	// }
	//
	// this.binding.clearArgumentList( );
	//
	// for ( Iterator iterator = argsMap.keySet( ).iterator( );
	// iterator.hasNext( ); )
	// {
	// String arg = (String) iterator.next( );
	// AggregationArgument argHandle =
	// StructureFactory.createAggregationArgument( );
	// argHandle.setName( getArgumentByDisplayName(
	// this.binding.getAggregateFunction( ),
	// arg ) );
	// argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
	// this.binding.addArgument( argHandle );
	// }
	// }
	// }

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
		try
		{
			IAggregationInfo info = DataUtil.getAggregationFactory( )
					.getAggrInfo( function );
			Iterator arguments = info.getParameters( ).iterator( );
			for ( ; arguments.hasNext( ); )
			{
				IParameterInfo argInfo = (IParameterInfo) arguments.next( );
				if ( argInfo.getName( ).equals( argument ) )
					return argInfo.getDisplayName( );
			}
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
		}
		return null;
	}

	public void validate( )
	{
		if ( txtName != null
				&& ( txtName.getText( ) == null || txtName.getText( )
						.trim( )
						.equals( "" ) ) ) //$NON-NLS-1$
		{
			dialog.setCanFinish( false );
		}
		else if ( txtExpression != null
				&& ( txtExpression.getText( ) == null || txtExpression.getText( )
						.trim( )
						.equals( "" ) ) ) //$NON-NLS-1$
		{
			dialog.setCanFinish( false );
		}
		else
		{
			if ( this.binding == null )// create bindnig, we should check if
										// the binding name already exists.
			{
				for ( Iterator iterator = this.bindingHolder.getColumnBindings( )
						.iterator( ); iterator.hasNext( ); )
				{
					ComputedColumnHandle computedColumn = (ComputedColumnHandle) iterator.next( );
					if ( computedColumn.getName( ).equals( txtName.getText( ) ) )
					{
						dialog.setCanFinish( false );
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
			dialog.setCanFinish( true );
			this.messageLine.setText( "" ); //$NON-NLS-1$
			this.messageLine.setImage( null );

			if ( txtExpression != null
					&& ( txtExpression.getText( ) == null || txtExpression.getText( )
							.trim( )
							.equals( "" ) ) ) //$NON-NLS-1$
			{
				dialog.setCanFinish( false );
				return;
			}
			if ( cmbDataField != null
					&& ( cmbDataField.getText( ) == null || cmbDataField.getText( )
							.trim( )
							.equals( "" ) ) && cmbDataField.isEnabled( ) ) //$NON-NLS-1$
			{
				dialog.setCanFinish( false );
				return;
			}
			dialog.setCanFinish( true );
		}
	}

	public boolean differs( ComputedColumnHandle binding )
	{
		if ( isAggregate( ) )
		{
			if ( !strEquals( binding.getName( ), txtName.getText( ) ) )
				return true;
			if ( !strEquals( binding.getDisplayName( ),
					txtDisplayName.getText( ) ) )
				return true;
			if ( !strEquals( binding.getDataType( ), getDataType( ) ) )
				return true;
			if ( !strEquals( binding.getExpression( ), cmbDataField.getText( ) ) )
				return true;
			if ( !strEquals( binding.getAggregateFunction( ),
					getFunctionByDisplayName( cmbFunction.getText( ) ).getName( ) ) )
				return true;
			if ( !strEquals( binding.getFilterExpression( ),
					txtFilter.getText( ) ) )
				return true;
			if ( !strEquals( cmbAggOn.getText( ), binding.getAggregateOn( ) ) )
				return true;

			for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
			{
				AggregationArgumentHandle handle = (AggregationArgumentHandle) iterator.next( );
				String argDisplayName = getArgumentDisplayNameByName( binding.getAggregateFunction( ),
						handle.getName( ) );
				if ( argsMap.containsKey( argDisplayName ) )
				{
					if ( !strEquals( handle.getValue( ),
							( (Text) argsMap.get( argDisplayName ) ).getText( ) ) )
					{
						return true;
					}
				}
				else
				{
					return true;
				}
			}

		}
		else
		{
			if ( !strEquals( txtName.getText( ), binding.getName( ) ) )
				return true;
			if ( !strEquals( txtDisplayName.getText( ),
					binding.getDisplayName( ) ) )
				return true;
			if ( !strEquals( getDataType( ), binding.getDataType( ) ) )
				return true;
			if ( !strEquals( txtExpression.getText( ), binding.getExpression( ) ) )
				return true;
		}
		return false;
	}

	private boolean strEquals( String left, String right )
	{
		if ( left == right )
			return true;
		if ( left == null )
			return "".equals( right );
		if ( right == null )
			return "".equals( left );
		return left.equals( right );
	}

	private String getDataType( )
	{
		for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
		{
			if ( DATA_TYPE_CHOICES[i].getDisplayName( )
					.equals( cmbType.getText( ) ) )
			{
				return DATA_TYPE_CHOICES[i].getName( );
			}
		}
		return "";
	}

	public ComputedColumnHandle editBinding( ComputedColumnHandle binding )
			throws SemanticException
	{
		if ( isAggregate( ) )
		{
			binding.setDisplayName( txtDisplayName.getText( ) );

			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}

			binding.setExpression( cmbDataField.getText( ) );
			binding.setAggregateFunction( getFunctionByDisplayName( cmbFunction.getText( ) ).getName( ) );
			binding.setFilterExpression( txtFilter.getText( ) );

			binding.clearAggregateOnList( );
			String aggStr = cmbAggOn.getText( );
			StringTokenizer token = new StringTokenizer( aggStr, "," );

			while ( token.hasMoreTokens( ) )
			{
				String agg = token.nextToken( );
				if ( !agg.equals( ALL ) )
					binding.addAggregateOn( agg );
			}

			binding.clearArgumentList( );

			for ( Iterator iterator = argsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
				argHandle.setName( getArgumentByDisplayName( binding.getAggregateFunction( ),
						arg ) );
				argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
				binding.addArgument( argHandle );
			}
		}
		else
		{
			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}
			binding.setDisplayName( txtDisplayName.getText( ) );
			binding.setExpression( txtExpression.getText( ) );
		}
		return binding;
	}

	public ComputedColumnHandle newBinding( ReportItemHandle bindingHolder,
			String name ) throws SemanticException
	{
		ComputedColumn column = StructureFactory.newComputedColumn( bindingHolder,
				name == null ? txtName.getText( ) : name );
		ComputedColumnHandle binding = DEUtil.addColumn( bindingHolder,
				column,
				true );
		return editBinding( binding );
	}

}
