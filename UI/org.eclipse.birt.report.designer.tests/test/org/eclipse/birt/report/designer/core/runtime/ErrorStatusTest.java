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

package org.eclipse.birt.report.designer.core.runtime;

import junit.framework.TestCase;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.core.runtime.IStatus;

/**
 * 
 */

public class ErrorStatusTest extends TestCase {

	private ErrorStatus status;

	private final static String REASON = "TestReason"; //$NON-NLS-1$
	private final static Exception EXCEPTION = new Exception(REASON);

	public void setUp() {
		status = new ErrorStatus(ReportPlugin.REPORT_UI, 1001, REASON, EXCEPTION);
	}

	public void testErrorStatus() {
		assertEquals(REASON, status.getMessage());
		assertEquals(1001, status.getErrorCode());
		assertEquals(EXCEPTION, status.getException());
		assertEquals(ReportPlugin.REPORT_UI, status.getPlugin());
	}

	public void testAddStatus() {
		status.addStatus(REASON + IStatus.INFO, IStatus.INFO);
		assertEquals(REASON + IStatus.INFO, status.getChildren()[0].getMessage());
		assertEquals(IStatus.INFO, status.getSeverity());
		status.addStatus(REASON + IStatus.WARNING, IStatus.WARNING);
		assertEquals(REASON + IStatus.WARNING, status.getChildren()[1].getMessage());
		assertEquals(IStatus.WARNING, status.getSeverity());
		status.addStatus(REASON + IStatus.ERROR, IStatus.ERROR);
		assertEquals(REASON + IStatus.ERROR, status.getChildren()[2].getMessage());
		assertEquals(IStatus.ERROR, status.getSeverity());
	}

	public void testAddWarning() {
		status.addWarning(REASON);
		assertEquals(REASON, status.getChildren()[0].getMessage());
		assertEquals(IStatus.WARNING, status.getSeverity());
	}

	public void testAddError() {
		status.addError(REASON);
		assertEquals(REASON, status.getChildren()[0].getMessage());
		assertEquals(IStatus.ERROR, status.getSeverity());
	}

	public void testAddInformation() {
		status.addInformation(REASON);
		assertEquals(REASON, status.getChildren()[0].getMessage());
		assertEquals(IStatus.INFO, status.getSeverity());
	}

	public void testAddChildren() {
		status.addWarning(REASON);
		assertEquals(1, status.getChildren().length);
		assertEquals(IStatus.WARNING, status.getSeverity());
		status.addError(REASON);
		assertEquals(2, status.getChildren().length);
		assertEquals(IStatus.ERROR, status.getSeverity());
		status.addInformation(REASON);
		assertEquals(3, status.getChildren().length);
		assertEquals(IStatus.ERROR, status.getSeverity());
	}

}
