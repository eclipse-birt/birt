/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.layout.pdf.hyphen;

public class DefaultWordRecognizer implements IWordRecognizer {

	final static char SPACE = ' ';

	protected int start;

	protected String text;

	protected char splitChar;

	protected Word lastWord = null;

	protected Word currentWord = null;

	public DefaultWordRecognizer(String text) {
		this.text = text;

	}

	public int getLastWordEnd() {
		return lastWord == null ? 0 : lastWord.getEnd();
	}

	public boolean hasWord() {
		return getLastWordEnd() != text.length();
	}

	public Word getNextWord() {
		lastWord = currentWord;
		if (start > text.length() - 1) {
			return null;
		}

		for (int i = start; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c == SPACE) {
				currentWord = new Word(text, start, i + 1);
				start = i + 1;
				return currentWord;
			} else {
				int lineBreakLength = getLineBreakLength(text, i);
				if (lineBreakLength == 0)
					continue;
				if (i == start) {
					currentWord = new Word(text, start, i + lineBreakLength);
					start = i + lineBreakLength;
					return currentWord;
				} else {
					currentWord = new Word(text, start, i);
					start = i;
					return currentWord;
				}
			}
		}
		currentWord = new Word(text, start, text.length());
		start = text.length();

		return currentWord;

	}

	private int getLineBreakLength(String text, int index) {
		char c = text.charAt(index);
		if (c == '\n') {
			return 1;
		}
		if (c == '\r') {
			if (index + 1 < text.length() && text.charAt(index + 1) == '\n') {
				return 2;
			} else {
				return 1;
			}
		}
		return 0;
	}
}
