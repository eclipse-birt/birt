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

import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Base
 * Sample Data</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.BaseSampleDataImpl#getDataSetRepresentation
 * <em>Data Set Representation</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class BaseSampleDataImpl extends EObjectImpl implements BaseSampleData {

	/**
	 * The default value of the '{@link #getDataSetRepresentation() <em>Data Set
	 * Representation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getDataSetRepresentation()
	 * @generated
	 * @ordered
	 */
	protected static final String DATA_SET_REPRESENTATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDataSetRepresentation() <em>Data Set
	 * Representation</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getDataSetRepresentation()
	 * @generated
	 * @ordered
	 */
	protected String dataSetRepresentation = DATA_SET_REPRESENTATION_EDEFAULT;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected BaseSampleDataImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.BASE_SAMPLE_DATA;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getDataSetRepresentation() {
		return dataSetRepresentation;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setDataSetRepresentation(String newDataSetRepresentation) {
		String oldDataSetRepresentation = dataSetRepresentation;
		dataSetRepresentation = newDataSetRepresentation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION,
					oldDataSetRepresentation, dataSetRepresentation));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			return getDataSetRepresentation();
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
		case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			setDataSetRepresentation((String) newValue);
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
		case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			setDataSetRepresentation(DATA_SET_REPRESENTATION_EDEFAULT);
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
		case DataPackage.BASE_SAMPLE_DATA__DATA_SET_REPRESENTATION:
			return DATA_SET_REPRESENTATION_EDEFAULT == null ? dataSetRepresentation != null
					: !DATA_SET_REPRESENTATION_EDEFAULT.equals(dataSetRepresentation);
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
		result.append(" (dataSetRepresentation: "); //$NON-NLS-1$
		result.append(dataSetRepresentation);
		result.append(')');
		return result.toString();
	}

	/**
	 * @generated
	 */
	public BaseSampleData copyInstance() {
		BaseSampleDataImpl dest = new BaseSampleDataImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(BaseSampleData src) {

		// attributes

		dataSetRepresentation = src.getDataSetRepresentation();

	}

} // BaseSampleDataImpl
