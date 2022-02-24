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

package org.eclipse.birt.doc.util;

public abstract class HtmlDocReader {
	protected HTMLParser parser = new HTMLParser();
	int pushToken = -2;

	protected int getToken() {
		if (pushToken == -2)
			return parser.getToken();
		int token = pushToken;
		pushToken = -2;
		return token;
	}

	protected void pushToken(int token) {
		pushToken = token;
	}

	protected void skipTo(String tag) {
		for (;;) {
			int token = getToken();
			if (token == HTMLParser.EOF)
				return;
			if (isElement(token, tag))
				return;
		}
	}

	protected String getTextTo(String endTag) {
		return getTextTo(endTag, false);
	}

	protected boolean isBlockEnd(int token) {
		if (token != HTMLParser.ELEMENT)
			return false;
		String tag = parser.getTokenText().toLowerCase();
		return (tag.equals("h1") || tag.equals("h2") || tag.equals("h3") || tag.equals("/body") || tag.equals("/html"));

	}

	protected String getTextTo(String endTag, boolean textOnly) {
		String startTag = null;
		if (endTag.startsWith("/"))
			startTag = endTag.substring(1);
		StringBuffer text = new StringBuffer();
		boolean inCode = false;
		for (;;) {
			int token = getToken();
			if (token == HTMLParser.TEXT) {
				text.append(parser.getTokenText());
				continue;
			} else if (token != HTMLParser.ELEMENT)
				continue;
			String tag = parser.getTokenText().toLowerCase();
			if (tag.equals(endTag)) {
				break;
			}
			if (isBlockEnd(token)) {
				pushToken(token);
				break;
			}
			if (startTag != null && tag.equalsIgnoreCase(startTag)) {
				pushToken(token);
				break;
			}
			if (textOnly && parser.isFormatTag()) {
				continue;
			}
			if (tag.equals("span")) {
				String classValue = parser.getAttrib("class");
				if (classValue != null && classValue.equals("CodeText")) //$NON-NLS-1$ //$NON-NLS-2$
				{
					inCode = true;
					text.append("<code>"); //$NON-NLS-1$
				}
			} else if (tag.equals("/span") && inCode) //$NON-NLS-1$
			{
				inCode = false;
				text.append("</code>"); //$NON-NLS-1$
			} else {
				text.append(parser.getFullElement());
			}
		}
		if (textOnly) {
			int posn = text.indexOf("\n"); //$NON-NLS-1$
			while (posn != -1) {
				text.setCharAt(posn, ' ');
				posn = text.indexOf("\n"); //$NON-NLS-1$
			}
			posn = text.indexOf("  ");
			while (posn != -1) {
				text.deleteCharAt(posn);
				posn = text.indexOf("  ");
			}
		}
		return text.toString().trim();
	}

	protected boolean isPara(int token, String className) {
		if (token != HTMLParser.ELEMENT)
			return false;
		if (!parser.getTokenText().equalsIgnoreCase("p")) //$NON-NLS-1$
			return false;
		if (className == null)
			return true;
		String pClass = parser.getAttrib("class");
		if (pClass == null)
			return false;
		return (pClass.equalsIgnoreCase(className)); // $NON-NLS-1$
	}

	protected boolean isElement(int token, String tag) {
		if (token != HTMLParser.ELEMENT)
			return false;
		return parser.getTokenText().equals(tag);
	}

	protected boolean startsWith(String line, String prefix) {
		if (line.length() < prefix.length())
			return false;
		String test = line.substring(0, prefix.length());
		return test.equalsIgnoreCase(prefix);
	}

	protected String getTail(String line, String prefix) {
		String tail = line.substring(prefix.length());
		return tail.trim();
	}

	protected String strip(String text, String tag) {
		if (text.startsWith("<" + tag + ">"))
			text = text.substring(tag.length() + 2);
		if (text.endsWith("</" + tag + ">"))
			text = text.substring(0, text.length() - tag.length() - 3);
		return text;
	}

	protected String append(String value, String toAdd) {
		if (value == null)
			return toAdd;
		return value + toAdd;
	}

	public boolean isBlockEnd(String tag) {
		return tag.equals("h1") || //$NON-NLS-1$
				tag.equals("h2") || //$NON-NLS-1$
				tag.equals("h3") || //$NON-NLS-1$
				tag.equals("h4") || //$NON-NLS-1$
				tag.equals("h5") || //$NON-NLS-1$
				tag.equals("/body") || //$NON-NLS-1$
				tag.equals("/html"); //$NON-NLS-1$
	}

	public boolean isBlank(String s) {
		return s == null || s.trim().length() == 0;
	}

	protected String stripPara(String orig) {
		if (orig == null)
			return null;
		StringBuffer text = new StringBuffer(orig);
		for (;;) {
			int len = text.length();
			if (len == 0)
				break;
			if (Character.isWhitespace(text.charAt(0))) {
				text.deleteCharAt(0);
				continue;
			}
			if (Character.isWhitespace(text.charAt(len - 1))) {
				text.deleteCharAt(len - 1);
				continue;
			}
			if (len >= 3 && text.substring(0, 3).equalsIgnoreCase("<p>")) {
				text.delete(0, 3);
				continue;
			}
			if (len >= 6 && text.substring(0, 6).equalsIgnoreCase("&nbsp;")) {
				text.delete(0, 6);
				continue;
			}
			if (len >= 3 && text.substring(len - 3, len).equalsIgnoreCase("<p>")) {
				text.delete(len - 3, len);
				continue;
			}
			if (len >= 4 && text.substring(len - 4, len).equalsIgnoreCase("</p>")) {
				text.delete(len - 4, len);
				continue;
			}
			if (len >= 6 && text.substring(len - 6, len).equalsIgnoreCase("&nbsp;")) {
				text.delete(len - 6, len);
				continue;
			}
			break;
		}

		return text.toString();
	}

	protected String copySection() {
		parser.ignoreWhitespace(false);
		StringBuffer text = new StringBuffer();
		int token;
		for (;;) {
			token = getToken();
			if (token == HTMLParser.EOF)
				break;
			if (token == HTMLParser.TEXT) {
				text.append(parser.getTokenText());
				continue;
			}
			if (token != HTMLParser.ELEMENT)
				continue;
			if (isBlockEnd(token)) {
				pushToken(token);
				break;
			}
			text.append(parser.getFullElement());
		}
		parser.ignoreWhitespace(true);
		return text.toString().trim();
	}

}
