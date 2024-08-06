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

package org.eclipse.birt.report.engine.layout.pdf.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Bullet frame
 *
 * @since 3.3
 *
 */
public class BulletFrame {

	// list style string
	/** property: list style type, disc */
	public static String CSS_LISTSTYLETYPE_DISC_VALUE = "disc";
	/** property: list style type, circle */
	public static String CSS_LISTSTYLETYPE_CIRCLE_VALUE = "circle";
	/** property: list style type, square */
	public static String CSS_LISTSTYLETYPE_SQUARE_VALUE = "square";
	/** property: list style type, decimal with leading zero */
	public static String CSS_LISTSTYLETYPE_DECIMALLEADINGZERO_VALUE = "decimal-leading-zero";
	/** property: list style type, lower Roman */
	public static String CSS_LISTSTYLETYPE_LOWERROMAN_VALUE = "lower-roman";
	/** property: list style type, upper Roman */
	public static String CSS_LISTSTYLETYPE_UPPERROMAN_VALUE = "upper-roman";
	/** property: list style type, lower Greek */
	public static String CSS_LISTSTYLETYPE_LOWERGREEK_VALUE = "lower-greek";
	/** property: list style type, lower Latin */
	public static String CSS_LISTSTYLETYPE_LOWERLATINC_VALUE = "lower-latin";
	/** property: list style type, upper Latin */
	public static String CSS_LISTSTYLETYPE_UPPERLATIN_VALUE = "upper-latin";
	/** property: list style type, Armenian */
	public static String CSS_LISTSTYLETYPE_ARMENIAN_VALUE = "armenian";
	/** property: list style type, Georgian */
	public static String CSS_LISTSTYLETYPE_GEORGIAN_VALUE = "georgian";
	/** property: list style type, lower alpha */
	public static String CSS_LISTSTYLETYPE_LOWERALPHA_VALUE = "lower-alpha";
	/** property: list style type, upper alpha */
	public static String CSS_LISTSTYLETYPE_UPPERALPHA_VALUE = "upper-alpha";
	/** property: list style type, no style */
	public static String CSS_LISTSTYLETYPE_NONE_VALUE = "none";
	/** property: list style type, inherited */
	public static String CSS_LISTSTYLETYPE_INHERIT_VALUE = "inherit";
	/** property: list style type, decimal */
	public static String CSS_LISTSTYLETYPE_DECIMAL = "decimal";

	// list style type:
	/** property: list style type, numeric, disc */
	public static final int LIST_DISC_VALUE = 0;
	/** property: list style type, numeric, circle */
	public static final int LIST_STYLE_CIRCLE = 1;
	/** property: list style type, numeric, square */
	public static final int LIST_STYLE_SQUARE = 2;
	/** property: list style type, numeric, decimal with leading zero */
	public static final int LIST_STYLE_DECIMALLEADINGZERO = 3;
	/** property: list style type, numeric, lower Roman */
	public static final int LIST_STYLE_LOWERROMAN = 4;
	/** property: list style type, numeric, upper Roman */
	public static final int LIST_STYLE_UPPERROMAN = 5;
	/** property: list style type, numeric, lower Greek */
	public static final int LIST_STYLE_LOWERGREEK = 6;
	/** property: list style type, numeric, upper Latin */
	public static final int LIST_STYLE_UPPERLATIN = 7;
	/** property: list style type, numeric, upper Latin */
	public static final int LIST_STYLE_LOWERLATIN = 8;
	/** property: list style type, numeric, Armenian */
	public static final int LIST_STYLE_ARMENIAN = 9;
	/** property: list style type, numeric, Georgian */
	public static final int LIST_STYLE_GEORGIAN = 10;
	/** property: list style type, numeric, lower alpha */
	public static final int LIST_STYLE_LOWERALPHA = 11;
	/** property: list style type, numeric, upper alpha */
	public static final int LIST_STYLE_UPPERALPHA = 12;
	/** property: list style type, numeric, no style */
	public static final int LIST_STYLE_NONE = 13;
	/** property: list style type, numeric, inherited */
	public static final int LIST_STYLE_INHERIT = 14;
	/** property: list style type, numeric, decimal */
	public static final int LIST_STYLE_DECIMAL = 15;

	/** property: mapping of types, string to numeric */
	public static final Map<String, Integer> TYPES = new HashMap<>();// mapping
																		// the
																		// types
	/** property: empty string */
	public static final String EMPTYSTRING = "";

	/** property: disc sign */
	public static final char DISC_CHAR = 0x2022;

	/** property: circle sign */
	public static final char CIRCLE_CHAR = 0x25E6;

	/** property: square sign */
	public static final char SQUARE_CHAR = 0x25AA;

	/** property: zero char */
	public static final char ZERO_CHAR = '0';

	// char table
	// ALPHA table
	/** property: alpha size */
	public static final int ALPHA_SIZE = 26;

	/** property: lower alpha chars */
	public static final char LOWER_ALPHA_CHARS[] = { 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, // a
			// b
			// c
			// d
			// e
			0x0066, 0x0067, 0x0068, 0x0069, 0x006A, // f g h i j
			0x006B, 0x006C, 0x006D, 0x006E, 0x006F, // k l m n o
			0x0070, 0x0071, 0x0072, 0x0073, 0x0074, // p q r s t
			0x0075, 0x0076, 0x0077, 0x0078, 0x0079, // u v w x y
			0x007A // z
	};

	/** property: upper alpha chars */
	public static final char UPPER_ALPHA_CHARS[] = { 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, // A
			// B
			// C
			// D
			// E
			0x0046, 0x0047, 0x0048, 0x0049, 0x004A, // F G H I J
			0x004B, 0x004C, 0x004D, 0x004E, 0x004F, // K L M N O
			0x0050, 0x0051, 0x0052, 0x0053, 0x0054, // P Q R S T
			0x0055, 0x0056, 0x0057, 0x0058, 0x0059, // U V W X Y
			0x005A // Z
	};

	/** property: lower Greek char size */
	public static final int LOWER_GREEK_CHARS_SIZE = 24;

	// Note: 0x03C2 GREEK FINAL SIGMA is not used in here....
	/** property: lower Greek chars */
	public static final char LOWER_GREEK_CHARS[] = { 0x03B1, 0x03B2, 0x03B3, 0x03B4, 0x03B5, // alpha
			// beta
			// gamma
			// delta
			// epsilon
			0x03B6, 0x03B7, 0x03B8, 0x03B9, 0x03BA, // zeta eta theta iota kappa
			0x03BB, 0x03BC, 0x03BD, 0x03BE, 0x03BF, // lamda mu nu xi omicron
			0x03C0, 0x03C1, 0x03C3, 0x03C4, 0x03C5, // pi rho sigma tau upsilon
			0x03C6, 0x03C7, 0x03C8, 0x03C9 // phi chi psi omega
	};

	/** property: lower Roman chars a */
	public static final char LOWER_ROMAN_CHARSA[] = { 'i', 'x', 'c', 'm' };// "ixcm";

	/** property: upper Roman chars a */
	public static final char UPPER_ROMAN_CHARSA[] = { 'I', 'X', 'C', 'M' };// "IXCM";

	/** property: lower Roman chars b */
	public static final char LOWER_ROMAN_CHARSB[] = { 'v', 'l', 'd' };// "vld";

	/** property: upper Roman chars b */
	public static final char UPPER_ROMAN_CHARSB[] = { 'V', 'L', 'D' };// "VLD";

	/** property: Georgian value */
	public static char GEORGIAN_VALUE[] = { // 4 * 9 + 1 = 37
			// 1 2 3 4 5 6 7 8 9
			0x10D0, 0x10D1, 0x10D2, 0x10D3, 0x10D4, 0x10D5, 0x10D6, 0x10F1, 0x10D7,
			// 10 20 30 40 50 60 70 80 90
			0x10D8, 0x10D9, 0x10DA, 0x10DB, 0x10DC, 0x10F2, 0x10DD, 0x10DE, 0x10DF,
			// 100 200 300 400 500 600 700 800 900
			0x10E0, 0x10E1, 0x10E2, 0x10F3, 0x10E4, 0x10E5, 0x10E6, 0x10E7, 0x10E8,
			// 1000 2000 3000 4000 5000 6000 7000 8000 9000
			0x10E9, 0x10EA, 0x10EB, 0x10EC, 0x10ED, 0x10EE, 0x10F4, 0x10EF, 0x10F0,
			// 10000
			0x10F5 };

	static {
		TYPES.put(CSS_LISTSTYLETYPE_DISC_VALUE, LIST_DISC_VALUE);
		TYPES.put(CSS_LISTSTYLETYPE_CIRCLE_VALUE, LIST_STYLE_CIRCLE);
		TYPES.put(CSS_LISTSTYLETYPE_SQUARE_VALUE, LIST_STYLE_SQUARE);
		TYPES.put(CSS_LISTSTYLETYPE_DECIMALLEADINGZERO_VALUE, LIST_STYLE_DECIMALLEADINGZERO);
		TYPES.put(CSS_LISTSTYLETYPE_LOWERROMAN_VALUE, LIST_STYLE_LOWERROMAN);
		TYPES.put(CSS_LISTSTYLETYPE_UPPERROMAN_VALUE, LIST_STYLE_UPPERROMAN);
		TYPES.put(CSS_LISTSTYLETYPE_LOWERGREEK_VALUE, LIST_STYLE_LOWERGREEK);
		TYPES.put(CSS_LISTSTYLETYPE_LOWERLATINC_VALUE, LIST_STYLE_LOWERLATIN);
		TYPES.put(CSS_LISTSTYLETYPE_UPPERLATIN_VALUE, LIST_STYLE_UPPERLATIN);
		TYPES.put(CSS_LISTSTYLETYPE_ARMENIAN_VALUE, LIST_STYLE_ARMENIAN);
		TYPES.put(CSS_LISTSTYLETYPE_GEORGIAN_VALUE, LIST_STYLE_GEORGIAN);
		TYPES.put(CSS_LISTSTYLETYPE_LOWERALPHA_VALUE, LIST_STYLE_LOWERALPHA);
		TYPES.put(CSS_LISTSTYLETYPE_UPPERALPHA_VALUE, LIST_STYLE_UPPERALPHA);
		TYPES.put(CSS_LISTSTYLETYPE_NONE_VALUE, LIST_STYLE_NONE);
		TYPES.put(CSS_LISTSTYLETYPE_INHERIT_VALUE, LIST_STYLE_INHERIT);
		TYPES.put(CSS_LISTSTYLETYPE_DECIMAL, LIST_STYLE_DECIMAL);

	}

	private int type;

	/**
	 * Constructor
	 *
	 * @param styleType style type
	 */
	public BulletFrame(String styleType) {
		initial(styleType);

	}

	private void initial(String styleType) {
		if (TYPES.get(styleType) != null) {
			type = TYPES.get(styleType);
		} else {
			type = LIST_STYLE_NONE;
		}

	}

	/**
	 * Paint a bullet list
	 *
	 * @param ordinal list type
	 * @return a bullet list
	 */
	public String paintBullet(int ordinal) {
		StringBuffer buffer = new StringBuffer();
		switch (type) {
		case LIST_DISC_VALUE:
			buffer.append(DISC_CHAR);
			break;
		case LIST_STYLE_CIRCLE:
			buffer.append(CIRCLE_CHAR);
			break;
		case LIST_STYLE_SQUARE:
			buffer.append(SQUARE_CHAR);
			break;
		case LIST_STYLE_DECIMALLEADINGZERO:
			decimalLeadingZeroToText(ordinal, buffer);
			break;
		case LIST_STYLE_LOWERROMAN:
			romanToText(ordinal, buffer, LOWER_ROMAN_CHARSA, LOWER_ROMAN_CHARSB);
			break;
		case LIST_STYLE_UPPERROMAN:
			romanToText(ordinal, buffer, UPPER_ROMAN_CHARSA, UPPER_ROMAN_CHARSB);
			break;
		case LIST_STYLE_LOWERGREEK:
			charListToText(ordinal, buffer, LOWER_GREEK_CHARS, LOWER_GREEK_CHARS_SIZE);
			break;
		case LIST_STYLE_UPPERLATIN:
		case LIST_STYLE_UPPERALPHA:
			charListToText(ordinal, buffer, UPPER_ALPHA_CHARS, ALPHA_SIZE);
			break;
		case LIST_STYLE_LOWERLATIN:
		case LIST_STYLE_LOWERALPHA:
			charListToText(ordinal, buffer, LOWER_ALPHA_CHARS, ALPHA_SIZE);
			break;
		case LIST_STYLE_ARMENIAN:
			armenianToText(ordinal, buffer);
			break;
		case LIST_STYLE_GEORGIAN:
			georgianToText(ordinal, buffer);
			break;
		case LIST_STYLE_DECIMAL:
			decimalToText(ordinal, buffer);
			break;
		case LIST_STYLE_NONE:
		case LIST_STYLE_INHERIT:
		default:
			buffer.append(EMPTYSTRING);
			break;
		}
		return buffer.toString();
	}

	private void decimalToText(int ordinal, StringBuffer buffer) {
		buffer.append(ordinal);
	}

	private void georgianToText(int ordinal, StringBuffer buffer) {
		if (ordinal < 1 || ordinal > 19999) {
			buffer.append(ordinal);
			return;
		}
		int d = 0;
		do {
			int cur = ordinal % 10;
			if (cur > 0) {
				char u = GEORGIAN_VALUE[(d * 9) + (cur - 1)];
				buffer.append(u);
			}
			++d;
			ordinal /= 10;
		} while (ordinal > 0);
		buffer.reverse();
	}

	private void armenianToText(int ordinal, StringBuffer buffer) {
		if (ordinal < 1 || ordinal > 9999) {
			buffer.append(ordinal);
			return;
		}
		int d = 0;
		do {
			int cur = ordinal % 10;
			if (cur > 0) {
				char u = (char) (0x0530 + (d * 9) + cur);
				buffer.append(u);
			}
			++d;
			ordinal /= 10;
		} while (ordinal > 0);
		buffer.reverse();
	}

	private void charListToText(int ordinal, StringBuffer buffer, char[] chars, int aBase) {
		if (ordinal < 1) {
			buffer.append(ordinal);
			return;
		}
		do {
			ordinal--;
			int cur = ordinal % aBase;
			buffer.append(chars[cur]);
			ordinal /= aBase;
		} while (ordinal > 0);
		buffer.reverse();
	}

	private void decimalLeadingZeroToText(int ordinal, StringBuffer buffer) {
		if (ordinal < 10 && ordinal > 0) {
			buffer.append(ZERO_CHAR);
		}
		buffer.append(ordinal);
	}

	private void romanToText(int ordinal, StringBuffer buffer, char[] achars, char[] bchars) {
		if (ordinal < 1 && ordinal > 3999) {
			buffer.append(ordinal);
			return;
		}
		StringBuffer addOn = new StringBuffer();
		StringBuffer decStr = new StringBuffer();
		decStr.append(ordinal);
		int len = decStr.length();
		int dp = 0;
		int end = len;
		int romanPos = len;
		int n;
		for (; dp < end; dp++) {
			romanPos--;

			switch (decStr.charAt(dp)) {
			case '3':
				addOn.append(achars[romanPos]);
				// FALLTHROUGH
			case '2':
				addOn.append(achars[romanPos]);
				// FALLTHROUGH
			case '1':
				addOn.append(achars[romanPos]);
				break;
			case '4':
				addOn.append(achars[romanPos]);
				// FALLTHROUGH
			case '5':
			case '6':
			case '7':
			case '8':
				addOn.append(bchars[romanPos]);
				for (n = 0; '5' + n < decStr.charAt(dp); n++) {
					addOn.append(achars[romanPos]);
				}
				break;
			case '9':
				addOn.append(achars[romanPos]);
				addOn.append(achars[romanPos + 1]);
				break;
			default:
				break;
			}

		}
		buffer.append(addOn);
	}

}
