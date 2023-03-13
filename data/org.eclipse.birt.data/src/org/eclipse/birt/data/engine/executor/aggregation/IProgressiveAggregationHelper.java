/*******************************************************************************
 * Copyright (c) 2004, 2011 Actuate Corporation.
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

package org.eclipse.birt.data.engine.executor.aggregation;

import java.util.List;
import java.util.Set;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IAggrInfo;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 *
 */

public interface IProgressiveAggregationHelper {

	void onRow(int startingGroupLevel, int endingGroupLevel, IResultObject ro, int currentRowIndex)
			throws DataException;

	void close() throws DataException;

	Object getLatestAggrValue(String name) throws DataException;

	/**
	 * Get the aggregate value
	 *
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	Object getAggrValue(String name, IResultIterator ri) throws DataException;

	List getAggrValues(String name) throws DataException;

	boolean hasAggr(String name) throws DataException;

	Set<String> getAggrNames() throws DataException;

	IAggrInfo getAggrInfo(String aggrName) throws DataException;

}
