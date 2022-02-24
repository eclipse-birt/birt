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
 * The interface for Cube element to store the constants on it.
 */

public interface ICubeModel {

	/**
	 * Name of the property that defines a list of filter conditions.
	 */
	static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Name of the property which indicates whether this measure group is default in
	 * cube.
	 */
	String DEFAULT_MEASURE_GROUP_PROP = "defaultMeasureGroup"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds dimension list in cube element.
	 */

	static final String DIMENSIONS_PROP = "dimensions"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds measure group list in cube element.
	 */

	static final String MEASURE_GROUPS_PROP = "measureGroups"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies a list of privilige of users.
	 */

	static final String ACCESS_CONTROLS_PROP = "accessControls"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the expression to calculate ACL for the
	 * measure. This expression is evaluated once during the generation of the cube.
	 */
	String ACL_EXPRESSION_PROP = "ACLExpression"; //$NON-NLS-1$
}
