/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/
package org.eclipse.birt.report.engine.layout.area.impl;

import org.eclipse.birt.report.engine.content.IReportContent;
import org.eclipse.birt.report.engine.css.engine.StyleConstants;
import org.eclipse.birt.report.engine.layout.area.IArea;
import org.eclipse.birt.report.engine.layout.pdf.util.PropertyUtil;

public class LineArea extends LogicContainerArea {
	LineArea(IReportContent report) {
		super(report);
	}

	public void addChild(IArea area) {
		int childHorizontalSpan = area.getX() + area.getWidth();
		int childVerticalSpan = area.getY() + area.getHeight();

		if (childHorizontalSpan > width) {
			setWidth(childHorizontalSpan
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_RIGHT)));
		}

		if (childVerticalSpan > height) {
			setHeight(childVerticalSpan
					+ PropertyUtil.getDimensionValue(style.getProperty(StyleConstants.STYLE_PADDING_BOTTOM)));
		}
		children.add(area);
	}
}
