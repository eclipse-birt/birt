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

package org.eclipse.birt.report.engine.layout;

import org.eclipse.birt.report.engine.layout.area.IArea;

public interface ITextLayoutManager {

	/**
	 * This method is for inline text only. If a inline text has a left or right
	 * padding/border/margin, the space holders keeps space for the
	 * padding/border/margin when layouting.
	 *
	 * @param area the inline container which contains the padding/border/margin.
	 */
	void addSpaceHolder(IArea area);

	/**
	 * Tells the TextLayoutManager to add a line of text into its parent.
	 *
	 * @param area for block text, it is the line of text itself. for inline text,
	 *             it is the inline container which contains the line of text.
	 */
	void addTextLine(IArea area);

	/**
	 * Tells the TextLayoutManager to start a new line.
	 *
	 */
	void newLine();

	/**
	 * Returns the remain space available to add new areas in current line.
	 */
	int getFreeSpace();

	/**
	 * Returns true if current text content exceeds current page; otherwise, returns
	 * false.
	 */
	boolean needPause();
}
