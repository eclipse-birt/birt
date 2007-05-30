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

import org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractBindingDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.BindingExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfoList;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IMethodInfo;
import org.eclipse.birt.report.model.elements.interfaces.IMeasureModel;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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
	protected static final String ALL = "All"; //$NON-NLS-1$
	protected static final String DISPLAY_NAME = Messages.getString( "BindingDialogHelper.text.displayName" ); //$NON-NLS-1$

	//TODO should model generate the default name?
	protected static final String DEFAULT_ITEM_NAME = "data item";

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( );
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices( null );
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET );

	private Text txtName, txtFilter, txtExpression;
	private Combo cmbType, cmbFunction, cmbDataField, cmbAggOn;
	private Button btnTable, btnGroup;
	private Composite argsComposite;

	private String name;
	private String typeSelect;
	private String expression;
	private String functionName;
	private Map argsMap = new HashMap( );

	private Composite composite;
	private Text txtDisplayName;

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
			gd.heightHint = 120;
		}
		composite.setLayoutData( gd );

		new Label( composite, SWT.NONE ).setText( NAME );
		txtName = new Text( composite, SWT.BORDER );

		txtName.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		WidgetUtil.createGridPlaceholder( composite, 1, false );

		txtName.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}

		} );

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

	}

	public void initDialog( )
	{
		if ( getBinding( ) == null )//create
		{
			setTypeSelect( dataTypes[0] );
			setName( DEFAULT_ITEM_NAME );
		}
		else
		{
			setName( getBinding( ).getName( ) );
			setDisplayName( getBinding( ).getDisplayName( ) );
			setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( getBinding( ).getDataType( ) )
					.getDisplayName( ) );
			setDataFieldExpression( getBinding( ).getExpression( ) );
		}
		if ( isAggregate( ) )
		{
			initFunction( );
			initDataFields( );
			initFilter( );
			initAggOn( );
		}
	}

	private void initAggOn( )
	{
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );
			String[] aggOns = getAggOns( xtabHandle );
			cmbAggOn.setItems( aggOns );

			if ( getBinding( ) != null )
			{
				List aggOnList = getBinding( ).getAggregateOnList( );
				String aggstr = "";
				int i = 0;
				for ( Iterator iterator = aggOnList.iterator( ); iterator.hasNext( ); )
				{
					if ( i > 0 )
						aggstr += ",";
					String name = (String) iterator.next( );
					aggstr += name;
					i++;
				}
				for ( int j = 0; j < aggOns.length; j++ )
				{
					if ( aggOns[j].equals( aggstr ) )
						cmbAggOn.select( j );
				}
			}
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
			if ( argsMap.containsKey( arg.getName( ) ) )
			{
				if ( arg.getValue( ) != null )
				{
					Text txtArg = (Text) argsMap.get( arg.getName( ) );
					txtArg.setText( arg.getValue( ) );
				}
			}
		}
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
				.getElement( ReportDesignConstants.MEASURE_ELEMENT )
				.getProperty( IMeasureModel.FUNCTION_PROP )
				.getAllowedChoices( )
				.getChoices( );
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
			String[] mesures = new String[xtabHandle.getMeasureCount( )];
			for ( int i = 0; i < mesures.length; i++ )
			{
				mesures[i] = DEUtil.getExpression( xtabHandle.getMeasure( i ).getCubeMeasure( ) );
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
		cmbDataField = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbDataField.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

		WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbDataField.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				validate( );
			}
		} );

//		cmbDataField.addSelectionListener( new SelectionAdapter( ) {
//
//			public void widgetSelected( SelectionEvent e )
//			{
//				cmbDataField.setText( getColumnBindingExpressionByName( cmbDataField.getText( ) ) );
//			}
//		} );

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

		cmbAggOn = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbAggOn.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );

	}

	private void createCommonSection( Composite composite )
	{
		new Label( composite, SWT.NONE ).setText( EXPRESSION );
		txtExpression = new Text( composite, SWT.BORDER );
		txtExpression.setLayoutData( new GridData( GridData.FILL_HORIZONTAL
				| GridData.GRAB_HORIZONTAL ) );
		createExpressionButton( composite, txtExpression );
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
					lblArg.setText( argName + ":" );
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
		dialog.getShell( ).layout( );
	}

	private void createExpressionButton( final Composite parent, final Text text )
	{
		Button expressionButton = new Button( parent, SWT.PUSH );

		if ( expressionProvider == null )
			expressionProvider = new BindingExpressionProvider( this.bindingHolder );

		setExpressionButtonImage( expressionButton );
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
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );
			String[] mesureNames = new String[xtabHandle.getMeasureCount( )];
			for ( int i = 0; i < mesureNames.length; i++ )
			{
				if ( xtabHandle.getMeasure( i )
						.getCubeMeasureName( )
						.equals( name ) )
					return DEUtil.getExpression( xtabHandle.getMeasure( i )
							.getCubeMeasure( ) );
			}
		}
		catch ( ExtendedElementException e )
		{
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
					argList.add( argInfo.getName( ) );
				}
				break;
			}
		}
		return argList;
	}

	public void save( ) throws Exception
	{
		if ( txtName.getText( ) != null
				&& txtName.getText( ).trim( ).length( ) > 0 )
		{

			if ( isAggregate( ) )
			{
				saveAggregate( );
			}
			else
			{
				if ( getBinding( ) == null )
				{
					ComputedColumn newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
							txtName.getText( ) );
					for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
					{
						if ( DATA_TYPE_CHOICES[i].getDisplayName( )
								.endsWith( cmbType.getText( ) ) )
						{
							newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
							break;
						}
					}
					newBinding.setExpression( txtExpression.getText( ) );
					newBinding.setDisplayName( txtDisplayName.getText( ) );
					this.binding = DEUtil.addColumn( getBindingHolder( ),
							newBinding,
							true );
				}
				else
				{
					for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
					{
						if ( DATA_TYPE_CHOICES[i].getDisplayName( )
								.endsWith( cmbType.getText( ) ) )
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
		if ( getBinding( ) == null )
		{
			if ( cmbDataField.getText( ) == null
					|| cmbDataField.getText( ).trim( ).length( ) == 0 )
			{
				return;
			}
			ComputedColumn newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
					txtName.getText( ) );
			newBinding.setDisplayName( txtDisplayName.getText( ) );
			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.endsWith( cmbType.getText( ) ) )
				{
					newBinding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}
			newBinding.setExpression( cmbDataField.getText( ) );
			newBinding.setAggregateFunction( getFunctionByDisplayName( cmbFunction.getText( ) ) );
			newBinding.setFilterExpression( txtFilter.getText( ) );

			newBinding.clearAggregateOnList( );
			String aggStr = cmbAggOn.getText( );
			StringTokenizer token = new StringTokenizer( aggStr );

			while ( token.hasMoreTokens( ) )
			{
				String agg = token.nextToken( );
				if ( !agg.equals( ALL ) )
					newBinding.addAggregateOn( agg );
			}

			this.binding = DEUtil.addColumn( getBindingHolder( ),
					newBinding,
					true );

			for ( Iterator iterator = argsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
				argHandle.setName( arg );
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

			this.binding.clearAggregateOnList( );
			String aggStr = cmbAggOn.getText( );
			StringTokenizer token = new StringTokenizer( aggStr );

			while ( token.hasMoreTokens( ) )
			{
				String agg = token.nextToken( );
				if ( !agg.equals( ALL ) )
					this.binding.addAggregateOn( agg );
			}

			this.binding.clearArgumentList( );

			for ( Iterator iterator = argsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
				argHandle.setName( arg );
				argHandle.setValue( ( (Text) argsMap.get( arg ) ).getText( ) );
				this.binding.addArgument( argHandle );
			}
		}
	}

	public void validate( )
	{
		if ( txtName != null
				&& ( txtName.getText( ) == null || txtName.getText( )
						.trim( )
						.equals( "" ) ) )
		{
			dialog.setCanFinish( false );
		}
		else if ( cmbDataField != null
				&& ( cmbDataField.getText( ) == null || cmbDataField.getText( )
						.trim( )
						.equals( "" ) ) )
		{
			dialog.setCanFinish( false );
		}
		else
		{
			dialog.setCanFinish( true );
		}
	}

}
