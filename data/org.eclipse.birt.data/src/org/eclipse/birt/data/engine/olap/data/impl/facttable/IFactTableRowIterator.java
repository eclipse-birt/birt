
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
	int getDimensionCount();

	/**
	 *
	 * @param dimensionName
	 * @return
	 */
	int getDimensionIndex(String dimensionName);

	/**
	 *
	 * @param dimensionIndex
	 * @return
	 */
	int getDimensionPosition(int dimensionIndex);

	/**
	 *
	 * @return
	 */
	int[] getDimensionPosition();

	/**
	 *
	 * @param measureIndex
	 * @return
	 */
	Object getMeasure(int measureIndex);

	/**
	 *
	 * @return
	 */
	int getMeasureCount();

	/**
	 *
	 * @return
	 */
	MeasureInfo[] getMeasureInfos();

	/**
	 *
	 * @param measureName
	 * @return
	 */
	int getMeasureIndex(String measureName);

	/**
	 *
	 * @return
	 * @throws IOException
	 * @throws DataException
	 */
	boolean next() throws IOException, DataException;

	/**
	 * @throws IOException
	 */
	void close() throws DataException, IOException;

	/**
	 *
	 * @return
	 */
	boolean isDuplicatedRow();
}
