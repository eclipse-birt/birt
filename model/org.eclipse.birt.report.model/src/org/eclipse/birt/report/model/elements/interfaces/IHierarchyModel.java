/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	static final String FILTER_PROP = "filter"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds all the level elements.
	 */

	static final String LEVELS_PROP = "levels"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies a list of privilige of users.
	 */

	static final String ACCESS_CONTROLS_PROP = "accessControls"; //$NON-NLS-1$
}
