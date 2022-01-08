/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.nLayout.area.impl;

import org.eclipse.birt.report.engine.content.ITextContent;

/**
 * this listener is only invoked in HTML render phase
 */
public class InlineTextRenderListener implements ITextListener {
	private int textStartPos = -1;
	private int textLength = 0;

	private int readTextLength = 0;

	private boolean listeningStatus = false;

	private int offset = 0;
	private int dimension = 0;

	private InlineTextArea inlineContainer = null;

	public InlineTextRenderListener(InlineTextArea inlineContainer, int offset, int dimension) {
		this.inlineContainer = inlineContainer;
		this.offset = offset;
		this.dimension = dimension;
		onNewLineEvent();
	}

	public int getTextStart() {
		return textStartPos;
	}

	public int getTextLength() {
		return textLength;
	}

	public void onAddEvent(TextArea textArea) {
		if (listeningStatus) {
			if (textStartPos == -1) {
				textStartPos = textArea.offset;
			}
			readTextLength = readTextLength + textArea.textLength;
		}
	}

	int lastTotalWidth = 0;

	public void onNewLineEvent() {
		lastTotalWidth += inlineContainer.getAllocatedWidth();
		if (lastTotalWidth < offset || lastTotalWidth > offset + dimension) {
			listeningStatus = false;
		} else {
			listeningStatus = true;
			textLength = readTextLength;
		}
	}

	public void onTextEndEvent() {
		if (listeningStatus) {
			textLength = readTextLength;
		}
	}

	public String getSplitText() {
		ITextContent textContent = (ITextContent) inlineContainer.content;
		if (textStartPos == -1 || textLength == 0) {
			return "";
		} else {
			String splitText = textContent.getText().substring(textStartPos, textStartPos + textLength);
			return splitText;
		}
	}
}
