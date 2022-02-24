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
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.w3c.dom.css.CSSValue;

public abstract class InlineStackingArea extends ContainerArea {

	public InlineStackingArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		this.isInlineStacking = true;
	}

	public InlineStackingArea(InlineStackingArea area) {
		super(area);
	}

	public abstract void endLine(boolean endParagraph) throws BirtException;

	public abstract boolean isEmptyLine();

	public abstract int getMaxLineWidth();

	public void update(AbstractArea area) throws BirtException {
		currentIP += area.getAllocatedWidth();
	}

	public void add(AbstractArea area) {
		children.add(area);
		area.setAllocatedPosition(currentIP + getOffsetX(), currentBP + getOffsetY());

	}

	public int getBaseLine() {
		if (baseLine == 0 && children.size() > 0) {
			Iterator iter = children.iterator();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				baseLine = Math.max(baseLine, child.getY() + child.getBaseLine());
			}
		}
		return baseLine;
	}

	public void updateChildrenPosition() throws BirtException {
		first = false;
		currentIP = 0;
		if (children.size() > 0) {
			Iterator iter = children.iterator();
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
		Iterator iter = getChildren();
		while (iter.hasNext()) {
			AbstractArea child = (AbstractArea) iter.next();
			CSSValue vAlign = child.getVerticalAlign();
			if (IStyle.TOP_VALUE.equals(vAlign)) {
				continue;
			}
			int spacing = getContentHeight() - child.getAllocatedHeight();
			if (spacing < 0) {
				spacing = 0;
			}

			if ((vAlign == null && !isImageContainer(child)) || IStyle.BASELINE_VALUE.equals(vAlign)) {
				// FIXME to implement basline alignment
				int lineHeight = parent.getLineHeight();
				if (lineHeight > 0) {
					// align to middle, fix issue 164072
					child.setPosition(child.getX(), getBaseLine() - child.getBaseLine() + spacing / 2);
				} else {
					child.setPosition(child.getX(), getBaseLine() - child.getBaseLine());
				}
			} else if (IStyle.BOTTOM_VALUE.equals(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing);
			} else if (IStyle.MIDDLE_VALUE.equals(vAlign)) {
				child.setPosition(child.getX(), child.getY() + spacing / 2);
			}
		}
	}

	private boolean isImageContainer(AbstractArea area) {
		if (area instanceof ImageInlineContainer || area instanceof ImageBlockContainer) {
			return true;
		} else {
			return false;
		}
	}

	public abstract void setTextIndent(ITextContent content);

}
