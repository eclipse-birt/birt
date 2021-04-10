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

public interface ITimeFunction {
	/**
	 * Set the type of time function.
	 */
	public void setBaseTimePeriod(ITimePeriod timePeriod) throws DataException;

	/**
	 * Get the type of time function.
	 */
	public ITimePeriod getBaseTimePeriod() throws DataException;

	/**
	 * Set the reference date of a time function.
	 */
	public void setTimeDimension(String timeDimension) throws DataException;

	/**
	 * Get the time dimension based on which the time function will be calculated
	 * against.
	 * 
	 * @return
	 * @throws DataException
	 */
	public String getTimeDimension() throws DataException;

	/**
	 * Set the reference date of a time function.
	 * 
	 * @param referenceDate
	 * @throws DataException
	 */
	public void setReferenceDate(IReferenceDate referenceDate) throws DataException;

	/**
	 * Get the reference date of a time function.
	 * 
	 * @return
	 * @throws DataException
	 */
	public IReferenceDate getReferenceDate() throws DataException;

	/**
	 * Set the relative time period of a time function.
	 * 
	 * @param relativeTimePeriod
	 * @throws DataException
	 */
	public void setRelativeTimePeriod(ITimePeriod relativeTimePeriod) throws DataException;

	/**
	 * Get the relative time period of a time function.
	 * 
	 * @return
	 * @throws DataException
	 */
	public ITimePeriod getRelativeTimePeriod() throws DataException;
}
