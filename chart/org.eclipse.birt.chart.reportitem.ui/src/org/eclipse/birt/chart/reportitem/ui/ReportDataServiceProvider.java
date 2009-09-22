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

import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.DataRowExpressionEvaluatorAdapter;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.reportitem.AbstractChartBaseQueryGenerator;
import org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.BaseGroupedQueryResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.ChartBaseQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartCubeQueryHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.SharedCubeResultSetEvaluator;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.plugin.ChartReportItemPlugin;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartExpressionUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IDataQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.api.IResultIterator;
import org.eclipse.birt.data.engine.api.querydefn.BaseQueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.QueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.ICubeQueryResults;
import org.eclipse.birt.data.engine.olap.api.IPreparedCubeQuery;
import org.eclipse.birt.data.engine.olap.api.query.IBaseCubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.DataRequestSession;
import org.eclipse.birt.report.data.adapter.api.DataSessionContext;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.designer.data.ui.util.DataSetProvider;
import org.eclipse.birt.report.designer.data.ui.util.DummyEngineTask;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.preferences.PreferenceFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.impl.EngineTask;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineFactory;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.re.CrosstabQueryUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.core.resources.IProject;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.Collator;
import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Data service provider of chart builder for BIRT integration.
 */

public class ReportDataServiceProvider implements IDataServiceProvider
{

	protected ExtendedItemHandle itemHandle;

	protected ChartWizardContext context;

	/** The helper handle is used to do things for share binding case. */
	private final ShareBindingQueryHelper fShareBindingQueryHelper = new ShareBindingQueryHelper( );

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

	public void setWizardContext( ChartWizardContext context )
	{
		this.context = context;
	}

	ModuleHandle getReportDesignHandle( )
	{
		return itemHandle.getModuleHandle( );
	}

	protected String[] getAllDataSets( )
	{
		List<DataSetHandle> list = getReportDesignHandle( ).getVisibleDataSets( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			names[i] = list.get( i ).getQualifiedName( );
		}
		return names;
	}

	protected String[] getAllDataCubes( )
	{
		List<CubeHandle> list = getReportDesignHandle( ).getVisibleCubes( );
		String[] names = new String[list.size( )];
		for ( int i = 0; i < list.size( ); i++ )
		{
			names[i] = list.get( i ).getQualifiedName( );
		}
		return names;
	}

	/**
	 * Gets data cube from chart itself
	 * 
	 * @return data cube name
	 */
	String getDataCube( )
	{
		CubeHandle cube = itemHandle.getCube( );
		if ( cube == null )
		{
			return null;
		}
		return cube.getQualifiedName( );
	}
	
	/**
	 * Gets data cube from chart container
	 * 
	 * @return data cube name
	 */
	String getInheritedCube( )
	{
		CubeHandle cube = null;
		DesignElementHandle container = itemHandle.getContainer( );
		while ( container != null )
		{
			if ( container instanceof ReportItemHandle )
			{
				cube = ( (ReportItemHandle) container ).getCube( );
				if ( cube != null )
				{
					break;
				}
			}
			container = container.getContainer( );
		}
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
			boolean isPreviousDataBindingReference = false;
			if ( itemHandle.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				isPreviousDataBindingReference = true;
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
				if ( !cubeName.equals( getDataCube( ) ) || isPreviousDataBindingReference )
				{
					CubeHandle cubeHandle = getReportDesignHandle( ).findCube( cubeName );
					itemHandle.setCube( cubeHandle );
					// Clear parameters and filters, binding if dataset changed
					clearBindings( );
					generateBindings( generateComputedColumns( cubeHandle ) );
				}
			}
			ChartWizard.removeException( ChartWizard.RepDSProvider_Cube_ID );
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( ChartWizard.RepDSProvider_Cube_ID,
					e.getLocalizedMessage( ) );
		}
	}

	public final String[] getPreviewHeader( ) throws ChartException
	{
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		List<String> list = new ArrayList<String>( );
		boolean bInheritColumnsOnly = isInheritColumnsOnly( );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle binding = iterator.next( );
			// Remove the aggregation bindings if "inherit columns" used.
			if ( bInheritColumnsOnly && binding.getAggregateFunction( ) != null )
			{
				continue;
			}
			list.add( ( binding ).getName( ) );
		}
		return list.toArray( new String[list.size( )] );
	}

	public final List<Object[]> getPreviewData( ) throws ChartException
	{
		if ( isSharedBinding( ) || isInheritColumnsGroups( ) )
		{
			return fShareBindingQueryHelper.getPreviewRowData( getPreviewHeadersInfo( ),
					-1,
					true );
		}
		return getPreviewRowData( getPreviewHeader( true ), -1, true );
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
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		if ( iterator == null )
		{
			return new ColumnBindingInfo[]{};
		}
		
		List<ComputedColumnHandle> columnList = new ArrayList<ComputedColumnHandle>( );
		boolean bInheritColumnsOnly = isInheritColumnsOnly( );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle binding = iterator.next( );
			// Remove the aggregation bindings if "inherit columns" used.
			if ( bInheritColumnsOnly && binding.getAggregateFunction( ) != null )
			{
				continue;
			}
			columnList.add( binding );
		}

		if ( columnList.size( ) == 0 )
		{
			return new ColumnBindingInfo[]{};
		}

		ColumnBindingInfo[] columnHeaders = null;
		if ( isTableSharedBinding( ) || isInheritColumnsGroups( ) )
		{
			columnHeaders = fShareBindingQueryHelper.getPreviewHeadersInfo( columnList );
		}
		else
		{
			columnHeaders = new ColumnBindingInfo[columnList.size( )];
			for ( int i = 0; i < columnHeaders.length; i++ )
			{
				ComputedColumnHandle cch = columnList.get( i );
				columnHeaders[i] = new ColumnBindingInfo( cch.getName( ),
						ExpressionUtil.createJSRowExpression( cch.getName( ) ),
						null,
						null,
						cch );
			}
		}

		// Sort columns.
		List<ColumnBindingInfo> columnsSet = new ArrayList<ColumnBindingInfo>();
		for ( int i = columnHeaders.length - 1; i >=0; i-- )
			columnsSet.add( columnHeaders[i] );
		Collections.sort( columnsSet,  new ColumnNameComprator() );
		columnHeaders = columnsSet.toArray( new ColumnBindingInfo[]{} );
		return columnHeaders;
	}

	static class ColumnNameComprator
			implements
				Comparator<ColumnBindingInfo>,
				Serializable
	{

		/**
		 * Comment for <code>serialVersionUID</code>
		 */
		private static final long serialVersionUID = 1L;

		// use JDK String's compare method to map the same sorting logic as
		// column binding dialog(270079)
		// private com.ibm.icu.text.Collator collator =
		// com.ibm.icu.text.Collator.getInstance( );
		public int compare( ColumnBindingInfo src, ColumnBindingInfo target )
		{
			return src.getName( ).compareTo( target.getName( ) );
		}
	}
	
	private String[] getPreviewHeader( boolean isExpression )
			throws ChartException
	{
		ColumnBindingInfo[] cbis = getPreviewHeadersInfo( );
		String[] exps = new String[cbis.length];
		int i = 0;
		for ( ColumnBindingInfo cbi : cbis )
		{
			if ( isExpression )
			{
				exps[i] = ExpressionUtil.createJSRowExpression( cbi.getName(  ) );
			}
			else
			{
				exps[i] = cbi.getName( );
			}
			i++;
		}
		
		return exps;
	}

	protected final List<Object[]> getPreviewRowData(
			String[] columnExpression, int rowCount, boolean isStringType )
			throws ChartException
	{
		List<Object[]> dataList = new ArrayList<Object[]>( );

		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader( parentLoader,
				itemHandle.getModuleHandle( ) );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );

		ReportEngine engine = null;
		DummyEngineTask engineTask = null;
		DataRequestSession session = null;
		try
		{
			int maxRow = getMaxRow( );
			if ( isReportDesignHandle( ) )
			{
				engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( new EngineConfig( ) );
				engineTask = new DummyEngineTask( engine,
						new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) itemHandle.getModuleHandle( ) ),
						itemHandle.getModuleHandle( ) );
				session = prepareDataRequestSession( engineTask, maxRow, false );
				engineTask.run( );
			}
			else
			{
				session = prepareDataRequestSession( maxRow, false );
			}

			QueryDefinition queryDefn = new QueryDefinition( );
			queryDefn.setMaxRows( maxRow );
			queryDefn.setDataSetName( getDataSetFromHandle( ).getQualifiedName( ) );
			
			for ( int i = 0; i < columnExpression.length; i++ )
			{
				queryDefn.addResultSetExpression( columnExpression[i],
						new ScriptExpression( columnExpression[i] ) );
			}

			handleGroup( queryDefn, itemHandle, session.getModelAdaptor( ) );
			
			// Iterate parameter bindings to check if its expression is a
			// explicit
			// value, otherwise use default value of parameter as its
			// expression.
			resetParametersForDataPreview( getDataSetFromHandle( ), queryDefn );

			Iterator<FilterConditionHandle> filtersIterator = getFiltersIterator( );
			IQueryResults actualResultSet = session.executeQuery( queryDefn,
					null,
					filtersIterator,
					ChartReportItemUtil.getColumnDataBindings( itemHandle, true ) );
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

			if ( engine == null && session != null )
			{
				session.shutdown( );
			}
			if ( engineTask != null )
			{
				engineTask.close( );
			}
			if ( engine != null )
			{
				engine.destroy( );
			}
		}
		return dataList;
	}

	private boolean isReportDesignHandle( )
	{
		return itemHandle.getModuleHandle( ) instanceof ReportDesignHandle;
	}

	/**
	 * Add group definitions into query definition.
	 * 
	 * @param queryDefn
	 *            query definition.
	 * @param reportItemHandle
	 *            the item handle contains groups.
	 */
	private void handleGroup( QueryDefinition queryDefn,
			ExtendedItemHandle reportItemHandle, IModelAdapter modelAdapter )
	{
		ReportItemHandle handle = ChartReportItemUtil.getBindingHolder( reportItemHandle );
		if ( handle instanceof ListingHandle )
		{
			SlotHandle groups = ( (ListingHandle) handle ).getGroups( );
			 for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
			{
				ChartBaseQueryHelper.handleGroup( iter.next( ), queryDefn, modelAdapter );
			}
		}
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
		Map<String, DataSetParameterHandle> dsphMap = new LinkedHashMap<String, DataSetParameterHandle>( );
		if ( dataSetHandle != null )
		{
			Iterator iterParams = dataSetHandle.parametersIterator( );
			for ( ; iterParams.hasNext( ); )
			{
				DataSetParameterHandle dsph = (DataSetParameterHandle) iterParams.next( );
				dsphMap.put( dsph.getName( ), dsph );
			}
		}

		// 2. Get parameters setting from current handle.
		Iterator<ParamBindingHandle> iterParams = itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP )
				.iterator( );
		Map<String, ParamBindingHandle> pbhMap = new LinkedHashMap<String, ParamBindingHandle>( );
		while ( iterParams.hasNext( ) )
		{
			ParamBindingHandle paramBindingHandle = iterParams.next( );
			pbhMap.put( paramBindingHandle.getParamName( ), paramBindingHandle );
		}

		// 3. Check and reset parameters expression.
		Object[] names = pbhMap.keySet( ).toArray( );
		for ( int i = 0; i < names.length; i++ )
		{
			DataSetParameterHandle dsph = dsphMap.get( names[i] );
			ScriptExpression se = null;
			String expr = pbhMap.get( names[i] ).getExpression( );
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

	/**
	 * Gets data set from chart itself
	 * 
	 * @return data set name
	 */
	String getDataSet( )
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
		if ( isInMultiView( ) || isInXTabMeasureCell( ) )
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
	boolean isInMultiView( )
	{
		return itemHandle.getContainer( ) instanceof MultiViewsHandle;
	}

	/**
	 * Gets data set from chart container
	 * 
	 * @return data set name
	 */
	String getInheritedDataSet( )
	{
		List<DataSetHandle> list = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( list.size( ) > 0 )
		{
			return list.get( 0 ).getQualifiedName( );
		}
		return null;
	}

	public void setDataSet( String datasetName )
	{
		try
		{
			// Clean references if it's set
			boolean isPreviousDataBindingReference = false;
			if ( itemHandle.getDataBindingType( ) == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF )
			{
				isPreviousDataBindingReference = true;
				itemHandle.setDataBindingReference( null );
			}

			itemHandle.setCube( null );

			if ( datasetName == null )
			{

				if ( getDataSet( ) != null )
				{
					// Clean old bindings and use container's binding
					clearBindings( );
				}

				itemHandle.setDataSet( null );
			}
			else
			{
				DataSetHandle dataset = getReportDesignHandle( ).findDataSet( datasetName );
				if ( isPreviousDataBindingReference
						|| itemHandle.getDataSet( ) != dataset )
				{
					itemHandle.setDataSet( dataset );

					// Clean old bindings and add new bindings
					clearBindings( );
					generateBindings( generateComputedColumns( dataset ) );
				}
			}
			ChartWizard.removeException( ChartWizard.RepDSProvider_Set_ID );
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( ChartWizard.RepDSProvider_Set_ID,
					e.getLocalizedMessage( ) );
		}
	}

	private void clearBindings( ) throws SemanticException
	{
		clearProperty( itemHandle.getPropertyHandle( ReportItemHandle.PARAM_BINDINGS_PROP ) );
		clearProperty( itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP ) );
		clearProperty( itemHandle.getColumnBindings( ) );
	}

	private void clearProperty( PropertyHandle property )
			throws SemanticException
	{
		if ( property != null )
		{
			property.clearValue( );
		}
	}

	private Iterator<?> getPropertyIterator( PropertyHandle property )
	{
		if ( property != null )
		{
			return property.iterator( );
		}
		return null;
	}

	private void generateBindings( List<ComputedColumn> columnList )
			throws SemanticException
	{
		if ( columnList.size( ) > 0 )
		{
			for ( Iterator<ComputedColumn> iter = columnList.iterator( ); iter.hasNext( ); )
			{
				DEUtil.addColumn( itemHandle, iter.next( ), false );
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
	private List<ComputedColumn> generateComputedColumns( DataSetHandle dataSetHandle )
			throws SemanticException
	{
		if ( dataSetHandle != null )
		{
			List<ResultSetColumnHandle> resultSetColumnList = DataUtil.getColumnList( dataSetHandle );
			List<ComputedColumn> columnList = new ArrayList<ComputedColumn>( );
			for ( ResultSetColumnHandle resultSetColumn : resultSetColumnList )
			{
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						resultSetColumn.getColumnName( ) );
				column.setDataType( resultSetColumn.getDataType( ) );
				column.setExpression( DEUtil.getExpression( resultSetColumn ) );
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.emptyList( );
	}

	private List<ComputedColumn> generateComputedColumns( CubeHandle cubeHandle )
			throws SemanticException
	{
		if ( cubeHandle != null )
		{
			List<ComputedColumn> columnList = new ArrayList<ComputedColumn>( );
			// Add levels
			for ( LevelHandle levelHandle : ChartCubeUtil.getAllLevels( cubeHandle ) )
			{
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartCubeUtil.createLevelBindingName( levelHandle ) );
				column.setDataType( levelHandle.getDataType( ) );
				column.setExpression( ChartCubeUtil.createDimensionExpression( levelHandle ) );
				columnList.add( column );
			}
			// Add measures
			for ( MeasureHandle measureHandle : ChartCubeUtil.getAllMeasures( cubeHandle ) )
			{
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartCubeUtil.createMeasureBindingName( measureHandle ) );
				column.setDataType( measureHandle.getDataType( ) );
				column.setExpression( ExpressionUtil.createJSMeasureExpression( measureHandle.getName( ) ) );
				column.setAggregateFunction( measureHandle.getFunction( ) );
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.emptyList( );
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
		List<DataSetHandle> datasetList = DEUtil.getDataSetList( itemHandle.getContainer( ) );
		if ( datasetList.size( ) > 0 )
		{
			return datasetList.get( 0 );
		}
		return null;
	}

	/**
	 * The method is designed for overriding purpose. Subclasses can do some
	 * additional processing on the QueryDefinition here (e.g. add sorts or
	 * filters), before that it is going to be excuted.
	 * 
	 * @param queryDefn
	 */
	protected void processQueryDefinition( QueryDefinition queryDefn )
	{
		// nothing to do here.
	}

	private StyleHandle[] getAllStyleHandles( )
	{
		List<StyleHandle> sLst = getReportDesignHandle( ).getAllStyles( );
		StyleHandle[] list = sLst.toArray( new StyleHandle[sLst.size( )] );
		Arrays.sort( list, new Comparator<StyleHandle>( ) {

			Collator collator = Collator.getInstance( ULocale.getDefault( ) );

			public int compare( StyleHandle s1, StyleHandle s2 )
			{
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

		List<IPredefinedStyle> preStyles = new DesignEngine( new DesignConfig( ) ).getMetaData( )
				.getPredefinedStyles( );
		List<String> preStyleNames = new ArrayList<String>( );

		for ( int i = 0; i < preStyles.size( ); i++ )
		{
			preStyleNames.add( preStyles.get( i ).getName( ) );
		}

		List<String> sytleNames = new ArrayList<String>( );
		for ( int i = 0; i < newItems.length; i++ )
		{
			if ( preStyleNames.indexOf( newItems[i] ) == -1 )
			{
				sytleNames.add( newItems[i] );
			}
		}

		return sytleNames.toArray( new String[]{} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getAllStyleDisplayNames()
	 */
	public String[] getAllStyleDisplayNames( )
	{
		List<String> styles = Arrays.asList( getAllStyles( ) );
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
			ChartWizard.removeException( ChartWizard.RepDSProvider_Style_ID );
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( ChartWizard.RepDSProvider_Style_ID,
					e.getLocalizedMessage( ) );
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
				&& ChartReportItemUtil.getBindingHolder( itemHandle ) != null;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getDataType(java.lang.String)
	 */
	public DataType getDataType( String expression )
	{
		if ( expression == null || expression.trim( ).length( ) == 0 )
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

	private String getQueryStringForProcessing( String expression )
	{
		if ( expression.indexOf( "[\"" ) > 0 ) //$NON-NLS-1$
		{
			return expression.substring( expression.indexOf( "[\"" ) + 2, expression.indexOf( "\"]" ) ); //$NON-NLS-1$//$NON-NLS-2$
		}
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
		// Below calling 'ChartReportItemUtil.checkStringInExpression' exists
		// problem, it just check special case, like row["a"]+"Q"+row["b"].
		// In fact, if expression is a script, it should be no way to get the
		// return type.
		// Now the only thing we can do is to try to consider more situations
		// and avoid wrong check, we will just check the single line expression.
		// If it is not a single line expression, the data type will no be
		// checked. Of course even if we just check single line expression, it
		// still will get wrong result for valid expression, but it will avoid
		// error check in many situations.
		// The ChartReportItemUtil.checkStringInExpression will be refactored.
		
		boolean complexScripts = false;
		for ( int i = 0 ; i < expression.length( ); i++ )
		{
			if ( expression.charAt( i ) == '\n' && i != ( expression.length( ) - 1 ) )
			{
				complexScripts = true;
				break;
			}
		}
		
		// Checks if expression contains string
		if ( !complexScripts && ChartExpressionUtil.checkStringInExpression( expression ) )
		{
			return new Object[]{
					true, DataType.TEXT_LITERAL
			};
		}
		
		// simple means one binding expression without js function
		if ( !ChartExpressionUtil.isRowBinding( expression, false )
				&& !ChartExpressionUtil.isCubeBinding( expression, false ) )
		{
			return new Object[]{
					false, null
			};
		}

		Object[] returnObj = new Object[2];
		returnObj[0] = Boolean.FALSE;
		String columnName = getQueryStringForProcessing( expression );

		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getAllColumnBindingsIterator( itemHandle );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle cc = iterator.next( );
			if ( cc.getName( ).equalsIgnoreCase( columnName ) )
			{
				String dataType = cc.getDataType( );
				if ( dataType == null )
				{
					continue;
				}
				if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_STRING )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB ) )
				{
					returnObj[0] = Boolean.TRUE;
					returnObj[1] = DataType.TEXT_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN ) )
				{
					returnObj[0] = Boolean.TRUE;
					returnObj[1] = DataType.NUMERIC_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_DATE )
						|| dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_TIME ) )
				{
					returnObj[0] = Boolean.TRUE;
					returnObj[1] = DataType.DATE_TIME_LITERAL;
					break;
				}
				else if ( dataType.equals( DesignChoiceConstants.COLUMN_DATA_TYPE_ANY ) )
				{
					returnObj[0] = Boolean.TRUE;
					returnObj[1] = null;
					break;
				}
			}
		}
		return returnObj;
	}

	protected String[] getAllReportItemReferences( )
	{
		List<ReportItemHandle> referenceList = itemHandle.getNamedDataBindingReferenceList( );
		List<String> itemsWithName = new ArrayList<String>( );
		for ( int i = 0; i < referenceList.size( ); i++ )
		{
			String qualfiedName = referenceList.get( i ).getQualifiedName( );
			if ( qualfiedName != null && qualfiedName.length( ) > 0 )
			{
				itemsWithName.add( qualfiedName );
			}
		}
		return itemsWithName.toArray( new String[itemsWithName.size( )] );
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
			
			if ( referenceName == null )
			{
				itemHandle.setDataBindingReference( null );
				// Select the corresponding data set, no need to reset bindings
			}
			else
			{
				itemHandle.setDataSet( null );
				itemHandle.setCube( null );
				
				if ( !referenceName.equals( getReportItemReference( ) ) )
				{
					// Change reference and reset all bindings
					itemHandle.setDataBindingReference( (ReportItemHandle) getReportDesignHandle( ).findElement( referenceName ) );
					// //Model will reset bindings
					// clearBindings( );
					// generateBindings( );
				}
			}
			ChartWizard.removeException( ChartWizard.RepDSProvider_Ref_ID );
		}
		catch ( SemanticException e )
		{
			ChartWizard.showException( ChartWizard.RepDSProvider_Ref_ID,
					e.getLocalizedMessage( ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#prepareRowExpressionEvaluator(org.eclipse.birt.chart.model.Chart,
	 *      java.lang.String[], int, boolean)
	 */
	public IDataRowExpressionEvaluator prepareRowExpressionEvaluator( Chart cm,
			List<String> columnExpression, int rowCount, boolean isStringType )
			throws ChartException
	{
		// Set thread context class loader so Rhino can find POJOs in workspace
		// projects
		ClassLoader oldContextLoader = Thread.currentThread( )
				.getContextClassLoader( );
		ClassLoader parentLoader = oldContextLoader;
		if ( parentLoader == null )
			parentLoader = this.getClass( ).getClassLoader( );
		ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader( parentLoader,
				itemHandle.getModuleHandle( ) );
		Thread.currentThread( ).setContextClassLoader( newContextLoader );
		
		IDataRowExpressionEvaluator evaluator = null;
		DataRequestSession session = null;
		ReportEngine engine = null;
		DummyEngineTask engineTask = null;

		try
		{
			if ( isReportDesignHandle( ) )
			{
				engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( new EngineConfig( ) );
				engineTask = new DummyEngineTask( engine,
						new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) itemHandle.getModuleHandle( ) ),
						itemHandle.getModuleHandle( ) );
			}
			
			CubeHandle cube = ChartCubeUtil.getBindingCube( itemHandle );
			if ( cube != null )
			{
				if ( isReportDesignHandle( ) )
				{
					session = prepareDataRequestSession( engineTask,
							getMaxRow( ),
							true );
					engineTask.run( );
				}
				else
				{
					session = prepareDataRequestSession( getMaxRow( ), true );
				}

				// Create evaluator for data cube, even if in multiple view
				evaluator = createCubeEvaluator( cube, session, engineTask, cm );
			}
			else
			{
				if ( isReportDesignHandle( ) )
				{

					session = prepareDataRequestSession( engineTask,
							getMaxRow( ),
							false );
					engineTask.run( );
				}
				else
				{
					session = prepareDataRequestSession( getMaxRow( ), false );
				}
				
				// Create evaluator for data set
				if ( isSharedBinding( )
						&& !ChartReportItemUtil.isOldChartUsingInternalGroup( itemHandle,
								cm )
						|| isInheritColumnsGroups( ) )
				{
					if ( isSharingChart( true ) )
					{
						evaluator = createBaseEvaluator( (ExtendedItemHandle) itemHandle.getDataBindingReference( ),
								cm,
								columnExpression,
								session,
								engineTask );
					}
					else
					{
						evaluator = fShareBindingQueryHelper.createShareBindingEvaluator( cm,
								columnExpression,
							session,
							engineTask );
					}
				}
				else
				{
					evaluator = createBaseEvaluator( itemHandle,
							cm,
							columnExpression,
							session,
							engineTask );
					
				}
			}
			return evaluator;
		}
		catch ( BirtException e )
		{
			if ( engine == null && session != null )
			{
				session.shutdown( );
			}
			if ( engineTask != null )
			{
				engineTask.close( );
			}
			
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}
		catch ( RuntimeException e )
		{
			if ( engine == null && session != null )
			{
				session.shutdown( );
			}
			if ( engineTask != null )
			{
				engineTask.close( );
			}
			
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}
		finally
		{
			if ( engine != null )
			{
				engine.destroy( );
			}
			// Restore old thread context class loader
			Thread.currentThread( ).setContextClassLoader( oldContextLoader );

		}
	}

	/**
	 * Create base evaluator for chart using data set.
	 * 
	 * @param handle
	 * @param cm
	 * @param columnExpression
	 * @param session
	 * @param engineTask
	 * @return
	 * @throws ChartException
	 */
	private IDataRowExpressionEvaluator createBaseEvaluator(
			ExtendedItemHandle handle, Chart cm, List<String> columnExpression,
			final DataRequestSession session, final EngineTask engineTask )
			throws ChartException
	{
		IQueryResults actualResultSet;
		BaseQueryHelper cbqh = new BaseQueryHelper( handle, cm );
		QueryDefinition queryDefn = (QueryDefinition) cbqh.createBaseQuery( columnExpression );

		// Iterate parameter bindings to check if its expression is a
		// explicit
		// value, otherwise use default value of parameter as its
		// expression.
		resetParametersForDataPreview( getDataSetFromHandle( ), queryDefn );
		
		handleGroup( queryDefn, handle, session.getModelAdaptor( ) );
		
		processQueryDefinition( queryDefn );

		try
		{
			Iterator<FilterConditionHandle> filtersIterator = getFiltersIterator( );
			
			actualResultSet = session.executeQuery( queryDefn,
					null,
					filtersIterator,
					ChartReportItemUtil.getColumnDataBindings( handle, true ) );

			if ( actualResultSet != null )
			{
				if ( ChartReportItemUtil.isOldChartUsingInternalGroup( itemHandle,
						cm ) )
				{
					return createSimpleExpressionEvaluator( actualResultSet );
				}
				else
				{
					return new BaseGroupedQueryResultSetEvaluator( actualResultSet.getResultIterator( ),
							ChartReportItemUtil.isSetSummaryAggregation( cm ),
							cm ) {

						/*
						 * (non-Javadoc)
						 * 
						 * @seeorg.eclipse.birt.chart.reportitem.
						 * BaseGroupedQueryResultSetEvaluator#close()
						 */
						@Override
						public void close( )
						{
							super.close( );
							if ( engineTask != null )
							{
								engineTask.close( );
							}
							else if ( session != null )
							{
								session.shutdown( );
							}
						}

					};
				}
			}
		}
		catch ( BirtException e )
		{
			throw new ChartException( ChartReportItemUIActivator.ID,
					ChartException.DATA_BINDING,
					e );
		}

		return null;
	}

	/**
	 * Create a simple expression evaluator, it will causes chart engine using
	 * default grouping instead of DTE's grouping.
	 * 
	 * @param actualResultSet
	 * @return
	 * @throws BirtException
	 */
	private IDataRowExpressionEvaluator createSimpleExpressionEvaluator(
			IQueryResults actualResultSet ) throws BirtException
	{
		final IResultIterator resultIterator = actualResultSet.getResultIterator( );
		return new DataRowExpressionEvaluatorAdapter( ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory.
			 * IDataRowExpressionEvaluator
			 * #evaluate(java.lang.String)
			 */
			public Object evaluate( String expression )
			{
				try
				{
					return resultIterator.getValue( expression );
				}
				catch ( BirtException e )
				{
					return null;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory.
			 * IDataRowExpressionEvaluator
			 * #evaluateGlobal(java.lang.String)
			 */
			public Object evaluateGlobal( String expression )
			{
				return evaluate( expression );
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory.
			 * IDataRowExpressionEvaluator#next()
			 */
			public boolean next( )
			{
				try
				{
					return resultIterator.next( );
				}
				catch ( BirtException e )
				{
					return false;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory.
			 * IDataRowExpressionEvaluator#close()
			 */
			public void close( )
			{
				try
				{
					resultIterator.close( );
				}
				catch ( BirtException e )
				{
					return;
				}
			}

			/*
			 * (non-Javadoc)
			 * 
			 * @seeorg.eclipse.birt.chart.factory.
			 * IDataRowExpressionEvaluator#first()
			 */
			public boolean first( )
			{
				try
				{
					return resultIterator.next( );
				}
				catch ( BirtException e )
				{
					return false;
				}
			}
		};
	}

	/**
	 * Returns iterator of all filters.
	 * 
	 * @return
	 */
	private Iterator<FilterConditionHandle> getFiltersIterator( )
	{
		List<FilterConditionHandle> filterList = new ArrayList<FilterConditionHandle>( );
		PropertyHandle ph = null;
		// Inherit case.
		if ( getDataSet( ) == null && getReportItemReference( ) == null )
		{
			// Get filters on container.
			ReportItemHandle bindingHolder = ChartReportItemUtil.getBindingHolder( itemHandle );
			ph = bindingHolder == null ? null
					: bindingHolder.getPropertyHandle( ExtendedItemHandle.FILTER_PROP );
			if ( ph != null )
			{
				Iterator<FilterConditionHandle> filterIterator = ph.iterator( );
				if ( filterIterator != null )
				{
					while ( filterIterator.hasNext( ) )
					{
						// Design time doesn't support parent query expressions
						FilterConditionHandle filter = filterIterator.next( );
						if ( ( filter.getValue1( ) == null || filter.getValue1( )
								.indexOf( "._outer" ) < 0 ) //$NON-NLS-1$
								&& ( filter.getValue2( ) == null || filter.getValue2( )
										.indexOf( "._outer" ) < 0 ) ) //$NON-NLS-1$
						{
							filterList.add( filter );
						}
					}
				}
			}
		}
		
		// Get filters on current item hanlde.
		ph = itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP );
		if ( ph != null )
		{
			Iterator<FilterConditionHandle> filterIterator = ph.iterator( );
			if ( filterIterator != null )
			{
				while ( filterIterator.hasNext( ) )
				{
					// Design time doesn't support parent query expressions
					FilterConditionHandle filter = filterIterator.next( );
					if ( ( filter.getValue1( ) == null || filter.getValue1( )
							.indexOf( "._outer" ) < 0 )//$NON-NLS-1$
							&& ( filter.getValue2( ) == null || filter.getValue2( )
									.indexOf( "._outer" ) < 0 ) ) //$NON-NLS-1$
					{
						filterList.add( filter );
					}
				}
			}
		}
		
		return filterList.isEmpty( ) ? null : filterList.iterator( );
	}
	
	/**
	 * Creates the evaluator for Cube Live preview.
	 * 
	 * @param cube
	 * @param session
	 * @param engineTask
	 * @return
	 * @throws BirtException
	 */
	private IDataRowExpressionEvaluator createCubeEvaluator( CubeHandle cube,
			final DataRequestSession session, final EngineTask engineTask,
			final Chart cm )
			throws BirtException
	{
		// Use the chart model in context, because this model will be updated
		// once UI changes it. On the contrary, the model in handle may be old.
		IBaseCubeQueryDefinition qd = null;

		ReportItemHandle referredHandle = ChartReportItemUtil.getReportItemReference( itemHandle );
		boolean isChartCubeReference = ChartItemUtil.isChartReportItemHandle( referredHandle );
		if ( referredHandle != null && !isChartCubeReference )
		{
			// If it is 'sharing' case, include sharing crosstab and multiple
			// view, we just invokes referred crosstab handle to create query.
			ExtendedItemHandle bindingHandle = (ExtendedItemHandle) referredHandle;
			qd = CrosstabQueryUtil.createCubeQuery( (CrosstabReportItemHandle) bindingHandle.getReportItem( ),
					null,
					true,
					true,
					true,
					true,
					true,
					true );
		}
		else
		{
			qd = new ChartCubeQueryHelper( itemHandle,
				cm ).createCubeQuery( null );
		}

		session.defineCube( cube );

		// Always cube query returned
		IPreparedCubeQuery ipcq = session.prepare( (ICubeQueryDefinition) qd );

		// Sharing case
		if ( referredHandle != null && !isChartCubeReference )
		{
			return new SharedCubeResultSetEvaluator( (ICubeQueryResults) session.execute( ipcq,
					null,
					new ScriptContext( ) ),
					qd,
					cm ) {

				/*
				 * (non-Javadoc)
				 * 
				 * @see
				 * org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#
				 * close()
				 */
				@Override
				public void close( )
				{
					super.close( );
					if ( engineTask != null )
					{
						engineTask.close( );
					}
					else if ( session != null )
					{
						session.shutdown( );
					}
				}
			};
		}

		return new BIRTCubeResultSetEvaluator( (ICubeQueryResults) session.execute( ipcq,
				null,
				new ScriptContext( ) ) ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see
			 * org.eclipse.birt.chart.reportitem.BIRTCubeResultSetEvaluator#
			 * close()
			 */
			@Override
			public void close( )
			{
				super.close( );
				if ( engineTask != null )
				{
					engineTask.close( );
				}
				else if ( session != null )
				{
					session.shutdown( );
				}
			}

		};
	}

	/**
	 * @param maxRow
	 * @param isCubeMode
	 * @return
	 * @throws BirtException
	 */
	private DataRequestSession prepareDataRequestSession(
			EngineTask engineTask, int maxRow, boolean isCudeMode )
			throws BirtException
	{

		// DataSessionContext dsc = new DataSessionContext(
		// DataSessionContext.MODE_DIRECT_PRESENTATION,
		// getReportDesignHandle( ) );

		// Bugzilla #210225.
		// If filter is set on report item handle of chart, here should not use
		// data cache mode and get all valid data firstly, then set row limit on
		// query(QueryDefinition.setMaxRows) to get required rows.
		PropertyHandle filterProperty = itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP );
		if ( filterProperty == null
				|| filterProperty.getListValue( ) == null
				|| filterProperty.getListValue( ).size( ) == 0 )
		{
			Map<String, Integer> appContext = new HashMap<String, Integer>( );
			if ( !isCudeMode )
			{
				appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT,
						Integer.valueOf( maxRow ) );
			}
			else
			{
				appContext.put( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE,
						Integer.valueOf( maxRow ) );
				appContext.put( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE,
						Integer.valueOf( maxRow ) );
			}
			engineTask.setAppContext( appContext );
		}

		DataRequestSession session = engineTask.getDataSession( );
		return session;
	}

	/**
	 * @param maxRow
	 * @param isCubeMode
	 * @return
	 * @throws BirtException
	 */
	private DataRequestSession prepareDataRequestSession( int maxRow,
			boolean isCudeMode ) throws BirtException
	{
		DataSessionContext dsc = new DataSessionContext( DataSessionContext.MODE_DIRECT_PRESENTATION,
				getReportDesignHandle( ) );

		// Bugzilla #210225.
		// If filter is set on report item handle of chart, here should not use
		// data cache mode and get all valid data firstly, then set row limit on
		// query(QueryDefinition.setMaxRows) to get required rows.
		PropertyHandle filterProperty = itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP );
		if ( filterProperty == null
				|| filterProperty.getListValue( ) == null
				|| filterProperty.getListValue( ).size( ) == 0 )
		{
			Map<String, Integer> appContext = new HashMap<String, Integer>( );
			if ( !isCudeMode )
			{
				appContext.put( DataEngine.DATA_SET_CACHE_ROW_LIMIT,
						Integer.valueOf( maxRow ) );
			}
			else
			{
				appContext.put( DataEngine.CUBECURSOR_FETCH_LIMIT_ON_COLUMN_EDGE,
						Integer.valueOf( maxRow ) );
				appContext.put( DataEngine.CUBECUSROR_FETCH_LIMIT_ON_ROW_EDGE,
						Integer.valueOf( maxRow ) );
			}
			dsc.setAppContext( appContext );
		}

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
			// Used query expressions have been wrapped as bindings, so do not
			// wrap them again.
			super( handle, chart, false );
		}

		/**
		 * Create query definition.
		 * 
		 * @param columnExpression
		 * @throws DataException
		 */
		public IDataQueryDefinition createBaseQuery(
				List<String> columnExpression ) throws ChartException
		{
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );

			DataSetHandle dsHandle = getDataSetFromHandle( );

			queryDefn.setDataSetName( dsHandle == null ? null
					: dsHandle.getQualifiedName( ) );

			for ( int i = 0; i < columnExpression.size( ); i++ )
			{
				String expr = columnExpression.get( i );
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

			generateExtraBindings( queryDefn );

			return queryDefn;
		}

		/**
		 * Add aggregate bindings of value series for grouping case.
		 * 
		 * @param query
		 * @param seriesDefinitions
		 * @param innerMostGroupDef
		 * @param valueExprMap
		 * @param baseSD
		 * @throws DataException
		 * @throws DataException
		 */
		protected void addValueSeriesAggregateBindingForGrouping(
				BaseQueryDefinition query,
				EList<SeriesDefinition> seriesDefinitions,
				GroupDefinition innerMostGroupDef,
				Map<String, String[]> valueExprMap, SeriesDefinition baseSD )
				throws ChartException
		{
			for ( SeriesDefinition orthSD : seriesDefinitions )
			{
				Series series = orthSD.getDesignTimeSeries( );

				// The qlist contains available expressions which have
				// aggregation.
				List<Query> qlist = ChartEngine.instance( )
						.getDataSetProcessor( series.getClass( ) )
						.getDataDefinitionsForGrouping( series );

				for ( Query qry : series.getDataDefinition( ) )
				{
					String expr = qry.getDefinition( );
					if ( expr == null || "".equals( expr ) ) //$NON-NLS-1$
					{
						continue;
					}

					String aggName = ChartUtil.getAggregateFuncExpr( orthSD,
							baseSD,
							qry );
					if ( aggName == null || "".equals( aggName ) ) //$NON-NLS-1$
					{
						continue;
					}

					// Get a unique name.
					String name = ChartUtil.generateBindingNameOfValueSeries( qry,
							orthSD,
							baseSD );
					if ( fNameSet.contains( name ) )
					{
						query.getBindings( ).remove( name );
					}
					
					fNameSet.add( name );
					
					Binding colBinding = new Binding( name );

					colBinding.setDataType( org.eclipse.birt.core.data.DataType.ANY_TYPE );

					if ( qlist.contains( qry ) ) // Has aggregation.
					{
						// Set binding expression by different aggregation, some
						// aggregations can't set expression, like Count and so
						// on.
						try
						{
							setBindingExpressionDueToAggregation( colBinding,
									expr,
									aggName );
						}
						catch ( DataException e1 )
						{
							throw new ChartException( ChartReportItemPlugin.ID,
									ChartException.DATA_BINDING,
									e1 );
						}

						if ( innerMostGroupDef != null )
						{
							try
							{
								colBinding.addAggregateOn( innerMostGroupDef.getName( ) );
							}
							catch ( DataException e )
							{
								throw new ChartException( ChartReportItemPlugin.ID,
										ChartException.DATA_BINDING,
										e );
							}
						}

						// Set aggregate parameters.
						colBinding.setAggrFunction( ChartReportItemUtil.convertToDtEAggFunction( aggName ) );

						IAggregateFunction aFunc = PluginSettings.instance( )
								.getAggregateFunction( aggName );
						if ( aFunc.getParametersCount( ) > 0 )
						{
							String[] parameters = ChartUtil.getAggFunParameters( orthSD,
									baseSD,
									qry );

							for ( int i = 0; i < parameters.length
									&& i < aFunc.getParametersCount( ); i++ )
							{
								String param = parameters[i];
								colBinding.addArgument( new ScriptExpression( param ) );
							}
						}
					}
					else
					{
						// Direct setting expression for non-aggregation case.
						colBinding.setExpression( new ScriptExpression( expr ) );
					}

					String newExpr = getExpressionForEvaluator( name );

					try
					{
						query.addBinding( colBinding );
					}
					catch ( DataException e )
					{
						throw new ChartException( ChartReportItemPlugin.ID,
								ChartException.DATA_BINDING,
								e );
					}

					valueExprMap.put( expr, new String[]{
							name, newExpr
					} );
				}
			}
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
	} // End of class BaseQueryHelper.

	boolean isInXTabMeasureCell( )
	{
		return ChartCubeUtil.isInXTabMeasureCell( itemHandle );
	}

	boolean isPartChart( )
	{
		return ChartCubeUtil.isPlotChart( itemHandle )
				|| ChartCubeUtil.isAxisChart( itemHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#isSharedBinding()
	 */
	boolean isSharedBinding( )
	{
		return ( itemHandle.getDataBindingReference( ) != null || isInMultiView( ) );
	}

	/**
	 * Check if current item handle is table shared binding reference.
	 * 
	 * @return
	 * @since 2.3
	 */
	boolean isTableSharedBinding( )
	{
		return isSharedBinding( )
				&& ChartCubeUtil.getBindingCube( itemHandle ) == null;
	}

	/**
	 * Set predefined expressions for table shared binding, in table shared
	 * binding case, the binding/expression is read only, so predefined them.
	 * 
	 * @param headers
	 * @since 2.3
	 */
	public void setPredefinedExpressions( ColumnBindingInfo[] headers )
	{
		if ( isSharedBinding( ) || isInheritColumnsGroups( ) )
		{
			fShareBindingQueryHelper.setPredefinedExpressions( headers );
		}
		else
		{
			// Add expressions into predefined query for the content assist
			// function when user set category/value series/Y optional
			// expressions on UI.
			Map<String, ColumnBindingInfo> commons = new LinkedHashMap<String, ColumnBindingInfo>( );
			for ( int i = 0; i < headers.length; i++ )
			{
				commons.put( ExpressionUtil.createJSRowExpression( headers[i].getName( ) ),
						headers[i] );
			}
			Object[][] expressions = new Object[commons.size( )][2];
			int index = 0;
			for ( Iterator<Map.Entry<String, ColumnBindingInfo>> iter = commons.entrySet( )
					.iterator( ); iter.hasNext( ); )
			{
				Entry<String, ColumnBindingInfo> entry = iter.next( );
				expressions[index][0] = entry.getKey( );
				expressions[index][1] = entry.getValue( );
				index++;
			}
			
			context.addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY, expressions );
			context.addPredefinedQuery( ChartUIConstants.QUERY_VALUE, expressions );
			context.addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL, expressions );
		}
	}

	/**
	 * Returns report item handle.
	 * 
	 * @since 2.3
	 */
	public ReportItemHandle getReportItemHandle( )
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
	 * The class declares some methods for processing query share with table.
	 * 
	 * @since 2.3
	 */
	class ShareBindingQueryHelper
	{
		/**
		 * Set predefined expressions for UI selections.
		 * 
		 * @param headers
		 */
		private void setPredefinedExpressions( ColumnBindingInfo[] headers )
		{
			Object[] expressionsArray = getPredefinedExpressionsForSharing( headers );

			context.addPredefinedQuery( ChartUIConstants.QUERY_CATEGORY,
					(Object[]) expressionsArray[0] );
			context.addPredefinedQuery( ChartUIConstants.QUERY_OPTIONAL,
					(Object[]) expressionsArray[1] );
			context.addPredefinedQuery( ChartUIConstants.QUERY_VALUE,
					(Object[]) expressionsArray[2] );
		}

		/**
		 * Returns predefined expressions for sharing case.
		 * 
		 * @param headers
		 * @return
		 */
		private Object[] getPredefinedExpressionsForSharing(
				ColumnBindingInfo[] headers )
		{
			Map<String, ColumnBindingInfo> commons = new LinkedHashMap<String, ColumnBindingInfo> ( );
			Map<String, ColumnBindingInfo>  aggs = new LinkedHashMap<String, ColumnBindingInfo> ( );
			Map<String, ColumnBindingInfo>  groups = new LinkedHashMap<String, ColumnBindingInfo> ( );
			Map<String, ColumnBindingInfo>  groupsWithAgg = new LinkedHashMap<String, ColumnBindingInfo> ( );
			Map<String, ColumnBindingInfo> groupsWithoutAgg;

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

			groupsWithoutAgg = new LinkedHashMap<String, ColumnBindingInfo> ( groups );
			for ( Iterator<Entry <String, ColumnBindingInfo> > iter = groupsWithoutAgg.entrySet( ).iterator( ); iter.hasNext( ); )
			{
				Entry<String, ColumnBindingInfo>  entry = iter.next( );
				String groupName = entry.getValue( ).getName( );
				ColumnBindingInfo[] aggsValues = aggs.values( ).toArray( new ColumnBindingInfo[]{} );

				// Remove some groups defined aggregate.
				for ( int j = 0; j < aggs.size( ); j++ )
				{
					if ( groupName.equals( ( (ComputedColumnHandle) aggsValues[j].getObjectHandle( ) ).getAggregateOn( ) ) )
					{
						iter.remove( );
						groupsWithAgg.put( entry.getKey( ), entry.getValue( ) );
						break;
					}
				}
			}

			// TODO
			// ? Now(2008/02/18), for share binding case, we use following
			// rules:
			// 1. Y optional grouping just allow to use first grouping
			// definition in table.
			// 2. Category series allow to use all grouping definitions and
			// binding.

			// Prepare category and Y optional items.
			
			Object[][] categorys = new Object[0][];
			Object[][] optionals = new Object[0][];
		
			categorys = new Object[groups.size( ) + commons.size( )][2];
			int index = 0;
			for ( Entry<String, ColumnBindingInfo> entry : groups.entrySet( ) )
			{
				categorys[index][0] = entry.getKey( );
				categorys[index][1] = entry.getValue( );
				index++;
			}
			for ( Entry<String, ColumnBindingInfo> entry : commons.entrySet( ) )
			{
				categorys[index][0] = entry.getKey( );
				categorys[index][1] = entry.getValue( );
				index++;
			}

			// Prepare Y optional items.
			int size = ( groups.size( ) > 0 ) ? 1 : 0;
			optionals = new Object[size][2];
			if ( groups.size( ) > 0 )
			{
				Entry<String, ColumnBindingInfo> entry = groups.entrySet( ).iterator( ).next( );;
				optionals[0][0] = entry.getKey( );
				optionals[0][1] = entry.getValue( );
			}

			// Prepare value items.
			Object[][] values = new Object[aggs.size( ) + commons.size( )][2];
			index = 0;
			for ( Entry<String, ColumnBindingInfo> entry : aggs.entrySet( ) )
			{
				values[index][0] = entry.getKey( );
				values[index][1] = entry.getValue( );
				index++;
			}
			for ( Entry<String, ColumnBindingInfo> entry : commons.entrySet( ) )
			{
				values[index][0] = entry.getKey( );
				values[index][1] = entry.getValue( );
				index++;
			}

			return new Object[]{
					categorys, optionals, values
			};
		}

		/**
		 * Prepare data expression evaluator for query share with table.
		 * 
		 * @param cm
		 * @param session
		 * @param engineTask
		 * @return
		 * @throws BirtException
		 * @throws AdapterException
		 * @throws DataException
		 * @throws ChartException
		 */
		private IDataRowExpressionEvaluator createShareBindingEvaluator(
				Chart cm, List<String> columnExpression,
				final DataRequestSession session,
				final EngineTask engineTask )
				throws BirtException,
				AdapterException, DataException, ChartException
		{
			IQueryResults actualResultSet;
			// Now only create query for table shared binding.
			// Create query definition.
			QueryDefinition queryDefn = new QueryDefinition( );
			int maxRow = getMaxRow( );
			queryDefn.setMaxRows( maxRow );

			// Binding columns, aggregates, filters and sorts.
			final Map<String, String> bindingExprsMap = new HashMap<String, String>( );

			Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
			List<ComputedColumnHandle> columnList = new ArrayList<ComputedColumnHandle>( );
			while ( iterator.hasNext( ) )
			{
				columnList.add( iterator.next( ) );
			}

			generateShareBindingsWithTable( getPreviewHeadersInfo( columnList ),
					queryDefn,
					session,
					bindingExprsMap );

			// Add custom expression of chart.
			addCustomExpressions( queryDefn,
					cm,
					columnExpression,
					bindingExprsMap );

			actualResultSet = session.executeQuery( queryDefn,
					null,
					getPropertyIterator( itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP ) ),
					null );

			if ( actualResultSet != null )
			{
				return new BaseGroupedQueryResultSetEvaluator( actualResultSet.getResultIterator( ),
						ChartReportItemUtil.isSetSummaryAggregation( cm ),
						cm ) {

					/*
					 * (non-Javadoc)
					 * 
					 * @see org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator#evaluate(java.lang.String)
					 */
					public Object evaluate( String expression )
					{
						try
						{
							String newExpr = bindingExprsMap.get( expression );
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
							sLogger.log( e );
						}
						return null;
					}

					/*
					 * (non-Javadoc)
					 * 
					 * @seeorg.eclipse.birt.chart.reportitem.
					 * BaseGroupedQueryResultSetEvaluator#close()
					 */
					@Override
					public void close( )
					{
						super.close( );
						if ( engineTask != null )
						{
							engineTask.close( );
						}
						else if ( session != null )
						{
							session.shutdown( );
						}
					}
				};
			}
			return null;
		}

		/**
		 * Add custom expressions of chart to query.
		 * 
		 * @param queryDefn
		 * @param cm
		 * @param bindingExprsMap
		 * @throws DataException
		 */
		private void addCustomExpressions( QueryDefinition queryDefn, Chart cm,
				List<String> columnExpression,
				final Map<String, String> bindingExprsMap )
				throws DataException
		{
			List<Query> queryList = ChartBaseQueryHelper.getAllQueryExpressionDefinitions( cm );

			Set<String> exprSet = new HashSet<String>( );
			for ( Query query : queryList )
			{
				String expr = query.getDefinition( );
				if ( expr != null )
				{
					exprSet.add( expr );
				}
			}

			exprSet.addAll( columnExpression );

			for ( String expr : exprSet )
			{
				if ( expr.length( ) > 0 &&
						!bindingExprsMap.containsKey( expr ) )
				{
					String name = StructureFactory.newComputedColumn( itemHandle,
							ChartUtil.escapeSpecialCharacters( expr ) )
							.getName( );
					queryDefn.addBinding( new Binding( name,
							new ScriptExpression( expr ) ) );

					bindingExprsMap.put( expr, name );
				}
			}
		}

		/**
		 * Returns columns header info.
		 * 
		 * @throws ChartException
		 * @since BIRT 2.3
		 */
		private final ColumnBindingInfo[] getPreviewHeadersInfo(
				List<ComputedColumnHandle> columnList ) throws ChartException
		{
			if ( columnList == null || columnList.size( ) == 0 )
			{
				return new ColumnBindingInfo[0];
			}

			ColumnBindingInfo[] columnHeaders = null;

			// Process grouping and aggregate on group case.
			// Get groups.
			List<GroupHandle> groupList = getGroupsOfSharedBinding( );

			columnHeaders = new ColumnBindingInfo[columnList.size( )
					+ groupList.size( )];
			int index = 0;
			for ( int i = 0; i < groupList.size( ); i++ )
			{
				GroupHandle gh = groupList.get( i );
				String groupName = gh.getName( );
				String groupKeyExpr = gh.getKeyExpr( );
				String tooltip = Messages.getString( "ReportDataServiceProvider.Tooltip.GroupExpression" ) + groupKeyExpr; //$NON-NLS-1$
				columnHeaders[index++] = new ColumnBindingInfo( groupName,
						groupKeyExpr, // Use expression for group.
						ColumnBindingInfo.GROUP_COLUMN,
						"icons/obj16/group.gif", //$NON-NLS-1$
						tooltip,
						gh );

				for ( Iterator<ComputedColumnHandle> iter = columnList.iterator( ); iter.hasNext( ); )
				{
					ComputedColumnHandle cch = iter.next( );

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
			for ( Iterator<ComputedColumnHandle> iter = columnList.iterator( ); iter.hasNext( ); )
			{
				ComputedColumnHandle cch = iter.next( );

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
			for ( Iterator<ComputedColumnHandle> iter = columnList.iterator( ); iter.hasNext( ); )
			{
				ComputedColumnHandle cch = iter.next( );
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

			return columnHeaders;
		}

		/**
		 * Returns groups in shared binding.
		 * 
		 * @return
		 */
		private List<GroupHandle> getGroupsOfSharedBinding( )
		{
			List<GroupHandle> groupList = new ArrayList<GroupHandle>( );
			ListingHandle table = null;
			if ( isInheritColumnsGroups( ) )
			{
				table = findListingInheritance( );
			}
			else
			{
				ReportItemHandle handle = getReportItemHandle( );
				table = getSharedListingHandle( handle );
			}
			if ( table != null )
			{
				SlotHandle groups = table.getGroups( );
				for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
				{
					groupList.add( iter.next( ) );
				}
			}
			return groupList;
		}

		/**
		 * Returns correct shared table handle when chart is sharing table's
		 * query.
		 * 
		 * @param itemHandle
		 * @return
		 */
		private ListingHandle getSharedListingHandle(
				ReportItemHandle itemHandle )
		{
			if ( itemHandle instanceof ListingHandle )
			{
				return (ListingHandle) itemHandle;
			}

			ReportItemHandle handle = itemHandle.getDataBindingReference( );
			if ( handle != null )
			{
				return getSharedListingHandle( handle );
			}

			if ( itemHandle.getContainer( ) instanceof MultiViewsHandle )
			{
				return getSharedListingHandle( (ReportItemHandle) itemHandle.getContainer( )
						.getContainer( ) );
			}

			return null;
		}

		/**
		 * Returns preview row data for table shared binding, it will share
		 * table's bindings and get them data.
		 * 
		 * @param headers
		 * @param rowCount
		 * @param isStringType
		 * @return
		 * @throws ChartException
		 * @since 2.3
		 */
		private List<Object[]> getPreviewRowData( ColumnBindingInfo[] headers,
				int rowCount, boolean isStringType ) throws ChartException
		{
			List<Object[]> dataList = new ArrayList<Object[]>( );
			// Set thread context class loader so Rhino can find POJOs in
			// workspace
			// projects
			ClassLoader oldContextLoader = Thread.currentThread( )
					.getContextClassLoader( );
			ClassLoader parentLoader = oldContextLoader;
			if ( parentLoader == null )
				parentLoader = this.getClass( ).getClassLoader( );
			ClassLoader newContextLoader = DataSetProvider.getCustomScriptClassLoader( parentLoader,
					itemHandle.getModuleHandle( ) );
			Thread.currentThread( ).setContextClassLoader( newContextLoader );

			ReportEngine engine = null;
			DummyEngineTask engineTask = null;
			DataRequestSession session = null;
			try
			{
				int maxRow = getMaxRow( );
				if ( isReportDesignHandle( ) )
				{
					engine = (ReportEngine) new ReportEngineFactory( ).createReportEngine( new EngineConfig( ) );
					engineTask = new DummyEngineTask( engine,
							new ReportEngineHelper( engine ).openReportDesign( (ReportDesignHandle) itemHandle.getModuleHandle( ) ),
							itemHandle.getModuleHandle( ) );
					session = prepareDataRequestSession( engineTask,
							maxRow,
							false );
					engineTask.run( );
				}
				else
				{
					session = prepareDataRequestSession( maxRow, false );
				}

				// Create query definition.
				QueryDefinition queryDefn = new QueryDefinition( );
				queryDefn.setMaxRows( maxRow );

				// Binding columns, aggregates, filters and sorts.
				List<String> columns = generateShareBindingsWithTable( headers,
						queryDefn,
						session,
						new HashMap<String, String>( ) );

				IQueryResults actualResultSet = session.executeQuery( queryDefn,
						null,
						getPropertyIterator( itemHandle.getPropertyHandle( ExtendedItemHandle.FILTER_PROP ) ),
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
								record[n] = DataTypeUtil.toString( iter.getValue( columns.get( n ) ) );
							}
							dataList.add( record );
						}
						else
						{
							Object[] record = new Object[columnCount];
							for ( int n = 0; n < columnCount; n++ )
							{
								record[n] = iter.getValue( columns.get( n ) );
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
				Thread.currentThread( )
						.setContextClassLoader( oldContextLoader );
				
				if ( engine == null && session != null )
				{
					session.shutdown( );
				}
				if ( engineTask != null )
				{
					engineTask.close( );
				}
				if ( engine != null )
				{
					engine.destroy( );
				}
			}
			return dataList;
		}

		/**
		 * Generate share bindings with table into query.
		 * 
		 * @param headers
		 * @param queryDefn
		 * @param session
		 * @param bindingExprsMap
		 * @return
		 * @throws AdapterException
		 * @throws DataException
		 */
		private List<String> generateShareBindingsWithTable(
				ColumnBindingInfo[] headers, QueryDefinition queryDefn,
				DataRequestSession session, Map<String, String> bindingExprsMap )
				throws AdapterException, DataException
		{
			List<String> columns = new ArrayList<String>( );
			ReportItemHandle reportItemHandle = getReportItemHandle( );
			if ( isInheritColumnsGroups( ) )
			{
				reportItemHandle = findListingInheritance( );
			}
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
						// Use original binding expression as map key for
						// aggregate
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

			// Add sorts.
			if ( reportItemHandle instanceof ListingHandle )
			{
				queryDefn.getSorts( )
						.addAll( ChartBaseQueryHelper.createSorts( ( (ListingHandle) reportItemHandle ).sortsIterator( ) ) );
			}

			return columns;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#update(java.lang.String,
	 *      java.lang.Object)
	 */
	public boolean update( String type, Object value )
	{
		boolean isUpdated = false;
		if ( ChartUIConstants.COPY_SERIES_DEFINITION.equals( type ))
		{
			copySeriesDefinition( value );
		}
		else if ( ChartUIConstants.QUERY_VALUE.equals( type )
				&& getDataCube( ) != null
				&& isSharedBinding( ) )
		{
			// Need to automated set category/Y optional bindings by value
			// series
			// binding.
			// 1. Get all bindings.
			Map<String, ComputedColumnHandle> bindingMap = new LinkedHashMap<String, ComputedColumnHandle>( );
			for ( Iterator<ComputedColumnHandle> bindings = ChartReportItemUtil.getAllColumnBindingsIterator( itemHandle ); bindings.hasNext( ); )
			{
				ComputedColumnHandle column = bindings.next( );
				bindingMap.put( column.getName( ), column );
			}

			// 2. Get value series bindings.
			String bindingName = ChartExpressionUtil.getCubeBindingName( (String) value,
					false );
			ComputedColumnHandle computedBinding = bindingMap.get( bindingName );

			// 3. Get all levels which value series binding aggregate on and set
			// correct binding to category/ Y optional.
			List<String> aggOnList = computedBinding.getAggregateOnList( );
			if ( aggOnList.size( ) > 0 )
			{
				String[] levelNames = CubeUtil.splitLevelName( aggOnList.get( 0 ) );
				String dimExpr = ExpressionUtil.createJSDimensionExpression( levelNames[0],
						levelNames[1] );
				List<String> names = ChartCubeUtil.getRelatedBindingNames( dimExpr,
						bindingMap.values( ) );
				// Set category.
				if ( names.size( ) > 0 )
				{
					SeriesDefinition sd = ChartUIUtil.getBaseSeriesDefinitions( context.getModel( ) )
							.get( 0 );
					sd.getDesignTimeSeries( )
							.getDataDefinition( )
							.get( 0 )
							.setDefinition( ExpressionUtil.createJSDataExpression( names.get( 0 ) ) );
					isUpdated = true;
				}
			}

			if ( aggOnList.size( ) > 1 )
			{
				String[] levelNames = CubeUtil.splitLevelName( aggOnList.get( 1 ) );
				String dimExpr = ExpressionUtil.createJSDimensionExpression( levelNames[0],
						levelNames[1] );
				List<String> names = ChartCubeUtil.getRelatedBindingNames( dimExpr,
						bindingMap.values( ) );
				// Set Y optional.
				int size = names.size( );
				if ( size > 0 )
				{
					for ( Iterator<SeriesDefinition> iter = ChartUIUtil.getAllOrthogonalSeriesDefinitions( context.getModel( ) )
							.iterator( ); iter.hasNext( ); )
					{
						SeriesDefinition sd = iter.next( );
						sd.getQuery( )
								.setDefinition( ExpressionUtil.createJSDataExpression( names.get( 0 ) ) );
						isUpdated = true;
					}
				}
			}
			else
			{
				// When chart share cube with other chart, the measure
				// expression(value series expressions) doesn't contain level
				// information, so here only update Y optional expression for
				// sharing with crosstab case.
				if ( !ChartItemUtil.isChartReportItemHandle( ChartItemUtil.getReportItemReference( itemHandle ) ) )
				{
					for ( Iterator<SeriesDefinition> iter = ChartUIUtil.getAllOrthogonalSeriesDefinitions( context.getModel( ) )
							.iterator( ); iter.hasNext( ); )
					{
						SeriesDefinition sd = iter.next( );
						sd.getQuery( ).setDefinition( "" ); //$NON-NLS-1$
						isUpdated = true;
					}
				}
			}
		}
		else if ( ChartUIConstants.QUERY_CATEGORY.equals( type ) && value instanceof String )
		{
			EList<SeriesDefinition> baseSDs = ChartUtil.getBaseSeriesDefinitions( context.getModel( ) );
			for ( SeriesDefinition sd : baseSDs )
			{
				EList<Query> dds = sd.getDesignTimeSeries( ).getDataDefinition( );
				Query q = dds.get( 0 );
				if ( q.getDefinition( ) == null
						|| "".equals( q.getDefinition( ).trim( ) ) ) //$NON-NLS-1$
				{
					q.setDefinition( (String) value );
				}
			}
		}
		else if ( ChartUIConstants.QUERY_OPTIONAL.equals( type ) && value instanceof String )
		{
			List<SeriesDefinition> orthSDs = ChartUtil.getAllOrthogonalSeriesDefinitions( context.getModel( ) );
			for ( SeriesDefinition sd : orthSDs )
			{
				Query q = sd.getQuery( );

				if ( q == null )
				{
					sd.setQuery( QueryImpl.create( (String) value ) );
					continue;
				}

				if ( q.getDefinition( ) == null
						|| "".equals( q.getDefinition( ).trim( ) ) ) //$NON-NLS-1$
				{
					q.setDefinition( (String) value );
				}
			}
		}
		return isUpdated;
	}

	/**
	 * Check if the cube of current item handle has multiple dimensions.
	 * 
	 * @return
	 * @since 2.3
	 */
	boolean hasMultiCubeDimensions( )
	{
		CubeHandle cube = ChartCubeUtil.getBindingCube( itemHandle );
		if ( ChartCubeUtil.getDimensionCount( cube ) > 1 )
		{
			return true;
		}

		return false;
	}

	boolean isInXTabAggrCell( )
	{
		try
		{
			AggregationCellHandle cell = ChartCubeUtil.getXtabContainerCell( itemHandle );
			if ( cell != null )
			{
				return ChartCubeUtil.isAggregationCell( cell );
			}
		}
		catch ( BirtException e )
		{
			// Silent exception
		}
		return false;
	}
	
	boolean isInheritColumnsSet( )
	{
		PropertyHandle property = itemHandle.getPropertyHandle( ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS );
		return property != null && property.isSet( );
	}
	
	boolean isInheritColumnsOnly( )
	{
		return itemHandle.getDataSet( ) == null
				&& ChartReportItemUtil.isContainerInheritable( itemHandle )
				&& context.isInheritColumnsOnly( );
	}

	boolean isInheritColumnsGroups( )
	{
		return itemHandle.getDataSet( ) == null
				&& ChartReportItemUtil.isContainerInheritable( itemHandle )
				&& !context.isInheritColumnsOnly( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#getStates()
	 */
	public int getState( )
	{
		int states = 0;
		if ( getDataSet( ) != null )
		{
			states |= HAS_DATA_SET;
		}
		if ( getDataCube( ) != null )
		{
			states |= HAS_CUBE;
		}
		if ( getInheritedDataSet( ) != null )
		{
			states |= INHERIT_DATA_SET;
		}
		if ( getInheritedCube( ) != null )
		{
			states |= INHERIT_CUBE;
		}
		if ( itemHandle.getDataBindingReference( ) != null )
		{
			states |= DATA_BINDING_REFERENCE;
		}
		if ( isInMultiView( ) )
		{
			states |= IN_MULTI_VIEWS;
		}
		if ( isSharedBinding( ) )
		{
			states |= SHARE_QUERY;
			if ( isSharingChart( false ) )
			{
				states |= SHARE_CHART_QUERY;
			}
			
			if ( isSharingChart( true ) )
			{
				states |= SHARE_CHART_QUERY_RECURSIVELY;
			}
			
			if ( ChartCubeUtil.getBindingCube( itemHandle ) != null )
			{
				states |= SHARE_CROSSTAB_QUERY;
			}
			else if ( !isSharingChart( false ) )
			{
				states |= SHARE_TABLE_QUERY;
			}
		}
		if ( isPartChart( ) )
		{
			states |= PART_CHART;
		}
		if ( hasMultiCubeDimensions( ) )
		{
			states |= MULTI_CUBE_DIMENSIONS;
		}
		if ( isInheritColumnsOnly( ) )
		{
			states |= INHERIT_COLUMNS_ONLY;
		}
		if ( isInheritColumnsGroups( ) )
		{
			states |= INHERIT_COLUMNS_GROUPS;
		}

		return states;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkState(int)
	 */
	public boolean checkState( int state )
	{
		return ( getState( ) & state ) == state;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider#checkData(String,
	 *      java.lang.Object)
	 */
	public Object checkData( String checkType, Object data )
	{
		if ( ChartUIConstants.QUERY_OPTIONAL.equals( checkType )
				|| ChartUIConstants.QUERY_CATEGORY.equals( checkType ) )
		{
			// Only check query for Cube/Crosstab sharing cases.
			if ( checkState( IDataServiceProvider.HAS_CUBE )
					|| checkState( IDataServiceProvider.SHARE_CROSSTAB_QUERY ) )
			{
				return Boolean.valueOf( ChartXTabUIUtil.checkQueryExpression( checkType,
						data,
						context.getModel( ),
						itemHandle,
						this ) );
			}
		}

		return null;
	}
	
	private ListingHandle findListingInheritance( )
	{
		DesignElementHandle container = itemHandle.getContainer( );
		while ( container != null )
		{
			if ( container instanceof ListingHandle
					&& ( (ListingHandle) container ).getDataSet( ) != null )
			{
				return (ListingHandle) container;
			}
			container = container.getContainer( );
		}
		return null;
	}
	
	/**
	 * Calculate the number of expressions binded to category series, value
	 * series or y-optional grouping .
	 * 
	 * @param expr
	 * @return count number
	 */
	int getNumberOfSameDataDefinition( String expr )
	{
		if(expr == null)
		{
			return 0;
		}
		int count = 0;
		Chart chart = context.getModel( );
		String[] expres = ChartUtil.getCategoryExpressions( chart );
		for(String s:expres)
		{
			if(expr.equals( s ))
			{
				count++;
			}
		}
		expres = ChartUtil.getValueSeriesExpressions( chart );
		for(String s:expres)
		{
			if ( expr.equals( s ) )
			{
				count++;
			}
		}
		expres = ChartUtil.getYOptoinalExpressions( chart );
		for ( String s : expres )
		{
			if ( expr.equals( s ) )
			{
				count++;
			}
		}
		return count;
	}

	/**
	 * Check if current item handle is table shared binding reference. Check if
	 * current is sharing query with other chart.
	 * 
	 * @param isRecursive
	 *            <code>true</code> means it will be recursive to find out the
	 *            shared chart case, else just check if the direct reference is
	 *            a chart item.
	 * @return
	 */
	boolean isSharingChart( boolean isRecursive )
	{
		boolean isShare = isSharedBinding( );
		if ( isRecursive )
		{
			return isShare
					&& ChartItemUtil.isChartReportItemHandle( ChartReportItemUtil.getReportItemReference( itemHandle ) );
		}
		else
		{
			return isShare
					&& ChartItemUtil.isChartReportItemHandle( itemHandle.getDataBindingReference( ) );
		}
	}
	
	/**
	 * @param target
	 * @since 2.5.1
	 */
	protected void copySeriesDefinition( Object target )
	{
		Chart targetCM = context.getModel( );
		if ( target != null && target instanceof Chart )
		{
			targetCM = (Chart) target;
		}
		ExtendedItemHandle refHandle = ChartItemUtil.getChartReferenceItemHandle( itemHandle );
		if ( refHandle != null )
		{
			ChartReportItemUtil.copyChartSeriesDefinition( ChartReportItemUtil.getChartFromHandle( refHandle ),
					targetCM );
		}
	}
	
	/**
	 * @param obj
	 * @param type
	 * @return
	 */
	public String[] getSeriesExpressionsFrom( Object obj, String type )
	{
		if ( !( obj instanceof Chart ) )
		{
			return new String[]{};
		}
		
		if( ChartUIConstants.QUERY_CATEGORY.equals( type ))
		{
			return ChartUtil.getCategoryExpressions( (Chart) obj );
		}
		else if (ChartUIConstants.QUERY_OPTIONAL.equals( type) )
		{
			return ChartUtil.getYOptoinalExpressions( (Chart) obj );
		}
		else if ( ChartUIConstants.QUERY_VALUE.equals( type ) )
		{
			return ChartUtil.getValueSeriesExpressions( (Chart) obj );
		}
		
		return new String[]{};
	}
}