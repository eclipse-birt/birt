/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.core.archive.compound.v3;

abstract public class Ext2Block extends Block {

	static final int BLOCK_SIZE = Ext2FileSystem.BLOCK_SIZE;

	final Ext2FileSystem fs;

	Ext2Block(Ext2FileSystem fs, int blockId) {
		super(fs == null ? null : fs.cacheManager, blockId);
		this.fs = fs;
	}

	Ext2FileSystem getFileSystem() {
		return fs;
	}
}
