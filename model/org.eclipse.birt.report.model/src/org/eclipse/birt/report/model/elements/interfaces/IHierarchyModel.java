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
 * The interface for Hierarchy element to store the constants on it.
 */

public interface IHierarchyModel {
	/**
	 * Name of the property that defines a list of filter conditions.
	 */
	String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds all the level elements.
	 */

	String LEVELS_PROP = "levels"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies a list of privilige of users.
	 */

	String ACCESS_CONTROLS_PROP = "accessControls"; //$NON-NLS-1$
}
