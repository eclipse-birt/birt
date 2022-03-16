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

public class XMLEncodeUtilTest extends TestCase {

	public void testEncodeText() {

		String result = XMLEncodeUtil.encodeAttr("\u0000A\"&<\uD840\uDc00\r\n\t");
		assertEquals(result, "A&#34;&amp;&lt;\uD840\uDC00&#13;&#10;&#9;");

		result = XMLEncodeUtil.encodeAttr("ABCD\u3400 CDEF");
		assertEquals("ABCD\u3400 CDEF", result);

		result = XMLEncodeUtil.encodeCdata("\u0000A\"&<\uD840\uDc00\r\n\t");
		assertEquals("A\"&<\uD840\uDc00\r\n\t", result);

		result = XMLEncodeUtil.encodeText("\u0000A\"&<\uD840\uDc00\r\n\t");
		assertEquals("A\"&amp;&lt;\uD840\uDc00\r\n\t", result);
	}
}
