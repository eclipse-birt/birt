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
package org.eclipse.birt.report.engine.layout.pdf.hyphen;

import junit.framework.TestCase;

public class DefaultWordRecognizerTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/*
	 * Test method for
	 * 'org.eclipse.birt.report.engine.layout.hyphen.DefaultWordRecognizer.
	 * getLastWordEnd()' Test method for
	 * 'org.eclipse.birt.report.engine.layout.hyphen.DefaultWordRecognizer.
	 * getNextWord()'
	 */
	public void testWordRecognize() {
		String str = " simple \n test ";
		DefaultWordRecognizer wr = new DefaultWordRecognizer(str);
		Word word = wr.getNextWord();
		assertTrue(" ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("simple ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue(" ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("test ".equals(word.getValue()));

		str = "simple\ntest";
		wr = new DefaultWordRecognizer(str);
		word = wr.getNextWord();
		assertTrue("simple".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("test".equals(word.getValue()));

		str = "simple\n\n\n";
		wr = new DefaultWordRecognizer(str);
		word = wr.getNextWord();
		assertTrue("simple".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));

		str = "first\nsecond\r\nthird\rfourth\r";
		wr = new DefaultWordRecognizer(str);
		word = wr.getNextWord();
		assertTrue("first".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("second".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\r\n".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("third".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\r".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("fourth".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\r".equals(word.getValue()));

	}

}
