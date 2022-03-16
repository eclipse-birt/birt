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
	boolean contains(String aggrName);

	/**
	 * Return the group level of certain aggregation.
	 *
	 * @param aggrName
	 * @return
	 */
	int getGroupLevel(String aggrName);

	/**
	 *
	 * @return
	 */
	boolean isRunningAggr(String aggrName);

	/**
	 *
	 * @param aggrName
	 * @param groupInstanceIndex
	 * @return
	 * @throws DataException
	 */
	Object getValue(String aggrName, int groupInstanceIndex) throws DataException;

	void close() throws DataException;
}
