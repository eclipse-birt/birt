package org.eclipse.birt.data.engine.api.timefunction;

public interface ITimeFunctionCreator {
	IPeriodsFunction createPeriodsToDateFunction(String levelType, boolean isCurrent);

	IPeriodsFunction createTrailingFunction(String levelType, int Offset);

	IParallelPeriod createParallelPeriodFunction(String levelType, int Offset);
}
