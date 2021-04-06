/*******************************************************************************
 * Copyright (c) 2004, 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
