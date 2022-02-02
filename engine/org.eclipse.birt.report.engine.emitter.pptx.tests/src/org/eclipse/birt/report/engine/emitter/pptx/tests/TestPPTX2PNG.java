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

package org.eclipse.birt.report.engine.emitter.pptx.tests;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Test;

public class TestPPTX2PNG {

	@Test
	public void testText() throws Exception {
		runDesign("text.rptdesign");
	}

	@Test
	public void testTable() throws Exception {
		runDesign("table.rptdesign");
	}

	public void testMasterSlider() throws Exception {
		runDesign("master-page.rptdesign");
	}

	protected void runDesign(String name) throws Exception {
		String resource = "org/eclipse/birt/report/engine/emitter/pptx/tests/designs/pptx_project/Report Designs/"
				+ name;
		String output = "./utest/" + name;
		String input = output + "/report.rptdesign";
		copyResource(resource, input);
		new DesignToPNG().convert(input, output);
	}

	private void copyResource(String resource, String fileName) throws IOException {
		InputStream in = DesignToPNG.class.getClassLoader().getResourceAsStream(resource);
		if (in == null) {
			throw new IOException();
		}
		new File(fileName).getParentFile().mkdirs();
		BufferedInputStream bi = new BufferedInputStream(in);
		FileOutputStream fi = new FileOutputStream(fileName);
		try {
			byte[] buffer = new byte[4096];
			int size = bi.read(buffer);
			while (size > 0) {
				fi.write(buffer, 0, size);
				size = bi.read(buffer);
			}
		} finally {
			fi.close();
			bi.close();
			in.close();
		}

	}

}
