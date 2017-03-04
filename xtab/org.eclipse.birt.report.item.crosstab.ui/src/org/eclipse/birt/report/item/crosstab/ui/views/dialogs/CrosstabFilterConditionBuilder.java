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

package org.eclipse.birt.report.item.crosstab.ui.views.dialogs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo;
import org.eclipse.birt.report.data.adapter.api.IDimensionLevel;
import org.eclipse.birt.report.designer.data.ui.util.CubeValueSelector;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.designer.internal.ui.dialogs.expression.ExpressionEditor;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.extension.ExtendedDataModelUIAdapterHelper;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ValueCombo;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionButtonUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.TabularDimensionNodeProvider;
import org.eclipse.birt.report.designer.ui.cubebuilder.provider.TabularLevelNodeProvider;
import org.eclipse.birt.report.designer.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.util.ExceptionUtil;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.dialogs.CrosstabFilterExpressionProvider;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabAdaptUtil;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.plugin.CrosstabPlugin;
import org.eclipse.birt.report.item.crosstab.ui.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.ui.views.attributes.widget.ExpressionValueCellEditor;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.layout.GridLayoutFactory;
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
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

/**
 * CrosstabFilterConditionBuilder
 */
public class CrosstabFilterConditionBuilder extends BaseTitleAreaDialog
{

	private static final String DECORATOR_STRING = Messages.getString( "CrosstabFilterConditionBuilder.Item.Decorator" ); //$NON-NLS-1$
	protected Composite parentComposite = null;
	protected List groupLevelList;
	protected List groupLevelNameList;
	protected List measureList;
	protected List measureNameList;
	protected List cubeLevelNameList;
	protected FilterConditionElementHandle filterConditionElement;
	protected LevelViewHandle levelViewHandle;
	protected MeasureViewHandle measureViewHandle;
	protected CrosstabReportItemHandle crosstabHandle;
	protected Group group;
	protected Table memberValueTable;
	protected TableViewer dynamicViewer;
	protected Button groupBtn, measureBtn;
	protected Composite memberValueGroup;
	protected CCombo expressionCCombo;

	protected String[] columns = new String[]{
			" ", //$NON-NLS-1$
			Messages.getString( "SelColumnMemberValue.Column.Level" ), //$NON-NLS-1$
			Messages.getString( "SelColumnMemberValue.Column.Value" ) //$NON-NLS-1$
	};

	protected MemberValueHandle memberValueHandle;
	protected List referencedLevelList;

	protected final static String CHOICE_SELECT_VALUE = Messages.getString( "ExpressionValueCellEditor.selectValueAction" ); //$NON-NLS-1$

	public static final String DLG_MESSAGE_EDIT = org.eclipse.birt.report.designer.nls.Messages.getString( "FilterConditionBuilder.DialogMessage.Edit" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_NEW = org.eclipse.birt.report.designer.nls.Messages.getString( "FilterConditionBuilder.DialogMessage.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = org.eclipse.birt.report.designer.nls.Messages.getString( "FilterConditionBuilder.DialogTitle.Edit" ); //$NON-NLS-1$
	public static final String DLG_TITLE_NEW = org.eclipse.birt.report.designer.nls.Messages.getString( "FilterConditionBuilder.DialogTitle.New" ); //$NON-NLS-1$

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	protected static final String[] EMPTY_ARRAY = new String[]{};

	protected static Logger logger = Logger.getLogger( CrosstabFilterConditionBuilder.class.getName( ) );
	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;
	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER, true );
		IChoice[] chs = chset.getChoices( new AlphabeticallyComparator( ) );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
	}

	/**
	 * Returns how many value fields this operator needs.
	 * 
	 * @param operatorValue
	 */
	public static int determineValueVisible( String operatorValue )
	{
		if ( DesignChoiceConstants.FILTER_OPERATOR_ANY.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_FALSE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_TRUE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NULL.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL.equals( operatorValue ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_LT.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_LE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_EQ.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_GE.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_GT.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_LIKE.equals( operatorValue ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN.equals( operatorValue ) )
		{
			return 2;
		}
		else if ( DesignChoiceConstants.FILTER_OPERATOR_IN.equals( operatorValue )
				|| DesignChoiceConstants.FILTER_OPERATOR_NOT_IN.equals( operatorValue ) )
		{
			return 3;
		}

		return 1;
	}

	/**
	 * Returns the index for given operator value in the operator list.
	 * 
	 * @param value
	 */
	protected static int getIndexForOperatorValue( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return i;
			}
		}

		return 0;
	}

	/**
	 * Returns the operator display name by its value.
	 * 
	 * @param value
	 */
	public static String getNameForOperator( String value )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][1].equals( value ) )
			{
				return OPERATOR[i][0];
			}
		}

		return ""; //$NON-NLS-1$
	}

	/**
	 * Returns the operator value by its display name.
	 * 
	 * @param name
	 */
	public static String getValueForOperator( String name )
	{
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			if ( OPERATOR[i][0].equals( name ) )
			{
				return OPERATOR[i][1];
			}
		}

		return null;
	}

	protected Button addBtn, editBtn, delBtn, delAllBtn;

	protected CCombo addExpressionValue;

	protected Label andLable;

	protected IChoiceSet choiceSet;

	protected List columnList;
	protected transient ReportElementHandle currentItem = null;

	protected DesignElementHandle designHandle;

	protected Composite dummy1, dummy2;

	protected CCombo operator;

	protected IExpressionProvider expressionProvider;

	protected CCombo expressionValue1, expressionValue2;

	protected Label label1, label2;

	protected final String NULL_STRING = null;

	protected SelectionListener operatorSelection = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			operatorChange( );
			getShell( ).pack( );
		}
	};

	protected List selValueList = new ArrayList( );

	protected Table table;

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement == null )
			{
				return new Object[0];
			}
			else if ( inputElement instanceof List )
			{
				return ( (List) inputElement ).toArray( );
			}
			return null;
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}
	};

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider( ) {

		public void addListener( ILabelProviderListener listener )
		{
		}

		public void dispose( )
		{
		}

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( columnIndex == 0 )
			{
				if ( element instanceof Expression )
				{
					return ( (Expression) element ).getStringExpression( );
				}
				return element.toString( );
			}
			return ""; //$NON-NLS-1$
		}

		public boolean isLabelProperty( Object element, String property )
		{
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
		}
	};

	protected TableViewer tableViewer;

	protected String title, message;

	protected List valueList = new ArrayList( );

	protected Composite valueListComposite;

	protected int valueVisible;

	public void setInput( FilterConditionElementHandle input, Object target )
	{
		this.filterConditionElement = input;

		if ( target instanceof MeasureViewHandle )
		{
			this.measureViewHandle = (MeasureViewHandle) target;
		}
		else if ( target instanceof LevelViewHandle )
		{
			this.levelViewHandle = (LevelViewHandle) target;
		}
		else
		{
			this.crosstabHandle = (CrosstabReportItemHandle) target;
		}

	}

	protected List valueListConList = new ArrayList( );
	protected static final String CHOICE_FILTER = Messages.getString( "ExpressionValueCellEditor.filterBy" ); //$NON-NLS-1$

	// private Button cubeBtn;

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		if ( editor != null )
		{
			editor.setExpressionProvider( new CrosstabFilterExpressionProvider( handle ) );
		}
	}

	private int createValueListComposite( Composite parent )
	{

		if ( addExpressionValue != null && !addExpressionValue.isDisposed( ) )
		{
			return 0;
		}

		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			ExpressionButtonUtil.getExpressionButton( expressionValue1 )
					.getControl( )
					.dispose( );
			expressionValue1.dispose( );
			expressionValue1 = null;

			ExpressionButtonUtil.getExpressionButton( expressionValue2 )
					.getControl( )
					.dispose( );
			expressionValue2.dispose( );
			expressionValue2 = null;

			andLable.dispose( );
			andLable = null;
		}

		valueListConList.clear( );

		addExpressionValue = createMultiExpressionValue( parent );
		addExpressionValue.setLayoutData( GridDataFactory.swtDefaults( )
				.hint( 150, SWT.DEFAULT )
				.create( ) );

		valueListConList.add( addExpressionValue );
		valueListConList.add( ExpressionButtonUtil.getExpressionButton( addExpressionValue )
				.getControl( ) );

		addBtn = new Button( parent, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) ); //$NON-NLS-1$
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) ); //$NON-NLS-1$
		setButtonCGridLayoutData( addBtn );
		valueListConList.add( addBtn );
		addBtn.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				Expression value = ExpressionButtonUtil.getExpression( addExpressionValue );
				if ( valueList.indexOf( value ) < 0 )
				{
					valueList.add( value );
					tableViewer.refresh( );
					updateButtons( );
					addExpressionValue.setFocus( );
					addExpressionValue.setText( "" ); //$NON-NLS-1$
				}
				else
				{
					addBtn.setEnabled( false );
				}
			}
		} );
		/*
		 * Label dummy = new Label( parent, SWT.NONE ); valueListConList.add(
		 * dummy );
		 * 
		 * Label dummy2 = new Label( parent, SWT.NONE ); valueListConList.add(
		 * dummy2 );
		 */
		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		table = new Table( parent, tableStyle );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.heightHint = 100;
		data.horizontalSpan = 3;
		table.setLayoutData( data );
		valueListConList.add( table );
		table.setHeaderVisible( true );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		String[] columNames = new String[]{
			Messages.getString( "FilterConditionBuilder.list.item1" ), //$NON-NLS-1$
		};
		int[] columLength = new int[]{
			268
		};
		for ( i = 0; i < columNames.length; i++ )
		{
			column = new TableColumn( table, SWT.NONE, i );
			column.setText( columNames[i] );
			column.setWidth( columLength[i] );
		}
		table.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				checkEditDelButtonStatus( );
			}
		} );

		table.addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					delTableValue( );
				}

			}

			public void keyReleased( KeyEvent e )
			{
			}

		} );
		table.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				editTableValue( );
			}
		} );

		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( columNames );
		tableViewer.setLabelProvider( tableLableProvier );
		tableViewer.setContentProvider( tableContentProvider );

		Composite rightPart = new Composite( parent, SWT.NONE );
		rightPart.setLayoutData( GridDataFactory.swtDefaults( )
				.grab( true, true )
				.create( ) );
		GridLayout layout = new GridLayout( );
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout( layout );

		valueListConList.add( rightPart );

		editBtn = new Button( rightPart, SWT.PUSH );
		editBtn.setText( Messages.getString( "FilterConditionBuilder.button.edit" ) ); //$NON-NLS-1$
		editBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.edit.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( editBtn );
		editBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				editTableValue( );
			}

		} );

		delBtn = new Button( rightPart, SWT.PUSH );
		delBtn.setText( Messages.getString( "FilterConditionBuilder.button.delete" ) ); //$NON-NLS-1$
		delBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.delete.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delBtn );
		delBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				delTableValue( );
			}

		} );

		delAllBtn = new Button( rightPart, SWT.PUSH );
		delAllBtn.setText( Messages.getString( "FilterConditionBuilder.button.deleteall" ) ); //$NON-NLS-1$
		delAllBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.deleteall.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delAllBtn );
		delAllBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				int count = valueList.size( );
				if ( count > 0 )
				{
					valueList.clear( );
					tableViewer.refresh( );
					updateButtons( );
				}
				else
				{
					delAllBtn.setEnabled( false );
				}
			}

		} );
		parentComposite.layout( true, true );

		if ( getButtonBar( ) != null )
			parent.getShell( ).pack( );

		return 1;
	}

	private int create2ValueComposite( Composite condition )
	{
		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			return 0;
		}

		// if ( valueListComposite != null && !valueListComposite.isDisposed( )
		// )
		// {
		// valueListComposite.dispose( );
		// valueListComposite = null;
		// }

		if ( valueListConList.size( ) > 0 )
		{
			int count = valueListConList.size( );
			for ( int i = 0; i < count; i++ )
			{
				Object obj = valueListConList.get( i );
				if ( obj != null
						&& obj instanceof Widget
						&& ( !( (Widget) obj ).isDisposed( ) ) )
				{
					( (Widget) obj ).dispose( );
				}
			}
		}
		valueListConList.clear( );

		expressionValue1 = createExpressionValue( condition );
		expressionValue1.setLayoutData( GridDataFactory.swtDefaults( )
				.hint( 150, SWT.DEFAULT )
				.create( ) );
		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		// andLable.setVisible( false );
		andLable.setEnabled( false );
		andLable.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 2, 1 )
				.create( ) );
		// dummy2 = createDummy( condition, 3 );

		expressionValue2 = createExpressionValue( condition );
		expressionValue2.setLayoutData( GridDataFactory.swtDefaults( )
				.hint( 150, SWT.DEFAULT )
				.create( ) );

		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( getIndexForOperatorValue( "eq" ) ); //$NON-NLS-1$
			operatorChange( );
		}

		parentComposite.layout( true, true );

		if ( getButtonBar( ) != null )
			condition.getShell( ).pack( );
		return 1;
	}

	protected void operatorChange( )
	{
		String value = getValueForOperator( operator.getText( ) );

		valueVisible = determineValueVisible( value );

		if ( valueVisible == 3 )
		{
			int ret = createValueListComposite( valuesComposite );
			if ( ret != 0 )
			{
				valueList = new ArrayList( );
				if ( filterConditionElement != null )
				{
					if ( filterConditionElement.getValue1ExpressionList( )
							.getListValue( ) != null
							&& filterConditionElement.getValue1ExpressionList( )
									.getListValue( )
									.size( ) > 0 )
						valueList.addAll( filterConditionElement.getValue1ExpressionList( )
								.getListValue( ) );
				}

				tableViewer.setInput( valueList );
				WidgetUtil.setExcludeGridData( tableViewer.getControl( ), false );
			}

		}
		else
		{
			int ret = create2ValueComposite( valuesComposite );
			if ( ret != 0 && filterConditionElement != null )
			{
				expressionValue1.setText( DEUtil.resolveNull( filterConditionElement.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( filterConditionElement.getValue2( ) ) );
			}
			if ( tableViewer != null && !tableViewer.getControl( ).isDisposed( ) )
				WidgetUtil.setExcludeGridData( tableViewer.getControl( ), true );
		}

		if ( valueVisible == 0 )
		{
			WidgetUtil.setExcludeGridData( expressionValue1, true );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue1 )
					.getControl( ),
					true );
			WidgetUtil.setExcludeGridData( expressionValue2, true );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue2 )
					.getControl( ),
					true );
			WidgetUtil.setExcludeGridData( andLable, true );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 1 )
		{
			WidgetUtil.setExcludeGridData( expressionValue1, false );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue1 )
					.getControl( ),
					false );
			WidgetUtil.setExcludeGridData( expressionValue2, true );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue2 )
					.getControl( ),
					true );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 2 )
		{
			WidgetUtil.setExcludeGridData( expressionValue1, false );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue1 )
					.getControl( ),
					false );
			WidgetUtil.setExcludeGridData( expressionValue2, false );
			WidgetUtil.setExcludeGridData( ExpressionButtonUtil.getExpressionButton( expressionValue2 )
					.getControl( ),
					false );
			WidgetUtil.setExcludeGridData( andLable, false );
			andLable.setVisible( true );
			andLable.setEnabled( true );
		}
		WidgetUtil.setExcludeGridData( valuesComposite, valueVisible == 0 );
		WidgetUtil.setExcludeGridData( valuesLabel, valueVisible == 0 );
		updateButtons( );
		valuesComposite.getParent( ).layout( );
	}

	/**
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( String title, String message )
	{
		this( UIUtil.getDefaultShell( ), title, message );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public CrosstabFilterConditionBuilder( Shell parentShell, String title,
			String message )
	{
		super( parentShell );
		this.title = title;
		this.message = message;
	}

	private void initializeDialog( )
	{
		getLevels( );
		getMeasures( );
		getCubeLevelNames( );
		groupBtn.setSelection( true );
		targetSelectionChanged( false );

	}

	protected Control createDialogArea( Composite parent )
	{
		UIUtil.bindHelp( parent,
				IHelpContextIds.INSERT_EDIT_FILTER_CONDITION_DIALOG_ID );

		Composite area = (Composite) super.createDialogArea( parent );
		Composite contents = new Composite( area, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contents.setLayout( new GridLayout( ) );

		this.setTitle( title );
		this.setMessage( message );
		getShell( ).setText( title );

		applyDialogFont( contents );
		initializeDialogUnits( area );

		createFilterConditionContent( contents );

		return area;
	}

	protected void createFilterConditionContent( Composite innerParent )
	{
		UIUtil.bindHelp( innerParent,
				IHelpContextIds.XTAB_FILTER_CONDITION_BUILDER );

		parentComposite = innerParent;

		Composite parentControl = new Composite( innerParent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 480;
		parentControl.setLayoutData( gd );
		parentControl.setLayout( new GridLayout( 4, false ) );

		Composite groupContainer = new Composite( parentControl, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 4;
		groupContainer.setLayoutData( gd );
		groupContainer.setLayout( new GridLayout( 4, false ) );

		Label label = new Label( groupContainer, SWT.NONE );
		label.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.Target" ) ); //$NON-NLS-1$
		gd = new GridData( );
		gd.widthHint = label.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x < 60 ? 60
				: label.computeSize( SWT.DEFAULT, SWT.DEFAULT ).x;
		label.setLayoutData( gd );

		groupBtn = new Button( groupContainer, SWT.RADIO );
		groupBtn.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.GroupLevel" ) ); //$NON-NLS-1$
		groupBtn.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 1, 1 )
				.create( ) );

		groupGroupLevel = new CCombo( groupContainer, SWT.READ_ONLY
				| SWT.BORDER );
		groupGroupLevel.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 2, 1 )
				.hint( 200, SWT.DEFAULT )
				.create( ) );
		groupGroupLevel.setVisibleItemCount( 30 );
		groupGroupLevel.addListener( SWT.Modify, groupLeveModify );

		new Label( groupContainer, SWT.NONE );
		measureBtn = new Button( groupContainer, SWT.RADIO );
		measureBtn.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.Measure" ) ); //$NON-NLS-1$
		measureBtn.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 1, 1 )
				.create( ) );

		measureGroupLevel = new CCombo( groupContainer, SWT.READ_ONLY
				| SWT.BORDER );
		measureGroupLevel.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 2, 1 )
				.hint( 200, SWT.DEFAULT )
				.create( ) );
		measureGroupLevel.setVisibleItemCount( 30 );
		measureGroupLevel.addListener( SWT.Modify, groupLeveModify );

		new Label( groupContainer, SWT.NONE );
		detailBtn = new Button( groupContainer, SWT.RADIO );
		detailBtn.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.Detal" ) ); //$NON-NLS-1$
		detailBtn.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 3, 1 )
				.create( ) );

		groupBtn.addListener( SWT.Selection, targetSelectionListener );
		measureBtn.addListener( SWT.Selection, targetSelectionListener );
		detailBtn.addListener( SWT.Selection, targetSelectionListener );
		// cubeBtn.addListener( SWT.Selection, targetSelectionListener );

		GridData gridData = new GridData( GridData.FILL_HORIZONTAL );
		gridData.horizontalSpan = 4;
		new Label( parentControl, SWT.SEPARATOR | SWT.HORIZONTAL ).setLayoutData( gridData );

		Label lb = new Label( parentControl, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$

		expressionCCombo = createExpressionCombo( parentControl );
		expressionCCombo.setLayoutData( GridDataFactory.swtDefaults( )
				.hint( 150, SWT.DEFAULT )
				.create( ) );

		operator = new CCombo( parentControl, SWT.READ_ONLY | SWT.BORDER );
		operator.setVisibleItemCount( 30 );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( operatorSelection );

		valuesLabel = new Label( parentControl, SWT.NONE );
		valuesComposite = new Composite( parentControl, SWT.NONE );
		valuesComposite.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 3, 1 )
				.create( ) );
		valuesComposite.setLayout( GridLayoutFactory.swtDefaults( )
				.margins( 0, 0 )
				.numColumns( 4 )
				.create( ) );
		create2ValueComposite( valuesComposite );

		new Label( parentControl, SWT.NONE );
		updateAggrButton = new Button( parentControl, SWT.CHECK );
		updateAggrButton.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.UpdateAggregation" ) ); //$NON-NLS-1$
		updateAggrButton.setLayoutData( GridDataFactory.swtDefaults( )
				.span( 3, 1 )
				.create( ) );
		updateAggrButton.setSelection( filterConditionElement != null ? filterConditionElement.updateAggregation( )
					: Boolean.TRUE );

		memberValueGroup = new Composite( parentControl, SWT.NONE );
		memberValueGroup.setLayout( new GridLayout( ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 5;
		memberValueGroup.setLayoutData( gd );
		createMemberValuesGroup( memberValueGroup );

		initializeDialog( );

		syncViewProperties( );

	}

	private CCombo createExpressionCombo( Composite parent )
	{
		final CCombo expressionValue = new CCombo( parent, SWT.BORDER );

		expressionValue.addMouseListener( new MouseAdapter( ) {

			public void mouseDown( MouseEvent e )
			{
				List bindingList = new ArrayList( );
				LevelViewHandle level = null;
				MeasureViewHandle measure = null;
				if ( groupBtn.getSelection( ) )
				{
					if ( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) > 0
							&& groupLevelList != null )
					{
						if ( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) - 1 < groupLevelList.size( ) )
						{
							level = (LevelViewHandle) groupLevelList.get( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) - 1 );
							if ( level != null )
							{
								bindingList = getReferableBindings( level );
							}
						}
						else
						{
							bindingList = getReferableBindings( getDimensionStrExpression( groupGroupLevel.getText( ) ) );
						}
					}
				}
				else if ( measureBtn.getSelection( ) )
				{
					if ( measureGroupLevel.indexOf( measureGroupLevel.getText( ) ) > 0
							&& measureList != null
							&& measureList.size( ) > 0 )
					{
						measure = (MeasureViewHandle) measureList.get( measureGroupLevel.indexOf( measureGroupLevel.getText( ) ) - 1 );
					}

					if ( measure != null )
					{
						bindingList = getReferableBindings( measure );
					}
				}
				else if ( detailBtn.getSelection( ) )
				{
					expressionValue.remove( 0,
							expressionValue.getItemCount( ) - 1 );

					List<String> cubeLevelNames = getCubeLevelNames( );
					for ( int i = 0; i < cubeLevelNames.size( ); i++ )
					{
						expressionValue.add( cubeLevelNames.get( i ) );
					}

					if ( expressionValue.getItemCount( ) > 0 )
					{
						expressionValue.add( DECORATOR_STRING
								+ new BindingGroup( IBindingMetaInfo.DIMENSION_TYPE ).getBindingGroupName( )
								+ DECORATOR_STRING,
								0 );
					}
					return;
				}

				BindingGroup bindingGroup[] = new BindingGroup[]{
						new BindingGroup( IBindingMetaInfo.MEASURE_TYPE ),
						new BindingGroup( IBindingMetaInfo.DIMENSION_TYPE ),
						new BindingGroup( IBindingMetaInfo.GRAND_TOTAL_TYPE ),
						new BindingGroup( IBindingMetaInfo.SUB_TOTAL_TYPE ),
						new BindingGroup( IBindingMetaInfo.OTHER_TYPE )
				};

				for ( int i = 0; i < bindingList.size( ); i++ )
				{
					IBindingMetaInfo metaInfo = (IBindingMetaInfo) bindingList.get( i );
					for ( int j = 0; j < bindingGroup.length; j++ )
					{
						if ( bindingGroup[j].type == metaInfo.getBindingType( ) )
						{
							bindingGroup[j].addBinding( metaInfo.getBindingName( ) );
							break;
						}
					}
				}

				expressionValue.remove( 0, expressionValue.getItemCount( ) - 1 );
				for ( int i = 0; i < bindingGroup.length; i++ )
				{
					BindingGroup group = bindingGroup[i];
					if ( group.getBindings( ) != null
							&& !group.getBindings( ).isEmpty( ) )
					{
						expressionValue.add( DECORATOR_STRING
								+ group.getBindingGroupName( )
								+ DECORATOR_STRING );
						for ( int j = 0; j < group.getBindings( ).size( ); j++ )
						{
							expressionValue.add( (String) group.getBindings( )
									.get( j ) );
						}
					}
				}
			}

		} );

		expressionValue.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				String selection = e.text;
				if ( expressionValue.indexOf( selection ) == -1 )
				{
					e.doit = true;
					return;
				}

				if ( selection.startsWith( DECORATOR_STRING ) )
				{
					int index = expressionValue.indexOf( selection );
					if ( index < expressionValue.getItemCount( ) - 1 )
						selection = expressionValue.getItem( index + 1 );
					else
						selection = ""; //$NON-NLS-1$
				}

				IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter( expressionCCombo );
				if ( converter != null )
				{
					String result = null;
					if ( detailBtn.getSelection( ) )
					{
						String[] splits = selection.split( "/" ); //$NON-NLS-1$
						String[] expression = new String[3];
						if ( splits.length > 0 )
							expression[0] = splits[0];
						if ( splits.length > 1 )
							expression[1] = splits[1];
						if ( splits.length > 2 )
							expression[2] = splits[2];
						
						if( CrosstabUtil.isBoundToLinkedDataSet( getCrosstab((ExtendedItemHandle) designHandle ))
								&& ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( ) != null)
						{
							LevelHandle levelHandle = null;
							for ( LevelHandle level : getCubeLevels() )
							{
								if( level.getName().equals(splits[1]) )
								{
									levelHandle = level;
									break;
								}
							}
							result = ExtendedDataModelUIAdapterHelper.getInstance( ).getAdapter( )
									.createExtendedDataItemExpression( levelHandle );
						}
						else
						{
							result = converter.getDimensionExpression( expression[0],
									expression[1],
									expression[2] );
						}
					}
					else
					{
						result = converter.getCubeBindingExpression( selection );
					}
					if ( result != null )
						expressionValue.setText( result );
					else
						expressionValue.setText( "" ); //$NON-NLS-1$
				}
				e.doit = false;
				updateMemberValues( );
				needRefreshList = true;
				updateButtons( );
			}
		} );

		Listener listener = new Listener( ) {

			public void handleEvent( Event event )
			{
				updateMemberValues( );
				needRefreshList = true;
				updateButtons( );
			}

		};

		ExpressionProvider provider = getCrosstabExpressionProvider( );

		ExpressionButtonUtil.createExpressionButton( parent,
				expressionValue,
				provider,
				designHandle,
				listener );

		return expressionValue;
	}

	private CCombo createExpressionValue( Composite parent )
	{
		final CCombo expressionValue = new CCombo( parent, SWT.BORDER );
		expressionValue.add( CHOICE_SELECT_VALUE );
		expressionValue.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				String selection = e.text;
				if ( expressionValue.indexOf( selection ) == -1 )
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
		expressionValue.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expressionValue.getSelectionIndex( ) == -1 )
					return;
				String selection = expressionValue.getItem( expressionValue.getSelectionIndex( ) );
				if ( selection.equals( CHOICE_SELECT_VALUE ) )
				{
					String value = getSelectionValue( expressionValue );
					if ( value != null )
						expressionValue.setText( value );
				}
			}
		} );
		expressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		createValueExpressionButton( parent, expressionValue );

		return expressionValue;
	}

	protected String getSelectionValue( CCombo CCombo )
	{
		String retValue = null;
		List selectValueList = getSelectedValueList( );
		if ( selectValueList == null || selectValueList.size( ) == 0 )
		{
			MessageDialog.openInformation( null,
					Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
					Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$

		}
		else
		{
			SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
			dialog.setSelectedValueList( selectValueList );

			if ( dialog.open( ) == IDialogConstants.OK_ID )
			{
				IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter( CCombo );
				retValue = dialog.getSelectedExprValue( converter );
			}
		}

		return retValue;
	}

	private CCombo createMultiExpressionValue( Composite parent )
	{
		final CCombo expressionValue = new CCombo( parent, SWT.BORDER );
		expressionValue.add( CHOICE_SELECT_VALUE );
		expressionValue.addVerifyListener( new VerifyListener( ) {

			public void verifyText( VerifyEvent e )
			{
				String selection = e.text;
				if ( expressionValue.indexOf( selection ) == -1 )
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
		expressionValue.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expressionValue.getSelectionIndex( ) == -1 )
					return;
				String selection = expressionValue.getItem( expressionValue.getSelectionIndex( ) );
				if ( selection.equals( CHOICE_SELECT_VALUE ) )
				{
					selectMultiValues( expressionValue );
				}
			}
		} );
		expressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkAddButtonStatus( );
				updateButtons( );
			}
		} );

		createMultiValueExpressionButton( parent, expressionValue );

		return expressionValue;
	}

	private void createMultiValueExpressionButton( Composite parent,
			final CCombo combo )
	{
		Listener listener = new Listener( ) {

			public void handleEvent( Event event )
			{
				addBtn.setEnabled( false );

				boolean change = false;

				Expression expression = ExpressionButtonUtil.getExpression( combo );
				if ( expression == null
						|| expression.getStringExpression( ).trim( ).length( ) == 0 )
					return;
				if ( valueList.indexOf( expression ) < 0 )
				{
					valueList.add( expression );
					change = true;
				}

				if ( change )
				{
					tableViewer.refresh( );
					updateButtons( );
					combo.setFocus( );
					combo.setText( "" );
				}
			}
		};

		ExpressionButtonUtil.createExpressionButton( parent,
				combo,
				getCrosstabExpressionProvider( ),
				designHandle,
				listener );

	}

	private void selectMultiValues( CCombo CCombo )
	{
		String[] retValue = null;

		List selectValueList = getSelectedValueList( );
		if ( selectValueList == null || selectValueList.size( ) == 0 )
		{
			MessageDialog.openInformation( null,
					Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
					Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$

		}
		else
		{
			SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
			dialog.setSelectedValueList( selectValueList );
			dialog.setMultipleSelection( true );
			if ( dialog.open( ) == IDialogConstants.OK_ID )
			{
				retValue = dialog.getSelectedExprValues( );
			}
		}

		if ( retValue != null )
		{
			addBtn.setEnabled( false );

			if ( retValue.length == 1 )
			{
				CCombo.setText( DEUtil.resolveNull( retValue[0] ) );
			}
			else if ( retValue.length > 1 )
			{
				CCombo.setText( "" ); //$NON-NLS-1$
			}

			boolean change = false;
			List strValues = new ArrayList( );
			for ( int i = 0; i < valueList.size( ); i++ )
			{
				strValues.add( ( (Expression) valueList.get( i ) ).getStringExpression( ) );
			}
			for ( int i = 0; i < retValue.length; i++ )
			{
				if ( strValues.indexOf( DEUtil.resolveNull( retValue[i] ) ) < 0 )
				{
					valueList.add( new Expression( DEUtil.resolveNull( retValue[i] ),
							ExpressionButtonUtil.getExpressionButton( CCombo )
									.getExpressionHelper( )
									.getExpressionType( ) ) );
					change = true;
				}
			}
			if ( change )
			{
				tableViewer.refresh( );
				updateButtons( );
				CCombo.setFocus( );
			}
		}
	}

	private void createValueExpressionButton( Composite parent,
			final CCombo CCombo )
	{
		Listener listener = new Listener( ) {

			public void handleEvent( Event event )
			{
				updateButtons( );
			}

		};

		ExpressionProvider provider = getCrosstabExpressionProvider( );

		ExpressionButtonUtil.createExpressionButton( parent,
				CCombo,
				provider,
				designHandle,
				listener );
	}

	private CrosstabFilterExpressionProvider getCrosstabExpressionProvider( )
	{
		if ( provider == null )
			provider = new CrosstabFilterExpressionProvider( designHandle );
		return provider;
	}

	protected Listener targetSelectionListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			targetSelectionChanged( );
		}

	};

	private void targetSelectionChanged( )
	{
		targetSelectionChanged( true );
	}

	private void targetSelectionChanged( boolean fireEvent )
	{
		if ( groupBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( true );
			parentComposite.layout( true, true );
			String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
			groupGroupLevel.setEnabled( true );
			measureGroupLevel.setEnabled( false );
			measureGroupLevel.removeAll( );
			groupGroupLevel.removeAll( );
			groupGroupLevel.setItems( groupLeveNames );
			groupGroupLevel.add( Messages.getString( "CrosstabFilterConditionBuilder.Item.SelectLevel" ), 0 ); //$NON-NLS-1$
			groupGroupLevel.select( 0 );
			expressionCCombo.removeAll( );
			expressionCCombo.setEnabled( false );
			ExpressionButtonUtil.getExpressionButton( expressionCCombo )
					.setEnabled( false );
			getCrosstabExpressionProvider( ).setDetail( false );
			if ( fireEvent )
			{
				filterTargetChanged( );
			}
			if(updateAggrButton!=null)
				updateAggrButton.setVisible( true );

		}
		else if ( measureBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( false );
			parentComposite.layout( true, true );
			String measureNames[] = (String[]) measureNameList.toArray( new String[measureNameList.size( )] );
			measureGroupLevel.setEnabled( true );
			groupGroupLevel.setEnabled( false );
			groupGroupLevel.removeAll( );
			measureGroupLevel.removeAll( );
			measureGroupLevel.setItems( measureNames );
			measureGroupLevel.add( Messages.getString( "CrosstabFilterConditionBuilder.Item.SelectMeasure" ), 0 ); //$NON-NLS-1$
			measureGroupLevel.select( 0 );
			expressionCCombo.removeAll( );
			expressionCCombo.setEnabled( false );
			ExpressionButtonUtil.getExpressionButton( expressionCCombo )
					.setEnabled( false );
			getCrosstabExpressionProvider( ).setDetail( false );
			if ( fireEvent )
			{
				filterTargetChanged( );
			}
			if(updateAggrButton!=null)
				updateAggrButton.setVisible( true );
		}
		else if ( detailBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( false );
			parentComposite.layout( true, true );
			measureGroupLevel.setEnabled( false );
			groupGroupLevel.setEnabled( false );
			groupGroupLevel.removeAll( );
			measureGroupLevel.removeAll( );
			expressionCCombo.removeAll( );
			expressionCCombo.setEnabled( false );
			ExpressionButtonUtil.getExpressionButton( expressionCCombo )
					.setEnabled( false );
			getCrosstabExpressionProvider( ).setDetail( true );
			if ( fireEvent )
			{
				filterTargetChanged( );
			}
			if(updateAggrButton!=null)
				updateAggrButton.setVisible( false );
		}
		updateMemberValues( );
		updateButtons( );
	}

	private String getDimensionStrExpression( String input )
	{
		String[] levels = input.split( "/" ); //$NON-NLS-1$
		String expression = "dimension"; //$NON-NLS-1$
		for ( String l : levels )
		{
			expression += "[\"" + l + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
		}
		return expression;
	}

	protected ValueCombo.ISelection2 dimensionValueAction = new ValueCombo.ISelection2( ) {

		public String doSelection( String CComboValue, int selectedIndex,
				String selectedValue )
		{
			return getDimensionStrExpression( selectedValue );
		}

		public String doSelection( String input )
		{
			return input;
		}
	};

	protected Listener groupLeveModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			filterTargetChanged( );
		}
	};

	protected void filterTargetChanged( )
	{
		needRefreshList = true;
		String targetString = null;
		if ( groupBtn.getSelection( ) )
		{
			// use select group level for filter condition
			if ( groupLevelList != null
					&& groupLevelList.size( ) > 0
					&& groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) > 0
					&& groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) <= groupLevelList.size( ) )
			{
				expressionCCombo.setEnabled( true );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( true );
				LevelViewHandle level = (LevelViewHandle) groupLevelList.get( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) - 1 );
				DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle( level.getCubeLevel( ) );
				targetString = ExpressionUtil.createJSDimensionExpression( dimensionHandle.getName( ),
						level.getCubeLevel( ).getName( ) );
			}
			else
			{
				expressionCCombo.setEnabled( false );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( false );
				updateMemberValues( );
				updateButtons( );
				return;
			}
		}
		else if ( measureBtn.getSelection( ) )
		{
			if ( measureList != null
					&& measureList.size( ) > 0
					&& measureGroupLevel.indexOf( measureGroupLevel.getText( ) ) > 0
					&& measureGroupLevel.indexOf( measureGroupLevel.getText( ) ) <= measureList.size( ) )
			{
				expressionCCombo.setEnabled( true );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( true );
				MeasureViewHandle measure = (MeasureViewHandle) measureList.get( measureGroupLevel.indexOf( measureGroupLevel.getText( ) ) - 1 );
				targetString = measure.getCubeMeasure( ).getName( );
			}
			else
			{
				expressionCCombo.setEnabled( false );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( false );
				updateMemberValues( );
				updateButtons( );
				return;
			}
		}
		else if ( detailBtn.getSelection( ) )
		{
			if ( cubeLevelNameList != null && cubeLevelNameList.size( ) > 0 )
			{
				expressionCCombo.setEnabled( true );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( true );
			}
			else
			{
				expressionCCombo.setEnabled( false );
				ExpressionButtonUtil.getExpressionButton( expressionCCombo )
						.setEnabled( false );
			}
			updateMemberValues( );
			updateButtons( );
			return;
		}

		if ( targetString != null )
		{
			ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
			CrosstabReportItemHandle crosstab = null;
			try
			{
				crosstab = (CrosstabReportItemHandle) element.getReportItem( );
			}
			catch ( ExtendedElementException ex )
			{
				ExceptionUtil.handle( ex );
			}
			DataRequestSession session = null;
			List retList = null;
			try
			{
				session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
				retList = getReferableBindings(session, crosstab, null, targetString);
				
				if ( retList != null && retList.size( ) > 0 )
				{
					IBindingMetaInfo meta = (IBindingMetaInfo) retList.get( 0 );
					IExpressionConverter converter = ExpressionButtonUtil.getCurrentExpressionConverter( expressionCCombo );
					expressionCCombo.setText( converter.getCubeBindingExpression( meta.getBindingName( ) ) );
				}
			}
			catch ( Exception ex )
			{
				logger.log( Level.SEVERE, ex.getMessage( ), ex );
			}
			finally
			{
				if ( session != null )
				{
					session.shutdown( );
				}
			}
		}
		updateMemberValues( );
		updateButtons( );
	}

	protected void createMemberValuesGroup( Composite content )
	{
		group = new Group( content, SWT.NONE );
		group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelColumnMemberValue" ) ); //$NON-NLS-1$
		group.setLayout( new GridLayout( ) );

		memberValueTable = new Table( group, SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION );
		memberValueTable.setLinesVisible( true );
		memberValueTable.setHeaderVisible( true );
		memberValueTable.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 150;
		gd.horizontalSpan = 3;
		group.setLayoutData( gd );

		dynamicViewer = new TableViewer( memberValueTable );

		TableColumn column = new TableColumn( memberValueTable, SWT.LEFT );
		column.setText( columns[0] );
		column.setWidth( 15 );

		TableColumn column1 = new TableColumn( memberValueTable, SWT.LEFT );
		column1.setResizable( columns[1] != null );
		if ( columns[1] != null )
		{
			column1.setText( columns[1] );
		}
		column1.setWidth( 200 );

		TableColumn column2 = new TableColumn( memberValueTable, SWT.LEFT );
		column2.setResizable( columns[2] != null );
		if ( columns[2] != null )
		{
			column2.setText( columns[2] );
		}
		column2.setWidth( 200 );

		dynamicViewer.setColumnProperties( columns );
		editor = new ExpressionValueCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		TextCellEditor textEditor = new TextCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		TextCellEditor textEditor2 = new TextCellEditor( dynamicViewer.getTable( ),
				SWT.READ_ONLY );
		CellEditor[] cellEditors = new CellEditor[]{
				textEditor, textEditor2, editor
		};
		if ( designHandle != null )
		{
			editor.setExpressionProvider( getCrosstabExpressionProvider( ) );
			editor.setReportElement( (ExtendedItemHandle) designHandle );
		}

		dynamicViewer.setCellEditors( cellEditors );

		dynamicViewer.setContentProvider( contentProvider );
		dynamicViewer.setLabelProvider( labelProvider );
		dynamicViewer.setCellModifier( cellModifier );
		dynamicViewer.addSelectionChangedListener( selectionChangeListener );
	}

	private ISelectionChangedListener selectionChangeListener = new ISelectionChangedListener( ) {

		public void selectionChanged( SelectionChangedEvent event )
		{
			ISelection selection = event.getSelection( );
			if ( selection instanceof StructuredSelection )
			{
				Object obj = ( (StructuredSelection) selection ).getFirstElement( );
				if ( obj != null
						&& obj instanceof MemberValueHandle
						&& editor != null )
				{
					editor.setMemberValue( (MemberValueHandle) obj );
				}
			}

		}
	};

	private static final String dummyChoice = "dummy"; //$NON-NLS-1$
	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputObj )
		{
			if ( !( inputObj instanceof List ) )
			{
				return new Object[0];
			}
			return ( (List) inputObj ).toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( columnIndex == 0 )
			{
				if ( element == dummyChoice )
					return Messages.getString( "LevelPropertyDialog.MSG.CreateNew" ); //$NON-NLS-1$
				else
				{
					if ( element instanceof RuleHandle )
					{
						return ( (RuleHandle) element ).getDisplayExpression( );
					}
					return ""; //$NON-NLS-1$
				}
			}
			else if ( columnIndex == 1 )
			{
				LevelHandle level = ( (MemberValueHandle) element ).getLevel( );
				if ( level != null )
				{
					return DEUtil.resolveNull( level.getName( ) );
				}
				else if (( (MemberValueHandle) element ).getCubeLevelName( ) != null)
				{
					return CubeUtil.splitLevelName( ( (MemberValueHandle) element ).getCubeLevelName( ) )[1];
				}
				else
				{
					return ""; //$NON-NLS-1$
				}
			}
			else if ( columnIndex == 2 )
			{
				String value = ( (MemberValueHandle) element ).getValue( );
				return value == null ? "" : value; //$NON-NLS-1$
			}
			return ""; //$NON-NLS-1$
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
			if ( Arrays.asList( columns ).indexOf( property ) == 2 )
			{
				return true;
			}
			else
			{
				return false;
			}
		}

		public Object getValue( Object element, String property )
		{
			if ( Arrays.asList( columns ).indexOf( property ) != 2 )
			{
				return ""; //$NON-NLS-1$
			}
			String value = ( (MemberValueHandle) element ).getValue( );
			return value == null ? "" : value; //$NON-NLS-1$
		}

		public void modify( Object element, String property, Object value )
		{
			if ( Arrays.asList( columns ).indexOf( property ) != 2 )
			{
				return;
			}
			TableItem item = (TableItem) element;
			MemberValueHandle memberValue = (MemberValueHandle) item.getData( );
			try
			{
				( memberValue ).setValue( (String) value );
			}
			catch ( SemanticException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}

			dynamicViewer.refresh( );
		}
	};
	private ExpressionValueCellEditor editor;

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{

		if ( filterConditionElement != null )
		{
			groupBtn.setSelection( levelViewHandle != null );
			measureBtn.setSelection( measureViewHandle != null );
			detailBtn.setSelection( crosstabHandle != null );
			targetSelectionChanged( );

			valueVisible = determineValueVisible( filterConditionElement.getOperator( ) );
			if ( valueVisible == 3 )
			{
				createValueListComposite( valuesComposite );
				valueList = new ArrayList( filterConditionElement.getValue1ExpressionList( )
						.getListValue( ) );
				if ( valueList != null )
				{
					tableViewer.setInput( valueList );
				}
			}
			else
			{
				create2ValueComposite( valuesComposite );
				if ( filterConditionElement != null )
				{
					if ( filterConditionElement.getValue1ExpressionList( )
							.getListValue( ) != null
							&& filterConditionElement.getValue1ExpressionList( )
									.getListValue( )
									.size( ) > 0 )
					{
						ExpressionButtonUtil.initExpressionButtonControl( expressionValue1,
								filterConditionElement.getValue1ExpressionList( )
										.getListValue( )
										.get( 0 ) );
					}
					ExpressionButtonUtil.initExpressionButtonControl( expressionValue2,
							filterConditionElement,
							FilterCondition.VALUE2_MEMBER );
				}
			}

			int index = 0;
			if ( levelViewHandle != null )
			{
				index = groupLevelList.indexOf( levelViewHandle );
				if ( index >= 0 )
				{
					groupGroupLevel.select( index + 1 );
				}
			}
			else if ( measureViewHandle != null )
			{
				index = measureList.indexOf( measureViewHandle );
				if ( index >= 0 )
				{
					measureGroupLevel.select( index + 1 );
				}
			}

			ExpressionButtonUtil.initExpressionButtonControl( expressionCCombo,
					filterConditionElement,
					FilterCondition.EXPR_MEMBER );
			operator.select( getIndexForOperatorValue( filterConditionElement.getOperator( ) ) );
			operatorChange( );

			int vv = determineValueVisible( filterConditionElement.getOperator( ) );

			if ( vv == 0 )
			{
				expressionValue1.setVisible( false );
				ExpressionButtonUtil.getExpressionButton( expressionValue1 )
						.getControl( )
						.setVisible( false );
				expressionValue2.setVisible( false );
				ExpressionButtonUtil.getExpressionButton( expressionValue2 )
						.getControl( )
						.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 1 )
			{
				expressionValue1.setVisible( true );
				ExpressionButtonUtil.getExpressionButton( expressionValue1 )
						.getControl( )
						.setVisible( true );
				expressionValue2.setVisible( false );
				ExpressionButtonUtil.getExpressionButton( expressionValue2 )
						.getControl( )
						.setVisible( false );
				andLable.setVisible( false );

				// ( (GridData) expressionValue2.getLayoutData( )
				// ).horizontalSpan = 1;
				// ( (GridData) expressionValue1.getLayoutData( )
				// ).horizontalSpan = 2;
				// parentComposite.layout( true, true );

			}
			else if ( vv == 2 )
			{
				expressionValue1.setVisible( true );
				ExpressionButtonUtil.getExpressionButton( expressionValue1 )
						.getControl( )
						.setVisible( true );
				expressionValue2.setVisible( true );
				ExpressionButtonUtil.getExpressionButton( expressionValue2 )
						.getControl( )
						.setVisible( true );
				andLable.setVisible( true );
				andLable.setEnabled( true );

				// ( (GridData) expressionValue1.getLayoutData( )
				// ).horizontalSpan = 1;
				// ( (GridData) expressionValue2.getLayoutData( )
				// ).horizontalSpan = 2;
				// parentComposite.getParent( ).layout( true, true );
			}
			else if ( valueVisible == 3 )
			{
				if ( expressionCCombo.getText( ).length( ) == 0 )
				{
					setEnableValueListComposite( false );
				}
				else
				{
					setEnableValueListComposite( true );
				}
			}
		}
		updateMemberValues( );
		updateButtons( );
	}

	private void setEnableValueListComposite( boolean val )
	{
		if ( valueListConList.size( ) > 0 )
		{
			int count = valueListConList.size( );
			for ( int i = 0; i < count; i++ )
			{
				Object obj = valueListConList.get( i );
				if ( obj != null
						&& obj instanceof Control
						&& ( !( (Widget) obj ).isDisposed( ) ) )
				{
					( (Control) obj ).setEnabled( val );
				}
			}
		}

	}

	// private Text createText( Composite parent )
	// {
	// Text txt = new Text( parent, SWT.BORDER );
	// GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
	// gdata.widthHint = 100;
	// txt.setLayoutData( gdata );
	//
	// return txt;
	// }

	private transient boolean needRefreshList = true;

	private List getSelectedValueList( )
	{
		if ( needRefreshList == false )
		{
			return selValueList;
		}
		CubeHandle cube = null;
		CrosstabReportItemHandle crosstab = null;
		if ( designHandle instanceof ExtendedItemHandle )
		{
			try
			{
				Object obj = ( (ExtendedItemHandle) designHandle ).getReportItem( );
				if ( obj instanceof CrosstabReportItemHandle )
				{
					crosstab = (CrosstabReportItemHandle) obj;
				}

				crosstab = (CrosstabReportItemHandle) ( (ExtendedItemHandle) designHandle ).getReportItem( );
				cube = crosstab.getCube( );
			}
			catch ( ExtendedElementException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}

		}
		if ( cube == null || expressionCCombo.getText( ).length( ) == 0 )
		{
			return new ArrayList( );
		}
		Iterator iter = null;

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			String expression = null;
			if ( ExpressionType.JAVASCRIPT.equals( ExpressionButtonUtil.getExpression( expressionCCombo )
					.getType( ) ) )
			{
				expression = expressionCCombo.getText( );
			}
			else
			{
				expression = ExpressionButtonUtil.getCurrentExpressionConverter( expressionCCombo )
						.convertExpression( expressionCCombo.getText( ),
								ExpressionType.JAVASCRIPT,
								IExpressionConverter.EXPRESSION_CLASS_CUBE );
			}

			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,designHandle.getModuleHandle() ) );

			DataService.getInstance( ).registerSession( cube, session );

			if (CrosstabUtil.isBoundToLinkedDataSet( crosstab ))
			{
				cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab, true );
			}
			else
			{
				cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab );
			}

			Map context = session.getDataSessionContext( ).getAppContext( );

			iter = CubeValueSelector.getMemberValueIterator( session,
					cube,
					expression,
					cubeQueryDefn,
					context );
		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		selValueList = new ArrayList( );
		int count = 0;
		int MAX_COUNT = PreferenceFactory.getInstance( )
				.getPreferences( CrosstabPlugin.getDefault( ),
						UIUtil.getCurrentProject( ) )
				.getInt( CrosstabPlugin.PREFERENCE_FILTER_LIMIT );
		while ( iter != null && iter.hasNext( ) )
		{
			Object obj = iter.next( );
			if ( obj != null )
			{
				if ( selValueList.indexOf( obj ) < 0 )
				{
					selValueList.add( obj );
					if ( ++count >= MAX_COUNT )
					{
						break;
					}
				}

			}

		}
		needRefreshList = false;
		if ( session != null )
		{
			session.shutdown( );
		}
		return selValueList;
	}

	private List getMeasures( )
	{
		if ( measureList != null )
		{
			return measureList;
		}
		measureList = new ArrayList( );
		measureNameList = new ArrayList( );
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crossTab = null;
		try
		{
			crossTab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		if ( crossTab == null )
		{
			return measureList;
		}

		int count = crossTab.getMeasureCount( );
		for ( int i = 0; i < count; i++ )
		{
			MeasureViewHandle measure = crossTab.getMeasure( i );
			if ( measure instanceof ComputedMeasureViewHandle )
				continue;
			if ( measure.getCubeMeasure( ) == null )
				continue;
			measureList.add( measure );
			measureNameList.add( measure.getCubeMeasure( ).getFullName( ) );
		}

		return measureList;
	}

	private List getLevels( )
	{
		if ( groupLevelList != null )
		{
			return groupLevelList;
		}
		groupLevelList = new ArrayList( );
		groupLevelNameList = new ArrayList( );
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crossTab = null;
		try
		{
			crossTab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		if ( crossTab == null )
		{
			return groupLevelList;
		}
		if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE )
					.getModelHandle( );
			getLevel( (ExtendedItemHandle) elementHandle );
		}

		if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
		{
			DesignElementHandle elementHandle = crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE )
					.getModelHandle( );
			getLevel( (ExtendedItemHandle) elementHandle );
		}

		return groupLevelList;
	}

	private void getLevel( ExtendedItemHandle handle )
	{
		CrosstabViewHandle crossTabViewHandle = null;
		try
		{
			crossTabViewHandle = (CrosstabViewHandle) handle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		if ( crossTabViewHandle == null )
		{
			return;
		}
		int dimensionCount = crossTabViewHandle.getDimensionCount( );
		for ( int i = 0; i < dimensionCount; i++ )
		{
			DimensionViewHandle dimension = crossTabViewHandle.getDimension( i );
			int levelCount = dimension.getLevelCount( );
			for ( int j = 0; j < levelCount; j++ )
			{
				LevelViewHandle levelHandle = dimension.getLevel( j );
				groupLevelList.add( levelHandle );
				if ( levelHandle.getCubeLevel( ) != null )
				{
					groupLevelNameList.add( levelHandle.getCubeLevel( )
							.getFullName( ) );
				}

			}
		}

	}

	protected void checkAddButtonStatus( )
	{
		if ( addExpressionValue != null )
		{
			String value = addExpressionValue.getText( );
			if ( value == null
					|| value.length( ) == 0
					|| value.trim( ).length( ) == 0 )
			{
				addBtn.setEnabled( false );
				return;
			}
			// if ( value != null )
			// {
			// value = value.trim( );
			// }
			if ( valueList.indexOf( ExpressionButtonUtil.getExpression( addExpressionValue ) ) < 0 )
			{
				addBtn.setEnabled( true );
			}
			else
			{
				addBtn.setEnabled( false );
			}
		}
	}

	protected void checkEditDelButtonStatus( )
	{
		if ( tableViewer == null )
		{
			return;
		}
		boolean enabled = ( tableViewer.getSelection( ) == null ) ? false
				: true;
		if ( enabled == true
				&& tableViewer.getSelection( ) instanceof StructuredSelection )
		{
			StructuredSelection selection = (StructuredSelection) tableViewer.getSelection( );
			if ( selection.toList( ).size( ) <= 0 )
			{
				enabled = false;
			}
		}
		editBtn.setEnabled( enabled );
		delBtn.setEnabled( enabled );

		enabled = table.getItemCount( ) > 0 ? true : false;
		delAllBtn.setEnabled( enabled );

	}

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK( )
	{
		if ( groupBtn.getSelection( )
				&& groupGroupLevel.getText( ) != null
				&& ( groupGroupLevel.getText( ).length( ) == 0 || groupGroupLevel.getSelectionIndex( ) == 0 ) )
		{
			return false;
		}

		if ( measureBtn.getSelection( )
				&& measureGroupLevel.getText( ) != null
				&& ( measureGroupLevel.getText( ).length( ) == 0 || measureGroupLevel.getSelectionIndex( ) == 0 ) )
		{
			return false;
		}

		if ( expressionCCombo == null )
		{
			return false;
		}

		if ( !isExpressionOK( ) )
		{
			return false;
		}

		return checkValues( );
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	protected boolean isExpressionOK( )
	{
		if ( expressionCCombo == null )
		{
			return false;
		}

		if ( !expressionCCombo.isEnabled( )
				|| expressionCCombo.getText( ) == null
				|| expressionCCombo.getText( ).length( ) == 0 )
		{
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		LevelViewHandle level = null;
		MeasureViewHandle measure = null;
		CrosstabReportItemHandle crosstab = null;
		if ( groupBtn.getSelection( ) )
		{
			level = (LevelViewHandle) groupLevelList.get( groupGroupLevel.getSelectionIndex( ) - 1 );
		}
		else if ( measureBtn.getSelection( ) )
		{
			measure = (MeasureViewHandle) measureList.get( measureGroupLevel.getSelectionIndex( ) - 1 );
		}
		else if ( detailBtn.getSelection( ) )
		{
			ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
			try
			{
				crosstab = (CrosstabReportItemHandle) element.getReportItem( );
			}
			catch ( ExtendedElementException e )
			{
				ExceptionUtil.handle( e );
			}
		}

		try
		{
			if ( filterConditionElement == null )
			{
				FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
						.newFilterConditionElement( );
				filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				ExpressionButtonUtil.saveExpressionButtonControl( expressionCCombo,
						filter,
						FilterCondition.EXPR_MEMBER );

				if ( !detailBtn.getSelection( ) )
				{
					filter.setUpdateAggregation( (Boolean) updateAggrButton.getSelection( ) );
				}

				if ( valueVisible == 3 )
				{
					filter.setValue1( valueList );
					filter.setValue2( "" ); //$NON-NLS-1$
				}
				else
				{
					assert ( !expressionValue1.isDisposed( ) );
					assert ( !expressionValue2.isDisposed( ) );
					if ( expressionValue1.getVisible( ) )
					{
						List valueList = new ArrayList( );
						valueList.add( ExpressionButtonUtil.getExpression( expressionValue1 ) );
						filter.setValue1( valueList );
					}
					else
					{
						filter.setValue1( NULL_STRING );
					}

					if ( expressionValue2.getVisible( ) )
					{
						ExpressionButtonUtil.saveExpressionButtonControl( expressionValue2,
								filter,
								FilterCondition.VALUE2_MEMBER );
					}
					else
					{
						filter.setValue2( NULL_STRING );
					}
				}

				if ( level != null )
				{
					if ( referencedLevelList != null
							&& referencedLevelList.size( ) > 0 )
					{
						filter.add( FilterConditionElementHandle.MEMBER_PROP,
								memberValueHandle );
					}

					level.getModelHandle( )
							.add( ILevelViewConstants.FILTER_PROP, filter );
				}
				else if ( measure != null )
				{
					measure.getModelHandle( )
							.add( IMeasureViewConstants.FILTER_PROP, filter );
				}
				else if ( crosstab != null )
				{
					crosstab.getModelHandle( )
							.add( ICrosstabReportItemConstants.FILTER_PROP,
									filter );
				}

			}
			else
			{
				// will update later;
				if ( ( groupBtn.getSelection( ) && level == levelViewHandle )
						|| ( measureBtn.getSelection( ) && measure == measureViewHandle ) ) // unchanged
				{

					filterConditionElement.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

					filterConditionElement.setUpdateAggregation( updateAggrButton.getSelection( ) );

					if ( valueVisible == 3 )
					{
						filterConditionElement.setValue1( valueList );
						filterConditionElement.setValue2( NULL_STRING );
					}
					else
					{
						assert ( !expressionValue1.isDisposed( ) );
						assert ( !expressionValue2.isDisposed( ) );
						if ( expressionValue1.getVisible( ) )
						{
							List valueList = new ArrayList( );
							valueList.add( ExpressionButtonUtil.getExpression( expressionValue1 ) );
							filterConditionElement.setValue1( valueList );
						}
						else
						{
							filterConditionElement.setValue1( NULL_STRING );
						}

						if ( expressionValue2.getVisible( ) )
						{
							ExpressionButtonUtil.saveExpressionButtonControl( expressionValue2,
									filterConditionElement,
									FilterCondition.VALUE2_MEMBER );
						}
						else
						{
							filterConditionElement.setValue2( NULL_STRING );
						}
					}
					ExpressionButtonUtil.saveExpressionButtonControl( expressionCCombo,
							filterConditionElement,
							FilterCondition.EXPR_MEMBER );

					if ( groupBtn.getSelection( ) )
					{
						if ( filterConditionElement.getMember( ) != null )
						{
							filterConditionElement.drop( FilterConditionElementHandle.MEMBER_PROP,
									0 );
						}

						if ( referencedLevelList != null
								&& referencedLevelList.size( ) > 0 )
						{
							filterConditionElement.add( FilterConditionElementHandle.MEMBER_PROP,
									memberValueHandle );
						}
					}
					else if ( measureBtn.getSelection( ) )
					{
						if ( filterConditionElement.getMember( ) != null )
						{
							filterConditionElement.drop( FilterConditionElementHandle.MEMBER_PROP,
									0 );
						}
					}

				}
				else
				{
					FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
							.newFilterConditionElement( );
					filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
							DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

					ExpressionButtonUtil.saveExpressionButtonControl( expressionCCombo,
							filter,
							FilterCondition.EXPR_MEMBER );

					if ( valueVisible == 3 )
					{
						filter.setValue1( valueList );
						filter.setValue2( NULL_STRING );
					}
					else
					{
						assert ( !expressionValue1.isDisposed( ) );
						assert ( !expressionValue2.isDisposed( ) );
						if ( expressionValue1.getVisible( ) )
						{
							List valueList = new ArrayList( );
							valueList.add( ExpressionButtonUtil.getExpression( expressionValue1 ) );
							filter.setValue1( valueList );
						}
						else
						{
							filter.setValue1( NULL_STRING );
						}

						if ( expressionValue2.getVisible( ) )
						{
							ExpressionButtonUtil.saveExpressionButtonControl( expressionValue2,
									filter,
									FilterCondition.VALUE2_MEMBER );
						}
						else
						{
							filter.setValue2( NULL_STRING );
						}
					}

					if ( levelViewHandle != null )
					{
						levelViewHandle.getModelHandle( )
								.drop( ILevelViewConstants.FILTER_PROP,
										filterConditionElement );
					}
					if ( measureViewHandle != null )
					{
						measureViewHandle.getModelHandle( )
								.drop( IMeasureViewConstants.FILTER_PROP,
										filterConditionElement );
					}
					if ( crosstabHandle != null )
					{
						crosstabHandle.getModelHandle( )
								.drop( ICrosstabReportItemConstants.FILTER_PROP,
										filterConditionElement );
					}

					if ( level != null )
					{
						if ( referencedLevelList != null
								&& referencedLevelList.size( ) > 0 )
						{
							filter.add( FilterConditionElementHandle.MEMBER_PROP,
									memberValueHandle );
						}
						level.getModelHandle( )
								.add( ILevelViewConstants.FILTER_PROP, filter );
					}
					else if ( measure != null )
					{
						measure.getModelHandle( )
								.add( IMeasureViewConstants.FILTER_PROP, filter );
					}
					else if ( crosstab != null )
					{
						crosstab.getModelHandle( )
								.add( ICrosstabReportItemConstants.FILTER_PROP,
										filter );
					}
				}
			}
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}

		setReturnCode( OK );
		close( );
	}

	private List getReferableBindings( Object target )
	{
		List retList = new ArrayList( );

		String targetString = null;
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crosstab = null;
		try
		{
			crosstab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException ex )
		{
			ExceptionUtil.handle( ex );
		}
		if ( target instanceof LevelViewHandle )
		{
			LevelViewHandle level = (LevelViewHandle) target;
			if ( level.getCubeLevel( ) == null )
			{
				return retList;
			}

			// get targetLevel
			DimensionHandle dimensionHandle = CrosstabAdaptUtil.getDimensionHandle( level.getCubeLevel( ) );
			targetString = ExpressionUtil.createJSDimensionExpression( dimensionHandle.getName( ),
					level.getCubeLevel( ).getName( ) );

		}
		else if ( target instanceof MeasureViewHandle )
		{
			MeasureViewHandle measure = (MeasureViewHandle) target;
			if ( measure.getCubeMeasure( ) == null )
			{
				return retList;
			}

			// get targetMeausre
			targetString = measure.getCubeMeasure( ).getName( );
		}
		else if ( target instanceof String )
		{
			targetString = (String) target;
		}

		// get cubeQueryDefn
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			
			retList = getReferableBindings(session, crosstab, target, targetString);

		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		finally
		{
			if ( session != null )
			{
				session.shutdown( );
			}
		}

		return retList;
	}

	private MemberValueHandle getChildMemberValue( MemberValueHandle memberValue )
	{
		if ( memberValue.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) != 1
				|| memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
						0 ) == null )
		{
			return null;
		}
		return (MemberValueHandle) memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
				0 );
	}

	private void dropChildMemberValue( MemberValueHandle memberValue )
	{
		MemberValueHandle child = getChildMemberValue( memberValue );
		if ( child == null )
			return;
		try
		{
			memberValue.drop( IMemberValueModel.MEMBER_VALUES_PROP, 0 );
		}
		catch ( SemanticException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
	}

	private MemberValueHandle updateMemberValuesFromLevelList(
			List referenceLevels, MemberValueHandle memberValue )
	{

		if ( measureBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( false );
			return null;
		}
		else
		{
			memberValueGroup.setVisible( true );
		}

		int count = referenceLevels.size( );
		MemberValueHandle lastMemberValue = memberValue;

		int hasCount = 0;
		while ( true )
		{
			hasCount++;
			LevelHandle tempLevel = getLevelHandle( (IDimensionLevel) referenceLevels.get( hasCount - 1 ) );
			if ( lastMemberValue.getLevel( ) != tempLevel
					&& !( lastMemberValue.getCubeLevelName().equals(tempLevel.getFullName()) && isBoundToLinkedDataSet( ) ))
			{
				try
				{
					lastMemberValue.setLevel( tempLevel );
					dropChildMemberValue( lastMemberValue );
				}
				catch ( SemanticException e )
				{
					logger.log( Level.SEVERE, e.getMessage( ), e );
				}
				break;
			}

			if ( getChildMemberValue( lastMemberValue ) == null )
			{
				break;
			}
			if ( hasCount >= count )
			{
				dropChildMemberValue( lastMemberValue );
				break;
			}
			lastMemberValue = getChildMemberValue( lastMemberValue );

		}

		for ( int i = hasCount; i < count; i++ )
		{
			MemberValueHandle newValue = DesignElementFactory.getInstance( )
					.newMemberValue( );
			LevelHandle tempLevel = getLevelHandle( (IDimensionLevel) referenceLevels.get( i ) );
			try
			{
				newValue.setLevel( tempLevel );
				newValue.setValue( "" );
				lastMemberValue.add( IMemberValueModel.MEMBER_VALUES_PROP,
						newValue );
			}
			catch ( SemanticException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}

			lastMemberValue = newValue;
		}

		return memberValue;
	}

	private boolean isBoundToLinkedDataSet()
	{
		CrosstabReportItemHandle crosstab = null;
		try
		{
			crosstab = (CrosstabReportItemHandle) ((ExtendedItemHandle) designHandle).getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			return false;
		}
		return CrosstabUtil.isBoundToLinkedDataSet(crosstab);
	}
	
	private LevelHandle getLevelHandle( IDimensionLevel levelInfo )
	{

		LevelHandle levelHandle = null;
		String levelName = levelInfo.getLevelName( );
		String dimensionName = levelInfo.getDimensionName( );
		ExtendedItemHandle extHandle = (ExtendedItemHandle) designHandle;
		CrosstabReportItemHandle crosstab = null;
		try
		{
			crosstab = (CrosstabReportItemHandle) extHandle.getReportItem( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		DimensionViewHandle dimension = CrosstabUtil.getDimensionViewHandle( crosstab,
				dimensionName );
		// LevelViewHandle level = getLevel(dimension, levelName );
		LevelViewHandle level = dimension.findLevel( levelName );
		levelHandle = level.getCubeLevel( );
		return levelHandle;
	}

	private void updateMemberValues( )
	{
		if ( groupBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( true );
		}
		else
		{
			memberValueGroup.setVisible( false );
			return;
		}

		if ( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) <= 0
				|| expressionCCombo.getText( ).length( ) == 0 )
		{
			memberValueTable.setEnabled( false );
			return;
		}
		LevelViewHandle level = null;
		if ( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) > 0
				&& groupLevelList != null
				&& groupLevelList.size( ) > 0 )
		{
			if ( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) - 1 < groupLevelList.size( ) )
				level = (LevelViewHandle) groupLevelList.get( groupGroupLevel.indexOf( groupGroupLevel.getText( ) ) - 1 );
		}
		if ( level == null )
		{
			memberValueTable.setEnabled( false );
			return;
		}

		// fix bug 191080 to update Member value Label.
		if ( level.getAxisType( ) == ICrosstabConstants.COLUMN_AXIS_TYPE )
		{
			group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelColumnMemberValue" ) ); //$NON-NLS-1$
		}
		else
		{
			group.setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.SelRowMemberValue" ) ); //$NON-NLS-1$
		}

		String expression = null;
		if ( ExpressionType.JAVASCRIPT.equals( ExpressionButtonUtil.getExpression( expressionCCombo )
				.getType( ) ) )
		{
			expression = expressionCCombo.getText( );
		}
		else
		{
			String bindingName = ExpressionButtonUtil.getCurrentExpressionConverter( expressionCCombo )
					.getBinding( expressionCCombo.getText( ) );
			expression = ExpressionUtility.getExpressionConverter( ExpressionType.JAVASCRIPT )
					.getCubeBindingExpression( bindingName );
		}

		referencedLevelList = CrosstabUtil.getReferencedLevels( level,
				expression );
		if ( referencedLevelList == null || referencedLevelList.size( ) == 0 )
		{
			memberValueTable.setEnabled( false );
			return;
		}

		editor.setReferencedLevelList( referencedLevelList );

		memberValueTable.setEnabled( true );
		memberValueHandle = null;
		if ( level == levelViewHandle )
		{
			memberValueHandle = filterConditionElement.getMember( );
		}

		if ( memberValueHandle == null )
		{
			memberValueHandle = DesignElementFactory.getInstance( )
					.newMemberValue( );
			try
			{
				memberValueHandle.setValue( "" );
				memberValueHandle.setLevel( getLevelHandle( (IDimensionLevel) referencedLevelList.get( 0 )) );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
		memberValueHandle = updateMemberValuesFromLevelList( referencedLevelList,
				memberValueHandle );
		List memList = getMemberValueList( memberValueHandle );
		dynamicViewer.setInput( memList );
	}

	private List getMemberValueList( MemberValueHandle parent )
	{
		List list = new ArrayList( );
		if ( parent == null )
		{
			return list;
		}

		MemberValueHandle memberValue = parent;

		while ( true )
		{
			list.add( memberValue );
			if ( memberValue.getContentCount( IMemberValueModel.MEMBER_VALUES_PROP ) != 1
					|| memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
							0 ) == null )
			{
				break;
			}
			memberValue = (MemberValueHandle) memberValue.getContent( IMemberValueModel.MEMBER_VALUES_PROP,
					0 );
		}
		return list;
	}

	static class BindingGroup
	{

		int type;
		List list = new ArrayList( );

		BindingGroup( int type )
		{
			this.type = type;
		}

		void addBinding( String bindingName )
		{
			list.add( bindingName );
		}

		List getBindings( )
		{
			return list;
		}

		String getBindingGroupName( )
		{

			if ( this.type == IBindingMetaInfo.MEASURE_TYPE )
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.Measures" ); //$NON-NLS-1$
			}
			else if ( this.type == IBindingMetaInfo.DIMENSION_TYPE )
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.Dimension" ); //$NON-NLS-1$
			}
			else if ( this.type == IBindingMetaInfo.GRAND_TOTAL_TYPE )
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.GrandTotal" ); //$NON-NLS-1$
			}
			else if ( this.type == IBindingMetaInfo.SUB_TOTAL_TYPE )
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.SubTotal" ); //$NON-NLS-1$
			}
			else if ( this.type == IBindingMetaInfo.OTHER_TYPE )
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.OtherType" ); //$NON-NLS-1$
			}
			else
			{
				return Messages.getString( "FilterbyTree.Bindings.Catogory.Undefined" ); //$NON-NLS-1$
			}
		}

	}

	/**
	 * Set the layout data of the button to a GridData with appropriate heights
	 * and widths.
	 * 
	 * @param button
	 */
	protected void setButtonCGridLayoutData( Button button )
	{
		int widthHint = convertHorizontalDLUsToPixels( IDialogConstants.BUTTON_WIDTH );
		Point minSize = button.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
		button.setLayoutData( GridDataFactory.swtDefaults( )
				.hint( Math.max( widthHint, minSize.x ), SWT.DEFAULT )
				.create( ) );
	}

	private Composite valuesComposite;
	private CCombo groupGroupLevel;
	private CCombo measureGroupLevel;
	private Button detailBtn;
	private CrosstabFilterExpressionProvider provider;
	private Label valuesLabel;
	private Button updateAggrButton;

	protected void editTableValue( )
	{
		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
		if ( selection.getFirstElement( ) != null
				&& selection.getFirstElement( ) instanceof Expression )
		{
			Expression initValue = (Expression) selection.getFirstElement( );

			ExpressionEditor editor = new ExpressionEditor( Messages.getString( "CrosstabFilterConditionBuilder.ExpressionEditor.Title" ) ); //$NON-NLS-1$
			editor.setExpression( initValue );
			editor.setInput( filterConditionElement,
					getCrosstabExpressionProvider( ),
					false );

			if ( editor.open( ) == OK )
			{
				Expression result = editor.getExpression( );
				if ( result == null
						|| result.getStringExpression( ) == null
						|| result.getStringExpression( ).length( ) == 0 )
				{
					MessageDialog.openInformation( getShell( ),
							Messages.getString( "CrosstabFilterConditionBuilder.MsgDlg.Title" ), //$NON-NLS-1$
							Messages.getString( "CrosstabFilterConditionBuilder.MsgDlg.Msg" ) ); //$NON-NLS-1$
					return;
				}
				int index = table.getSelectionIndex( );
				valueList.remove( index );
				valueList.add( index, result );
				tableViewer.refresh( );
				table.select( index );
			}
			updateButtons( );
		}
		else
		{
			editBtn.setEnabled( false );
		}
	}

	protected void createButtonsForButtonBar( Composite parent )
	{
		super.createButtonsForButtonBar( parent );
		updateButtons( );
	}

	protected void updateButtons( )
	{
		enableInput( isExpressionOK( ) );
		if ( getButton( IDialogConstants.OK_ID ) != null )
		{
			getButton( IDialogConstants.OK_ID ).setEnabled( isConditionOK( ) );
		}

	}

	protected void enableInput( boolean val )
	{
		operator.setEnabled( val );
		if ( valueVisible != 3 )
		{
			if ( expressionValue1 != null )
			{
				expressionValue1.setEnabled( val );
				ExpressionButtonUtil.getExpressionButton( expressionValue1 )
						.setEnabled( val );
			}
			if ( expressionValue2 != null )
			{
				expressionValue2.setEnabled( val );
				ExpressionButtonUtil.getExpressionButton( expressionValue2 )
						.setEnabled( val );
			}
			if ( andLable != null )
			{
				andLable.setEnabled( val );
			}

		}
		else
		{
			setControlEnable( valueListComposite, val );
			if ( val )
			{
				checkAddButtonStatus( );
				checkEditDelButtonStatus( );
			} // or set all the children control to false
		}
		if ( updateAggrButton != null )
			updateAggrButton.setEnabled( val );
	}

	protected void setControlEnable( Control control, boolean bool )
	{
		if ( control == null || control.isDisposed( ) )
		{
			return;
		}
		control.setEnabled( bool );
		Composite tmp = null;
		if ( control instanceof Composite )
		{
			tmp = (Composite) control;
		}
		if ( tmp != null && tmp.getChildren( ) != null )
		{
			for ( int i = 0; i < tmp.getChildren( ).length; i++ )
			{
				setControlEnable( tmp.getChildren( )[i], bool );
			}
		}
	}

	protected boolean checkValues( )
	{
		if ( valueVisible == 3 )
		{
			if ( valueList.size( ) <= 0 )
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			assert ( !expressionValue1.isDisposed( ) );
			assert ( !expressionValue2.isDisposed( ) );

			if ( expressionValue1.getVisible( ) )
			{
				if ( expressionValue1.getText( ) == null
						|| expressionValue1.getText( ).trim( ).length( ) == 0 )
				{
					return false;
				}
			}

			if ( expressionValue2.getVisible( ) )
			{
				if ( expressionValue2.getText( ) == null
						|| expressionValue2.getText( ).trim( ).length( ) == 0 )
				{
					return false;
				}
			}
		}

		return true;
	}

	protected void delTableValue( )
	{
		int index = table.getSelectionIndex( );
		if ( index > -1 )
		{
			valueList.remove( index );
			tableViewer.refresh( );
			if ( valueList.size( ) > 0 )
			{
				if ( valueList.size( ) <= index )
				{
					index = index - 1;
				}
				table.select( index );
			}
			updateButtons( );
		}
		else
		{
			delBtn.setEnabled( false );
		}
	}

	private List getCubeLevelNames( )
	{
		if ( cubeLevelNameList != null )
		{
			return cubeLevelNameList;
		}
		cubeLevelNameList = new ArrayList( );
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CubeHandle cube = null;
		try
		{
			cube = ( (CrosstabReportItemHandle) element.getReportItem( ) ).getCube( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		if ( cube == null )
		{
			return cubeLevelNameList;
		}
		Object[] dimensions = cube.getContents( CubeHandle.DIMENSIONS_PROP )
				.toArray( );
		TabularDimensionNodeProvider dimensionProvider = new TabularDimensionNodeProvider( );
		TabularLevelNodeProvider levelProvider = new TabularLevelNodeProvider( );
		for ( Object o : dimensions )
		{
			if ( o instanceof DimensionHandle )
			{
				DimensionHandle dimension = (DimensionHandle) o;
				Object[] levels = dimensionProvider.getChildren( o );
				for ( Object l : levels )
				{
					if ( l instanceof LevelHandle )
					{
						LevelHandle level = (LevelHandle) l;
						String levelName = dimension.getName( ) + "/" //$NON-NLS-1$
								+ level.getName( );
						if ( !isGroupLevel( levelName ) )
							cubeLevelNameList.add( levelName );
						Object[] attrs = levelProvider.getChildren( l );
						for ( Object a : attrs )
						{
							if ( a instanceof LevelAttributeHandle )
							{
								LevelAttributeHandle attr = (LevelAttributeHandle) a;

								levelName = dimension.getName( ) + "/" //$NON-NLS-1$
										+ level.getName( )
										+ "/" //$NON-NLS-1$
										+ attr.getName( );
								if ( !isGroupLevel( levelName ) )
									cubeLevelNameList.add( levelName );

							}
						}
					}
				}
			}
		}
		return cubeLevelNameList;
	}

	private List<LevelHandle> getCubeLevels()
	{
		List<LevelHandle> lhs = new ArrayList<LevelHandle>();
		
		ExtendedItemHandle element = (ExtendedItemHandle) designHandle;
		CubeHandle cube = null;
		try
		{
			cube = ( (CrosstabReportItemHandle) element.getReportItem( ) ).getCube( );
		}
		catch ( ExtendedElementException e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
		}
		if ( cube == null )
		{
			return lhs;
		}
		
		Object[] dimensions = cube.getContents( CubeHandle.DIMENSIONS_PROP ).toArray( );
		TabularDimensionNodeProvider dimensionProvider = new TabularDimensionNodeProvider( );
		TabularLevelNodeProvider levelProvider = new TabularLevelNodeProvider( );
		for ( Object o : dimensions )
		{
			if ( o instanceof DimensionHandle )
			{
				DimensionHandle dimension = (DimensionHandle) o;
				Object[] levels = dimensionProvider.getChildren( o );
				for ( Object l : levels )
				{
					if ( l instanceof LevelHandle )
					{
						lhs.add((LevelHandle) l);
					}
				}
			}
		}
		return lhs;
	}
	
	private boolean isGroupLevel( String levelName )
	{
		for ( Object level : groupLevelNameList )
		{
			if ( levelName.equals( level ) )
				return true;
		}
		return false;
	}

	private List getReferableBindings( DataRequestSession session, CrosstabReportItemHandle crosstab, Object target, String targetString) throws Exception
	{
		ICubeQueryDefinition cubeQueryDefn;
		List retList = new ArrayList();
		if(CrosstabUtil.isBoundToLinkedDataSet( crosstab ))
		{
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab, true );
			if ( groupBtn.getSelection( ) || target instanceof LevelViewHandle || target instanceof String )
			{
				retList = session.getCubeQueryUtil( )
						.getReferableBindingsForLinkedDataSetCube( targetString,
								cubeQueryDefn,
								false );
			}
			else if ( measureBtn.getSelection( ) || target instanceof MeasureViewHandle )
			{
				retList = session.getCubeQueryUtil( )
						.getReferableMeasureBindingsForLinkedDataSetCube( targetString,
								cubeQueryDefn );
			}
		}
		else
		{
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab );
			if ( groupBtn.getSelection( ) || target instanceof LevelViewHandle || target instanceof String)
			{
				retList = session.getCubeQueryUtil( )
						.getReferableBindings( targetString,
								cubeQueryDefn,
								false );
			}
			else if ( measureBtn.getSelection( ) || target instanceof MeasureViewHandle )
			{
				retList = session.getCubeQueryUtil( )
						.getReferableMeasureBindings( targetString,
								cubeQueryDefn );
			}
		}
		return retList;
	}
	
	private CrosstabReportItemHandle getCrosstab(ExtendedItemHandle element)
	{
		CrosstabReportItemHandle crosstab = null;
		try
		{
			crosstab = (CrosstabReportItemHandle) element.getReportItem( );
		}
		catch ( ExtendedElementException ex )
		{
			ExceptionUtil.handle( ex );
		}
		
		return crosstab;
	}
}
