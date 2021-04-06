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
import org.eclipse.birt.report.model.core.StructureContext;

/**
 * The structure handle of time interval.
 */

public class TimeIntervalHandle extends StructureHandle {

	/**
	 * Constructs the handle of time interval.
	 * 
	 * @param element the handle of the element which defines the structure
	 * @param context the context of this structure
	 */

	public TimeIntervalHandle(DesignElementHandle element, StructureContext context) {
		super(element, context);
	}

	/**
	 * Sets the measure value.
	 * 
	 * @param measure the measure value to set
	 * @throws SemanticException
	 */

	public void setMeasure(int measure) throws SemanticException {
		setProperty(TimeInterval.MEASURE_MEMBER, measure);
	}

	/**
	 * Returns the measure value.
	 * 
	 * @return the measure value
	 */

	public int getMeasure() {
		return getIntProperty(TimeInterval.MEASURE_MEMBER);
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
	 * @throws SemanticException
	 */
	public void setUnit(String unit) throws SemanticException {
		setProperty(TimeInterval.UNIT_MEMBER, unit);
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
		return getStringProperty(TimeInterval.UNIT_MEMBER);
	}

}
