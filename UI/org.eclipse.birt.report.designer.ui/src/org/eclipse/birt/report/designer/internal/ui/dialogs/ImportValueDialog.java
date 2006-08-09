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

package org.eclipse.birt.report.designer.internal.ui.dialogs;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.PatternSyntaxException;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.format.DateFormatter;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IPreparedQuery;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.ChoiceSetFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.util.ULocale;

/**
 * The dialog used to import values from data sets
 * <dt><b>Styles: (Defined in DesingChoicesConstants) </b></dt>
 * <dd>PARAM_TYPE_STRING</dd>
 * <dd>PARAM_TYPE_FLOAT</dd>
 * <dd>PARAM_TYPE_DECIMAL</dd>
 * <dd>PARAM_TYPE_DATETIME</dd>
 * <dd>PARAM_TYPE_BOOLEAN</dd>
 */

public class ImportValueDialog extends BaseDialog
{

	private static final String DLG_TITLE = Messages.getString( "ImportValueDialog.Title" ); //$NON-NLS-1$

	private static final String LABEL_SELECT_DATASET = Messages.getString( "ImportValueDialog.Label.SelectDataSet" ); //$NON-NLS-1$
	private static final String LABEL_SELECT_COLUMN = Messages.getString( "ImportValueDialog.Label.SelectColumn" ); //$NON-NLS-1$
	private static final String LABEL_SELECT_VALUE = Messages.getString( "ImportValueDialog.Label.SelectValue" ); //$NON-NLS-1$

	private static final String DATE_TIME_PATTERN = "MM/dd/yyyy hh:mm:ss a"; //$NON-NLS-1$

	private Combo dataSetChooser, columnChooser;
	private Text valueEditor;
	private List valueList, selectedList;
	private Button add, addAll, remove, removeAll;

	private String currentDataSetName;
	private ArrayList resultList = new ArrayList( );;

	private java.util.List columnList;
	private int selectedColumnIndex;

	private DataEngine engine;

	private String style;

	/**
	 * Constructs a new instance of the dialog
	 */
	public ImportValueDialog( String style )
	{
		super( DLG_TITLE );
		Assert.isTrue( DEUtil.getMetaDataDictionary( )
				.getChoiceSet( DesignChoiceConstants.CHOICE_PARAM_TYPE )
				.contains( style ) );
		this.style = style;
	}

	protected Control createDialogArea( Composite parent )
	{
		Composite composite = (Composite) super.createDialogArea( parent );
		createColumnSelectionArea( composite );
		createValueSelectionArea( composite );
		UIUtil.bindHelp( composite, IHelpContextIds.IMPORT_VALUE_DIALOG_ID );
		return composite;
	}

	private void createColumnSelectionArea( Composite parent )
	{
		Composite selectionArea = new Composite( parent, SWT.NONE );
		selectionArea.setLayout( UIUtil.createGridLayoutWithoutMargin( 2, false ) );
		selectionArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( selectionArea, SWT.NONE ).setText( LABEL_SELECT_DATASET );
		dataSetChooser = new Combo( selectionArea, SWT.DROP_DOWN
				| SWT.READ_ONLY );
		dataSetChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		dataSetChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				String newSelection = dataSetChooser.getText( );
				if ( !currentDataSetName.equals( newSelection ) )
				{
					currentDataSetName = newSelection;
					refreshColumns( );
				}
			}

		} );

		new Label( selectionArea, SWT.NONE ).setText( LABEL_SELECT_COLUMN );
		columnChooser = new Combo( selectionArea, SWT.DROP_DOWN | SWT.READ_ONLY );
		columnChooser.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		columnChooser.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				int newSelectedIndex = columnChooser.getSelectionIndex( );
				if ( selectedColumnIndex != newSelectedIndex )
				{
					selectedColumnIndex = newSelectedIndex;
					refreshValues( );
				}
			}

		} );

	}

	private void createValueSelectionArea( Composite parent )
	{
		Composite selectionArea = new Composite( parent, SWT.NONE );
		selectionArea.setLayout( new GridLayout( 3, false ) );
		selectionArea.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		Composite subComposite = new Composite( selectionArea, SWT.NONE );
		subComposite.setLayout( UIUtil.createGridLayoutWithoutMargin( ) );
		subComposite.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );

		new Label( subComposite, SWT.NONE ).setText( LABEL_SELECT_VALUE );
		valueEditor = new Text( subComposite, SWT.BORDER | SWT.SINGLE );
		valueEditor.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		valueEditor.addModifyListener( new ModifyListener( ) {

			public void modifyText( ModifyEvent e )
			{
				filteValues( );
			}
		} );

		GridData gd = new GridData( );
		gd.horizontalSpan = 2;
		new Label( selectionArea, SWT.NONE ).setLayoutData( gd ); // Dummy

		valueList = new List( selectionArea, SWT.MULTI
				| SWT.BORDER
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		setListLayoutData( valueList );
		valueList.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				addSelected( );
			}

			public void widgetSelected( SelectionEvent e )
			{
				updateButtons( );
			}
		} );
		Composite buttonBar = new Composite( selectionArea, SWT.NONE );
		GridLayout layout = UIUtil.createGridLayoutWithoutMargin( );
		// layout.verticalSpacing = 10;
		buttonBar.setLayout( layout );
		// buttonBar.setLayoutData( new GridData( GridData.FILL_VERTICAL ) );

		addAll = new Button( buttonBar, SWT.PUSH );
		addAll.setText( ">>" ); //$NON-NLS-1$
		addAll.setLayoutData( new GridData( GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_END
				| GridData.FILL_HORIZONTAL ) );

		addAll.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				addAll( );
			}

		} );

		add = new Button( buttonBar, SWT.PUSH );
		add.setText( ">" ); //$NON-NLS-1$
		add.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		add.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				addSelected( );
			}

		} );

		remove = new Button( buttonBar, SWT.PUSH );
		remove.setText( "<" ); //$NON-NLS-1$
		remove.setLayoutData( new GridData( GridData.FILL_HORIZONTAL ) );
		remove.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				removeSelected( );
			}

		} );

		removeAll = new Button( buttonBar, SWT.PUSH );
		removeAll.setText( "<<" ); //$NON-NLS-1$
		removeAll.setLayoutData( new GridData( GridData.FILL_VERTICAL
				| GridData.VERTICAL_ALIGN_BEGINNING
				| GridData.FILL_HORIZONTAL ) );
		removeAll.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				removeAll( );
			}

		} );

		selectedList = new List( selectionArea, SWT.MULTI
				| SWT.BORDER
				| SWT.V_SCROLL
				| SWT.H_SCROLL );
		setListLayoutData( selectedList );
		selectedList.addSelectionListener( new SelectionListener( ) {

			public void widgetDefaultSelected( SelectionEvent e )
			{
				removeSelected( );
			}

			public void widgetSelected( SelectionEvent e )
			{
				updateButtons( );
			}
		} );
	}

	private void setListLayoutData( List list )
	{
		GridData gd = new GridData( GridData.FILL_BOTH );
		gd.heightHint = 200;
		gd.widthHint = 200;
		list.setLayoutData( gd );
	}

	private void addSelected( )
	{
		String[] selected = valueList.getSelection( );
		int selectedIndex = valueList.getSelectionIndex( );
		int oldListSize = valueList.getItemCount( );

		if ( selected.length == 0 )
		{
			selected = new String[]{
				valueEditor.getText( )
			};
		}
		for ( int i = 0; i < selected.length; i++ )
		{
			if ( selectedList.indexOf( selected[i] ) == -1 )
			{
				selectedList.add( selected[i] );
			}
		}
		filteValues( );

		if ( selected.length == 1 )
		{
			int nextSelected = ( ( selectedIndex + 1 ) < oldListSize ) ? selectedIndex
					: ( selectedIndex - 1 );
			valueList.select( nextSelected );
		}
		else if ( ( selected.length > 1 ) && ( valueList.getItemCount( ) > 0 ) )
		{
			valueList.select( 0 );
		}

		updateButtons( );
	}

	private void addAll( )
	{
		String[] values = valueList.getItems( );
		for ( int i = 0; i < values.length; i++ )
		{
			if ( selectedList.indexOf( values[i] ) == -1 )
			{
				selectedList.add( values[i] );
			}
		}
		filteValues( );
	}

	private void removeSelected( )
	{
		int selectedIndex = selectedList.getSelectionIndex( );
		int oldListSize = selectedList.getItemCount( );

		String[] selected = selectedList.getSelection( );
		for ( int i = 0; i < selected.length; i++ )
		{
			selectedList.remove( selected[i] );
		}
		filteValues( );
		if ( selected.length == 1 )
		{
			int nextSelected = ( ( selectedIndex + 1 ) < oldListSize ) ? selectedIndex
					: ( selectedIndex - 1 );
			selectedList.select( nextSelected );
		}
		else if ( ( selected.length > 1 ) && ( valueList.getItemCount( ) > 0 ) )
		{
			selectedList.select( 0 );
		}
		updateButtons( );
	}

	private void removeAll( )
	{
		selectedList.removeAll( );
		filteValues( );
	}

	protected boolean initDialog( )
	{
		try
		{
			engine = DataEngine.newDataEngine( DataEngineContext.newInstance( DataEngineContext.DIRECT_PRESENTATION,
					null,
					null,
					null ) );
		}
		catch ( BirtException e )
		{
			ExceptionHandler.handle( e );
			return false;
		}

		dataSetChooser.setItems( ChoiceSetFactory.getDataSets( ) );
		dataSetChooser.select( 0 );
		currentDataSetName = dataSetChooser.getText( );
		refreshColumns( );
		return true;
	}

	private void refreshColumns( )
	{
		DataSetHandle dataSetHandle = getDataSetHandle( );
		try
		{
			columnList = DataUtil.getColumnList( dataSetHandle );
		}
		catch ( SemanticException e )
		{
			ExceptionHandler.handle( e );
		}

		columnChooser.removeAll( );
		selectedColumnIndex = -1;
		if ( columnList.size( ) == 0 )
		{
			columnChooser.setItems( new String[0] );
		}
		else
		{
			ArrayList matachedColumnList = new ArrayList( );
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				ResultSetColumnHandle column = (ResultSetColumnHandle) iter.next( );
				if ( matchType( column ) )
				{
					columnChooser.add( column.getColumnName( ) );
					matachedColumnList.add( column );
					selectedColumnIndex = 0;
				}
			}

		}
		columnChooser.select( selectedColumnIndex );
		columnChooser.setEnabled( selectedColumnIndex == 0 );
		refreshValues( );
	}

	private boolean matchType( ResultSetColumnHandle column )
	{
		if ( style.equals( DesignChoiceConstants.PARAM_TYPE_STRING )
				|| DesignChoiceConstants.COLUMN_DATA_TYPE_ANY.equals( column.getDataType( ) ) )
		{
			return true;
		}
		if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals( column.getDataType( ) ) )
		{
			return style.equals( DesignChoiceConstants.PARAM_TYPE_DATETIME );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL.equals( column.getDataType( ) ) )
		{
			return style.equals( DesignChoiceConstants.PARAM_TYPE_DECIMAL )
					|| style.equals( DesignChoiceConstants.PARAM_TYPE_INTEGER );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT.equals( column.getDataType( ) ) )
		{
			return style.equals( DesignChoiceConstants.PARAM_TYPE_FLOAT );
		}
		else if ( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER.equals( column.getDataType( ) ) )
		{
			return style.equals( DesignChoiceConstants.PARAM_TYPE_INTEGER );
		}
		return false;
	}

	private void refreshValues( )
	{
		resultList.clear( );
		if ( columnChooser.isEnabled( ) )
		{
			ResultSetColumnHandle selectedColumn = null;
			try
			{
				BaseQueryDefinition query = (BaseQueryDefinition) DataUtil.getPreparedQuery( engine,
						getDataSetHandle( ) )
						.getReportQueryDefn( );
				String queryExpr = null;
				for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
				{
					ResultSetColumnHandle column = (ResultSetColumnHandle) iter.next( );
					if ( column.getColumnName( )
							.equals( columnChooser.getText( ) ) )
					{
						queryExpr = DEUtil.getResultSetColumnExpression( column.getColumnName( ) );
						selectedColumn = column;
						break;
					}
				}
				if ( queryExpr == null )
				{
					return;
				}
				ScriptExpression expression = new ScriptExpression( queryExpr );
				String columnBindingName = "_$_COLUMNBINDINGNAME_$_";
				query.addResultSetExpression( columnBindingName, expression );
				// query.addExpression( expression, BaseTransform.ON_EACH_ROW );

				IPreparedQuery preparedQuery = engine.prepare( (IQueryDefinition) query );
				IQueryResults results = preparedQuery.execute( null );
				if ( results != null )
				{
					IResultIterator iter = results.getResultIterator( );
					if ( iter != null )
					{
						DateFormatter formatter = new DateFormatter( DATE_TIME_PATTERN,
								ULocale.US );
						while ( iter.next( ) )
						{
							String result = null;
							if ( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals( selectedColumn.getDataType( ) ) )
							{

								result = formatter.format( iter.getDate( columnBindingName ) );
							}
							else
							{
								result = iter.getString( columnBindingName );
							}
							if ( !StringUtil.isBlank( result )
									&& !resultList.contains( result ) )
							{
								resultList.add( result );
							}
						}
					}

					results.close( );
				}
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
				valueList.removeAll( );
				valueList.deselectAll( );
				updateButtons( );
			}
			filteValues( );
		}
		else
		{
			valueList.removeAll( );
			valueList.deselectAll( );
			updateButtons( );
		}
	}

	private void filteValues( )
	{
		valueList.removeAll( );
		valueList.deselectAll( );
		for ( Iterator itor = resultList.iterator( ); itor.hasNext( ); )
		{
			String value = (String) itor.next( );
			try
			{
				if ( selectedList.indexOf( value ) == -1
						&& ( value.startsWith( valueEditor.getText( ).trim( ) ) || value.matches( valueEditor.getText( )
								.trim( ) ) ) )
				{
					valueList.add( value );
				}
			}
			catch ( PatternSyntaxException e )
			{
			}
		}
		updateButtons( );
	}

	private void updateButtons( )
	{
		add.setEnabled( valueList.getSelectionCount( ) != 0
				|| ( valueEditor.getText( ).trim( ).length( ) != 0 && selectedList.indexOf( valueEditor.getText( )
						.trim( ) ) == -1 ) );
		addAll.setEnabled( valueList.getItemCount( ) != 0 );
		remove.setEnabled( selectedList.getSelectionCount( ) != 0 );
		removeAll.setEnabled( selectedList.getItemCount( ) != 0 );
		getOkButton( ).setEnabled( selectedList.getItemCount( ) != 0 );
	}

	protected void okPressed( )
	{
		setResult( selectedList.getItems( ) );
		super.okPressed( );
	}

	private DataSetHandle getDataSetHandle( )
	{
		return SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.findDataSet( currentDataSetName );
	}

	public boolean close( )
	{
		if ( engine != null )
		{
			engine.shutdown( );
		}
		return super.close( );
	}

}