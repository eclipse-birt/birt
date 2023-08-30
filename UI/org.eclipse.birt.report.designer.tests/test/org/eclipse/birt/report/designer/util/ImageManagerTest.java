/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

import org.eclipse.birt.report.designer.tests.TestsPlugin;
import org.eclipse.birt.report.designer.testutil.BaseTestCase;
import org.eclipse.birt.report.designer.testutil.PlatformUtil;
import org.eclipse.birt.report.model.api.elements.structures.EmbeddedImage;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.graphics.Image;

/**
 *
 */

public class ImageManagerTest extends BaseTestCase {

	private Image localImage;

	private static String iconPath;

	private static String iconURL;

	private static final String TEST_FILE = "icon/test.jpg"; //$NON-NLS-1$
	// Doesn't exist
	private static final String TEST_ERROR_FILE = "icon/error.jpg"; //$NON-NLS-1$ //not exists

	// Invalid url
	private static final String TEST_ERROR_URL = "http://"; //$NON-NLS-1$

	static {
		try {
			URL iconURL = Platform.asLocalURL(TestsPlugin.getDefault().getBundle().getEntry("/"));
			ImageManagerTest.iconURL = iconURL.toString();
			iconPath = iconURL.getFile();
		} catch (IOException e) {
		}

	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		localImage = new Image(null, iconPath + TEST_FILE);
	}

	@Override
	protected void tearDown() throws Exception {
		localImage.dispose();

		super.tearDown();
	}

	/*
	 * Class under test for Image getImage(String)
	 */

	public void testGetImageByPath() throws Exception {
		Image image = ImageManager.getInstance().getImage(iconPath + TEST_FILE);
		assertNotNull(image);
		if (PlatformUtil.isWindows()) {// platform related issue
			assertTrue(Arrays.equals(image.getImageData().data, localImage.getImageData().data));
		}
	}

	/*
	 * Class under test for Image getImage(String)
	 */

	public void testGetImageByWrongPath() throws Exception {
		assertNull(ImageManager.getInstance().getImage(TEST_ERROR_FILE));
	}

	/*
	 * Class under test for Image getImage(URL)
	 */
	public void testGetImageByURL() throws Exception {
		String imageURL = iconURL + TEST_FILE;
		Image image = ImageManager.getInstance().getImage(imageURL);
		assertNotNull(image);
		assertTrue(Arrays.equals(image.getImageData().data, localImage.getImageData().data));
		assertEquals(image, ImageManager.getInstance().getImage(imageURL));
	}

	/*
	 * Class under test for Image getImage(URL)
	 */

	public void testGetImageByWrongURL() throws Exception {
		assertNull(ImageManager.getInstance().getImage(TEST_ERROR_URL));
	}

	/*
	 * Class under test for Image getImage(EmbeddedImage)
	 */
	public void testGetImageByEmbeddedImage() throws Exception {
		EmbeddedImage embeddedImage = new EmbeddedImage("Test"); //$NON-NLS-1$
		FileInputStream is = new FileInputStream(iconPath + TEST_FILE);
		byte[] data = new byte[is.available()];
		is.read(data);
		embeddedImage.setData(data);
		getReportDesign().handle().addImage(embeddedImage);
		Image image = ImageManager.getInstance().getEmbeddedImage(getReportDesignHandle(), embeddedImage.getName());
		assertNotNull(image);
		if (PlatformUtil.isWindows()) {// platform related issue
			assertTrue(Arrays.equals(image.getImageData().data, localImage.getImageData().data));
		}
		assertEquals(image,
				ImageManager.getInstance().getEmbeddedImage(getReportDesignHandle(), embeddedImage.getName()));
	}

	public void testLoadImage() throws IOException {
		Image image = ImageManager.getInstance().loadImage(iconPath + TEST_FILE);
		assertNotNull(image);
		assertEquals(image, ImageManager.getInstance().loadImage(iconPath + TEST_FILE));
		assertEquals(image, ImageManager.getInstance().getImage(iconPath + TEST_FILE));
		try {
			ImageManager.getInstance().loadImage(TEST_ERROR_FILE);
		} catch (Exception e) {
			return;
		}
		fail();
	}
}
