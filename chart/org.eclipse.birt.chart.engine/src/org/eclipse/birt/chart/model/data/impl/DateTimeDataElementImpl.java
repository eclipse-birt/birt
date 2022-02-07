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

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import com.ibm.icu.util.Calendar;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Date
 * Time Data Element</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl#getValue
 * <em>Value</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class DateTimeDataElementImpl extends DataElementImpl implements DateTimeDataElement {

	/**
	 * The default value of the '{@link #getValue() <em>Value</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected static final long VALUE_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected long value = VALUE_EDEFAULT;

	/**
	 * This is true if the Value attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean valueESet;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected DateTimeDataElementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.DATE_TIME_DATA_ELEMENT;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public long getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setValue(long newValue) {
		long oldValue = value;
		value = newValue;
		boolean oldValueESet = valueESet;
		valueESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.DATE_TIME_DATA_ELEMENT__VALUE, oldValue,
					value, !oldValueESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetValue() {
		long oldValue = value;
		boolean oldValueESet = valueESet;
		value = VALUE_EDEFAULT;
		valueESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.DATE_TIME_DATA_ELEMENT__VALUE, oldValue,
					VALUE_EDEFAULT, oldValueESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetValue() {
		return valueESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
			return getValue();
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
		case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
			setValue((Long) newValue);
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
		case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
			unsetValue();
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
		case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
			return isSetValue();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(this.getClass().getInterfaces()[0].getSimpleName());
		result.append(" (value: "); //$NON-NLS-1$
		if (valueESet)
			result.append(value);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to return an initialized DateTimeDataElement
	 * 
	 * @param dtdeValue
	 * @return
	 */
	public static final DateTimeDataElement create(Calendar caValue) {
		final DateTimeDataElement dtde = DataFactory.eINSTANCE.createDateTimeDataElement();
		dtde.setValue(caValue.getTime().getTime());
		return dtde;
	}

	/**
	 * A convenience method provided to return an initialized DateTimeDataElement
	 * 
	 * @param dtdeValue
	 * @return
	 */
	public static final DateTimeDataElement create(long lValue) {
		final DateTimeDataElement dtde = DataFactory.eINSTANCE.createDateTimeDataElement();
		dtde.setValue(lValue);
		return dtde;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.data.DateTimeDataElement#getValueAsCalendar()
	 */
	public final Calendar getValueAsCalendar() {
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(value);
		return c;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.model.data.DateTimeDataElement#getValueAsCDateTime()
	 */
	public final CDateTime getValueAsCDateTime() {
		return new CDateTime(value);
	}

	/**
	 * @generated
	 */
	public DateTimeDataElement copyInstance() {
		DateTimeDataElementImpl dest = new DateTimeDataElementImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(DateTimeDataElement src) {

		super.set(src);

		// attributes

		value = src.getValue();

		valueESet = src.isSetValue();

	}

} // DateTimeDataElementImpl
