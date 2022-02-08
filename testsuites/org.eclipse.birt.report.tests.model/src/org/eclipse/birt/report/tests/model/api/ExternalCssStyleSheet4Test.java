/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.model.api;

import java.io.InputStream;
import java.util.ArrayList;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SessionHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * TestCases for ExternalCssStyleSheet.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * 
 * <tr>
 * <td>{@link #testImportDuplicatedStyles()}</td>
 * </tr>
 * 
 * </table>
 * 
 */
public class ExternalCssStyleSheet4Test extends BaseTestCase {

	// private String fileName = null;
	private String fileName = "ExternalCssStyleSheetTest4.css";
	private String designFileName = "ExternalCssStyleSheetTest4.xml";

	protected ReportDesignHandle designHandle = null;

	public ExternalCssStyleSheet4Test(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {

		return new TestSuite(ExternalCssStyleSheet4Test.class);
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		copyInputToFile(INPUT_FOLDER + "/" + designFileName);
		SessionHandle session = DesignEngine.newSession(ULocale.ENGLISH);
		designHandle = session.openDesign(getTempFolder() + "/" + INPUT_FOLDER + "/" + designFileName);
	}

	/**
	 * Test import duplicate css styles
	 * 
	 * @throws Exception
	 */
	public void testImportDuplicatedStyles() throws Exception {

		// open a external style sheet with inputstream

		CssStyleSheetHandle stylesheet = loadStyleSheet(fileName);
		assertNotNull(stylesheet);
		SharedStyleHandle style1 = stylesheet.findStyle("STYLE1");
		SharedStyleHandle style2 = stylesheet.findStyle("STYLE2");
		assertNotNull(style1);
		assertNotNull(style2);
		ArrayList styleList = new ArrayList();
		styleList.add(0, style1);
		styleList.add(1, style2);
		assertEquals(2, styleList.size());

		// import a external style sheet with same name as the existing style in
		// report
		designHandle.importCssStyles(stylesheet, styleList);
		assertEquals(3, designHandle.getStyles().getCount());

		assertEquals("style1", designHandle.getStyles().get(0).getName());
		assertEquals("STYLE11", designHandle.getStyles().get(1).getName());
		assertEquals("STYLE2", designHandle.getStyles().get(2).getName());

		// apply styles to report element
		TableHandle table = (TableHandle) designHandle.findElement("MyTable");
		LabelHandle label = (LabelHandle) designHandle.findElement("MyLabel");
		SharedStyleHandle style_a = (SharedStyleHandle) designHandle.getStyles().get(0);
		SharedStyleHandle style_b = (SharedStyleHandle) designHandle.getStyles().get(1);
		table.setStyle(style_a);
		label.setStyle(style_b);
		assertEquals("2em", label.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals("1em", label.getStringProperty(IStyleModel.MARGIN_LEFT_PROP));
		assertEquals("2em", table.getStringProperty(IStyleModel.FONT_SIZE_PROP));
		assertEquals("italic", table.getStringProperty(IStyleModel.FONT_STYLE_PROP));
	}

	private CssStyleSheetHandle loadStyleSheet(String fileName) throws Exception {
		fileName = INPUT_FOLDER + "/" + fileName;
		InputStream is = ExternalCssStyleSheet4Test.class.getResourceAsStream(fileName);
		return designHandle.openCssStyleSheet(is);
	}
}
