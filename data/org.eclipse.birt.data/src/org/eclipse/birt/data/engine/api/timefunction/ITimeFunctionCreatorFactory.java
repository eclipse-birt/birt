package org.eclipse.birt.data.engine.api.timefunction;

import org.eclipse.birt.core.exception.BirtException;

public interface ITimeFunctionCreatorFactory {
	public static final String EXTENSION_TIME_FUNCTION_CREATOR_FACTORY = "org.eclipse.birt.data.TimeFunctionCreatorFactory";

	ITimeFunctionCreator createTimeFunctionCreator() throws BirtException;
}
