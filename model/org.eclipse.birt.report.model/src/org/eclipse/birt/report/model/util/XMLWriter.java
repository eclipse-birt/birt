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

package org.eclipse.birt.report.model.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Stack;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.api.util.UnicodeUtil;

/**
 * General-purpose utility to write an XML file. Provides methods for writing
 * tags, attributes and text. Maintains state for elements and generates the
 * proper closing tags when needed. Provides the ability to "conditionally"
 * start a tag: the tag will be written only if it actually contains attribute
 * or contents.
 * 
 */

public class XMLWriter {

	/**
	 * Line counter.
	 */

	private int lineCounter = 1;

	/**
	 * Control flag indicating whether need mark line number when writing.
	 */

	protected boolean markLineNumber = true;

	/**
	 * The default output encoding is UTF-8.
	 */

	protected final static String OUTPUT_ENCODING = "UTF-8"; //$NON-NLS-1$

	/**
	 * The output stream.
	 */

	protected PrintStream out = null;

	/**
	 * The stack of open tags.
	 */

	protected Stack<String> elementStack = new Stack<String>();

	/**
	 * Flag to indicate if a tag is currently "active": the &ltTag portion has been
	 * written, but not the closing &gt.
	 */

	protected boolean elementActive = false;

	/**
	 * Counts the number of attribute seen so far for a tag. Used to control the
	 * number of attributes that appear on one print line.
	 */

	private int attrCount = 0;

	/**
	 * Stack of conditional attributes to be started only if they contain something.
	 */

	protected Stack<String> pendingElementStack = new Stack<String>();

	/**
	 * Protected constructor
	 * 
	 */

	protected XMLWriter() {
	}

	/**
	 * Constructor.
	 * 
	 * @param outputFile the file to write
	 * @param signature  the UTF signature
	 * @throws java.io.IOException if write error occurs
	 */

	public XMLWriter(File outputFile, String signature) throws java.io.IOException {
		FileOutputStream stream = new FileOutputStream(outputFile);
		out = new PrintStream(stream, false, OUTPUT_ENCODING);
		init(signature);
	}

	/**
	 * Constructor.
	 * 
	 * @param outputFile         the file to write
	 * @param signature          the UTF signature
	 * @param needMarkLineNumber control flag, whether need mark line number
	 * @throws java.io.IOException if write error occurs
	 */

	public XMLWriter(File outputFile, String signature, boolean needMarkLineNumber) throws java.io.IOException {
		FileOutputStream stream = new FileOutputStream(outputFile);
		out = new PrintStream(stream, false, OUTPUT_ENCODING);
		init(signature);
		markLineNumber = needMarkLineNumber;
	}

	/**
	 * Constructor.
	 * 
	 * @param os        the output stream to which the design file is written.
	 * @param signature the UTF signature
	 * @throws IOException if write error occurs
	 */

	public XMLWriter(OutputStream os, String signature) throws IOException {
		out = new PrintStream(os, false, OUTPUT_ENCODING);

		init(signature);
	}

	/**
	 * Constructor.
	 * 
	 * @param os                 the output stream to which the design file is
	 *                           written.
	 * @param signature          the UTF signature
	 * @param needMarkLineNumber
	 * @throws IOException if write error occurs
	 */

	public XMLWriter(OutputStream os, String signature, boolean needMarkLineNumber) throws IOException {
		out = new PrintStream(os, false, OUTPUT_ENCODING);
		markLineNumber = needMarkLineNumber;
		init(signature);
	}

	/**
	 * Write the header line for the XML file.
	 * 
	 * @param signature the UTF signature
	 */

	private void init(String signature) {
		writeUTFSignature(signature);
		out.print("<?xml version=\"1.0\" encoding=\"" //$NON-NLS-1$
				+ OUTPUT_ENCODING + "\"?>"); //$NON-NLS-1$
		printLine();
	}

	/**
	 * Writes the unicode signature (BOM) information to the file. Currently only
	 * UTF-8 is supported.
	 * 
	 * @param signature the unicode signature in the design file.
	 */

	protected final void writeUTFSignature(String signature) {
		if (UnicodeUtil.SIGNATURE_UTF_8.equals(signature)) {
			out.write(0xEF);
			out.write(0xBB);
			out.write(0xBF);
		}

		if (UnicodeUtil.SIGNATURE_UNICODE_BIG.equals(signature)) {
			out.write(0xFE);
			out.write(0xFF);
		}

		if (UnicodeUtil.SIGNATURE_UNICODE_LITTLE.equals(signature)) {
			out.write(0xFF);
			out.write(0xFE);
		}

		if (UnicodeUtil.SIGNATURE_UCS4_BIG.equals(signature)) {
			out.write(0x00);
			out.write(0x00);
			out.write(0xFE);
			out.write(0xFF);
		}

		if (UnicodeUtil.SIGNATURE_UNICODE_LITTLE.equals(signature)) {
			out.write(0x00);
			out.write(0x00);
			out.write(0xFF);
			out.write(0xFE);
		}

	}

	/**
	 * Close the write at the completion of the file.
	 */

	public final void close() {
		assert elementStack.size() == 0;
		out.close();
		out = null;
	}

	/**
	 * Starts an XML element.
	 * 
	 * @param tagName the name of the element
	 */

	public final void startElement(String tagName) {
		flushPendingElements();
		closeTag();
		emitStartTag(tagName);
	}

	/**
	 * Implementation method to write a &ltTag start tag.
	 * 
	 * @param tagName the tag to write
	 */

	protected void emitStartTag(String tagName) {
		elementStack.push(tagName);
		elementActive = true;
		out.print("<"); //$NON-NLS-1$
		out.print(tagName);
		attrCount = 0;
	}

	/**
	 * Implementation method to prepare for writing an attribute.
	 */

	protected void checkAttribute() {
		// Write any conditional elements waiting for content. If we get
		// here, we're about to write an attribute, so the elements do
		// have content.

		flushPendingElements();

		// Write only three attributes on each print line.

		if (attrCount++ == 3) {
			printLine();
			attrCount = 0;
		}
	}

	/**
	 * Private method to write any conditional elements that are not yet started.
	 */

	protected final void flushPendingElements() {
		while (!pendingElementStack.isEmpty()) {
			closeTag();
			emitStartTag(pendingElementStack.remove(0));
		}
	}

	/**
	 * Write a string attribute. The string is assumed to be valid for an attribute.
	 * That is, it cannot contain newlines, etc.
	 * 
	 * @param attrName the name of the attribute
	 * @param value    the attribute value
	 */

	public void attribute(String attrName, String value) {
		if (value == null)
			return;
		checkAttribute();
		assert elementActive;
		out.print(" "); //$NON-NLS-1$
		out.print(attrName);
		out.print("=\""); //$NON-NLS-1$

		// Scan the string character-by-character to look for non-ASCII
		// characters that must be hex encoded.

		int len = value.length();
		for (int i = 0; i < len; i++) {
			char c = value.charAt(i);

			if (c == '&')
				out.print("&amp;"); //$NON-NLS-1$
			else if (c == '<')
				out.print("&lt;"); //$NON-NLS-1$
			else if (c == '"')
				out.print("&quot;"); //$NON-NLS-1$
			else if (c < 0x20) {
				out.print("&#x"); //$NON-NLS-1$
				out.print(Integer.toHexString(c));
				out.print(';');
			} else
				out.print(c);
		}
		out.print("\""); //$NON-NLS-1$
	}

	/**
	 * Write a attribute with an integer value.
	 * 
	 * @param attrName the name of the attribute
	 * @param value    the integer value
	 */

	public final void attribute(String attrName, int value) {
		assert elementActive;
		checkAttribute();
		out.print(" "); //$NON-NLS-1$
		out.print(attrName);
		out.print("=\""); //$NON-NLS-1$
		out.print(value);
		out.print("\""); //$NON-NLS-1$
	}

	/**
	 * Write an attribute with a floating-point (double) value.
	 * 
	 * @param attrName the name of the attribute
	 * @param value    double-precision floating point value
	 */

	public final void attribute(String attrName, double value) {
		assert elementActive;
		checkAttribute();
		out.print(" "); //$NON-NLS-1$
		out.print(attrName);
		out.print("=\""); //$NON-NLS-1$
		out.print(value);
		out.print("\""); //$NON-NLS-1$
	}

	/**
	 * Write an attribute with a boolean value.
	 * 
	 * @param attrName name of the attribute
	 * @param value    Boolean value
	 */

	public final void attribute(String attrName, boolean value) {
		assert elementActive;
		checkAttribute();
		out.print(" "); //$NON-NLS-1$
		out.print(attrName);
		out.print("=\""); //$NON-NLS-1$
		out.print(value ? "true" //$NON-NLS-1$
				: "false"); //$NON-NLS-1$
		out.print("\""); //$NON-NLS-1$
	}

	/**
	 * Indicates the end of the current element. Writes the appropriate end tag.
	 */

	public void endElement() {
		// Check if we never actually wrote the tag because it had
		// no content.

		if (!pendingElementStack.isEmpty()) {
			pendingElementStack.pop();
			return;
		}

		// Close a tag for which the start tag was written.

		assert elementStack.size() > 0;
		String tagName = elementStack.pop();
		if (elementActive) {
			out.print("/>"); //$NON-NLS-1$
		} else {
			out.print("</"); //$NON-NLS-1$
			out.print(tagName);
			out.print(">"); //$NON-NLS-1$
		}
		printLine();
		elementActive = false;
	}

	/**
	 * Write text into the current element.
	 * 
	 * @param text the text to write
	 */

	public void text(String text) {
		closeTextTag();
		if (text == null)
			return;

		// Write the text character-by-character to encode special characters.

		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == '&')
				out.print("&amp;"); //$NON-NLS-1$
			else if (c == '<')
				out.print("&lt;"); //$NON-NLS-1$

			// according to XML specification
			// http://www.w3.org/TR/2006/REC-xml11-20060816/#syntax. The right
			// angle bracket (>) may be represented using the string "&gt;", and
			// MUST, for compatibility, be escaped using either "&gt;" or a
			// character reference when it appears in the string "]]>" in
			// content.
			else if (c == '>') {
				if (i - 2 >= 0 && text.charAt(i - 1) == ']' && text.charAt(i - 2) == ']')
					out.print("&gt;"); //$NON-NLS-1$
				else
					out.print(c);
			} else if (c == '\n') {
				doPrintLine();
			} else if (c == '\r') {
				out.print("&#13;"); //$NON-NLS-1$
			} else
				out.print(c);
		}
	}

	/**
	 * Write text into the current element. The text has the CDATA feature.
	 * 
	 * @param text the text to write
	 */

	public void textCDATA(String text) {
		closeTextTag();
		if (text == null)
			return;

		// Write the text character-by-character to encode special characters.
		out.print("<![CDATA["); //$NON-NLS-1$

		if (!markLineNumber)
			out.print(text);
		else {
			int len = text.length();
			for (int i = 0; i < len; i++) {
				char c = text.charAt(i);
				if (c == '\n')
					doPrintLine();
				else
					out.print(c);
			}
		}

		out.print("]]>"); //$NON-NLS-1$
	}

	/**
	 * Writes a literal string. No translation is done on the string.
	 * 
	 * @param text the literal string
	 */

	public void literal(String text) {
		if (!markLineNumber) {
			out.print(text);
			return;
		}

		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == '\n')
				doPrintLine();
			else
				out.print(c);
		}
	}

	/**
	 * Implementaion method to close the tag for an element.
	 */

	private void closeTag() {
		if (!elementActive)
			return;
		elementActive = false;
		out.print(">"); //$NON-NLS-1$
		printLine();
	}

	/**
	 * Implementation method to close the tag for an element that is to contain
	 * text.
	 */

	protected final void closeTextTag() {
		if (!elementActive)
			return;
		elementActive = false;
		out.print(">"); //$NON-NLS-1$
	}

	/**
	 * Write text enclosed in the given element.
	 * 
	 * @param tag  the element tag that encloses the text
	 * @param text the text to write
	 */

	public final void taggedText(String tag, String text) {
		if (text == null)
			return;
		startElement(tag);
		text(text);
		endElement();
	}

	/**
	 * Conditionally starts an element. The element will appear in the file only if
	 * it, or one of its children, has content. The element will not appear if
	 * neither it, nor any of its children, have either an attribute or text. Use
	 * <code>endElement</code> to close the conditional element.
	 * 
	 * @param element element name
	 */

	public final void conditionalStartElement(String element) {
		pendingElementStack.push(element);
	}

	/**
	 * Writes an attribute using an HTML RGB value: #RRGGBB.
	 * 
	 * @param attrName attribute name
	 * @param rgb      RGB value
	 */

	public final void rgbAttribute(String attrName, int rgb) {
		assert elementActive;
		checkAttribute();
		out.print(" "); //$NON-NLS-1$
		out.print(attrName);
		out.print("=\""); //$NON-NLS-1$
		out.print(StringUtil.toRgbText(rgb));
		out.print("\""); //$NON-NLS-1$
	}

	/**
	 * Returns the line number.
	 * 
	 * @return the line number
	 */

	public final int getLineCounter() {
		return lineCounter;
	}

	/**
	 * Writes long text to the output.
	 * 
	 * @param text the text to write
	 */

	public void indentLongText(String text) {
		text(text);
	}

	/**
	 * Prints '\n', and plus the line counter.
	 */

	protected void doPrintLine() {
		out.print('\n');
		if (markLineNumber)
			lineCounter++;
	}

	/**
	 * Prints new line, and plus the line counter.
	 */
	protected void printLine() {
		doPrintLine();
	}

	public void writeBase64Text(String text) {
		assert text != null;

		closeTextTag();

		if (!markLineNumber) {
			out.print(text);
			return;
		}
		printLine();
		out.print(text);
		printLine();
	}
}
