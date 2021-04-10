
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
