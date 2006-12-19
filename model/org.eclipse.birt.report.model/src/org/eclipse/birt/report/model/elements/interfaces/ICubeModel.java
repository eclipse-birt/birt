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
 * The interface for Cube element to store the constants on it.
 */

public interface ICubeModel
{

	/**
	 * Name of the property that specifies the data-set in cube element.
	 */

	static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies a list of joints with some
	 * hierarchies.
	 */

	static final String DIMENSION_CONDITIONS_PROP = "dimensionConditions"; //$NON-NLS-1$

	/**
	 * Identifier of the slot that holds dimension list in cube element.
	 */

	static final int DIMENSION_SLOT = 0;

	/**
	 * Identifier of the slot that holds measure list in cube element.
	 */

	static final int MEASURE_SLOT = 1;

	/**
	 * Number of slots in the cube element.
	 */

	static final int SLOT_COUNT = 2;
}
