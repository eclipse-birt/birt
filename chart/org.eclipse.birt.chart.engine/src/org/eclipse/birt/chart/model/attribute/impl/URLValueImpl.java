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

package org.eclipse.birt.chart.model.attribute.impl;

import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>URL Value</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getBaseUrl <em>Base Url</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getBaseParameterName <em>Base Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getValueParameterName <em>Value Parameter Name</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.URLValueImpl#getSeriesParameterName <em>Series Parameter Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class URLValueImpl extends ActionValueImpl implements URLValue
{

    /**
     * The default value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getBaseUrl()
     * @generated @ordered
     */
    protected static final String BASE_URL_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBaseUrl() <em>Base Url</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getBaseUrl()
     * @generated @ordered
     */
    protected String baseUrl = BASE_URL_EDEFAULT;

    /**
     * The default value of the '{@link #getTarget() <em>Target</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getTarget()
     * @generated @ordered
     */
    protected static final String TARGET_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getTarget() <em>Target</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getTarget()
     * @generated @ordered
     */
    protected String target = TARGET_EDEFAULT;

    /**
     * The default value of the '{@link #getBaseParameterName() <em>Base Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getBaseParameterName()
     * @generated @ordered
     */
    protected static final String BASE_PARAMETER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getBaseParameterName() <em>Base Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getBaseParameterName()
     * @generated @ordered
     */
    protected String baseParameterName = BASE_PARAMETER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getValueParameterName() <em>Value Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getValueParameterName()
     * @generated @ordered
     */
    protected static final String VALUE_PARAMETER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getValueParameterName() <em>Value Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getValueParameterName()
     * @generated @ordered
     */
    protected String valueParameterName = VALUE_PARAMETER_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getSeriesParameterName() <em>Series Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesParameterName()
     * @generated @ordered
     */
    protected static final String SERIES_PARAMETER_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSeriesParameterName() <em>Series Parameter Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesParameterName()
     * @generated @ordered
     */
    protected String seriesParameterName = SERIES_PARAMETER_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected URLValueImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass()
    {
        return AttributePackage.eINSTANCE.getURLValue();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getBaseUrl()
    {
        return baseUrl;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setBaseUrl(String newBaseUrl)
    {
        String oldBaseUrl = baseUrl;
        baseUrl = newBaseUrl;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__BASE_URL, oldBaseUrl, baseUrl));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getTarget()
    {
        return target;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setTarget(String newTarget)
    {
        String oldTarget = target;
        target = newTarget;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__TARGET, oldTarget, target));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getBaseParameterName()
    {
        return baseParameterName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setBaseParameterName(String newBaseParameterName)
    {
        String oldBaseParameterName = baseParameterName;
        baseParameterName = newBaseParameterName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__BASE_PARAMETER_NAME, oldBaseParameterName, baseParameterName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getValueParameterName()
    {
        return valueParameterName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setValueParameterName(String newValueParameterName)
    {
        String oldValueParameterName = valueParameterName;
        valueParameterName = newValueParameterName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME, oldValueParameterName, valueParameterName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String getSeriesParameterName()
    {
        return seriesParameterName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setSeriesParameterName(String newSeriesParameterName)
    {
        String oldSeriesParameterName = seriesParameterName;
        seriesParameterName = newSeriesParameterName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME, oldSeriesParameterName, seriesParameterName));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.URL_VALUE__BASE_URL:
                return getBaseUrl();
            case AttributePackage.URL_VALUE__TARGET:
                return getTarget();
            case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
                return getBaseParameterName();
            case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
                return getValueParameterName();
            case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
                return getSeriesParameterName();
        }
        return eDynamicGet(eFeature, resolve);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eSet(EStructuralFeature eFeature, Object newValue)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.URL_VALUE__BASE_URL:
                setBaseUrl((String)newValue);
                return;
            case AttributePackage.URL_VALUE__TARGET:
                setTarget((String)newValue);
                return;
            case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
                setBaseParameterName((String)newValue);
                return;
            case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
                setValueParameterName((String)newValue);
                return;
            case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
                setSeriesParameterName((String)newValue);
                return;
        }
        eDynamicSet(eFeature, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void eUnset(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.URL_VALUE__BASE_URL:
                setBaseUrl(BASE_URL_EDEFAULT);
                return;
            case AttributePackage.URL_VALUE__TARGET:
                setTarget(TARGET_EDEFAULT);
                return;
            case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
                setBaseParameterName(BASE_PARAMETER_NAME_EDEFAULT);
                return;
            case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
                setValueParameterName(VALUE_PARAMETER_NAME_EDEFAULT);
                return;
            case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
                setSeriesParameterName(SERIES_PARAMETER_NAME_EDEFAULT);
                return;
        }
        eDynamicUnset(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean eIsSet(EStructuralFeature eFeature)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.URL_VALUE__BASE_URL:
                return BASE_URL_EDEFAULT == null ? baseUrl != null : !BASE_URL_EDEFAULT.equals(baseUrl);
            case AttributePackage.URL_VALUE__TARGET:
                return TARGET_EDEFAULT == null ? target != null : !TARGET_EDEFAULT.equals(target);
            case AttributePackage.URL_VALUE__BASE_PARAMETER_NAME:
                return BASE_PARAMETER_NAME_EDEFAULT == null ? baseParameterName != null : !BASE_PARAMETER_NAME_EDEFAULT.equals(baseParameterName);
            case AttributePackage.URL_VALUE__VALUE_PARAMETER_NAME:
                return VALUE_PARAMETER_NAME_EDEFAULT == null ? valueParameterName != null : !VALUE_PARAMETER_NAME_EDEFAULT.equals(valueParameterName);
            case AttributePackage.URL_VALUE__SERIES_PARAMETER_NAME:
                return SERIES_PARAMETER_NAME_EDEFAULT == null ? seriesParameterName != null : !SERIES_PARAMETER_NAME_EDEFAULT.equals(seriesParameterName);
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public String toString()
    {
        if (eIsProxy()) return super.toString();

        StringBuffer result = new StringBuffer(super.toString());
        result.append(" (baseUrl: ");
        result.append(baseUrl);
        result.append(", target: ");
        result.append(target);
        result.append(", baseParameterName: ");
        result.append(baseParameterName);
        result.append(", valueParameterName: ");
        result.append(valueParameterName);
        result.append(", seriesParameterName: ");
        result.append(seriesParameterName);
        result.append(')');
        return result.toString();
    }

    /**
     * A convenience method provided to create a new URLValue instance
     * 
     * NOTE: Manually written
     * 
     * @param sBaseUrl
     * @param sTarget
     * @param sBaseParameterName
     * @param sValueParameterName
     * @param sSeriesParameterName
     * @return
     */
    public static final URLValue create(String sBaseUrl, String sTarget, String sBaseParameterName,
        String sValueParameterName, String sSeriesParameterName)
    {
        final URLValue uv = AttributeFactory.eINSTANCE.createURLValue();
        uv.setBaseUrl(sBaseUrl);
        uv.setTarget(sTarget);
        uv.setBaseParameterName(sBaseParameterName);
        uv.setValueParameterName(sValueParameterName);
        uv.setSeriesParameterName(sSeriesParameterName);
        return uv;
    }

    /**
     *  
     */
    private static final char cEncode = '%';

    /**
     * A static/fast lookup table provided for
     */
    private final static String[] hex =
    {
        "00", "01", "02", "03", "04", "05", "06", "07", "08", "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11",
        "12", "13", "14", "15", "16", "17", "18", "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23",
        "24", "25", "26", "27", "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32", "33", "34", "35",
        "36", "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40", "41", "42", "43", "44", "45", "46", "47",
        "48", "49", "4a", "4b", "4c", "4d", "4e", "4f", "50", "51", "52", "53", "54", "55", "56", "57", "58", "59",
        "5a", "5b", "5c", "5d", "5e", "5f", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "6a", "6b",
        "6c", "6d", "6e", "6f", "70", "71", "72", "73", "74", "75", "76", "77", "78", "79", "7a", "7b", "7c", "7d",
        "7e", "7f", "80", "81", "82", "83", "84", "85", "86", "87", "88", "89", "8a", "8b", "8c", "8d", "8e", "8f",
        "90", "91", "92", "93", "94", "95", "96", "97", "98", "99", "9a", "9b", "9c", "9d", "9e", "9f", "a0", "a1",
        "a2", "a3", "a4", "a5", "a6", "a7", "a8", "a9", "aa", "ab", "ac", "ad", "ae", "af", "b0", "b1", "b2", "b3",
        "b4", "b5", "b6", "b7", "b8", "b9", "ba", "bb", "bc", "bd", "be", "bf", "c0", "c1", "c2", "c3", "c4", "c5",
        "c6", "c7", "c8", "c9", "ca", "cb", "cc", "cd", "ce", "cf", "d0", "d1", "d2", "d3", "d4", "d5", "d6", "d7",
        "d8", "d9", "da", "db", "dc", "dd", "de", "df", "e0", "e1", "e2", "e3", "e4", "e5", "e6", "e7", "e8", "e9",
        "ea", "eb", "ec", "ed", "ee", "ef", "f0", "f1", "f2", "f3", "f4", "f5", "f6", "f7", "f8", "f9", "fa", "fb",
        "fc", "fd", "fe", "ff"
    };

    /**
     * Encodes portions of a URL as needed
     * 
     * @param sText
     * @return
     */
    public static final String encode(String sText)
    {
        if (sText == null)
        {
            return null;
        }

        final StringBuffer sb = new StringBuffer();
        final char chrarry[] = sText.toCharArray();
        int ch = 0;

        for (int i = 0; i < chrarry.length; i++)
        {
            ch = chrarry[i];
            if (('A' <= ch && ch <= 'Z') // 'A'..'Z'
                || ('a' <= ch && ch <= 'z') // 'a'..'z'
                || ('0' <= ch && ch <= '9')) // '0'..'9'
            {
                sb.append(chrarry[i]);
            }
            else if (ch <= 0x007f) // other ASCII
            {
                sb.append(cEncode + hex[ch]);
            }
            else if (ch <= 0x07FF) // non-ASCII <= 0x7FF
            {
                sb.append(cEncode + hex[0xc0 | (ch >> 6)]);
                sb.append(cEncode + hex[0x80 | (ch & 0x3F)]);
            }
            else
            // 0x7FF < ch <= 0xFFFF
            {
                sb.append(cEncode + hex[0xe0 | (ch >> 12)]);
                sb.append(cEncode + hex[0x80 | ((ch >> 6) & 0x3F)]);
                sb.append(cEncode + hex[0x80 | (ch & 0x3F)]);
            }
        }
        return sb.toString();
    }

} //URLValueImpl
