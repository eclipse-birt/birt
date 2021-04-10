/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.impl;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.EngineCase;

/**
 *
 */

public class ImageScaleTest extends EngineCase {

	String designFile = "org/eclipse/birt/report/engine/api/impl/image_scale.xml";

	public void testHtmlImageScale() throws Exception {
		String result = render(designFile);
		Pattern pattern = Pattern.compile("<img .*?style=\".*?width\\: (.*?);.*?height\\: (.*?);.*?\"",
				Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(result);

		String[][] expected = new String[][] { { "0.969in", "2.208in" }, { "0.969in", "0.463in" },
				{ "2.083in", "1.604in" }, { "1.353in", "1.604in" }, { "0.833in", "2.469in" },
				{ "0.833in", "0.987in" } };
		int start = 0;
		int index = 0;
		while (matcher.find(start)) {
			String w = matcher.group(1);
			String h = matcher.group(2);
			assertEquals(expected[index][0], w);
			assertEquals(expected[index][1], h);
			index++;
			start = matcher.end();
		}
	}

}
