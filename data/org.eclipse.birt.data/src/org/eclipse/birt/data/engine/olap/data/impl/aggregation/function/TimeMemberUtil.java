package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.Date;

import org.eclipse.birt.data.engine.olap.data.api.ILevel;
import org.eclipse.birt.data.engine.olap.data.api.cube.IDimension;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.TimeZone;
import com.ibm.icu.util.ULocale;

public class TimeMemberUtil
{
	private static ULocale defaultLocale = ULocale.getDefault( );
	private static TimeZone timeZone = TimeZone.getDefault( );
	
	public static void setDefaultLocale(ULocale defaultLocale)
	{
		TimeMemberUtil.defaultLocale = defaultLocale;
	}

	public static void setTimeZone(TimeZone timeZone)
	{
		TimeMemberUtil.timeZone = timeZone;
	}
	
	public static ULocale getDefaultLocale()
	{
		return defaultLocale;
	}

	public static TimeZone getTimeZone()
	{
		return timeZone;
	}
	
	public static TimeMember getCurrentMember( IDimension timeDimension, TimeMember cellTimeMember )
	{
		return toMember( timeDimension, null, cellTimeMember );
	}
	
	private static int getLowestLevelIndex( IDimension timeDimension, TimeMember cellTimeMember )
	{
		ILevel[] levels = timeDimension.getHierarchy( ).getLevels();
		String[] levelType = cellTimeMember.getLevelType();
		for( int i = 0; i < levels.length; i++ )
		{
			if( levels[i].getLeveType().equals( levelType[levelType.length-1]))
			{
				return i;
			}
		}
		return -1;
	}

	public static TimeMember toMember( IDimension timeDimension, Date referenceDate, TimeMember cellTimeMember )
	{
		ILevel[] levels = timeDimension.getHierarchy( ).getLevels();
		String[] levelType = null;
		if( referenceDate != null )
			levelType = new String[levels.length - 1];
		else
		{
			levelType = new String[getLowestLevelIndex(timeDimension, cellTimeMember)+1];
		}
		int[] levelValue = new int[levelType.length];
		Calendar cal = getCalendar( referenceDate );
		for( int i = 0; i < cellTimeMember.getLevelType().length; i++)
		{
			if( TimeMember.TIME_LEVEL_TYPE_YEAR.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.YEAR, cellTimeMember.getMemberValue()[i] );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_QUARTER.equals( cellTimeMember.getLevelType()[i] ) )
			{
				int month = cal.get( Calendar.MONTH ) % 3 + ( cellTimeMember.getMemberValue()[i] - 1 ) * 3;
				cal.set( Calendar.MONTH, month );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_MONTH.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.MONTH, cellTimeMember.getMemberValue()[i] - 1 );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.DAY_OF_MONTH, cellTimeMember.getMemberValue()[i] );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.DAY_OF_WEEK, cellTimeMember.getMemberValue()[i] );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.DAY_OF_YEAR, cellTimeMember.getMemberValue()[i] );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.WEEK_OF_YEAR, cellTimeMember.getMemberValue()[i] );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( cellTimeMember.getLevelType()[i] ) )
			{
				cal.set( Calendar.WEEK_OF_MONTH, cellTimeMember.getMemberValue()[i] );
			}
		}
		for( int i = 0; i < levelType.length; i++ )
		{
			levelType[i] = levels[i].getLeveType( );
			if( TimeMember.TIME_LEVEL_TYPE_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_QUARTER.equals( levelType[i] ) )
			{
				levelValue[i] = quarter( cal );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.MONTH ) + 1;
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_MONTH );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_WEEK );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.WEEK_OF_YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.WEEK_OF_MONTH );
			}
		}
		
		return new TimeMember( levelValue, levelType );
	}
	
	public static TimeMember toMember( IDimension timeDimension, Date referenceDate )
	{
		ILevel[] levels = timeDimension.getHierarchy( ).getLevels();
		String[] levelType = null;
		levelType = new String[levels.length - 1];
		
		int[] levelValue = new int[levelType.length];
		Calendar cal = getCalendar( referenceDate );
		
		for( int i = 0; i < levelType.length; i++ )
		{
			levelType[i] = levels[i].getLeveType( );
			if( TimeMember.TIME_LEVEL_TYPE_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_QUARTER.equals( levelType[i] ) )
			{
				levelValue[i] = quarter( cal );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.MONTH ) + 1;
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_MONTH );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_WEEK );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.DAY_OF_YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.WEEK_OF_YEAR );
			}
			else if( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH.equals( levelType[i] ) )
			{
				levelValue[i] = cal.get( Calendar.WEEK_OF_MONTH );
			}
		}
		
		return new TimeMember( levelValue, levelType );
	}
	
	private static int quarter( Calendar cal )
	{
		int month = cal.get( Calendar.MONTH );
		switch ( month )
		{
			case Calendar.JANUARY :
			case Calendar.FEBRUARY :
			case Calendar.MARCH :
				return 1;
			case Calendar.APRIL :
			case Calendar.MAY :
			case Calendar.JUNE :
				return 2;
			case Calendar.JULY :
			case Calendar.AUGUST :
			case Calendar.SEPTEMBER :
				return 3;
			case Calendar.OCTOBER :
			case Calendar.NOVEMBER :
			case Calendar.DECEMBER :
				return 4;
			default :
				return -1;
		}
	}
	
	/**
	 * 
	 * @param d
	 * @return
	 */
	private static Calendar getCalendar( Date d )
	{
		Calendar c = Calendar.getInstance( timeZone, defaultLocale );
		
		//Fix for ted 38388
		c.setMinimalDaysInFirstWeek( 1 );
		if ( d == null )
		{
			c.clear( );
			c.set( 1970, 0, 1 );
		}
		else
		{
			c.setTime( d );
		}
		return c;
	}
}
