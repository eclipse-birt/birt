/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.script.IReportContext;

/**
 * unit test used to test if the cached image handle is called.
 */
public class CachedImageHandlerTest extends EngineCase {

	static final String DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/cached-image-handler.rptdesign";
	static final String REPORT_DESIGN = "./utest/test.rptdesign";
	static final String REPORT_DOCUMENT = "./utest/test.rptdocument";
	static final String TEST_FOLDER = "./utest/";

	public void setUp() {
		removeFile(TEST_FOLDER);
		copyResource(DESIGN_RESOURCE, REPORT_DESIGN);
	}

	public void tearDown() {
		removeFile(TEST_FOLDER);
	}

	public void testRender() throws Exception {
		CachedImageHandler imageHandler = new CachedImageHandler();

		EngineConfig config = new EngineConfig();
		RenderOption option = new RenderOption();
		option.setImageHandler(imageHandler);
		config.setEmitterConfiguration(RenderOption.OUTPUT_FORMAT_HTML, option);
		IReportEngine engine = new ReportEngine(config);

		// first we need create the report document
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		IRunTask task = engine.createRunTask(report);
		task.run(REPORT_DOCUMENT);
		task.close();

		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);

		// then we need render the report, this time the image is cached.
		IRenderTask render = engine.createRenderTask(document);
		HTMLRenderOption options = new HTMLRenderOption();
		options.setOutputFormat("html");
		options.setOutputStream(new ByteArrayOutputStream());
		render.setRenderOption(options);
		render.render();
		render.close();

		assertEquals(1, imageHandler.cachedImageCount);
		assertEquals(0, imageHandler.customImageCount);
		assertEquals(1, imageHandler.fileImageCount);
		// render the report again, the cached image should be return.
		render = engine.createRenderTask(document);
		render.setRenderOption(options);
		render.render();
		render.close();

		assertEquals(1, imageHandler.cachedImageCount);
		assertEquals(0, imageHandler.customImageCount);
		assertEquals(2, imageHandler.fileImageCount);
		document.close();

		engine.destroy();
	}

	class CachedImageHandler extends HTMLImageHandler {

		int cachedImageCount = 0;
		int customImageCount = 0;
		int fileImageCount = 0;

		HashMap map = new HashMap();

		public CachedImage getCachedImage(String id, int sourceType, IReportContext context) {
			String url = (String) map.get(id);
			if (url != null) {
				return new CachedImage(id, url);
			}
			return null;
		}

		public CachedImage addCachedImage(String id, int sourceType, IImage image, IReportContext context) {
			cachedImageCount++;
			String url = "CACHED_IMAGE:" + cachedImageCount;
			map.put(id, url);
			CachedImage cache = new CachedImage(id, url);
			return cache;
		}

		public String onCustomImage(IImage image, IReportContext context) {
			customImageCount++;
			return "CUSTOM_IMAGE:" + customImageCount;
		}

		public String onFileImage(IImage image, IReportContext context) {
			fileImageCount++;
			return image.getID();
		}
	}

}
