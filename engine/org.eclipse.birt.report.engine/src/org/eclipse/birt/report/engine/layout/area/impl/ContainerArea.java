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

package org.eclipse.birt.report.engine.layout.area.impl;

import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public abstract class ContainerArea extends AbstractArea implements IContainerArea {

	protected boolean needClip;
	protected int maxChildrenBaseLine = 0;
	protected int maxChildrenBaseLineBelow = 0;

	ContainerArea(IContent content) {
		super(content);
	}

	ContainerArea(IReportContent report) {
		super(report);
	}

	protected ArrayList children = new ArrayList();

	public Iterator getChildren() {
		return children.iterator();
	}

	public void addChild(IArea area) {
		children.add(area);
	}

	public void addChild(int index, IArea area) {
		children.add(index, area);
	}

	public int indexOf(IArea area) {
		return children.indexOf(area);
	}

	public void removeAll() {
		children.clear();
	}

	public void removeChild(IArea area) {
		children.remove(area);
	}

	public void accept(IAreaVisitor visitor) {
		visitor.visitContainer(this);
	}

	public int getChildrenCount() {
		return children.size();
	}

	public int getContentY() {
		return PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_TOP_WIDTH), width)
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_TOP), width);
	}

	public int getContentX() {
		return PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_LEFT_WIDTH), width)
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_LEFT), width);
	}

	// get height of empty container
	public int getIntrisicHeight() {
		return PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_TOP))
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_TOP))
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_TOP_WIDTH))
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_MARGIN_BOTTOM))
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_PADDING_BOTTOM))
				+ PropertyUtil.getDimensionValue(style.getProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH));
	}

	public boolean needClip() {
		return needClip;
	}

	public void setNeedClip(boolean needClip) {
		this.needClip = needClip;
	}

	public int getMaxChildrenBaseLine() {
		return maxChildrenBaseLine;
	}

	public void setMaxChildrenBaseLine(int maxChildrenBaseLine) {
		this.maxChildrenBaseLine = maxChildrenBaseLine;
	}

	public int getMaxChildrenBaseLineBelow() {
		return maxChildrenBaseLineBelow;
	}

	public void setMaxChildrenBaseLineBelow(int maxChildrenBaseLineBelow) {
		this.maxChildrenBaseLineBelow = maxChildrenBaseLineBelow;
	}

}
