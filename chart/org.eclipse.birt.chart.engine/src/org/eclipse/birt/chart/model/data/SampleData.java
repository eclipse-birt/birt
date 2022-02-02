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
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Sample
 * Data</b></em>'. <!-- end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This type sample data that will be used to display the chart at design-time.
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.SampleData#getBaseSampleData
 * <em>Base Sample Data</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SampleData#getOrthogonalSampleData
 * <em>Orthogonal Sample Data</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.data.DataPackage#getSampleData()
 * @model
 * @generated
 */
public interface SampleData extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Base Sample Data</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.BaseSampleData}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the sample data for base series in the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Base Sample Data</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSampleData_BaseSampleData()
	 * @model type="org.eclipse.birt.chart.model.data.BaseSampleData"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<BaseSampleData> getBaseSampleData();

	/**
	 * Returns the value of the '<em><b>Orthogonal Sample Data</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.OrthogonalSampleData}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the sample data for orthogonal series in the chart.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Orthogonal Sample Data</em>' containment
	 *         reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSampleData_OrthogonalSampleData()
	 * @model type="org.eclipse.birt.chart.model.data.OrthogonalSampleData"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<OrthogonalSampleData> getOrthogonalSampleData();

	/**
	 * Returns the value of the '<em><b>Ancillary Sample Data</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.BaseSampleData}. <!-- begin-user-doc
	 * -->
	 * <p>
	 * If the meaning of the '<em>Ancillary Sample Data</em>' containment reference
	 * list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Ancillary Sample Data</em>' containment
	 *         reference list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSampleData_AncillarySampleData()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='AncillarySampleData'"
	 * @generated
	 */
	EList<BaseSampleData> getAncillarySampleData();

	/**
	 * @generated
	 */
	SampleData copyInstance();

} // SampleData
