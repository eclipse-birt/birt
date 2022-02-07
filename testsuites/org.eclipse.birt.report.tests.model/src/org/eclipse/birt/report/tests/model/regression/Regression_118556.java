/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * Description: Select "always overwrite existing elements when export report
 * design to a library", error message of "duplicate element" still pops up.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>In a report design, select Window>Preference>BIRT>Library.
 * <li>Select "always overwrite existing elements when export report design to a
 * library".
 * <li>Export the same element to a library file twice.
 * <li>Hint message still pops up.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Export the same element to a library file twice(overwrite mode), ensure that
 * no exception will occur.
 * </p>
 */
public class Regression_118556 extends BaseTestCase {
	private String OUTPUT = "regression_118556_lib.out";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

	}

	protected void tearDown() throws Exception {
		removeResource();
	}

	/**
	 * @throws SemanticException
	 * @throws IOException
	 * @throws DesignFileException
	 * 
	 */

	public void test_regression_118556() throws SemanticException, DesignFileException, IOException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);
		ReportDesignHandle designHandle = session.createDesign();

		ElementFactory factory = designHandle.getElementFactory();
		LabelHandle label = factory.newLabel("newLabel"); //$NON-NLS-1$
		label.setText("Sample Label"); //$NON-NLS-1$

		designHandle.getBody().add(label);

		// String lib = this.getTempFolder( ) + "/" + OUTPUT_FOLDER
		// + "/" + "regression_118556_lib.out"; //$NON-NLS-1$

		String lib = this.genOutputFile(OUTPUT);
		designHandle.saveAs(lib);

		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("newLabel"); //$NON-NLS-1$

		ElementExportUtil.exportElement(labelHandle, OUTPUT, true);

		// the second time

		try {
			ElementExportUtil.exportElement(labelHandle, OUTPUT, true);
		} catch (Exception e) {
			fail();
		}
	}
}
