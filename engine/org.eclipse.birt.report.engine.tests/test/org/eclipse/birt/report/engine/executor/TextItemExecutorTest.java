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
 * Test case for TextItemExecutor
 *
 */
public class TextItemExecutorTest extends ReportItemExecutorTestAbs {

	public void testText() throws Exception {
		compare("text_test.xml", "text_test.txt");
	}
}
