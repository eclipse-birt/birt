/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.designer.data.ui.dataset;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.designer.data.ui.util.DataSetExecutorHelper;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.dialogs.properties.AbstractPropertyPage;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.core.Listener;
import org.eclipse.birt.report.model.api.elements.structures.DataSetParameter;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.PlatformUI;

/**
 * Property page to preview the output parameters.
 * 
 */

public class OutputParameterPreviewPage extends AbstractPropertyPage
		implements
			Listener
{
	private Table outputParameterTable = null;
	private boolean modelChanged = true;
	
	private static final String PREFIX_OUTPUTPARAMETER = "outputParams"; //$NON-NLS-1$
	private static final String PREFIX_PARAMETER = "PARAMS_"; //$NON-NLS-1$
	
	/**
	 * The constructor.
	 */
	public OutputParameterPreviewPage( )
	{
		super( );
	}

	/*
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#createPageControl(org.eclipse.swt.widgets.Composite)
	 */
	public Control createPageControl( Composite parent )
	{
		outputParameterTable = new Table( parent, SWT.FULL_SELECTION
				| SWT.MULTI );
		outputParameterTable.setHeaderVisible( true );
		outputParameterTable.setLinesVisible( true );
		( (DataSetHandle) getContainer( ).getModel( ) ).addListener( this );

		outputParameterTable.addMouseListener( new MouseAdapter( ) {

			/*
			 *  (non-Javadoc)
			 * @see org.eclipse.swt.events.MouseListener#mouseUp(org.eclipse.swt.events.MouseEvent)
			 */
			public void mouseUp( MouseEvent e )
			{
				// if not mouse left button
				if ( e.button != 1 )
				{
					MenuManager menuManager = new MenuManager( );

					ResultSetTableAction copyAction = ResultSetTableActionFactory.createResultSetTableAction( outputParameterTable,
							ResultSetTableActionFactory.COPY_ACTION );
					ResultSetTableAction selectAllAction = ResultSetTableActionFactory.createResultSetTableAction( outputParameterTable,
							ResultSetTableActionFactory.SELECTALL_ACTION );
					menuManager.add( copyAction );
					menuManager.add( selectAllAction );

					menuManager.update( );

					copyAction.update( );
					selectAllAction.update( );

					Menu contextMenu = menuManager.createContextMenu( outputParameterTable );

					contextMenu.setEnabled( true );
					contextMenu.setVisible( true );
				}
			}
		} );

		return outputParameterTable;
	}

	/*
	 * @see org.eclipse.birt.model.core.Listener#elementChanged(org.eclipse.birt.model.api.DesignElementHandle,
	 *      org.eclipse.birt.model.activity.NotificationEvent)
	 */
	public void elementChanged( DesignElementHandle focus, NotificationEvent ev )
	{
		if ( focus.equals( getContainer( ).getModel( ) ) )
		{
			modelChanged = true;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.IPropertyPage#pageActivated()
	 */
	public void pageActivated( )
	{
		getContainer( ).setMessage( Messages.getString( "dataset.editor.outputparameters" ),//$NON-NLS-1$
				IMessageProvider.NONE );

		if ( modelChanged
				|| ( (DataSetEditor) this.getContainer( ) ).modelChanged( ) )
		{
			modelChanged = false;
			runUpdateResults( );
		}
	}

	/**
	 * Update table result
	 */
	private void runUpdateResults( )
	{
		if ( outputParameterTable != null && !outputParameterTable.isDisposed( ) )
		{
			clearResultSetTable( );
			PlatformUI.getWorkbench( )
					.getDisplay( )
					.asyncExec( new Runnable( ) {
						/*
						 *  (non-Javadoc)
						 * @see java.lang.Runnable#run()
						 */
						public void run( )
						{
							if ( outputParameterTable != null
									&& !outputParameterTable.isDisposed( ) )
							{
								updateResults( );
							}
						}
					} );
		}
	}
	
	/**
	 * Clear table result and release its UI resource
	 */
	private void clearResultSetTable( )
	{
		// Clear the columns
		TableColumn[] columns = outputParameterTable.getColumns( );
		for ( int n = 0; n < columns.length; n++ )
		{
			columns[n].dispose( );
		}
		// clear everything else
		outputParameterTable.removeAll( );
	}
	
	/**
	 * update output parameter table result
	 */
	private void updateResults( )
	{
		int outputParamsSize = outputParametersSize( );
		if ( outputParamsSize == 0 )
			return;
		
		DataRequestSession session = null;
		try
		{
			
			ModuleHandle handle = null;
			DataSetHandle dsHandle = ( (DataSetEditor) getContainer( ) ).getHandle( );
			handle = dsHandle.getModuleHandle( );
			EngineConfig ec = new EngineConfig( );
			ClassLoader parent = Thread.currentThread( ).getContextClassLoader( );
			if ( parent == null )
			{
				parent = this.getClass( ).getClassLoader( );
			}
			ClassLoader customClassLoader = DataSetProvider.getCustomScriptClassLoader( parent, handle );
			ec.getAppContext( ).put( EngineConstants.APPCONTEXT_CLASSLOADER_KEY,
					customClassLoader );
			
			ReportDesignHandle copiedReport = (ReportDesignHandle) ( handle.copy( ).getHandle( null ) );
			
			ReportEngine engine = (ReportEngine)new ReportEngineFactory( ).createReportEngine( ec );
			
			DummyEngineTask engineTask = new DummyEngineTask( engine,
					new ReportEngineHelper( engine ).openReportDesign( copiedReport ),
					copiedReport );
			
			Map appContext = new HashMap( );
			appContext.put( DataEngine.MEMORY_DATA_SET_CACHE,
					Integer.valueOf( ( (DataSetHandle) getContainer( ).getModel( ) ).getRowFetchLimit( ) ) );

			engineTask.setAppContext( appContext );
			engineTask.run( );
			
			session = engineTask.getDataSession( );
			
			// query defintion
			QueryDefinition query = new QueryDefinition( );
			query.setDataSetName( ( (DataSetEditor) getContainer( ) ).getHandle( )
					.getQualifiedName( ) );
			query.setMaxRows( 1 );

			PropertyHandle propertyHandle = ( (DataSetEditor) getContainer( ) ).getHandle( )
					.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );
			int paramsSize = propertyHandle.getListValue( ).size( );
			Iterator paramIter = propertyHandle.iterator( );

			int outputParamIndex = 0;
			TableLayout layout = new TableLayout( );
			TableColumn column = null;
			TableItem tableItem = null;

			List paramColumnBindingNames = new ArrayList( );
			for ( int n = 1; n <= paramsSize; n++ )
			{
				DataSetParameterHandle paramDefn = (DataSetParameterHandle) paramIter.next( );

				// get output parameters alone
				if ( !paramDefn.isOutput( ) )
					continue;

				column = new TableColumn( outputParameterTable, SWT.LEFT );
				column.setText( paramDefn.getName( ) );
				column.setResizable( true );
				layout.addColumnData( new ColumnPixelData( 120, true ) );

				String bindingName = PREFIX_PARAMETER + ( outputParamIndex++ );
				IBinding binding = new Binding( bindingName );
				binding.setExpression( new ScriptExpression( PREFIX_OUTPUTPARAMETER
						+ "[\"" + paramDefn.getName( ) + "\"]" ) ); //$NON-NLS-1$ //$NON-NLS-2$
				binding.setDataType( DataAdapterUtil.adaptModelDataType( paramDefn.getDataType( ) ) );
				paramColumnBindingNames.add( bindingName );
				query.addBinding( binding );
				column.pack( );
			}

			outputParameterTable.setLayout( layout );
			outputParameterTable.layout( true );

			DataSetExecutorHelper helper = new DataSetExecutorHelper( );
			IQueryResults actualResultSet = helper.execute( ( (DataSetEditor) getContainer( ) ).getHandle( ),
					query,
					true,
					true,
					session );
			if ( actualResultSet != null )
			{
				IResultIterator iter = actualResultSet.getResultIterator( );
				iter.next( );

				String[] record = new String[outputParamIndex];
				for ( int n = 0; n < record.length; n++ )
				{
					record[n] = iter.getString( paramColumnBindingNames.get( n )
							.toString( ) );
				}
				tableItem = new TableItem( outputParameterTable, SWT.NONE );
				tableItem.setText( record );

				actualResultSet.close( );

			}
		}
		catch ( BirtException ex )
		{
			ExceptionHandler.handle( ex );
		}
		finally
		{
			if ( session != null )
			{
				session.shutdown( );
			}
		}
	}
	
	/**
	 * @return the count of output parameters
	 */
	private int outputParametersSize( )
	{
		// first check whether parameter list is null
		PropertyHandle propertyHandle = ( (DataSetEditor) getContainer( ) ).getHandle( )
				.getPropertyHandle( DataSetHandle.PARAMETERS_PROP );
		List paramList = propertyHandle.getListValue( );
		if ( paramList == null || paramList.size( ) == 0 )
			return 0;

		// second check whether there is output parameter
		int size = 0;
		int paramSize = paramList.size( );
		for ( int i = 0; i < paramSize; i++ )
		{
			DataSetParameter parameter = (DataSetParameter) paramList.get( i );
			if ( parameter.isOutput( ) == true )
			{
				size++;
			}
		}

		return size;
	}
	
	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performOk()
	 */
	public boolean performOk( )
	{
		( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
		return super.performOk( );
	}
	
	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#performCancel()
	 */
	public boolean performCancel( )
	{
		( (DataSetHandle) getContainer( ).getModel( ) ).removeListener( this );
		return super.performCancel( );
	}
	
	/*
	 * @see org.eclipse.birt.report.designer.ui.dialogs.properties.IPropertyPage#getToolTip()
	 */
	public String getToolTip( )
	{
		return Messages.getString( "dataset.outputparameters.preview.tooltip" ); //$NON-NLS-1$
	}
	
}
