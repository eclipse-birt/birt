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

package org.eclipse.birt.report.tests.model.api;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * 
 * Tests cases for ReportDesignHandle.
 * <p>
 * 
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testReportDesignOtherMethods()}</td>
 * <td>Tests to get element and design handle.</td>
 * <td>Returns the design and design handle correctly.</td>
 * </tr>
 * <tr>
 * <td>{@link #testFindCssHandle()}</td>
 * <td>Tests to get translations.</td>
 * <td>Information of translations matches with the input design file.</td>
 * </tr>
 * 
 * </table>
 * 
 * 
 */
public class ReportDesignHandleTest extends BaseTestCase {

	// protected static final String pluginpath =
	// System.getProperty("eclipse.home")+"/plugins/"+ PLUGIN_NAME +"/bin/";
	/**
	 * @param name
	 */
	public ReportDesignHandleTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {
		return new TestSuite(ReportDesignHandleTest.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + "ReportDesignHandleTest.xml");
		copyInputToFile(INPUT_FOLDER + "/" + "ReportDesignHandleTest_css.xml");
		copyInputToFile(INPUT_FOLDER + "/" + "ReportDesignHandleTest_css1.css");

		openDesign("ReportDesignHandleTest.xml"); //$NON-NLS-1$
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * Tests cases for methods on ReportDesignHandle.
	 * 
	 */

	public void testReportDesignOtherMethods() {
		assertFalse(designHandle.isEnableACL());
		try {
			designHandle.setEnableACL(true);
		} catch (SemanticException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(designHandle.isEnableACL());
	}

	/**
	 * Test findIncludedCssStyleSheetHandleByFileName() Test
	 * findCssStyleSheetHandleByFileName()
	 * 
	 * @throws DesignFileException
	 */
	public void testFindCssHandle() throws DesignFileException {
		openDesign("ReportDesignHandleTest_css.xml");

		IncludedCssStyleSheetHandle includeCssHandle = designHandle
				.findIncludedCssStyleSheetHandleByFileName("ReportDesignHandleTest_css.css");
		assertNotNull(includeCssHandle);
		assertEquals("ReportDesignHandleTest_css.css", includeCssHandle.getFileName());

		CssStyleSheetHandle cssHandle = designHandle
				.findCssStyleSheetHandleByFileName("ReportDesignHandleTest_css.css");
		assertNull(cssHandle);

		cssHandle = designHandle.findCssStyleSheetHandleByFileName("ReportDesignHandleTest_css1.css");
		assertNotNull(cssHandle);
		assertEquals("ReportDesignHandleTest_css1.css", cssHandle.getFileName());

		includeCssHandle = designHandle.findIncludedCssStyleSheetHandleByFileName("test2.css");
		assertNull(includeCssHandle);

		cssHandle = designHandle.findCssStyleSheetHandleByFileName("test2.css");
		assertNull(cssHandle);
	}
}
