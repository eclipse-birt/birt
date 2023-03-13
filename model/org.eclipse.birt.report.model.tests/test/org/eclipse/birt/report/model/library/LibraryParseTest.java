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

package org.eclipse.birt.report.model.library;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.eclipse.birt.report.model.api.IncludedCssStyleSheetHandle;
import org.eclipse.birt.report.model.api.LabelHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.command.CssException;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.api.elements.structures.ConfigVariable;
import org.eclipse.birt.report.model.api.elements.structures.CustomColor;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.model.css.CssStyle;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.Theme;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the properties of library in reading and writing. This test case also
 * includes testing semantic check.
 */

public class LibraryParseTest extends BaseTestCase {

	private String fileName = "LibraryParseTest.xml"; //$NON-NLS-1$
	private String goldenFileName = "LibraryParseTest_golden.xml"; //$NON-NLS-1$
	private String semanticCheckFileName = "LibraryParseTest_1.xml"; //$NON-NLS-1$

	/**
	 * Tests add css style sheet
	 *
	 * @throws Exception
	 */

	public void testAddCssStyleSheetOperation() throws Exception {
		openDesign("LibraryParserWithCss_Add.xml"); //$NON-NLS-1$

		// 'captionfigcolumn'
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		// 'note'
		LabelHandle labelHandle2 = (LabelHandle) designHandle.findElement("label2");//$NON-NLS-1$

		// 'captionfigcolumn' from report design new.css file
		assertEquals("new.css", ((CssStyle) labelHandle.getStyle().getElement())//$NON-NLS-1$
				.getCssStyleSheet().getFileName());
		// 'node' from report design new.css file
		assertEquals("new.css", ((CssStyle) labelHandle2.getStyle().getElement())//$NON-NLS-1$
				.getCssStyleSheet().getFileName());

		// add css file
		CssStyleSheetHandle sheetHandle = designHandle.openCssStyleSheet("reslove.css");//$NON-NLS-1$
		designHandle.addCss(sheetHandle);

		// 'captionfigcolumn' from report design resolve.css file
		assertEquals("reslove.css", ((CssStyle) labelHandle.getStyle().getElement())//$NON-NLS-1$
				.getCssStyleSheet().getFileName());
		// 'note' from report design resolve.css file
		assertEquals("reslove.css", ((CssStyle) labelHandle2.getStyle().getElement())//$NON-NLS-1$
				.getCssStyleSheet().getFileName());
	}

	/**
	 * Tests drop css style sheet
	 *
	 * @throws Exception
	 */

	public void testDropCssStyleSheetOperation() throws Exception {
		openDesign("LibraryParserWithCss_Drop.xml"); //$NON-NLS-1$

		// 'code'
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		// 'uilabel'
		LabelHandle labelHandle2 = (LabelHandle) designHandle.findElement("label2");//$NON-NLS-1$

		// get 'code' from theme css style
		assertTrue(((CssStyle) labelHandle.getStyle().getElement()).getContainer() instanceof Theme);
		// get 'uilabel' from report design css style
		assertTrue(((CssStyle) labelHandle2.getStyle().getElement()).getContainer() instanceof ReportDesign);

		// drop report design css.
		CssStyleSheetHandle sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);
		designHandle.dropCss(sheetHandle);

		// get 'code' from theme css style
		assertTrue(((CssStyle) labelHandle.getStyle().getElement()).getContainer() instanceof Theme);

		// get 'captionfigcolumn' from theme style
		assertTrue(((CssStyle) labelHandle2.getStyle().getElement()).getContainer() instanceof Theme);

	}

	/**
	 * Test Semantic error when reload css style sheet. See bugzilla #201991.There
	 * is a label named 'label3' first can't find style, so size of semantic error
	 * is one. Then reload the new css file which contains such style , then this
	 * label can find style, so size of semantic error is zero.
	 *
	 * @throws Exception
	 */

	public void testReloadAndCheckSemanticError() throws Exception {
		// copy file

		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "LibraryParserWithCss_Reload.xml");//$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "base.css"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "new2.css"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		String baseFilePath = (String) filePaths.get(1);
		String newFilePath = (String) filePaths.get(2);

		openDesign(designFilePath, false);

		// 'captionfigcolumn2'
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label3");//$NON-NLS-1$

		assertNull(labelHandle.getStyle());
		assertEquals(1, labelHandle.getSemanticErrors().size());
		// copy new2.css to base.css
		copyContentToFile(newFilePath, baseFilePath);

		// reload css only exist four kind of styles
		CssStyleSheetHandle sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);

		designHandle.reloadCss(sheetHandle);

		// 'code' style name is the same.
		assertNotNull(labelHandle.getStyle());
		assertEquals("CaptionFigColumn2", labelHandle.getStyle().getName());//$NON-NLS-1$

		assertEquals(0, labelHandle.getSemanticErrors().size());
	}

	/**
	 * Tests reload css style sheet
	 *
	 * @throws Exception
	 */

	public void testReloadCssStyleSheetOperation() throws Exception {
		// copy file

		List fileNames = new ArrayList();
		fileNames.add(INPUT_FOLDER + "LibraryParserWithCss_Reload.xml");//$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "base.css"); //$NON-NLS-1$
		fileNames.add(INPUT_FOLDER + "new.css"); //$NON-NLS-1$

		List filePaths = dumpDesignAndLibrariesToFile(fileNames);
		String designFilePath = (String) filePaths.get(0);
		String baseFilePath = (String) filePaths.get(1);
		String newFilePath = (String) filePaths.get(2);

		openDesign(designFilePath, false);

		// 'code'
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		// 'captionfigcolumn'
		LabelHandle labelHandle2 = (LabelHandle) designHandle.findElement("label2");//$NON-NLS-1$

		assertNotNull(labelHandle.getStyle());
		assertEquals("italic", labelHandle2.getStyle().getFontStyle());//$NON-NLS-1$

		// copy new.css to base.css
		copyContentToFile(newFilePath, baseFilePath);

		// reload css only exist four kind of styles
		CssStyleSheetHandle sheetHandle = (CssStyleSheetHandle) designHandle.getAllCssStyleSheets().get(0);

		designHandle.reloadCss(sheetHandle);

		// reference of 'code' style is null.

		SharedStyleHandle styleHandle = labelHandle.getStyle();
		assertNull(styleHandle);

		// reference of 'CaptionFigColumn' style is changed.

		SharedStyleHandle styleHandle2 = labelHandle2.getStyle();
		assertNotNull(styleHandle2);
		assertEquals("oblique", styleHandle2.getFontStyle());//$NON-NLS-1$

		File file = new File(baseFilePath);
		file.delete();

		try {
			designHandle.reloadCss(sheetHandle);
			fail();
		} catch (CssException e) {
			assertEquals(CssException.DESIGN_EXCEPTION_CSS_NOT_FOUND, e.getErrorCode());
		}
	}

	/**
	 * Tests resolve style
	 *
	 * @throws Exception
	 */

	public void testResolveStyle() throws Exception {
		openDesign("LibraryParserWithCss_Reslove.xml"); //$NON-NLS-1$

		// 'code'
		LabelHandle labelHandle = (LabelHandle) designHandle.findElement("label");//$NON-NLS-1$

		// 'captionfigcolumn'
		LabelHandle labelHandle2 = (LabelHandle) designHandle.findElement("label2");//$NON-NLS-1$

		// 'codename'
		LabelHandle labelHandle3 = (LabelHandle) designHandle.findElement("label3");//$NON-NLS-1$

		// 'uilabel'
		LabelHandle labelHandle4 = (LabelHandle) designHandle.findElement("label4"); //$NON-NLS-1$

		// theme is read-only in library , so can't be drop
		LibraryHandle libHandle = designHandle.getLibrary("LibParserWithCss_Lib");//$NON-NLS-1$
		ThemeHandle themeHandle = libHandle.findTheme("theme1");//$NON-NLS-1$
		List csses = themeHandle.getAllCssStyleSheets();
		CssStyleSheetHandle sheetHandle = (CssStyleSheetHandle) csses.get(0);

		assertFalse(themeHandle.canAddCssStyleSheet(sheetHandle));
		assertFalse(themeHandle.canAddCssStyleSheet("base.css"));//$NON-NLS-1$
		assertFalse(themeHandle.canDropCssStyleSheet(sheetHandle));

		// get style from report design
		assertEquals("center", labelHandle.getStyle().getTextAlign());//$NON-NLS-1$
		// get style from report design css style
		assertEquals("right", labelHandle2.getStyle().getTextAlign());//$NON-NLS-1$
		// get style from theme style.

		assertNull(labelHandle3.getStyle().getTextAlign());
		// get style from theme css style
		assertNull(labelHandle4.getStyle().getTextAlign());

		assertTrue(((Style) labelHandle3.getStyle().getElement()).getContainer() instanceof Theme);
		assertTrue(((Style) labelHandle4.getStyle().getElement()).getContainer() instanceof Theme);

		assertFalse(labelHandle3.getStyle().getElement() instanceof CssStyle);
		assertTrue(labelHandle4.getStyle().getElement() instanceof CssStyle);
	}

	/**
	 * Copies the source to destination file.
	 *
	 * @param source absolute source file path
	 * @param dest   absolute destination file path
	 * @throws Exception
	 */

	private void copyContentToFile(String source, String dest) throws Exception {
		FileInputStream fis = new FileInputStream(source);
		FileOutputStream fos = new FileOutputStream(dest);
		byte[] fileData = new byte[5120];
		int readCount = -1;
		while ((readCount = fis.read(fileData)) != -1) {
			fos.write(fileData, 0, readCount);
		}

		fos.close();
		fis.close();

	}

	/**
	 * Copies a bunch of design/library files to the temporary folder.
	 *
	 * @param fileNames the design/library file names. The first item is the main
	 *                  design file.
	 * @return the file path of the design file
	 * @throws Exception
	 */

	private List dumpDesignAndLibrariesToFile(List fileNames) throws Exception {
		List filePaths = new ArrayList();
		for (int i = 0; i < fileNames.size(); i++) {
			String resourceName = (String) fileNames.get(i);
			filePaths.add(copyContentToFile(resourceName));
		}

		return filePaths;
	}

	/**
	 * Tests all properties and slots.
	 *
	 * @throws Exception if any exception
	 */

	public void testParser() throws Exception {
		openLibrary(fileName, ULocale.ENGLISH);

		assertEquals("W.C. Fields", libraryHandle.getStringProperty(Library.AUTHOR_PROP)); //$NON-NLS-1$
		assertEquals("http://company.com/reportHelp.html", libraryHandle.getStringProperty(Library.HELP_GUIDE_PROP)); //$NON-NLS-1$
		assertEquals("Whiz-Bang Plus", libraryHandle.getStringProperty(Library.CREATED_BY_PROP)); //$NON-NLS-1$

		// title

		assertEquals("TITLE_ID", libraryHandle.getStringProperty(Library.TITLE_ID_PROP)); //$NON-NLS-1$
		assertEquals("Sample Report", libraryHandle.getStringProperty(Library.TITLE_PROP)); //$NON-NLS-1$

		// comments

		assertEquals("First sample report.", libraryHandle.getStringProperty(Library.COMMENTS_PROP)); //$NON-NLS-1$

		// description

		assertEquals("DESCRIP_ID", libraryHandle.getStringProperty(Library.DESCRIPTION_ID_PROP)); //$NON-NLS-1$
		assertEquals("This is a first sample report.", libraryHandle.getStringProperty(Library.DESCRIPTION_PROP)); //$NON-NLS-1$

		// color-palette

		PropertyHandle colorPalette = libraryHandle.getPropertyHandle(Library.COLOR_PALETTE_PROP);
		List colors = colorPalette.getListValue();
		assertEquals(2, colors.size());
		CustomColor color = (CustomColor) colors.get(0);
		assertEquals("cus red", color.getName()); //$NON-NLS-1$
		assertEquals(111, color.getRGB());
		assertEquals("cus red key", color.getDisplayNameID()); //$NON-NLS-1$
		assertEquals("cus red display", color.getDisplayName()); //$NON-NLS-1$
		color = (CustomColor) colors.get(1);
		assertEquals("cus blue", color.getName()); //$NON-NLS-1$
		assertEquals(222, color.getRGB());
		assertEquals("cus blue key", color.getDisplayNameID()); //$NON-NLS-1$

		// config-vars
		PropertyHandle configVarHandle = libraryHandle.getPropertyHandle(Library.CONFIG_VARS_PROP);
		List configVars = configVarHandle.getListValue();
		assertEquals(4, configVars.size());
		ConfigVariable var = (ConfigVariable) configVars.get(0);
		assertEquals("var1", var.getName()); //$NON-NLS-1$
		assertEquals("mumble.jpg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(1);
		assertEquals("var2", var.getName()); //$NON-NLS-1$
		assertEquals("abcdefg", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(2);
		assertEquals("var3", var.getName()); //$NON-NLS-1$
		assertEquals("", var.getValue()); //$NON-NLS-1$
		var = (ConfigVariable) configVars.get(3);
		assertEquals("var4", var.getName()); //$NON-NLS-1$
		assertEquals(null, var.getValue());

		// images
		PropertyHandle imageHandle = libraryHandle.getPropertyHandle(Library.IMAGES_PROP);
		List images = imageHandle.getListValue();
		assertEquals(3, images.size());
		EmbeddedImage image = (EmbeddedImage) images.get(0);
		assertEquals("image1", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(libraryHandle.getModule())); //$NON-NLS-1$
		assertEquals("imagetesAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(libraryHandle.getModule()))).substring(0, 11));

		image = (EmbeddedImage) images.get(1);
		assertEquals("image2", image.getName()); //$NON-NLS-1$
		assertEquals("image/gif", image.getType(libraryHandle.getModule())); //$NON-NLS-1$
		assertEquals("/9j/4AAQSkZJRgA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(libraryHandle.getModule()))).substring(0, 15));

		image = (EmbeddedImage) images.get(2);
		assertEquals("image3", image.getName()); //$NON-NLS-1$
		assertEquals("image/bmp", image.getType(libraryHandle.getModule())); //$NON-NLS-1$
		assertEquals("AAAA", //$NON-NLS-1$
				new String(Base64.encodeBase64(image.getData(libraryHandle.getModule()))));

		SlotHandle themes = libraryHandle.getThemes();
		assertEquals(2, themes.getCount());

		ThemeHandle theme = (ThemeHandle) themes.get(0);
		assertEquals(1, theme.getStyles().getCount());

		theme = (ThemeHandle) themes.get(1);
		assertEquals(2, theme.getStyles().getCount());

		theme = libraryHandle.findTheme("theme1");//$NON-NLS-1$
		IncludedCssStyleSheetHandle css = (IncludedCssStyleSheetHandle) theme.includeCssesIterator().next();
		assertEquals("base.css", css.getFileName());//$NON-NLS-1$
		List styles = theme.getAllStyles();
		assertEquals(10, styles.size());
	}

	/**
	 * Tests writing the properties.
	 *
	 * @throws Exception if any error found.
	 */

	public void testWriter() throws Exception {
		openLibrary(fileName, ULocale.ENGLISH);

		libraryHandle.setProperty(Library.AUTHOR_PROP, "Report Author"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.HELP_GUIDE_PROP, "Help guide"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.CREATED_BY_PROP, "Report Creator"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.BASE_PROP, "c:\\base"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.UNITS_PROP, "cm"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.INCLUDE_RESOURCE_PROP, "new include resource"); //$NON-NLS-1$

		libraryHandle.setProperty(Library.TITLE_ID_PROP, "New title id"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.TITLE_PROP, "New title"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.COMMENTS_PROP, "New comments"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.DESCRIPTION_ID_PROP, "New description id"); //$NON-NLS-1$
		libraryHandle.setProperty(Library.DESCRIPTION_PROP, "New description"); //$NON-NLS-1$

		saveLibrary();
		assertTrue(compareFile(goldenFileName));
	}

	/**
	 * Test config variable.
	 *
	 * @throws Exception if any error found.
	 */
	public void testConfigVars() throws Exception {
		openLibrary(fileName, ULocale.ENGLISH);

		ConfigVariable configVar = new ConfigVariable();
		configVar.setName("VarToAdd"); //$NON-NLS-1$
		configVar.setValue("ValueToAdd"); //$NON-NLS-1$

		ConfigVariable newConfigVar = new ConfigVariable();
		newConfigVar.setName("VarToReplace"); //$NON-NLS-1$
		newConfigVar.setValue("ValueToReplace"); //$NON-NLS-1$
	}

	/**
	 * Checks the semantic error of Library.
	 *
	 * @throws Exception if any exception
	 */

	public void testSemanticError() throws Exception {
		openLibrary(semanticCheckFileName);
		// assertEquals( 5, errors.size( ) );
		//
		// int i = 0;
		//
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
		// ( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
		// ( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_EXISTS,
		// ( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		// assertEquals( PropertyValueException.DESIGN_EXCEPTION_VALUE_REQUIRED,
		// ( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
		// assertEquals( SemanticError.DESIGN_EXCEPTION_MISSING_MASTER_PAGE,
		// ( (ErrorDetail) errors.get( i++ ) ).getErrorCode( ) );
	}

}
