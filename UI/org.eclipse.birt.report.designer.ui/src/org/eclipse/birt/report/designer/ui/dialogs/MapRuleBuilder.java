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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.SelectValueFetcher;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ResourceEditDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.util.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.dialogs.provider.MapHandleProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.AlphabeticallyComparator;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.FontManager;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MapRuleHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.MapRule;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceColors;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
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
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Widget;
import org.eclipse.ui.PlatformUI;

/**
 * Dialog for adding or editing map rule.
 */

public class MapRuleBuilder extends BaseDialog
{

	private final String NULL_STRING = null;
	protected IExpressionProvider expressionProvider;
	protected transient String bindingName = null;
	protected ReportElementHandle currentItem = null;

	protected String[] popupItems = null;

	protected static String[] EMPTY_ARRAY = new String[]{};

	public static final String DLG_TITLE_NEW = Messages.getString( "MapRuleBuilder.DialogTitle.New" ); //$NON-NLS-1$
	public static final String DLG_TITLE_EDIT = Messages.getString( "MapRuleBuilder.DialogTitle.Edit" ); //$NON-NLS-1$

	protected List compositeList = new ArrayList( );
	/**
	 * Usable operators for building map rule conditions.
	 */
	static final String[][] OPERATOR;

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( MapRule.STRUCTURE_NAME,
				MapRule.OPERATOR_MEMBER );
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

	protected List valueList = new ArrayList( );

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
		else if ( DesignChoiceConstants.MAP_OPERATOR_IN.equals( operatorValue )
				|| DesignChoiceConstants.MAP_OPERATOR_NOT_IN.equals( operatorValue ) )
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

	private MapRuleHandle handle;

	private MapHandleProvider provider;

	private int handleCount;

	protected Combo expression, operator;

	private Text display;

	// private ExpressionValue value1, value2;
	protected Composite valueListComposite;
	protected Combo addExpressionValue;
	protected Button addBtn, editBtn, delBtn, delAllBtn;
	protected Table table;
	protected TableViewer tableViewer;

	protected int valueVisible;

	private Combo expressionValue1, expressionValue2;

	private Label andLable;

	private Text resourceKeytext;

	private Button btnBrowse;

	private Button btnReset;

	protected DesignElementHandle designHandle;

	protected static final String VALUE_OF_THIS_DATA_ITEM = Messages.getString( "HighlightRuleBuilderDialog.choice.ValueOfThisDataItem" ); //$NON-NLS-1$

	protected static String[] actions = new String[]{
			Messages.getString( "ExpressionValueCellEditor.selectValueAction" ), //$NON-NLS-1$
			Messages.getString( "ExpressionValueCellEditor.buildExpressionAction" ), //$NON-NLS-1$
	};

	private ParamBindingHandle[] bindingParams = null;

	/**
	 * Default constructor.
	 * 
	 * @param parentShell
	 *            Parent Shell
	 * @param title
	 *            Window Title
	 */
	public MapRuleBuilder( Shell parentShell, String title,
			MapHandleProvider provider )
	{
		super( parentShell, title );

		this.provider = provider;
	}

	protected List columnList;

	/**
	 * Constant, represents empty String array.
	 */
	private static final String[] EMPTY = new String[0];

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

	protected Object getResultSetColumn( String name )
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

	protected SelectionListener expSelListener = new SelectionAdapter( ) {

		public void widgetSelected( SelectionEvent e )
		{
			if ( expression.getText( ).equals( VALUE_OF_THIS_DATA_ITEM )
					&& designHandle instanceof DataItemHandle )
			{
				expression.setText( DEUtil.getColumnExpression( ( (DataItemHandle) designHandle ).getResultSetColumn( ) ) );
			}
			else
			{
				String newValue = expression.getText( );
				String value = DEUtil.getExpression( getResultSetColumn( newValue ) );
				if ( value != null )
					newValue = value;
				expression.setText( newValue );
			}

			updateButtons( );
		}
	};

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#createContents(org.eclipse.swt.widgets.Composite)
	 */
	protected Control createContents( Composite parent )
	{
		UIUtil.bindHelp( parent, IHelpContextIds.INSERT_EDIT_MAP_RULE_DIALOG_ID );
		refreshList( );

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

		Label lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilderDialog.text.Condition" ) ); //$NON-NLS-1$

		Composite condition = new Composite( innerParent, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 180;
		condition.setLayoutData( gdata );
		glayout = new GridLayout( 4, false );
		condition.setLayout( glayout );

		expression = new Combo( condition, SWT.NONE );
		gdata = new GridData( );
		gdata.widthHint = 100;
		expression.setLayoutData( gdata );
		expression.setItems( getDataSetColumns( ) );
		fillExpression( expression );
		expression.addSelectionListener( expSelListener );
		expression.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				updateButtons( );
			}
		} );

		Button expBuilder = new Button( condition, SWT.PUSH );
		// expBuilder.setText( "..." ); //$NON-NLS-1$
		UIUtil.setExpressionButtonImage( expBuilder );
		// gdata = new GridData( );
		// gdata.heightHint = 20;
		// gdata.widthHint = 20;
		// expBuilder.setLayoutData( gdata );
		expBuilder.setToolTipText( Messages.getString( "HighlightRuleBuilderDialog.tooltip.ExpBuilder" ) ); //$NON-NLS-1$
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
		operator.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				update2ValueStatus( );
			}
		} );

		refreshList( );

		create2ValueComposite( condition );

		lb = new Label( innerParent, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilderDialog.text.Display" ) ); //$NON-NLS-1$

		Composite format = new Composite( innerParent, SWT.NONE );
		format.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		glayout = new GridLayout( );
		format.setLayout( glayout );

		display = new Text( format, SWT.BORDER );
		gdata = new GridData( );
		gdata.widthHint = 300;
		display.setLayoutData( gdata );

		Composite space = new Composite( innerParent, SWT.NONE );
		gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.heightHint = 20;
		space.setLayoutData( gdata );

		createResourceKeyArea( innerParent );

		lb = new Label( innerParent, SWT.SEPARATOR | SWT.HORIZONTAL );
		lb.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		if ( handle != null )
		{
			syncViewProperties( );
		}
		else
		{
			update2ValueStatus( );
		}

		updateButtons( );

		return composite;
	}

	protected int create2ValueComposite( Composite condition )
	{
		if ( expressionValue1 != null && !expressionValue1.isDisposed( ) )
		{
			return 0;
		}
		disposeComposites( );

		GridData gd = new GridData( GridData.END );
		gd.widthHint = 110;
		gd.heightHint = 20;
		expressionValue1 = new Combo( condition, SWT.NONE );
		expressionValue1.setLayoutData( gd );
		expressionValue1.setItems( popupItems );

		expressionValue1.addListener( SWT.Verify, expValueVerifyListener );
		expressionValue1.addListener( SWT.Modify, textModifyListener );
		expressionValue1.addListener( SWT.Selection, popBtnSelectionListener );
		compositeList.add( expressionValue1 );

		Composite dummy = createDummy( condition, 3 );
		compositeList.add( dummy );

		andLable = new Label( condition, SWT.NONE );
		andLable.setText( Messages.getString( "HighlightRuleBuilderDialog.text.AND" ) ); //$NON-NLS-1$
		andLable.setVisible( false );
		compositeList.add( andLable );

		dummy = createDummy( condition, 3 );
		compositeList.add( dummy );

		expressionValue2 = new Combo( condition, SWT.NONE );
		expressionValue2.setLayoutData( gd );
		expressionValue2.setItems( popupItems );
		compositeList.add( expressionValue2 );

		expressionValue2.addListener( SWT.Verify, expValueVerifyListener );
		expressionValue2.addListener( SWT.Modify, textModifyListener );
		expressionValue2.addListener( SWT.Selection, popBtnSelectionListener );
		expressionValue2.setVisible( false );

		if ( operator.getItemCount( ) > 0
				&& operator.getSelectionIndex( ) == -1 )
		{
			operator.select( 0 );
		}
		condition.getParent( ).layout( true, true );

		return 1;
	}

	private int createValueListComposite( Composite parent )
	{
		if ( valueListComposite != null && !valueListComposite.isDisposed( ) )
		{
			return 0;
		}
		disposeComposites( );

		valueListComposite = new Composite( parent, SWT.NONE );
		GridData gdata = new GridData( GridData.FILL_HORIZONTAL );
		gdata.horizontalSpan = 4;
		valueListComposite.setLayoutData( gdata );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 4;
		valueListComposite.setLayout( layout );

		compositeList.add( valueListComposite );

		Group group = new Group( valueListComposite, SWT.NONE );
		GridData data = new GridData( GridData.FILL_HORIZONTAL );
		data.heightHint = 118;
		data.horizontalSpan = 3;
		data.horizontalIndent = 0;
		data.horizontalAlignment = SWT.BEGINNING;
		data.grabExcessHorizontalSpace = true;
		group.setLayoutData( data );
		layout = new GridLayout( );
		layout.numColumns = 4;
		group.setLayout( layout );

		new Label( group, SWT.NONE ).setText( Messages.getString( "FilterConditionBuilder.label.value" ) );

		GridData expgd = new GridData( );
		expgd.widthHint = 100;

		addExpressionValue = new Combo( group, SWT.NONE );
		addExpressionValue.setLayoutData( expgd );

		addBtn = new Button( group, SWT.PUSH );
		addBtn.setText( Messages.getString( "FilterConditionBuilder.button.add" ) );
		addBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.add.tooltip" ) );
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
					addExpressionValue.setText( "" );
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
		data = new GridData( GridData.FILL_VERTICAL );
		data.horizontalSpan = 4;
		table.setLayoutData( data );

		table.setHeaderVisible( false );
		table.setLinesVisible( true );
		TableColumn column;
		int i;
		String[] columNames = new String[]{
			Messages.getString( "FilterConditionBuilder.list.item1" ),
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

			public void keyReleased( KeyEvent e )
			{
				// TODO Auto-generated method stub

			}

		} );
		table.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
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
							expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
						else
							expressionBuilder.setExpressionProvier( expressionProvider );
					}

					if ( expressionBuilder.open( ) == OK )
					{
						String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
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
		} );

		tableViewer = new TableViewer( table );
		tableViewer.setUseHashlookup( true );
		tableViewer.setColumnProperties( columNames );
		tableViewer.setLabelProvider( tableLableProvier );
		tableViewer.setContentProvider( tableContentProvider );

		Composite rightPart = new Composite( valueListComposite, SWT.NONE );
		data = new GridData( GridData.FILL_BOTH | GridData.VERTICAL_ALIGN_END );
		rightPart.setLayoutData( data );
		layout = new GridLayout( );
		layout.makeColumnsEqualWidth = true;
		rightPart.setLayout( layout );

		editBtn = new Button( rightPart, SWT.PUSH );
		editBtn.setText( Messages.getString( "FilterConditionBuilder.button.edit" ) );
		editBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.edit.tooltip" ) );
		setButtonLayoutData( editBtn );
		editBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
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
							expressionBuilder.setExpressionProvier( new ExpressionProvider( designHandle ) );
						else
							expressionBuilder.setExpressionProvier( expressionProvider );
					}

					if ( expressionBuilder.open( ) == OK )
					{
						String result = DEUtil.resolveNull( expressionBuilder.getResult( ) );
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

		} );

		delBtn = new Button( rightPart, SWT.PUSH );
		delBtn.setText( Messages.getString( "FilterConditionBuilder.button.delete" ) );
		delBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.delete.tooltip" ) );
		setButtonLayoutData( delBtn );
		delBtn.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub

			}

			public void widgetSelected( SelectionEvent e )
			{
				// TODO Auto-generated method stub
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

		} );

		delAllBtn = new Button( rightPart, SWT.PUSH );
		delAllBtn.setText( Messages.getString( "FilterConditionBuilder.button.deleteall" ) );
		delAllBtn.setToolTipText( Messages.getString( "FilterConditionBuilder.button.deleteall.tooltip" ) );
		setButtonLayoutData( delAllBtn );
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

		addExpressionValue.addListener( SWT.Verify, expValueVerifyListener );
		addExpressionValue.addListener( SWT.Selection, popBtnSelectionListener );
		addExpressionValue.setItems( popupItems );

		parent.getParent( ).layout( true, true );

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
			return "";
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

	protected void checkAddButtonStatus( )
	{
		if ( addExpressionValue != null && ( !addExpressionValue.isDisposed( ) ) )
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

	private void disposeComposites( )
	{
		if ( compositeList.size( ) > 0 )
		{
			int count = compositeList.size( );
			for ( int i = 0; i < count; i++ )
			{
				Object obj = compositeList.get( i );
				if ( obj != null
						&& obj instanceof Widget
						&& ( !( (Widget) obj ).isDisposed( ) ) )
				{
					( (Widget) obj ).dispose( );
				}
			}
		}
		compositeList.clear( );
	}

	private void update2ValueStatus( )
	{
		String value = getValueForOperator( operator.getText( ) );

		valueVisible = determineValueVisible( value );

		if ( valueVisible == 3 )
		{
			int ret = createValueListComposite( operator.getParent( ) );
			if ( ret != 0 )
			{
				if ( handle != null )
				{
					valueList = new ArrayList( handle.getValue1List( ) );
				}

				tableViewer.setInput( valueList );
			}

		}
		else
		{
			int ret = create2ValueComposite( operator.getParent( ) );
			if ( ret != 0 && handle != null )
			{
				expressionValue1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );
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
		}
		updateButtons( );
	}

	private Listener textModifyListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			updateButtons( );
		}
	};

	private Listener popBtnSelectionListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			Widget widget = event.widget;
			assert ( widget instanceof Combo );
			popBtnSelectionAction( (Combo) widget );
		}

	};

	protected Listener expValueVerifyListener = new Listener( ) {

		public void handleEvent( Event event )
		{
			// TODO Auto-generated method stub
			Combo thisCombo = (Combo) event.widget;
			String text = event.text;
			if ( text != null && thisCombo.indexOf( text ) >= 0 )
			{
				event.doit = false;
			}
			else
			{
				event.doit = true;
			}
		}
	};

	private void refreshList( )
	{
		ArrayList finalItems = new ArrayList( 10 );
		for ( int n = 0; n < actions.length; n++ )
		{
			finalItems.add( actions[n] );
		}

		popupItems = (String[]) finalItems.toArray( EMPTY_ARRAY );
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

	protected void popBtnSelectionAction( Combo comboWidget )
	{
		// comboWidget.setItems( popupItems );

		int selectionIndex = comboWidget.getSelectionIndex( );
		if ( selectionIndex < 0 )
		{
			return;
		}
		String value = comboWidget.getItem( selectionIndex );
		
		boolean isAddClick = false;
		if ( tableViewer != null
				&& ( addBtn != null && ( !addBtn.isDisposed( ) ) ) )
		{
			isAddClick = true;
		}
		

		for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
		{
			String columnName = ( (ComputedColumnHandle) ( iter.next( ) ) ).getName( );
			if ( DEUtil.getColumnExpression( columnName )
					.equals( expression.getText( ) ) )
			{
				bindingName = columnName;
				break;
			}
		}

		boolean returnValue = false;
		if ( value != null )
		{
			String newValues[] = new String[1];
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
						if(isAddClick)
						{
							dialog.setMultipleSelection(true);
						}
						if ( dialog.open( ) == IDialogConstants.OK_ID )
						{
							returnValue = true;
							newValues = dialog.getSelectedExprValues( );
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
				else if ( designHandle instanceof TabularCubeHandle )
				{
					DataSetHandle dataSet = ( (TabularCubeHandle) designHandle ).getDataSet( );
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
							returnValue = true;
							newValues = dialog.getSelectedExprValues( );
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
			}
			else if ( value.equals( actions[1] ) )
			{
				ExpressionBuilder dialog = new ExpressionBuilder( PlatformUI.getWorkbench( )
						.getDisplay( )
						.getActiveShell( ),
						comboWidget.getText( ) );

				if ( expressionProvider == null )
					dialog.setExpressionProvier( new ExpressionProvider( designHandle ) );
				else
					dialog.setExpressionProvier( expressionProvider );

				if ( dialog.open( ) == IDialogConstants.OK_ID )
				{
					returnValue = true;
					newValues[0] = dialog.getResult( );
				}
			}
			else if ( selectionIndex > 3 )
			{
				newValues[0] = "params[\"" + value + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
			}
			if ( returnValue )
			{
				if(addExpressionValue == comboWidget)
				{
					comboWidget.setText("");
					addBtn.setEnabled( false );
				}
				
				if ( isAddClick )
				{
					boolean change = false;
					for(int i = 0; i < newValues.length; i ++)
					{
						if ( valueList.indexOf( DEUtil.resolveNull( newValues[i] ) ) < 0 )
						{
							valueList.add(  DEUtil.resolveNull( newValues[i] ) );
							change = true;
						}					
					}
					if(change)
					{
						tableViewer.refresh( );
						updateButtons( );
						addExpressionValue.setFocus( );
					}

				}

			}
		}
	}

	private Composite createResourceKeyArea( Composite parent )
	{
		Composite resourceKeyArea = new Composite( parent, SWT.NONE );
		resourceKeyArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		GridLayout glayout = new GridLayout( 4, false );
		resourceKeyArea.setLayout( glayout );

		GridData gd = new GridData( GridData.FILL_HORIZONTAL );
		resourceKeyArea.setLayoutData( gd );

		Label lb = new Label( resourceKeyArea, SWT.NONE );
		lb.setText( Messages.getString( "MapRuleBuilder.Button.ResourceKey" ) ); //$NON-NLS-1$
		resourceKeytext = new Text( resourceKeyArea, SWT.BORDER | SWT.READ_ONLY );
		resourceKeytext.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		btnBrowse = new Button( resourceKeyArea, SWT.PUSH );
		btnBrowse.setLayoutData( new GridData( ) );
		btnBrowse.setText( "..." ); //$NON-NLS-1$

		btnBrowse.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleBrowserSelectedEvent( );
			}
		} );

		btnReset = new Button( resourceKeyArea, SWT.PUSH );
		btnReset.setLayoutData( new GridData( ) );
		btnReset.setText( Messages.getString( "MapRuleBuilder.Button.Reset" ) ); //$NON-NLS-1$

		btnReset.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleSelectedEvent( null );
			};

		} );
		checkResourceKey( );

		Label noteLabel = new Label( parent, SWT.NONE | SWT.WRAP );
		gd = new GridData( GridData.FILL_HORIZONTAL );
		gd.horizontalSpan = 4;
		gd.widthHint = 350;
		noteLabel.setLayoutData( gd );
		noteLabel.setText( Messages.getString( "I18nPage.text.Note" ) ); //$NON-NLS-1$
		return resourceKeyArea;
	}

	protected void handleBrowserSelectedEvent( )
	{
		ResourceEditDialog dlg = new ResourceEditDialog( btnBrowse.getShell( ),
				Messages.getString( "ResourceKeyDescriptor.title.SelectKey" ) ); //$NON-NLS-1$

		dlg.setResourceURL( provider.getResourceURL( ) );

		if ( dlg.open( ) == Window.OK )
		{
			handleSelectedEvent( (String) dlg.getResult( ) );
		}
	}

	private void handleSelectedEvent( String newValue )
	{
		if ( "".equals( newValue ) )//$NON-NLS-1$
		{
			newValue = null;
		}

		resourceKeytext.setText( DEUtil.resolveNull( newValue ) );

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
		label.setText( getTitle( ) );

		return titleArea;
	}

	private Composite createDummy( Composite parent, int colSpan )
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
	public void updateHandle( MapRuleHandle handle, int handleCount )
	{
		this.handle = handle;
		this.handleCount = handleCount;
	}

	/*
	 * Set design handle for the Map Rule builder
	 */
	public void setDesignHandle( DesignElementHandle handle )
	{
		this.designHandle = handle;
		initializeProviderType( );
		inilializeColumnList( handle );
		initializeParamterBinding( handle );
	}

	public void setReportElement( ReportElementHandle reportItem )
	{
		currentItem = reportItem;
	}

	protected void inilializeColumnList( DesignElementHandle handle )
	{
		columnList = DEUtil.getVisiableColumnBindingsList( handle );
	}

	private void initializeParamterBinding( DesignElementHandle handle )
	{
		if ( handle instanceof ReportItemHandle )
		{
			ReportItemHandle inputHandle = (ReportItemHandle) handle;
			List list = new ArrayList( );
			for ( Iterator iterator = inputHandle.paramBindingsIterator( ); iterator.hasNext( ); )
			{
				ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterator.next( );
				list.add( paramBindingHandle );
			}
			bindingParams = new ParamBindingHandle[list.size( )];
			list.toArray( bindingParams );
		}
	}

	/*
	 * Return the hanle of Map Rule builder
	 */
	public MapRuleHandle getHandle( )
	{
		return handle;
	}

	private void fillExpression( Combo control )
	{
		String te = "";//$NON-NLS-1$

		if ( handle != null )
		{
			te = handle.getTestExpression( );
		}

		// String te = provider.getTestExpression( );

		if ( ( designHandle instanceof DataItemHandle )
				&& ( ( (DataItemHandle) designHandle ).getResultSetColumn( ) != null ) )
		{
			control.add( VALUE_OF_THIS_DATA_ITEM );
		}

		if ( control.getItemCount( ) == 0 )
		{
			control.add( DEUtil.resolveNull( null ) );
			control.select( control.getItemCount( ) - 1 );
		}

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

	private void enableInput( boolean val )
	{
		operator.setEnabled( val );
		if ( valueVisible != 3 )
		{
			if ( expressionValue1 != null && ( !expressionValue1.isDisposed( ) ) )
				expressionValue1.setEnabled( val );
			if ( expressionValue2 != null && ( !expressionValue2.isDisposed( ) ) )
				expressionValue2.setEnabled( val );
			if ( andLable != null && ( !andLable.isDisposed( ) ) )
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
			}
		}

		display.setEnabled( val );
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
	private boolean isConditionOK( )
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
			if ( expressionValue1.getVisible( ) )
			{
				if ( expressionValue1.getText( ) == null
						|| expressionValue1.getText( ).length( ) == 0 )
				{
					return false;
				}
			}

			if ( expressionValue2.getVisible( ) )
			{
				if ( expressionValue2.getText( ) == null
						|| expressionValue2.getText( ).length( ) == 0 )
				{
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * SYNC the control value according to the handle.
	 */
	private void syncViewProperties( )
	{
		// expression.setText( DEUtil.resolveNull( provider.getTestExpression( )
		// ) );

		expression.setText( DEUtil.resolveNull( handle.getTestExpression( ) ) );
		operator.select( getIndexForOperatorValue( handle.getOperator( ) ) );

		String value = getValueForOperator( operator.getText( ) );
		valueVisible = determineValueVisible( value );

		if ( valueVisible == 3 )
		{
			createValueListComposite( operator.getParent( ) );
			if ( handle != null )
			{
				valueList = new ArrayList( handle.getValue1List( ) );
			}

			tableViewer.setInput( valueList );
		}
		else
		{
			create2ValueComposite( operator.getParent( ) );
			if ( handle != null )
			{
				expressionValue1.setText( DEUtil.resolveNull( handle.getValue1( ) ) );
				expressionValue2.setText( DEUtil.resolveNull( handle.getValue2( ) ) );
			}

		}

		valueVisible = determineValueVisible( handle.getOperator( ) );

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

		display.setText( DEUtil.resolveNull( handle.getDisplay( ) ) );
		resourceKeytext.setText( DEUtil.resolveNull( handle.getDisplayKey( ) ) );
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
			// provider.setTestExpression( DEUtil.resolveNull(
			// expression.getText( ) ) );

			valueVisible = determineValueVisible( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

			if ( handle == null )
			{
				MapRule rule = StructureFactory.createMapRule( );

				rule.setProperty( MapRule.OPERATOR_MEMBER,
						DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				if ( valueVisible == 3 )
				{
					rule.setValue1( valueList );
					rule.setValue2( "" );
				}
				else
				{
					if ( expressionValue1.isVisible( ) )
					{
						rule.setProperty( MapRule.VALUE1_MEMBER,
								DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					if ( expressionValue2.isVisible( ) )
					{
						rule.setProperty( MapRule.VALUE2_MEMBER,
								DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
				}

				rule.setProperty( MapRule.DISPLAY_MEMBER,
						DEUtil.resolveNull( display.getText( ) ) );

				// set test expression for new map rule
				rule.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );

				handle = provider.doAddItem( rule, handleCount );
			}
			else
			{
				handle.setOperator( DEUtil.resolveNull( getValueForOperator( operator.getText( ) ) ) );

				if ( valueVisible != 3 )
				{
					if ( expressionValue1.isVisible( ) )
					{
						handle.setValue1( DEUtil.resolveNull( expressionValue1.getText( ) ) );
					}
					else
					{
						handle.setValue1( NULL_STRING );
					}
					if ( expressionValue2.isVisible( ) )
					{
						handle.setValue2( DEUtil.resolveNull( expressionValue2.getText( ) ) );
					}
					else
					{
						handle.setValue2( NULL_STRING );
					}
				}
				else
				{
					handle.setValue1( valueList );
					handle.setValue2( "" );
				}

				handle.setDisplay( DEUtil.resolveNull( display.getText( ) ) );
				handle.setDisplayKey( DEUtil.resolveNull( resourceKeytext.getText( ) ) );
				handle.setTestExpression( DEUtil.resolveNull( expression.getText( ) ) );
			}
		}
		catch ( Exception e )
		{
			WidgetUtil.processError( getShell( ), e );
		}

		super.okPressed( );
	}

	private void editValue( Control control )
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
			ExpressionProvider expressionProvider = new ExpressionProvider( designHandle );
			expressionProvider.addFilter( new ExpressionFilter( ) {

				public boolean select( Object parentElement, Object element )
				{
					if ( ExpressionFilter.CATEGORY.equals( parentElement )
							&& ExpressionProvider.CURRENT_CUBE.equals( element ) )
					{
						return false;
					}
					return true;
				}

			} );
			expressionBuilder.setExpressionProvier( expressionProvider );
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

	private void checkResourceKey( )
	{
		checkResourceKey( null );
	}

	private void checkResourceKey( MapRuleHandle handle )
	{
		resourceKeytext.setEnabled( true );
		btnBrowse.setEnabled( true );
		btnReset.setEnabled( true );

		String baseName = provider.getBaseName( );
		if ( baseName == null )
		{
			btnBrowse.setEnabled( false );
		}
		else
		{
			URL resource = provider.getResourceURL( );
			String path = null;
			try
			{
				if ( resource != null )
				{
					path = FileLocator.resolve( resource ).getFile( );
				}

			}
			catch ( IOException e )
			{
				logger.log( Level.SEVERE, e.getMessage( ), e );
			}
			if ( resource == null || path == null || !new File( path ).exists( ) )
			{
				btnBrowse.setEnabled( false );
			}
			else
			{
				btnBrowse.setEnabled( true );
			}
		}
		if ( handle != null )
		{
			resourceKeytext.setText( DEUtil.resolveNull( handle.getDisplayKey( ) ) );
		}

	}

	protected String getExpression( String resultSet )
	{
		if ( provider.getExpressionType( ) == MapHandleProvider.EXPRESSION_TYPE_ROW )
		{
			return DEUtil.getColumnExpression( resultSet );
		}
		else if ( provider.getExpressionType( ) == MapHandleProvider.EXPRESSION_TYPE_DATA )
		{
			return DEUtil.getDataExpression( resultSet );
		}

		return null;

	}

	private void initializeProviderType( )
	{
		if ( designHandle instanceof DataItemHandle )
		{
			DataItemHandle dataItem = (DataItemHandle) designHandle;
			if ( dataItem.getContainer( ) instanceof ExtendedItemHandle )
			{
				provider.setExpressionType( MapHandleProvider.EXPRESSION_TYPE_DATA );
			}
			else
			{
				provider.setExpressionType( MapHandleProvider.EXPRESSION_TYPE_ROW );
			}
		}
	}
}