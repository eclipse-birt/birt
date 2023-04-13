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

public class BorderInfomation {

	public static final String BORDER_LEFT = "left"; //$NON-NLS-1$
	public static final String BORDER_TOP = "top"; //$NON-NLS-1$
	public static final String BORDER_RIGHT = "right"; //$NON-NLS-1$
	public static final String BORDER_BOTTOM = "bottom"; //$NON-NLS-1$
	public static final String BORDER_DIAGONAL = "diagonal"; //$NON-NLS-1$
	public static final String BORDER_ANTIDIAGONAL = "antidiagonal"; //$NON-NLS-1$

	public static String getBorderBottom() {
		return BORDER_BOTTOM;
	}

	public String getDefaultStyle() {
		return defaultStyle;
	}

	public String getDefaultWidth() {
		return defaultWidth;
	}

	public RGB getDefaultColor() {
		return defaultColor;
	}

	public String getInheritedStyle() {
		return inheritedStyle;
	}

	public String getInheritedWidth() {
		return inheritedWidth;
	}

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

	public RGB getColor() {
		if (color != null) {
			return color;
		} else if (inheritedColor != null) {
			return inheritedColor;
		} else {
			return defaultColor;
		}
	}

	public boolean isInheritedColor() {
		if (color != null) {
			return false;
		} else if (inheritedColor != null) {
			return true;
		} else {
			return false;
		}
	}

	public String getPosition() {
		return position;
	}

	public String getStyle() {
		if (style != null && !style.equals("")) {
			return style;
		} else if (inheritedStyle != null && !inheritedStyle.equals("")) {
			return inheritedStyle;
		} else {
			return defaultStyle;
		}
	}

	public String getOriginStyle() {
		return style;
	}

	public RGB getOriginColor() {
		return color;
	}

	public String getOriginWidth() {
		return width;
	}

	public boolean isInheritedStyle() {
		if (style != null && !style.equals("")) {
			return false;
		} else if (inheritedStyle != null && !inheritedStyle.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public String getWidth() {
		if (width != null && !width.equals("")) {
			return width;
		} else if (inheritedWidth != null && !inheritedWidth.equals("")) {
			return inheritedWidth;
		} else {
			return defaultWidth;
		}
	}

	public boolean isInheritedWidth() {
		if (width != null && !width.equals("")) {
			return false;
		} else if (inheritedWidth != null && !inheritedWidth.equals("")) {
			return true;
		} else {
			return false;
		}
	}

	public void setColor(RGB color) {
		this.color = color;
	}

	public void setDefaultColor(RGB defaultColor) {
		this.defaultColor = defaultColor;
	}

	public void setInheritedColor(RGB color) {
		this.inheritedColor = color;
	}

	public void setInheritedStyle(String style) {
		this.inheritedStyle = style;
	}

	public void setInheritedWidth(String width) {
		this.inheritedWidth = width;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public void setDefaultStyle(String style) {
		this.defaultStyle = style;
	}

	public void setDefaultWidth(String width) {
		this.defaultWidth = width;
	}
}
