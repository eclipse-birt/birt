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
 * Utility class entry
 */
public class SizeOfUtil {
	/** single instance */
	public static SizeOfUtil instance = new SizeOfUtil();

	/**
	 * Return size of object
	 * 
	 * @param objectInstance
	 * @return object size
	 * @throws Exception
	 */
	public int getObjectSize(ObjectInstance objectInstance) throws Exception {
		return SizeOf.getObjectSize(objectInstance);
	}

	/**
	 * Return size point of a certain time
	 * 
	 * @return size point
	 * @throws Exception
	 */
	public SizePoint getUsedMemorySizePoint() throws Exception {
		return new SizePoint(SizeOf.getUsedMemory());
	}

	/**
	 * Return size span between zp1 and zp2, the result is computed by the value of
	 * zp2 subtracts that of zp1.
	 * 
	 * @param zp1
	 * @param zp2
	 * @return size span
	 */
	public long getSizePointSpan(SizePoint zp1, SizePoint zp2) {
		return zp2.getSize() - zp1.getSize();
	}

	/**
	 * String object nees to be created in a special way, and this method is a
	 * helper method for this purpose.
	 * 
	 * @param length
	 * @return string with specified length
	 */
	public static String newString(int length) {
		char[] result = new char[length];
		for (int i = 0; i < length; ++i)
			result[i] = ' ';

		return new String(result);
	}

	/**
	 * Util class
	 */
	public class SizePoint {
		/** stored size */
		private long size;

		/**
		 * Construction
		 * 
		 * @param size
		 */
		private SizePoint(long size) {
			this.size = size;
		}

		/**
		 * @return stored size
		 */
		public long getSize() {
			return size;
		}
	}

}
