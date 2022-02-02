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

import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Series;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Dial
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that holds data for Dial
 * Charts. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.DialSeries#getDial
 * <em>Dial</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.DialSeries#getNeedle
 * <em>Needle</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getDialSeries()
 * @model extendedMetaData="name='DialSeries' kind='elementOnly'"
 * @generated
 */
public interface DialSeries extends Series {

	/**
	 * Returns the value of the '<em><b>Dial</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the Dial for this series <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Dial</em>' containment reference.
	 * @see #setDial(Dial)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDialSeries_Dial()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Dial'"
	 * @generated
	 */
	Dial getDial();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries#getDial <em>Dial</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Dial</em>' containment reference.
	 * @see #getDial()
	 * @generated
	 */
	void setDial(Dial value);

	/**
	 * Returns the value of the '<em><b>Needle</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Defines the
	 * needle to be used in the Dial <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Needle</em>' containment reference.
	 * @see #setNeedle(Needle)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDialSeries_Needle()
	 * @model containment="true" required="true" extendedMetaData="kind='element'
	 *        name='Needle'"
	 * @generated
	 */
	Needle getNeedle();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.DialSeries#getNeedle
	 * <em>Needle</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Needle</em>' containment reference.
	 * @see #getNeedle()
	 * @generated
	 */
	void setNeedle(Needle value);

	/**
	 * @generated
	 */
	DialSeries copyInstance();

} // DialSeries
