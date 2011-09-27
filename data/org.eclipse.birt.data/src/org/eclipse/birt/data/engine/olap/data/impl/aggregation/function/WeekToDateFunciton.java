
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Use the WTD function to return a set of members of the Time hierarchy from
 * the same week, up to and including a particular member.
 * 
 * @author peng.shi
 * 
 */
public class WeekToDateFunciton extends AbstractMDX implements IPeriodsFunction
{

	private final long dayTimeInMills = 24 * 3600 * 1000;

	@Override
	public List<TimeMember> getResult( TimeMember member )
	{
		List timeMembers = new ArrayList<TimeMember>( );
		String[] levelTypes = member.getLevelType( );
		int[] values = member.getMemberValue( );

		Calendar cal = new GregorianCalendar( );
		String baseType = translateToCal( cal, levelTypes, values );

		if ( baseType.equals( WEEK ) )
		{
			timeMembers.add( member );
		}
		else if ( baseType.equals( DAY ) )
		{
			int weekday = cal.get( Calendar.DAY_OF_WEEK );

			int[] tmp;
			Calendar newCal = new GregorianCalendar( );
			for ( int i = 1; i <= weekday; i++ )
			{
				newCal.setTimeInMillis( cal.getTimeInMillis( )
						- ( weekday - i ) * dayTimeInMills );
				tmp = getValueFromCal( newCal, levelTypes );
				TimeMember timeMember = new TimeMember( tmp, levelTypes );
				timeMembers.add( timeMember );
			}

		}

		return timeMembers;
	}

}
