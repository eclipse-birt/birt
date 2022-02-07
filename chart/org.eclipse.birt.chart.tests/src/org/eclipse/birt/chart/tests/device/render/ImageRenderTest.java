/***********************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.tests.device.render;

import java.io.File;
import java.util.Properties;

import org.eclipse.birt.chart.api.ChartEngine;
import org.eclipse.birt.core.framework.PlatformConfig;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * The test suite for core
 */

public class ImageRenderTest {
	protected static final String OUTDIR = "output";//$NON-NLS-1$
	protected static final String INDIR = "input";//$NON-NLS-1$
	protected static final String CONTROLDIR = "golden";//$NON-NLS-1$
	protected static final String DRAWEXT = ".drw";//$NON-NLS-1$
	protected String fixedDir;
	protected String workspaceDir;
	protected String type;
	protected TestSuite suite;

	public ImageRenderTest(TestSuite suite) {
		this.suite = suite;
	}

	protected void processDir(File dir, String dirName) throws Exception {
		File[] files = dir.listFiles();

		for (int x = 0; x < files.length; x++) {
			File file = files[x];
			if (file.isDirectory()) {
				File outFileDir = new File(
						workspaceDir + File.separator + OUTDIR + dirName + File.separator + file.getName());
				outFileDir.mkdirs();
				processDir(file, dirName + File.separator + file.getName());
			} else if (file.getName().endsWith(DRAWEXT)) {
				String fileName = file.getName();
				fileName = fileName.substring(0, fileName.length() - (DRAWEXT.length()));
				suite.addTest(new ImageOutputBaseTest(file, dirName, fileName, workspaceDir));
			}
		}
	}

	public void process() throws Exception {
		Properties p = System.getProperties();
		// This is a standlone test
		PlatformConfig config = new PlatformConfig();
		// config.setProperty( "STANDALONE", "true" ); //$NON-NLS-1$ //$NON-NLS-2$
		ChartEngine.instance(config);
		fixedDir = File.separator + "src" + File.separator + "org"//$NON-NLS-1$ //$NON-NLS-2$
				+ File.separator + "eclipse" + File.separator + "birt" + File.separator + "chart"//$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				+ File.separator + "tests" + File.separator + "device";//$NON-NLS-1$ //$NON-NLS-2$
		workspaceDir = (String) p.get("user.dir") + fixedDir;//$NON-NLS-1$
		File inputDir = new File(workspaceDir + File.separator + INDIR);
		processDir(inputDir, "");//$NON-NLS-1$
	}

	public static Test suite() {
		TestSuite suite = new TestSuite("ImageRenderTest: Test for render devices");//$NON-NLS-1$
		ImageRenderTest processSuite = new ImageRenderTest(suite);
		try {
			processSuite.process();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return suite;
	}
}
