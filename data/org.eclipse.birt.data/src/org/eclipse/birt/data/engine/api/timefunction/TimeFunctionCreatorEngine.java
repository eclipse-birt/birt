package org.eclipse.birt.data.engine.api.timefunction;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;

public class TimeFunctionCreatorEngine {

	public static ITimeFunctionCreator newTimeFunctionCreator() throws BirtException {
		Platform.startup(null);

		Object factory = Platform
				.createFactoryObject(ITimeFunctionCreatorFactory.EXTENSION_TIME_FUNCTION_CREATOR_FACTORY);
		if (factory instanceof ITimeFunctionCreatorFactory) {
			return ((ITimeFunctionCreatorFactory) factory).createTimeFunctionCreator();
		} else {
			throw new DataException(ResourceConstants.LOAD_FACTORY_ERROR);
		}
	}
}
