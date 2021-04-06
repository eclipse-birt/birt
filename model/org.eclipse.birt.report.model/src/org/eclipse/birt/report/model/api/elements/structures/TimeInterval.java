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
import org.eclipse.birt.report.model.api.TimeIntervalHandle;
import org.eclipse.birt.report.model.core.Structure;

/**
 * Time interval structure.
 * 
 */

public class TimeInterval extends Structure {

	/**
	 * Name of the structure.
	 */

	public static final String STRUCTURE_NAME = "TimeInterval"; //$NON-NLS-1$

	/**
	 * Name of the measure member.
	 */

	public static final String MEASURE_MEMBER = "measure"; //$NON-NLS-1$

	/**
	 * Name of the unit member.
	 */
	public static final String UNIT_MEMBER = "unit"; //$NON-NLS-1$

	/**
	 * Value of the measure.
	 */

	protected int measure = 0;

	/**
	 * Value of the unit.
	 */
	protected String unit = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.IStructure#getStructName()
	 */

	public String getStructName() {
		return STRUCTURE_NAME;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getIntrinsicProperty(java
	 * .lang.String)
	 */

	protected Object getIntrinsicProperty(String propName) {
		if (MEASURE_MEMBER.equals(propName))
			return measure;
		else if (UNIT_MEMBER.equals(propName))
			return unit;

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#setIntrinsicProperty(java
	 * .lang.String, java.lang.Object)
	 */

	protected void setIntrinsicProperty(String propName, Object value) {
		if (MEASURE_MEMBER.equals(propName))
			measure = ((Integer) value).intValue();
		else if (UNIT_MEMBER.equals(propName))
			unit = (String) value;
		else
			assert false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#handle(org.eclipse.birt.
	 * report.model.api.SimpleValueHandle, int)
	 */
	protected StructureHandle handle(SimpleValueHandle valueHandle, int index) {
		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.core.Structure#getHandle(org.eclipse.birt
	 * .report.model.api.SimpleValueHandle)
	 */
	public StructureHandle getHandle(SimpleValueHandle valueHandle) {
		return new TimeIntervalHandle(valueHandle.getElementHandle(), getContext());
	}

	/**
	 * Sets the measure value.
	 * 
	 * @param measure the measure value to set
	 */

	public void setMeasure(int measure) {
		setProperty(MEASURE_MEMBER, measure);
	}

	/**
	 * Returns the measure value.
	 * 
	 * @return the measure value
	 */

	public int getMeasure() {
		return (Integer) getProperty(null, MEASURE_MEMBER);
	}

	/**
	 * Sets the unit of the time interval. The value can one of the following value
	 * defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * </ul>
	 * 
	 * @param unit the unit to set
	 */
	public void setUnit(String unit) {
		setProperty(UNIT_MEMBER, unit);
	}

	/**
	 * Returns the unit of the time interval. The value can be one of the following
	 * value defined in <code>DesignChoiceConstants</code>:
	 * 
	 * <ul>
	 * <li><code>INTERVAL_SECOND</code>
	 * <li><code>INTERVAL_MINUTE</code>
	 * <li><code>INTERVAL_HOUR</code>
	 * </ul>
	 * 
	 * @return the unit
	 */
	public String getUnit() {
		return (String) getProperty(null, UNIT_MEMBER);
	}

}
