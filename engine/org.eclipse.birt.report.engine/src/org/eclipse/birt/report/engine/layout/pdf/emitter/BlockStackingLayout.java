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
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.impl.AbstractArea;
import org.eclipse.birt.report.engine.layout.area.impl.AreaFactory;
import org.eclipse.birt.report.engine.layout.area.impl.ContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class BlockStackingLayout extends ContainerLayout {

	public BlockStackingLayout(LayoutEngineContext context, ContainerLayout parent, IContent content) {
		super(context, parent, content);
		isInline = false;
	}

	@Override
	protected void initialize() throws BirtException {
		currentContext = new ContainerContext();
		contextList.add(currentContext);
		createRoot();
		validateBoxProperty(content, currentContext.root.getStyle(), parent.getCurrentMaxContentWidth(),
				context.getMaxHeight());
		calculateSpecifiedWidth();
		// initialize offsetX and offsetY
		offsetX = currentContext.root.getContentX();
		offsetY = currentContext.root.getContentY();

		if (specifiedWidth > 0) {
			currentContext.root.setAllocatedWidth(specifiedWidth);
		} else {
			currentContext.root.setAllocatedWidth(parent.getCurrentMaxContentWidth());
		}
		currentContext.maxAvaWidth = currentContext.root.getContentWidth();

		currentContext.root.setAllocatedHeight(parent.getCurrentMaxContentHeight());
		currentContext.maxAvaHeight = currentContext.root.getContentHeight();

	}

	@Override
	protected void closeLayout(ContainerContext currentContext, int index, boolean finished) throws BirtException {
		if ((currentContext.root == null) || (!finished && currentContext.root.getChildrenCount() == 0)) {
			return;
		}
		IStyle areaStyle = currentContext.root.getStyle();
		int height = currentContext.currentBP + getOffsetY()
				+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_PADDING_BOTTOM))
				+ getDimensionValue(areaStyle.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
		calculateSpecifiedHeight();
		if (specifiedHeight > height) {
			CSSValue verticalAlign = areaStyle.getProperty(IStyle.STYLE_VERTICAL_ALIGN);
			if (IStyle.BOTTOM_VALUE.equals(verticalAlign) || IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
				int offset = specifiedHeight - height;
				if (IStyle.BOTTOM_VALUE.equals(verticalAlign)) {
					Iterator iter = currentContext.root.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset);
					}
				} else if (IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
					Iterator iter = currentContext.root.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset / 2);
					}
				}
			}
			height = specifiedHeight;
		}
		currentContext.root.setHeight(height);
		if (parent != null) {
			parent.addToRoot(currentContext.root, index);
		} else {
			content.setExtension(IContent.LAYOUT_EXTENSION, currentContext.root);
		}
	}

	@Override
	protected void createRoot() {
		currentContext.root = (ContainerArea) AreaFactory.createBlockContainer(content);
	}

	@Override
	public int getLineHeight() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return PropertyUtil.getLineHeight(contentStyle.getLineHeight());
		}
		return 0;
	}

	public String getTextAlign() {
		if (content != null) {
			IStyle contentStyle = content.getComputedStyle();
			return contentStyle.getTextAlign();
		}
		return null;
	}

}
