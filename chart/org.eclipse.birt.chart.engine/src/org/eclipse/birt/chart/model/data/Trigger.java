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
import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.TriggerFlow;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Trigger</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 *
 * This type defines a Trigger. A trigger defines interactivity for a chart
 * component.
 *
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.Trigger#getCondition
 * <em>Condition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Trigger#getAction
 * <em>Action</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow
 * <em>Trigger Flow</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getTrigger()
 * @model extendedMetaData="name='Trigger' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface Trigger extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Condition</b></em>' attribute. The default
	 * value is <code>"Mouse_Hover"</code>. The literals are from the enumeration
	 * {@link org.eclipse.birt.chart.model.attribute.TriggerCondition}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies the condition for the Trigger. This value determines when the
	 * Action is processed.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Condition</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see #isSetCondition()
	 * @see #unsetCondition()
	 * @see #setCondition(TriggerCondition)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getTrigger_Condition()
	 * @model default="Mouse_Hover" unsettable="true" required="true"
	 *        extendedMetaData="kind='element' name='Condition'"
	 * @generated
	 */
	TriggerCondition getCondition();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getCondition
	 * <em>Condition</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Condition</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerCondition
	 * @see #isSetCondition()
	 * @see #unsetCondition()
	 * @see #getCondition()
	 * @generated
	 */
	void setCondition(TriggerCondition value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getCondition
	 * <em>Condition</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetCondition()
	 * @see #getCondition()
	 * @see #setCondition(TriggerCondition)
	 * @generated
	 */
	void unsetCondition();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.data.Trigger#getCondition
	 * <em>Condition</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 *
	 * @return whether the value of the '<em>Condition</em>' attribute is set.
	 * @see #unsetCondition()
	 * @see #getCondition()
	 * @see #setCondition(TriggerCondition)
	 * @generated
	 */
	boolean isSetCondition();

	/**
	 * Returns the value of the '<em><b>Action</b></em>' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 *
	 * Specifies the Action for the Trigger. This value describes the interactivity
	 * of the trigger.
	 *
	 * <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Action</em>' containment reference.
	 * @see #setAction(Action)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getTrigger_Action()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	Action getAction();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getAction <em>Action</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Action</em>' containment reference.
	 * @see #getAction()
	 * @generated
	 */
	void setAction(Action value);

	/**
	 * Returns the value of the '<em><b>Trigger Flow</b></em>' attribute. The
	 * default value is <code>"Capture"</code>. The literals are from the
	 * enumeration {@link org.eclipse.birt.chart.model.attribute.TriggerFlow}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies
	 * the bubbling behavior of the Trigger. <!-- end-model-doc -->
	 *
	 * @return the value of the '<em>Trigger Flow</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @see #isSetTriggerFlow()
	 * @see #unsetTriggerFlow()
	 * @see #setTriggerFlow(TriggerFlow)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getTrigger_TriggerFlow()
	 * @model default="Capture" unsettable="true" extendedMetaData="kind='element'
	 *        name='TriggerFlow'"
	 * @generated
	 */
	TriggerFlow getTriggerFlow();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow <em>Trigger
	 * Flow</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @param value the new value of the '<em>Trigger Flow</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.TriggerFlow
	 * @see #isSetTriggerFlow()
	 * @see #unsetTriggerFlow()
	 * @see #getTriggerFlow()
	 * @generated
	 */
	void setTriggerFlow(TriggerFlow value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow <em>Trigger
	 * Flow</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #isSetTriggerFlow()
	 * @see #getTriggerFlow()
	 * @see #setTriggerFlow(TriggerFlow)
	 * @generated
	 */
	void unsetTriggerFlow();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.Trigger#getTriggerFlow <em>Trigger
	 * Flow</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @return whether the value of the '<em>Trigger Flow</em>' attribute is set.
	 * @see #unsetTriggerFlow()
	 * @see #getTriggerFlow()
	 * @see #setTriggerFlow(TriggerFlow)
	 * @generated
	 */
	boolean isSetTriggerFlow();

	/**
	 * @generated
	 */
	@Override
	Trigger copyInstance();

} // Trigger
