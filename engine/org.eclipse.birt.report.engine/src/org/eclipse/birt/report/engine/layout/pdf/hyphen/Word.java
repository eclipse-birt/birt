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
 * Henning von Bargen - Added at least a bit of JavaDoc, added SOFT HYPHEN support.
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.hyphen;

/**
 * <p>
 * Despite its name, this describes a <em>fragment</em> of a word of text.
 * </p>
 * <p>
 * If the word does not contain possible hyphenation / line-breaking points,
 * then it is a whole word. But if the word contains Unicode MINUS or HYPHEN or
 * SOFT HYPHEN symbols, then the {@link BreakIterator} splits this whole word
 * into more than one Word instances.
 * </p>
 * <p>
 * For example, "extra-ordinary" will be split into two Word instances "extra-"
 * and "ordinary".
 * </p>
 */
public class Word {
	protected int start;
	protected int end;
	protected String text;

	/**
	 * Create a Word instance as a substring of a given text.
	 *
	 * @see String#substring(int,int)
	 *
	 * @param text  Text
	 * @param start start index of the substring
	 * @param end   end index of the substring (exclusive).
	 */
	public Word(String text, int start, int end) {
		this.text = text;
		this.start = start;
		this.end = end;
	}

	public int getStart() {
		return this.start;
	}

	public int getEnd() {
		return this.end;
	}

	public String getValue() {
		return text.substring(start, end);
	}

	public int getLength() {
		return end - start;
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("value: [" + getValue() + "]"); //$NON-NLS-1$ //$NON-NLS-2$
		str.append(" StartIndex:" + start); //$NON-NLS-1$
		str.append(" EndIndex:" + end); //$NON-NLS-1$
		return str.toString();
	}
}
