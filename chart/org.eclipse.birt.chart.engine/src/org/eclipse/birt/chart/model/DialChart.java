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

package org.eclipse.birt.chart.model;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Dial
 * Chart</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> DialChart represent a special type of
 * ChartWithoutAxes class that contains information specific to dials.
 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
 * To create an instance use the factory method:<br/>
 * DialChartImpl.create( );
 * </p>
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.DialChart#isDialSuperimposition
 * <em>Dial Superimposition</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.ModelPackage#getDialChart()
 * @model extendedMetaData="name='DialChart' kind='elementOnly'"
 * @generated
 */
public interface DialChart extends ChartWithoutAxes {

	/**
	 * Returns the value of the '<em><b>Dial Superimposition</b></em>' attribute.
	 * The default value is <code>"true"</code>. <!-- begin-user-doc --> <!--
	 * end-user-doc --> <!-- begin-model-doc --> The boolean attribute
	 * "DialSuperimposition" specifies whether all dials are superimposed on one
	 * another, or laid out in separate cells. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Dial Superimposition</em>' attribute.
	 * @see #isSetDialSuperimposition()
	 * @see #unsetDialSuperimposition()
	 * @see #setDialSuperimposition(boolean)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getDialChart_DialSuperimposition()
	 * @model default="true" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        extendedMetaData="kind='element' name='DialSuperimposition'"
	 * @generated
	 */
	boolean isDialSuperimposition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.DialChart#isDialSuperimposition <em>Dial
	 * Superimposition</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Dial Superimposition</em>' attribute.
	 * @see #isSetDialSuperimposition()
	 * @see #unsetDialSuperimposition()
	 * @see #isDialSuperimposition()
	 * @generated
	 */
	void setDialSuperimposition(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.DialChart#isDialSuperimposition <em>Dial
	 * Superimposition</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @see #isSetDialSuperimposition()
	 * @see #isDialSuperimposition()
	 * @see #setDialSuperimposition(boolean)
	 * @generated
	 */
	void unsetDialSuperimposition();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.DialChart#isDialSuperimposition <em>Dial
	 * Superimposition</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Dial Superimposition</em>' attribute is
	 *         set.
	 * @see #unsetDialSuperimposition()
	 * @see #isDialSuperimposition()
	 * @see #setDialSuperimposition(boolean)
	 * @generated
	 */
	boolean isSetDialSuperimposition();

	/**
	 * @generated
	 */
	@Override
	DialChart copyInstance();

} // DialChart
