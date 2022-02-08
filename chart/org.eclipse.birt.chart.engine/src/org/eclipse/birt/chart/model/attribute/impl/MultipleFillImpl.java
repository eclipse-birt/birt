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

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object
 * '<em><b>Multiple Fill</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.MultipleFillImpl#getFills
 * <em>Fills</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MultipleFillImpl extends FillImpl implements MultipleFill {

	/**
	 * The cached value of the '{@link #getFills() <em>Fills</em>}' containment
	 * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getFills()
	 * @generated
	 * @ordered
	 */
	protected EList<Fill> fills;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected MultipleFillImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AttributePackage.Literals.MULTIPLE_FILL;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<Fill> getFills() {
		if (fills == null) {
			fills = new EObjectContainmentEList<Fill>(Fill.class, this, AttributePackage.MULTIPLE_FILL__FILLS);
		}
		return fills;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AttributePackage.MULTIPLE_FILL__FILLS:
			return ((InternalEList<?>) getFills()).basicRemove(otherEnd, msgs);
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
		case AttributePackage.MULTIPLE_FILL__FILLS:
			return getFills();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AttributePackage.MULTIPLE_FILL__FILLS:
			getFills().clear();
			getFills().addAll((Collection<? extends Fill>) newValue);
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
		case AttributePackage.MULTIPLE_FILL__FILLS:
			getFills().clear();
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
		case AttributePackage.MULTIPLE_FILL__FILLS:
			return fills != null && !fills.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * @generated
	 */
	protected void set(MultipleFill src) {

		// children

		if (src.getFills() != null) {
			EList<Fill> list = getFills();
			for (Fill element : src.getFills()) {
				list.add(element.copyInstance());
			}
		}

	}

	/**
	 * Manually written.
	 * 
	 * @return
	 */
	public static MultipleFill create() {
		return AttributeFactory.eINSTANCE.createMultipleFill();
	}

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	public MultipleFill copyInstance() {
		MultipleFillImpl dest = new MultipleFillImpl();

		EList<Fill> tFills = getFills();
		if (tFills != null) {
			EList<Fill> list = dest.getFills();
			for (Fill element : tFills) {
				list.add(element.copyInstance());
			}
		}
		return dest;
	}

} // MultipleFillImpl
