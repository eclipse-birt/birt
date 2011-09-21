/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.structures.TimeInterval;
import org.eclipse.birt.report.model.api.elements.structures.TimePeriod;
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * The structure handle of TimePeriod.
 */

public class TimePeriodHandle extends StructureHandle
{

	/**
	 * Constructs the handle of time period.
	 * 
	 * @param element
	 *            the handle of the element which defines the structure
	 * @param context
	 *            the context of this structure
	 */

	public TimePeriodHandle( DesignElementHandle element,
			StructureContext context )
	{
		super( element, context );
	}

	/**
	 * Sets the number of unit.
	 * 
	 * @param number
	 *            the number of the unit to set
	 */

	public void setNumberOfUnit( int number ) throws SemanticException
	{
		setProperty( TimePeriod.NUMBER_OF_UNIT_MEMBER, number );
	}

	/**
	 * Returns the number of unit.
	 * 
	 * @return the number of unit
	 */

	public int getNumberOfUnit( )
	{
		return getIntProperty( TimePeriod.NUMBER_OF_UNIT_MEMBER );
	}

	/**
	 * Sets the time period type. The value can one of the following value
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * </ul>
	 * 
	 * @param type
	 *            the time period type to set
	 */
	public void setTimePeriodType( String type ) throws SemanticException
	{
		setProperty( TimePeriod.TIME_PERIOD_TYPE_MEMBER, type );
	}

	/**
	 * Returns the time period type. The value can be one of the following value
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_YEAR</code>
	 * <li><code>INTERVAL_QUARTER</code>
	 * <li><code>INTERVAL_MONTH</code>
	 * <li><code>INTERVAL_WEEK</code>
	 * <li><code>INTERVAL_DAY</code>
	 * </ul>
	 * 
	 * @return the time period type
	 */
	public String getTimePeriodType( )
	{
		return getStringProperty( TimePeriod.TIME_PERIOD_TYPE_MEMBER );
	}

}
