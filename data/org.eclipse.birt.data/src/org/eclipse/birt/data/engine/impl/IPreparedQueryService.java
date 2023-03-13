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

import org.eclipse.birt.data.engine.api.IQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Mainly provided for subquery execution
 */
public interface IPreparedQueryService {
	/**
	 * @return the associated data source query
	 */
	PreparedDataSourceQuery getDataSourceQuery();

	/**
	 * @param iterator
	 * @param subQueryName
	 * @param subScope
	 * @return query execution result of sub query
	 * @throws DataException
	 */
	IQueryResults execSubquery(IResultIterator iterator, IQueryExecutor parentExecutor, String subQueryName,
			Scriptable subScope) throws DataException;

}
