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
 * The interface for Measure element to store the constants on it.
 */

public interface IMeasureModel {

	/**
	 * Name of the property that defines the function to apply on measure element.
	 */

	String FUNCTION_PROP = "function"; //$NON-NLS-1$

	/**
	 * Name of the property. If the isCalculated is false, then the value of this
	 * property should be a column name. If it is true, the value of this property
	 * should be an expression for the computed measure.
	 */

	String MEASURE_EXPRESSION_PROP = "measureExpression"; //$NON-NLS-1$

	/**
	 * Name of the property that indicate whether this measure is computed by other
	 * measures.
	 */

	String IS_CALCULATED_PROP = "isCalculated"; //$NON-NLS-1$

	/**
	 * Name of the property that gives out the data type of this measure.
	 */
	String DATA_TYPE_PROP = "dataType"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the expression to calculate ACL for the
	 * measure. This expression is evaluated once during the generation of the cube.
	 */
	String ACL_EXPRESSION_PROP = "ACLExpression"; //$NON-NLS-1$

	/**
	 * Name of the action property, which defines what action can be performed when
	 * clicking the measure.
	 */

	String ACTION_PROP = "action"; //$NON-NLS-1$

	/**
	 * Name of the format property, which saves the format of the measure.
	 */
	String FORMAT_PROP = "format"; //$NON-NLS-1$

	/**
	 * Name of the alignment property.
	 */
	String ALIGNMENT_PROP = "alignment"; //$NON-NLS-1$

	/**
	 * Name of the property that indicates whether this measure is visible or not.
	 */
	String IS_VISIBLE_PROP = "isVisible"; //$NON-NLS-1$

}
