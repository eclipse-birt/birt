/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.report.designer.internal.ui.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Element;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * 
 */

public class RTFParser {

	public static void parse(String rtfString, RTFDocumentHandler handler) throws IOException, BadLocationException {
		RTFEditorKit rtfeditorkit = new RTFEditorKit();
		DefaultStyledDocument document = new DefaultStyledDocument();
		ByteArrayInputStream bytearrayinputstream = new ByteArrayInputStream(rtfString.getBytes());
		rtfeditorkit.read(bytearrayinputstream, document, 0);
		Element element = document.getDefaultRootElement();
		parseElement(document, element, handler, true);
	}

	private static void parseElement(DefaultStyledDocument document, Element parent, RTFDocumentHandler handler,
			boolean lostLast) {
		for (int i = 0; i < parent.getElementCount(); i++) {
			if (lostLast && i == parent.getElementCount() - 1 && parent.getElementCount() != 1) {
				break;
			}
			Element element = parent.getElement(i);
			AttributeSet attributeset = element.getAttributes();
			handler.startElement(element.getName(), attributeset);
			if (element.getName().equalsIgnoreCase("content")) {
				try {
					int start = element.getStartOffset();
					int end = element.getEndOffset();
					String s = document.getText(start, end - start);
					handler.content(s);
				} catch (BadLocationException e) {
				}
			}
			parseElement(document, element, handler, false);
			handler.endElement(element.getName());
		}
	}
}
