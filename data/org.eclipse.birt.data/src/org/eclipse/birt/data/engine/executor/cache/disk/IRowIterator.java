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
	void reset() throws DataException;

	/**
	 * Returns the current object in the iteration
	 *
	 * @throws IOException
	 */
	IResultObject fetch() throws IOException, DataException;

	/**
	 * Closes the resource associated with this IRowIterator.
	 */
	void close() throws DataException;
}
