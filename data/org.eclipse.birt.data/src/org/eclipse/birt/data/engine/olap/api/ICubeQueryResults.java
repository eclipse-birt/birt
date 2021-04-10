
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public ICubeCursor getCubeCursor() throws DataException;

	/**
	 * Cancel the current operation.
	 */
	public void cancel();
}
