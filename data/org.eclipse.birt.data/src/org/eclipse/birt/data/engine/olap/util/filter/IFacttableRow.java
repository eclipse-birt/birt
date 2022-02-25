
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
	Object getMeasureValue(String measureName) throws DataException;

	/**
	 *
	 * @param dimensionName
	 * @param levelName
	 * @return
	 * @throws DataException
	 */
	Object[] getLevelKeyValue(String dimensionName, String levelName) throws DataException, IOException;

	/**
	 *
	 * @param dimensionName
	 * @param levelName
	 * @return
	 * @throws DataException
	 */
	Object getLevelAttributeValue(String dimensionName, String levelName, String attributeName)
			throws DataException, IOException;
}
