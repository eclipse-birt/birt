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

package org.eclipse.birt.chart.model.impl;

import java.util.ArrayList;
import java.util.Collection;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.ModelFactory;
import org.eclipse.birt.chart.model.ModelPackage;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LegendItemType;
import org.eclipse.birt.chart.model.attribute.SeriesHint;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Chart Without Axes</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.impl.ChartWithoutAxesImpl#getSeriesDefinitions <em>Series Definitions</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class ChartWithoutAxesImpl extends ChartImpl implements ChartWithoutAxes
{

    /**
     * The cached value of the '{@link #getSeriesDefinitions() <em>Series Definitions</em>}' containment reference
     * list. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesDefinitions()
     * @generated
     * @ordered
     */
    protected EList seriesDefinitions = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected ChartWithoutAxesImpl()
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
        return ModelPackage.eINSTANCE.getChartWithoutAxes();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList getSeriesDefinitions()
    {
        if (seriesDefinitions == null)
        {
            seriesDefinitions = new EObjectContainmentEList(SeriesDefinition.class, this,
                ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS);
        }
        return seriesDefinitions;
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
                case ModelPackage.CHART_WITHOUT_AXES__DESCRIPTION:
                    return basicSetDescription(null, msgs);
                case ModelPackage.CHART_WITHOUT_AXES__BLOCK:
                    return basicSetBlock(null, msgs);
                case ModelPackage.CHART_WITHOUT_AXES__EXTENDED_PROPERTIES:
                    return ((InternalEList) getExtendedProperties()).basicRemove(otherEnd, msgs);
                case ModelPackage.CHART_WITHOUT_AXES__SAMPLE_DATA:
                    return basicSetSampleData(null, msgs);
                case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
                    return ((InternalEList) getSeriesDefinitions()).basicRemove(otherEnd, msgs);
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
            case ModelPackage.CHART_WITHOUT_AXES__VERSION:
                return getVersion();
            case ModelPackage.CHART_WITHOUT_AXES__TYPE:
                return getType();
            case ModelPackage.CHART_WITHOUT_AXES__SUB_TYPE:
                return getSubType();
            case ModelPackage.CHART_WITHOUT_AXES__DESCRIPTION:
                return getDescription();
            case ModelPackage.CHART_WITHOUT_AXES__BLOCK:
                return getBlock();
            case ModelPackage.CHART_WITHOUT_AXES__DIMENSION:
                return getDimension();
            case ModelPackage.CHART_WITHOUT_AXES__SCRIPT:
                return getScript();
            case ModelPackage.CHART_WITHOUT_AXES__UNITS:
                return getUnits();
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_THICKNESS:
                return new Double(getSeriesThickness());
            case ModelPackage.CHART_WITHOUT_AXES__GRID_COLUMN_COUNT:
                return new Integer(getGridColumnCount());
            case ModelPackage.CHART_WITHOUT_AXES__EXTENDED_PROPERTIES:
                return getExtendedProperties();
            case ModelPackage.CHART_WITHOUT_AXES__SAMPLE_DATA:
                return getSampleData();
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
                return getSeriesDefinitions();
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
            case ModelPackage.CHART_WITHOUT_AXES__VERSION:
                setVersion((String) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__TYPE:
                setType((String) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SUB_TYPE:
                setSubType((String) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__DESCRIPTION:
                setDescription((Text) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__BLOCK:
                setBlock((Block) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__DIMENSION:
                setDimension((ChartDimension) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SCRIPT:
                setScript((String) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__UNITS:
                setUnits((String) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_THICKNESS:
                setSeriesThickness(((Double) newValue).doubleValue());
                return;
            case ModelPackage.CHART_WITHOUT_AXES__GRID_COLUMN_COUNT:
                setGridColumnCount(((Integer) newValue).intValue());
                return;
            case ModelPackage.CHART_WITHOUT_AXES__EXTENDED_PROPERTIES:
                getExtendedProperties().clear();
                getExtendedProperties().addAll((Collection) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SAMPLE_DATA:
                setSampleData((SampleData) newValue);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
                getSeriesDefinitions().clear();
                getSeriesDefinitions().addAll((Collection) newValue);
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
            case ModelPackage.CHART_WITHOUT_AXES__VERSION:
                unsetVersion();
                return;
            case ModelPackage.CHART_WITHOUT_AXES__TYPE:
                setType(TYPE_EDEFAULT);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SUB_TYPE:
                setSubType(SUB_TYPE_EDEFAULT);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__DESCRIPTION:
                setDescription((Text) null);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__BLOCK:
                setBlock((Block) null);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__DIMENSION:
                unsetDimension();
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SCRIPT:
                setScript(SCRIPT_EDEFAULT);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__UNITS:
                setUnits(UNITS_EDEFAULT);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_THICKNESS:
                unsetSeriesThickness();
                return;
            case ModelPackage.CHART_WITHOUT_AXES__GRID_COLUMN_COUNT:
                unsetGridColumnCount();
                return;
            case ModelPackage.CHART_WITHOUT_AXES__EXTENDED_PROPERTIES:
                getExtendedProperties().clear();
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SAMPLE_DATA:
                setSampleData((SampleData) null);
                return;
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
                getSeriesDefinitions().clear();
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
            case ModelPackage.CHART_WITHOUT_AXES__VERSION:
                return isSetVersion();
            case ModelPackage.CHART_WITHOUT_AXES__TYPE:
                return TYPE_EDEFAULT == null ? type != null : !TYPE_EDEFAULT.equals(type);
            case ModelPackage.CHART_WITHOUT_AXES__SUB_TYPE:
                return SUB_TYPE_EDEFAULT == null ? subType != null : !SUB_TYPE_EDEFAULT.equals(subType);
            case ModelPackage.CHART_WITHOUT_AXES__DESCRIPTION:
                return description != null;
            case ModelPackage.CHART_WITHOUT_AXES__BLOCK:
                return block != null;
            case ModelPackage.CHART_WITHOUT_AXES__DIMENSION:
                return isSetDimension();
            case ModelPackage.CHART_WITHOUT_AXES__SCRIPT:
                return SCRIPT_EDEFAULT == null ? script != null : !SCRIPT_EDEFAULT.equals(script);
            case ModelPackage.CHART_WITHOUT_AXES__UNITS:
                return UNITS_EDEFAULT == null ? units != null : !UNITS_EDEFAULT.equals(units);
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_THICKNESS:
                return isSetSeriesThickness();
            case ModelPackage.CHART_WITHOUT_AXES__GRID_COLUMN_COUNT:
                return isSetGridColumnCount();
            case ModelPackage.CHART_WITHOUT_AXES__EXTENDED_PROPERTIES:
                return extendedProperties != null && !extendedProperties.isEmpty();
            case ModelPackage.CHART_WITHOUT_AXES__SAMPLE_DATA:
                return sampleData != null;
            case ModelPackage.CHART_WITHOUT_AXES__SERIES_DEFINITIONS:
                return seriesDefinitions != null && !seriesDefinitions.isEmpty();
        }
        return eDynamicIsSet(eFeature);
    }

    /**
     * A convenience method to create an initialized 'ChartWithoutAxes' instance
     * 
     * @return
     */
    public static final ChartWithoutAxes create()
    {
        final ChartWithoutAxes cwoa = ModelFactory.eINSTANCE.createChartWithoutAxes();
        ((ChartWithoutAxesImpl) cwoa).initialize();
        return cwoa;
    }

    /**
     * 
     * Note: Manually written
     */
    protected final void initialize()
    {
        // INITIALIZE SUPER'S MEMBERS
        super.initialize();
        setGridColumnCount(1);
        getLegend().setItemType(LegendItemType.CATEGORIES_LITERAL);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.Chart#getSeriesForLegend()
     */
    public final SeriesDefinition[] getSeriesForLegend()
    {
        final ArrayList al = new ArrayList();
        EList elOrthogonalSD;
        final EList elBaseSD = getSeriesDefinitions();
        for (int i = 0; i < elBaseSD.size(); i++)
        {
            elOrthogonalSD = ((SeriesDefinition) elBaseSD.get(i)).getSeriesDefinitions();
            al.addAll(elOrthogonalSD);
        }

        return (SeriesDefinition[]) al.toArray(SeriesDefinition.EMPTY_ARRAY);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.ChartWithoutAxes#getRunTimeSeries()
     */
    public final Series[] getRunTimeSeries()
    {
        final ArrayList al = new ArrayList(8);
        final EList el = getSeriesDefinitions();
        recursivelyGetSeries(el, al, 0, -1);
        return (Series[]) al.toArray(Series.EMPTY_ARRAY);
    }

    /**
     * Walks down the series definition tree and retrieves all runtime series.
     * 
     * @param elSDs
     * @param al
     * @param iLevel
     */
    public final void recursivelyGetSeries(EList elSDs, ArrayList al, int iLevel, int iLevelToOmit)
    {
        SeriesDefinition sd;
        EList el;

        for (int i = 0; i < elSDs.size(); i++)
        {
            sd = (SeriesDefinition) elSDs.get(i);
            if (iLevel != iLevelToOmit)
            {
                al.addAll(sd.getRunTimeSeries());
            }
            el = sd.getSeriesDefinitions();
            recursivelyGetSeries(el, al, iLevel + 1, iLevelToOmit);
        }
    }

    /**
     * Walks down the series definition tree and removes all runtime series.
     * 
     * @param elSDs
     * @param al
     * @param iLevel
     */
    private static final void recursivelyRemoveRuntimeSeries(EList elSDs, int iLevel, int iLevelToOmit)
    {
        SeriesDefinition sd;
        EList el;

        for (int i = 0; i < elSDs.size(); i++)
        {
            sd = (SeriesDefinition) elSDs.get(i);
            if (iLevel != iLevelToOmit)
            {
                sd.getSeries().removeAll(sd.getRunTimeSeries());
            }
            el = sd.getSeriesDefinitions();
            recursivelyRemoveRuntimeSeries(el, iLevel + 1, iLevelToOmit);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.Chart#clearSections(int)
     */
    public final void clearSections(int iSectionType)
    {
        if ((iSectionType & IConstants.RUN_TIME) == IConstants.RUN_TIME)
        {
            recursivelyRemoveRuntimeSeries(getSeriesDefinitions(), 0, -1);
        }
    }

} //ChartWithoutAxesImpl
