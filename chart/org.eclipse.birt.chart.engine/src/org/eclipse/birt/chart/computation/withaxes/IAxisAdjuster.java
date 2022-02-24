/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.computation.withaxes;

import org.eclipse.birt.chart.exception.ChartException;

/**
 * The interface declares function to adjust axis(axes) scale and location.
 * 
 * @since 2.5
 */

public interface IAxisAdjuster {
	/**
	 * Adjust axis(axes) scale and location.
	 * 
	 * @throws ChartException
	 */
	public void adjust() throws ChartException;

}
