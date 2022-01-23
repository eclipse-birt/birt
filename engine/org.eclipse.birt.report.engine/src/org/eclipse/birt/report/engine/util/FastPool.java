/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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
