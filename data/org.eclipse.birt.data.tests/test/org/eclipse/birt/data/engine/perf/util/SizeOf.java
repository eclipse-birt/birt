/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.data.engine.perf.util;

/**
 * Used to create new object instance
 */
interface ObjectInstance {
	/**
	 * @return object instance
	 */
	Object newInstance();
}

/**
 * Achieve the same function with sizeof operator in C language.
 */
class SizeOf {
	/** single instance pattern */
	private final static SizeOf instance = new SizeOf();

	/** global runtime instance */
	private final Runtime runtime = Runtime.getRuntime();

	/**
	 * return the size of memory occupied by object
	 *
	 * @param objectInstance
	 * @return size of object
	 * @throws Exception
	 */
	public static int getObjectSize(ObjectInstance objectInstance) throws Exception {
		return instance.objectSize(objectInstance);
	}

	/**
	 * compute the size of memory occupied by object
	 *
	 * @param objectInstance
	 * @return size of object
	 * @throws Exception
	 */
	private int objectSize(ObjectInstance objectInstance) throws Exception {
		// init
		final int objectCount = 1000;
		final Object[] objects = new Object[objectCount];

		// 1: get memory before allocation
		long heapSize1 = getActualUsedMemory();

		// 2: allocate memory
		for (int i = 0; i < objectCount; i++) {
			objects[i] = objectInstance.newInstance();
		}

		// 3: get memory after allocation
		long heapSize2 = getActualUsedMemory();

		// compute size of object
		return Math.round(((float) (heapSize2 - heapSize1)) / objectCount);
	}

	/**
	 * return actual used memory presently
	 *
	 * @return actual used memory
	 * @throws Exception
	 */
	public static long getUsedMemory() throws Exception {
		return instance.getActualUsedMemory();
	}

	/**
	 * @return used memory of current JVM
	 * @throws Exception
	 */
	private long getActualUsedMemory() throws Exception {
		runGC();
		return getCurrentUsedMemory();
	}

	/**
	 * Running garbage collector several times to remove all unused object
	 *
	 * @throws Exception
	 */
	private void runGC() throws Exception {
		for (int i = 0; i < 15; i++) {
			_runGC();
		}
	}

	/**
	 * Running once
	 *
	 * @throws Exception
	 */
	private void _runGC() throws Exception {
		long usedMemBeforeGC = getCurrentUsedMemory();
		long usedMemAfterGC = Long.MAX_VALUE;

		for (int i = 0; (usedMemBeforeGC < usedMemAfterGC) && (i < 1000); i++) {
			runtime.runFinalization();
			runtime.gc();
			Thread.yield();

			usedMemAfterGC = usedMemBeforeGC;
			usedMemBeforeGC = getCurrentUsedMemory();
		}
	}

	/**
	 * Return current used memory of JVM
	 *
	 * @return current used memory
	 */
	private long getCurrentUsedMemory() {
		return runtime.totalMemory() - runtime.freeMemory();
	}

}
