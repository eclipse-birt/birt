/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.device.util;

/**
 * This class is responsible to encode/decode special characters for HTML.
 * 
 * @since 2.6
 */

public class HTMLEncoderAdapter implements ICharacterEncoderAdapter {

	private static HTMLEncoderAdapter instance;

	/**
	 * Returns instance of this class.
	 * 
	 * @return
	 */
	public static HTMLEncoderAdapter getInstance() {
		if (instance == null) {
			instance = new HTMLEncoderAdapter();
		}

		return instance;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.device.util.ICharacterEncoderAdapter#escape(java.lang.
	 * String)
	 */
	public String escape(String s) {
		if (s == null) {
			return ""; //$NON-NLS-1$
		}
		StringBuffer result = null;
		char[] s2char = s.toCharArray();

		for (int i = 0, max = s2char.length, delta = 0; i < max; i++) {
			char c = s2char[i];
			String replacement = null;
			// Filters the char not defined.
			if (!(c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD))) {
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
			}
			if (c == '&') {
				replacement = "&amp;"; //$NON-NLS-1$
			} else if (c == '"') {
				replacement = "&#34;"; //$NON-NLS-1$
			} else if (c == '\'') {
				replacement = "&#39;"; //$NON-NLS-1$
			} else if (c == '\r') {
				replacement = "&#13;"; //$NON-NLS-1$
			} else if (c == '>') {
				replacement = "&gt;"; //$NON-NLS-1$
			} else if (c == '<') {
				replacement = "&lt;"; //$NON-NLS-1$
			} else if (c >= 0x80) {
				replacement = "&#x" + Integer.toHexString(c) + ';'; //$NON-NLS-1$
			}
			if (replacement != null) {
				if (result == null) {
					result = new StringBuffer(s);
				}
				result.replace(i + delta, i + delta + 1, replacement);
				delta += (replacement.length() - 1);
			}
		}
		if (result == null) {
			return s;
		}
		return result.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.device.util.ICharacterEncoderAdapter#
	 * transformToJsConstants(java.lang.String)
	 */
	public String transformToJsConstants(String s) {
		if (s == null)
			return null;

		StringBuffer buffer = new StringBuffer();
		int length = s.length();
		for (int i = 0; i < length; i++) {
			char c = s.charAt(i);
			switch (c) {
			case '\\':
				buffer.append("\\\\");//$NON-NLS-1$
				break;
			case '\b':
				buffer.append("\\b");//$NON-NLS-1$
				break;
			case '\t':
				buffer.append("\\t");//$NON-NLS-1$
				break;
			case '\n':
				buffer.append("\\n");//$NON-NLS-1$
				break;
			case '\f':
				buffer.append("\\f");//$NON-NLS-1$
				break;
			case '\r':
				buffer.append("\\r");//$NON-NLS-1$
				break;
			case '"':
				buffer.append("\\\"");//$NON-NLS-1$
				break;
			case '\'':
				buffer.append("\\\'");//$NON-NLS-1$
				break;
			default:
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

}
