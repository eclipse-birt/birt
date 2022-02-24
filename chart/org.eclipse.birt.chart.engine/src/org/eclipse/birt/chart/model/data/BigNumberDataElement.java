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

package org.eclipse.birt.chart.model.data;

import java.math.BigDecimal;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Big
 * Number Data Element</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> Element BigNUmberDataElement represents a
 * DataElement which BigDecimal value. <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.BigNumberDataElement#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getBigNumberDataElement()
 * @model extendedMetaData="name='BigNumberDataElement' kind='elementOnly'"
 * @generated
 */
public interface BigNumberDataElement extends DataElement {

	/**
	 * Returns the value of the '<em><b>Value</b></em>' attribute. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Value</em>' attribute isn't clear, there really
	 * should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' attribute.
	 * @see #setValue(BigDecimal)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getBigNumberDataElement_Value()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.Decimal" required="true"
	 *        extendedMetaData="kind='element' name='Value'"
	 * @generated
	 */
	BigDecimal getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.BigNumberDataElement#getValue
	 * <em>Value</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' attribute.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(BigDecimal value);

	/**
	 * @generated
	 */
	BigNumberDataElement copyInstance();

} // BigNumberDataElement
