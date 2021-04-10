/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.File;
import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignConfig;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * The chart name in the exported library is confusing, for an example,
 * "extended item "
 * </p>
 * Test description:
 * <p>
 * Export the design file which contain a chart to a library, the chart name in
 * the exported library is the "Chart"
 * </p>
 */

public class Regression_122357 extends BaseTestCase {

	private static String INPUT = "Regression_122357.xml"; //$NON-NLS-1$
	private static String LIB = "regression_122357_exportlib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyInputToFile(INPUT_FOLDER + "/" + INPUT);
		// copyGoldenToFile ( GOLDEN_FOLDER + "/" + goldenFileName );

	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 * @throws IOException
	 */

	public void test_regression_122357() throws DesignFileException, SemanticException, IOException {
		DesignEngine engine = new DesignEngine(new DesignConfig());
		SessionHandle session = engine.newSessionHandle(ULocale.ENGLISH);

		ReportDesignHandle design = session.openDesign(getTempFolder() + "/" + INPUT_FOLDER + "/" + INPUT);

		String output = this.genOutputFile(LIB);
		File outputFile = new File(output);
		if (outputFile.exists())
			outputFile.delete();

		ElementExportUtil.exportDesign(design, output, true, true);

		// LibraryHandle lib = session.openLibrary( this.genOutputFile(LIB));
		LibraryHandle lib = session.openLibrary(output);
		DesignElementHandle chart = lib.getComponents().get(0);
		assertEquals("NewChart", chart.getName()); //$NON-NLS-1$
		assertEquals("Chart", ((ExtendedItemHandle) chart).getExtensionName()); //$NON-NLS-1$
	}
}
