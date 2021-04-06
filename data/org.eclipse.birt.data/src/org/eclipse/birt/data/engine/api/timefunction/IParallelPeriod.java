package org.eclipse.birt.data.engine.api.timefunction;

import org.eclipse.birt.data.engine.api.timefunction.TimeMember;

public interface IParallelPeriod {
	// return a members of the Time hierarchy
	TimeMember getResult(TimeMember member);
}
