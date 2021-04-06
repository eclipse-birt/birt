
package org.eclipse.birt.report.tests.model.regression;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.model.api.CssSharedStyleHandle;
import org.eclipse.birt.report.model.api.SharedStyleHandle;
import org.eclipse.birt.report.model.api.css.CssStyleSheetHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * After import the css style sheet from layout, all the style imported are
 * treated as CSS Style
 * <p>
 * Test description: Test that the import the css style and it will transfer as
 * the custom style.
 * <p>
 * </p>
 */

public class Regression_180134 extends BaseTestCase {

	public void setUp() throws Exception {
		super.setUp();
		removeResource();

		// copyInputToFile ( INPUT_FOLDER + "/" + "regression_180134.css" );
		copyResource_INPUT("regression_180134.css", "regression_180134.css");

	}

	public void tearDown() {
		removeResource();
	}

	public void test_Regression_180134() throws Exception {
		createDesign();

		String CSSFile = this.getTempFolder() + "/input/regression_180134.css";
		System.out.println(CSSFile);
		// InputStream is = getResourceAStream( CSSFile );
		File CSSFileFile = new File(CSSFile);
		if (CSSFileFile.exists()) {
			FileInputStream is = new FileInputStream(CSSFileFile);
			CssStyleSheetHandle cssStyleHandle = designHandle.openCssStyleSheet(is);
			List selectionList = new ArrayList();
			Iterator iterator = cssStyleHandle.getStyleIterator();
			while (iterator.hasNext()) {
				selectionList.add(iterator.next());
			}
			designHandle.importCssStyles(cssStyleHandle, selectionList);

			// check
			assertEquals(8, designHandle.getStyles().getCount());
			SharedStyleHandle styleHandle = (SharedStyleHandle) designHandle.getStyles().get(0);
			assertFalse(styleHandle instanceof CssSharedStyleHandle);

			saveAs("regression_180134.rptdesign");
		} else {
			System.out.println("regression_180134.css is not exist");
		}
	}
}
