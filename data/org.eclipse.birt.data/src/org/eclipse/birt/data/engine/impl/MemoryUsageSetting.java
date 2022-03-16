/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/
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
