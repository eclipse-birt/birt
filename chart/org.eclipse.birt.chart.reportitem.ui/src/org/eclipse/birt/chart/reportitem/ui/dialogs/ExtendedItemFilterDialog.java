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

package org.eclipse.birt.chart.reportitem.ui.dialogs;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.reportitem.ui.dialogs.widget.ExpressionValueCellEditor;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.PropertyHandleTableViewer;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.Utility;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.ExpressionProvider;
import org.eclipse.birt.report.designer.ui.dialogs.IExpressionProvider;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.ui.widget.ComboBoxExpressionCellEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.jface.dialogs.TrayDialog;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class ExtendedItemFilterDialog extends TrayDialog
		implements
			ITableLabelProvider,
			ISelectionChangedListener
{

	private transient PropertyHandleTableViewer viewer = null;

	private transient String[] columnExpressions = null;

	private transient PropertyHandle filters = null;
	private transient FilterCondition newFilter = null;
	private transient boolean isOperatorSet = false;

	private static String[] operators;
	private static String[] operatorDisplayNames;

	static
	{
		IChoiceSet chset = ChoiceSetFactory.getStructChoiceSet( FilterCondition.FILTER_COND_STRUCT,
				FilterCondition.OPERATOR_MEMBER );
		IChoice[] chs = chset.getChoices( );
		operators = new String[chs.length];
		operatorDisplayNames = new String[chs.length];

		for ( int i = 0; i < chs.length; i++ )
		{
			operators[i] = chs[i].getName( );
			operatorDisplayNames[i] = chs[i].getDisplayName( );
		}
	}

	private transient ComboBoxExpressionCellEditor columnNameEditor = null;

	private transient ExtendedItemHandle reportItemHandle = null;

	private transient IDataServiceProvider dataServiceProvider = null;

	public ExtendedItemFilterDialog( ExtendedItemHandle reportItemHandle,
			IDataServiceProvider dataServiceProvider )
	{
		super( getActiveShell( ) );
		this.reportItemHandle = reportItemHandle;
		this.dataServiceProvider = dataServiceProvider;
	}

	static Shell getActiveShell( )
	{
		Shell shell = PlatformUI.getWorkbench( ).getDisplay( ).getActiveShell( );
		if ( shell == null )
		{
			shell = Display.getCurrent( ).getActiveShell( );
		}
		return shell;
	}

	protected Control createDialogArea( Composite parent )
	{
		ChartUIUtil.bindHelp( parent,
				ChartHelpContextIds.DIALOG_DATA_SET_FILTER );
		getShell( ).setText( Messages.getString( "dataset.editor.filters" ) ); //$NON-NLS-1$

		( (GridData) parent.getLayoutData( ) ).heightHint = 200;
		Composite composite = (Composite) super.createDialogArea( parent );

		initColumnNames( );
		viewer = new PropertyHandleTableViewer( composite, true, true, true );
		viewer.getControl( ).setLayoutData( new GridData( GridData.FILL_BOTH ) );

		TableColumn column = new TableColumn( viewer.getViewer( ).getTable( ),
				SWT.LEFT );
		column.setText( " " ); //$NON-NLS-1$
		column.setResizable( false );
		column.setWidth( 19 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( Messages.getString( "dataset.editor.title.expression" ) ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( Messages.getString( "dataset.editor.title.operator" ) ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( Messages.getString( "dataset.editor.title.value1" ) ); //$NON-NLS-1$
		column.setWidth( 100 );
		column = new TableColumn( viewer.getViewer( ).getTable( ), SWT.LEFT );
		column.setText( Messages.getString( "dataset.editor.title.value2" ) ); //$NON-NLS-1$
		column.setWidth( 100 );

		initializeFilters( );

		viewer.getViewer( )
				.setContentProvider( new IStructuredContentProvider( ) {

					public Object[] getElements( Object inputElement )
					{
						ArrayList filterList = new ArrayList( 10 );
						Iterator iter = filters.iterator( );
						if ( iter != null )
						{
							while ( iter.hasNext( ) )
							{
								filterList.add( iter.next( ) );
							}
						}

						if ( newFilter == null )
						{
							newFilter = new FilterCondition( );
						}

						filterList.add( newFilter );
						return filterList.toArray( );
					}

					public void dispose( )
					{

					}

					public void inputChanged( Viewer viewer, Object oldInput,
							Object newInput )
					{

					}
				} );
		viewer.getViewer( ).setLabelProvider( this );
		viewer.getViewer( ).setInput( filters );

		setupEditors( );
		addListeners( );

		pageActivated( );

		SessionHandleAdapter.getInstance( )
				.getCommandStack( )
				.startTrans( "Modify Filters" ); //$NON-NLS-1$
		return composite;
	}

	protected void setShellStyle( int newShellStyle )
	{
		super.setShellStyle( newShellStyle
				| SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL );
	}

	private void setupEditors( )
	{
		CellEditor[] editors = new CellEditor[5];
		if ( columnExpressions == null )
		{
			columnExpressions = new String[]{};
		}
		columnNameEditor = new ComboBoxExpressionCellEditor( viewer.getViewer( )
				.getTable( ), columnExpressions, SWT.NONE );
		IExpressionProvider expressionProvider = new ExpressionProvider( reportItemHandle );
		columnNameEditor.setExpressionProvider( expressionProvider );
		// columnNameEditor.addFilter( new DataSetExpressionFilter( ) );

		editors[1] = columnNameEditor;
		editors[2] = new ComboBoxCellEditor( viewer.getViewer( ).getTable( ),
				operatorDisplayNames,
				SWT.READ_ONLY );

		ExpressionValueCellEditor editor = new ExpressionValueCellEditor( viewer.getViewer( )
				.getTable( ) );
		editor.setExpressionProvider( expressionProvider );
		// editor.addFilter( new DataSetExpressionFilter( ) );
		editor.setReportElement( reportItemHandle );
		editors[3] = editor;
		editor = new ExpressionValueCellEditor( viewer.getViewer( ).getTable( ) );
		editor.setExpressionProvider( expressionProvider );
		// editor.addFilter( new DataSetExpressionFilter( ) );
		editor.setReportElement( reportItemHandle );
		editors[4] = editor;

		viewer.getViewer( ).setCellEditors( editors );
		viewer.getViewer( ).setColumnProperties( new String[]{
				"", //$NON-NLS-1$ 
				"expr", //$NON-NLS-1$ 
				"operator", //$NON-NLS-1$
				"value1", //$NON-NLS-1$
				"value2" //$NON-NLS-1$
		} );

		viewer.getViewer( ).setCellModifier( new ICellModifier( ) {

			public boolean canModify( Object element, String property )
			{
				String operator = ""; //$NON-NLS-1$
				String expr = null;

				try
				{
					operator = (String) Utility.getProperty( element,
							"operator" ); //$NON-NLS-1$
					expr = (String) Utility.getProperty( element, "expr" ); //$NON-NLS-1$
				}
				catch ( Exception ex )
				{
					ExceptionHandler.handle( ex );
				}

				if ( operator == null )
				{
					operator = ""; //$NON-NLS-1$
				}

				if ( element == newFilter && !property.equals( "expr" ) ) //$NON-NLS-1$
					return false;

				if ( property.equals( "value2" ) && //$NON-NLS-1$ 
						!( operator.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) || operator.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) ) )
				{
					return false;
				}

				if ( property.equals( "value1" ) && //$NON-NLS-1$
						( operator.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL )
								|| operator.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL )
								|| operator.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE ) || operator.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) ) )
				{
					return false;
				}

				String bindingName = null;
				for ( int i = 0; i < columnExpressions.length; i++ )
				{
					if ( DEUtil.getColumnExpression( columnExpressions[i] )
							.equals( expr ) )
					{
						bindingName = columnExpressions[i];
						break;
					}
				}
				( (ExpressionValueCellEditor) ( viewer.getViewer( )
						.getCellEditors( )[3] ) ).setBindingName( bindingName );
				( (ExpressionValueCellEditor) ( viewer.getViewer( )
						.getCellEditors( )[4] ) ).setBindingName( bindingName );
				return true;
			}

			public Object getValue( Object element, String property )
			{
				Object value = null;
				try
				{
					value = Utility.getProperty( element, property );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
				if ( "operator".equals( property ) ) //$NON-NLS-1$
				{
					value = new Integer( getOperatorIndex( (String) value ) );
				}
				if ( value == null )
				{
					value = ""; //$NON-NLS-1$
				}
				return value;
			}

			public void modify( Object element, String property, Object value )
			{
				Object actualElement = ( (TableItem) element ).getData( );
				if ( value != null )
				{
					try
					{
						if ( "operator".equals( property ) ) //$NON-NLS-1$
						{
							Integer index = (Integer) value;
							if ( index.intValue( ) > -1
									&& index.intValue( ) < operators.length )
							{
								value = operators[index.intValue( )];
								if ( value.equals( DesignChoiceConstants.FILTER_OPERATOR_NULL )
										|| value.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_NULL )
										|| value.equals( DesignChoiceConstants.FILTER_OPERATOR_TRUE )
										|| value.equals( DesignChoiceConstants.FILTER_OPERATOR_FALSE ) )
								{
									Utility.setProperty( actualElement,
											"value1", //$NON-NLS-1$
											"" ); //$NON-NLS-1$
									Utility.setProperty( actualElement,
											"value2", //$NON-NLS-1$
											"" ); //$NON-NLS-1$
									viewer.getViewer( ).refresh( );
								}
								else if ( !( value.equals( DesignChoiceConstants.FILTER_OPERATOR_BETWEEN ) || value.equals( DesignChoiceConstants.FILTER_OPERATOR_NOT_BETWEEN ) ) )
								{
									Utility.setProperty( actualElement,
											"value2", //$NON-NLS-1$
											"" ); //$NON-NLS-1$
									viewer.getViewer( ).refresh( );
								}
							}
							else
							{
								value = ""; //$NON-NLS-1$
							}
						}
						else if ( "expr".equals( property ) ) //$NON-NLS-1$
						{
							if ( isColumnName( (String) value ) )
							{
								value = "row[\"" + DEUtil.escape( (String) value ) + "\"]"; //$NON-NLS-1$ //$NON-NLS-2$
							}
							if ( actualElement != newFilter
									&& ( value == null || ( (String) value ).trim( )
											.length( ) == 0 ) )
							{
								ExceptionHandler.openMessageBox( Messages.getString( "filters.error.title" ), //$NON-NLS-1$
										Messages.getString( "filters.empty.columnName" ), //$NON-NLS-1$
										SWT.ICON_INFORMATION );
								viewer.getViewer( )
										.update( actualElement, null );
								return;
							}
						}
						Utility.setProperty( actualElement, property, value );
					}
					catch ( InvocationTargetException ite )
					{
						Exception e = new Exception( Messages.getString( "DataSetFiltersPage.exception.msg.operator.notSupport" ) //$NON-NLS-1$
								+ value );
						ExceptionHandler.handle( e );
					}
					catch ( Exception ex )
					{
						ex.printStackTrace( );
						ExceptionHandler.handle( ex );
					}

					viewer.getViewer( )
							.update( ( (TableItem) element ).getData( ), null );
					// If this is a new Item then add this item to the handle
					// and refresh the viewer

					if ( actualElement instanceof FilterCondition )
					{
						isOperatorSet = true;
						if ( newFilter.getExpr( ) != null
								&& newFilter.getExpr( ).trim( ).length( ) > 0 )
						{
							try
							{
								filters.addItem( newFilter );
								clearNewFilter( );
								viewer.getViewer( ).refresh( );
							}
							catch ( SemanticException e )
							{
								ExceptionHandler.handle( e );
							}
						}
					}
				}
			}
		} );
	}

	private void addListeners( )
	{
		viewer.getViewer( ).getTable( ).addKeyListener( new KeyListener( ) {

			public void keyPressed( KeyEvent e )
			{
			}

			public void keyReleased( KeyEvent e )
			{
				if ( e.keyCode == SWT.DEL )
				{
					removeSelectedItem( );
				}
				setPageProperties( );
			}

		} );

		viewer.getRemoveButton( )
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						removeSelectedItem( );
						setPageProperties( );
					}

					public void widgetDefaultSelected( SelectionEvent e )
					{
					}

				} );

		viewer.getRemoveMenuItem( )
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						removeSelectedItem( );
						setPageProperties( );
					}

					public void widgetDefaultSelected( SelectionEvent e )
					{
					}

				} );

		viewer.getRemoveAllMenuItem( )
				.addSelectionListener( new SelectionListener( ) {

					public void widgetSelected( SelectionEvent e )
					{
						removeAllItem( );
						setPageProperties( );
					}

					public void widgetDefaultSelected( SelectionEvent e )
					{
						widgetSelected( e );
					}
				} );

		viewer.getViewer( ).addSelectionChangedListener( this );

	}

	private void initColumnNames( )
	{
		try
		{
			columnExpressions = dataServiceProvider.getPreviewHeader( );
		}
		catch ( ChartException e )
		{
			WizardBase.displayException( e );
		}
	}

	private boolean isColumnName( String name )
	{
		for ( int n = 0; n < columnExpressions.length; n++ )
		{
			if ( columnExpressions[n].equals( name ) )
			{
				return true;
			}
		}
		return false;
	}

	private void initializeFilters( )
	{
		filters = reportItemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP );
	}

	private int getOperatorIndex( String filter )
	{
		for ( int n = 0; n < operators.length; n++ )
		{
			if ( operators[n].equals( filter ) )
			{
				return n;
			}
		}

		return -1;
	}

	private void pageActivated( )
	{
		initColumnNames( );
		if ( columnExpressions != null )
		{
			columnNameEditor.setItems( columnExpressions );
		}
		// The proeprties of the various controls on the page
		// will be set depending on the filters
		setPageProperties( );
	}

	/**
	 * Depending on the value of the Filters the properties of various controls
	 * on this page are set
	 */
	private void setPageProperties( )
	{

		boolean filterConditionExists = false;

		filterConditionExists = ( filters != null
				&& filters.getListValue( ) != null && filters.getListValue( )
				.size( ) > 0 );
		viewer.getDownButton( ).setEnabled( filterConditionExists );
		viewer.getUpButton( ).setEnabled( filterConditionExists );
		viewer.getRemoveButton( ).setEnabled( filterConditionExists );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object,
	 *      int)
	 */
	public Image getColumnImage( Object element, int columnIndex )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object,
	 *      int)
	 */
	public String getColumnText( Object element, int columnIndex )
	{
		String value = null;
		try
		{
			if ( element != newFilter )
			{
				switch ( columnIndex )
				{
					case 1 :
					{
						value = (String) Utility.getProperty( element, "expr" ); //$NON-NLS-1$
						break;
					}
					case 2 :
					{
						if ( element != newFilter || isOperatorSet )
						{
							int index = getOperatorIndex( (String) Utility.getProperty( element,
									"operator" ) ); //$NON-NLS-1$
							if ( index > -1 )
							{
								value = operatorDisplayNames[index];
							}
							else
							{
								value = (String) Utility.getProperty( element,
										"operator" ); //$NON-NLS-1$
							}
						}
						break;
					}
					case 3 :
					{
						value = (String) Utility.getProperty( element, "value1" ); //$NON-NLS-1$
						break;
					}
					case 4 :
					{
						value = (String) Utility.getProperty( element, "value2" ); //$NON-NLS-1$
						break;
					}
				}
			}
			else if ( columnIndex == 1 )
			{
				value = Messages.getString( "filters.prompt.new" ); //$NON-NLS-1$
			}
		}
		catch ( Exception ex )
		{
			ExceptionHandler.handle( ex );
		}
		if ( value == null )
		{
			value = ""; //$NON-NLS-1$
		}
		return value;
	}

	private void clearNewFilter( )
	{
		newFilter = null;
		isOperatorSet = false;
	}

	private void removeSelectedItem( )
	{
		// Get the current selection and delete that row
		int index = viewer.getViewer( ).getTable( ).getSelectionIndex( );
		int count = ( filters.getListValue( ) == null ) ? 0
				: filters.getListValue( ).size( );
		// Do not allow deletion of the last item.
		if ( index == count )
		{
			clearNewFilter( );
			viewer.getViewer( ).refresh( );
		}
	}

	private void removeAllItem( )
	{
		clearNewFilter( );
		viewer.getViewer( ).refresh( );
	}

	public void addListener( ILabelProviderListener listener )
	{
		// TODO Auto-generated method stub

	}

	protected void okPressed( )
	{
		SessionHandleAdapter.getInstance( ).getCommandStack( ).commit( );
		super.okPressed( );
	}

	protected void cancelPressed( )
	{
		SessionHandleAdapter.getInstance( ).getCommandStack( ).rollback( );
		super.cancelPressed( );
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

	public void selectionChanged( SelectionChangedEvent event )
	{
		// TODO Auto-generated method stub
		setPageProperties( );
	}
}
