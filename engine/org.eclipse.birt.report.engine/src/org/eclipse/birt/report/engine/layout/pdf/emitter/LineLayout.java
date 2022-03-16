/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 * IBM Corporation - Bidi reordering implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.FloatValue;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.area.impl.TextArea;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSPrimitiveValue;

import com.ibm.icu.text.Bidi;

public class LineLayout extends InlineStackingLayout implements IInlineStackingLayout {

	/**
	 * the base-level of the line created by this layout manager. each LineArea has
	 * a fixed base-level.
	 */
	private byte baseLevel = Bidi.DIRECTION_LEFT_TO_RIGHT;

	/**
	 * line counter
	 */
	protected int lineCount = 0;

	/**
	 * current position in current line
	 */
	protected int currentPosition = 0;

	protected boolean breakAfterRelayout = false;

	protected boolean lineFinished = true;

	protected HashMap positionMap = new HashMap();

	protected ContainerArea last = null;

	protected int expectedIP = 0;

	protected IReportItemExecutor unfinishedExecutor = null;

	protected boolean isEmpty = true;

	protected int lineHeight;

	public LineLayout(LayoutEngineContext context, ContainerLayout parent) {
		super(context, parent, null);
		isInBlockStacking = false;
	}

	@Override
	protected void createRoot() {
		currentContext.root = AreaFactory.createLineArea(context.getReport());
		lineCount++;
	}

	@Override
	protected void initialize() {
		int currentIP = 0;
		if (contextList.size() > 0) {
			currentIP = contextList.get(contextList.size() - 1).currentIP;
		}
		currentContext = new ContainerContext();
		currentContext.currentIP = currentIP;
		contextList.add(currentContext);
		createRoot();
		currentContext.maxAvaWidth = parent.getCurrentMaxContentWidth();
		currentContext.maxAvaHeight = parent.getCurrentMaxContentHeight();
		currentContext.root.setWidth(parent.getCurrentMaxContentWidth());
		lineHeight = ((BlockStackingLayout) parent).getLineHeight();

		// Derive the baseLevel from the parent content direction.
		if (parent.content != null) {
			if (CSSConstants.CSS_RTL_VALUE.equals(parent.content.getComputedStyle().getDirection())) {
				baseLevel = Bidi.DIRECTION_RIGHT_TO_LEFT;
			}
		}
	}

	@Override
	public void setTextIndent(ITextContent content) {
		if (isEmpty) {
			if (content != null) {
				IStyle contentStyle = content.getComputedStyle();
				currentContext.currentIP = getDimensionValue(contentStyle.getProperty(StyleConstants.STYLE_TEXT_INDENT),
						currentContext.maxAvaWidth);
			}
		}
	}

	@Override
	public boolean endLine() throws BirtException {
		closeLayout(false);
		initialize();
		currentContext.currentIP = 0;
		return true;
	}

	/**
	 * submit current line to parent true if succeed
	 *
	 * @return
	 */
	/*
	 * protected boolean endLine( ContainerContext currentContext, int index ) {
	 * currentContext.root.setHeight( Math.max( currentContext.root.getHeight( ),
	 * lineHeight ) ); align(currentContext, false );
	 * if(currentContext.root.getChildrenCount( )>0) { int size =
	 * parent.contextList.size(); parent.addAreaFromLast( currentContext.root, index
	 * ); } return true; }
	 */

	/*
	 * protected void closeLayout(ContainerContext currentContext, int index,
	 * boolean finished ) { if ( currentContext.root.getChildrenCount( ) == 0 ) {
	 * lineCount--; return; } currentContext.root.setHeight( Math.max(
	 * currentContext.root.getHeight( ), lineHeight ) ); align( currentContext, true
	 * ); parent.addArea( currentContext.root, index ); }
	 */

	@Override
	protected void closeLayout() throws BirtException {
		closeLayout(true);
	}

	protected void closeLayout(boolean isLastLine) throws BirtException {
		int size = contextList.size();
		if (size == 1) {
			currentContext = contextList.removeFirst();
			currentContext.root.setHeight(Math.max(currentContext.root.getHeight(), lineHeight));
			if (currentContext.root.getChildrenCount() > 0) {
				align(currentContext, isLastLine);
				boolean succeed = parent.addArea(currentContext.root, parent.contextList.size() - 1);
				if (succeed) {
				} else {
					parent.autoPageBreak();
					parent.addToRoot(currentContext.root, parent.contextList.size() - 1);
					if (isInBlockStacking) {
						if (parent.contextList.size() > 1) {
							parent.closeExcludingLast();
						}
					}
				}
			}
		} else {
			for (int i = 0; i < size; i++) {
				currentContext = contextList.removeFirst();
				if (currentContext.root.getChildrenCount() > 0) {
					parent.addToRoot(currentContext.root, i);
				}
			}
			if (parent.isInBlockStacking) {
				parent.closeLayout();
			}
		}
	}

	@Override
	public void addToRoot(AbstractArea area) {
		area.setAllocatedPosition(currentContext.currentIP, currentContext.currentBP);
		currentContext.currentIP += area.getAllocatedWidth();

		if (currentContext.currentIP > currentContext.root.getWidth()) {
			currentContext.root.setWidth(currentContext.currentIP);
		}
		int height = area.getAllocatedHeight();
		if (currentContext.currentBP + height > currentContext.root.getHeight()) {
			currentContext.root.setHeight(currentContext.currentBP + height);
		}
		currentContext.root.addChild(area);
		isEmpty = false;
		lineFinished = false;
	}

	protected void align(ContainerContext currentContext, boolean lastLine) {
		assert (parent instanceof BlockStackingLayout);
		String align = ((BlockStackingLayout) parent).getTextAlign();

		// bidi_hcg: handle empty and justify align in RTL direction as right
		// alignment
		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(((BlockStackingLayout) parent).content, align,
				lastLine);

		// single line
		if ((isRightAligned || CSSConstants.CSS_CENTER_VALUE.equalsIgnoreCase(align))) {
			int spacing = currentContext.root.getWidth() - currentContext.currentIP;
			Iterator iter = currentContext.root.getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				if (spacing > 0) {
					// if ( CSSConstants.CSS_RIGHT_VALUE.equalsIgnoreCase( align ) )
					if (isRightAligned) {
						area.setAllocatedPosition(spacing + area.getAllocatedX(), area.getAllocatedY());
					} else if (CSSConstants.CSS_CENTER_VALUE.equalsIgnoreCase(align)) {
						area.setAllocatedPosition(spacing / 2 + area.getAllocatedX(), area.getAllocatedY());
					}
				}
			}

		} else if (CSSConstants.CSS_JUSTIFY_VALUE.equalsIgnoreCase(align) && !lastLine) {
			justify(currentContext);
		}
		if (context.getBidiProcessing()) {
			reorderVisually(currentContext.root);
		}
		verticalAlign();
	}

	private int adjustWordSpacing(int wordSpacing, ContainerArea area) {
		Iterator iter = area.getChildren();
		int delta = 0;
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				String text = ((TextArea) child).getText();
				int blankNumber = text.split(" ").length - 1;
				if (blankNumber > 0) {
					IStyle style = child.getStyle();
					int original = getDimensionValue(style.getProperty(StyleConstants.STYLE_WORD_SPACING));
					IStyle areaStyle = new AreaStyle((AbstractStyle) style);
					areaStyle.setProperty(StyleConstants.STYLE_WORD_SPACING,
							new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + wordSpacing));
					((TextArea) child).setStyle(areaStyle);
					int spacing = wordSpacing * blankNumber;
					child.setWidth(child.getWidth() + spacing);
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
		Iterator iter = area.getChildren();
		int delta = 0;
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {

				String text = ((TextArea) child).getText();
				int letterNumber = (text.length() > 1 ? (text.length() - 1) : 0);
				IStyle style = child.getStyle();
				int original = getDimensionValue(style.getProperty(StyleConstants.STYLE_LETTER_SPACING));
				IStyle areaStyle = new AreaStyle((AbstractStyle) style);
				areaStyle.setProperty(StyleConstants.STYLE_LETTER_SPACING,
						new FloatValue(CSSPrimitiveValue.CSS_NUMBER, original + letterSpacing));
				((TextArea) child).setStyle(areaStyle);
				int spacing = letterSpacing * letterNumber;
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

	private int getBlankNumber(ContainerArea area) {
		int count = 0;
		Iterator iter = area.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				String text = ((TextArea) child).getText();
				count = count + text.split(" ").length - 1;
			} else if (child instanceof ContainerArea) {
				count += getBlankNumber((ContainerArea) child);
			}
		}
		return count;
	}

	private int getLetterNumber(ContainerArea area) {
		int count = 0;
		Iterator iter = area.getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			if (child instanceof TextArea) {
				String text = ((TextArea) child).getText();
				count = (text.length() > 1 ? (text.length() - 1) : 0) - 1;
			} else if (child instanceof ContainerArea) {
				count += getLetterNumber((ContainerArea) child);
			}
		}
		return count;
	}

	protected void justify(ContainerContext currentContext) {
		int spacing = currentContext.root.getContentWidth() - currentContext.currentIP;
		int blankNumber = getBlankNumber(currentContext.root);
		if (blankNumber > 0) {
			int wordSpacing = spacing / blankNumber;
			adjustWordSpacing(wordSpacing, (ContainerArea) currentContext.root);
		} else {
			int letterNumber = getLetterNumber(currentContext.root);
			if (letterNumber > 0) {
				int letterSpacing = spacing / letterNumber;
				adjustLetterSpacing(letterSpacing, (ContainerArea) currentContext.root);
			}
		}

	}

	@Override
	public int getMaxLineWidth() {
		return currentContext.maxAvaWidth;
	}

	@Override
	public boolean isEmptyLine() {
		return isRootEmpty();
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
		AbstractArea[] children = new AbstractArea[n];
		byte[] levels = new byte[n];
		Iterator<?> iter = parent.getChildren();

		for (; i < n && iter.hasNext(); i++) {
			children[i] = (AbstractArea) iter.next();

			if (children[i] instanceof TextArea) {
				levels[i] = (byte) ((TextArea) children[i]).getRunLevel();
			} else {
				levels[i] = baseLevel;
				if (children[i] instanceof ContainerArea) {
					// We assume that each inline container area should be
					// treated as an inline block-level element.
					reorderVisually((ContainerArea) children[i]);
				}
			}
		}
		if (n > 1) {
			int x = children[0].getAllocatedX();
			Bidi.reorderVisually(levels, 0, children, 0, n);

			for (i = 0; i < n - 1; i++) {
				children[i].setAllocatedPosition(x, children[i].getAllocatedY());
				x += children[i].getAllocatedWidth();
			}
			children[i].setAllocatedPosition(x, children[i].getAllocatedY());
		}
	}

}
