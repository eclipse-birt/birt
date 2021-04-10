/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.util.Iterator;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * Description:
 * <p>
 * Use an embedded image defined in library to add a new image in report, it is
 * marked invalid when previewed.
 * <p>
 * Steps to reproduce:
 * <ol>
 * <li>Use a library in a report.
 * <li>Drag an embedded image from library explorer into outline view.
 * <li>Add an image in the report and use the embedded image defined in library.
 * <li>Preview this report.
 * <li>The image can't be displayed properly.
 * </ol>
 * </p>
 * Test description:
 * <p>
 * Report include a library, library has an embedded image, extends an embedded
 * image from library, and create an image that use the child, make sure the
 * image is valid.
 * </p>
 */
public class Regression_121022 extends BaseTestCase {

	private final static String REPORT = "regression_121022.xml"; //$NON-NLS-1$

	private final static String libname = "regression_121022_lib.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		copyResource_INPUT(REPORT, REPORT);
		copyResource_INPUT(libname, libname);
	}

	/**
	 * @throws DesignFileException
	 * @throws SemanticException
	 */

	public void test_regression_121022() throws DesignFileException, SemanticException {
		openDesign(REPORT);
		LibraryHandle lib = designHandle.getLibrary("regression_121022_lib"); //$NON-NLS-1$

		Iterator imageIter = lib.imagesIterator();
		EmbeddedImageHandle embeddedImage = (EmbeddedImageHandle) imageIter.next();
		assertEquals("regression_121022_lib.sample.gif", embeddedImage.getQualifiedName()); //$NON-NLS-1$

		ElementFactory factory = designHandle.getElementFactory();

		// create an image that use the embedded image in the library.

		ImageHandle imageHandle = factory.newImage("newImage"); //$NON-NLS-1$
		imageHandle.setSource(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED);
		imageHandle.setImageName("regression_121022_lib.sample.gif"); //$NON-NLS-1$

		designHandle.getBody().add(imageHandle);

		// make sure the image is valid and the referenced embedded image can be
		// accessed.

		ImageHandle image = (ImageHandle) designHandle.findElement("newImage"); //$NON-NLS-1$
		assertTrue(image.isValid());
		assertNotNull(image.getEmbeddedImage().getData());

	}
}
