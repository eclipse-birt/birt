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

/**
 *
 * Perfect hash class
 *
 * @since 3.3
 *
 */
public class PerfectHash {
	// bidi_hcg: Aligned with generated token.cpp after the "direction" keyword
	// was added.

	final static int TOTAL_KEYWORDS = 70;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 26;

	final static int MIN_HASH_VALUE = 11;

	final static int MAX_HASH_VALUE = 174;

	/* maximum key range = 164, duplicates = 0 */

	static int asso_values[] = { 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175,
			175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175,
			175, 175, 175, 175, 175, 175, 175, 75, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175,
			175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175,
			175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 175, 105, 0, 30, 5, 20, 55, 10,
			5, 50, 175, 0, 100, 0, 20, 0, 10, 175, 0, 60, 0, 175, 0, 30, 45, 65, 175, 175, 175, 175, 175, 175 };

	/**
	 * Get the hash value
	 *
	 * @param str string to create the hash value
	 * @return Return the hash value
	 */
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

	static int lengthtable[] = { 11, 12, 13, 11, 18, 19, 10, 21, 22, 18, 19, 21, 12, 14, 25, 26, 13, 9, 25, 11, 18, 19,
			21, 5, 16, 17, 25, 16, 17, 11, 17, 8, 14, 10, 6, 12, 16, 12, 14, 10, 16, 7, 16, 14, 11, 12, 11, 17, 13, 16,
			17, 13, 6, 9, 10, 21, 17, 11, 17, 16, 21, 21, 14, 11, 5, 7, 16, 11, 21, 14 };

	static String wordlist[] = { "margin-left" /* hash value = 11, index = 0 */,
			"margin-right" /* hash value = 12, index = 1 */, "margin-bottom" /* hash value = 13, index = 2 */,
			"data-format" /* hash value = 16, index = 3 */, "border-right-color" /* hash value = 18, index = 4 */,
			"border-bottom-color" /* hash value = 19, index = 5 */, "margin-top" /* hash value = 20, index = 6 */,
			"border-diagonal-color" /* hash value = 21, index = 7 */,
			"border-diagonal-number" /* hash value = 22, index = 8 */,
			"border-right-width" /* hash value = 23, index = 9 */,
			"border-bottom-width" /* hash value = 24, index = 10 */,
			"border-diagonal-width" /* hash value = 26, index = 11 */, "padding-left" /* hash value = 27, index = 12 */,
			"padding-bottom" /* hash value = 29, index = 13 */,
			"border-antidiagonal-color" /* hash value = 30, index = 14 */,
			"border-antidiagonal-number" /* hash value = 31, index = 15 */,
			"padding-right" /* hash value = 33, index = 16 */, "direction" /* hash value = 34, index = 17 */,
			"border-antidiagonal-width" /* hash value = 35, index = 18 */,
			"padding-top" /* hash value = 36, index = 19 */, "border-right-style" /* hash value = 38, index = 20 */,
			"border-bottom-style" /* hash value = 39, index = 21 */,
			"border-diagonal-style" /* hash value = 41, index = 22 */, "width" /* hash value = 45, index = 23 */,
			"border-top-color" /* hash value = 46, index = 24 */, "background-repeat" /* hash value = 47, index = 25 */,
			"border-antidiagonal-style" /* hash value = 50, index = 26 */,
			"border-top-width" /* hash value = 51, index = 27 */, "background-height" /* hash value = 52, index = 28 */,
			"text-indent" /* hash value = 56, index = 29 */, "page-break-before" /* hash value = 57, index = 30 */,
			"overflow" /* hash value = 58, index = 31 */, "text-transform" /* hash value = 59, index = 32 */,
			"can-shrink" /* hash value = 60, index = 33 */, "height" /* hash value = 61, index = 34 */,
			"word-spacing" /* hash value = 62, index = 35 */, "text-linethrough" /* hash value = 66, index = 36 */,
			"number-align" /* hash value = 72, index = 37 */, "visible-format" /* hash value = 74, index = 38 */,
			"text-align" /* hash value = 75, index = 39 */, "background-color" /* hash value = 76, index = 40 */,
			"orphans" /* hash value = 77, index = 41 */, "background-width" /* hash value = 81, index = 42 */,
			"vertical-align" /* hash value = 84, index = 43 */, "font-weight" /* hash value = 86, index = 44 */,
			"font-variant" /* hash value = 87, index = 45 */, "master-page" /* hash value = 91, index = 46 */,
			"border-left-color" /* hash value = 92, index = 47 */, "show-if-blank" /* hash value = 93, index = 48 */,
			"border-top-style" /* hash value = 96, index = 49 */, "border-left-width" /* hash value = 97, index = 50 */,
			"text-overline" /* hash value = 98, index = 51 */, "widows" /* hash value = 101, index = 52 */,
			"font-size" /* hash value = 104, index = 53 */, "font-style" /* hash value = 105, index = 54 */,
			"background-position-x" /* hash value = 106, index = 55 */,
			"page-break-inside" /* hash value = 107, index = 56 */, "white-space" /* hash value = 111, index = 57 */,
			"border-left-style" /* hash value = 112, index = 58 */,
			"background-image" /* hash value = 116, index = 59 */,
			"background-image-type" /* hash value = 121, index = 60 */,
			"background-position-y" /* hash value = 126, index = 61 */,
			"text-underline" /* hash value = 129, index = 62 */, "line-height" /* hash value = 131, index = 63 */,
			"color" /* hash value = 135, index = 64 */, "display" /* hash value = 137, index = 65 */,
			"page-break-after" /* hash value = 141, index = 66 */, "font-family" /* hash value = 151, index = 67 */,
			"background-attachment" /* hash value = 156, index = 68 */,
			"letter-spacing" /* hash value = 174, index = 69 */
	};

	/** property: lookup array */
	public static int lookup[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, -1, -1, 3, -1, 4, 5, 6, 7, 8, 9,
			10, -1, 11, 12, -1, 13, 14, 15, -1, 16, 17, 18, 19, -1, 20, 21, -1, 22, -1, -1, -1, 23, 24, 25, -1, -1, 26,
			27, 28, -1, -1, -1, 29, 30, 31, 32, 33, 34, 35, -1, -1, -1, 36, -1, -1, -1, -1, -1, 37, -1, 38, 39, 40, 41,
			-1, -1, -1, 42, -1, -1, 43, -1, 44, 45, -1, -1, -1, 46, 47, 48, -1, -1, 49, 50, 51, -1, -1, 52, -1, -1, 53,
			54, 55, 56, -1, -1, -1, 57, 58, -1, -1, -1, 59, -1, -1, -1, -1, 60, -1, -1, -1, -1, 61, -1, -1, 62, -1, 63,
			-1, -1, -1, 64, -1, 65, -1, -1, -1, 66, -1, -1, -1, -1, -1, -1, -1, -1, -1, 67, -1, -1, -1, -1, 68, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 69

	};

	/**
	 * Get the lookup index
	 *
	 * @param str string to get the lookup index
	 * @return Return the lookup index
	 */
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
