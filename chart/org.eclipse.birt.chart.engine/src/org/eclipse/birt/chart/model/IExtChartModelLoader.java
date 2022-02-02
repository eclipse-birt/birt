/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.model;

import org.eclipse.emf.ecore.EPackage;

/**
 * This class defines a common interface to load extra chart model.
 * 
 * @since 2.6
 */

public interface IExtChartModelLoader {
	/**
	 * Returns the package instance of extra chart type.
	 * 
	 * @return
	 */
	EPackage getChartTypePackage();
}
