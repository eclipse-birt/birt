/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.model.regression;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ImageHandle;
import org.eclipse.birt.report.tests.model.BaseTestCase;

/**
 * <b>Bug Description:</b>
 * <p>
 * embedded image name issues
 * <p>
 * <b>steps to reproduce:</b>
 * <ol>
 * <li>create a library
 * <li>insert an embedded image (logo.gif)
 * <li>publish this library
 * <li>create a template
 * <li>insert an embedded image (logo.gif)
 * <li>extend the embedded image we created earlier in library
 * <li>preview report in HTML, PDF, and Web viewer
 * </ol>
 * <b>expected behavior:</b>
 * <p>
 * two images are displayed properly
 * <p>
 * <b>actual behavior:</b>
 * <p>
 * the one extend from library cannot be displayd properly
 * <p>
 * <b>Test Description:</b>
 * <p>
 * Check embeded image source for the local image and extended image, they are
 * different
 */
public class Regression_152699 extends BaseTestCase {

	private String filename = "Regression_152699.xml"; //$NON-NLS-1$

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(filename, filename);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @throws DesignFileException
	 * @throws Exception
	 */
	public void test_regression_152699() throws DesignFileException {
		openDesign(filename);
		ImageHandle localimage = (ImageHandle) designHandle.findElement("Image");//$NON-NLS-1$
		ImageHandle extendimage = (ImageHandle) designHandle.findElement("NewImage"); //$NON-NLS-1$

		byte[] localdata = localimage.getEmbeddedImage().getData();
		byte[] extenddata = extendimage.getEmbeddedImage().getData();

		assertEquals(2, localdata.length);
		assertEquals(3, extenddata.length);

	}

}
