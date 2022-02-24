
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
package org.eclipse.birt.data.engine.olap.data.api.cube;

import java.io.IOException;

/**
 * 
 */

public interface ICube {

	/**
	 * 
	 * @return
	 */
	public IDimension[] getDimesions();

	/**
	 * 
	 * @param dimIterators
	 * @param aggregationType
	 * @throws IOException
	 */
	public void close() throws IOException;

	/**
	 * 
	 * @return
	 */
	public String[] getMeasureNames();
}
