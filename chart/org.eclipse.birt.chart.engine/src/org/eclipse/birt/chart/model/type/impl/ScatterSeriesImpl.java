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

package org.eclipse.birt.chart.model.type.impl;

import java.util.Collection;

import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Scatter Series</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class ScatterSeriesImpl extends LineSeriesImpl implements ScatterSeries
{

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ScatterSeriesImpl()
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
        return TypePackage.eINSTANCE.getScatterSeries();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, Class baseClass,
        NotificationChain msgs)
    {
        if (featureID >= 0)
        {
            switch (eDerivedStructuralFeatureID(featureID, baseClass))
            {
                case TypePackage.SCATTER_SERIES__LABEL:
                    return basicSetLabel(null, msgs);
                case TypePackage.SCATTER_SERIES__DATA_DEFINITION:
                    return ((InternalEList) getDataDefinition()).basicRemove(otherEnd, msgs);
                case TypePackage.SCATTER_SERIES__DATA_POINT:
                    return basicSetDataPoint(null, msgs);
                case TypePackage.SCATTER_SERIES__DATA_SET:
                    return basicSetDataSet(null, msgs);
                case TypePackage.SCATTER_SERIES__FORMAT_SPECIFIER:
                    return basicSetFormatSpecifier(null, msgs);
                case TypePackage.SCATTER_SERIES__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case TypePackage.SCATTER_SERIES__MARKER:
                    return basicSetMarker(null, msgs);
                case TypePackage.SCATTER_SERIES__LINE_ATTRIBUTES:
                    return basicSetLineAttributes(null, msgs);
                case TypePackage.SCATTER_SERIES__SHADOW_COLOR:
                    return basicSetShadowColor(null, msgs);
                default:
                    return eDynamicInverseRemove(otherEnd, featureID, baseClass, msgs);
            }
        }
        return eBasicSetContainer(null, featureID, msgs);
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
            case TypePackage.SCATTER_SERIES__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.SCATTER_SERIES__LABEL:
                return getLabel();
            case TypePackage.SCATTER_SERIES__DATA_DEFINITION:
                return getDataDefinition();
            case TypePackage.SCATTER_SERIES__SERIES_IDENTIFIER:
                return getSeriesIdentifier();
            case TypePackage.SCATTER_SERIES__DATA_POINT:
                return getDataPoint();
            case TypePackage.SCATTER_SERIES__DATA_SET:
                return getDataSet();
            case TypePackage.SCATTER_SERIES__FORMAT_SPECIFIER:
                return getFormatSpecifier();
            case TypePackage.SCATTER_SERIES__LABEL_POSITION:
                return getLabelPosition();
            case TypePackage.SCATTER_SERIES__STACKED:
                return isStacked() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.SCATTER_SERIES__TRIGGERS:
                return getTriggers();
            case TypePackage.SCATTER_SERIES__TRANSLUCENT:
                return isTranslucent() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.SCATTER_SERIES__MARKER:
                return getMarker();
            case TypePackage.SCATTER_SERIES__LINE_ATTRIBUTES:
                return getLineAttributes();
            case TypePackage.SCATTER_SERIES__CURVE:
                return isCurve() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.SCATTER_SERIES__SHADOW_COLOR:
                return getShadowColor();
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
            case TypePackage.SCATTER_SERIES__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.SCATTER_SERIES__LABEL:
                setLabel((Label) newValue);
                return;
            case TypePackage.SCATTER_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                getDataDefinition().addAll((Collection) newValue);
                return;
            case TypePackage.SCATTER_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier((String) newValue);
                return;
            case TypePackage.SCATTER_SERIES__DATA_POINT:
                setDataPoint((DataPoint) newValue);
                return;
            case TypePackage.SCATTER_SERIES__DATA_SET:
                setDataSet((DataSet) newValue);
                return;
            case TypePackage.SCATTER_SERIES__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) newValue);
                return;
            case TypePackage.SCATTER_SERIES__LABEL_POSITION:
                setLabelPosition((Position) newValue);
                return;
            case TypePackage.SCATTER_SERIES__STACKED:
                setStacked(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.SCATTER_SERIES__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case TypePackage.SCATTER_SERIES__TRANSLUCENT:
                setTranslucent(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.SCATTER_SERIES__MARKER:
                setMarker((Marker) newValue);
                return;
            case TypePackage.SCATTER_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) newValue);
                return;
            case TypePackage.SCATTER_SERIES__CURVE:
                setCurve(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.SCATTER_SERIES__SHADOW_COLOR:
                setShadowColor((ColorDefinition) newValue);
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
            case TypePackage.SCATTER_SERIES__VISIBLE:
                unsetVisible();
                return;
            case TypePackage.SCATTER_SERIES__LABEL:
                setLabel((Label) null);
                return;
            case TypePackage.SCATTER_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                return;
            case TypePackage.SCATTER_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier(SERIES_IDENTIFIER_EDEFAULT);
                return;
            case TypePackage.SCATTER_SERIES__DATA_POINT:
                setDataPoint((DataPoint) null);
                return;
            case TypePackage.SCATTER_SERIES__DATA_SET:
                setDataSet((DataSet) null);
                return;
            case TypePackage.SCATTER_SERIES__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) null);
                return;
            case TypePackage.SCATTER_SERIES__LABEL_POSITION:
                unsetLabelPosition();
                return;
            case TypePackage.SCATTER_SERIES__STACKED:
                unsetStacked();
                return;
            case TypePackage.SCATTER_SERIES__TRIGGERS:
                getTriggers().clear();
                return;
            case TypePackage.SCATTER_SERIES__TRANSLUCENT:
                unsetTranslucent();
                return;
            case TypePackage.SCATTER_SERIES__MARKER:
                setMarker((Marker) null);
                return;
            case TypePackage.SCATTER_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) null);
                return;
            case TypePackage.SCATTER_SERIES__CURVE:
                unsetCurve();
                return;
            case TypePackage.SCATTER_SERIES__SHADOW_COLOR:
                setShadowColor((ColorDefinition) null);
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
            case TypePackage.SCATTER_SERIES__VISIBLE:
                return isSetVisible();
            case TypePackage.SCATTER_SERIES__LABEL:
                return label != null;
            case TypePackage.SCATTER_SERIES__DATA_DEFINITION:
                return dataDefinition != null && !dataDefinition.isEmpty();
            case TypePackage.SCATTER_SERIES__SERIES_IDENTIFIER:
                return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null : !SERIES_IDENTIFIER_EDEFAULT
                    .equals(seriesIdentifier);
            case TypePackage.SCATTER_SERIES__DATA_POINT:
                return dataPoint != null;
            case TypePackage.SCATTER_SERIES__DATA_SET:
                return dataSet != null;
            case TypePackage.SCATTER_SERIES__FORMAT_SPECIFIER:
                return formatSpecifier != null;
            case TypePackage.SCATTER_SERIES__LABEL_POSITION:
                return isSetLabelPosition();
            case TypePackage.SCATTER_SERIES__STACKED:
                return isSetStacked();
            case TypePackage.SCATTER_SERIES__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case TypePackage.SCATTER_SERIES__TRANSLUCENT:
                return isSetTranslucent();
            case TypePackage.SCATTER_SERIES__MARKER:
                return marker != null;
            case TypePackage.SCATTER_SERIES__LINE_ATTRIBUTES:
                return lineAttributes != null;
            case TypePackage.SCATTER_SERIES__CURVE:
                return isSetCurve();
            case TypePackage.SCATTER_SERIES__SHADOW_COLOR:
                return shadowColor != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /*
     *  (non-Javadoc)
     * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
     */
    public final boolean canBeStacked()
    {
        return false;
    }
    
    /**
     * A convenience method to create an initialized 'Series' instance
     * 
     * @return
     */
    public static final Series create()
    {
        final ScatterSeries ss = TypeFactory.eINSTANCE.createScatterSeries();
        ((ScatterSeriesImpl) ss).initialize();
        return ss;
    }

    /**
     * Initializes all member variables within this object recursively
     * 
     * Note: Manually written
     */
    protected final void initialize()
    {
        super.initialize();
        getLineAttributes().setVisible(false);
        getMarker().setType(MarkerType.CROSSHAIR_LITERAL);
    }

} //ScatterSeriesImpl
