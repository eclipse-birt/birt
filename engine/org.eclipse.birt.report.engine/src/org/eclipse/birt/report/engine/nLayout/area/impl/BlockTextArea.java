/***********************************************************************
 * Copyright (c) 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.nLayout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.ITextContent;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.eclipse.birt.report.engine.nLayout.LayoutContext;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.ILayout;
import org.w3c.dom.css.CSSValue;

public class BlockTextArea extends BlockContainerArea implements ILayout {
	private BlockTextRenderListener listener = null;

	private String helpText = null;

	public BlockTextArea(ContainerArea parent, LayoutContext context, IContent content) {
		super(parent, context, content);
		if (context.isInHtmlRender()) {
			InstanceID id = content.getInstanceID();
			if (id != null) {
				SizeBasedContent hint = (SizeBasedContent) context.getHtmlLayoutContext().getPageHintManager()
						.getSizeBasedContentMapping().get(id.toUniqueString());
				if (hint != null) {
					parent.width = hint.width;
					listener = new BlockTextRenderListener(this, hint.offsetInContent, hint.dimension);
				}
			}
		}
	}

	public BlockTextArea(BlockTextArea area) {
		super(area);
	}

	public void layout() throws BirtException {
		initialize();
		removeHyperlinkForBlankText();
		TextLineArea line = new TextLineArea(this, context);
		line.initialize();
		line.setTextIndent((ITextContent) content);
		TextAreaLayout text = new TextAreaLayout(line, context, content);
		text.initialize();
		if (context.isInHtmlRender()) {
			text.addListener(listener);
		}
		text.layout();
		// some tricks here, TextLineArea need to be closed before the
		// TextAreaLayout. Because the BlockTextRenderListener need to know
		// whether the last line is inserted or split into next page.
		line.close();
		text.close();
		close();
	}

	private void removeHyperlinkForBlankText() {
		String text = ((ITextContent) content).getText();
		if (text == null || text.length() == 0) {
			setAction(null);
		}
	}

	protected BlockTextArea getSplitArea(ArrayList ablatedChildren, int newHeight) {
		BlockTextArea newArea = (BlockTextArea) super.getSplitArea(ablatedChildren, newHeight);
		addToExtension(newArea);
		return newArea;
	}

	public BlockTextArea cloneArea() {
		BlockTextArea newArea = new BlockTextArea(this);
		return newArea;
	}

	public void close() throws BirtException {
		super.close();
		verticalAlign();
		addToExtension(this);
		updateTextContent();
		checkDisplayNone();
	}

	private void verticalAlign() {
		CSSValue verticalAlign = content.getComputedStyle().getProperty(IStyle.STYLE_VERTICAL_ALIGN);
		if (IStyle.BOTTOM_VALUE.equals(verticalAlign) || IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
			int totalHeight = 0;
			Iterator<IArea> iter = this.getChildren();
			while (iter.hasNext()) {
				AbstractArea child = (AbstractArea) iter.next();
				totalHeight += child.getAllocatedHeight();
			}
			int offset = this.getContentHeight() - totalHeight;
			if (offset > 0) {
				if (IStyle.BOTTOM_VALUE.equals(verticalAlign)) {
					iter = this.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset);
					}
				} else if (IStyle.MIDDLE_VALUE.equals(verticalAlign)) {
					iter = this.getChildren();
					while (iter.hasNext()) {
						AbstractArea child = (AbstractArea) iter.next();
						child.setAllocatedPosition(child.getAllocatedX(), child.getAllocatedY() + offset / 2);
					}
				}

			}
		}
	}

	private void addToExtension(BlockTextArea area) {
		if (context.isFixedLayout() && context.getEngineTaskType() == IEngineTask.TASK_RUN) {
			ArrayList<BlockTextArea> list = (ArrayList<BlockTextArea>) content.getExtension(IContent.LAYOUT_EXTENSION);
			if (list == null) {
				list = new ArrayList<BlockTextArea>();
				content.setExtension(IContent.LAYOUT_EXTENSION, list);
			}
			if (area.finished) {
				if (list.isEmpty() || (list.size() > 0 && !list.get(list.size() - 1).finished)) {
					list.add(area);
				}
			} else {
				if (list.size() > 0 && list.get(list.size() - 1).finished) {
					list.add(list.size() - 1, area);
				} else {
					list.add(area);
				}
			}
		}
	}

	private void updateTextContent() {
		if (context.isInHtmlRender()) {
			((ITextContent) content).setText(listener.getSplitText());
		}
	}

	protected void update() throws BirtException {
		if (parent != null) {
			if (context.isFixedLayout() && getContentHeight() > specifiedHeight && specifiedHeight > 0) {
				setContentHeight(specifiedHeight);
				setNeedClip(true);
			}
			if (!isInInlineStacking && context.isAutoPageBreak()) {
				int aHeight = getAllocatedHeight();
				int size = children.size();
				if ((aHeight + parent.getAbsoluteBP() > context.getMaxBP()) && (size > 1)) {
					IStyle style = content.getComputedStyle();
					// Minimum number of lines of a paragraph that must appear
					// at the top of a page.
					int widow = Math.min(size, PropertyUtil.getIntValue(style.getProperty(IStyle.STYLE_WIDOWS)));
					// Minimum number of lines of a paragraph that must appear
					// at the bottom of a page.
					int orphan = Math.min(size, PropertyUtil.getIntValue(style.getProperty(IStyle.STYLE_ORPHANS)));
					for (int i = 0; i < size; i++) {
						TextLineArea line = (TextLineArea) children.get(i);
						if (i > 0 && i < orphan) {
							line.setPageBreakBefore(IStyle.AVOID_VALUE);
						} else if (i > size - widow) {
							line.setPageBreakBefore(IStyle.AVOID_VALUE);
						}
					}
				}
				while (aHeight + parent.getAbsoluteBP() >= context.getMaxBP()) {
					if (!parent.autoPageBreak()) {
						break;
					}
					aHeight = getAllocatedHeight();
				}
			}
			parent.update(this);
		}
	}

	public String getHelpText() {
		return helpText;
	}

	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
}
