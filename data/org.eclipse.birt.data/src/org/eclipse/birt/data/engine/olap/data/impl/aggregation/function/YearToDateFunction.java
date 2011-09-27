package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class YearToDateFunction extends AbstractMDX implements IPeriodsFunction
{
	public List<TimeMember> getResult( TimeMember member )
	{
		String[] levels = member.getLevelType( );
		int[] values = member.getMemberValue( );
		
		Calendar cal = new GregorianCalendar();
		String calculateUnit = this.translateToCal( cal, levels, values ) ;
		
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
		}

		return list;
	}
}
