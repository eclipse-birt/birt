package org.eclipse.birt.data.engine.api.timefunction;

import java.util.List;

import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

public interface IPeriodsFunction {
	// return a set of members of the Time hierarchy
	List<TimeMember> getResult(TimeMember member);
}
