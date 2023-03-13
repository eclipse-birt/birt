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

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.RiserType;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Bar
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that, during design time,
 * holds the query data for Bar charts, and during run time, holds the value for
 * each riser in the series. When rendered, a riser extends from the x-axis to
 * the value of each data point. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.BarSeries#getRiser
 * <em>Riser</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline
 * <em>Riser Outline</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getBarSeries()
 * @model extendedMetaData="name='BarSeries' kind='elementOnly'"
 * @generated
 */
public interface BarSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Riser</b></em>' attribute. The default value
	 * is <code>"Rectangle"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.RiserType}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies the 'Riser' to be used for displaying the data values in the chart.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Riser</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see #isSetRiser()
	 * @see #unsetRiser()
	 * @see #setRiser(RiserType)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getBarSeries_Riser()
	 * @model default="Rectangle" unique="false" unsettable="true"
	 * @generated
	 */
	RiserType getRiser();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiser <em>Riser</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Riser</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.RiserType
	 * @see #isSetRiser()
	 * @see #unsetRiser()
	 * @see #getRiser()
	 * @generated
	 */
	void setRiser(RiserType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiser <em>Riser</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetRiser()
	 * @see #getRiser()
	 * @see #setRiser(RiserType)
	 * @generated
	 */
	void unsetRiser();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiser <em>Riser</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Riser</em>' attribute is set.
	 * @see #unsetRiser()
	 * @see #getRiser()
	 * @see #setRiser(RiserType)
	 * @generated
	 */
	boolean isSetRiser();

	/**
	 * Returns the value of the '<em><b>Riser Outline</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 *
	 * Defines the color to be used for the Riser outline.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Riser Outline</em>' containment reference.
	 * @see #setRiserOutline(ColorDefinition)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getBarSeries_RiserOutline()
	 * @model containment="true" resolveProxies="false"
	 * @generated
	 */
	ColorDefinition getRiserOutline();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BarSeries#getRiserOutline <em>Riser
	 * Outline</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @param value the new value of the '<em>Riser Outline</em>' containment
	 *              reference.
	 * @see #getRiserOutline()
	 * @generated
	 */
	void setRiserOutline(ColorDefinition value);

	/**
	 * @generated
	 */
	@Override
	BarSeries copyInstance();

} // BarSeries
