/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public int getSubQueryStartingIndex() throws DataException;
}
