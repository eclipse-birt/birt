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
package org.eclipse.birt.data.engine.executor.cache.disk;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultObject;

/**
 * An iterator on result objects.
 */
interface IRowIterator {
	/**
	 * Moves iterator to the first object.
	 *
	 */
	public void reset() throws DataException;

	/**
	 * Returns the current object in the iteration
	 * 
	 * @throws IOException
	 */
	public IResultObject fetch() throws IOException, DataException;

	/**
	 * Closes the resource associated with this IRowIterator.
	 */
	public void close() throws DataException;
}
