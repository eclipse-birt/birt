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

package org.eclipse.birt.report.engine.executor;

/**
 *
 * table item executor test
 *
 */
public class TableItemExecutorTest extends ReportItemExecutorTestAbs {

	/**
	 * test single table
	 *
	 * @throws Exception
	 */
	public void testExcuteTable1() throws Exception {
		compare("table1.xml", "table1.txt");
	}

	public void testEmptyTable() throws Exception {
		compare("empty_table.xml", "empty_table.txt");
	}
}
