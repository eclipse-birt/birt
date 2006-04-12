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

import java.util.List;

import org.eclipse.birt.report.designer.internal.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ComputedColumnExpressionFilter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.attributes.widget.ExpressionCellEditor;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.jface.util.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

/**
 * 
 */

public class ColumnBindingDialog extends BaseDialog
{

	private static final String dummyChoice = "dummy"; //$NON-NLS-1$
	public static final String DEFAULT_DLG_TITLE = Messages.getString( "ColumnBindingDialog.DialogTitle" ); //$NON-NLS-1$

	private static final String LABEL_COLUMN_BINDINGS = Messages.getString( "ColumnBindingDialog.Label.ColumnBinding" ); //$NON-NLS-1$
	private static final String BUTTON_GENERATE = Messages.getString( "ColumnBindingDialog.Button.Generate" ); //$NON-NLS-1$
	private static final String COLUMN_NAME = Messages.getString( "ColumnBindingDialog.Column.Name" ); //$NON-NLS-1$
	private static final String COLUMN_DATATYPE = Messages.getString( "ColumnBindingDialog.Column.DataType" ); //$NON-NLS-1$
	private static final String COLUMN_EXPRESSION = Messages.getString( "ColumnBindingDialog.Column.Expression" ); //$NON-NLS-1$
	private static final String INPUT_PROPMT = Messages.getString( "ColumnBindingDialog.InputPrompt" ); //$NON-NLS-1$

	private static final IChoiceSet dataTypeChoiceSet = DesignEngine.getMetaDataDictionary( )
			.getChoiceSet( DesignChoiceConstants.CHOICE_COLUMN_DATA_TYPE );
	private static final IChoice[] dataTypes = dataTypeChoiceSet.getChoices( null );
	private static final String[] dataTypeDisplayNames = ChoiceSetFactory.getDisplayNamefromChoiceSet( dataTypeChoiceSet );

	private boolean canSelect = false;

	private ReportItemHandle inputElement;
	// private List bindingList;

	private Button generateButton;
	private TableViewer bindingTable;
	private ExpressionCellEditor expressionCellEditor;
	private String selectedColumnName = null;

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
			List elementsList = DEUtil.getVisiableColumnBindingsList( (DesignElementHandle) inputElement );
			elementsList.add( dummyChoice );
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

	private ICellModifier cellModifier = new ICellModifier( ) {

		public boolean canModify( Object element, String property )
		{
			if ( property == null
					|| ( element == dummyChoice && !COLUMN_NAME.equals( property ) ) )
			{
				return false;
			}
			return true;
		}

		public Object getValue( Object element, String property )
		{
			if ( element == dummyChoice )
			{
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
							false );
					if ( element == dummyChoice )
					{
						if ( newName == null )
						{
							return;
						}
						ComputedColumn column = StructureFactory.createComputedColumn( );
						column.setName( newName );
						column.setExpression( "" ); //$NON-NLS-1$
						DEUtil.addColumn( DEUtil.getBindingHolder( inputElement ),
								column,
								true );
					}
					else
					{
						ComputedColumnHandle columnHandle = (ComputedColumnHandle) element;
						boolean selectedNameChanged = false;
						if ( columnHandle.getName( )
								.equals( selectedColumnName ) )
						{
							selectedNameChanged = true;
						}
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
						bindingHandle.setExpression( (String) value );
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
		this( DEFAULT_DLG_TITLE, false );
	}

	public ColumnBindingDialog( String title )
	{
		super( title, false );
	}

	public ColumnBindingDialog( boolean canSelect )
	{
		this( DEFAULT_DLG_TITLE, canSelect );
	}

	public ColumnBindingDialog( String title, boolean canSelect )
	{
		super( title );
		this.canSelect = canSelect;
	}

	public void setInput( ReportItemHandle input )
	{
		Assert.isNotNull( input );
		this.inputElement = input;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite parentComposite = (Composite) super.createDialogArea( parent );
		/**
		 * Label & button
		 */
		Composite composite = new Composite( parentComposite, SWT.NONE );
		composite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		composite.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );

		Label label = new Label( composite, SWT.NONE );
		label.setText( LABEL_COLUMN_BINDINGS );
		label.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		generateButton = new Button( composite, SWT.PUSH );
		generateButton.setText( BUTTON_GENERATE );
		generateButton.setLayoutData( new GridData( ) );
		generateButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				try
				{
					generateBindingColumns( );
				}
				catch ( SemanticException e1 )
				{
					ExceptionHandler.handle( e1 );
				}
			}
		} );

		/**
		 * Binding table
		 */
		Table table = new Table( parentComposite, SWT.SINGLE
				| SWT.FULL_SELECTION
				| SWT.BORDER
				| ( canSelect ? SWT.CHECK : 0 ) );
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 200;
		table.setLayoutData( gd );
		table.setLinesVisible( true );
		table.setHeaderVisible( true );
		table.addKeyListener( new KeyAdapter( ) {

			/**
			 * @see org.eclipse.swt.events.KeyAdapter#keyReleased(org.eclipse.swt.events.KeyEvent)
			 */
			public void keyReleased( KeyEvent e )
			{
				// If Delete pressed, delete the selected row
				if ( e.keyCode == SWT.DEL )
				{
					IStructuredSelection selection = (IStructuredSelection) bindingTable.getSelection( );
					if ( selection.getFirstElement( ) instanceof ComputedColumnHandle )
					{
						deleteRow( (ComputedColumnHandle) selection.getFirstElement( ) );
					}
				}
			}
		} );
		String[] columns = null;
		int[] columnWidth = null;
		CellEditor[] cellEditors;

		expressionCellEditor = new ExpressionCellEditor( table );
		columns = new String[]{
				null, COLUMN_NAME, COLUMN_DATATYPE, COLUMN_EXPRESSION
		};
		columnWidth = new int[]{
				canSelect ? 25 : 20, 150, 70, 150,
		};
		cellEditors = new CellEditor[]{
				null,
				new TextCellEditor( table ),
				new ComboBoxCellEditor( table, dataTypeDisplayNames ),
				expressionCellEditor
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
		bindingTable.setCellModifier( cellModifier );
		bindingTable.setInput( inputElement );
		bindingTable.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				// updateTableButtons( );
				if ( !bindingTable.getSelection( ).isEmpty( ) )
				{
					Object obj = ( (IStructuredSelection) bindingTable.getSelection( ) ).getFirstElement( );
					if ( obj instanceof ComputedColumnHandle )
					{
						ComputedColumnHandle column = (ComputedColumnHandle) obj;
						BindingExpressionProvider provider = new BindingExpressionProvider( column.getElementHandle( ) );
						provider.addFilter( new ComputedColumnExpressionFilter( bindingTable ) );
						expressionCellEditor.setExpressionProvider( provider );
					}
				}
			}

		} );

		return parentComposite;
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
		generateButton.setEnabled( inputElement.getDataSet( ) != null
				|| DEUtil.getBindingHolder( inputElement ).getDataSet( ) != null );
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
			refreshBindingTable( );
		}
		catch ( PropertyValueException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void generateBindingColumns( ) throws SemanticException
	{
		if ( UIUtil.generateComputedColumns( inputElement ) )
		{
			refreshBindingTable( );
		}
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
	}

	private ComputedColumnHandle getSelectColumnHandle( )
	{
		if ( selectedColumnName != null )
		{
			for ( int i = 0; i < bindingTable.getTable( ).getItemCount( ) - 1; i++ )
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

}
