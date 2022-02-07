/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.core.DataException;

public interface IPLSDataPopulator {

	/**
	 * Return the enclosed document iterator.
	 * 
	 * @return
	 */
	public ResultIterator getDocumentIterator();

	/**
	 * 
	 * @throws BirtException
	 */
	public void close() throws BirtException;

	/**
	 * Move to next qualified row.
	 * 
	 * @return
	 * @throws DataException
	 */
	public boolean next() throws DataException;
}
