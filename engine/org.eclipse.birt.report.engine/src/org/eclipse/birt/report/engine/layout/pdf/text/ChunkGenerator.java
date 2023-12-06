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

package org.eclipse.birt.report.engine.layout.pdf.text;

import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.layout.pdf.ISplitter;
import org.eclipse.birt.report.engine.layout.pdf.font.FontMappingManager;
import org.eclipse.birt.report.engine.layout.pdf.font.FontSplitter;

import com.ibm.icu.text.Bidi;

/**
 * Class to generate data chunks / text segments
 *
 * @since 3.3
 *
 */
public class ChunkGenerator {
	private FontMappingManager fontManager;
	private ITextContent textContent;
	private boolean bidiProcessing;
	private boolean fontSubstitution;
	private String text;

	private ISplitter bidiSplitter = null;
	private ISplitter fontSplitter = null;

	/**
	 * Constructor
	 *
	 * @param fontManager      font manager
	 * @param textContent      text content
	 * @param bidiProcessing   bidi processing flag
	 * @param fontSubstitution is font substitution used flag
	 */
	public ChunkGenerator(FontMappingManager fontManager, ITextContent textContent, boolean bidiProcessing,
			boolean fontSubstitution) {
		this.fontManager = fontManager;
		this.textContent = textContent;
		this.text = textContent.getText();
		this.bidiProcessing = bidiProcessing;
		this.fontSubstitution = fontSubstitution;

		if (text == null || text.length() == 0) {
			return;
		}
		if (bidiProcessing) {
			// FIXME implement the getDirection() method in ComputedStyle.
			if (CSSConstants.CSS_RTL_VALUE.equals(textContent.getComputedStyle().getDirection())) {
				bidiSplitter = new BidiSplitter(
						new Chunk(text, 0, Bidi.DIRECTION_RIGHT_TO_LEFT, Bidi.DIRECTION_RIGHT_TO_LEFT));
			} else {
				bidiSplitter = new BidiSplitter(
						new Chunk(text, 0, Bidi.DIRECTION_LEFT_TO_RIGHT, Bidi.DIRECTION_LEFT_TO_RIGHT));
			}
		}

		if (null == bidiSplitter) {
			fontSplitter = new FontSplitter(fontManager, new Chunk(text), textContent, fontSubstitution);
		} else if (bidiSplitter.hasMore()) {
			fontSplitter = new FontSplitter(fontManager, bidiSplitter.getNext(), textContent, fontSubstitution);
		}

	}

	/**
	 * Has more elements
	 *
	 * @return Return the validation result of more elements
	 */
	public boolean hasMore() {
		if (text == null || text.length() == 0) {
			return false;
		}
		if (bidiProcessing) {
			if (null == bidiSplitter) {
				return false;
			}
			if (bidiSplitter.hasMore()) {
				return true;
			}
		}
		if (null == fontSplitter) {
			return false;
		}
		if (fontSplitter.hasMore()) {
			return true;
		}
		return false;
	}

	/**
	 * Get the next text part
	 *
	 * @return Return the next text part
	 */
	public Chunk getNext() {
		while (null != fontSplitter) {
			if (fontSplitter.hasMore()) {
				return fontSplitter.getNext();
			}
			fontSplitter = null;
			if (null != bidiSplitter && bidiSplitter.hasMore()) {
				fontSplitter = new FontSplitter(fontManager, bidiSplitter.getNext(), textContent, fontSubstitution);
			} else {
				return null;
			}
		}
		return null;
	}
}
