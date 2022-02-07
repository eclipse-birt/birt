/***********************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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

package org.eclipse.birt.core.ui.swt.custom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;

/**
 * Drawing text in graphics
 */
class TextCanvas extends Canvas implements PaintListener, FocusListener {

	private static final int DEFAULT_MARGIN = 3;
	private int leftMargin = DEFAULT_MARGIN;
	private int topMargin = DEFAULT_MARGIN;
	private int rightMargin = DEFAULT_MARGIN;
	private int bottomMargin = DEFAULT_MARGIN;

	private String text;

	private Font textFont;

	private boolean isFocusIn = false;

	public TextCanvas(Composite parent, int iStyle, String text) {
		super(parent, iStyle);
		this.text = text;
		this.addPaintListener(this);
		this.addFocusListener(this);
	}

	public String getText() {
		return this.text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setTextFont(Font font) {
		this.textFont = font;
	}

	public Font getTextFont() {
		return textFont;
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

		if (textFont != null) {
			gc.setFont(textFont);
		}

		if (text != null) {
			gc.drawText(text, 2, 2);
		}
	}

	public void focusGained(FocusEvent e) {
		isFocusIn = true;

	}

	public void focusLost(FocusEvent e) {
		isFocusIn = false;
	}

	public Point computeSize(int wHint, int hHint, boolean changed) {
		checkWidget();
		Point e = getTotalSize(text);
		if (wHint == SWT.DEFAULT) {
			e.x += leftMargin + rightMargin;
		} else {
			e.x = wHint;
		}
		if (hHint == SWT.DEFAULT) {
			e.y += topMargin + bottomMargin;
		} else {
			e.y = hHint;
		}
		return e;
	}

	private Point getTotalSize(String text) {
		Point size = new Point(0, 0);

		GC gc = new GC(this);
		if (text != null && text.length() > 0) {
			Point e = gc.textExtent(text);
			size.x += e.x;
			size.y = Math.max(size.y, e.y);
		} else {
			size.y = Math.max(size.y, gc.getFontMetrics().getHeight());
		}
		gc.dispose();

		return size;
	}
}
