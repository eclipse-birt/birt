/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.data.engine.api.timefunction;

public interface ITimePeriod {
	/**
	 * Represent the number of time period, say, the "N" in "last N Year".
	 * 
	 * @return
	 */
	public int countOfUnit();

	/**
	 * Represent the basic unit of a time period.
	 * 
	 * @return
	 */
	public TimePeriodType getType();

	/**
	 * Represent whether it should be calculated in the scope of a current period
	 * 
	 * @return
	 */
	public boolean isCurrent();
}
