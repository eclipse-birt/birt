/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.olap.util.sort;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;

/**
 *
 */

public interface IJSSortHelper extends ITargetSort {
	/**
	 *
	 * @param row
	 * @return
	 * @throws DataException
	 */
	Object evaluate(IResultRow row) throws DataException;

	/**
	 * close this helper to clean up the registered javascript objects.
	 */
	void close();

}
