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

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.CubeQueryUtil;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo;
import org.eclipse.birt.report.data.adapter.api.timeFunction.IArgumentInfo.Period_Type;
import org.eclipse.birt.report.data.adapter.api.timeFunction.ITimeFunction;
import org.eclipse.birt.report.data.adapter.api.timeFunction.TimeFunctionManager;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.data.function.layout.IArgumentLayout;
import org.eclipse.birt.report.designer.internal.ui.dialogs.AbstractBindingDialogHelper;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionButton;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.extension.IExtendedDataModelUIAdapter;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CLabel;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MenuButton;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.expressions.ExpressionFilter;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.CalculationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.CalculationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureGroupHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.TraverseEvent;
import org.eclipse.swt.events.TraverseListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
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
	protected static final String DISPLAY_NAME_ID = Messages.getString( "BindingDialogHelper.text.displayNameID" ); //$NON-NLS-1$
	protected static final String DEFAULT_ITEM_NAME = Messages.getString( "BindingDialogHelper.bindingName.dataitem" ); //$NON-NLS-1$
	protected static final String DEFAULT_AGGREGATION_NAME = Messages.getString( "BindingDialogHelper.bindingName.aggregation" ); //$NON-NLS-1$
	private static final String DEFAULT_TIMEPERIOD_NAME = Messages.getString( "CrosstabBindingDialogHelper.bindngName.timeperiod" ); //$NON-NLS-1$
	private static final String CALCULATION_TYPE = Messages.getString( "CrosstabBindingDialogHelper.calculation.label" ); //$NON-NLS-1$
	private static final String CALCULATION_GROUP = Messages.getString( "CrosstabBindingDialogHelper.calculation.group" ); //$NON-NLS-1$

	private static final String EMPTY_STRING = ""; //$NON-NLS-1$

	protected static final IChoiceSet DATA_TYPE_CHOICE_SET = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( );
	protected static final IChoice[] DATA_TYPE_CHOICES = DATA_TYPE_CHOICE_SET.getChoices( null );
	protected String[] dataTypes = ChoiceSetFactory.getDisplayNamefromChoiceSet( DATA_TYPE_CHOICE_SET );

	private Text txtName, txtFilter, txtExpression;
	private Text dateText;
	private Combo cmbType, cmbFunction, cmbAggOn, calculationType,
			timeDimension;
	private Composite paramsComposite;
	private Group calculationComposite;

	private Map<String, Control> paramsMap = new HashMap<String, Control>( );
	private Map<String, String> paramsValueMap = new HashMap<String, String>( );

	private Composite composite;
	private Text txtDisplayName, txtDisplayNameID;
	private ComputedColumn newBinding;
	private CLabel messageLine;
	private Label lbName, lbDisplayNameID;
	private Object container;
	private Button btnDisplayNameID, btnRemoveDisplayNameID;
	private List<ITimeFunction> times;
	private Button todayButton, dateSelectionButton, recentButton;
	private Label dateFormatLbl;
	private Map<String, Control> calculationParamsMap = new HashMap<String, Control>( );
	private Map<String, String> calculationParamsValueMap = new HashMap<String, String>( );
	private boolean isStatic = true;
	private ExpressionButton button;
	private boolean commonBinding = false;
	
	public void createContent( Composite parent )
	{
		composite = parent;

		( (GridLayout) composite.getLayout( ) ).numColumns = 4;

		lbName = new Label( composite, SWT.NONE );
		lbName.setText( NAME );

		txtName = new Text( composite, SWT.BORDER );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		gd.widthHint = 250;
		txtName.setLayoutData( gd );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		txtName.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				modifyDialogContent( );
				validate( );
			}

		} );

		lbDisplayNameID = new Label( composite, SWT.NONE );
		lbDisplayNameID.setText( DISPLAY_NAME_ID );
		lbDisplayNameID.addTraverseListener( new TraverseListener( ) {

			public void keyTraversed( TraverseEvent e )
			{
				if ( e.detail == SWT.TRAVERSE_MNEMONIC && e.doit )
				{
					e.detail = SWT.TRAVERSE_NONE;
					if ( btnDisplayNameID.isEnabled( ) )
					{
						openKeySelectionDialog( );
					}
				}
			}
		} );

		txtDisplayNameID = new Text( composite, SWT.BORDER | SWT.READ_ONLY );
		txtDisplayNameID.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				modifyDialogContent( );
				validate( );
			}

		} );
		txtDisplayNameID.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnDisplayNameID = new Button( composite, SWT.NONE );
		btnDisplayNameID.setEnabled( getAvailableResourceUrls( ) != null
				&& getAvailableResourceUrls( ).length > 0 ? true : false );
		btnDisplayNameID.setText( "..." ); //$NON-NLS-1$
		btnDisplayNameID.setToolTipText( Messages.getString( "ResourceKeyDescriptor.button.browse.tooltip" ) ); //$NON-NLS-1$
		btnDisplayNameID.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				openKeySelectionDialog( );
			}
		} );

		btnRemoveDisplayNameID = new Button( composite, SWT.NONE );
		btnRemoveDisplayNameID.setImage( ReportPlatformUIImages.getImage( ISharedImages.IMG_TOOL_DELETE ) );
		btnRemoveDisplayNameID.setToolTipText( Messages.getString( "ResourceKeyDescriptor.button.reset.tooltip" ) ); //$NON-NLS-1$
		btnRemoveDisplayNameID.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				txtDisplayNameID.setText( EMPTY_STRING );
				txtDisplayName.setText( EMPTY_STRING );
				updateRemoveBtnState( );
			}
		} );

		new Label( composite, SWT.NONE ).setText( DISPLAY_NAME );
		txtDisplayName = new Text( composite, SWT.BORDER );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		txtDisplayName.setLayoutData( gd );
		txtDisplayName.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				modifyDialogContent( );
				validate( );
			}

		} );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );
		GridData gData = new GridData( SWT.FILL, SWT.FILL, true, true, 4, 1 );
		gData.heightHint = lbName.computeSize(SWT.DEFAULT, SWT.DEFAULT).y;
		Label upperBreak = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		upperBreak.setLayoutData(gData);

		new Label( composite, SWT.NONE ).setText( DATA_TYPE );
		cmbType = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
		cmbType.setLayoutData( gd );
		cmbType.setVisibleItemCount( 30 );

		cmbType.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent arg0 )
			{
				validate( );
			}

			public void widgetSelected( SelectionEvent arg0 )
			{
				modifyDialogContent( );

				validate( );
			}
		} );
		if ( isTimePeriod( ) )
		{
			createCalculationSelection( composite );
		}

		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		if ( isAggregate( ) )
		{
			createAggregateSection( composite );
		}
		else if ( isCommonBinding( ) )
		{
			createCommonSection( composite );
		}
		else
		{
			createHybridSection( composite );
		}
		
		Label lowerBreak = new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL);
		lowerBreak.setLayoutData(gData);
		
		if ( isTimePeriod( ) )
		{
			new Label( composite, SWT.NONE ).setText( Messages.getString( "CrosstabBindingDialogHelper.timedimension.label" ) ); //$NON-NLS-1$
			timeDimension = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
			timeDimension.setLayoutData( gd );
			timeDimension.setVisibleItemCount( 30 );

			timeDimension.addSelectionListener( new SelectionListener( ) {

				public void widgetDefaultSelected( SelectionEvent arg0 )
				{
					validate( );
				}

				public void widgetSelected( SelectionEvent arg0 )
				{
					handleTimeDimensionSelectEvent( );

					modifyDialogContent( );

					validate( );
				}
			} );

			createDataSelection( composite );
		}
		createMessageSection( composite );

		gd = new GridData( GridData.FILL_BOTH );
		composite.setLayoutData( gd );
		setContentSize( composite );
	}

	private void createDataSelection( Composite composite )
	{
		Label referDataLabel = new Label( composite, SWT.NONE );
		referDataLabel.setText( Messages.getString( "CrosstabBindingDialogHelper.referencedate.label" ) ); //$NON-NLS-1$

		GridData gd = new GridData( );
		gd.verticalAlignment = SWT.BEGINNING;
		referDataLabel.setLayoutData( gd );
		Composite radioContainer = new Composite( composite, SWT.NONE );
		GridLayout layout = new GridLayout( );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;
		radioContainer.setLayoutData( gd );

		layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 2;
		radioContainer.setLayout( layout );

		todayButton = new Button( radioContainer, SWT.RADIO );
		todayButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				dateButtonSelection( true );
			}
		} );
		todayButton.setText( Messages.getString( "CrosstabBindingDialogHelper.today.label" ) ); //$NON-NLS-1$
		todayButton.setLayoutData( new GridData( GridData.FILL,
				GridData.FILL,
				true,
				false,
				2,
				1 ) );

		dateSelectionButton = new Button( radioContainer, SWT.RADIO );
		dateSelectionButton.setText( Messages.getString( "CrosstabBindingDialogHelper.thisdate.label" ) ); //$NON-NLS-1$
		dateSelectionButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				dateButtonSelection( true );
			}
		} );

		Composite dateSelecionContainer = new Composite( radioContainer,
				SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		dateSelecionContainer.setLayoutData( gridData );

		layout = new GridLayout( );
		layout.marginWidth = layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		layout.numColumns = 2;
		dateSelecionContainer.setLayout( layout );

		dateText = new Text( dateSelecionContainer, SWT.BORDER | SWT.MULTI );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = dateText.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y
				- dateText.getBorderWidth( )
				* 2;
		dateText.setLayoutData( gd );
		dateText.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				dateFormatLbl.setText( getDateHintText( ) );
				modifyDialogContent( );
				validate( );
			}
		} );
		dateText.addFocusListener( new FocusListener( ) {

			public void focusGained( FocusEvent e )
			{
				todayButton.setSelection( false );
				recentButton.setSelection( false );
				dateSelectionButton.setSelection( true );
				dateButtonSelection( true );
			}

			public void focusLost( FocusEvent e )
			{

			}
		} );

		if ( expressionProvider == null )
		{
			if ( isAggregate( ) )
				expressionProvider = new CrosstabAggregationExpressionProvider( this.bindingHolder,
						this.binding );
			else
				expressionProvider = new CrosstabBindingExpressionProvider( this.bindingHolder,
						this.binding );
		}

		button = ExpressionButtonUtil.createExpressionButton( dateSelecionContainer,
				dateText,
				expressionProvider,
				this.bindingHolder,
				changeExprTypeListener,
				true,
				SWT.PUSH );
		dateText.setData( ExpressionButtonUtil.EXPR_TYPE,
				ExpressionType.CONSTANT );
		button.refresh( );

		new Label( radioContainer, SWT.NONE );
		dateFormatLbl = new Label( radioContainer, SWT.NONE );
		dateFormatLbl.setText( getDateHintText( ) );
		dateFormatLbl.setForeground( ColorManager.getColor( 128, 128, 128 ) );
		dateFormatLbl.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		recentButton = new Button( radioContainer, SWT.RADIO );
		recentButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent event )
			{
				if ( isStatic )
				{
					isStatic = false;
					initCalculationTypeCombo( getTimeDimsionName( ) );
				}
				modifyDialogContent( );
				validate( );
			}
		} );
		recentButton.setText( Messages.getString( "CrosstabBindingDialogHelper.recentday.description" ) ); //$NON-NLS-1$
		recentButton.setLayoutData( new GridData( GridData.FILL,
				GridData.FILL,
				true,
				false,
				3,
				1 ) );

		radioContainer.setTabList( new Control[]{
				todayButton,
				dateSelectionButton,
				dateSelecionContainer,
				recentButton
		} );
	}

	private void dateButtonSelection( boolean isStaticDate )
	{
		if ( isStaticDate )
		{
			if ( !isStatic )
			{
				isStatic = true;
				initCalculationTypeCombo( getTimeDimsionName( ) );
			}
		}
		else
		{
			if ( isStatic )
			{
				isStatic = false;
				initCalculationTypeCombo( getTimeDimsionName( ) );
			}
		}
		modifyDialogContent( );
		validate( );
	}

	private String getDateHintText( )
	{
		return ExpressionType.CONSTANT.equalsIgnoreCase( button.getExpressionHelper( )
				.getExpressionType( ) ) ? Messages.getString( "CrosstabBindingDialogHelper.thisdate.example.label.constant" )
				: Messages.getString( "CrosstabBindingDialogHelper.thisdate.example.label" );
	}

	private Listener changeExprTypeListener = new Listener( ) {

		public void handleEvent( Event e )
		{
			if ( e.widget instanceof MenuButton )
			{
				dateFormatLbl.setText( getDateHintText( ) );
				composite.getShell( ).pack( true );
			}
		}

	};

	private void handleTimeDimensionSelectEvent( )
	{
		String dimensionName = getTimeDimsionName( );
		initCalculationTypeCombo( dimensionName );
		boolean inUseDimsion = isUseDimension( dimensionName );
		if ( inUseDimsion )
		{
			recentButton.setEnabled( true );
		}
		else
		{
			recentButton.setEnabled( false );
		}
	}

	private boolean isUseDimension( String dimensionName )
	{
		boolean inUseDimsion = false;
		CrosstabReportItemHandle crosstab = getCrosstabReportItemHandle( );
		if ( crosstab.getCube( ) == null )
		{
			return false;
		}
		int count = crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE );
		for ( int i = 0; i < count; i++ )
		{
			DimensionHandle cubeDim = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE, i ).getCubeDimension( );
			if( cubeDim == null )
			{
				continue;
			}
			if ( cubeDim.getName( ).equals( dimensionName ) )
			{
				inUseDimsion = true;
			}
		}

		count = crosstab.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE );
		for ( int i = 0; i < count; i++ )
		{
			DimensionHandle cubeDim = crosstab.getDimension( ICrosstabConstants.ROW_AXIS_TYPE, i ).getCubeDimension( );
			if( cubeDim == null )
			{
				continue;
			}
			if ( cubeDim.getName( ).equals( dimensionName ) )
			{
				inUseDimsion = true;
			}
		}

		return inUseDimsion;
	}

	private void initCalculationTypeCombo( String dimensionName )
	{
		CubeHandle cube = getCrosstabReportItemHandle( ).getCube( );
		if ( cube == null )
		{
			return;
		}
		DimensionHandle handle = cube.getDimension( dimensionName );
		String cal = calculationType.getText( );
		isStatic = true;
		if ( recentButton.getSelection( ) )
		{
			isStatic = false;
		}
		times = TimeFunctionManager.getCalculationTypes( handle,
				getUseLevels( dimensionName ),
				isStatic );
		
		Collections.sort(times, new Comparator<ITimeFunction>() {

			public int compare(ITimeFunction o1, ITimeFunction o2) 
			{
				return o1.getDisplayName().compareTo(o2.getDisplayName());
			}
		});
		String[] items = new String[times.size( )];
		String[] names = new String[times.size( )];
		for ( int i = 0; i < times.size( ); i++ )
		{
			items[i] = times.get( i ).getDisplayName( );
			names[i] = times.get( i ).getName( );
		}

		calculationType.setItems( items );
		if ( getBinding( ) == null )
		{
			if ( cal != null && getItemIndex( items, cal ) >= 0 )
			{
				calculationType.select( getItemIndex( items, cal ) );
			}
			else
			{
				calculationType.select( 0 );
			}
			handleCalculationSelectEvent( );
		}
		else
		{
			if ( cal != null && getItemIndex( items, cal ) >= 0 )
			{
				calculationType.select( getItemIndex( items, cal ) );
			}
			else
			{
				ITimeFunction function = getTimeFunctionByDisplaName( getBinding( ).getCalculationType( ) );
				if ( function == null )
				{
					return;
				}
				String name = function.getName( );
				int itemIndex = getItemIndex( names, name );
				if ( itemIndex >= 0 )
				{
					calculationType.select( itemIndex );
				}
				else
				{
					calculationType.select( 0 );
				}
			}
			handleCalculationSelectEvent( );
			ITimeFunction function = getTimeFunctionByIndex( calculationType.getSelectionIndex( ) );
			List<IArgumentInfo> infos = function.getArguments( );
			for ( int i = 0; i < infos.size( ); i++ )
			{
				String argName = infos.get( i ).getName( );
				String argValue = calculationParamsValueMap.get( argName );
				if ( calculationParamsMap.containsKey( argName ) )
				{
					if ( getArgumentValue( getBinding( ), argName ) != null )
					{
						Control control = calculationParamsMap.get( argName );
						ExpressionHandle obj = (ExpressionHandle) getArgumentValue( getBinding( ),
								argName );
						if ( infos.get( i ).getPeriodChoices( ) == null
								|| infos.get( i ).getPeriodChoices( ).isEmpty( ) )
						{
							initExpressionButtonControl( control, obj );
						}
						else
						{
							// Period_Type type = (Period_Type)obj;
							Combo combo = (Combo) control;
							String str = obj.getStringExpression( );
							if ( str == null || str.length( ) == 0 )
							{
								combo.select( 0 );
							}
							else
							{
								int comboIndex = getItemIndex( combo.getItems( ),
										str );
								if ( comboIndex >= 0 )
								{
									combo.select( comboIndex );
								}
								else
								{
									combo.select( 0 );
								}

							}
						}
						// restore arg value
						if ( control instanceof Text && argValue != null )
						{
							( (Text) control ).setText( argValue );
						}
					}
				}
			}

			// init args
		}

	}

	private static ExpressionButton getExpressionButton( Control control )
	{
		Object button = control.getData( ExpressionButtonUtil.EXPR_BUTTON );
		if ( button instanceof ExpressionButton )
		{
			return ( (ExpressionButton) button );
		}
		return null;
	}

	public static void initExpressionButtonControl( Control control,
			ExpressionHandle value )
	{

		ExpressionButton button = getExpressionButton( control );
		if ( button != null && button.getExpressionHelper( ) != null )
		{
			button.getExpressionHelper( ).setExpressionType( value == null
					|| value.getType( ) == null ? UIUtil.getDefaultScriptType( )
					: (String) value.getType( ) );
			String stringValue = value == null
					|| value.getExpression( ) == null ? "" : (String) value.getExpression( ); //$NON-NLS-1$
			button.getExpressionHelper( ).setExpression( stringValue );
			button.refresh( );
		}
	}

	private List<String> getUseLevels( String dimensionName )
	{
		List<String> retValue = new ArrayList<String>( );
		DimensionViewHandle viewHandle = getCrosstabReportItemHandle( ).getDimension( dimensionName );
		if ( viewHandle == null )
		{
			return retValue;
		}
		int count = viewHandle.getLevelCount( );
		for ( int i = 0; i < count; i++ )
		{
			LevelViewHandle levelHandle = viewHandle.getLevel( i );
			retValue.add( levelHandle.getCubeLevel( ).getName( ) );
		}
		return retValue;
	}

	private CrosstabReportItemHandle getCrosstabReportItemHandle( )
	{
		try
		{
			return (CrosstabReportItemHandle) ( ( (ExtendedItemHandle) bindingHolder ).getReportItem( ) );
		}
		catch ( ExtendedElementException e )
		{
			return null;
		}
	}

	private Object getArgumentValue( ComputedColumnHandle handle, String name )
	{
		Iterator iter = handle.calculationArgumentsIterator( );
		while ( iter.hasNext( ) )
		{
			CalculationArgumentHandle argument = (CalculationArgumentHandle) iter.next( );
			if ( name.equals( argument.getName( ) ) )
			{
				return argument.getValue( );
			}
		}
		return null;
	}

	private String getTimeDimsionName( )
	{
		String dimensionName = timeDimension.getText( );
		return dimensionName;
		// Set<IDimLevel> sets;
		// try
		// {
		// sets = ExpressionUtil.getReferencedDimLevel( dimensionName );
		// }
		// catch ( CoreException e )
		// {
		// return null;
		// }
		// Iterator<IDimLevel> iter = sets.iterator( );
		// if ( iter.hasNext( ) )
		// {
		// return iter.next( ).getDimensionName( );
		// }
		//
		// return null;
	}

	private void createCalculationSelection( Composite composite )
	{
		calculationComposite = new Group( composite, SWT.NONE );
		calculationComposite.setText( CALCULATION_GROUP );

		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 4;
		// gridData.exclude = true;
		calculationComposite.setLayoutData( gridData );

		GridLayout layout = new GridLayout( );
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		// layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 5;
		Layout parentLayout = calculationComposite.getParent( ).getLayout( );
		if ( parentLayout instanceof GridLayout )
			layout.horizontalSpacing = ( (GridLayout) parentLayout ).horizontalSpacing;
		calculationComposite.setLayout( layout );

		Label calculationLable = new Label( calculationComposite, SWT.NONE );
		calculationLable.setText( CALCULATION_TYPE );
		GridData gd = new GridData( );
		gd.widthHint = lbName.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x
				- layout.marginWidth
				- 3;
		calculationLable.setLayoutData( gd );

		calculationType = new Combo( calculationComposite, SWT.BORDER
				| SWT.READ_ONLY );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 4;
		calculationType.setLayoutData( gd );
		calculationType.setVisibleItemCount( 30 );

		calculationType.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent arg0 )
			{
				validate( );
			}

			public void widgetSelected( SelectionEvent arg0 )
			{
				handleCalculationSelectEvent( );

				modifyDialogContent( );

				validate( );
			}
		} );
	}

	private void handleCalculationSelectEvent( )
	{
		Control[] children = calculationComposite.getChildren( );
		for ( int i = 2; i < children.length; i++ )
		{
			children[i].dispose( );
		}

		ITimeFunction function = getTimeFunctionByIndex( calculationType.getSelectionIndex( ) );
		if ( function == null )
		{
			// ( (GridData) calculationComposite.getLayoutData( ) ).heightHint =
			// 0;
			// ( (GridData) calculationComposite.getLayoutData( ) ).exclude =
			// true;
			// calculationComposite.setVisible( false );
		}
		else
		{
			calculationParamsMap.clear( );
			List<IArgumentInfo> infos = function.getArguments( );
			if ( infos == null || infos.size( ) == 0 )
			{
				// ( (GridData) calculationComposite.getLayoutData( )
				// ).heightHint = 0;
				// ( (GridData) calculationComposite.getLayoutData( ) ).exclude
				// = true;
				// calculationComposite.setVisible( false );
			}
			else
			{
				List<IArgumentLayout> argLayouts = DataService.getInstance( )
						.getArgumentLayout( function, infos );

				( (GridData) calculationComposite.getLayoutData( ) ).exclude = false;
				( (GridData) calculationComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;
				calculationComposite.setVisible( true );

				int width = 0;
				if ( calculationComposite.getParent( ).getLayout( ) instanceof GridLayout )
				{
					Control[] controls = calculationComposite.getParent( )
							.getChildren( );
					for ( int i = 0; i < controls.length; i++ )
					{
						if ( controls[i] instanceof Label
								&& ( (GridData) controls[i].getLayoutData( ) ).horizontalSpan == 1 )
						{
							int labelWidth = controls[i].getBounds( ).width
									- controls[i].getBorderWidth( )
									* 2;
							if ( labelWidth > width )
								width = labelWidth;
						}
					}
				}

				for ( int i = 0; i < infos.size( ); i++ )
				{
					IArgumentInfo info = infos.get( i );
					IArgumentLayout argLayout = argLayouts.get( i );
					int layoutHint = argLayout.getLayoutHint( );

					final List<Period_Type> types = info.getPeriodChoices( );
					final String name = info.getName( );
					final String displayName = info.getDisplayName( );
					Label lblParam = null;

					if ( IArgumentLayout.ALIGN_BLOCK == layoutHint )
					{
						createPeriodLabelPart( lblParam, displayName, false );
						createPeriodPart( name, types, 4 );
					}
					else if ( IArgumentLayout.ALIGN_INLINE_BEFORE == layoutHint )
					{
						createPeriodLabelPart( lblParam, displayName, false );
						createPeriodPart( name, types, 1 );
					}
					else if ( IArgumentLayout.LIGN_INLINEL_AFTER == layoutHint )
					{
						createPeriodPart( name, types, 1 );
						createPeriodLabelPart( lblParam, displayName, true );
					}
					else if ( IArgumentLayout.ALIGN_INLINE_NONE == layoutHint )
					{
						createPeriodPart( name, types, 2 );
					}
				}
			}
		}

		composite.layout( true, true );
		setContentSize( composite );
	}

	/**
	 * Creates the label control part of an argument info.
	 * 
	 * @param lblParam
	 *            The Label control
	 * @param displayName
	 *            The display text
	 * @param width
	 *            the width hint of the control
	 * @param isPlacedAfter
	 *            before or after the Period part
	 */
	private void createPeriodLabelPart( Label lblParam, String displayName,
			boolean isPlacedAfter )
	{
		lblParam = new Label( calculationComposite, SWT.NONE );
		lblParam.setText( displayName + ( isPlacedAfter ? "" : ":" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		// if ( !infos.get( i ).isOptional( ) )
		// lblParam.setText( "*" + lblParam.getText( ) );
		GridData gd = new GridData( );
		gd.widthHint = lblParam.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		// if ( gd.widthHint < width )
		// gd.widthHint = width;
		lblParam.setLayoutData( gd );
	}

	/**
	 * Creates the Period control part of an argument info.
	 * 
	 * @param name
	 *            The name of the argument info
	 * @param types
	 *            The period types
	 * @param numColumns
	 *            Number of columns the control takes
	 */
	private void createPeriodPart( final String name, List<Period_Type> types,
			int numColumns )
	{
		if ( types != null && types.size( ) > 0 )
		{
			final Combo cmbDataField = new Combo( calculationComposite,
					SWT.BORDER | SWT.READ_ONLY );
			cmbDataField.setLayoutData( GridDataFactory.fillDefaults( )
					.grab( true, false )
					.span( numColumns, 1 )
					.create( ) );
			cmbDataField.setVisibleItemCount( 30 );
			initCalculationDataFields( cmbDataField, name, types );

			cmbDataField.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					modifyDialogContent( );
					validate( );

					calculationParamsValueMap.put( name, cmbDataField.getText( ) );
				}
			} );

			calculationParamsMap.put( name, cmbDataField );
		}
		else
		{
			final Text txtParam = new Text( calculationComposite, SWT.BORDER
					| SWT.MULTI );
			GridData gridData = new GridData( SWT.FILL, SWT.FILL, true, false );
			// gridData.horizontalIndent = 0;
			// gridData.horizontalSpan = 2;
			gridData.heightHint = txtParam.computeSize( SWT.DEFAULT,
					SWT.DEFAULT ).y - txtParam.getBorderWidth( ) * 2;
			txtParam.setLayoutData( gridData );
			initCalculationTextFild( txtParam, name );
			txtParam.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					modifyDialogContent( );
					validate( );
					calculationParamsValueMap.put( name, txtParam.getText( ) );
				}
			} );
			createExpressionButton( calculationComposite, txtParam );
			calculationParamsMap.put( name, txtParam );
		}
	}

	private void initCalculationTextFild( Text txtParam, String name )
	{
		if ( calculationParamsValueMap.containsKey( name ) )
		{
			txtParam.setText( calculationParamsValueMap.get( name ) );
			return;
		}
	}

	private void initCalculationDataFields( Combo cmbDataField, String name,
			List<Period_Type> list )
	{
		String[] strs = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			strs[i] = list.get( i ).displayName( );
		}
		cmbDataField.setItems( strs );
		if ( calculationParamsValueMap.containsKey( name ) )
		{
			cmbDataField.setText( calculationParamsValueMap.get( name ) );
			return;
		}
		cmbDataField.select( 0 );
	}

	private ITimeFunction getTimeFunctionByIndex( int index )
	{
		if ( times == null )
		{
			return null;
		}
		if ( index < 0 || index >= times.size( ) )
		{
			return null;
		}

		return times.get( index );
	}

	private ITimeFunction getTimeFunctionByDisplaName( String name )
	{
		if ( times == null )
		{
			return null;
		}

		for ( int i = 0; i < times.size( ); i++ )
		{
			if ( times.get( i ).getName( ).equals( name ) )
			{
				return times.get( i );
			}
		}
		return null;
	}

	private void openKeySelectionDialog( )
	{
		ResourceEditDialog dlg = new ResourceEditDialog( composite.getShell( ),
				Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

		dlg.setResourceURLs( getResourceURLs( ) );

		if ( dlg.open( ) == Window.OK )
		{
			String[] result = (String[]) dlg.getDetailResult( );
			txtDisplayNameID.setText( result[0] );
			txtDisplayName.setText( result[1] );
			updateRemoveBtnState( );
		}
	}

	private boolean hasInitDialog = false;

	public void initDialog( )
	{
		cmbType.setItems( dataTypes );
		txtDisplayName.setFocus( );

		if ( isAggregate( ) )
		{
			initFunction( );
			initFilter( );
			// if (!isTimePeriod( ))
			{
				initAggOn( );
			}
		}
		else if ( isCommonBinding( ) )
		{
		}
		else
		{
			// We need an expression field that behaves like an aggregate
			// In that case, we find the expression logic is heavily
			// tied to the function logic
			initFunction( );
			// if (!isTimePeriod( ))
			{
				initAggOn( );
			}
		}

		if ( isTimePeriod( ) )
		{
			initTimeDimension( );
			initReferenceDate( );
			initCalculationTypeCombo( getTimeDimsionName( ) );
		}

		if ( getBinding( ) == null )// create
		{
			if ( cmbType.getSelectionIndex( ) < 0 )
			{
				setTypeSelect( dataTypes[0] );
			}
			if ( isTimePeriod( ) )
			{
				this.newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
						DEFAULT_TIMEPERIOD_NAME );
			}
			else
			{
				this.newBinding = StructureFactory.newComputedColumn( getBindingHolder( ),
						isAggregate( ) ? DEFAULT_AGGREGATION_NAME
								: DEFAULT_ITEM_NAME );
			}
			setName( this.newBinding.getName( ) );
		}
		else
		{
			setName( getBinding( ).getName( ) );
			setDisplayName( getBinding( ).getDisplayName( ) );
			setDisplayNameID( getBinding( ).getDisplayNameID( ) );
			if ( getBinding( ).getDataType( ) != null )
				if ( DATA_TYPE_CHOICE_SET.findChoice( getBinding( ).getDataType( ) ) != null )
					setTypeSelect( DATA_TYPE_CHOICE_SET.findChoice( getBinding( ).getDataType( ) )
							.getDisplayName( ) );
				else
					cmbType.setText( "" ); //$NON-NLS-1$
			if ( getBinding( ).getExpression( ) != null )
				setDataFieldExpression( getBinding( ) );
		}

		if ( this.getBinding( ) != null )
		{
			this.txtName.setEnabled( false );
		}

		validate( );

		hasInitDialog = true;

		composite.getShell( ).pack( );
	}

	private void initReferenceDate( )
	{
		String dimensionName = getTimeDimsionName( );

		boolean inUseDimsion = isUseDimension( dimensionName );

		if ( getBinding( ) == null ) // new Relative Time Period
		{
			ExtendedItemHandle handle = (ExtendedItemHandle) getBindingHolder( );

			List<ComputedColumnHandle> dimensionHandle = new ArrayList( );

			for ( Iterator<ComputedColumnHandle> iter = handle.columnBindingsIterator( ); iter.hasNext( ); )
			{
				ComputedColumnHandle cHandle = iter.next( );

				if ( cHandle.getTimeDimension( ) != null
						&& !cHandle.getTimeDimension( ).equals( EMPTY_STRING ) )
				{
					dimensionHandle.add( 0, cHandle );
				}
			}

			if ( 0 == dimensionHandle.size( )
					|| !setRefDate( dimensionHandle.get( 0 ), inUseDimsion ) )
			{
				todayButton.setSelection( true );
			}
		}
		else
		// edit Relative Time Period
		{
			setRefDate( getBinding( ), inUseDimsion );
		}
		if ( inUseDimsion )
		{
			recentButton.setEnabled( true );
		}
		else
		{
			recentButton.setEnabled( false );
		}

	}

	private boolean setRefDate( ComputedColumnHandle handle,
			boolean inUseDimsion )
	{
		String type = handle.getReferenceDateType( );

		if ( type == null )
			return false;

		if ( DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY.equals( type ) )
		{
			todayButton.setSelection( true );
			return true;
		}
		else if ( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE.equals( type ) )
		{
			dateSelectionButton.setSelection( true );
			ExpressionHandle value = handle.getReferenceDateValue( );

			dateText.setText( value == null || value.getExpression( ) == null ? "" : (String) value.getExpression( ) ); //$NON-NLS-1$
			dateText.setData( ExpressionButtonUtil.EXPR_TYPE, value == null
					|| value.getType( ) == null ? ExpressionType.CONSTANT
					: (String) value.getType( ) );
			ExpressionButton button = (ExpressionButton) dateText.getData( ExpressionButtonUtil.EXPR_BUTTON );
			if ( button != null )
				button.refresh( );

			dateFormatLbl.setText( getDateHintText( ) );

			return true;
		}
		else if ( DesignChoiceConstants.REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION.equals( type ) )
		{
			if ( getBinding( ) == null && !inUseDimsion )
			{
				return false;
			}
			else
			{
				recentButton.setSelection( true );
				return true;
			}
		}

		return false;
	}

	private void initTimeDimension( )
	{
		String[] strs = getTimeDimensions( );
		if ( strs == null )
		{
			return;
		}
		timeDimension.setItems( strs );

		if ( getBinding( ) == null )
		{
			String str = getFirstUseDimensonDisplayName( );
			if ( str != null && str.length( ) > 0 )
			{
				int itemIndex = getItemIndex( strs, str );
				if ( itemIndex >= 0 )
				{
					timeDimension.select( itemIndex );
				}
				else
				{
					timeDimension.select( 0 );
				}
			}
			else
			{
				timeDimension.select( 0 );
			}
		}
		else
		{
			String value = getBinding( ).getTimeDimension( );
			int itemIndex = getItemIndex( strs, value );
			timeDimension.select( itemIndex );
		}
	}

	private String getFirstUseDimensonDisplayName( )
	{
		CrosstabReportItemHandle crosstab = getCrosstabReportItemHandle( );
		int count = crosstab.getDimensionCount( ICrosstabConstants.COLUMN_AXIS_TYPE );
		for ( int i = 0; i < count; i++ )
		{
			DimensionViewHandle viewHandle = crosstab.getDimension( ICrosstabConstants.COLUMN_AXIS_TYPE,
					i );
			if ( isAvaliableTimeDimension( viewHandle.getCubeDimension( ) ) )
			{
				// return ExpressionUtil.createJSDimensionExpression(
				// viewHandle.getCubeDimension( )
				// .getName( ),
				// null );
				return viewHandle.getCubeDimension( ).getName( );
			}
		}

		count = crosstab.getDimensionCount( ICrosstabConstants.ROW_AXIS_TYPE );
		for ( int i = 0; i < count; i++ )
		{
			DimensionViewHandle viewHandle = crosstab.getDimension( ICrosstabConstants.ROW_AXIS_TYPE,
					i );
			if ( isAvaliableTimeDimension( viewHandle.getCubeDimension( ) ) )
			{
				// return ExpressionUtil.createJSDimensionExpression(
				// viewHandle.getCubeDimension( )
				// .getName( ),
				// null );
				return viewHandle.getCubeDimension( ).getName( );
			}
		}
		return null;
	}

	private boolean isAvaliableTimeDimension( DimensionHandle dimension )
	{
		if ( CrosstabAdaptUtil.isTimeDimension( dimension ) )
		{
			DimensionViewHandle viewHandle = getCrosstabReportItemHandle( ).getDimension( dimension.getName( ) );
			if ( viewHandle == null )
			{
				int count = dimension.getDefaultHierarchy( ).getLevelCount( );
				if ( count == 0 )
				{
					return false;
				}
				LevelHandle levelHandle = dimension.getDefaultHierarchy( )
						.getLevel( 0 );
				if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( levelHandle.getDateTimeLevelType( ) ) )
				{
					return true;
				}
			}
			else
			{
				int count = viewHandle.getLevelCount( );
				if ( count == 0 )
				{
					return false;
				}
				LevelViewHandle levelViewHandle = viewHandle.getLevel( 0 );
				if ( DesignChoiceConstants.DATE_TIME_LEVEL_TYPE_YEAR.equals( levelViewHandle.getCubeLevel( )
						.getDateTimeLevelType( ) ) )
				{
					return true;
				}
			}
		}

		return false;
	}

	private String[] getTimeDimensions( )
	{
		List<String> strs = new ArrayList<String>( );

		CrosstabReportItemHandle crosstab = getCrosstabReportItemHandle( );
		CubeHandle cube = crosstab.getCube( );
		if ( cube == null )
		{
			return new String[0];
		}
		List list = cube.getContents( ICubeModel.DIMENSIONS_PROP );
		if( list == null )
		{
			return new String[0];
		}
		for ( int i = 0; i < list.size( ); i++ )
		{
			DimensionHandle dimension = (DimensionHandle) list.get( i );
			if ( isAvaliableTimeDimension( dimension ) )
			{
				// strs.add( ExpressionUtil.createJSDimensionExpression(
				// dimension.getName( ),
				// null ) );
				strs.add( dimension.getName( ) );
			}
		}

		return strs.toArray( new String[strs.size( )] );
	}

	private void initAggOn( )
	{
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );
			String[] aggOns = getAggOns( xtabHandle );
			cmbAggOn.setItems( aggOns );

			String aggstr = ""; //$NON-NLS-1$
			if ( getBinding( ) != null )
			{
				List aggOnList = getBinding( ).getAggregateOnList( );
				int i = 0;
				for ( Iterator iterator = aggOnList.iterator( ); iterator.hasNext( ); )
				{
					if ( i > 0 )
						aggstr += ","; //$NON-NLS-1$
					String name = (String) iterator.next( );
					aggstr += name;
					i++;
				}
			}
			else if ( isTimePeriod( ) )
			{
				List rowLevelList = getCrosstabViewHandleLevels( xtabHandle,
						ICrosstabConstants.ROW_AXIS_TYPE );
				List columnLevelList = getCrosstabViewHandleLevels( xtabHandle,
						ICrosstabConstants.COLUMN_AXIS_TYPE );
				if ( rowLevelList.size( ) != 0 && columnLevelList.size( ) == 0 )
				{
					aggstr = (String) rowLevelList.get( rowLevelList.size( ) - 1 );
				}
				else if ( rowLevelList.size( ) == 0
						&& columnLevelList.size( ) != 0 )
				{
					aggstr = (String) columnLevelList.get( columnLevelList.size( ) - 1 );
				}
				else if ( rowLevelList.size( ) != 0
						&& columnLevelList.size( ) != 0 )
				{
					aggstr = (String) rowLevelList.get( rowLevelList.size( ) - 1 )
							+ ","
							+ (String) columnLevelList.get( columnLevelList.size( ) - 1 );
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
						aggstr += ","; //$NON-NLS-1$
					}
				}
				if ( cellHandle.getAggregationOnColumn( ) != null )
				{
					aggstr += cellHandle.getAggregationOnColumn( )
							.getFullName( );
				}
			}
			else if ( container instanceof AggregationCellHandle )
			{
				AggregationCellHandle cellHandle = (AggregationCellHandle) container;
				if ( cellHandle.getAggregationOnRow( ) != null )
				{
					aggstr += cellHandle.getAggregationOnRow( ).getFullName( );
					if ( cellHandle.getAggregationOnColumn( ) != null )
					{
						aggstr += ","; //$NON-NLS-1$
					}
				}
				if ( cellHandle.getAggregationOnColumn( ) != null )
				{
					aggstr += cellHandle.getAggregationOnColumn( )
							.getFullName( );
				}
			}
			String[] strs = aggstr.split( "," );//$NON-NLS-1$
			String temAddOns = "";//$NON-NLS-1$
			if ( strs != null && strs.length > 1 )
			{
				for ( int i = strs.length - 1; i >= 0; i-- )
				{
					temAddOns = temAddOns + strs[i];
					if ( i != 0 )
					{
						temAddOns = temAddOns + ",";//$NON-NLS-1$
					}
				}
			}
			for ( int j = 0; j < aggOns.length; j++ )
			{
				if ( aggOns[j].equals( aggstr ) )
				{
					cmbAggOn.select( j );
					return;
				}
				if ( aggOns[j].equals( temAddOns ) )
				{
					cmbAggOn.select( j );
					return;
				}
			}
			cmbAggOn.select( 0 );
		}
		catch ( ExtendedElementException e )
		{
			ExceptionUtil.handle( e );
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
				aggOnList.add( name + "," + name2 ); //$NON-NLS-1$
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
					if ( level.getCubeLevel( ) != null )
					{
						levelList.add( level.getCubeLevel( ).getFullName( ) );
					}
				}
			}
		}
		return levelList;
	}

	private void initFilter( )
	{
		ExpressionButtonUtil.initExpressionButtonControl( txtFilter,
				binding,
				ComputedColumn.FILTER_MEMBER );
	}

	private void initFunction( )
	{
		if ( !isAggregate( ) )
		{
			// In this case there is no function field
			// And the binding has no arguments
			handleFunctionSelectEvent( );
			return;
		}
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
			ExceptionUtil.handle( e );
		}
		// List args = getFunctionArgs( functionString );
		// bindingColumn.argumentsIterator( )
		for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
		{
			AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next( );
			String argName = DataAdapterUtil.adaptArgumentName( arg.getName( ) );
			if ( paramsMap.containsKey( argName ) )
			{
				if ( arg.getValue( ) != null )
				{
					Control control = paramsMap.get( argName );
					ExpressionButtonUtil.initExpressionButtonControl( control,
							arg,
							AggregationArgument.VALUE_MEMBER );
				}
			}
		}
	}

	private String[] getFunctionDisplayNames( )
	{
		IAggrFunction[] choices = getFunctions( );
		if ( choices == null )
			return new String[0];

		String[] displayNames = new String[choices.length];
		for ( int i = 0; i < choices.length; i++ )
		{
			displayNames[i] = choices[i].getDisplayName( );
		}
		return displayNames;
	}

	private IAggrFunction getFunctionByDisplayName( String displayName )
	{
		IAggrFunction[] choices = getFunctions( );
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
			return DataUtil.getAggregationManager( )
					.getAggregation( function )
					.getDisplayName( );
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
			return null;
		}
	}

	private IAggrFunction[] getFunctions( )
	{
		try
		{
			List aggrInfoList = DataUtil.getAggregationManager( )
					.getAggregations( AggregationManager.AGGR_XTAB );
			return (IAggrFunction[]) aggrInfoList.toArray( new IAggrFunction[0] );
		}
		catch ( BirtException e )
		{
			ExceptionUtil.handle( e );
			return new IAggrFunction[0];
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

		return ""; //$NON-NLS-1$
	}

	private void initTextField( Text txtParam, IParameterDefn param )
	{
		if ( paramsValueMap.containsKey( param.getName( ) ) )
		{
			txtParam.setText( paramsValueMap.get( param.getName( ) ) );
			return;
		}
		if ( binding != null )
		{
			for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
			{
				AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next( );
				if ( arg.getName( ).equals( param.getName( ) ) )
				{
					if ( arg.getValue( ) != null )
						txtParam.setText( arg.getValue( ) );
					return;
				}
			}
		}
	}

	/**
	 * fill the cmbDataField with binding holder's bindings
	 * 
	 * @param param
	 */
	private void initDataFields( Combo cmbDataField, IParameterDefn param )
	{
		List<String> datas = getMesures( );
		datas.addAll( getDatas( ) );
		String[] items = datas.toArray( new String[datas.size( )] );
		cmbDataField.setItems( items );

		if ( paramsValueMap.containsKey( param.getName( ) ) )
		{
			cmbDataField.setText( paramsValueMap.get( param.getName( ) ) );
			return;
		}
		if ( binding != null )
		{
			for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
			{
				AggregationArgumentHandle arg = (AggregationArgumentHandle) iterator.next( );
				if ( arg.getName( ).equals( param.getName( ) ) )
				{
					if ( arg.getValue( ) != null )
					{
						for ( int i = 0; i < items.length; i++ )
						{
							if ( items[i].equals( arg.getValue( ) ) )
							{
								cmbDataField.select( i );
								return;
							}
						}
						cmbDataField.setText( arg.getValue( ) );
						return;
					}
				}
			}
			// backforward compatble
			if ( binding.getExpression( ) != null )
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
	}

	private List<String> getMesures( )
	{
		List<String> measures = new ArrayList<String>( );
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );

			measures.add( "" ); //$NON-NLS-1$

			// for ( int i = 0; i < xtabHandle.getMeasureCount( ); i++ )
			// {
			// MeasureViewHandle mv = xtabHandle.getMeasure( i );
			//
			// if ( mv instanceof ComputedMeasureViewHandle )
			// {
			// continue;
			// }
			// measures.add( DEUtil.getExpression( mv.getCubeMeasure( ) ) );
			// }

			CubeHandle cubeHandle = xtabHandle.getCube( );
			
			if(CrosstabUtil.isBoundToLinkedDataSet( xtabHandle ))
			{
				for (int i = 0; i < xtabHandle.getMeasureCount( ); i++)
				{
					if( xtabHandle.getMeasure( i ) instanceof ComputedMeasureViewHandle )
					{
						continue;
					}
					if ( isCommonBinding( ) )
					{
						String str = ExpressionUtil.createDataSetRowExpression( xtabHandle.getMeasure( i ).getCubeMeasureName( ) );
						if ( !measures.contains( str ) )
						{
							measures.add( str );
						}
					}
					else
					{
						String str = ExpressionUtil.createJSMeasureExpression( xtabHandle.getMeasure( i ).getCubeMeasureName( ) );
						if ( !measures.contains( str ) )
						{
							measures.add( str );
						}
					}
				}
			}
			else if ( cubeHandle != null )
			{	
				List children = cubeHandle.getContents( CubeHandle.MEASURE_GROUPS_PROP );
				if( children == null )
				{
					return new ArrayList<String>();
				}
				for ( int i = 0; i < children.size( ); i++ )
				{
					MeasureGroupHandle group = (MeasureGroupHandle) children.get( i );
					List measreHandles = group.getContents( MeasureGroupHandle.MEASURES_PROP );
					for ( int j = 0; j < measreHandles.size( ); j++ )
					{
						MeasureHandle measure = (MeasureHandle) measreHandles.get( j );
						String str = DEUtil.getExpression( measure );
						if ( !measures.contains( str ) )
						{
							measures.add( str );
						}
					}
				}
			}
		}
		catch ( ExtendedElementException e )
		{
		}
		return measures;
	}

	private List<String> getDatas( )
	{
		List<String> datas = new ArrayList<String>( );
		try
		{
			CrosstabReportItemHandle xtabHandle = (CrosstabReportItemHandle) ( (ExtendedItemHandle) getBindingHolder( ) ).getReportItem( );

			try
			{
				IBinding[] aggregateBindings = CubeQueryUtil.getAggregationBindings( getCrosstabBindings( xtabHandle ) );
				for ( IBinding binding : aggregateBindings )
				{
					if ( getBinding( ) == null
							|| !getBinding( ).getName( )
									.equals( binding.getBindingName( ) ) )
						datas.add( ExpressionUtil.createJSDataExpression( binding.getBindingName( ) ) );
				}
			}
			catch ( AdapterException e )
			{
			}
			catch ( BirtException e )
			{
			}

		}
		catch ( ExtendedElementException e )
		{
		}
		return datas;
	}

	private IBinding[] getCrosstabBindings( CrosstabReportItemHandle xtabHandle )
			throws BirtException
	{
		Iterator bindingItr = ( (ExtendedItemHandle) xtabHandle.getModelHandle( ) ).columnBindingsIterator( );
		ModuleHandle module = ( (ExtendedItemHandle) xtabHandle.getModelHandle( ) ).getModuleHandle( );

		List<IBinding> bindingList = new ArrayList<IBinding>( );

		if ( bindingItr != null )
		{
			Map<String, String> cache = new HashMap<String, String>( );

			List<String> rowLevelNameList = new ArrayList<String>( );
			List<String> columnLevelNameList = new ArrayList<String>( );

			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );

			try
			{
				IModelAdapter modelAdapter = session.getModelAdaptor( );

				while ( bindingItr.hasNext( ) )
				{
					ComputedColumnHandle column = (ComputedColumnHandle) bindingItr.next( );

					// now user dte model adpater to transform the binding
					IBinding binding;
					try
					{
						binding = modelAdapter.adaptBinding( column,
								ExpressionLocation.CUBE );
					}
					catch ( Exception e )
					{
						continue;
					}
					if ( binding == null )
					{
						continue;
					}

					// still need add aggregateOn field
					List aggrList = column.getAggregateOnList( );

					if ( aggrList != null )
					{
						for ( Iterator aggrItr = aggrList.iterator( ); aggrItr.hasNext( ); )
						{
							String baseLevel = (String) aggrItr.next( );

							CrosstabUtil.addHierachyAggregateOn( xtabHandle,
									binding,
									baseLevel,
									rowLevelNameList,
									columnLevelNameList,
									cache);
						}
					}
					bindingList.add( binding );
				}
			}
			finally
			{
				session.shutdown( );
			}
		}
		return bindingList.toArray( new IBinding[bindingList.size( )] );
	}

	private void setDataFieldExpression( ComputedColumnHandle binding )
	{
		if ( binding.getExpression( ) != null )
		{
			if ( isAggregate( ) )
			{
				IAggrFunction function = getFunctionByDisplayName( cmbFunction.getText( ) );
				if ( function != null )
				{
					IParameterDefn[] params = function.getParameterDefn( );
					for ( final IParameterDefn param : params )
					{
						if ( param.isDataField( ) )
						{
							Control control = paramsMap.get( param.getName( ) );
							if ( ExpressionButtonUtil.getExpressionButton( control ) != null )
							{
								ExpressionButtonUtil.initExpressionButtonControl( control,
										binding,
										ComputedColumn.EXPRESSION_MEMBER );
							}
							else
							{
								if ( control instanceof Combo )
								{
									( (Combo) control ).setText( binding.getExpression( ) );
								}
								else if ( control instanceof CCombo )
								{
									( (CCombo) control ).setText( binding.getExpression( ) );
								}
								else if ( control instanceof Text )
								{
									( (Text) control ).setText( binding.getExpression( ) );
								}
							}
						}
					}
				}
			}
			else
			{
				if ( txtExpression != null && !txtExpression.isDisposed( ) )
				{
					ExpressionButtonUtil.initExpressionButtonControl( txtExpression,
							binding,
							ComputedColumn.EXPRESSION_MEMBER );
				}
			}
		}
	}

	private void setName( String name )
	{
		if ( name != null && txtName != null )
			txtName.setText( name );
	}

	private void setDisplayName( String displayName )
	{
		if ( displayName != null && txtDisplayName != null )
			txtDisplayName.setText( displayName );
	}

	private void setDisplayNameID( String displayNameID )
	{
		if ( displayNameID != null && txtDisplayNameID != null )
			txtDisplayNameID.setText( displayNameID );
	}

	private void setTypeSelect( String typeSelect )
	{
		if ( cmbType != null )
		{
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
		gd.horizontalSpan = 3;
		cmbFunction.setLayoutData( gd );
		cmbFunction.setVisibleItemCount( 30 );
		// WidgetUtil.createGridPlaceholder( composite, 1, false );

		cmbFunction.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleFunctionSelectEvent( );
				modifyDialogContent( );
				validate( );
			}
		} );

		paramsComposite = new Composite( composite, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 4;
		gridData.exclude = true;
		paramsComposite.setLayoutData( gridData );
		GridLayout layout = new GridLayout( );
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 4;
		Layout parentLayout = paramsComposite.getParent( ).getLayout( );
		if ( parentLayout instanceof GridLayout )
			layout.horizontalSpacing = ( (GridLayout) parentLayout ).horizontalSpacing;
		paramsComposite.setLayout( layout );
		
		createFilterCondition(composite, gridData);

		// if (!isTimePeriod( ))
	    {
			Label lblAggOn = new Label( composite, SWT.NONE );
			lblAggOn.setText( AGGREGATE_ON );
			gridData = new GridData( );
			gridData.verticalAlignment = GridData.BEGINNING;
			lblAggOn.setLayoutData( gridData );

			cmbAggOn = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
			gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 3;
			cmbAggOn.setLayoutData( gridData );
			cmbAggOn.setVisibleItemCount( 30 );
			cmbAggOn.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					modifyDialogContent( );
				}
			} );
		}
	}
	
	private void createHybridSection( Composite composite )
	{

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 3;

		paramsComposite = new Composite( composite, SWT.NONE );
		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 4;
		gridData.exclude = true;
		paramsComposite.setLayoutData( gridData );
		GridLayout layout = new GridLayout( );
		// layout.horizontalSpacing = layout.verticalSpacing = 0;
		layout.marginWidth = layout.marginHeight = 0;
		layout.numColumns = 4;
		Layout parentLayout = paramsComposite.getParent( ).getLayout( );
		if ( parentLayout instanceof GridLayout )
			layout.horizontalSpacing = ( (GridLayout) parentLayout ).horizontalSpacing;
		paramsComposite.setLayout( layout );
		
		// if (!isTimePeriod( ))
	    {
			Label lblAggOn = new Label( composite, SWT.NONE );
			lblAggOn.setText( AGGREGATE_ON );
			gridData = new GridData( );
			gridData.verticalAlignment = GridData.BEGINNING;
			lblAggOn.setLayoutData( gridData );

			cmbAggOn = new Combo( composite, SWT.BORDER | SWT.READ_ONLY );
			gridData = new GridData( GridData.FILL_HORIZONTAL );
			gridData.horizontalSpan = 3;
			cmbAggOn.setLayoutData( gridData );
			cmbAggOn.setVisibleItemCount( 30 );
			cmbAggOn.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					modifyDialogContent( );
				}
			} );
		}
	}
	
	private void createFilterCondition(Composite composite,GridData gridData)
	{
		new Label( composite, SWT.NONE ).setText( FILTER_CONDITION );
		txtFilter = new Text( composite, SWT.BORDER | SWT.MULTI );
		gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 2;
		gridData.heightHint = txtFilter.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y
				- txtFilter.getBorderWidth( )
				* 2;
		txtFilter.setLayoutData( gridData );
		txtFilter.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				modifyDialogContent( );
			}
		} );

		// createExpressionButton( composite, txtFilter );
		IExpressionProvider filterExpressionProvider;
		IExtendedDataModelUIAdapter adapter = ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( );
		if(adapter != null && adapter.getBoundExtendedData( this.bindingHolder ) != null)
		{
			filterExpressionProvider = adapter.getBindingExpressionProvider( this.bindingHolder, this.binding );
			((ExpressionProvider)filterExpressionProvider).addFilter( new ExpressionFilter( ) {

				public boolean select( Object parentElement, Object element )
				{
					if ( parentElement instanceof String )
					{
						String parent = (String) parentElement;
						if ( ExpressionFilter.CATEGORY.equals( parent ) )
						{
							if ( element instanceof String )
							{
								String elementString = (String) element;
								if ( ExpressionProvider.COLUMN_BINDINGS.equals( elementString ) )
								{
									return false;
								}
							}
						}
					}
					return true;
				}
			} );
		}
		else
		{
			filterExpressionProvider = new CrosstabAggregationExpressionProvider( this.bindingHolder,
					this.binding ) {

				protected List getChildrenList( Object parent )
				{
					List children = super.getChildrenList( parent );
					List retValue = new ArrayList( );
					retValue.addAll( children );
					if ( parent instanceof MeasureGroupHandle )
					{
						for ( int i = 0; i < children.size( ); i++ )
						{
							Object obj = children.get( i );
							if ( obj instanceof MeasureHandle
									&& ( (MeasureHandle) obj ).isCalculated( ) )
							{
								retValue.remove( obj );
							}
						}
					}

					return retValue;
				}
			};
		}
		ExpressionButtonUtil.createExpressionButton( composite,
				txtFilter,
				filterExpressionProvider,
				this.bindingHolder );
	}
	
	private void createCommonSection( Composite composite )
	{
		new Label( composite, SWT.NONE ).setText( EXPRESSION );
		txtExpression = new Text( composite, SWT.BORDER | SWT.MULTI );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 2;
		gd.heightHint = txtExpression.computeSize( SWT.DEFAULT, SWT.DEFAULT ).y
				- txtExpression.getBorderWidth( )
				* 2;
		txtExpression.setLayoutData( gd );
		createExpressionButton( composite, txtExpression );
		txtExpression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				modifyDialogContent( );
				validate( );
			}

		} );
	}

	private void createMessageSection( Composite composite )
	{
		messageLine = new CLabel( composite, SWT.LEFT );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.horizontalSpan = 4;
		messageLine.setLayoutData( layoutData );
	}

	protected void handleFunctionSelectEvent( )
	{
		Control[] children = paramsComposite.getChildren( );
		for ( int i = 0; i < children.length; i++ )
		{
			children[i].dispose( );
		}

		String displayName;
		if ( isAggregate( ) )
		{
			displayName = cmbFunction.getText( );
		}
		else
		{
			displayName = "SUM"; //$NON-NLS-1$
		}

		IAggrFunction function = getFunctionByDisplayName( displayName );
		if ( function != null )
		{
			paramsMap.clear( );
			IParameterDefn[] params = function.getParameterDefn( );
			if ( params.length > 0 )
			{
				( (GridData) paramsComposite.getLayoutData( ) ).exclude = false;
				( (GridData) paramsComposite.getLayoutData( ) ).heightHint = SWT.DEFAULT;

				int width = 0;
				if ( paramsComposite.getParent( ).getLayout( ) instanceof GridLayout )
				{
					Control[] controls = paramsComposite.getParent( )
							.getChildren( );
					for ( int i = 0; i < controls.length; i++ )
					{
						if ( controls[i] instanceof Label
								&& ( (GridData) controls[i].getLayoutData( ) ).horizontalSpan == 1 )
						{
							int labelWidth = controls[i].getBounds( ).width
									- controls[i].getBorderWidth( )
									* 2;
							if ( labelWidth > width )
								width = labelWidth;
						}
					}
				}

				for ( final IParameterDefn param : params )
				{
					Label lblParam = new Label( paramsComposite, SWT.NONE );
					lblParam.setText( param.getDisplayName( ) + ":" ); //$NON-NLS-1$
					// if ( !param.isOptional( ) )
					// lblParam.setText( "*" + lblParam.getText( ) );
					GridData gd = new GridData( );
					gd.widthHint = lblParam.computeSize( SWT.DEFAULT,
							SWT.DEFAULT ).x;
					if ( gd.widthHint < width )
						gd.widthHint = width;
					lblParam.setLayoutData( gd );

					if ( param.isDataField( ) )
					{
						final Combo cmbDataField = new Combo( paramsComposite,
								SWT.BORDER );
						cmbDataField.setLayoutData( GridDataFactory.fillDefaults( )
								.grab( true, false )
								.span( 3, 1 )
								.create( ) );
						cmbDataField.setVisibleItemCount( 30 );
						initDataFields( cmbDataField, param );

						cmbDataField.addModifyListener( new ModifyListener( ) {

							public void modifyText( ModifyEvent e )
							{
								modifyDialogContent( );
								validate( );
								paramsValueMap.put( param.getName( ),
										cmbDataField.getText( ) );
							}
						} );

						paramsMap.put( param.getName( ), cmbDataField );
					}
					else
					{
						final Text txtParam = new Text( paramsComposite,
								SWT.BORDER | SWT.MULTI );
						txtParam.addModifyListener( new ModifyListener( ) {

							public void modifyText( ModifyEvent e )
							{
								modifyDialogContent( );
								validate( );
								paramsValueMap.put( param.getName( ),
										txtParam.getText( ) );
							}
						} );
						GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
						gridData.horizontalIndent = 0;
						gridData.horizontalSpan = 2;
						gridData.heightHint = txtParam.computeSize( SWT.DEFAULT,
								SWT.DEFAULT ).y
								- txtParam.getBorderWidth( )
								* 2;
						txtParam.setLayoutData( gridData );
						initTextField( txtParam, param );
						createExpressionButton( paramsComposite, txtParam );
						paramsMap.put( param.getName( ), txtParam );
					}
				}
			}
			else
			{
				( (GridData) paramsComposite.getLayoutData( ) ).heightHint = 0;
				// ( (GridData) paramsComposite.getLayoutData( ) ).exclude =
				// true;
			}

			// this.cmbDataField.setEnabled( function.needDataField( ) );
			try
			{
				cmbType.setText( getDataTypeDisplayName( DataAdapterUtil.adapterToModelDataType( DataUtil.getAggregationManager( )
						.getAggregation( function.getName( ) )
						.getDataType( ) ) ) );
			}
			catch ( BirtException e )
			{
				ExceptionUtil.handle( e );
			}
		}
		else
		{
			( (GridData) paramsComposite.getLayoutData( ) ).heightHint = 0;
			( (GridData) paramsComposite.getLayoutData( ) ).exclude = true;
			// new Label( argsComposite, SWT.NONE ).setText( "no args" );
		}
		composite.layout( true, true );
		setContentSize( composite );
	}

	private void createExpressionButton( final Composite parent,
			final Control control )
	{
		if ( expressionProvider == null )
		{
			if ( isAggregate( ) )
				expressionProvider = new CrosstabAggregationExpressionProvider( this.bindingHolder,
						this.binding );
			else if(ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( ) != null
					&& ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( ).getBoundExtendedData( this.bindingHolder ) != null)
			{
				expressionProvider = ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( )
						.getBindingExpressionProvider( this.bindingHolder, this.binding );
			}
			else
				expressionProvider = new CrosstabBindingExpressionProvider( this.bindingHolder,
						this.binding );
		}
		ExpressionButtonUtil.createExpressionButton( parent,
				control,
				expressionProvider,
				this.bindingHolder );
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
			// bugzilla 273368
			// if expression is "measure['...']", aggregation do not support
			// IAggrFunction.RUNNING_AGGR function
			if ( isAggregate( ) )
			{
				IAggrFunction function = getFunctionByDisplayName( cmbFunction.getText( ) );
				IParameterDefn[] params = function.getParameterDefn( );
				if ( params.length > 0 )
				{
					for ( final IParameterDefn param : params )
					{

						if ( param.isDataField( ) )
						{
							Combo cmbDataField = (Combo) paramsMap.get( param.getName( ) );
							String expression = cmbDataField.getText( );
							DataRequestSession session = null;
							try
							{
								session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
								if ( session.getCubeQueryUtil( )
										.getReferencedMeasureName( expression ) != null
										&& function.getType( ) == IAggrFunction.RUNNING_AGGR )
								{
									dialog.setCanFinish( false );
									this.messageLine.setText( Messages.getFormattedString( "BindingDialogHelper.error.improperexpression", //$NON-NLS-1$
											new Object[]{
												function.getName( )
											} ) );
									this.messageLine.setImage( PlatformUI.getWorkbench( )
											.getSharedImages( )
											.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
									return;
								}

								dialog.setCanFinish( true );
							}
							catch ( Exception e )
							{

							}
							finally
							{
								if ( session != null )
								{
									session.shutdown( );
								}
							}
						}
					}
				}
			}

			dialogCanFinish( );
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
			if ( isAggregate( ) )
			{
				try
				{
					IAggrFunction aggregation = DataUtil.getAggregationManager( )
							.getAggregation( getFunctionByDisplayName( cmbFunction.getText( ) ).getName( ) );

					if ( aggregation.getParameterDefn( ).length > 0 )
					{
						IParameterDefn[] parameters = aggregation.getParameterDefn( );
						for ( IParameterDefn param : parameters )
						{
							if ( !param.isOptional( ) )
							{
								String paramValue = getControlValue( paramsMap.get( param.getName( ) ) );
								if ( paramValue == null
										|| paramValue.trim( ).equals( "" ) ) //$NON-NLS-1$
								{
									dialog.setCanFinish( false );
									return;
								}
							}
						}
					}
				}
				catch ( BirtException e )
				{
					// TODO show error message in message panel
				}
			}

			if ( isTimePeriod( ) )
			{
				ITimeFunction timeFunction = getTimeFunctionByIndex( calculationType.getSelectionIndex( ) );
				if ( timeFunction != null )
				{

					List<IArgumentInfo> infos = timeFunction.getArguments( );

					for ( int i = 0; i < infos.size( ); i++ )
					{
						String paramValue = getControlValue( calculationParamsMap.get( infos.get( i )
								.getName( ) ) );
						if ( paramValue == null
								|| paramValue.trim( ).equals( "" ) && !infos.get( i ).isOptional( ) ) //$NON-NLS-1$
						{
							dialog.setCanFinish( false );
							return;
						}

					}
					String dimensionName = getTimeDimsionName( );
					if ( !isUseDimension( dimensionName )
							&& recentButton.getSelection( ) )
					{
						this.messageLine.setText( Messages.getString( "CrosstabBindingDialogHelper.timeperiod.wrongdate" ) ); //$NON-NLS-1$
						this.messageLine.setImage( PlatformUI.getWorkbench( )
								.getSharedImages( )
								.getImage( ISharedImages.IMG_OBJS_ERROR_TSK ) );
						dialog.setCanFinish( false );
						return;
					}
					if ( dateSelectionButton.getSelection( )
							&& ( dateText.getText( ) == null || dateText.getText( )
									.trim( )
									.equals( "" ) ) ) //$NON-NLS-1$

					{
						dialog.setCanFinish( false );
						return;
					}
				}
				else
				{
					dialog.setCanFinish( false );
					return;
				}
			}
			dialogCanFinish( );
		}
		updateRemoveBtnState( );
	}

	private void dialogCanFinish( )
	{
		if ( !hasModified && isEditModal( ) )
			dialog.setCanFinish( false );
		else
			dialog.setCanFinish( true );
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
			if ( !strEquals( binding.getDisplayNameID( ),
					txtDisplayNameID.getText( ) ) )
				return true;
			if ( !strEquals( binding.getDataType( ), getDataType( ) ) )
				return true;
			try
			{
				if ( !strEquals( DataAdapterUtil.adaptModelAggregationType( binding.getAggregateFunction( ) ),
						getFunctionByDisplayName( cmbFunction.getText( ) ).getName( ) ) )
					return true;
			}
			catch ( AdapterException e )
			{
			}
			if (!exprEquals( (Expression) binding.getExpressionProperty( ComputedColumn.FILTER_MEMBER )
					.getValue( ),
					ExpressionButtonUtil.getExpression( txtFilter ) ) )
				return true;
			if ( /* !isTimePeriod( ) && */!strEquals( cmbAggOn.getText( ),
					DEUtil.getAggregateOn( binding ) ) )
				return true;

			IAggrFunction function = getFunctionByDisplayName( cmbFunction.getText( ) );
			if ( function != null )
			{
				IParameterDefn[] params = function.getParameterDefn( );
				for ( final IParameterDefn param : params )
				{
					if ( paramsMap.containsKey( param.getName( ) ) )
					{
						Expression paramValue = ExpressionButtonUtil.getExpression( paramsMap.get( param.getName( ) ) );
						for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
						{
							AggregationArgumentHandle handle = (AggregationArgumentHandle) iterator.next( );
							if ( param.getName( ).equals( handle.getName( ) )
									&& !exprEquals( (Expression) handle.getExpressionProperty( AggregationArgument.VALUE_MEMBER )
											.getValue( ),
											paramValue ) )
							{
								return true;
							}
						}
						if ( param.isDataField( )
								&& binding.getExpression( ) != null
								&& !exprEquals( (Expression) binding.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER )
										.getValue( ),
										paramValue ) )
						{
							return true;
						}
					}
				}
			}
		}
		else if ( isCommonBinding( ) )
		{
			if ( !strEquals( txtName.getText( ), binding.getName( ) ) )
				return true;
			if ( !strEquals( txtDisplayName.getText( ),
					binding.getDisplayName( ) ) )
				return true;
			if ( !strEquals( txtDisplayNameID.getText( ),
					binding.getDisplayNameID( ) ) )
				return true;
			if ( !strEquals( getDataType( ), binding.getDataType( ) ) )
				return true;
			if ( !exprEquals( ExpressionButtonUtil.getExpression( txtExpression ),
					(Expression) binding.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER )
							.getValue( ) ) )
				return true;
		}
		else
		{
			if ( !strEquals( txtName.getText( ), binding.getName( ) ) )
				return true;
			if ( !strEquals( txtDisplayName.getText( ),
					binding.getDisplayName( ) ) )
				return true;
			if ( !strEquals( txtDisplayNameID.getText( ),
					binding.getDisplayNameID( ) ) )
				return true;
			if ( !strEquals( getDataType( ), binding.getDataType( ) ) )
				return true;
			if ( /* !isTimePeriod( ) && */!strEquals( cmbAggOn.getText( ),
					DEUtil.getAggregateOn( binding ) ) )
				return true;
			IAggrFunction function = getFunctionByDisplayName( "SUM" ); //$NON-NLS-1$
			if ( function != null )
			{
				IParameterDefn[] params = function.getParameterDefn( );
				for ( final IParameterDefn param : params )
				{
					if ( paramsMap.containsKey( param.getName( ) ) )
					{
						Expression paramValue = ExpressionButtonUtil.getExpression( paramsMap.get( param.getName( ) ) );
						for ( Iterator iterator = binding.argumentsIterator( ); iterator.hasNext( ); )
						{
							AggregationArgumentHandle handle = (AggregationArgumentHandle) iterator.next( );
							if ( param.getName( ).equals( handle.getName( ) )
									&& !exprEquals( (Expression) handle.getExpressionProperty( AggregationArgument.VALUE_MEMBER )
											.getValue( ),
											paramValue ) )
							{
								return true;
							}
						}
						if ( param.isDataField( )
								&& binding.getExpression( ) != null
								&& !exprEquals( (Expression) binding.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER )
										.getValue( ),
										paramValue ) )
						{
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	private boolean exprEquals( Expression left, Expression right )
	{
		if ( left == null && right == null )
		{
			return true;
		}
		else if ( left == null && right != null )
		{
			return right.getExpression( ) == null;
		}
		else if ( left != null && right == null )
		{
			return left.getExpression( ) == null;
		}
		else if ( left.getStringExpression( ) == null
				&& right.getStringExpression( ) == null )
			return true;
		else if ( strEquals( left.getStringExpression( ),
				right.getStringExpression( ) )
				&& strEquals( left.getType( ), right.getType( ) ) )
			return true;
		return false;
	}

	private String getControlValue( Control control )
	{
		if ( control instanceof Text )
		{
			return ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			return ( (Combo) control ).getText( );
		}
		return null;
	}

	private boolean strEquals( String left, String right )
	{
		if ( left == right )
			return true;
		if ( left == null )
			return "".equals( right ); //$NON-NLS-1$
		if ( right == null )
			return "".equals( left ); //$NON-NLS-1$
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
		return ""; //$NON-NLS-1$
	}

	public ComputedColumnHandle editBinding( ComputedColumnHandle binding )
			throws SemanticException
	{
		if ( isAggregate( ) )
		{
			binding.setDisplayName( txtDisplayName.getText( ) );
			binding.setDisplayNameID( txtDisplayNameID.getText( ) );
			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}

			binding.setAggregateFunction( getFunctionByDisplayName( cmbFunction.getText( ) ).getName( ) );
			ExpressionButtonUtil.saveExpressionButtonControl( txtFilter,
					binding,
					ComputedColumn.FILTER_MEMBER );

			binding.clearAggregateOnList( );
			// if (!isTimePeriod( ))
			{
				String aggStr = cmbAggOn.getText( );
				StringTokenizer token = new StringTokenizer( aggStr, "," ); //$NON-NLS-1$

				while ( token.hasMoreTokens( ) )
				{
					String agg = token.nextToken( );
					if ( !agg.equals( ALL ) )
						binding.addAggregateOn( agg );
				}
			}

			// remove expression created in old version.
			binding.setExpression( null );
			binding.clearArgumentList( );

			for ( Iterator iterator = paramsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				String value = getControlValue( paramsMap.get( arg ) );
				if ( value != null )
				{
					AggregationArgument argHandle = StructureFactory.createAggregationArgument( );
					argHandle.setName( arg );
					if ( ExpressionButtonUtil.getExpressionButton( paramsMap.get( arg ) ) != null )
					{
						ExpressionButtonUtil.saveExpressionButtonControl( paramsMap.get( arg ),
								argHandle,
								AggregationArgument.VALUE_MEMBER );
					}
					else
					{
						Expression expression = new Expression( value,
								ExpressionType.JAVASCRIPT );
						argHandle.setExpressionProperty( AggregationArgument.VALUE_MEMBER,
								expression );
					}
					binding.addArgument( argHandle );
				}
			}
		}
		else if ( isCommonBinding( ) )
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
			binding.setDisplayNameID( txtDisplayNameID.getText( ) );
			if ( ExpressionButtonUtil.getExpressionButton( txtExpression ) != null )
			{
				ExpressionButtonUtil.saveExpressionButtonControl( txtExpression,
						binding,
						ComputedColumn.EXPRESSION_MEMBER );
			}
			else
			{
				Expression expression = new Expression( getControlValue( txtExpression ),
						ExpressionType.JAVASCRIPT );
				binding.setExpressionProperty( AggregationArgument.VALUE_MEMBER,
						expression );
			}
		}
		else
		{
			binding.setDisplayName( txtDisplayName.getText( ) );
			binding.setDisplayNameID( txtDisplayNameID.getText( ) );
			for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
			{
				if ( DATA_TYPE_CHOICES[i].getDisplayName( )
						.equals( cmbType.getText( ) ) )
				{
					binding.setDataType( DATA_TYPE_CHOICES[i].getName( ) );
					break;
				}
			}

			binding.clearAggregateOnList( );
			// if (!isTimePeriod( ))
			{
				String aggStr = cmbAggOn.getText( );
				StringTokenizer token = new StringTokenizer( aggStr, "," ); //$NON-NLS-1$

				while ( token.hasMoreTokens( ) )
				{
					String agg = token.nextToken( );
					if ( !agg.equals( ALL ) )
						binding.addAggregateOn( agg );
				}
			}

			// remove expression created in old version.
			binding.setExpression( null );
			binding.clearArgumentList( );

			for ( Iterator iterator = paramsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				String arg = (String) iterator.next( );
				String value = getControlValue( paramsMap.get( arg ) );
				if ( value != null )
				{
					Expression expression = new Expression( value,
							ExpressionType.JAVASCRIPT );
					binding.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
							expression );
				}
			}
		}

		if ( isTimePeriod( ) )
		{
			ITimeFunction timeFunction = getTimeFunctionByIndex( calculationType.getSelectionIndex( ) );

			String dimensionName = timeDimension.getText( );
			// Expression dimensionExpression = new Expression( dimensionName,
			// ExpressionType.JAVASCRIPT );
			//
			// binding.setExpressionProperty(
			// ComputedColumn.TIME_DIMENSION_MEMBER,
			// dimensionExpression );
			binding.setTimeDimension( dimensionName );

			binding.setCalculationType( timeFunction.getName( ) );
			binding.setProperty( ComputedColumn.CALCULATION_ARGUMENTS_MEMBER,
					null );
			// save the args
			for ( Iterator iterator = calculationParamsMap.keySet( ).iterator( ); iterator.hasNext( ); )
			{
				CalculationArgument argument = StructureFactory.createCalculationArgument( );

				String arg = (String) iterator.next( );
				argument.setName( arg );
				String value = getControlValue( calculationParamsMap.get( arg ) );
				if ( value != null )
				{
					if ( ExpressionButtonUtil.getExpressionButton( calculationParamsMap.get( arg ) ) != null )
					{
						Expression expr = getExpressionByControl( calculationParamsMap.get( arg ) );
						argument.setValue( expr );
					}
					else
					{
						Expression expr = new Expression( value,
								ExpressionType.JAVASCRIPT );
						argument.setValue( expr );
					}

					binding.addCalculationArgument( argument );
				}
			}

			// add refred day

			if ( todayButton.getSelection( ) )
			{
				binding.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_TODAY );
			}
			else if ( dateSelectionButton.getSelection( ) )
			{
				binding.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_FIXED_DATE );
				ExpressionButtonUtil.saveExpressionButtonControl( dateText,
						binding,
						ComputedColumn.REFERENCE_DATE_VALUE_MEMBER );

			}
			else if ( recentButton.getSelection( ) )
			{
				binding.setReferenceDateType( DesignChoiceConstants.REFERENCE_DATE_TYPE_ENDING_DATE_IN_DIMENSION );
			}
		}
		return binding;
	}

	public static Expression getExpressionByControl( Control control )
			throws SemanticException
	{
		ExpressionButton button = getExpressionButton( control );
		if ( button != null && button.getExpressionHelper( ) != null )
		{
			Expression expression = new Expression( button.getExpressionHelper( )
					.getExpression( ),
					button.getExpressionHelper( ).getExpressionType( ) );

			return expression;
		}
		return null;
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

	public void setContainer( Object container )
	{
		this.container = container;
	}

	public boolean canProcessAggregation( )
	{
		return true;
	}

	private URL[] getAvailableResourceUrls( )
	{
		List<URL> urls = new ArrayList<URL>( );
		String[] baseNames = getBaseNames( );
		if ( baseNames == null )
			return urls.toArray( new URL[0] );
		else
		{
			for ( int i = 0; i < baseNames.length; i++ )
			{
				URL url = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findResource( baseNames[i],
								IResourceLocator.MESSAGE_FILE );
				if ( url != null )
					urls.add( url );
			}
			return urls.toArray( new URL[0] );
		}
	}

	private String[] getBaseNames( )
	{
		List<String> resources = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getIncludeResources( );
		if ( resources == null )
			return null;
		else
			return resources.toArray( new String[0] );
	}

	private URL[] getResourceURLs( )
	{
		String[] baseNames = getBaseNames( );
		if ( baseNames == null )
			return null;
		else
		{
			URL[] urls = new URL[baseNames.length];
			for ( int i = 0; i < baseNames.length; i++ )
			{
				urls[i] = SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.findResource( baseNames[i],
								IResourceLocator.MESSAGE_FILE );
			}
			return urls;
		}
	}

	private void updateRemoveBtnState( )
	{
		btnRemoveDisplayNameID.setEnabled( txtDisplayNameID.getText( )
				.equals( EMPTY_STRING ) ? false : true );
	}

	private boolean isEditModal = false;

	public void setEditModal( boolean isEditModal )
	{
		this.isEditModal = isEditModal;
	}

	public boolean isEditModal( )
	{
		return isEditModal;
	}

	private void modifyDialogContent( )
	{
		if ( hasInitDialog && isEditModal( ) && hasModified == false )
		{
			hasModified = true;
			validate( );
		}
	}

	public boolean isFullAggregate( )
	{
		return super.isAggregate( );
	}

	public void setFullAggregate( boolean isAggregate )
	{
		super.setAggregate( isAggregate );
	}

	public boolean isCommonBinding( )
	{
		return commonBinding;
	}

	public void setCommonBinding( boolean common )
	{
		this.commonBinding = common;
	}

	private boolean hasModified = false;

	@Override
	public void setBinding(ComputedColumnHandle binding)
	{
		super.setBinding(binding);
		if ( this.binding == null )
		{
			this.setCommonBinding( !this.isFullAggregate() );
		}
		else if ( this.binding.getAggregateOn( ) != null
				&& !this.binding.getAggregateOn( ).trim( ).equals( "" ) ) //$NON-NLS-1$	}
		{
			this.setCommonBinding( false );
		}
		else
		{
			this.setCommonBinding( !this.isFullAggregate() );
		}
	}
}
