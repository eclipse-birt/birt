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

import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Series Grouping</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#isEnabled <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingUnit <em>Grouping Unit</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingInterval <em>Grouping Interval</em>}
 * </li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupType <em>Group Type</em>}</li>
 * <li>
 * {@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getAggregateExpression <em>Aggregate Expression</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class SeriesGroupingImpl extends EObjectImpl implements SeriesGrouping
{

    /**
     * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isEnabled()
     * @generated
     * @ordered
     */
    protected static final boolean ENABLED_EDEFAULT = false;

    /**
     * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #isEnabled()
     * @generated
     * @ordered
     */
    protected boolean enabled = ENABLED_EDEFAULT;

    /**
     * This is true if the Enabled attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean enabledESet = false;

    /**
     * The default value of the '{@link #getGroupingUnit() <em>Grouping Unit</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getGroupingUnit()
     * @generated
     * @ordered
     */
    protected static final String GROUPING_UNIT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGroupingUnit() <em>Grouping Unit</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * 
     * @see #getGroupingUnit()
     * @generated
     * @ordered
     */
    protected String groupingUnit = GROUPING_UNIT_EDEFAULT;

    /**
     * The default value of the '{@link #getGroupingInterval() <em>Grouping Interval</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getGroupingInterval()
     * @generated
     * @ordered
     */
    protected static final int GROUPING_INTERVAL_EDEFAULT = 0;

    /**
     * The cached value of the '{@link #getGroupingInterval() <em>Grouping Interval</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getGroupingInterval()
     * @generated
     * @ordered
     */
    protected int groupingInterval = GROUPING_INTERVAL_EDEFAULT;

    /**
     * This is true if the Grouping Interval attribute has been set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     * @ordered
     */
    protected boolean groupingIntervalESet = false;

    /**
     * The default value of the '{@link #getGroupType() <em>Group Type</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getGroupType()
     * @generated
     * @ordered
     */
    protected static final String GROUP_TYPE_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getGroupType() <em>Group Type</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @see #getGroupType()
     * @generated
     * @ordered
     */
    protected String groupType = GROUP_TYPE_EDEFAULT;

    /**
     * The default value of the '{@link #getAggregateExpression() <em>Aggregate Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAggregateExpression()
     * @generated
     * @ordered
     */
    protected static final String AGGREGATE_EXPRESSION_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getAggregateExpression() <em>Aggregate Expression</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #getAggregateExpression()
     * @generated
     * @ordered
     */
    protected String aggregateExpression = AGGREGATE_EXPRESSION_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected SeriesGroupingImpl()
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
        return DataPackage.eINSTANCE.getSeriesGrouping();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isEnabled()
    {
        return enabled;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setEnabled(boolean newEnabled)
    {
        boolean oldEnabled = enabled;
        enabled = newEnabled;
        boolean oldEnabledESet = enabledESet;
        enabledESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__ENABLED, oldEnabled,
                enabled, !oldEnabledESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetEnabled()
    {
        boolean oldEnabled = enabled;
        boolean oldEnabledESet = enabledESet;
        enabled = ENABLED_EDEFAULT;
        enabledESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_GROUPING__ENABLED, oldEnabled,
                ENABLED_EDEFAULT, oldEnabledESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetEnabled()
    {
        return enabledESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getGroupingUnit()
    {
        return groupingUnit;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setGroupingUnit(String newGroupingUnit)
    {
        String oldGroupingUnit = groupingUnit;
        groupingUnit = newGroupingUnit;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUPING_UNIT,
                oldGroupingUnit, groupingUnit));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public int getGroupingInterval()
    {
        return groupingInterval;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setGroupingInterval(int newGroupingInterval)
    {
        int oldGroupingInterval = groupingInterval;
        groupingInterval = newGroupingInterval;
        boolean oldGroupingIntervalESet = groupingIntervalESet;
        groupingIntervalESet = true;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUPING_INTERVAL,
                oldGroupingInterval, groupingInterval, !oldGroupingIntervalESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void unsetGroupingInterval()
    {
        int oldGroupingInterval = groupingInterval;
        boolean oldGroupingIntervalESet = groupingIntervalESet;
        groupingInterval = GROUPING_INTERVAL_EDEFAULT;
        groupingIntervalESet = false;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_GROUPING__GROUPING_INTERVAL,
                oldGroupingInterval, GROUPING_INTERVAL_EDEFAULT, oldGroupingIntervalESet));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public boolean isSetGroupingInterval()
    {
        return groupingIntervalESet;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getGroupType()
    {
        return groupType;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setGroupType(String newGroupType)
    {
        String oldGroupType = groupType;
        groupType = newGroupType;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUP_TYPE,
                oldGroupType, groupType));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public String getAggregateExpression()
    {
        return aggregateExpression;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public void setAggregateExpression(String newAggregateExpression)
    {
        String oldAggregateExpression = aggregateExpression;
        aggregateExpression = newAggregateExpression;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION,
                oldAggregateExpression, aggregateExpression));
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
            case DataPackage.SERIES_GROUPING__ENABLED:
                return isEnabled() ? Boolean.TRUE : Boolean.FALSE;
            case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
                return getGroupingUnit();
            case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
                return new Integer(getGroupingInterval());
            case DataPackage.SERIES_GROUPING__GROUP_TYPE:
                return getGroupType();
            case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
                return getAggregateExpression();
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
            case DataPackage.SERIES_GROUPING__ENABLED:
                setEnabled(((Boolean) newValue).booleanValue());
                return;
            case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
                setGroupingUnit((String) newValue);
                return;
            case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
                setGroupingInterval(((Integer) newValue).intValue());
                return;
            case DataPackage.SERIES_GROUPING__GROUP_TYPE:
                setGroupType((String) newValue);
                return;
            case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
                setAggregateExpression((String) newValue);
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
            case DataPackage.SERIES_GROUPING__ENABLED:
                unsetEnabled();
                return;
            case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
                setGroupingUnit(GROUPING_UNIT_EDEFAULT);
                return;
            case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
                unsetGroupingInterval();
                return;
            case DataPackage.SERIES_GROUPING__GROUP_TYPE:
                setGroupType(GROUP_TYPE_EDEFAULT);
                return;
            case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
                setAggregateExpression(AGGREGATE_EXPRESSION_EDEFAULT);
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
            case DataPackage.SERIES_GROUPING__ENABLED:
                return isSetEnabled();
            case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
                return GROUPING_UNIT_EDEFAULT == null ? groupingUnit != null : !GROUPING_UNIT_EDEFAULT
                    .equals(groupingUnit);
            case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
                return isSetGroupingInterval();
            case DataPackage.SERIES_GROUPING__GROUP_TYPE:
                return GROUP_TYPE_EDEFAULT == null ? groupType != null : !GROUP_TYPE_EDEFAULT.equals(groupType);
            case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
                return AGGREGATE_EXPRESSION_EDEFAULT == null ? aggregateExpression != null
                    : !AGGREGATE_EXPRESSION_EDEFAULT.equals(aggregateExpression);
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
        result.append(" (enabled: ");
        if (enabledESet)
            result.append(enabled);
        else
            result.append("<unset>");
        result.append(", groupingUnit: ");
        result.append(groupingUnit);
        result.append(", groupingInterval: ");
        if (groupingIntervalESet)
            result.append(groupingInterval);
        else
            result.append("<unset>");
        result.append(", groupType: ");
        result.append(groupType);
        result.append(", aggregateExpression: ");
        result.append(aggregateExpression);
        result.append(')');
        return result.toString();
    }

    /**
     * A convenience method provided to create a series grouping instance and initialize its member variables
     * 
     * NOTE: Manually written
     * 
     * @return
     */
    public static final SeriesGrouping create()
    {
        final SeriesGrouping sg = DataFactory.eINSTANCE.createSeriesGrouping();
        sg.setAggregateExpression("Sum");
        sg.setGroupingInterval(2);
        sg.setEnabled(false);
        sg.setGroupType("Text");
        return sg;
    }

    
} //SeriesGroupingImpl
