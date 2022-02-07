/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Preview, the icon of image that copied from template image that from library
 * can lost.
 * <p>
 * Steps:
 * <ol>
 * <li>Create a library that include image
 * <li>Create a template, user the library, drag the image to the layout
 * <li>Publish the template
 * <li>Create a report use the template
 * <li>The layout can display the image, copy and paste the image
 * <li>Preview in html
 * <li>Open the Outline view
 * </ol>
 * <b>Actual Results:</b>
 * <p>
 * The copied image's icon has lost in the outline view
 * <p>
 * <b>Expected Results:</b>
 * <p>
 * The image's icon can be displayed correctly in the outline view.
 * </p>
 * Test description:
 * <p>
 * Follow the steps, use a template that include a library, and the template
 * extends an image from library, ensure the the referenced image can be
 * accessed from the design.
 * </p>
 */
public class Regression_121008 extends BaseTestCase {

	private final static String TEMPLATE = "regression_121008_template.xml"; //$NON-NLS-1$
	private final static String libname = "regression_121008_lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(TEMPLATE, TEMPLATE);
		copyResource_INPUT(libname, libname);
	}

	/**
	 * @throws ContentException
	 * @throws NameException
	 * @throws DesignFileException
	 */

	public void test_regression_121008() throws ContentException, NameException, DesignFileException {
		openDesign(TEMPLATE);
		ImageHandle image = (ImageHandle) designHandle.findElement("NewImage"); //$NON-NLS-1$

		ImageHandle copy = (ImageHandle) image.copy().getHandle(design);
		designHandle.rename(copy);

		designHandle.getBody().paste(copy);
		ImageHandle image2 = (ImageHandle) designHandle.getBody().get(1);

		// ensure the the referenced image can be accessed from the design.

		assertEquals("regression_121008_lib.lvback.gif", image2.getEmbeddedImage().getQualifiedName());
		assertNotNull(image2.getEmbeddedImage().getData());
	}
}
