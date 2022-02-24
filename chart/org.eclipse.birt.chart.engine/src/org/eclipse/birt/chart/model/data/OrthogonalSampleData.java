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
 * '<em><b>Orthogonal Sample Data</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type sample data for an orthogonal series.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation
 * <em>Data Set Representation</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex
 * <em>Series Definition Index</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getOrthogonalSampleData()
 * @model extendedMetaData="name='OrthogonalSampleData' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface OrthogonalSampleData extends IChartObject {

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
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getOrthogonalSampleData_DataSetRepresentation()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getDataSetRepresentation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getDataSetRepresentation
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
	 * Returns the value of the '<em><b>Series Definition Index</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Holds the index for the series definition for which this sample data is
	 * applicable.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series Definition Index</em>' attribute.
	 * @see #isSetSeriesDefinitionIndex()
	 * @see #unsetSeriesDefinitionIndex()
	 * @see #setSeriesDefinitionIndex(int)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getOrthogonalSampleData_SeriesDefinitionIndex()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	int getSeriesDefinitionIndex();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex
	 * <em>Series Definition Index</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Series Definition Index</em>'
	 *              attribute.
	 * @see #isSetSeriesDefinitionIndex()
	 * @see #unsetSeriesDefinitionIndex()
	 * @see #getSeriesDefinitionIndex()
	 * @generated
	 */
	void setSeriesDefinitionIndex(int value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex
	 * <em>Series Definition Index</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetSeriesDefinitionIndex()
	 * @see #getSeriesDefinitionIndex()
	 * @see #setSeriesDefinitionIndex(int)
	 * @generated
	 */
	void unsetSeriesDefinitionIndex();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.OrthogonalSampleData#getSeriesDefinitionIndex
	 * <em>Series Definition Index</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Series Definition Index</em>' attribute
	 *         is set.
	 * @see #unsetSeriesDefinitionIndex()
	 * @see #getSeriesDefinitionIndex()
	 * @see #setSeriesDefinitionIndex(int)
	 * @generated
	 */
	boolean isSetSeriesDefinitionIndex();

	/**
	 * @generated
	 */
	OrthogonalSampleData copyInstance();

} // OrthogonalSampleData
