/*
 *************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
