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
package org.eclipse.birt.data.engine.olap.data.impl.aggregation.function;

import org.eclipse.birt.data.engine.api.timefunction.IParallelPeriod;
import org.eclipse.birt.data.engine.api.timefunction.IPeriodsFunction;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunctionCreator;

public class TimeFunctionCreator implements ITimeFunctionCreator {
	public IPeriodsFunction createPeriodsToDateFunction(String levelType, boolean isCurrent) {
		return TimeFunctionFactory.createPeriodsToDateFunction(levelType, isCurrent);
	}

	public IPeriodsFunction createTrailingFunction(String levelType, int Offset) {
		return TimeFunctionFactory.createTrailingFunction(levelType, Offset);
	}

	public IParallelPeriod createParallelPeriodFunction(String levelType, int Offset) {
		return TimeFunctionFactory.createParallelPeriodFunction(levelType, Offset);
	}
}
