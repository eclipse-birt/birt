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

package org.eclipse.birt.report.engine.internal.document.v2;

import org.eclipse.birt.report.engine.EngineCase;

public class ReportContentTest extends EngineCase {

	final static String REPORT_DOCUMENT_NAME = ".internal.test.rptdocument";

	@Override
	public void setUp() {
		removeFile(REPORT_DOCUMENT_NAME);
	}

	@Override
	public void tearDown() {
		removeFile(REPORT_DOCUMENT_NAME);
	}

	public void testReportContentStream() throws Exception {
		doWrite();
		doRead();
	}

	protected void doWrite() throws Exception {
	}

	protected void doRead() throws Exception {
	}
}
