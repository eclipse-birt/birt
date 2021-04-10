/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data.impl;

import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '
 * <em><b>Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.DataSetImpl#getValues
 * <em>Values</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DataSetImpl extends EObjectImpl implements DataSet {

	/**
	 * The default value of the '{@link #getValues() <em>Values</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
	protected static final Object VALUES_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getValues() <em>Values</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValues()
	 * @generated
	 * @ordered
	 */
	protected Object values = VALUES_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DataSetImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.DATA_SET;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public Object getValues() {
		return values;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValues(Object newValues) {
		Object oldValues = values;
		values = newValues;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.DATA_SET__VALUES, oldValues, values));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.DATA_SET__VALUES:
			return getValues();
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
		case DataPackage.DATA_SET__VALUES:
			setValues(newValue);
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
		case DataPackage.DATA_SET__VALUES:
			setValues(VALUES_EDEFAULT);
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
		case DataPackage.DATA_SET__VALUES:
			return VALUES_EDEFAULT == null ? values != null : !VALUES_EDEFAULT.equals(values);
		}
		return super.eIsSet(featureID);
	}

	public DataSet copyInstance() {
		DataSetImpl dest = new DataSetImpl();
		dest.set(this);
		return dest;
	}

	protected void set(DataSet src) {
		values = src.getValues();
		isBigNumber = src.isBigNumber();
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
		result.append(" (values: "); //$NON-NLS-1$
		result.append(values);
		result.append(')');
		return result.toString();
	}

	private boolean isBigNumber = false;

	/**
	 * Sets true/false to indicate if the values in data set are big number.
	 * 
	 * @param isBigNumber
	 */
	public void setIsBigNumber(boolean isBigNumber) {
		this.isBigNumber = isBigNumber;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.model.data.DataSet#isBigNumber()
	 */
	public boolean isBigNumber() {
		return this.isBigNumber;
	}
} // DataSetImpl
