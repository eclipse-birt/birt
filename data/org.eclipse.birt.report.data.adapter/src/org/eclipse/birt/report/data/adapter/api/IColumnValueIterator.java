
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;

/**
 * 
 */

public interface IColumnValueIterator {
	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	public boolean next() throws BirtException;

	/**
	 * 
	 * @return
	 * @throws BirtException
	 */
	public Object getValue() throws BirtException;

	/**
	 * 
	 * @throws BirtException
	 */
	public void close() throws BirtException;
}
