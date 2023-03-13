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
 * test ListItemExecutor
 *
 *
 */
public class ListItemExecutorTest extends ReportItemExecutorTestAbs {

	/**
	 * test single table
	 *
	 * @throws Exception
	 */
	public void testExcuteList1() throws Exception {
		compare("List1.xml", "List1.txt");
	}
}
