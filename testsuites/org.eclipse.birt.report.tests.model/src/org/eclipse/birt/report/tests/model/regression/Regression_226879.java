/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.InputStream;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.css.StyleSheetException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Regression description:
 * </p>
 * [regression]Fail to setCurrentView to chart in table onPrepare method.[1302]
 * </p>
 * Test description:
 * <p>
 * </p>
 */
public class Regression_226879 extends BaseTestCase {
	private final static String CSS = "regression_226879.css";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + CSS);

		SessionHandle sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = sessionHandle.createDesign();
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws Exception
	 * 
	 */

	public void test_regression_225252() throws Exception {
		CssStyleSheetHandle cssStyleSheetHandle = loadStyleSheet(CSS);
		assertEquals(1, cssStyleSheetHandle.getUnsupportedStyles().size());
		assertEquals("p:first-line", cssStyleSheetHandle.getUnsupportedStyles().get(0).toString());
	}

	private CssStyleSheetHandle loadStyleSheet(String fileName) throws StyleSheetException {
		fileName = INPUT_FOLDER + "/" + fileName;
		InputStream is = Regression_226879.class.getResourceAsStream(fileName);
		return designHandle.openCssStyleSheet(is);
	}
}
