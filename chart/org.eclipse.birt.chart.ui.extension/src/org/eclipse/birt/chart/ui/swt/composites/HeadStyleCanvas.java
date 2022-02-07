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

import org.eclipse.birt.chart.model.attribute.LineDecorator;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Draw the graphics within the HeadStyleChooser
 * 
 */
public class HeadStyleCanvas extends Canvas implements PaintListener, FocusListener {

	protected int iLineDecorator = 0;

	protected boolean isFocusIn = false;

	public HeadStyleCanvas(Composite parent, int iStyle, int iLineDecorator) {
		super(parent, iStyle);
		this.iLineDecorator = iLineDecorator;
		this.addPaintListener(this);
		this.addFocusListener(this);
	}

	public int getHeadStyle() {
		return this.iLineDecorator;
	}

	public void setHeadStyle(int iLineDecorator) {
		this.iLineDecorator = iLineDecorator;
	}

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
		gc.setLineWidth(1);
		gc.drawLine(10, this.getSize().y / 2, this.getSize().x - 10, this.getSize().y / 2);
		if (iLineDecorator == LineDecorator.ARROW) {
			int[] points = { this.getSize().x - 15, this.getSize().y / 2 - 3, this.getSize().x - 15,
					this.getSize().y / 2 + 3, this.getSize().x - 10, this.getSize().y / 2 };
			gc.setLineWidth(3);
			gc.drawPolygon(points);
		} else if (iLineDecorator == LineDecorator.CIRCLE) {
			gc.setLineWidth(4);
			gc.drawOval(this.getSize().x - 14, this.getSize().y / 2 - 3, 6, 6);
		}

	}

	public void focusGained(FocusEvent e) {
		isFocusIn = true;

	}

	public void focusLost(FocusEvent e) {
		isFocusIn = false;
	}
}
