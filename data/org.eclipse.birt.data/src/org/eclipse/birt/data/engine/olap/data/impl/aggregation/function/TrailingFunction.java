
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * The Trailing N Period function returns a set of consecutive members that
 * include a specified member and can be located by the member and the Offset
 * number.Now we support trailing in 5 level, Year, Quarter, Month, Week, Day
 * 
 * @author peng.shi
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

	@Override
	public List<TimeMember> getResult( TimeMember member )
	{
		List<TimeMember> timeMembers = new ArrayList<TimeMember>( );

		String[] levelTypes = member.getLevelType( );
		int[] values = member.getMemberValue( );

		Calendar cal = new GregorianCalendar( );
		translateToCal( cal, levelTypes, values );

		timeMembers.add( member );
		int[] tmp;
		for ( int i = 0; i < offset - 1; i++ )
		{
			if ( levelType.equals( YEAR ) )
			{
				cal.add( Calendar.YEAR, -1 );
			}
			else if ( levelType.equals( MONTH ) )
			{
				cal.add( Calendar.MONTH, -1 );
			}
			else if ( levelType.equals( QUARTER ) )
			{
				// Calendar not support quarter, so add 3 month
				cal.add( Calendar.MONTH, -3 );
			}
			else if ( levelType.equals( WEEK ) )
			{
				cal.add( Calendar.WEEK_OF_YEAR, -1 );
			}
			else if ( levelType.equals( DAY ) )
			{
				cal.add( Calendar.DATE, -1 );
			}

			tmp = getValueFromCal( cal, levelTypes );

			TimeMember timeMember = new TimeMember( tmp, levelTypes );
			timeMembers.add( timeMember );
		}

		// sort the member by ascending in time dimension
		List<TimeMember> newTimeMemebers = new ArrayList<TimeMember>( );
		for ( int i = timeMembers.size( ) - 1; i >= 0; i-- )
		{
			newTimeMemebers.add( timeMembers.get( i ) );
		}

		return newTimeMemebers;

	}

}
