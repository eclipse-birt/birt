/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.emitter;

import java.util.logging.Level;

public class HTMLEncodeUtil extends XMLEncodeUtil {

	static char[] HTML_TEXT_ENCODING = new char[] { '&', '<' };
	static char[] HTML_WHITE_SPACE_ENCODING = new char[] { '&', '<', ' ', '\t', '\r', '\n' };

	static final String ESCAPE_BR = "<br/>";
	static final String ESCAPE_NBSP = "&#xa0;";
	static final String ESCAPE_AMP = "&amp;";
	static final String ESCAPE_LT = "&lt;";

	/**
	 * Replace the escape characters.
	 * 
	 * @param s           The string needs to be replaced.
	 * @param whiteespace A <code>boolean<code> value indicating if the white space
	 *                    character should be converted or not.
	 * @return The replaced string.
	 */
	static public String encodeText(String text, boolean whitespace) {
		char[] chars = text.toCharArray();
		int length = chars.length;
		int index = testEscape(chars, whitespace ? HTML_WHITE_SPACE_ENCODING : HTML_TEXT_ENCODING);

		if (index >= length) {
			return text;
		}
		StringBuilder sb = new StringBuilder(length * 2);
		sb.append(chars, 0, index);

		while (index < length) {
			char c = chars[index++];
			if (Character.isHighSurrogate(c)) {
				index += decodeSurrogate(c, chars, index, sb);
			} else if (isValidCodePoint(c)) {
				if (c == '&') {
					sb.append(ESCAPE_AMP);
				} else if (c == '<') {
					sb.append(ESCAPE_LT);
				} else if (c == '\r' || c == '\n') {
					if (whitespace) {
						index += encodeLineBreak(c, chars, index, sb);
					} else {
						sb.append(c);
					}
				} else if (c == ' ' || c == '\t') {
					if (whitespace) {
						index += encodeWhitespace(c, chars, index, sb);
					} else {
						sb.append(c);
					}
				} else {
					sb.append(c);
				}
			} else {
				logger.log(Level.WARNING, MESSAGE_INVALID_CHARACTER, Integer.valueOf(c));
			}
		}

		return sb.toString();
	}

	/**
	 * Implement white-space:pre-wrap escaping.
	 * 
	 * see http://www.w3.org/TR/CSS2/text.html#white-space-prop
	 * 
	 * If 'white-space' is set to 'pre' or 'pre-wrap', any sequence of spaces
	 * (U+0020) unbroken by an element boundary is treated as a sequence of
	 * non-breaking spaces. However, for 'pre-wrap', a line breaking opportunity
	 * exists at the end of the sequence.
	 * 
	 * For single white space, if it is the first character or after a BR, replace
	 * it to &nbsp;. If it is the last character or followed by a <BR>
	 * replace it to &nbsp; otherwise, keep it unchanged. For multiple white space,
	 * replace it to &nbsp; except the last one. If it is the last one or followned
	 * by a <BR>
	 * , replace all the whitespaced to BR
	 */
	static public int encodeWhitespace(char c1, char[] chars, int offset, StringBuilder sb) {
		boolean isFirstLine = offset == 1;
		int index = offset;
		while (index < chars.length) {
			char nc = chars[index++];
			if (nc == ' ' || nc == '\t') {
				sb.append(ESCAPE_NBSP);
				continue;
			}
			if (nc == '\r' || nc == '\n') {
				// the white space before the line break
				sb.append(ESCAPE_NBSP);
				return index - offset - 1;
			}
			// the white space before any other character
			if (isFirstLine) {
				sb.append(ESCAPE_NBSP);
			} else {
				sb.append(' ');
			}
			return index - offset - 1;
		}
		// it is the last white space
		sb.append(ESCAPE_NBSP);
		return index - offset;
	}

	static int encodeLineBreak(char c, char[] chars, int offset, StringBuilder sb) {
		int index = offset;
		if (c == '\r' && index < chars.length && chars[index] == '\n') {
			index++;
		}
		sb.append(ESCAPE_BR);
		if (index >= chars.length) {
			return index - offset;
		}
		char nc = chars[index++];
		if (nc != ' ' && nc != '\t') {
			// [br][none-white]
			return index - offset - 1;
		}
		// [br][ws]... The first character is always replaces with nbsp
		sb.append(ESCAPE_NBSP);
		// test the following characters
		while (index < chars.length) {
			nc = chars[index++];
			if (nc == ' ' || nc == '\t') {
				if (index >= chars.length) {
					sb.append(ESCAPE_NBSP);
					return index - offset;
				}
				char nnc = chars[index];
				if (nnc == ' ' || nnc == '\t') {
					sb.append(ESCAPE_NBSP);
					continue;
				}
				if (nnc == '\r' || nnc == '\n') {
					sb.append(ESCAPE_NBSP);
				} else {
					sb.append(' ');
				}
				return index - offset;
			}
			return index - offset - 1;
		}
		return index - offset;
	}
}
