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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * 
 */
public class AbstractCrosstabModelTask implements ICrosstabConstants
{

	protected CrosstabReportItemHandle crosstab = null;

	/**
	 * 
	 * @param focus
	 */
	public AbstractCrosstabModelTask( AbstractCrosstabItemHandle focus )
	{
		if ( focus == null )
			throw new IllegalArgumentException( "The focus for the task can not be null" ); //$NON-NLS-1$
		this.crosstab = focus.getCrosstab( );
	}

	class AggregationInfo
	{

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		/**
		 * 
		 * @param rowDimension
		 * @param rowLevel
		 * @param colDimension
		 * @param colLevel
		 */
		public AggregationInfo( String rowDimension, String rowLevel,
				String colDimension, String colLevel )
		{
			this.rowDimension = rowDimension;
			this.rowLevel = rowLevel;
			this.colDimension = colDimension;
			this.colLevel = colLevel;
		}

		/**
		 * @return the rowDimension
		 */
		public String getRowDimension( )
		{
			return rowDimension;
		}

		/**
		 * @return the rowLevel
		 */
		public String getRowLevel( )
		{
			return rowLevel;
		}

		/**
		 * @return the colDimension
		 */
		public String getColDimension( )
		{
			return colDimension;
		}

		/**
		 * @return the colLevel
		 */
		public String getColLevel( )
		{
			return colLevel;
		}
	}

	protected AggregationInfo getAggregationInfo(
			LevelViewHandle leftLevelView, LevelViewHandle rightLevelView )
	{
		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;
		if ( leftLevelView == null )
		{
			if ( rightLevelView != null )
			{
				if ( rightLevelView.getCrosstab( ) != crosstab
						|| rightLevelView.getCubeLevelName( ) == null
						|| rightLevelView.getCubeLevelName( ).length( ) == 0 )
					return null;
				int axisType = rightLevelView.getAxisType( );
				if ( axisType == COLUMN_AXIS_TYPE )
				{
					colDimension = ( (DimensionViewHandle) rightLevelView.getContainer( ) ).getCubeDimensionName( );
					colLevel = rightLevelView.getCubeLevelName( );
				}
				else
				{
					rowDimension = ( (DimensionViewHandle) rightLevelView.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rightLevelView.getCubeLevelName( );
				}
			}
		}
		else
		{
			if ( leftLevelView.getCrosstab( ) != crosstab
					|| leftLevelView.getCubeLevelName( ) == null
					|| leftLevelView.getCubeLevelName( ).length( ) == 0 )
				return null;
			if ( rightLevelView == null )
			{
				int axisType = leftLevelView.getAxisType( );
				if ( axisType == COLUMN_AXIS_TYPE )
				{
					colDimension = ( (DimensionViewHandle) leftLevelView.getContainer( ) ).getCubeDimensionName( );
					colLevel = leftLevelView.getCubeLevelName( );
				}
				else
				{
					rowDimension = ( (DimensionViewHandle) leftLevelView.getContainer( ) ).getCubeDimensionName( );
					rowLevel = leftLevelView.getCubeLevelName( );
				}
			}
			else
			{
				if ( rightLevelView.getCrosstab( ) != crosstab
						|| rightLevelView.getCubeLevelName( ) == null
						|| rightLevelView.getCubeLevelName( ).length( ) == 0 )
					return null;

				int axisType = leftLevelView.getAxisType( );
				if ( rightLevelView.getAxisType( ) != CrosstabModelUtil.getOppositeAxisType( axisType ) )
					return null;
				if ( axisType == COLUMN_AXIS_TYPE )
				{
					colDimension = ( (DimensionViewHandle) leftLevelView.getContainer( ) ).getCubeDimensionName( );
					colLevel = leftLevelView.getCubeLevelName( );
					rowDimension = ( (DimensionViewHandle) rightLevelView.getContainer( ) ).getCubeDimensionName( );
					rowLevel = rightLevelView.getCubeLevelName( );
				}
				else
				{
					rowDimension = ( (DimensionViewHandle) leftLevelView.getContainer( ) ).getCubeDimensionName( );
					rowLevel = leftLevelView.getCubeLevelName( );
					colDimension = ( (DimensionViewHandle) rightLevelView.getContainer( ) ).getCubeDimensionName( );
					colLevel = rightLevelView.getCubeLevelName( );
				}
			}
		}
		return new AggregationInfo( rowDimension,
				rowLevel,
				colDimension,
				colLevel );
	}

	/**
	 * 
	 * @param functions
	 * @param measures
	 * @return
	 */
	protected boolean isValidParameters( List functions, List measures )
	{
		if ( functions == null || measures == null )
		{
			return false;
		}
		if ( measures.size( ) == 0 || functions.size( ) == 0 )
		{
			return false;
		}
		if ( measures.size( ) != functions.size( ) )
		{
			return false;
		}
		return true;
	}

	protected void verifyTotalMeasureFunctions( int axisType, List functions,
			List measures )
	{
		if ( functions == null
				|| measures == null
				|| functions.size( ) == 0
				|| measures.size( ) == 0 )
		{
			return;
		}

		// use all measures if the total direction is oppsite to the measure
		// direction to avoid hole
		boolean isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals( crosstab.getMeasureDirection( ) );

		if ( ( isVerticalMeasure && axisType == COLUMN_AXIS_TYPE )
				|| ( !isVerticalMeasure && axisType == ROW_AXIS_TYPE ) )
		{
			String defaultFunction = (String) functions.get( 0 );

			for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
			{
				MeasureViewHandle mv = crosstab.getMeasure( i );

				if ( !measures.contains( mv ) )
				{
					measures.add( mv );
					functions.add( defaultFunction );
				}
			}

		}
	}

	/**
	 * 
	 * @param theLevelView
	 * @param measureList
	 * @param functionList
	 * @param isAdd
	 * @param checkCounterAxis
	 * @throws SemanticException
	 */
	protected void addMeasureAggregations( LevelViewHandle theLevelView,
			List measureList, List functionList, boolean checkCounterAxis )
			throws SemanticException
	{
		if ( crosstab == null || theLevelView.getCrosstab( ) != crosstab )
			return;
		if ( measureList == null || measureList.isEmpty( ) )
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType( theLevelView.getAxisType( ) );

		// if the level view not specifies a cube level, then do nothing
		String dimensionName = ( (DimensionViewHandle) theLevelView.getContainer( ) ).getCubeDimensionName( );
		String levelName = theLevelView.getCubeLevelName( );
		if ( levelName == null || dimensionName == null )
			return;

		// status identify this level is innermost or not
		boolean isInnerMost = theLevelView.isInnerMost( );

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for ( int dimension = 0; dimension < crosstab.getDimensionCount( counterAxisType ); dimension++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( counterAxisType,
					dimension );
			for ( int level = 0; level < dimensionView.getLevelCount( ); level++ )
			{
				// one level exists in this crosstab, then set
				// isCounterAxisEmpty to false
				isCounterAxisEmpty = false;

				LevelViewHandle levelView = dimensionView.getLevel( level );
				String rowDimension = null;
				String rowLevel = null;
				String colDimension = null;
				String colLevel = null;
				if ( counterAxisType == ROW_AXIS_TYPE )
				{
					rowDimension = dimensionView.getCubeDimensionName( );
					rowLevel = levelView.getCubeLevelName( );
					colDimension = dimensionName;
					colLevel = levelName;
				}
				else if ( counterAxisType == COLUMN_AXIS_TYPE )
				{
					rowDimension = dimensionName;
					rowLevel = levelName;
					colDimension = dimensionView.getCubeDimensionName( );
					colLevel = levelView.getCubeLevelName( );
				}

				// if 'isLevelInnerMost' is true, then add aggregation for
				// those not innermost and has aggregation levels in counter
				// axis; otherwise 'isLevelInnerMost' is false, then add
				// aggregation for those is innermost or has aggregation
				// levels in counter axis
				if ( ( isInnerMost && !levelView.isInnerMost( ) && levelView.getAggregationHeader( ) != null )
						|| ( !isInnerMost
								&& theLevelView.getAggregationHeader( ) != null && ( levelView.isInnerMost( ) || levelView.getAggregationHeader( ) != null ) ) )
				{
					for ( int i = 0; i < measureList.size( ); i++ )
					{
						MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
						if ( measureView.getCrosstab( ) != crosstab )
							continue;

						String function = functionList == null ? null
								: (String) functionList.get( i );
						// if checkCounterMeasureList is true, then we need to
						// check the counter level view is aggregated on the
						// measure, otherwise do nothing
						if ( checkCounterAxis
								&& !CrosstabModelUtil.isAggregationOn( measureView,
										levelView.getCubeLevelName( ),
										counterAxisType ) )
							continue;
						CrosstabModelUtil.addDataItem( crosstab,
								measureView,
								function,
								rowDimension,
								rowLevel,
								colDimension,
								colLevel );
					}
				}
			}
		}

		// add aggregation for crosstab grand total; or there is no levels
		// and no grand total, we still need to add one aggregation
		if ( crosstab.getGrandTotal( counterAxisType ) != null
				|| ( isCounterAxisEmpty && theLevelView.getAggregationHeader( ) != null ) )
		{
			String rowDimension = null;
			String rowLevel = null;
			String colDimension = null;
			String colLevel = null;
			if ( counterAxisType == ROW_AXIS_TYPE )
			{
				colDimension = dimensionName;
				colLevel = levelName;
			}
			else if ( counterAxisType == COLUMN_AXIS_TYPE )
			{
				rowDimension = dimensionName;
				rowLevel = levelName;
			}

			for ( int i = 0; i < measureList.size( ); i++ )
			{
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
				if ( measureView.getCrosstab( ) != crosstab )
					continue;
				String function = functionList == null ? null
						: (String) functionList.get( i );
				// if checkCounterMeasureList is true, then we need to
				// check the counter level view is aggregated on the
				// measure, otherwise do nothing
				if ( checkCounterAxis
						&& !CrosstabModelUtil.isAggregationOn( measureView,
								null,
								counterAxisType ) )
					continue;

				CrosstabModelUtil.addDataItem( crosstab,
						measureView,
						function,
						rowDimension,
						rowLevel,
						colDimension,
						colLevel );

			}
		}
	}

	/**
	 * 
	 * @param axisType
	 * @param measureList
	 * @param functionList
	 * @param isAdd
	 * @param checkCounterAxis
	 * @throws SemanticException
	 */
	protected void addMeasureAggregations( int axisType, List measureList,
			List functionList, boolean checkCounterAxis )
			throws SemanticException
	{
		if ( crosstab == null
				|| measureList == null
				|| measureList.isEmpty( )
				|| crosstab.getGrandTotal( axisType ) == null )
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
		String dimensionName = null;
		String levelName = null;

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for ( int dimension = 0; dimension < crosstab.getDimensionCount( counterAxisType ); dimension++ )
		{
			DimensionViewHandle dimensionView = crosstab.getDimension( counterAxisType,
					dimension );
			for ( int level = 0; level < dimensionView.getLevelCount( ); level++ )
			{
				// one level exists in this crosstab, then set
				// isCounterAxisEmpty to false
				isCounterAxisEmpty = false;

				LevelViewHandle levelView = dimensionView.getLevel( level );
				String rowDimension = null;
				String rowLevel = null;
				String colDimension = null;
				String colLevel = null;
				if ( counterAxisType == ROW_AXIS_TYPE )
				{
					rowDimension = dimensionView.getCubeDimensionName( );
					rowLevel = levelView.getCubeLevelName( );
					colDimension = dimensionName;
					colLevel = levelName;
				}
				else if ( counterAxisType == COLUMN_AXIS_TYPE )
				{
					rowDimension = dimensionName;
					rowLevel = levelName;
					colDimension = dimensionView.getCubeDimensionName( );
					colLevel = levelView.getCubeLevelName( );
				}

				// if 'isLevelInnerMost' is true, then add aggregation for
				// those not innermost and has aggregation levels in counter
				// axis; otherwise 'isLevelInnerMost' is false, then add
				// aggregation for those is innermost or has aggregation
				// levels in counter axis
				if ( levelView.isInnerMost( )
						|| levelView.getAggregationHeader( ) != null )
				{
					for ( int i = 0; i < measureList.size( ); i++ )
					{
						MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
						if ( measureView.getCrosstab( ) != crosstab )
							continue;

						String function = functionList == null ? null
								: (String) functionList.get( i );
						// if checkCounterMeasureList is true, then we
						// need to check the counter level view is
						// aggregated on
						// the measure, otherwise do nothing
						if ( checkCounterAxis
								&& !CrosstabModelUtil.isAggregationOn( measureView,
										levelView.getCubeLevelName( ),
										counterAxisType ) )
							continue;
						CrosstabModelUtil.addDataItem( crosstab,
								measureView,
								function,
								rowDimension,
								rowLevel,
								colDimension,
								colLevel );
					}
				}
			}
		}

		// add aggregation for crosstab grand total; or there is no levels
		// and no grand total, we still need to add one aggregation
		if ( crosstab.getGrandTotal( counterAxisType ) != null
				|| isCounterAxisEmpty )
		{
			String rowDimension = null;
			String rowLevel = null;
			String colDimension = null;
			String colLevel = null;

			for ( int i = 0; i < measureList.size( ); i++ )
			{
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
				if ( measureView.getCrosstab( ) != crosstab )
					continue;

				String function = functionList == null ? null
						: (String) functionList.get( i );
				// if checkCounterMeasureList is true, then we need to
				// check the counter level view is aggregated on the
				// measure, otherwise do nothing
				if ( checkCounterAxis
						&& !CrosstabModelUtil.isAggregationOn( measureView,
								null,
								counterAxisType ) )
					continue;

				CrosstabModelUtil.addDataItem( crosstab,
						measureView,
						function,
						rowDimension,
						rowLevel,
						colDimension,
						colLevel );

			}
		}
	}

	/**
	 * Removes all the aggregations related with the level view.
	 * 
	 * @param levelView
	 */
	protected void removeMeasureAggregations( LevelViewHandle levelView )
			throws SemanticException
	{
		if ( levelView == null || levelView.getCrosstab( ) != crosstab )
			return;
		String dimensionName = ( (DimensionViewHandle) levelView.getContainer( ) ).getCubeDimensionName( );
		String levelName = levelView.getCubeLevelName( );
		if ( dimensionName == null || levelName == null )
			return;
		removeMeasureAggregations( dimensionName,
				levelName,
				levelView.getAxisType( ) );
	}

	/**
	 * Removes all the aggregations related with the grand-total in the
	 * specified axis type.
	 * 
	 * @param axisType
	 */
	protected void removeMeasureAggregations( int axisType )
			throws SemanticException
	{
		if ( !CrosstabModelUtil.isValidAxisType( axisType ) )
			return;
		removeMeasureAggregations( null, null, axisType );
	}

	/**
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @param axisType
	 */
	private void removeMeasureAggregations( String dimensionName,
			String levelName, int axisType ) throws SemanticException
	{
		if ( crosstab == null || !CrosstabModelUtil.isValidAxisType( axisType ) )
			return;

		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			for ( int j = 0; j < measureView.getAggregationCount( ); j++ )
			{
				AggregationCellHandle aggregationCell = measureView.getAggregationCell( 0 );
				String propName = CrosstabModelUtil.getAggregationOnPropName( axisType );
				String value = aggregationCell.getModelHandle( )
						.getStringProperty( propName );
				if ( ( value == null && levelName == null )
						|| ( value != null && value.equals( levelName ) ) )
					aggregationCell.getModelHandle( ).drop( );
			}
		}

	}

	/**
	 * 
	 * @param crosstab
	 * @param measureView
	 * @param toValidateLevelView
	 * @param aggregationLevels
	 * @throws SemanticException
	 */
	protected void validateMeasure( MeasureViewHandle measureView,
			LevelViewHandle toValidateLevelView, int toValidateAxisType,
			List aggregationLevels ) throws SemanticException
	{
		if ( measureView == null
				|| aggregationLevels == null
				|| ( toValidateLevelView != null && measureView.getCrosstab( ) != toValidateLevelView.getCrosstab( ) ) )
			return;
		if ( toValidateLevelView != null
				&& toValidateLevelView.getCubeLevelName( ) == null )
			return;
		if ( toValidateLevelView != null
				&& toValidateAxisType != toValidateLevelView.getAxisType( ) )
			return;

		boolean isInnerMost = toValidateLevelView == null ? false
				: toValidateLevelView.isInnerMost( );
		List unAggregationLevels = new ArrayList( );
		int unAggregationCount = 0;
		for ( int i = 0; i < aggregationLevels.size( ); i++ )
		{
			LevelViewHandle levelView = (LevelViewHandle) aggregationLevels.get( i );
			if ( isInnerMost )
			{
				// if the toValidate is innermost, then no aggregation is
				// generated with this and counter innermost one in the couter
				// axis
				if ( !levelView.isInnerMost( ) )
				{
					assert levelView.getAggregationHeader( ) != null;
					if ( getAggregation( measureView,
							toValidateLevelView,
							levelView ) == null )
					{
						unAggregationLevels.add( levelView );
						unAggregationCount++;
					}
				}
			}
			else
			{
				if ( getAggregation( measureView,
						toValidateLevelView,
						levelView ) == null )
				{
					unAggregationLevels.add( levelView );
					unAggregationCount++;
				}
			}
		}

		int maxAggregationCount = aggregationLevels.size( );
		// if the counter axis has grand-total, then consider the aggregation
		// with it
		if ( crosstab.getGrandTotal( CrosstabModelUtil.getOppositeAxisType( toValidateAxisType ) ) != null )
		{
			if ( getAggregation( measureView, toValidateLevelView, null ) == null )
			{
				maxAggregationCount++;
				unAggregationCount++;
			}
		}

		// then do checks about the unAggregationLevels the aggregation count is
		// valid: 1) 0 or the max count -1 if toValidate is innermost; 2) 0 or
		// max count if toValidate is not innermost
		// if ( ( isInnerMost && unAggregationCount != 0 && unAggregationCount
		// != maxAggregationCount - 1 )
		// || ( !isInnerMost && unAggregationCount != 0 && unAggregationCount !=
		// maxAggregationCount ) )

		if ( unAggregationCount > 0 )
		{
			for ( int i = 0; i < unAggregationLevels.size( )
					&& unAggregationCount > 0; i++ )
			{
				LevelViewHandle levelView = (LevelViewHandle) unAggregationLevels.get( i );
				String function = getAggregationFunction( measureView,
						levelView,
						CrosstabModelUtil.getOppositeAxisType( toValidateAxisType ),
						toValidateLevelView,
						toValidateAxisType );

				addAggregation( measureView,
						toValidateLevelView,
						levelView,
						function );
				unAggregationCount--;
			}

			// if unaggregationCount not equals 0, it means that the grand-total
			// is not validated, then handle this
			if ( unAggregationCount != 0 )
			{
				String function = getAggregationFunction( measureView,
						null,
						CrosstabModelUtil.getOppositeAxisType( toValidateAxisType ),
						toValidateLevelView,
						toValidateAxisType );
				addAggregation( measureView,
						toValidateLevelView,
						null,
						function );
			}
		}

	}

	/**
	 * Gets the aggregation cell for the given two level views.
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param rightLevelView
	 * @return
	 */
	private AggregationCellHandle getAggregation(
			MeasureViewHandle measureView, LevelViewHandle leftLevelView,
			LevelViewHandle rightLevelView )
	{
		if ( measureView == null || measureView.getCrosstab( ) != crosstab )
			return null;

		AggregationInfo infor = getAggregationInfo( leftLevelView,
				rightLevelView );
		if ( infor == null )
			return null;

		return measureView.getAggregationCell( infor.getRowDimension( ),
				infor.getRowLevel( ),
				infor.getColDimension( ),
				infor.getColLevel( ) );
	}

	/**
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param rightLevelView
	 * @param function
	 * @throws SemanticException
	 */
	private void addAggregation( MeasureViewHandle measureView,
			LevelViewHandle leftLevelView, LevelViewHandle rightLevelView,
			String function ) throws SemanticException
	{
		if ( measureView == null || measureView.getCrosstab( ) != crosstab )
			return;
		AggregationInfo infor = getAggregationInfo( leftLevelView,
				rightLevelView );
		if ( infor == null )
			return;

		CrosstabModelUtil.addDataItem( measureView.getCrosstab( ),
				measureView,
				function,
				infor.getRowDimension( ),
				infor.getRowLevel( ),
				infor.getColDimension( ),
				infor.getColLevel( ) );
	}

	/**
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param leftAxisType
	 * @param rightLevelView
	 * @param rightAxisType
	 * @return
	 */
	private String getAggregationFunction( MeasureViewHandle measureView,
			LevelViewHandle leftLevelView, int leftAxisType,
			LevelViewHandle rightLevelView, int rightAxisType )
	{
		if ( measureView == null || measureView.getCrosstab( ) != crosstab )
			return null;
		if ( !CrosstabModelUtil.isValidAxisType( leftAxisType )
				|| !CrosstabModelUtil.isValidAxisType( rightAxisType ) )
			return null;
		if ( leftAxisType != CrosstabModelUtil.getOppositeAxisType( rightAxisType ) )
			return null;

		// search the column first, then the row
		if ( COLUMN_AXIS_TYPE == leftAxisType )
		{
			String function = null;
			function = getAggregationFunction( measureView,
					leftLevelView,
					leftAxisType );
			if ( function != null )
				return function;
			return getAggregationFunction( measureView,
					rightLevelView,
					rightAxisType );
		}
		String function = null;
		function = getAggregationFunction( measureView,
				rightLevelView,
				rightAxisType );
		if ( function != null )
			return function;
		return getAggregationFunction( measureView, leftLevelView, leftAxisType );

	}

	/**
	 * Gets the aggregation function by the level view.
	 * 
	 * @param measureView
	 * @param levelView
	 * @param axisType
	 * @return
	 */
	private String getAggregationFunction( MeasureViewHandle measureView,
			LevelViewHandle levelView, int axisType )
	{
		// grand-total
		if ( levelView == null )
			return crosstab.getAggregationFunction( axisType, measureView );
		// sub-total
		return levelView.getAggregationFunction( measureView );
	}

	/**
	 * 
	 * @param leftLevelView
	 * @param rightLevelView
	 * @throws SemanticException
	 */
	protected void removeMeasureAggregation( LevelViewHandle leftLevelView,
			LevelViewHandle rightLevelView ) throws SemanticException
	{
		AggregationInfo infor = getAggregationInfo( leftLevelView,
				rightLevelView );
		for ( int i = 0; i < crosstab.getMeasureCount( ); i++ )
		{
			MeasureViewHandle measureView = crosstab.getMeasure( i );
			measureView.removeAggregation( infor.getRowDimension( ),
					infor.getRowLevel( ),
					infor.getColDimension( ),
					infor.getColLevel( ) );
		}
	}
}
