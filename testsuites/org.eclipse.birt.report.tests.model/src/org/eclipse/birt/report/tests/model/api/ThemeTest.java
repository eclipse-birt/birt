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

import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.util.ElementExportUtil;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.tests.model.BaseTestCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * TestCases for Library Theme.
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 *
 * <tr>
 * <td>{@link #testDefineThemes()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testUsingTheme()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testThemeSearchAlgorithm()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testLibraryUseTheme()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testExportStyleToDefaultTheme()}</td>
 * </tr>
 *
 * <tr>
 * <td>{@link #testCopyPasteTheme()}</td>
 * </tr>
 * </table>
 *
 */

public class ThemeTest extends BaseTestCase {

	private ElementFactory factory = null;
	private ThemeHandle theme = null;
	private ThemeHandle theme1 = null;
	private SharedStyleHandle style1 = null;
	private SharedStyleHandle style2 = null;
	private TableHandle table = null;
	private LabelHandle label = null;
	private String LibA = "LibraryAIncludeTheme.xml";
	private String LibB = "LibraryBIncludeTheme.xml";
	private String LibC = "LibraryCIncludeTheme.xml";
	private String LibC1 = "LibraryCIncludeTheme_1.xml";
	private String LibCInclTheme = "LibraryCIncludeTheme.xml";

	public ThemeTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	public static Test suite() {
		return new TestSuite(ThemeTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		copyInputToFile(INPUT_FOLDER + "/" + LibA);
		copyInputToFile(INPUT_FOLDER + "/" + LibB);
		copyInputToFile(INPUT_FOLDER + "/" + LibC);
		copyInputToFile(INPUT_FOLDER + "/" + LibC1);
		copyInputToFile(INPUT_FOLDER + "/" + "ThemeTest1.xml");
		copyInputToFile(INPUT_FOLDER + "/" + "ThemeTest2.xml");
		copyInputToFile(INPUT_FOLDER + "/" + "ThemeTest2_1.xml");
		copyInputToFile(INPUT_FOLDER + "/" + LibCInclTheme);
		// System.out.println("ThemeTest1.xml");
	}

	@Override
	public void tearDown() {
		removeResource();
		// System.out.println("ThemeTest1.xml");
	}

	/**
	 * Test create theme
	 *
	 * @throws Exception
	 */
	public void testDefineThemes() throws Exception {
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();
		library = (Library) libraryHandle.getModule();
		factory = new ElementFactory(library);

		// define theme without name
		theme = factory.newTheme(null);
		theme1 = factory.newTheme(null);
		assertEquals("NewTheme", theme.getName());
		libraryHandle.getThemes().add(theme);

		// define theme with duplicated names
		libraryHandle.getThemes().add(theme1);
		assertEquals("NewTheme1", theme1.getName());

		// define theme with styles
		style1 = factory.newStyle("Style1");
		style2 = factory.newStyle("Style2");
		theme.getStyles().add(style1);
		theme.getStyles().add(style2);
		assertEquals(2, theme.getStyles().getCount());

	}

	/**
	 * Test use theme
	 *
	 * @throws Exception
	 */
	public void testUsingTheme() throws Exception {
		System.out.println("ThemeTest1.xml");
		openDesign("ThemeTest1.xml");
		// LibA: theme1, theme2
		// LibB: theme1
		designHandle.includeLibrary(LibA, "LibA");
		designHandle.includeLibrary(LibB, "LibB");

		table = (TableHandle) designHandle.findElement("mytable");
		label = (LabelHandle) designHandle.findElement("mylabel");
		assertNotNull(table);
		assertNotNull(label);

		// Specify theme1 for report design
		designHandle.setThemeName("LibB.theme1");
		assertEquals("dashed", table.getProperty(Style.BORDER_LEFT_STYLE_PROP));
		assertEquals(ColorPropertyType.GREEN, label.getProperty(Style.COLOR_PROP));

		// specify theme2 for report design
		designHandle.setThemeName("LibA.theme2");
		assertEquals("10pt", table.getStringProperty(Style.FONT_SIZE_PROP));
		assertEquals("#808080", table.getStringProperty(Style.COLOR_PROP));
		assertEquals("10mm", label.getStringProperty(Style.MARGIN_TOP_PROP));
	}

	/**
	 * Test hierachy in theme styles and report styles
	 *
	 * @throws Exception
	 */
	public void testThemeSearchAlgorithm() throws Exception {
		// report design has four styles:
		// custom styles: mytable, mylabel
		// selector styles: table, label
		openDesign("ThemeTest2.xml");
		table = (TableHandle) designHandle.findElement("mytable");
		label = (LabelHandle) designHandle.findElement("mylabel");
		SharedStyleHandle RCS1 = designHandle.findStyle("mytable");
		SharedStyleHandle RCS2 = designHandle.findStyle("mylabel");
		SharedStyleHandle RSS1 = designHandle.findStyle("table");
		SharedStyleHandle RSS2 = designHandle.findStyle("label");
		assertNotNull(table);
		assertNotNull(label);
		assertNotNull(RCS1);
		assertNotNull(RCS2);
		assertNotNull(RSS1);
		assertNotNull(RSS2);

		// LibC has a theme with four styles:
		// custom styles: mytable, mylabel
		// selector styles: table, label
		openLibrary(LibCInclTheme, true);
		libraryHandle.saveAs(getTempFolder() + "/" + INPUT_FOLDER + "/" + LibC1);
		StyleHandle LCS1 = libraryHandle.findTheme("theme1").findStyle("mytable");
		StyleHandle LCS2 = libraryHandle.findTheme("theme1").findStyle("mylabel");
		StyleHandle LSS1 = libraryHandle.findTheme("theme1").findStyle("table");
		StyleHandle LSS2 = libraryHandle.findTheme("theme1").findStyle("label");
		assertNotNull(LCS1);
		assertNotNull(LCS2);
		assertNotNull(LSS1);
		assertNotNull(LSS2);

		designHandle.includeLibrary(LibC1, "LibC");

		// Specify theme1 for report design
		designHandle.setThemeName("LibC.theme1");
		table.setStyleName("mytable");
		label.setStyleName("mylabel");
		assertEquals("large", table.getStringProperty(Style.FONT_SIZE_PROP));
		assertEquals("#0000FF", label.getStringProperty(Style.BACKGROUND_COLOR_PROP));

		// drop custom styles in report design
		designHandle.getStyles().drop(RCS1);
		designHandle.getStyles().drop(RCS2);
		assertEquals("10pt", table.getStringProperty(Style.FONT_SIZE_PROP));
		assertEquals("red", label.getStringProperty(Style.BACKGROUND_COLOR_PROP));

		// designHandle.saveAs(this.getFullQualifiedClassName()+ "/"+
		// INPUT_FOLDER + "/" + "ThemeTest2_1.xml");

		// drop custom styles in library
		libraryHandle.findTheme("theme1").getStyles().drop(LCS1);
		libraryHandle.findTheme("theme1").getStyles().drop(LCS2);
		libraryHandle.save();
		openDesign("ThemeTest2_1.xml");
		assertEquals("100%", designHandle.findElement("mytable").getStringProperty(Style.FONT_SIZE_PROP));
		assertEquals("aqua", designHandle.findElement("mylabel").getStringProperty(Style.BACKGROUND_COLOR_PROP));

		// drop selector styles in report design
		// designHandle.findStyle("table").drop();
		// designHandle.findStyle("label").drop();
		// assertEquals("small",designHandle.findElement("mytable").getStringProperty(Style.FONT_SIZE_PROP));
		// assertEquals("#808080",designHandle.findElement("mylabel").getStringProperty(Style.BACKGROUND_COLOR_PROP));
		// designHandle.save();

		// drop selector styles in library
//		openLibrary( "LibraryCIncludeTheme.xml" );
		assertNotNull(libraryHandle.findTheme("theme1").findStyle("table"));
		libraryHandle.findTheme("theme1").findStyle("label").drop();
		libraryHandle.save();
		openDesign("ThemeTest2_1.xml");
		assertEquals("100%", designHandle.findElement("mytable").getStringProperty(Style.FONT_SIZE_PROP));
		assertNotNull(designHandle.findElement("mylabel").getStringProperty(Style.BACKGROUND_COLOR_PROP));

		// drop theme in library
		openLibrary("LibraryCIncludeTheme_1.xml");
		libraryHandle.findTheme("theme1").drop();
		assertNull(libraryHandle.findTheme("LibC1.theme1"));

	}

	/**
	 * Test use theme in library
	 *
	 * @throws Exception
	 */
	public void testLibraryUseTheme() throws Exception {

		openLibrary(LibA, true);
		library = (Library) libraryHandle.getModule();
		factory = new ElementFactory(library);
		table = factory.newTableItem("mytable");
		label = factory.newLabel("mylabel");
		libraryHandle.getComponents().add(table);
		libraryHandle.getComponents().add(label);
		assertNotNull(libraryHandle.findElement("mytable"));
		assertNotNull(libraryHandle.findElement("mylabel"));

		libraryHandle.setThemeName("theme1");
		assertEquals("\"Arial\"", libraryHandle.findElement("mytable").getStringProperty(Style.FONT_FAMILY_PROP));
		assertEquals("#FF0000", libraryHandle.findElement("mytable").getStringProperty(Style.COLOR_PROP));

		libraryHandle.setThemeName("theme2");
		assertEquals("10pt", libraryHandle.findElement("mytable").getStringProperty(Style.FONT_SIZE_PROP));
		assertEquals("#808080", libraryHandle.findElement("mytable").getStringProperty(Style.COLOR_PROP));
		assertEquals("10mm", libraryHandle.findElement("mylabel").getStringProperty(Style.MARGIN_TOP_PROP));
	}

	/**
	 * Test export style to theme
	 *
	 * @throws Exception
	 */
	public void testExportStyleToDefaultTheme() throws Exception {
		sessionHandle = DesignEngine.newSession(ULocale.ENGLISH);
		libraryHandle = sessionHandle.createLibrary();

		openDesign("ThemeTest2.xml");
		SharedStyleHandle RCS1 = designHandle.findStyle("mytable");
		SharedStyleHandle RCS2 = designHandle.findStyle("mylabel");
		SharedStyleHandle RSS1 = designHandle.findStyle("table");
		SharedStyleHandle RSS2 = designHandle.findStyle("label");
		assertNotNull(RCS1);
		assertNotNull(RCS2);
		assertNotNull(RSS1);
		assertNotNull(RSS2);

		ElementExportUtil.exportElement(RCS1, libraryHandle, false);
		ElementExportUtil.exportElement(RCS2, libraryHandle, false);
		ElementExportUtil.exportElement(RSS1, libraryHandle, false);
		ElementExportUtil.exportElement(RSS2, libraryHandle, false);

		assertEquals("mytable", libraryHandle.getTheme().getStyles().get(0).getName());
		assertEquals("mylabel", libraryHandle.getTheme().getStyles().get(1).getName());
		assertEquals("table", libraryHandle.getTheme().getStyles().get(2).getName());
		assertEquals("label", libraryHandle.getTheme().getStyles().get(3).getName());
	}

	/**
	 * Test copy/paste theme
	 *
	 * @throws Exception
	 */
	public void testCopyPasteTheme() throws Exception {
		openLibrary(LibC, true);
		ThemeHandle defaulttheme = libraryHandle.findTheme("defaultTheme");
		ThemeHandle theme1 = libraryHandle.findTheme("theme1");
		assertNotNull(defaulttheme);
		assertNotNull(theme1);
		ThemeHandle defaultcopy = (ThemeHandle) defaulttheme.copy().getHandle(libraryHandle.getModule());
		ThemeHandle defaultcopy1 = (ThemeHandle) defaulttheme.copy().getHandle(libraryHandle.getModule());
		ThemeHandle theme1copy = (ThemeHandle) theme1.copy().getHandle(libraryHandle.getModule());
		ThemeHandle theme1copy1 = (ThemeHandle) theme1.copy().getHandle(libraryHandle.getModule());

		try {
			libraryHandle.getThemes().add(defaultcopy);
			fail();
		} catch (NameException e) {
			assertNotNull(e);
		}

		defaultcopy.setName("defaultTheme1");
		theme1copy.setName("theme11");

		libraryHandle.getThemes().add(defaultcopy);
		libraryHandle.getThemes().add(theme1copy);
		assertEquals("defaultTheme1", libraryHandle.getThemes().get(2).getName());
		assertEquals("theme11", libraryHandle.getThemes().get(3).getName());

		// paste themes to another library
		openLibrary(LibB);
		defaultcopy1.setName("defaultTheme1");
		theme1copy1.setName("theme11");
		libraryHandle.getThemes().add(defaultcopy1);
		libraryHandle.getThemes().add(theme1copy1);
		assertEquals("defaultTheme1", libraryHandle.getThemes().get(2).getName());
		assertEquals("theme11", libraryHandle.getThemes().get(3).getName());

	}

}
