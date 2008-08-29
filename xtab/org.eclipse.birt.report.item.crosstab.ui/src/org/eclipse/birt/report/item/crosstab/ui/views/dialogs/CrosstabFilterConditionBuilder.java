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
import java.util.logging.Level;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IBindingMetaInfo;
import org.eclipse.birt.report.data.adapter.api.IDimensionLevel;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MultiValueCombo;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ValueCombo;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.FilterConditionBuilder;
import org.eclipse.birt.report.designer.ui.dialogs.SelectValueDialog;
import org.eclipse.birt.report.designer.ui.dialogs.TreeValueDialog;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.CGridData;
import org.eclipse.birt.report.designer.ui.widget.CGridLayout;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
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
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.RuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IMemberValueModel;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
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
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;

/**
 * CrosstabFilterConditionBuilder
 */
public class CrosstabFilterConditionBuilder extends FilterConditionBuilder
{

	private final String NULL_STRING = null;

	public static final String DLG_MESSAGE_NEW = Messages.getString( "CrosstabFilterConditionBuilder.DialogMessage.New" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_EDIT = Messages.getString( "CrosstabFilterConditionBuilder.DialogMessage.Edit" ); //$NON-NLS-1$

	protected static final String[][] OPERATOR;
	protected Composite parentComposite = null;
	static
	{
		IChoiceSet chset = ChoiceSetFactory.getElementChoiceSet( ReportDesignConstants.FILTER_CONDITION_ELEMENT,
				IFilterConditionElementModel.OPERATOR_PROP );
		IChoice[] chs = chset.getChoices( new AlphabeticallyComparator( ) );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
	}

	private transient boolean refreshItems = true;
	protected Combo comboGroupLevel;
	protected List groupLevelList;
	protected List groupLevelNameList;
	protected List measureList;
	protected List measureNameList;
	protected FilterConditionElementHandle inputHandle;
	protected LevelViewHandle levelViewHandle;
	protected MeasureViewHandle measureViewHandle;
	protected Group group;
	protected Table memberValueTable;
	protected TableViewer dynamicViewer;
	protected Button groupBtn, measureBtn;
	protected Composite memberValueGroup;
	protected ValueCombo expression;
	protected Label targetLabel;

	protected String[] columns = new String[]{
			" ", //$NON-NLS-1$
			Messages.getString( "SelColumnMemberValue.Column.Level" ), //$NON-NLS-1$
			Messages.getString( "SelColumnMemberValue.Column.Value" ) //$NON-NLS-1$
	};

	private static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected MemberValueHandle memberValueHandle;
	protected List referencedLevelList;

	public void setInput( FilterConditionElementHandle input, Object target )
	{
		this.inputHandle = input;

		if ( target instanceof LevelViewHandle )
		{
			this.levelViewHandle = (LevelViewHandle) target;
		}
		else
		{
			this.levelViewHandle = null;
		}

		if ( target instanceof MeasureViewHandle )
		{
			this.measureViewHandle = (MeasureViewHandle) target;
		}
		else
		{
			this.measureViewHandle = null;
		}

	}

	protected List valueListConList = new ArrayList( );

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		super.setDesignHandle( handle );
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
			expressionValue1.dispose( );
			expressionValue1 = null;

			expressionValue2.dispose( );
			expressionValue2 = null;

			andLable.dispose( );
			andLable = null;
		}

		valueListConList.clear( );

		// valueListComposite = new Composite( parent, SWT.NONE );
		// GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		// gdata.widthHint = 300;
		// gdata.horizontalSpan = 4;
		// valueListComposite.setLayoutData( gdata );
		// GridLayout layout = new GridLayout( );
		// layout.numColumns = 4;
		// valueListComposite.setLayout( layout );

		// Composite group = new Composite( valueListComposite, SWT.NONE );
		// GridData data = new GridData( );
		// data.heightHint = 106;
		// data.horizontalSpan = 3;
		// data.horizontalIndent = 0;
		// data.horizontalAlignment = SWT.BEGINNING;
		// data.grabExcessHorizontalSpace = true;
		// group.setLayoutData( data );
		// layout = new GridLayout( );
		// layout.numColumns = 4;
		// group.setLayout( layout );
		//
		// new Label( group, SWT.NONE ).setText( Messages.getString(
		// "CrosstabFilterConditionBuilder.label.value" ) );

		CGridData expgd = new CGridData( GridData.HORIZONTAL_ALIGN_FILL );
		expgd.horizontalSpan = 2;

		addExpressionValue = new MultiValueCombo( parent, SWT.NONE );
		addExpressionValue.setLayoutData( expgd );

		valueListConList.add( addExpressionValue );
		refreshList( );
		addExpressionValue.setItems( popupItems );

		addExpressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkAddButtonStatus( );
				updateButtons( );
			}
		} );

		addBtn = new Button( parent, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) ); //$NON-NLS-1$
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) ); //$NON-NLS-1$
		setButtonCGridLayoutData( addBtn );
		valueListConList.add( addBtn );
		addBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
			}

			public void widgetSelected( SelectionEvent e )
			{
				String value = addExpressionValue.getText( ).trim( );
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

		Label dummy = new Label( parent, SWT.NONE );
		valueListConList.add( dummy );

		Label dummy2 = new Label( parent, SWT.NONE );
		valueListConList.add( dummy2 );

		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		table = new Table( parent, tableStyle );
		CGridData data = new CGridData( GridData.FILL_BOTH );
		data.horizontalSpan = 3;
		data.grabExcessHorizontalSpace = true;
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
		data = new CGridData( CGridData.FILL_BOTH
				| CGridData.VERTICAL_ALIGN_END );
		rightPart.setLayoutData( data );
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

		// addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		// addExpressionValue.addListener( SWT.Selection, btnSelListener );
		addExpressionValue.addSelectionListener( 0, mAddSelValueAction );
		addExpressionValue.addSelectionListener( 1, mAddExpValueAction );
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

		CGridData expgd = new CGridData( );
		expgd.horizontalSpan = 2;
		expgd.widthHint = 150;
		expressionValue1 = new ValueCombo( condition, SWT.NONE );
		expressionValue1.setLayoutData( expgd );
		expressionValue1.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		// expressionValue1.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue1.addListener( SWT.Selection, btnSelListener );
		expressionValue1.addSelectionListener( 0, selectValueAction );
		expressionValue1.addSelectionListener( 1, expValueAction );

		refreshList( );
		expressionValue1.setItems( popupItems );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		// andLable.setVisible( false );
		andLable.setEnabled( false );
		andLable.setLayoutData( new CGridData( CGridData.HORIZONTAL_ALIGN_END ) );
		// dummy2 = createDummy( condition, 3 );

		expressionValue2 = new ValueCombo( condition, SWT.NONE );
		expgd = new CGridData( );
		expgd.horizontalAlignment = CGridData.FILL;
		expgd.horizontalSpan = 1;
		expressionValue2.setLayoutData( expgd );

		expressionValue2.setItems( popupItems );
		// expressionValue2.setVisible( false );
		expressionValue2.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		// expressionValue2.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue2.addListener( SWT.Selection, btnSelListener );
		expressionValue2.addSelectionListener( 0, selectValueAction );
		expressionValue2.addSelectionListener( 1, expValueAction );

		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( 0 );
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
			int ret = createValueListComposite( operator.getParent( ) );
			if ( ret != 0 )
			{
				if ( inputHandle != null )
				{
					valueList = new ArrayList( inputHandle.getValue1List( ) );
				}

				tableViewer.setInput( valueList );
			}

		}
		else
		{
			int ret = create2ValueComposite( operator.getParent( ) );
			if ( ret != 0 && inputHandle != null )
			{
				expressionValue1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
			}

		}

		if ( valueVisible == 0 )
		{
			expressionValue1.setVisible( false );
			expressionValue2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( valueVisible == 1 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( false );
			andLable.setVisible( false );

			// ( (GridData) expressionValue2.getLayoutData( )
			// ).horizontalSpan = 1;
			// ( (GridData) expressionValue1.getLayoutData( )
			// ).horizontalSpan = 2;
			// parentComposite.layout( true, true );

		}
		else if ( valueVisible == 2 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( true );
			andLable.setVisible( true );
			andLable.setEnabled( true );

			// ( (GridData) expressionValue1.getLayoutData( )
			// ).horizontalSpan = 1;
			// ( (GridData) expressionValue2.getLayoutData( )
			// ).horizontalSpan = 2;
			// parentComposite.getParent( ).layout( true, true );
		}
		updateButtons( );
	}

	protected SelectionListener OpoertorSelection = new SelectionListener( ) {

		public void widgetSelected( SelectionEvent e )
		{
			operatorChange( );
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
		}
	};

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
		super( parentShell, title, message );
		initializeListener( );
	}

	private void initializeDialog( )
	{
		getLevels( );
		getMeasures( );
		String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
		comboGroupLevel.setItems( groupLeveNames );
		expression.setItems( expActions );

		groupBtn.setSelection( true );
		measureBtn.setSelection( false );
		targetLabel.setText( Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel" ) ); //$NON-NLS-1$

	}

	protected void createFilterConditionContent( Composite innerParent )
	{
		UIUtil.bindHelp( innerParent,
				IHelpContextIds.XTAB_FILTER_CONDITION_BUILDER );

		parentComposite = innerParent;

		Composite parentControl = new Composite( innerParent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.heightHint = 400;
		parentControl.setLayoutData( gd );
		parentControl.setLayout( new GridLayout( ) );

		Composite targetComposite = new Composite( parentControl, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		targetComposite.setLayoutData( gd );
		GridLayout glayout = new GridLayout( 3, false );
		targetComposite.setLayout( glayout );

		new Label( targetComposite, SWT.NONE ).setText( Messages.getString( "CrosstabFilterConditionBuilder.Label.Target" ) ); //$NON-NLS-1$
		groupBtn = new Button( targetComposite, SWT.RADIO );
		groupBtn.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.GroupLevel" ) ); //$NON-NLS-1$
		measureBtn = new Button( targetComposite, SWT.RADIO );
		measureBtn.setText( Messages.getString( "CrosstabFilterConditionBuilder.Button.Measure" ) ); //$NON-NLS-1$

		groupBtn.addListener( SWT.Selection, targetSelectionListener );
		measureBtn.addListener( SWT.Selection, targetSelectionListener );

		Composite groupLevelParent = new Composite( parentControl, SWT.NONE );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		groupLevelParent.setLayoutData( gd );
		CGridLayout cglayout = new CGridLayout( 5, false );
		groupLevelParent.setLayout( cglayout );

		targetLabel = new Label( groupLevelParent, SWT.NONE );
		CGridData cgd = new CGridData( );
		targetLabel.setLayoutData( cgd );
		cgd.widthHint = UIUtil.getMaxStringWidth( new String[]{
				Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel" ), //$NON-NLS-1$
				Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.Measure" ) //$NON-NLS-1$
		},
				targetLabel );
		targetLabel.setText( Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel" ) ); //$NON-NLS-1$

		comboGroupLevel = new Combo( groupLevelParent, SWT.READ_ONLY );
		CGridData cgdata = new CGridData( CGridData.HORIZONTAL_ALIGN_FILL );
		cgdata.horizontalSpan = 2;
		cgdata.maximumWidth = 180;
		comboGroupLevel.setLayoutData( cgdata );

		Label dummyLabel = new Label( groupLevelParent, SWT.NONE );
		cgdata = new CGridData( GridData.FILL_HORIZONTAL );
		cgdata.horizontalSpan = 2;
		dummyLabel.setLayoutData( cgdata );

		comboGroupLevel.addListener( SWT.Modify, ComboGroupLeveModify );

		Label lb = new Label( groupLevelParent, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$
		lb.setLayoutData( new CGridData( ) );

		expression = new ValueCombo( groupLevelParent, SWT.NONE );
		CGridData expgd = new CGridData( );
		expgd.horizontalSpan = 2;
		expgd.widthHint = 150;
		expression.setLayoutData( expgd );

		expression.addListener( SWT.Modify, expressionModify );
		// expression.addListener( SWT.Verify, expValueVerifyListener );
		// expression.addListener( SWT.Selection, exprValuePopBtnListener );
		expression.addSelectionListener( 0, filterByAction );
		expression.addSelectionListener( 1, expValueAction );

		operator = new Combo( groupLevelParent, SWT.READ_ONLY );
		cgd = new CGridData( CGridData.FILL_HORIZONTAL );
		cgd.horizontalSpan = 2;
		operator.setLayoutData( cgd );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( OpoertorSelection );
		new Label( groupLevelParent, SWT.NONE );

		create2ValueComposite( groupLevelParent );

		memberValueGroup = new Composite( parentControl, SWT.NONE );
		memberValueGroup.setLayout( new GridLayout( ) );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 5;
		memberValueGroup.setLayoutData( gd );
		createMemberValuesGroup( memberValueGroup );

		initializeDialog( );

		syncViewProperties( );

	}

	private static String[] expActions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.filterBy" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected Listener targetSelectionListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			targetSelectionChanged( );
		}

	};

	private void targetSelectionChanged( )
	{
		if ( groupBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( true );
			targetLabel.setText( Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.GroupLevel" ) ); //$NON-NLS-1$
			String groupLeveNames[] = (String[]) groupLevelNameList.toArray( new String[groupLevelNameList.size( )] );
			comboGroupLevel.removeAll( );
			comboGroupLevel.setItems( groupLeveNames );

			if ( comboGroupLevel.getItemCount( ) == 0 )
			{
				comboGroupLevel.add( DEUtil.resolveNull( null ) );
			}
			comboGroupLevel.select( 0 );

			updateMemberValues( );

		}
		else if ( measureBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( false );
			targetLabel.setText( Messages.getString( "CrosstabFilterConditionBuilder.DialogTitle.Label.Measure" ) ); //$NON-NLS-1$
			String measureNames[] = (String[]) measureNameList.toArray( new String[measureNameList.size( )] );
			comboGroupLevel.removeAll( );
			comboGroupLevel.setItems( measureNames );

			if ( comboGroupLevel.getItemCount( ) == 0 )
			{
				comboGroupLevel.add( DEUtil.resolveNull( null ) );
			}
			comboGroupLevel.select( 0 );
		}

	}
	protected Listener exprValuePopBtnListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			Combo thisCombo = (Combo) event.widget;
			int selectionIndex = thisCombo.getSelectionIndex( );
			if ( selectionIndex < 0 )
			{
				return;
			}
			String value = thisCombo.getItem( selectionIndex );
			List bindingList = new ArrayList( );

			boolean returnValue = false;
			if ( value != null )
			{
				String newValue = null;
				if ( value.equals( ( expActions[0] ) ) )
				{
					// String exprText = expression.getText( );

					LevelViewHandle level = null;
					MeasureViewHandle measure = null;
					if ( groupBtn.getSelection( ) )
					{
						if ( comboGroupLevel.getSelectionIndex( ) != -1
								&& groupLevelList != null
								&& groupLevelList.size( ) > 0 )
						{
							level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
						}

						if ( level == null )
						{
							return;
						}
						bindingList = getReferableBindings( level );
					}
					else if ( measureBtn.getSelection( ) )
					{
						if ( comboGroupLevel.getSelectionIndex( ) != -1
								&& measureList != null
								&& measureList.size( ) > 0 )
						{
							measure = (MeasureViewHandle) measureList.get( comboGroupLevel.getSelectionIndex( ) );
						}

						if ( measure == null )
						{
							return;
						}
						bindingList = getReferableBindings( measure );
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

					TreeValueDialog dialog = new TreeValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							treeLabelProvider,
							treeContentProvider );

					dialog.setInput( bindingGroup );
					dialog.setValidator( vialidator );
					dialog.setTitle( Messages.getString( "FilterbyTree.Title" ) ); //$NON-NLS-1$
					dialog.setMessage( Messages.getString( "FilterbyTree.Message" ) ); //$NON-NLS-1$
					dialog.addListener( SWT.PaintItem, valueTreePaintListener );
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						returnValue = true;
						String string = (String) dialog.getResult( )[0];
						newValue = ExpressionUtil.createJSDataExpression( string );
					}

				}
				else if ( value.equals( expActions[1] ) )
				{
					ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							expression.getText( ) );

					dialog.setExpressionProvier( new CrosstabFilterExpressionProvider( designHandle ) );

					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						returnValue = true;
						newValue = dialog.getResult( );
					}
				}
				if ( returnValue )
				{
					thisCombo.setText( DEUtil.resolveNull( newValue ) );
				}
			}
		}
	};

	private Listener valueTreePaintListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			// TODO Auto-generated method stub
			TreeItem item = (TreeItem) event.item;
			Object data = item.getData( );
			if ( ( data != null ) && ( data instanceof BindingGroup ) )
			{
				Font font = item.getFont( );
				FontData[] fontData = font.getFontData( );
				Font newFont = FontManager.getFont( fontData[0].getName( ),
						fontData[0].getHeight( ),
						fontData[0].getStyle( ) | SWT.BOLD );
				item.setFont( newFont );
			}

		}
	};

	private ISelectionStatusValidator vialidator = new ISelectionStatusValidator( ) {

		public IStatus validate( Object[] selection )
		{
			if ( selection.length == 1 && selection[0] instanceof String )
			{
				return new Status( IStatus.OK,
						CrosstabPlugin.ID,
						IStatus.OK,
						"", //$NON-NLS-1$
						null );
			}

			return new Status( IStatus.ERROR,
					CrosstabPlugin.ID,
					IStatus.ERROR,
					"", //$NON-NLS-1$
					null );
		}
	};

	protected ILabelProvider treeLabelProvider = new ILabelProvider( ) {

		public Image getImage( Object element )
		{
			return null;
		}

		public String getText( Object element )
		{
			if ( element == null )
			{
				return null;
			}
			if ( element instanceof BindingGroup )
			{
				return ( (BindingGroup) element ).getBindingGroupName( );
			}
			else
			{
				return element.toString( );
			}
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

	protected ITreeContentProvider treeContentProvider = new ITreeContentProvider( ) {

		public Object[] getChildren( Object parentElement )
		{
			if ( parentElement instanceof BindingGroup[] )
			{
				return (BindingGroup[]) parentElement;
			}
			else if ( parentElement instanceof BindingGroup )
			{
				return ( (BindingGroup) parentElement ).getBindings( )
						.toArray( );
			}
			else if ( parentElement instanceof String )
			{
				parentElement.toString( );
			}
			return null;
		}

		public Object getParent( Object element )
		{
			return null;
		}

		public boolean hasChildren( Object element )
		{
			if ( element instanceof BindingGroup[] )
			{
				return true;
			}
			else if ( element instanceof BindingGroup )
			{
				if ( ( (BindingGroup) element ).getBindings( ).size( ) > 0 )
				{
					return true;
				}
				else
				{
					return false;
				}

			}
			if ( element instanceof String )
			{
				return false;
			}
			return false;
		}

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof BindingGroup[] )
			{
				return (BindingGroup[]) inputElement;
			}
			else if ( inputElement instanceof BindingGroup )
			{
				return ( (BindingGroup) inputElement ).getBindings( ).toArray( );
			}

			return null;
		}

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}
	};
	protected Listener ComboGroupLeveModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			updateMemberValues( );
		}
	};

	protected Listener expressionModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			assert e.widget instanceof Combo;

			updateMemberValues( );
			needRefreshList = true;
			updateButtons( );
		}
	};

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
			editor.setExpressionProvider( new CrosstabFilterExpressionProvider( designHandle ) );
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

		if ( inputHandle == null )
		{
			if ( comboGroupLevel.getItemCount( ) == 0 )
			{
				comboGroupLevel.add( DEUtil.resolveNull( null ) );
			}
			comboGroupLevel.select( 0 );
			updateMemberValues( );
		}
		else
		{

			groupBtn.setSelection( measureViewHandle == null );
			measureBtn.setSelection( measureViewHandle != null );
			targetSelectionChanged( );

			valueVisible = determineValueVisible( inputHandle.getOperator( ) );
			if ( valueVisible == 3 )
			{
				createValueListComposite( operator.getParent( ) );
				valueList = new ArrayList( inputHandle.getValue1List( ) );
				tableViewer.setInput( valueList );
			}
			else
			{
				create2ValueComposite( operator.getParent( ) );
				expressionValue1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
			}

			int index = 0;
			if ( groupBtn.getSelection( ) )
			{
				index = groupLevelList.indexOf( levelViewHandle );
			}
			else
			{
				index = measureList.indexOf( measureViewHandle );
			}

			if ( index >= 0 )
			{
				comboGroupLevel.select( index );
			}

			expression.setText( DEUtil.resolveNull( inputHandle.getExpr( ) ) );
			operator.select( getIndexForOperatorValue( inputHandle.getOperator( ) ) );

			int vv = determineValueVisible( inputHandle.getOperator( ) );

			if ( vv == 0 )
			{
				expressionValue1.setVisible( false );
				expressionValue2.setVisible( false );;
				andLable.setVisible( false );
			}
			else if ( vv == 1 )
			{
				expressionValue1.setVisible( true );
				expressionValue2.setVisible( false );
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
				expressionValue2.setVisible( true );
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
				if ( expression.getText( ).length( ) == 0 )
				{
					setEnableValueListComposite( false );
				}
				else
				{
					setEnableValueListComposite( true );
				}
			}
		}

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
		if ( cube == null
				|| ( !( cube instanceof TabularCubeHandle ) )
				|| expression.getText( ).length( ) == 0 )
		{
			return new ArrayList( );
		}
		Iterator iter = null;

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab );
			iter = session.getCubeQueryUtil( )
					.getMemberValueIterator( (TabularCubeHandle) cube,
							expression.getText( ),
							cubeQueryDefn );
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
		// // test begin
		// for(int i =0; i < 5; i ++)
		// {
		// valueList.add( new Integer(i) );
		// }
		// // test end
		needRefreshList = false;
		return selValueList;
	}

	// private Listener btnSelListener = new Listener( ) {
	//
	// public void handleEvent( Event event )
	// {
	// Combo thisCombo = (Combo) event.widget;
	// int selectionIndex = thisCombo.getSelectionIndex( );
	// if ( selectionIndex < 0 )
	// {
	// return;
	// }
	// String value = thisCombo.getItem( selectionIndex );
	// boolean returnValue = false;
	//
	// boolean isAddClick = false;
	// if ( tableViewer != null
	// && ( addBtn != null && ( !addBtn.isDisposed( ) ) ) )
	// {
	// isAddClick = true;
	// }
	//
	// if ( value != null )
	// {
	// String newValues[] = new String[1];
	// if ( value.equals( ( actions[0] ) ) )
	// {
	//
	// List selectValueList = getSelectedValueList( );
	// if ( selectValueList == null
	// || selectValueList.size( ) == 0 )
	// {
	// MessageDialog.openInformation( null,
	//								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
	//								Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
	//
	// }
	// else
	// {
	// SelectValueDialog dialog = new SelectValueDialog(
	// PlatformUI.getWorkbench( )
	// .getDisplay( )
	// .getActiveShell( ),
	//								Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
	// dialog.setSelectedValueList( selectValueList );
	// if ( isAddClick )
	// {
	// dialog.setMultipleSelection( true );
	// }
	// if ( dialog.open( ) == IDialogConstants.OK_ID )
	// {
	// returnValue = true;
	// newValues = dialog.getSelectedExprValues( );
	// }
	// }
	//
	// }
	// else if ( value.equals( actions[1] ) )
	// {
	// ExpressionBuilder dialog = new ExpressionBuilder(
	// PlatformUI.getWorkbench( )
	// .getDisplay( )
	// .getActiveShell( ),
	// thisCombo.getText( ) );
	//
	// dialog.setExpressionProvier( new CrosstabFilterExpressionProvider(
	// designHandle ) );
	//
	// if ( dialog.open( ) == IDialogConstants.OK_ID )
	// {
	// returnValue = true;
	// newValues[0] = dialog.getResult( );
	// }
	// }
	// else if ( selectionIndex > 3 )
	// {
	//					newValues[0] = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
	// }
	// if ( returnValue )
	// {
	// if ( addExpressionValue == thisCombo )
	// {
	//						thisCombo.setText( "" ); //$NON-NLS-1$
	// addBtn.setEnabled( false );
	// }
	// else if ( newValues.length == 1 )
	// {
	// thisCombo.setText( DEUtil.resolveNull( newValues[0] ) );
	// }
	//
	// if ( isAddClick )
	// {
	// boolean change = false;
	// for ( int i = 0; i < newValues.length; i++ )
	// {
	// if ( valueList.indexOf( DEUtil.resolveNull( newValues[i] ) ) < 0 )
	// {
	// valueList.add( DEUtil.resolveNull( newValues[i] ) );
	// change = true;
	// }
	// }
	// if ( change )
	// {
	// tableViewer.refresh( );
	// updateButtons( );
	// addExpressionValue.setFocus( );
	// }
	//
	// }
	// }
	// }
	//
	// }
	// };

	private void refreshList( )
	{
		if ( refreshItems )
		{
			ArrayList finalItems = new ArrayList( 10 );
			for ( int n = 0; n < actions.length; n++ )
			{
				finalItems.add( actions[n] );
			}

			if ( currentItem != null )
			{
				// addParamterItems( finalItems );
			}
			popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
		}
		refreshItems = false;
	}

	// private List getGroupLevelNameList( )
	// {
	// if ( groupLevelNameList != null || groupLevelNameList.size( ) == 0 )
	// {
	// return groupLevelNameList;
	// }
	// getLevels( );
	// return groupLevelNameList;
	// }

	// private List getMeasureNameList( )
	// {
	// if ( measureNameList != null || measureNameList.size( ) == 0 )
	// {
	// return measureNameList;
	// }
	// getMeasures( );
	// return measureNameList;
	// }

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
		String value = addExpressionValue.getText( );
		if ( value == null
				|| value.length( ) == 0
				|| value.trim( ).length( ) == 0 )
		{
			addBtn.setEnabled( false );
			return;
		}
		if ( value != null )
		{
			value = value.trim( );
		}
		if ( valueList.indexOf( value ) < 0 )
		{
			addBtn.setEnabled( true );
		}
		else
		{
			addBtn.setEnabled( false );
		}
	}

	protected void checkEditDelButtonStatus( )
	{
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
		if ( comboGroupLevel.getText( ) != null
				&& comboGroupLevel.getText( ).length( ) == 0 )
		{
			return false;
		}

		if ( expression == null )
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
		if ( expression == null )
		{
			return false;
		}

		if ( expression.getText( ) == null
				|| expression.getText( ).length( ) == 0 )
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
		assert ( comboGroupLevel.getSelectionIndex( ) >= 0 );
		if ( groupBtn.getSelection( ) )
		{
			level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
		}
		else if ( measureBtn.getSelection( ) )
		{
			measure = (MeasureViewHandle) measureList.get( comboGroupLevel.getSelectionIndex( ) );
		}

		try
		{
			if ( inputHandle == null )
			{
				FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
						.newFilterConditionElement( );
				filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

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
						filter.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					else
					{
						filter.setValue1( NULL_STRING );
					}

					if ( expressionValue2.getVisible( ) )
					{
						filter.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
					else
					{
						filter.setValue2( NULL_STRING );
					}
				}

				if ( groupBtn.getSelection( ) )
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
				else if ( measureBtn.getSelection( ) )
				{
					measure.getModelHandle( )
							.add( IMeasureViewConstants.FILTER_PROP, filter );
				}

			}
			else
			{
				// will update later;
				if ( ( groupBtn.getSelection( ) && level == levelViewHandle )
						|| ( measureBtn.getSelection( ) && measure == measureViewHandle ) ) // unchanged
				{

					inputHandle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

					if ( valueVisible == 3 )
					{
						inputHandle.setValue1( valueList );
						inputHandle.setValue2( "" ); //$NON-NLS-1$
					}
					else
					{
						if ( expressionValue1.getVisible( ) )
						{
							inputHandle.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
						}
						else
						{
							inputHandle.setValue1( NULL_STRING );
						}

						if ( expressionValue2.getVisible( ) )
						{
							inputHandle.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
						}
						else
						{
							inputHandle.setValue2( NULL_STRING );
						}
					}
					inputHandle.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

					if ( groupBtn.getSelection( ) )
					{
						if ( inputHandle.getMember( ) != null )
						{
							inputHandle.drop( FilterConditionElementHandle.MEMBER_PROP,
									0 );
						}

						if ( referencedLevelList != null
								&& referencedLevelList.size( ) > 0 )
						{
							inputHandle.add( FilterConditionElementHandle.MEMBER_PROP,
									memberValueHandle );
						}
					}
					else if ( measureBtn.getSelection( ) )
					{
						if ( inputHandle.getMember( ) != null )
						{
							inputHandle.drop( FilterConditionElementHandle.MEMBER_PROP,
									0 );
						}
					}

				}
				else if ( ( groupBtn.getSelection( ) && level != levelViewHandle )
						|| ( measureBtn.getSelection( ) && measure != measureViewHandle ) )
				{
					FilterConditionElementHandle filter = DesignElementFactory.getInstance( )
							.newFilterConditionElement( );
					filter.setProperty( IFilterConditionElementModel.OPERATOR_PROP,
							DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

					filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );

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
							filter.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
						}
						else
						{
							filter.setValue1( NULL_STRING );
						}

						if ( expressionValue2.getVisible( ) )
						{
							filter.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
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
										inputHandle );
					}
					if ( measureViewHandle != null )
					{
						measureViewHandle.getModelHandle( )
								.drop( IMeasureViewConstants.FILTER_PROP,
										inputHandle );
					}

					if ( groupBtn.getSelection( ) )
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
					else if ( measureBtn.getSelection( ) )
					{
						measure.getModelHandle( )
								.add( IMeasureViewConstants.FILTER_PROP, filter );
					}

				}

			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		setReturnCode( OK );
		close( );
	}

	private List getReferableBindings( Object target )
	{
		List retList = new ArrayList( );

		String targetString = null;
		CrosstabReportItemHandle crosstab = null;
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

			crosstab = level.getCrosstab( );

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
			crosstab = measure.getCrosstab( );
		}

		// get cubeQueryDefn
		ICubeQueryDefinition cubeQueryDefn = null;
		DataRequestSession session = null;
		try
		{
			session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION ) );
			cubeQueryDefn = CrosstabUIHelper.createBindingQuery( crosstab );
			if ( target instanceof LevelViewHandle )
			{
				retList = session.getCubeQueryUtil( )
						.getReferableBindings( targetString,
								cubeQueryDefn,
								false );
			}
			else if ( target instanceof MeasureViewHandle )
			{
				retList = session.getCubeQueryUtil( )
						.getReferableMeasureBindings( targetString,
								cubeQueryDefn );
			}

		}
		catch ( Exception e )
		{
			logger.log( Level.SEVERE, e.getMessage( ), e );
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
			if ( lastMemberValue.getLevel( ) != tempLevel )
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
		if ( measureBtn.getSelection( ) )
		{
			memberValueGroup.setVisible( false );
			return;
		}
		else
		{
			memberValueGroup.setVisible( true );
		}

		if ( comboGroupLevel.getSelectionIndex( ) < 0
				|| expression.getText( ).length( ) == 0 )
		{
			memberValueTable.setEnabled( false );
			return;
		}
		LevelViewHandle level = null;
		if ( comboGroupLevel.getSelectionIndex( ) != -1
				&& groupLevelList != null
				&& groupLevelList.size( ) > 0 )
		{
			level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
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

		referencedLevelList = CrosstabUtil.getReferencedLevels( level,
				expression.getText( ) );
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
			memberValueHandle = inputHandle.getMember( );
		}

		if ( memberValueHandle == null )
		{
			memberValueHandle = DesignElementFactory.getInstance( )
					.newMemberValue( );
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

	class BindingGroup
	{

		int type;
		String displayName;
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
		CGridData data = new CGridData( CGridData.HORIZONTAL_ALIGN_FILL );
		int widthHint = convertHorizontalDLUsToPixels( IDialogConstants.BUTTON_WIDTH );
		Point minSize = button.computeSize( SWT.DEFAULT, SWT.DEFAULT, true );
		data.widthHint = Math.max( widthHint, minSize.x );
		button.setLayoutData( data );
	}

	private void initializeListener( )
	{
		mAddExpValueAction = new MultiValueCombo.ISelection( ) {

			public void doAfterSelection( MultiValueCombo combo )
			{
				// TODO Auto-generated method stub
				mAddSelValueAction.doAfterSelection( combo );
			}

			public String[] doSelection( String input )
			{
				String[] retValue = null;
				// TODO Auto-generated method stub

				ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						input );

				dialog.setExpressionProvier( new CrosstabFilterExpressionProvider( designHandle ) );

				if ( dialog.open( ) == IDialogConstants.OK_ID )
				{
					if ( dialog.getResult( ).length( ) != 0 )
					{
						retValue = new String[]{
							dialog.getResult( )
						};
					}
				}

				return retValue;
			}

		};

		mAddSelValueAction = new MultiValueCombo.ISelection( ) {

			public void doAfterSelection( MultiValueCombo combo )
			{
				// TODO Auto-generated method stub
				addBtn.setEnabled( false );

				if ( addExpressionValue.getSelStrings( ).length == 1 )
				{
					addExpressionValue.setText( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[0] ) );
				}
				else if ( addExpressionValue.getSelStrings( ).length > 1 )
				{
					addExpressionValue.setText( "" ); //$NON-NLS-1$
				}

				boolean change = false;
				for ( int i = 0; i < addExpressionValue.getSelStrings( ).length; i++ )
				{
					if ( valueList.indexOf( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[i] ) ) < 0 )
					{
						valueList.add( DEUtil.resolveNull( addExpressionValue.getSelStrings( )[i] ) );
						change = true;
					}
				}
				if ( change )
				{
					tableViewer.refresh( );
					updateButtons( );
					addExpressionValue.setFocus( );
				}

			}

			public String[] doSelection( String input )
			{
				String[] retValue = null;
				// TODO Auto-generated method stub

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

				return retValue;
			}

		};

		selectValueAction = new ValueCombo.ISelection( ) {

			public String doSelection( String input )
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
						retValue = dialog.getSelectedExprValue( );
					}
				}

				return retValue;
			}
		};

		expValueAction = new ValueCombo.ISelection( ) {

			public String doSelection( String input )
			{
				String retValue = null;
				ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						input );

				dialog.setExpressionProvier( new CrosstabFilterExpressionProvider( designHandle ) );

				if ( dialog.open( ) == IDialogConstants.OK_ID )
				{
					retValue = dialog.getResult( );
				}
				return retValue;
			}
		};

		filterByAction = new ValueCombo.ISelection( ) {

			public String doSelection( String input )
			{
				String retValue = null;
				// TODO Auto-generated method stub

				List bindingList = new ArrayList( );
				LevelViewHandle level = null;
				MeasureViewHandle measure = null;
				if ( groupBtn.getSelection( ) )
				{
					if ( comboGroupLevel.getSelectionIndex( ) != -1
							&& groupLevelList != null
							&& groupLevelList.size( ) > 0 )
					{
						level = (LevelViewHandle) groupLevelList.get( comboGroupLevel.getSelectionIndex( ) );
					}

					if ( level == null )
					{
						return retValue;
					}
					bindingList = getReferableBindings( level );
				}
				else if ( measureBtn.getSelection( ) )
				{
					if ( comboGroupLevel.getSelectionIndex( ) != -1
							&& measureList != null
							&& measureList.size( ) > 0 )
					{
						measure = (MeasureViewHandle) measureList.get( comboGroupLevel.getSelectionIndex( ) );
					}

					if ( measure == null )
					{
						return retValue;
					}
					bindingList = getReferableBindings( measure );
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

				TreeValueDialog dialog = new TreeValueDialog( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						treeLabelProvider,
						treeContentProvider );

				dialog.setInput( bindingGroup );
				dialog.setValidator( vialidator );
				dialog.setTitle( Messages.getString( "FilterbyTree.Title" ) ); //$NON-NLS-1$
				dialog.setMessage( Messages.getString( "FilterbyTree.Message" ) ); //$NON-NLS-1$
				dialog.addListener( SWT.PaintItem, valueTreePaintListener );
				if ( dialog.open( ) == IDialogConstants.OK_ID )
				{
					String string = (String) dialog.getResult( )[0];
					retValue = ExpressionUtil.createJSDataExpression( string );
				}

				return retValue;
			}
		};

	}

	protected ValueCombo.ISelection filterByAction;

	protected void editTableValue( )
	{

		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection( );
		if ( selection.getFirstElement( ) != null
				&& selection.getFirstElement( ) instanceof String )
		{
			String initValue = (String) selection.getFirstElement( );

			ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
					initValue );

			if ( designHandle != null )
			{
				if ( expressionProvider == null
						|| ( !( expressionProvider instanceof CrosstabFilterExpressionProvider ) ) )
				{
					expressionProvider = new CrosstabFilterExpressionProvider( designHandle );
				}
				expressionBuilder.setExpressionProvier( expressionProvider );
			}

			if ( expressionBuilder.open( ) == OK )
			{
				String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
				if ( result.length( ) == 0 )
				{
					MessageDialog.openInformation( getShell( ),
							Messages.getString( "MapRuleBuilderDialog.MsgDlg.Title" ),
							Messages.getString( "MapRuleBuilderDialog.MsgDlg.Msg" ) );
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

}
