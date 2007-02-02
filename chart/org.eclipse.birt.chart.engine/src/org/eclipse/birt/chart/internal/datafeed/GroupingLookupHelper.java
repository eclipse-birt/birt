/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.util.ULocale;

/**
 * Helper to lookup the index of each data definition in the evaluator data.
 */

public class GroupingLookupHelper
{

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/trace" ); //$NON-NLS-1$

	private LinkedHashMap lhmAggExp = new LinkedHashMap( 8 );

	private List lstAll = new ArrayList( 8 );

	private String strBaseAggExp = null;

	private int iLookup = 0;

	/**
	 * Constructor. Finds all data expressions and aggregation expressions in
	 * the chart model in the order and restore them in the lookup list
	 * 
	 * @param cm
	 *            chart model
	 * @param iae
	 *            IActionEvaluator to get the expressions in triggers
	 * @throws ChartException
	 */
	public GroupingLookupHelper( Chart cm, IActionEvaluator iae )
			throws ChartException
	{
		if ( cm instanceof ChartWithAxes )
		{
			initRowExpressions( (ChartWithAxes) cm, iae );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			initRowExpressions( (ChartWithoutAxes) cm, iae );
		}
	}

	/**
	 * Constructor. Restore all expressions in the lookup list.
	 * 
	 * @param dataExps
	 *            data expressions collection
	 * @param aggExps
	 *            aggregation expressions collection
	 * 
	 */
	public GroupingLookupHelper( Collection dataExps, Collection aggExps )
	{
		Iterator dataIterator = dataExps.iterator( );
		Iterator aggIterator = aggExps.iterator( );
		while ( dataIterator.hasNext( ) && aggIterator.hasNext( ) )
		{
			String dataExp = (String) dataIterator.next( );
			String aggExp = (String) aggIterator.next( );
			lstAll.add( dataExp );
			lhmAggExp.put( generateKey( dataExp, aggExp ),
					new Integer( iLookup++ ) );
		}
	}

	/**
	 * Gets the list for all data expressions. Only for lookup, and can't be
	 * changed directly.
	 * 
	 * @return the list for all data expressions
	 */
	public List getExpressions( )
	{
		return lstAll;
	}

	private String generateKey( String dataExp, String aggExp )
	{
		if ( aggExp == null )
		{
			return dataExp;
		}
		return dataExp + aggExp;
	}

	/**
	 * Finds the index according to the data expression and aggregation
	 * expression of base series grouping.
	 * 
	 * @param dataExp
	 *            the data expression to lookup
	 * @return the index of the data expression in the evaluator data
	 */
	public int findIndex( String dataExp )
	{
		return findIndex( dataExp, this.strBaseAggExp );
	}

	/**
	 * Finds the index according to the combination of data expression and
	 * aggregation expression.
	 * 
	 * @param dataExp
	 *            data expression
	 * @param aggExp
	 *            aggregation expression
	 * @return the index in the evaluator data
	 */
	public int findIndex( String dataExp, String aggExp )
	{
		Object value = null;
		if ( aggExp == null || aggExp.trim( ).length( ) == 0 )
		{
			value = lhmAggExp.get( generateKey( dataExp, this.strBaseAggExp ) );
		}
		else
		{
			value = lhmAggExp.get( generateKey( dataExp, aggExp ) );

		}
		return value instanceof Integer ? ( (Integer) value ).intValue( ) : -1;
	}

	/**
	 * Finds the index according to the combination of data expression and
	 * aggregation expression in a batch. Note that all data expressions must
	 * match the same aggregation expression.
	 * 
	 * @param dataExpArray
	 *            data expression array
	 * @param aggExp
	 *            aggregation expression
	 * @return the index array in the evaluator data
	 */
	public int[] findBatchIndex( String[] dataExpArray, String aggExp )
	{
		int[] indexArray = new int[dataExpArray.length];
		for ( int i = 0; i < indexArray.length; i++ )
		{
			indexArray[i] = this.findIndex( dataExpArray[i], aggExp );
		}
		return indexArray;
	}

	private boolean addDataExp( String dataExp )
	{
		return addDataExp( dataExp, this.strBaseAggExp );
	}

	private boolean addDataExp( String dataExp, String aggExp )
	{
		if ( dataExp != null && dataExp.trim( ).length( ) > 0 )
		{
			String key = generateKey( dataExp, aggExp );
			if ( !lhmAggExp.containsKey( key ) )
			{
				lhmAggExp.put( key, new Integer( iLookup++ ) );
				lstAll.add( dataExp );
			}
			return true;
		}
		return false;
	}

	private void addLookupForBaseSeries( SeriesDefinition baseSD )
			throws ChartException
	{
		final Query qBaseSeriesDefinition = baseSD.getQuery( );
		String sExpression = qBaseSeriesDefinition.getDefinition( );
		if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
		{
			// Ignore expression for base series definition
			logger.log( ILogger.WARNING,
					Messages.getString( "dataprocessor.log.baseSeriesDefn3", sExpression, ULocale.getDefault( ) ) ); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = baseSD.getDesignTimeSeries( );
		EList elBaseSeries = seBase.getDataDefinition( );
		if ( elBaseSeries.size( ) != 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.FoundDefnAssociatedWithX", //$NON-NLS-1$
					new Object[]{
						String.valueOf( elBaseSeries.size( ) )
					},
					null );
		}

		if ( !addDataExp( ( (Query) elBaseSeries.get( 0 ) ).getDefinition( ) ) )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.DefinitionUnspecified", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}
	}

	private void addLookupForOrthogonalSeries( EList lstOrthogonalSDs,
			IActionEvaluator iae ) throws ChartException
	{
		for ( int k = 0; k < lstOrthogonalSDs.size( ); k++ )
		{
			SeriesDefinition orthoSD = (SeriesDefinition) lstOrthogonalSDs.get( k );
			Query qOrthogonalSeriesDefinition = orthoSD.getQuery( );
			if ( qOrthogonalSeriesDefinition == null )
			{
				return;
			}

			String strOrthoAgg = getOrthogonalAggregationExpression( orthoSD );
			addDataExp( qOrthogonalSeriesDefinition.getDefinition( ),
					strOrthoAgg );

			Series seOrthogonal = orthoSD.getDesignTimeSeries( );
			EList elOrthogonalSeries = seOrthogonal.getDataDefinition( );
			if ( elOrthogonalSeries.isEmpty( ) )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_BINDING,
						"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
						new Object[]{
								String.valueOf( k ), seOrthogonal
						},
						Messages.getResourceBundle( ) );
			}

			boolean bAnyQueries = false;
			for ( int i = 0; i < elOrthogonalSeries.size( ); i++ )
			{
				Query qOrthogonalSeries = (Query) elOrthogonalSeries.get( i );
				if ( qOrthogonalSeries == null ) // NPE PROTECTION
				{
					continue;
				}

				if ( addDataExp( qOrthogonalSeries.getDefinition( ),
						strOrthoAgg ) )
				{
					bAnyQueries = true;
				}
			}

			if ( !bAnyQueries )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_BINDING,
						"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
						new Object[]{
								String.valueOf( k ), seOrthogonal
						},
						Messages.getResourceBundle( ) );
			}

			// Add orthogonal series trigger expressions.
			String[] triggerExprs = DataProcessor.getSeriesTriggerExpressions( seOrthogonal,
					iae );
			if ( triggerExprs != null )
			{
				for ( int t = 0; t < triggerExprs.length; t++ )
				{
					addDataExp( triggerExprs[t], strOrthoAgg );
				}
			}
		}
	}

	private void initRowExpressions( ChartWithoutAxes cwoa, IActionEvaluator iae )
			throws ChartException
	{
		EList elSD = cwoa.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.CannotDecipher", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition baseSD = (SeriesDefinition) elSD.get( 0 );		
		this.strBaseAggExp = getAggregationExpression( baseSD );
		addLookupForBaseSeries( baseSD );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		addLookupForOrthogonalSeries( baseSD.getSeriesDefinitions( ), iae );
	}

	private void initRowExpressions( ChartWithAxes cwa, IActionEvaluator iae )
			throws ChartException
	{
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList elSD = axPrimaryBase.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					"dataprocessor.exception.CannotDecipher2", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition baseSD = (SeriesDefinition) elSD.get( 0 );
		this.strBaseAggExp = getAggregationExpression( baseSD );
		addLookupForBaseSeries( baseSD );

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		for ( int j = 0; j < axaOrthogonal.length; j++ )
		{
			addLookupForOrthogonalSeries( axaOrthogonal[j].getSeriesDefinitions( ),
					iae );
		}

	}

	static String getAggregationExp( SeriesDefinition baseSD,
			SeriesDefinition orthoSD )
	{
		boolean bBaseGroupEnabled = baseSD.getGrouping( ).isEnabled( );
		String strOrthoAgg = null;
		if ( bBaseGroupEnabled )
		{
			String strBaseAgg = baseSD.getGrouping( ).getAggregateExpression( );
			boolean bOrthoGroupEnabled = orthoSD.getGrouping( ).isEnabled( );
			strOrthoAgg = bOrthoGroupEnabled ? orthoSD.getGrouping( )
					.getAggregateExpression( ) : strBaseAgg;
		}
		return strOrthoAgg;
	}

	/**
	 * Simply gets aggregation expressions for the series definitions. If
	 * grouping is not enabled, return null
	 * 
	 * @param sd
	 *            series definition
	 * @return aggregation expressions for the series definitions, or null if
	 *         grouping is disabled.
	 */
	static String getAggregationExpression( SeriesDefinition sd )
	{
		SeriesGrouping grouping = sd.getGrouping( );
		if ( grouping.isSetEnabled( ) && grouping.isEnabled( ) )
		{
			return grouping.getAggregateExpression( );
		}
		return null;
	}

	/**
	 * Gets aggregation expressions of orthogonal series definition. If base
	 * series doesn't enable grouping, return null. If its own grouping is null,
	 * return the one of base grouping, otherwise, return its own.
	 * 
	 * @param orthoSD
	 *            orthogonal series definition
	 * @return If base series doesn't enable grouping, return null. If its own
	 *         grouping is null, return the one of base grouping, otherwise,
	 *         return its own.
	 */
	public String getOrthogonalAggregationExpression( SeriesDefinition orthoSD )
	{
		String strOrthoAgg = null;
		SeriesGrouping grouping = orthoSD.getGrouping( );
		// Only if base series has enabled grouping
		if ( this.strBaseAggExp != null )
		{
			if ( grouping.isSetEnabled( ) && grouping.isEnabled( ) )
			{
				// Set own group
				strOrthoAgg = grouping.getAggregateExpression( );
			}
			else
			{
				// Set base group
				strOrthoAgg = strBaseAggExp;
			}
		}
		return strOrthoAgg;
	}

}
