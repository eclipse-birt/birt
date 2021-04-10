
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
