/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * Interface for Sub Query Executor, which defines some behavior that only
 * needed for subqueries.
 *
 */
public interface ISubQueryExecutor extends IQueryExecutor {
	/**
	 * Return the starting row index of the group instance in which the subquery is
	 * defined.
	 *
	 * @return
	 * @throws DataException
	 */
	int getSubQueryStartingIndex() throws DataException;
}
