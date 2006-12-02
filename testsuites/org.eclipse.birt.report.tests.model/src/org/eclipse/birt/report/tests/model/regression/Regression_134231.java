/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
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
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(INPUT, INPUT);
		copyResource_INPUT(LIBRARY, LIBRARY);

	}

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 * @throws SemanticException
	 */

	public void test_regression_134231() throws DesignFileException,
			IOException, SemanticException {
		openDesign(INPUT);

		// backup the library file, as we need to modify the input file during
		// test case, the backed-up one will be copied back when case finished.

		//makeOutputDir();
		// the following code may not work when test plugin is packaged as jar
		// "getFullQualifiedClassName( )" is recommend to replace "this.getClassFolder()"
		//copyFile(this.getClassFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY,
		//		this.getClassFolder() + "/" + OUTPUT_FOLDER + "/" + LIBRARY);

		
		
		// find the child text

		TextItemHandle text = (TextItemHandle) designHandle
				.findElement("NewText"); //$NON-NLS-1$
		assertEquals("Sample Text", text.getContent()); //$NON-NLS-1$
		System.out.println(text.getStringProperty(StyleHandle.COLOR_PROP));
		assertEquals("red", text.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$

		// Go to library, change the style, set the font color as "blue".

		openLibrary(LIBRARY);
		StyleHandle s1 = libraryHandle.findStyle("s1"); //$NON-NLS-1$
		s1.setStringProperty(StyleHandle.COLOR_PROP, "blue"); //$NON-NLS-1$

		
		//TODO delete it. See if this piece of code is continue to work after packaging
		String className = getFullQualifiedClassName( );
		String tgt = className +  "/" + INPUT_FOLDER + "/"
		+ LIBRARY;
		
		//className = className.replace( '.', '/' );
		//String src = className + "/" + folder + "/" + src;
		
		libraryHandle.saveAs( tgt );
		
		//copyResource_INPUT(this.getClassFolder() + "/" + INPUT_FOLDER + "/"
		//		+ LIBRARY, LIBRARY);
		// refresh the libraries, make sure the style property is refreshed in
		// the child text

		designHandle.reloadLibraries();
		text = (TextItemHandle) designHandle.findElement("NewText"); //$NON-NLS-1$
		assertEquals("blue", text.getStringProperty(StyleHandle.COLOR_PROP)); //$NON-NLS-1$

		// we recover the library file, copied back from backup.

		copyFile(this.getClassFolder() + "/" + OUTPUT_FOLDER + "/" + LIBRARY,
				this.getClassFolder() + "/" + INPUT_FOLDER + "/" + LIBRARY);
	}
}
