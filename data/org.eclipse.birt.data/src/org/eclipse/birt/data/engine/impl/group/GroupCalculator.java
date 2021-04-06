
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.data.engine.impl.group;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * This calculator is used to calculate a group key basing group interval.
 */

abstract class GroupCalculator implements ICalculator {
	protected Object intervalStart;
	protected double intervalRange;

	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public GroupCalculator(Object intervalStart, double intervalRange) throws DataException {
		assert intervalRange >= 0;
		this.intervalStart = intervalStart;
		this.intervalRange = intervalRange;
	}
}
