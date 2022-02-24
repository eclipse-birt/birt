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
 * test grid item executor test for single record and empty resultset
 * 
 */
public class GridItemExecutorTest extends ReportItemExecutorTestAbs {
	/**
	 * test single table
	 * 
	 * @throws Exception
	 */
	public void testExcutegrid1() throws Exception {
		compare("grid.xml", "grid.txt");
	}

}
