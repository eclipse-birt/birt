/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for DynamicFilterParameter element to store the constants.
 */
public interface IDynamicFilterParameterModel {

	/**
	 * Name of the display type property.
	 */
	String DSIPLAY_TYPE_PROP = "displayType"; //$NON-NLS-1$

	/**
	 * Name of the filterOperator property.
	 */
	String FILTER_OPERATOR_PROP = "filterOperator"; //$NON-NLS-1$

	/**
	 * Name of the column property.
	 */
	String COLUMN_PROP = "column"; //$NON-NLS-1$

	/**
	 * Name of the native data type property.
	 */
	String NATIVE_DATA_TYPE_PROP = "nativeDataType"; //$NON-NLS-1$

}
