/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.extension.IReportItemExecutor;
import org.eclipse.birt.report.engine.layout.ILineStackingLayoutManager;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class PDFInlineContainerLM extends PDFInlineStackingLM implements ILineStackingLayoutManager {
	ILineStackingLayoutManager lineParent;
	int childHeight = 0;

	public PDFInlineContainerLM(PDFLayoutEngineContext context, PDFStackingLM parent, IContent content,
			IReportItemExecutor executor) {
		super(context, parent, content, executor);
		lineParent = (ILineStackingLayoutManager) parent;
	}

	protected void closeLayout() {
		// TODO support specified height/width/alignment
		if (root != null) {
			root.setContentHeight(childHeight);
			IStyle areaStyle = root.getStyle();
			int width = getCurrentIP() + getOffsetX()
					+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_RIGHT))
					+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
			root.setWidth(width);
			int height = 0;
			Iterator iter = root.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				height = Math.max(height, child.getAllocatedHeight());
			}
			root.setContentHeight(height);
		}
		// FIXME verticalAlign may effect the root height.
		verticalAlign();
	}

	protected void createRoot() {
		root = (ContainerArea) AreaFactory.createInlineContainer(content);
	}

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
		while (executor.hasNextChild()) {
			IReportItemExecutor childExecutor = executor.getNextChild();
			if (childExecutor != null) {
				if (layoutChildNode(childExecutor)) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean layoutChildNode(IReportItemExecutor childExecutor) throws BirtException {
		boolean hasNextPage = false;
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

	protected void initialize() {
		createRoot();
		maxAvaWidth = parent.getCurrentMaxContentWidth();
		maxAvaHeight = parent.getCurrentMaxContentHeight();
		currentBP = 0;
		currentIP = 0;
	}

	public int getTextIndent() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return getDimensionValue(contentStyle.getProperty(StyleConstants.STYLE_TEXT_INDENT), maxAvaWidth);
		}
		return 0;
	}

	public boolean addArea(IArea area, boolean keepWithPrevious, boolean keepWithNext) {
		AbstractArea child = (AbstractArea) area;
		submit(child);

		return true;
	}

	public void submit(AbstractArea area) {
		root.addChild(area);
		area.setAllocatedPosition(getCurrentIP(), getCurrentBP());
		setCurrentIP(getCurrentIP() + area.getAllocatedWidth());
	}

	public boolean endLine() {
		if (root != null && root.getChildrenCount() > 0) {
			closeLayout();
			parent.submit(root);
		}
		if (parent instanceof ILineStackingLayoutManager) {
			boolean ret = ((ILineStackingLayoutManager) parent).endLine();
			if (ret) {
				initialize();
				return true;
			}
			return false;
		}
		assert (false);
		return true;
	}

	public int getMaxLineWidth() {
		return lineParent.getMaxLineWidth();
	}

	public boolean isEmptyLine() {
		if (root != null && root.getChildrenCount() > 0) {
			return false;
		}
		return lineParent.isEmptyLine();
	}

	public void setTextIndent(ITextContent content) {
		if (parent instanceof ILineStackingLayoutManager) {
			((ILineStackingLayoutManager) parent).setTextIndent(content);
		}
	}

}
