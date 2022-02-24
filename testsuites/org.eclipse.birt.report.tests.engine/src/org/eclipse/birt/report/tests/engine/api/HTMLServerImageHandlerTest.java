/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLServerImageHandler;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>HTMLServerImageHandler test</b>
 * <p>
 * This case tests methods in HTMLServerImageHandler API.
 */
public class HTMLServerImageHandlerTest extends EngineCase {

	final static String INPUT = "EmbedImage.txt";

	/**
	 * @param name
	 */
	public HTMLServerImageHandlerTest(String name) {
		super(name);
	}

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(HTMLServerImageHandlerTest.class);
	}

	/**
	 * Test OnDesignImage() method
	 */
	public void testOnDesignImage() {
		try {
			// Get embedded image byte array
			/*
			 * String plug_path=EngineCase.PLUGIN_PATH; String
			 * file_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_INPUT")
			 * +System.getProperty("file.separator")+"EmbedImage.txt"; String
			 * path=plug_path+System.getProperty("file.separator")+file_path;
			 */
			String path = this.getFullQualifiedClassName() + "/" + INPUT_FOLDER + "/" + INPUT;

			byte[] imageBytes = readImageFile(path);
			assertNotNull(imageBytes);

			// Test onDesignImage()

			HTMLRenderContext context = new HTMLRenderContext();
			context.setBaseImageURL(".");
			context.setImageDirectory(".");
			HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
			Image image = new Image(imageBytes, "image1");
			RenderOptionBase option = new RenderOptionBase();
			image.setRenderOption(option);
			File f = null;
			int count = 0;
			String fPath = System.getProperty("user.dir");
			do {
				count++;
				String fp = fPath + System.getProperty("file.separator") + "design" + String.valueOf(count);
				f = new File(fp); // $NON-NLS-1$
				if (f.exists()) {
					f.delete();
					continue;
				} else {
					break;
				}
			} while (true);

			String str = imageHandler.onDesignImage(image, context);
			String strGet = "./design1";
			assertEquals("", str, strGet);
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
		HTMLServerImageHandler handler = new HTMLServerImageHandler();
		String urlGet = handler.onURLImage(image, context);
		assertEquals("OnURLImage() fail", url, urlGet);
	}

	/**
	 * Test onCustomeImage() method
	 */
	public void testOnCustomImage() {
		try {
			// Get embedded image byte array
			/*
			 * String plug_path=EngineCase.PLUGIN_PATH; String
			 * file_path=EngineCase.RESOURCE_BUNDLE.getString("CASE_INPUT")
			 * +System.getProperty("file.separator")+"EmbedImage.txt"; String
			 * path=plug_path+System.getProperty("file.separator")+file_path;
			 */
			String path = getClassFolder() + System.getProperty("file.separator") + INPUT_FOLDER
					+ System.getProperty("file.separator") + "EmbedImage.txt";

			byte[] imageBytes = readImageFile(path);

			// Test onDesignImage()

			HTMLRenderContext context = new HTMLRenderContext();
			context.setBaseImageURL(".");
			context.setImageDirectory(".");
			HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
			Image image = new Image(imageBytes, "image1");
			RenderOptionBase option = new RenderOptionBase();
			image.setRenderOption(option);
			File f = null;
			int count = 1;
			String fPath = System.getProperty("user.dir");
			do {
				count++;
				String fp = fPath + System.getProperty("file.separator") + "custom" + String.valueOf(count);
				f = new File(fp); // $NON-NLS-1$
				if (f.exists()) {
					f.delete();
					continue;
				} else {
					break;
				}
			} while (true);

			String str = imageHandler.onCustomImage(image, context);
			byte[] actualBytes = readImageFile(str);
			// Here, we want to verify actual contents of image from text above
			int length = imageBytes.length;
			assertEquals(length, actualBytes.length);
			for (int index = 0; index < length; index++) {
				assertEquals(imageBytes[index], actualBytes[index]);
			}
			removeFile(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private byte[] readImageFile(String path) throws FileNotFoundException, IOException {
		File imageFile = new File(path);
		long size = imageFile.length();
		InputStream is = new BufferedInputStream(new FileInputStream(imageFile));
		byte[] imageBytes = new byte[(int) size];
		is.read(imageBytes);
		return imageBytes;
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
		HTMLServerImageHandler handler = new HTMLServerImageHandler();
		String urlGet = handler.onDocImage(image, context);
		assertNull("OnDocImage() fail", urlGet);
	}

}
