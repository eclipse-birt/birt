/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.ThemeHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Tests the function of importing a css file to the library/report.
 */

public class ImportCssTest extends BaseTestCase {

	private String cssFileName = "base.css"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */

	protected void setUp() throws Exception {
		super.setUp();
		sessionHandle = DesignEngine.newSession((ULocale) null);
	}

	/**
	 * Loads the style sheet with the given file path and the module.
	 * 
	 * @param fileName the file name
	 * @param module   the module
	 * @return a <code>CssStyleSheetHandle</code>
	 * @throws Exception
	 */

	private CssStyleSheetHandle loadStyleSheet(String fileName, ModuleHandle module) throws Exception {
		fileName = INPUT_FOLDER + fileName;
		InputStream is = getResourceAStream(fileName);
		return module.openCssStyleSheet(is);
	}

	/**
	 * Gets all styles in the stylesheet.
	 * 
	 * @param styleSheet the style sheet
	 * @return a list containing styles. Each item is <code>StyleHandle</code>.
	 */

	private List<StyleHandle> getAllStyles(CssStyleSheetHandle styleSheet) {
		List<StyleHandle> selectedStyles = new ArrayList<StyleHandle>();
		for (Iterator<StyleHandle> iter1 = styleSheet.getStyleIterator(); iter1.hasNext();)
			selectedStyles.add(iter1.next());

		return selectedStyles;
	}

	/**
	 * Test cases:
	 * 
	 * <ul>
	 * <li>import css styles to the styles slot
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testImportCssToDesign() throws Exception {
		// designHandle = sessionHandle.createDesign( );
		createDesign();

		CssStyleSheetHandle styleSheet = loadStyleSheet(cssFileName, designHandle);

		designHandle.importCssStyles(styleSheet, getAllStyles(styleSheet));
		assertEquals(0, designHandle.getAllCssStyleSheets().size());

		// import css is customer style not css style
		Object obj = designHandle.getAllStyles().get(0);
		assertTrue(obj instanceof SharedStyleHandle);
		assertFalse(obj instanceof CssSharedStyleHandle);

		save();
		assertTrue(compareFile("ImportCssToDesign_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * Style names are not duplicate.
	 * 
	 * <ul>
	 * <li>import css styles to the default theme.
	 * <li>import css styles to the specified theme.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testImportCssToLibraryWithoutDuplicate() throws Exception {
		// import css styles to the default theme.

		libraryHandle = sessionHandle.createLibrary();

		CssStyleSheetHandle styleSheet = loadStyleSheet(cssFileName, libraryHandle);

		libraryHandle.importCssStyles(styleSheet, getAllStyles(styleSheet));
		saveLibrary();
		assertTrue(compareFile("ImportCssToLibrary_golden.xml")); //$NON-NLS-1$

		// import css styles to the specified theme.

		libraryHandle = sessionHandle.createLibrary();
		libraryHandle.getThemes().add(libraryHandle.getElementFactory().newTheme("theme1")); //$NON-NLS-1$
		styleSheet = loadStyleSheet(cssFileName, libraryHandle);

		libraryHandle.importCssStyles(styleSheet, getAllStyles(styleSheet), "theme1"); //$NON-NLS-1$

		saveLibrary();
		assertTrue(compareFile("ImportCssToLibrary1_golden.xml")); //$NON-NLS-1$
	}

	/**
	 * Test cases:
	 * 
	 * Style names are duplicate.
	 * 
	 * <ul>
	 * <li>import css styles to the specified theme.
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testImportCssToLibraryWithDuplicate() throws Exception {
		// import css styles to the specified theme.

		libraryHandle = sessionHandle.createLibrary();
		ThemeHandle theme1 = libraryHandle.getElementFactory().newTheme("theme1");//$NON-NLS-1$
		libraryHandle.getThemes().add(theme1);
		theme1.getStyles().add(libraryHandle.getElementFactory().newStyle("table")); //$NON-NLS-1$

		CssStyleSheetHandle styleSheet = loadStyleSheet(cssFileName, libraryHandle);

		libraryHandle.importCssStyles(styleSheet, getAllStyles(styleSheet), "theme1"); //$NON-NLS-1$

		saveLibrary();
		assertTrue(compareFile("ImportCssToLibrary2_golden.xml")); //$NON-NLS-1$

	}

	/**
	 * Test container of css style sheet handle when import to report design.
	 * 
	 * @throws Exception
	 */

	public void testContainerOfCssStyleSheet() throws Exception {
		openDesign("ImportCssReport.xml"); //$NON-NLS-1$
		CssStyleSheetHandle cssStyleSheetHandle = (CssStyleSheetHandle) designHandle.getTheme().getAllCssStyleSheets()
				.get(0);
		SharedStyleHandle styleHandle = cssStyleSheetHandle.findStyle("table"); //$NON-NLS-1$
		assertTrue(styleHandle instanceof CssSharedStyleHandle);
		CssSharedStyleHandle cssStyleHandle = (CssSharedStyleHandle) styleHandle;
		DesignElementHandle elementHandle = cssStyleHandle.getCssStyleSheetHandle().getContainerHandle();
		assertTrue(elementHandle instanceof ThemeHandle);
	}

	/**
	 * Test cases:
	 * 
	 * Style names are not duplicate.
	 * 
	 * <ul>
	 * 1. imports css styles to the unexisted specified theme 1. imports css styles
	 * to the unexisted default theme
	 * </ul>
	 * 
	 * @throws Exception
	 */

	public void testImportCssToLibraryWithoutThemeElement() throws Exception {
		// import css styles to the specified theme.

		openLibrary("ImportCssToLibrary3.xml", ULocale.ENGLISH); //$NON-NLS-1$

		CssStyleSheetHandle styleSheet = loadStyleSheet(cssFileName, libraryHandle);

		// create theme1 in this method

		libraryHandle.importCssStyles(styleSheet, getAllStyles(styleSheet));

		saveLibrary();
		assertTrue(compareFile("ImportCssToLibrary3_golden.xml")); //$NON-NLS-1$

		// create default theme in this method

		openLibrary("ImportCssToLibrary4.xml", ULocale.ENGLISH); //$NON-NLS-1$

		styleSheet = loadStyleSheet(cssFileName, libraryHandle);

		libraryHandle.importCssStyles(styleSheet, getAllStyles(styleSheet));

		saveLibrary();
		assertTrue(compareFile("ImportCssToLibrary4_golden.xml")); //$NON-NLS-1$

	}
}