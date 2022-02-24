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

package org.eclipse.birt.report.model.command;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.command.CustomMsgException;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * Tests the error messages defined by ContentException.
 */

public class CustomMsgExceptionTest extends BaseTestCase {

	/**
	 * Tests the error message.
	 * 
	 * @throws Exception
	 */

	public void testErrorMessages() throws Exception {

		SessionHandle session = new DesignEngine(new DesignConfig()).newSessionHandle(TEST_LOCALE);
		ReportDesign report = session.createDesign().getDesign();
		os = new ByteArrayOutputStream();

		CustomMsgException error = new CustomMsgException(report,
				CustomMsgException.DESIGN_EXCEPTION_RESOURCE_KEY_REQUIRED);
		print(error);

		error = new CustomMsgException(report, "ResourceKey.ReportDesign.Title", "en", //$NON-NLS-1$ //$NON-NLS-2$
				CustomMsgException.DESIGN_EXCEPTION_DUPLICATE_LOCALE);
		print(error);

		error = new CustomMsgException(report, null, "abc", //$NON-NLS-1$
				CustomMsgException.DESIGN_EXCEPTION_INVALID_LOCALE);
		print(error);

		error = new CustomMsgException(report, "ResourceKey.ReportDesign.Title", "en", //$NON-NLS-1$ //$NON-NLS-2$
				CustomMsgException.DESIGN_EXCEPTION_TRANSLATION_NOT_FOUND);
		print(error);

		os.close();

		assertTrue(compareFile("CustomMsgExceptionError.golden.txt")); //$NON-NLS-1$

	}

	private void print(CustomMsgException error) {
		String code = error.getErrorCode();
		try {
			os.write(code.getBytes());
			for (int i = code.length(); i < 60; i++)
				os.write(' ');
			os.write(error.getMessage().getBytes());
			os.write('\n');
		} catch (IOException e) {
			assert false;
		}
	}

}
