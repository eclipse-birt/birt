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
package org.eclipse.birt.report.engine.layout.pdf;

import org.eclipse.birt.report.engine.layout.pdf.hyphen.Word;

import junit.framework.TestCase;

public class WordRecognizerWrapperTest extends TestCase {
	final String testStr = " This is a test! 中国 ￥2000 $3446 \"Apple\" ";
	final String testStr2 = "(";
	final String testStr3 = ")";

	public void test() {
		WordRecognizerWrapper wr = new WordRecognizerWrapper(testStr, java.util.Locale.ENGLISH);
//		Word current = null;
//		while (( current = wr.getNextWord( ))!= null)
//		{
//			System.out.println("|" + current.getValue( ) + "|");
//		}

		Word word = wr.getNextWord();
		assertTrue(" ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("This ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("is ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("a ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("test! ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("中".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("国 ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("￥2000 ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("$3446 ".equals(word.getValue()));
		word = wr.getNextWord();
		assertTrue("\"Apple\" ".equals(word.getValue()));

		wr = new WordRecognizerWrapper(testStr2, java.util.Locale.ENGLISH);
		word = wr.getNextWord();
		assertTrue("(".equals(word.getValue()));
		wr = new WordRecognizerWrapper(testStr3, java.util.Locale.ENGLISH);
		word = wr.getNextWord();
		assertTrue(")".equals(word.getValue()));
	}
}
