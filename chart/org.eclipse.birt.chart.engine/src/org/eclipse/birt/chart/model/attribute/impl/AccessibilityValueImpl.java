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

import org.eclipse.birt.chart.model.attribute.AccessibilityValue;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Accessibility Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl#getText
 * <em>Text</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.AccessibilityValueImpl#getAccessibility
 * <em>Accessibility</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class AccessibilityValueImpl extends ActionValueImpl implements AccessibilityValue {

	/**
	 * The default value of the '{@link #getText() <em>Text</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected static final String TEXT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getText() <em>Text</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getText()
	 * @generated
	 * @ordered
	 */
	protected String text = TEXT_EDEFAULT;

	/**
	 * The default value of the '{@link #getAccessibility() <em>Accessibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getAccessibility()
	 * @generated
	 * @ordered
	 */
	protected static final String ACCESSIBILITY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getAccessibility() <em>Accessibility</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @see #getAccessibility()
	 * @generated
	 * @ordered
	 */
	protected String accessibility = ACCESSIBILITY_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AccessibilityValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.ACCESSIBILITY_VALUE;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getText() {
		return text;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setText(String newText) {
		String oldText = text;
		text = newText;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.ACCESSIBILITY_VALUE__TEXT, oldText,
					text));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getAccessibility() {
		return accessibility;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAccessibility(String newAccessibility) {
		String oldAccessibility = accessibility;
		accessibility = newAccessibility;
		if (eNotificationRequired()) {
			eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.ACCESSIBILITY_VALUE__ACCESSIBILITY,
					oldAccessibility, accessibility));
		}
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AttributePackage.ACCESSIBILITY_VALUE__TEXT:
			return getText();
		case AttributePackage.ACCESSIBILITY_VALUE__ACCESSIBILITY:
			return getAccessibility();
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
		case AttributePackage.ACCESSIBILITY_VALUE__TEXT:
			setText((String) newValue);
			return;
		case AttributePackage.ACCESSIBILITY_VALUE__ACCESSIBILITY:
			setAccessibility((String) newValue);
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
		case AttributePackage.ACCESSIBILITY_VALUE__TEXT:
			setText(TEXT_EDEFAULT);
			return;
		case AttributePackage.ACCESSIBILITY_VALUE__ACCESSIBILITY:
			setAccessibility(ACCESSIBILITY_EDEFAULT);
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
		case AttributePackage.ACCESSIBILITY_VALUE__TEXT:
			return TEXT_EDEFAULT == null ? text != null : !TEXT_EDEFAULT.equals(text);
		case AttributePackage.ACCESSIBILITY_VALUE__ACCESSIBILITY:
			return ACCESSIBILITY_EDEFAULT == null ? accessibility != null
					: !ACCESSIBILITY_EDEFAULT.equals(accessibility);
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
		result.append(" (text: "); //$NON-NLS-1$
		result.append(text);
		result.append(", accessibility: "); //$NON-NLS-1$
		result.append(accessibility);
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	@Override
	public AccessibilityValue copyInstance() {
		AccessibilityValueImpl dest = new AccessibilityValueImpl();
		dest.set(this);
		return dest;
	}

	protected void set(AccessibilityValue src) {
		super.set(src);
		text = src.getText();
		accessibility = src.getAccessibility();
	}

} // AccessibilityValueImpl
