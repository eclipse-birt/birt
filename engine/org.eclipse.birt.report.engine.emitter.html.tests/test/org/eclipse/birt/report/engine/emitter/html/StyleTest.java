
/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

public class StyleTest extends HTMLReportEmitterTestCase {
	private static String designFile = "org/eclipse/birt/report/engine/emitter/html/styleTest.xml";

	@Override
	public String getWorkSpace() {
		// TODO Auto-generated method stub
		return "./styleTest";
	}

	public void testInlineStyle() throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setEmbeddable(true);
		options.setEnableInlineStyle(true);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		List instanceIDs = new ArrayList();
		options.setInstanceIDs(instanceIDs);
		options.setOutputStream(output);
		IRenderTask task = createRenderTask(designFile);
		task.setRenderOption(options);
		task.render();
		task.close();
		String content = new String(output.toByteArray());
		output.close();

		String regex = "<style type=\"text/css\">";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());

		regex = "<div[^<>]*style=\"[^<>]*color: rgb(255, 0, 0)[^<>]*>aaaa</div>";
		matcher = Pattern.compile(regex).matcher(content);
		assertEquals(false, matcher.find());
	}

	public void testCSSStyleClass() throws EngineException, IOException {
		HTMLRenderOption options = new HTMLRenderOption();
		options.setEmbeddable(true);
		options.setEnableInlineStyle(false);

		ByteArrayOutputStream output = new ByteArrayOutputStream();
		List instanceIDs = new ArrayList();
		options.setInstanceIDs(instanceIDs);
		options.setOutputStream(output);
		IRenderTask task = createRenderTask(designFile);
		task.setRenderOption(options);
		task.render();
		task.close();
		String content = new String(output.toByteArray());
		output.close();

		String regex = "<style type=\"text/css\">";
		Matcher matcher = Pattern.compile(regex).matcher(content);
		assertEquals(true, matcher.find());

		regex = "<div[^<>]*class=\"[^<>]*CustomerStyle[^<>]*>aaaa</div>";
		matcher = Pattern.compile(regex).matcher(content);
		assertEquals(false, matcher.find());

		regex = "<div[^<>]*style=\"[^<>]*color: rgb(255, 0, 0)[^<>]*>aaaa</div>";
		matcher = Pattern.compile(regex).matcher(content);
		assertEquals(false, matcher.find());
	}

}
