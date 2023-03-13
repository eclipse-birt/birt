/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class XMLEncodeUtil {

	protected static Logger logger = Logger.getLogger(XMLEncodeUtil.class.getName());

	/**
	 * In <a href="http://www.w3.org/TR/REC-xml/">XML specification 1.0 </a> section
	 * 2.2 Character, the valid characters in XML should be limited to:
	 *
	 * Any Unicode character, excluding the surrogate blocks, FFFE, and FFFF.
	 *
	 * Char ::= #x9 | #xA | #xD | [#x20-#xD7FF] | [#xE000-#xFFFD] |
	 * [#x10000-#x10FFFF]
	 *
	 */
	protected static boolean isValidCodePoint(int ch) {
		if (ch == 0x09 || ch == 0x0A || ch == 0x0D || (ch >= 0x20 && ch <= 0xD7FF) || (ch >= 0xE000 && ch <= 0xFFFD)
				|| (ch >= 0x10000 && ch <= 0x10FFFF)) {
			return true;
		}
		return false;
	}

	protected static int testEscape(char[] chars, char[] encodings) {
		int index = 0;
		int length = chars.length;
		while (index < length) {
			char c1 = chars[index++];
			if (Character.isHighSurrogate(c1)) {
				if (index < length) {
					char c2 = chars[index++];
					if (Character.isLowSurrogate(c2)) {
						int cp = Character.toCodePoint(c1, c2);
						if (isValidCodePoint(cp)) {
							continue;
						}
					}
					return index - 2;
				}
				return index - 1;
			} else {
				if (isValidCodePoint(c1)) {
					if (encodings != null) {
						for (char ch : encodings) {
							if (c1 == ch) {
								return index - 1;
							}
						}
					}
					continue;
				}
				return index - 1;
			}
		}
		return length;
	}

	protected static final char[] XML_TEXT_ENCODE = { '&', '<' };

	/**
	 * Replace the escape character
	 *
	 * @param s           The string needs to be replaced.
	 * @param whiteespace A <code>boolean<code> value indicating if the white space
	 *                    character should be converted or not.
	 * @return the replaced string
	 */
	static public String encodeText(String s) {
		char[] chars = s.toCharArray();
		int length = chars.length;
		int index = testEscape(chars, XML_TEXT_ENCODE);
		if (index >= length) {
			return s;
		}

		StringBuilder sb = new StringBuilder(2 * length);
		sb.append(chars, 0, index);

		while (index < length) {
			char c = chars[index++];
			if (Character.isHighSurrogate(c)) {
				index += decodeSurrogate(c, chars, index, sb);
			} else if (isValidCodePoint(c)) {
				if (c == '&') {
					sb.append("&amp;");
				} else if (c == '<') {
					sb.append("&lt;");
				} else {
					sb.append(c);
				}
			} else {
				logger.log(Level.WARNING, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
			}
		}
		return sb.toString();
	}

	protected static final char[] XML_ATTR_ENCODE = { '&', '<', '>', '"', '\r', '\n', '\t' };

	/**
	 * Replaces the escape character in attribute value.
	 *
	 * @param s The string needs to be replaced.
	 * @return the replaced string
	 */
	static public String encodeAttr(String s) {
		char[] chars = s.toCharArray();
		int length = chars.length;
		int index = testEscape(chars, XML_ATTR_ENCODE);
		if (index >= length) {
			return s;
		}

		StringBuilder sb = new StringBuilder(2 * length);
		sb.append(chars, 0, index);

		while (index < length) {
			char c = chars[index++];
			if (Character.isHighSurrogate(c)) {
				index += decodeSurrogate(c, chars, index, sb);
			} else if (isValidCodePoint(c)) {
				if (c == '&') {
					sb.append("&amp;");
				} else if (c == '<') {
					sb.append("&lt;");
				} else if (c == '"') {
					sb.append("&#34;");
				} else if (c == '\r') {
					sb.append("&#13;");
				} else if (c == '\n') {
					sb.append("&#10;");
				} else if (c == '\t') {
					sb.append("&#9;");
				} else {
					sb.append(c);
				}
			} else {
				logger.log(Level.WARNING, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
			}
		}
		return sb.toString();
	}

	static public String encodeCdata(String s) {
		char[] chars = s.toCharArray();
		int length = chars.length;
		int index = testEscape(chars, null);
		if (index >= length) {
			return s;
		}

		StringBuilder sb = new StringBuilder(2 * length);
		sb.append(chars, 0, index);

		while (index < length) {
			char c = chars[index++];
			if (Character.isHighSurrogate(c)) {
				index += decodeSurrogate(c, chars, index, sb);
			} else if (isValidCodePoint(c)) {
				sb.append(c);
			} else {
				logger.log(Level.WARNING, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
			}
		}
		return sb.toString();
	}

	/**
	 * decode the surrogate pair into an XML encoding, such as &amp;xAAA;.
	 *
	 *
	 * @param c      the high surrogate
	 * @param chars  the char buffer
	 * @param offset the low surrogate index
	 * @param sb     string builder contains the encoding
	 * @return index to be append to the index. 0 if there is no valid encoding.
	 */
	protected static int decodeSurrogate(char c, char[] chars, int offset, StringBuilder sb) {
		if (Character.isHighSurrogate(c)) {
			if (offset < chars.length) {
				char nc = chars[offset];
				if (Character.isLowSurrogate(nc)) {
					int cp = Character.toCodePoint(c, nc);
					if (isValidCodePoint(cp)) {
						sb.append(c);
						sb.append(nc);
						return 1;
					}
					logger.log(Level.INFO, MESSAGE_INVALID_SURROGATE,
							new Object[] { Integer.valueOf(c), Integer.valueOf(nc) });
				}
				logger.log(Level.INFO, MESSAGE_UNMATCH_SURROGATE,
						new Object[] { Integer.valueOf(c), Integer.valueOf(nc) });
			}
			logger.log(Level.INFO, MESSAGE_MISSING_LOW_SURROGATE, new Object[] { Integer.valueOf(c) });
		}
		logger.log(Level.INFO, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
		return 0;
	}

	protected static final String MESSAGE_MISSING_LOW_SURROGATE = "Missing low surrogate for: 0x{0}";
	protected static final String MESSAGE_UNMATCH_SURROGATE = "Unmatch surrogate parie: 0x{0}, 0x{1}";
	protected static final String MESSAGE_INVALID_CHARACTER = "Invalid XML character:0x{0}";
	protected static final String MESSAGE_INVALID_SURROGATE = "Invalid XML surrogate pair:0x{0}, 0x{1}";
}
