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

package org.eclipse.birt.data.aggregation.impl;

import org.eclipse.birt.core.data.DataType;

/**
 * 
 */

public class SupportedDataTypes {

	/**
	 * support any data type.
	 */
	public static final int[] ANY = new int[] { DataType.ANY_TYPE };

	/**
	 * support both integer and double data types.
	 */
	public static final int[] CALCULATABLE = new int[] { DataType.BOOLEAN_TYPE, DataType.INTEGER_TYPE,
			DataType.DOUBLE_TYPE, DataType.DECIMAL_TYPE, DataType.STRING_TYPE, DataType.DATE_TYPE,
			DataType.SQL_DATE_TYPE, DataType.SQL_TIME_TYPE };

}
