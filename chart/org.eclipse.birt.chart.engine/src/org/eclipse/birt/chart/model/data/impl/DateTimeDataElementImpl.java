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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.DateTimeDataElement;
import org.eclipse.birt.chart.util.CDateTime;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Date Time Data Element</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.DateTimeDataElementImpl#getValue <em>Value</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class DateTimeDataElementImpl extends DataElementImpl implements DateTimeDataElement
{

    /**
     * The default value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected static final long VALUE_EDEFAULT = 0L;

    /**
     * The cached value of the '{@link #getValue() <em>Value</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getValue()
     * @generated
     * @ordered
     */
    protected long value = VALUE_EDEFAULT;

    /**
     * This is true if the Value attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean valueESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected DateTimeDataElementImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected EClass eStaticClass()
    {
        return DataPackage.eINSTANCE.getDateTimeDataElement();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public long getValue()
    {
        return value;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setValue(long newValue)
    {
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
    public void unsetValue()
    {
        long oldValue = value;
        boolean oldValueESet = valueESet;
        value = VALUE_EDEFAULT;
        valueESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.DATE_TIME_DATA_ELEMENT__VALUE,
                oldValue, VALUE_EDEFAULT, oldValueESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetValue()
    {
        return valueESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
                return new Long(getValue());
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
                setValue(((Long) newValue).longValue());
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
                unsetValue();
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.DATE_TIME_DATA_ELEMENT__VALUE:
                return isSetValue();
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String toString()
    {
        if (eIsProxy())
            return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (value: ");
        if (valueESet)
            result.append(value);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * A convenience method provided to return an initialized DateTimeDataElement
     * 
     * @param dtdeValue
     * @return
     */
    public static final DateTimeDataElement create(Calendar caValue)
    {
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
    public static final DateTimeDataElement create(long lValue)
    {
        final DateTimeDataElement dtde = DataFactory.eINSTANCE.createDateTimeDataElement();
        dtde.setValue(lValue);
        return dtde;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.data.DateTimeDataElement#getValueAsCalendar()
     */
    public final Calendar getValueAsCalendar()
    {
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(value);
        return c;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.data.DateTimeDataElement#getValueAsCDateTime()
     */
    public final CDateTime getValueAsCDateTime()
    {
        return new CDateTime(value);
    }
} //DateTimeDataElementImpl
