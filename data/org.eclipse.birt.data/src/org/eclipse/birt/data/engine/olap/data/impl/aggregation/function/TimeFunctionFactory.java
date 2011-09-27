package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

public class TimeFunctionFactory
{
	public static IPeriodsFunction createPeriodsToDateFunction( String levelTYpe )
	{
		return null;
	}
	
	public static IPeriodsFunction createTrailingFunction( String levelTYpe, int Offset )
	{
		return new TrailingFunction(levelTYpe, Offset);
	}
	
	public static IParallelPeriod createParallelPeriodFunction( String levelTYpe, int Offset )
	{
		return null;
	}
}
