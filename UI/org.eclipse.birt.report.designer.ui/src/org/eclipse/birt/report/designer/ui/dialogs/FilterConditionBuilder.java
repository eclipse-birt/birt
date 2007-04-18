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
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.FilterHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.IReportGraphicConstants;
import org.eclipse.birt.report.designer.ui.ReportPlatformUIImages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.PopupSelectionList;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog for adding or editing map rule.
 */

public class FilterConditionBuilder extends BaseDialog
{

	public static final String DLG_TITLE_NEW = Messages.getString( "FilterConditionBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "FilterConditionBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	private transient String[] popupItems = null;

	private static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	/**
	 * Usable operators for building map rule conditions.
	 */
	protected static final String[][] OPERATOR;

	private transient String bindingName;

	private ParamBindingHandle[] bindingParams = null;

	private transient boolean refreshItems = true;

	private transient ReportElementHandle currentItem = null;

	private static String[] EMPTY_ARRAY = new String[]{};

	private List columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

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
	public FilterConditionBuilder( String title )
	{
		this( UIUtil.getDefaultShell( ), title );
	}

	/**
	 * @param parentShell
	 * @param title
	 */
	public FilterConditionBuilder( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( MapRule.STRUCTURE_NAME,
				MapRule.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( );
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
		if ( DesignChoiceConstants.MAP_OPERATOR_ANY.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_FALSE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_TRUE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NULL.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_NULL.equals( operatorValue ) )
		{
			return 0;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_LT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_EQ.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GE.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_GT.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_LIKE.equals( operatorValue ) )
		{
			return 1;
		}
		else if ( DesignChoiceConstants.MAP_OPERATOR_BETWEEN.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_BETWEEN.equals( operatorValue ) )
		{
			return 2;
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
	static int getIndexForOperatorValue( String value )
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

	protected Button valBuilder1, valBuilder2;

	protected Text value1, value2;

	protected Label andLable;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "FilterConditionBuilder.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	/**
	 * Default constructor.
	 * 
	 * @param parentShell
	 *            Parent Shell
	 * @param title
	 *            Window Title
	 */
	public FilterConditionBuilder( Shell parentShell, String title,
			FilterHandleProvider provider )
	{
		super( parentShell, title );
	}

	private String[] getDataSetColumns( )
	{
		if ( columnList.isEmpty( ) )
		{
			return EMPTY;
		}
		String[] values = new String[columnList.size( )];
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			values[i] = ( (ComputedColumnHandle) columnList.get( i ) ).getName( );
		}
		return values;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent,
				IHelpContextIds.INSERT_EDIT_FILTER_CONDITION_DIALOG_ID );

		GridData gdata;
		GridLayout glayout;
		Composite contents = new Composite( parent, SWT.NONE );
		contents.setLayout( new GridLayout( ) );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		createTitleArea( contents );

		Composite composite = new Composite( contents, SWT.NONE );
		glayout = new GridLayout( );
		glayout.marginHeight = 0;
		glayout.marginWidth = 0;
		glayout.verticalSpacing = 0;
		composite.setLayout( glayout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		applyDialogFont( composite );
		initializeDialogUnits( composite );

		Composite innerParent = (Composite) createDialogArea( composite );
		createButtonBar( composite );

		createFilterConditionContent( innerParent );

		updateButtons( );

		return composite;
	}

	protected void createFilterConditionContent( Composite innerParent )
	{
		Label lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "FilterConditionBuilder.text.Condition" ) ); //$NON-NLS-1$

		Composite condition = new Composite( innerParent, SWT.NONE );
		condition.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		GridData gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.addListener( SWT.Selection, ComboModify );
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
		setExpressionButtonImage( expBuilder );
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
		operator.addSelectionListener( OpoertorSelection );

		ExpressionValue expressionValue1 = new ExpressionValue( condition,
				SWT.NONE,
				expression );
		value1 = expressionValue1.getValueText( );
		valBuilder1 = expressionValue1.getPopupButton( );

		createDummy( condition, 3 );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "FilterConditionBuilder.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );

		createDummy( condition, 3 );

		ExpressionValue expressionValue2 = new ExpressionValue( condition,
				SWT.NONE,
				expression );
		value2 = expressionValue2.getValueText( );
		valBuilder2 = expressionValue2.getPopupButton( );

		value2.setVisible( false );
		valBuilder2.setVisible( false );

		if ( operator.getItemCount( ) > 0 )
		{
			operator.select( 0 );
		}

		Composite space = new Composite( innerParent, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 15;
		space.setLayoutData( gdata );

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( inputHandle != null )
		{
			syncViewProperties( );
		}
	}

	protected SelectionListener OpoertorSelection = new SelectionListener( ) {

		public void widgetSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub
			String value = getValueForOperator( operator.getText( ) );

			int vv = determineValueVisible( value );

			if ( vv == 0 )
			{
				value1.setVisible( false );
				valBuilder1.setVisible( false );
				value2.setVisible( false );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 1 )
			{
				value1.setVisible( true );
				valBuilder1.setVisible( true );
				value2.setVisible( false );
				valBuilder2.setVisible( false );
				andLable.setVisible( false );
			}
			else if ( vv == 2 )
			{
				value1.setVisible( true );
				valBuilder1.setVisible( true );
				value2.setVisible( true );
				valBuilder2.setVisible( true );
				andLable.setVisible( true );
			}
			updateButtons( );
		}

		public void widgetDefaultSelected( SelectionEvent e )
		{
			// TODO Auto-generated method stub

		}
	};

	protected Listener ComboModify = new Listener( ) {

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

	private Object getResultSetColumn( String name )
	{
		if ( columnList.isEmpty( ) )
		{
			return null;
		}
		for ( int i = 0; i < columnList.size( ); i++ )
		{
			ComputedColumnHandle column = (ComputedColumnHandle) columnList.get( i );
			if ( column.getName( ).equals( name ) )
			{
				return column;
			}
		}
		return null;
	}

	private Composite createTitleArea( Composite parent )
	{
		int heightMargins = 3;
		int widthMargins = 8;
		final Composite titleArea = new Composite( parent, SWT.NONE );
		FormLayout layout = new FormLayout( );
		layout.marginHeight = heightMargins;
		layout.marginWidth = widthMargins;
		titleArea.setLayout( layout );

		Display display = parent.getDisplay( );
		Color background = JFaceColors.getBannerBackground( display );
		GridData layoutData = new GridData( GridData.FILL_HORIZONTAL );
		layoutData.heightHint = 20 + ( heightMargins * 2 );
		titleArea.setLayoutData( layoutData );
		titleArea.setBackground( background );

		titleArea.addPaintListener( new PaintListener( ) {

			public void paintControl( PaintEvent e )
			{
				e.gc.setForeground( titleArea.getDisplay( )
						.getSystemColor( SWT.COLOR_WIDGET_NORMAL_SHADOW ) );
				Rectangle bounds = titleArea.getClientArea( );
				bounds.height = bounds.height - 2;
				bounds.width = bounds.width - 1;
				e.gc.drawRectangle( bounds );
			}
		} );

		Label label = new Label( titleArea, SWT.NONE );
		label.setBackground( background );
		label.setFont( FontManager.getFont( label.getFont( ).toString( ),
				10,
				SWT.BOLD ) );
		label.setText( getTitle( ) ); //$NON-NLS-1$

		return titleArea;
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
		inilializeColumnList( this.designHandle );
	}

	private void inilializeColumnList( DesignElementHandle handle )
	{
		columnList = DEUtil.getVisiableColumnBindingsList( handle );
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
		getOkButton( ).setEnabled( isConditionOK( ) );
	}

	protected void enableInput( boolean val )
	{
		operator.setEnabled( val );
		value1.setEnabled( val );
		value2.setEnabled( val );
		valBuilder1.setEnabled( val );
		valBuilder2.setEnabled( val );
	}

	/**
	 * Gets if the expression field is not empty.
	 */
	private boolean isExpressionOK( )
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
	private boolean checkValues( )
	{
		if ( value1.getVisible( ) )
		{
			if ( value1.getText( ) == null
					|| value1.getText( ).trim( ).length( ) == 0 )
			{
				return false;
			}
		}

		if ( value2.getVisible( ) )
		{
			if ( value2.getText( ) == null
					|| value2.getText( ).trim( ).length( ) == 0 )
			{
				return false;
			}
		}

		return true;
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	protected void syncViewProperties( )
	{
		// expression.setText( DEUtil.resolveNull( provider.getTestExpression( )
		// ) );

		expression.setText( DEUtil.resolveNull( inputHandle.getExpr( ) ) );
		operator.select( getIndexForOperatorValue( inputHandle.getOperator( ) ) );
		value1.setText( DEUtil.resolveNull( inputHandle.getValue1( ) ) );
		value2.setText( DEUtil.resolveNull( inputHandle.getValue2( ) ) );
		int vv = determineValueVisible( inputHandle.getOperator( ) );

		if ( vv == 0 )
		{
			value1.setVisible( false );
			valBuilder1.setVisible( false );
			value2.setVisible( false );
			valBuilder2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( vv == 1 )
		{
			value1.setVisible( true );
			valBuilder1.setVisible( true );
			value2.setVisible( false );
			valBuilder2.setVisible( false );
			andLable.setVisible( false );
		}
		else if ( vv == 2 )
		{
			value1.setVisible( true );
			valBuilder1.setVisible( true );
			value2.setVisible( true );
			valBuilder2.setVisible( true );
			andLable.setVisible( true );
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
				filter.setProperty( FilterCondition.VALUE1_MEMBER,
						DEUtil.resolveNull( value1.getText( ) ) );
				filter.setProperty( FilterCondition.VALUE2_MEMBER,
						DEUtil.resolveNull( value2.getText( ) ) );

				// set test expression for new map rule
				filter.setExpr( DEUtil.resolveNull( expression.getText( ) ) );
				PropertyHandle propertyHandle = designHandle.getPropertyHandle( ListingHandle.FILTER_PROP );
				propertyHandle.addItem( filter );
			}
			else
			{
				inputHandle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );
				if ( value1.getVisible( ) )
				{
					inputHandle.setValue1( DEUtil.resolveNull( value1.getText( ) ) );
				}
				else
				{
					inputHandle.setValue1( "" );
				}

				if ( value2.getVisible( ) )
				{
					inputHandle.setValue2( DEUtil.resolveNull( value2.getText( ) ) );
				}
				else
				{
					inputHandle.setValue2( "" );
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
			expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
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

			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
					reportItem.getModuleHandle( ) ) );
			selectValueList.addAll( session.getColumnValueSet( reportItem.getDataSet( ),
					reportItem.paramBindingsIterator( ),
					reportItem.columnBindingsIterator( ),
					bindingName ) );
			session.shutdown( );
		}
		else
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return selectValueList;
	}

	private class ExpressionValue
	{

		Text valueText;
		Button btnPopup;

		Text getValueText( )
		{
			return valueText;
		}

		Button getPopupButton( )
		{
			return btnPopup;
		}

		ExpressionValue( Composite parent, int style, final Combo expressionText )
		{
			Composite composite = new Composite( parent, SWT.NONE );
			composite.setLayout( new ExpressionLayout( ) );
			GridData gdata = new GridData( GridData.END );
			gdata.widthHint = 120;
			gdata.heightHint = 20;
			composite.setLayoutData( gdata );
			// GridLayout layout = new GridLayout(2,false);
			// composite.setLayout(layout);
			valueText = createText( composite );
			valueText.addModifyListener( new ModifyListener( ) {

				public void modifyText( ModifyEvent e )
				{
					updateButtons( );
				}
			} );
			btnPopup = new Button( composite, SWT.ARROW | SWT.DOWN );
			btnPopup.addSelectionListener( new SelectionListener( ) {

				public void widgetSelected( SelectionEvent e )
				{
					refreshList( );
					Rectangle textBounds = valueText.getBounds( );
					Point pt = valueText.toDisplay( textBounds.x, textBounds.y );
					Rectangle rect = new Rectangle( pt.x,
							pt.y,
							valueText.getParent( ).getBounds( ).width,
							textBounds.height );

					PopupSelectionList popup = new PopupSelectionList( valueText.getParent( )
							.getShell( ) );
					popup.setItems( popupItems );
					String value = popup.open( rect );
					int selectionIndex = popup.getSelectionIndex( );

					bindingName = null;
					for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
					{
						String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );
						if ( DEUtil.getColumnExpression( columnName )
								.equals( expressionText.getText( ) ) )
						{
							bindingName = columnName;
							break;
						}
					}

					if ( value != null )
					{
						String newValue = null;
						if ( value.equals( ( actions[0] ) ) )
						{
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
										newValue = dialog.getSelectedExprValue( );
									}
								}
								catch ( Exception ex )
								{
									MessageDialog.openError( null,
											Messages.getString( "SelectValueDialog.selectValue" ),
											Messages.getString( "SelectValueDialog.messages.error.selectVauleUnavailable" )
													+ "\n"
													+ ex.getMessage( ) );
								}
							}
							else
							{
								MessageDialog.openInformation( null,
										Messages.getString( "SelectValueDialog.selectValue" ),
										Messages.getString( "SelectValueDialog.messages.info.selectVauleUnavailable" ) );
							}
						}
						else if ( value.equals( actions[1] ) )
						{
							ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
									.getDisplay( )
									.getActiveShell( ),
									valueText.getText( ) );

							dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );

							if ( dialog.open( ) == IDialogConstants.OK_ID )
							{
								newValue = dialog.getResult( );
							}
						}
						else if ( selectionIndex > 3 )
						{
							newValue = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
						}
						if ( newValue != null )
						{
							valueText.setText( newValue );
						}
					}
				}

				public void widgetDefaultSelected( SelectionEvent e )
				{
					// TODO Auto-generated method stub

				}

			} );

		}

		private class ExpressionLayout extends Layout
		{

			public void layout( Composite editor, boolean force )
			{
				Rectangle bounds = editor.getClientArea( );
				Point size = btnPopup.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
				valueText.setBounds( 0, 0, bounds.width - size.x, bounds.height );
				btnPopup.setBounds( bounds.width - size.x,
						0,
						size.x,
						bounds.height );
			}

			public Point computeSize( Composite editor, int wHint, int hHint,
					boolean force )
			{
				if ( wHint != SWT.DEFAULT && hHint != SWT.DEFAULT )
					return new Point( wHint, hHint );
				Point contentsSize = valueText.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
				Point buttonSize = btnPopup.computeSize( SWT.DEFAULT,
						SWT.DEFAULT,
						force );
				// Just return the button width to ensure the button is not
				// clipped
				// if the label is long.
				// The label will just use whatever extra width there is
				Point result = new Point( buttonSize.x,
						Math.max( contentsSize.y, buttonSize.y ) );
				return result;
			}
		}

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
}