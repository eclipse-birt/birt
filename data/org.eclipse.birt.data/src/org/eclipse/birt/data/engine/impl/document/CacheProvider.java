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
package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * This GroupUtil class do only know the information service of CacheProvider.
 */
public interface CacheProvider {

	/**
	 * @return total row count
	 */
	public int getCount();

	/**
	 * @return current result index
	 */
	public int getCurrentIndex();

	/**
	 * Move current result cursor to a specified index
	 * 
	 * @param destIndex
	 * @throws DataException
	 */
	public void moveTo(int destIndex) throws DataException;

	public boolean next() throws DataException;

}
