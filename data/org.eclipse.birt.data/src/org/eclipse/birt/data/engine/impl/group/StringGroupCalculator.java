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
 * This calculator is used to calculate a string group key basing group
 * interval.
 */
class StringGroupCalculator extends GroupCalculator {

	private int interval;

	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public StringGroupCalculator(Object intervalStart, double intervalRange) throws DataException {
		super(intervalStart, intervalRange);
		interval = (int) (Math.round(intervalRange));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.impl.group.GroupCalculator#calculate(java.lang.
	 * Object)
	 */
	public Object calculate(Object value) throws BirtException {
		if (value == null || value.equals("")) {
			return value;
		}

		if (value.toString().length() <= interval)
			return value;

		return value.toString().substring(0, interval);
	}
}
