
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
package org.eclipse.birt.data.engine.executor.cache;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * The interface defined the behavior of a RowResultSet which will be used in
 * SmartCache.
 */

public interface IRowResultSet {
	/**
	 * @return result meta data
	 */
	IResultClass getMetaData();

	/**
	 * Notice the return value of this function is IResultObject. The null value
	 * indicates the cursor exceeds the end of result set.
	 *
	 * @param stopSign
	 * @return next result data
	 * @throws DataException
	 */
	IResultObject next() throws DataException;

	/**
	 * Return the index.
	 *
	 * @return
	 * @throws DataException
	 */
	int getIndex() throws DataException;
}
