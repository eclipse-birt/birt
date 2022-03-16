/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;
import java.util.LinkedList;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.IBlockStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.content.BlockStackingExecutor;
import org.w3c.dom.css.CSSValue;

/**
 * represents block stacking layout manager
 *
 */
public abstract class PDFBlockStackingLM extends PDFStackingLM implements IBlockStackingLayoutManager {

	protected IReportItemExecutor blockExecutor = null;

	protected CompositeArea keepWithCache = new CompositeArea();

	protected boolean keepWith = false;

	@Override
	public int getCurrentMaxContentHeight() {
		return maxAvaHeight - currentBP - keepWithCache.getHeight();
	}

	protected void addChild(PDFAbstractLM child) {
		this.child = child;
	}

	public PDFBlockStackingLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
	}

	@Override
	protected boolean traverseChildren() throws BirtException {
		boolean hasNextPage = false;
		if (child != null) {
			hasNextPage = child.layout();
			if (hasNextPage) {
				if (child.isFinished()) {
					child = null;
				}
				return true;
			}

		}
		if (blockExecutor == null) {
			// get first child of this container
			blockExecutor = createExecutor();
		}
		while (blockExecutor.hasNextChild()) {
			IReportItemExecutor childExecutor = blockExecutor.getNextChild();
			if (childExecutor != null) {
				if (layoutChildNode(childExecutor)) {
					return true;
				}
			}
		}
		return false;
	}

	protected IReportItemExecutor createExecutor() {
		return new BlockStackingExecutor(content, executor);
	}

	private boolean layoutChildNode(IReportItemExecutor childExecutor) throws BirtException {
		boolean hasNextPage;
		IContent childContent = childExecutor.execute();
		PDFAbstractLM childLM = getFactory().createLayoutManager(this, childContent, childExecutor);
		hasNextPage = childLM.layout();
		if (hasNextPage && !childLM.isFinished()) {
			child = childLM;
		} else {
			child = null;
		}
		return hasNextPage;
	}

	@Override
	protected void initialize() throws BirtException {
		if (root == null) {
			createRoot();
			validateBoxProperty(root.getStyle(), parent.getCurrentMaxContentWidth(), context.getMaxHeight());
			calculateSpecifiedWidth();
			// initialize offsetX and offsetY
			setOffsetX(root.getContentX());
			setOffsetY(isFirst ? root.getContentY() : 0);
			// initialize current position
			setCurrentBP(0);
			setCurrentIP(0);
		}
		// support user defined width
		int maxW = parent.getCurrentMaxContentWidth();
		if (specifiedWidth > 0) {
			maxW = Math.min(maxW, specifiedWidth);
		}
		root.setAllocatedWidth(maxW);
		maxAvaWidth = root.getContentWidth();

		root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		maxAvaHeight = root.getContentHeight();

	}

	@Override
	protected void closeLayout() {
		if (root == null) {
			return;
		}
		IStyle areaStyle = root.getStyle();
		if (!isLast) {
			// set dimension property for root
			// TODO support user defined height
			areaStyle.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
			areaStyle.setProperty(IStyle.STYLE_PADDING_BOTTOM, IStyle.NUMBER_0);
			areaStyle.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
		}

		// FIXME currently do not consider a special case...
		int height = getCurrentBP() + getOffsetY()
				+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_BOTTOM))
				+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		if (isFirst && isLast) {
			calculateSpecifiedHeight();
			if (specifiedHeight > height) {
				CSSValue verticalAlign = areaStyle.getProperty(IStyle.STYLE_VERTICAL_ALIGN);
				if (IStyle.BOTTOM_VALUE.equals(verticalAlign) || IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
					int offset = specifiedHeight - height;
					if (IStyle.BOTTOM_VALUE.equals(verticalAlign)) {
						Iterator iter = root.getChildren();
						while (iter.hasNext()) {
							AbstractArea child = (AbstractArea) iter.next();
							child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset);
						}
					} else if (IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
						Iterator iter = root.getChildren();
						while (iter.hasNext()) {
							AbstractArea child = (AbstractArea) iter.next();
							child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset / 2);
						}
					}
				}
				height = specifiedHeight;
			}
		}
		root.setHeight(height);
	}

	@Override
	public boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext) {
		// ignore empty area
		if (area == null) {
			return true;
		}

		keepWith = keepWith || keepWithNext || pageBreakAvoid;

		if (!keepWith && !keepWithPrevious) {
			clearCache();
		}

		AbstractArea aArea = (AbstractArea) area;
		if (keepWithCache.getHeight() + aArea.getAllocatedHeight() + getCurrentBP() > getMaxAvaHeight()) {
			if (isPageEmpty()) {
				addToRoot(aArea);
			} else {
				context.setAutoPageBreak(true);
				return false;
			}
		} else if (keepWithPrevious || keepWith) {
			keepWithCache.add(area);
		} else {
			addToRoot(aArea);
		}
		keepWith = keepWithNext;
		if (!keepWith) {
			clearCache();
		}
		return true;
	}

	public String getTextAlign() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return contentStyle.getTextAlign();
		}
		return null;
	}

	@Override
	protected void cancelChildren() throws BirtException {
		if (child != null) {
			child.cancel();
		}
	}

	@Override
	protected boolean hasNextChild() throws BirtException {
		if (child == null && (blockExecutor != null && !blockExecutor.hasNextChild())) {
			return false;
		}
		return true;

	}

	public void setKeepWithNext(boolean keepWithNext) {
		if (keepWithNext) {
			keepWith = true;
		}

		if (!keepWith && !pageBreakAvoid) {
			clearCache();
		}
	}

	@Override
	protected boolean clearCache() {
		while (!keepWithCache.isEmpty()) {
			AbstractArea area = (AbstractArea) keepWithCache.getFirst();
			boolean succeed = addToRoot(area);
			if (!succeed) {
				// autoPageBreak();
				return false;
			} else {
				keepWithCache.removeFirst();
			}
		}
		return true;

	}

	protected boolean addToRoot(AbstractArea area) {
		root.addChild(area);
		area.setAllocatedPosition(currentIP + offsetX, currentBP + offsetY);
		currentBP += area.getAllocatedHeight();
		if (currentIP + area.getAllocatedWidth() > root.getContentWidth()) {
			root.setNeedClip(true);
		}

		if (currentBP > maxAvaHeight) {
			root.setNeedClip(true);
		}
		return true;
	}

	protected static class CompositeArea {
		protected LinkedList cache = new LinkedList();

		protected int height = 0;

		public CompositeArea() {
		}

		public int size() {
			return cache.size();
		}

		public void add(IArea area) {
			AbstractArea aArea = (AbstractArea) area;
			cache.addLast(area);
			height += aArea.getAllocatedHeight();
		}

		public int getHeight() {
			return this.height;
		}

		public boolean isEmpty() {
			return cache.isEmpty();
		}

		public Iterator getChildren() {
			return cache.iterator();
		}

		public IArea getFirst() {
			if (!cache.isEmpty()) {
				return (IArea) cache.getFirst();
			}
			return null;
		}

		public IArea removeFirst() {
			if (!cache.isEmpty()) {
				AbstractArea area = ((AbstractArea) cache.removeFirst());
				height -= area.getAllocatedHeight();
				return area;
			}
			return null;
		}

		public void addAll(CompositeArea areas) {
			Iterator iter = areas.getChildren();
			while (iter.hasNext()) {
				add((IArea) iter.next());
			}
		}

		public CompositeArea getLastN(int count) {
			CompositeArea areas = new CompositeArea();
			for (int i = 0; i < count; i++) {
				AbstractArea area = (AbstractArea) this.getFirst();
				if (area != null) {
					areas.add(area);
				} else {
					return areas;
				}
			}
			return areas;
		}

		public CompositeArea getFirstN(int count) {
			CompositeArea areas = new CompositeArea();
			for (int i = 0; i < count; i++) {
				AbstractArea area = (AbstractArea) this.getFirst();
				if (area != null) {
					areas.add(area);
				} else {
					return areas;
				}
			}
			return areas;
		}

		public CompositeArea getAreas(int height, boolean force) {
			if (height >= this.height) {
				return this;
			} else {
				CompositeArea areas = new CompositeArea();
				int current = 0;
				while (current < height) {
					AbstractArea area = (AbstractArea) this.getFirst();
					if (area != null) {
						current += area.getAllocatedHeight();
						if (current <= height) {
							areas.add(area);
							removeFirst();
						} else {
							if (current == 0 && force) {
								areas.add(area);
								removeFirst();
							}
							return areas;
						}
					}
				}

			}
			return null;
		}

	}

	protected static class KeepWithMgr {
		protected CompositeArea cache = new CompositeArea();

		protected int maxHeight = 0;

		public KeepWithMgr() {

		}

		public KeepWithMgr(int maxHeight) {
			this.maxHeight = maxHeight;
		}

		public void setMaxHeight(int maxHeight) {
			this.maxHeight = maxHeight;
		}

		public void addLast(IArea area) {
			cache.add(area);
		}

		public boolean isOverFlow() {
			return cache.getHeight() > maxHeight;
		}

		public int getHeight() {
			return cache.getHeight();
		}

		public boolean isEmpty() {
			return cache.isEmpty();
		}

		public IArea getFirst() {
			if (!cache.isEmpty()) {
				return (IArea) cache.getFirst();
			}
			return null;
		}

		public void removeFirst() {
			if (!cache.isEmpty()) {
				cache.removeFirst();
			}
		}

	}

	@Override
	public void autoPageBreak() {
		if (!isRootEmpty()) {
			closeLayout();
			submitRoot();
			return;
		} else if (!keepWithCache.isEmpty()) {
			clearCache();
			if (!isRootEmpty()) {
				closeLayout();
				submitRoot();
				return;
			}
		}
		if (child != null) {
			child.autoPageBreak();
			closeLayout();
			submitRoot();
		}

	}

	@Override
	public void submit(AbstractArea area) {
		root.addChild(area);
		area.setAllocatedPosition(currentIP + offsetX, currentBP + offsetY);
		currentBP += area.getAllocatedHeight();
	}

	@Override
	protected boolean submitRoot() {
		if (root == null) {
			return true;
		}
		boolean success = true;
		if (parent != null) {
			if (isLast) {
				// FIXME to support keepWithPrevious
				success = parent.addArea(root, false, isKeepWithNext());
			} else if (context.isAutoPageBreak() && isFirst) {
				success = parent.addArea(root, false, false);
			} else {
				parent.submit(root);
			}
			if (success) {
				isFirst = false;
				root = null;
			}
		} else if (content != null) {
			content.setExtension(IContent.LAYOUT_EXTENSION, root);
			// root = null;
		}
		return success;
	}

}
