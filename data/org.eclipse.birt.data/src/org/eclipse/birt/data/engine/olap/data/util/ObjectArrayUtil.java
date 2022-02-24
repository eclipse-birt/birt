
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
package org.eclipse.birt.data.engine.olap.data.util;

/**
 * 
 */

public class ObjectArrayUtil {
	public static Object[] convert(Object[][] objects) {
		int objectLength = 1;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null)
				objectLength += objects[i].length + 1;
			else
				objectLength += 1;
		}

		Object[] result = new Object[objectLength];
		int pos = 0;
		result[0] = new Integer(objects.length);
		pos++;

		for (int i = 0; i < objects.length; i++) {
			if (objects[i] == null) {
				result[pos] = Integer.valueOf(-1);
				pos++;
			} else if (objects[i].length == 0) {
				result[pos] = Integer.valueOf(0);
				pos++;
			} else {
				result[pos] = Integer.valueOf(objects[i].length);
				pos++;
				System.arraycopy(objects[i], 0, result, pos, objects[i].length);
				pos += objects[i].length;
			}
		}
		return result;
	}

	public static Object[][] convert(Object[] objects) {
		int pos = 0;
		Object[][] result = new Object[((Integer) objects[pos]).intValue()][];
		pos++;

		for (int i = 0; i < result.length; i++) {
			int len = ((Integer) objects[pos]).intValue();
			pos++;
			if (len == 0) {
				result[i] = new Object[0];
			} else if (len > 0) {
				result[i] = new Object[len];
				System.arraycopy(objects, pos, result[i], 0, len);
				pos += len;
			}
		}
		return result;
	}
}
