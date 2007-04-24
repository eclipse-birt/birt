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
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * DimensionViewTask
 */
public class DimensionViewTask extends AbstractCrosstabModelTask
{

	protected DimensionViewHandle dimensionView = null;

	/**
	 * 
	 * @param focus
	 */
	public DimensionViewTask( DimensionViewHandle focus )
	{
		super( focus );
		this.dimensionView = focus;
	}

	/**
	 * Inserts a level handle into a dimension view. This method will add the
	 * aggregations and data-item automatically.
	 * 
	 * @param dimensionView
	 * @param levelHandle
	 * @param index
	 * @return
	 * @throws SemanticException
	 */
	public LevelViewHandle insertLevel( LevelHandle levelHandle, int index )
			throws SemanticException
	{
		if ( levelHandle != null )
		{
			// if cube dimension container of this cube level element is not
			// what is referred by this dimension view, then the insertion is
			// forbidden
			if ( !levelHandle.getContainer( )
					.getContainer( )
					.getQualifiedName( )
					.equals( dimensionView.getCubeDimensionName( ) ) )
			{
				// TODO: throw exception
				dimensionView.getLogger( ).log( Level.WARNING, "" ); //$NON-NLS-1$
				return null;
			}

			// if this level handle has referred by an existing level view,
			// then log error and do nothing
			if ( dimensionView.getLevel( levelHandle.getQualifiedName( ) ) != null )
			{
				dimensionView.getLogger( ).log( Level.SEVERE,
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL,
						levelHandle.getQualifiedName( ) );
				throw new CrosstabException( dimensionView.getModelHandle( )
						.getElement( ), new String[]{
						levelHandle.getQualifiedName( ),
						dimensionView.getModelHandle( )
								.getElement( )
								.getIdentifier( )
				}, MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL );
			}
		}

		CommandStack stack = dimensionView.getCommandStack( );
		stack.startTrans( Messages.getString( "DimensionViewTask.msg.insert.level" ) ); //$NON-NLS-1$

		LevelViewHandle levelView = null;

		try
		{
			ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory.createLevelView( dimensionView.getModuleHandle( ),
					levelHandle );
			if ( extendedItemHandle != null )
			{
				dimensionView.getLevelsProperty( ).add( extendedItemHandle,
						index );

				levelView = (LevelViewHandle) CrosstabUtil.getReportItem( extendedItemHandle,
						LEVEL_VIEW_EXTENSION_NAME );

				// if level handle is specified, do some post work after adding
				if ( levelHandle != null && crosstab != null )
				{
					doPostInsert( levelView );
				}
			}
		}
		catch ( SemanticException e )
		{
			stack.rollback( );
			throw e;
		}
		stack.commit( );

		return levelView;
	}

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	private void doPostInsert( LevelViewHandle levelView )
			throws SemanticException
	{
		int axisType = dimensionView.getAxisType( );
		if ( levelView.isInnerMost( ) )
		{
			// if originally there is no levels and grand total,
			// then remove the aggregations for the axis type and
			// the counter axis level aggregations
			if ( CrosstabModelUtil.getAllLevelCount( crosstab, axisType ) <= 1 )
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel( levelView, axisType );

				if ( crosstab.getGrandTotal( axisType ) == null )
				{
					removeMeasureAggregations( axisType );
				}
			}
			else
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel( levelView, axisType );

				// add one aggregation: the original innermost level
				// before this level is added and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = CrosstabModelUtil.getPrecedingLevel( levelView );
				int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
				assert precedingLevel != null;
				LevelViewHandle innerMostLevelView = CrosstabModelUtil.getInnerMostLevel( crosstab,
						counterAxisType );
				if ( precedingLevel.getAggregationHeader( ) != null )
				{
					if ( innerMostLevelView != null )
					{
						String dimensionName = ( (DimensionViewHandle) innerMostLevelView.getContainer( ) ).getCubeDimensionName( );

						String levelName = innerMostLevelView.getCubeLevelName( );
						List measureList = precedingLevel.getAggregationMeasures( );
						List functionList = new ArrayList( );
						for ( int i = 0; i < measureList.size( ); i++ )
						{
							MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
							String function = precedingLevel.getAggregationFunction( measureView );
							functionList.add( function );
						}

						// add the data-item
						CrosstabModelUtil.addMeasureAggregations( crosstab,
								dimensionName,
								levelName,
								counterAxisType,
								( (DimensionViewHandle) precedingLevel.getContainer( ) ).getCubeDimensionName( ),
								precedingLevel.getCubeLevelName( ),
								measureList,
								functionList );
					}
					else
					{
						// TODO add dummy grandtotal???
					}
				}
				else
				{
					// orginally, the preceding one is the innermost, we add
					// some aggregations for this innermost, even though it has
					// no sub-total; however, now, it is not innermost and
					// neither has sub-total, therefore, we should remove
					// aggregations about this
					removeMeasureAggregations( precedingLevel );
				}
			}
		}
		else
		{
			// if the added level view is not innermost and has
			// aggregation header, then add aggregations for this
			// level view and all counterpart axis levels and grand
			// total
			if ( levelView.getAggregationHeader( ) != null )
			{
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel( levelView, axisType );
			}
		}
	}

	// /**
	// *
	// * @param axisType
	// * @param isInnerMost
	// * @throws SemanticException
	// */
	// public void doInsertBefore( int axisType, boolean isInnerMost )
	// throws SemanticException
	// {
	// int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
	//
	// LevelViewHandle innerMostLeveView = CrosstabModelUtil
	// .getInnerMostLevel( crosstab, counterAxisType );
	// if ( CrosstabModelUtil.getAllLevelCount( crosstab, axisType ) <= 0 )
	// {
	// // if originally there is no levels and grand total,
	// // then remove the aggregations for the axis type and
	// // the counter axis level aggregations
	// if ( crosstab.getGrandTotal( axisType ) == null )
	// removeMeasureAggregations( axisType );
	// }
	// else if ( isInnerMost )
	// {
	// // add one aggregation: the original innermost level
	// // before this level is added and the innermost
	// // level in the counter axis if the orginal
	// // innermost has aggregation header
	// LevelViewHandle precedingLevel = CrosstabModelUtil
	// .getInnerMostLevel( crosstab, axisType );
	// assert precedingLevel != null;
	// if ( precedingLevel.getAggregationHeader( ) != null )
	// {
	// if ( innerMostLeveView != null )
	// {
	// String dimensionName = ( (DimensionViewHandle) innerMostLeveView
	// .getContainer( ) ).getCubeDimensionName( );
	//
	// String levelName = innerMostLeveView.getCubeLevelName( );
	// List measureList = precedingLevel.getAggregationMeasures( );
	// List functionList = new ArrayList( );
	// for ( int i = 0; i < measureList.size( ); i++ )
	// {
	// MeasureViewHandle measureView = (MeasureViewHandle) measureList
	// .get( i );
	// String function = precedingLevel
	// .getAggregationFunction( measureView );
	// functionList.add( function );
	// }
	//
	// // add the data-item
	// CrosstabModelUtil.addMeasureAggregations( crosstab,
	// dimensionName, levelName, counterAxisType,
	// ( (DimensionViewHandle) precedingLevel
	// .getContainer( ) ).getCubeDimensionName( ),
	// precedingLevel.getCubeLevelName( ), measureList,
	// functionList );
	// }
	// else
	// {
	// // there is no levels in the counter axis, then we should
	// // add do nothing
	// }
	// }
	// else
	// {
	// // orginally, the preceding one is the innermost, we add
	// // some aggregations for this innermost, even though it has
	// // no sub-total; however, now, it is not innermost and
	// // neither has sub-total, therefore, we should remove
	// // aggregations about this
	// removeMeasureAggregations( precedingLevel );
	// }
	// }
	// }

	/**
	 * 
	 * @param levelView
	 * @param axisType
	 * @throws SemanticException
	 */
	private void addAggregationForLevel( LevelViewHandle levelView, int axisType )
			throws SemanticException
	{
		assert CrosstabModelUtil.isValidAxisType( axisType );
		if ( levelView != null && levelView.getAxisType( ) != axisType )
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );

		// first add all aggregation for the added level view, for it is
		// innermost
		for ( int dimension = 0; dimension < crosstab.getDimensionCount( counterAxisType ); dimension++ )
		{
			DimensionViewHandle tempDimensionView = crosstab.getDimension( counterAxisType,
					dimension );
			for ( int level = 0; level < tempDimensionView.getLevelCount( ); level++ )
			{
				LevelViewHandle tempLevelView = tempDimensionView.getLevel( level );

				// if level view is not null, that is not grand-total
				if ( levelView != null )
				{
					boolean isInnerMost = levelView.isInnerMost( );
					if ( isInnerMost && tempLevelView.isInnerMost( ) )
						continue;
				}
				// if this level has no sub-total, do nothing
				if ( tempLevelView.getAggregationHeader( ) == null )
					continue;
				List measureList = tempLevelView.getAggregationMeasures( );
				AggregationInfo infor = getAggregationInfo( levelView,
						tempLevelView );
				for ( int i = 0; i < measureList.size( ); i++ )
				{
					MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
					String function = tempLevelView.getAggregationFunction( measureView );
					CrosstabModelUtil.addDataItem( crosstab,
							measureView,
							function,
							infor.getRowDimension( ),
							infor.getRowLevel( ),
							infor.getColDimension( ),
							infor.getColLevel( ) );
				}
			}
		}

		// handle for grand-total
		if ( crosstab.getGrandTotal( counterAxisType ) != null
				|| CrosstabModelUtil.getAllLevelCount( crosstab,
						counterAxisType ) == 0 )
		{
			List measureList = crosstab.getAggregationMeasures( counterAxisType );
			AggregationInfo infor = getAggregationInfo( levelView, null );
			for ( int i = 0; i < measureList.size( ); i++ )
			{
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get( i );
				String function = crosstab.getAggregationFunction( counterAxisType,
						measureView );
				CrosstabModelUtil.addDataItem( crosstab,
						measureView,
						function,
						infor.getRowDimension( ),
						infor.getRowLevel( ),
						infor.getColDimension( ),
						infor.getColLevel( ) );
			}
		}

	}

	/**
	 * Removes a level view that refers a cube level element with the given
	 * name.
	 * 
	 * @param name
	 *            name of the cube level element to remove
	 * @throws SemanticException
	 */
	public void removeLevel( String name ) throws SemanticException
	{
		LevelViewHandle levelView = dimensionView.getLevel( name );
		if ( levelView != null )
		{
			removeLevel( levelView, true );
		}
	}

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	void removeLevel( LevelViewHandle levelView, boolean needTransaction )
			throws SemanticException
	{
		assert levelView != null;

		CommandStack stack = null;

		if ( needTransaction )
		{
			stack = dimensionView.getCommandStack( );
			stack.startTrans( Messages.getString( "DimensionViewTask.msg.remove.level" ) ); //$NON-NLS-1$
		}

		try
		{
			// adjust measure aggregations and then remove level view from
			// the design tree, the order can not reversed
			if ( crosstab != null )
			{
				doPreRemove( levelView );
			}

			levelView.getModelHandle( ).drop( );
		}
		catch ( SemanticException e )
		{
			if ( needTransaction )
			{
				stack.rollback( );
			}

			throw e;
		}

		if ( needTransaction )
		{
			stack.commit( );
		}
	}

	/**
	 * Removes a level view at the given position. The position index is 0-based
	 * integer.
	 * 
	 * @param index
	 *            the position index of the level view to remove
	 * @throws SemanticException
	 */
	public void removeLevel( int index ) throws SemanticException
	{
		LevelViewHandle levelView = dimensionView.getLevel( index );
		if ( levelView != null )
		{
			removeLevel( levelView, true );
		}
	}

	// /**
	// *
	// * @param levelView
	// * @param axisType
	// * @param isInnerMost
	// * @throws SemanticException
	// */
	// public void doRemovePost( int axisType, boolean isInnerMost )
	// throws SemanticException
	// {
	// int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
	//
	// if ( CrosstabModelUtil.getAllLevelCount( crosstab, axisType ) <= 0 )
	// {
	// // there is no level left and grand-total is not set, then we add
	// // aggregations for the whole axis
	// if ( crosstab.getGrandTotal( axisType ) == null )
	// addAggregationForLevel( null, axisType );
	// }
	// else if ( isInnerMost )
	// {
	// // remove one aggregation: when the level is removed, the second
	// // innermost level becomes the innermost, then we should remove the
	// // aggregation that is aggregated on this and the innermost in the
	// // counter axis
	// LevelViewHandle innerMostLevel = CrosstabModelUtil
	// .getInnerMostLevel( crosstab, axisType );
	// assert innerMostLevel != null;
	// if ( innerMostLevel.getAggregationHeader( ) != null )
	// {
	// LevelViewHandle innerMostLevelView = CrosstabModelUtil
	// .getInnerMostLevel( crosstab, counterAxisType );
	// if ( innerMostLevelView != null )
	// {
	// removeMeasureAggregation( innerMostLevelView,
	// innerMostLevel );
	// }
	// }
	// else
	// {
	// // orginally, the preceding one is the second innermost and now
	// // becomes the innermost, so we should add aggregations even if
	// // it has no sub-total
	// addAggregationForLevel( innerMostLevel, axisType );
	// }
	// }
	// }

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	private void doPreRemove( LevelViewHandle levelView )
			throws SemanticException
	{
		if ( crosstab == null )
			return;

		int axisType = dimensionView.getAxisType( );
		if ( levelView.isInnerMost( ) )
		{
			if ( CrosstabModelUtil.getAllLevelCount( crosstab, axisType ) <= 1 )
			{
				// no level exists when this level is removed: if no
				// grand-total, then we should add aggregations for the empty
				// axis
				if ( crosstab.getGrandTotal( axisType ) == null )
				{
					addAggregationForLevel( null, axisType );
				}

				// remove aggregations related with the level view
				removeMeasureAggregations( levelView );
			}
			else
			{
				// remove one aggregation: the original second innermost level
				// before this level is removed and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = CrosstabModelUtil.getPrecedingLevel( levelView );

				assert precedingLevel != null;
				if ( precedingLevel.getAggregationHeader( ) != null )
				{
					int counterAxisType = CrosstabModelUtil.getOppositeAxisType( axisType );
					LevelViewHandle innerMostLevelView = CrosstabModelUtil.getInnerMostLevel( crosstab,
							counterAxisType );
					if ( innerMostLevelView != null )
					{
						// remove aggregation with innermost level on counter
						// axis, since this will become the detail cell
						removeMeasureAggregation( precedingLevel,
								innerMostLevelView );

						// remove the aggregation header cell since now it
						// becomes innermost
						precedingLevel.getAggregationHeaderProperty( ).drop( 0 );
					}
					else
					{
						// no levels on counter axis, we should remove subtotal
						// for the new innermost level
						precedingLevel.removeSubTotal( );
					}
				}
				else
				{
					// orginally, the preceding one is the second innermost and
					// now becomes the innermost, so we should add aggregations
					// even if it has no sub-total
					addAggregationForLevel( precedingLevel, axisType );
				}

				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				removeMeasureAggregations( levelView );
			}
		}
		else
		{
			// if the added level view is not innermost and has
			// aggregation header, then remove aggregations for this
			// level view and all counterpart axis levels and grand
			// total
			if ( levelView.getAggregationHeader( ) != null )
			{
				removeMeasureAggregations( levelView );
			}
		}
	}
}
