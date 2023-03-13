/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.fieldassist;

import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.jface.fieldassist.IControlContentAdapter;
import org.eclipse.jface.fieldassist.IControlContentAdapter2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Control;

/**
 * The class is used to set and retrieve content from CCombo. it just refer to
 * the source code of {@link org.eclipse.jface.fieldassist.ComboContentAdapter}
 * for CCombo.
 *
 * @since 2.5
 */

public class CComboContentAdapter implements IControlContentAdapter, IControlContentAdapter2 {

	/*
	 * Set to <code>true</code> if we should compute the text vertical bounds rather
	 * than just use the field size. Workaround for
	 * https://bugs.eclipse.org/bugs/show_bug.cgi?id=164748 The corresponding SWT
	 * bug is https://bugs.eclipse.org/bugs/show_bug.cgi?id=44072
	 */
	private static final boolean COMPUTE_TEXT_USING_CLIENTAREA = !"carbon".equals(SWT.getPlatform()); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 *
	 * @seeorg.eclipse.jface.dialogs.taskassistance.IControlContentAdapter#
	 * getControlContents(org.eclipse.swt.widgets.Control)
	 */
	@Override
	public String getControlContents(Control control) {
		return ChartUIUtil.getText(control);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter#setControlContents
	 * (org.eclipse.swt.widgets.Control, java.lang.String, int)
	 */
	@Override
	public void setControlContents(Control control, String text, int cursorPosition) {
		ChartUIUtil.setText(control, text);
		((CCombo) control).setSelection(new Point(cursorPosition, cursorPosition));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.jface.fieldassist.IControlContentAdapter#insertControlContents
	 * (org.eclipse.swt.widgets.Control, java.lang.String, int)
	 */
	@Override
	public void insertControlContents(Control control, String text, int cursorPosition) {
		CCombo combo = (CCombo) control;
		String contents = ChartUIUtil.getText(combo);
		Point selection = combo.getSelection();
		StringBuilder sb = new StringBuilder();
		sb.append(contents.substring(0, selection.x));
		sb.append(text);
		if (selection.y < contents.length()) {
			sb.append(contents.substring(selection.y));
		}
		ChartUIUtil.setText(combo, sb.toString());
		selection.x = selection.x + cursorPosition;
		selection.y = selection.x;
		combo.setSelection(selection);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter#getCursorPosition
	 * (org.eclipse.swt.widgets.Control)
	 */
	@Override
	public int getCursorPosition(Control control) {
		return ((CCombo) control).getSelection().x;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter#getInsertionBounds
	 * (org.eclipse.swt.widgets.Control)
	 */
	@Override
	public Rectangle getInsertionBounds(Control control) {
		// This doesn't take horizontal scrolling into affect.
		// see https://bugs.eclipse.org/bugs/show_bug.cgi?id=204599
		CCombo combo = (CCombo) control;
		int position = combo.getSelection().y;
		String contents = ChartUIUtil.getText(combo);
		GC gc = new GC(combo);
		gc.setFont(combo.getFont());
		Point extent = gc.textExtent(contents.substring(0, Math.min(position, contents.length())));
		gc.dispose();
		if (COMPUTE_TEXT_USING_CLIENTAREA) {
			return new Rectangle(combo.getClientArea().x + extent.x, combo.getClientArea().y, 1,
					combo.getClientArea().height);
		}
		return new Rectangle(extent.x, 0, 1, combo.getSize().y);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter#setCursorPosition
	 * (org.eclipse.swt.widgets.Control, int)
	 */
	@Override
	public void setCursorPosition(Control control, int index) {
		((CCombo) control).setSelection(new Point(index, index));
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#getSelection(org.eclipse.swt.widgets.Control)
	 *
	 * @since 3.4
	 */
	@Override
	public Point getSelection(Control control) {
		return ((CCombo) control).getSelection();
	}

	/**
	 * @see org.eclipse.jface.fieldassist.IControlContentAdapter2#setSelection(org.eclipse.swt.widgets.Control,
	 *      org.eclipse.swt.graphics.Point)
	 */
	@Override
	public void setSelection(Control control, Point range) {
		((CCombo) control).setSelection(range);
	}

}
