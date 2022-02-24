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

package org.eclipse.birt.report.engine.nLayout.area.style;

import java.awt.Color;

import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;
import org.w3c.dom.css.CSSValue;

public class BorderInfo extends AreaConstants {

	private Color color;
	private int width;
	private int style;

	public BorderInfo(Color color, int style, int width) {
		this.color = color;
		this.style = style;
		this.width = width;
	}

	public BorderInfo(BorderInfo border) {
		this.color = border.color;
		this.style = border.style;
		this.width = border.width;
	}

	public BorderInfo(CSSValue color, CSSValue style, CSSValue width) {
		this(PropertyUtil.getColor(color), valueStyleMap.get(style), PropertyUtil.getDimensionValue(width));
	}

	public BorderInfo(CSSValue color, CSSValue style, int width) {
		this(PropertyUtil.getColor(color), valueStyleMap.get(style), width);
	}

	public int getStyle() {
		return style;
	}

	public Color getColor() {
		return color;
	}

	public int getWidth() {
		return width;
	}
}
