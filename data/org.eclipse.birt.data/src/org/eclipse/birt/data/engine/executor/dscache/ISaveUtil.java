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

package org.eclipse.birt.data.engine.executor.dscache;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * 
 */

interface ISaveUtil {

	/**
	 * @param resultObject
	 * @throws DataException
	 */
	public abstract void saveObject(IResultObject resultObject) throws DataException;

	/**
	 * @throws DataException
	 */
	public abstract void close() throws DataException;

}