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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.ui.actions.NewDataSetAction;
import org.eclipse.birt.report.designer.ui.dialogs.ColumnBindingDialog;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.jface.window.Window;

/**
 * Data service provider for chart builder.
 */

public class ReportDataServiceProvider implements IDataServiceProvider
{

	private ExtendedItemHandle itemHandle;
	private DataSetHandle dsHandle;
	private ArrayList bindingList;

	/**
	 * This flag indicates whether the error is found when fetching data. This
	 * is to help reduce invalid query.
	 */
	private boolean isErrorFound = false;

	public ReportDataServiceProvider( ExtendedItemHandle itemHandle )
	{
		super( );
		this.itemHandle = itemHandle;
		ChartDataSetManager.initCurrentInstance( getReportDesignHandle( ) );
	}

	private ModuleHandle getReportDesignHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#startDataBinding()
	 */
	public void startDataBinding( )
	{
		dsHandle = itemHandle.getDataSet( );

		bindingList = new ArrayList( );
		Iterator columnBindingIterator = itemHandle.columnBindingsIterator( );
		while ( columnBindingIterator.hasNext( ) )
		{
			bindingList.add( ( (ComputedColumnHandle) columnBindingIterator.next( ) ).getStructure( ) );
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#rollbackDataBinding()
	 */
	public void rollbackDataBinding( )
	{
		try
		{
			itemHandle.setDataSet( dsHandle );
			itemHandle.getColumnBindings( ).clearValue( );

			for ( int i = 0; i < bindingList.size( ); i++ )
			{
				itemHandle.addColumnBinding( (ComputedColumn) bindingList.get( i ),
						true );
			}
		}
		catch ( SemanticException se )
		{
			se.printStackTrace( );
		}
		
		dsHandle = null;
		bindingList = null;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#commitDataBinding()
	 */
	public void commitDataBinding( )
	{
		dsHandle = null;
		bindingList = null;		
	}

	public String[] getAllDataSets( )
	{
		List list = getReportDesignHandle( ).getVisibleDataSets( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < names.length; i++ )
		{
			names[i] = ( (DataSetHandle) list.get( i ) ).getQualifiedName( );
		}
		return names;
	}

	public final String[] getPreviewHeader( ) throws ChartException
	{
		Iterator iterator = getColumnDataBindings( );
		ArrayList list = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			list.add( ( (ComputedColumnHandle) iterator.next( ) ).getName( ) );
		}
		return (String[]) list.toArray( new String[0] );
	}

	public final List getPreviewData( ) throws ChartException
	{
		return getPreviewRowData( getPreviewHeader( true ), -1, true );
	}

	private String[] getPreviewHeader( boolean isExpression )
			throws ChartException
	{
		String[] exps = getPreviewHeader( );
		if ( isExpression )
		{
			for ( int i = 0; i < exps.length; i++ )
			{
				exps[i] = ChartUIUtil.getExpressionString( exps[i] );
			}
		}
		return exps;
	}

	/**
	 * Returns column bindings of current handle or the combination with nearest
	 * container
	 * 
	 */
	private Iterator getColumnDataBindings( )
	{
		if ( getBoundDataSet( ) != null )
		{
			return itemHandle.columnBindingsIterator( );
		}
		DesignElementHandle handle = DEUtil.getBindingHolder( itemHandle );
		if ( handle instanceof ReportItemHandle )
		{
			ArrayList list = new ArrayList( );
			Iterator i = ( (ReportItemHandle) handle ).columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
			}
			i = itemHandle.columnBindingsIterator( );
			while ( i.hasNext( ) )
			{
				list.add( i.next( ) );
			}
			return list.iterator( );
		}
		return itemHandle.columnBindingsIterator( );
	}

	protected final List getPreviewRowData( String[] columnExpression,
			int rowCount, boolean isStringType ) throws ChartException
	{
		ArrayList dataList = new ArrayList( );

		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = ChartDataSetManager.getCustomScriptClassLoader( parentLoader );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );

		try
		{
			DataSetHandle datasetHandle = getDataSetFromHandle( );
			IQueryResults actualResultSet = ChartDataSetManager.getCurrentInstance( )
					.getCacheResult( datasetHandle,
							itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
									.iterator( ),
							itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
									.iterator( ),
							getColumnDataBindings( ),
							columnExpression,
							rowCount <= 0 ? getMaxRow( ) : rowCount );
			if ( actualResultSet != null )
			{
				String[] expressions = columnExpression;// extractExpressionNames(
				// actualResultSet );
				int columnCount = expressions.length;
				IResultIterator iter = actualResultSet.getResultIterator( );
				while ( iter.next( ) )
				{
					if ( isStringType )
					{
						String[] record = new String[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							record[n] = iter.getString( expressions[n] );
						}
						dataList.add( record );
					}
					else
					{
						Object[] record = new Object[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							record[n] = iter.getValue( expressions[n] );
						}
						dataList.add( record );
					}
				}

				actualResultSet.close( );
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemPlugin.ID,
					ChartException.DATA_BINDING,
					e );
		}
		finally
		{
			// Restore old thread context class loader
			Thread.currentThread( ).setContextClassLoader( oldContextLoader );
		}

		return dataList;
	}

	public String getBoundDataSet( )
	{
		if ( itemHandle.getDataSet( ) == null )
		{
			return null;
		}
		return itemHandle.getDataSet( ).getQualifiedName( );
	}

	public String getReportDataSet( )
	{
		List list = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( list.size( ) > 0 )
		{
			return ( (DataSetHandle) list.get( 0 ) ).getQualifiedName( );
		}
		return null;
	}

	public void setContext( Object context )
	{
		itemHandle = (ExtendedItemHandle) context;
	}

	public void setDataSet( String datasetName )
	{
		boolean needClean = true;
		try
		{
			if ( datasetName == null )
			{
				itemHandle.setDataSet( null );
			}
			else
			{
				DataSetHandle dataset = getReportDesignHandle( ).findDataSet( datasetName );
				if ( itemHandle.getDataSet( ) == dataset )
				{
					needClean = false;
				}
				itemHandle.setDataSet( dataset );
			}
			if ( needClean )
			{
				// Clear parameters and filters, binding if dataset changed
				itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
				itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
						.clearValue( );
				itemHandle.getColumnBindings( ).clearValue( );
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.displayException( e );
		}
	}

	/**
	 * Gets dataset from ReportItemHandle at first. If null, get dataset from
	 * its container.
	 * 
	 * @return direct dataset
	 */
	protected DataSetHandle getDataSetFromHandle( )
	{
		if ( itemHandle.getDataSet( ) != null )
		{
			return itemHandle.getDataSet( );
		}
		List datasetList = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( datasetList.size( ) > 0 )
		{
			return (DataSetHandle) datasetList.get( 0 );
		}
		return null;
	}

	public int invoke( int command )
	{
		if ( command == COMMAND_NEW_DATASET )
		{
			return invokeNewDataSet( );
		}
		else if ( command == COMMAND_EDIT_FILTER )
		{
			return invokeEditFilter( );
		}
		else if ( command == COMMAND_EDIT_PARAMETER )
		{
			return invokeEditParameter( );
		}
		else if ( command == COMMAND_EDIT_BINDING )
		{
			return invokeDataBinding( );
		}
		return Window.CANCEL;
	}

	protected int invokeNewDataSet( )
	{
		new NewDataSetAction( ).run( );
		return Window.OK;
	}

	protected int invokeEditFilter( )
	{
		ExtendedItemFilterDialog page = new ExtendedItemFilterDialog( itemHandle,
				this );
		return page.open( );
	}

	protected int invokeEditParameter( )
	{
		ReportItemParametersDialog page = new ReportItemParametersDialog( itemHandle );
		return page.open( );
	}

	protected int invokeDataBinding( )
	{
		ColumnBindingDialog page = new ColumnBindingDialog( false );
		page.setInput( itemHandle );
		return page.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	public String[] getAllStyles( )
	{
		List list = getReportDesignHandle( ).getAllStyles( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < names.length; i++ )
		{
			names[i] = ( (SharedStyleHandle) list.get( i ) ).getName( );
		}
		Arrays.sort( names );
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getCurrentStyle()
	 */
	public String getCurrentStyle( )
	{
		if ( itemHandle.getStyle( ) == null )
		{
			return null;
		}
		return itemHandle.getStyle( ).getName( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#setStyle(java.lang.String)
	 */
	public void setStyle( String styleName )
	{
		try
		{
			if ( styleName == null )
			{
				itemHandle.setStyle( null );
			}
			else
			{
				itemHandle.setStyle( getStyle( styleName ) );
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.displayException( e );
		}
	}

	private SharedStyleHandle getStyle( String styleName )
	{
		return getReportDesignHandle( ).findStyle( styleName );
	}

	public final Object[] getDataForColumns( String[] sExpressions,
			int iMaxRecords, boolean byRow ) throws ChartException
	{
		List rowData = getPreviewRowData( sExpressions, iMaxRecords, false );
		if ( byRow )
		{
			return rowData.toArray( );
		}
		List columnData = new ArrayList( );
		for ( int i = 0; i < sExpressions.length; i++ )
		{
			Object[] columnArray = new Object[rowData.size( )];
			for ( int j = 0; j < rowData.size( ); j++ )
			{
				columnArray[j] = ( (Object[]) rowData.get( j ) )[i];
			}
			columnData.add( columnArray );
		}
		return columnData.toArray( );
	}

	public void dispose( )
	{
		// TODO DataEngine should be disposed when closing report design, rather
		// than when closing chart wizard. Currently, do nothing at this time.

		// ChartDataSetManager.getCurrentInstance( ).dispose( );
	}

	private int getMaxRow( )
	{
		return ChartReportItemPlugin.getDefault( )
				.getPluginPreferences( )
				.getInt( ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
	}

	public boolean isLivePreviewEnabled( )
	{
		return !isErrorFound
				&& ChartReportItemPlugin.getDefault( )
						.getPluginPreferences( )
						.getBoolean( ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE );
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}

}
