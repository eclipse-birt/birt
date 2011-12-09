
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

/**
 * The Trailing N Period function returns a set of consecutive members that
 * include a specified member and can be located by the member and the Offset
 * number.Now we support trailing in 5 level, Year, Quarter, Month, Week, Day
 * 
 * 
 */
public class TrailingFunction extends AbstractMDX implements IPeriodsFunction
{

	private String levelType = "";
	private int offset = 0;

	public TrailingFunction( String levelType, int offset )
	{
		this.levelType = levelType;
		this.offset = offset;
	}

	/*
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.IPeriodsFunction#getResult(org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.TimeMember)
	 */
	public List<TimeMember> getResult( TimeMember member )
	{
		List<TimeMember> timeMembers = new ArrayList<TimeMember>( );

		String[] levelTypes = member.getLevelType( );
		int[] values = member.getMemberValue( );

		Calendar cal1 = new GregorianCalendar( TimeMemberUtil.getTimeZone( ),
				TimeMemberUtil.getDefaultLocale( ) );
		cal1.clear( );
		String calculateUnit = translateToCal( cal1, levelTypes, values );
		Calendar cal2 = (Calendar) cal1.clone( );
        int year = 1;
        int year_woy = 1;
		if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_YEAR ) )
		{
			cal1.add( Calendar.YEAR, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_MONTH ) )
		{
			cal1.add( Calendar.MONTH, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_QUARTER ) )
		{
			// Calendar not support quarter, so add 3 month
			cal1.add( Calendar.MONTH, offset * 3 );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH ) )
		{
			if ( !calculateUnit.equals( DAY ) )
			{
				year_woy = cal1.get( Calendar.YEAR_WOY );
				year = cal1.get( Calendar.YEAR );
				// year_woy < year, means last week of previous year
				// for example. 2011/1/1, the year_woy is 2010
				if ( year_woy < year )
				{
					cal1.set( Calendar.DAY_OF_WEEK, 7 );
				}
				else if (year_woy > year)
				{
					cal1.set( Calendar.DAY_OF_WEEK, 1 );
				}
			}
			cal1.add( Calendar.WEEK_OF_YEAR, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
		{
			if ( !calculateUnit.equals( DAY ) )
			{
				year_woy = cal1.get( Calendar.YEAR_WOY );
				year = cal1.get( Calendar.YEAR );
				// year_woy < year, means last week of previous year
				// for example. 2011/1/1, the year_woy is 2010
				if ( year_woy < year )
				{
					cal1.set( Calendar.DAY_OF_WEEK, 7 );
				}
				else if (year_woy > year)
				{
					cal1.set( Calendar.DAY_OF_WEEK, 1 );
				}
			}
			cal1.add( Calendar.WEEK_OF_YEAR, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH ) )
		{
			cal1.add( Calendar.DATE, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK ) )
		{
			cal1.add( Calendar.DATE, offset );
		}
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR ) )
		{
			cal1.add( Calendar.DATE, offset );
		}
		
		timeMembers.add( member );

		// TimeMember.levelTypes=["year"(2009),"month"(3)],TrailingFunciton.levelType="Quarter",offset=1
		// the result will be 2009.3,2009.2,2009.1. when we add quarter to the
		// calendar, the date will be
		// 2008.12, so we must set it to 2009.1,here we should add the date
		// based on the calculateUnit.
		if ( calculateUnit.equals( YEAR ) )
		{
			if (levelType.equals( YEAR ))
			{
				cal1.add( Calendar.YEAR, -Math.abs( offset ) / offset );
			}
		}
		else if ( calculateUnit.equals( QUARTER ) )
		{
			cal1.add( Calendar.MONTH, -Math.abs( offset ) / offset * 3 );
		}
		else if ( calculateUnit.equals( WEEK ) )
		{
			cal1.add( Calendar.WEEK_OF_YEAR, -Math.abs( offset ) / offset );
			cal1.set( Calendar.DAY_OF_WEEK, 1 );
		}
		else if ( calculateUnit.equals( MONTH ) )
		{
			cal1.add( Calendar.MONTH, -Math.abs( offset ) / offset );
		}
		else if ( calculateUnit.equals( DAY ) )
		{
			cal1.add( Calendar.DATE, -Math.abs( offset ) / offset );
		}

		int[] fillDateTmp;
		TimeMember timeMember;
		int step = Math.abs( offset ) / offset;
		int[] cal1Value = getValueFromCal( cal1, levelTypes );
		int[] cal2Value = getValueFromCal( cal2, levelTypes );

		while ( !compareIntArray( cal1Value, cal2Value ) )
		{
			if ( calculateUnit.equals( WEEK ) )
			{
				this.addExtraWeek( timeMembers, cal2, new TimeMember( cal2Value, levelTypes ), levelTypes );
			}
			
			if ( calculateUnit.equals( YEAR ) )
			{
				cal2.add( Calendar.YEAR, step );
			}
			if ( calculateUnit.equals( QUARTER ) )
			{
				cal2.add( Calendar.MONTH, step * 3 );
			}
			else if ( calculateUnit.equals( MONTH ) )
			{
				cal2.add( Calendar.MONTH, step );
			}
			else if ( calculateUnit.equals( WEEK ) )
			{
				cal2.add( Calendar.WEEK_OF_YEAR, step );
				cal2.set( Calendar.DAY_OF_WEEK, 1 );
			}
			else if ( calculateUnit.equals( DAY ) )
			{
				cal2.add( Calendar.DATE, step );
			}

			fillDateTmp = getValueFromCal( cal2, levelTypes );
			timeMember = new TimeMember( fillDateTmp, levelTypes );
			timeMembers.add( timeMember );
			
			cal2Value = getValueFromCal( cal2, levelTypes );
		}

		return timeMembers;
	}

	private boolean compareIntArray( int[] tmp1, int[] tmp2 )
	{
		if ( tmp1.length != tmp2.length )
		{
			return false;
		}
		for ( int i = 0; i < tmp1.length; i++ )
		{
			if ( tmp1[i] != tmp2[i] )
			{
				return false;
			}
		}

		return true;
	}
}
