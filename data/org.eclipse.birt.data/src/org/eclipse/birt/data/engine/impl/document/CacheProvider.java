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
package org.eclipse.birt.data.engine.impl.document;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * This GroupUtil class do only know the information service of CacheProvider.
 */
public interface CacheProvider {

	/**
	 * @return total row count
	 */
	int getCount();

	/**
	 * @return current result index
	 */
	int getCurrentIndex();

	/**
	 * Move current result cursor to a specified index
	 *
	 * @param destIndex
	 * @throws DataException
	 */
	void moveTo(int destIndex) throws DataException;

	boolean next() throws DataException;

}
