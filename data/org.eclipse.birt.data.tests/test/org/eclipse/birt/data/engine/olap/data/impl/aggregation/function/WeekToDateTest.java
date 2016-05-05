
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.function.WeekToDateFunciton;

import testutil.BaseTestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;
import static org.junit.Assert.*;

/**
 * this class test week to date function, you can refer to WeekToDateFunction
 * for details
 * 
 * @author peng.shi
 * 
 */
public class WeekToDateTest extends BaseTestCase
{
	@Test
    public void testWTD1( ) throws IOException
	{
		int[] values = new int[]{
				2011, 3, 9, 4, 39, 5, 22, 265
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR
		};
		TimeMember timeMember = new TimeMember( values, types );
		ReferenceDate referenceDate = new ReferenceDate(new Date(2011,8,22));
		WeekToDateFunciton weekToDate = new WeekToDateFunciton( );
		((AbstractMDX)weekToDate).setReferenceDate( referenceDate );
		List<TimeMember> timeMembers = weekToDate.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testWTD2( ) throws IOException
	{
		int[] values = new int[]{
				2011, 20, 5
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK
		};
		
		ReferenceDate referenceDate = new ReferenceDate(new Date(2011,4,19));
		WeekToDateFunciton weekToDate = new WeekToDateFunciton( );
		((AbstractMDX)weekToDate).setReferenceDate( referenceDate );
		
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = weekToDate.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testWTD3( ) throws IOException
	{
		int[] values = new int[]{
				2002, 1, 2, 1, 7
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_WEEK
		};
		
		ReferenceDate referenceDate = new ReferenceDate(new Date(2002,1,9));
		WeekToDateFunciton weekToDate = new WeekToDateFunciton( );
		((AbstractMDX)weekToDate).setReferenceDate( referenceDate );
		
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = weekToDate.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testWTD4( ) throws IOException
	{
		int[] values = new int[]{
				2011, 1, 2, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH
		};
		
		ReferenceDate referenceDate = new ReferenceDate(new Date(2011,1,5));
		WeekToDateFunciton weekToDate = new WeekToDateFunciton( );
		((AbstractMDX)weekToDate).setReferenceDate( referenceDate );
		
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = weekToDate.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}

	private void printMembers( List<TimeMember> timeMembers )
	{
		String[] levelTypes;
		int[] memberValues;
		for ( TimeMember timeMember : timeMembers )
		{
			levelTypes = timeMember.getLevelType( );
			memberValues = timeMember.getMemberValue( );
			for ( int i = 0; i < levelTypes.length; i++ )
			{
				testPrint( levelTypes[i] + " " );
			}
			testPrintln( "" );
			for ( int i = 0; i < memberValues.length; i++ )
			{
				testPrint( memberValues[i] + " " );
			}
			testPrintln( "" );
		}
	}
}
