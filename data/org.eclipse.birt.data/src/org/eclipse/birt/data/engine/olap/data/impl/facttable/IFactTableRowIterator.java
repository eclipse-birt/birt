
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
package org.eclipse.birt.data.engine.olap.data.impl.facttable;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.api.MeasureInfo;

/**
 * 
 */

public interface IFactTableRowIterator {
	/**
	 * 
	 * @return
	 */
	public int getDimensionCount();

	/**
	 * 
	 * @param dimensionName
	 * @return
	 */
	public int getDimensionIndex(String dimensionName);

	/**
	 * 
	 * @param dimensionIndex
	 * @return
	 */
	public int getDimensionPosition(int dimensionIndex);

	/**
	 * 
	 * @return
	 */
	public int[] getDimensionPosition();

	/**
	 * 
	 * @param measureIndex
	 * @return
	 */
	public Object getMeasure(int measureIndex);

	/**
	 * 
	 * @return
	 */
	public int getMeasureCount();

	/**
	 * 
	 * @return
	 */
	public MeasureInfo[] getMeasureInfos();

	/**
	 * 
	 * @param measureName
	 * @return
	 */
	public int getMeasureIndex(String measureName);

	/**
	 * 
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	public boolean next() throws IOException, DataException;

	/**
	 * @throws IOException
	 */
	public void close() throws DataException, IOException;

	/**
	 * 
	 * @return
	 */
	public boolean isDuplicatedRow();
}
