/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.StyleHandle;
import org.eclipse.birt.report.model.api.TextItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * <p>
 * Description: Extend a report item from library which contains a customer
 * style in the report design, change its style in library. Refresh library
 * explorer will not sync the correct style in report layout.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Create a library, add a new style, set the font color as "red".
 * <li>Add a text in the library, apply the above style on this element.
 * <li>New a report, use this library, pull this text from library explorer to
 * the report layout.
 * <li>Go to library, change the style, set the font color as "blue".
 * <li>Then in the library explorer of report design, refresh.
 * <li>The extended element in layout will not changed accordingly, while
 * preview result will get the right one.
 * </ol>
 * <p>
 * <b>Expected result:</b>
 * <p>
 * If the style of text is changed, the "refresh" action will cause to sync the
 * correct sytle in layout.
 * </p>
 * <b>Test description:</b>
 * <p>
 * Library file has a text, report include the library and extends the text.
 * Follow the steps, change the library style and refresh the report, make sure
 * the style property is refreshed in the child text.
 * </p>
 */
public class Regression_134231 extends BaseTestCase {

	private final static String INPUT = "regression_134231.xml"; //$NON-NLS-1$

	private final static String LIBRARY = "regression_134231_lib.xml"; //$NON-NLS-1$

	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 * @throws SemanticException
	 */

	public void test_regression_134231() throws DesignFileException, IOException, SemanticException {

		openDesign(INPUT);
		openLibrary(LIBRARY);

		TextItemHandle text = (TextItemHandle) designHandle.findElement("NewText"); //$NON-NLS-1$
		assertEquals("Sample Text", text.getContent()); //$NON-NLS-1$

		StyleHandle style = libraryHandle.findStyle("s1");
		assertNotNull(style);

		assertEquals("#FF0000", text.getStringProperty(style.COLOR_PROP));
		assertEquals("small", text.getStringProperty(style.FONT_SIZE_PROP)); //$NON-NLS-1$

		// Go to library, change the style, set the font color as "blue".

		style.setStringProperty(StyleHandle.COLOR_PROP, "blue"); //$NON-NLS-1$
		assertEquals("blue", style.getStringProperty(StyleHandle.COLOR_PROP));
//		String tgt = this.genOutputFile( LIBRARY );
//		libraryHandle.saveAs( tgt );

		// designHandle.reloadLibraries( );
		text = (TextItemHandle) designHandle.findElement("NewText");//$NON-NLS-1$
		assertEquals("#FF0000", text.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$
		// copyFile( LIBRARY, LIBRARY );
	}
}
