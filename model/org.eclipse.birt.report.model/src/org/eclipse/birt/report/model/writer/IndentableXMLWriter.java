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

package org.eclipse.birt.report.model.writer;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

import org.eclipse.birt.report.model.util.XMLWriter;

/**
 * Writes the XML file with indents.
 *
 */

public class IndentableXMLWriter extends XMLWriter {

	/**
	 * Maximum characters in one line.
	 */

	protected final static int MAX_CHARS_PER_LINE = 80;

	/**
	 * Platform-independent line separator.
	 */

	public static final String LINE_SEPARATOR = "\n"; //$NON-NLS-1$

	/**
	 * The indent space.
	 */

	protected static final String TAB = "    "; //$NON-NLS-1$

	/**
	 * The indents which are cached for writing.
	 */

	protected ArrayList<String> cachedIndents = new ArrayList<>();

	/**
	 * The name of the tag that is being written.
	 */

	private String lastTagName = null;

	/**
	 * Constructor.
	 *
	 * @param outputFile the file to write
	 * @param signature  the unicode signature of the design file
	 * @throws java.io.IOException if write error occurs
	 */

	public IndentableXMLWriter(File outputFile, String signature) throws IOException {
		super(outputFile, signature);
	}

	/**
	 * Constructor.
	 *
	 * @param outputFile         the file to write
	 * @param signature          the unicode signature of the design file
	 * @param needMarkLineNumber control flag, whether need mark line number
	 * @throws java.io.IOException if write error occurs
	 */

	public IndentableXMLWriter(File outputFile, String signature, boolean needMarkLineNumber) throws IOException {
		super(outputFile, signature, needMarkLineNumber);
	}

	/**
	 * Constructor.
	 *
	 * @param os        the output stream to which the design file is written.
	 * @param signature the unicode signature of the design file
	 * @throws IOException if write error occurs
	 */

	public IndentableXMLWriter(OutputStream os, String signature) throws IOException {
		super(os, signature);
	}

	/**
	 * Constructor.
	 *
	 * @param os                 the output stream to which the design file is
	 *                           written.
	 * @param signature          the unicode signature of the design file
	 * @param signature          the unicode signature of the design file
	 * @param needMarkLineNumber
	 * @throws IOException if write error occurs
	 */

	public IndentableXMLWriter(OutputStream os, String signature, boolean needMarkLineNumber) throws IOException {
		super(os, signature, needMarkLineNumber);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.util.XMLWriter#emitStartTag(java.lang.String )
	 */

	protected IndentableXMLWriter() {
	}

	@Override
	protected void emitStartTag(String tagName) {
		// Record the current tag name for endDocument()

		lastTagName = tagName;

		literal(getIndent(elementStack.size()));

		super.emitStartTag(tagName);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.util.XMLWriter#endElement()
	 */

	@Override
	public void endElement() {
		String tagName = ""; //$NON-NLS-1$

		// Get the tag name from the top of stack

		if (!elementStack.isEmpty()) {
			tagName = elementStack.peek();
		}

		// No indent for the leaf nodes on the desing tree. Like <property
		// name="height">x</property>, <freeform name="freeform1"/>

		if (!elementStack.isEmpty() && !tagName.equalsIgnoreCase(lastTagName) && (pendingElementStack.isEmpty()
				|| (!pendingElementStack.isEmpty() && !tagName.equals(pendingElementStack.peek())))) {
			literal(getIndent(elementStack.size() - 1));
		}

		// currentTagName is the last tag that was written to the output stream.
		// It is a trick to set the currentTagName before the
		// super.endElement(). So that it can show all tabs correctly.

		lastTagName = tagName;

		super.endElement();
	}

	/**
	 * Returns the indent space for the given level. The level should be step one
	 * each time.
	 *
	 * @param level the indent level
	 * @return the indent space
	 */

	private String getIndent(int level) {
		String indent = ""; //$NON-NLS-1$

		if (cachedIndents.size() == 0) {
			cachedIndents.add(indent);
		} else if (cachedIndents.size() <= level) {
			indent = cachedIndents.get(cachedIndents.size() - 1);
			indent += TAB;
			cachedIndents.add(indent);
		} else {
			indent = cachedIndents.get(level);
		}

		return indent;
	}

	/**
	 * Returns tabs for the current element.
	 *
	 * @return the indents for the current XML element.
	 */

	private String getCurElementIndent() {
		if (cachedIndents.size() == 0) {
			return ""; //$NON-NLS-1$
		}

		String indent = cachedIndents.get(elementStack.size() - 1);
		return indent;
	}

	/**
	 * Writes long text to the output.
	 *
	 * @param text the text to write
	 */

	@Override
	public void indentLongText(String text) {
		assert text != null;
		assert text.length() >= MAX_CHARS_PER_LINE;

		closeTextTag();
		String curTabs = getCurElementIndent().concat(TAB);
		literal(LINE_SEPARATOR + curTabs);

		// Write the text character-by-character to encode special characters.

		int len = text.length();
		for (int i = 0; i < len; i++) {
			char c = text.charAt(i);
			if (c == '&') {
				literal("&amp;"); //$NON-NLS-1$
			} else if (c == '<') {
				literal("&lt;"); //$NON-NLS-1$
			} else {
				literal(Character.toString(c));
			}

			// append CRLF to the end of the line.

			if ((i + 1 != len) && (i + 1) % MAX_CHARS_PER_LINE == 0) {
				literal(LINE_SEPARATOR + curTabs);
			}
		}

		literal(LINE_SEPARATOR + getCurElementIndent());

	}

	@Override
	public void writeBase64Text(String text) {
		assert (text != null);

		closeTextTag();
		if (!markLineNumber) {
			out.print(text);
			return;
		}

		printLine();

		String tabs = getCurElementIndent().concat(TAB);
		int length = text.length();
		int lineStart = 0;
		while (lineStart < length) {
			int lineEnd = lineStart + MAX_CHARS_PER_LINE;
			if (lineEnd > length) {
				lineEnd = length;
			}
			out.print(tabs);
			out.print(text.substring(lineStart, lineEnd));
			printLine();
			lineStart = lineEnd;
		}
	}
}
