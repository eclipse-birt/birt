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
 * 
 *
 */

public interface ITabularCubeModel {

	/**
	 * Name of the property that specifies a list of joints with some hierarchies.
	 */

	static final String DIMENSION_CONDITIONS_PROP = "dimensionConditions"; //$NON-NLS-1$

	/**
	 * Name of the property that specifies the data-set in cube element.
	 */

	static final String DATA_SET_PROP = "dataSet"; //$NON-NLS-1$

	/**
	 * Name of the property that determines whether to automatically generate a
	 * primary key for elements that use this cube so that user no longer need to
	 * set the aggregation for measure.
	 */
	String AUTO_KEY_PROP = "autoKey"; //$NON-NLS-1$
}
