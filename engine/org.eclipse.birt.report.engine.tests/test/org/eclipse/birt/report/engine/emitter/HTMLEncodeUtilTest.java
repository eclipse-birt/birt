/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

import junit.framework.TestCase;

public class HTMLEncodeUtilTest extends TestCase {

	public void testEncodeText() {

		String result = HTMLEncodeUtil.encodeText("\u0000A\"&<\uD840\uDc00 \r\n\t");
		assertEquals("A\"&amp;&lt;\uD840\uDc00 \r\n\t", result);

		result = HTMLEncodeUtil.encodeText("\u0000A\"&<\uD840\uDc00 \r\n\t", true);
		assertEquals("A\"&amp;&lt;\uD840\uDc00&#xa0;<br/>&#xa0;", result);

		result = HTMLEncodeUtil.encodeText(" \r\r\n\n\r\t\t  ", true);
		assertEquals("&#xa0;<br/><br/><br/><br/>&#xa0;&#xa0;&#xa0;&#xa0;", result);

		result = HTMLEncodeUtil.encodeText(" ", true);
		assertEquals("&#xa0;", result);

		result = HTMLEncodeUtil.encodeText(" \n ", true);
		assertEquals("&#xa0;<br/>&#xa0;", result);

		result = HTMLEncodeUtil.encodeText("a\n \n", true);
		assertEquals("a<br/>&#xa0;<br/>", result);

		result = HTMLEncodeUtil.encodeText("\n a", true);
		assertEquals("<br/>&#xa0;a", result);

		result = HTMLEncodeUtil.encodeText("\n  \n", true);
		assertEquals("<br/>&#xa0;&#xa0;<br/>", result);

		result = HTMLEncodeUtil.encodeText("\n   a", true);
		assertEquals("<br/>&#xa0;&#xa0; a", result);

		result = HTMLEncodeUtil.encodeText("\n   ", true);
		assertEquals("<br/>&#xa0;&#xa0;&#xa0;", result);

		result = HTMLEncodeUtil.encodeText("\n", true);
		assertEquals("<br/>", result);

	}
}
