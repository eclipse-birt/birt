/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.core.archive.compound.v3;

import java.io.IOException;

import org.eclipse.birt.core.archive.cache.Cacheable;
import org.eclipse.birt.core.archive.cache.FileCacheManager;

/**
 * 
 * the block is not synchronized as the above top logic ensure that the write
 * and read is synchronized.
 * 
 */
abstract public class Block extends Cacheable {

	final int blockId;

	public Block(FileCacheManager caches, int blockId) {
		super(caches, Integer.valueOf(blockId));
		this.blockId = blockId;
	}

	public int getBlockId() {
		return blockId;
	}

	abstract public void refresh() throws IOException;

	abstract public void flush() throws IOException;
}
