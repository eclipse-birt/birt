
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
