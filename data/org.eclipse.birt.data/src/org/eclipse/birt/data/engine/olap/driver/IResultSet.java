/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public IEdgeAxis getRowEdgeResult();

	/**
	 * Get resultset axis for column edge.
	 * 
	 * @return
	 */
	public IEdgeAxis getColumnEdgeResult();

	/**
	 * Get resultset axis for page edge.
	 * 
	 * @return
	 */
	public IEdgeAxis getPageEdgeResult();

	/**
	 * Get resultset axis for all aggregation.
	 * 
	 * @return
	 */
	public IEdgeAxis[] getMeasureResult();

	/**
	 * Get resultset axis for certain aggregation.
	 * 
	 * @param name
	 * @return
	 */
	public IEdgeAxis getMeasureResult(String name) throws DataException;

}
