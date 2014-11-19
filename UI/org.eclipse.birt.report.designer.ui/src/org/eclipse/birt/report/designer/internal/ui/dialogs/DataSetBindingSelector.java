/*******************************************************************************
 * Copyright (c) 2012 Actuate Corporation.
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
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.AutoResizeTableLayout;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableColumn;


public class DataSetBindingSelector extends BaseDialog
{

	public static final String NONE = Messages.getString( "BindingPage.None" ); //$NON-NLS-1$
	private CheckboxTableViewer columnViewers;
	private Combo dataSetCombo;
	private String dataSetName;
	private DataSetHandle datasetHandle;
	private String[] columns;
	private boolean validateEmptyResults=false;
	private List<DataSetHandle> datasets;

	private static final IChoice[] DATA_TYPE_CHOICES = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( )
			.getChoices( );

	class DataSetColumnProvider extends LabelProvider implements
			ITableLabelProvider,
			IStructuredContentProvider
	{

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof Iterator )
			{
				Iterator iter = (Iterator) inputElement;
				List list = new ArrayList( );
				while ( iter.hasNext( ) )
					list.add( iter.next( ) );
				return list.toArray( );
			}
			return new Object[0];
		}

		public Image getColumnImage( Object element, int columnIndex )
		{
			return null;
		}

		public String getColumnText( Object element, int columnIndex )
		{
			ResultSetColumnHandle column = (ResultSetColumnHandle) element;
			if ( columnIndex == 1 )
			{
				return column.getColumnName( );
			}
			if ( columnIndex == 2 )
			{
				return getDataTypeDisplayName( column.getDataType( ) );
			}
			return null;
		}

	}

	public DataSetBindingSelector( Shell parentShell, String title )
	{
		super( parentShell, title );
	}

	public Control createDialogArea( Composite parent )
	{

		UIUtil.bindHelp( parent, IHelpContextIds.SELECT_DATASET_BINDING_COLUMN );

		Composite area = (Composite) super.createDialogArea( parent );
		Composite contents = new Composite( area, SWT.NONE );
		contents.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		GridLayout layout = new GridLayout( );
		layout.numColumns = 2;
		contents.setLayout( layout );

		createDataSetContents( contents );
		createColumnBindingContents( contents );

		return area;
	}

	protected Control createContents( Composite parent )
	{
		Control control = super.createContents( parent );
		enableOKButton();
		return  control;
	}
	protected void createColumnBindingContents( Composite parent )
	{
		columnViewers = CheckboxTableViewer.newCheckList( parent, SWT.CHECK
				| SWT.BORDER
				| SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 450;
		data.heightHint = 200;
		data.horizontalSpan = 2;
		data.verticalIndent = 5;
		columnViewers.getTable( ).setLayoutData( data );

		columnViewers.getTable( ).setHeaderVisible( true );
		columnViewers.getTable( ).setLinesVisible( true );

		new TableColumn( columnViewers.getTable( ), SWT.NONE ).setText( "" ); //$NON-NLS-1$
		new TableColumn( columnViewers.getTable( ), SWT.NONE ).setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ) );//$NON-NLS-1$
		new TableColumn( columnViewers.getTable( ), SWT.NONE ).setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ) ); //$NON-NLS-1$

		TableLayout layout = new AutoResizeTableLayout( columnViewers.getTable( ) );
		layout.addColumnData( new ColumnWeightData( 6, true ) );
		layout.addColumnData( new ColumnWeightData( 47, true ) );
		layout.addColumnData( new ColumnWeightData( 47, true ) );
		columnViewers.getTable( ).setLayout( layout );
		columnViewers.addSelectionChangedListener(new ISelectionChangedListener() {
		      public void selectionChanged(SelectionChangedEvent event) {
		    	  enableOKButton();
		      }
		    });
		DataSetColumnProvider provider = new DataSetColumnProvider( );
		columnViewers.setLabelProvider( provider );
		columnViewers.setContentProvider( provider );

		
		

		Composite buttonContainer = new Composite( parent, SWT.NONE );
		data = new GridData( GridData.FILL_HORIZONTAL );
		data.horizontalSpan = 2;
		buttonContainer.setLayoutData( data );
		GridLayout gdLayout = new GridLayout( );
		gdLayout.numColumns = 2;
		gdLayout.marginWidth = gdLayout.marginHeight = 0;
		buttonContainer.setLayout( gdLayout );

		Button selectAllButton = new Button( buttonContainer, SWT.PUSH );
		selectAllButton.setText( Messages.getString( "DataSetBindingSelector.Button.SelectAll" ) ); //$NON-NLS-1$
		selectAllButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				columnViewers.setAllChecked( true );
				enableOKButton();
			}
		} );

		Button deselectAllButton = new Button( buttonContainer, SWT.PUSH );
		deselectAllButton.setText( Messages.getString( "DataSetBindingSelector.Button.DeselectAll" ) ); //$NON-NLS-1$
		deselectAllButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				columnViewers.setAllChecked( false );
				enableOKButton();
			}
		} );

		handleDatasetComboSelectedEvent( );

		if ( columns != null )
		{
			int count = columnViewers.getTable( ).getItemCount( );
			List columnList = Arrays.asList( columns );
			for ( int i = 0; i < count; i++ )
			{
				ResultSetColumnHandle column = (ResultSetColumnHandle) columnViewers.getElementAt( i );
				if ( columnList.contains( column.getColumnName( ) ) )
				{
					columnViewers.setChecked( column, true );
				}
			}
		}
	}

	protected void createDataSetContents( Composite parent )
	{

		if ( dataSetName != null )
		{
			Label lb = new Label( parent, SWT.NONE );
			lb.setText( Messages.getString( "DataSetBindingSelector.Label.SelectBindingColumns" ) ); //$NON-NLS-1$
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			data.horizontalSpan = 2;
			lb.setLayoutData( data );
		}
		else
		{
			Label dateSetLabel = new Label( parent, SWT.NONE );
			dateSetLabel.setText( Messages.getString( "DataSetBindingSelector.Combo.DataSet" ) ); //$NON-NLS-1$
			dataSetCombo = new Combo( parent, SWT.BORDER | SWT.READ_ONLY );
			initDateSetHandles();
			dataSetCombo.setItems(getDataSetComboList());
			dataSetCombo.select( 0 );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			dataSetCombo.setLayoutData( data );
			dataSetCombo.setVisibleItemCount( 30 );
			dataSetCombo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					handleDatasetComboSelectedEvent( );
				}

			} );
		}

	}

	private void initDateSetHandles() {
		ModuleHandle handle = SessionHandleAdapter.getInstance()
				.getReportDesignHandle();
		datasets = org.eclipse.birt.report.designer.internal.ui.util.UIUtil
				.getVisibleDataSetHandles(handle);
	}

	private String[] getDataSetComboList() {
		String[] comboList = new String[datasets.size() + 1];
		comboList[0] = NONE;
		for (int i = 0; i < datasets.size(); i++) {
			comboList[i + 1] = datasets.get(i).getQualifiedName();
		}
		return comboList;
	}

	private DataSetHandle getSelectedDataSet() {
		if (dataSetCombo.getSelectionIndex() > 0) {
			return (DataSetHandle) datasets.get(dataSetCombo
					.getSelectionIndex() - 1);
		}
		return null;
	}
	protected void handleDatasetComboSelectedEvent( )
	{
		Iterator iter = null;
		DataSetHandle handle=null;
		if(datasetHandle!=null){
			handle=datasetHandle;
		}else if(dataSetName!=null){
			handle=DataUtil.findDataSet(dataSetName);
		}else{
			handle =getSelectedDataSet();
		}		
		if ( handle != null )
		{
			try
			{
				CachedMetaDataHandle cmdh = DataSetUIUtil.getCachedMetaDataHandle( handle );
				iter = cmdh.getResultSet( ).iterator( );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
			
		}
		else
		{
			iter = new LinkedDataSetAdapter( ).getDataSetResLinkedDataModel( dataSetName );
		}
		if ( iter != null )
		{
			columnViewers.setInput( iter );
		}
		else
		{
			columnViewers.setInput( Collections.EMPTY_LIST.iterator( ) );
		}

	}

	private Object[] result;

	protected void okPressed( )
	{
	
		result = new Object[3];
		if ( dataSetName != null || dataSetCombo.getSelectionIndex( ) > 0 )
		{
			if ( dataSetName == null )
			{
				result[0] = dataSetCombo.getItem( dataSetCombo.getSelectionIndex( ) );
			}
			else
			{
				result[0] = dataSetName;
			}
			if ( columnViewers.getCheckedElements( ) != null )
			{
				result[1] = columnViewers.getCheckedElements( );
				List<String> list = new ArrayList<String>( );
				for ( int i = 0; i < columnViewers.getTable( ).getItemCount( ); i++ )
				{
					ResultSetColumnHandle column = (ResultSetColumnHandle) columnViewers.getElementAt( i );
					if ( !columnViewers.getChecked( column ) )
					{
						list.add( column.getColumnName( ) );
					}
				}
				result[2] = list.toArray( );
			}
			else
			{
				result[1] = null;
				List<String> list = new ArrayList<String>( );
				for ( int i = 0; i < columnViewers.getTable( ).getItemCount( ); i++ )
				{
					ResultSetColumnHandle column = (ResultSetColumnHandle) columnViewers.getElementAt( i );
					if ( !columnViewers.getChecked( column ) )
					{
						list.add( column.getColumnName( ) );
					}
				}

				if ( list.isEmpty( ) )
					result[2] = null;
				else
					result[2] = list.toArray( );
			}
		}
		else
		{
			result[0] = null;
			result[1] = null;
			result[2] = null;
		}
		super.okPressed( );
	}

	public Object getResult( )
	{
		return result;
	}

	public void setDataSet( String dataSetName )
	{
		this.dataSetName = dataSetName;
	}

	private String getDataTypeDisplayName( String dataType )
	{
		for ( int i = 0; i < DATA_TYPE_CHOICES.length; i++ )
		{
			IChoice choice = DATA_TYPE_CHOICES[i];
			if ( choice.getName( ).equals( dataType ) )
			{
				return choice.getDisplayName( );
			}
		}
		return dataType;
	}

	private void enableOKButton() {
		if (getOkButton() != null && !getOkButton().isDisposed()) {
			if(validateEmptyResults){
				getOkButton().setEnabled(columnViewers.getCheckedElements().length >0);
			}
		}
	}
	public void setColumns( String[] columns )
	{
		this.columns = columns;
	}

	public void setValidateEmptyResults(boolean validateEmptyResults) {
		this.validateEmptyResults = validateEmptyResults;
	}

	public DataSetHandle getDatasetHandle() {
		return datasetHandle;
	}

	public void setDatasetHandle(DataSetHandle datasetHandle) {
		if(datasetHandle!=null){
			this.datasetHandle = datasetHandle;
			this.dataSetName=datasetHandle.getQualifiedName();
		}
	}

}
