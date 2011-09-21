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

package org.eclipse.birt.report.model.api.elements.structures;

import org.eclipse.birt.report.model.api.SimpleValueHandle;
import org.eclipse.birt.report.model.api.StructureHandle;
import org.eclipse.birt.report.model.api.TimePeriodHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Time period structure.
 * 
 */

public class TimePeriod extends Structure
{

	/**
	 * Name of the structure.
	 */

	public static final String STRUCTURE_NAME = "TimePeriod"; //$NON-NLS-1$

	/**
	 * Name of the member that specifies the time period type, which is one of
	 * following choices: year, quarter, month, week and day.
	 */

	public static final String TIME_PERIOD_TYPE_MEMBER = "timePeriodType"; //$NON-NLS-1$

	/**
	 * Name of the member that specifies the number of the time unit.
	 */
	public static final String NUMBER_OF_UNIT_MEMBER = "numberOfUnit"; //$NON-NLS-1$

	/**
	 * Value of the time period type.
	 */

	protected String timePeriodType = null;

	/**
	 * Value of the number of unit.
	 */
	protected int numberOfUnit = 0;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName( )
	{
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty( String propName )
	{
		if ( TIME_PERIOD_TYPE_MEMBER.equals( propName ) )
			return timePeriodType;
		else if ( NUMBER_OF_UNIT_MEMBER.equals( propName ) )
			return numberOfUnit;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty( String propName, Object value )
	{
		if ( TIME_PERIOD_TYPE_MEMBER.equals( propName ) )
			timePeriodType = (String) value;
		else if ( NUMBER_OF_UNIT_MEMBER.equals( propName ) )
			numberOfUnit = ( (Integer) value ).intValue( );
		else
			assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle( SimpleValueHandle valueHandle, int index )
	{
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */
	public StructureHandle getHandle( SimpleValueHandle valueHandle )
	{
		return new TimePeriodHandle( valueHandle.getElementHandle( ),
				getContext( ) );
	}

	/**
	 * Sets the number of unit.
	 * 
	 * @param number
	 *            the number of the unit to set
	 */

	public void setNumberOfUnit( int number )
	{
		setProperty( NUMBER_OF_UNIT_MEMBER, number );
	}

	/**
	 * Returns the number of unit.
	 * 
	 * @return the number of unit
	 */

	public int getNumberOfUnit( )
	{
		return (Integer) getProperty( null, NUMBER_OF_UNIT_MEMBER );
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
	public void setTimePeriodType( String type )
	{
		setProperty( TIME_PERIOD_TYPE_MEMBER, type );
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
		return (String) getProperty( null, TIME_PERIOD_TYPE_MEMBER );
	}

}
