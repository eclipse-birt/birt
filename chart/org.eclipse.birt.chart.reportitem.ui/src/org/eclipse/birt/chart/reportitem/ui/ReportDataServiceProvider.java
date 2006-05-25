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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ExtendedItemFilterDialog;
import org.eclipse.birt.chart.reportitem.ui.dialogs.ReportItemParametersDialog;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
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
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.jface.window.Window;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.ULocale;

/**
 * Data service provider for chart builder.
 */

public class ReportDataServiceProvider implements IDataServiceProvider
{

	private ExtendedItemHandle itemHandle;

	/**
	 * This flag indicates whether the error is found when fetching data. This
	 * is to help reduce invalid query.
	 */
	private boolean isErrorFound = false;

	public ReportDataServiceProvider( ExtendedItemHandle itemHandle )
	{
		super( );
		this.itemHandle = itemHandle;
	}

	private ModuleHandle getReportDesignHandle( )
	{
		return SessionHandleAdapter.getInstance( ).getReportDesignHandle( );
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
		// if ( getBoundDataSet( ) != null )
		// {
		// return itemHandle.columnBindingsIterator( );
		// }
		// DesignElementHandle handle = DEUtil.getBindingHolder( itemHandle );
		// if ( handle instanceof ReportItemHandle )
		// {
		// ArrayList list = new ArrayList( );
		// Iterator i = ( (ReportItemHandle) handle ).columnBindingsIterator( );
		// while ( i.hasNext( ) )
		// {
		// list.add( i.next( ) );
		// }
		// i = itemHandle.columnBindingsIterator( );
		// while ( i.hasNext( ) )
		// {
		// list.add( i.next( ) );
		// }
		// return list.iterator( );
		// }
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
		ClassLoader newContextLoader = getCustomScriptClassLoader( parentLoader );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );

		try
		{
			QueryDefinition queryDefn = new QueryDefinition( );
			queryDefn.setMaxRows( getMaxRow( ) );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getName( ) );

			DataRequestSession session = DataRequestSession.newSession( new DataSessionContext( DataSessionContext.CACHE_USE_ALWAYS,
					getReportDesignHandle( ) ) );
			for ( int i = 0; i < columnExpression.length; i++ )
			{
				queryDefn.addResultSetExpression( columnExpression[i],
						new ScriptExpression( columnExpression[i] ) );
			}

			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
							.iterator( ),
					itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
							.iterator( ),
					getColumnDataBindings( ) );
			if ( actualResultSet != null )
			{
				String[] expressions = columnExpression;
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
			throw new ChartException( ChartReportItemUIActivator.ID,
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
				// Do nothing if column bindings from container exist or no
				// inheritance in initialization
				if ( getBoundDataSet( ) == null
						&& ( getReportDataSet( ) == null || itemHandle.columnBindingsIterator( )
								.hasNext( ) ) )
				{
					needClean = false;
				}
				else
				{
					itemHandle.setDataSet( null );
				}
			}
			else
			{
				DataSetHandle dataset = getReportDesignHandle( ).findDataSet( datasetName );
				if ( itemHandle.getDataSet( ) == dataset )
				{
					needClean = false;
				}
				else
				{
					itemHandle.setDataSet( dataset );
				}
			}
			if ( needClean )
			{
				// Clear parameters and filters, binding if dataset changed
				itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
						.clearValue( );
				itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
						.clearValue( );
				itemHandle.getColumnBindings( ).clearValue( );

				List columnList = DataUtil.generateComputedColumns( itemHandle );
				if ( columnList.size( ) > 0 )
				{
					for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
					{
						DEUtil.addColumn( itemHandle,
								(ComputedColumn) iter.next( ),
								false );
					}
				}
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
		ColumnBindingDialog page = new ColumnBindingDialog( false ) {

			protected void addBinding( ComputedColumn column )
			{
				try
				{
					DEUtil.addColumn( itemHandle, column, true );
				}
				catch ( SemanticException e )
				{
					// WizardBase.displayException( e );
				}
			}

			protected List getBindingList( DesignElementHandle inputElement )
			{
				Iterator iterator = getColumnDataBindings( );
				List list = new ArrayList( );
				while ( iterator.hasNext( ) )
				{
					list.add( iterator.next( ) );
				}
				return list;
			}
		};
		page.setInput( itemHandle );
		return page.open( );
	}

	private StyleHandle[] getAllStyleHandles( )
	{
		StyleHandle[] list = (StyleHandle[]) getReportDesignHandle( ).getAllStyles( )
				.toArray( new StyleHandle[0] );
		Arrays.sort( list, new Comparator( ) {

			Collator collator = Collator.getInstance( ULocale.getDefault( ) );

			public int compare( Object o1, Object o2 )
			{
				StyleHandle s1 = (StyleHandle) o1;
				StyleHandle s2 = (StyleHandle) o2;

				return collator.compare( s1.getDisplayLabel( ),
						s2.getDisplayLabel( ) );
			}

		} );
		return list;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.interfaces.IDataServiceProvider#getAllStyles()
	 */
	public String[] getAllStyles( )
	{
		StyleHandle[] handles = getAllStyleHandles( );
		String[] names = new String[handles.length];
		for ( int i = 0; i < names.length; i++ )
		{
			names[i] = handles[i].getName( );
		}
		return names;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getAllStyleDisplayNames()
	 */
	public String[] getAllStyleDisplayNames( )
	{
		StyleHandle[] handles = getAllStyleHandles( );
		String[] names = new String[handles.length];
		for ( int i = 0; i < names.length; i++ )
		{
			names[i] = handles[i].getDisplayLabel( );
		}
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
		return ChartReportItemUIActivator.getDefault( )
				.getPluginPreferences( )
				.getInt( ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
	}

	public boolean isLivePreviewEnabled( )
	{
		return !isErrorFound
				&& ChartReportItemUIActivator.getDefault( )
						.getPluginPreferences( )
						.getBoolean( ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE );
	}

	public boolean isInvokingSupported( )
	{
		return true;
	}

	private ClassLoader getCustomScriptClassLoader( ClassLoader parent )
	{
		// For Bugzilla 106580: in order for Data Set Preview to locate POJO, we
		// need to set current thread's context class loader to a custom loader
		// which has the following path:
		// All workspace Java project's class path (this class path is already
		// has already calculated byorg.eclipse.birt.report.debug.ui plugin, and
		// set as system property "workspace.projectclasspath"
		String classPath = System.getProperty( "workspace.projectclasspath" ); //$NON-NLS-1$
		if ( classPath == null || classPath.length( ) == 0 )
			return parent;

		String[] classPathArray = classPath.split( ";", -1 ); //$NON-NLS-1$
		int count = classPathArray.length;
		URL[] urls = new URL[count];
		for ( int i = 0; i < count; i++ )
		{
			File file = new File( classPathArray[i] );
			try
			{
				urls[i] = file.toURL( );
			}
			catch ( MalformedURLException e )
			{
				urls[i] = null;
			}
		}

		return new URLClassLoader( urls, parent );
	}

}
