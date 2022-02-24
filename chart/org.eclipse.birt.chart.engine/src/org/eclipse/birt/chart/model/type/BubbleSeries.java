/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Orientation;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Bubble
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that holds data for Bubble
 * Charts. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccLineAttributes
 * <em>Acc Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation
 * <em>Acc Orientation</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getBubbleSeries()
 * @model extendedMetaData="name='BubbleSeries' kind='elementOnly'"
 * @generated
 */
public interface BubbleSeries extends ScatterSeries {

	/**
	 * Returns the value of the '<em><b>Acc Line Attributes</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * Specifies the attributes for the line used to represent the acceleration line
	 * to the Bubble.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Acc Line Attributes</em>' containment
	 *         reference.
	 * @see #setAccLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getBubbleSeries_AccLineAttributes()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='AccLineAttributes'"
	 * @generated
	 */
	LineAttributes getAccLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccLineAttributes
	 * <em>Acc Line Attributes</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Acc Line Attributes</em>' containment
	 *              reference.
	 * @see #getAccLineAttributes()
	 * @generated
	 */
	void setAccLineAttributes(LineAttributes value);

	/**
	 * Returns the value of the '<em><b>Acc Orientation</b></em>' attribute. The
	 * default value is <code>"Horizontal"</code>. The literals are from the
	 * enumeration {@link org.eclipse.birt.chart.model.attribute.Orientation}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the orientation of the acceleration line. <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Acc Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetAccOrientation()
	 * @see #unsetAccOrientation()
	 * @see #setAccOrientation(Orientation)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getBubbleSeries_AccOrientation()
	 * @model default="Horizontal" unsettable="true"
	 *        extendedMetaData="kind='element' name='AccOrientation'"
	 * @generated
	 */
	Orientation getAccOrientation();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation
	 * <em>Acc Orientation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Acc Orientation</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.Orientation
	 * @see #isSetAccOrientation()
	 * @see #unsetAccOrientation()
	 * @see #getAccOrientation()
	 * @generated
	 */
	void setAccOrientation(Orientation value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation
	 * <em>Acc Orientation</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetAccOrientation()
	 * @see #getAccOrientation()
	 * @see #setAccOrientation(Orientation)
	 * @generated
	 */
	void unsetAccOrientation();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.type.BubbleSeries#getAccOrientation
	 * <em>Acc Orientation</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Acc Orientation</em>' attribute is set.
	 * @see #unsetAccOrientation()
	 * @see #getAccOrientation()
	 * @see #setAccOrientation(Orientation)
	 * @generated
	 */
	boolean isSetAccOrientation();

	/**
	 * @generated
	 */
	BubbleSeries copyInstance();

} // BubbleSeries
