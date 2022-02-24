
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

import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * 
 */

public class CompareUtil {
	public static int compare(Object[] objs1, Object[] objs2) {
		boolean[] asc = new boolean[objs1.length];
		Arrays.fill(asc, true);
		return compare(objs1, objs2, asc);
	}

	public static int compare(Object[] objs1, Object[] objs2, boolean[] asc) {
		int result = 0;
		for (int i = 0; i < objs1.length; i++) {
			Object temp1 = objs1[i];
			Object temp2 = objs2[i];

			result = compare(temp1, temp2) * (asc[i] ? 1 : -1);
			if (result != 0)
				break;
		}
		return result;
	}

	public static int compare(Object temp1, Object temp2) {
		try {
			return ScriptEvalUtil.compare(temp1, temp2);
		} catch (DataException e) {
			throw new IllegalArgumentException();
		}
	}

	public static void sort(IDiskArray array, Comparator comparator, IStructureCreator creator) throws IOException {
		DiskSortedStack ss = new DiskSortedStack(4096, false, comparator, creator);
		for (int i = 0; i < array.size(); i++) {
			ss.push(array.get(i));
		}

		array.clear();

		for (int i = 0; i < ss.size(); i++) {
			array.add(ss.pop());
		}
	}
}
