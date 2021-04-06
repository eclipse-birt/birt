package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.api.DataEngine;
import org.eclipse.birt.data.engine.cache.Constants;

public class MemoryUsageSetting {
	public static void setMemoryUsage(String memoryUsage) {
		if (memoryUsage == null || memoryUsage.equals(DataEngine.MEMORY_USAGE_NORMAL)) {
			Constants.setNormalMemoryUsage();
		} else if (memoryUsage.equals(DataEngine.MEMORY_USAGE_AGGRESSIVE)) {
			Constants.setAggressiveMemoryUsage();
		} else if (memoryUsage.equals(DataEngine.MEMORY_USAGE_CONSERVATIVE)) {
			Constants.setConservativeMemoryUsage();
		}
	}
}
