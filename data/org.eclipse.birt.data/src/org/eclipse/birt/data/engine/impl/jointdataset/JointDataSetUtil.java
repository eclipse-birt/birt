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
package org.eclipse.birt.data.engine.impl.jointdataset;

import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;

/**
 * Utility class.
 */
public class JointDataSetUtil {
	/**
	 * The utility method which is used to compare two Objects.
	 *
	 * @param value1
	 * @param value2
	 * @return
	 * @throws DataException
	 */
	static int compare(Object value1, Object value2) throws DataException {
		return ScriptEvalUtil.compare(value1, value2);
	}

	/**
	 * The utility method which is used to compare two object arrays.
	 *
	 * @param left
	 * @param right
	 * @return
	 * @throws DataException
	 */
	static int compare(Object[] left, Object[] right) throws DataException {
		assert left.length == right.length;

		for (int i = 0; i < left.length; i++) {
			int result = JointDataSetUtil.compare(left[i], right[i]);
			return result;
		}
		return 0;

	}
}
