/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.css.engine;

public class PerfectHash {
	// bidi_hcg: Aligned with generated token.cpp after the "direction" keyword
	// was added.

	final static int TOTAL_KEYWORDS = 62;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 21;

	final static int MIN_HASH_VALUE = 10;

	final static int MAX_HASH_VALUE = 111;

	/* maximum key range = 102, duplicates = 0 */

	static int asso_values[] = { 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
			112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
			112, 112, 112, 112, 112, 112, 112, 45, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
			112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112,
			112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 112, 52, 5, 0, 0, 15, 40, 20, 0,
			40, 112, 15, 0, 20, 5, 0, 5, 112, 5, 40, 0, 112, 15, 45, 30, 36, 112, 112, 112, 112, 112, 112, 112 };

	public static int hash(String str) {
		int hashValue = str.length();
		switch (hashValue) {
		default:
			hashValue += asso_values[str.charAt(11)];
		case 11:
		case 10:
		case 9:
		case 8:
		case 7:
		case 6:
		case 5:
		case 4:
		case 3:
			hashValue += asso_values[str.charAt(2)];
		case 2:
		case 1:
			hashValue += asso_values[str.charAt(0)];
			break;
		}
		return hashValue + asso_values[str.charAt(str.length() - 1)];
	}

	static int lengthtable[] = { 5, 11, 11, 12, 13, 9, 11, 17, 16, 17, 18, 19, 10, 16, 18, 19, 11, 12, 14, 10, 11, 18,
			19, 10, 6, 12, 5, 16, 7, 11, 12, 13, 21, 17, 13, 14, 16, 21, 8, 9, 10, 16, 17, 13, 14, 16, 17, 21, 14, 16,
			7, 11, 17, 14, 6, 11, 17, 16, 14, 12, 11, 21 };
	static String wordlist[] = { "color" /* hash value = 10, index = 0 */,
			"data-format" /* hash value = 11, index = 1 */, "line-height" /* hash value = 16, index = 2 */,
			"padding-left" /* hash value = 17, index = 3 */, "padding-right" /* hash value = 18, index = 4 */,
			"direction" /* hash value = 19, index = 5 */, "padding-top" /* hash value = 21, index = 6 */,
			"background-height" /* hash value = 22, index = 7 */, "background-color" /* hash value = 26, index = 8 */,
			"background-repeat" /* hash value = 27, index = 9 */,
			"border-right-width" /* hash value = 28, index = 10 */,
			"border-bottom-width" /* hash value = 29, index = 11 */, "can-shrink" /* hash value = 30, index = 12 */,
			"border-top-color" /* hash value = 31, index = 13 */,
			"border-right-color" /* hash value = 33, index = 14 */,
			"border-bottom-color" /* hash value = 34, index = 15 */, "margin-left" /* hash value = 36, index = 16 */,
			"margin-right" /* hash value = 37, index = 17 */, "padding-bottom" /* hash value = 39, index = 18 */,
			"margin-top" /* hash value = 40, index = 19 */, "text-indent" /* hash value = 41, index = 20 */,
			"border-right-style" /* hash value = 43, index = 21 */,
			"border-bottom-style" /* hash value = 44, index = 22 */, "text-align" /* hash value = 45, index = 23 */,
			"height" /* hash value = 46, index = 24 */, "number-align" /* hash value = 47, index = 25 */,
			"width" /* hash value = 50, index = 26 */, "text-linethrough" /* hash value = 51, index = 27 */,
			"orphans" /* hash value = 52, index = 28 */, "font-weight" /* hash value = 56, index = 29 */,
			"font-variant" /* hash value = 57, index = 30 */, "margin-bottom" /* hash value = 58, index = 31 */,
			"background-position-x" /* hash value = 61, index = 32 */,
			"page-break-before" /* hash value = 62, index = 33 */, "text-overline" /* hash value = 63, index = 34 */,
			"text-transform" /* hash value = 64, index = 35 */, "background-width" /* hash value = 66, index = 36 */,
			"background-position-y" /* hash value = 67, index = 37 */, "overflow" /* hash value = 68, index = 38 */,
			"font-size" /* hash value = 69, index = 39 */, "font-style" /* hash value = 70, index = 40 */,
			"border-top-width" /* hash value = 71, index = 41 */, "border-left-width" /* hash value = 72, index = 42 */,
			"show-if-blank" /* hash value = 73, index = 43 */, "letter-spacing" /* hash value = 74, index = 44 */,
			"background-image" /* hash value = 76, index = 45 */, "border-left-color" /* hash value = 77, index = 46 */,
			"background-attachment" /* hash value = 78, index = 47 */,
			"vertical-align" /* hash value = 79, index = 48 */, "border-top-style" /* hash value = 81, index = 49 */,
			"display" /* hash value = 83, index = 50 */, "master-page" /* hash value = 86, index = 51 */,
			"border-left-style" /* hash value = 87, index = 52 */, "visible-format" /* hash value = 89, index = 53 */,
			"widows" /* hash value = 91, index = 54 */, "font-family" /* hash value = 92, index = 55 */,
			"page-break-inside" /* hash value = 97, index = 56 */, "page-break-after" /* hash value = 98, index = 57 */,
			"text-underline" /* hash value = 99, index = 58 */, "word-spacing" /* hash value = 102, index = 59 */,
			"white-space" /* hash value = 111, index = 60 */, "background-image-type" /* hash value = ??, index = 61 */
	};

	static short lookup[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, -1, -1, -1, -1, 2, 3, 4, 5, -1, 6, 7, -1,
			-1, -1, 8, 9, 10, 11, 12, 13, -1, 14, 15, -1, 16, 17, -1, 18, 19, 20, -1, 21, 22, 23, 24, 25, -1, -1, 26,
			27, 28, -1, -1, -1, 29, 30, 31, -1, -1, 32, 33, 34, 35, -1, 36, 37, 38, 39, 40, 41, 42, 43, 44, -1, 45, 46,
			47, 48, -1, 49, -1, 50, -1, -1, 51, 52, -1, 53, -1, 54, 55, -1, -1, -1, -1, 56, 57, 58, -1, -1, 59, -1, -1,
			-1, -1, -1, -1, -1, -1, 60, -1 };

	public static int in_word_set(String str) {
		int len = str.length();

		if (len <= MAX_WORD_LENGTH && len >= MIN_WORD_LENGTH) {
			int key = hash(str);

			if (key <= MAX_HASH_VALUE && key >= 0) {
				int index = lookup[key];

				if (index >= 0) {
					if (len == lengthtable[index]) {
						String s = wordlist[index];

						if (s.equals(str)) {
							return index;
						}
					}
				}

			}
		}
		return -1;
	}
}
