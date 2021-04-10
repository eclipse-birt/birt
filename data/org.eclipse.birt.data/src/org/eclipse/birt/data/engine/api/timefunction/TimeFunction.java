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

import org.eclipse.birt.data.engine.core.DataException;

public class TimeFunction implements ITimeFunction {
	private ITimePeriod baseTimePeriod, relativeTimePeriod;
	private String timeDimension;
	private IReferenceDate referenceDate;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#setBaseTimePeriod
	 * (org.eclipse.birt.data.engine.api.timefunction.ITimePeriod)
	 */
	public void setBaseTimePeriod(ITimePeriod timePeriod) throws DataException {
		this.baseTimePeriod = timePeriod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#getBaseTimePeriod
	 * ()
	 */
	public ITimePeriod getBaseTimePeriod() throws DataException {
		return this.baseTimePeriod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#setTimeDimension(
	 * java.lang.String)
	 */
	public void setTimeDimension(String timeDimension) throws DataException {
		this.timeDimension = timeDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#getTimeDimension(
	 * )
	 */
	public String getTimeDimension() throws DataException {
		return this.timeDimension;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#setReferenceDate(
	 * org.eclipse.birt.data.engine.api.timefunction.IReferenceDate)
	 */
	public void setReferenceDate(IReferenceDate referenceDate) throws DataException {
		this.referenceDate = referenceDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#getReferenceDate(
	 * )
	 */
	public IReferenceDate getReferenceDate() throws DataException {
		return this.referenceDate;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#
	 * setRelativeTimePeriod(org.eclipse.birt.data.engine.api.timefunction.
	 * ITimePeriod)
	 */
	public void setRelativeTimePeriod(ITimePeriod relativeTimePeriod) throws DataException {
		this.relativeTimePeriod = relativeTimePeriod;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.api.timefunction.ITimeFunction#
	 * getRelativeTimePeriod()
	 */
	public ITimePeriod getRelativeTimePeriod() throws DataException {
		return this.relativeTimePeriod;
	}

}
