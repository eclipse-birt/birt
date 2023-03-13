/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.driver;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * This class is to represent multi-dimension resultset. It includes row edge,
 * column edge, page edge( aggregation)'s resultset axis.
 *
 */
public interface IResultSet {
	/**
	 * Get resultset axis for row edge.
	 *
	 * @return
	 */
	IEdgeAxis getRowEdgeResult();

	/**
	 * Get resultset axis for column edge.
	 *
	 * @return
	 */
	IEdgeAxis getColumnEdgeResult();

	/**
	 * Get resultset axis for page edge.
	 *
	 * @return
	 */
	IEdgeAxis getPageEdgeResult();

	/**
	 * Get resultset axis for all aggregation.
	 *
	 * @return
	 */
	IEdgeAxis[] getMeasureResult();

	/**
	 * Get resultset axis for certain aggregation.
	 *
	 * @param name
	 * @return
	 */
	IEdgeAxis getMeasureResult(String name) throws DataException;

}
