/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.data.adapter.api.DataAdapterUtil;
import org.eclipse.birt.report.data.adapter.api.ICubeQueryUtil;
import org.eclipse.birt.report.designer.internal.ui.expressions.IExpressionConverter;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LevelAttributeHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.HierarchyHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.api.olap.TabularLevelHandle;
import org.eclipse.birt.report.model.elements.interfaces.ICubeModel;

/**
 * Utility class for XTab integration in UI
 */

public class ChartXTabUIUtil extends ChartCubeUtil
{

	/**
	 * Check if the expressions of category and Y optional have same dimension.
	 * 
	 * @param checkType
	 * @param data
	 * @param cm
	 * @param itemHandle
	 * @param provider
	 * @return <code>true</code> means the data check is past.
	 * @since 2.3
	 */
	public static boolean checkQueryExpression( String checkType, Object data,
			Chart cm, ExtendedItemHandle itemHandle,
			ReportDataServiceProvider provider )
	{
		Map<String, Query[]> queryDefinitionsMap = QueryUIHelper.getQueryDefinitionsMap( cm );
		return checkQueryExpression( checkType,
				data,
				queryDefinitionsMap,
				itemHandle,
				provider );
	}

	public static boolean isTransposedChartWithAxes( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return ( (ChartWithAxes) cm ).isTransposed( );
		}
		throw new IllegalArgumentException( Messages.getString( "Error.ChartShouldIncludeAxes" ) ); //$NON-NLS-1$
	}

	/**
	 * Check if the expressions of category and Y optional have same dimension.
	 * 
	 * @param checkType
	 * @param data
	 * @param queryDefinitionsMap
	 * @param itemHandle
	 * @param provider
	 * 
	 * @since 2.5.1
	 */
	@SuppressWarnings("unchecked")
	public static boolean checkQueryExpression( String checkType, Object data,
			Map<String, Query[]> queryDefinitionsMap,
			ExtendedItemHandle itemHandle, ReportDataServiceProvider provider )
	{
		if ( data == null || "".equals( data ) ) //$NON-NLS-1$
		{
			return true;
		}
		
		ExpressionCodec exprCodec = ChartReportItemHelper.instance( )
				.createExpressionCodec( itemHandle );

		String categoryDimension = null;
		String yOptionDimension = null;
		String categoryBindName = null;
		String yOptionBindName = null;

		String expression = (String) data;

		// Compare if dimensions between category expression and Y optional
		// expression are same.
		Iterator<ComputedColumnHandle> columnBindings = null;
		if ( ChartReportItemHelper.instance( )
				.getBindingCubeHandle( itemHandle ) != null
				&& provider.isInheritanceOnly( )
				|| provider.isSharedBinding( ) )
		{
			ReportItemHandle reportItemHandle = provider.getReportItemHandle( );
			columnBindings = reportItemHandle.getColumnBindings( ).iterator( );
		}
		else if ( ChartReportItemHelper.instance( )
				.getBindingCubeHandle( itemHandle ) != null
				|| ( provider.isInXTabMeasureCell( ) && !provider.isPartChart( ) ) ) //
		{
			columnBindings = getAllColumnBindingsIterator( itemHandle );
		}

		if ( ChartUIConstants.QUERY_OPTIONAL.equals( checkType ) )
		{
			String categoryExpr = null;
			Query[] querys = queryDefinitionsMap.get( ChartUIConstants.QUERY_CATEGORY );
			if ( querys != null && querys.length > 0 )
			{
				categoryExpr = querys[0].getDefinition( );
			}
			if ( categoryExpr == null || "".equals( categoryExpr ) ) //$NON-NLS-1$
			{
				return true;
			}

			categoryBindName = exprCodec.getCubeBindingName( categoryExpr, true );
			yOptionBindName = exprCodec.getCubeBindingName( expression, true );
		}
		else if ( ChartUIConstants.QUERY_CATEGORY.equals( checkType ) )
		{
			String yOptionExpr = null;
			Query[] querys = queryDefinitionsMap.get( ChartUIConstants.QUERY_OPTIONAL );
			if ( querys != null && querys.length > 0 )
			{
				yOptionExpr = querys[0].getDefinition( );
			}
			if ( yOptionExpr == null || "".equals( yOptionExpr ) ) //$NON-NLS-1$
			{
				return true;
			}

			categoryBindName = exprCodec.getCubeBindingName( expression, true );
			yOptionBindName = exprCodec.getCubeBindingName( yOptionExpr, true );
		}

		if ( columnBindings == null )
		{
			return true;
		}

		while ( columnBindings.hasNext( ) )
		{
			ComputedColumnHandle columnHandle = columnBindings.next( );
			ChartReportItemHelper.instance( ).loadExpression( exprCodec, columnHandle );
			String bindName = columnHandle.getName( );
			if ( !exprCodec.isDimensionExpresion( ) )
			{
				continue;
			}

			if ( bindName.equals( categoryBindName ) )
			{
				categoryDimension = exprCodec.getLevelNames( )[0];
			}

			if ( bindName.equals( yOptionBindName ) )
			{
				yOptionDimension = exprCodec.getLevelNames( )[0];
			}
		}

		if ( ( categoryDimension != null && yOptionDimension != null && categoryDimension.equals( yOptionDimension ) ) )
		{
			return false;
		}
		else
		{
			return true;
		}
	}

	/**
	 * Returns level names in a dimension which is relevant to specified cube
	 * binding expression.
	 * 
	 * @param cubeBinding
	 *            specified cube binding expressions.
	 * @param cube
	 *            cube handle.
	 * @param includeSelf
	 *            indicates if the level names should include the level name of
	 *            specified cube binding.
	 * @param useFullName
	 *            indicates if the level names should use full name or simple
	 *            name.
	 * @return level name array
	 * @throws BirtException
	 *             if the format of specified cube binding expression is
	 *             illegal.
	 * @since 2.5.2
	 */
	@SuppressWarnings({
			"unchecked"
	})
	public static List<String> getLevelNamesInDimension( String cubeBinding,
			CubeHandle cube, boolean includeSelf, boolean useFullName )
			throws BirtException
	{
		if ( cubeBinding == null || cube == null)
		{
			return Collections.emptyList( ); 
		}
		
		List<IColumnBinding> bindings = ExpressionUtil.extractColumnExpressions( cubeBinding,
				ExpressionUtil.DATA_INDICATOR );
		if ( bindings.isEmpty( ) )
		{
			return Collections.emptyList( );
		}

		String levelFullName = bindings.get( 0 ).getResultSetColumnName( );
		String[] names = levelFullName.split( "/" ); //$NON-NLS-1$
		if ( names.length < 2 )
		{
			return Collections.emptyList( );
		}

		String dimensionName = names[0];
		if ( cube.getContentCount( ICubeModel.DIMENSIONS_PROP ) <= 0 )
		{
			return Collections.emptyList( );
		}

		List<String> levelNames = new ArrayList<String>( );
		Iterator<DimensionHandle> dimensions = cube.getContents( ICubeModel.DIMENSIONS_PROP )
				.iterator( );
		while ( dimensions.hasNext( ) )
		{
			DimensionHandle dimensionHandle = dimensions.next( );
			if ( dimensionName.equals( dimensionHandle.getName( ) ) )
			{
				HierarchyHandle hierarchy = (HierarchyHandle) ( dimensionHandle ).getContent( DimensionHandle.HIERARCHIES_PROP,
						0 );
				int count = hierarchy.getLevelCount( );
				for ( int i = 0; i < count; i++ )
				{
					String fullName = hierarchy.getLevel( i ).getFullName( );
					if ( !includeSelf && fullName.equals( levelFullName ) )
					{
						continue;
					}

					if ( !useFullName )
					{
						levelNames.add( hierarchy.getLevel( i ).getName( ) );
					}
					else
					{
						levelNames.add( fullName );
					}
				}

				return levelNames;
			}
		}
		return Collections.emptyList( );
	}

	/**
	 * This method corrects binding name to avoid duplicate.
	 * 
	 * @param column
	 * @param nameSet
	 */
	private static void correctBindingName( ComputedColumn column,
			Set<String> nameSet )
	{
		String name = column.getName( );
		if ( nameSet.contains( name ) )
		{
			String newName = name;
			try
			{
				int num = Integer.valueOf( name.substring( name.length( ) - 1,
						name.length( ) ) ).intValue( );
				newName = name.substring( 0, name.length( ) - 1 )
						+ String.valueOf( num + 1 );
			}
			catch ( Exception e )
			{
				// Do nothing.
				newName = name + "1"; //$NON-NLS-1$
			}
			column.setName( newName );
			nameSet.add( newName );
		}
		else
		{
			nameSet.add( name );
		}
	}
	
	public static List<ComputedColumn> generateComputedColumns(
			ExtendedItemHandle itemHandle, CubeHandle cubeHandle )
	{
		if ( cubeHandle != null )
		{
			Set<String> bindingNameSet = new HashSet<String>();
			
			List<ComputedColumn> columnList = new ArrayList<ComputedColumn>( );

			String exprType = UIUtil.getDefaultScriptType( );
			IExpressionConverter exprConverter = ExpressionUtility.getExpressionConverter( exprType );
			// Add levels
			for ( LevelHandle levelHandle : ChartCubeUtil.getAllLevels( cubeHandle ) )
			{
				// If the display field is set in dimension level, it needs to
				// add an attribute named 'DisplayName' in expression.
				String displayNameAttr = null;
				String dataType = levelHandle.getDataType( );
				if ( levelHandle instanceof TabularLevelHandle
						&& ( (TabularLevelHandle) levelHandle ).getDisplayColumnName( ) != null
						&& ( (TabularLevelHandle) levelHandle ).getDisplayColumnName( )
								.trim( )
								.length( ) > 0 )
				{
					displayNameAttr = ICubeQueryUtil.DISPLAY_NAME_ATTR;
					dataType = DesignChoiceConstants.COLUMN_DATA_TYPE_STRING;
				}
				
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartCubeUtil.createLevelBindingName( levelHandle ) );
				column.setDataType( dataType );
				column.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
						new Expression( exprConverter.getDimensionExpression( levelHandle.getContainer( )
								.getContainer( )
								.getName( ),
								levelHandle.getName( ),
								displayNameAttr ),
								exprType ) );
				correctBindingName( column, bindingNameSet);
				columnList.add( column );

				// Add LevelAttributes
				Iterator itLevelAttr = levelHandle.attributesIterator( );
				while ( itLevelAttr.hasNext( ) )
				{
					LevelAttributeHandle laHandle = (LevelAttributeHandle) itLevelAttr.next( );
					ComputedColumn columnLA = StructureFactory.newComputedColumn( itemHandle,
							ChartCubeUtil.createLevelAttrBindingName( levelHandle,
									laHandle ) );
					columnLA.setDataType( laHandle.getDataType( ) );
					columnLA.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
							new Expression( exprConverter.getDimensionExpression( levelHandle.getContainer( )
									.getContainer( )
									.getName( ),
									levelHandle.getName( ),
									laHandle.getName( ) ),
									exprType ) );
					correctBindingName( columnLA, bindingNameSet);
					columnList.add( columnLA );
				}

			}
			// Add measures
			for ( MeasureHandle measureHandle : ChartCubeUtil.getAllMeasures( cubeHandle ) )
			{
				ComputedColumn column = StructureFactory.newComputedColumn( itemHandle,
						ChartCubeUtil.createMeasureBindingName( measureHandle ) );
				column.setDataType( measureHandle.getDataType( ) );
				column.setDisplayName( measureHandle.getDisplayName( ) );
				String nameKey = measureHandle.getDisplayNameKey( );
				if ( nameKey != null )
				{
					column.setDisplayNameID( nameKey );
				}
				column.setExpressionProperty( ComputedColumn.EXPRESSION_MEMBER,
						new Expression( exprConverter.getMeasureExpression( measureHandle.getName( ) ),
								exprType ) );
				column.setAggregateFunction( DataAdapterUtil.getRollUpAggregationName( measureHandle.getFunction( ) ) );
				correctBindingName( column, bindingNameSet);
				columnList.add( column );
			}
			return columnList;
		}
		return Collections.emptyList( );
	}
}
