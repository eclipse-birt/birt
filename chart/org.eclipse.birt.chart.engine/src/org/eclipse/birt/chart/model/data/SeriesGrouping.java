/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.data;

import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Series
 * Grouping</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type holds all grouping-related information for a series.
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled
 * <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit
 * <em>Grouping Unit</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin
 * <em>Grouping Origin</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval
 * <em>Grouping Interval</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType
 * <em>Group Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression
 * <em>Aggregate Expression</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateParameters
 * <em>Aggregate Parameters</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping()
 * @model extendedMetaData="name='SeriesGrouping' kind='elementOnly'"
 * @extends IChartObject
 * @generated
 */
public interface SeriesGrouping extends IChartObject {

	/**
	 * Returns the value of the '<em><b>Enabled</b></em>' attribute. <!--
	 * begin-user-doc --> Gets whether or not grouping is enabled. <!-- end-user-doc
	 * --> <!-- begin-model-doc -->
	 * 
	 * Specifies the interval of spanning.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Enabled</em>' attribute.
	 * @see #isSetEnabled()
	 * @see #unsetEnabled()
	 * @see #setEnabled(boolean)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_Enabled()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
	 * @generated
	 */
	boolean isEnabled();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled
	 * <em>Enabled</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Enabled</em>' attribute.
	 * @see #isSetEnabled()
	 * @see #unsetEnabled()
	 * @see #isEnabled()
	 * @generated
	 */
	void setEnabled(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled
	 * <em>Enabled</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetEnabled()
	 * @see #isEnabled()
	 * @see #setEnabled(boolean)
	 * @generated
	 */
	void unsetEnabled();

	/**
	 * Returns whether the value of the '
	 * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled
	 * <em>Enabled</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Enabled</em>' attribute is set.
	 * @see #unsetEnabled()
	 * @see #isEnabled()
	 * @see #setEnabled(boolean)
	 * @generated
	 */
	boolean isSetEnabled();

	/**
	 * Returns the value of the '<em><b>Grouping Unit</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the unit of grouping.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Grouping Unit</em>' attribute.
	 * @see #setGroupingUnit(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupingUnit()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	GroupingUnitType getGroupingUnit();

	/**
	 * Sets the value of the '
	 * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit
	 * <em>Grouping Unit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Grouping Unit</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.GroupingUnitType
	 * @see #isSetGroupingUnit()
	 * @see #unsetGroupingUnit()
	 * @see #getGroupingUnit()
	 * @generated
	 */
	void setGroupingUnit(GroupingUnitType value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit
	 * <em>Grouping Unit</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetGroupingUnit()
	 * @see #getGroupingUnit()
	 * @see #setGroupingUnit(GroupingUnitType)
	 * @generated
	 */
	void unsetGroupingUnit();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit
	 * <em>Grouping Unit</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Grouping Unit</em>' attribute is set.
	 * @see #unsetGroupingUnit()
	 * @see #getGroupingUnit()
	 * @see #setGroupingUnit(GroupingUnitType)
	 * @generated
	 */
	boolean isSetGroupingUnit();

	/**
	 * Returns the value of the '<em><b>Grouping Origin</b></em>' containment
	 * reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc
	 * -->
	 * 
	 * A data element that specifies the starting point for grouping. This does not
	 * apply to grouping of Text values. If undefined, grouping starts at the
	 * minimum value.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Grouping Origin</em>' containment reference.
	 * @see #setGroupingOrigin(DataElement)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupingOrigin()
	 * @model containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	DataElement getGroupingOrigin();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingOrigin
	 * <em>Grouping Origin</em>}' containment reference. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Grouping Origin</em>' containment
	 *              reference.
	 * @see #getGroupingOrigin()
	 * @generated
	 */
	void setGroupingOrigin(DataElement value);

	/**
	 * Returns the value of the '<em><b>Grouping Interval</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the interval of grouping.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Grouping Interval</em>' attribute.
	 * @see #isSetGroupingInterval()
	 * @see #unsetGroupingInterval()
	 * @see #setGroupingInterval(int)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupingInterval()
	 * @model unique="false" unsettable="true"
	 *        dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
	 * @generated
	 */
	double getGroupingInterval();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval
	 * <em>Grouping Interval</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Grouping Interval</em>' attribute.
	 * @see #isSetGroupingInterval()
	 * @see #unsetGroupingInterval()
	 * @see #getGroupingInterval()
	 * @generated
	 */
	void setGroupingInterval(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval
	 * <em>Grouping Interval</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetGroupingInterval()
	 * @see #getGroupingInterval()
	 * @see #setGroupingInterval(double)
	 * @generated
	 */
	void unsetGroupingInterval();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval
	 * <em>Grouping Interval</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Grouping Interval</em>' attribute is
	 *         set.
	 * @see #unsetGroupingInterval()
	 * @see #getGroupingInterval()
	 * @see #setGroupingInterval(double)
	 * @generated
	 */
	boolean isSetGroupingInterval();

	/**
	 * Returns the value of the '<em><b>Group Type</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the type of data to be grouped. (e.g. Text, Number, Date/Time etc.)
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Group Type</em>' attribute.
	 * @see #setGroupType(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupType()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	DataType getGroupType();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType
	 * <em>Group Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @param value the new value of the '<em>Group Type</em>' attribute.
	 * @see org.eclipse.birt.chart.model.attribute.DataType
	 * @see #isSetGroupType()
	 * @see #unsetGroupType()
	 * @see #getGroupType()
	 * @generated
	 */
	void setGroupType(DataType value);

	/**
	 * Unsets the value of the '
	 * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType
	 * <em>Group Type</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc
	 * -->
	 * 
	 * @see #isSetGroupType()
	 * @see #getGroupType()
	 * @see #setGroupType(DataType)
	 * @generated
	 */
	void unsetGroupType();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType
	 * <em>Group Type</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Group Type</em>' attribute is set.
	 * @see #unsetGroupType()
	 * @see #getGroupType()
	 * @see #setGroupType(DataType)
	 * @generated
	 */
	boolean isSetGroupType();

	/**
	 * Returns the value of the '<em><b>Aggregate Expression</b></em>' attribute.
	 * <!-- begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Specifies the aggregate function to be applied on orthogonal values for each
	 * grouped unit.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Aggregate Expression</em>' attribute.
	 * @see #setAggregateExpression(String)
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_AggregateExpression()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        required="true"
	 * @generated
	 */
	String getAggregateExpression();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression
	 * <em>Aggregate Expression</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Aggregate Expression</em>' attribute.
	 * @see #isSetAggregateExpression()
	 * @see #unsetAggregateExpression()
	 * @see #getAggregateExpression()
	 * @generated
	 */
	void setAggregateExpression(String value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression
	 * <em>Aggregate Expression</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetAggregateExpression()
	 * @see #getAggregateExpression()
	 * @see #setAggregateExpression(String)
	 * @generated
	 */
	void unsetAggregateExpression();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression
	 * <em>Aggregate Expression</em>}' attribute is set. <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Aggregate Expression</em>' attribute is
	 *         set.
	 * @see #unsetAggregateExpression()
	 * @see #getAggregateExpression()
	 * @see #setAggregateExpression(String)
	 * @generated
	 */
	boolean isSetAggregateExpression();

	/**
	 * Returns the value of the '<em><b>Aggregate Parameters</b></em>' attribute
	 * list. The list contents are of type {@link java.lang.String}. <!--
	 * begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Aggregate Parameters</em>' attribute list isn't
	 * clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Aggregate Parameters</em>' attribute list.
	 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_AggregateParameters()
	 * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String"
	 *        extendedMetaData="kind='element' name='AggregateParameters'"
	 * @generated
	 */
	EList<String> getAggregateParameters();

	/**
	 * @generated
	 */
	SeriesGrouping copyInstance();

} // SeriesGrouping
