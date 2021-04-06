/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.api.HTMLCompleteImageHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>HTMLCompleteImageHandler test</b>
 * <p>
 * This case tests methods in HTMLCompleteImageHandler API.
 */

public class HTMLCompleteImageHandlerTest extends EngineCase {

	final static String INPUT = "EmbedImage.txt";

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	public void tearDown() {
		removeResource();
	}

	/**
	 * @param name
	 */

	/**
	 * Test OnDesignImage() method
	 */
	public void testOnDesignImage() {
		try {
			// Get embedded image byte array

			String input = this.genInputFile(INPUT);

			File imageFile = new File(input);
			long size = imageFile.length();
			InputStream is = new BufferedInputStream(new FileInputStream(imageFile));
			byte[] imageBytes = new byte[(int) size];
			is.read(imageBytes);
			assertNotNull(imageBytes);

			// Test onDesignImage()

			HTMLRenderContext context = new HTMLRenderContext();
			context.setImageDirectory(this.genOutputFile("image"));
			HTMLCompleteImageHandler imageHandler = new HTMLCompleteImageHandler();
			Image image = new Image(imageBytes, "image1");
			RenderOptionBase option = new RenderOptionBase();

			image.setRenderOption(option);
			File f = null;
			int count = 0;
			do {
				count++;
				String fp = this.genOutputFolder() + "/image/" + "design" + String.valueOf(count);
				f = new File(fp); // $NON-NLS-1$
				if (f.exists()) {
					f.delete();
					continue;
				} else {
					break;
				}
			} while (true);

			String str = imageHandler.onDesignImage(image, context);
			URL url = new URL(str);
			assertTrue("Failed to get design image in " + str, new File(url.getFile()).exists());
			removeFile(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test onURLImage() method
	 */
	public void testOnURLImage() {
		String url = "http://image";
		Image image = new Image(url);
		RenderOptionBase option = new RenderOptionBase();
		image.setRenderOption(option);
		HTMLRenderContext context = new HTMLRenderContext();
		HTMLCompleteImageHandler handler = new HTMLCompleteImageHandler();
		String urlGet = handler.onURLImage(image, context);
		assertEquals("OnURLImage() fail", url, urlGet);
	}

	/**
	 * Test onCustomeImage() method
	 */
	public void testOnCustomImage() {
		try {
			// Get embedded image byte array

			String input = this.genInputFile(INPUT);

			File imageFile = new File(input);
			long size = imageFile.length();
			InputStream is = new BufferedInputStream(new FileInputStream(imageFile));
			byte[] imageBytes = new byte[(int) size];
			is.read(imageBytes);

			// Test onDesignImage()

			HTMLRenderContext context = new HTMLRenderContext();
			context.setImageDirectory(this.genOutputFile("image"));
			HTMLCompleteImageHandler imageHandler = new HTMLCompleteImageHandler();
			Image image = new Image(imageBytes, "image1");
			RenderOptionBase option = new RenderOptionBase();
			image.setRenderOption(option);
			File f = null;
			int count = 1;
			do {
				count++;
				String fp = this.genOutputFolder() + "/image/" + "custom" + String.valueOf(count);
				f = new File(fp); // $NON-NLS-1$
				if (f.exists()) {
					f.delete();
					continue;
				} else {
					break;
				}
			} while (true);

			String str = imageHandler.onCustomImage(image, context);
			URL url = new URL(str);
			assertTrue("Failed to get custom image in " + str, new File(url.getFile()).exists());
			removeFile(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Test onDocImage() method Not implemented at 1.0.1,so return null.
	 */
	public void testOnDocImage() {
		String url = "http://image";
		Image image = new Image(url);
		RenderOptionBase option = new RenderOptionBase();
		image.setRenderOption(option);
		HTMLRenderContext context = new HTMLRenderContext();
		HTMLCompleteImageHandler handler = new HTMLCompleteImageHandler();
		String urlGet = handler.onDocImage(image, context);
		assertNull("OnDocImage() fail", urlGet);
	}
}
