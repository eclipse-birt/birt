/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.render;

import org.eclipse.birt.chart.computation.DataPointHints;
import org.eclipse.birt.chart.computation.DataSetIterator;
import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * This interface defines the rendering hints required by series renderer.
 */
public interface ISeriesRenderingHints {

	/**
	 * Indicates the state is unknown.
	 */
	int UNDEFINED = 0;

	/**
	 * Indicates the base and orthogonal datasets are synchronized.
	 */
	int BASE_ORTHOGONAL_IN_SYNC = 1;

	/**
	 * Indicates the base and orthogonal datasets are out of sync.
	 */
	int BASE_ORTHOGONAL_OUT_OF_SYNC = 2;

	/**
	 * Indicates the base dataset is empty.
	 */
	int BASE_EMPTY = 4;

	/**
	 * Indicates the orthogonal dataset is empty.
	 */
	int ORTHOGONAL_EMPTY = 8;

	/**
	 * Indicates the base and ancillary datasets are synchronized.
	 */
	int BASE_ANCILLARY_IN_SYNC = 16;

	/**
	 * Indicates the base and ancillary datasets are out of sync.
	 */
	int BASE_ANCILLARY_OUT_OF_SYNC = 32;

	/**
	 * Indicates the ancillary dataset is empty.
	 */
	int ANCILLARY_EMPTY = 64;

	/**
	 * @return Returns the dataset structure state of current series rendering
	 *         hints. The value could be one of these defined in this interface:
	 *         <ul>
	 *         <li>{@link #BASE_ORTHOGONAL_IN_SYNC}
	 *         <li>{@link #BASE_ORTHOGONAL_OUT_OF_SYNC}
	 *         <li>{@link #BASE_ANCILLARY_IN_SYNC}
	 *         <li>{@link #BASE_ANCILLARY_OUT_OF_SYNC}
	 *         <li>{@link #BASE_EMPTY}
	 *         <li>{@link #ORTHOGONAL_EMPTY}
	 *         <li>{@link #ANCILLARY_EMPTY}
	 *         <li>{@link #UNDEFINED}
	 *         </ul>
	 */
	int getDataSetStructure();

	/**
	 * @return Returns the dataset bound to base series.
	 */
	DataSetIterator getBaseDataSet();

	/**
	 * @return Returns the dataset bound to orthogonal series.
	 */
	DataSetIterator getOrthogonalDataSet();

	/**
	 * Returns current client area bounds.
	 *
	 * @param bReduceByInsets Specifies if reduce the insets.
	 * @return
	 */
	Bounds getClientAreaBounds(boolean bReduceByInsets);

	/**
	 * @return Returns all datapointhints for current rendering.
	 *
	 * @see DataPointHints
	 */
	DataPointHints[] getDataPoints();
}
