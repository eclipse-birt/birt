/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *******************************************************************************/

package org.eclipse.birt.chart.script.api.data;

import com.ibm.icu.util.Calendar;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Date
 * Time Data Element</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type defines a single element of date/time data.
 * 
 * <!-- end-model-doc -->
 * 
 */

public interface IDateTimeDataElement extends IDataElement {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds a single date value as a long.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(long)
	 */
	long getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 */
	void setValue(long value);

	/**
	 * A convenient method provided to return the datetime value as a Calendar
	 * 
	 * 
	 * @return calendar date
	 */
	Calendar getValueAsCalendar();
}
