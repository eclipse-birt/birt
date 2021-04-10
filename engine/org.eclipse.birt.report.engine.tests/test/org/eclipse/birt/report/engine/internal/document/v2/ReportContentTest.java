/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.internal.document.v2;

import org.eclipse.birt.report.engine.EngineCase;

public class ReportContentTest extends EngineCase {

	final static String REPORT_DOCUMENT_NAME = ".internal.test.rptdocument";

	public void setUp() {
		removeFile(REPORT_DOCUMENT_NAME);
	}

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
