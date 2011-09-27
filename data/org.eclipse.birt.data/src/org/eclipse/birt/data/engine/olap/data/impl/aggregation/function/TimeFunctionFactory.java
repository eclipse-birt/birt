package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

public class TimeFunctionFactory
{
	public static IPeriodsFunction createPeriodsToDateFunction( String levelType )
	{
		if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_YEAR ) )
			return new YearToDateFunction( );
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_QUARTER ) )
			return new QuarterToDateFunction( );
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_MONTH ) )
			return new MonthToDateFunction( );
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_MONTH ) )
			return new WeekToDateFunciton( );
		else if ( levelType.equals( TimeMember.TIME_LEVEL_TYPE_WEEK_OF_YEAR ) )
			return new WeekToDateFunciton( );
		return null;
	}
	
	public static IPeriodsFunction createTrailingFunction( String levelType, int Offset )
	{
		return new TrailingFunction( levelType, Offset );
	}
	
	public static IParallelPeriod createParallelPeriodFunction( String levelType, int Offset )
	{
		return new PreviousNPeriodsFunction( levelType, Offset );
	}
}
