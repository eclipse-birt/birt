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

import org.eclipse.birt.report.engine.content.IRowContent;
import org.eclipse.birt.report.engine.content.IStyle;
import org.eclipse.birt.report.engine.layout.area.IArea;

public class RowArea extends ContainerArea {
	public void addChild(IArea area) {
		super.addChild(area);
	}

	RowArea(IRowContent row) {
		super(row);
		style.setProperty(IStyle.STYLE_BORDER_TOP_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_LEFT_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_RIGHT_WIDTH, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_BORDER_BOTTOM_WIDTH, IStyle.NUMBER_0);
		// Row does not support margin, remove them.
		style.setProperty(IStyle.STYLE_MARGIN_TOP, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_LEFT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_RIGHT, IStyle.NUMBER_0);
		style.setProperty(IStyle.STYLE_MARGIN_BOTTOM, IStyle.NUMBER_0);
	}

	public int getRowID() {
		if (content != null) {
			return ((IRowContent) content).getRowID();
		}
		return 0;
	}

}
