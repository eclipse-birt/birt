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
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Size</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.SizeImpl#getHeight <em>Height</em>}</li>
 *   <li>{@link org.eclipse.birt.chart.model.attribute.impl.SizeImpl#getWidth <em>Width</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SizeImpl extends EObjectImpl implements Size
{

    /**
     * The default value of the '{@link #getHeight() <em>Height</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getHeight()
     * @generated @ordered
     */
    protected static final double HEIGHT_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getHeight() <em>Height</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getHeight()
     * @generated @ordered
     */
    protected double height = HEIGHT_EDEFAULT;

    /**
     * This is true if the Height attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean heightESet = false;

    /**
     * The default value of the '{@link #getWidth() <em>Width</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getWidth()
     * @generated @ordered
     */
    protected static final double WIDTH_EDEFAULT = 0.0;

    /**
     * The cached value of the '{@link #getWidth() <em>Width</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getWidth()
     * @generated @ordered
     */
    protected double width = WIDTH_EDEFAULT;

    /**
     * This is true if the Width attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean widthESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SizeImpl()
    {
        super();
    }

    /**
     * An additional constructor that allows creation of a Size instance and initializing member variables.
     * 
     * Note: Manually written
     * 
     * @param dWidth
     * @param dHeight
     */
    public SizeImpl(double dWidth, double dHeight)
    {
        width = dWidth;
        height = dHeight;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass()
    {
        return AttributePackage.eINSTANCE.getSize();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public double getHeight()
    {
        return height;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setHeight(double newHeight)
    {
        double oldHeight = height;
        height = newHeight;
        boolean oldHeightESet = heightESet;
        heightESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.SIZE__HEIGHT, oldHeight, height, !oldHeightESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void unsetHeight()
    {
        double oldHeight = height;
        boolean oldHeightESet = heightESet;
        height = HEIGHT_EDEFAULT;
        heightESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.SIZE__HEIGHT, oldHeight, HEIGHT_EDEFAULT, oldHeightESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetHeight()
    {
        return heightESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public double getWidth()
    {
        return width;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void setWidth(double newWidth)
    {
        double oldWidth = width;
        width = newWidth;
        boolean oldWidthESet = widthESet;
        widthESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, AttributePackage.SIZE__WIDTH, oldWidth, width, !oldWidthESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public void unsetWidth()
    {
        double oldWidth = width;
        boolean oldWidthESet = widthESet;
        width = WIDTH_EDEFAULT;
        widthESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, AttributePackage.SIZE__WIDTH, oldWidth, WIDTH_EDEFAULT, oldWidthESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public boolean isSetWidth()
    {
        return widthESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case AttributePackage.SIZE__HEIGHT:
                return new Double(getHeight());
            case AttributePackage.SIZE__WIDTH:
                return new Double(getWidth());
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
            case AttributePackage.SIZE__HEIGHT:
                setHeight(((Double)newValue).doubleValue());
                return;
            case AttributePackage.SIZE__WIDTH:
                setWidth(((Double)newValue).doubleValue());
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
            case AttributePackage.SIZE__HEIGHT:
                unsetHeight();
                return;
            case AttributePackage.SIZE__WIDTH:
                unsetWidth();
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
            case AttributePackage.SIZE__HEIGHT:
                return isSetHeight();
            case AttributePackage.SIZE__WIDTH:
                return isSetWidth();
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
        result.append(" (height: ");
        if (heightESet) result.append(height); else result.append("<unset>");
        result.append(", width: ");
        if (widthESet) result.append(width); else result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /**
     * /** Convenient creation of a Size object and instantiates member variables
     * 
     * NOTE: Manually written
     * 
     * @param dWidth
     * @param dHeight
     * 
     * @return
     */
    public static final Size create(double dWidth, double dHeight)
    {
        final Size sz = AttributeFactory.eINSTANCE.createSize();
        sz.setWidth(dWidth);
        sz.setHeight(dHeight);
        return sz;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.attribute.Size#scale(double)
     */
    public final void scale(double dScale)
    {
        setWidth(getWidth() * dScale);
        setHeight(getHeight() * dScale);
    }

} //SizeImpl
