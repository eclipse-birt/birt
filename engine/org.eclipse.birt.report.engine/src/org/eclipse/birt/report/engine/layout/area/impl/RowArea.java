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
