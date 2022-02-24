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

package org.eclipse.birt.report.engine.emitter.html;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import junit.framework.TestCase;

import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;

/**
 * Unit test for resource manager and HTML report emitter.
 * 
 */
abstract public class HTMLReportEmitterTestCase extends TestCase {

	public abstract String getWorkSpace();

	protected static final String REPORT_DESIGN = "design.rptdesign";
	protected static final String REPORT_DOCUMENT = "reportdocument";

	protected IReportEngine engine;

	public void setUp() {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		EngineConfig config = new EngineConfig();
		engine = createReportEngine(config);
	}

	public void tearDown() {
		engine.shutdown();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
		removeFile(new File(getWorkSpace())); // $NON-NLS-1$
	}

	public byte[] loadResource(String src) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			int size = in.available();
			byte[] buffer = new byte[size];
			in.read(buffer);
			in.close();
			return buffer;
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
		return null;
	}

	public void unzip(String src, String folder) {

	}

	public IReportEngine createReportEngine() {
		return createReportEngine(null);
	}

	public IReportEngine createReportEngine(EngineConfig config) {
		if (config == null) {
			config = new EngineConfig();
		}

		// assume we has in the platform
		Object factory = Platform.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		if (factory instanceof IReportEngineFactory) {
			return ((IReportEngineFactory) factory).createReportEngine(config);
		}
		return null;
	}

	protected IReportDocument createReportDocument(String designFileName) throws EngineException {
		useDesignFile(designFileName);
		createReportDocument();
		return engine.openReportDocument(REPORT_DOCUMENT);
	}

	protected IRunAndRenderTask createRunAndRenderTask(String designFileName) throws EngineException {
		useDesignFile(designFileName);
		IReportRunnable reportRunnable = engine.openReportDesign(REPORT_DESIGN);
		return engine.createRunAndRenderTask(reportRunnable);
	}

	protected IRenderTask createRenderTask(String designFileName) throws EngineException {
		IReportDocument document = createReportDocument(designFileName);
		return engine.createRenderTask(document);
	}

	protected void useDesignFile(String fileName) {
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
		copyResource(fileName, REPORT_DESIGN);
	}

	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument() throws EngineException {
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		// create an IRunTask
		IRunTask task = engine.createRunTask(report);
		// execute the report to create the report document.
		task.run(REPORT_DOCUMENT);
		// close the task, release the resource.
		task.close();
	}

	/**
	 * Get the content of the stream as string
	 * 
	 * @param in the input stream
	 * @return the string content of the input stream
	 * @throws Exception
	 */
	public String loadReportContent() {
		InputStream in = null;
		try {
			in = new BufferedInputStream(new FileInputStream(getWorkSpace() + "/" + HTMLReportEmitter.REPORT_FILE)); //$NON-NLS-1$
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return loadStreamContent(in);
	}

	protected String loadBodyContent() {
		String content = loadReportContent();
		int indexOfBody = content.indexOf("<body>"); //$NON-NLS-1$
		int indexOfEndBody = content.indexOf("</body>"); //$NON-NLS-1$
		return content.substring(indexOfBody + 6, indexOfEndBody);
	}

	protected String loadGoldenContent(String name) {
		InputStream in = this.getClass().getResourceAsStream(name);
		return loadStreamContent(in);
	}

	protected String loadStreamContent(InputStream in) {
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int chr;
			while ((chr = in.read()) != -1) {
				out.write(chr);
			}
			in.close();
			return out.toString().replaceAll("[\\s|\\t]*\\n[\\s|\\t]*", ""); //$NON-NLS-1$
		} catch (Exception ex) {
			return null;
		}
	}

	public void copyResource(String src, String tgt) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(src);
		assertTrue(in != null);
		try {
			FileOutputStream fos = new FileOutputStream(tgt);
			byte[] fileData = new byte[5120];
			int readCount = -1;
			while ((readCount = in.read(fileData)) != -1) {
				fos.write(fileData, 0, readCount);
			}
			fos.close();
			in.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

	public void removeFile(File file) {
		if (file.isDirectory()) {
			File[] children = file.listFiles();
			for (int i = 0; i < children.length; i++) {
				removeFile(children[i]);
			}
		}
		if (file.exists()) {
			if (!file.delete()) {
				System.out.println(file.toString() + " can't be removed");
			}
		}
	}

	public void removeFile(String file) {
		removeFile(new File(file));
	}

	public void render(String design, IRenderOption options) throws EngineException {
		IReportDocument document = createReportDocument(design);
		IRenderTask render = engine.createRenderTask(document);
		render.setRenderOption(options);
		render.render();
		render.close();
	}

	public void runAndRender(String design, IRenderOption options) throws EngineException {
		IReportRunnable reportRunnable = engine.openReportDesign(design);
		IRunAndRenderTask runAndRenderTask = engine.createRunAndRenderTask(reportRunnable);
		runAndRenderTask.setRenderOption(options);
		runAndRenderTask.run();
		runAndRenderTask.close();
	}
}
