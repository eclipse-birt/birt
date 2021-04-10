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

package org.eclipse.birt.report.engine.layout.pdf.hyphen;

/**
 * This class represents a hyphenated word.
 *
 */
public class Hyphenation {

	private int[] hyphenPoints;
	private String word;

	/**
	 * number of hyphenation points in word
	 */
	private int len;

	/**
	 * rawWord as made of alternating strings and {@link Hyphen Hyphen} instances
	 */
	Hyphenation(String word, int[] points) {
		this.word = word;
		hyphenPoints = points;
		len = points.length;
	}

	/**
	 * @return the number of hyphenation points in the word
	 */
	public int length() {
		return len;
	}

	/**
	 * @return the pre-break text, not including the hyphen character
	 */
	public String getPreHyphenText(int index) {
		return word.substring(0, hyphenPoints[index]);
	}

	public String getHyphenText(int startIndex, int endIndex) {
		return word.substring(hyphenPoints[startIndex], hyphenPoints[endIndex]);
	}

	/**
	 * @return the post-break text
	 */
	public String getPostHyphenText(int index) {
		return word.substring(hyphenPoints[index]);
	}

	/**
	 * @return the hyphenation points
	 */
	public int[] getHyphenationPoints() {
		return hyphenPoints;
	}

	public String toString() {
		StringBuffer str = new StringBuffer();
		int start = 0;
		for (int i = 0; i < len; i++) {
			str.append(word.substring(start, hyphenPoints[i]) + "-"); //$NON-NLS-1$
			start = hyphenPoints[i];
		}
		str.append(word.substring(start));
		return str.toString();
	}

}
