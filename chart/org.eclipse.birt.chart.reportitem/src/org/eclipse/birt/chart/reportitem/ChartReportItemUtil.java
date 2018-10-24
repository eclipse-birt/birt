/*******************************************************************************
 * Copyright (c) 2007, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.olap.cursor.CubeCursor;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.IGroupedDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.render.IActionRenderer;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.api.IChartReportItem;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.api.querydefn.Binding;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter.ExpressionLocation;
import org.eclipse.birt.report.engine.api.IHTMLActionHandler;
import org.eclipse.birt.report.engine.api.IHTMLImageHandler;
import org.eclipse.birt.report.engine.api.script.IReportContext;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.engine.extension.ICubeResultSet;
import org.eclipse.birt.report.engine.extension.IQueryResultSet;
import org.eclipse.birt.report.engine.extension.IReportItemPresentation;
import org.eclipse.birt.report.engine.extension.IReportItemPresentationInfo;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExpressionListHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.util.CubeUtil;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.core.runtime.IAdapterManager;
import org.eclipse.emf.common.util.EList;

/**
 * Utility class for Chart integration as report item
 */

public class ChartReportItemUtil extends ChartItemUtil
{

	/**
	 * The field indicates it will revise chart model under reference report
	 * item case.
	 */
	public static final int REVISE_REFERENCE_REPORT_ITEM = 1;
	private static final IChartReportItemFactory baseFactory = new ChartReportItemFactoryBase( );
	private final static String DATA_BASE64 = "data:;base64,"; //$NON-NLS-1$
	
	/**
	 * Revise chart model.
	 * 
	 * @param reviseType
	 * @param cm
	 * @param itemHandle
	 */
	public static void reviseChartModel( int reviseType, Chart cm,
			ReportItemHandle itemHandle )
	{
		switch ( reviseType )
		{
			case REVISE_REFERENCE_REPORT_ITEM :
				String[] categoryExprs = ChartUtil.getCategoryExpressions( cm );
				if ( itemHandle.getDataBindingReference( ) != null
						&& isBaseGroupingDefined( cm )
						&& !( categoryExprs.length > 0 && isSharedGroupExpression( categoryExprs[0],
								itemHandle ) ) )
				{
					// In older version of chart, it is allowed to set grouping
					// on category series when sharing report item, but now it
					// isn't allowed, so this calls will revise chart model to
					// remove category series grouping flag for the case.
					SeriesDefinition baseSD = null;
					if ( cm instanceof ChartWithAxes )
					{
						ChartWithAxes cwa = (ChartWithAxes) cm;
						baseSD = cwa.getBaseAxes( )[0].getSeriesDefinitions( )
								.get( 0 );
					}
					else if ( cm instanceof ChartWithoutAxes )
					{
						ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
						baseSD = cwoa.getSeriesDefinitions( ).get( 0 );
					}
					if ( baseSD != null && baseSD.getGrouping( ) != null )
					{
						baseSD.getGrouping( ).unsetEnabled( );
					}
				}
				break;
		}
	}

	/**
	 * Checks if chart should use internal grouping or DTE grouping.
	 * 
	 * @param chartHandle
	 *            handle with version
	 * @return true means old report using internal grouping
	 * @since 2.3.1
	 */
	public static boolean isOldChartUsingInternalGroup(
			ReportItemHandle chartHandle, Chart cm )
	{
		String reportVer = chartHandle.getModuleHandle( ).getVersion( );
		if ( reportVer == null
				|| ChartUtil.compareVersion( reportVer, "3.2.16" ) < 0 ) //$NON-NLS-1$
		{
			return true;
		}

		// Since if the chart is serialized into a document, the version number
		// will always be
		// the newest, so we can only detect an old chart using internal group
		// with following facts:
		// 1. the chart has an grouping on base seriesDefination
		// 2. shared binding is used.
		// 3. whether the chart is sharing data with a table or list
		// 4. the shared binding is not grouped.
		if ( chartHandle.getDataBindingReference( ) != null
				&& isBaseGroupingDefined( cm )
				&& isSharingTableData( chartHandle )
				&& !isSharedGroupExpression( ChartUtil.getCategoryExpressions( cm )[0],
						chartHandle ) )
		{
			return true;
		}
		return false;
	}

	/**
	 * Check whether the reportitem is sharing data with a table or list.
	 * 
	 * @param handle
	 * @return
	 */
	private static boolean isSharingTableData( ReportItemHandle handle )
	{
		return getReportItemReference( handle ) instanceof ListingHandle;
	}

	/**
	 * Check if specified expression is a grouping expression of shared report
	 * item.
	 * 
	 * @param expression
	 * @param handle
	 * @return group expression or not
	 */
	@SuppressWarnings("unchecked")
	private static boolean isSharedGroupExpression( String expression,
			ReportItemHandle handle )
	{
		ReportItemHandle itemHandle = getReportItemReference( handle );
		if ( itemHandle instanceof ListingHandle )
		{
			List<GroupHandle> groupList = new ArrayList<GroupHandle>( );
			SlotHandle groups = ( (ListingHandle) itemHandle ).getGroups( );
			for ( Iterator<GroupHandle> iter = groups.iterator( ); iter.hasNext( ); )
			{
				groupList.add( iter.next( ) );
			}

			if ( groupList.size( ) == 0 )
			{
				return false;
			}

			ExpressionCodec exprCodec = ChartModelHelper.instance( )
					.createExpressionCodec( );

			for ( GroupHandle gh : groupList )
			{
				loadExpression( exprCodec, gh );
				if ( expression.contains( exprCodec.getBindingName( ) ) )
				{
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if result set is empty
	 * 
	 * @param set
	 *            result set
	 * @throws BirtException
	 * @since 2.3
	 */
	public static boolean isEmpty( IBaseResultSet set ) throws BirtException
	{
		if ( set instanceof IQueryResultSet )
		{
			return ( (IQueryResultSet) set ).isEmpty( );
		}  else if ( set instanceof ICubeResultSet )
		{
			  CubeCursor cursor = (( (ICubeResultSet) set ).getCubeCursor());
			  return cursor.getOrdinateEdge().size() != 0;
		}

		return false;
	}

	public static <T> T getAdapter( Object adaptable, Class<T> adapterClass )
	{
		IAdapterManager adapterManager = org.eclipse.birt.core.framework.Platform.getAdapterManager( );
		return adapterClass.cast( adapterManager.loadAdapter( adaptable,
				adapterClass.getName( ) ) );
	}

	public static IReportItemPresentation instanceReportItemPresentation(
			ExtendedItemHandle handle, IReportItemPresentationInfo info )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createReportItemPresentation( info );
	}

	public static IActionRenderer instanceActionRenderer(
			ExtendedItemHandle handle, IHTMLActionHandler handler,
			IDataRowExpressionEvaluator evaluator, IReportContext context )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createActionRenderer( handle,
				handler,
				evaluator,
				context );
	}

	public static Serializer instanceSerializer( ExtendedItemHandle handle )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createSerializer( handle );
	}

	public static ChartCubeQueryHelper instanceCubeQueryHelper(
			ExtendedItemHandle handle, Chart cm, IModelAdapter modelAdapter )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createCubeQueryHelper( handle, cm, modelAdapter );
	}
	
	public static ChartBaseQueryHelper  instanceQueryHelper(
			ExtendedItemHandle handle, Chart cm, IModelAdapter modelAdapter )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createQueryHelper( handle, cm, modelAdapter );
	}

	public static IGroupedDataRowExpressionEvaluator instanceCubeEvaluator(
			ExtendedItemHandle handle, Chart cm, ICubeResultSet set )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createCubeEvaluator( cm, set );
	}

	public static IChartReportItem instanceChartReportItem( ExtendedItemHandle handle )
	{
		IChartReportItemFactory factory = getAdapter( handle,
				IChartReportItemFactory.class );
		if ( factory == null )
		{
			factory = baseFactory;
		}
		return factory.createChartReportItem( handle );
	}
	
	/**
	 * Checks if shared scale is needed when computation
	 * 
	 * @param eih
	 *            handle
	 * @param cm
	 *            chart model
	 * @return shared binding needed or not
	 * @since 2.3
	 */
	public static boolean canScaleShared( ReportItemHandle eih, Chart cm )
	{
		return cm instanceof ChartWithAxes
				&& eih.getDataSet( ) == null
				&& getBindingHolder( eih ) != null
				&& ChartCubeUtil.isInXTabMeasureCell( eih );
	}

	/**
	 * In some cases, if the expression in subquery is a simple binding, and
	 * this binding is from parent query, should copy the binding from parent
	 * and insert into subquery.
	 * 
	 * @param query
	 *            subquery
	 * @param expr
	 *            expression
	 * @throws DataException
	 * @since 2.3.1 and 2.4.0
	 */
	@SuppressWarnings({
			"deprecation", "unchecked"
	})
	public static void copyAndInsertBindingFromContainer(
			ISubqueryDefinition query, String expr ) throws DataException
	{
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		String bindingName = exprCodec.getRowBindingName( expr, false );
		if ( bindingName != null
				&& !query.getBindings( ).containsKey( bindingName )
				&& query.getParentQuery( )
						.getBindings( )
						.containsKey( bindingName ) )
		{
			// Copy the binding from container and insert it into
			// subquery
			IBinding parentBinding = (IBinding) query.getParentQuery( )
					.getBindings( )
					.get( bindingName );
			IBinding binding = new Binding( bindingName,
					parentBinding.getExpression( ) );
			binding.setAggrFunction( parentBinding.getAggrFunction( ) );
			binding.setDataType( parentBinding.getDataType( ) );
			binding.setDisplayName( parentBinding.getDisplayName( ) );
			binding.setFilter( parentBinding.getFilter( ) );
			// Copy aggregations
			List<String> aggregationOnList = parentBinding.getAggregatOns( );
			if ( aggregationOnList != null )
			{
				for ( String aggregateOn : aggregationOnList )
				{
					binding.addAggregateOn( aggregateOn );
					// Copy groups if used and not added previously
					if ( findGroupInQuery( query, aggregateOn ) == null )
					{
						GroupDefinition group = findGroupInQuery( query.getParentQuery( ),
								aggregateOn );
						if ( group != null )
						{
							( (SubqueryDefinition) query ).addGroup( group );
						}
					}
				}
			}
			// Copy arguments
			List<?> argumentsList = parentBinding.getArguments( );
			if ( argumentsList != null )
			{
				for ( Object argument : argumentsList )
				{
					binding.addArgument( (IBaseExpression) argument );
				}
			}
			// Exportable is true for new subquery bindings
			query.addBinding( binding );
		}
	}
	
	@SuppressWarnings("unchecked")
	private static GroupDefinition findGroupInQuery(
			IBaseQueryDefinition query, String groupName )
	{
		List<GroupDefinition> groups = query.getGroups( );
		if ( groups != null )
		{
			for ( GroupDefinition group : groups )
			{
				if ( group.getName( ).equals( groupName ) )
				{
					return group;
				}
			}
		}
		return null;
	}

	/**
	 * Copy chart's sample data.
	 * 
	 * @param srcCM
	 * @param targetCM
	 * @since 2.6.2
	 */
	public static void copyChartSampleData( Chart srcCM, Chart targetCM )
	{
		if ( srcCM.getSampleData( ) != null )
		{
			targetCM.setSampleData( srcCM.getSampleData( ).copyInstance( ) );
		}
		else
		{
			targetCM.setSampleData( null );
		}
	}
	
	/**
	 * Copy series definition from one chart model to another.
	 * 
	 * @param srcCM
	 * @param targetCM
	 * @since 2.5
	 */
	public static void copyChartSeriesDefinition( Chart srcCM, Chart targetCM )
	{
		boolean isSameType = srcCM.getType( ).equals( targetCM.getType( ) );
		// Copy category series definitions.
		EList<SeriesDefinition> srcRsds = ChartUtil.getBaseSeriesDefinitions( srcCM );
		EList<SeriesDefinition> tagRsds = ChartUtil.getBaseSeriesDefinitions( targetCM );
		for ( int i = 0; i < srcRsds.size( ); i++ )
		{
			SeriesDefinition sd = srcRsds.get( i );
			SeriesDefinition tagSD = null;
			if ( i >= tagRsds.size( ) )
			{
				tagSD = SeriesDefinitionImpl.create( );
				// Add to target chart model.
				if ( targetCM instanceof ChartWithAxes )
				{
					( (ChartWithAxes) targetCM ).getAxes( )
							.get( 0 )
							.getSeriesDefinitions( )
							.add( tagSD );
				}
				else if ( targetCM instanceof ChartWithoutAxes )
				{
					( (ChartWithoutAxes) targetCM ).getSeriesDefinitions( )
							.add( tagSD );
				}
			}
			else
			{
				tagSD = tagRsds.get( i );
			}

			copySDQueryAttributes( sd, tagSD );
			
			if ( srcCM instanceof ChartWithAxes
					&& targetCM instanceof ChartWithAxes )
			{
				// Also update axis type
				if ( ( (ChartWithAxes) srcCM ).getAxes( ).get( 0 ).isSetType( ) )
				{
					( (ChartWithAxes) targetCM ).getAxes( )
							.get( 0 )
							.setType( ( (ChartWithAxes) srcCM ).getAxes( )
									.get( 0 )
									.getType( ) );
				}
			}
		}

		// Copy Y series definitions.
		if ( targetCM instanceof ChartWithAxes )
		{
			EList<Axis> tagAxisList = ( (ChartWithAxes) targetCM ).getAxes( )
					.get( 0 )
					.getAssociatedAxes( );

			if ( srcCM instanceof ChartWithAxes )
			{
				EList<Axis> srcAxisList = ( (ChartWithAxes) srcCM ).getAxes( )
						.get( 0 )
						.getAssociatedAxes( );

				if ( tagAxisList.size( ) > srcAxisList.size( ) )
				{
					for ( int i = ( tagAxisList.size( ) - 1 ); i >= srcAxisList.size( ); i-- )
					{
						tagAxisList.remove( i );
					}
				}

				if ( isSameType )
				{
					// If source chart type is equal with target chart type,
					// copy additional axes from source into target.

					for ( int i = 0; i < srcAxisList.size( ); i++ )
					{
						if ( i >= tagAxisList.size( ) )
						{
							// src size > target size, copy pending axis from
							// source to target.
							tagAxisList.add( srcAxisList.get( i )
									.copyInstance( ) );
						}

						srcRsds = srcAxisList.get( i ).getSeriesDefinitions( );
						tagRsds = tagAxisList.get( i ).getSeriesDefinitions( );
						copySDListQueryAttributes( srcRsds, tagRsds, isSameType );
						
						// Also update axis type
						if ( srcAxisList.get( i ).isSetType( ) )
						{
							tagAxisList.get( i ).setType( srcAxisList.get( i ).getType( ) );
						}
					}
				}
				else
				{
					int minsize = srcAxisList.size( ) > tagAxisList.size( ) ? tagAxisList.size( )
							: srcAxisList.size( );
					for ( int i = 0; i < minsize; i++ )
					{
						srcRsds = srcAxisList.get( i ).getSeriesDefinitions( );
						tagRsds = tagAxisList.get( i ).getSeriesDefinitions( );

						copySDListQueryAttributes( srcRsds, tagRsds, isSameType );
						
						// Also update axis type
						if ( srcAxisList.get( i ).isSetType( ) )
						{
							tagAxisList.get( i ).setType( srcAxisList.get( i ).getType( ) );
						}
					}
				}
			}
			else
			{
				srcRsds = ( (ChartWithoutAxes) srcCM ).getSeriesDefinitions( )
						.get( 0 )
						.getSeriesDefinitions( );
				if ( tagAxisList.size( ) > 1 )
				{
					for ( int i = 1; i < tagAxisList.size( ); i++ )
					{
						tagAxisList.remove( i );
					}
				}
				tagRsds = tagAxisList.get( 0 ).getSeriesDefinitions( );

				copySDListQueryAttributes( srcRsds, tagRsds, isSameType );
			}
		}
		else
		{
			tagRsds = ( (ChartWithoutAxes) targetCM ).getSeriesDefinitions( )
					.get( 0 )
					.getSeriesDefinitions( );
			if ( srcCM instanceof ChartWithAxes )
			{
				srcRsds = ( (ChartWithAxes) srcCM ).getAxes( )
						.get( 0 )
						.getAssociatedAxes( )
						.get( 0 )
						.getSeriesDefinitions( );
			}
			else
			{
				srcRsds = ( (ChartWithoutAxes) srcCM ).getSeriesDefinitions( )
						.get( 0 )
						.getSeriesDefinitions( );
			}

			copySDListQueryAttributes( srcRsds, tagRsds, isSameType );
		}
	}

	/**
	 * @param srcRsds
	 * @param tagRsds
	 */
	private static void copySDListQueryAttributes(
			EList<SeriesDefinition> srcRsds, EList<SeriesDefinition> tagRsds,
			boolean sameChartType )
	{
		if ( tagRsds.size( ) > srcRsds.size( ) )
		{
			for ( int i = ( tagRsds.size( ) - 1 ); i >= srcRsds.size( ); i-- )
			{
				tagRsds.remove( i );
			}
		}

		if ( sameChartType )
		{
			for ( int i = 0; i < srcRsds.size( ); i++ )
			{
				if ( i >= tagRsds.size( ) )
				{
					// Copy
					tagRsds.add( srcRsds.get( i ).copyInstance( ) );
				}

				SeriesDefinition sd = srcRsds.get( i );
				SeriesDefinition tagSD = tagRsds.get( i );
				copySDQueryAttributes( sd, tagSD );
			}
		}
		else
		{
			int minSDsize = srcRsds.size( ) > tagRsds.size( ) ? tagRsds.size( )
					: srcRsds.size( );
			for ( int i = 0; i < minSDsize; i++ )
			{
				SeriesDefinition sd = srcRsds.get( i );
				SeriesDefinition tagSD = tagRsds.get( i );
				copySDQueryAttributes( sd, tagSD );

			}
		}
	}

	/**
	 * @param sd
	 * @param tagSD
	 */
	private static void copySDQueryAttributes( SeriesDefinition sd,
			SeriesDefinition tagSD )
	{
		if ( sd.getQuery( ) != null )
		{
			tagSD.setQuery( sd.getQuery( ).copyInstance( ) );
		}
		else
		{
			tagSD.setQuery( null );
		}
		if ( sd.getGrouping( ) != null )
		{
			tagSD.setGrouping( sd.getGrouping( ).copyInstance( ) );
		}
		else
		{
			tagSD.setGrouping( null );
		}
		if ( sd.isSetSorting( ) )
		{
			tagSD.setSorting( sd.getSorting( ) );
		}

		if ( sd.getSortKey( ) != null )
		{
			tagSD.setSortKey( sd.getSortKey( ).copyInstance( ) );
		}
		else
		{
			tagSD.setSortKey( null );
		}
		if ( sd.isSetZOrder( ) )
		{
			tagSD.setZOrder( sd.getZOrder( ) );
		}
		int tagSize = tagSD.getSeries( ).size( );
		int srcSize = sd.getSeries( ).size( );
		if ( tagSize > srcSize )
		{
			for ( int i = ( tagSize - 1 ); i >= srcSize; i-- )
				tagSD.getSeries( ).remove( i );
		}

		// Copy data definitions.
		int i = 0;
		for ( ; i < srcSize; i++ )
		{
			if ( i >= tagSize )
			{
				// New a series and copy data definitions.
				Series tagSeries = tagSD.getSeries( ).get( 0 ).copyInstance( );
				tagSD.getSeries( ).add( tagSeries );

				Series srcSeries = sd.getSeries( ).get( i );
				tagSeries.getDataDefinition( ).clear( );
				for ( Query q : srcSeries.getDataDefinition( ) )
					tagSeries.getDataDefinition( ).add( q.copyInstance( ) );
			}
			else
			{
				// Copy data definitions.
				Series tagSeries = tagSD.getSeries( ).get( i );
				Series srcSeries = sd.getSeries( ).get( i );
				tagSeries.getDataDefinition( ).clear( );
				for ( Query q : srcSeries.getDataDefinition( ) )
					tagSeries.getDataDefinition( ).add( q.copyInstance( ) );
			}
		}
	}

	public static ScriptExpression newExpression( IModelAdapter adapter,
			ComputedColumnHandle binding )
	{
		ExpressionHandle eh = binding.getExpressionProperty( ComputedColumn.EXPRESSION_MEMBER );
		if ( eh == null || eh.getValue( ) == null )
		{
			return null;
		}
		return adapter.adaptExpression( (Expression) eh.getValue( ) );
	}
	
	public static ScriptExpression newExpression( IModelAdapter adapter,
			AggregationArgumentHandle binding )
	{
		ExpressionHandle eh = binding.getExpressionProperty( AggregationArgument.VALUE_MEMBER );
		if ( eh == null || eh.getValue( ) == null )
		{
			return null;
		}
		return adapter.adaptExpression( (Expression) eh.getValue( ) );
	}
	
	public static List<ScriptExpression> newExpression( IModelAdapter adapter,
			ParamBindingHandle binding )
	{
		ExpressionListHandle eh = binding.getExpressionListHandle( );
		List<Expression> exprs = eh.getListValue( );
		if ( exprs == null )
		{
			return Collections.emptyList( );
		}
		List<ScriptExpression> ses = new ArrayList<ScriptExpression>( exprs.size( ) );
		for ( Expression expr : exprs )
		{
			ses.add( adapter.adaptExpression( expr ) );
		}
		return ses;
	}
	
	public static ScriptExpression newExpression( IModelAdapter adapter,
			ExpressionCodec exprCodec, String expr )
	{
		exprCodec.decode( expr );
		return adapter.adaptExpression( new Expression( exprCodec.getExpression( ),
				exprCodec.getType( ) ) );
	}
	
	public static IScriptExpression adaptExpression( ExpressionCodec exprCodec,
			IModelAdapter modelAdapter, boolean bCube )
	{
		return modelAdapter.adaptExpression( new Expression( exprCodec.getExpression( ),
				exprCodec.getType( ) ),
				bCube ? ExpressionLocation.CUBE : ExpressionLocation.TABLE );
	}

	public static Expression getExpression( GroupHandle gh )
	{
		ExpressionHandle eh = gh.getExpressionProperty( IGroupElementModel.KEY_EXPR_PROP );
		if ( eh != null && eh.getValue( ) != null )
		{
			return (Expression) eh.getValue( );
		}
		return null;
	}
	
	public static boolean isKeepCubeHierarchyAndNotCubeTopLevelOnCategory(
			Chart model, CubeHandle cubeHandle, ReportItemHandle itemHandle )
	{
		if ( model == null )
		{
			return false;
		}
		boolean isKeepCubeHierarchy = isKeepCubeHierarchyOnCategory( model );

		if ( !isKeepCubeHierarchy )
		{
			return false;
		}

		if ( cubeHandle == null )
		{
			return false;
		}

		SeriesDefinition sd = ChartUtil.getBaseSeriesDefinitions( model )
				.get( 0 );

		Query query = ChartUtil.getDataQuery( sd, 0 );

		return !isCubeTopLevel( query, itemHandle );
	}

	public static boolean isKeepCubeHierarchyAndNotCubeTopLevelOnSeries(
			Chart model, CubeHandle cubeHandle, ReportItemHandle itemHandle )
	{
		if ( model == null )
		{
			return false;
		}
		
		boolean isKeepCubeHierarchy = isKeepCubeHierarchyOnSeries( model );

		if ( !isKeepCubeHierarchy )
		{
			return false;
		}

		if ( cubeHandle == null )
		{
			return false;
		}

		Query query = ChartUtil.getAllOrthogonalSeriesDefinitions( model )
				.get( 0 )
				.getQuery( );

		return !isCubeTopLevel( query, itemHandle );
	}
	
	public static ExpressionCodec getDimensionExpresion( String definition,
			ReportItemHandle itemHandle )
	{
		ExpressionCodec exprc = null;
		if ( itemHandle instanceof ExtendedItemHandle )
		{
			exprc = ChartReportItemHelper.instance( )
					.createExpressionCodec( (ExtendedItemHandle) itemHandle );
		}
		else
		{
			exprc = ChartModelHelper.instance( ).createExpressionCodec( );
		}
		String bindName = exprc.getBindingName( definition );
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle binding = iterator.next( );
			if ( binding.getName( ).equals( bindName ) )
			{
				ChartItemUtil.loadExpression( exprc, binding );
				break;
			}
		}

		if ( exprc.isDimensionExpresion( ) )
		{
			return exprc;
		}
		else
		{
			return null;
		}				
	}
	
	private static boolean isCubeTopLevel( Query query ,ReportItemHandle itemHandle)
	{
		ExpressionCodec exprc = ChartModelHelper.instance( )
				.createExpressionCodec( );
		String bindName = exprc.getBindingName( query.getDefinition( ) );
		Iterator<ComputedColumnHandle> iterator = ChartReportItemUtil.getColumnDataBindings( itemHandle );
		while ( iterator.hasNext( ) )
		{
			ComputedColumnHandle binding = iterator.next( );
			if ( binding.getName( ).equals( bindName ) )
			{
				ChartItemUtil.loadExpression( exprc, binding );
				break;
			}
		}

		if ( !exprc.isDimensionExpresion( ) )
		{
			return false;
		}

		CubeHandle cube =  ChartReportItemHelper.instance( )
				.getBindingCubeHandle( itemHandle );

		// Add row/column edge
		String[] levels = exprc.getLevelNames( );
		String dimensionName = levels[0];
		DimensionHandle dimHandle = cube.getDimension( dimensionName );
		if ( dimHandle == null )
		{
			return false;
		}
		HierarchyHandle hieHandle = dimHandle.getDefaultHierarchy( );

		// If not multi-dimension return true;If multi-dimension,
		// return true if is parent top level
		if ( hieHandle.getLevelCount( ) != 0
				&& ( hieHandle.getLevelCount( ) == 1 || hieHandle.getLevel( 0 )
						.getName( )
						.equals( levels[1] ) ) )
		{
			return true;
		}
		return false;
	}

	public static boolean validateCubeResultSetBinding(
			ReportItemHandle itemHandle, Chart cm )
			throws ExtendedElementException
	{
		ReportItemHandle handle = null;
		if ( ChartItemUtil.isReportItemReference( itemHandle )
				|| ChartItemUtil.isInMultiViews( itemHandle ) )
		{
			handle = ChartItemUtil.getReportItemReference( itemHandle );
		}
		if ( handle == null )
		{
			handle = ChartItemUtil.getInheritedHandle( itemHandle );
		}

		boolean isCrosstab = false;
		if ( handle instanceof ExtendedItemHandle )
		{
			isCrosstab = "Crosstab".equals( ( (ExtendedItemHandle) handle ).getExtensionName( ) ); //$NON-NLS-1$
		}
		else
		{
			DesignElementHandle container = itemHandle.getContainer( );
			while ( container != null )
			{
				if ( container instanceof ExtendedItemHandle
						&& "Crosstab".equals( ( (ExtendedItemHandle) container ).getExtensionName( ) ) ) //$NON-NLS-1$
				{
					handle = (ReportItemHandle) container;
					isCrosstab = true;
					break;
				}
				container = container.getContainer( );
			}
		}
		if ( !isCrosstab )
		{
			return true; // not cross tab, ignore.
		}

		CrosstabReportItemHandle crossTab = (CrosstabReportItemHandle) ( (ExtendedItemHandle) handle ).getReportItem( );
		ExpressionCodec exprCodec = ChartModelHelper.instance( )
				.createExpressionCodec( );
		List<String> rowAxisDimensions = new ArrayList<String>( );
		List<String> colAxisDimensions = new ArrayList<String>( );
		/* get cross tab row and column dimensions */
		if ( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ) != null )
		{
			rowAxisDimensions = ChartReportItemHelper.instance( )
					.getLevelBindingNamesOfCrosstab( crossTab.getCrosstabView( ICrosstabConstants.ROW_AXIS_TYPE ),
							itemHandle );
		}
		if ( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ) != null )
		{
			colAxisDimensions = ChartReportItemHelper.instance( )
					.getLevelBindingNamesOfCrosstab( crossTab.getCrosstabView( ICrosstabConstants.COLUMN_AXIS_TYPE ),
							itemHandle );
		}

		// get all bindings
		Map<String, ComputedColumnHandle> bindingMap = new LinkedHashMap<String, ComputedColumnHandle>( );
		for ( Iterator<ComputedColumnHandle> bindings = ChartReportItemUtil.getAllColumnBindingsIterator( itemHandle ); bindings.hasNext( ); )
		{
			ComputedColumnHandle column = bindings.next( );
			bindingMap.put( column.getName( ), column );
		}

		for ( SeriesDefinition seriesDefinition : ChartUtil.getAllOrthogonalSeriesDefinitions( cm ) )
		{
			Series series = seriesDefinition.getDesignTimeSeries( );
			List<Query> queries = series.getDataDefinition( );
			for ( Query query : queries )
			{
				String bindingName = exprCodec.getBindingName( query.getDefinition( ) );
				ComputedColumnHandle computedBinding = bindingMap.get( bindingName );
				if ( computedBinding == null )
				{
					return false; // if orthogonal series binding does not
									// exist, it must be removed in cross-tab
				}
				@SuppressWarnings("unchecked")
				List<String> aggOnList = computedBinding.getAggregateOnList( );
				String[] categoryExpressions = ChartUtil.getCategoryExpressions( cm );
				String[] yOptionalExpressions = ChartUtil.getYOptoinalExpressions( cm );
				boolean isInXTab = ChartCubeUtil.isInXTabMeasureCell( itemHandle );
				if ( aggOnList.size( ) > 0 )
				{
					// check first aggregation on category and optionalY.
					if ( !searchAggregation( aggOnList.get( 0 ),
							bindingMap,
							rowAxisDimensions,
							colAxisDimensions,
							categoryExpressions,
							yOptionalExpressions,
							itemHandle ) )
					{
						return false;
					}
				}
				if ( !isInXTab && aggOnList.size( ) > 1 ) // XTab has category
															// only.
				{
					if ( !searchAggregation( aggOnList.get( 1 ),
							bindingMap,
							rowAxisDimensions,
							colAxisDimensions,
							categoryExpressions,
							yOptionalExpressions,
							itemHandle ) )
					{
						return false;
					}
				}
			}
		}

		return true;
	}

	private static boolean searchAggregation( String aggOn,
			Map<String, ComputedColumnHandle> bindingMap,
			List<String> rowAxisDimensions,
			List<String> colAxisDimensions, String[] catecoryBindNames,
			String[] yOptionalBindNames, ReportItemHandle itemHandle )
	{
		ExpressionCodec exprCodec = null;
		if ( itemHandle instanceof ExtendedItemHandle )
		{
			exprCodec = ChartReportItemHelper.instance( )
					.createExpressionCodec( (ExtendedItemHandle) itemHandle );
		}
		else
		{
			exprCodec = ChartModelHelper.instance( ).createExpressionCodec( );
		}
		String[] levelNames = CubeUtil.splitLevelName( aggOn );
		ComputedColumnHandle cch = ChartReportItemHelper.instance( )
				.findDimensionBinding( exprCodec,
						levelNames[0],
						levelNames[1],
						bindingMap.values( ),
						itemHandle );
		if ( cch == null || cch.getName( ) == null )
		{
			return false; // dimension binding not found
		}

		/* see aggregation is on row or column dimension */
		List<String> dimensions = null;
		if ( rowAxisDimensions.contains( cch.getName( ) ) )
		{
			dimensions = rowAxisDimensions;
		}
		else if ( colAxisDimensions.contains( cch.getName( ) ) )
		{
			dimensions = colAxisDimensions;
		}

		if ( dimensions != null )
		{
			for ( String field : catecoryBindNames )
			{
				if ( dimensions.contains( exprCodec.getBindingName( field ) ) )
				{
					return true;
				}
			}

			for ( String field : yOptionalBindNames )
			{
				if ( dimensions.contains( exprCodec.getBindingName( field ) ) )
				{
					return true;
				}
			}
		}

		return false;
	}
	
	/**
	 * get absolute  url
	 * @param image
	 * @param handle
	 * @param reportContext
	 * @return url
	 */
	public static String getImageAbsoluteURL( Image image,
			ExtendedItemHandle handle, IReportContext reportContext )
	{
		if ( reportContext == null )
		{
			return getImageAbsoluteURL( image, handle );
		}
		else
		{
			return getImageAbsoluteURL( image, reportContext );
		}
	}
	
	/**
	 * get absolute local url
	 * @param image
	 * @param handle
	 * @return local url
	 */
	public static String getImageAbsoluteURL( Image image,
			ExtendedItemHandle handle )
	{
		if ( handle == null )
		{
			return null;
		}
		
		switch ( image.getSource( ) )
		{
			case STATIC :
				// url case
				return image.getURL( );
			case FILE :
				// resource case
				URL url = handle.getModuleHandle( )
						.findResource( image.getURL( ), IResourceLocator.IMAGE );
				if ( url != null )
				{
					return url.toExternalForm( );
				}
				return null;
			case REPORT :
				// embedded case
				EmbeddedImage embeddedImage = handle.getModuleHandle( )
						.findImage( image.getURL( ) );
				if ( embeddedImage != null )
				{
					return DATA_BASE64
							+ new String( Base64.encodeBase64( embeddedImage.getData( handle.getModuleHandle( )
									.getModule( ) ) ) );
				}
				return null;
			default :
				return null;
		}
	}
	
	/**
	 * get absolute server url
	 * @param image
	 * @param reportContext
	 * @return web url
	 */
	public static String getImageAbsoluteURL( Image image,
			IReportContext reportContext )
	{
		
		if ( image == null
				|| reportContext == null
				|| reportContext.getRenderOption( ) == null
				|| reportContext.getDesignHandle( ) == null
				|| image.getURL( ) == null )
		{
			return null;
		}
		
		IHTMLImageHandler imageHandler = reportContext.getRenderOption( )
				.getImageHandler( );
		String filePath = image.getURL( );
		ReportDesignHandle reportDesign = reportContext.getDesignHandle( );
		String uri = null;
		switch ( image.getSource( ) )
		{
			case STATIC :
				// url case
				return image.getURL( );
			case FILE :
				// resource case
				filePath = image.getURL( );
				if ( filePath == null )
				{
					return null;
				}
				URL url = reportDesign.findResource( filePath,
						IResourceLocator.IMAGE );
				if ( url != null )
				{
					filePath = url.toExternalForm( );
				}

				uri = imageHandler.onCustomImage( new org.eclipse.birt.report.engine.api.impl.Image( filePath ),
						reportContext );

				if ( uri != null )
				{
					image.setURL( uri );
				}

				return uri;
			case REPORT :
				// embedded case
				EmbeddedImage embeddedImage = reportDesign.getModuleHandle( )
						.findImage( filePath );

				if ( embeddedImage != null )
				{
					org.eclipse.birt.report.engine.api.impl.Image imageParam = new org.eclipse.birt.report.engine.api.impl.Image( embeddedImage.getData( reportDesign.getModule( ) ),
							filePath );

					uri = imageHandler.onCustomImage( imageParam, reportContext );

					if ( uri != null )
					{
						image.setURL( uri );
					}

					return uri;
				}
				return null;
			default :
				return null;

		}	
	}
	
	/**
	 * Returns if reference binding can accept this type of extended item
	 * 
	 * @param extensionName
	 *            extension name of item
	 * @return true means reference binding can accept this type of extended
	 *         item
	 * @since 3.7
	 */
	public static boolean isAvailableExtensionToReferenceDataBinding(
			String extensionName )
	{
		return ChartReportItemConstants.CHART_EXTENSION_NAME.equals( extensionName )
				|| ICrosstabConstants.CROSSTAB_EXTENSION_NAME.equals( extensionName );
	}

}
