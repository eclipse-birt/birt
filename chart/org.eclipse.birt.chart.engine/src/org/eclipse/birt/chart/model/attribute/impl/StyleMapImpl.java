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

import org.eclipse.birt.chart.model.attribute.AttributePackage;
import org.eclipse.birt.chart.model.attribute.StyleMap;
import org.eclipse.birt.chart.model.attribute.StyledComponent;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Style Map</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getComponentName <em>Component Name</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.attribute.impl.StyleMapImpl#getStyle <em>Style</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class StyleMapImpl extends EObjectImpl implements StyleMap
{

    /**
     * The default value of the '{@link #getComponentName() <em>Component Name</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getComponentName()
     * @generated @ordered
     */
    protected static final StyledComponent COMPONENT_NAME_EDEFAULT = StyledComponent.CHART_TITLE_LITERAL;

    /**
     * The cached value of the '{@link #getComponentName() <em>Component Name</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getComponentName()
     * @generated @ordered
     */
    protected StyledComponent componentName = COMPONENT_NAME_EDEFAULT;

    /**
     * This is true if the Component Name attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean componentNameESet = false;

    /**
     * The default value of the '{@link #getStyle() <em>Style</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getStyle()
     * @generated @ordered
     */
    protected static final String STYLE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getStyle() <em>Style</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getStyle()
     * @generated @ordered
     */
    protected String style = STYLE_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected StyleMapImpl()
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
        return AttributePackage.eINSTANCE.getStyleMap();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public StyledComponent getComponentName()
    {
        return componentName;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setComponentName(StyledComponent newComponentName)
    {
        StyledComponent oldComponentName = componentName;
        componentName = newComponentName == null ? COMPONENT_NAME_EDEFAULT : newComponentName;
        boolean oldComponentNameESet = componentNameESet;
        componentNameESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE_MAP__COMPONENT_NAME,
                oldComponentName, componentName, !oldComponentNameESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetComponentName()
    {
        StyledComponent oldComponentName = componentName;
        boolean oldComponentNameESet = componentNameESet;
        componentName = COMPONENT_NAME_EDEFAULT;
        componentNameESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.STYLE_MAP__COMPONENT_NAME,
                oldComponentName, COMPONENT_NAME_EDEFAULT, oldComponentNameESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetComponentName()
    {
        return componentNameESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getStyle()
    {
        return style;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStyle(String newStyle)
    {
        String oldStyle = style;
        style = newStyle;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.STYLE_MAP__STYLE, oldStyle, style));
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
            case AttributePackage.STYLE_MAP__COMPONENT_NAME:
                return getComponentName();
            case AttributePackage.STYLE_MAP__STYLE:
                return getStyle();
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
            case AttributePackage.STYLE_MAP__COMPONENT_NAME:
                setComponentName((StyledComponent) newValue);
                return;
            case AttributePackage.STYLE_MAP__STYLE:
                setStyle((String) newValue);
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
            case AttributePackage.STYLE_MAP__COMPONENT_NAME:
                unsetComponentName();
                return;
            case AttributePackage.STYLE_MAP__STYLE:
                setStyle(STYLE_EDEFAULT);
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
            case AttributePackage.STYLE_MAP__COMPONENT_NAME:
                return isSetComponentName();
            case AttributePackage.STYLE_MAP__STYLE:
                return STYLE_EDEFAULT == null ? style != null : !STYLE_EDEFAULT.equals(style);
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
        result.append(" (componentName: ");
        if (componentNameESet)
            result.append(componentName);
        else
            result.append("<unset>");
        result.append(", style: ");
        result.append(style);
        result.append(')');
        return result.toString();
    }

} //StyleMapImpl
