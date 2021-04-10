
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
	public boolean add(Object o) throws IOException;

	/**
	 * Get the element by index.
	 * 
	 * @param index
	 * @return
	 * @throws IOException
	 */
	public Object get(int index) throws IOException;

	/**
	 * Return array size.
	 * 
	 * @return
	 */
	public int size();

	/**
	 * Close this disk array and release the used resource.
	 * 
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * Clear the elements in this disk array.
	 * 
	 * @throws IOException
	 */
	public void clear() throws IOException;
}
