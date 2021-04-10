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
