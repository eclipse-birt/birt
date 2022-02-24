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

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.computation.DataSetIterator;

/**
 * This interface defines the 3D rendering hints required by series renderer.
 */
public interface ISeriesRenderingHints3D extends ISeriesRenderingHints {

	/**
	 * Returns the dataset bound to Series.
	 * 
	 * @return
	 */
	public DataSetIterator getSeriesDataSet();
}
