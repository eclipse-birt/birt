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

package org.eclipse.birt.report.engine.emitter.pdf;

import java.awt.Color;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.engine.api.script.instance.IScriptStyle;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfOutline;

/**
 * Class of PDF TOC handler
 *
 * @since 3.3
 *
 */
public class TOCHandler {

	/**
	 * The TOC node list.
	 */
	protected TOCNode root;
	/**
	 * The Pdf outline.
	 */
	protected PdfOutline outline;
	/**
	 * All bookMarks created during PDF rendering.
	 */
	protected Set<String> bookmarks;
	/**
	 * The counter to indicate how many pdf outline has been created.
	 */
	private long counter = 0;
	/**
	 * The max number of pdf outline.
	 */
	private static final long MAX_COUNT = 70000l;

	/**
	 * The constructor.
	 *
	 * @param root      The TOC node in which need to build PDF outline
	 * @param outline   pdf outline
	 * @param bookmarks bookmarks of the pdf
	 */
	public TOCHandler(TOCNode root, PdfOutline outline, Set<String> bookmarks) {
		this.root = root;
		this.outline = outline;
		this.bookmarks = bookmarks;
	}

	/**
	 * @deprecated get the root of the TOC tree.
	 * @return The TOC root node
	 */
	@Deprecated
	public TOCNode getTOCRoot() {
		return this.root;
	}

	/**
	 * Create the TOC of the PDF
	 */
	public void createTOC() {
		createTOC(root, outline, bookmarks);
	}

	/**
	 * create a PDF outline for tocNode, using the pol as the parent PDF outline.
	 *
	 * @param tocNode   The tocNode whose children need to build a PDF outline tree
	 * @param pol       The parent PDF outline for these children
	 * @param bookmarks All bookMarks created during rendering
	 */
	protected void createTOC(TOCNode tocNode, PdfOutline pol, Set<String> bookmarks) {
		if (isOutlineSizeOverflow() || null == tocNode || null == tocNode.getChildren()) {
			return;
		}
		for (Iterator<?> i = tocNode.getChildren().iterator(); i.hasNext();) {
			TOCNode node = (TOCNode) i.next();
			if (!bookmarks.contains(node.getBookmark())) {
				createTOC(node, outline, bookmarks);
				continue;
			}
			PdfOutline outline = new PdfOutline(pol, PdfAction.gotoLocalPage(node.getBookmark(), false),
					node.getDisplayString());
			countOutlineSize();
			IScriptStyle style = node.getTOCStyle();
			String color = style.getColor();
			if (color != null) {
				color = color.toLowerCase();
			}
			Color awtColor = PropertyUtil.getColor(color);
			if (awtColor != null) {
				outline.setColor(awtColor);
			}
			String fontStyle = style.getFontStyle();
			String fontWeight = style.getFontWeight();
			int styleValue = PropertyUtil.getFontStyle(fontStyle, fontWeight);
			outline.setStyle(styleValue);
			createTOC(node, outline, bookmarks);
		}
	}

	protected boolean isOutlineSizeOverflow() {
		return counter > MAX_COUNT;
	}

	protected void countOutlineSize() {
		counter++;
	}
}
