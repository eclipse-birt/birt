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
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.ActionValue;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Action</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type defines an Action. An action is a property defining interactivity
 * for an element. It is associated in a trigger with a trigger condition that
 * defines when the action is to be processed.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.Action#getType
 * <em>Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Action#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getAction()
 * @model extendedMetaData="name='Action' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Action extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute. The default value
	 * is <code>"URL_Redirect"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.ActionType}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the type of Action. This value determines the way the Action Value
	 * is processed.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #setType(ActionType)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getAction_Type()
	 * @model default="URL_Redirect" unique="false" unsettable="true"
	 *        required="true"
	 * @generated
	 */
	ActionType getType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Action#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.ActionType
	 * @see #isSetType()
	 * @see #unsetType()
	 * @see #getType()
	 * @generated
	 */
	void setType(ActionType value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Action#getType <em>Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetType()
	 * @see #getType()
	 * @see #setType(ActionType)
	 * @generated
	 */
	void unsetType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Action#getType <em>Type</em>}'
	 * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Type</em>' attribute is set.
	 * @see #unsetType()
	 * @see #getType()
	 * @see #setType(ActionType)
	 * @generated
	 */
	boolean isSetType();

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference. <!--
	 * begin-user-doc --> Gets the value of the action. This value defines the
	 * details for the action to be performed for a given element. (e.g. If action
	 * type is 'URL', the actual URL will be the value). <!-- end-user-doc --> <!--
	 * begin-model-doc -->
	 * 
	 * Specifies the value of the Action.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(ActionValue)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getAction_Value()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	ActionValue getValue();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Action#getValue <em>Value</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(ActionValue value);

	/**
	 * @generated
	 */
	Action copyInstance();

} // Action
