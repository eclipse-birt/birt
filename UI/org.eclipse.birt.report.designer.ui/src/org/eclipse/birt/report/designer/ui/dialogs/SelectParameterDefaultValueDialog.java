/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.dialogs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

import com.ibm.icu.util.ULocale;

/**
 * Presents a list of values from dataset, allows user to select to define
 * default value for dynamic parameter
 * 
 */
public class SelectParameterDefaultValueDialog extends BaseDialog
{

	//	private static final String STANDARD_DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a"; //$NON-NLS-1$
	private TableViewer tableViewer = null;
	private Table table = null;
	private Object[] selectedItems = null;
	private int[] selectedIndices = null;
	private int sortDir = SWT.UP;
	private java.util.List<Object> columnValueList = new ArrayList<Object>( );
	private final String NULL_VALUE_DISPLAY = Messages.getString( "SelectValueDialog.SelectValue.NullValue" ); //$NON-NLS-1$

	public SelectParameterDefaultValueDialog( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	public void setColumnValueList( Collection valueList )
	{
		columnValueList.clear( );
		columnValueList.addAll( valueList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jface.dialogs.Dialog#createDialogArea(org.eclipse.swt.widgets
	 * .Composite)
	 */
	protected Control createDialogArea( Composite parent )
	{
		Composite composite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		composite.setLayout( layout );
		composite.setLayoutData( new GridData( GridData.FILL_BOTH ) );

		tableViewer = new TableViewer( composite, SWT.V_SCROLL
				| SWT.H_SCROLL
				| SWT.MULTI );
		GridData data = new GridData( GridData.FILL_BOTH );
		data.heightHint = 250;
		data.widthHint = 300;

		table = tableViewer.getTable( );
		table.setLayoutData( data );
		table.setHeaderVisible( true );

		final TableColumn column = new TableColumn( table, SWT.NONE );
		column.setText( Messages.getString( "SelectValueDialog.selectValue" ) ); //$NON-NLS-1$
		column.setWidth( data.widthHint );

		TableItem item = new TableItem( table, SWT.NONE );
		item.setText( 0, Messages.getString( "SelectValueDialog.retrieving" ) ); //$NON-NLS-1$

		column.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				sortDir = sortDir == SWT.UP ? SWT.DOWN : SWT.UP;
				table.setSortDirection( sortDir );
				tableViewer.setSorter( new TableSorter( sortDir ) );
			}
		} );

		table.addMouseListener( new MouseAdapter( ) {

			public void mouseDoubleClick( MouseEvent e )
			{
				if ( table.getSelectionCount( ) > 0 )
				{
					okPressed( );
				}
			}
		} );


		PlatformUI.getWorkbench( ).getDisplay( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				populateList( );
			}

		} );

		UIUtil.bindHelp( parent,
				IHelpContextIds.SELECT_PARAMETER_DEFAULT_VALUE_DIALOG_ID );
		return composite;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.dialogs.Dialog#okPressed()
	 */
	protected void okPressed( )
	{
		selectedIndices = table.getSelectionIndices( );
		if ( columnValueList.get( selectedIndices[0] ) == null )
		{
			setResult( null );
		}
		else
		{
			selectedItems = new Object[table.getSelectionCount( )];
			for ( int i = 0; i < table.getSelectionCount( ); i++ )
			{
				selectedItems[i] = table.getSelection( )[i].getData( );
			}
			setResult( table.getSelection( ) );
		}
		super.okPressed( );
	}

	/**
	 * populate all available value in selectValueList
	 */
	private void populateList( )
	{
		try
		{
			if ( this.getShell( ) == null || this.getShell( ).isDisposed( ) )
				return;
			if ( this.getOkButton( ) != null && !this.getOkButton( ).isDisposed( ) )
				getOkButton( ).setEnabled( false );

			table.removeAll( );
			table.deselectAll( );
			tableViewer.setContentProvider( new ContentProvider( ) );
			tableViewer.setLabelProvider( new TableLabelProvider( ) );

			if ( columnValueList != null )
			{
				tableViewer.setInput( columnValueList );
			}
			else
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "SelectValueDialog.errorRetrievinglist" ), Messages.getString( "SelectValueDialog.noExpressionSet" ) ); //$NON-NLS-1$ //$NON-NLS-2$
			}

			if ( table.getItemCount( ) > 0 )
			{
				table.select( 0 );
				getOkButton( ).setEnabled( true );
			}
			for ( int i = 0; i < table.getItemCount( ); i++ )
			{
				table.getItem( i ).setData( columnValueList.get( i ) );
			}

			table.setSortColumn( table.getColumn( 0 ) );
			table.setSortDirection( sortDir );
			tableViewer.setSorter( new TableSorter( sortDir ) );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	public String[] getSelectedValue( )
	{
		String[] exprValues = null;
		if ( selectedIndices != null && selectedIndices.length > 0 )
		{
			exprValues = new String[selectedIndices.length];
			for ( int i = 0; i < selectedIndices.length; i++ )
			{
				if ( selectedItems[i] == null )
				{
					exprValues[i] = "null"; //$NON-NLS-1$
				}
				else
				{
					exprValues[i] = String.valueOf( selectedItems[i] );
				}
			}
		}
		return exprValues;
	}

	public class TableSorter extends ViewerSorter
	{

		private int sortDir;

		private TableSorter( int sortDir )
		{
			this.sortDir = sortDir;
		}

		public int compare( Viewer viewer, Object e1, Object e2 )
		{
			if ( sortDir == SWT.UP )
			{
				if ( e1 instanceof Integer )
				{
					return ( (Integer) e1 ).compareTo( (Integer) e2 );
				}
				else if ( e1 instanceof Double )
				{
					return ( (Double) e1 ).compareTo( (Double) e2 );
				}
				else if ( e1 instanceof BigDecimal )
				{
					return ( (BigDecimal) e1 ).compareTo( (BigDecimal) e2 );
				}
				else
				{
					return e1.toString( ).compareTo( e2.toString( ) );
				}
			}
			else if ( sortDir == SWT.DOWN )
			{
				if ( e2 instanceof Integer )
				{
					return ( (Integer) e2 ).compareTo( (Integer) e1 );
				}
				else if ( e2 instanceof Double )
				{
					return ( (Double) e2 ).compareTo( (Double) e1 );
				}
				else if ( e2 instanceof BigDecimal )
				{
					return ( (BigDecimal) e2 ).compareTo( (BigDecimal) e1 );
				}
				else
				{
					return e2.toString( ).compareTo( e1.toString( ) );
				}
			}
			return 0;
		}
	}

	public class ContentProvider implements IStructuredContentProvider
	{

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof List )
			{
				return ( (List) inputElement ).toArray( );
			}
			return new Object[0];
		}

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput, Object newInput )
		{
		}
	}

	public class TableLabelProvider extends LabelProvider implements ITableLabelProvider
	{

		public String getColumnText( Object element, int columnIndex )
		{
			DateFormatter formatter = new DateFormatter( ULocale.US );

			if ( columnIndex == 0 )
			{
				if ( element != null )
				{
					if ( element instanceof java.sql.Date )
					{
						formatter.applyPattern( "yyyy-MM-dd" ); //$NON-NLS-1$
						return formatter.format( (Date) element );
					}
					else if ( element instanceof java.sql.Time )
					{
						formatter.applyPattern( "HH:mm:ss.SSS" ); //$NON-NLS-1$
						return formatter.format( (Date) element );
					}
					else if ( element instanceof Date )
					{
						formatter.applyPattern( "yyyy-MM-dd HH:mm:ss.SSS" ); //$NON-NLS-1$
						return formatter.format( (Date) element );
					}
					else
						return element.toString( );
				}
				return NULL_VALUE_DISPLAY;
			}
			return null;
		}

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}
	}

}
