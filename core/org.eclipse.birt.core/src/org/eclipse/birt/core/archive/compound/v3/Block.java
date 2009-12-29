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

import java.io.IOException;

import org.eclipse.birt.core.archive.cache.Cacheable;

/**
 * 
 * the block is not synchronized as the above top logic ensure that the write
 * and read is synchronized.
 * 
 */
abstract public class Block implements Cacheable
{

	Integer cacheKey;
	int blockId;

	public int getBlockId( )
	{
		return blockId;
	}

	public Object getCacheKey( )
	{
		return cacheKey;
	}

	abstract public void refresh( ) throws IOException;

	abstract public void flush( ) throws IOException;
}
