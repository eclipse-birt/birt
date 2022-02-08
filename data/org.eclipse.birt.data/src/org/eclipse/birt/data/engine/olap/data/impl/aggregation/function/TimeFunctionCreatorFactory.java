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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunctionCreator;
import org.eclipse.birt.data.engine.api.timefunction.ITimeFunctionCreatorFactory;

public class TimeFunctionCreatorFactory implements ITimeFunctionCreatorFactory {
	public ITimeFunctionCreator createTimeFunctionCreator() throws BirtException {
		return new TimeFunctionCreator();
	}
}
