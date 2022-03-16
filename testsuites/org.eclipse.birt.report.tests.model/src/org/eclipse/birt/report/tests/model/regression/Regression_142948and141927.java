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
import java.net.URL;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * Regression description:
 * </p>
 * <b>142948:</b> Invoke DefaultResourceLocator.findResource() with a url string
 * like "file:/....." will return null, even the file exists
 * </p>
 * <b>141927:</b> Resource locator doesn't work when set as "http://.."
 * </p>
 * Test description:
 * <p>
 * Find resource like "file:/.....", if it exists, won't return null. If it
 * doesn't exist, return null
 * </p>
 * Find resource with HTTP protocol
 */

public class Regression_142948and141927 extends BaseTestCase {

	private String filename = "Regression_142948and141927.xml"; //$NON-NLS-1$

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();

		// retrieve two input files from tests-model.jar file

		copyInputToFile(INPUT_FOLDER + "/" + filename);
	}

	/**
	 * @throws DesignFileException
	 * @throws IOException
	 */
	public void test_regression_142948and141927() throws DesignFileException, IOException {
		openDesign(filename);

		designHandle.setFileName(null);
		String filePath = "file:/" + getTempFolder() + "/" + INPUT_FOLDER //$NON-NLS-1$
				+ "/" + filename;

		designHandle.setFileName(filePath);
		System.out.println(filePath);
		assertNotNull(designHandle.getFileName());
		URL url = designHandle.findResource(filePath, IResourceLocator.LIBRARY);

		url = designHandle.findResource("NoExistedDesign.xml", //$NON-NLS-1$
				IResourceLocator.LIBRARY);
		assertNull(url);

		// Find resource with HTTP protocol
		designHandle.getModule().setSystemId(new URL("http://www.eclipse.org/")); //$NON-NLS-1$

		url = designHandle.findResource("images/EclipseBannerPic.jpg", //$NON-NLS-1$
				IResourceLocator.IMAGE);

		assertEquals("http://www.eclipse.org/images/EclipseBannerPic.jpg", //$NON-NLS-1$
				url.toString());

	}

}
