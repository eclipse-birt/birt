package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;


public interface IParallelPeriod
{
	//return a members of the Time hierarchy
	TimeMember getResult( TimeMember member );
}
