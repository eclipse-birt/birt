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

package org.eclipse.birt.chart.model.data;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Series Grouping</b></em>'. <!--
 * end-user-doc -->
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
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled <em>Enabled</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit <em>Grouping Unit</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval <em>Grouping Interval</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType <em>Group Type</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression <em>Aggregate Expression</em>}
 * </li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping()
 * @model
 * @generated
 */
public interface SeriesGrouping extends EObject
{

    /**
     * Returns the value of the '<em><b>Enabled</b></em>' attribute. <!-- begin-user-doc --> Gets whether or not
     * grouping is enabled. <!-- end-user-doc --> <!-- begin-model-doc -->
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
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean" required="true"
     * @generated
     */
    boolean isEnabled();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled <em>Enabled</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Enabled</em>' attribute.
     * @see #isSetEnabled()
     * @see #unsetEnabled()
     * @see #isEnabled()
     * @generated
     */
    void setEnabled(boolean value);

    /**
     * Unsets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled <em>Enabled</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetEnabled()
     * @see #isEnabled()
     * @see #setEnabled(boolean)
     * @generated
     */
    void unsetEnabled();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#isEnabled <em>Enabled</em>}' attribute is set. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Enabled</em>' attribute is set.
     * @see #unsetEnabled()
     * @see #isEnabled()
     * @see #setEnabled(boolean)
     * @generated
     */
    boolean isSetEnabled();

    /**
     * Returns the value of the '<em><b>Grouping Unit</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the unit of grouping.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Grouping Unit</em>' attribute.
     * @see #setGroupingUnit(String)
     * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupingUnit()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getGroupingUnit();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingUnit <em>Grouping Unit</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Grouping Unit</em>' attribute.
     * @see #getGroupingUnit()
     * @generated
     */
    void setGroupingUnit(String value);

    /**
     * Returns the value of the '<em><b>Grouping Interval</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
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
     * @model unique="false" unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Int" required="true"
     * @generated
     */
    int getGroupingInterval();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval <em>Grouping Interval</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Grouping Interval</em>' attribute.
     * @see #isSetGroupingInterval()
     * @see #unsetGroupingInterval()
     * @see #getGroupingInterval()
     * @generated
     */
    void setGroupingInterval(int value);

    /**
     * Unsets the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval <em>Grouping Interval</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #isSetGroupingInterval()
     * @see #getGroupingInterval()
     * @see #setGroupingInterval(int)
     * @generated
     */
    void unsetGroupingInterval();

    /**
     * Returns whether the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupingInterval <em>Grouping Interval</em>}'
     * attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @return whether the value of the '<em>Grouping Interval</em>' attribute is set.
     * @see #unsetGroupingInterval()
     * @see #getGroupingInterval()
     * @see #setGroupingInterval(int)
     * @generated
     */
    boolean isSetGroupingInterval();

    /**
     * Returns the value of the '<em><b>Group Type</b></em>' attribute. <!-- begin-user-doc --> <!-- end-user-doc
     * --> <!-- begin-model-doc -->
     * 
     * Specifies the type of data to be grouped. (e.g. Text, Number, Date/Time etc.)
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Group Type</em>' attribute.
     * @see #setGroupType(String)
     * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_GroupType()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getGroupType();

    /**
     * Sets the value of the '{@link org.eclipse.birt.chart.model.data.SeriesGrouping#getGroupType <em>Group Type</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Group Type</em>' attribute.
     * @see #getGroupType()
     * @generated
     */
    void setGroupType(String value);

    /**
     * Returns the value of the '<em><b>Aggregate Expression</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Specifies the aggregate function to be applied on orthogonal values for each grouped unit.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Aggregate Expression</em>' attribute.
     * @see #setAggregateExpression(String)
     * @see org.eclipse.birt.chart.model.data.DataPackage#getSeriesGrouping_AggregateExpression()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
     * @generated
     */
    String getAggregateExpression();

    /**
     * Sets the value of the '
     * {@link org.eclipse.birt.chart.model.data.SeriesGrouping#getAggregateExpression <em>Aggregate Expression</em>}'
     * attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value
     *            the new value of the '<em>Aggregate Expression</em>' attribute.
     * @see #getAggregateExpression()
     * @generated
     */
    void setAggregateExpression(String value);

} // SeriesGrouping
