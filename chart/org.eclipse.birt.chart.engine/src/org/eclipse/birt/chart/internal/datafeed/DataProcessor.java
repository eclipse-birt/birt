/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.datafeed;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.datafeed.IResultSetDataSet;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IActionEvaluator;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.factory.RunTimeContext;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.DateTimeDataSet;
import org.eclipse.birt.chart.model.data.NumberDataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.StockDataSet;
import org.eclipse.birt.chart.model.data.TextDataSet;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.script.ScriptHandler;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * An internal class used for data binding, runtime series generating.
 */
public class DataProcessor
{

	private final RunTimeContext rtc;
	private final IActionEvaluator iae;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/trace" ); //$NON-NLS-1$

	/**
	 * The constructor.
	 * 
	 * @param rtc
	 */
	public DataProcessor( RunTimeContext rtc, IActionEvaluator iae )
	{
		this.rtc = rtc;
		this.iae = iae;
	}

	/**
	 * Returns all associated datarow expressions in chart model
	 */
	public static List getRowExpressions( Chart cm, IActionEvaluator iae )
			throws ChartException
	{
		if ( cm instanceof ChartWithAxes )
		{
			return getRowExpressions( (ChartWithAxes) cm, iae );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			return getRowExpressions( (ChartWithoutAxes) cm, iae );
		}
		return null;
	}

	private static List getRowExpressions( ChartWithoutAxes cwoa,
			IActionEvaluator iae ) throws ChartException
	{
		final ArrayList alExpressions = new ArrayList( 4 );
		EList elSD = cwoa.getSeriesDefinitions( );
		if ( elSD.size( ) != 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.CannotDecipher", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES DEFINITION
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 );
		final Query qBaseSeriesDefinition = sd.getQuery( );
		String sExpression = qBaseSeriesDefinition.getDefinition( );
		if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
		{
			// Ignore expression for base series definition
			logger.log( ILogger.WARNING,
					Messages.getString( "dataprocessor.log.baseSeriesDefn3", sExpression, ULocale.getDefault( ) ) ); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
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

		final Query qBaseSeries = (Query) elBaseSeries.get( 0 );
		sExpression = qBaseSeries.getDefinition( );
		if ( sExpression != null
				&& sExpression.trim( ).length( ) > 0
				&& !alExpressions.contains( sExpression ) )
		{
			alExpressions.add( sExpression );
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.DefinitionUnspecified", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList elOrthogonalSeries;
		elSD = sd.getSeriesDefinitions( ); // ALL ORTHOGONAL SERIES DEFINITIONS
		int iCount = 0;
		boolean bAnyQueries;
		for ( int k = 0; k < elSD.size( ); k++ )
		{
			sd = (SeriesDefinition) elSD.get( k );
			qOrthogonalSeriesDefinition = sd.getQuery( );
			if ( qOrthogonalSeriesDefinition == null )
			{
				continue;
			}
			sExpression = qOrthogonalSeriesDefinition.getDefinition( );
			if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
			{
				// FILTER OUT DUPLICATE ENTRIES
				if ( alExpressions.contains( sExpression ) )
				{
					int iRemovalIndex = alExpressions.indexOf( sExpression );
					if ( iRemovalIndex > iCount )
					{
						alExpressions.remove( iRemovalIndex );
						alExpressions.add( iCount++, sExpression );
					}
					else
					{
						// DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
						// continue;
					}
				}
				else
				{
					// INSERT AT START
					alExpressions.add( iCount++, sExpression );
				}
			}

			seOrthogonal = sd.getDesignTimeSeries( );
			elOrthogonalSeries = seOrthogonal.getDataDefinition( );
			if ( elOrthogonalSeries.isEmpty( ) )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_BINDING,
						"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
						new Object[]{
								String.valueOf( iCount ), seOrthogonal
						},
						Messages.getResourceBundle( ) );
			}

			bAnyQueries = false;
			for ( int i = 0; i < elOrthogonalSeries.size( ); i++ )
			{
				qOrthogonalSeries = (Query) elOrthogonalSeries.get( i );
				if ( qOrthogonalSeries == null ) // NPE PROTECTION
				{
					continue;
				}

				sExpression = qOrthogonalSeries.getDefinition( );
				if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
				{
					bAnyQueries = true;
					if ( !alExpressions.contains( sExpression ) )
					{
						alExpressions.add( sExpression ); // APPEND AT END
					}
				}
			}
			if ( !bAnyQueries )
			{
				throw new ChartException( ChartEnginePlugin.ID,
						ChartException.DATA_BINDING,
						"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
						new Object[]{
								String.valueOf( iCount ), seOrthogonal
						},
						Messages.getResourceBundle( ) );
			}

			// Add orthogonal series trigger expressions.
			String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonal,
					iae );
			if ( triggerExprs != null )
			{
				for ( int t = 0; t < triggerExprs.length; t++ )
				{
					String tgexp = triggerExprs[t];
					if ( !alExpressions.contains( tgexp ) )
					{
						alExpressions.add( tgexp ); // APPEND AT END
					}
				}
			}
		}

		return alExpressions;
	}

	private static List getRowExpressions( ChartWithAxes cwa,
			IActionEvaluator iae ) throws ChartException
	{
		final ArrayList alExpressions = new ArrayList( 4 );
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
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 );
		final Query qBaseSeriesDefinition = sd.getQuery( );
		String sExpression = qBaseSeriesDefinition.getDefinition( );
		if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
		{
			// ignore expression for base series definition
			logger.log( ILogger.WARNING,
					Messages.getString( "dataprocessor.log.XSeriesDefn", sExpression, ULocale.getDefault( ) ) ); //$NON-NLS-1$
		}

		// PROJECT THE EXPRESSION ASSOCIATED WITH THE BASE SERIES EXPRESSION
		final Series seBase = sd.getDesignTimeSeries( );
		EList elBaseSeries = seBase.getDataDefinition( );
		if ( elBaseSeries.size( ) != 1 )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.FoundMoreThanOneDefnAssociateX", //$NON-NLS-1$
					new Object[]{
						String.valueOf( elBaseSeries.size( ) )
					},
					Messages.getResourceBundle( ) );
		}

		final Query qBaseSeries = (Query) elBaseSeries.get( 0 );
		sExpression = qBaseSeries.getDefinition( );
		if ( sExpression != null
				&& sExpression.trim( ).length( ) > 0
				&& !alExpressions.contains( sExpression ) )
		{
			alExpressions.add( sExpression );
		}
		else
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					"dataprocessor.exception.definitionsAssociatedWithX", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		// PROJECT ALL DATA DEFINITIONS ASSOCIATED WITH THE ORTHOGONAL SERIES
		Query qOrthogonalSeriesDefinition, qOrthogonalSeries;
		Series seOrthogonal;
		EList elOrthogonalSeries;
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		int iCount = 0;
		boolean bAnyQueries;
		for ( int j = 0; j < axaOrthogonal.length; j++ )
		{
			elSD = axaOrthogonal[j].getSeriesDefinitions( );
			for ( int k = 0; k < elSD.size( ); k++ )
			{
				sd = (SeriesDefinition) elSD.get( k );
				qOrthogonalSeriesDefinition = sd.getQuery( );
				if ( qOrthogonalSeriesDefinition == null )
				{
					continue;
				}

				sExpression = qOrthogonalSeriesDefinition.getDefinition( );
				if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
				{
					// FILTER OUT DUPLICATE ENTRIES
					if ( alExpressions.contains( sExpression ) )
					{
						int iRemovalIndex = alExpressions.indexOf( sExpression );
						if ( iRemovalIndex > iCount )
						{
							alExpressions.remove( iRemovalIndex );
							alExpressions.add( iCount++, sExpression );
						}
						else
						{
							// DON'T ADD IF PREVIOUSLY ADDED BEFORE 'iCount'
							// continue;
						}
					}
					else
					{
						// INSERT AT START
						alExpressions.add( iCount++, sExpression );
					}
				}

				seOrthogonal = sd.getDesignTimeSeries( );
				elOrthogonalSeries = seOrthogonal.getDataDefinition( );
				if ( elOrthogonalSeries.isEmpty( ) )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.DATA_BINDING,
							"dataprocessor.exception.DefnExpMustAssociateY", //$NON-NLS-1$
							new Object[]{
									String.valueOf( iCount ), seOrthogonal
							},
							Messages.getResourceBundle( ) );
				}

				bAnyQueries = false;
				for ( int i = 0; i < elOrthogonalSeries.size( ); i++ )
				{
					qOrthogonalSeries = (Query) elOrthogonalSeries.get( i );
					if ( qOrthogonalSeries == null ) // NPE PROTECTION
					{
						continue;
					}

					sExpression = qOrthogonalSeries.getDefinition( );
					if ( sExpression != null
							&& sExpression.trim( ).length( ) > 0 )
					{
						bAnyQueries = true;
						if ( !alExpressions.contains( sExpression ) )
						{
							alExpressions.add( sExpression ); // APPEND AT END
						}
					}
				}

				if ( !bAnyQueries )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.DATA_BINDING,
							"dataprocessor.exception.AtLeastOneDefnExpMustAssociateY", //$NON-NLS-1$
							new Object[]{
									String.valueOf( iCount ), seOrthogonal
							},
							Messages.getResourceBundle( ) );
				}

				// Add orthogonal series trigger expressions.
				String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonal,
						iae );
				if ( triggerExprs != null )
				{
					for ( int t = 0; t < triggerExprs.length; t++ )
					{
						String tgexp = triggerExprs[t];
						if ( !alExpressions.contains( tgexp ) )
						{
							alExpressions.add( tgexp ); // APPEND AT END
						}
					}
				}
			}
		}

		return alExpressions;
	}

	/**
	 * Returns all valid trigger expressions from series.
	 */
	private static String[] getSeriesTriggerExpressions( Series se,
			IActionEvaluator iae )
	{
		ArrayList rt = new ArrayList( );

		if ( se != null && iae != null )
		{
			for ( Iterator itr = se.getTriggers( ).iterator( ); itr.hasNext( ); )
			{
				Trigger tg = (Trigger) itr.next( );

				String[] expra = iae.getActionExpressions( tg.getAction( ),
						StructureSource.createSeries( se ) );

				if ( expra != null && expra.length > 0 )
				{
					for ( int i = 0; i < expra.length; i++ )
					{
						String expr = expra[i];
						if ( expr != null
								&& expr.trim( ).length( ) > 0
								&& !rt.contains( expr ) )
						{
							rt.add( expr );
						}
					}
				}
			}
		}

		if ( rt.size( ) > 0 )
		{
			return (String[]) rt.toArray( new String[rt.size( )] );
		}
		return null;
	}

	private GroupKey[] findGroupKeys( Chart cm )
	{
		if ( cm instanceof ChartWithAxes )
		{
			return findGroupKeys( (ChartWithAxes) cm );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			return findGroupKeys( (ChartWithoutAxes) cm );
		}
		return null;
	}

	private GroupKey[] findGroupKeys( ChartWithoutAxes cwoa )
	{
		final List alKeys = new ArrayList( 4 );
		EList elSD = cwoa.getSeriesDefinitions( );

		// Find all orthogonal group keys in model
		SeriesDefinition sd = (SeriesDefinition) elSD.get( 0 );
		elSD = sd.getSeriesDefinitions( );

		Query qOrthogonalSeriesDefinition;
		String sExpression;

		for ( int i = 0; i < elSD.size( ); i++ )
		{
			sd = (SeriesDefinition) elSD.get( i );
			qOrthogonalSeriesDefinition = sd.getQuery( );
			if ( qOrthogonalSeriesDefinition == null )
			{
				continue;
			}

			sExpression = qOrthogonalSeriesDefinition.getDefinition( );
			if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
			{
				GroupKey sortKey = new GroupKey( sExpression,
						sd.isSetSorting( ) ? sd.getSorting( ) : null );

				if ( !alKeys.contains( sortKey ) )
				{
					alKeys.add( sortKey );
				}
			}
		}

		return (GroupKey[]) alKeys.toArray( new GroupKey[alKeys.size( )] );
	}

	private GroupKey[] findGroupKeys( ChartWithAxes cwa )
	{
		final ArrayList alKeys = new ArrayList( 4 );

		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];

		// Find all orthogonal group keys in model
		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		EList elSD;
		SeriesDefinition sd;
		Query qOrthogonalSeriesDefinition;
		String sExpression;

		for ( int i = 0; i < axaOrthogonal.length; i++ )
		{
			elSD = axaOrthogonal[i].getSeriesDefinitions( );

			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				qOrthogonalSeriesDefinition = sd.getQuery( );
				if ( qOrthogonalSeriesDefinition == null )
				{
					continue;
				}

				sExpression = qOrthogonalSeriesDefinition.getDefinition( );
				if ( sExpression != null && sExpression.trim( ).length( ) > 0 )
				{
					GroupKey sortKey = new GroupKey( sExpression,
							sd.isSetSorting( ) ? sd.getSorting( ) : null );
					if ( !alKeys.contains( sortKey ) )
					{
						alKeys.add( sortKey );
					}
				}
			}
		}

		return (GroupKey[]) alKeys.toArray( new GroupKey[alKeys.size( )] );
	}

	/**
	 * Uses IDataRowExpressionEvaluator to create a ResultSetWrapper
	 * 
	 * @return A wrapper of the chart resultset
	 * 
	 * @throws ChartException
	 */
	protected ResultSetWrapper mapToChartResultSet(
			IDataRowExpressionEvaluator idre, Chart cm ) throws ChartException
	{
		// Collect all used data expressions
		LinkedHashMap lhmLookup = new LinkedHashMap( );
		Collection co = getRowExpressions( cm, iae );
		Iterator it = co.iterator( );
		String sxp;
		int i = 0;
		while ( it.hasNext( ) )
		{
			sxp = (String) it.next( );
			lhmLookup.put( sxp, new Integer( i++ ) );
		}

		// WALK THROUGH RESULTS
		final int iColumnCount = i;
		final List liResultSet = new ArrayList( );
		Object[] oaTuple;
		int iColumnIndex;
		boolean hasFirst = idre.first( );

		if ( hasFirst )
		{
			do
			{
				oaTuple = new Object[iColumnCount];
				it = co.iterator( );
				iColumnIndex = 0;
				while ( it.hasNext( ) )
				{
					oaTuple[iColumnIndex++] = idre.evaluate( (String) it.next( ) );
				}
				liResultSet.add( oaTuple );
			} while ( idre.next( ) );
		}
		// !Don't close evaluator here, let creator close it.
		// idre.close( );

		// Prepare orthogonal grouping keys
		final GroupKey[] groupKeys = findGroupKeys( cm );

		// update key index.
		for ( i = 0; i < groupKeys.length; i++ )
		{
			groupKeys[i].setKeyIndex( ( (Integer) lhmLookup.get( groupKeys[i].getKey( ) ) ).intValue( ) );
		}

		// create resultset wrapper
		final ResultSetWrapper rsw = new ResultSetWrapper( lhmLookup.keySet( ),
				liResultSet,
				groupKeys );

		SeriesDefinition sdGrouping = null;
		String[] saOrthogonalDataDefinitions = null;

		// TODO ??do we need processing trigger expr too?
		// search all orthogonal series data definitions for base grouping
		if ( cm instanceof ChartWithAxes )
		{
			ArrayList alODD = new ArrayList( 8 );
			ChartWithAxes cwa = (ChartWithAxes) cm;
			Axis[] axaBase = cwa.getBaseAxes( );
			Axis[] axaOrthogonal = null;
			Series SE;
			String sExpression;
			EList elSD, elDD;

			// EACH BASE AXIS
			for ( int j = 0; j < axaBase.length; j++ )
			{
				sdGrouping = (SeriesDefinition) axaBase[j].getSeriesDefinitions( )
						.get( 0 );
				axaOrthogonal = cwa.getOrthogonalAxes( axaBase[j], true );

				// EACH ORTHOGONAL AXIS
				for ( i = 0; i < axaOrthogonal.length; i++ )
				{
					elSD = axaOrthogonal[i].getSeriesDefinitions( );
					// EACH ORTHOGONAL SERIES
					for ( int k = 0; k < elSD.size( ); k++ )
					{
						SE = ( (SeriesDefinition) elSD.get( k ) ).getDesignTimeSeries( );

						if ( SE != null )
						{
							elDD = SE.getDataDefinition( );
							// FOR EACH QUERY
							for ( int n = 0; n < elDD.size( ); n++ )
							{
								sExpression = ( (Query) elDD.get( n ) ).getDefinition( );

								if ( sExpression != null
										&& sExpression.trim( ).length( ) > 0
										&& !alODD.contains( sExpression ) )
								{
									// ADD NEW VALID EXPRESSION
									alODD.add( sExpression );
								}
							}
						}
					}
				}
			}

			saOrthogonalDataDefinitions = (String[]) alODD.toArray( new String[alODD.size( )] );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			ArrayList alODD = new ArrayList( 8 );
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			Series SE;
			String sExpression;
			EList elSD, elDD;

			sdGrouping = (SeriesDefinition) cwoa.getSeriesDefinitions( )
					.get( 0 );
			elSD = sdGrouping.getSeriesDefinitions( );

			// EACH ORTHOGONAL SERIES
			for ( int k = 0; k < elSD.size( ); k++ )
			{
				SE = ( (SeriesDefinition) elSD.get( k ) ).getDesignTimeSeries( );

				if ( SE != null )
				{
					elDD = SE.getDataDefinition( );
					// FOR EACH QUERY
					for ( int n = 0; n < elDD.size( ); n++ )
					{
						sExpression = ( (Query) elDD.get( n ) ).getDefinition( );

						if ( sExpression != null
								&& sExpression.trim( ).length( ) > 0
								&& !alODD.contains( sExpression ) )
						{
							// ADD NEW VALID EXPRESSION
							alODD.add( sExpression );
						}
					}
				}
			}

			saOrthogonalDataDefinitions = (String[]) alODD.toArray( new String[alODD.size( )] );
		}

		// apply base series grouping
		rsw.applyBaseSeriesGrouping( sdGrouping, saOrthogonalDataDefinitions );

		return rsw;
	}

	/**
	 * Fills the model chart runtime series with the data
	 * 
	 * @throws ChartException
	 */
	public void generateRuntimeSeries( IDataRowExpressionEvaluator idre,
			Chart cm ) throws ChartException
	{
		ResultSetWrapper rsw = mapToChartResultSet( idre, cm );

		cm.clearSections( IConstants.RUN_TIME );

		if ( cm instanceof ChartWithAxes )
		{
			generateRuntimeSeries( (ChartWithAxes) cm, rsw );
		}
		else if ( cm instanceof ChartWithoutAxes )
		{
			generateRuntimeSeries( (ChartWithoutAxes) cm, rsw );
		}
	}

	private void generateRuntimeSeries( ChartWithoutAxes cwoa,
			ResultSetWrapper rsw ) throws ChartException
	{
		final int iGroupCount = rsw.getGroupCount( );

		// POPULATE THE BASE RUNTIME SERIES
		EList elSD = cwoa.getSeriesDefinitions( );
		final SeriesDefinition sdBase = (SeriesDefinition) elSD.get( 0 );
		final SortOption baseSorting = sdBase.isSetSorting( ) ? sdBase.getSorting( )
				: null;
		final Series seBaseDesignSeries = sdBase.getDesignTimeSeries( );
		final Series seBaseRuntimeSeries = (Series) EcoreUtil.copy( seBaseDesignSeries );
		sdBase.getSeries( ).add( seBaseRuntimeSeries );

		int iOrthogonalSeriesDefinitionCount = 0;
		int iBaseColumnIndex = 0;
		SeriesDefinition sd;
		Query qy;
		String sExpression;

		EList dda = sdBase.getDesignTimeSeries( ).getDataDefinition( );
		if ( dda.size( ) > 0 )
		{
			List columns = getRowExpressions( cwoa, iae );
			iBaseColumnIndex = columns.indexOf( ( (Query) dda.get( 0 ) ).getDefinition( ) );
			if ( iBaseColumnIndex == -1 )
			{
				iBaseColumnIndex = 0;
			}
		}

		elSD = sdBase.getSeriesDefinitions( );
		for ( int j = 0; j < elSD.size( ); j++ )
		{
			sd = (SeriesDefinition) elSD.get( j );
			qy = sd.getQuery( );
			if ( qy == null )
			{
				continue;
			}
			sExpression = qy.getDefinition( );
			if ( sExpression == null || sExpression.length( ) == 0 )
			{
				continue;
			}
			iOrthogonalSeriesDefinitionCount++;
		}

		if ( iOrthogonalSeriesDefinitionCount < 1 )
		{
			fillSeriesDataSet( cwoa,
					seBaseRuntimeSeries,
					rsw.getSubset( iBaseColumnIndex ) );

			// POPULATE ONE ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;
			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH ORTHOGONAL
			// SERIES DEFINITION
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );
				seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );
				seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

				// Retrieve trigger expressions.
				String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
						iae );

				fillSeriesDataSet( cwoa,
						seOrthogonalRuntimeSeries,
						rsw.getSubset( seOrthogonalDesignSeries.getDataDefinition( ) ),
						triggerExprs, // Just use trigger expression as
						// the key.
						rsw.getSubset( triggerExprs ) );
				seOrthogonalRuntimeSeries.setSeriesIdentifier( seOrthogonalDesignSeries.getSeriesIdentifier( ) );
				sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );
			}
		}
		else
		{
			// compute all base values.
			Object[] oa = rsw.getMergedGroupingBaseValues( iBaseColumnIndex,
					baseSorting );

			List baseValues = (List) oa[0];
			List idxList = (List) oa[1];
			final int maxCount = baseValues.size( );

			// populate base series dataset.
			// use max-count group values as the base values.
			Object[] baseData = populateSeriesDataSet( seBaseRuntimeSeries,
					new ResultSetDataSet( baseValues,
							rsw.getColumnDataType( iBaseColumnIndex ) ) );

			// POPULATE ALL ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;

			List orthogonalDataList = new ArrayList( );

			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );
				seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );

				// Retrieve trigger expressions.
				String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
						iae );

				for ( int k = 0; k < iGroupCount; k++ )
				{
					seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

					Object[] odata = populateSeriesDataSet( seOrthogonalRuntimeSeries,
							rsw.getSubset( k,
									seOrthogonalDesignSeries.getDataDefinition( ) ),
							rsw.getSubset( k, triggerExprs ) );

					odata[3] = new Integer( rsw.getGroupRowCount( k ) );
					odata[4] = new Integer( k );
					odata[5] = triggerExprs;

					orthogonalDataList.add( odata );
				}
			}

			Object[] orthogonalData = orthogonalDataList.toArray( );

			// try correct all invalid datasets.
			for ( int i = 0; i < orthogonalData.length; i++ )
			{
				DataSet ds = (DataSet) ( (Object[]) orthogonalData[i] )[0];
				DataSet[] userDs = (DataSet[]) ( (Object[]) orthogonalData[i] )[6];
				int groupIndex = ( (Integer) ( (Object[]) orthogonalData[i] )[4] ).intValue( );

				ds = adjustDataSet( ds,
						maxCount,
						(List) idxList.get( groupIndex ),
						userDs );

				( (Object[]) orthogonalData[i] )[3] = ds;
			}

			// Fill all runtime series dataset after validation.
			// Fill the base series.
			fillSeriesDataSet( (IDataSetProcessor) baseData[1],
					seBaseRuntimeSeries,
					(DataSet) baseData[0] );

			int odx = 0;

			// Fill ALL ORTHOGONAL SERIES
			elSD = sdBase.getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
			// ORTHOGONAL
			// SERIES DEFINITION
			{
				sdOrthogonal = (SeriesDefinition) elSD.get( j );

				for ( int k = 0; k < iGroupCount; k++ ) // FOR
				// EACH
				// ORTHOGONAL
				// RUNTIME SERIES
				{
					Object[] odata = (Object[]) orthogonalData[odx];
					seOrthogonalRuntimeSeries = (Series) odata[2];
					fillSeriesDataSet( (IDataSetProcessor) odata[1],
							seOrthogonalRuntimeSeries,
							(DataSet) odata[0],
							(String[]) odata[5],
							(DataSet[]) odata[6] );

					qy = sdOrthogonal.getQuery( );
					sExpression = ( qy == null ) ? IConstants.UNDEFINED_STRING
							: qy.getDefinition( );
					if ( sExpression == null )
						sExpression = IConstants.UNDEFINED_STRING;
					// TODO format the group key.
					seOrthogonalRuntimeSeries.setSeriesIdentifier( rsw.getGroupKey( k,
							sExpression ) );
					sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );

					odx++;
				}
			}
		}
	}

	private void generateRuntimeSeries( ChartWithAxes cwa,
			ResultSetWrapper rsw ) throws ChartException
	{
		final int iGroupCount = rsw.getGroupCount( );

		// POPULATE THE BASE RUNTIME SERIES
		final Axis axPrimaryBase = cwa.getPrimaryBaseAxes( )[0];
		EList elSD = axPrimaryBase.getSeriesDefinitions( );
		final SeriesDefinition sdBase = (SeriesDefinition) elSD.get( 0 );
		final SortOption baseSorting = sdBase.isSetSorting( ) ? sdBase.getSorting( )
				: null;
		final Series seBaseDesignSeries = sdBase.getDesignTimeSeries( );
		final Series seBaseRuntimeSeries = (Series) EcoreUtil.copy( seBaseDesignSeries );
		sdBase.getSeries( ).add( seBaseRuntimeSeries );

		final Axis[] axaOrthogonal = cwa.getOrthogonalAxes( axPrimaryBase, true );
		int iOrthogonalSeriesDefinitionCount = 0;
		int iBaseColumnIndex = 0;
		SeriesDefinition sd;
		Query qy;
		String sExpression;

		EList dda = sdBase.getDesignTimeSeries( ).getDataDefinition( );
		if ( dda.size( ) > 0 )
		{
			List columns = getRowExpressions( cwa, iae );
			iBaseColumnIndex = columns.indexOf( ( (Query) dda.get( 0 ) ).getDefinition( ) );
			if ( iBaseColumnIndex == -1 )
			{
				iBaseColumnIndex = 0;
			}
		}

		for ( int i = 0; i < axaOrthogonal.length; i++ )
		{
			elSD = axaOrthogonal[i].getSeriesDefinitions( );
			for ( int j = 0; j < elSD.size( ); j++ )
			{
				sd = (SeriesDefinition) elSD.get( j );
				qy = sd.getQuery( );
				if ( qy == null )
				{
					continue;
				}
				sExpression = qy.getDefinition( );
				if ( sExpression == null || sExpression.length( ) == 0 )
				{
					continue;
				}
				iOrthogonalSeriesDefinitionCount++;
			}
		}

		if ( iOrthogonalSeriesDefinitionCount < 1 )
		{
			fillSeriesDataSet( cwa,
					seBaseRuntimeSeries,
					rsw.getSubset( iBaseColumnIndex ) );

			// POPULATE ONE ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;
			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
				// ORTHOGONAL
				// SERIES DEFINITION
				{
					sdOrthogonal = (SeriesDefinition) elSD.get( j );
					seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );
					seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

					// Retrieve trigger expressions.
					String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
							iae );

					// Add trigger to user datasets
					fillSeriesDataSet( cwa,
							seOrthogonalRuntimeSeries,
							rsw.getSubset( seOrthogonalDesignSeries.getDataDefinition( ) ),
							triggerExprs, // Just use trigger expression as
							// the key.
							rsw.getSubset( triggerExprs ) );
					seOrthogonalRuntimeSeries.setSeriesIdentifier( seOrthogonalDesignSeries.getSeriesIdentifier( ) );
					sdOrthogonal.getSeries( ).add( seOrthogonalRuntimeSeries );
				}
			}
		}
		else
		{
			// compute all base values.
			Object[] oa = rsw.getMergedGroupingBaseValues( iBaseColumnIndex,
					baseSorting );

			List baseValues = (List) oa[0];
			List idxList = (List) oa[1];
			final int maxCount = baseValues.size( );

			// populate base series dataset.
			// use max-count group values as the base values.
			Object[] baseData = populateSeriesDataSet( seBaseRuntimeSeries,
					new ResultSetDataSet( baseValues,
							rsw.getColumnDataType( iBaseColumnIndex ) ) );

			// POPULATE ALL ORTHOGONAL SERIES
			Series seOrthogonalDesignSeries;
			Series seOrthogonalRuntimeSeries;
			SeriesDefinition sdOrthogonal;

			List orthogonalDataList = new ArrayList( );

			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ )
				{
					sdOrthogonal = (SeriesDefinition) elSD.get( j );
					seOrthogonalDesignSeries = sdOrthogonal.getDesignTimeSeries( );

					// Retrieve trigger expressions.
					String[] triggerExprs = getSeriesTriggerExpressions( seOrthogonalDesignSeries,
							iae );

					for ( int k = 0; k < iGroupCount; k++ )
					{
						seOrthogonalRuntimeSeries = (Series) EcoreUtil.copy( seOrthogonalDesignSeries );

						Object[] odata = populateSeriesDataSet( seOrthogonalRuntimeSeries,
								rsw.getSubset( k,
										seOrthogonalDesignSeries.getDataDefinition( ) ),
								rsw.getSubset( k, triggerExprs ) );

						odata[3] = new Integer( rsw.getGroupRowCount( k ) );
						odata[4] = new Integer( k );
						odata[5] = triggerExprs;

						orthogonalDataList.add( odata );
					}
				}
			}

			Object[] orthogonalData = orthogonalDataList.toArray( );

			// try correct all invalid datasets.
			for ( int i = 0; i < orthogonalData.length; i++ )
			{
				DataSet ds = (DataSet) ( (Object[]) orthogonalData[i] )[0];
				DataSet[] userDs = (DataSet[]) ( (Object[]) orthogonalData[i] )[6];
				int groupIndex = ( (Integer) ( (Object[]) orthogonalData[i] )[4] ).intValue( );

				ds = adjustDataSet( ds,
						maxCount,
						(List) idxList.get( groupIndex ),
						userDs );

				( (Object[]) orthogonalData[i] )[3] = ds;
			}

			// Fill all runtime series dataset after validation.
			// Fill the base series.
			fillSeriesDataSet( (IDataSetProcessor) baseData[1],
					seBaseRuntimeSeries,
					(DataSet) baseData[0] );

			int odx = 0;

			// Fill ALL ORTHOGONAL SERIES
			for ( int i = 0; i < axaOrthogonal.length; i++ ) // FOR EACH AXIS
			{
				elSD = axaOrthogonal[i].getSeriesDefinitions( );
				for ( int j = 0; j < elSD.size( ); j++ ) // FOR EACH
				// ORTHOGONAL
				// SERIES DEFINITION
				{
					sdOrthogonal = (SeriesDefinition) elSD.get( j );

					for ( int k = 0; k < iGroupCount; k++ ) // FOR
					// EACH
					// ORTHOGONAL
					// RUNTIME SERIES
					{
						Object[] odata = (Object[]) orthogonalData[odx];
						seOrthogonalRuntimeSeries = (Series) odata[2];
						fillSeriesDataSet( (IDataSetProcessor) odata[1],
								seOrthogonalRuntimeSeries,
								(DataSet) odata[0],
								(String[]) odata[5],
								(DataSet[]) odata[6] );

						qy = sdOrthogonal.getQuery( );
						sExpression = ( qy == null ) ? IConstants.UNDEFINED_STRING
								: qy.getDefinition( );
						if ( sExpression == null )
							sExpression = IConstants.UNDEFINED_STRING;
						// TODO format the group key.
						seOrthogonalRuntimeSeries.setSeriesIdentifier( rsw.getGroupKey( k,
								sExpression ) );
						sdOrthogonal.getSeries( )
								.add( seOrthogonalRuntimeSeries );

						odx++;
					}
				}
			}
		}
	}

	private DataSet adjustDataSet( DataSet ds, int maxcount,
			List indexMap, DataSet[] userDs )
	{
		ds = adjustEachDataSet( ds, indexMap );

		if ( userDs != null && userDs.length > 0 )
		{
			for ( int i = 0; i < userDs.length; i++ )
			{
				DataSet usds = adjustEachDataSet( userDs[i], indexMap );
				userDs[i] = usds;
			}
		}

		return ds;
	}

	private DataSet adjustEachDataSet( DataSet ds, List indexMap )
	{
		Collection co;
		double[] da;
		Double[] dda;
		long[] la;
		Calendar[] ca;
		String[] sa;
		Object[] oa;

		int[] indexArray = new int[indexMap.size( )];

		for ( int i = 0; i < indexArray.length; i++ )
		{
			indexArray[i] = ( (Integer) indexMap.get( i ) ).intValue( );
		}

		Object oContent = ds.getValues( );
		if ( ds instanceof NumberDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof double[] )
			{
				da = (double[]) oContent;

				double[] doubleBuffer = new double[indexArray.length];
				Arrays.fill( doubleBuffer, Double.NaN );
				for ( int i = 0; i < da.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						doubleBuffer[idx] = da[i];
					}
				}
				ds.setValues( doubleBuffer );
			}
			else if ( oContent instanceof Double[] )
			{
				dda = (Double[]) oContent;

				Double[] doubleBuffer = new Double[indexArray.length];
				for ( int i = 0; i < dda.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						doubleBuffer[idx] = dda[i];
					}
				}
				ds.setValues( doubleBuffer );
			}
			else if ( oContent instanceof Number )
			{
				da = new double[]{
					( (Number) oContent ).doubleValue( )
				};

				double[] doubleBuffer = new double[indexArray.length];
				Arrays.fill( doubleBuffer, Double.NaN );
				int idx = indexArray[0];
				if ( idx != -1 )
				{
					doubleBuffer[idx] = da[0];
				}
				ds.setValues( doubleBuffer );
			}
		}
		else if ( ds instanceof DateTimeDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof long[] )
			{
				la = (long[]) oContent;

				double[] longBuffer = new double[indexArray.length];
				Arrays.fill( longBuffer, Double.NaN );
				for ( int i = 0; i < la.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						longBuffer[idx] = la[i];
					}
				}
				ds.setValues( longBuffer );
			}
			else if ( oContent instanceof Calendar[] )
			{
				ca = (Calendar[]) oContent;

				Calendar[] calendarBuffer = new Calendar[indexArray.length];
				for ( int i = 0; i < ca.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						calendarBuffer[idx] = ca[i];
					}
				}
				ds.setValues( calendarBuffer );
			}

		}
		else if ( ds instanceof TextDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof String[] )
			{
				sa = (String[]) oContent;

				String[] stringBuffer = new String[indexArray.length];
				for ( int i = 0; i < sa.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						stringBuffer[idx] = sa[i];
					}
				}
				ds.setValues( stringBuffer );
			}
		}
		else if ( ds instanceof StockDataSet )
		{
			if ( oContent instanceof Collection )
			{
				co = (Collection) oContent;

				Object[] objBuffer = new Object[indexArray.length];
				int i = 0;
				for ( Iterator itr = co.iterator( ); itr.hasNext( ); )
				{
					Object o = itr.next( );
					int idx = indexArray[i++];
					if ( idx != -1 )
					{
						objBuffer[idx] = o;
					}
				}

				co.clear( );
				for ( i = 0; i < objBuffer.length; i++ )
				{
					co.add( objBuffer[i] );
				}
			}
			else if ( oContent instanceof Object[] )
			{
				oa = (Object[]) oContent;

				Object[] objectBuffer = new Object[indexArray.length];
				for ( int i = 0; i < oa.length; i++ )
				{
					int idx = indexArray[i];
					if ( idx != -1 )
					{
						objectBuffer[idx] = oa[i];
					}
				}
				ds.setValues( objectBuffer );
			}
		}

		return ds;
	}

	/**
	 * Populates the runtime dataset.
	 * 
	 * @param seRuntime
	 * @param rsds
	 * @return the returned object array contains [DataSet, IDataSetProcessor,
	 *         RuntimeSeries, GroupRowCount, GroupIndex, UserKeys,
	 *         UserDataSets].
	 * @throws ChartException
	 */
	private Object[] populateSeriesDataSet( Series seRuntime,
			IResultSetDataSet rsds ) throws ChartException
	{
		return populateSeriesDataSet( seRuntime, rsds, null );
	}

	/**
	 * Populates the runtime dataset.
	 * 
	 * @param seRuntime
	 * @param rsds
	 * @return the returned object array contains [DataSet, IDataSetProcessor,
	 *         RuntimeSeries, GroupRowCount, GroupIndex, UserKeys,
	 *         UserDataSets].
	 * @throws ChartException
	 */
	private Object[] populateSeriesDataSet( Series seRuntime,
			IResultSetDataSet rsds, IResultSetDataSet userRsds )
			throws ChartException
	{
		IDataSetProcessor idsp = null;
		try
		{
			idsp = PluginSettings.instance( )
					.getDataSetProcessor( seRuntime.getClass( ) );
		}
		catch ( ChartException pex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					pex );
		}

		DataSet ds = null;

		ds = idsp.populate( rsds, null );

		DataSet[] usds = null;
		if ( userRsds != null )
		{
			// process user dataset.
			UserDataSetProcessor tdsp = new UserDataSetProcessor( );
			usds = tdsp.populate( userRsds );
		}

		return new Object[]{
				ds, idsp, seRuntime, null, null, null, usds
		};
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( IDataSetProcessor idsp,
			Series seRuntime, DataSet ds ) throws ChartException
	{
		fillSeriesDataSet( idsp, seRuntime, ds, null, null );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( IDataSetProcessor idsp,
			Series seRuntime, DataSet ds, String[] userKeys, DataSet[] userDs )
			throws ChartException
	{
		final ScriptHandler sh = rtc.getScriptHandler( );

		ScriptHandler.callFunction( sh,
				ScriptHandler.BEFORE_DATA_SET_FILLED,
				seRuntime,
				idsp,
				rtc.getScriptContext( ) );

		seRuntime.setDataSet( ds );

		if ( userDs != null && userKeys != null )
		{
			// process user dataset.
			for ( int i = 0; i < Math.min( userDs.length, userKeys.length ); i++ )
			{
				seRuntime.setDataSet( userKeys[i], userDs[i] );
			}
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.AFTER_DATA_SET_FILLED,
				seRuntime,
				ds,
				rtc.getScriptContext( ) );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( Chart cm, Series seRuntime,
			IResultSetDataSet rsds ) throws ChartException
	{
		fillSeriesDataSet( cm, seRuntime, rsds, null, null );
	}

	/**
	 * Fill series with populated and adjusted dataset.
	 */
	private void fillSeriesDataSet( Chart cm, Series seRuntime,
			IResultSetDataSet rsds, String[] userKeys,
			IResultSetDataSet userRsds ) throws ChartException
	{
		final ScriptHandler sh = rtc.getScriptHandler( );
		IDataSetProcessor idsp = null;
		try
		{
			idsp = PluginSettings.instance( )
					.getDataSetProcessor( seRuntime.getClass( ) );
		}
		catch ( ChartException pex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.DATA_BINDING,
					pex );
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.BEFORE_DATA_SET_FILLED,
				seRuntime,
				idsp,
				rtc.getScriptContext( ) );

		DataSet ds = null;
		ds = idsp.populate( rsds, null );

		seRuntime.setDataSet( ds );

		if ( userRsds != null && userKeys != null )
		{
			// process user dataset.
			UserDataSetProcessor tdsp = new UserDataSetProcessor( );
			DataSet[] usds = tdsp.populate( userRsds );

			for ( int i = 0; i < Math.min( usds.length, userKeys.length ); i++ )
			{
				seRuntime.setDataSet( userKeys[i], usds[i] );
			}
		}

		ScriptHandler.callFunction( sh,
				ScriptHandler.AFTER_DATA_SET_FILLED,
				seRuntime,
				ds,
				rtc.getScriptContext( ) );
	}

}
