
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
package org.eclipse.birt.data.engine.olap.api;

import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;

/**
 * The new interface ICubeQueryResults is used for acquiring of a CubeCursor.
 */

public interface ICubeQueryResults extends IBaseQueryResults {
	/**
	 * Return the CubeCursor instance that is created by the ICubeQueryResults
	 * instance.
	 *
	 * @return
	 * @throws DataException
	 */
	ICubeCursor getCubeCursor() throws DataException;

	/**
	 * Cancel the current operation.
	 */
	void cancel();
}
