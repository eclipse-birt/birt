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

package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IRDAggrUtil {

	/**
	 * 
	 * @param aggrName
	 * @return
	 */
	public abstract boolean contains(String aggrName);

	/**
	 * Return the group level of certain aggregation.
	 * 
	 * @param aggrName
	 * @return
	 */
	public abstract int getGroupLevel(String aggrName);

	/**
	 * 
	 * @return
	 */
	public abstract boolean isRunningAggr(String aggrName);

	/**
	 * 
	 * @param aggrName
	 * @param groupInstanceIndex
	 * @return
	 * @throws DataException
	 */
	public abstract Object getValue(String aggrName, int groupInstanceIndex) throws DataException;

	public abstract void close() throws DataException;
}
