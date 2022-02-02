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

	public abstract void onRow(int startingGroupLevel, int endingGroupLevel, IResultObject ro, int currentRowIndex)
			throws DataException;

	public abstract void close() throws DataException;

	public abstract Object getLatestAggrValue(String name) throws DataException;

	/**
	 * Get the aggregate value
	 * 
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	public abstract Object getAggrValue(String name, IResultIterator ri) throws DataException;

	public abstract List getAggrValues(String name) throws DataException;

	public abstract boolean hasAggr(String name) throws DataException;

	public abstract Set<String> getAggrNames() throws DataException;

	public abstract IAggrInfo getAggrInfo(String aggrName) throws DataException;

}
