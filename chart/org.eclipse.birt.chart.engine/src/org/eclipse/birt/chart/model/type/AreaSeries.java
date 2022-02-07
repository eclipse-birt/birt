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

package org.eclipse.birt.chart.model.type;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Area
 * Series</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc --> This is a Series type that, during design time,
 * holds the query data for Area charts, and during run time, holds the value
 * for each data point in the series. When rendered, a line connects each data
 * point, and the area below the line is filled with the series color. <!--
 * end-model-doc -->
 *
 *
 * @see org.eclipse.birt.chart.model.type.TypePackage#getAreaSeries()
 * @model extendedMetaData="name='AreaSeries' kind='elementOnly'"
 * @generated
 */
public interface AreaSeries extends LineSeries {

	/**
	 * @generated
	 */
	AreaSeries copyInstance();

} // AreaSeries
