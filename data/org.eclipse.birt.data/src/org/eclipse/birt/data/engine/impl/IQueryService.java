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
package org.eclipse.birt.data.engine.impl;

import org.eclipse.birt.data.engine.core.DataException;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */
public interface IQueryService {

	/**
	 * @return
	 */
	public boolean isClosed();

	/**
	 * @return
	 */
	public int getNestedLevel();

	/**
	 * @return
	 */
	public Scriptable getQueryScope();

	/**
	 * @return
	 * @throws DataException
	 */
	public IExecutorHelper getExecutorHelper() throws DataException;

	/**
	 * @param nestedCount
	 * @return
	 */
	public DataSetRuntime[] getDataSetRuntime(int nestedCount);

}
