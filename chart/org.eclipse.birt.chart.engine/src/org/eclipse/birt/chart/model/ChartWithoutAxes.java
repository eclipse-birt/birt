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
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Chart Without Axes</b></em>'. <!--
 * end-user-doc -->
 * 
 * <!-- begin-model-doc -->
 * 
 * This is a chart without axes...(e.g. Pie Chart, Donut Chart etc.).
 * 
 * <!-- end-model-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.ChartWithoutAxes#getSeriesDefinitions <em>Series Definitions</em>}</li>
 * </ul>
 * </p>
 * 
 * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes()
 * @model @generated
 */
public interface ChartWithoutAxes extends Chart
{

    /**
     * Returns the value of the '<em><b>Series Definitions</b></em>' containment reference list. The list contents
     * are of type {@link org.eclipse.birt.chart.model.data.SeriesDefinition}. <!-- begin-user-doc --> <!--
     * end-user-doc --> <!-- begin-model-doc -->
     * 
     * Defines the source of the series.
     * 
     * <!-- end-model-doc -->
     * 
     * @return the value of the '<em>Series Definitions</em>' containment reference list.
     * @see org.eclipse.birt.chart.model.ModelPackage#getChartWithoutAxes_SeriesDefinitions()
     * @model type="org.eclipse.birt.chart.model.data.SeriesDefinition" containment="true" resolveProxies="false"
     *        required="true"
     * @generated
     */
    EList getSeriesDefinitions();

    /**
     * 
     * @return
     */
    Series[] getRunTimeSeries();
} // ChartWithoutAxes
