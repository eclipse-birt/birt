/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
