/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.data.ui.util.DTPUtil;
import org.eclipse.birt.report.designer.data.ui.util.DataSetExecutorHelper;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.data.ui.util.Utility;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.designer.ui.preferences.DateSetPreferencePage;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.engine.executor.ExecutionContext;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.OdaDataSetParameterHandle;
import org.eclipse.birt.report.model.api.ParameterHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ScalarParameterHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Preferences;
import org.eclipse.core.runtime.Status;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.UIJob;

/**
 * Property page to preview the resultset.
 * 
 */

public class ResultSetPreviewPage extends AbstractPropertyPage
		implements
			Listener
{

	private TableViewer resultSetTableViewer = null;
	private transient Table resultSetTable = null;
	private boolean modelChanged = true;
	private boolean needsUpdateUI = true;
	private int columnCount = -1;
	private List recordList = null;
	private DataSetViewData[] metaData;
	
	private List errorList = new ArrayList();
	private String[] columnBindingNames;
	private int previousMaxRow = -1;
	private CLabel promptLabel;

	/**
	 * The constructor.
	 */
	public ResultSetPreviewPage( )
	{
		super( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl( Composite parent )
	{
		Composite resultSetComposite = new Composite( parent, SWT.NONE );
		GridLayout layout = new GridLayout( );
		layout.verticalSpacing = 15;
		resultSetComposite.setLayout( layout );
		resultSetComposite.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		resultSetTable = new Table( resultSetComposite, SWT.FULL_SELECTION | SWT.MULTI | SWT.VIRTUAL | SWT.BORDER );
		resultSetTable.setHeaderVisible( true );
		resultSetTable.setLinesVisible( true );
		resultSetTable.setLayoutData( new GridData( GridData.FILL_BOTH ) );
		( (DataSetHandle) getContainer( ).getModel( ) ).addListener( this );

		resultSetTable.addMouseListener( new MouseAdapter( ) {

			public void mouseUp( MouseEvent e )
			{
				// if not mouse left button
				if ( e.button != 1 )
				{
					MenuManager menuManager = new MenuManager( );

					ResultSetTableAction copyAction = ResultSetTableActionFactory.createResultSetTableAction( resultSetTable,
							ResultSetTableActionFactory.COPY_ACTION );
					ResultSetTableAction selectAllAction = ResultSetTableActionFactory.createResultSetTableAction( resultSetTable,
							ResultSetTableActionFactory.SELECTALL_ACTION );
					menuManager.add( copyAction );
					menuManager.add( selectAllAction );

					menuManager.update( );

					copyAction.update( );
					selectAllAction.update( );

					Menu contextMenu = menuManager.createContextMenu( resultSetTable );

					contextMenu.setEnabled( true );
					contextMenu.setVisible( true );
				}
			}
		} );

		createResultSetTableViewer( );
		promptLabel = new CLabel( resultSetComposite, SWT.WRAP );
		GridData labelData = new GridData( GridData.FILL_HORIZONTAL );
		promptLabel.setLayoutData( labelData );
		
		return resultSetComposite;
	}

	private void createResultSetTableViewer( )
	{
		resultSetTableViewer = new TableViewer( resultSetTable );
		resultSetTableViewer.setSorter( null );
		resultSetTableViewer.setContentProvider( new IStructuredContentProvider( ) {

			public Object[] getElements( Object inputElement )
			{
				if ( inputElement instanceof List )
				{
					return ( (List) inputElement ).toArray( );
				}

				return new Object[0];
			}

			public void inputChanged( Viewer viewer, Object oldInput,
					Object newInput )
			{
			}

			public void dispose( )
			{
			}
		} );
		resultSetTableViewer.setLabelProvider( new ITableLabelProvider( ) {

			public Image getColumnImage( Object element, int columnIndex )
			{
				return null;
			}

			public String getColumnText( Object element, int columnIndex )
			{
				return ( (CellValue[]) element )[columnIndex].getDisplayValue( );
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
		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated( )
	{
		getContainer( ).setMessage( Messages.getString( "dataset.editor.preview" ),//$NON-NLS-1$
				IMessageProvider.NONE );

		if ( modelChanged
				|| ( (DataSetEditor) this.getContainer( ) ).modelChanged( ) )
		{
			modelChanged = false;

			new UIJob( "" ) { //$NON-NLS-1$

				public IStatus runInUIThread( IProgressMonitor monitor )
				{
					updateResultsProcess( );
					return Status.OK_STATUS;
				}
			}.schedule( );
		}
	}

	protected final void clearResultSetTable( )
	{
		if ( recordList == null )
			recordList = new ArrayList( );
		else
			recordList.clear( );
		
		// clear everything else
		resultSetTable.removeAll( );
		
		// Clear the columns
		TableColumn[] columns = resultSetTable.getColumns( );
		for ( int n = 0; n < columns.length; n++ )
		{
			columns[n].dispose( );
		}
		
	}

	/**
	 * Get resultSet
	 * 
	 * @return
	 */
	private IQueryResults executeProcess( DataRequestSession session, ExecutionContext context )
	{
		errorList = new ArrayList( );
		try
		{
			
			metaData = ( (DataSetEditor) this.getContainer( ) ).getCurrentItemModel( );
			columnCount = metaData == null ? 0 : metaData.length;

			// Create a new Report Query definition for retrieving the
			// columns
			QueryDefinition query = new QueryDefinition( );

			
			query.setDataSetName( ( (DataSetEditor) getContainer( ) ).getHandle( ).getQualifiedName( ) );

			int maxRow = getMaxRowPreference( );
			query.setMaxRows( maxRow );
			registerParameterBinding( query,
					( (DataSetEditor) getContainer( ) ).getHandle( ), session );

			columnBindingNames = new String[columnCount];
			ScriptExpression[] expressions = new ScriptExpression[columnCount];

			for ( int n = 0; n < columnCount; n++ )
			{
				columnBindingNames[n] = metaData[n].getName( );
				expressions[n] = new ScriptExpression( ExpressionUtil.createJSDataSetRowExpression( metaData[n].getName( ) ) );
				expressions[n].setDataType( metaData[n].getDataType( ) );
				query.addResultSetExpression( columnBindingNames[n],
						expressions[n] );
			}

			boolean needCache = false;
			if ( this.previousMaxRow != maxRow )
			{
				this.previousMaxRow = maxRow;
				needCache = true;
			}
			DataSetExecutorHelper helper = new DataSetExecutorHelper( );
			ExternalUIUtil.populateApplicationContext( ( (DataSetEditor) getContainer( ) ).getHandle( ), session );
			IQueryResults resultSet = helper.execute( ( (DataSetEditor) getContainer( ) ).getHandle( ),
					query,
					true,
					true,
					needCache,
					context,
					session );
			return resultSet;
		}
		catch ( BirtException e )
		{
			errorList.add( e );
			return null;
		}
	}

	private void registerParameterBinding( QueryDefinition query, DataSetHandle dsHandle, DataRequestSession session )
			throws BirtException, DesignFileException
	{
		PropertyHandle handle = dsHandle.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );

		if ( handle != null )
		{
			Iterator paramIter = handle.iterator( );
			while ( paramIter.hasNext( ) )
			{
				Expression value = null;
				DataSetParameterHandle paramDefn = (DataSetParameterHandle) paramIter.next( );
				if ( paramDefn.isInput( ) )
				{
					if ( paramDefn instanceof OdaDataSetParameterHandle
							&& ( (OdaDataSetParameterHandle) paramDefn ).getParamName( ) != null )
					{
						// get the value from report parameter
						value = this.getParameterValueExpression( dsHandle,
								(OdaDataSetParameterHandle) paramDefn );
					}
					if ( value != null )
					{
						InputParameterBinding binding = new InputParameterBinding( paramDefn.getName( ),
								session.getModelAdaptor( )
										.adaptExpression( value ) );
						query.addInputParamBinding( binding );
					}
				}
			}
		}
	}
	
	private Expression getParameterValueExpression( DataSetHandle dsHandle,
			OdaDataSetParameterHandle paramDefn ) throws BirtException
	{
		Expression value = ExternalUIUtil.getParamValueExpression( dsHandle, paramDefn );
		return value;
	}
		
	private int getMaxRowPreference( )
	{
		int maxRow;
		Preferences preferences = ReportPlugin.getDefault( )
				.getPluginPreferences( );
		if ( preferences.contains( DateSetPreferencePage.USER_MAXROW ) )
		{
			maxRow = preferences.getInt( DateSetPreferencePage.USER_MAXROW );
		}
		else
		{
			maxRow = DateSetPreferencePage.DEFAULT_MAX_ROW;
			preferences.setValue( DateSetPreferencePage.USER_MAXROW, maxRow );
		}
		return maxRow;
	}

	/**
	 * Show ProgressMonitorDialog
	 * 
	 */
	private void updateResultsProcess( )
	{
		needsUpdateUI = true;
		clearResultSetTable( );
		
		IRunnableWithProgress runnable = new IRunnableWithProgress( ) {

			public void run( IProgressMonitor monitor )
					throws InvocationTargetException, InterruptedException
			{
				monitor.beginTask( "", IProgressMonitor.UNKNOWN ); //$NON-NLS-1$

				if ( resultSetTable != null && !resultSetTable.isDisposed( ) )
				{

					// Set thread context class loader so Rhino can find POJOs
					// in
					// workspace
					// projects
					ClassLoader oldContextLoader = Thread.currentThread( )
							.getContextClassLoader( );
					ClassLoader parentLoader = oldContextLoader;
					if ( parentLoader == null )
						parentLoader = this.getClass( ).getClassLoader( );
					
					ModuleHandle handle;
					DataSetHandle dsHandle = ( (DataSetEditor) getContainer( ) ).getHandle( );
					handle = dsHandle.getModuleHandle( );

					Map dataSetBindingMap = new HashMap( );
					Map dataSourceBindingMap = new HashMap( );
					try
					{
						if ( handle instanceof ReportDesignHandle )
						{
							ReportDesignHandle copiedReport = (ReportDesignHandle) ( handle.copy( ).getHandle( null ) );

							ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader( parentLoader,
									copiedReport );
							Thread.currentThread( )
									.setContextClassLoader( newContextLoader );
							
							EngineConfig ec = new EngineConfig( );
							ec.getAppContext( )
									.put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
											newContextLoader );
							ReportEngine engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( ec );
							DataSetUIUtil.clearPropertyBindingMap( dsHandle,
									dataSetBindingMap,
									dataSourceBindingMap );

							DummyEngineTask engineTask = new DummyEngineTask( engine,
									new ReportEngineHelper( engine ).openReportDesign( copiedReport ),
									copiedReport );
							DataRequestSession session = engineTask.getDataSession( );
							session.getDataSessionContext( ).getAppContext( ).put( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS, 
									DTPUtil.getInstance( ).createResourceIdentifiers( ));

							Map appContext = new HashMap( );
							appContext.put( DataEngine.MEMORY_DATA_SET_CACHE,
									new Integer( ( (DataSetHandle) getContainer( ).getModel( ) ).getRowFetchLimit( ) ) );

							engineTask.setAppContext( appContext );
							engineTask.run( );
							
							IQueryResults resultSet = executeProcess( session, engineTask.getExecutionContext( ) );
							populateRecords( resultSet );
							engineTask.close( );
							engine.destroy( );
							monitor.done( );
						}
						else
						{
							DataSessionContext context;
							context = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
									( (DataSetEditor) getContainer( ) ).getHandle( )
											.getModuleHandle( ) );
							
							DataRequestSession session = DataRequestSession.newSession( context );

							Map appContext = new HashMap( );
							appContext.put( DataEngine.MEMORY_DATA_SET_CACHE,
									new Integer( ( (DataSetHandle) getContainer( ).getModel( ) ).getRowFetchLimit( ) ) );
							appContext.put( ResourceIdentifiers.ODA_APP_CONTEXT_KEY_CONSUMER_RESOURCE_IDS, 
									DTPUtil.getInstance( ).createResourceIdentifiers( ));
							if ( context.getAppContext( ) != null )
							{
								appContext.putAll( context.getAppContext( ) );
							}
							context.setAppContext( appContext );
							IQueryResults resultSet = executeProcess( session, null );
							populateRecords( resultSet );
							session.shutdown( );
						}
					}
					catch ( BirtException e )
					{
						ExceptionHandler.handle( e );
					}
					finally
					{
						try
						{
							DataSetUIUtil.resetPropertyBinding( dsHandle,
									dataSetBindingMap,
									dataSourceBindingMap );
						}
						catch ( SemanticException e )
						{
						}
					}		

					// Restore old thread context class loader
					Thread.currentThread( )
							.setContextClassLoader( oldContextLoader );
				}
			}
		};
		try
		{
			new ProgressMonitorDialog( PlatformUI.getWorkbench( )
					.getDisplay( )
					.getActiveShell( ) ) {

				protected void cancelPressed( )
				{
					super.cancelPressed( );
					needsUpdateUI = false;
				}

			}.run( true, true, runnable );
		}
		catch ( InvocationTargetException e )
		{
			ExceptionHandler.handle( e );
		}
		catch ( InterruptedException e )
		{
			ExceptionHandler.handle( e );
		}
		
		updateResultSetTableUI( );
	}

	/**
	 * Populate records to be retrieved when re-render resultSetTable
	 * 
	 * @param metaData
	 * @param query
	 * @throws BirtException
	 */
	private void populateRecords( IQueryResults actualResultSet )
	{
		try
		{
			if ( actualResultSet != null )
			{
				IResultIterator iter = actualResultSet.getResultIterator( );
				if ( columnCount > 0 )
				{
					while ( iter.next( ) )
					{
						CellValue[] record = new CellValue[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							CellValue cv = new CellValue( );
							Object value = iter.getValue( columnBindingNames[n] );
							String disp = null;
							if( value instanceof Number )
								disp = value.toString( );
							else
								disp = iter.getString( columnBindingNames[n] );
							cv.setDisplayValue( disp );
							cv.setRealValue( value );
							record[n] = cv;
						}
						recordList.add( record );
					}
				}
				setPromptLabelText( );
				actualResultSet.close( );
			}
		}
		catch ( RuntimeException e )
		{
			errorList.add( e );
		}
		catch ( BirtException e )
		{
			errorList.add( e );
		}
	}

	/**
	 * Set the prompt label text
	 * 
	 */
	private void setPromptLabelText( )
	{
		Display.getDefault( ).syncExec( new Runnable( ) {

			public void run( )
			{
				String prompt = "";
				prompt = Messages.getFormattedString( "dataset.resultset.preview.promptMessage.recordsNum",
						new Object[]{
							recordList.size( )
						} );
				if ( recordList != null )
				{
					if ( recordList.size( ) >= getMaxRowPreference( ) )
					{
						prompt += " " + Messages.getString( "dataset.resultset.preview.promptMessage.MoreRecordsExist" );
					}
				}
				if ( promptLabel != null )
				{
					promptLabel.setText( prompt );
				}
			}
		} );
	}

	private void updateResultSetTableUI( )
	{
		if ( !needsUpdateUI )
			return;

		if ( !errorList.isEmpty( ) )
		{
			setPromptLabelText( );
			ExceptionHandler.handle( (Exception) errorList.get( 0 ) );
		}
		else
		{
			if ( metaData != null )
				createColumns( metaData );
			insertRecords( );
		}
	}

	private void createColumns( DataSetViewData[] rsMd )
	{
		DataSetViewData[] columnsModel = rsMd;
		TableColumn column = null;
		TableLayout layout = new TableLayout( );

		for ( int n = 0; n < rsMd.length; n++ )
		{
			column = new TableColumn( resultSetTable, SWT.LEFT );

			column.setText( getColumnDisplayName( columnsModel, n ) );
			column.setResizable( true );
			layout.addColumnData( new ColumnPixelData( 120, true ) );
			addColumnSortListener( column, n );
			column.pack( );
		}
		resultSetTable.setLayout( layout );
		resultSetTable.layout( true );
	}

	private void insertRecords( )
	{
		resultSetTableViewer.setInput( recordList );
	}

	private String getColumnDisplayName( DataSetViewData[] columnsModel,
			int index )
	{
		if ( columnsModel == null
				|| columnsModel.length == 0 || index < 0
				|| index > columnsModel.length )
		{
			return "";//$NON-NLS-1$
		}

		return columnsModel[index].getDisplayName( );
	}

	/**
	 * Add listener to a column
	 * 
	 * @param column
	 * @param n
	 */
	private void addColumnSortListener( TableColumn column, final int index )
	{
		column.addSelectionListener( new SelectionListener( ) {

			private boolean asc = false;

			public void widgetSelected( SelectionEvent e )
			{
				sort( index, asc );
				asc = !asc;
			}

			public void widgetDefaultSelected( SelectionEvent e )
			{

			}
		} );
	}

	/**
	 * Carry out sort operation against certain column
	 * 
	 * @param columnIndex
	 *            the column based on which the sort operation would be carried
	 *            out
	 * @param asc
	 *            the sort direction
	 */
	private void sort( final int columnIndex, final boolean asc )
	{
		resultSetTable.setSortColumn( resultSetTable.getColumn( columnIndex ) );
		resultSetTable.setSortDirection( asc == true ? SWT.DOWN : SWT.UP );
		this.resultSetTableViewer.setSorter( new ViewerSorter( ) {

			// @Override
			public int compare( Viewer viewer, Object e1, Object e2 )
			{
				CellValue cv1 = ( (CellValue[]) e1 )[columnIndex];
				CellValue cv2 = ( (CellValue[]) e2 )[columnIndex];
				int result = 0;
				if ( cv1 == null && cv1 != cv2 )
					result = -1;
				else if ( cv1 != null )
					result = cv1.compareTo( cv2 );
				if ( !asc )
					return result;
				else
					return result * -1;
			}

		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( focus.equals( getContainer( ).getModel( ) )
				|| ( (DataSetEditor) this.getContainer( ) ).modelChanged( ) )
		{
			modelChanged = true;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performCancel()
	 */
	public boolean performCancel( )
	{
		( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
		return super.performCancel( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
		return super.performOk( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#getToolTip()
	 */
	public String getToolTip( )
	{
		return Messages.getString( "dataset.resultset.preview.tooltip" ); //$NON-NLS-1$
	}

}

/**
 * The Action factory
 */
final class ResultSetTableActionFactory
{

	public static final int COPY_ACTION = 1;
	public static final int SELECTALL_ACTION = 2;

	public static ResultSetTableAction createResultSetTableAction(
			Table resultSetTable, int operationID )
	{
		assert resultSetTable != null;

		ResultSetTableAction rsTableAction = null;

		if ( operationID == COPY_ACTION )
		{
			rsTableAction = new CopyAction( resultSetTable );
		}
		else if ( operationID == SELECTALL_ACTION )
		{
			rsTableAction = new SelectAllAction( resultSetTable );
		}

		return rsTableAction;
	}
}

/**
 * An implementation of Action
 */
abstract class ResultSetTableAction extends Action
{

	protected Table resultSetTable = null;

	public ResultSetTableAction( Table resultSetTable, String actionName )
	{
		super( actionName );
		this.resultSetTable = resultSetTable;
	}

	/**
	 * This method update the state of the action. Particularly, it will disable
	 * the action under certain circumstance.
	 */
	public abstract void update( );
}

/**
 * Copy action.
 */
final class CopyAction extends ResultSetTableAction
{

	/**
	 * @param resultSetTable
	 *            the ResultSetTable against which the action is applied to
	 */
	public CopyAction( Table resultSetTable )
	{
		super( resultSetTable, Messages.getString( "CopyAction.text" ) ); //$NON-NLS-1$
		this.setImageDescriptor( Utility.getImageDescriptor( ISharedImages.IMG_TOOL_COPY ) );
	}

	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		StringBuffer textData = new StringBuffer( );

		for ( int i = 0; i < resultSetTable.getColumnCount( ); i++ )
		{
			textData.append( resultSetTable.getColumn( i ).getText( ) + "\t" ); //$NON-NLS-1$
		}
		textData.append( "\n" ); //$NON-NLS-1$

		TableItem[] tableItems = resultSetTable.getSelection( );
		for ( int i = 0; i < tableItems.length; i++ )
		{
			for ( int j = 0; j < resultSetTable.getColumnCount( ); j++ )
			{
				textData.append( tableItems[i].getText( j ) + "\t" ); //$NON-NLS-1$
			}
			textData.append( "\n" ); //$NON-NLS-1$
		}

		Clipboard clipboard = new Clipboard( resultSetTable.getDisplay( ) );
		clipboard.setContents( new Object[]{
			textData.toString( )
		}, new Transfer[]{
			TextTransfer.getInstance( )
		} );
		clipboard.dispose( );
	}

	/*
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.ResultSetTableAction#update()
	 */
	public void update( )
	{
		if ( resultSetTable.getItems( ).length < 1
				|| resultSetTable.getSelectionCount( ) < 1 )
		{
			this.setEnabled( false );
		}
	}
}

/**
 * Select All Action
 */
final class SelectAllAction extends ResultSetTableAction
{

	/**
	 * @param resultSetTable
	 *            the ResultSetTable against which the action is applied to
	 */
	public SelectAllAction( Table resultSetTable )
	{
		super( resultSetTable, Messages.getString( "SelectAllAction.text" ) ); //$NON-NLS-1$
	}

	/*
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		resultSetTable.selectAll( );
	}

	/*
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.ResultSetTableAction#update()
	 */
	public void update( )
	{
		if ( resultSetTable.getItems( ).length < 1 )
		{
			this.setEnabled( false );
		}
	}
}

final class CellValue implements Comparable
{
	private Object realValue;
	private String displayValue;

	public int compareTo( Object o )
	{
		if ( o == null )
		{
			return 1;
		}
		CellValue other = (CellValue) o;
		try
		{
			return ScriptEvalUtil.compare( this.realValue, other.realValue);
		}
		catch ( DataException e )
		{
			// should never get here
			assert ( false );
			return -1;
		}
	}

	public String toString( )
	{
		return displayValue == null ? "" : displayValue; //$NON-NLS-1$
	}

	public void setRealValue( Object realValue )
	{
		this.realValue = realValue;
	}

	public void setDisplayValue( String displayValue )
	{
		this.displayValue = displayValue;
	}

	public String getDisplayValue( )
	{
		return displayValue;
	}
}