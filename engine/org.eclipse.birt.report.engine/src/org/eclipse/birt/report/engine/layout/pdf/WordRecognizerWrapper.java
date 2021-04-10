/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Locale;

import org.eclipse.birt.report.engine.layout.pdf.hyphen.IWordRecognizer;
import org.eclipse.birt.report.engine.layout.pdf.hyphen.Word;

import com.ibm.icu.text.BreakIterator;

public class WordRecognizerWrapper implements IWordRecognizer {
	private String text;
	private BreakIterator breakIterator = null;

	private int start = 0;
	private int end = 0;

	public WordRecognizerWrapper(String text, Locale locale) {
		this.text = text;
		breakIterator = BreakIterator.getLineInstance(locale);
		breakIterator.setText(text);
	}

	public boolean hasWord() {
		return end != BreakIterator.DONE && end < text.length();
	}

	public Word getNextWord() {
		start = end;
		end = breakIterator.next();
		if (end != BreakIterator.DONE) {
			return new Word(text, start, end);
		}
		return null;
	}

}
