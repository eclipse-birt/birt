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

/**
 * <!-- begin-user-doc --> A representation of the model object
 * '<em><b>Element</b></em>'. <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * 
 * This type defines a single element of data to be plotted in a chart. A data
 * element can hold a scalar or a multi-dimensional value.
 * 
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.birt.chart.model.data.DataPackage#getDataElement()
 * @model extendedMetaData="name='DataElement' kind='empty'"
 * @extends IChartObject
 * @generated
 */
public interface DataElement extends IChartObject {

	/**
	 * @generated
	 */
	DataElement copyInstance();

} // DataElement
