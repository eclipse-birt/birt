/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
