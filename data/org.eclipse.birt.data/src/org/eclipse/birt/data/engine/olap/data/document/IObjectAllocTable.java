
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
package org.eclipse.birt.data.engine.olap.data.document;

import java.io.IOException;

/**
 * A object alloc table.
 */

public interface IObjectAllocTable {
	static final int BLOCK_SIZE = 4096;

	/**
	 * Get next block of the given block..
	 * 
	 * @param blockNo
	 * @return
	 * @throws IOException
	 */
	public int getNextBlock(int blockNo) throws IOException;

	/**
	 * Allocated a new block to a object. The parameter blockNo is the last block of
	 * this oject.
	 * 
	 * @param blockNo
	 * @return
	 * @throws IOException
	 */
	public int allocateBlock(int blockNo) throws IOException;

	/**
	 * Sets the length of this named object.
	 * 
	 * @param name
	 * @param length
	 * @throws IOException
	 */
	public void setObjectLength(String name, long length) throws IOException;
}
