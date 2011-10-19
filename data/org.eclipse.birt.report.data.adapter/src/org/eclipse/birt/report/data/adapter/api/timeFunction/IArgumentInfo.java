/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.adapter.api.timeFunction;

import java.util.List;

public interface IArgumentInfo 
{
	/**
	 * Arguments for time function
	 */
	public static final String PERIOD_1 = "Period1";
	public static final String PERIOD_2 = "Period2";
	public static final String N_PERIOD1 = "N for Period1";
	public static final String N_PERIOD2 = "N for Period2";
	
	/**
	 * Period choice for period argument
	 *
	 */
	public enum Period_Type { YEAR, QUARTER, MONTH, WEEK, DAY };

	/**
	 * Get argument name for time function
	 * @return
	 */
	public String getName( );
	
	/**
	 * Is this argument required for this time function
	 * @return
	 */
	public boolean isOptional( );
	
	/**
	 * Available value choices for this time function
	 * @return
	 */
	public List<Period_Type> getPeriodChoices( );
	
	/**
	 * Get description for this argument
	 * @return
	 */
	public String getDescription( );
}
