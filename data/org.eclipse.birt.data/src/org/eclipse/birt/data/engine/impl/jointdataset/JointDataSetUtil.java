/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
			if (result != 0) {
				return result;
			}
			return 0;
		}
		return 0;

	}
}
