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

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.w3c.dom.css.CSSValue;

/**
 * Definition of the inline stacking area
 *
 * @since 3.3
 *
 */
public abstract class InlineStackingArea extends ContainerArea {

	/**
	 * Constructor container based
	 *
	 * @param parent
	 * @param context
	 * @param content
	 */
	public InlineStackingArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		this.isInlineStacking = true;
	}

	/**
	 * Constructor area based
	 *
	 * @param area
	 */
	public InlineStackingArea(InlineStackingArea area) {
		super(area);
	}

	/**
	 * End the line
	 *
	 * @param endParagraph
	 * @throws BirtException
	 */
	public abstract void endLine(boolean endParagraph) throws BirtException;

	/**
	 * Is the line empty
	 *
	 * @return true, line is empty
	 */
	public abstract boolean isEmptyLine();

	/**
	 * Get the maximum line width
	 *
	 * @return Return maximum line width
	 */
	public abstract int getMaxLineWidth();

	@Override
	public void update(AbstractArea area) throws BirtException {
		currentIP += area.getAllocatedWidth();
	}

	@Override
	public void add(AbstractArea area) {
		children.add(area);
		area.setAllocatedPosition(currentIP + getOffsetX(), currentBP + getOffsetY());

	}

	@Override
	public int getBaseLine() {
		if (baseLine == 0 && children.size() > 0) {
			Iterator<IArea> iter = children.iterator();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				baseLine = Math.max(baseLine, child.getY() + child.getBaseLine());
			}
		}
		return baseLine;
	}

	@Override
	public void updateChildrenPosition() {
		first = false;
		currentIP = 0;
		if (children.size() > 0) {
			Iterator<IArea> iter = children.iterator();
			int x = getOffsetX();
			int w = 0;
			while (iter.hasNext()) {
				ContainerArea area = (ContainerArea) iter.next();
				// if(iter.hasNext( ))
				{
					area.setAllocatedPosition(x, area.getY());
					int aw = area.getAllocatedWidth();
					x += aw;
					w += aw;
					if (area.finished) {
						currentIP += aw;
					}
				}
			}
			setContentWidth(w);
		} else {
			setContentWidth(0);
		}
	}

	protected void verticalAlign() {
		Iterator<IArea> iter = getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			CSSValue vAlign = child.getVerticalAlign();
			if (CSSValueConstants.TOP_VALUE.equals(vAlign)) {
				continue;
			}
			int spacing = getContentHeight() - child.getAllocatedHeight();

			if ((vAlign == null && !isImageContainer(child)) || CSSValueConstants.BASELINE_VALUE.equals(vAlign)) {
				// FIXME to implement baseline alignment
				int lineHeight = parent.getLineHeight();
				if (lineHeight > 0) {
					// align to middle, fix issue 164072
					child.setPosition(child.getX(), getBaseLine() - child.getBaseLine() + spacing / 2);
				} else {
					child.setPosition(child.getX(), getBaseLine() - child.getBaseLine());
				}
			} else if (CSSValueConstants.BOTTOM_VALUE.equals(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing);
			} else if (CSSValueConstants.MIDDLE_VALUE.equals(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing / 2);
			}
		}
	}

	private boolean isImageContainer(AbstractArea area) {
		if (area instanceof ImageInlineContainer || area instanceof ImageBlockContainer) {
			return true;
		}
		return false;
	}

	/**
	 * Set the text indent
	 *
	 * @param content
	 */
	public abstract void setTextIndent(ITextContent content);

}
