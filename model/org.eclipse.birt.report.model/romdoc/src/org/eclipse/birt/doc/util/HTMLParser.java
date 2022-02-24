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

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;

public class HTMLParser {
	FileReader reader;
	LineNumberReader in;
	String token;
	ArrayList attribs = new ArrayList();
	int pushC = -1;
	private boolean ignoreWhitespace = true;

	public static final int EOF = -1;
	public static final int TEXT = 1;
	public static final int DOCTYPE = 2;
	public static final int ELEMENT = 3;
	public static final int COMMENT = 4;
	public static final int SPECIAL_ELEMENT = 5;

	public static final int START_ELEMENT = 0;
	public static final int END_ELEMENT = 1;
	public static final int SINGLE_ELEMENT = 2;

	public HTMLParser() {
	}

	public void open(String fileName) throws FileNotFoundException {
		reader = new FileReader(fileName);
		in = new LineNumberReader(reader);
	}

	/**
	 *
	 */
	public void close() {
		try {
			in.close();
			reader.close();
		} catch (IOException e1) {
			// Ignore
		}
	}

	public String getTokenText() {
		return token;
	}

	public int getElementType() {
		if (token.startsWith("/")) { //$NON-NLS-1$
			return END_ELEMENT;
		}
		if (token.endsWith("/")) { //$NON-NLS-1$
			return SINGLE_ELEMENT;
		}
		return START_ELEMENT;
	}

	public String getElement() {
		if (token.startsWith("/")) { //$NON-NLS-1$
			return token.substring(1);
		}
		if (token.endsWith("/")) { //$NON-NLS-1$
			return token.substring(0, token.length() - 1);
		}
		return token;

	}

	public ArrayList getAttribs() {
		return attribs;
	}

	public String getAttrib(String name) {
		for (int i = 0; i < attribs.size(); i++) {
			AttribPair a = (AttribPair) attribs.get(i);
			if (a.attrib.equalsIgnoreCase(name)) {
				return a.value;
			}
		}
		return null;
	}

	private int getC() {
		if (pushC != -1) {
			int c = pushC;
			pushC = -1;
			return c;
		}
		try {
			return in.read();
		} catch (IOException e) {
			return EOF;
		}
	}

	private void pushC(int c) {
		pushC = c;
	}

	public int getToken() {
		for (;;) {
			int c = getC();
			switch (c) {
			case -1:
				return EOF;
			case '<':
				return getElement(c);
			default: {
				parseText(c);
				if (!ignoreWhitespace || token.trim().length() > 0) {
					return TEXT;
				}
			}
			}
		}
	}

	private int parseText(int c) {
		StringBuilder text = new StringBuilder();
		for (;;) {
			if (c == EOF) {
				break;
			}
			if (c == '<') {
				pushC(c);
				break;
			}

			// Convert MS-Word-style quotes.

			if (c == 8220 || c == 8221) {
				text.append("&quot;");
			} else {
				text.append((char) c);
			}
			c = getC();
		}

		token = text.toString();
		return TEXT;
	}

	private int skipSpace(int c) {
		while (c != EOF && Character.isWhitespace((char) c)) {
			c = getC();
		}
		return c;
	}

	private int getElement(int c) {
		c = getC();

		// Broken element

		if (c == EOF) {
			return EOF;
		}

		if (c == '!') {
			return getSpecialElement();
		}

		attribs.clear();
		c = skipSpace(c);
		if (c == EOF) {
			return EOF;
		}

		StringBuilder tag = new StringBuilder();
		if (c == '/') {
			tag.append((char) c);
			c = skipSpace(getC());
			while (c != EOF && c != '>' && !Character.isWhitespace((char) c)) {
				tag.append((char) c);
				c = getC();
			}
			token = tag.toString();
			for (;;) {
				if (c == '>' || c == -1) {
					break;
				}
				c = getC();
			}
			return ELEMENT;
		}

		while (c != EOF && c != '>' && c != '/' && !Character.isWhitespace((char) c)) {
			tag.append((char) c);
			c = getC();
		}
		if (c == EOF) {
			token = tag.toString();
			return ELEMENT;
		}

		for (;;) {
			c = skipSpace(c);
			if (c == EOF || c == '>' || c == '/') {
				break;
			}
			c = getAttrib(c);
		}
		if (c == '/') {
			tag.append((char) c);
			for (;;) {
				c = getC();
				if (c == -1 || c == '>') {
					break;
				}
			}
		}
		token = tag.toString();
		return ELEMENT;
	}

	private int getAttrib(int c) {
		AttribPair a = new AttribPair();
		StringBuilder s = new StringBuilder();
		while (c != EOF && c != '=' && !Character.isWhitespace((char) c)) {
			s.append((char) c);
			c = getC();
		}
		a.attrib = s.toString();
		c = skipSpace(c);
		if (c != '=') {
			attribs.add(a);
			return c;
		}
		s = new StringBuilder();
		c = skipSpace(getC());
		if (c == '\'' || c == '"') {
			int quote = c;
			for (;;) {
				c = getC();
				if (c == -1) {
					break;
				}
				if (c == quote) {
					c = getC();
					break;
				}
				if (c == '\\') {
					c = getC();
					if (c == EOF) {
						break;
					}
					s.append('\\');
					s.append((char) c);
				} else {
					s.append((char) c);
				}
			}
		} else {
			for (;;) {
				c = getC();
				if (c == -1) {
					break;
				}
				if (c == '>' || c == '/' || Character.isWhitespace((char) c)) {
					c = getC();
					break;
				}
				s.append((char) c);
			}
		}
		a.value = s.toString();
		attribs.add(a);
		return c;
	}

	static class AttribPair {
		String attrib;
		String value;
	}

	private int getSpecialElement() {
		StringBuilder text = new StringBuilder();
		text.append("<!"); //$NON-NLS-1$
		for (;;) {
			int c = getC();
			if (c == EOF || c == '>') {
				break;
			}
			text.append((char) c);
		}
		text.append('>');
		token = text.toString();
		if (token.startsWith("<!--")) { //$NON-NLS-1$
			return COMMENT;
		}
		return SPECIAL_ELEMENT;
	}

	static String formatTags[] = { "i", "b", //$NON-NLS-1$//$NON-NLS-2$
			"strong", "em", //$NON-NLS-1$//$NON-NLS-2$
			"code", "span", //$NON-NLS-1$ //$NON-NLS-2$
			"a" //$NON-NLS-1$
	};

	public boolean isFormatTag() {
		return isFormatTag(getElement());
	}

	public boolean isFormatTag(String tag) {
		for (int i = 0; i < formatTags.length; i++) {
			if (formatTags[i].equalsIgnoreCase(tag)) {
				return true;
			}
		}
		return false;
	}

	public Object getFullElement() {
		StringBuilder text = new StringBuilder();
		text.append('<');
		int elementType = getElementType();
		if (elementType == END_ELEMENT) {
			text.append('/');
		}
		text.append(getElement());

		for (int i = 0; i < attribs.size(); i++) {
			text.append(' ');
			AttribPair a = (AttribPair) attribs.get(i);
			text.append(a.attrib);
			text.append("=\""); //$NON-NLS-1$
			if (a.value != null) {
				text.append(a.value);
			}
			text.append("\""); //$NON-NLS-1$
		}
		if (elementType == SINGLE_ELEMENT) {
			text.append('/');
		}
		text.append('>');
		return text.toString();
	}

	public int getLineNo() {
		return in.getLineNumber();
	}

	public void ignoreWhitespace(boolean b) {
		ignoreWhitespace = b;
	}

}
