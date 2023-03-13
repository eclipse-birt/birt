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

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 *
 */
public class LineCanvas extends Canvas implements PaintListener, FocusListener {

	protected int iLineStyle = SWT.LINE_SOLID;

	protected int iLineWidth = 1;

	protected boolean isFocusIn = false;

	public LineCanvas(Composite parent, int iStyle, int iLineStyle, int iLineWidth) {
		super(parent, iStyle);
		this.iLineStyle = iLineStyle;
		this.iLineWidth = iLineWidth;
		this.addPaintListener(this);
		this.addFocusListener(this);
	}

	public int getLineStyle() {
		return this.iLineStyle;
	}

	public void setLineStyle(int iLineStyle) {
		this.iLineStyle = iLineStyle;
	}

	public int getLineWidth() {
		return this.iLineWidth;
	}

	public void setLineWidth(int iLineWidth) {
		this.iLineWidth = iLineWidth;
	}

	@Override
	public void paintControl(PaintEvent pe) {
		if (isEnabled() && isFocusControl()) {
			isFocusIn = true;
		}

		Color cForeground = null;
		Color cBackground = null;
		if (this.isEnabled()) {
			cForeground = getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
			cBackground = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
		} else {
			cForeground = getDisplay().getSystemColor(SWT.COLOR_DARK_GRAY);
			cBackground = getDisplay().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND);
		}

		GC gc = pe.gc;
		if (isFocusIn) {
			gc.setBackground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION));
			gc.setForeground(getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT));
		} else {
			gc.setBackground(cBackground);
			gc.setForeground(cForeground);
		}

		gc.fillRectangle(0, 0, this.getSize().x, this.getSize().y);
		gc.setLineStyle(iLineStyle);
		gc.setLineWidth(iLineWidth);
		gc.drawLine(10, this.getSize().y / 2, this.getSize().x - 10, this.getSize().y / 2);

	}

	@Override
	public void setEnabled(boolean bState) {
		super.setEnabled(bState);
		redraw();
	}

	@Override
	public void focusGained(FocusEvent e) {
		isFocusIn = true;

	}

	@Override
	public void focusLost(FocusEvent e) {
		isFocusIn = false;
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		Point size = new Point(100, 20);
		if (wHint != SWT.DEFAULT) {
			size.x = wHint;
		}
		if (hHint != SWT.DEFAULT) {
			size.y = hHint;
		}
		Rectangle trim = computeTrim(0, 0, size.x, size.y);
		return new Point(trim.width, trim.height);
	}
}
