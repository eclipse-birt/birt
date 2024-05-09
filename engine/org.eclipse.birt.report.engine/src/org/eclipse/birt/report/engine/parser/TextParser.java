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

package org.eclipse.birt.report.engine.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Parses the content of a text-related item.
 * <p>
 * During parsing a plain text string, only control codes '\r', '\n', '\r\n'
 * '\n\r' are converted to "p" element in HTML, space chars are treated as
 * non-breaking space, i.e., &nbsp; tab characters are treated as spaces, and
 * others characters are preserved without change to be passed on to Emitter for
 * outputting.
 * <p>
 * After parsing, the root of the DOM tree is a <code>Document</code> node with
 * an <code>Element</code> child node whose tag name is body. All other nodes
 * that need to be processed are descendant nodes of "body" node.
 * <p>
 *
 */
public class TextParser {

	/**
	 * property: text type "auto" to verify automatically the text content type,
	 * e.g. "plain", "html"
	 */
	public static String TEXT_TYPE_AUTO = "auto"; //$NON-NLS-1$

	/** property: text type "plain" for plain text */
	public static String TEXT_TYPE_PLAIN = "plain"; //$NON-NLS-1$

	/** property: text type "html" for HTML text */
	public static String TEXT_TYPE_HTML = "html"; //$NON-NLS-1$

	/** property: text type "rtf" for RTF text */
	public static String TEXT_TYPE_RTF = "rtf"; //$NON-NLS-1$

	/** property: HTML document prefix */
	public static String HTML_PREFIX = "<html>"; //$NON-NLS-1$

	/** property: RTF document prefix */
	public static String RTF_PREFIX = "\\rtf"; //$NON-NLS-1$

	/**
	 * logs syntax errors.
	 */
	protected static Logger logger = Logger.getLogger(TextParser.class.getName());

	/**
	 * Parse the input text to get a DOM tree.
	 *
	 * @param text     the text to be parsed
	 * @param textType the text type (case-insensitive). Valid types includes
	 *                 auto,plain,html. If null, it is regarded as auto; if set to
	 *                 any other value, treat the text as plain text.
	 * @return DOM tree if no error exists,otherwise null.
	 */
	public Document parse(String text, String textType) {
		// Handle null case
		if (text == null || text.length() == 0) {
			return null;
		}

		// If the type is null or auto, resets the text type based on the
		// content prefix
		if (null == textType || TEXT_TYPE_AUTO.equalsIgnoreCase(textType)) {
			int index = 0;
			int len = text.length();

			// remove white spaces
			while (index < len && Character.isWhitespace(text.charAt(index))) {
				index++;
			}

			// Checks if the first six characters are "<html>"
			if ((len - index) >= 6 && text.substring(index, index + 6).equalsIgnoreCase(HTML_PREFIX)) {
				textType = TEXT_TYPE_HTML;
			} else if ((len - index) >= 4 && text.substring(index, index + 4).equalsIgnoreCase(RTF_PREFIX)) {
				textType = TEXT_TYPE_RTF;
			} else {
				textType = TEXT_TYPE_PLAIN; // Assume plain text in any other
				// cases
			}
		}

		Document doc = null;

		if (TEXT_TYPE_HTML.equalsIgnoreCase(textType)) {
			try {
				// Convert input string to an input stream because JTidy accepts
				// a stream only
				doc = new HTMLTextParser().parseHTML(new ByteArrayInputStream(text.getBytes("UTF-8"))); //$NON-NLS-1$
			} catch (UnsupportedEncodingException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
		} else if (TEXT_TYPE_RTF.equalsIgnoreCase(textType)) {
			assert false; // Not supported yet
			return null;
		} else {
			if (!TEXT_TYPE_PLAIN.equalsIgnoreCase(textType)) {
				logger.log(Level.WARNING, "Invalid text type. The content is treated as plain text."); //$NON-NLS-1$
			}
			doc = new PlainTextParser().parsePlainText(text);

		}

		if (doc != null && doc.getFirstChild() != null && doc.getFirstChild() instanceof Element) {

			((Element) (doc.getFirstChild())).setAttribute("text-type", //$NON-NLS-1$
					textType);
		}

		return doc;
	}

	/**
	 * Parse the input stream to get a DOM tree.
	 *
	 * @param in       the input stream
	 * @param textType the text type (case-insensitive). Valid types includes
	 *                 auto,plain,html. If null, it is regarded as auto; if set to
	 *                 any other value, treat the text as plain text.
	 * @return DOM tree if no error exists,otherwise null.
	 */
	public Document parse(InputStream in, String textType) {
		// Handle the null case
		if (in == null) {
			return null;
		}

		InputStream tmpInputStream = in;

		// If the type is null or auto, resets the text type based on the
		// content prefix
		if (null == textType || TEXT_TYPE_AUTO.equalsIgnoreCase(textType)) {
			StringBuilder buf = new StringBuilder();
			int chr;
			try {
				// Skips the white space
				while ((chr = in.read()) != -1 && Character.isWhitespace((char) chr)) {
					buf.append((char) chr);
				}

				// Reads the next (up-to) six characters
				for (int headLen = 0; headLen < 6 && chr != -1; headLen++) {
					buf.append((char) chr);
					chr = in.read();
				}

				// Checks the type of text
				if (buf.toString().toLowerCase().endsWith(HTML_PREFIX)) {
					textType = TEXT_TYPE_HTML;
				} else if (buf.toString().toLowerCase().endsWith(RTF_PREFIX)) {
					textType = TEXT_TYPE_RTF;
				} else {
					textType = TEXT_TYPE_PLAIN;
				}

				if (chr != -1) {
					buf.append((char) chr);
				}

				// Pushes back the characters that are read for text type
				// detection
				byte[] head = buf.toString().getBytes();
				PushbackInputStream pin = new PushbackInputStream(in, head.length);
				// Push back these bytes to the stream to ensure that the stream
				// is complete.
				pin.unread(head, 0, head.length);
				tmpInputStream = pin;
			} catch (IOException e) {
				logger.log(Level.SEVERE, e.getMessage(), e);
				return null;
			}
		}

		Document doc = null;

		if (TEXT_TYPE_HTML.equalsIgnoreCase(textType)) {
			doc = new HTMLTextParser().parseHTML(tmpInputStream);
		} else if (TEXT_TYPE_RTF.equals(textType)) {
			assert false; // not supported
			return null;
		} else {
			if (!TEXT_TYPE_PLAIN.equalsIgnoreCase(textType)) {
				logger.log(Level.WARNING, "Invalid text type. The content is treated as plain text."); //$NON-NLS-1$
			}
			// All other types are considered as the plain text.
			doc = new PlainTextParser().parsePlainText(tmpInputStream);

		}

		if (doc != null && doc.getFirstChild() != null && doc.getFirstChild() instanceof Element) {

			((Element) (doc.getFirstChild())).setAttribute("text-type", //$NON-NLS-1$
					textType);
		}

		return doc;
	}
}
