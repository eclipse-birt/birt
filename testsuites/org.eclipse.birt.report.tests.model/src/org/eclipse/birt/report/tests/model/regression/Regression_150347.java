/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Regression description:</b>
 * <p>
 * Support binary stream as preview image.
 * <p>
 * It seems that Model will add a property in ReportDesign named "thumbnail",
 * which saves the binary stream of the Thumbnail image.
 * <p>
 * Test description:
 * <p>
 * Ensure getThumbnail(), setThumbnail() and deleteThumbnail() are supported in
 * ReportDesignHandle.
 * <p>
 */
public class Regression_150347 extends BaseTestCase {

	/**
	 * @throws IOException
	 * @throws SemanticException
	 */

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("worm.jpg", "worm.jpg");
	}

	public void tearDown() {
		removeResource();
	}

	public void test_regression_150347() throws IOException, SemanticException {
		ReportDesignHandle designHandle = this.createDesign();
		InputStream is = Regression_150347.class.getResourceAsStream("input/worm.jpg"); //$NON-NLS-1$

		byte[] imageBytes = streamToBytes(is);

		designHandle.setThumbnail(imageBytes);
		byte[] data = designHandle.getThumbnail();

		assertEquals(imageBytes.length, data.length);

		designHandle.deleteThumbnail();
		assertNull(designHandle.getThumbnail());

	}
}
