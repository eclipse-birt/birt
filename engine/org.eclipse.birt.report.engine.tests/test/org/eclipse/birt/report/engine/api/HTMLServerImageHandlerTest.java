
/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.engine.api;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.Image;
import org.eclipse.birt.report.model.api.IResourceLocator;

/**
 * 
 */

public class HTMLServerImageHandlerTest extends EngineCase {
	protected IReportEngine engine = null;
	protected IReportRunnable runnable = null;

	protected static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/HTMLServerImageHandlerTest.rptdesign";
	protected static final String REPORT_DESIGN = "HTMLServerImageHandlerTest.rptdesign";

	public void setUp() throws Exception {
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);

		engine = createReportEngine();
		runnable = engine.openReportDesign(REPORT_DESIGN);
	}

	public void tearDown() {
		// shut down the engine.
		if (engine != null) {
			engine.shutdown();
		}
		removeFile(REPORT_DESIGN);
	}

	/**
	 * API test to test Multi-Types Image
	 */
	public void testMultiTypesImage() {
		String blankURL = "http://image";
		Image image = new Image(blankURL);
		RenderOptionBase option = new RenderOptionBase();
		image.setRenderOption(option);
		int[] imageTypes = new int[] { Image.DESIGN_IMAGE, Image.REPORTDOC_IMAGE, Image.URL_IMAGE, Image.FILE_IMAGE,
				Image.CUSTOM_IMAGE, Image.INVALID_IMAGE };
		HTMLRenderContext context = new HTMLRenderContext();
		HTMLServerImageHandler handler = new HTMLServerImageHandler();
		for (int size = imageTypes.length, index = 0; index < size; index++) {
			String result = null;
			switch (imageTypes[index]) {
			case Image.DESIGN_IMAGE:
				result = handler.onDesignImage(image, context);
				break;
			case Image.REPORTDOC_IMAGE:
				result = handler.onDocImage(image, context);
				/* not implement */
				break;
			case Image.URL_IMAGE:
				result = handler.onURLImage(image, context);
				break;
			case Image.FILE_IMAGE:
				result = handler.onFileImage(image, context);
				break;
			case Image.CUSTOM_IMAGE:
				result = handler.onCustomImage(image, context);
				break;
			case Image.INVALID_IMAGE:
				result = "";
				/* not implement */
				break;
			}
			if (result != null && result.length() > 0) {
				assertTrue(isValid(result));
			}
		}
	}

	/**
	 * API test on HTMLServerImageHandler.onDocImage( ) method. This method is not
	 * implemented so far, so the default return value is *null*
	 */
	public void testOnDocImage() {
		HTMLServerImageHandler handler = new HTMLServerImageHandler();
		String result = handler.onDocImage(null, null);
		assertNull(result);
	}

	/**
	 * API test on HTMLServerImageHandler.onURLImage( ) method. This test get a
	 * connection for the web specified by the URL
	 */
	public void testOnURLImage() {
		try {
			final String ACTU_IMG_URL = "https://mail.google.com/mail/help/images/logo1.gif";
			HTMLRenderContext context = new HTMLRenderContext();
			context.setImageDirectory("");
			Image image = new Image(ACTU_IMG_URL);
			HTMLServerImageHandler handler = new HTMLServerImageHandler();
			String urlString = handler.onURLImage(image, context);

			URL url = runnable.getDesignHandle().getModule().findResource(urlString, IResourceLocator.IMAGE);
			InputStream inputStream = url.openConnection().getInputStream();
			int availableBytes = inputStream.available();
			assert (availableBytes > 0);
		} catch (java.net.ConnectException ce) {
			ce.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	public void testOnFileImage() {
		// todo
	}

	public void testoOnCustomImage() {
		// todo
	}

	/**
	 * API test on HTMLServerImageHandler.onDesignImage( ) method
	 */
	public void testOnDesignImage() {
		HTMLRenderContext context = new HTMLRenderContext();
		context.setImageDirectory(".");
		context.setBaseImageURL(".");
		Image image = (Image) runnable.getImage("img.jpg");
		RenderOptionBase option = new RenderOptionBase();
		image.setRenderOption(option);
		HTMLServerImageHandler imageHandler = new HTMLServerImageHandler();
		String resultPath = imageHandler.onDesignImage(image, context);
		File resultFile = new File(resultPath);
		assertTrue(resultFile.exists());
		assertTrue(resultFile.length() > 0);
		removeFile(resultPath);
	}

	private boolean isFileAbsolute(String path) {
		assert (path != null);
		return (new File(path)).isAbsolute();
	}

	private boolean isValid(String path) {
		final String[] URL_PREFIXS = new String[] { "http:", "https:" };
		if (path.startsWith(URL_PREFIXS[0]) || path.startsWith(URL_PREFIXS[1])) {
			return true;
		}
		return isFileAbsolute(path);
	}
}
