/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import junit.framework.TestCase;

/**
 * 
 */
public class ReportRunnerTest extends TestCase {

	String workspaceFolder;

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		// This test is intentionaly made invalid
		// If running this test, we must first deploy birt here
		workspaceFolder = "./TempWorkspace" + String.valueOf(new Date().getTime()) + "/";

		new File(workspaceFolder).mkdirs();
		copyResource("test.xml", workspaceFolder + "test.xml");
		copyResource("test.jpg", workspaceFolder + "test.jpg");
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		removeFile(new File(workspaceFolder));
	}

	/*
	 * This test case is destined to fail, because this test case needs birt be
	 * deployed. To may this test case success, we must first deploy birt in setUp.
	 */
	public void testExecution() {
		/*
		 * String[] args = new String[] { "--parameter", "param1=./workspace/test.xml",
		 * "./workspace/test.xml" }; ReportRunner.main(args);
		 * assertFileExist("./workspace/test.html");
		 */
		// assertFileExist("./workspace/test.xml_fo/report.fo");
		// assertFileExist("./workspace/test.xml_pdf/report.pdf");
	}

	protected void assertFileExist(String fileName) {
		File file = new File(fileName);

		assert file.exists();
		assert file.length() != 0;
	}

	// TODO add command line parser please.
	public void testCommandlineParser() {
	}

	protected void removeFile(File file) {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			for (int i = 0; i < files.length; i++) {
				removeFile(files[i]);
			}
		}
		file.delete();
	}

	protected void copyResource(String src, String target) {
		try {
			InputStream in = getClass().getResourceAsStream(src);
			byte[] buffer = new byte[in.available()];
			in.read(buffer);
			in.close();
			OutputStream out = new FileOutputStream(target);
			out.write(buffer);
			out.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			fail();
		}
	}

}
