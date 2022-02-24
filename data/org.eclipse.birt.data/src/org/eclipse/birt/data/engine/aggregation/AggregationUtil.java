/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
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

package org.eclipse.birt.data.engine.aggregation;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;

/**
 * 
 */

public class AggregationUtil {

	private AggregationUtil() {
	}

	/**
	 * to check whether the specified <code>aggrFunc</code> requires data field
	 * parameters.
	 * 
	 * @param aggrFunc
	 * @return
	 */
	public static boolean needDataField(IAggrFunction aggrFunc) {
		if (aggrFunc == null)
			return false;
		IParameterDefn[] params = aggrFunc.getParameterDefn();
		for (int i = 0; i < params.length; i++) {
			if (params[i].isDataField())
				return true;
		}
		return false;
	}

}
