
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.ReferenceDate;
import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

import testutil.BaseTestCase;

import org.junit.Test;

/**
 * this class test the trailing function,you can refer to TrailingFunction for
 * details.
 * 
 * @author peng.shi
 * 
 */
public class TrailingTest extends BaseTestCase
{
	@Test
    public void testTrailing1( ) throws IOException
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
		IPeriodsFunction periodsFunction = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_YEAR,
				-3 );
		ReferenceDate referenceDate = new ReferenceDate( new Date( 2011, 8, 22 ) );
		( (AbstractMDX) periodsFunction ).setReferenceDate( referenceDate );

		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing2( ) throws IOException
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
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				-3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,22));
		((AbstractMDX)periodsFunction).setReferenceDate( referenceDate );		
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing3( ) throws IOException
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
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_MONTH,
				-3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,22));
		((AbstractMDX)periodsFunction).setReferenceDate( referenceDate );		
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing4( ) throws IOException
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
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,22));
		((AbstractMDX)periodsFunction).setReferenceDate( referenceDate );		
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing5( ) throws IOException
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
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR,
				3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,22));
		((AbstractMDX)periodsFunction).setReferenceDate( referenceDate );		
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing6( ) throws IOException
	{
		int[] values = new int[]{
				2011, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				3 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );

	}
	@Test
    public void testTrailing7( ) throws IOException
	{
		int[] values = new int[]{
				2008, 2, 29
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_YEAR,
				3 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );

	}
	@Test
    public void testTrailing8( ) throws IOException
	{
		int[] values = new int[]{
				2008, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing9( ) throws IOException
	{
		int[] values = new int[]{
				2008, 1, 20
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				-1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing10( ) throws IOException
	{
		int[] values = new int[]{
				2008, 1, 2
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				-1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing11( ) throws IOException
	{
		int[] values = new int[]{
				2008, 2, 20
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				-1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing12( ) throws IOException
	{
		int[] values = new int[]{
				2008, 1, 2, 20
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				-1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing13( ) throws IOException
	{
		int[] values = new int[]{
				2011, 3, 9, 4, 39
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_MONTH,
				-3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,28));
		((AbstractMDX)periodsFunction).setReferenceDate( referenceDate );
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing14( ) throws IOException
	{
		int[] values = new int[]{
				2011, 3
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_YEAR,
				-1 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing15( ) throws IOException
	{
		int[] values = new int[]{
			2011
		};
		String[] types = new String[]{
			TimeMember.TIME_LEVEL_TYPE_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_YEAR,
				-3 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing16( ) throws IOException
	{
		int[] values = new int[]{
			2003
		};
		String[] types = new String[]{
			TimeMember.TIME_LEVEL_TYPE_YEAR
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR,
				-120 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing17( ) throws IOException
	{
		int[] values = new int[]{
			2003
		};
		String[] types = new String[]{
			TimeMember.TIME_LEVEL_TYPE_YEAR
		};
		TimeMember timeMember = new TimeMember( values, types );
		List<TimeMember> timeMembers = TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_DAY_OF_YEAR,
				120 )
				.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing18( ) throws IOException
	{
		int[] values = new int[]{
				2011, 1, 1, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
				-3 );
		ReferenceDate referenceDate = new ReferenceDate ( new Date(2011,8,28));
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing19( ) throws IOException
	{
		int[] values = new int[]{
				2011, 12, 4, 52
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
				3 );
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing20( ) throws IOException
	{
		int[] values = new int[]{
				2010, 12, 5, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
				3 );
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	@Test
    public void testTrailing21( ) throws IOException
	{
		int[] values = new int[]{
				2004, 1, 1, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_QUARTER,
				1 );
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
		printMembers( timeMembers );
		checkOutputFile( );
	}
	
	
	/**
	 * Test for week of year, no any month/quarter output level.
	 * In this case, weeks across year will be printed out, weeks
	 * across month will not be printed out. 
	 * @throws IOException
	 */
	@Test
    public void testTrailing22( ) throws IOException
	{
		int[] values = new int[]{
				2004, 1
		};
		String[] types = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR,
		};
		TimeMember timeMember = new TimeMember( values, types );
		
		IPeriodsFunction periodsFunction =  TimeFunctionFactory.createTrailingFunction( TimeMember.TIME_LEVEL_TYPE_YEAR,
				1 );
		
		List<TimeMember> timeMembers = periodsFunction.getResult( timeMember );
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
