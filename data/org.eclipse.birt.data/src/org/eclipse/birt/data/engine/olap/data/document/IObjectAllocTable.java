
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

/**
 * A object alloc table.
 */

public interface IObjectAllocTable {
	int BLOCK_SIZE = 4096;

	/**
	 * Get next block of the given block..
	 *
	 * @param blockNo
	 * @return
	 * @throws IOException
	 */
	int getNextBlock(int blockNo) throws IOException;

	/**
	 * Allocated a new block to a object. The parameter blockNo is the last block of
	 * this oject.
	 *
	 * @param blockNo
	 * @return
	 * @throws IOException
	 */
	int allocateBlock(int blockNo) throws IOException;

	/**
	 * Sets the length of this named object.
	 *
	 * @param name
	 * @param length
	 * @throws IOException
	 */
	void setObjectLength(String name, long length) throws IOException;
}
