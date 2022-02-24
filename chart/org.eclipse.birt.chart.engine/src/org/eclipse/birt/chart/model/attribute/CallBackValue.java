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

package org.eclipse.birt.chart.model.attribute;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Call
 * Back Value</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> CallBackValue extends the type ActionValue specific
 * for callbacks.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.CallBackValue#getIdentifier
 * <em>Identifier</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCallBackValue()
 * @model extendedMetaData="name='CallBackValue' kind='elementOnly'"
 * @generated
 */
public interface CallBackValue extends ActionValue {

	/**
	 * Returns the value of the '<em><b>Identifier</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Identifier" specify a call back identifier to be used in the call back
	 * routine. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Identifier</em>' attribute.
	 * @see #setIdentifier(String)
	 * @see org.eclipse.birt.chart.model.attribute.AttributePackage#getCallBackValue_Identifier()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='Identifier'"
	 * @generated
	 */
	String getIdentifier();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.attribute.CallBackValue#getIdentifier
	 * <em>Identifier</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 *
	 * @param value the new value of the '<em>Identifier</em>' attribute.
	 * @see #getIdentifier()
	 * @generated
	 */
	void setIdentifier(String value);

	/**
	 * @generated
	 */
	@Override
	CallBackValue copyInstance();

} // CallBackValue
