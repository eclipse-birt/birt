package org.eclipse.birt.report.tests.model.api;

import java.util.ArrayList;

import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for ExternalCssStyleSheet.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 *
 * <tr>
 * <td>{@link #testImportExternalCssStyleSheet()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testImportExternalCssStyleSheetWithFile()}</td>
 * </tr>
 * </table>
 *
 */
public class ExternalCssStyleSheet3Test extends BaseTestCase {

	private String fileName = "ExternalCssStyleSheet3Test.css";

	public ExternalCssStyleSheet3Test(String name) {
		super(name);
	}

	public static Test suite() {

		return new TestSuite(ExternalCssStyleSheet3Test.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyInputToFile(INPUT_FOLDER + "/" + fileName);
		super.createBlankDesign();

	}

	/**
	 * Test Import CSS style
	 *
	 * @throws Exception
	 */
	public void testImportExternalCssStyleSheet() throws Exception {

		// open a external style sheet with relative filename
		designHandle.setBase(PLUGIN_PATH);

		CssStyleSheetHandle stylesheet = loadStyleSheet(getTempFolder() + "/" + INPUT_FOLDER + "/" + fileName);
		assertNotNull(stylesheet);
		SharedStyleHandle style1 = stylesheet.findStyle("STYLE1");
		SharedStyleHandle style2 = stylesheet.findStyle("styl2");
		SharedStyleHandle style3 = stylesheet.findStyle("style3");
		assertNotNull(style1);
		assertNull(style2);
		assertNotNull(style3);
		ArrayList styleList = new ArrayList();
		styleList.add(0, style1);
		styleList.add(1, style3);

		assertEquals(0, designHandle.getStyles().getCount());
		// import a external style sheet into a report design
		designHandle.importCssStyles(stylesheet, styleList);
		// two styles must be copied: style1 and style3
		assertEquals(2, designHandle.getStyles().getCount());
		assertEquals("STYLE1", designHandle.getStyles().get(0).getName());
		assertEquals("style3", designHandle.getStyles().get(1).getName());
	}

	/**
	 * Test import css style from invalid file
	 *
	 * @throws Exception
	 */
	public void testImportExternalCssStyleSheetWithFile() throws Exception {

		// open a no-existing external style
		try {
			// CssStyleSheetHandle stylesheet3 =
			// loadStyleSheet(fileName+"NoCssStyleSheet.xml");
			CssStyleSheetHandle stylesheet3 = loadStyleSheet(fileName);
			fail();
		} catch (Exception e) {
			assertNotNull(e);
		}

	}

	private CssStyleSheetHandle loadStyleSheet(String fileName) throws Exception {
		// fileName = INPUT_FOLDER + "/" + fileName;
		return designHandle.openCssStyleSheet(fileName);
	}

}
