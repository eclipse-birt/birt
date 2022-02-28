
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
package org.eclipse.birt.data.engine.cache;

/**
 *
 */

public class Constants {
	public static int LIST_BUFFER_SIZE = 4000;
	public static int MAX_LIST_BUFFER_SIZE = 6000;
	public static int FACT_TABLE_BUFFER_SIZE = 40000;
	public static int MAX_DIMENSION_LENGTH = 1000000;

	public static final int FACT_TABLE_BLOCK_SIZE = 2048;
	public static final int MAX_FACT_TABLE_BLOCK_SIZE = 8192;

	private static boolean aggressiveMemoryUsage = false;

	public static boolean isAggressiveMemoryUsage() {
		return aggressiveMemoryUsage;
	}

	public static void setAggressiveMemoryUsage() {
		LIST_BUFFER_SIZE = 2000000;
		MAX_LIST_BUFFER_SIZE = 2000000;
		FACT_TABLE_BUFFER_SIZE = 2000000;
		aggressiveMemoryUsage = true;
	}

	public static void setNormalMemoryUsage() {
		LIST_BUFFER_SIZE = 4000;
		MAX_LIST_BUFFER_SIZE = 6000;
		FACT_TABLE_BUFFER_SIZE = 40000;
		aggressiveMemoryUsage = false;
	}

	public static void setConservativeMemoryUsage() {
		LIST_BUFFER_SIZE = 4000;
		MAX_LIST_BUFFER_SIZE = 6000;
		FACT_TABLE_BUFFER_SIZE = 40000;
		aggressiveMemoryUsage = false;
	}
}
