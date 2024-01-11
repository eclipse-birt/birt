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
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.DataFormatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;
import org.eclipse.birt.report.engine.nLayout.area.style.TextStyle;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

import com.ibm.icu.text.Bidi;

/**
 * Definition of line area
 *
 * @since 3.3
 *
 */
public class LineArea extends InlineStackingArea {

	protected byte baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

	protected boolean setIndent = false;

	/**
	 * Constructor container absed
	 *
	 * @param parent
	 * @param context
	 */
	public LineArea(ContainerArea parent, LayoutContext context) {
		super(parent, context, null);
		assert (parent != null);
		isInInlineStacking = parent.isInInlineStacking;
	}

	/**
	 * Constructor area based
	 *
	 * @param area
	 */
	public LineArea(LineArea area) {
		super(area);
		this.baseLevel = area.baseLevel;
		this.isInlineStacking = true;
		this.isInInlineStacking = area.isInInlineStacking;
	}

	/**
	 * Set base level
	 *
	 * @param baseLevel
	 */
	public void setBaseLevel(byte baseLevel) {
		this.baseLevel = baseLevel;
	}

	@Override
	public void addChild(IArea area) {
		// FIXME ?
		int childHorizontalSpan = area.getX() + area.getWidth();
		int childVerticalSpan = area.getY() + area.getHeight();

		if (childHorizontalSpan > width) {
			setWidth(childHorizontalSpan);
		}

		if (childVerticalSpan > height) {
			setHeight(childVerticalSpan);
		}
		children.add(area);
	}

	@Override
	public void setTextIndent(ITextContent content) {
		if (currentIP == 0 && !setIndent && content != null) {
			IStyle contentStyle = content.getComputedStyle();
			currentIP = PropertyUtil.getDimensionValue(contentStyle.getProperty(StyleConstants.STYLE_TEXT_INDENT),
					maxAvaWidth);
			setIndent = true;
		}
	}

	/**
	 * Generate alignment of context
	 *
	 * @param endParagraph
	 * @param context
	 */
	public void align(boolean endParagraph, LayoutContext context) {
		assert (parent instanceof BlockContainerArea);
		CSSValue align = ((BlockContainerArea) parent).getTextAlign();

		// bidi_hcg: handle empty and justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(parent.content, align, endParagraph);
		boolean isCentered = CSSValueConstants.CENTER_VALUE.equals(align);
		boolean isJustified = CSSValueConstants.JUSTIFY_VALUE.equals(align) && !endParagraph;

		// single line
		int spacing = width - currentIP;
		spacing -= adjustSpacingForSoftHyphen();
		int adjustLeftWhiteSpace = ignoreLeftMostWhiteSpace();
		int adjustRightWhiteSpace = ignoreRightMostWhiteSpace();
		if ((isRightAligned)) {
			Iterator<IArea> iter = getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				if (parent.content.isDirectionRTL()) {
					area.setPosition(spacing + area.getX(), area.getY());
				} else {
					area.setPosition(spacing + area.getX() + adjustRightWhiteSpace, area.getY());
				}
			}
		} else if (isCentered) {
			Iterator<IArea> iter = getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				area.setPosition(spacing / 2 + area.getX() - adjustLeftWhiteSpace + adjustRightWhiteSpace, area.getY());
			}
		} else if (isJustified) {
			justify(spacing, adjustLeftWhiteSpace, adjustRightWhiteSpace);
		} else {
			// is left aligned
			if (parent.content != null && !parent.content.isDirectionRTL()) {
				if (adjustLeftWhiteSpace != 0) {
					String sp = null;
					DataFormatValue dfv = parent.content.getComputedStyle().getDataFormat();
					if (dfv != null) {
						sp = dfv.getStringPattern();
					}
					if (sp == null || !sp.startsWith("^")) {
						Iterator<IArea> iter = getChildren();
						while (iter.hasNext()) {
							AbstractArea area = (AbstractArea) iter.next();
							area.setPosition(area.getX() - adjustLeftWhiteSpace, area.getY());
						}
					} else {
						// Do not adjust (see https://github.com/eclipse-birt/birt/discussions/1536)
					}
				}
			}
		}
		if (context.getBidiProcessing()) {
			reorderVisually(this);
		}
		verticalAlign();
	}

	private int adjustSpacingForSoftHyphen() {
		if (lastTextArea != null) {
			int softHyphenWidth = lastTextArea.getSoftHyphenWidth();
			return softHyphenWidth;
		}
		return 0;
	}

	private int ignoreRightMostWhiteSpace() {
		if (lastTextArea != null) {
			String text = lastTextArea.getText();
			if (null != text) {
				char[] charArray = text.toCharArray();
				int len = charArray.length;
				while (len > 0 && (charArray[len - 1] <= ' ')) {
					len--;
				}
				if (len != charArray.length) {
					return lastTextArea.getTextWidth(text.substring(len));
				}
			}
		}
		return 0;
	}

	private int ignoreLeftMostWhiteSpace() {
		TextArea firstTextArea = findFirstNonEmptyTextArea(this);
		if (firstTextArea != null) {
			String text = firstTextArea.getText();
			if (null != text) {
				char[] charArray = text.toCharArray();
				int len = 0;
				while (len < charArray.length && (charArray[len] <= ' ')) {
					len++;
				}
				if (len > 0) {
					return firstTextArea.getTextWidth(text.substring(0, len));
				}
			}
		}
		return 0;
	}

	private int adjustWordSpacing(int wordSpacing, ContainerArea area) {
		if (wordSpacing == 0) {
			return 0;
		}
		Iterator<IArea> iter = area.getChildren();
		int delta = 0;
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				TextArea textArea = (TextArea) child;
				int whiteSpaceCount = textArea.getWhiteSpaceCount();
				if (whiteSpaceCount > 0) {
					TextStyle style = new TextStyle(textArea.getStyle());
					int original = style.getWordSpacing();
					style.setWordSpacing(original + wordSpacing);
					textArea.setStyle(style);
					int spacing = wordSpacing * whiteSpaceCount;
					child.setWidth(child.getWidth() + spacing);
					// If the text area is the first in the line,
					// and it starts with white space, then we must
					// remove that white space because otherwise this
					// results in the text starting to far right due
					// to a wordSpacing to the left.
					if (textArea.isFirstInLine()) {
						String text = textArea.getText();
						if (text != null && text.length() > 0) {
							for (int i = 0; i < text.length(); i++) {
								if (text.charAt(i) <= 32) {
									delta -= spacing;
								} else {
									break;
								}
							}
						}
					}
					child.setPosition(child.getX() + delta, child.getY());
					delta += spacing;
				}
			} else if (child instanceof ContainerArea) {
				child.setPosition(child.getX() + delta, child.getY());
				int spacing = adjustWordSpacing(wordSpacing, (ContainerArea) child);
				child.setWidth(child.getWidth() + spacing);
				delta += spacing;
			} else {
				child.setPosition(child.getX() + delta, child.getY());
			}
		}
		return delta;
	}

	private int adjustLetterSpacing(int letterSpacing, ContainerArea area) {
		Iterator<IArea> iter = area.getChildren();
		int delta = 0;
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				TextArea textArea = (TextArea) child;
				String text = textArea.getText();
				int letterSpacingNumber = (text.length() > 1 ? (text.length() - 1) : 0);
				TextStyle style = new TextStyle(textArea.getStyle());
				int original = style.getLetterSpacing();
				style.setLetterSpacing(original + letterSpacing);
				textArea.setStyle(style);
				int spacing = letterSpacing * letterSpacingNumber;
				child.setWidth(child.getWidth() + spacing);
				child.setPosition(child.getX() + delta, child.getY());
				delta += spacing;

			} else if (child instanceof ContainerArea) {
				child.setPosition(child.getX() + delta, child.getY());
				int spacing = adjustLetterSpacing(letterSpacing, (ContainerArea) child);
				child.setWidth(child.getWidth() + spacing);
				delta += spacing;
			} else {
				child.setPosition(child.getX() + delta, child.getY());
			}
		}
		return delta;
	}

	/**
	 * The first text area in a line.
	 */
	private TextArea firstTextArea = null;

	/**
	 * The last text area in a line.
	 */
	private TextArea lastTextArea = null;

	private static class IntTupleCounter {
		public int first;
		public int second;

		public IntTupleCounter() {
			first = 0;
			second = 0;
		}

		public void add(IntTupleCounter other) {
			first += other.first;
			second += other.second;
		}

	}

	/**
	 * Count the characters and white spaces.
	 *
	 * This is a recursive function.
	 *
	 * @param area
	 * @return
	 */
	private IntTupleCounter countCharactersAndWhiteSpace(ContainerArea area) {
		IntTupleCounter count = new IntTupleCounter();
		Iterator<IArea> iter = area.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				TextArea taChild = (TextArea) child;
				taChild.countCharactersAndWhiteSpace();
				count.first += taChild.getCharacterCount();
				count.second += taChild.getWhiteSpaceCount();
			} else if (child instanceof ContainerArea) {
				count.add(countCharactersAndWhiteSpace((ContainerArea) child));
			}
		}
		return count;
	}

	protected void justify(int spacing, int adjustLeftWhiteSpace, int adjustRightWhiteSpace) {
		// 1. Gets the white space number. The last white space of a line should not be
		// counted.
		// 2. adjust the position for every text area in the line and ignore the right
		// most white space by modifying the text.
		spacing = spacing + adjustLeftWhiteSpace + adjustRightWhiteSpace;
		if (adjustLeftWhiteSpace > 0) {
			if (!parent.content.isDirectionRTL()) {
				if (adjustLeftWhiteSpace != 0) {
					Iterator<IArea> iter = getChildren();
					while (iter.hasNext()) {
						AbstractArea area = (AbstractArea) iter.next();
						area.setPosition(area.getX() - adjustLeftWhiteSpace, area.getY());
					}
				}
			}
		}
		IntTupleCounter count = countCharactersAndWhiteSpace(this);
		int charactersCount = count.first;
		int whiteSpaceCount = count.second;
		if (whiteSpaceCount > 0) {
			int wordSpacing = spacing / whiteSpaceCount;
			adjustWordSpacing(wordSpacing, this);
		} else {
			if (charactersCount > 1) {
				int letterSpacing = spacing / (charactersCount - 1);
				adjustLetterSpacing(letterSpacing, this);
			}
		}

	}

	/**
	 * Puts container's child areas into the visual (display) order and repositions
	 * them following that order horizontally.
	 *
	 * @author Lina Kemmel
	 */
	private void reorderVisually(ContainerArea parent) {
		int n = parent.getChildrenCount();
		if (n == 0) {
			return;
		}

		int i = 0;
		AbstractArea[] areas = new AbstractArea[n];
		byte[] levels = new byte[n];

		Iterator<?> iter = parent.getChildren();
		for (; i < n && iter.hasNext(); i++) {
			AbstractArea area = (AbstractArea) iter.next();
			areas[i] = area;

			if (area instanceof TextArea) {
				levels[i] = (byte) ((TextArea) area).getRunLevel();
			} else {
				levels[i] = baseLevel;
				if (area instanceof InlineStackingArea) {
					// We assume that each inline container area should be
					// treated as an inline block-level element.
					reorderVisually((ContainerArea) area);
				}
			}
		}
		if (n > 1) {
			int x = areas[0].getAllocatedX();
			Bidi.reorderVisually(levels, 0, areas, 0, n);
			for (i = 0; i < n - 1; i++) {
				if (!areas[i].isIgnoreReordering()) {
					areas[i].setAllocatedPosition(x, areas[i].getAllocatedY());
					x += areas[i].getAllocatedWidth();
				}
			}
			if (!areas[i].isIgnoreReordering()) {
				areas[i].setAllocatedPosition(x, areas[i].getAllocatedY());
			}
		}
	}

	@Override
	public void endLine(boolean endParagraph) throws BirtException {

		close(false, endParagraph);
		// initialize( );
		currentIP = 0;
		if (endParagraph) {
			setIndent = false;
		}
	}

	@Override
	public int getMaxLineWidth() {
		return maxAvaWidth;
	}

	@Override
	public boolean isEmptyLine() {
		return getChildrenCount() == 0;
	}

	@Override
	public void update(AbstractArea area) throws BirtException {
		int aWidth = area.getAllocatedWidth();
		if (aWidth + currentIP > maxAvaWidth) {
			removeChild(area);
			endLine(false);
			children.add(area);
		}
		area.setAllocatedPosition(currentIP, currentBP);
		currentIP += aWidth;
		int height = area.getAllocatedHeight();
		if (height > getHeight()) {
			this.height = height;
		}
	}

	protected void close(boolean isLastLine, boolean endParagraph) throws BirtException {
		if (children.size() == 0) {
			return;
		}

		// Mark the first TextArea as "firstInLine".
		firstTextArea = findFirstNonEmptyTextArea(this);
		if (firstTextArea != null) {
			firstTextArea.markAsFirstInLine();
		}

		// Handle Soft Hyphen:
		// Mark the last TextArea as "lastInLine".
		lastTextArea = findLastNonEmptyTextArea(this);
		if (lastTextArea != null) {
			lastTextArea.markAsLastInLine();
		}

		int lineHeight = ((BlockContainerArea) parent).getLineHeight();
		if (lineHeight != 0) {
			height = lineHeight;
		}
		width = Math.max(currentIP, maxAvaWidth);
		align(endParagraph, context);
		checkDisplayNone();
		if (isLastLine) {
			parent.add(this);
			checkPageBreak();
			parent.update(this);
			this.finished = true;
		} else {
			LineArea area = cloneArea();
			area.children = children;
			area.context = context;
			area.setParent(parent);
			Iterator<IArea> iter = area.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				child.setParent(area);
			}
			children = new ArrayList<IArea>();
			parent.add(area);
			area.checkPageBreak();
			parent.update(area);
			area.finished = true;
			// setPosition(parent.currentIP + parent.getOffsetX( ),
			// parent.getOffsetY() + parent.currentBP);
			height = 0;
			this.baseLine = 0;
		}
	}

	@Override
	public void close() throws BirtException {
		close(true, true);
		finished = true;
	}

	@Override
	public void initialize() throws BirtException {
		hasStyle = false;
		boxStyle = BoxStyle.DEFAULT;
		localProperties = LocalProperties.DEFAULT;
		maxAvaWidth = parent.getCurrentMaxContentWidth();
		width = maxAvaWidth;
		// Derive the baseLevel from the parent content direction.
		if (parent.content != null) {
			// IContent#isDirectionRTL already looks at computed style
			if (parent.content.isDirectionRTL()) {
				baseLevel = Bidi.DIRECTION_RIGHT_TO_LEFT;
			}
		}
		// parent.add( this );
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		assert (height < this.height);
		LineArea result = null;
		Iterator<IArea> iter = children.iterator();
		while (iter.hasNext()) {
			ContainerArea child = (ContainerArea) iter.next();

			if (child.getMinYPosition() <= height) {
				iter.remove();
				if (result == null) {
					result = cloneArea();
				}
				result.addChild(child);
				child.setParent(result);
			} else {
				SplitResult splitChild = child.split(height - child.getY(), force);
				ContainerArea splitChildArea = splitChild.getResult();
				if (splitChildArea != null) {
					if (result == null) {
						result = cloneArea();
					}
					result.addChild(splitChildArea);
					splitChildArea.setParent(result);
				} else {
					// reset the line height due to negative height caused by page break.
					// Otherwise the first line could be rendered too far down.
					// See (old) BIRT bug 562873 and
					// https://github.com/eclipse-birt/birt/issues/1443.
					if (height < 0) {
						height = 0;
					}
					child.setY(Math.max(0, child.getY() - height));
				}
			}

		}

		if (result != null) {
			int h = 0;
			iter = result.getChildren();
			while (iter.hasNext()) {
				ContainerArea child = (ContainerArea) iter.next();
				h = Math.max(h, child.getAllocatedHeight());
			}
			result.setHeight(h);
		}

		if (children.size() > 0) {
			int h = 0;
			iter = getChildren();
			while (iter.hasNext()) {
				ContainerArea child = (ContainerArea) iter.next();
				h = Math.max(h, child.getAllocatedHeight());
			}
			setHeight(h);
		}
		if (result != null) {
			return new SplitResult(result, SplitResult.SPLIT_SUCCEED_WITH_PART);
		}
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public LineArea cloneArea() {
		return new LineArea(this);
	}

	@Override
	public SplitResult splitLines(int lineCount) {
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public boolean isPageBreakAfterAvoid() {
		return false;
	}

	@Override
	public boolean isPageBreakBeforeAvoid() {
		return false;
	}

	@Override
	public boolean isPageBreakInsideAvoid() {
		return false;
	}

	/**
	 * Gets the last TextArea actually containing some text.
	 *
	 * This is a recursive function. It is first called with this LineArea itself.
	 *
	 */
	private TextArea findLastNonEmptyTextArea(ContainerArea area) {
		TextArea last = null;
		TextArea candidate;
		Iterator<IArea> iter = area.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				candidate = (TextArea) child;
				if (candidate.textLength > 0) {
					last = candidate;
				}
			} else if (child instanceof ContainerArea) {
				candidate = findLastNonEmptyTextArea((ContainerArea) child);
				if (candidate != null) {
					last = candidate;
				}
			}
		}
		return last;
	}

	/**
	 * Gets the first TextArea actually containing some text.
	 *
	 * This is a recursive function. It is first called with this LineArea itself.
	 *
	 */
	private TextArea findFirstNonEmptyTextArea(ContainerArea area) {
		Iterator<IArea> iter = area.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				if (((TextArea) child).textLength > 0) {
					return (TextArea) child;
				}
			} else if (child instanceof ContainerArea) {
				TextArea candidate = findFirstNonEmptyTextArea((ContainerArea) child);
				if (candidate != null) {
					return candidate;
				}
			}
		}
		return null;
	}

}
