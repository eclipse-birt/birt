
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
package org.eclipse.birt.data.engine.olap.util.filter;

import java.io.IOException;

import org.eclipse.birt.data.engine.core.DataException;

/**
 * 
 */

public interface IFacttableRow {
	/**
	 * 
	 * @param measureName
	 * @return
	 * @throws DataException
	 */
	public Object getMeasureValue(String measureName) throws DataException;

	/**
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @return
	 * @throws DataException
	 */
	public Object[] getLevelKeyValue(String dimensionName, String levelName) throws DataException, IOException;

	/**
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @return
	 * @throws DataException
	 */
	public Object getLevelAttributeValue(String dimensionName, String levelName, String attributeName)
			throws DataException, IOException;
}
