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
 * $Id: DataSetParameterImpl.java,v 1.1.2.1 2010/11/29 06:23:52 rlu Exp $
 */
package org.eclipse.birt.report.model.adapter.oda.model.impl;

import org.eclipse.birt.report.model.adapter.oda.model.DataSetParameter;
import org.eclipse.birt.report.model.adapter.oda.model.DynamicList;
import org.eclipse.birt.report.model.adapter.oda.model.ModelPackage;

import org.eclipse.datatools.connectivity.oda.design.ParameterDefinition;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Data
 * Set Parameter</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl#getParameterDefinition
 * <em>Parameter Definition</em>}</li>
 * <li>{@link org.eclipse.birt.report.model.adapter.oda.model.impl.DataSetParameterImpl#getDynamicList
 * <em>Dynamic List</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataSetParameterImpl extends EObjectImpl implements DataSetParameter {
	/**
	 * The cached value of the '{@link #getParameterDefinition() <em>Parameter
	 * Definition</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getParameterDefinition()
	 * @generated
	 * @ordered
	 */
	protected ParameterDefinition parameterDefinition;

	/**
	 * The cached value of the '{@link #getDynamicList() <em>Dynamic List</em>}'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getDynamicList()
	 * @generated
	 * @ordered
	 */
	protected DynamicList dynamicList;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DataSetParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ModelPackage.Literals.DATA_SET_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ParameterDefinition getParameterDefinition() {
		return parameterDefinition;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetParameterDefinition(ParameterDefinition newParameterDefinition,
			NotificationChain msgs) {
		ParameterDefinition oldParameterDefinition = parameterDefinition;
		parameterDefinition = newParameterDefinition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION, oldParameterDefinition,
					newParameterDefinition);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setParameterDefinition(ParameterDefinition newParameterDefinition) {
		if (newParameterDefinition != parameterDefinition) {
			NotificationChain msgs = null;
			if (parameterDefinition != null)
				msgs = ((InternalEObject) parameterDefinition).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION, null, msgs);
			if (newParameterDefinition != null)
				msgs = ((InternalEObject) newParameterDefinition).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION, null, msgs);
			msgs = basicSetParameterDefinition(newParameterDefinition, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION,
					newParameterDefinition, newParameterDefinition));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DynamicList getDynamicList() {
		return dynamicList;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDynamicList(DynamicList newDynamicList, NotificationChain msgs) {
		DynamicList oldDynamicList = dynamicList;
		dynamicList = newDynamicList;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST, oldDynamicList, newDynamicList);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDynamicList(DynamicList newDynamicList) {
		if (newDynamicList != dynamicList) {
			NotificationChain msgs = null;
			if (dynamicList != null)
				msgs = ((InternalEObject) dynamicList).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST, null, msgs);
			if (newDynamicList != null)
				msgs = ((InternalEObject) newDynamicList).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST, null, msgs);
			msgs = basicSetDynamicList(newDynamicList, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST,
					newDynamicList, newDynamicList));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION:
			return basicSetParameterDefinition(null, msgs);
		case ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST:
			return basicSetDynamicList(null, msgs);
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
		case ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION:
			return getParameterDefinition();
		case ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST:
			return getDynamicList();
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
		case ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION:
			setParameterDefinition((ParameterDefinition) newValue);
			return;
		case ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST:
			setDynamicList((DynamicList) newValue);
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
		case ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION:
			setParameterDefinition((ParameterDefinition) null);
			return;
		case ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST:
			setDynamicList((DynamicList) null);
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
		case ModelPackage.DATA_SET_PARAMETER__PARAMETER_DEFINITION:
			return parameterDefinition != null;
		case ModelPackage.DATA_SET_PARAMETER__DYNAMIC_LIST:
			return dynamicList != null;
		}
		return super.eIsSet(featureID);
	}

} // DataSetParameterImpl
