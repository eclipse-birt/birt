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
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Base
 * Sample Data</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type sample data for a base series.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation
 * <em>Data Set Representation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getBaseSampleData()
 * @model extendedMetaData="name='BaseSampleData' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface BaseSampleData extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Data Set Representation</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Holds the sample data for a single data set as a string in the form expected
	 * by the DataSetProcessor for the series.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Data Set Representation</em>' attribute.
	 * @see #setDataSetRepresentation(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getBaseSampleData_DataSetRepresentation()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getDataSetRepresentation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.BaseSampleData#getDataSetRepresentation
	 * <em>Data Set Representation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Data Set Representation</em>'
	 *              attribute.
	 * @see #getDataSetRepresentation()
	 * @generated
	 */
	void setDataSetRepresentation(String value);

	/**
	 * @generated
	 */
	@Override
	BaseSampleData copyInstance();

} // BaseSampleData
