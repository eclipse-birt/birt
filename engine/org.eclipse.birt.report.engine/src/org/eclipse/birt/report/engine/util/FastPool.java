/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.util;

public class FastPool {

	private Object[] pool;
	private int poolSize;

	public FastPool() {
		pool = new Object[16];
		poolSize = 0;

	}

	public boolean isEmpty() {
		return poolSize == 0;
	}

	public void add(Object obj) {
		if (poolSize >= pool.length) {
			Object[] newPool = new Object[pool.length + 16];
			System.arraycopy(pool, 0, newPool, 0, pool.length);
			pool = newPool;
		}
		pool[poolSize] = obj;
		poolSize++;
	}

	public Object remove() {
		poolSize--;
		return pool[poolSize];
	}

}
