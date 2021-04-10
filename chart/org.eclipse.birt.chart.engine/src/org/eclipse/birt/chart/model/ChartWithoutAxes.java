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

package org.eclipse.birt.chart.model;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Chart
 * Without Axes</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> ChartWithoutAxes represents a chart without any
 * axis. (e.g. Pie Chart, Dial Chart, etc).
 * <p xmlns="http://www.birt.eclipse.org/ChartModel">
 * To create an instance use the factory method:<br/>
 * ChartWithoutAxesImpl.create( );
 * </p>
 * 
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getSeriesDefinitions
 * <em>Series Definitions</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSlice <em>Min
 * Slice</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#isMinSlicePercent
 * <em>Min Slice Percent</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSliceLabel
 * <em>Min Slice Label</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getCoverage
 * <em>Coverage</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes()
 * @model extendedMetaData="name='ChartWithoutAxes' kind='elementOnly'"
 * @generated
 */
public interface ChartWithoutAxes extends Chart {

	/**
	 * Returns the value of the '<em><b>Series Definitions</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.data.SeriesDefinition}. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc -->
	 * 
	 * Defines the source of the series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Series Definitions</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_SeriesDefinitions()
	 * @model type="org.eclipse.birt.chart.model.data.SeriesDefinition"
	 *        containment="true" resolveProxies="false" required="true"
	 * @generated
	 */
	EList<SeriesDefinition> getSeriesDefinitions();

	/**
	 * Returns the value of the '<em><b>Min Slice</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The concept
	 * "min slice" is used for Pie-like charts, by which datapoints are displayed as
	 * slices of a whole graph, the size of the slices corresponds the value's
	 * percentage of the summary. The attribute "MinSlice" of type double specifies
	 * the threshold value of slices. A separate slice will be created for each
	 * datapoint whose value is greater than this threshold, and all values less
	 * than the threshold will be aggregated into one single slice called "min
	 * slice". <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Min Slice</em>' attribute.
	 * @see #isSetMinSlice()
	 * @see #unsetMinSlice()
	 * @see #setMinSlice(double)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_MinSlice()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Double"
	 *        extendedMetaData="kind='element' name='MinSlice'"
	 * @generated
	 */
	double getMinSlice();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSlice <em>Min
	 * Slice</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Min Slice</em>' attribute.
	 * @see #isSetMinSlice()
	 * @see #unsetMinSlice()
	 * @see #getMinSlice()
	 * @generated
	 */
	void setMinSlice(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSlice <em>Min
	 * Slice</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetMinSlice()
	 * @see #getMinSlice()
	 * @see #setMinSlice(double)
	 * @generated
	 */
	void unsetMinSlice();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSlice <em>Min
	 * Slice</em>}' attribute is set. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Min Slice</em>' attribute is set.
	 * @see #unsetMinSlice()
	 * @see #getMinSlice()
	 * @see #setMinSlice(double)
	 * @generated
	 */
	boolean isSetMinSlice();

	/**
	 * Returns the value of the '<em><b>Min Slice Percent</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The boolean
	 * attribute indicates whether the MinSlice value is defined as a percentage.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Min Slice Percent</em>' attribute.
	 * @see #isSetMinSlicePercent()
	 * @see #unsetMinSlicePercent()
	 * @see #setMinSlicePercent(boolean)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_MinSlicePercent()
	 * @model unsettable="true" dataType="org.eclipse.emf.ecore.xml.type.Boolean"
	 *        required="true" extendedMetaData="kind='element'
	 *        name='MinSlicePercent'"
	 * @generated
	 */
	boolean isMinSlicePercent();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#isMinSlicePercent
	 * <em>Min Slice Percent</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Min Slice Percent</em>' attribute.
	 * @see #isSetMinSlicePercent()
	 * @see #unsetMinSlicePercent()
	 * @see #isMinSlicePercent()
	 * @generated
	 */
	void setMinSlicePercent(boolean value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#isMinSlicePercent
	 * <em>Min Slice Percent</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @see #isSetMinSlicePercent()
	 * @see #isMinSlicePercent()
	 * @see #setMinSlicePercent(boolean)
	 * @generated
	 */
	void unsetMinSlicePercent();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#isMinSlicePercent
	 * <em>Min Slice Percent</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Min Slice Percent</em>' attribute is
	 *         set.
	 * @see #unsetMinSlicePercent()
	 * @see #isMinSlicePercent()
	 * @see #setMinSlicePercent(boolean)
	 * @generated
	 */
	boolean isSetMinSlicePercent();

	/**
	 * Returns the value of the '<em><b>Min Slice Label</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> The string
	 * attribute "MinSliceLabel" specifies the label text of the MinSlice.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Min Slice Label</em>' attribute.
	 * @see #setMinSliceLabel(String)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_MinSliceLabel()
	 * @model dataType="org.eclipse.emf.ecore.xml.type.String" required="true"
	 *        extendedMetaData="kind='element' name='MinSliceLabel'"
	 * @generated
	 */
	String getMinSliceLabel();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getMinSliceLabel
	 * <em>Min Slice Label</em>}' attribute. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Min Slice Label</em>' attribute.
	 * @see #getMinSliceLabel()
	 * @generated
	 */
	void setMinSliceLabel(String value);

	/**
	 * Returns the value of the '<em><b>Coverage</b></em>' attribute. <!--
	 * begin-user-doc --> <!-- end-user-doc --> <!-- begin-model-doc --> Attribute
	 * "Coverage" specifies the percentage of size that the chart graphics (pie or
	 * dial) takes from the client area of the plot. By default it's not set, which
	 * means the size will be auto adjusted.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Coverage</em>' attribute.
	 * @see #isSetCoverage()
	 * @see #unsetCoverage()
	 * @see #setCoverage(double)
	 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_Coverage()
	 * @model unsettable="true" dataType="org.eclipse.birt.chart.model.CoverageType"
	 *        required="true" extendedMetaData="kind='element' name='Coverage'"
	 * @generated
	 */
	double getCoverage();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getCoverage
	 * <em>Coverage</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Coverage</em>' attribute.
	 * @see #isSetCoverage()
	 * @see #unsetCoverage()
	 * @see #getCoverage()
	 * @generated
	 */
	void setCoverage(double value);

	/**
	 * Unsets the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getCoverage
	 * <em>Coverage</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @see #isSetCoverage()
	 * @see #getCoverage()
	 * @see #setCoverage(double)
	 * @generated
	 */
	void unsetCoverage();

	/**
	 * Returns whether the value of the
	 * '{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getCoverage
	 * <em>Coverage</em>}' attribute is set. <!-- begin-user-doc --> <!--
	 * end-user-doc -->
	 * 
	 * @return whether the value of the '<em>Coverage</em>' attribute is set.
	 * @see #unsetCoverage()
	 * @see #getCoverage()
	 * @see #setCoverage(double)
	 * @generated
	 */
	boolean isSetCoverage();

	/**
	 * 
	 * @return
	 */
	Series[] getRunTimeSeries();

	/**
	 * @generated
	 */
	ChartWithoutAxes copyInstance();

} // ChartWithoutAxes
