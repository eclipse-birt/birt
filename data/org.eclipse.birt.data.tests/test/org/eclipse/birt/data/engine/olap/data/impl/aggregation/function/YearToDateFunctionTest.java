
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.io.IOException;
import java.util.List;

import testutil.BaseTestCase;

public class YearToDateFunctionTest extends BaseTestCase
{

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown( ) throws Exception
	{
		super.tearDown( );
	}

	private void printResult( List<TimeMember> resultMember )
			throws IOException
	{
		for ( int i = 0; i < resultMember.size( ); i++ )
		{
			int[] resultValues = resultMember.get( i ).getMemberValue( );
			for ( int j = 0; j < resultValues.length; j++ )
			{
				testPrint( String.valueOf( resultValues[j]) );
				testPrint( " " );
			}
			testPrint( "\n" );
		}
	}

	public void testFunctions( ) throws IOException
	{
		int[] values = new int[]{
			2002
		};
		String[] levels = new String[]{
			TimeMember.TIME_LEVEL_TYPE_YEAR,
		};
		TimeMember member = new TimeMember( values, levels );

		List<TimeMember> resultMember = TimeFunctionFactory.createPeriodsToDateFunction( TimeMember.TIME_LEVEL_TYPE_YEAR )
				.getResult( member );

		printResult( resultMember );
		this.checkOutputFile( );
	}

	public void testFunctions1( ) throws IOException
	{
		int[] values = new int[]{
				2002, 8
		};
		String[] levels = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_MONTH
		};
		TimeMember member = new TimeMember( values, levels );

		List<TimeMember> resultMember = TimeFunctionFactory.createPeriodsToDateFunction( TimeMember.TIME_LEVEL_TYPE_YEAR )
				.getResult( member );
		printResult( resultMember );
		this.checkOutputFile( );
	}

	public void testFunctions2( ) throws IOException
	{
		int[] values = new int[]{
				2002, 3, 9
		};
		String[] levels = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH
		};
		TimeMember member = new TimeMember( values, levels );

		List<TimeMember> resultMember = TimeFunctionFactory.createPeriodsToDateFunction( TimeMember.TIME_LEVEL_TYPE_YEAR )
				.getResult( member );
		printResult( resultMember );
		this.checkOutputFile( );
	}

	public void testFunctions3( ) throws IOException
	{
		int[] values = new int[]{
				2004, 3, 8, 18
		};
		String[] levels = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_DAY_OF_MONTH
		};
		TimeMember member = new TimeMember( values, levels );

		List<TimeMember> resultMember = TimeFunctionFactory.createPeriodsToDateFunction( TimeMember.TIME_LEVEL_TYPE_YEAR )
				.getResult( member );

		printResult( resultMember );
		this.checkOutputFile( );
	}

	public void testFunctions4( ) throws IOException
	{
		int[] values = new int[]{
				2004, 3, 8, 4
		};
		String[] levels = new String[]{
				TimeMember.TIME_LEVEL_TYPE_YEAR,
				TimeMember.TIME_LEVEL_TYPE_QUARTER,
				TimeMember.TIME_LEVEL_TYPE_MONTH,
				TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH
		};
		TimeMember member = new TimeMember( values, levels );

		List<TimeMember> resultMember = TimeFunctionFactory.createPeriodsToDateFunction( TimeMember.TIME_LEVEL_TYPE_YEAR )
				.getResult( member );
		printResult( resultMember );
		this.checkOutputFile( );
	}
}
