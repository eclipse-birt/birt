package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import java.util.List;


public interface IPeriodsFunction
{
	//return a set of members of the Time hierarchy
	List<TimeMember> getResult( TimeMember member );		
}
