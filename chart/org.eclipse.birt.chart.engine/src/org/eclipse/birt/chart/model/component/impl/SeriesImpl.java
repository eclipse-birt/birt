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

package org.eclipse.birt.chart.model.component.impl;

import java.util.Collection;

import org.eclipse.birt.chart.computation.IConstants;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Position;
import org.eclipse.birt.chart.model.attribute.impl.ColorDefinitionImpl;
import org.eclipse.birt.chart.model.attribute.impl.DataPointImpl;
import org.eclipse.birt.chart.model.attribute.impl.LineAttributesImpl;
import org.eclipse.birt.chart.model.component.ComponentFactory;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.DataSet;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Series</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isVisible <em>Visible</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataDefinition <em>Data Definition</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getSeriesIdentifier <em>Series Identifier</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataPoint <em>Data Point</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getDataSet <em>Data Set</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getFormatSpecifier <em>Format Specifier</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getLabelPosition <em>Label Position</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isStacked <em>Stacked</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#getTriggers <em>Triggers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.component.impl.SeriesImpl#isTranslucent <em>Translucent</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SeriesImpl extends EObjectImpl implements Series
{

    /**
     * The default value of the '{@link #isVisible() <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isVisible()
     * @generated @ordered
     */
    protected static final boolean VISIBLE_EDEFAULT = true;

    /**
     * The cached value of the '{@link #isVisible() <em>Visible</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isVisible()
     * @generated @ordered
     */
    protected boolean visible = VISIBLE_EDEFAULT;

    /**
     * This is true if the Visible attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean visibleESet = false;

    /**
     * The cached value of the '{@link #getLabel() <em>Label</em>}' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getLabel()
     * @generated @ordered
     */
    protected Label label = null;

    /**
     * The cached value of the '{@link #getDataDefinition() <em>Data Definition</em>}' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDataDefinition()
     * @generated @ordered
     */
    protected EList dataDefinition = null;

    /**
     * The default value of the '{@link #getSeriesIdentifier() <em>Series Identifier</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesIdentifier()
     * @generated @ordered
     */
    protected static final String SERIES_IDENTIFIER_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSeriesIdentifier() <em>Series Identifier</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getSeriesIdentifier()
     * @generated @ordered
     */
    protected String seriesIdentifier = SERIES_IDENTIFIER_EDEFAULT;

    /**
     * The cached value of the '{@link #getDataPoint() <em>Data Point</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getDataPoint()
     * @generated @ordered
     */
    protected DataPoint dataPoint = null;

    /**
     * The cached value of the '{@link #getDataSet() <em>Data Set</em>}' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getDataSet()
     * @generated @ordered
     */
    protected DataSet dataSet = null;

    /**
     * The cached value of the '{@link #getFormatSpecifier() <em>Format Specifier</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getFormatSpecifier()
     * @generated @ordered
     */
    protected FormatSpecifier formatSpecifier = null;

    /**
     * The default value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated @ordered
     */
    protected static final Position LABEL_POSITION_EDEFAULT = Position.ABOVE_LITERAL;

    /**
     * The cached value of the '{@link #getLabelPosition() <em>Label Position</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     * 
     * @see #getLabelPosition()
     * @generated @ordered
     */
    protected Position labelPosition = LABEL_POSITION_EDEFAULT;

    /**
     * This is true if the Label Position attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean labelPositionESet = false;

    /**
     * The default value of the '{@link #isStacked() <em>Stacked</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isStacked()
     * @generated @ordered
     */
    protected static final boolean STACKED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isStacked() <em>Stacked</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isStacked()
     * @generated @ordered
     */
    protected boolean stacked = STACKED_EDEFAULT;

    /**
     * This is true if the Stacked attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean stackedESet = false;

    /**
     * The cached value of the '{@link #getTriggers() <em>Triggers</em>}' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getTriggers()
     * @generated @ordered
     */
    protected EList triggers = null;

    /**
     * The default value of the '{@link #isTranslucent() <em>Translucent</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #isTranslucent()
     * @generated @ordered
     */
    protected static final boolean TRANSLUCENT_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isTranslucent() <em>Translucent</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isTranslucent()
     * @generated @ordered
     */
    protected boolean translucent = TRANSLUCENT_EDEFAULT;

    /**
     * This is true if the Translucent attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated @ordered
     */
    protected boolean translucentESet = false;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected SeriesImpl()
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
        return ComponentPackage.eINSTANCE.getSeries();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isVisible()
    {
        return visible;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setVisible(boolean newVisible)
    {
        boolean oldVisible = visible;
        visible = newVisible;
        boolean oldVisibleESet = visibleESet;
        visibleESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__VISIBLE, oldVisible,
                visible, !oldVisibleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetVisible()
    {
        boolean oldVisible = visible;
        boolean oldVisibleESet = visibleESet;
        visible = VISIBLE_EDEFAULT;
        visibleESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__VISIBLE, oldVisible,
                VISIBLE_EDEFAULT, oldVisibleESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetVisible()
    {
        return visibleESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Label getLabel()
    {
        return label;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetLabel(Label newLabel, NotificationChain msgs)
    {
        Label oldLabel = label;
        label = newLabel;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.SERIES__LABEL, oldLabel, newLabel);
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
    public void setLabel(Label newLabel)
    {
        if (newLabel != label)
        {
            NotificationChain msgs = null;
            if (label != null)
                msgs = ((InternalEObject) label).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__LABEL, null, msgs);
            if (newLabel != null)
                msgs = ((InternalEObject) newLabel).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__LABEL, null, msgs);
            msgs = basicSetLabel(newLabel, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__LABEL, newLabel, newLabel));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList getDataDefinition()
    {
        if (dataDefinition == null)
        {
            dataDefinition = new EObjectContainmentEList(Query.class, this, ComponentPackage.SERIES__DATA_DEFINITION);
        }
        return dataDefinition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getSeriesIdentifier()
    {
        return seriesIdentifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setSeriesIdentifier(String newSeriesIdentifier)
    {
        String oldSeriesIdentifier = seriesIdentifier;
        seriesIdentifier = newSeriesIdentifier;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__SERIES_IDENTIFIER,
                oldSeriesIdentifier, seriesIdentifier));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public DataPoint getDataPoint()
    {
        return dataPoint;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetDataPoint(DataPoint newDataPoint, NotificationChain msgs)
    {
        DataPoint oldDataPoint = dataPoint;
        dataPoint = newDataPoint;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.SERIES__DATA_POINT, oldDataPoint, newDataPoint);
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
    public void setDataPoint(DataPoint newDataPoint)
    {
        if (newDataPoint != dataPoint)
        {
            NotificationChain msgs = null;
            if (dataPoint != null)
                msgs = ((InternalEObject) dataPoint).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__DATA_POINT, null, msgs);
            if (newDataPoint != null)
                msgs = ((InternalEObject) newDataPoint).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__DATA_POINT, null, msgs);
            msgs = basicSetDataPoint(newDataPoint, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__DATA_POINT, newDataPoint,
                newDataPoint));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public DataSet getDataSet()
    {
        return dataSet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetDataSet(DataSet newDataSet, NotificationChain msgs)
    {
        DataSet oldDataSet = dataSet;
        dataSet = newDataSet;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.SERIES__DATA_SET, oldDataSet, newDataSet);
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
    public void setDataSet(DataSet newDataSet)
    {
        if (newDataSet != dataSet)
        {
            NotificationChain msgs = null;
            if (dataSet != null)
                msgs = ((InternalEObject) dataSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__DATA_SET, null, msgs);
            if (newDataSet != null)
                msgs = ((InternalEObject) newDataSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__DATA_SET, null, msgs);
            msgs = basicSetDataSet(newDataSet, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__DATA_SET, newDataSet,
                newDataSet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public FormatSpecifier getFormatSpecifier()
    {
        return formatSpecifier;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public NotificationChain basicSetFormatSpecifier(FormatSpecifier newFormatSpecifier, NotificationChain msgs)
    {
        FormatSpecifier oldFormatSpecifier = formatSpecifier;
        formatSpecifier = newFormatSpecifier;
        if (eNotificationRequired())
        {
            ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                ComponentPackage.SERIES__FORMAT_SPECIFIER, oldFormatSpecifier, newFormatSpecifier);
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
    public void setFormatSpecifier(FormatSpecifier newFormatSpecifier)
    {
        if (newFormatSpecifier != formatSpecifier)
        {
            NotificationChain msgs = null;
            if (formatSpecifier != null)
                msgs = ((InternalEObject) formatSpecifier).eInverseRemove(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__FORMAT_SPECIFIER, null, msgs);
            if (newFormatSpecifier != null)
                msgs = ((InternalEObject) newFormatSpecifier).eInverseAdd(this, EOPPOSITE_FEATURE_BASE
                    - ComponentPackage.SERIES__FORMAT_SPECIFIER, null, msgs);
            msgs = basicSetFormatSpecifier(newFormatSpecifier, msgs);
            if (msgs != null)
                msgs.dispatch();
        }
        else if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__FORMAT_SPECIFIER,
                newFormatSpecifier, newFormatSpecifier));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public Position getLabelPosition()
    {
        return labelPosition;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setLabelPosition(Position newLabelPosition)
    {
        Position oldLabelPosition = labelPosition;
        labelPosition = newLabelPosition == null ? LABEL_POSITION_EDEFAULT : newLabelPosition;
        boolean oldLabelPositionESet = labelPositionESet;
        labelPositionESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__LABEL_POSITION,
                oldLabelPosition, labelPosition, !oldLabelPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetLabelPosition()
    {
        Position oldLabelPosition = labelPosition;
        boolean oldLabelPositionESet = labelPositionESet;
        labelPosition = LABEL_POSITION_EDEFAULT;
        labelPositionESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__LABEL_POSITION,
                oldLabelPosition, LABEL_POSITION_EDEFAULT, oldLabelPositionESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetLabelPosition()
    {
        return labelPositionESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isStacked()
    {
        return stacked;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setStacked(boolean newStacked)
    {
        boolean oldStacked = stacked;
        stacked = newStacked;
        boolean oldStackedESet = stackedESet;
        stackedESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__STACKED, oldStacked,
                stacked, !oldStackedESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetStacked()
    {
        boolean oldStacked = stacked;
        boolean oldStackedESet = stackedESet;
        stacked = STACKED_EDEFAULT;
        stackedESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__STACKED, oldStacked,
                STACKED_EDEFAULT, oldStackedESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetStacked()
    {
        return stackedESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public EList getTriggers()
    {
        if (triggers == null)
        {
            triggers = new EObjectContainmentEList(Trigger.class, this, ComponentPackage.SERIES__TRIGGERS);
        }
        return triggers;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isTranslucent()
    {
        return translucent;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setTranslucent(boolean newTranslucent)
    {
        boolean oldTranslucent = translucent;
        translucent = newTranslucent;
        boolean oldTranslucentESet = translucentESet;
        translucentESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, ComponentPackage.SERIES__TRANSLUCENT, oldTranslucent,
                translucent, !oldTranslucentESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetTranslucent()
    {
        boolean oldTranslucent = translucent;
        boolean oldTranslucentESet = translucentESet;
        translucent = TRANSLUCENT_EDEFAULT;
        translucentESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, ComponentPackage.SERIES__TRANSLUCENT,
                oldTranslucent, TRANSLUCENT_EDEFAULT, oldTranslucentESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetTranslucent()
    {
        return translucentESet;
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
                case ComponentPackage.SERIES__LABEL:
                    return basicSetLabel(null, msgs);
                case ComponentPackage.SERIES__DATA_DEFINITION:
                    return ((InternalEList) getDataDefinition()).basicRemove(otherEnd, msgs);
                case ComponentPackage.SERIES__DATA_POINT:
                    return basicSetDataPoint(null, msgs);
                case ComponentPackage.SERIES__DATA_SET:
                    return basicSetDataSet(null, msgs);
                case ComponentPackage.SERIES__FORMAT_SPECIFIER:
                    return basicSetFormatSpecifier(null, msgs);
                case ComponentPackage.SERIES__TRIGGERS:
                    return ((InternalEList) getTriggers()).basicRemove(otherEnd, msgs);
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
            case ComponentPackage.SERIES__VISIBLE:
                return isVisible() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.SERIES__LABEL:
                return getLabel();
            case ComponentPackage.SERIES__DATA_DEFINITION:
                return getDataDefinition();
            case ComponentPackage.SERIES__SERIES_IDENTIFIER:
                return getSeriesIdentifier();
            case ComponentPackage.SERIES__DATA_POINT:
                return getDataPoint();
            case ComponentPackage.SERIES__DATA_SET:
                return getDataSet();
            case ComponentPackage.SERIES__FORMAT_SPECIFIER:
                return getFormatSpecifier();
            case ComponentPackage.SERIES__LABEL_POSITION:
                return getLabelPosition();
            case ComponentPackage.SERIES__STACKED:
                return isStacked() ? Boolean.TRUE : Boolean.FALSE;
            case ComponentPackage.SERIES__TRIGGERS:
                return getTriggers();
            case ComponentPackage.SERIES__TRANSLUCENT:
                return isTranslucent() ? Boolean.TRUE : Boolean.FALSE;
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
            case ComponentPackage.SERIES__VISIBLE:
                setVisible(((Boolean) newValue).booleanValue());
                return;
            case ComponentPackage.SERIES__LABEL:
                setLabel((Label) newValue);
                return;
            case ComponentPackage.SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                getDataDefinition().addAll((Collection) newValue);
                return;
            case ComponentPackage.SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier((String) newValue);
                return;
            case ComponentPackage.SERIES__DATA_POINT:
                setDataPoint((DataPoint) newValue);
                return;
            case ComponentPackage.SERIES__DATA_SET:
                setDataSet((DataSet) newValue);
                return;
            case ComponentPackage.SERIES__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) newValue);
                return;
            case ComponentPackage.SERIES__LABEL_POSITION:
                setLabelPosition((Position) newValue);
                return;
            case ComponentPackage.SERIES__STACKED:
                setStacked(((Boolean) newValue).booleanValue());
                return;
            case ComponentPackage.SERIES__TRIGGERS:
                getTriggers().clear();
                getTriggers().addAll((Collection) newValue);
                return;
            case ComponentPackage.SERIES__TRANSLUCENT:
                setTranslucent(((Boolean) newValue).booleanValue());
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
            case ComponentPackage.SERIES__VISIBLE:
                unsetVisible();
                return;
            case ComponentPackage.SERIES__LABEL:
                setLabel((Label) null);
                return;
            case ComponentPackage.SERIES__DATA_DEFINITION:
                getDataDefinition().clear();
                return;
            case ComponentPackage.SERIES__SERIES_IDENTIFIER:
                setSeriesIdentifier(SERIES_IDENTIFIER_EDEFAULT);
                return;
            case ComponentPackage.SERIES__DATA_POINT:
                setDataPoint((DataPoint) null);
                return;
            case ComponentPackage.SERIES__DATA_SET:
                setDataSet((DataSet) null);
                return;
            case ComponentPackage.SERIES__FORMAT_SPECIFIER:
                setFormatSpecifier((FormatSpecifier) null);
                return;
            case ComponentPackage.SERIES__LABEL_POSITION:
                unsetLabelPosition();
                return;
            case ComponentPackage.SERIES__STACKED:
                unsetStacked();
                return;
            case ComponentPackage.SERIES__TRIGGERS:
                getTriggers().clear();
                return;
            case ComponentPackage.SERIES__TRANSLUCENT:
                unsetTranslucent();
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
            case ComponentPackage.SERIES__VISIBLE:
                return isSetVisible();
            case ComponentPackage.SERIES__LABEL:
                return label != null;
            case ComponentPackage.SERIES__DATA_DEFINITION:
                return dataDefinition != null && !dataDefinition.isEmpty();
            case ComponentPackage.SERIES__SERIES_IDENTIFIER:
                return SERIES_IDENTIFIER_EDEFAULT == null ? seriesIdentifier != null : !SERIES_IDENTIFIER_EDEFAULT
                    .equals(seriesIdentifier);
            case ComponentPackage.SERIES__DATA_POINT:
                return dataPoint != null;
            case ComponentPackage.SERIES__DATA_SET:
                return dataSet != null;
            case ComponentPackage.SERIES__FORMAT_SPECIFIER:
                return formatSpecifier != null;
            case ComponentPackage.SERIES__LABEL_POSITION:
                return isSetLabelPosition();
            case ComponentPackage.SERIES__STACKED:
                return isSetStacked();
            case ComponentPackage.SERIES__TRIGGERS:
                return triggers != null && !triggers.isEmpty();
            case ComponentPackage.SERIES__TRANSLUCENT:
                return isSetTranslucent();
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
        result.append(" (visible: ");
        if (visibleESet)
            result.append(visible);
        else
            result.append("<unset>");
        result.append(", seriesIdentifier: ");
        result.append(seriesIdentifier);
        result.append(", labelPosition: ");
        if (labelPositionESet)
            result.append(labelPosition);
        else
            result.append("<unset>");
        result.append(", stacked: ");
        if (stackedESet)
            result.append(stacked);
        else
            result.append("<unset>");
        result.append(", translucent: ");
        if (translucentESet)
            result.append(translucent);
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
        return false;
    }

    /**
     * A convenience method to create an initialized 'Series' instance
     * 
     * @return
     */
    public static Series create()
    {
        final Series se = ComponentFactory.eINSTANCE.createSeries();
        ((SeriesImpl) se).initialize();
        return se;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#initialize()
     */
    protected void initialize()
    {
        setStacked(false);
        setVisible(true);
        final Label la = LabelImpl.create();
        LineAttributes lia = LineAttributesImpl.create(ColorDefinitionImpl.BLACK(), LineStyle.SOLID_LITERAL, 1);
        la.setOutline(lia);
        lia.setVisible(false);
        //la.setBackground(ColorDefinitionImpl.YELLOW());
        setLabel(la);
        la.setVisible(false);
        setLabelPosition(Position.OUTSIDE_LITERAL);
        setSeriesIdentifier(IConstants.UNDEFINED_STRING);
        setDataPoint(DataPointImpl.create(null, null, ", "));
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.birt.chart.model.component.Series#canBeStacked()
     */
    public boolean canBeStacked()
    {
        return false;
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
} //SeriesImpl
