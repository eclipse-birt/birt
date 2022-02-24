/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data;

import org.eclipse.birt.chart.util.CDateTime;

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
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getDateTimeDataElement()
 * @model extendedMetaData="name='DateTimeDataElement' kind='elementOnly'"
 * @generated
 */
public interface DateTimeDataElement extends DataElement {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds a single date value as a long.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #setValue(long)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getDateTimeDataElement_Value()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Long"
	 *        required="true" extendedMetaData="kind='element' name='Value'"
	 * @generated
	 */
	long getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #isSetValue()
	 * @see #unsetValue()
	 * @see #getValue()
	 * @generated
	 */
	void setValue(long value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetValue()
	 * @see #getValue()
	 * @see #setValue(long)
	 * @generated
	 */
	void unsetValue();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.data.DateTimeDataElement#getValue
	 * <em>Value</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @return whether the value of the '<em>Value</em>' attribute is set.
	 * @see #unsetValue()
	 * @see #getValue()
	 * @see #setValue(long)
	 * @generated
	 */
	boolean isSetValue();

	/**
	 * A convenient method provided to return the datetime value as a Calendar
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	Calendar getValueAsCalendar();

	/**
	 * A convenient method provided to return the datetime value as a CDateTime
	 * wrapper
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	CDateTime getValueAsCDateTime();

	/**
	 * @generated
	 */
	DateTimeDataElement copyInstance();

} // DateTimeDataElement
