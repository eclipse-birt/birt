/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
