
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
package org.eclipse.birt.report.engine.emitter.html;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;

/**
 * 
 */

public class HTMLEmitterOptimizeTest extends HTMLReportEmitterTestCase {
	private static String designFile = "org/eclipse/birt/report/engine/emitter/html/TableTextDecoration.xml";

	public String getWorkSpace() {
		// TODO Auto-generated method stub
		return "./htmlEmitterOptimizeTest";
	}

	public void testPerformanceOptimize() throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setEnableAgentStyleEngine(true);
		options.setEmbeddable(true);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		List instanceIDs = new ArrayList();
		options.setInstanceIDs(instanceIDs);
		options.setOutputStream(output);
		// options.setEnableMetadata( true );
		IRenderTask task = createRenderTask(designFile);
		task.setRenderOption(options);
		task.render();
		task.close();
		String content = new String(output.toByteArray());
		output.close();

		String regex = "text-decoration: underline;";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());

		regex = "<div style=\" text-decoration: underline;\">";
		matcher = Pattern.compile(regex).matcher(content);
		assertEquals(false, matcher.find());
	}

	public void testVisionOptimize() throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setEnableAgentStyleEngine(false);
		options.setEmbeddable(true);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		List instanceIDs = new ArrayList();
		options.setInstanceIDs(instanceIDs);
		options.setOutputStream(output);
		// options.setEnableMetadata( true );
		IRenderTask task = createRenderTask(designFile);
		task.setRenderOption(options);
		task.render();
		task.close();
		String content = new String(output.toByteArray());
		output.close();

		String regex = "<div style=\" text-decoration: underline;";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());

	}
}
