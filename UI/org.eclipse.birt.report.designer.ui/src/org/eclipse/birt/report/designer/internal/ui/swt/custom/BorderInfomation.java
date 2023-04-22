/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.swt.custom;

import org.eclipse.swt.graphics.RGB;

/**
 * Border information
 *
 * @since 3.3
 *
 */
public class BorderInfomation {

	/** border property: border left */
	public static final String BORDER_LEFT = "left"; //$NON-NLS-1$
	/** border property: border top */
	public static final String BORDER_TOP = "top"; //$NON-NLS-1$
	/** border property: border right */
	public static final String BORDER_RIGHT = "right"; //$NON-NLS-1$
	/** border property: border bottom */
	public static final String BORDER_BOTTOM = "bottom"; //$NON-NLS-1$
	/** border property: border diagonal */
	public static final String BORDER_DIAGONAL = "diagonal"; //$NON-NLS-1$
	/** border property: border antidiagonal */
	public static final String BORDER_ANTIDIAGONAL = "antidiagonal"; //$NON-NLS-1$

	/**
	 * Get the border bottom
	 *
	 * @return Return the border bottom value
	 */
	public static String getBorderBottom() {
		return BORDER_BOTTOM;
	}

	/**
	 * Get the border default style
	 *
	 * @return Return the border default style
	 */
	public String getDefaultStyle() {
		return defaultStyle;
	}

	/**
	 * Get the border default width
	 *
	 * @return Return the border default width
	 */
	public String getDefaultWidth() {
		return defaultWidth;
	}

	/**
	 * Get the border default color
	 *
	 * @return Return the border default color
	 */
	public RGB getDefaultColor() {
		return defaultColor;
	}

	/**
	 * Get the border inherited style
	 *
	 * @return Return the border inherited style
	 */
	public String getInheritedStyle() {
		return inheritedStyle;
	}

	/**
	 * Get the border inherited width
	 *
	 * @return Return the border inherited width
	 */
	public String getInheritedWidth() {
		return inheritedWidth;
	}

	/**
	 * Get the border inherited color
	 *
	 * @return Return the border inherited color
	 */
	public RGB getInheritedColor() {
		return inheritedColor;
	}

	private String position;
	private String style;
	private RGB color;
	private String width;
	private String defaultStyle;
	private String defaultWidth;
	private RGB defaultColor;
	private String inheritedStyle;
	private String inheritedWidth;
	private RGB inheritedColor;

	/**
	 * Get the border color
	 *
	 * @return Return the border color
	 */
	public RGB getColor() {
		if (color != null) {
			return color;
		} else if (inheritedColor != null) {
			return inheritedColor;
		} else {
			return defaultColor;
		}
	}

	/**
	 * Check if the border color is inherited
	 *
	 * @return Return the check result of inherited check of the color
	 */
	public boolean isInheritedColor() {
		if (color != null) {
			return false;
		} else if (inheritedColor != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the position
	 *
	 * @return Return the position
	 */
	public String getPosition() {
		return position;
	}

	/**
	 * Get the border style
	 *
	 * @return Return the border style
	 */
	public String getStyle() {
		if (style != null && !style.equals("")) {
			return style;
		} else if (inheritedStyle != null && !inheritedStyle.equals("")) {
			return inheritedStyle;
		} else {
			return defaultStyle;
		}
	}

	/**
	 * Get the original style
	 *
	 * @return Return the original style
	 */
	public String getOriginStyle() {
		return style;
	}

	/**
	 * Get the original color
	 *
	 * @return Return the original color
	 */
	public RGB getOriginColor() {
		return color;
	}

	/**
	 * Get the original width
	 *
	 * @return Return the original width
	 */
	public String getOriginWidth() {
		return width;
	}

	/**
	 * Check if the style is inherited
	 *
	 * @return Return the check result if the border style is inherited
	 */
	public boolean isInheritedStyle() {
		if (style != null && !style.equals("")) {
			return false;
		} else if (inheritedStyle != null && !inheritedStyle.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get the width
	 *
	 * @return Return the width
	 */
	public String getWidth() {
		if (width != null && !width.equals("")) {
			return width;
		} else if (inheritedWidth != null && !inheritedWidth.equals("")) {
			return inheritedWidth;
		} else {
			return defaultWidth;
		}
	}

	/**
	 * Check if the width is inherited
	 *
	 * @return Return the check result if the border width is inherited
	 */
	public boolean isInheritedWidth() {
		if (width != null && !width.equals("")) {
			return false;
		} else if (inheritedWidth != null && !inheritedWidth.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Set the border color
	 *
	 * @param color RGB color
	 */
	public void setColor(RGB color) {
		this.color = color;
	}

	/**
	 * Set the border default color
	 *
	 * @param defaultColor RGB color
	 */
	public void setDefaultColor(RGB defaultColor) {
		this.defaultColor = defaultColor;
	}

	/**
	 * Set the inherited border color
	 *
	 * @param color RGB color
	 */
	public void setInheritedColor(RGB color) {
		this.inheritedColor = color;
	}

	/**
	 * Set the inherited border style
	 *
	 * @param style border style
	 */
	public void setInheritedStyle(String style) {
		this.inheritedStyle = style;
	}

	/**
	 * Set the inherited border width
	 *
	 * @param width border width
	 */
	public void setInheritedWidth(String width) {
		this.inheritedWidth = width;
	}

	/**
	 * Set the border position
	 *
	 * @param position border position
	 */
	public void setPosition(String position) {
		this.position = position;
	}

	/**
	 * Set the border style
	 *
	 * @param style border style
	 */
	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * Set the border width
	 *
	 * @param width border width
	 */
	public void setWidth(String width) {
		this.width = width;
	}

	/**
	 * Set the border default style
	 *
	 * @param style border style
	 */
	public void setDefaultStyle(String style) {
		this.defaultStyle = style;
	}

	/**
	 * Set the border default width
	 *
	 * @param width border width
	 */
	public void setDefaultWidth(String width) {
		this.defaultWidth = width;
	}
}
