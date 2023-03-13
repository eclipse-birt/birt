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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 * In run task, the inline text area need to record
 * <ul>
 * <li>floatPos</li>
 * <li>offsetInContent.</li>
 * <li>dimension</li>
 * <li>widthRestrict</li>
 * </ul>
 * to generate page hint.
 *
 */
public class InlineTextExtension {
	private ArrayList<InlineTextArea> lines = new ArrayList<>();

	private ArrayList<Integer> lineBreaks = new ArrayList<>();

	/**
	 * @see SizeBasedContent#floatPos
	 */
	private int floatPos;
	/**
	 * @see SizeBasedContent#offsetInContent
	 */
	private int offsetInContent;
	/**
	 * @see SizeBasedContent#dimension
	 */
	private int dimension = 0;
	/**
	 * @see SizeBasedContent#width
	 */
	private int widthRestrict;

	private boolean firstTimeEnter = true;

	/**
	 * when generating page hint, inline text must invoke this method first.
	 */
	public void updatePageHintInfo(InlineTextArea area) {
		// if the area is in the repeated header, its page hint info should only
		// be updated for one time.
		// And then we should not update the page hint info any more.
		// Because the cloned area will not be in the lines. And the page hint
		// info should not change any more as the area is in repeated header.
		if (lineBreaks == null || lineBreaks.size() == 0 || (!firstTimeEnter && area.isInRepeatedHeader())) {
			return;
		}

		if (firstTimeEnter) {
			firstTimeEnter = false;
		}

		offsetInContent = 0;
		floatPos = 0;
		dimension = 0;
		widthRestrict = 0;

		int lineNumber = lines.indexOf(area);
		// if current inlineText line is not the first line in current page for
		// the textContent, just ignore it.
		if (lineNumber != 0 && !lineBreaks.contains(lineNumber - 1)) {
			return;
		}

		Collections.sort(lineBreaks);
		int startLineNumber = lineNumber;

		for (int i = 0; i < startLineNumber; i++) {
			offsetInContent += lines.get(i).getAllocatedWidth();
		}

		for (Iterator<Integer> iter = lineBreaks.iterator(); iter.hasNext();) {
			int breakLineNumber = iter.next();
			if (breakLineNumber >= startLineNumber) {
				InlineTextArea startLine = lines.get(startLineNumber);
				floatPos = startLine.getAllocatedX();
				for (int i = startLineNumber; i <= breakLineNumber; i++) {
					dimension += lines.get(i).getAllocatedWidth();
					widthRestrict = startLine.parent.getWidth();
				}
				break;
			}
		}
	}

	public void addLine(InlineTextArea area) {
		lines.add(area);
	}

	public void replaceLine(InlineTextArea oldArea, InlineTextArea newArea) {
		int lineNumber = lines.indexOf(oldArea);
		lines.remove(lineNumber);
		lines.add(lineNumber, newArea);
	}

	public void addLineBreak(InlineTextArea area) {
		int lineNumber = lines.indexOf(area);
		lineBreaks.add(lineNumber);
	}

	public void addLineBreak() {
		int lineNumber = lines.size() - 1;
		lineBreaks.add(lineNumber);
	}

	public int getFloatPos() {
		return floatPos;
	}

	public int getOffsetInContent() {
		return offsetInContent;
	}

	public int getDimension() {
		return dimension;
	}

	public int getWidthRestrict() {
		return widthRestrict;
	}

}
