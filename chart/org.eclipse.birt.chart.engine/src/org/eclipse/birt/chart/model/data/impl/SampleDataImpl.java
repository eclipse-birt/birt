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

import java.util.Collection;

import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Sample Data</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl#getBaseSampleData <em>Base Sample Data</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.SampleDataImpl#getOrthogonalSampleData <em>Orthogonal Sample Data</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SampleDataImpl extends EObjectImpl implements SampleData
{

    /**
     * The cached value of the '{@link #getBaseSampleData() <em>Base Sample Data</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getBaseSampleData()
     * @generated @ordered
     */
    protected EList baseSampleData = null;

    /**
     * The cached value of the '{@link #getOrthogonalSampleData() <em>Orthogonal Sample Data</em>}' containment
     * reference list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getOrthogonalSampleData()
     * @generated @ordered
     */
    protected EList orthogonalSampleData = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected SampleDataImpl()
    {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    protected EClass eStaticClass()
    {
        return DataPackage.eINSTANCE.getSampleData();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList getBaseSampleData()
    {
        if (baseSampleData == null)
        {
            baseSampleData = new EObjectContainmentEList(BaseSampleData.class, this, DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA);
        }
        return baseSampleData;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public EList getOrthogonalSampleData()
    {
        if (orthogonalSampleData == null)
        {
            orthogonalSampleData = new EObjectContainmentEList(OrthogonalSampleData.class, this, DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA);
        }
        return orthogonalSampleData;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass, NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
                case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
                    return ((InternalEList)getBaseSampleData()).basicRemove(otherEnd, msgs);
                case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
                    return ((InternalEList)getOrthogonalSampleData()).basicRemove(otherEnd, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * @generated
     */
    public Object eGet(EStructuralFeature eFeature, boolean resolve)
    {
        switch (eDerivedStructuralFeatureID(eFeature))
        {
            case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
                return getBaseSampleData();
            case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
                return getOrthogonalSampleData();
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
            case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
                getBaseSampleData().clear();
                getBaseSampleData().addAll((Collection)newValue);
                return;
            case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
                getOrthogonalSampleData().clear();
                getOrthogonalSampleData().addAll((Collection)newValue);
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
            case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
                getBaseSampleData().clear();
                return;
            case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
                getOrthogonalSampleData().clear();
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
            case DataPackage.SAMPLE_DATA__BASE_SAMPLE_DATA:
                return baseSampleData != null && !baseSampleData.isEmpty();
            case DataPackage.SAMPLE_DATA__ORTHOGONAL_SAMPLE_DATA:
                return orthogonalSampleData != null && !orthogonalSampleData.isEmpty();
        }
        return eDynamicIsSet(eFeature);
    }

} //SampleDataImpl
