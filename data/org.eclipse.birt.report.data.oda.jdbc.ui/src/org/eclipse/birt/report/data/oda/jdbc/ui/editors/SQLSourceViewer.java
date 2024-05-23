/*******************************************************************************
 * Copyright (c) 2024 Thomas Gutmann.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 *
 * Contributors:
 *  Thomas Gutmann  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.data.oda.jdbc.ui.editors;

import org.eclipse.jface.text.IFindReplaceTarget;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.widgets.Composite;

/**
 * Extended standard source viewer for query text search
 *
 * @since 4.16
 *
 */
public class SQLSourceViewer extends SourceViewer {

	/**
	 * Constructor
	 *
	 * @param parent parent element
	 * @param ruler  vertical ruler
	 * @param styles editor style
	 */
	public SQLSourceViewer(Composite parent, IVerticalRuler ruler, int styles) {
		super(parent, ruler, styles);
	}

	/**
	 * Get the current cursor position from source viewer
	 *
	 * @return the current cursor position
	 */
	public int getCurrentCaretPosition() {
		return this.getTextWidget().getCaretOffset();
	}

	/**
	 * Find query text and mark
	 *
	 * @param findQueryText          query text to be searched
	 * @param forwardSearch          search is forward search (or backward search)
	 * @param queryTextCaseSensitive search is case sensitive
	 * @param queryTextWholeWord     the text is searched as a whole word
	 * @return query text found
	 */
	public boolean findQueryText(String findQueryText, boolean forwardSearch, boolean queryTextCaseSensitive,
			boolean queryTextWholeWord) {

		boolean textFound = false;
		int queryTextLength = this.getTextWidget().getText().length();
		int startPosition = getCurrentCaretPosition();
		int searchPositionFound = -1;

		ITextSelection selection = (ITextSelection) this.getSelectionProvider().getSelection();
		if (!forwardSearch && selection.getLength() > 0)
			startPosition -= selection.getLength();

		IFindReplaceTarget frt = this.getFindReplaceTarget();
		if (frt != null && frt.canPerformFind() && findQueryText != null) {

			int textLength = findQueryText.length();
			if (textLength > 0) {

				searchPositionFound = frt.findAndSelect(startPosition, findQueryText, forwardSearch,
						queryTextCaseSensitive,
						queryTextWholeWord);

				if (searchPositionFound >= 0) {
					textFound = true;

					// set the caret
					if ((searchPositionFound + textLength) <= queryTextLength) {
						this.getTextWidget().setCaretOffset(searchPositionFound + textLength);
					}

					// set the selection
					int[] selectionRange = { searchPositionFound, textLength };
					this.validateSelectionRange(selectionRange);
					if (selectionRange[0] > 0) {
						this.setSelectedRange(searchPositionFound, textLength);
					}
				}
			}
		}
		return textFound;
	}

}
