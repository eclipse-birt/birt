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

package org.eclipse.birt.report.engine.regression;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;

/**
 * test the supress duplication
 *
 */
public class Test_90378 extends EngineCase {

	public void test90378() throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IRenderOption option = new HTMLRenderOption();
		option.setOutputFormat("html");
		option.setOutputStream(out);
		render("org/eclipse/birt/report/engine/regression/90378.rptdesign", option);
		String report = out.toString();

		Pattern pattern = Pattern.compile("<div>b1</div>");
		Matcher matcher = pattern.matcher(report);
		int matches = 0;
		while (matcher.find()) {
			matches++;
		}
		assertEquals(2, matches);
	}

}
