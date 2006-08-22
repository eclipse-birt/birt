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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ComputedColumnExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.CCombo;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.page.WidgetUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * The dialog to select and edit column bindings
 */

public class ColumnBindingDialog extends BaseDialog
{

	private static final String MSG_ADD = Messages.getString( "ColumnBindingDialog.Text.Add" );

	private static final String MSG_EDIT = Messages.getString( "ColumnBindingDialog.Text.Edit" );

	private static final String MSG_DELETE = Messages.getString( "ColumnBindingDialog.Text.Del" );

	private static final String DEFAULT_COLUMN_NAME = "[result_set_col_name]"; //$NON-NLS-1$

	private static final String dummyChoice = "dummy"; //$NON-NLS-1$

	public static final String DEFAULT_DLG_TITLE = Messages.getString( "ColumnBindingDialog.DialogTitle" ); //$NON-NLS-1$

	private static final String ALL = Messages.getString( "ColumnBindingDialog.All" );//$NON-NLS-1$

	private static final String NONE_AGGREGATEON = Messages.getString( "ColumnBindingDialog.AGGREGATEON.NONE" );//$NON-NLS-1$

	private static final String CHOICE_FROM_CONTAINER = Messages.getString( "ColumnBindingDialog.Choice.FromContainer" );//$NON-NLS-1$

	private static final String CHOICE_NONE = Messages.getString( "ColumnBindingDialog.NONE" );//$NON-NLS-1$

	private static final String LABEL_COLUMN_BINDINGS = Messages.getString( "ColumnBindingDialog.Label.DataSet" ); //$NON-NLS-1$

	private static final String WARN_COLUMN_BINDINGS = Messages.getString( "ColumnBingingDialog.Label.Warn" ); //$NON-NLS-1$

	// private static final String BUTTON_GENERATE = Messages.getString(
	// "ColumnBindingDialog.Button.Generate" ); //$NON-NLS-1$
	private static final String COLUMN_NAME = Messages.getString( "ColumnBindingDialog.Column.Name" ); //$NON-NLS-1$

	private static final String COLUMN_DATATYPE = Messages.getString( "ColumnBindingDialog.Column.DataType" ); //$NON-NLS-1$

	private static final String COLUMN_EXPRESSION = Messages.getString( "ColumnBindingDialog.Column.Expression" ); //$NON-NLS-1$

	private static final String COLUMN_AGGREGATEON = Messages.getString( "ColumnBindingDialog.Column.AggregateOn" ); //$NON-NLS-1$

	private static final String INPUT_PROPMT = Messages.getString( "ColumnBindingDialog.InputPrompt" ); //$NON-NLS-1$

	private static final IChoiceSet dataTypeChoiceSet = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE );

	private static final IChoice[] dataTypes = dataTypeChoiceSet.getChoices( null );

	private static final String[] dataTypeDisplayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet( dataTypeChoiceSet );

	private boolean canSelect = false;

	protected ReportItemHandle inputElement;

	// private List bindingList;

	// private Button generateButton;
	private TableViewer bindingTable;

	private ExpressionCellEditor expressionCellEditor;

	private String selectedColumnName = null;

	private String NullChoice = null;

	private int selectIndex;

	private IStructuredContentProvider contentProvider = new IStructuredContentProvider( ) {

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			List elementsList = getBindingList( (DesignElementHandle) inputElement );
			// elementsList.add( dummyChoice );
			return elementsList.toArray( );
		}
	};

	private ITableLabelProvider labelProvider = new ITableLabelProvider( ) {

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			if ( element == dummyChoice )
			{
				if ( columnIndex == 1 )
				{
					return INPUT_PROPMT;
				}
				return ""; //$NON-NLS-1$
			}
			ComputedColumnHandle handle = ( (ComputedColumnHandle) element );
			String text = null;

			switch ( columnIndex )
			{
				case 1 :
					text = handle.getName( );
					break;
				case 2 :
					text = ChoiceSetFactory.getDisplayNameFromChoiceSet( handle.getDataType( ),
							dataTypeChoiceSet );
					break;
				case 3 :
					text = handle.getExpression( );
					break;
				case 4 :
					String value = handle.getAggregrateOn( );
					String groupType = DEUtil.getGroupControlType( inputElement );
					if ( value == null )
					{
						if ( ExpressionUtil.hasAggregation( handle.getExpression( ) )
								&& groupType != DEUtil.TYPE_GROUP_NONE )
						{
							text = ALL;
						}
						else
							text = NONE_AGGREGATEON;
					}
					else
					{
						if ( groupType == DEUtil.TYPE_GROUP_NONE )
						{
							text = NONE_AGGREGATEON;
							handle.setAggregateOn( null );
						}
						else
							text = value;
					}

					break;
			}

			if ( text == null )
			{
				text = ""; //$NON-NLS-1$
			}
			return text;
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

	private String highLightName = null;

	private ICellModifier cellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( property == null
					|| ( element == dummyChoice && !COLUMN_NAME.equals( property ) ) )
			{
				return false;
			}
			if ( COLUMN_AGGREGATEON.equals( property ) )
			{
				ComputedColumnHandle handle = ( (ComputedColumnHandle) element );
				if ( !ExpressionUtil.hasAggregation( handle.getExpression( ) )
						|| DEUtil.getGroupControlType( inputElement )
								.equals( DEUtil.TYPE_GROUP_NONE ) )
				{
					return false;
				}
			}

			return true;
		}

		public Object getValue( Object element, String property )
		{
			if ( element == dummyChoice )
			{
				ComputedColumn column = StructureFactory.newComputedColumn( inputElement,
						DEFAULT_COLUMN_NAME );
				column.setExpression( "" ); //$NON-NLS-1$
				String groupType = DEUtil.getGroupControlType( inputElement );
				if ( ExpressionUtil.hasAggregation( column.getExpression( ) ) )
				{
					if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
						column.setAggregrateOn( ( (GroupHandle) DEUtil.getGroups( inputElement )
								.get( 0 ) ).getName( ) );
					else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
						column.setAggregrateOn( null );
				}
				if ( !ExpressionUtil.hasAggregation( column.getExpression( ) )
						|| groupType.equals( DEUtil.TYPE_GROUP_NONE ) )
				{
					column.setAggregrateOn( null );
				}
				addBinding( column );
				highLightName = column.getName( );
				return ""; //$NON-NLS-1$
			}
			ComputedColumnHandle handle = ( (ComputedColumnHandle) element );
			String value = null;
			if ( COLUMN_NAME.equals( property ) )
			{
				value = handle.getName( );
			}
			else if ( COLUMN_DATATYPE.equals( property ) )
			{
				IChoice type = dataTypeChoiceSet.findChoice( handle.getDataType( ) );
				if ( type != null )
				{
					for ( int i = 0; i < dataTypeDisplayNames.length; i++ )
					{
						if ( dataTypeDisplayNames[i].equals( type.getDisplayName( ) ) )
						{
							return new Integer( i );
						}
					}
				}
				return new Integer( 0 );
			}
			else if ( COLUMN_EXPRESSION.equals( property ) )
			{
				value = handle.getExpression( );
			}
			else if ( COLUMN_AGGREGATEON.equals( property ) )
			{
				value = handle.getAggregrateOn( );;
				if ( value != null )
				{
					for ( int i = 1; i < groups.length; i++ )
					{
						if ( value.equals( groups[i] ) )
						{
							return new Integer( i );
						}
					}
				}
				return new Integer( 0 );
			}
			if ( value == null )
			{
				value = ""; //$NON-NLS-1$
			}
			return value;
		}

		public void modify( Object element, String property, Object value )
		{
			if ( element instanceof Item )
			{
				element = ( (Item) element ).getData( );
			}
			try
			{
				if ( COLUMN_NAME.equals( property ) )
				{
					String newName = UIUtil.convertToModelString( (String) value,
							true );
					if ( element == dummyChoice )
					{
						if ( newName == null )
						{
							return;
						}

						// ComputedColumn column =
						// StructureFactory.createComputedColumn( );
						// column.setName( newName );
						// column.setExpression( "" ); //$NON-NLS-1$
						// addBinding( column );
					}
					else
					{
						ComputedColumnHandle columnHandle = (ComputedColumnHandle) element;
						boolean selectedNameChanged = false;
						if ( columnHandle.getName( ) != null
								&& columnHandle.getName( )
										.equals( selectedColumnName ) )
						{
							selectedNameChanged = true;
						}
						if ( highLightName != null
								&& highLightName.equals( ( (ComputedColumnHandle) element ).getName( ) )
								&& !highLightName.equals( newName ) )
						{
							bindingTable.getTable( )
									.getItem( bindingTable.getTable( )
											.getSelectionIndex( ) )
									.setForeground( 1,
											Display.getDefault( )
													.getSystemColor( SWT.COLOR_LIST_FOREGROUND ) );
						}
						if ( !( columnHandle.getName( ) != null && columnHandle.getName( )
								.equals( newName ) ) )
							( (ComputedColumnHandle) element ).setName( newName );
						if ( selectedNameChanged )
						{
							selectedColumnName = newName;
						}
					}
				}
				else
				{
					ComputedColumnHandle bindingHandle = ( (ComputedColumnHandle) element );
					if ( COLUMN_DATATYPE.equals( property ) )
					{
						bindingHandle.setDataType( dataTypes[( (Integer) value ).intValue( )].getName( ) );
					}
					else if ( COLUMN_EXPRESSION.equals( property ) )
					{
						if ( !( bindingHandle.getExpression( ) != null && bindingHandle.getExpression( )
								.equals( (String) value ) ) )
						{
							bindingHandle.setExpression( (String) value );
							String groupType = DEUtil.getGroupControlType( inputElement );
							if ( ExpressionUtil.hasAggregation( bindingHandle.getExpression( ) ) )
							{
								if ( groupType.equals( DEUtil.TYPE_GROUP_GROUP ) )
									bindingHandle.setAggregrateOn( ( (GroupHandle) DEUtil.getGroups( inputElement )
											.get( 0 ) ).getName( ) );
								else if ( groupType.equals( DEUtil.TYPE_GROUP_LISTING ) )
									bindingHandle.setAggregrateOn( null );
							}
							if ( !ExpressionUtil.hasAggregation( bindingHandle.getExpression( ) )
									|| groupType.equals( DEUtil.TYPE_GROUP_NONE ) )
							{
								bindingHandle.setAggregrateOn( null );
							}
						}
					}
					else if ( COLUMN_AGGREGATEON.equals( property ) )
					{
						if ( ( (Integer) value ).intValue( ) == 0 )
							bindingHandle.setAggregrateOn( null );
						else
							bindingHandle.setAggregrateOn( groups[( (Integer) value ).intValue( )] );
					}
				}
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
			refreshBindingTable( );
		}
	};

	public ColumnBindingDialog( )
	{
		super( DEFAULT_DLG_TITLE );
	}

	public ColumnBindingDialog( String title )
	{
		super( title );
	}

	public ColumnBindingDialog( boolean canSelect )
	{
		super( DEFAULT_DLG_TITLE );
		this.canSelect = canSelect;
	}

	public ColumnBindingDialog( Shell parent, boolean canSelect )
	{
		this( parent, DEFAULT_DLG_TITLE, canSelect );
	}

	public ColumnBindingDialog( Shell parent, String title, boolean canSelect )
	{
		super( parent, title );
		this.canSelect = canSelect;
	}

	/*
	 * Set input for dialog
	 */
	public void setInput( ReportItemHandle input )
	{
		Assert.isNotNull( input );
		this.inputElement = input;
		ReportItemHandle container = DEUtil.getBindingHolder( input.getContainer( ) );
		if ( container != null
				&& ( container.getDataSet( ) != null || container.columnBindingsIterator( )
						.hasNext( ) ) )
		{
			NullChoice = CHOICE_FROM_CONTAINER;
		}
		else
		{
			NullChoice = CHOICE_NONE;
		}
	}

	protected Control createDialogArea( Composite parent )
	{

		UIUtil.bindHelp( parent, IHelpContextIds.COLUMNBINDING_DIALOG_ID );
		Composite parentComposite = (Composite) super.createDialogArea( parent );

		if ( this.canSelect )
		{
			/**
			 * Label & button
			 */
			Composite composite = new Composite( parentComposite, SWT.NONE );
			composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			composite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

			CLabel warnLabel = new CLabel( composite, SWT.NONE );
			warnLabel.setImage( PlatformUI.getWorkbench( )
					.getSharedImages( )
					.getImage( ISharedImages.IMG_OBJS_WARN_TSK ) );
			warnLabel.setText( WARN_COLUMN_BINDINGS );
			GridData gd = new GridData( GridData.FILL_HORIZONTAL );
			gd.horizontalSpan = 2;
			warnLabel.setLayoutData( gd );

			Label label = new Label( composite, SWT.NONE );
			label.setText( LABEL_COLUMN_BINDINGS );
			label.setLayoutData( new GridData( GridData.BEGINNING ) );

			// add data set combo selection.
			final CCombo combo = new CCombo( composite, SWT.READ_ONLY
					| SWT.BORDER );
			combo.setBackground( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getSystemColor( SWT.COLOR_LIST_BACKGROUND ) );
			String[] dataSets = ChoiceSetFactory.getDataSets( );
			String[] newList = new String[dataSets.length + 1];
			newList[0] = NullChoice;
			System.arraycopy( dataSets, 0, newList, 1, dataSets.length );
			combo.setItems( newList );
			String dataSetName = getDataSetName( );
			combo.deselectAll( );

			if ( dataSetName != null )
			{
				combo.setText( dataSetName );
			}
			else
			{
				combo.select( 0 );
			}
			combo.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
			gd = new GridData( );
			gd.widthHint = 250;
			combo.setLayoutData( gd );
			combo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent event )
				{
					String value = null;
					if ( combo.getSelectionIndex( ) != 0 )
					{
						value = combo.getText( );
					}
					int rCode = canChangeDataSet( value );
					if ( rCode == 2 )
					{
						String newName = getDataSetName( );
						if ( newName != null )
						{
							combo.setText( newName );
						}
						else
						{
							combo.select( 0 );
						}
					}
					else
					{
						try
						{
							DataSetHandle dataSet = null;
							if ( value != null )
							{
								dataSet = inputElement.getModuleHandle( )
										.findDataSet( value );
							}
							inputElement.setDataSet( dataSet );
							getParameterBindingPropertyHandle( ).clearValue( );
							if ( rCode == 0 )
							{
								inputElement.getColumnBindings( ).clearValue( );
							}
							generateBindingColumns( );
							setHihtLightColumn( );
						}
						catch ( SemanticException e )
						{
							ExceptionHandler.handle( e );
						}
					}
				}
			} );
		}
		Composite contentComposite = new Composite( parentComposite, SWT.NONE );
		contentComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contentComposite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2,
				false ) );
		/**
		 * Binding table
		 */
		final Table table = new Table( contentComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
				| ( canSelect ? SWT.CHECK : 0 ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 200;
		gd.verticalSpan = 3;
		table.setLayoutData( gd );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		// table.addKeyListener( new KeyAdapter( ) {
		//
		// /**
		// * @see
		// org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
		// */
		// public void keyReleased( KeyEvent e )
		// {
		// // If Delete pressed, delete the selected row
		// if ( e.keyCode == SWT.DEL )
		// {
		// IStructuredSelection selection = (IStructuredSelection)
		// bindingTable.getSelection( );
		// if ( selection.getFirstElement( ) instanceof ComputedColumnHandle )
		// {
		// deleteRow( (ComputedColumnHandle) selection.getFirstElement( ) );
		// }
		// }
		// }
		// } );

		table.addKeyListener( new KeyAdapter( ) {

			public void keyPressed( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					int itemCount = table.getItemCount( );
					if ( selectIndex == itemCount )
					{
						return;
					}
					if ( selectIndex == itemCount - 1 )
					{
						selectIndex--;
					}
					try
					{
						handleDelEvent( );
					}
					catch ( Exception e1 )
					{
						WidgetUtil.processError( getShell( ), e1 );
					}
					refreshBindingTable( );
				}
			}
		} );

		table.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				selectIndex = table.getSelectionIndex( );
				updateButtons( );
			}
		} );

		String[] columns = null;
		int[] columnWidth = null;
		CellEditor[] cellEditors;

		expressionCellEditor = new ExpressionCellEditor( table );
		columns = new String[]{
				null,
				COLUMN_NAME,
				COLUMN_DATATYPE,
				COLUMN_EXPRESSION,
				COLUMN_AGGREGATEON
		};
		columnWidth = new int[]{
				canSelect ? 25 : 20, 150, 70, 150, 150,
		};

		groups = new String[groupList.size( ) + 1];
		groups[0] = ALL;
		for ( int i = 0; i < groupList.size( ); i++ )
		{
			groups[i + 1] = ( (GroupHandle) groupList.get( i ) ).getName( );
		}

		cellEditors = new CellEditor[]{
				null,
				new TextCellEditor( table ),
				new ComboBoxCellEditor( table, dataTypeDisplayNames ),
				expressionCellEditor,
				new ComboBoxCellEditor( table, groups, SWT.READ_ONLY ),
		};
		for ( int i = 0; i < columns.length; i++ )
		{
			TableColumn column = new TableColumn( table, SWT.LEFT );
			column.setResizable( columns[i] != null );
			if ( columns[i] != null )
			{
				column.setText( columns[i] );
			}
			column.setWidth( columnWidth[i] );
		}

		if ( canSelect )
		{
			bindingTable = new CheckboxTableViewer( table );
			( (CheckboxTableViewer) bindingTable ).addCheckStateListener( new ICheckStateListener( ) {

				public void checkStateChanged( CheckStateChangedEvent event )
				{

					if ( event.getElement( ) instanceof ComputedColumnHandle )
					{
						ComputedColumnHandle handle = (ComputedColumnHandle) event.getElement( );
						if ( handle.getName( ).equals( selectedColumnName ) )
						{
							selectedColumnName = null;
						}
						else
						{
							selectedColumnName = handle.getName( );
						}
						updateSelection( );
						updateButtons( );
					}
					else
					{
						( (CheckboxTableViewer) bindingTable ).setChecked( dummyChoice,
								false );
					}
				}
			} );
		}
		else
		{
			bindingTable = new TableViewer( table );
		}
		bindingTable.setCellEditors( cellEditors );
		bindingTable.setColumnProperties( columns );
		bindingTable.setContentProvider( contentProvider );
		bindingTable.setLabelProvider( labelProvider );
		// bindingTable.setCellModifier( cellModifier );
		bindingTable.setInput( inputElement );

		bindingTable.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				if ( !bindingTable.getSelection( ).isEmpty( ) )
				{
					Object obj = ( (IStructuredSelection) bindingTable.getSelection( ) ).getFirstElement( );
					if ( obj == dummyChoice && !existHighLightColumn( ) )
					{
						bindingTable.refresh( );
						setHihtLightColumn( );
					}
					else if ( obj instanceof ComputedColumnHandle )
					{
						if ( expressionProvider != null )
							expressionCellEditor.setExpressionProvider( expressionProvider );
						else
						{
							ComputedColumnHandle column = (ComputedColumnHandle) obj;
							BindingExpressionProvider provider = new BindingExpressionProvider( column.getElementHandle( ) );
							provider.addFilter( new ComputedColumnExpressionFilter( bindingTable ) );
							expressionCellEditor.setExpressionProvider( provider );
						}
					}
				}
				updateButtons( );
			}

		} );

		btnAdd = new Button( contentComposite, SWT.PUSH );
		btnAdd.setText( MSG_ADD );
		GridData data = new GridData( );
		data.widthHint = Math.max( 60, btnAdd.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnAdd.setLayoutData( data );
		btnAdd.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleAddEvent( );
				refreshBindingTable( );
				if ( table.getItemCount( ) > 0 )
					selectIndex = ( table.getItemCount( ) - 1 );
				updateButtons( );
			}

		} );
		btnEdit = new Button( contentComposite, SWT.PUSH );
		btnEdit.setText( MSG_EDIT );
		data = new GridData( );
		data.widthHint = Math.max( 60, btnEdit.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnEdit.setLayoutData( data );
		btnEdit.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				handleEditEvent( );
				refreshBindingTable( );
			}

		} );
		btnDel = new Button( contentComposite, SWT.PUSH );
		btnDel.setText( MSG_DELETE );
		data = new GridData( GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.GRAB_VERTICAL );
		data.widthHint = Math.max( 60, btnDel.computeSize( SWT.DEFAULT,
				SWT.DEFAULT,
				true ).x );
		btnDel.setLayoutData( data );
		btnDel.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				if ( bindingTable.isCellEditorActive( ) )
				{
					bindingTable.cancelEditing( );
				}
				int pos = bindingTable.getTable( ).getSelectionIndex( );
				if ( pos == -1 )
				{
					bindingTable.getTable( ).setFocus( );
					return;
				}
				selectIndex = pos;
				int itemCount = bindingTable.getTable( ).getItemCount( );
				if ( selectIndex == itemCount - 1 )
				{
					selectIndex--;
				}
				try
				{
					handleDelEvent( );
				}
				catch ( Exception e1 )
				{
					WidgetUtil.processError( getShell( ), e1 );
				}
				refreshBindingTable( );
			}
		} );
		// initTableCellColor( );

		return parentComposite;
	}

	protected void handleAddEvent( )
	{
		DataColumnBindingDialog dialog = new DataColumnBindingDialog( );
		dialog.setInput( inputElement );
		dialog.setExpressionProvider( expressionProvider );
		if ( dialog.open( ) == Dialog.OK )
		{
			if ( bindingTable != null )
			{
				refreshBindingTable( );
				bindingTable.getTable( ).setSelection( bindingTable.getTable( )
						.getItemCount( ) - 1 );
			}
		}

	}

	protected void handleEditEvent( )
	{
		ComputedColumnHandle bindingHandle = null;
		int pos = bindingTable.getTable( ).getSelectionIndex( );
		if ( pos > -1 )
		{
			bindingHandle = (ComputedColumnHandle) ( (ReportItemHandle) DEUtil.getBindingHolder( inputElement ) ).getColumnBindings( )
					.getAt( pos );
		}
		if ( bindingHandle == null )
			return;
		DataColumnBindingDialog dialog = new DataColumnBindingDialog( );
		dialog.setInput( (ReportItemHandle) inputElement, bindingHandle );
		dialog.setExpressionProvider( expressionProvider );
		if ( dialog.open( ) == Dialog.OK )
		{
			 if ( bindingTable != null )
				 bindingTable.getTable( ).setSelection(pos );
		}
	}

	protected void handleDelEvent( )
	{
		int pos = bindingTable.getTable( ).getSelectionIndex( );
		if ( pos > -1 )
		{
			try
			{
				ComputedColumnHandle handle = (ComputedColumnHandle) ( DEUtil.getBindingHolder( inputElement ) ).getColumnBindings( )
						.getAt( pos );
				deleteRow( handle );
			}
			catch ( Exception e1 )
			{
				ExceptionHandler.handle( e1 );
			}
		}
	}

	private boolean existHighLightColumn( )
	{
		if ( highLightName == null )
			return false;
		for ( int i = 0; i < bindingTable.getTable( ).getItemCount( ); i++ )
		{
			TableItem item = bindingTable.getTable( ).getItem( i );
			if ( item.getText( 1 ).equals( highLightName ) )
				return true;
		}
		return false;
	}

	private void setHihtLightColumn( )
	{
		if ( highLightName == null )
			return;
		for ( int i = 0; i < bindingTable.getTable( ).getItemCount( ); i++ )
		{
			TableItem item = bindingTable.getTable( ).getItem( i );
			if ( item.getText( 1 ).equals( highLightName ) )
			{
				item.setForeground( 1, Display.getDefault( )
						.getSystemColor( SWT.COLOR_BLUE ) );
				bindingTable.getTable( ).setSelection( i );
			}
			else
			{
				item.getForeground( 1 ).equals( Display.getDefault( )
						.getSystemColor( SWT.COLOR_BLUE ) );
				item.setForeground( 1, Display.getDefault( )
						.getSystemColor( SWT.COLOR_LIST_FOREGROUND ) );
			}
		}
	}

	protected boolean initDialog( )
	{
		if ( canSelect )
		{
			if ( inputElement instanceof DataItemHandle )
			{
				selectedColumnName = ( (DataItemHandle) inputElement ).getResultSetColumn( );
				updateSelection( );
			}
		}
		/*
		 * generateButton.setEnabled( inputElement.getDataSet( ) != null ||
		 * DEUtil.getBindingHolder( inputElement ).getDataSet( ) != null );
		 */
		updateButtons( );
		return super.initDialog( );
	}

	protected void okPressed( )
	{
		if ( canSelect )
		{
			setResult( selectedColumnName );
			if ( inputElement instanceof DataItemHandle )
			{
				try
				{
					( (DataItemHandle) inputElement ).setResultSetColumn( selectedColumnName );
				}
				catch ( SemanticException e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
		super.okPressed( );
	}

	private void deleteRow( ComputedColumnHandle handle )
	{
		try
		{
			if ( handle.getName( ).equals( selectedColumnName ) )
			{
				selectedColumnName = null;
			}
			handle.drop( );
		}
		catch ( PropertyValueException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void generateBindingColumns( ) throws SemanticException
	{
		List columnList = DataUtil.generateComputedColumns( inputElement );
		if ( columnList.size( ) > 0 )
		{
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				addBinding( (ComputedColumn) iter.next( ) );
			}
		}
		refreshBindingTable( );
	}

	private void refreshBindingTable( )
	{
		bindingTable.refresh( );
		if ( canSelect )
		{
			updateSelection( );
		}
		updateButtons( );
	}

	private void updateButtons( )
	{
		boolean okEnable = false;
		if ( !canSelect || selectedColumnName != null )
		{
			okEnable = true;
		}
		getOkButton( ).setEnabled( okEnable );
		int min = 0;
		int max = bindingTable.getTable( ).getItemCount( ) - 1;

		if ( ( min <= selectIndex ) && ( selectIndex <= max ) )
		{
			btnDel.setEnabled( true );
			if ( btnEdit != null )
				btnEdit.setEnabled( true );
		}
		else
		{
			btnDel.setEnabled( false );
			if ( btnEdit != null )
				btnEdit.setEnabled( false );
		}
		bindingTable.getTable( ).select( selectIndex );
	}

	private ComputedColumnHandle getSelectColumnHandle( )
	{
		if ( selectedColumnName != null )
		{
			for ( int i = 0; i < bindingTable.getTable( ).getItemCount( ); i++ )
			{
				ComputedColumnHandle handle = (ComputedColumnHandle) bindingTable.getElementAt( i );
				if ( selectedColumnName.equals( handle.getName( ) ) )
				{
					return handle;
				}
			}
		}
		return null;
	}

	private void updateSelection( )
	{
		if ( canSelect )
		{
			( (CheckboxTableViewer) bindingTable ).setAllChecked( false );
			( (CheckboxTableViewer) bindingTable ).setGrayed( dummyChoice, true );
			if ( getSelectColumnHandle( ) != null )
			{
				( (CheckboxTableViewer) bindingTable ).setChecked( getSelectColumnHandle( ),
						true );
			}
		}
	}

	protected void addBinding( ComputedColumn column )
	{
		try
		{
			DEUtil.addColumn( DEUtil.getBindingHolder( inputElement ),
					column,
					false );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	protected List getBindingList( DesignElementHandle inputElement )
	{
		return DEUtil.getVisiableColumnBindingsList( inputElement );
	}

	private int canChangeDataSet( String newName )
	{
		String currentDataSetName = getDataSetName( );
		if ( currentDataSetName == null
				&& !inputElement.columnBindingsIterator( ).hasNext( ) )
		{
			return 0;
		}
		else if ( currentDataSetName == newName
				|| ( currentDataSetName != null && currentDataSetName.equals( newName ) ) )
		{
			return 2;
		}
		MessageDialog prefDialog = new MessageDialog( UIUtil.getDefaultShell( ),
				Messages.getString( "dataBinding.title.changeDataSet" ),//$NON-NLS-1$
				null,
				Messages.getString( "dataBinding.message.changeDataSet" ),//$NON-NLS-1$
				MessageDialog.INFORMATION,
				new String[]{
						IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.CANCEL_LABEL,
				},
				0 );
		return prefDialog.open( );
	}

	private String getDataSetName( )
	{
		if ( inputElement.getDataSet( ) == null )
		{
			return null;
		}
		String dataSetName = inputElement.getDataSet( ).getQualifiedName( );
		if ( StringUtil.isBlank( dataSetName ) )
		{
			dataSetName = null;
		}
		return dataSetName;
	}

	private List groupList = Collections.EMPTY_LIST;

	private String[] groups;

	protected Button btnDel;

	protected Button btnEdit;

	protected Button btnAdd;

	private PropertyHandle getParameterBindingPropertyHandle( )
	{
		return inputElement.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP );
	}

	/*
	 * Set data for Group List
	 */
	public void setGroupList( List groupList )
	{
		Assert.isNotNull( groupList );
		this.groupList = groupList;
	}

	protected ExpressionProvider expressionProvider;

	public void setExpressionProvider( ExpressionProvider provider )
	{
		expressionProvider = provider;
	}

}