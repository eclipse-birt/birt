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
 * Unit test for <code>ImageItemExecutor</code>.
 *
 */
public class ImageItemExecutorTest extends ReportItemExecutorTestAbs {

	public void testImageExcute() throws Exception {
		compare("image.xml", "image.txt");
	}
}
