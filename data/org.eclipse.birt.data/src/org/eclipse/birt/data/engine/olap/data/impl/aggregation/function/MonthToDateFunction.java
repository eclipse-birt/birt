
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

/**
 * 
 * Use the MTD function to return a set of members of the Time hierarchy from
 * the same month, up to and including a particular member. For example, you
 * might use the function to return the set of days in 2007.8 up to and
 * including 2007.8.21.
 * 
 * 
 */
public class MonthToDateFunction extends AbstractMDX
		implements
			IPeriodsFunction
{

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.IPeriodsFunction#getResult(org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.TimeMember)
	 */
	public List<TimeMember> getResult( TimeMember member )
	{
		List timeMembers = new ArrayList<TimeMember>( );
		String[] levelTypes = member.getLevelType( );
		int[] values = member.getMemberValue( );

		Calendar cal = new GregorianCalendar( TimeMemberUtil.getTimeZone( ),
				TimeMemberUtil.getDefaultLocale( ) );
		cal.clear( );
		cal.set(Calendar.DAY_OF_MONTH, 28);
		String baseType = translateToCal( cal, levelTypes, values );
		Calendar isCurrentCal = null;
		if ( isCurrent )
		{
			isCurrentCal = (Calendar) cal.clone( );
		}
		int[] tmp;
		if ( baseType.equals( MONTH ) )
		{
			timeMembers.add( member );
		}
		else if ( baseType.equals( WEEK ) )
		{
			int weekOfMonth = cal.get( Calendar.WEEK_OF_MONTH );
			int month = cal.get( Calendar.MONTH );
			int year = cal.get( Calendar.YEAR );
			for ( int i = 1; i <= weekOfMonth; i++ )
			{
				cal.set( Calendar.YEAR, year );
				cal.set( Calendar.MONTH, month );
				cal.set( Calendar.WEEK_OF_MONTH, i );
				tmp = getValueFromCal( cal, levelTypes );
				TimeMember timeMember = new TimeMember( tmp, levelTypes );
				timeMembers.add( timeMember );
			}
			if( isCurrent )
			{
				int currentMonth = isCurrentCal.get( Calendar.MONTH );
				isCurrentCal.add( Calendar.WEEK_OF_MONTH, 1 );
				while( currentMonth == isCurrentCal.get( Calendar.MONTH ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levelTypes );
					TimeMember newMember = new TimeMember( newValues, levelTypes );
					timeMembers.add( newMember );
					isCurrentCal.add( Calendar.WEEK_OF_MONTH, 1 );
				}
			}
		}
		else if ( baseType.equals( DAY ) )
		{
			int dayOfMonth = cal.get( Calendar.DAY_OF_MONTH );
			for ( int i = 1; i <= dayOfMonth; i++ )
			{
				cal.set( Calendar.DAY_OF_MONTH, i );
				tmp = getValueFromCal( cal, levelTypes );
				TimeMember timeMember = new TimeMember( tmp, levelTypes );
				timeMembers.add( timeMember );
			}
			if( isCurrent )
			{
				int currentMonth = isCurrentCal.get( Calendar.MONTH );
				isCurrentCal.add( Calendar.DAY_OF_MONTH, 1 );
				while( currentMonth == isCurrentCal.get( Calendar.MONTH ))
				{
					int[] newValues = getValueFromCal( isCurrentCal, levelTypes );
					TimeMember newMember = new TimeMember( newValues, levelTypes );
					timeMembers.add( newMember );
					isCurrentCal.add( Calendar.DAY_OF_MONTH, 1 );
				}
			}
		}

		return timeMembers;
	}

}
