
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

import java.util.Date;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;

/**
 * This calculator is used to calculate a datetime group key basing group interval.
 */

abstract class DateGroupCalculator extends GroupCalculator
{
	
	static protected Date defaultStart = new Date(70,0,1);
	
	/**
	 * 
	 * @param intervalStart
	 * @param intervalRange
	 * @throws BirtException
	 */
	public DateGroupCalculator(Object intervalStart, double intervalRange) throws BirtException
	{
		super( intervalStart, intervalRange );
		if ( intervalStart != null )
			this.intervalStart = DataTypeUtil.toDate( intervalStart );
	}
	
	/**
	 * 
	 * @return
	 */
	protected int getDateIntervalRange()
	{
		return (int)Math.round( intervalRange );
	}
}
