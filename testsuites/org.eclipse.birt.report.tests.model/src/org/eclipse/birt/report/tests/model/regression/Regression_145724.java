/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b> Change version after auto convert
 * <p>
 * By the request of 136536, user get warning message to confirm the auto
 * convert. If user select yes, the file convert to newest format, but the
 * version is still old. If user open this file again, he'll get the warning
 * message even the file converted.
 * <p>
 * <b>Test description:</b>
 * <p>
 * Open an old design report thant need convert, open/save it and check that the
 * version is updated.
 * <p>
 */
public class Regression_145724 extends BaseTestCase {

	private final static String REPORT = "test_version.rptdesign"; //$NON-NLS-1$

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 */

	@Override
	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(REPORT, REPORT);
	}

	@Override
	public void tearDown() {
		removeResource();
	}

	public void test_regression_145724() throws DesignFileException, IOException {
		openDesign(REPORT);

		saveAs("test_version.out"); //$NON-NLS-1$

		BufferedReader br = new BufferedReader(new FileReader(this.genOutputFile("test_version.out"))); //$NON-NLS-1$
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("<report")) { //$NON-NLS-1$
				break;
			}
		}

		int i = line.indexOf("version=\""); //$NON-NLS-1$
		int start = line.indexOf('"', i);
		int end = line.indexOf('"', start + 1);

		String version = line.substring(start + 1, end);
		assertEquals(DesignSchemaConstants.REPORT_VERSION, version);
	}
}
