/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.NumberDataElement;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.impl.NumberDataElementImpl;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.Collator;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 * Wraps an implementation specific host resultset into a chart specific
 * resultset that may be subsequently bound to individual series associated with
 * a chart instance.
 */
public final class ResultSetWrapper
{

	/**
	 * An internally maintained list containing all rows of resultset data
	 */
	final List liResultSet;

	/**
	 * The column expressions associated with the resultset
	 */
	final String[] saExpressionKeys;

	/**
	 * The data types associated with each column in the resultset
	 */
	final int[] iaDataTypes;

	/**
	 * The group breaks associated with all rows of data
	 */
	private int[] iaGroupBreaks = null;

	/**
	 * A lookup table internally used to locate a numeric column index using the
	 * associated expression
	 */
	private final Hashtable htLookup;

	/**
	 * Indicates whether series grouping was applied on the resultset to prevent
	 * duplicate grouping.
	 */
	private boolean bGroupingApplied = false;

	/**
	 * A reusable instance that indicates no group breaks
	 */
	private static final int[] NO_GROUP_BREAKS = new int[0];

	/**
	 * flag indicates if group used.
	 */
	private final boolean bGrouped;

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/datafeed" ); //$NON-NLS-1$

	/**
	 * The default constructor that allows creation of a resultset wrapper
	 * 
	 * @param stExpressionKeys
	 *            The set of expressions associated with each column in the
	 *            resultset
	 * @param liResultSet
	 *            A list of rows that represent the actual resultset data
	 *            content. Each row contains an Object[]
	 */
	public ResultSetWrapper( Set stExpressionKeys, List liResultSet,
			boolean bGrouped )
	{
		this.liResultSet = liResultSet;
		saExpressionKeys = (String[]) stExpressionKeys.toArray( new String[0] );
		iaDataTypes = new int[saExpressionKeys.length];
		setup( );
		htLookup = new Hashtable( );
		for ( int i = 0; i < saExpressionKeys.length; i++ )
		{
			htLookup.put( saExpressionKeys[i], new Integer( i ) );
		}
		iaGroupBreaks = findGroupBreaks( bGrouped );
		this.bGrouped = bGrouped;
	}

	/**
	 * Internally called to setup the structure of the resultset and initialize
	 * any metadata associated with it
	 */
	private final void setup( )
	{
		final Iterator it = liResultSet.iterator( );
		Object[] oaTuple;
		int iColumnCount = iaDataTypes.length;
		boolean[] boaFound = new boolean[iColumnCount];
		boolean bAllDone;
		while ( it.hasNext( ) )
		{
			oaTuple = (Object[]) it.next( );
			for ( int i = 0; i < iColumnCount; i++ )
			{
				bAllDone = true;
				if ( oaTuple[i] == null )
				{
					continue;
				}

				boaFound[i] = true;
				if ( oaTuple[i] instanceof Number ) // DYNAMICALLY DETERMINE
				// DATA TYPE
				{
					iaDataTypes[i] = IConstants.NUMERICAL;
				}
				else if ( oaTuple[i] instanceof String ) // DYNAMICALLY
				// DETERMINE DATA TYPE
				{
					iaDataTypes[i] = IConstants.TEXT;
				}
				else if ( oaTuple[i] instanceof Date
						|| oaTuple[i] instanceof Calendar ) // DYNAMICALLY
				// DETERMINE DATA
				// TYPE
				{
					iaDataTypes[i] = IConstants.DATE_TIME;
				}

				for ( int j = 0; j < iColumnCount; j++ )
				{
					if ( !boaFound[j] )
					{
						bAllDone = false;
						break;
					}
				}
				if ( bAllDone )
				{
					return;
				}
			}
		}
		logger.log( ILogger.ERROR,
				Messages.getString( "exception.resultset.data.type.retrieval.failed" //$NON-NLS-1$ 
				) );
	}

	/**
	 * Groups rows of data as specified in the grouping criteria for the series
	 * definition
	 * 
	 * @param sd
	 * @param sExpressionKey
	 * 
	 * @throws GenerationException
	 */
	public final void applySeriesGrouping( SeriesDefinition sd,
			String[] saExpressionKeys ) throws ChartException
	{
		// PREVENT REDUNDANT GROUPING
		if ( bGroupingApplied )
		{
			return;
		}
		bGroupingApplied = true;

		// VALIDATE SERIES GROUPING
		final SeriesGrouping sg = sd.getGrouping( );
		if ( sg == null )
		{
			return;
		}
		if ( !sg.isEnabled( ) )
		{
			return;
		}

		// LOOKUP AGGREGATE FUNCTION
		final String sFunctionName = sg.getAggregateExpression( );
		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		IAggregateFunction[] iafa = new IAggregateFunction[iOrthogonalSeriesCount];
		try
		{
			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				iafa[i] = PluginSettings.instance( )
						.getAggregateFunction( sFunctionName );
				iafa[i].initialize( );
			}
		}
		catch ( ChartException pex )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.GENERATION,
					pex );
		}

		// final int iGroupingInterval = sg.getGroupingInterval( );
		final DataType dtGrouping = sg.getGroupType( );
		if ( dtGrouping == DataType.NUMERIC_LITERAL )
		{
			// IDENTIFY THE MIN AND MAX IN THE DATASET
			final Series seBaseDesignTime = sd.getDesignTimeSeries( );
			final Query q = (Query) seBaseDesignTime.getDataDefinition( )
					.get( 0 );
			final int iSortColumnIndex = ( (Integer) htLookup.get( q.getDefinition( ) ) ).intValue( );

			SortOption so = null;
			if ( !sd.isSetSorting( ) )
			{
				logger.log( ILogger.WARNING,
						Messages.getString( "warn.unspecified.sorting", //$NON-NLS-1$
								new Object[]{
									sd
								},
								ULocale.getDefault( ) ) );
				so = SortOption.ASCENDING_LITERAL;
			}
			else
			{
				so = sd.getSorting( );
			}
			groupNumerically( liResultSet,
					iSortColumnIndex,
					so,
					saExpressionKeys,
					null,
					sg.getGroupingInterval( ),
					iafa );
		}
		else if ( dtGrouping == DataType.DATE_TIME_LITERAL )
		{
			// IDENTIFY THE MIN AND MAX IN THE DATASET
			final Series seBaseDesignTime = sd.getDesignTimeSeries( );
			final Query q = (Query) seBaseDesignTime.getDataDefinition( )
					.get( 0 );
			final int iSortColumnIndex = ( (Integer) htLookup.get( q.getDefinition( ) ) ).intValue( );

			SortOption so = null;
			if ( !sd.isSetSorting( ) )
			{
				logger.log( ILogger.WARNING,
						Messages.getString( "warn.unspecified.sorting", //$NON-NLS-1$
								new Object[]{
									sd
								},
								ULocale.getDefault( ) ) );
				so = SortOption.ASCENDING_LITERAL;
			}
			else
			{
				so = sd.getSorting( );
			}
			groupDateTime( liResultSet,
					iSortColumnIndex,
					so,
					saExpressionKeys,
					null,
					sg.getGroupingInterval( ),
					sg.getGroupingUnit( ),
					iafa );
		}
		else if ( dtGrouping == DataType.TEXT_LITERAL )
		{
			// IDENTIFY THE MIN AND MAX IN THE DATASET
			final Series seBaseDesignTime = sd.getDesignTimeSeries( );
			final Query q = (Query) seBaseDesignTime.getDataDefinition( )
					.get( 0 );
			final int iSortColumnIndex = ( (Integer) htLookup.get( q.getDefinition( ) ) ).intValue( );

			SortOption so = null;
			if ( !sd.isSetSorting( ) )
			{
				logger.log( ILogger.WARNING,
						Messages.getString( "warn.unspecified.sorting", //$NON-NLS-1$
								new Object[]{
									sd
								},
								ULocale.getDefault( ) ) );
				so = SortOption.ASCENDING_LITERAL;
			}
			else
			{
				so = sd.getSorting( );
			}

			groupTextually( liResultSet,
					iSortColumnIndex,
					so,
					saExpressionKeys,
					null,
					sg.getGroupingInterval( ),
					iafa );

		}

		// RE-COMPUTE GROUP BREAKS
		iaGroupBreaks = findGroupBreaks( bGrouped );
	}

	/**
	 * @param iSortColumnIndex
	 * @param so
	 * @param saExpressionKeys
	 * @param ndeBaseReference
	 * @param iGroupingInterval
	 * @param iafa
	 * 
	 * @throws GenerationException
	 */
	private final void groupNumerically( List resultSet, int iSortColumnIndex,
			SortOption so, String[] saExpressionKeys,
			NumberDataElement ndeBaseReference, long iGroupingInterval,
			IAggregateFunction[] iafa ) throws ChartException
	{
		Collections.sort( resultSet, new GroupingComparator( iSortColumnIndex,
				so ) );
		if ( ndeBaseReference == null )
		{
			Number obj = (Number) ( (Object[]) resultSet.get( 0 ) )[iSortColumnIndex];

			// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
			ndeBaseReference = NumberDataElementImpl.create( obj == null ? 0
					: obj.doubleValue( ) );
		}
		final Iterator it = resultSet.iterator( );
		Object[] oaTuple, oaSummarizedTuple = null;
		int iGroupIndex = 0, iLastGroupIndex = 0;
		boolean bFirst = true, bGroupBreak = false;
		double dBaseReference = ndeBaseReference.getValue( );

		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		final int[] iaColumnIndexes = new int[iOrthogonalSeriesCount];
		for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
		{
			iaColumnIndexes[i] = ( (Integer) htLookup.get( saExpressionKeys[i] ) ).intValue( );
		}

		while ( it.hasNext( ) )
		{
			oaTuple = (Object[]) it.next( );

			if ( oaTuple[iSortColumnIndex] != null )
			{
				if ( iGroupingInterval == 0 )
				{
					iGroupIndex++;
				}
				else
				{
					iGroupIndex = (int) Math.floor( Math.abs( ( ( (Number) oaTuple[iSortColumnIndex] ).doubleValue( ) - dBaseReference )
							/ iGroupingInterval ) );
				}
			}
			else
			{
				if ( iGroupingInterval == 0 )
				{
					iGroupIndex++;
				}
				else
				{
					// Treat null value as 0.
					iGroupIndex = (int) Math.floor( Math.abs( dBaseReference
							/ iGroupingInterval ) );
				}
			}

			if ( !bFirst )
			{
				bGroupBreak = ( iLastGroupIndex != iGroupIndex );
			}

			if ( bGroupBreak || bFirst )
			{
				if ( oaSummarizedTuple != null ) // FIRST ROW IN GROUP
				{
					for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
					{
						oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
						iafa[i].initialize( ); // RESET
					}
					// KEEP base value as the axis label.
					// oaSummarizedTuple[iSortColumnIndex] = new Double(
					// iLastGroupIndex );
				}
				else
				// FIRST ROW IN RS
				{
					bFirst = false;
				}
				oaSummarizedTuple = oaTuple;
			}
			else
			{
				it.remove( );
			}

			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				try
				{
					iafa[i].accumulate( oaTuple[iaColumnIndexes[i]] );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
			}
			iLastGroupIndex = iGroupIndex;
		}

		if ( oaSummarizedTuple != null ) // LAST ROW IN GROUP
		{
			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
				// iafa[i].initialize(); // DO NOT NEED TO RESET ANYMORE
			}
			// KEEP base value as the axis label.
			// oaSummarizedTuple[iSortColumnIndex] = new Double( iLastGroupIndex
			// );
		}

	}

	private int groupingUnit2CDateUnit( GroupingUnitType unit )
	{
		if ( unit != null )
		{
			switch ( unit.getValue( ) )
			{
				case GroupingUnitType.SECONDS :
					return Calendar.SECOND;
				case GroupingUnitType.MINUTES :
					return Calendar.MINUTE;
				case GroupingUnitType.HOURS :
					return Calendar.HOUR_OF_DAY;
				case GroupingUnitType.DAYS :
					return Calendar.DATE;
				case GroupingUnitType.WEEKS :
					return Calendar.WEEK_OF_YEAR;
				case GroupingUnitType.MONTHS :
					return Calendar.MONTH;
				case GroupingUnitType.YEARS :
					return Calendar.YEAR;
			}
		}

		return Calendar.MILLISECOND;
	}

	/**
	 * @param iSortColumnIndex
	 * @param so
	 * @param saExpressionKeys
	 * @param ndeBaseReference
	 * @param iGroupingInterval
	 * @param iafa
	 * 
	 * @throws GenerationException
	 */
	private final void groupDateTime( List resultSet, int iSortColumnIndex,
			SortOption so, String[] saExpressionKeys,
			CDateTime ndeBaseReference, long iGroupingInterval,
			GroupingUnitType groupingUnit, IAggregateFunction[] iafa )
			throws ChartException
	{
		Collections.sort( resultSet, new GroupingComparator( iSortColumnIndex,
				so ) );
		if ( ndeBaseReference == null )
		{
			Object obj = ( (Object[]) resultSet.get( 0 ) )[iSortColumnIndex];

			// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
			if ( obj instanceof CDateTime )
			{
				ndeBaseReference = (CDateTime) obj;
			}
			else if ( obj instanceof Calendar )
			{
				ndeBaseReference = new CDateTime( (Calendar) obj );
			}
			else if ( obj instanceof Date )
			{
				ndeBaseReference = new CDateTime( (Date) obj );
			}
			else
			{
				// set as the smallest Date.
				ndeBaseReference = new CDateTime( 0 );
			}
		}

		final Iterator it = resultSet.iterator( );
		Object[] oaTuple, oaSummarizedTuple = null;
		int iGroupIndex = 0, iLastGroupIndex = 0;
		boolean bFirst = true, bGroupBreak = false;
		CDateTime dBaseReference = ndeBaseReference;

		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		final int[] iaColumnIndexes = new int[iOrthogonalSeriesCount];
		for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
		{
			iaColumnIndexes[i] = ( (Integer) htLookup.get( saExpressionKeys[i] ) ).intValue( );
		}

		int cunit = groupingUnit2CDateUnit( groupingUnit );

		while ( it.hasNext( ) )
		{
			oaTuple = (Object[]) it.next( );

			if ( oaTuple[iSortColumnIndex] != null )
			{
				if ( iGroupingInterval == 0 )
				{
					iGroupIndex++;
				}
				else
				{
					CDateTime dBaseValue = null;

					Object obj = oaTuple[iSortColumnIndex];

					// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
					if ( obj instanceof CDateTime )
					{
						dBaseValue = (CDateTime) obj;
					}
					else if ( obj instanceof Calendar )
					{
						dBaseValue = new CDateTime( (Calendar) obj );
					}
					else if ( obj instanceof Date )
					{
						dBaseValue = new CDateTime( (Date) obj );
					}
					else
					{
						dBaseValue = new CDateTime( );
					}

					double diff = CDateTime.computeDifference( dBaseValue,
							dBaseReference,
							cunit );

					iGroupIndex = (int) Math.floor( Math.abs( diff
							/ iGroupingInterval ) );
				}
			}
			else
			{
				if ( iGroupingInterval == 0 )
				{
					iGroupIndex++;
				}
				else
				{
					// Treat null value as the smallest date.
					CDateTime dBaseValue = new CDateTime( 0 );

					double diff = CDateTime.computeDifference( dBaseValue,
							dBaseReference,
							cunit );

					iGroupIndex = (int) Math.floor( Math.abs( diff
							/ iGroupingInterval ) );
				}
			}

			if ( !bFirst )
			{
				bGroupBreak = ( iLastGroupIndex != iGroupIndex );
			}

			if ( bGroupBreak || bFirst )
			{
				if ( oaSummarizedTuple != null ) // FIRST ROW IN GROUP
				{
					for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
					{
						oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
						iafa[i].initialize( ); // RESET
					}
					// KEEP base value as the axis label.
					// oaSummarizedTuple[iSortColumnIndex] = new Double(
					// iLastGroupIndex );
				}
				else
				// FIRST ROW IN RS
				{
					bFirst = false;
				}
				oaSummarizedTuple = oaTuple;
			}
			else
			{
				it.remove( );
			}

			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				try
				{
					iafa[i].accumulate( oaTuple[iaColumnIndexes[i]] );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
			}
			iLastGroupIndex = iGroupIndex;
		}

		if ( oaSummarizedTuple != null ) // LAST ROW IN GROUP
		{
			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
				// iafa[i].initialize(); // DO NOT NEED TO RESET ANYMORE
			}
			// KEEP base value as the axis label.
			// oaSummarizedTuple[iSortColumnIndex] = new Double( iLastGroupIndex
			// );
		}

	}

	private final void groupTextually( List resultSet, int iSortColumnIndex,
			SortOption so, String[] saExpressionKeys, String ndeBaseReference,
			long iGroupingInterval, IAggregateFunction[] iafa )
			throws ChartException
	{
		Collections.sort( resultSet, new GroupingComparator( iSortColumnIndex,
				so ) );
		if ( ndeBaseReference == null )
		{
			// ASSIGN IT TO THE FIRST TYPLE'S GROUP EXPR VALUE
			ndeBaseReference = ChartUtil.stringValue( ( (Object[]) resultSet.get( 0 ) )[iSortColumnIndex] );
		}
		final Iterator it = resultSet.iterator( );
		Object[] oaTuple, oaSummarizedTuple = null;
		int iGroupIndex = 0, iLastGroupIndex = 0, iGroupCounter = 0;
		boolean bFirst = true, bGroupBreak = false;
		String dBaseReference = ndeBaseReference;

		final int iOrthogonalSeriesCount = saExpressionKeys.length;
		final int[] iaColumnIndexes = new int[iOrthogonalSeriesCount];
		for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
		{
			iaColumnIndexes[i] = ( (Integer) htLookup.get( saExpressionKeys[i] ) ).intValue( );
		}

		while ( it.hasNext( ) )
		{
			oaTuple = (Object[]) it.next( );

			if ( oaTuple[iSortColumnIndex] != null )
			{
				String dBaseValue = String.valueOf( oaTuple[iSortColumnIndex] );

				if ( !dBaseValue.equals( dBaseReference ) )
				{
					iGroupCounter++;
					dBaseReference = dBaseValue;
				}

				if ( iGroupCounter > iGroupingInterval )
				{
					iGroupIndex++;
				}
			}
			else
			{
				// current value is null, check last value.
				if ( dBaseReference != null )
				{
					iGroupCounter++;
					dBaseReference = null;
				}

				if ( iGroupCounter > iGroupingInterval )
				{
					iGroupIndex++;
				}
			}

			if ( !bFirst )
			{
				bGroupBreak = ( iLastGroupIndex != iGroupIndex );
			}

			if ( bGroupBreak )
			{
				// reset group counter
				iGroupCounter = 0;
			}

			if ( bGroupBreak || bFirst )
			{
				if ( oaSummarizedTuple != null ) // FIRST ROW IN GROUP
				{
					for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
					{
						oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
						iafa[i].initialize( ); // RESET
					}
					// KEEP base value as the axis label.
					// oaSummarizedTuple[iSortColumnIndex] = new Double(
					// iLastGroupIndex );
				}
				else
				// FIRST ROW IN RS
				{
					bFirst = false;
				}
				oaSummarizedTuple = oaTuple;
			}
			else
			{
				it.remove( );
			}

			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				try
				{
					iafa[i].accumulate( oaTuple[iaColumnIndexes[i]] );
				}
				catch ( IllegalArgumentException uiex )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.GENERATION,
							uiex );
				}
			}
			iLastGroupIndex = iGroupIndex;
		}

		if ( oaSummarizedTuple != null ) // LAST ROW IN GROUP
		{
			for ( int i = 0; i < iOrthogonalSeriesCount; i++ )
			{
				oaSummarizedTuple[iaColumnIndexes[i]] = iafa[i].getAggregatedValue( );
				// iafa[i].initialize(); // DO NOT NEED TO RESET ANYMORE
			}
			// KEEP base value as the axis label.
			// oaSummarizedTuple[iSortColumnIndex] = new Double( iLastGroupIndex
			// );
		}

	}

	/**
	 * GroupingComparator
	 */
	private static final class GroupingComparator implements Comparator
	{

		private final int iSortKey;
		private final boolean ascending;
		private final Collator collator;

		/**
		 * @param iSortKey
		 * @param so
		 */
		GroupingComparator( int iSortKey, SortOption so )
		{
			this.iSortKey = iSortKey;
			this.ascending = ( so == null || so == SortOption.ASCENDING_LITERAL );
			this.collator = Collator.getInstance( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public final int compare( Object o1, Object o2 )
		{
			final Object[] oaTuple1 = (Object[]) o1;
			final Object[] oaTuple2 = (Object[]) o2;
			final Object oC1 = oaTuple1[iSortKey];
			final Object oC2 = oaTuple2[iSortKey];

			if ( oC1 == null && oC2 == null )
			{
				return 0;
			}
			if ( oC1 == null && oC2 != null )
			{
				return ascending ? -1 : 1;
			}
			if ( oC1 != null && oC2 == null )
			{
				return ascending ? 1 : -1;
			}

			int ct;
			if ( oC1 instanceof String )
			{
				ct = collator.compare( oC1.toString( ), oC2.toString( ) );
			}
			else
			{
				ct = ( (Comparable) oC1 ).compareTo( oC2 );
			}

			return ascending ? ct : -ct;
		}
	}

	/**
	 * Returns a pre-computed group count associated with the resultset wrapper
	 * instance
	 * 
	 * @return A pre-computed group count associated with the resultset wrapper
	 *         instance
	 */
	public final int getGroupCount( )
	{
		return iaGroupBreaks.length + 1;
	}

	/**
	 * Returns the row count in specified group.
	 * 
	 * @param iGroupIndex
	 * @return
	 */
	public final int getGroupRowCount( int iGroupIndex )
	{
		int startRow = ( iGroupIndex <= 0 ) ? 0
				: iaGroupBreaks[iGroupIndex - 1];
		int endRow = ( iGroupIndex > iaGroupBreaks.length - 1 ) ? getRowCount( )
				: iaGroupBreaks[iGroupIndex];

		return endRow - startRow;
	}

	/**
	 * Returns a pre-computed column count associated with the resultset wrapper
	 * instance
	 * 
	 * @return A pre-computed column count associated with the resultset wrapper
	 *         instance
	 */
	public final int getColumnCount( )
	{
		return saExpressionKeys.length;
	}

	/**
	 * Returns the number of rows of data associated with the resultset wrapper
	 * instance
	 * 
	 * @return The number of rows of data associated with the resultset wrapper
	 *         instance
	 */
	public final int getRowCount( )
	{
		return liResultSet.size( );
	}

	/**
	 * Extracts the group's key value that remains unchanged for a given group
	 * 
	 * @param iGroupIndex
	 *            The group index for which the key is requested
	 * @param sExpressionKey
	 *            The expression column that holds the group key value
	 * 
	 * @return The group key value associated with the requested group index
	 */
	public final Object getGroupKey( int iGroupIndex, String sExpressionKey )
	{
		if ( !htLookup.containsKey( sExpressionKey ) )
		{
			return IConstants.UNDEFINED_STRING;
		}
		final int iColumnIndex = ( (Integer) htLookup.get( sExpressionKey ) ).intValue( );
		final int iRow = ( iGroupIndex <= 0 ) ? 0
				: iaGroupBreaks[iGroupIndex - 1];
		Iterator it = liResultSet.iterator( );
		int i = 0;
		while ( it.hasNext( ) && i++ < iRow )
		{
			it.next( );
		}
		if ( it.hasNext( ) )
		{
			return ( (Object[]) it.next( ) )[iColumnIndex];
		}
		return null; // THERE WAS NO DATA
	}

	/**
	 * Extracts the group's key value that remains unchanged for a given group
	 * 
	 * @param iGroupIndex
	 *            The group index for which the key is requested
	 * @param iColumnIndex
	 *            The column index from which the group key value is to be
	 *            extracted
	 * 
	 * @return The group key value associated with the requested group index
	 */
	public final Object getGroupKey( int iGroupIndex, int iColumnIndex )
	{
		final int iRow = ( iGroupIndex <= 0 ) ? 0
				: iaGroupBreaks[iGroupIndex - 1];
		Iterator it = liResultSet.iterator( );
		int i = 0;
		while ( it.hasNext( ) && i++ < iRow )
		{
			it.next( );
		}
		if ( it.hasNext( ) )
		{
			return ( (Object[]) it.next( ) )[iColumnIndex];
		}
		return null; // THERE WAS NO DATA
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param iGroupIndex
	 *            The group number for which a subset is requested
	 * @param sExpressionKey
	 *            A single expression column for which a subset is requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public final ResultSetDataSet getSubset( int iGroupIndex,
			String sExpressionKey )
	{
		return new ResultSetDataSet( this,
				new int[]{
					( (Integer) htLookup.get( sExpressionKey ) ).intValue( )
				},
				( iGroupIndex <= 0 ) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				( iGroupIndex >= iaGroupBreaks.length - 1 ) ? getRowCount( )
						: iaGroupBreaks[iGroupIndex] );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param iGroupIndex
	 *            The group number for which a subset is requested
	 * @param elExpressionKeys
	 *            The expression columns for which a subset is requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public final ResultSetDataSet getSubset( int iGroupIndex,
			EList elExpressionKeys )
	{
		final int n = elExpressionKeys.size( );
		final int[] iaColumnIndexes = new int[n];
		String sExpressionKey;
		for ( int i = 0; i < n; i++ )
		{
			sExpressionKey = ( (Query) elExpressionKeys.get( i ) ).getDefinition( );
			iaColumnIndexes[i] = ( (Integer) htLookup.get( sExpressionKey ) ).intValue( );
		}

		return new ResultSetDataSet( this,
				iaColumnIndexes,
				( iGroupIndex <= 0 ) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				( iGroupIndex > iaGroupBreaks.length - 1 ) ? getRowCount( )
						: iaGroupBreaks[iGroupIndex] );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param iGroupIndex
	 *            The group number for which a subset is requested
	 * @param sExpressionKeys
	 *            The expression columns for which a subset is requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public final ResultSetDataSet getSubset( int iGroupIndex,
			String[] sExpressionKeys )
	{
		if ( sExpressionKeys == null )
		{
			return null;
		}

		final int n = sExpressionKeys.length;
		final int[] iaColumnIndexes = new int[n];
		String sExpressionKey;
		for ( int i = 0; i < n; i++ )
		{
			sExpressionKey = sExpressionKeys[i];
			iaColumnIndexes[i] = ( (Integer) htLookup.get( sExpressionKey ) ).intValue( );
		}

		return new ResultSetDataSet( this,
				iaColumnIndexes,
				( iGroupIndex <= 0 ) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				( iGroupIndex > iaGroupBreaks.length - 1 ) ? getRowCount( )
						: iaGroupBreaks[iGroupIndex] );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param iGroupIndex
	 *            The group number for which a subset is requested
	 * @param iColumnIndex
	 *            A single column (defined by the index) for which the subset is
	 *            requested
	 * 
	 * @return An instance of the resultset subset
	 */
	public final ResultSetDataSet getSubset( int iGroupIndex, int iColumnIndex )
	{
		return new ResultSetDataSet( this,
				new int[]{
					iColumnIndex
				},
				( iGroupIndex <= 0 ) ? 0 : iaGroupBreaks[iGroupIndex - 1],
				( iGroupIndex >= iaGroupBreaks.length ) ? getRowCount( )
						: iaGroupBreaks[iGroupIndex] );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param elExpressions
	 *            The expression columns for which a resultset subset is being
	 *            requested
	 * 
	 * @return The resultset subset containing the requested columns and all
	 *         rows of the resultset
	 */
	public final ResultSetDataSet getSubset( EList elExpressions )
			throws ChartException
	{
		final int n = elExpressions.size( );
		final int[] iaColumnIndexes = new int[n];
		String sExpression;
		try
		{
			for ( int i = 0; i < n; i++ )
			{
				sExpression = ( (Query) elExpressions.get( i ) ).getDefinition( );
				iaColumnIndexes[i] = ( (Integer) htLookup.get( sExpression ) ).intValue( );
			}
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		return new ResultSetDataSet( this, iaColumnIndexes, 0, getRowCount( ) );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param sExpressionKey
	 *            A single expression column for which a resultset subset is
	 *            being requested
	 * 
	 * @return The resultset subset containing the requested column and all rows
	 *         of the resultset
	 */
	public final ResultSetDataSet getSubset( String sExpressionKey )
	{
		return new ResultSetDataSet( this, new int[]{
			( (Integer) htLookup.get( sExpressionKey ) ).intValue( )
		}, 0, getRowCount( ) );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param sExpressionKeys
	 *            The expression columns for which a resultset subset is being
	 *            requested
	 * 
	 * @return The resultset subset containing the requested columns and all
	 *         rows of the resultset
	 */
	public final ResultSetDataSet getSubset( String[] sExpressionKeys )
			throws ChartException
	{
		if ( sExpressionKeys == null )
		{
			return null;
		}

		final int n = sExpressionKeys.length;
		final int[] iaColumnIndexes = new int[n];
		String sExpression;
		try
		{
			for ( int i = 0; i < n; i++ )
			{
				sExpression = sExpressionKeys[i];
				iaColumnIndexes[i] = ( (Integer) htLookup.get( sExpression ) ).intValue( );
			}
		}
		catch ( Exception e )
		{
			throw new ChartException( ChartEnginePlugin.ID,
					ChartException.VALIDATION,
					"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
					Messages.getResourceBundle( ) );
		}

		return new ResultSetDataSet( this, iaColumnIndexes, 0, getRowCount( ) );
	}

	/**
	 * Creates an instance of a resultset subset that uses references to
	 * dynamically compute a subset of the original resultset instance rather
	 * than duplicate a copy of the original resultset data content
	 * 
	 * @param iColumnIndex
	 *            A single column for which a resultset subset is being
	 *            requested
	 * 
	 * @return The resultset subset containing the requested column (specified
	 *         by index) and all rows of the resultset
	 */
	public final ResultSetDataSet getSubset( int iColumnIndex )
	{
		return new ResultSetDataSet( this, new int[]{
			iColumnIndex
		}, 0, getRowCount( ) );
	}

	/**
	 * Returns the values for given column and compute index arrays.
	 * 
	 * @param iColumnIndex
	 * @return an array have two return objects, first is the base value list,
	 *         second is the index map for all grouped subset.
	 */
	public final Object[] getMergedGroupingBaseValues( int iColumnIndex,
			SortOption sorting )
	{
		int groupCount = getGroupCount( );

		final List idxList = new ArrayList( groupCount );

		Object oValue;
		ResultSetDataSet rsd;

		final List baseValue = new ArrayList( );
		List idx;

		for ( int k = 0; k < groupCount; k++ )
		{
			rsd = getSubset( k, iColumnIndex );

			idx = new ArrayList( );

			if ( k == 0 )
			{
				// if it's the first group, just add all values.
				int i = 0;
				while ( rsd.hasNext( ) )
				{
					oValue = rsd.next( )[0];

					baseValue.add( oValue );
					idx.add( new Integer( i++ ) );
				}
			}
			else
			{
				while ( rsd.hasNext( ) )
				{
					oValue = rsd.next( )[0];

					boolean matched = false;
					int insertPoint = -1;

					// compare to existing base values and find an available
					// position.
					for ( int j = 0; j < baseValue.size( ); j++ )
					{
						Object ov = baseValue.get( j );

						int cprt = compareObjects( oValue, ov );
						if ( cprt == 0 )
						{
							if ( !idx.contains( new Integer( j ) ) )
							{
								idx.add( new Integer( j ) );
								matched = true;
								break;
							}
							else if ( sorting != null )
							{
								insertPoint = j + 1;
							}
						}
						else if ( cprt < 0 )
						{
							if ( sorting == SortOption.DESCENDING_LITERAL )
							{
								insertPoint = j + 1;
							}
						}
						else if ( cprt > 0 )
						{
							if ( sorting == SortOption.ASCENDING_LITERAL )
							{
								insertPoint = j + 1;
							}
						}

					}

					if ( !matched )
					{
						if ( sorting != null && insertPoint == -1 )
						{
							// convert position to first since no value is
							// greater/less than current value.
							insertPoint = 0;
						}

						if ( insertPoint == -1
								|| insertPoint >= baseValue.size( ) )
						{
							// if no existing position available, append to the
							// end.
							baseValue.add( oValue );
							idx.add( new Integer( baseValue.size( ) - 1 ) );
						}
						else
						{
							// insert and adjust existing indices.
							baseValue.add( insertPoint, oValue );

							// adjust current group index and add new position.
							for ( int i = 0; i < idx.size( ); i++ )
							{
								int x = ( (Integer) idx.get( i ) ).intValue( );

								if ( x >= insertPoint )
								{
									idx.set( i, new Integer( x + 1 ) );
								}
							}
							idx.add( new Integer( insertPoint ) );

							// adjust computed group indices.
							for ( Iterator itr = idxList.iterator( ); itr.hasNext( ); )
							{
								List gidx = (List) itr.next( );
								for ( int i = 0; i < gidx.size( ); i++ )
								{
									int x = ( (Integer) gidx.get( i ) ).intValue( );

									if ( x >= insertPoint )
									{
										gidx.set( i, new Integer( x + 1 ) );
									}
								}
							}
						}
					}
				}
			}
			idxList.add( idx );
		}

		// align all index array to equal length, fill empty value with -1;
		int maxLen = baseValue.size( );
		for ( Iterator itr = idxList.iterator( ); itr.hasNext( ); )
		{
			List lst = (List) itr.next( );
			if ( lst.size( ) < maxLen )
			{
				int inc = maxLen - lst.size( );
				for ( int i = 0; i < inc; i++ )
				{
					lst.add( new Integer( -1 ) );
				}
			}
		}

		return new Object[]{
				baseValue, idxList
		};
	}

	/**
	 * Returns the data type of specified column.
	 * 
	 * @param iColumnIndex
	 * @return
	 */
	public final int getColumnDataType( int iColumnIndex )
	{
		return iaDataTypes[iColumnIndex];
	}

	/**
	 * Returns the iterator of associated resultset.
	 * 
	 * @return
	 */
	public Iterator iterator( )
	{
		if ( liResultSet != null )
		{
			return liResultSet.iterator( );
		}
		return null;
	}

	/**
	 * Internally walks through the resultset and computes the group breaks
	 * cached for subsequent use
	 * 
	 * @param bGrouped
	 *            Indicates if the resultset contains the group key
	 * 
	 * @return Row indexes containing changing group key values
	 */
	private final int[] findGroupBreaks( boolean bGrouped )
	{
		final int iColumnIndex = bGrouped ? 0 : -1;
		if ( iColumnIndex == -1 )
		{
			return NO_GROUP_BREAKS;
		}

		final Iterator it = liResultSet.iterator( );
		final ArrayList al = new ArrayList( 16 );
		boolean bFirst = true;
		Object oValue, oPreviousValue = null;
		int iRowIndex = 0;

		while ( it.hasNext( ) )
		{
			oValue = ( (Object[]) it.next( ) )[iColumnIndex];
			iRowIndex++;
			if ( bFirst )
			{
				bFirst = false;
				oPreviousValue = oValue;
				continue;
			}
			if ( compareObjects( oPreviousValue, oValue ) != 0 )
			{
				al.add( new Integer( iRowIndex - 1 ) );
			}
			oPreviousValue = oValue;
		}

		final int[] ia = new int[al.size( )];
		for ( int i = 0; i < al.size( ); i++ )
		{
			ia[i] = ( (Integer) al.get( i ) ).intValue( );
		}
		return ia;
	}

	/**
	 * Compares two objects of the same data type
	 * 
	 * @param a
	 *            Object one
	 * @param b
	 *            Object two
	 * 
	 * @return The result of the comparison
	 */
	public static int compareObjects( Object a, Object b )
	{
		// a == b
		if ( a == null && b == null )
		{
			return 0;
		}

		// a < b
		else if ( a == null && b != null )
		{
			return -1;
		}

		// a > b
		else if ( a != null && b == null )
		{
			return 1;
		}

		else if ( a instanceof String )
		{
			int iC = a.toString( ).compareTo( b.toString( ) );
			if ( iC != 0 )
				iC = ( ( iC < 0 ) ? -1 : 1 );
			return iC;
		}
		else if ( a instanceof Number )
		{
			final double d1 = ( (Number) a ).doubleValue( );
			final double d2 = ( (Number) b ).doubleValue( );
			return ( d1 == d2 ) ? 0 : ( d1 < d2 ) ? -1 : 1;
		}
		else if ( a instanceof java.util.Date )
		{
			final long d1 = ( (java.util.Date) a ).getTime( );
			final long d2 = ( (java.util.Date) b ).getTime( );
			return ( d1 == d2 ) ? 0 : ( d1 < d2 ) ? -1 : 1;
		}
		else if ( a instanceof Calendar )
		{
			final long d1 = ( (Calendar) a ).getTime( ).getTime( );
			final long d2 = ( (Calendar) b ).getTime( ).getTime( );
			return ( d1 == d2 ) ? 0 : ( d1 < d2 ) ? -1 : 1;
		}
		else
		// HANDLE AS STRINGs
		{
			return compareObjects( a.toString( ), b.toString( ) );
		}
	}
}