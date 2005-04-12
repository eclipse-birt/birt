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
import java.util.StringTokenizer;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AttributeFactory;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MarkerType;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.type.TypeFactory;
import org.eclipse.birt.chart.model.type.TypePackage;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Line Series</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getMarker <em>Marker</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getLineAttributes <em>Line Attributes</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#isCurve <em>Curve</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.LineSeriesImpl#getShadowColor <em>Shadow Color</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class LineSeriesImpl extends SeriesImpl implements LineSeries
{

    /**
     * The cached value of the '{@link #getMarker() <em>Marker</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getMarker()
     * @generated
     * @ordered
     */
    protected Marker marker = null;

    /**
     * The cached value of the '{@link #getLineAttributes() <em>Line Attributes</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getLineAttributes()
     * @generated
     * @ordered
     */
    protected LineAttributes lineAttributes = null;

    /**
     * The default value of the '{@link #isCurve() <em>Curve</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isCurve()
     * @generated
     * @ordered
     */
    protected static final boolean CURVE_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isCurve() <em>Curve</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isCurve()
     * @generated
     * @ordered
     */
    protected boolean curve = CURVE_EDEFAULT;

    /**
     * This is true if the Curve attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean curveESet = false;

    /**
     * The cached value of the '{@link #getShadowColor() <em>Shadow Color</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getShadowColor()
     * @generated
     * @ordered
     */
    protected ColorDefinition shadowColor = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected LineSeriesImpl()
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
        return TypePackage.eINSTANCE.getLineSeries();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Marker getMarker()
    {
        return marker;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetMarker(Marker newMarker, NotificationChain msgs)
    {
        Marker oldMarker = marker;
        marker = newMarker;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                TypePackage.LINE_SERIES__MARKER, oldMarker, newMarker);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setMarker(Marker newMarker)
    {
        if (newMarker != marker)
        {
            NotificationChain msgs = null;
            if (marker != null)
                msgs = ((InternalEObject) marker).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__MARKER, null, msgs);
            if (newMarker != null)
                msgs = ((InternalEObject) newMarker).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__MARKER, null, msgs);
            msgs = basicSetMarker(newMarker, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__MARKER, newMarker, newMarker));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public LineAttributes getLineAttributes()
    {
        return lineAttributes;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetLineAttributes(LineAttributes newLineAttributes, NotificationChain msgs)
    {
        LineAttributes oldLineAttributes = lineAttributes;
        lineAttributes = newLineAttributes;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                TypePackage.LINE_SERIES__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setLineAttributes(LineAttributes newLineAttributes)
    {
        if (newLineAttributes != lineAttributes)
        {
            NotificationChain msgs = null;
            if (lineAttributes != null)
                msgs = ((InternalEObject) lineAttributes).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__LINE_ATTRIBUTES, null, msgs);
            if (newLineAttributes != null)
                msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__LINE_ATTRIBUTES, null, msgs);
            msgs = basicSetLineAttributes(newLineAttributes, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__LINE_ATTRIBUTES,
                newLineAttributes, newLineAttributes));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isCurve()
    {
        return curve;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setCurve(boolean newCurve)
    {
        boolean oldCurve = curve;
        curve = newCurve;
        boolean oldCurveESet = curveESet;
        curveESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__CURVE, oldCurve, curve,
                !oldCurveESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetCurve()
    {
        boolean oldCurve = curve;
        boolean oldCurveESet = curveESet;
        curve = CURVE_EDEFAULT;
        curveESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, TypePackage.LINE_SERIES__CURVE, oldCurve,
                CURVE_EDEFAULT, oldCurveESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetCurve()
    {
        return curveESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public ColorDefinition getShadowColor()
    {
        return shadowColor;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetShadowColor(ColorDefinition newShadowColor, NotificationChain msgs)
    {
        ColorDefinition oldShadowColor = shadowColor;
        shadowColor = newShadowColor;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                TypePackage.LINE_SERIES__SHADOW_COLOR, oldShadowColor, newShadowColor);
            if (msgs == null)
                msgs = notification;
            else
                msgs.add(notification);
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setShadowColor(ColorDefinition newShadowColor)
    {
        if (newShadowColor != shadowColor)
        {
            NotificationChain msgs = null;
            if (shadowColor != null)
                msgs = ((InternalEObject) shadowColor).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__SHADOW_COLOR, null, msgs);
            if (newShadowColor != null)
                msgs = ((InternalEObject) newShadowColor).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.LINE_SERIES__SHADOW_COLOR, null, msgs);
            msgs = basicSetShadowColor(newShadowColor, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.LINE_SERIES__SHADOW_COLOR,
                newShadowColor, newShadowColor));
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
                case TypePackage.LINE_SERIES__LABEL:
                    return basicSetLabel(null, msgs);
                case TypePackage.LINE_SERIES__DATA_DEFINITION:
                    return ((InternalEList) getDataDefinition()).basicRemove(otherEnd, msgs);
                case TypePackage.LINE_SERIES__DATA_POINT:
                    return basicSetDataPoint(null, msgs);
                case TypePackage.LINE_SERIES__DATA_SET:
                    return basicSetDataSet(null, msgs);
                case TypePackage.LINE_SERIES__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case TypePackage.LINE_SERIES__MARKER:
                    return basicSetMarker(null, msgs);
                case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
                    return basicSetLineAttributes(null, msgs);
                case TypePackage.LINE_SERIES__SHADOW_COLOR:
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
            case TypePackage.LINE_SERIES__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.LINE_SERIES__LABEL:
                return getLabel();
            case TypePackage.LINE_SERIES__DATA_DEFINITION:
                return getDataDefinition();
            case TypePackage.LINE_SERIES__SERIES_IDENTIFIER:
                return getSeriesIdentifier();
            case TypePackage.LINE_SERIES__DATA_POINT:
                return getDataPoint();
            case TypePackage.LINE_SERIES__DATA_SET:
                return getDataSet();
            case TypePackage.LINE_SERIES__LABEL_POSITION:
                return getLabelPosition();
            case TypePackage.LINE_SERIES__STACKED:
                return isStacked() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.LINE_SERIES__TRIGGERS:
                return getTriggers();
            case TypePackage.LINE_SERIES__TRANSLUCENT:
                return isTranslucent() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.LINE_SERIES__MARKER:
                return getMarker();
            case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
                return getLineAttributes();
            case TypePackage.LINE_SERIES__CURVE:
                return isCurve() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.LINE_SERIES__SHADOW_COLOR:
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
            case TypePackage.LINE_SERIES__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.LINE_SERIES__LABEL:
                setLabel((Label) newValue);
                return;
            case TypePackage.LINE_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                getDataDefinition().addAll((Collection) newValue);
                return;
            case TypePackage.LINE_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier((Object) newValue);
                return;
            case TypePackage.LINE_SERIES__DATA_POINT:
                setDataPoint((DataPoint) newValue);
                return;
            case TypePackage.LINE_SERIES__DATA_SET:
                setDataSet((DataSet) newValue);
                return;
            case TypePackage.LINE_SERIES__LABEL_POSITION:
                setLabelPosition((Position) newValue);
                return;
            case TypePackage.LINE_SERIES__STACKED:
                setStacked(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.LINE_SERIES__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case TypePackage.LINE_SERIES__TRANSLUCENT:
                setTranslucent(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.LINE_SERIES__MARKER:
                setMarker((Marker) newValue);
                return;
            case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) newValue);
                return;
            case TypePackage.LINE_SERIES__CURVE:
                setCurve(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.LINE_SERIES__SHADOW_COLOR:
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
            case TypePackage.LINE_SERIES__VISIBLE:
                unsetVisible();
                return;
            case TypePackage.LINE_SERIES__LABEL:
                setLabel((Label) null);
                return;
            case TypePackage.LINE_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                return;
            case TypePackage.LINE_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier(SERIES_IDENTIFIER_EDEFAULT);
                return;
            case TypePackage.LINE_SERIES__DATA_POINT:
                setDataPoint((DataPoint) null);
                return;
            case TypePackage.LINE_SERIES__DATA_SET:
                setDataSet((DataSet) null);
                return;
            case TypePackage.LINE_SERIES__LABEL_POSITION:
                unsetLabelPosition();
                return;
            case TypePackage.LINE_SERIES__STACKED:
                unsetStacked();
                return;
            case TypePackage.LINE_SERIES__TRIGGERS:
                getTriggers().clear();
                return;
            case TypePackage.LINE_SERIES__TRANSLUCENT:
                unsetTranslucent();
                return;
            case TypePackage.LINE_SERIES__MARKER:
                setMarker((Marker) null);
                return;
            case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) null);
                return;
            case TypePackage.LINE_SERIES__CURVE:
                unsetCurve();
                return;
            case TypePackage.LINE_SERIES__SHADOW_COLOR:
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
            case TypePackage.LINE_SERIES__VISIBLE:
                return isSetVisible();
            case TypePackage.LINE_SERIES__LABEL:
                return label != null;
            case TypePackage.LINE_SERIES__DATA_DEFINITION:
                return dataDefinition != null && !dataDefinition.isEmpty();
            case TypePackage.LINE_SERIES__SERIES_IDENTIFIER:
                return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null : !SERIES_IDENTIFIER_EDEFAULT
                    .equals(seriesIdentifier);
            case TypePackage.LINE_SERIES__DATA_POINT:
                return dataPoint != null;
            case TypePackage.LINE_SERIES__DATA_SET:
                return dataSet != null;
            case TypePackage.LINE_SERIES__LABEL_POSITION:
                return isSetLabelPosition();
            case TypePackage.LINE_SERIES__STACKED:
                return isSetStacked();
            case TypePackage.LINE_SERIES__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case TypePackage.LINE_SERIES__TRANSLUCENT:
                return isSetTranslucent();
            case TypePackage.LINE_SERIES__MARKER:
                return marker != null;
            case TypePackage.LINE_SERIES__LINE_ATTRIBUTES:
                return lineAttributes != null;
            case TypePackage.LINE_SERIES__CURVE:
                return isSetCurve();
            case TypePackage.LINE_SERIES__SHADOW_COLOR:
                return shadowColor != null;
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
        result.append(" (curve: ");
        if (curveESet)
            result.append(curve);
        else
            result.append("<unset>");
        result.append(')');
        return result.toString();
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
     */
    public boolean canParticipateInCombination()
    {
        return true;
    }

    public void translateFrom(Series series, int iSeriesDefinitionIndex, Chart chart)
    {
        this.getLineAttributes().setVisible(true);
        this.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
        if (!(series instanceof ScatterSeries))
        {
            Marker marker = AttributeFactory.eINSTANCE.createMarker();
            marker.setSize(5);
            marker.setType(MarkerType.BOX_LITERAL);
            marker.setVisible(true);
            this.setMarker(marker);
        }
        else
        {
            this.setMarker(((ScatterSeries) series).getMarker());
        }

        // Copy generic series properties
        this.setLabel(series.getLabel());
        if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
            || series.getLabelPosition().equals(Position.OUTSIDE_LITERAL))
        {
            this.setLabelPosition(Position.ABOVE_LITERAL);
        }
        else
        {
            this.setLabelPosition(series.getLabelPosition());
        }

        this.setVisible(series.isVisible());
        this.setStacked(series.isStacked());
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_Triggers()))
        {
            this.getTriggers().addAll(series.getTriggers());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataPoint()))
        {
            this.setDataPoint(series.getDataPoint());
        }
        if (series.eIsSet(ComponentPackage.eINSTANCE.getSeries_DataDefinition()))
        {
            this.getDataDefinition().add(series.getDataDefinition().get(0));
        }

        // Copy series specific properties
        if (series instanceof StockSeries)
        {
            this.getLineAttributes().setColor(((StockSeries) series).getLineAttributes().getColor());
        }

        // Update the base axis to type text if it isn't already
        if (chart instanceof ChartWithAxes)
        {
            ((Axis) ((ChartWithAxes) chart).getAxes().get(0)).setCategoryAxis(true);
        }
        else
        {
            throw new IllegalArgumentException(chart.getClass().getName() + " is an invalid argument for LineSeriesImpl. The chart model must be an instance of org.eclipse.birt.chart.model.ChartWithAxes.");
        }

        // Update the sampledata in the model
        chart.setSampleData(getConvertedSampleData(chart.getSampleData(), iSeriesDefinitionIndex));
    }

    private SampleData getConvertedSampleData(SampleData currentSampleData, int iSeriesDefinitionIndex)
    {
        // Do NOT convert the base sample data since the base axis is not being changed

        // Convert orthogonal sample data
        EList osdList = currentSampleData.getOrthogonalSampleData();
        for (int i = 0; i < osdList.size(); i++)
        {
            if (i == iSeriesDefinitionIndex)
            {
                OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get(i);
                osd.setDataSetRepresentation(getConvertedOrthogonalSampleDataRepresentation(osd
                    .getDataSetRepresentation()));
                currentSampleData.getOrthogonalSampleData().set(i, osd);
            }
        }
        return currentSampleData;
    }

    private String getConvertedOrthogonalSampleDataRepresentation(String sOldRepresentation)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        StringBuffer sbNewRepresentation = new StringBuffer("");
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if (sElement.startsWith("H")) // Orthogonal sample data is for a stock chart (Orthogonal sample data CANNOT
            // be text
            {
                StringTokenizer strStockTokenizer = new StringTokenizer(sElement);
                sbNewRepresentation.append(strStockTokenizer.nextToken().trim().substring(1));
            }
            else
            {
                sbNewRepresentation.append(sElement);
            }
            sbNewRepresentation.append(",");
        }
        return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canShareAxisUnit()
     */
    public final boolean canShareAxisUnit()
    {
        return false;
    }

    /**
     * A convenience method to create an initialized 'Series' instance
     * 
     * @return
     */
    public static Series create() // SUBCLASSED BY ScatterSeriesImpl
    {
        final LineSeries ls = TypeFactory.eINSTANCE.createLineSeries();
        ((LineSeriesImpl) ls).initialize();
        return ls;
    }

    /**
     * Initializes all member variables within this object recursively
     * 
     * Note: Manually written
     */
    protected void initialize() // SUBCLASSED BY ScatterSeriesImpl
    {
        super.initialize();

        final LineAttributes lia = AttributeFactory.eINSTANCE.createLineAttributes();
        ((LineAttributesImpl) lia).set(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
        lia.setVisible(true);
        setLineAttributes(lia);
        setLabelPosition(Position.ABOVE_LITERAL);

        final Marker m = AttributeFactory.eINSTANCE.createMarker();
        m.setType(MarkerType.BOX_LITERAL);
        m.setSize(5);
        m.setVisible(true);
        setMarker(m);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
     */
    public boolean canBeStacked()
    {
        return true;
    }

} //LineSeriesImpl
