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
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.style.BoxStyle;

public class InlineContainerArea extends InlineStackingArea implements IContainerArea {

	protected transient InlineStackingArea lineParent = null;
	protected transient int lineCount = 1;

	public InlineContainerArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		this.isInlineStacking = true;
		lineParent = (InlineStackingArea) parent;
		isInInlineStacking = parent.isInInlineStacking;
	}

	public InlineContainerArea(InlineContainerArea area) {
		super(area);
	}

	protected void close(boolean isLastLine) throws BirtException {
		// TODO support specified height/width/alignment
		int contentWidth = currentIP;
		if (lineCount == 1) {
			if (specifiedWidth > contentWidth) {
				contentWidth = specifiedWidth;
			}
		}
		setContentWidth(contentWidth);
		int height = 0;
		Iterator iter = getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			height = Math.max(height, child.getAllocatedHeight());
		}
		setContentHeight(height);
		updateBackgroundImage();
		if (children.size() > 0) {
			verticalAlign();
		}
		checkDisplayNone();
		if (isLastLine) {
			checkPageBreak();
			parent.add(this);
			parent.update(this);
			this.finished = true;
		} else {
			checkPageBreak();
			InlineContainerArea area = cloneArea();
			addToExtension(area);
			area.context = context;
			area.children = children;
			// update the pareant of all children
			Iterator childIter = area.children.iterator();
			while (childIter.hasNext()) {
				AbstractArea childArea = (AbstractArea) childIter.next();
				childArea.setParent(area);
			}
			area.setParent(parent);
			children = new ArrayList();
			parent.addChild(area);
			parent.update(area);
			/*
			 * setPosition( parent.currentIP + parent.getOffsetX( ), parent .getOffsetY( ) +
			 * parent.currentBP );
			 */
			area.finished = true;
			currentIP = 0;
			height = 0;
		}
	}

	private void adjustHeight() {
		if (hasStyle) {
			localProperties.setMarginBottom(0);
			localProperties.setMarginTop(0);
			height = Math.min(height, context.getMaxBP());
		} else {
			height = Math.min(height, context.getMaxBP());
		}
	}

	@Override
	protected boolean checkPageBreak() throws BirtException {
		boolean ret = false;
		if (!isInInlineStacking && context.isAutoPageBreak()) {
			int aHeight = getAllocatedHeight();

			if (aHeight - 3000 > context.getMaxBP() && parent.getAbsoluteBP() == 0 && parent.currentBP == 0) {
				// In this case, page split can't get anything and current
				// inlineContainerArea won't fit in a single page. Endless loop
				// will occur so current inlineContainerArea's height has to be
				// addjusted
				adjustHeight();
				return false;
			}

			// When table resolves its bottom border, the total height may exceed the page
			// body height.
			// We add 3pt to avoid unexpected page break.
			int preAbsoluteBP = parent.getAbsoluteBP();
			int preCurrentBP = parent.currentBP;

			while (aHeight + parent.getAbsoluteBP() - 3000 > context.getMaxBP()) {
				if (!parent.autoPageBreak()) {
					return false;
				}
				aHeight = getAllocatedHeight();
				ret = true;
				if (parent.getAbsoluteBP() == preAbsoluteBP && parent.currentBP == preCurrentBP) {
					// page split doesn't change anything so current container's
					// height has to be adjusted to avoid endless loop
					if (isPageBreakAvoid(this)) {
						adjustHeight();
					}
					break;

				}
				preAbsoluteBP = parent.getAbsoluteBP();
				preCurrentBP = parent.currentBP;
			}
		}
		return ret;
	}

	protected boolean isPageBreakAvoid(IArea area) {
		if (area instanceof ContainerArea) {
			ContainerArea container = (ContainerArea) area;
			if (container.getChildrenCount() > 1) {
				return false;
			} else if (container.getChildrenCount() == 1) {
				return isPageBreakAvoid(container.getChild(0));
			}
			return true;
		}
		return true;
	}

	protected void addToExtension(InlineContainerArea area) {

	}

	@Override
	public void close() throws BirtException {
		close(true);
		finished = true;
	}

	@Override
	public void initialize() throws BirtException {
		IStyle style = content.getStyle();
		calculateSpecifiedWidth(content);
		if (style == null || style.isEmpty()) {
			hasStyle = false;
			boxStyle = BoxStyle.DEFAULT;
			localProperties = LocalProperties.DEFAULT;
		} else {
			buildProperties(content, context);
		}
		maxAvaWidth = parent.getCurrentMaxContentWidth();
		bookmark = content.getBookmark();
		action = content.getHyperlinkAction();
		vAlign = style != null ? style.getProperty(IStyle.STYLE_VERTICAL_ALIGN) : null;
		currentIP = 0;
		currentBP = 0;
		// parent.add( this );
	}

	@Override
	public InlineContainerArea cloneArea() {
		return new InlineContainerArea(this);
	}

	@Override
	public void endLine(boolean endParagraph) throws BirtException {
		lineCount++;
		if (getChildrenCount() > 0) {
			close(false);
		}
		if (lineParent != null) {
			lineParent.endLine(endParagraph);
			initialize();
		}
	}

	@Override
	public int getMaxLineWidth() {
		return lineParent.getMaxLineWidth();
	}

	@Override
	public boolean isEmptyLine() {
		if (getChildrenCount() > 0) {
			return false;
		}
		return lineParent.isEmptyLine();
	}

	@Override
	public void setTextIndent(ITextContent content) {
		int ip = lineParent.getCurrentIP();
		lineParent.setTextIndent(content);
		if (ip != lineParent.getCurrentIP()) {
			maxAvaWidth = parent.getCurrentMaxContentWidth();
		}
	}

	@Override
	public SplitResult split(int height, boolean force) throws BirtException {
		return SplitResult.SUCCEED_WITH_NULL;
	}

	@Override
	public SplitResult splitLines(int lineCount) throws BirtException {
		return SplitResult.SUCCEED_WITH_NULL;
	}

	public String getTagType() {
		Object generateBy = content.getGenerateBy();
		return "spanTODO";
	}

}
