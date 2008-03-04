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

public class SupportedDataTypes
{

	/**
	 * support any data type.
	 */
	public static final int[] ANY = new int[]{
		DataType.ANY_TYPE
	};

	/**
	 * support both integer and double data types.
	 */
	public static final int[] INTEGER_DOUBLE = new int[]{
			DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE
	};

	/**
	 * support integer, double and date types.
	 */
	public static final int[] INTEGER_DOUBLE_DATE = new int[]{
			DataType.INTEGER_TYPE, DataType.DOUBLE_TYPE, DataType.DATE_TYPE
	};
}
