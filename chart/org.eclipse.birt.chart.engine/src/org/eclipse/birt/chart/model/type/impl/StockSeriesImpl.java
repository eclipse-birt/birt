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

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.Vector;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
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
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Stock Series</b></em>'. <!-- end-user-doc
 * -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#getFill <em>Fill</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.impl.StockSeriesImpl#getLineAttributes <em>Line Attributes</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class StockSeriesImpl extends SeriesImpl implements StockSeries
{

    /**
     * The cached value of the '{@link #getFill() <em>Fill</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getFill()
     * @generated
     * @ordered
     */
    protected Fill fill = null;

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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected StockSeriesImpl()
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
        return TypePackage.eINSTANCE.getStockSeries();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Fill getFill()
    {
        return fill;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetFill(Fill newFill, NotificationChain msgs)
    {
        Fill oldFill = fill;
        fill = newFill;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                TypePackage.STOCK_SERIES__FILL, oldFill, newFill);
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
    public void setFill(Fill newFill)
    {
        if (newFill != fill)
        {
            NotificationChain msgs = null;
            if (fill != null)
                msgs = ((InternalEObject) fill).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.STOCK_SERIES__FILL, null, msgs);
            if (newFill != null)
                msgs = ((InternalEObject) newFill).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.STOCK_SERIES__FILL, null, msgs);
            msgs = basicSetFill(newFill, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__FILL, newFill, newFill));
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
                TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, oldLineAttributes, newLineAttributes);
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
                    - TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, null, msgs);
            if (newLineAttributes != null)
                msgs = ((InternalEObject) newLineAttributes).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - TypePackage.STOCK_SERIES__LINE_ATTRIBUTES, null, msgs);
            msgs = basicSetLineAttributes(newLineAttributes, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.STOCK_SERIES__LINE_ATTRIBUTES,
                newLineAttributes, newLineAttributes));
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
                case TypePackage.STOCK_SERIES__LABEL:
                    return basicSetLabel(null, msgs);
                case TypePackage.STOCK_SERIES__DATA_DEFINITION:
                    return ((InternalEList) getDataDefinition()).basicRemove(otherEnd, msgs);
                case TypePackage.STOCK_SERIES__DATA_POINT:
                    return basicSetDataPoint(null, msgs);
                case TypePackage.STOCK_SERIES__DATA_SET:
                    return basicSetDataSet(null, msgs);
                case TypePackage.STOCK_SERIES__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
                case TypePackage.STOCK_SERIES__FILL:
                    return basicSetFill(null, msgs);
                case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
                    return basicSetLineAttributes(null, msgs);
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
            case TypePackage.STOCK_SERIES__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.STOCK_SERIES__LABEL:
                return getLabel();
            case TypePackage.STOCK_SERIES__DATA_DEFINITION:
                return getDataDefinition();
            case TypePackage.STOCK_SERIES__SERIES_IDENTIFIER:
                return getSeriesIdentifier();
            case TypePackage.STOCK_SERIES__DATA_POINT:
                return getDataPoint();
            case TypePackage.STOCK_SERIES__DATA_SET:
                return getDataSet();
            case TypePackage.STOCK_SERIES__LABEL_POSITION:
                return getLabelPosition();
            case TypePackage.STOCK_SERIES__STACKED:
                return isStacked() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.STOCK_SERIES__TRIGGERS:
                return getTriggers();
            case TypePackage.STOCK_SERIES__TRANSLUCENT:
                return isTranslucent() ? Boolean.TRUE : Boolean.FALSE;
            case TypePackage.STOCK_SERIES__FILL:
                return getFill();
            case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
                return getLineAttributes();
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
            case TypePackage.STOCK_SERIES__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.STOCK_SERIES__LABEL:
                setLabel((Label) newValue);
                return;
            case TypePackage.STOCK_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                getDataDefinition().addAll((Collection) newValue);
                return;
            case TypePackage.STOCK_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier((Object) newValue);
                return;
            case TypePackage.STOCK_SERIES__DATA_POINT:
                setDataPoint((DataPoint) newValue);
                return;
            case TypePackage.STOCK_SERIES__DATA_SET:
                setDataSet((DataSet) newValue);
                return;
            case TypePackage.STOCK_SERIES__LABEL_POSITION:
                setLabelPosition((Position) newValue);
                return;
            case TypePackage.STOCK_SERIES__STACKED:
                setStacked(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.STOCK_SERIES__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case TypePackage.STOCK_SERIES__TRANSLUCENT:
                setTranslucent(((Boolean) newValue).booleanValue());
                return;
            case TypePackage.STOCK_SERIES__FILL:
                setFill((Fill) newValue);
                return;
            case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) newValue);
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
            case TypePackage.STOCK_SERIES__VISIBLE:
                unsetVisible();
                return;
            case TypePackage.STOCK_SERIES__LABEL:
                setLabel((Label) null);
                return;
            case TypePackage.STOCK_SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                return;
            case TypePackage.STOCK_SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier(SERIES_IDENTIFIER_EDEFAULT);
                return;
            case TypePackage.STOCK_SERIES__DATA_POINT:
                setDataPoint((DataPoint) null);
                return;
            case TypePackage.STOCK_SERIES__DATA_SET:
                setDataSet((DataSet) null);
                return;
            case TypePackage.STOCK_SERIES__LABEL_POSITION:
                unsetLabelPosition();
                return;
            case TypePackage.STOCK_SERIES__STACKED:
                unsetStacked();
                return;
            case TypePackage.STOCK_SERIES__TRIGGERS:
                getTriggers().clear();
                return;
            case TypePackage.STOCK_SERIES__TRANSLUCENT:
                unsetTranslucent();
                return;
            case TypePackage.STOCK_SERIES__FILL:
                setFill((Fill) null);
                return;
            case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
                setLineAttributes((LineAttributes) null);
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
            case TypePackage.STOCK_SERIES__VISIBLE:
                return isSetVisible();
            case TypePackage.STOCK_SERIES__LABEL:
                return label != null;
            case TypePackage.STOCK_SERIES__DATA_DEFINITION:
                return dataDefinition != null && !dataDefinition.isEmpty();
            case TypePackage.STOCK_SERIES__SERIES_IDENTIFIER:
                return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null : !SERIES_IDENTIFIER_EDEFAULT
                    .equals(seriesIdentifier);
            case TypePackage.STOCK_SERIES__DATA_POINT:
                return dataPoint != null;
            case TypePackage.STOCK_SERIES__DATA_SET:
                return dataSet != null;
            case TypePackage.STOCK_SERIES__LABEL_POSITION:
                return isSetLabelPosition();
            case TypePackage.STOCK_SERIES__STACKED:
                return isSetStacked();
            case TypePackage.STOCK_SERIES__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case TypePackage.STOCK_SERIES__TRANSLUCENT:
                return isSetTranslucent();
            case TypePackage.STOCK_SERIES__FILL:
                return fill != null;
            case TypePackage.STOCK_SERIES__LINE_ATTRIBUTES:
                return lineAttributes != null;
        }
        return eDynamicIsSet(eFeature);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canParticipateInCombination()
     */
    public final boolean canParticipateInCombination()
    {
        return true;
    }

    public void translateFrom(Series series, Chart chart)
    {
        System.out.println("StockSeriesImpl: DEBUG: translating from " + series.getClass().getName()
            + " to line series.");
        this.getLineAttributes().setVisible(true);
        this.getLineAttributes().setColor(ColorDefinitionImpl.BLACK());
        this.setStacked(false);

        // Copy generic series properties
        this.setLabel(series.getLabel());
        if (series.getLabelPosition().equals(Position.INSIDE_LITERAL)
            || series.getLabelPosition().equals(Position.OUTSIDE_LITERAL))
        {
            this.setLabelPosition(series.getLabelPosition());
        }
        else
        {
            this.setLabelPosition(Position.OUTSIDE_LITERAL);
        }
        this.setVisible(series.isVisible());
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
            this.getDataDefinition().addAll(series.getDataDefinition());
        }

        // Copy series specific properties
        if (series instanceof BarSeries)
        {
            this.getLineAttributes().setColor(((BarSeries) series).getRiserOutline());
        }
        else if (series instanceof LineSeries)
        {
            this.setLineAttributes(((LineSeries) series).getLineAttributes());
        }

        // Update the chart dimensions to 2D
        chart.setDimension(ChartDimension.TWO_DIMENSIONAL_LITERAL);

        // Update the base axis to type text if it isn't already
        if (chart instanceof ChartWithAxes)
        {
            ((Axis) ((ChartWithAxes) chart).getAxes().get(0)).setType(AxisType.DATE_TIME_LITERAL);
            ((Axis) ((ChartWithAxes) chart).getAxes().get(0)).setCategoryAxis(true);
            EList axes = ((Axis) ((ChartWithAxes) chart).getAxes().get(0)).getAssociatedAxes();
            for (int i = 0; i < axes.size(); i++)
            {
                ((Axis) axes.get(i)).setType(AxisType.LINEAR_LITERAL);
                ((Axis) axes.get(i)).setPercent(false);
            }
        }
        else
        {
            throw new IllegalArgumentException(
                chart.getClass().getName()
                    + " is an invalid argument for StockSeriesImpl. The chart model must be an instance of org.eclipse.birt.chart.model.ChartWithAxes.");
        }

        // Update the sampledata in the model
        chart.setSampleData(getConvertedSampleData(chart.getSampleData()));
    }

    private SampleData getConvertedSampleData(SampleData currentSampleData)
    {
        // Convert base sample data
        EList bsdList = currentSampleData.getBaseSampleData();
        Vector vNewBaseSampleData = new Vector();
        for (int i = 0; i < bsdList.size(); i++)
        {
            BaseSampleData bsd = (BaseSampleData) bsdList.get(i);
            bsd.setDataSetRepresentation(getConvertedBaseSampleDataRepresentation(bsd.getDataSetRepresentation()));
            vNewBaseSampleData.add(bsd);
        }
        currentSampleData.getBaseSampleData().clear();
        currentSampleData.getBaseSampleData().addAll(vNewBaseSampleData);

        // Convert orthogonal sample data
        EList osdList = currentSampleData.getOrthogonalSampleData();
        Vector vNewOrthogonalSampleData = new Vector();
        for (int i = 0; i < osdList.size(); i++)
        {
            OrthogonalSampleData osd = (OrthogonalSampleData) osdList.get(i);
            osd
                .setDataSetRepresentation(getConvertedOrthogonalSampleDataRepresentation(osd.getDataSetRepresentation()));
            vNewOrthogonalSampleData.add(osd);
        }
        currentSampleData.getOrthogonalSampleData().clear();
        currentSampleData.getOrthogonalSampleData().addAll(vNewOrthogonalSampleData);
        return currentSampleData;
    }

    private String getConvertedBaseSampleDataRepresentation(String sOldRepresentation)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        StringBuffer sbNewRepresentation = new StringBuffer("");
        SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy", Locale.getDefault());
        int iValueCount = 0;
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            if (!sElement.startsWith("'"))
            {
                Calendar cal = Calendar.getInstance();
                // Increment the date once for each entry so that you get a sequence of dates
                cal.set(Calendar.DATE, cal.get(Calendar.DATE) + iValueCount);
                sbNewRepresentation.append(sdf.format(cal.getTime()));
                iValueCount++;
            }
            else
            {
                sElement = sElement.substring(1, sElement.length() - 1);
                try
                {
                    sdf.parse(sElement);
                    sbNewRepresentation.append(sElement);
                }
                catch (ParseException e )
                {
                    Calendar cal = Calendar.getInstance();
                    // Increment the date once for each entry so that you get a sequence of dates
                    cal.set(Calendar.DATE, cal.get(Calendar.DATE) + iValueCount);
                    sbNewRepresentation.append(sdf.format(cal.getTime()));
                    iValueCount++;
                }
            }
            sbNewRepresentation.append(",");
        }
        return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
    }

    private String getConvertedOrthogonalSampleDataRepresentation(String sOldRepresentation)
    {
        StringTokenizer strtok = new StringTokenizer(sOldRepresentation, ",");
        NumberFormat nf = NumberFormat.getNumberInstance();
        StringBuffer sbNewRepresentation = new StringBuffer("");
        int iValueCount = 0;
        while (strtok.hasMoreTokens())
        {
            String sElement = strtok.nextToken().trim();
            try
            {
                if (nf.parse(sElement).doubleValue() < 0)
                {
                    // If the value is negative, use an arbitrary positive value
                    sElement = String.valueOf(4.0 + iValueCount);
                    iValueCount++;
                }
            }
            catch (ParseException e )
            {
                sElement = String.valueOf(4.0 + iValueCount);
                iValueCount++;
            }
            sbNewRepresentation.append("H");
            sbNewRepresentation.append(sElement);
            sbNewRepresentation.append(" ");

            sbNewRepresentation.append(" L");
            sbNewRepresentation.append(sElement);
            sbNewRepresentation.append(" ");

            sbNewRepresentation.append(" O");
            sbNewRepresentation.append(sElement);
            sbNewRepresentation.append(" ");

            sbNewRepresentation.append(" C");
            sbNewRepresentation.append(sElement);
            sbNewRepresentation.append(",");
        }
        return sbNewRepresentation.toString().substring(0, sbNewRepresentation.length() - 1);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canShareAxisUnit()
     */
    public boolean canShareAxisUnit()
    {
        return false;
    }

    /**
     * A convenience method to create an initialized 'Series' instance NOTE: Manually written
     * 
     * @return
     */
    public static final Series create()
    {
        final StockSeries ss = TypeFactory.eINSTANCE.createStockSeries();
        ((StockSeriesImpl) ss).initialize();
        return ss;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#initialize()
     */
    protected final void initialize()
    {
        super.initialize();
        final LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 0);
        lia.setVisible(true);
        setLineAttributes(lia);
    }
} //StockSeriesImpl
