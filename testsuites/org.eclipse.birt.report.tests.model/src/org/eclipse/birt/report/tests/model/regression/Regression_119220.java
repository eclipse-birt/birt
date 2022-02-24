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
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.elements.interfaces.IImageItemModel;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Image in library can't display properly when it is imported in a report
 * design
 * </p>
 * Test description:
 * <p>
 * Extended image items will refer to a library embedded image
 * </p>
 */

public class Regression_119220 extends BaseTestCase {

	private String filename = "Regression_119220.xml"; //$NON-NLS-1$
	private String imagename = "embeddedimage.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(filename, filename);
		copyResource_INPUT(imagename, imagename);
	}

	/**
	 * @throws DesignFileException
	 */

	public void test_regression_119220() throws DesignFileException {
		openDesign(filename);

		libraryHandle = designHandle.getLibrary("Lib"); //$NON-NLS-1$
		assertNotNull(libraryHandle);
		PropertyHandle libImages = libraryHandle.getPropertyHandle(ModuleHandle.IMAGES_PROP);

		ImageHandle imageHandle = (ImageHandle) designHandle.findElement("Image"); //$NON-NLS-1$

		assertNotNull(imageHandle);

		assertEquals("Lib.actuatetop.jpg", imageHandle.getImageName()); //$NON-NLS-1$
		assertEquals("Lib.actuatetop.jpg", imageHandle //$NON-NLS-1$
				.getProperty(IImageItemModel.IMAGE_NAME_PROP));

		// make sure the embedded image refer to the library.

		assertEquals(libImages.getAt(0).getStructure(), imageHandle.getEmbeddedImage().getStructure());
	}
}
