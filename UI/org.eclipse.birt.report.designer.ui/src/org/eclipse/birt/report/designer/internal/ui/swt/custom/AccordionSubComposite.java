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

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class AccordionSubComposite extends ScrolledComposite {

	public AccordionSubComposite(Composite parent, int style) {
		super(parent, style);
	}

	void onPaint(PaintEvent event) {
		Rectangle rect = getClientArea();
		if (rect.width == 0 || rect.height == 0) {
			return;
		}

		GC gc = event.gc;

		Rectangle r = getClientArea();

		Display disp = getDisplay();

		Color c1 = null;
		Color c2 = null;

		int style = getStyle();
		if ((style & SWT.SHADOW_IN) != 0) {
			c1 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
			c2 = disp.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
		}
		if ((style & SWT.SHADOW_OUT) != 0) {
			c1 = disp.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW);
			c2 = disp.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
		}

		if (c1 != null && c2 != null) {
			gc.setLineWidth(1);
			drawBevelRect(gc, r.x, r.y, r.width - 1, r.height - 1, c1, c2);
		}

	}

	private void drawBevelRect(GC gc, int x, int y, int w, int h, Color topleft, Color bottomright) {
		gc.setForeground(bottomright);
		gc.drawLine(x + w, y, x + w, y + h);
		gc.drawLine(x, y + h, x + w, y + h);

		gc.setForeground(topleft);
		gc.drawLine(x, y, x + w - 1, y);
		gc.drawLine(x, y, x, y + h - 1);
	}

}
