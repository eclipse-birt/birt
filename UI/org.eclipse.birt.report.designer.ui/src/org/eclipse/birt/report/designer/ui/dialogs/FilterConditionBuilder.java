/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseTitleAreaDialog;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.MultiValueCombo;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.ValueCombo;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.api.olap.TabularHierarchyHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
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
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog for adding or editing map rule.
 */

public class FilterConditionBuilder extends BaseTitleAreaDialog
{

	protected static Logger logger = Logger.getLogger( FilterConditionBuilder.class.getName( ) );

	public static final String DLG_TITLE_NEW = Messages.getString( "FilterConditionBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "FilterConditionBuilder.DialogTitle.Edit" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_NEW = Messages.getString( "FilterConditionBuilder.DialogMessage.New" ); //$NON-NLS-1$
	public static final String DLG_MESSAGE_EDIT = Messages.getString( "FilterConditionBuilder.DialogMessage.Edit" ); //$NON-NLS-1$

	protected transient String[] popupItems = null;

	private static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	protected final String NULL_STRING = null;
	protected Composite dummy1, dummy2;
	protected Label label1, label2;

	protected List valueList = new ArrayList( );

	protected List selValueList = new ArrayList( );

	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;

	private transient String bindingName;

	private ParamBindingHandle[] bindingParams = null;

	private transient boolean refreshItems = true;

	protected transient ReportElementHandle currentItem = null;

	protected static String[] EMPTY_ARRAY = new String[]{};

	protected List columnList;

	protected int valueVisible;

	protected Table table;
	protected TableViewer tableViewer;

	/**
	 * Constant, represents empty String array.
	 */
	protected static final String[] EMPTY = new String[0];

	public void setReportElement( ReportElementHandle reportItem )
	{
		currentItem = reportItem;
	}

	/**
	 * @param bindingName
	 *            The selectValueExpression to set.
	 */
	public void setBindingName( String bindingName )
	{
		this.bindingName = bindingName;
	}

	/**
	 * 
	 */
	public void setBindingParams( ParamBindingHandle[] params )
	{
		this.bindingParams = params;
	}

	/**
	 * @param title
	 */
	public FilterConditionBuilder( String title, String message )
	{
		this( UIUtil.getDefaultShell( ), title, message );
	}

	protected String title, message;
	protected IChoiceSet choiceSet;

	/**
	 * @param parentShell
	 * @param title
	 */
	public FilterConditionBuilder( Shell parentShell, String title,
			String message )
	{
		super( parentShell );
		this.title = title;
		this.message = message;
	}

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( new AlphabeticallyComparator( ) );
		OPERATOR = new String[chs.length][2];

		for ( int i = 0; i < chs.length; i++ )
		{
			OPERATOR[i][0] = chs[i].getDisplayName( );
			OPERATOR[i][1] = chs[i].getName( );
		}
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

	protected FilterConditionHandle inputHandle;

	protected Combo expression, operator;

	protected Button addBtn, editBtn, delBtn, delAllBtn;

	protected ValueCombo expressionValue1, expressionValue2;

	protected MultiValueCombo addExpressionValue;

	protected Composite valueListComposite;

	protected Label andLable;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "FilterConditionBuilder.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	protected String[] getDataSetColumns( )
	{
		if ( columnList.isEmpty( ) )
		{
			return EMPTY;
		}
		String[] values = new String[columnList.size( )];
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			values[i] = getColumnName( columnList.get( i ) );
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets
	 * .Composite)
	 */
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

		Composite anotherParent = new Composite( innerParent, SWT.NONE );
		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		anotherParent.setLayoutData( gd );
		GridLayout glayout = new GridLayout( 4, false );
		anotherParent.setLayout( glayout );

		Label lb = new Label( anotherParent, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$

		Label lb2 = new Label( anotherParent, SWT.NONE );
		lb2.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( anotherParent, SWT.NONE );

		Composite condition = new Composite( innerParent, SWT.NONE );
		gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 220;
		condition.setLayoutData( gd );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.addListener( SWT.Selection, comboModify );
		expression.setItems( getDataSetColumns( ) );
		if ( expression.getItemCount( ) == 0 )
		{
			expression.add( DEUtil.resolveNull( null ) );
		}
		expression.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
						&& designHandle instanceof DataItemHandle )
				{
					expression.setText( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
				}
				updateButtons( );
			}
		} );
		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		gdata = new GridData( );
		gdata.heightHint = 20;
		gdata.widthHint = 20;
		expBuilder.setLayoutData( gdata );
		UIUtil.setExpressionButtonImage( expBuilder );
		expBuilder.setToolTipText( Messages.getString( "FilterConditionBuilder.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
		expBuilder.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				editValue( expression );
			}
		} );

		operator = new Combo( condition, SWT.READ_ONLY );
		for ( int i = 0; i < OPERATOR.length; i++ )
		{
			operator.add( OPERATOR[i][0] );
		}
		operator.addSelectionListener( operatorSelection );

		create2ValueComposite( condition );

		if ( inputHandle != null )
		{
			syncViewProperties( );
		}

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
	}

	// protected Listener expValueVerifyListener = new Listener( ) {
	//
	// public void handleEvent( Event event )
	// {
	// // TODO Auto-generated method stub
	// Combo thisCombo = (Combo) event.widget;
	// String text = event.text;
	// if ( text != null && thisCombo.indexOf( text ) >= 0 )
	// {
	// event.doit = false;
	// }
	// else
	// {
	// event.doit = true;
	// }
	// }
	// };

	protected String getColumnName( Object obj )
	{
		if ( obj instanceof ComputedColumnHandle )
			return ( (ComputedColumnHandle) obj ).getName( );
		else if ( obj instanceof ResultSetColumnHandle )
			return ( (ResultSetColumnHandle) obj ).getColumnName( );
		else
			return ""; //$NON-NLS-1$
	}

	// private Listener expValueSelectionListener = new Listener( ) {
	//
	// public void handleEvent( Event event )
	// {
	// // TODO Auto-generated method stub
	// Combo thisCombo = (Combo) event.widget;
	// int selectionIndex = thisCombo.getSelectionIndex( );
	// if ( selectionIndex < 0 )
	// {
	// return;
	// }
	// String value = popupItems[selectionIndex];
	//
	// boolean isAddClick = false;
	// if ( tableViewer != null
	// && ( addBtn != null && ( !addBtn.isDisposed( ) ) ) )
	// {
	// isAddClick = true;
	// }
	//
	// bindingName = null;
	// for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
	// {
	// String columnName = getColumnName( iter.next( ) );
	// if ( DEUtil.getColumnExpression( columnName )
	// .equals( expression.getText( ) ) )
	// {
	// bindingName = columnName;
	// break;
	// }
	// }
	//
	// boolean returnValue = false;
	// if ( value != null )
	// {
	// String newValues[] = new String[1];
	// if ( value.equals( ( actions[0] ) ) )
	// {
	// if ( bindingName != null )
	// {
	// try
	// {
	// List selectValueList = getSelectValueList( );
	// SelectValueDialog dialog = new SelectValueDialog(
	// PlatformUI.getWorkbench( )
	// .getDisplay( )
	// .getActiveShell( ),
	//									Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
	// if ( isAddClick )
	// {
	// dialog.setMultipleSelection( true );
	// }
	// dialog.setSelectedValueList( selectValueList );
	// if ( bindingParams != null )
	// {
	// dialog.setBindingParams( bindingParams );
	// }
	// if ( dialog.open( ) == IDialogConstants.OK_ID )
	// {
	// returnValue = true;
	// newValues = dialog.getSelectedExprValues( );
	// }
	// }
	// catch ( Exception ex )
	// {
	// MessageDialog.openError( null,
	//									Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
	//									Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
	//											+ "\n" //$NON-NLS-1$
	// + ex.getMessage( ) );
	// }
	// }
	// else if ( designHandle instanceof TabularCubeHandle
	// || designHandle instanceof TabularHierarchyHandle )
	// {
	// DataSetHandle dataSet;
	// if ( designHandle instanceof TabularCubeHandle )
	// dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
	// else
	// {
	// dataSet = ( (TabularHierarchyHandle) designHandle ).getDataSet( );
	// if ( dataSet == null
	// && ( (TabularHierarchyHandle) designHandle ).getLevelCount( ) > 0 )
	// {
	// dataSet = ( (TabularCubeHandle) ( (TabularHierarchyHandle) designHandle
	// ).getContainer( )
	// .getContainer( ) ).getDataSet( );
	// }
	// }
	// String expressionString = expression.getText( );
	// try
	// {
	// List selectValueList = SelectValueFetcher.getSelectValueList(
	// expressionString,
	// dataSet );
	// SelectValueDialog dialog = new SelectValueDialog(
	// PlatformUI.getWorkbench( )
	// .getDisplay( )
	// .getActiveShell( ),
	//									Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
	// dialog.setSelectedValueList( selectValueList );
	// if ( isAddClick )
	// {
	// dialog.setMultipleSelection( true );
	// }
	// if ( dialog.open( ) == IDialogConstants.OK_ID )
	// {
	// returnValue = true;
	// newValues = dialog.getSelectedExprValues( );
	//
	// }
	//
	// }
	// catch ( BirtException e1 )
	// {
	// // TODO Auto-generated catch block
	// MessageDialog.openError( null,
	//									Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
	//									Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
	//											+ "\n" //$NON-NLS-1$
	// + e1.getMessage( ) );
	// }
	// }
	// else
	// {
	// MessageDialog.openInformation( null,
	//								Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
	//								Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
	// }
	// }
	// else if ( value.equals( actions[1] ) )
	// {
	// ExpressionBuilder dialog = new ExpressionBuilder(
	// PlatformUI.getWorkbench( )
	// .getDisplay( )
	// .getActiveShell( ),
	// thisCombo.getText( ) );
	//
	// if ( designHandle != null )
	// {
	// if ( expressionProvider == null )
	// {
	// if ( designHandle instanceof TabularCubeHandle
	// || designHandle instanceof TabularHierarchyHandle )
	// {
	// dialog.setExpressionProvier( new BindingExpressionProvider( designHandle,
	// null ) );
	// }
	// else
	// {
	// dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
	// }
	// }
	// else
	// {
	// dialog.setExpressionProvier( expressionProvider );
	// }
	// }
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
	//
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
	//
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
	//
	// }
	// }
	// }
	//
	// };

	private int create2ValueComposite( Composite condition )
	{

		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			return 0;
		}

		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			valueListComposite.dispose( );
			valueListComposite = null;
		}

		GridData expgd = new GridData( GridData.FILL_HORIZONTAL );
		expgd.minimumWidth = 100;

		expressionValue1 = new ValueCombo( condition, SWT.NONE );
		expressionValue1.setLayoutData( expgd );
		expressionValue1.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		// expressionValue1.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue1.addListener( SWT.Selection,
		// expValueSelectionListener );
		refreshList( );
		expressionValue1.setItems( popupItems );
		expressionValue1.addSelectionListener( 0, selectValueAction );
		expressionValue1.addSelectionListener( 1, expValueAction );

		dummy1 = createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		andLable.setEnabled( false );
		// andLable.setVisible( false );

		dummy2 = createDummy( condition, 3 );

		expressionValue2 = new ValueCombo( condition, SWT.NONE );
		expressionValue2.setLayoutData( expgd );
		expressionValue2.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		// expressionValue2.addListener( SWT.Verify, expValueVerifyListener );
		// expressionValue2.addListener( SWT.Selection,
		// expValueSelectionListener );
		expressionValue2.setItems( popupItems );

		expressionValue2.addSelectionListener( 0, selectValueAction );
		expressionValue2.addSelectionListener( 1, expValueAction );

		// expressionValue2.setVisible( false );

		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( 0 );
			operatorChange( );
		}
		condition.getParent( ).layout( true, true );
		if ( getButtonBar( ) != null )
			condition.getShell( ).pack( );
		return 1;
	}

	private int createValueListComposite( Composite parent )
	{

		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			return 0;
		}

		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			expressionValue1.dispose( );
			expressionValue1 = null;

			dummy1.dispose( );
			dummy1 = null;

			expressionValue2.dispose( );
			expressionValue2 = null;

			dummy2.dispose( );
			dummy2 = null;

			andLable.dispose( );
			andLable = null;
		}

		valueListComposite = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_BOTH );
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData( gdata );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		valueListComposite.setLayout( layout );

		Group group = new Group( valueListComposite, SWT.NONE );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData( data );
		layout = new GridLayout( );
		layout.numColumns = 4;
		group.setLayout( layout );

		new Label( group, SWT.NONE ).setText( Messages.getString( "FilterConditionBuilder.label.value" ) ); //$NON-NLS-1$

		GridData expgd = new GridData( );
		expgd.widthHint = 100;

		addExpressionValue = new MultiValueCombo( group, SWT.NONE );
		addExpressionValue.setLayoutData( expgd );

		addBtn = new Button( group, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) ); //$NON-NLS-1$
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( addBtn );

		addBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
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

		new Label( group, SWT.NONE );

		int tableStyle = SWT.SINGLE
				| SWT.BORDER
				| SWT.H_SCROLL
				| SWT.V_SCROLL
				| SWT.FULL_SELECTION;
		table = new Table( group, tableStyle );
		data = new GridData( GridData.FILL_BOTH );
		data.horizontalSpan = 4;
		table.setLayoutData( data );

		table.setHeaderVisible( false );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		String[] columNames = new String[]{
			Messages.getString( "FilterConditionBuilder.list.item1" ), //$NON-NLS-1$
		};
		int[] columLength = new int[]{
			288
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
				// TODO Auto-generated method stub
			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				checkEditDelButtonStatus( );
			}
		} );

		table.addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
				// TODO Auto-generated method stub
				if ( e.keyCode == SWT.DEL )
				{
					delTableValue( );
				}

			}

			public void keyReleased( KeyEvent e )
			{
				// TODO Auto-generated method stub

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

		Composite rightPart = new Composite( valueListComposite, SWT.NONE );
		data = new GridData( GridData.HORIZONTAL_ALIGN_END );
		rightPart.setLayoutData( data );
		layout = new GridLayout( );
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout( layout );

		editBtn = new Button( rightPart, SWT.PUSH );
		editBtn.setText( Messages.getString( "FilterConditionBuilder.button.edit" ) ); //$NON-NLS-1$
		editBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.edit.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( editBtn );
		GridData gd = (GridData) editBtn.getLayoutData( );
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.END;
		editBtn.setLayoutData( gd );
		editBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
				delTableValue( );
			}

		} );

		delAllBtn = new Button( rightPart, SWT.PUSH );
		delAllBtn.setText( Messages.getString( "FilterConditionBuilder.button.deleteall" ) ); //$NON-NLS-1$
		delAllBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.deleteall.tooltip" ) ); //$NON-NLS-1$
		setButtonLayoutData( delAllBtn );
		gd = (GridData) delAllBtn.getLayoutData( );
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.BEGINNING;
		delAllBtn.setLayoutData( gd );
		delAllBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
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

		addExpressionValue.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				checkAddButtonStatus( );
				updateButtons( );
			}
		} );

		// addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		// addExpressionValue.addListener( SWT.Selection,
		// expValueSelectionListener );

		refreshList( );
		addExpressionValue.setItems( popupItems );
		addExpressionValue.addSelectionListener( 0, mAddSelValueAction );
		addExpressionValue.addSelectionListener( 1, mAddExpValueAction );

		parent.getParent( ).layout( true, true );
		if ( getButtonBar( ) != null )
			parent.getShell( ).pack( );
		return 1;

	}

	protected ITableLabelProvider tableLableProvier = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			// TODO Auto-generated method stub
			if ( columnIndex == 0 )
			{
				return (String) element;
			}
			return ""; //$NON-NLS-1$
		}

		public void addListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public boolean isLabelProperty( Object element, String property )
		{
			// TODO Auto-generated method stub
			return false;
		}

		public void removeListener( ILabelProviderListener listener )
		{
			// TODO Auto-generated method stub

		}
	};

	protected IStructuredContentProvider tableContentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
			// TODO Auto-generated method stub

		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
			// TODO Auto-generated method stub

		}

		public Object[] getElements( Object inputElement )
		{
			// TODO Auto-generated method stub
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
	};

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
		}
		else if ( valueVisible == 2 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( true );
			andLable.setVisible( true );
			andLable.setEnabled( true );
		}
		updateButtons( );
	}

	protected SelectionListener operatorSelection = new SelectionListener( ) {

		public void widgetSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub
			operatorChange( );
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}
	};

	protected Listener comboModify = new Listener( ) {

		public void handleEvent( Event e )
		{
			Assert.isLegal( e.widget instanceof Combo );
			Combo combo = (Combo) e.widget;
			String newValue = combo.getText( );
			String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
			if ( value != null )
				newValue = value;
			combo.setText( newValue );
			updateButtons( );
		}
	};

	protected Object getResultSetColumn( String name )
	{
		if ( columnList.isEmpty( ) )
		{
			return null;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			if ( getColumnName( columnList.get( i ) ).equals( name ) )
			{
				return columnList.get( i );
			}
		}
		return null;
	}

	protected Composite createDummy( Composite parent, int colSpan )
	{
		Composite dummy = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 22;
		gdata.horizontalSpan = colSpan;
		gdata.heightHint = 10;
		dummy.setLayoutData( gdata );

		return dummy;
	}

	private Text createText( Composite parent )
	{
		Text txt = new Text( parent, SWT.BORDER );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.widthHint = 100;
		txt.setLayoutData( gdata );

		return txt;
	}

	/*
	 * Update handle for the Map Rule builder
	 */
	public void updateHandle( FilterConditionHandle handle, int handleCount )
	{
		this.inputHandle = handle;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		setColumnList( this.designHandle );
	}

	protected IExpressionProvider expressionProvider;

	public void setDesignHandle( DesignElementHandle handle,
			IExpressionProvider provider )
	{
		setDesignHandle( handle );
		this.expressionProvider = provider;
		setColumnList( this.designHandle );
	}

	protected void setColumnList( DesignElementHandle handle )
	{
		DataSetHandle dataset = null;
		if ( handle instanceof TabularCubeHandle
				|| handle instanceof TabularHierarchyHandle )
		{
			try
			{
				if ( handle instanceof TabularCubeHandle )
					dataset = ( (TabularCubeHandle) handle ).getDataSet( );
				else
				{
					dataset = ( (TabularHierarchyHandle) handle ).getDataSet( );
					if ( dataset == null
							&& ( (TabularHierarchyHandle) handle ).getLevelCount( ) > 0 )
					{
						dataset = ( (TabularCubeHandle) ( (TabularHierarchyHandle) handle ).getContainer( )
								.getContainer( ) ).getDataSet( );
					}
				}
				if ( dataset != null )

					columnList = DataUtil.getColumnList( dataset );
				else
					columnList = Collections.EMPTY_LIST;
			}
			catch ( SemanticException e )
			{
				// TODO Auto-generated catch block
				e.printStackTrace( );
			}

		}
		else
		{
			columnList = DEUtil.getVisiableColumnBindingsList( handle );
		}

	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public FilterConditionHandle getInputHandle( )
	{
		return inputHandle;
	}

	/**
	 * Refreshes the OK button state.
	 * 
	 */
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
				expressionValue1.setEnabled( val );
			if ( expressionValue2 != null )
				expressionValue2.setEnabled( val );
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

	/**
	 * Gets if the condition is available.
	 */
	protected boolean isConditionOK( )
	{
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
	 * Gets if the values of the condition is(are) available.
	 */
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
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{

		expression.setText( DEUtil.resolveNull( inputHandle.getExpr( ) ) );
		operator.select( getIndexForOperatorValue( inputHandle.getOperator( ) ) );
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
		}
		else if ( valueVisible == 2 )
		{
			expressionValue1.setVisible( true );
			expressionValue2.setVisible( true );;
			andLable.setVisible( true );
			andLable.setEnabled( true );
		}
		else if ( valueVisible == 3 )
		{
			if ( expression.getText( ).length( ) == 0 )
			{
				valueListComposite.setEnabled( false );
			}
			else
			{
				valueListComposite.setEnabled( true );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		try
		{
			if ( inputHandle == null )
			{
				FilterCondition filter = StructureFactory.createFilterCond( );
				filter.setProperty( FilterCondition.OPERATOR_MEMBER,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
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
				}

				// set test expression for new map rule
				filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );
				PropertyHandle propertyHandle = designHandle.getPropertyHandle( ListingHandle.FILTER_PROP );
				propertyHandle.addItem( filter );
			}
			else
			{
				inputHandle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				if ( valueVisible == 3 )
				{
					inputHandle.setValue1( valueList );
					inputHandle.setValue2( NULL_STRING );
				}
				else
				{
					assert ( !expressionValue1.isDisposed( ) );
					assert ( !expressionValue2.isDisposed( ) );
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
			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	protected void editValue( Control control )
	{
		String initValue = null;
		if ( control instanceof Text )
		{
			initValue = ( (Text) control ).getText( );
		}
		else if ( control instanceof Combo )
		{
			initValue = ( (Combo) control ).getText( );
		}
		ExpressionBuilder expressionBuilder = new ExpressionBuilder( getShell( ),
				initValue );

		if ( designHandle != null )
		{
			if ( expressionProvider == null )
			{
				if ( designHandle instanceof TabularCubeHandle
						|| designHandle instanceof TabularHierarchyHandle )
				{
					expressionBuilder.setExpressionProvier( new BindingExpressionProvider( designHandle,
							null ) );
				}
				else
				{
					expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
				}
			}
			else
			{
				expressionBuilder.setExpressionProvier( expressionProvider );
			}

		}

		if ( expressionBuilder.open( ) == OK )
		{
			String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
			if ( control instanceof Text )
			{
				( (Text) control ).setText( result );
			}
			else if ( control instanceof Combo )
			{
				( (Combo) control ).setText( result );
			}
		}
		updateButtons( );
	}

	/**
	 * Sets the model input.
	 * 
	 * @param input
	 */
	public void setInput( Object inputHandle )
	{
		if ( inputHandle instanceof FilterConditionHandle )
		{
			this.inputHandle = (FilterConditionHandle) inputHandle;
		}
		else
		{
			this.inputHandle = null;
		}

	}

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

	private List getSelectValueList( ) throws BirtException
	{
		List selectValueList = new ArrayList( );
		ReportItemHandle reportItem = DEUtil.getBindingHolder( currentItem );
		if ( bindingName != null && reportItem != null )
		{
			selectValueList = SelectValueFetcher.getSelectValueList( expression.getText( ),
					reportItem.getDataSet( ),
					false );
		}
		else
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return selectValueList;
	}

	public int open( )
	{
		if ( getShell( ) == null )
		{
			// create the window
			create( );
		}
		updateButtons( );
		return super.open( );
	}

	protected ValueCombo.ISelection selectValueAction = new ValueCombo.ISelection( ) {

		public String doSelection( String input )
		{
			String retValue = null;
			// TODO Auto-generated method stub
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				String columnName = getColumnName( iter.next( ) );
				if ( DEUtil.getColumnExpression( columnName )
						.equals( expression.getText( ) ) )
				{
					bindingName = columnName;
					break;
				}
			}

			if ( bindingName != null )
			{
				try
				{
					List selectValueList = getSelectValueList( );
					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$
					dialog.setSelectedValueList( selectValueList );
					if ( bindingParams != null )
					{
						dialog.setBindingParams( bindingParams );
					}

					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						retValue = dialog.getSelectedExprValue( );
					}

				}
				catch ( Exception ex )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ ex.getMessage( ) );
				}

			}
			else if ( designHandle instanceof TabularCubeHandle
					|| designHandle instanceof TabularHierarchyHandle )
			{

				DataSetHandle dataSet = null;
				if ( designHandle instanceof TabularCubeHandle )
				{
					dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
				}
				else
				{
					dataSet = ( (TabularHierarchyHandle) designHandle ).getDataSet( );
				}
				String expressionString = expression.getText( );
				try
				{
					List selectValueList = SelectValueFetcher.getSelectValueList( expressionString,
							dataSet );
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
				catch ( BirtException e1 )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage( ) );
				}
			}
			else
			{
				MessageDialog.openInformation( null,
						Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
						Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
			}

			return retValue;
		}
	};

	protected ValueCombo.ISelection expValueAction = new ValueCombo.ISelection( ) {

		public String doSelection( String input )
		{
			String retValue = null;

			// TODO Auto-generated method stub
			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					input );

			if ( expressionProvider == null )
				dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
			else
				dialog.setExpressionProvier( expressionProvider );

			if ( dialog.open( ) == IDialogConstants.OK_ID )
			{
				retValue = dialog.getResult( );
			}
			return retValue;
		}
	};

	protected MultiValueCombo.ISelection mAddExpValueAction = new MultiValueCombo.ISelection( ) {

		public String[] doSelection( String input )
		{
			String[] retValue = null;
			// TODO Auto-generated method stub

			ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ),
					input );

			if ( expressionProvider == null )
				dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
			else
				dialog.setExpressionProvier( expressionProvider );

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

		public void doAfterSelection( MultiValueCombo combo )
		{
			// TODO Auto-generated method stub
			mAddSelValueAction.doAfterSelection( combo );
		}

	};

	protected MultiValueCombo.ISelection mAddSelValueAction = new MultiValueCombo.ISelection( ) {

		public String[] doSelection( String input )
		{
			// TODO Auto-generated method stub
			String[] retValue = null;

			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				Object obj = iter.next( );
				String columnName = "";
				if ( obj instanceof ComputedColumnHandle )
				{
					columnName = ( (ComputedColumnHandle) ( obj ) ).getName( );
				}
				else if ( obj instanceof ResultSetColumnHandle )
				{
					columnName = ( (ResultSetColumnHandle) ( obj ) ).getColumnName( );
				}

				if ( DEUtil.getColumnExpression( columnName )
						.equals( expression.getText( ) ) )
				{
					bindingName = columnName;
					break;
				}
			}

			if ( bindingName != null )
			{
				try
				{
					List selectValueList = getSelectValueList( );
					SelectValueDialog dialog = new SelectValueDialog( PlatformUI.getWorkbench( )
							.getDisplay( )
							.getActiveShell( ),
							Messages.getString( "ExpressionValueCellEditor.title" ) ); //$NON-NLS-1$

					dialog.setMultipleSelection( true );

					dialog.setSelectedValueList( selectValueList );
					if ( bindingParams != null )
					{
						dialog.setBindingParams( bindingParams );
					}
					if ( dialog.open( ) == IDialogConstants.OK_ID )
					{
						retValue = dialog.getSelectedExprValues( );
					}
				}
				catch ( Exception ex )
				{
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ ex.getMessage( ) );
				}
			}
			else if ( designHandle instanceof TabularCubeHandle
					|| designHandle instanceof TabularHierarchyHandle )
			{
				DataSetHandle dataSet;
				if ( designHandle instanceof TabularCubeHandle )
					dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
				else
				{
					dataSet = ( (TabularHierarchyHandle) designHandle ).getDataSet( );
					if ( dataSet == null
							&& ( (TabularHierarchyHandle) designHandle ).getLevelCount( ) > 0 )
					{
						dataSet = ( (TabularCubeHandle) ( (TabularHierarchyHandle) designHandle ).getContainer( )
								.getContainer( ) ).getDataSet( );
					}
				}
				String expressionString = expression.getText( );
				try
				{
					List selectValueList = SelectValueFetcher.getSelectValueList( expressionString,
							dataSet );
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
				catch ( BirtException e1 )
				{
					// TODO Auto-generated catch block
					MessageDialog.openError( null,
							Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
							Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" ) //$NON-NLS-1$
									+ "\n" //$NON-NLS-1$
									+ e1.getMessage( ) );
				}
			}
			else
			{
				MessageDialog.openInformation( null,
						Messages.getString( "SelectValueDialog.selectValue" ), //$NON-NLS-1$
						Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) ); //$NON-NLS-1$
			}

			return retValue;
		}

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
	};

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
				if ( expressionProvider == null )
				{
					if ( designHandle instanceof TabularCubeHandle
							|| designHandle instanceof TabularHierarchyHandle )
					{
						expressionBuilder.setExpressionProvier( new BindingExpressionProvider( designHandle,
								null ) );
					}
					else
					{
						expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
					}
				}
				else
				{
					expressionBuilder.setExpressionProvier( expressionProvider );
				}
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

}