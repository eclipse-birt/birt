package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.ArrayList;
import java.util.List;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.GregorianCalendar;

public class QuarterToDateFunction extends AbstractMDX implements IPeriodsFunction
{

	public List<TimeMember> getResult( TimeMember member )
	{
		String[] levels = member.getLevelType( );
		int[] values = member.getMemberValue( );
		
		Calendar cal = new GregorianCalendar( TimeMemberUtil.getTimeZone( ),
				TimeMemberUtil.getDefaultLocale( ) );
		cal.setMinimalDaysInFirstWeek(1);
		String calculateUnit = this.translateToCal( cal, levels, values ) ;
		
		List<TimeMember> list = new ArrayList<TimeMember>( );
		
		if ( calculateUnit.equals( QUARTER ) )
		{
			TimeMember newMember = new TimeMember( values, levels );
			list.add( newMember );
		}
		else if ( calculateUnit.equals( MONTH ) )
		{
			int month = cal.get( Calendar.MONTH ) + 1;
			int quarter = cal.get( Calendar.MONTH ) / 3 + 1;
			int startMonth = quarter * 3 - 2;
			TimeMember newMember = null;
			for ( int i = startMonth; i <= month; i++ )
			{
				int[] newValues = getValueFromCal( cal, levels );
				newMember = new TimeMember( newValues, levels );
				list.add( newMember );
				cal.add( Calendar.MONTH, -1 );
			}
		}
		else if ( calculateUnit.equals( WEEK ) )
		{
			int weekOfYear = cal.get( Calendar.WEEK_OF_YEAR );
			int quarter = cal.get( Calendar.MONTH ) / 3 + 1;
			int startMonth = quarter * 3 - 2;
			
			Calendar startCal = (Calendar)cal.clone( );
			startCal.set( Calendar.MONTH, startMonth-1 );
			startCal.set( Calendar.DAY_OF_MONTH, 1 );
			int starWeek = startCal.get( Calendar.WEEK_OF_YEAR );
			TimeMember newMember = null;
			for ( int i = starWeek; i <= weekOfYear; i++ )
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
			int quarter = cal.get( Calendar.MONTH ) / 3 + 1;
			int startMonth = quarter * 3 - 2;
			Calendar startCal = (Calendar)cal.clone( );
			startCal.set( Calendar.MONTH, startMonth-1 );
			startCal.set( Calendar.DAY_OF_MONTH, 1 );
			int startDay = startCal.get( Calendar.DAY_OF_YEAR );
			TimeMember newMember = null;
			for ( int i = startDay; i <= dayOfYear; i++ )
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
