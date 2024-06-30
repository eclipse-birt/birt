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

import org.eclipse.birt.data.engine.core.DataException;

public interface ITimeFunction {
	/**
	 * Set the type of time function.
	 */
	void setBaseTimePeriod(ITimePeriod timePeriod) throws DataException;

	/**
	 * Get the type of time function.
	 */
	ITimePeriod getBaseTimePeriod() throws DataException;

	/**
	 * Set the reference date of a time function.
	 */
	void setTimeDimension(String timeDimension) throws DataException;

	/**
	 * Get the time dimension based on which the time function will be calculated
	 * against.
	 *
	 * @return
	 * @throws DataException
	 */
	String getTimeDimension() throws DataException;

	/**
	 * Set the reference date of a time function.
	 *
	 * @param referenceDate
	 * @throws DataException
	 */
	void setReferenceDate(IReferenceDate referenceDate) throws DataException;

	/**
	 * Get the reference date of a time function.
	 *
	 * @return
	 * @throws DataException
	 */
	IReferenceDate getReferenceDate() throws DataException;

	/**
	 * Set the relative time period of a time function.
	 *
	 * @param relativeTimePeriod
	 * @throws DataException
	 */
	void setRelativeTimePeriod(ITimePeriod relativeTimePeriod) throws DataException;

	/**
	 * Get the relative time period of a time function.
	 *
	 * @return
	 * @throws DataException
	 */
	ITimePeriod getRelativeTimePeriod() throws DataException;
}
