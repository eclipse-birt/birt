/*******************************************************************************
 * Copyright (c) 2004, 2007, 2008 Actuate Corporation.
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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator;
import org.eclipse.birt.chart.reportitem.ChartBuilderGroupedQueryResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.ChartXTabUtil;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.metadata.PredefinedStyle;
import org.eclipse.core.resources.IProject;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Data service provider of chart builder for BIRT integration.
 */

public class ReportDataServiceProvider implements IDataServiceProvider
{

	private ExtendedItemHandle itemHandle;

	static final String OPTION_NONE = Messages.getString( "ReportDataServiceProvider.Option.None" ); //$NON-NLS-1$

	/**
	 * This flag indicates whether the error is found when fetching data. This
	 * is to help reduce invalid query.
	 */
	private boolean isErrorFound = false;
	private IProject project = null;

	public ReportDataServiceProvider( ExtendedItemHandle itemHandle )
	{
		super( );
		this.itemHandle = itemHandle;
		project = UIUtil.getCurrentProject( );
	}

	private ModuleHandle getReportDesignHandle( )
	{
		return itemHandle.getModuleHandle( );
	}

	String[] getAllDataSets( )
	{
		List list = getReportDesignHandle( ).getVisibleDataSets( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			names[i] = ( (DataSetHandle) list.get( i ) ).getQualifiedName( );
		}
		return names;
	}

	String[] getAllDataCubes( )
	{
		List list = getReportDesignHandle( ).getVisibleCubes( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			names[i] = ( (CubeHandle) list.get( i ) ).getQualifiedName( );
		}
		return names;
	}

	String getDataCube( )
	{
		CubeHandle cube = itemHandle.getCube( );
		if ( cube == null )
		{
			return null;
		}
		return cube.getQualifiedName( );
	}

	void setDataCube( String cubeName )
	{
		try
		{
			// Clean references if it's set
			if ( itemHandle.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				itemHandle.setDataBindingReference( null );
			}
			itemHandle.setDataSet( null );

			if ( cubeName == null )
			{
				itemHandle.setCube( null );
				// Clear parameters and filters, binding if dataset changed
				clearBindings( );
			}
			else
			{
				if ( !cubeName.equals( getDataCube( ) ) )
				{
					CubeHandle cubeHandle = getReportDesignHandle( ).findCube( cubeName );
					itemHandle.setCube( cubeHandle );
					// Clear parameters and filters, binding if dataset changed
					clearBindings( );
					generateBindings( generateComputedColumns( cubeHandle ) );
				}
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	public final String[] getPreviewHeader( ) throws ChartException
	{
		Iterator iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		ArrayList list = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			list.add( ( (ComputedColumnHandle) iterator.next( ) ).getName( ) );
		}
		return (String[]) list.toArray( new String[list.size( )] );
	}

	public final List getPreviewData( ) throws ChartException
	{
		if ( !isSharedBinding( ) )
		{
			return getPreviewRowData( getPreviewHeader( true ), -1, true );
		}
		else
		{
			return getPreviewRowDataForTableSharedBinding( getPreviewHeadersInfo( ),
					-1,
					true );
		}
	}

	/**
	 * Returns columns header info.
	 * 
	 * @throws ChartException
	 * @since BIRT 2.3
	 */
	public final ColumnBindingInfo[] getPreviewHeadersInfo( )
			throws ChartException
	{
		Iterator iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		ArrayList columnList = new ArrayList( );
		while ( iterator.hasNext( ) )
		{
			columnList.add( iterator.next( ) );
		}

		if ( columnList.size( ) == 0 )
		{
			return null;
		}
		
		ColumnBindingInfo[] columnHeaders = null;
		if ( isTableSharedBinding( ) )
		{
			// Process grouping and aggregate on group case.
			// Get groups.
			List groupList = getGroupsOfSharedBinding( );

			columnHeaders = new ColumnBindingInfo[columnList.size( )
					+ groupList.size( )];
			int index = 0;
			for ( int i = 0; i < groupList.size( ); i++ )
			{
				GroupHandle gh = (GroupHandle) groupList.get( i );
				String groupName = gh.getName( );
				String groupKeyExpr = gh.getKeyExpr( );
				String tooltip = Messages.getString( "ReportDataServiceProvider.Tooltip.GroupExpression" ) + groupKeyExpr; //$NON-NLS-1$
				columnHeaders[index++] = new ColumnBindingInfo( groupName,
						groupKeyExpr, // Use expression for group.
						ColumnBindingInfo.GROUP_COLUMN,
						"icons/obj16/group.gif", //$NON-NLS-1$
						tooltip,
						gh );

				for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
				{
					ComputedColumnHandle cch = (ComputedColumnHandle) iter.next( );

					String aggOn = cch.getAggregateOn( );
					if ( groupName.equals( aggOn ) )
					{
						// Remove the column binding from list.
						iter.remove( );

						tooltip = Messages.getString( "ReportDataServiceProvider.Tooltip.Aggregate" ) + cch.getAggregateFunction( ) + "\n" + Messages.getString( "ReportDataServiceProvider.Tooltip.OnGroup" ) + groupName; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
						columnHeaders[index] = new ColumnBindingInfo( cch.getName( ),
								ExpressionUtil.createJSRowExpression( cch.getName( ) ), // Use
								// binding
								// expression
								// for
								// aggregate.
								ColumnBindingInfo.AGGREGATE_COLUMN,
								ChartUIConstants.IMAGE_SIGMA,
								tooltip,
								cch );
						columnHeaders[index].setChartAggExpression( ChartReportItemUtil.convertToChartAggExpression( cch.getAggregateFunction( ) ) );
						index++;
					}
				}
			}

			// Process aggregate on whole table case.
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				ComputedColumnHandle cch = (ComputedColumnHandle) iter.next( );

				if ( cch.getAggregateFunction( ) != null )
				{
					// Remove the column binding form list.
					iter.remove( );

					String tooltip = Messages.getString( "ReportDataServiceProvider.Tooltip.Aggregate" ) + cch.getAggregateFunction( ) + "\n" + Messages.getString( "ReportDataServiceProvider.Tooltip.OnGroup" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
					columnHeaders[index] = new ColumnBindingInfo( cch.getName( ),
							ExpressionUtil.createJSRowExpression( cch.getName( ) ), // Use
							// binding
							// expression
							// for
							// aggregate.
							ColumnBindingInfo.AGGREGATE_COLUMN,
							ChartUIConstants.IMAGE_SIGMA,
							tooltip,
							cch );
					columnHeaders[index].setChartAggExpression( ChartReportItemUtil.convertToChartAggExpression( cch.getAggregateFunction( ) ) );
					index++;
				}
			}

			// Process common bindings.
			for ( Iterator iter = columnList.iterator( ); iter.hasNext( ); )
			{
				ComputedColumnHandle cch = (ComputedColumnHandle) iter.next( );
				columnHeaders[index++] = new ColumnBindingInfo( cch.getName( ),
						ExpressionUtil.createJSRowExpression( cch.getName( ) ), // Use
						// binding
						// expression
						// for
						// common
						// binding.
						ColumnBindingInfo.COMMON_COLUMN,
						null,
						null,
						cch );
			}
		}
		else
		{
			columnHeaders = new ColumnBindingInfo[columnList.size( )];
			for ( int i = 0; i < columnHeaders.length; i++ )
			{
				ComputedColumnHandle cch = (ComputedColumnHandle) columnList.get( i );
				columnHeaders[i] = new ColumnBindingInfo( cch.getName( ),
						cch.getExpression( ),
						null,
						null,
						cch );
			}
		}

		return columnHeaders;
	}

	/**
	 * Returns groups in shared binding.
	 * 
	 * @return
	 */
	private List getGroupsOfSharedBinding( )
	{
		List groupList = new ArrayList( );
		ReportItemHandle handle = getReportItemHandleOfSharedBinding( );
		if ( handle instanceof TableHandle )
		{
			SlotHandle groups = ( (TableHandle) handle ).getGroups( );
			for ( Iterator iter = groups.iterator( ); iter.hasNext( ); )
			{
				groupList.add( iter.next( ) );
			}
		}
		return groupList;
	}

	private String[] getPreviewHeader( boolean isExpression )
			throws ChartException
	{
		String[] exps = getPreviewHeader( );
		if ( isExpression )
		{
			for ( int i = 0; i < exps.length; i++ )
			{
				exps[i] = ExpressionUtil.createJSRowExpression( exps[i] );
			}
		}
		return exps;
	}

	/**
	 * Returns preview row data for table shared binding, it will share table's
	 * bindings and get them data.
	 * 
	 * @param headers
	 * @param rowCount
	 * @param isStringType
	 * @return
	 * @throws ChartException
	 * @since 2.3
	 */
	protected List getPreviewRowDataForTableSharedBinding(
			ColumnBindingInfo[] headers, int rowCount, boolean isStringType )
			throws ChartException
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
			// Create query definition.
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );

			DataRequestSession session = prepareDataRequestSession( getMaxRow( ) );

			// Binding columns, aggregates, filters and sorts.
			List columns = generateTableSharedBindingQuery( headers,
					queryDefn,
					session,
					new HashMap( ) );

			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					null,
					itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
							.iterator( ),
					null );

			if ( actualResultSet != null )
			{
				int columnCount = columns.size( );
				IResultIterator iter = actualResultSet.getResultIterator( );
				while ( iter.next( ) )
				{
					if ( isStringType )
					{
						String[] record = new String[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							// Bugzilla#190229, to get string with localized
							// format
							record[n] = DataTypeUtil.toString( iter.getValue( (String) columns.get( n ) ) );
						}
						dataList.add( record );
					}
					else
					{
						Object[] record = new Object[columnCount];
						for ( int n = 0; n < columnCount; n++ )
						{
							record[n] = iter.getValue( (String) columns.get( n ) );
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

	private List generateTableSharedBindingQuery( ColumnBindingInfo[] headers,
			QueryDefinition queryDefn, DataRequestSession session,
			Map bindingExprsMap ) throws AdapterException, DataException
	{
		List columns = new ArrayList( );
		ReportItemHandle reportItemHandle = getReportItemHandleOfSharedBinding( );
		queryDefn.setDataSetName( reportItemHandle.getDataSet( )
				.getQualifiedName( ) );
		for ( int i = 0; i < headers.length; i++ )
		{
			ColumnBindingInfo chi = headers[i];
			int type = chi.getColumnType( );
			switch ( type )
			{
				case ColumnBindingInfo.COMMON_COLUMN :
				case ColumnBindingInfo.AGGREGATE_COLUMN :
					IBinding binding = session.getModelAdaptor( )
							.adaptBinding( (ComputedColumnHandle) chi.getObjectHandle( ) );
					queryDefn.addBinding( binding );

					columns.add( binding.getBindingName( ) );
					// Use original binding expression as map key for aggregate
					// and common binding.
					bindingExprsMap.put( chi.getExpression( ),
							binding.getBindingName( ) );

					break;
				case ColumnBindingInfo.GROUP_COLUMN :
					GroupDefinition gd = session.getModelAdaptor( )
							.adaptGroup( (GroupHandle) chi.getObjectHandle( ) );
					queryDefn.addGroup( gd );

					String name = StructureFactory.newComputedColumn( reportItemHandle,
							gd.getName( ) )
							.getName( );
					binding = new Binding( name );
					binding.setExpression( new ScriptExpression( gd.getKeyExpression( ) ) );
					queryDefn.addBinding( binding );

					columns.add( name );

					// Use created binding expression as map key for group.
					bindingExprsMap.put( ( (ScriptExpression) binding.getExpression( ) ).getText( ),
							binding.getBindingName( ) );
					break;
			}
		}
		return columns;
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
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getQualifiedName( ) );

			DataRequestSession session = prepareDataRequestSession( maxRow );
			for ( int i = 0; i < columnExpression.length; i++ )
			{
				queryDefn.addResultSetExpression( columnExpression[i],
						new ScriptExpression( columnExpression[i] ) );
			}

			// Iterate parameter bindings to check if its expression is a
			// explicit
			// value, otherwise use default value of parameter as its
			// expression.
			resetParametersForDataPreview( getDataSetFromHandle( ), queryDefn );

			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					null,
					itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
							.iterator( ),
					ChartReportItemUtil.getColumnDataBindings( itemHandle ) );
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
							// Bugzilla#190229, to get string with localized
							// format
							record[n] = DataTypeUtil.toString( iter.getValue( expressions[n] ) );
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

	/**
	 * Check if current parameters is explicit value in chart builder, otherwise
	 * default value of parameters will be used to get preview data.
	 * 
	 * @param dataSetHandle
	 *            current dataset handle.
	 * @param queryDefn
	 *            query definition.
	 * @return query definition.
	 */
	private QueryDefinition resetParametersForDataPreview(
			DataSetHandle dataSetHandle, QueryDefinition queryDefn )
	{
		// 1. Get parameters description from dataset.
		Iterator iterParams = dataSetHandle.parametersIterator( );
		Map dsphMap = new LinkedHashMap( );
		for ( ; iterParams.hasNext( ); )
		{
			DataSetParameterHandle dsph = (DataSetParameterHandle) iterParams.next( );
			dsphMap.put( dsph.getName( ), dsph );
		}

		// 2. Get parameters setting from current handle.
		iterParams = itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
				.iterator( );
		Map pbhMap = new LinkedHashMap( );
		for ( ; iterParams.hasNext( ); )
		{
			ParamBindingHandle paramBindingHandle = (ParamBindingHandle) iterParams.next( );
			pbhMap.put( paramBindingHandle.getParamName( ), paramBindingHandle );
		}

		// 3. Check and reset parameters expression.
		Object[] names = pbhMap.keySet( ).toArray( );
		for ( int i = 0; i < names.length; i++ )
		{
			DataSetParameterHandle dsph = (DataSetParameterHandle) dsphMap.get( names[i] );
			ScriptExpression se = null;
			String expr = ( (ParamBindingHandle) pbhMap.get( names[i] ) ).getExpression( );
			// The parameters must be a explicit value, the 'row[]' and
			// 'row.' expressions only be converted to a explicit value
			// under runtime case. So use default value of parameter to get
			// data in Chart Builder.
			if ( dsph.getDefaultValue( ) != null
					&& ( expr == null || expr.indexOf( "row[" ) >= 0 || expr.indexOf( "row." ) >= 0 ) ) //$NON-NLS-1$ //$NON-NLS-2$
			{
				se = new ScriptExpression( dsph.getDefaultValue( ) );
			}
			else
			{
				se = new ScriptExpression( expr );
			}

			InputParameterBinding ipb = new InputParameterBinding( (String) names[i],
					se );
			queryDefn.addInputParamBinding( ipb );
		}

		return queryDefn;
	}

	String getBoundDataSet( )
	{
		if ( itemHandle.getDataSet( ) == null )
		{
			return null;
		}
		return itemHandle.getDataSet( ).getQualifiedName( );
	}

	/**
	 * Checks if only inheritance allowed.
	 * 
	 */
	boolean isInheritanceOnly( )
	{
		if ( isInMultiView( ) || isInXTab( ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Check if chart is in multiple view.
	 * 
	 * @return
	 * @since 2.3
	 */
	private boolean isInMultiView( )
	{
		return itemHandle.getContainer( ) instanceof MultiViewsHandle;
	}

	String getReportDataSet( )
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

	void setDataSet( String datasetName )
	{
		try
		{
			// Clean references if it's set
			if ( itemHandle.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				itemHandle.setDataBindingReference( null );
			}

			itemHandle.setCube( null );

			if ( datasetName == null )
			{
				if ( getBoundDataSet( ) != null )
				{
					// Clean old bindings and use container's binding
					clearBindings( );
				}
				itemHandle.setDataSet( null );
			}
			else
			{
				DataSetHandle dataset = getReportDesignHandle( ).findDataSet( datasetName );
				if ( itemHandle.getDataSet( ) != dataset )
				{
					itemHandle.setDataSet( dataset );

					// Clean old bindings and add new bindings
					clearBindings( );
					generateBindings( generateComputedColumns( dataset ) );
				}
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	private void clearBindings( ) throws SemanticException
	{
		itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
				.clearValue( );
		itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
				.clearValue( );
		itemHandle.getColumnBindings( ).clearValue( );
	}

	private void generateBindings( List columnList ) throws SemanticException
	{
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

	/**
	 * Generate computed columns for the given report item with the closest data
	 * set available.
	 * 
	 * @param dataSetHandle
	 *            Data Set. No aggregation created.
	 * 
	 * @return true if succeed,or fail if no column generated.
	 * @see DataUtil#generateComputedColumns(ReportItemHandle)
	 * 
	 */
	private List generateComputedColumns( DataSetHandle dataSetHandle )
			throws SemanticException
	{
		if ( dataSetHandle != null )
		{
			List resultSetColumnList = DataUtil.getColumnList( dataSetHandle );
			ArrayList columnList = new ArrayList( );
			for ( Iterator iter = resultSetColumnList.iterator( ); iter.hasNext( ); )
			{
				ResultSetColumnHandle resultSetColumn = (ResultSetColumnHandle) iter.next( );
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						resultSetColumn.getColumnName( ) );
				column.setDataType( resultSetColumn.getDataType( ) );
				column.setExpression( DEUtil.getExpression( resultSetColumn ) );
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.EMPTY_LIST;
	}

	private List generateComputedColumns( CubeHandle cubeHandle )
			throws SemanticException
	{
		if ( cubeHandle != null )
		{
			List columnList = new ArrayList( );
			// Add levels
			List levels = ChartXTabUtil.getAllLevels( cubeHandle );
			for ( Iterator iter = levels.iterator( ); iter.hasNext( ); )
			{
				LevelHandle levelHandle = (LevelHandle) iter.next( );
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartXTabUtil.createLevelBindingName( levelHandle ) );
				column.setDataType( levelHandle.getDataType( ) );
				column.setExpression( ChartXTabUtil.createDimensionExpression( levelHandle ) );
				columnList.add( column );
			}
			// Add measures
			List measures = ChartXTabUtil.getAllMeasures( cubeHandle );
			for ( Iterator iter = measures.iterator( ); iter.hasNext( ); )
			{
				MeasureHandle measureHandle = (MeasureHandle) iter.next( );
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartXTabUtil.createMeasureBindingName( measureHandle ) );
				column.setDataType( measureHandle.getDataType( ) );
				column.setExpression( ExpressionUtil.createJSMeasureExpression( measureHandle.getName( ) ) );
				column.setAggregateFunction( measureHandle.getFunction( ) );
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.EMPTY_LIST;
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

	private StyleHandle[] getAllStyleHandles( )
	{
		List sLst = getReportDesignHandle( ).getAllStyles( );
		StyleHandle[] list = (StyleHandle[]) sLst.toArray( new StyleHandle[sLst.size( )] );
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
			names[i] = handles[i].getQualifiedName( );
		}

		// Filter predefined styles to make its logic same with report design
		// side.
		names = filterPreStyles( names );
		return names;
	}

	/**
	 * Filter predefined styles.
	 * 
	 * @param items
	 *            all available styles
	 * @return filtered styles.
	 */
	private String[] filterPreStyles( String items[] )
	{
		String[] newItems = items;
		if ( items == null )
		{
			newItems = new String[]{};
		}

		List preStyles = new DesignEngine( new DesignConfig( ) ).getMetaData( )
				.getPredefinedStyles( );
		List preStyleNames = new ArrayList( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( ( (PredefinedStyle) preStyles.get( i ) ).getName( ) );
		}

		List sytleNames = new ArrayList( );
		for ( int i = 0; i < newItems.length; i++ )
		{
			if ( preStyleNames.indexOf( newItems[i] ) == -1 )
			{
				sytleNames.add( newItems[i] );
			}
		}

		return (String[]) ( sytleNames.toArray( new String[]{} ) );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getAllStyleDisplayNames()
	 */
	public String[] getAllStyleDisplayNames( )
	{
		List styles = Arrays.asList( getAllStyles( ) );
		StyleHandle[] handles = getAllStyleHandles( );
		String[] names = new String[styles.size( )];
		for ( int i = 0, j = 0; i < handles.length; i++ )
		{
			// Remove predefined styles to make its logic same with report
			// design side.
			if ( styles.contains( handles[i].getQualifiedName( ) ) )
			{
				names[j++] = handles[i].getDisplayLabel( );
			}
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
		return itemHandle.getStyle( ).getQualifiedName( );
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
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	private SharedStyleHandle getStyle( String styleName )
	{
		return getReportDesignHandle( ).findStyle( styleName );
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataForColumns(java.lang.String[],
	 *      int, boolean)
	 * @deprecated since BIRT 2.3, the method is not used for chart live
	 *             preview, use
	 *             {@link #prepareRowExpressionEvaluator(Chart, List, int, boolean)}
	 *             instead.
	 * 
	 */
	public final Object[] getDataForColumns( String[] sExpressions,
			int iMaxRecords, boolean byRow ) throws ChartException
	{
		return null;
	}

	private int getMaxRow( )
	{
		return PreferenceFactory.getInstance( )
				.getPreferences( ChartReportItemUIActivator.getDefault( ),
						project )
				.getInt( ChartReportItemUIActivator.PREFERENCE_MAX_ROW );
	}

	public boolean isLivePreviewEnabled( )
	{
		return !isErrorFound
				&& PreferenceFactory.getInstance( )
						.getPreferences( ChartReportItemUIActivator.getDefault( ),
								project )
						.getBoolean( ChartReportItemUIActivator.PREFERENCE_ENALBE_LIVE )
				&& ( getReportDataSet( ) != null || getBoundDataSet( ) != null );
	}

	boolean isInvokingSupported( )
	{
		// If report item reference is set, all operations, including filters,
		// parameter bindings and column bindings are not supported
		if ( isSharedBinding( ) || isInMultiView( ) )
		{
			return false;
		}
		ReportItemHandle container = DEUtil.getBindingHolder( itemHandle );
		if ( container != null )
		{
			// To check container's report item reference
			return container.getDataBindingReference( ) == null;
		}
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

		String[] classPathArray = classPath.split( File.pathSeparator, -1 );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataType(java.lang.String)
	 */
	public DataType getDataType( String expression )
	{
		// Do not check type for cube
		if ( expression == null
				|| expression.trim( ).length( ) == 0
				|| ChartXTabUtil.getBindingCube( itemHandle ) != null )
		{
			return null;
		}

		// Find data types from self column bindings first
		Object[] returnObj = findDataType( expression, itemHandle );
		if ( ( (Boolean) returnObj[0] ).booleanValue( ) )
		{
			return (DataType) returnObj[1];
		}

		// Find data types from its container column bindings.
		ReportItemHandle parentHandle = DEUtil.getBindingHolder( itemHandle );
		if ( parentHandle != null )
		{
			returnObj = findDataType( expression, parentHandle );
			if ( ( (Boolean) returnObj[0] ).booleanValue( ) )
			{
				return (DataType) returnObj[1];
			}
		}

		// Try to parse with number format
		try
		{
			NumberFormat.getInstance( ).parse( expression );
			return DataType.NUMERIC_LITERAL;
		}
		catch ( ParseException e )
		{
		}

		// Try to parse with date format
		try
		{
			DateFormat.getInstance( ).parse( expression );
			return DataType.DATE_TIME_LITERAL;
		}
		catch ( ParseException e )
		{
		}

		// Return null for unknown data type.
		return null;
	}

	/**
	 * Find data type of expression from specified item handle.
	 * 
	 * @param expression
	 *            expression.
	 * @param itemHandle
	 *            specified item handle.
	 * @return an object array, size is two, the first element is a boolean
	 *         object, if its value is <code>true</code> then means the data
	 *         type is found and the second element of array stores the data
	 *         type; if its value is <code>false</code> then means that data
	 *         type is not found.
	 */
	private Object[] findDataType( String expression,
			ReportItemHandle itemHandle )
	{
		Object[] returnObj = new Object[2];
		returnObj[0] = new Boolean( false );

		Iterator iterator = ChartReportItemUtil.getAllColumnBindingsIterator( itemHandle );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle cc = (ComputedColumnHandle) iterator.next( );
			if ( expression.toUpperCase( )
					.indexOf( cc.getName( ).toUpperCase( ) ) >= 0 )
			{
				String dataType = cc.getDataType( );
				if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.TEXT_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.NUMERIC_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = DataType.DATE_TIME_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
				{
					returnObj[0] = new Boolean( true );
					returnObj[1] = null;
					break;
				}
			}
		}
		return returnObj;
	}

	String[] getAllReportItemReferences( )
	{
		List referenceList = itemHandle.getAvailableDataBindingReferenceList( );
		String[] references = new String[referenceList.size( )];
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			references[i] = ( (ReportItemHandle) referenceList.get( i ) ).getQualifiedName( );
		}
		return references;
	}

	String getReportItemReference( )
	{
		ReportItemHandle ref = itemHandle.getDataBindingReference( );
		if ( ref == null )
		{
			return null;
		}
		return ref.getQualifiedName( );
	}

	void setReportItemReference( String referenceName )
	{
		try
		{
			itemHandle.setDataSet( null );
			itemHandle.setCube( null );

			if ( referenceName == null )
			{
				itemHandle.setDataBindingReference( null );
				// Select the corresponding data set, no need to reset bindings
			}
			else
			{
				if ( !referenceName.equals( getReportItemReference( ) ) )
				{
					// Change reference and reset all bindings
					itemHandle.setDataBindingReference( (ReportItemHandle) getReportDesignHandle( ).findElement( referenceName ) );
					// //Model will reset bindings
					// clearBindings( );
					// generateBindings( );
				}
			}
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#prepareRowExpressionEvaluator(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.String[], int, boolean)
	 */
	public IDataRowExpressionEvaluator prepareRowExpressionEvaluator( Chart cm,
			List columnExpression, int rowCount, boolean isStringType )
			throws ChartException
	{
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
			IQueryResults actualResultSet = null;
			if ( isSharedBinding( ) )
			{
				// Now only create query for table shared binding.
				// Create query definition.
				QueryDefinition queryDefn = new QueryDefinition( );
				int maxRow = getMaxRow( );
				queryDefn.setMaxRows( maxRow );

				DataRequestSession session = prepareDataRequestSession( getMaxRow( ) );

				// Binding columns, aggregates, filters and sorts.
				final Map bindingExprsMap = new HashMap( );
				generateTableSharedBindingQuery( getPreviewHeadersInfo( ),
						queryDefn,
						session,
						bindingExprsMap );

				actualResultSet = session.executeQuery( queryDefn,
						null,
						itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
								.iterator( ),
						null );

				if ( actualResultSet != null )
				{
					return new ChartBuilderGroupedQueryResultSetEvaluator( actualResultSet.getResultIterator( ),
							ChartReportItemUtil.hasAggregation( cm ) ) {

						/*
						 * (non-Javadoc)
						 * 
						 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
						 */
						public Object evaluate( String expression )
						{
							try
							{
								String newExpr = (String) bindingExprsMap.get( expression );
								if ( newExpr != null )
								{
									return fResultIterator.getValue( newExpr );
								}
								else
								{
									return fResultIterator.getValue( expression );
								}
							}
							catch ( BirtException e )
							{
								fLogger.log( e );
							}
							return null;
						}
					};
				}
			}
			else
			{
				DataRequestSession session = prepareDataRequestSession( getMaxRow( ) );

				BaseQueryHelper cbqh = new BaseQueryHelper( itemHandle, cm );
				QueryDefinition queryDefn = (QueryDefinition) cbqh.createBaseQuery( columnExpression );

				// Iterate parameter bindings to check if its expression is a
				// explicit
				// value, otherwise use default value of parameter as its
				// expression.
				resetParametersForDataPreview( getDataSetFromHandle( ),
						queryDefn );

				actualResultSet = session.executeQuery( queryDefn,
						null,
						itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP )
								.iterator( ),
						ChartReportItemUtil.getColumnDataBindings( itemHandle ) );
				if ( actualResultSet != null )
				{
					return new ChartBuilderGroupedQueryResultSetEvaluator( actualResultSet.getResultIterator( ),
							ChartReportItemUtil.hasAggregation( cm ) );
				}
			}

		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}
		catch ( RuntimeException e )
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

		return null;
	}

	/**
	 * @param maxRow
	 * @return
	 * @throws BirtException
	 */
	private DataRequestSession prepareDataRequestSession( int maxRow )
			throws BirtException
	{
		DataSessionContext dsc = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
				getReportDesignHandle( ) );

		Map appContext = new HashMap( );
		appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT,
				new Integer( maxRow ) );
		dsc.setAppContext( appContext );

		DataRequestSession session = DataRequestSession.newSession( dsc );
		return session;
	}

	/**
	 * The class is responsible to create query definition.
	 * 
	 * @since BIRT 2.3
	 */
	class BaseQueryHelper extends AbstractChartBaseQueryGenerator
	{

		/**
		 * Constructor of the class.
		 * 
		 * @param chart
		 * @param handle
		 * @param query
		 */
		public BaseQueryHelper( ExtendedItemHandle handle, Chart chart )
		{
			super( handle, chart );
		}

		/**
		 * Create query definition.
		 * 
		 * @param columnExpression
		 * @throws DataException
		 */
		public IDataQueryDefinition createBaseQuery( List columnExpression )
				throws ChartException
		{
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getQualifiedName( ) );

			for ( int i = 0; i < columnExpression.size( ); i++ )
			{
				String expr = (String) columnExpression.get( i );
				Binding colBinding = new Binding( expr );
				colBinding.setExpression( new ScriptExpression( expr ) );
				try
				{
					queryDefn.addBinding( colBinding );
				}
				catch ( DataException e )
				{
					throw new ChartException( ChartReportItemPlugin.ID,
							ChartException.DATA_BINDING,
							e );
				}
				fNameSet.add( expr );
			}

			generateGroupBindings( queryDefn );

			return queryDefn;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator#createBaseQuery(org.eclipse.birt.data.engine.api.IDataQueryDefinition)
		 */
		public IDataQueryDefinition createBaseQuery( IDataQueryDefinition parent )
		{
			throw new UnsupportedOperationException( "Don't be implemented in the class." ); //$NON-NLS-1$
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator#updateQueryDefinitionForSortOnAggregateExpression(org.eclipse.birt.chart.model.data.Query,
		 *      java.lang.String, java.lang.String)
		 */
		protected void updateQueryDefinitionForSortOnAggregateExpression(
				Query query, String bindName, String newExpr )
		{
			query.setDefinition( bindName );
		}

	} // End of class BaseQueryHelper.

	public boolean isInXTab( )
	{
		return ChartXTabUtil.isChartInXTab( itemHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#isSharedBinding()
	 */
	public boolean isSharedBinding( )
	{
		return ( itemHandle.getDataBindingReference( ) != null || isInMultiView( ) );
	}

	/**
	 * Check if current item handle is table shared binding reference.
	 * 
	 * @return
	 * @since 2.3
	 */
	private boolean isTableSharedBinding( )
	{
		return isSharedBinding( )
				&& ChartXTabUtil.getBindingCube( itemHandle ) == null;
	}

	/**
	 * Set predefined expressions for table shared binding, in table shared
	 * binding case, the binding/expression is read only, so predefined them.
	 * 
	 * @param context
	 * @param headers
	 * @since 2.3
	 */
	public void setPredefinedExpressionsForTableSharedBinding(
			ChartWizardContext context, ColumnBindingInfo[] headers )
	{
		if ( isSharedBinding( ) )
		{
			Map commons = new LinkedHashMap( );
			Map aggs = new LinkedHashMap( );
			Map groups = new LinkedHashMap( );
			Map groupsWithAgg = new LinkedHashMap( );
			Map groupsWithoutAgg = new LinkedHashMap( );

			for ( int i = 0; i < headers.length; i++ )
			{
				int type = headers[i].getColumnType( );
				switch ( type )
				{

					case ColumnBindingInfo.COMMON_COLUMN :
						commons.put( ExpressionUtil.createJSRowExpression( headers[i].getName( ) ),
								headers[i] );
						break;
					case ColumnBindingInfo.AGGREGATE_COLUMN :
						aggs.put( ExpressionUtil.createJSRowExpression( headers[i].getName( ) ),
								headers[i] );
						break;
					case ColumnBindingInfo.GROUP_COLUMN :
						groups.put( ExpressionUtil.createJSRowExpression( headers[i].getName( ) ),
								headers[i] );
						break;
				}
			}

			groupsWithoutAgg = new LinkedHashMap( groups );
			for ( Iterator iter = groupsWithoutAgg.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				String groupName = ( (ColumnBindingInfo) entry.getValue( ) ).getName( );
				Object[] aggsValues = aggs.values( ).toArray( );
				for ( int j = 0; j < aggs.size( ); j++ )
				{
					if ( groupName.equals( ( (ComputedColumnHandle) ( (ColumnBindingInfo) aggsValues[j] ).getObjectHandle( ) ).getAggregateOn( ) ) )
					{
						iter.remove( );
						groupsWithAgg.put( entry.getKey( ), entry.getValue( ) );
					}
				}
			}

			// Prepare category items.
			Object[][] categorys = new Object[groupsWithAgg.size( )
					+ commons.size( )][2];
			int index = 0;
			for ( Iterator iter = groupsWithAgg.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				categorys[index][0] = entry.getKey( );
				categorys[index][1] = entry.getValue( );
				index++;
			}
			for ( Iterator iter = commons.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				categorys[index][0] = entry.getKey( );
				categorys[index][1] = entry.getValue( );
				index++;
			}

			// Prepare Y optional items.
			int size = 0;
			if ( groupsWithAgg.size( ) > 0 )
			{
			    // Except inner most group, since the Y optional group is always not a inner most group.
				size = groupsWithAgg.size( ) - 1;
			}
			Object[][] optionals = new Object[groupsWithoutAgg.size( ) + size][2];
			index = 0;
			// add groups which don't include aggregate.
			for ( Iterator iter = groupsWithoutAgg.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				optionals[index][0] = entry.getKey( );
				optionals[index][1] = entry.getValue( );
				index++;
			}
			// Add groups which includes aggregate, except the inner most group.
			for ( Iterator iter = groupsWithAgg.entrySet( ).iterator( ); index < optionals.length
					&& iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				optionals[index][0] = entry.getKey( );
				optionals[index][1] = entry.getValue( );
				index++;
			}

			// Prepare value items.
			Object[][] values = new Object[aggs.size( ) + commons.size( )][2];
			index = 0;
			for ( Iterator iter = aggs.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				values[index][0] = entry.getKey( );
				values[index][1] = entry.getValue( );
				index++;
			}
			for ( Iterator iter = commons.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry entry = (Entry) iter.next( );
				values[index][0] = entry.getKey( );
				values[index][1] = entry.getValue( );
				index++;
			}

			context.addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
					categorys );
			context.addPredefinedQuery( ChartUIConstants.QUERY_VALUE, values );
			context.addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
					optionals );
		}
		else
		{
			context.addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY, null );
			context.addPredefinedQuery( ChartUIConstants.QUERY_VALUE, null );
			context.addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL, null );
		}
	}

	/**
	 * Returns report item handle of shared binding.
	 * 
	 * @since 2.3
	 */
	public ReportItemHandle getReportItemHandleOfSharedBinding( )
	{
		if ( isSharedBinding( ) )
		{
			if ( isInMultiView( ) )
			{
				return (ReportItemHandle) itemHandle.getContainer( )
						.getContainer( );
			}

			return itemHandle.getDataBindingReference( );
		}

		return itemHandle;
	}

	/**
	 * Returns expression for cube and shared binding.
	 * 
	 * @param expression
	 * @return data binding name of specified expression. If it is not in cude
	 *         mode, return <code>null</code>. If it is not shared binding,
	 *         return specified expression. If it is not a valid crosstab
	 *         expression, return <code>null</code>.
	 * @since 2.3
	 */
	public String getExpressionForCubeMode( String expression )
	{
		if ( ChartXTabUtil.getBindingCube( itemHandle ) == null )
		{
			return null;
		}

		if ( expression == null || !isSharedBinding( ) )
		{
			return expression;
		}

		ReportItemHandle reportItemHandle = getReportItemHandleOfSharedBinding( );
		for ( Iterator iter = reportItemHandle.getColumnBindings( ).iterator( ); iter.hasNext( ); )
		{
			ComputedColumnHandle cch = (ComputedColumnHandle) iter.next( );

			if ( cch.getExpression( ).equals( expression ) )
			{
				return ExpressionUtil.createJSDataExpression( cch.getName( ) );
			}
		}

		return null;
	}
}