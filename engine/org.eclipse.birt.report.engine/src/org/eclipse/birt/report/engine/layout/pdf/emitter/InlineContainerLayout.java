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
 ***********************************************************************/

package org.eclipse.birt.report.engine.layout.pdf.emitter;

import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;

public class InlineContainerLayout extends InlineStackingLayout implements IInlineStackingLayout {
	IInlineStackingLayout lineParent = null;
	int lineCount = 1;

	public InlineContainerLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		isInBlockStacking = false;
		lineParent = (IInlineStackingLayout) parent;
	}

	public void setTextIndent(ITextContent content) {
		lineParent.setTextIndent(content);
	}

	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) {
		// TODO support specified height/width/alignment
		if (currentContext.root != null) {
			IStyle areaStyle = currentContext.root.getStyle();
			int width = currentContext.currentIP + getOffsetX()
					+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_RIGHT))
					+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
			if (lineCount == 1) {
				calculateSpecifiedWidth();
				if (specifiedWidth > width) {
					width = specifiedWidth;
				}
			}
			currentContext.root.setWidth(width);
			int height = 0;
			Iterator iter = currentContext.root.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				height = Math.max(height, child.getAllocatedHeight());
			}
			currentContext.root.setContentHeight(height);
		}
		verticalAlign();
		parent.addToRoot(currentContext.root, index);

	}

	public void addToRoot(AbstractArea area) {
		currentContext.root.addChild(area);
		area.setAllocatedPosition(currentContext.currentIP, currentContext.currentBP);
		currentContext.currentIP += area.getAllocatedWidth();
	}

	protected void createRoot() {
		currentContext.root = (ContainerArea) AreaFactory.createInlineContainer(content);
	}

	protected void initialize() {
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		createRoot();
		currentContext.maxAvaWidth = parent.getCurrentMaxContentWidth();
		currentContext.maxAvaHeight = parent.getCurrentMaxContentHeight();
		currentContext.currentBP = currentContext.root.getContentY();
		currentContext.currentIP = currentContext.root.getContentX();
	}

	public boolean endLine() throws BirtException {
		lineCount++;
		boolean ret = true;
		if (currentContext.root != null && currentContext.root.getChildrenCount() > 0) {
			closeLayout();
		}
		if (lineParent != null) {
			ret = lineParent.endLine();
			initialize();
		}
		return ret;
	}

	public int getMaxLineWidth() {
		return lineParent.getMaxLineWidth();
	}

	public boolean isEmptyLine() {
		if (currentContext.root != null && currentContext.root.getChildrenCount() > 0) {
			return false;
		}
		return lineParent.isEmptyLine();
	}

}
