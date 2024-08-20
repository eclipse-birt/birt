/*******************************************************************************
 * Copyright (c) 2004, 2023, 2024 Actuate Corporation and others
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

	final static int TOTAL_KEYWORDS = 71;

	final static int MIN_WORD_LENGTH = 5;

	final static int MAX_WORD_LENGTH = 26;

	final static int MIN_HASH_VALUE = 11;

	final static int MAX_HASH_VALUE = 174;

	/* maximum key range = 164, duplicates = 0 */

	static int asso_values[] = { 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170,
			170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170,
			170, 170, 170, 170, 170, 170, 170, 95, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170,
			170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170,
			170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 170, 100, 0, 30, 5, 20, 60, 10,
			5, 45, 170, 5, 100, 0, 20, 0, 10, 170, 0, 50, 0, 170, 0, 45, 40, 65, 170, 170, 170, 170, 170, 170 };

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
			21, 16, 17, 25, 11, 17, 14, 6, 17, 5, 16, 14, 10, 16, 7, 10, 12, 8, 15, 16, 12, 14, 11, 16, 13, 11, 12, 13,
			16, 21, 17, 6, 9, 10, 16, 17, 21, 17, 14, 11, 21, 7, 11, 17, 5, 16, 21, 11, 14 };

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
			"border-diagonal-style" /* hash value = 41, index = 22 */,
			"border-top-color" /* hash value = 46, index = 23 */, "background-repeat" /* hash value = 47, index = 24 */,
			"border-antidiagonal-style" /* hash value = 50, index = 25 */,
			"text-indent" /* hash value = 51, index = 26 */, "background-height" /* hash value = 52, index = 27 */,
			"text-transform" /* hash value = 54, index = 28 */, "height" /* hash value = 56, index = 29 */,
			"page-break-before" /* hash value = 57, index = 30 */, "width" /* hash value = 60, index = 31 */,
			"text-linethrough" /* hash value = 61, index = 32 */, "visible-format" /* hash value = 64, index = 33 */,
			"can-shrink" /* hash value = 65, index = 34 */, "border-top-width" /* hash value = 66, index = 35 */,
			"orphans" /* hash value = 67, index = 36 */, "text-align" /* hash value = 70, index = 37 */,
			"number-align" /* hash value = 72, index = 38 */, "overflow" /* hash value = 73, index = 39 */,
			"text-decoration" /* hash value = 75, index = 40 */, "background-color" /* hash value = 76, index = 41 */,
			"word-spacing" /* hash value = 77, index = 42 */, "vertical-align" /* hash value = 79, index = 43 */,
			"master-page" /* hash value = 81, index = 44 */, "border-top-style" /* hash value = 86, index = 45 */,
			"show-if-blank" /* hash value = 88, index = 46 */, "font-weight" /* hash value = 91, index = 47 */,
			"font-variant" /* hash value = 92, index = 48 */, "text-overline" /* hash value = 93, index = 49 */,
			"background-width" /* hash value = 96, index = 50 */,
			"background-position-x" /* hash value = 101, index = 51 */,
			"page-break-inside" /* hash value = 102, index = 52 */, "widows" /* hash value = 106, index = 53 */,
			"font-size" /* hash value = 109, index = 54 */, "font-style" /* hash value = 110, index = 55 */,
			"background-image" /* hash value = 111, index = 56 */,
			"border-left-color" /* hash value = 112, index = 57 */,
			"background-image-type" /* hash value = 116, index = 58 */,
			"border-left-width" /* hash value = 117, index = 59 */, "text-underline" /* hash value = 119, index = 60 */,
			"white-space" /* hash value = 121, index = 61 */,
			"background-position-y" /* hash value = 126, index = 62 */, "display" /* hash value = 127, index = 63 */,
			"line-height" /* hash value = 131, index = 64 */, "border-left-style" /* hash value = 132, index = 65 */,
			"color" /* hash value = 135, index = 66 */, "page-break-after" /* hash value = 136, index = 67 */,
			"background-attachment" /* hash value = 151, index = 68 */,
			"font-family" /* hash value = 156, index = 69 */, "letter-spacing" /* hash value = 169, index = 70 */
	};

	/** property: lookup array */
	public static int lookup[] = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 0, 1, 2, -1, -1, 3, -1, 4, 5, 6, 7, 8, 9,
			10, -1, 11, 12, -1, 13, 14, 15, -1, 16, 17, 18, 19, -1, 20, 21, -1, 22, -1, -1, -1, -1, 23, 24, -1, -1, 25,
			26, 27, -1, 28, -1, 29, 30, -1, -1, 31, 32, -1, -1, 33, 34, 35, 36, -1, -1, 37, -1, 38, 39, -1, 40, 41, 42,
			-1, 43, -1, 44, -1, -1, -1, -1, 45, -1, 46, -1, -1, 47, 48, 49, -1, -1, 50, -1, -1, -1, -1, 51, 52, -1, -1,
			-1, 53, -1, -1, 54, 55, 56, 57, -1, -1, -1, 58, 59, -1, 60, -1, 61, -1, -1, -1, -1, 62, 63, -1, -1, -1, 64,
			65, -1, -1, 66, 67, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 68, -1, -1, -1, -1, 69, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, 70 };
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
