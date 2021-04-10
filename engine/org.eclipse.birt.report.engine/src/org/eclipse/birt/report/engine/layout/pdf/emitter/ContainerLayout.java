/***********************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.util.BidiAlignmentResolver;
import org.w3c.dom.css.CSSValue;

public abstract class ContainerLayout extends Layout {

	protected ContainerContext currentContext;

	protected LinkedList<ContainerContext> contextList = new LinkedList<ContainerContext>();

	protected int offsetX = 0;

	protected int offsetY = 0;

	protected boolean isInBlockStacking = true;

	protected boolean isInline = false;

	public ContainerLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		if (parent != null && !parent.isInBlockStacking) {
			isInBlockStacking = false;
		}
	}

	public void layout() throws BirtException {

	}

	public boolean isPageEmpty() {
		if (!isRootEmpty()) {
			return false;
		} else {
			if (parent != null) {
				return parent.isPageEmpty();
			}
		}
		return true;
	}

	public void addToRoot(AbstractArea area, int index) {
		ContainerContext cc = currentContext;
		currentContext = contextList.get(index);
		addToRoot(area);
		currentContext = cc;
	}

	protected void addToRoot(AbstractArea area) {
		addToRoot(area, true);
	}

	protected void addToRoot(AbstractArea area, boolean clipFlag) {
		currentContext.root.addChild(area);
		area.setAllocatedPosition(currentContext.currentIP + offsetX, currentContext.currentBP + offsetY);
		currentContext.currentBP += area.getAllocatedHeight();
		if (clipFlag) {
			if (currentContext.currentIP + area.getAllocatedWidth() > currentContext.root.getContentWidth()) {
				currentContext.root.setNeedClip(true);
			} else if (currentContext.currentBP > currentContext.maxAvaHeight) {
				currentContext.root.setNeedClip(true);
			}
		}
	}

	public boolean addArea(AbstractArea area) {
		return addArea(area, true);
	}

	public boolean addArea(AbstractArea area, int index) {
		ContainerContext cc = currentContext;
		currentContext = contextList.get(index);
		boolean ret = addArea(area);
		currentContext = cc;
		return ret;
	}

	protected boolean addArea(AbstractArea area, boolean clipFlag) {

		if (currentContext != null) {
			if (!context.autoPageBreak) {
				addToRoot(area, clipFlag);
				return true;
			} else {
				if (area.getAllocatedHeight() + currentContext.currentBP > getMaxAvaHeight()) {
					if (isPageEmpty()) {
						addToRoot(area, clipFlag);
						return true;
					} else {
						/*
						 * if ( isInBlockStacking ) { flushPage( contextList.size() ); } autoPageBreak(
						 * ); addToRoot( area, clipFlag ); return true;
						 */
						return false;
					}
				} else {
					addToRoot(area, clipFlag);
					return true;
				}
			}
		}
		return true;
	}

	public void autoPageBreak() throws BirtException {
		if (parent != null) {
			parent.autoPageBreak();
		}
		int size = contextList.size();
		if (size == 0 || size > 0 && currentContext == contextList.getLast()) {
			initialize();
		} else {
			int index = contextList.indexOf(currentContext) + 1;
			if (index >= 0 && index < contextList.size()) {
				setCurrentContext(index);
			}
		}
	}

	public int getMaxAvaWidth() {
		return currentContext.maxAvaWidth;
	}

	public int getMaxAvaHeight() {
		return currentContext.maxAvaHeight;
	}

	public int getCurrentMaxContentWidth() {
		return currentContext.maxAvaWidth - currentContext.currentIP;
	}

	public int getCurrentMaxContentHeight() {
		return currentContext.maxAvaHeight - currentContext.currentBP;
	}

	public int getOffsetX() {
		return offsetX;
	}

	public int getOffsetY() {
		return offsetY;
	}

	protected boolean isRootEmpty() {
		return !(currentContext.root != null && currentContext.root.getChildrenCount() > 0);
	}

	public int getLineHeight() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return PropertyUtil.getLineHeight(contentStyle.getLineHeight());
		}
		return 0;
	}

	protected abstract void createRoot();

	public void step(int step) {
		if (currentContext != null) {
			int index = contextList.indexOf(currentContext) + step;
			if (index >= 0 && index < contextList.size()) {
				setCurrentContext(index);
				if (parent != null) {
					parent.step(step);
				}
			}
		}
	}

	public void gotoLastPage() {
		int size = contextList.size();
		if (size == 1) {
			return;
		} else {
			int index = contextList.indexOf(currentContext);
			if (index != size - 1) {
				setCurrentContext(size - 1);
				parent.step(size - 1 - index);
			}
		}
	}

	public void gotoFirstPage() {
		int size = contextList.size();
		if (size == 1) {
			return;
		} else {
			int index = contextList.indexOf(currentContext);
			if (index > 0) {
				setCurrentContext(0);
				parent.step(0 - index);
			}
		}
	}

	protected void setCurrentContext(int index) {
		if (index >= 0 && index < contextList.size()) {
			currentContext = contextList.get(index);
		}
	}

	protected void closeExcludingLast() throws BirtException {
		// Current layout should be in block stacking.
		int size = contextList.size();
		closeFirstN(size - 1);
	}

	protected void closeFirstN(int size) throws BirtException {
		for (int i = 0; i < size; i++) {
			closeLayout(contextList.removeFirst(), i, false);
		}
		setCurrentContext(0);
		if (parent != null) {
			parent.closeFirstN(size);
		}
	}

	protected void closeLayout() throws BirtException {
		int size = contextList.size();
		if (isInline) {
			for (int i = 0; i < size; i++) {
				closeLayout(contextList.removeFirst(), i, i == size - 1);
			}
			if (isInBlockStacking) {
				if (size > 1) {
					parent.closeExcludingLast();
				}
			}
			if (parent != null) {
				parent.gotoFirstPage();
			}
		} else {
			if (parent != null) {
				for (int i = 0; i < size; i++) {
					int parentSize = parent.contextList.size();
					closeLayout(contextList.removeFirst(), parentSize - size + i, i == size - 1);
				}
				if (isInBlockStacking) {
					if (size > 1) {
						parent.closeExcludingLast();
					}
				}
				parent.gotoLastPage();
			} else {
				// Current layout should be page layout
				for (int i = 0; i < size; i++) {
					closeLayout(contextList.removeFirst(), i, i == size - 1);
				}
			}

		}
	}

	protected abstract void closeLayout(ContainerContext currentContext, int index, boolean finished)
			throws BirtException;

	protected void align(ContainerArea container) {
		if (container == null || content == null)
			return;

		CSSValue align = content.getComputedStyle().getProperty(IStyle.STYLE_TEXT_ALIGN);

		boolean isRightAligned = BidiAlignmentResolver.isRightAligned(content, align, false);

		if (isRightAligned || IStyle.CENTER_VALUE.equals(align)) {
			Iterator<?> iter = container.getChildren();
			while (iter.hasNext()) {
				AbstractArea area = (AbstractArea) iter.next();
				int spacing = getMaxAvaWidth() - area.getAllocatedWidth();
				if (spacing > 0) {
					if (isRightAligned) {
						area.setAllocatedPosition(spacing + area.getAllocatedX(), area.getAllocatedY());
					} else if (IStyle.CENTER_VALUE.equals(align)) {
						area.setAllocatedPosition(spacing / 2 + area.getAllocatedX(), area.getAllocatedY());
					}
				}
			}
		}
	}

	class ContainerContext {
		protected ContainerArea root;

		protected int currentIP = 0;

		protected int currentBP = 0;

		protected int maxAvaHeight = 0;

		protected int maxAvaWidth = 0;
	}

}
