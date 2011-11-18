package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;


public class YearToDateFunction extends AbstractMDX implements IPeriodsFunction
{
	public List<TimeMember> getResult( TimeMember member )
	{
		String[] levels = member.getLevelType( );
		int[] values = member.getMemberValue( );
		
		Calendar cal = new GregorianCalendar( TimeMemberUtil.getTimeZone( ),
				TimeMemberUtil.getDefaultLocale( ) );
		cal.clear( );
		cal.setMinimalDaysInFirstWeek(1);
		String calculateUnit = this.translateToCal( cal, levels, values ) ;
		Calendar isCurrentCal = null;
		if ( isCurrent )
		{
			isCurrentCal = (Calendar) cal.clone( );
		}
		
		List<TimeMember> list = new ArrayList<TimeMember>( );
		if ( calculateUnit.equals( YEAR ) )
		{
			TimeMember newMember = new TimeMember( values, levels );
			list.add( newMember );
		}
		else if ( calculateUnit.equals( QUARTER ) )
		{
			int quarter = cal.get( Calendar.MONTH ) / 3 + 1;
			TimeMember newMember = null;
			for ( int i = 1; i <= quarter; i++ )
			{
				int[] newValues = getValueFromCal( cal, levels );
				newMember = new TimeMember( newValues, levels );
				list.add( newMember );
				cal.add( Calendar.MONTH, -3 );
			}
			if( isCurrent )
			{
				int year = isCurrentCal.get( Calendar.YEAR );
				isCurrentCal.add( Calendar.MONTH, 3 );
				while( year == isCurrentCal.get( Calendar.YEAR ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levels );
					newMember = new TimeMember( newValues, levels );
					list.add( newMember );
					isCurrentCal.add( Calendar.MONTH, 3 );
				}
			}
		}
		else if ( calculateUnit.equals( MONTH ) )
		{
			int month = cal.get( Calendar.MONTH )+1;
			TimeMember newMember = null;
			for( int i=1; i<=month ; i++)
			{
				int[] newValues = getValueFromCal( cal,levels);
				newMember = new TimeMember(newValues,levels);
				list.add( newMember );
				cal.add( Calendar.MONTH, -1 );
			}
			if( isCurrent )
			{
				int year = isCurrentCal.get( Calendar.YEAR );
				isCurrentCal.add( Calendar.MONTH, 1 );
				while( year == isCurrentCal.get( Calendar.YEAR ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levels );
					newMember = new TimeMember( newValues, levels );
					list.add( newMember );
					isCurrentCal.add( Calendar.MONTH, 1 );
				}
			}
		}
		else if ( calculateUnit.equals( WEEK ) )
		{
			int weekOfYear = cal.get( Calendar.WEEK_OF_YEAR );
			TimeMember newMember = null;
			for ( int i = 1; i <= weekOfYear; i++ )
			{
				int[] newValues = getValueFromCal( cal, levels );
				newMember = new TimeMember( newValues, levels );
				list.add( newMember );
				cal.add( Calendar.WEEK_OF_YEAR, -1 );
			}
			if( isCurrent )
			{
				int year = isCurrentCal.get( Calendar.YEAR );
				isCurrentCal.add( Calendar.WEEK_OF_YEAR, 1 );
				while( year == isCurrentCal.get( Calendar.YEAR ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levels );
					newMember = new TimeMember( newValues, levels );
					list.add( newMember );
					isCurrentCal.add( Calendar.WEEK_OF_YEAR, 1 );
				}
			}
		}
		else if ( calculateUnit.equals( DAY ) )
		{
			int dayOfYear = cal.get( Calendar.DAY_OF_YEAR );
			TimeMember newMember = null;
			for ( int i = 1; i <= dayOfYear; i++ )
			{
				int[] newValues = getValueFromCal( cal, levels );
				newMember = new TimeMember( newValues, levels );
				list.add( newMember );
				cal.add( Calendar.DAY_OF_YEAR, -1 );
			}
			if( isCurrent )
			{
				int year = isCurrentCal.get( Calendar.YEAR );
				isCurrentCal.add( Calendar.DAY_OF_YEAR, 1 );
				while( year == isCurrentCal.get( Calendar.YEAR ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levels );
					newMember = new TimeMember( newValues, levels );
					list.add( newMember );
					isCurrentCal.add( Calendar.DAY_OF_YEAR, 1 );
				}
			}
		}

		return list;
	}
}
