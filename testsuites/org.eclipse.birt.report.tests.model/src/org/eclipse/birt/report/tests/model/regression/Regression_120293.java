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

import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * There is no response when I drag an embedded image from library explorer into
 * outline view.
 * </p>
 * Test description:
 * <p>
 * To create an embedded image from an existed embeded image.
 * </p>
 */

public class Regression_120293 extends BaseTestCase {
	final static String INPUT1 = "Library_1.xml";
	final static String INPUT2 = "DesignWithoutLibrary.xml";

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file
		// copyResource_INPUT( INPUT1 , INPUT1 );
		// copyResource_INPUT( INPUT2 , INPUT2 );
		copyInputToFile(INPUT_FOLDER + "/" + INPUT1);
		copyInputToFile(INPUT_FOLDER + "/" + INPUT2);
	}

	/**
	 * @throws Exception
	 */

	public void test_regression_120293() throws Exception {
		openLibrary(INPUT1, true); // $NON-NLS-1$

		Iterator imageIter = libraryHandle.imagesIterator();
		EmbeddedImageHandle baseImage = (EmbeddedImageHandle) imageIter.next();

		openDesign(INPUT2); // $NON-NLS-1$
		designHandle.includeLibrary(INPUT1, "Lib1"); //$NON-NLS-1$ //$NON-NLS-2$

		EmbeddedImage newImage = StructureFactory.newEmbeddedImageFrom(baseImage, "image1", designHandle); //$NON-NLS-1$

		assertEquals("image1", newImage.getName()); //$NON-NLS-1$
		assertNotNull(newImage.getData(design));

		designHandle.addImage(newImage);

		boolean added = false;
		for (Iterator iter = designHandle.imagesIterator(); iter.hasNext();) {
			String name = ((EmbeddedImageHandle) iter.next()).getName();
			if ("image1".equalsIgnoreCase(name)) //$NON-NLS-1$
			{
				added = true;
			}
		}

		assertTrue(added);
	}
}
