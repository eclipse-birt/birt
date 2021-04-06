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

import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.data.DataElement;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.DataPackage;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeEList;

/**
 * <!-- begin-user-doc --> An implementation of the model object ' <em><b>Series
 * Grouping</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#isEnabled
 * <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingUnit
 * <em>Grouping Unit</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingOrigin
 * <em>Grouping Origin</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupingInterval
 * <em>Grouping Interval</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getGroupType
 * <em>Group Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getAggregateExpression
 * <em>Aggregate Expression</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.impl.SeriesGroupingImpl#getAggregateParameters
 * <em>Aggregate Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class SeriesGroupingImpl extends EObjectImpl implements SeriesGrouping {

	/**
	 * The default value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected static final boolean ENABLED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isEnabled() <em>Enabled</em>}' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isEnabled()
	 * @generated
	 * @ordered
	 */
	protected boolean enabled = ENABLED_EDEFAULT;

	/**
	 * This is true if the Enabled attribute has been set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean enabledESet;

	/**
	 * The default value of the ' {@link #getGroupingUnit() <em>Grouping Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingUnit()
	 * @generated
	 * @ordered
	 */
	protected static final GroupingUnitType GROUPING_UNIT_EDEFAULT = GroupingUnitType.DAYS_LITERAL;

	/**
	 * The cached value of the ' {@link #getGroupingUnit() <em>Grouping Unit</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingUnit()
	 * @generated
	 * @ordered
	 */
	protected GroupingUnitType groupingUnit = GROUPING_UNIT_EDEFAULT;

	/**
	 * This is true if the Grouping Unit attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupingUnitESet;

	/**
	 * @since BIRT 2.3
	 */
	protected static final int DEFAULT_GROUPING_INTERVAL = 1;

	/**
	 * @since BIRT 2.3
	 */
	protected static final String DEFAULT_AGGREGATE_EXPRESSION = "Sum"; //$NON-NLS-1$

	/**
	 * The cached value of the '{@link #getGroupingOrigin() <em>Grouping
	 * Origin</em>}' containment reference. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #getGroupingOrigin()
	 * @generated
	 * @ordered
	 */
	protected DataElement groupingOrigin;

	/**
	 * The default value of the '{@link #getGroupingInterval() <em>Grouping
	 * Interval</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingInterval()
	 * @generated
	 * @ordered
	 */
	protected static final double GROUPING_INTERVAL_EDEFAULT = 1.0;

	/**
	 * The cached value of the '{@link #getGroupingInterval() <em>Grouping
	 * Interval</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupingInterval()
	 * @generated
	 * @ordered
	 */
	protected double groupingInterval = GROUPING_INTERVAL_EDEFAULT;

	/**
	 * This is true if the Grouping Interval attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupingIntervalESet;

	/**
	 * The default value of the '{@link #getGroupType() <em>Group Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 */
	protected static final DataType GROUP_TYPE_EDEFAULT = DataType.TEXT_LITERAL;

	/**
	 * The cached value of the '{@link #getGroupType() <em>Group Type</em>}'
	 * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getGroupType()
	 * @generated
	 * @ordered
	 */
	protected DataType groupType = GROUP_TYPE_EDEFAULT;

	/**
	 * This is true if the Group Type attribute has been set. <!-- begin-user-doc
	 * --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean groupTypeESet;

	/**
	 * The default value of the '{@link #getAggregateExpression() <em>Aggregate
	 * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAggregateExpression()
	 * @generated
	 * @ordered
	 */
	protected static final String AGGREGATE_EXPRESSION_EDEFAULT = "Sum";

	/**
	 * The cached value of the '{@link #getAggregateExpression() <em>Aggregate
	 * Expression</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #getAggregateExpression()
	 * @generated
	 * @ordered
	 */
	protected String aggregateExpression = AGGREGATE_EXPRESSION_EDEFAULT;

	/**
	 * This is true if the Aggregate Expression attribute has been set. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	protected boolean aggregateExpressionESet;

	/**
	 * The cached value of the '{@link #getAggregateParameters() <em>Aggregate
	 * Parameters</em>}' attribute list. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #getAggregateParameters()
	 * @generated
	 * @ordered
	 */
	protected EList<String> aggregateParameters;

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected SeriesGroupingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DataPackage.Literals.SERIES_GROUPING;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setEnabled(boolean newEnabled) {
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
	public void unsetEnabled() {
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
	public boolean isSetEnabled() {
		return enabledESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public GroupingUnitType getGroupingUnit() {
		return groupingUnit;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGroupingUnit(GroupingUnitType newGroupingUnit) {
		GroupingUnitType oldGroupingUnit = groupingUnit;
		groupingUnit = newGroupingUnit == null ? GROUPING_UNIT_EDEFAULT : newGroupingUnit;
		boolean oldGroupingUnitESet = groupingUnitESet;
		groupingUnitESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUPING_UNIT,
					oldGroupingUnit, groupingUnit, !oldGroupingUnitESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetGroupingUnit() {
		GroupingUnitType oldGroupingUnit = groupingUnit;
		boolean oldGroupingUnitESet = groupingUnitESet;
		groupingUnit = GROUPING_UNIT_EDEFAULT;
		groupingUnitESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_GROUPING__GROUPING_UNIT,
					oldGroupingUnit, GROUPING_UNIT_EDEFAULT, oldGroupingUnitESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetGroupingUnit() {
		return groupingUnitESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataElement getGroupingOrigin() {
		return groupingOrigin;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetGroupingOrigin(DataElement newGroupingOrigin, NotificationChain msgs) {
		DataElement oldGroupingOrigin = groupingOrigin;
		groupingOrigin = newGroupingOrigin;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					DataPackage.SERIES_GROUPING__GROUPING_ORIGIN, oldGroupingOrigin, newGroupingOrigin);
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
	public void setGroupingOrigin(DataElement newGroupingOrigin) {
		if (newGroupingOrigin != groupingOrigin) {
			NotificationChain msgs = null;
			if (groupingOrigin != null)
				msgs = ((InternalEObject) groupingOrigin).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_GROUPING__GROUPING_ORIGIN, null, msgs);
			if (newGroupingOrigin != null)
				msgs = ((InternalEObject) newGroupingOrigin).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - DataPackage.SERIES_GROUPING__GROUPING_ORIGIN, null, msgs);
			msgs = basicSetGroupingOrigin(newGroupingOrigin, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUPING_ORIGIN,
					newGroupingOrigin, newGroupingOrigin));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public double getGroupingInterval() {
		return groupingInterval;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGroupingInterval(double newGroupingInterval) {
		double oldGroupingInterval = groupingInterval;
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
	public void unsetGroupingInterval() {
		double oldGroupingInterval = groupingInterval;
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
	public boolean isSetGroupingInterval() {
		return groupingIntervalESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DataType getGroupType() {
		return groupType;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setGroupType(DataType newGroupType) {
		DataType oldGroupType = groupType;
		groupType = newGroupType == null ? GROUP_TYPE_EDEFAULT : newGroupType;
		boolean oldGroupTypeESet = groupTypeESet;
		groupTypeESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__GROUP_TYPE, oldGroupType,
					groupType, !oldGroupTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetGroupType() {
		DataType oldGroupType = groupType;
		boolean oldGroupTypeESet = groupTypeESet;
		groupType = GROUP_TYPE_EDEFAULT;
		groupTypeESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_GROUPING__GROUP_TYPE,
					oldGroupType, GROUP_TYPE_EDEFAULT, oldGroupTypeESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetGroupType() {
		return groupTypeESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public String getAggregateExpression() {
		return aggregateExpression;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setAggregateExpression(String newAggregateExpression) {
		String oldAggregateExpression = aggregateExpression;
		aggregateExpression = newAggregateExpression;
		boolean oldAggregateExpressionESet = aggregateExpressionESet;
		aggregateExpressionESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION,
					oldAggregateExpression, aggregateExpression, !oldAggregateExpressionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void unsetAggregateExpression() {
		String oldAggregateExpression = aggregateExpression;
		boolean oldAggregateExpressionESet = aggregateExpressionESet;
		aggregateExpression = AGGREGATE_EXPRESSION_EDEFAULT;
		aggregateExpressionESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION,
					oldAggregateExpression, AGGREGATE_EXPRESSION_EDEFAULT, oldAggregateExpressionESet));
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public boolean isSetAggregateExpression() {
		return aggregateExpressionESet;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EList<String> getAggregateParameters() {
		if (aggregateParameters == null) {
			aggregateParameters = new EDataTypeEList<String>(String.class, this,
					DataPackage.SERIES_GROUPING__AGGREGATE_PARAMETERS);
		}
		return aggregateParameters;
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN:
			return basicSetGroupingOrigin(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case DataPackage.SERIES_GROUPING__ENABLED:
			return isEnabled();
		case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
			return getGroupingUnit();
		case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN:
			return getGroupingOrigin();
		case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
			return getGroupingInterval();
		case DataPackage.SERIES_GROUPING__GROUP_TYPE:
			return getGroupType();
		case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
			return getAggregateExpression();
		case DataPackage.SERIES_GROUPING__AGGREGATE_PARAMETERS:
			return getAggregateParameters();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case DataPackage.SERIES_GROUPING__ENABLED:
			setEnabled((Boolean) newValue);
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
			setGroupingUnit((GroupingUnitType) newValue);
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN:
			setGroupingOrigin((DataElement) newValue);
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
			setGroupingInterval((Double) newValue);
			return;
		case DataPackage.SERIES_GROUPING__GROUP_TYPE:
			setGroupType((DataType) newValue);
			return;
		case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
			setAggregateExpression((String) newValue);
			return;
		case DataPackage.SERIES_GROUPING__AGGREGATE_PARAMETERS:
			getAggregateParameters().clear();
			getAggregateParameters().addAll((Collection<? extends String>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case DataPackage.SERIES_GROUPING__ENABLED:
			unsetEnabled();
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
			unsetGroupingUnit();
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN:
			setGroupingOrigin((DataElement) null);
			return;
		case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
			unsetGroupingInterval();
			return;
		case DataPackage.SERIES_GROUPING__GROUP_TYPE:
			unsetGroupType();
			return;
		case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
			unsetAggregateExpression();
			return;
		case DataPackage.SERIES_GROUPING__AGGREGATE_PARAMETERS:
			getAggregateParameters().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case DataPackage.SERIES_GROUPING__ENABLED:
			return isSetEnabled();
		case DataPackage.SERIES_GROUPING__GROUPING_UNIT:
			return isSetGroupingUnit();
		case DataPackage.SERIES_GROUPING__GROUPING_ORIGIN:
			return groupingOrigin != null;
		case DataPackage.SERIES_GROUPING__GROUPING_INTERVAL:
			return isSetGroupingInterval();
		case DataPackage.SERIES_GROUPING__GROUP_TYPE:
			return isSetGroupType();
		case DataPackage.SERIES_GROUPING__AGGREGATE_EXPRESSION:
			return isSetAggregateExpression();
		case DataPackage.SERIES_GROUPING__AGGREGATE_PARAMETERS:
			return aggregateParameters != null && !aggregateParameters.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (enabled: "); //$NON-NLS-1$
		if (enabledESet)
			result.append(enabled);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", groupingUnit: "); //$NON-NLS-1$
		if (groupingUnitESet)
			result.append(groupingUnit);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", groupingInterval: "); //$NON-NLS-1$
		if (groupingIntervalESet)
			result.append(groupingInterval);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", groupType: "); //$NON-NLS-1$
		if (groupTypeESet)
			result.append(groupType);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", aggregateExpression: "); //$NON-NLS-1$
		if (aggregateExpressionESet)
			result.append(aggregateExpression);
		else
			result.append("<unset>"); //$NON-NLS-1$
		result.append(", aggregateParameters: "); //$NON-NLS-1$
		result.append(aggregateParameters);
		result.append(')');
		return result.toString();
	}

	/**
	 * A convenience method provided to create a series grouping instance and
	 * initialize its member variables
	 * 
	 * NOTE: Manually written
	 * 
	 * @return
	 */
	public static final SeriesGrouping create() {
		final SeriesGrouping sg = DataFactory.eINSTANCE.createSeriesGrouping();
		sg.setAggregateExpression(DEFAULT_AGGREGATE_EXPRESSION);
		// sg.setGroupingInterval( DEFAULT_GROUPING_INTERVAL );
		// sg.setEnabled( false );
		sg.setGroupType(DataType.TEXT_LITERAL);
		return sg;
	}

	/**
	 * @generated
	 */
	public SeriesGrouping copyInstance() {
		SeriesGroupingImpl dest = new SeriesGroupingImpl();
		dest.set(this);
		return dest;
	}

	/**
	 * @generated
	 */
	protected void set(SeriesGrouping src) {

		// children

		if (src.getGroupingOrigin() != null) {
			setGroupingOrigin(src.getGroupingOrigin().copyInstance());
		}

		// attributes

		enabled = src.isEnabled();

		enabledESet = src.isSetEnabled();

		groupingUnit = src.getGroupingUnit();

		groupingUnitESet = src.isSetGroupingUnit();

		groupingInterval = src.getGroupingInterval();

		groupingIntervalESet = src.isSetGroupingInterval();

		groupType = src.getGroupType();

		groupTypeESet = src.isSetGroupType();

		aggregateExpression = src.getAggregateExpression();

		aggregateExpressionESet = src.isSetAggregateExpression();

		if (src.getAggregateParameters() != null) {
			EList<String> listSrc = src.getAggregateParameters();
			EList<String> list = new BasicEList<String>(listSrc.size());

			for (String element : listSrc) {
				list.add(element);
			}

			aggregateParameters = list;
		}

	}

} // SeriesGroupingImpl
