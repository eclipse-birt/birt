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

import org.eclipse.birt.chart.model.IChartObject;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Set</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type holds the data associated with a series.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.DataSet#getValues
 * <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getDataSet()
 * @model extendedMetaData="name='DataSet' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface DataSet extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Values</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds data values for the series.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Values</em>' attribute.
	 * @see #setValues(Object)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getDataSet_Values()
	 * @model dataType="org.eclipse.birt.chart.model.data.Data" required="true"
	 *        extendedMetaData="kind='element' name='Values'"
	 * @generated
	 */
	Object getValues();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.DataSet#getValues <em>Values</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Values</em>' attribute.
	 * @see #getValues()
	 * @generated
	 */
	void setValues(Object value);

	/**
	 * @generated
	 */
	@Override
	DataSet copyInstance();

	/**
	 * Checks if the values in data set are big number.
	 *
	 * @return
	 * @since 2.6
	 */
	boolean isBigNumber();

} // DataSet
