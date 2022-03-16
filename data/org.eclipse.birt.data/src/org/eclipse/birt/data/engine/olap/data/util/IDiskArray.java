
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
package org.eclipse.birt.data.engine.olap.data.util;

import java.io.IOException;

import org.eclipse.birt.data.engine.api.ICloseListener;

/**
 * A disk based array.
 */

public interface IDiskArray extends ICloseListener {
	/**
	 * Add one element to this array.
	 *
	 * @param o
	 * @return
	 * @throws IOException
	 */
	boolean add(Object o) throws IOException;

	/**
	 * Get the element by index.
	 *
	 * @param index
	 * @return
	 * @throws IOException
	 */
	Object get(int index) throws IOException;

	/**
	 * Return array size.
	 *
	 * @return
	 */
	int size();

	/**
	 * Close this disk array and release the used resource.
	 *
	 * @throws IOException
	 */
	@Override
	void close() throws IOException;

	/**
	 * Clear the elements in this disk array.
	 *
	 * @throws IOException
	 */
	void clear() throws IOException;
}
