/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/
/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */

package org.eclipse.birt.chart.model.type;

import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Difference Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that, during design time,
 * holds the query data for Difference charts, and during run time, holds the
 * values for each data point in the series. Each data point in a Difference
 * Series holds two values. When rendered, a line connects each high value of
 * each data point, another line connects each low value of each data point, and
 * the area between the high and low lines is filled with the series color. <!--
 * end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeMarkers
 * <em>Negative Markers</em>}</li>
 * <li>{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes
 * <em>Negative Line Attributes</em>}</li>
 * </ul>
 * </p>
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries()
 * @model extendedMetaData="name='DifferenceSeries' kind='elementOnly'"
 * @generated
 */
public interface DifferenceSeries extends AreaSeries {

	/**
	 * Returns the value of the '<em><b>Negative Markers</b></em>' containment
	 * reference list. The list contents are of type
	 * {@link org.eclipse.birt.chart.model.attribute.Marker}. <!-- begin-user-doc
	 * --> <!-- end-user-doc --> <!-- begin-model-doc --> Specifies the marker to be
	 * used for displaying the data point on the negative line in the chart. <!--
	 * end-model-doc -->
	 * 
	 * @return the value of the '<em>Negative Markers</em>' containment reference
	 *         list.
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries_NegativeMarkers()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='NegativeMarkers'"
	 * @generated
	 */
	EList<Marker> getNegativeMarkers();

	/**
	 * Returns the value of the '<em><b>Negative Line Attributes</b></em>'
	 * containment reference. <!-- begin-user-doc --> <!-- end-user-doc --> <!--
	 * begin-model-doc --> Specifies the attributes for the negative line used to
	 * represent this series.
	 * 
	 * <!-- end-model-doc -->
	 * 
	 * @return the value of the '<em>Negative Line Attributes</em>' containment
	 *         reference.
	 * @see #setNegativeLineAttributes(LineAttributes)
	 * @see org.eclipse.birt.chart.model.type.TypePackage#getDifferenceSeries_NegativeLineAttributes()
	 * @model containment="true" extendedMetaData="kind='element'
	 *        name='NegativeLineAttributes'"
	 * @generated
	 */
	LineAttributes getNegativeLineAttributes();

	/**
	 * Sets the value of the
	 * '{@link org.eclipse.birt.chart.model.type.DifferenceSeries#getNegativeLineAttributes
	 * <em>Negative Line Attributes</em>}' containment reference. <!--
	 * begin-user-doc --> <!-- end-user-doc -->
	 * 
	 * @param value the new value of the '<em>Negative Line Attributes</em>'
	 *              containment reference.
	 * @see #getNegativeLineAttributes()
	 * @generated
	 */
	void setNegativeLineAttributes(LineAttributes value);

	/**
	 * A convenient method to get an instance copy. This is much faster than the
	 * ECoreUtil.copy().
	 */
	DifferenceSeries copyInstance();

} // DifferenceSeries
