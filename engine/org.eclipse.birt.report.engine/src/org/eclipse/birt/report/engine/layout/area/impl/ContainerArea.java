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
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.area.IAreaVisitor;
import org.eclipse.birt.report.engine.layout.area.IContainerArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

/**
 * Container area
 *
 * @since 3.3
 *
 */
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

	protected ArrayList<IArea> children = new ArrayList<IArea>();

	@Override
	public Iterator<IArea> getChildren() {
		return children.iterator();
	}

	@Override
	public void addChild(IArea area) {
		children.add(area);
	}

	/**
	 * Add children area
	 *
	 * @param index index of child area
	 * @param area  child area
	 */
	public void addChild(int index, IArea area) {
		children.add(index, area);
	}

	/**
	 * Index of area
	 *
	 * @param area child area
	 * @return Return the area index
	 */
	public int indexOf(IArea area) {
		return children.indexOf(area);
	}

	/**
	 * Remove all child areas
	 */
	public void removeAll() {
		children.clear();
	}

	/**
	 * Remove child area
	 *
	 * @param area child area to be removed
	 */
	public void removeChild(IArea area) {
		children.remove(area);
	}

	@Override
	public void accept(IAreaVisitor visitor) {
		visitor.visitContainer(this);
	}

	@Override
	public int getChildrenCount() {
		return children.size();
	}

	/**
	 * Get the content Y
	 *
	 * @return Return the content Y
	 */
	public int getContentY() {
		return PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH), width)
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP), width);
	}

	/**
	 * Get the content X
	 *
	 * @return Return the content X
	 */
	public int getContentX() {
		return PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH), width)
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT), width);
	}

	/**
	 * Get height of empty container
	 *
	 * @return Return the height of empty container
	 */
	public int getIntrisicHeight() {
		return PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP))
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP))
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM))
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM))
				+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH));
	}

	@Override
	public boolean needClip() {
		return needClip;
	}

	@Override
	public void setNeedClip(boolean needClip) {
		this.needClip = needClip;
	}

	/**
	 * Get max children base line
	 *
	 * @return Return the max children base line
	 */
	public int getMaxChildrenBaseLine() {
		return maxChildrenBaseLine;
	}

	/**
	 * Set max children base line
	 *
	 * @param maxChildrenBaseLine max children base line
	 */
	public void setMaxChildrenBaseLine(int maxChildrenBaseLine) {
		this.maxChildrenBaseLine = maxChildrenBaseLine;
	}

	/**
	 * Get max children base line below
	 *
	 * @return Return the max children base line below
	 */
	public int getMaxChildrenBaseLineBelow() {
		return maxChildrenBaseLineBelow;
	}

	/**
	 * Set max children base line below
	 *
	 * @param maxChildrenBaseLineBelow max children base line below
	 */
	public void setMaxChildrenBaseLineBelow(int maxChildrenBaseLineBelow) {
		this.maxChildrenBaseLineBelow = maxChildrenBaseLineBelow;
	}

}
