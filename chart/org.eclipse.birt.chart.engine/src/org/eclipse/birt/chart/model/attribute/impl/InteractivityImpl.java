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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LegendBehaviorType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Interactivity</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl#isEnable
 * <em>Enable</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.InteractivityImpl#getLegendBehavior
 * <em>Legend Behavior</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class InteractivityImpl extends EObjectImpl implements Interactivity {

	/**
	 * The default value of the '{@link #isEnable() <em>Enable</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isEnable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLE_EDEFAULT = true;

	/**
	 * The cached value of the '{@link #isEnable() <em>Enable</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isEnable()
	 * @generated
	 * @ordered
	 */
	protected boolean enable = ENABLE_EDEFAULT;

	/**
	 * This is true if the Enable attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean enableESet;

	/**
	 * The default value of the '{@link #getLegendBehavior() <em>Legend
	 * Behavior</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLegendBehavior()
	 * @generated
	 * @ordered
	 */
	protected static final LegendBehaviorType LEGEND_BEHAVIOR_EDEFAULT = LegendBehaviorType.NONE_LITERAL;

	/**
	 * The cached value of the '{@link #getLegendBehavior() <em>Legend
	 * Behavior</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getLegendBehavior()
	 * @generated
	 * @ordered
	 */
	protected LegendBehaviorType legendBehavior = LEGEND_BEHAVIOR_EDEFAULT;

	/**
	 * This is true if the Legend Behavior attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean legendBehaviorESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected InteractivityImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.INTERACTIVITY;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isEnable() {
		return enable;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEnable(boolean newEnable) {
		boolean oldEnable = enable;
		enable = newEnable;
		boolean oldEnableESet = enableESet;
		enableESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.INTERACTIVITY__ENABLE, oldEnable,
					enable, !oldEnableESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetEnable() {
		boolean oldEnable = enable;
		boolean oldEnableESet = enableESet;
		enable = ENABLE_EDEFAULT;
		enableESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.INTERACTIVITY__ENABLE, oldEnable,
					ENABLE_EDEFAULT, oldEnableESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetEnable() {
		return enableESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public LegendBehaviorType getLegendBehavior() {
		return legendBehavior;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setLegendBehavior(LegendBehaviorType newLegendBehavior) {
		LegendBehaviorType oldLegendBehavior = legendBehavior;
		legendBehavior = newLegendBehavior == null ? LEGEND_BEHAVIOR_EDEFAULT : newLegendBehavior;
		boolean oldLegendBehaviorESet = legendBehaviorESet;
		legendBehaviorESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR,
					oldLegendBehavior, legendBehavior, !oldLegendBehaviorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetLegendBehavior() {
		LegendBehaviorType oldLegendBehavior = legendBehavior;
		boolean oldLegendBehaviorESet = legendBehaviorESet;
		legendBehavior = LEGEND_BEHAVIOR_EDEFAULT;
		legendBehaviorESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR,
					oldLegendBehavior, LEGEND_BEHAVIOR_EDEFAULT, oldLegendBehaviorESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetLegendBehavior() {
		return legendBehaviorESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.INTERACTIVITY__ENABLE:
			return isEnable();
		case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR:
			return getLegendBehavior();
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
		case AttributePackage.INTERACTIVITY__ENABLE:
			setEnable((Boolean) newValue);
			return;
		case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR:
			setLegendBehavior((LegendBehaviorType) newValue);
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
		case AttributePackage.INTERACTIVITY__ENABLE:
			unsetEnable();
			return;
		case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR:
			unsetLegendBehavior();
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
		case AttributePackage.INTERACTIVITY__ENABLE:
			return isSetEnable();
		case AttributePackage.INTERACTIVITY__LEGEND_BEHAVIOR:
			return isSetLegendBehavior();
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
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (enable: "); //$NON-NLS-1$
		if (enableESet)
			result.append(enable);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", legendBehavior: "); //$NON-NLS-1$
		if (legendBehaviorESet)
			result.append(legendBehavior);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * @return
	 */
	public static Interactivity create() {
		Interactivity itr = AttributeFactory.eINSTANCE.createInteractivity();
		return itr;
	}

	/**
	 * @generated
	 */
	public Interactivity copyInstance() {
		InteractivityImpl dest = new InteractivityImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(Interactivity src) {

		// attributes

		enable = src.isEnable();

		enableESet = src.isSetEnable();

		legendBehavior = src.getLegendBehavior();

		legendBehaviorESet = src.isSetLegendBehavior();

	}

} // InteractivityImpl
