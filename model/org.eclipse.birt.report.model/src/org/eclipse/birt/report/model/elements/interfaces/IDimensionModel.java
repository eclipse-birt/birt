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
 * The interface for Dimension element to store the constants on it.
 */

public interface IDimensionModel {

	/**
	 * Name of the property which indicates whether a dimension element is a special
	 * type of Time.
	 */

	String IS_TIME_TYPE_PROP = "isTimeType"; //$NON-NLS-1$

	/**
	 * Name of the property which defines the default hierarchy element in it.
	 */
	String DEFAULT_HIERARCHY_PROP = "defaultHierarchy"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds a list of Hierarchy elements.
	 */

	String HIERARCHIES_PROP = "hierarchies"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the expression to calculate ACL for the
	 * dimension. This expression is evaluated once during the generation of the
	 * cube.
	 */
	String ACL_EXPRESSION_PROP = "ACLExpression"; //$NON-NLS-1$
}
