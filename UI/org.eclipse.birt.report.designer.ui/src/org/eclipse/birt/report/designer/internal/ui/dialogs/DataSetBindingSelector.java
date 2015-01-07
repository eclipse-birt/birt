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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.data.ui.dataset.DataSetUIUtil;
import org.eclipse.birt.report.designer.internal.ui.swt.custom.AutoResizeTableLayout;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.BaseDialog;
import org.eclipse.birt.report.designer.ui.views.attributes.providers.LinkedDataSetAdapter;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CachedMetaDataHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TreeViewerColumn;
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
import org.eclipse.swt.widgets.TreeItem;

/**
 * DataSetBindingSelector
 */
public class DataSetBindingSelector extends BaseDialog
{

	public static final String NONE = Messages.getString( "BindingPage.None" ); //$NON-NLS-1$
	private CheckboxTableViewer columnTableViewer;
	private CheckboxTreeViewer columnTreeViewer;
	private Composite contentPane;
	private Combo dataSetCombo;
	private String dataSetName;
	private DataSetHandle datasetHandle;
	private String[] columns;
	private boolean validateEmptyResults = false;
	private List<DataSetHandle> datasets;
	private Object[] result;

	private static final IChoice[] DATA_TYPE_CHOICES = DEUtil.getMetaDataDictionary( )
			.getStructure( ComputedColumn.COMPUTED_COLUMN_STRUCT )
			.getMember( ComputedColumn.DATA_TYPE_MEMBER )
			.getAllowedChoices( )
			.getChoices( );

	/**
	 * DataSetColumnProvider
	 */
	private static class DataSetColumnProvider extends LabelProvider implements
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

	/**
	 * GroupedColumnProvider
	 */
	private static class GroupedColumnProvider implements ITreeContentProvider
	{

		public void dispose( )
		{
		}

		public void inputChanged( Viewer viewer, Object oldInput,
				Object newInput )
		{
		}

		public Object[] getElements( Object inputElement )
		{
			if ( inputElement instanceof Map )
			{
				return ( (Map) inputElement ).entrySet( ).toArray( );
			}
			return new Object[0];
		}

		public Object[] getChildren( Object parentElement )
		{
			if ( parentElement instanceof Entry )
			{
				return ( (List) ( (Entry) parentElement ).getValue( ) ).toArray( );
			}
			return new Object[0];
		}

		public Object getParent( Object element )
		{
			return null;
		}

		public boolean hasChildren( Object element )
		{
			Object[] cc = getChildren( element );
			return cc != null && cc.length > 0;
		}
	}

	/**
	 * GroupedColumnNameProvider
	 */
	private static class GroupedColumnNameProvider extends ColumnLabelProvider
	{

		@Override
		public String getText( Object element )
		{
			if ( element instanceof Entry )
			{
				return (String) ( (Entry) element ).getKey( );
			}
			else if ( element instanceof ResultSetColumnHandle )
			{
				ResultSetColumnHandle column = (ResultSetColumnHandle) element;
				return column.getColumnName( );
			}
			return ""; //$NON-NLS-1$
		}
	}

	/**
	 * GroupedColumnTypeProvider
	 */
	private static class GroupedColumnTypeProvider extends ColumnLabelProvider
	{

		@Override
		public String getText( Object element )
		{
			if ( element instanceof ResultSetColumnHandle )
			{
				ResultSetColumnHandle column = (ResultSetColumnHandle) element;
				return getDataTypeDisplayName( column.getDataType( ) );
			}
			return ""; //$NON-NLS-1$
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

		contentPane = new Composite( contents, SWT.None );
		contentPane.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		contentPane.setLayout( new GridLayout( ) );

		columnTableViewer = null;
		columnTreeViewer = null;

		Object input = populateInput( );

		if ( input instanceof Map )
		{
			createColumnBindingTreeContents( contentPane, input );
		}
		else
		{
			createColumnBindingTableContents( contentPane, input );
		}

		return area;
	}

	protected Control createContents( Composite parent )
	{
		Control control = super.createContents( parent );
		enableOKButton( );
		return control;
	}

	protected void createColumnBindingTableContents( Composite parent,
			Object input )
	{
		columnTableViewer = CheckboxTableViewer.newCheckList( parent, SWT.CHECK
				| SWT.BORDER
				| SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 450;
		data.heightHint = 200;
		data.horizontalSpan = 2;
		data.verticalIndent = 5;
		columnTableViewer.getTable( ).setLayoutData( data );

		columnTableViewer.getTable( ).setHeaderVisible( true );
		columnTableViewer.getTable( ).setLinesVisible( true );

		new TableColumn( columnTableViewer.getTable( ), SWT.NONE ).setText( "" ); //$NON-NLS-1$
		new TableColumn( columnTableViewer.getTable( ), SWT.NONE ).setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ) );//$NON-NLS-1$
		new TableColumn( columnTableViewer.getTable( ), SWT.NONE ).setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ) ); //$NON-NLS-1$

		TableLayout layout = new AutoResizeTableLayout( columnTableViewer.getTable( ) );
		layout.addColumnData( new ColumnWeightData( 6, true ) );
		layout.addColumnData( new ColumnWeightData( 47, true ) );
		layout.addColumnData( new ColumnWeightData( 47, true ) );
		columnTableViewer.getTable( ).setLayout( layout );
		columnTableViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				enableOKButton( );
			}
		} );
		DataSetColumnProvider provider = new DataSetColumnProvider( );
		columnTableViewer.setLabelProvider( provider );
		columnTableViewer.setContentProvider( provider );

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
				columnTableViewer.setAllChecked( true );
				enableOKButton( );
			}
		} );

		Button deselectAllButton = new Button( buttonContainer, SWT.PUSH );
		deselectAllButton.setText( Messages.getString( "DataSetBindingSelector.Button.DeselectAll" ) ); //$NON-NLS-1$
		deselectAllButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				columnTableViewer.setAllChecked( false );
				enableOKButton( );
			}
		} );

		handleDataSetComboSelectedEvent( input );

		if ( columns != null )
		{
			int count = columnTableViewer.getTable( ).getItemCount( );
			List columnList = Arrays.asList( columns );
			for ( int i = 0; i < count; i++ )
			{
				ResultSetColumnHandle column = (ResultSetColumnHandle) columnTableViewer.getElementAt( i );
				if ( columnList.contains( column.getColumnName( ) ) )
				{
					columnTableViewer.setChecked( column, true );
				}
			}
		}
	}

	protected void createColumnBindingTreeContents( Composite parent,
			Object input )
	{
		columnTreeViewer = new CheckboxTreeViewer( parent, SWT.CHECK
				| SWT.BORDER
				| SWT.FULL_SELECTION );

		GridData data = new GridData( GridData.FILL_BOTH );
		data.widthHint = 450;
		data.heightHint = 200;
		data.horizontalSpan = 2;
		data.verticalIndent = 5;
		columnTreeViewer.getTree( ).setLayoutData( data );

		columnTreeViewer.getTree( ).setHeaderVisible( true );
		columnTreeViewer.getTree( ).setLinesVisible( true );

		TreeViewerColumn tvc1 = new TreeViewerColumn( columnTreeViewer,
				SWT.NONE );
		tvc1.getColumn( )
				.setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.Name" ) );//$NON-NLS-1$
		tvc1.getColumn( ).setWidth( 230 );
		tvc1.setLabelProvider( new GroupedColumnNameProvider( ) );

		TreeViewerColumn tvc2 = new TreeViewerColumn( columnTreeViewer,
				SWT.NONE );
		tvc2.getColumn( )
				.setText( Messages.getString( "DataSetColumnBindingsFormHandleProvider.Column.DataType" ) ); //$NON-NLS-1$
		tvc2.getColumn( ).setWidth( 100 );
		tvc2.setLabelProvider( new GroupedColumnTypeProvider( ) );

		columnTreeViewer.addSelectionChangedListener( new ISelectionChangedListener( ) {

			public void selectionChanged( SelectionChangedEvent event )
			{
				enableOKButton( );
			}
		} );

		columnTreeViewer.addCheckStateListener( new ICheckStateListener( ) {

			public void checkStateChanged( CheckStateChangedEvent event )
			{
				Object element = event.getElement( );

				if ( element instanceof Entry )
				{
					columnTreeViewer.setGrayed( element, false );
					columnTreeViewer.setSubtreeChecked( element,
							event.getChecked( ) );
				}
				else
				{
					Map<String, List> input = (Map<String, List>) columnTreeViewer.getInput( );
					for ( Entry<String, List> ent : input.entrySet( ) )
					{
						List children = ent.getValue( );
						if ( children.contains( element ) )
						{
							Object parent = ent;

							boolean allChecked = event.getChecked( );
							boolean graySet = false;

							for ( Object cc : children )
							{
								if ( columnTreeViewer.getChecked( cc ) != allChecked )
								{
									columnTreeViewer.setGrayed( parent, true );
									columnTreeViewer.setChecked( parent, true );
									graySet = true;

									break;
								}
							}

							if ( !graySet )
							{
								columnTreeViewer.setGrayed( parent, false );
								columnTreeViewer.setChecked( parent, allChecked );
							}

							break;
						}
					}
				}

				enableOKButton( );
			}
		} );

		columnTreeViewer.setContentProvider( new GroupedColumnProvider( ) );

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
				TreeItem[] items = columnTreeViewer.getTree( ).getItems( );

				for ( TreeItem ti : items )
				{
					columnTreeViewer.setGrayed( ti.getData( ), false );
					columnTreeViewer.setSubtreeChecked( ti.getData( ), true );
				}
				enableOKButton( );
			}
		} );

		Button deselectAllButton = new Button( buttonContainer, SWT.PUSH );
		deselectAllButton.setText( Messages.getString( "DataSetBindingSelector.Button.DeselectAll" ) ); //$NON-NLS-1$
		deselectAllButton.addSelectionListener( new SelectionAdapter( ) {

			public void widgetSelected( SelectionEvent e )
			{
				TreeItem[] items = columnTreeViewer.getTree( ).getItems( );

				for ( TreeItem ti : items )
				{
					columnTreeViewer.setGrayed( ti.getData( ), false );
					columnTreeViewer.setSubtreeChecked( ti.getData( ), false );
				}
				enableOKButton( );
			}
		} );

		handleDataSetComboSelectedEvent( input );

		if ( columns != null )
		{
			Set<String> columnSet = new HashSet<String>( Arrays.asList( columns ) );

			TreeItem[] items = columnTreeViewer.getTree( ).getItems( );

			for ( TreeItem ti : items )
			{
				TreeItem[] ccs = ti.getItems( );

				int count = 0;

				for ( TreeItem cc : ccs )
				{
					ResultSetColumnHandle column = (ResultSetColumnHandle) cc.getData( );
					if ( columnSet.contains( column.getColumnName( ) ) )
					{
						columnTreeViewer.setChecked( column, true );
						count++;
					}
				}

				if ( count == ccs.length )
				{
					columnTreeViewer.setChecked( ti.getData( ), true );
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
			datasets = UIUtil.getVisibleDataSetHandles( SessionHandleAdapter.getInstance( )
					.getModule( ) );
			dataSetCombo.setItems( getDataSetComboList( ) );
			initDateSetHandles();
			dataSetCombo.setItems(getDataSetComboList());
			dataSetCombo.select( 0 );
			GridData data = new GridData( GridData.FILL_HORIZONTAL );
			dataSetCombo.setLayoutData( data );
			dataSetCombo.setVisibleItemCount( 30 );
			dataSetCombo.addSelectionListener( new SelectionAdapter( ) {

				public void widgetSelected( SelectionEvent e )
				{
					Object input = populateInput( );

					if ( input instanceof Map )
					{
						if ( columnTreeViewer == null )
						{
							disposeChildren( contentPane );
							columnTableViewer = null;
							createColumnBindingTreeContents( contentPane, input );
						}
					}
					else
					{
						if ( columnTableViewer == null )
						{
							disposeChildren( contentPane );
							columnTreeViewer = null;
							createColumnBindingTableContents( contentPane,
									input );
						}
					}

					handleDataSetComboSelectedEvent( input );
				}

			} );
		}
	}

	private void disposeChildren( Composite parent )
	{
		Control[] cc = parent.getChildren( );

		if ( cc != null )
		{
			for ( Control c : cc )
			{
				c.dispose( );
			}
		}
	}

	private String[] getDataSetComboList() {
		ModuleHandle handle = getModel();
		String[] comboList = new String[datasets.size() + 1];
		comboList[0] = NONE;
		for (int i = 0; i < datasets.size(); i++) {
			comboList[i + 1] = datasets.get(i).getQualifiedName();
			if (handle.findDataSet(comboList[i + 1]) != datasets.get(i)) {
				comboList[i + 1] += Messages
						.getString("BindingGroupDescriptorProvider.Flag.DataModel");
			}
		}
		return comboList;
	}

	private DataSetHandle getSelectedDataSet( )
	{
		if ( dataSetCombo.getSelectionIndex( ) > 0 )
		{
			return (DataSetHandle) datasets.get( dataSetCombo.getSelectionIndex( ) - 1 );
		}
		return null;
	}

	private ModuleHandle getModel() {
		return SessionHandleAdapter.getInstance().getReportDesignHandle();
	}

	private void initDateSetHandles() {

		datasets = org.eclipse.birt.report.designer.internal.ui.util.UIUtil
				.getVisibleDataSetHandles(getModel());
	}

	private Object populateInput( )
	{
		Object input = null;
		DataSetHandle handle = null;

		if ( datasetHandle != null )
		{
			handle = datasetHandle;

			LinkedDataSetAdapter adapter = new LinkedDataSetAdapter( );
			for ( Iterator iterator = adapter.getVisibleLinkedDataSetsDataSetHandles( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) )
					.iterator( ); iterator.hasNext( ); )
			{
				DataSetHandle dataSetHandle = (DataSetHandle) iterator.next( );
				if ( dataSetHandle.getQualifiedName( ).equals( dataSetName ) )
				{
					// if dataet is linkeddatamodel, reset the handle to get
					// grouped view.
					handle = null;
					break;
				}
			}
		}
		else if ( dataSetName != null )
		{
			handle = DataUtil.findDataSet( dataSetName );
		}
		else
		{
			handle = getSelectedDataSet( );
		}
		if ( handle != null )
		{
			try
			{
				CachedMetaDataHandle cmdh = DataSetUIUtil.getCachedMetaDataHandle( handle );
				input = cmdh.getResultSet( ).iterator( );
			}
			catch ( SemanticException e )
			{
				ExceptionHandler.handle( e );
			}
		}
		else
		{
			input = new LinkedDataSetAdapter( ).getGroupedResultSetColumns( dataSetName );
		}

		return input;
	}

	protected void handleDataSetComboSelectedEvent( Object input )
	{
		if ( input instanceof Iterator )
		{
			columnTableViewer.setInput( input );
		}
		else if ( input instanceof Map )
		{
			columnTreeViewer.setInput( input );
			columnTreeViewer.expandAll( );
		}
		else
		{
			if ( columnTableViewer != null )
			{
				columnTableViewer.setInput( null );
			}

			if ( columnTreeViewer != null )
			{
				columnTreeViewer.setInput( null );
			}
		}

	}

	protected void okPressed( )
	{

		result = new Object[3];
		if ( dataSetName != null || dataSetCombo.getSelectionIndex( ) > 0 )
		{
			if ( dataSetName == null )
			{
				result[0] = getSelectedDataSet( );
				result[0] = getSelectedDataSet();
			}
			else
			{
				result[0] = datasetHandle;
			}

			if ( columnTableViewer != null )
			{
				if ( columnTableViewer.getCheckedElements( ) != null )
				{
					result[1] = columnTableViewer.getCheckedElements( );
					List<String> list = new ArrayList<String>( );
					for ( int i = 0; i < columnTableViewer.getTable( )
							.getItemCount( ); i++ )
					{
						ResultSetColumnHandle column = (ResultSetColumnHandle) columnTableViewer.getElementAt( i );
						if ( !columnTableViewer.getChecked( column ) )
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
					for ( int i = 0; i < columnTableViewer.getTable( )
							.getItemCount( ); i++ )
					{
						ResultSetColumnHandle column = (ResultSetColumnHandle) columnTableViewer.getElementAt( i );
						if ( !columnTableViewer.getChecked( column ) )
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
				if ( columnTreeViewer.getCheckedElements( ) != null )
				{
					Object[] selection = columnTreeViewer.getCheckedElements( );

					List<ResultSetColumnHandle> cols = new ArrayList<ResultSetColumnHandle>( );

					for ( Object obj : selection )
					{
						if ( obj instanceof ResultSetColumnHandle )
						{
							cols.add( (ResultSetColumnHandle) obj );
						}
					}
					result[1] = cols.toArray( );

					List<String> list = new ArrayList<String>( );
					for ( int i = 0; i < columnTreeViewer.getTree( )
							.getItemCount( ); i++ )
					{
						TreeItem ti = columnTreeViewer.getTree( ).getItem( i );

						for ( int j = 0; j < ti.getItemCount( ); j++ )
						{
							TreeItem sti = ti.getItem( j );

							if ( !sti.getChecked( ) )
							{
								list.add( ( (ResultSetColumnHandle) sti.getData( ) ).getColumnName( ) );
							}
						}
					}
					result[2] = list.toArray( );
				}
				else
				{
					result[1] = null;
					List<String> list = new ArrayList<String>( );
					for ( int i = 0; i < columnTreeViewer.getTree( )
							.getItemCount( ); i++ )
					{
						TreeItem ti = columnTreeViewer.getTree( ).getItem( i );

						for ( int j = 0; j < ti.getItemCount( ); j++ )
						{
							TreeItem sti = ti.getItem( j );

							if ( !sti.getChecked( ) )
							{
								list.add( ( (ResultSetColumnHandle) sti.getData( ) ).getColumnName( ) );
							}
						}
					}

					if ( list.isEmpty( ) )
						result[2] = null;
					else
						result[2] = list.toArray( );
				}
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
		setDataSet( dataSetName, true );
	}

	public void setDataSet( String dataSetName, boolean isDataSet )
	{
		this.dataSetName = dataSetName;
		if ( isDataSet )
		{
			datasetHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.findDataSet( dataSetName );
		}
		else
		{
			LinkedDataSetAdapter adapter = new LinkedDataSetAdapter( );
			for ( Iterator iterator = adapter.getVisibleLinkedDataSetsDataSetHandles( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ) )
					.iterator( ); iterator.hasNext( ); )
			{
				DataSetHandle dataSetHandle = (DataSetHandle) iterator.next( );
				if ( dataSetHandle.getQualifiedName( ).equals( dataSetName ) )
				{
					this.datasetHandle = dataSetHandle;
					break;
				}
			}
		}
	}

	private static String getDataTypeDisplayName( String dataType )
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

	private void enableOKButton( )
	{
		if ( getOkButton( ) != null && !getOkButton( ).isDisposed( ) )
		{
			if ( validateEmptyResults )
			{
				if ( columnTableViewer != null )
				{
					getOkButton( ).setEnabled( columnTableViewer.getCheckedElements( ).length > 0 );
				}
				else
				{
					getOkButton( ).setEnabled( columnTreeViewer.getCheckedElements( ).length > 0 );
				}
			}
		}
	}

	public void setColumns( String[] columns )
	{
		this.columns = columns;
	}

	public void setValidateEmptyResults( boolean validateEmptyResults )
	{
		this.validateEmptyResults = validateEmptyResults;
	}

	public DataSetHandle getDatasetHandle( )
	{
		return datasetHandle;
	}

	public void setDatasetHandle( DataSetHandle datasetHandle )
	{
		if ( datasetHandle != null )
		{
			this.datasetHandle = datasetHandle;
			this.dataSetName = datasetHandle.getQualifiedName( );
		}
	}

}
