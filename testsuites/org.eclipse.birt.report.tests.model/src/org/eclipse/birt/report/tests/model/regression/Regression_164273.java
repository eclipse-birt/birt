/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * Exception is thrown out when export a report to library It doesn't support
 * java-based event handler, either.
 * <p>
 * <b>Test Description:</b>
 * <ol>
 * <li>New a library with a data source, a data set, a dynamic report parameter,
 * a data binding to the data set.
 * <li>New a report use the library, and drag the data source, data set, report
 * parameter and the data from Library Explorer to Outline.
 * <li>Drag the parameter from Outline to Layout, save.
 * <li>Export the report to library.
 * </ol>
 */
public class Regression_164273 extends BaseTestCase {

	private final static String REPORT = "regression_164273.xml";

	public void test_regression_164273() throws Exception {
		openDesign(REPORT);
		openLibrary("Library_1.xml");

		try {
			// export with duplicate names.
			ElementExportUtil.exportDesign(designHandle, libraryHandle, true, true);
		} catch (NameException e) {
		}
	}
}
