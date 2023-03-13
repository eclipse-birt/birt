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

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.attribute.TriggerCondition;
import org.eclipse.birt.chart.model.attribute.TriggerFlow;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Trigger</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl#getCondition
 * <em>Condition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl#getAction
 * <em>Action</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.TriggerImpl#getTriggerFlow
 * <em>Trigger Flow</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class TriggerImpl extends EObjectImpl implements Trigger {

	/**
	 * The default value of the '{@link #getCondition() <em>Condition</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCondition()
	 * @generated
	 * @ordered
	 */
	protected static final TriggerCondition CONDITION_EDEFAULT = TriggerCondition.MOUSE_HOVER_LITERAL;

	/**
	 * The cached value of the '{@link #getCondition() <em>Condition</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getCondition()
	 * @generated
	 * @ordered
	 */
	protected TriggerCondition condition = CONDITION_EDEFAULT;

	/**
	 * This is true if the Condition attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean conditionESet;

	/**
	 * The cached value of the '{@link #getAction() <em>Action</em>}' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getAction()
	 * @generated
	 * @ordered
	 */
	protected Action action;

	/**
	 * The default value of the '{@link #getTriggerFlow() <em>Trigger Flow</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTriggerFlow()
	 * @generated
	 * @ordered
	 */
	protected static final TriggerFlow TRIGGER_FLOW_EDEFAULT = TriggerFlow.CAPTURE_LITERAL;

	/**
	 * The cached value of the '{@link #getTriggerFlow() <em>Trigger Flow</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getTriggerFlow()
	 * @generated
	 * @ordered
	 */
	protected TriggerFlow triggerFlow = TRIGGER_FLOW_EDEFAULT;

	/**
	 * This is true if the Trigger Flow attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 *
	 * @generated
	 * @ordered
	 */
	protected boolean triggerFlowESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TriggerImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.TRIGGER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TriggerCondition getCondition() {
		return condition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCondition(TriggerCondition newCondition) {
		TriggerCondition oldCondition = condition;
		condition = newCondition == null ? CONDITION_EDEFAULT : newCondition;
		boolean oldConditionESet = conditionESet;
		conditionESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.TRIGGER__CONDITION, oldCondition,
					condition, !oldConditionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetCondition() {
		TriggerCondition oldCondition = condition;
		boolean oldConditionESet = conditionESet;
		condition = CONDITION_EDEFAULT;
		conditionESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.TRIGGER__CONDITION, oldCondition,
					CONDITION_EDEFAULT, oldConditionESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetCondition() {
		return conditionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Action getAction() {
		return action;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetAction(Action newAction, NotificationChain msgs) {
		Action oldAction = action;
		action = newAction;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, DataPackage.TRIGGER__ACTION,
					oldAction, newAction);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAction(Action newAction) {
		if (newAction != action) {
			NotificationChain msgs = null;
			if (action != null) {
				msgs = ((InternalEObject) action).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.TRIGGER__ACTION, null, msgs);
			}
			if (newAction != null) {
				msgs = ((InternalEObject) newAction).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.TRIGGER__ACTION, null, msgs);
			}
			msgs = basicSetAction(newAction, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.TRIGGER__ACTION, newAction, newAction));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TriggerFlow getTriggerFlow() {
		return triggerFlow;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTriggerFlow(TriggerFlow newTriggerFlow) {
		TriggerFlow oldTriggerFlow = triggerFlow;
		triggerFlow = newTriggerFlow == null ? TRIGGER_FLOW_EDEFAULT : newTriggerFlow;
		boolean oldTriggerFlowESet = triggerFlowESet;
		triggerFlowESet = true;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.TRIGGER__TRIGGER_FLOW, oldTriggerFlow,
					triggerFlow, !oldTriggerFlowESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void unsetTriggerFlow() {
		TriggerFlow oldTriggerFlow = triggerFlow;
		boolean oldTriggerFlowESet = triggerFlowESet;
		triggerFlow = TRIGGER_FLOW_EDEFAULT;
		triggerFlowESet = false;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.TRIGGER__TRIGGER_FLOW, oldTriggerFlow,
					TRIGGER_FLOW_EDEFAULT, oldTriggerFlowESet));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isSetTriggerFlow() {
		return triggerFlowESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DataPackage.TRIGGER__ACTION:
			return basicSetAction(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.TRIGGER__CONDITION:
			return getCondition();
		case DataPackage.TRIGGER__ACTION:
			return getAction();
		case DataPackage.TRIGGER__TRIGGER_FLOW:
			return getTriggerFlow();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DataPackage.TRIGGER__CONDITION:
			setCondition((TriggerCondition) newValue);
			return;
		case DataPackage.TRIGGER__ACTION:
			setAction((Action) newValue);
			return;
		case DataPackage.TRIGGER__TRIGGER_FLOW:
			setTriggerFlow((TriggerFlow) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case DataPackage.TRIGGER__CONDITION:
			unsetCondition();
			return;
		case DataPackage.TRIGGER__ACTION:
			setAction((Action) null);
			return;
		case DataPackage.TRIGGER__TRIGGER_FLOW:
			unsetTriggerFlow();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DataPackage.TRIGGER__CONDITION:
			return isSetCondition();
		case DataPackage.TRIGGER__ACTION:
			return action != null;
		case DataPackage.TRIGGER__TRIGGER_FLOW:
			return isSetTriggerFlow();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) {
			return super.toString();
		}

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (condition: "); //$NON-NLS-1$
		if (conditionESet) {
			result.append(condition);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(", triggerFlow: "); //$NON-NLS-1$
		if (triggerFlowESet) {
			result.append(triggerFlow);
		} else {
			result.append("<unset>"); //$NON-NLS-1$
		}
		result.append(')');
		return result.toString();
	}

	/**
	 * This convenience method initializes and provides a trigger instance for use
	 *
	 * NOTE: Manually written
	 *
	 * @return
	 */
	public static final Trigger create(TriggerCondition tc, Action a) {
		final Trigger tg = DataFactory.eINSTANCE.createTrigger();
		if (tc == null) {
			tc = TriggerCondition.ONCLICK_LITERAL;
		}
		tg.setCondition(tc);
		tg.setAction(a);
		return tg;
	}

	/**
	 * @generated
	 */
	@Override
	public Trigger copyInstance() {
		TriggerImpl dest = new TriggerImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Trigger src) {

		// children

		if (src.getAction() != null) {
			setAction(src.getAction().copyInstance());
		}

		// attributes

		condition = src.getCondition();

		conditionESet = src.isSetCondition();

		triggerFlow = src.getTriggerFlow();

		triggerFlowESet = src.isSetTriggerFlow();

	}

} // TriggerImpl
