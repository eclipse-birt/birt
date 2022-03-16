
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

import org.eclipse.birt.data.engine.olap.data.api.ILevel;

/**
 *
 */

public interface IHierarchy {
	/**
	 * Get hierarchy name
	 *
	 * @return
	 */
	String getName();

	/**
	 * Get all levels of this hierarchy
	 *
	 * @return
	 */
	ILevel[] getLevels();

	/**
	 * Get the member size of the lowest level.
	 *
	 * @return
	 */
	int size();

	/**
	 * Closes this hierarchy and safely releases the associated resources .
	 *
	 * @throws IOException
	 */
	void close() throws IOException;
}
