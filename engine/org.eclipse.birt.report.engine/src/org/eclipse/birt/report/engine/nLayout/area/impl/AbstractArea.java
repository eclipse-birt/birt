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
import java.util.logging.Logger;

import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.nLayout.area.IArea;
import org.eclipse.birt.report.engine.nLayout.area.IContainerArea;
import org.eclipse.birt.report.engine.nLayout.area.IImageArea;
import org.eclipse.birt.report.engine.nLayout.area.ITemplateArea;
import org.eclipse.birt.report.engine.nLayout.area.ITextArea;
import org.w3c.dom.css.CSSValue;

/**
 * abstract area which is the default implementation of <code>IArea</code>
 *
 */
public abstract class AbstractArea implements IArea {

	protected static Logger logger = Logger.getLogger(AbstractArea.class.getName());
	/**
	 * x position of this area in parent area, calculated from border box.
	 */
	protected int x;

	/**
	 * y position of this area in parent area, calculated from border box.
	 */
	protected int y;

	/**
	 * width of this area
	 */
	protected int width;

	/**
	 * height of this area
	 */
	protected int height;

	/**
	 * the baseline
	 */
	protected int baseLine = 0;

	protected float scale = 1.0f;

	protected transient CSSValue vAlign;

	protected String bookmark = null;

	protected boolean ignoreReordering = false;

	protected transient ContainerArea parent;

	protected transient boolean isDummy = false;

	AbstractArea(AbstractArea area) {
		this.x = area.getX();
		this.y = area.getY();
		this.baseLine = area.getBaseLine();
		this.bookmark = area.getBookmark();
		this.action = area.getAction();
		this.scale = area.getScale();
		this.width = area.getWidth();
		this.height = area.getHeight();
		this.isDummy = area.isDummy;
	}

	/**
	 * Get the parent container
	 *
	 * @return Return the parent container
	 */
	public ContainerArea getParent() {
		return parent;
	}

	/**
	 * Set parent container
	 *
	 * @param parent
	 */
	public void setParent(ContainerArea parent) {
		this.parent = parent;
	}

	/**
	 * Get the vertical align at container
	 *
	 * @return Return the vertical align at container
	 */
	public CSSValue getVerticalAlign() {
		return vAlign;
	}

	/**
	 * Set vertical align of the container
	 *
	 * @param vAlign
	 */
	public void setVerticalAlign(CSSValue vAlign) {
		this.vAlign = vAlign;
	}

	AbstractArea() {

	}

	@Override
	public String getBookmark() {
		return bookmark;
	}

	/**
	 * Set the bookmark of the container
	 *
	 * @param bookmark
	 */
	public void setBookmark(String bookmark) {
		this.bookmark = bookmark;
	}

	@Override
	public IHyperlinkAction getAction() {
		return action;
	}

	/**
	 * Set the hyperlink action of the container
	 *
	 * @param action
	 */
	public void setAction(IHyperlinkAction action) {
		this.action = action;
	}

	/**
	 * Set the position X of the container
	 *
	 * @param x X position of the container
	 */
	public void setX(int x) {
		this.x = x;
	}

	/**
	 * Set the position Y of the container
	 *
	 * @param y Y position of the container
	 */
	public void setY(int y) {
		this.y = y;
	}

	protected IHyperlinkAction action = null;

	/**
	 * Set scale factor of the container
	 *
	 * @param scale scale factor of the container
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public float getScale() {
		return this.scale;
	}

	/**
	 * get X position of this area
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * get Y position of this area
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * Set the position of the container
	 *
	 * @param x X position of the container
	 * @param y Y position of the container
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the allocated position of the container
	 *
	 * @param x X position of the container
	 * @param y Y position of the container
	 */
	public void setAllocatedPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set the allocated position Y of the container
	 *
	 * @param ay allocated position Y of the container
	 */
	public void setAllocatedY(int ay) {
		y = ay;
	}

	/**
	 * Set the allocated position X of the container
	 *
	 * @param ax allocated position X of the container
	 */
	public void setAllocatedX(int ax) {
		x = ax;
	}

	/**
	 * set width of this area
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * set width of this area
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * get height of this area
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * set height of this area
	 *
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Get the allocated width
	 *
	 * @return Return the allocated width
	 */
	public int getAllocatedWidth() {
		return width;
	}

	/**
	 * Get the allocated height
	 *
	 * @return Return the allocated height
	 */
	public int getAllocatedHeight() {
		return height;
	}

	/**
	 * Sets the baseLine
	 *
	 * @param baseLine
	 */
	public void setBaseLine(int baseLine) {
		this.baseLine = baseLine;
	}

	/**
	 * Gets the baseline
	 *
	 * @return the baseline
	 */
	public int getBaseLine() {
		if (baseLine == 0) {
			return height;
		}
		return baseLine;
	}

	/**
	 * Get the abstract area
	 *
	 * @return Return the abstract area
	 */
	public abstract AbstractArea cloneArea();

	/**
	 * Create a deep clone of the area
	 *
	 * @return Return the deep clone of the area
	 */
	public AbstractArea deepClone() {
		return cloneArea();
	}

	/**
	 * Get the allocated position X
	 *
	 * @return Return the allocated position X
	 */
	public int getAllocatedX() {
		return x;
	}

	/**
	 * Get the allocated position Y
	 *
	 * @return Return the allocated position Y
	 */
	public int getAllocatedY() {
		return y;
	}

	/**
	 * Verify the reordering whether it is to ignore
	 *
	 * @return true, ignore reordering
	 */
	public boolean isIgnoreReordering() {
		return ignoreReordering;
	}

	/**
	 * Set the reordering ignore flag (true: ignore)
	 *
	 * @param ignoreReordering
	 */
	public void setIgnoreReordering(boolean ignoreReordering) {
		this.ignoreReordering = ignoreReordering;
	}

	/**
	 * Print debug information
	 *
	 * @param area
	 */
	public static void debugPrint(IArea area) {
		if (area instanceof IContainerArea) {
			System.out.print(area.getClass() + "||");
			System.out.println("x:" + area.getX() + " y:" + area.getY() + " width:" + area.getWidth() + " height:"
					+ area.getHeight());
			for (Iterator<IArea> i = ((IContainerArea) area).getChildren(); i.hasNext();) {
				IArea child = i.next();
				debugPrint(child);
			}
		} else {
			debugPrintVisual(area);
		}
	}

	/**
	 * Print debug information of visuals
	 *
	 * @param area
	 */
	public static void debugPrintVisual(IArea area) {
		if (area instanceof ITextArea) {
			System.out.println(((ITextArea) area).getText());
		} else if (area instanceof IImageArea) {
			System.out.println("[image]");
		} else if (area instanceof ITemplateArea) {
			System.out.println("[template]");
		}
	}

	/**
	 * Print debug information of with computed position X, Y
	 *
	 * @param area
	 */
	public static void debugPrintWithComputedXY(IArea area) {
		debugPrint(area, 0, 0);
	}

	private static void debugPrint(IArea area, int x, int y) {
		if (area instanceof IContainerArea) {
			System.out.print(area.getClass() + "||");
			if (null != ((ContainerArea) area).getContent()) {
				System.out.print(((ContainerArea) area).getContent().getInstanceID() + "||");
			}
			if (area instanceof CellArea) {
				System.out.print("rowspan:" + ((CellArea) area).getRowSpan() + "||");
			}
			x = x + area.getX();
			y = y + area.getY();
			System.out.println("x:" + x + " y:" + y + " width:" + area.getWidth() + " height:" + area.getHeight());
			for (Iterator<IArea> i = ((IContainerArea) area).getChildren(); i.hasNext();) {
				IArea child = i.next();
				debugPrint(child, x, y);
			}
			x = x - area.getX();
			y = x - area.getY();
		} else {
			debugPrintVisual(area);
		}
	}

}
