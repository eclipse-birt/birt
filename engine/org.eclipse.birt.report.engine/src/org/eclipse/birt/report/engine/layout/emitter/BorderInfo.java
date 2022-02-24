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
package org.eclipse.birt.report.engine.layout.emitter;

import java.awt.Color;

public class BorderInfo {
	public static final int TOP_BORDER = 0;
	public static final int RIGHT_BORDER = 1;
	public static final int BOTTOM_BORDER = 2;
	public static final int LEFT_BORDER = 3;

	public int startX, startY, endX, endY;
	public int borderWidth;
	public Color borderColor;
	public int borderStyle;
	public int borderType;

	public BorderInfo(int startX, int startY, int endX, int endY, int borderWidth, Color borderColor, int borderStyle,
			int borderType) {
		this.startX = startX;
		this.startY = startY;
		this.endX = endX;
		this.endY = endY;
		this.borderWidth = borderWidth;
		this.borderColor = borderColor;
		if (borderStyle != org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo.BORDER_STYLE_DOUBLE
				&& borderStyle != org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo.BORDER_STYLE_DASHED
				&& borderStyle != org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo.BORDER_STYLE_DOTTED) {
			this.borderStyle = org.eclipse.birt.report.engine.nLayout.area.style.BorderInfo.BORDER_STYLE_SOLID;
		} else {
			this.borderStyle = borderStyle;
		}

		this.borderType = borderType;
	}
}
