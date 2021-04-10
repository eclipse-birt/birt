package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunctionCreator;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunctionCreatorFactory;

public class TimeFunctionCreatorFactory implements ITimeFunctionCreatorFactory {
	public ITimeFunctionCreator createTimeFunctionCreator() throws BirtException {
		return new TimeFunctionCreator();
	}
}
