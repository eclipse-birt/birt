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

package org.eclipse.birt.data.engine.executor.dscache;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultClass;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

interface ILoadUtil {

	/**
	 * @param resultObject
	 * @throws DataException
	 */
	public abstract IResultObject loadObject() throws DataException;

	/**
	 * @return
	 * @throws DataException
	 */
	public abstract IResultClass loadResultClass() throws DataException;

	/**
	 * @throws DataException
	 */
	public abstract void close() throws DataException;

}
