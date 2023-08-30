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

import org.eclipse.birt.report.engine.content.IContent;
import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.content.impl.ReportContent;
import org.eclipse.birt.report.engine.css.dom.AbstractStyle;
import org.eclipse.birt.report.engine.css.dom.AreaStyle;
import org.eclipse.birt.report.engine.css.dom.ComputedStyle;
import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.css.engine.value.css.CSSValueConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

/**
 * abstract area which is the default implementation of <code>IArea</code>
 *
 */
public abstract class AbstractArea implements IArea {

	/**
	 * style of this area
	 */
	protected IStyle style;

	/**
	 * x position of this area in parent area
	 */
	protected int x;

	/**
	 * y position of this area in parent area
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
	 * the content object
	 */
	protected IContent content;

	protected boolean hasBoxProperty = true;

	/**
	 * the baseline
	 */
	protected int baseLine = 0;

	private static BIRTCSSEngine emptyCssEngine = new BIRTCSSEngine();

	/**
	 * constructor
	 *
	 * @param content
	 */
	AbstractArea(IContent content) {
		this.content = content;
		if (content != null) {
			style = new AreaStyle((ComputedStyle) content.getComputedStyle());
			IStyle contentStyle = content.getStyle();
			if (contentStyle == null || contentStyle.isEmpty()) {
				hasBoxProperty = false;
			}
		} else {
			style = new AreaStyle(emptyCssEngine);
			hasBoxProperty = false;
		}
	}

	AbstractArea(IContent content, IStyle style) {
		this.content = content;
		this.style = style;
	}

	AbstractArea(IReportContent report) {
		if (report != null) {
			assert (report instanceof ReportContent);
			style = new AreaStyle(((ReportContent) report).getCSSEngine());

		} else {
			style = new AreaStyle(emptyCssEngine);
		}
		hasBoxProperty = false;
	}

	protected float scale = 1.0f;

	/**
	 * Set the scale
	 *
	 * @param scale scale value
	 */
	public void setScale(float scale) {
		this.scale = scale;
	}

	@Override
	public float getScale() {
		return this.scale;
	}

	/**
	 * set allocated position
	 *
	 * @param ax
	 * @param ay
	 */
	public void setAllocatedPosition(int ax, int ay) {
		if (hasBoxProperty) {
			x = ax + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT));
			y = ay + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP));
		} else {
			x = ax;
			y = ay;
		}
	}

	/**
	 * set allocated height
	 *
	 * @param aHeight
	 */
	public void setAllocatedHeight(int aHeight) {
		if (hasBoxProperty) {
			height = aHeight - PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP))
					- PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM));
		} else {
			height = aHeight;
		}
	}

	/**
	 * set allocated width
	 *
	 * @param aWidth
	 */
	public void setAllocatedWidth(int aWidth) {
		if (hasBoxProperty) {
			int totalMarginWidth = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT));
			if (totalMarginWidth >= aWidth) {
				style.setProperty(StyleConstants.STYLE_MARGIN_LEFT, CSSValueConstants.NUMBER_0);
				style.setProperty(StyleConstants.STYLE_MARGIN_RIGHT, CSSValueConstants.NUMBER_0);
				width = aWidth;
			} else {
				width = aWidth - totalMarginWidth;
			}
		} else {
			width = aWidth;
		}
	}

	/**
	 * Set content height
	 *
	 * @param cHeight content height
	 */
	public void setContentHeight(int cHeight) {
		if (hasBoxProperty) {
			height = cHeight + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		} else {
			height = cHeight;
		}
	}

	/**
	 * Set content width
	 *
	 * @param cWidth content width
	 */
	public void setContentWidth(int cWidth) {
		if (hasBoxProperty) {
			width = cWidth + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT));
		} else {
			width = cWidth;
		}
	}

	/**
	 * Get allocated X position
	 *
	 * @return Return the allocated X position
	 */
	public int getAllocatedX() {
		if (hasBoxProperty) {
			return x - PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT));
		}
		return x;
	}

	/**
	 * Get allocated Y position
	 *
	 * @return Return the allocated Y position
	 */
	public int getAllocatedY() {
		if (hasBoxProperty) {
			return y - PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP));
		}
		return y;
	}

	/**
	 * Get content width
	 *
	 * @return Return the content width
	 */
	public int getContentWidth() {
		if (hasBoxProperty) {
			int totalPaddngWidth = PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_LEFT))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT));
			int totalBorderWidth = PropertyUtil
					.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH));
			if (width <= totalPaddngWidth) {
				style.setProperty(StyleConstants.STYLE_PADDING_LEFT, CSSValueConstants.NUMBER_0);
				style.setProperty(StyleConstants.STYLE_PADDING_RIGHT, CSSValueConstants.NUMBER_0);
				return width - totalBorderWidth;
			}
			return width - totalPaddngWidth - totalBorderWidth;
		}
		return width;
	}

	/**
	 * Get content height
	 *
	 * @return Return the content height
	 */
	public int getContentHeight() {
		if (hasBoxProperty) {
			return height - PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH))
					- PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH))
					- PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_TOP))
					- PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM));
		}
		return height;
	}

	/**
	 * Get allocated width
	 *
	 * @return Return the allocated width
	 */
	public int getAllocatedWidth() {
		if (hasBoxProperty) {
			return width + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_LEFT))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_RIGHT));
		}
		return width;
	}

	/**
	 * Get allocated height
	 *
	 * @return Return the allocated height
	 */
	public int getAllocatedHeight() {
		if (hasBoxProperty) {
			return height + PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_TOP))
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_MARGIN_BOTTOM));
		}
		return height;
	}

	/**
	 * Get style of this area
	 */
	@Override
	public IStyle getStyle() {
		return new WrappedAreaStyle((AbstractStyle) style);
	}

	/**
	 * Get X position of this area
	 */
	@Override
	public int getX() {
		return x;
	}

	/**
	 * Get Y position of this area
	 */
	@Override
	public int getY() {
		return y;
	}

	/**
	 * Set the position
	 *
	 * @param x
	 * @param y
	 */
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/**
	 * Set width of this area
	 *
	 * @param width
	 */
	public void setWidth(int width) {
		this.width = width;
	}

	/**
	 * Set width of this area
	 */
	@Override
	public int getWidth() {
		return width;
	}

	/**
	 * Get height of this area
	 */
	@Override
	public int getHeight() {
		return height;
	}

	/**
	 * Set height of this area
	 *
	 * @param height
	 */
	public void setHeight(int height) {
		this.height = height;
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
	 * get content object
	 */
	@Override
	public IContent getContent() {
		return content;
	}

	protected void removeMargin() {
		style.setProperty(StyleConstants.STYLE_MARGIN_LEFT, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_MARGIN_RIGHT, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_MARGIN_TOP, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_MARGIN_BOTTOM, CSSValueConstants.NUMBER_0);

	}

	protected void removeBorder() {
		style.setProperty(StyleConstants.STYLE_BORDER_TOP_WIDTH, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_BORDER_BOTTOM_WIDTH, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_BORDER_LEFT_WIDTH, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_BORDER_RIGHT_WIDTH, CSSValueConstants.NUMBER_0);
	}

	protected void removePadding() {
		style.setProperty(StyleConstants.STYLE_PADDING_LEFT, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_PADDING_RIGHT, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_PADDING_TOP, CSSValueConstants.NUMBER_0);
		style.setProperty(StyleConstants.STYLE_PADDING_BOTTOM, CSSValueConstants.NUMBER_0);
	}

	class WrappedAreaStyle extends AbstractStyle implements IStyle {

		IStyle style;

		WrappedAreaStyle(AbstractStyle style) {
			super(style.getCSSEngine());
			this.style = style;
		}

		@Override
		public CSSValue getProperty(int index) {
			return style.getProperty(index);
		}

		@Override
		public boolean isEmpty() {
			return style.isEmpty();
		}

		@Override
		public void setProperty(int index, CSSValue value) {
			style.setProperty(index, value);
			AbstractArea.this.hasBoxProperty = true;
		}
	}

}
