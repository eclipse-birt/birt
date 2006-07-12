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

package org.eclipse.birt.chart.util;

import java.util.Date;
import java.util.Locale;

import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.plugin.ChartEnginePlugin;

import com.ibm.icu.text.SimpleDateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

/**
 * A convenience data type provided to aid in wrapping a datetime value used
 * with datetime data elements. Refer to
 * {@link org.eclipse.birt.chart.model.data.DateTimeDataElement}
 */
public class CDateTime extends GregorianCalendar
{

	private static final long serialVersionUID = 1L;

	private static final int MILLIS_IN_SECOND = 1000;

	private static final int MILLIS_IN_MINUTE = MILLIS_IN_SECOND * 60;

	private static final int MILLIS_IN_HOUR = MILLIS_IN_MINUTE * 60;

	private static final int MILLIS_IN_DAY = MILLIS_IN_HOUR * 24;

	private static final int[] iaUnitTypes = {
			Calendar.MILLISECOND,
			Calendar.SECOND,
			Calendar.MINUTE,
			Calendar.HOUR_OF_DAY,
			Calendar.DATE,
			Calendar.MONTH,
			Calendar.YEAR
	};

	private static final SimpleDateFormat _sdf = new SimpleDateFormat( "MM-dd-yyyy HH:mm:ss" ); //$NON-NLS-1$

	/**
	 * A zero-arg default constructor
	 */
	public CDateTime( )
	{
		super( );
	}

	/**
	 * A constructor that creates an instance from a given
	 * <code>java.util.Date</code> value
	 * 
	 * @param d
	 *            A previously defined Date instance
	 */
	public CDateTime( Date d )
	{
		super( );
		setTime( d );
	}

	/**
	 * A constructor that creates an instance from a given <code>Calendar</code>
	 * value
	 * 
	 * @param c
	 *            A previously defined Calendar instance
	 */
	public CDateTime( Calendar c )
	{
		super( );
		setTime( c.getTime( ) );
	}

	/**
	 * A constructor that creates an instance from a given <code>long</code>
	 * value
	 * 
	 * @param lTimeInMillis
	 *            The time defined in milliseconds
	 */
	public CDateTime( long lTimeInMillis )
	{
		super( );
		setTimeInMillis( lTimeInMillis );
	}

	/**
	 * A constructor that creates an instance for a specified year, month and
	 * date
	 * 
	 * @param year
	 *            The year associated with this instance
	 * @param month
	 *            The month index (1-12) of the year (1-based)
	 * @param date
	 *            The day of the month associated with this instance
	 */
	public CDateTime( int year, int month, int date )
	{
		super( year, month - 1, date );
	}

	/**
	 * A constructor that creates an instance for a specified year, month, date,
	 * hour and minute
	 * 
	 * @param year
	 *            The year associated with this instance
	 * @param month
	 *            The month index (1-12) of the year (1-based)
	 * @param date
	 *            The day of the month associated with this instance
	 * @param hour
	 *            The hour (0-23) of the day (military) associated with this
	 *            instance
	 * @param minute
	 *            The minute (0-59) of the hour associated with this instance
	 */
	public CDateTime( int year, int month, int date, int hour, int minute )
	{
		super( year, month - 1, date, hour, minute );
	}

	/**
	 * A constructor that creates an instance for a specified year, month, date,
	 * hour and minute
	 * 
	 * @param year
	 *            The year associated with this instance
	 * @param month
	 *            The month index (1-12) of the year (1-based)
	 * @param date
	 *            The day of the month associated with this instance
	 * @param hour
	 *            The hour (0-23) of the day (military) associated with this
	 *            instance
	 * @param minute
	 *            The minute (0-59) of the hour associated with this instance
	 * @param second
	 *            The second (0-59) of the minute associated with this instance
	 */
	public CDateTime( int year, int month, int date, int hour, int minute,
			int second )
	{
		super( year, month - 1, date, hour, minute, second );
	}

	/**
	 * A constructor that creates a default instance for a given locale
	 * 
	 * @param locale
	 *            The locale for which the instance is being created
	 * @deprecated use {@link #CDateTime(ULocale)} instead.
	 */
	public CDateTime( Locale aLocale )
	{
		super( aLocale );
	}

	/**
	 * A constructor that creates a default instance for a given locale
	 * 
	 * @param locale
	 *            The locale for which the instance is being created
	 * @since 2.1
	 */
	public CDateTime( ULocale locale )
	{
		super( locale );
	}

	/**
	 * A constructor that creates a default instance for a given timezone
	 * 
	 * @param tz
	 *            The timezone for which the instance is being created
	 */
	public CDateTime( TimeZone tz )
	{
		super( tz );
	}

	/**
	 * A constructor that creates a default instance for a given timezone and
	 * locale
	 * 
	 * @param tz
	 *            The timezone for which the instance is being created
	 * @param locale
	 *            The locale for which the instance is being created
	 * @deprecated use {@link #CDateTime(TimeZone, ULocale)} instead.
	 */
	public CDateTime( TimeZone tz, Locale locale )
	{
		super( tz, locale );
	}

	/**
	 * A constructor that creates a default instance for a given timezone and
	 * locale
	 * 
	 * @param tz
	 *            The timezone for which the instance is being created
	 * @param locale
	 *            The locale for which the instance is being created
	 */
	public CDateTime( TimeZone tz, ULocale locale )
	{
		super( tz, locale );
	}

	/**
	 * A convenient method used in building the ticks for a datetime scale.
	 * Computes a new datetime object relative to the existing one moving back
	 * by 'step' units.
	 * 
	 * @param iUnit
	 * @param iStep
	 * 
	 * @return
	 */
	public CDateTime backward( int iUnit, int iStep )
	{
		CDateTime cd = (CDateTime) clone( );
		cd.add( iUnit, -iStep );
		return cd;
	}

	/**
	 * A convenient method used in building the ticks for a datetime scale.
	 * Computes a new datetime object relative to the existing one moving
	 * forward by 'step' units.
	 * 
	 * @param iUnit
	 * @param iStep
	 * @return
	 */
	public CDateTime forward( int iUnit, int iStep )
	{
		CDateTime cd = (CDateTime) clone( );
		cd.add( iUnit, iStep );
		return cd;
	}

	/**
	 * Returns the year associated with this instance
	 * 
	 * @return The year associated with this instance
	 */
	public final int getYear( )
	{
		return get( Calendar.YEAR );
	}

	/**
	 * Returns the month (0-based) associated with this instance
	 * 
	 * @return The month associated with this instance
	 */
	public final int getMonth( )
	{
		return get( Calendar.MONTH );
	}

	/**
	 * Returns the day of the month associated with this instance
	 * 
	 * @return The day of the month associated with this instance
	 */
	public final int getDay( )
	{
		return get( Calendar.DATE );
	}

	/**
	 * Returns the hour (military) associated with this instance
	 * 
	 * @return The hour associated with this instance
	 */
	public final int getHour( )
	{
		return get( Calendar.HOUR_OF_DAY );
	}

	/**
	 * Returns the minute associated with this instance
	 * 
	 * @return The minute associated with this instance
	 */
	public final int getMinute( )
	{
		return get( Calendar.MINUTE );
	}

	/**
	 * Returns the second associated with this instance
	 * 
	 * @return The second associated with this instance
	 */
	public final int getSecond( )
	{
		return get( Calendar.SECOND );
	}

	/**
	 * Returns the most significant datetime unit in which there's a difference
	 * or 0 if there is no difference.
	 * 
	 * @return The least significant 'Calendar' unit in which a difference
	 *         occurred
	 */
	public static final int getDifference( CDateTime cdt1, CDateTime cdt2 )
	{
		if ( cdt1.getYear( ) != cdt2.getYear( ) )
		{
			return Calendar.YEAR;
		}
		else if ( cdt1.getMonth( ) != cdt2.getMonth( ) )
		{
			return Calendar.MONTH;
		}
		else if ( cdt1.getDay( ) != cdt2.getDay( ) )
		{
			return Calendar.DATE;
		}
		else if ( cdt1.getHour( ) != cdt2.getHour( ) )
		{
			return Calendar.HOUR_OF_DAY;
		}
		else if ( cdt1.getMinute( ) != cdt2.getMinute( ) )
		{
			return Calendar.MINUTE;
		}
		else if ( cdt1.getSecond( ) != cdt2.getSecond( ) )
		{
			return Calendar.SECOND;
		}
		else
		{
			return 0;
		}
	}

	/**
	 * Returns a preferred format specifier for tick labels that represent axis
	 * values that will be computed based on the difference between cdt1 and
	 * cdt2
	 * 
	 * @param iUnit
	 *            The unit for which a preferred pattern is being requested
	 * 
	 * @return A preferred datetime format pattern for the given unit
	 */
	public static final String getPreferredFormat( int iUnit )
	{
		if ( iUnit == Calendar.YEAR )
		{
			return "yyyy"; //$NON-NLS-1$
		}
		else if ( iUnit == Calendar.MONTH )
		{
			return "MMM yyyy"; //$NON-NLS-1$
		}
		else if ( iUnit == Calendar.DATE )
		{
			return "MM-dd-yyyy"; //$NON-NLS-1$
		}
		else if ( iUnit == Calendar.HOUR_OF_DAY )
		{
			return "MM-dd-yy\nHH:mm"; //$NON-NLS-1$
		}
		else if ( iUnit == Calendar.MINUTE )
		{
			return "HH:mm:ss"; //$NON-NLS-1$
		}
		else if ( iUnit == Calendar.SECOND )
		{
			return "HH:mm:ss"; //$NON-NLS-1$
		}
		return null;
	}

	/**
	 * Computes the difference between two given datetime values as a fraction
	 * for the requested field.
	 * 
	 * @param cdt1
	 *            The first datetime value
	 * @param cdt2
	 *            The second datetime value
	 * @param iUnit
	 *            The field with respect to which the difference is being
	 *            computed as a fraction
	 * 
	 * @return The fractional difference between the two specified datetime
	 *         values
	 */
	public static final double computeDifference( CDateTime cdt1,
			CDateTime cdt2, int iUnit )
	{
		final long l1 = cdt1.getTimeInMillis( );
		final long l2 = cdt2.getTimeInMillis( );
		if ( iUnit == Calendar.MILLISECOND )
		{
			return ( l1 - l2 );
		}
		else if ( iUnit == Calendar.SECOND )
		{
			return ( l1 - l2 ) / MILLIS_IN_SECOND;
		}
		else if ( iUnit == Calendar.MINUTE )
		{
			return ( l1 - l2 ) / MILLIS_IN_MINUTE;
		}
		else if ( iUnit == Calendar.HOUR_OF_DAY )
		{
			return ( l1 - l2 ) / MILLIS_IN_HOUR;
		}
		else if ( iUnit == Calendar.DATE )
		{
			return ( l1 - l2 ) / MILLIS_IN_DAY;
		}
		else if ( iUnit == Calendar.WEEK_OF_YEAR )
		{
			final double dDays = computeDifference( cdt1, cdt2, Calendar.DATE );
			return dDays / 7.0;
		}
		else if ( iUnit == Calendar.MONTH )
		{
			final double dYears = cdt1.getYear( ) - cdt2.getYear( );
			return dYears * 12 + ( cdt1.getMonth( ) - cdt2.getMonth( ) );
		}
		else if ( iUnit == Calendar.YEAR )
		{
			return cdt1.getYear( ) - cdt2.getYear( );
		}
		return 0;
	}

	/**
	 * Walks through all values in a dataset and computes the least significant
	 * unit for which a difference was noted.
	 * 
	 * @param dsi
	 *            The dataset iterator that facilitates visiting individual
	 *            values
	 * 
	 * @return The least significant unit for which a difference in datetime
	 *         values was noted
	 */
	public static final int computeUnit( DataSetIterator dsi )
	{
		Calendar cCurr, cPrev;

		for ( int k = 0; k < iaUnitTypes.length; k++ )
		{
			cPrev = (Calendar) dsi.last( );
			dsi.reset( );
			while ( dsi.hasNext( ) )
			{
				cCurr = (Calendar) dsi.next( );
				if ( cCurr != null
						&& cPrev != null
						&& cCurr.get( iaUnitTypes[k] ) != cPrev.get( iaUnitTypes[k] ) )
				{
					return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE
					// WAS NOTED
				}

				if ( cCurr != null )
				{
					cPrev = cCurr;
				}
			}
		}

		// if all no difference, return year as default unit.
		return Calendar.YEAR;
	}

	/**
	 * Walks through all values in a datetime array and computes the least
	 * significant unit for which a difference was noted.
	 * 
	 * @param cdta
	 *            A datetime array for which the least significant unit
	 *            difference is to be computed
	 * 
	 * @return The least significant unit for which a difference in datetime
	 *         values was noted
	 */
	public static final int computeUnit( CDateTime[] cdta )
			throws ChartException
	{
		int j;
		for ( int k = 0; k < iaUnitTypes.length; k++ )
		{
			for ( int i = 0; i < cdta.length; i++ )
			{
				j = i + 1;
				if ( j > cdta.length - 1 )
				{
					j = 0;
				}

				if ( cdta[i] == null || cdta[j] == null )
				{
					throw new ChartException( ChartEnginePlugin.ID,
							ChartException.VALIDATION,
							"exception.base.orthogonal.null.datadefinition", //$NON-NLS-1$
							Messages.getResourceBundle( ) );
				}

				if ( cdta[i].get( iaUnitTypes[k] ) != cdta[j].get( iaUnitTypes[k] ) )
				{
					return iaUnitTypes[k]; // THE UNIT FOR WHICH A DIFFERENCE
					// WAS NOTED
				}
			}
		}

		// if all no difference, return year as default unit.
		return Calendar.YEAR;
	}

	/**
	 * Returns the number of days for a particular (month,year) combination
	 * 
	 * @param iMonth
	 *            The month (0-11) for which the day count is to be retrieved
	 * @param iYear
	 *            The year for which the day count is to be retrieved
	 * @return
	 */
	public static final int getMaximumDaysIn( int iMonth, int iYear )
	{
		CDateTime cdt = new CDateTime( );
		cdt.set( Calendar.YEAR, iYear );
		cdt.set( Calendar.MONTH, iMonth );
		return cdt.getActualMaximum( Calendar.DATE );
	}

	/**
	 * Returns the number of days for a particular year
	 * 
	 * @param iYear
	 *            The year for which the day count is to be retrieved
	 * 
	 * @return The number of days in the specified year
	 */
	public static final int getMaximumDaysIn( int iYear )
	{
		CDateTime cdt = new CDateTime( );
		cdt.set( Calendar.YEAR, iYear );
		return cdt.getActualMaximum( Calendar.DAY_OF_YEAR );
	}

	/**
	 * A convenience method provided to return the number of milliseconds
	 * available in a given unit
	 * 
	 * @param iUnit
	 *            The unit for which the number of milliseconds are to be
	 *            computed
	 * 
	 * @return The number of milliseconds for the specified unit
	 */
	public static final double inMillis( int iUnit )
	{
		if ( iUnit == Calendar.SECOND )
		{
			return MILLIS_IN_SECOND;
		}
		else if ( iUnit == Calendar.MINUTE )
		{
			return MILLIS_IN_MINUTE;
		}
		else if ( iUnit == Calendar.HOUR )
		{
			return MILLIS_IN_HOUR;
		}
		else if ( iUnit == Calendar.DATE )
		{
			return MILLIS_IN_DAY;
		}
		else if ( iUnit == Calendar.MONTH )
		{
			return MILLIS_IN_DAY * 30.4375;
		}
		else if ( iUnit == Calendar.YEAR )
		{
			return MILLIS_IN_DAY * 365.25;
		}
		return 0;
	}

	/**
	 * Zeroes out all units for this datetime instance below a specified unit.
	 * 
	 * @param iUnit
	 *            The unit below which all values are to be zeroed out
	 */
	public final void clearBelow( int iUnit )
	{
		if ( iUnit == YEAR )
		{
			set( Calendar.MILLISECOND, 0 );
			set( Calendar.SECOND, 0 );
			set( Calendar.MINUTE, 0 );
			set( Calendar.HOUR, 0 );
			set( Calendar.DATE, 1 );
			set( Calendar.MONTH, 0 );
		}
		else if ( iUnit == MONTH )
		{
			set( Calendar.MILLISECOND, 0 );
			set( Calendar.SECOND, 0 );
			set( Calendar.MINUTE, 0 );
			set( Calendar.HOUR, 0 );
			set( Calendar.DATE, 1 );
		}
		else if ( iUnit == DATE )
		{
			set( Calendar.MILLISECOND, 0 );
			set( Calendar.SECOND, 0 );
			set( Calendar.MINUTE, 0 );
			set( Calendar.HOUR, 0 );
		}
		else if ( iUnit == HOUR )
		{
			set( Calendar.MILLISECOND, 0 );
			set( Calendar.SECOND, 0 );
			set( Calendar.MINUTE, 0 );
		}
		else if ( iUnit == MINUTE )
		{
			set( Calendar.MILLISECOND, 0 );
			set( Calendar.SECOND, 0 );
		}
	}

	/**
	 * Parses a value formatted as MM-dd-yyyy HH:mm:ss and attempts to create an
	 * instance of this object
	 * 
	 * @param sDateTimeValue
	 *            The value to be parsed
	 * @return An instance of the datetime value created
	 */
	public static CDateTime parse( String sDateTimeValue )
	{
		try
		{
			return new CDateTime( _sdf.parse( sDateTimeValue ) );
		}
		catch ( Exception ex )
		{
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public final String toString( )
	{
		return _sdf.format( getTime( ) );
	}
}
