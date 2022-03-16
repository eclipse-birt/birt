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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Text;

/**
 * The class extends {@link org.eclipse.jface.fieldassist.TextContentAdapter},
 * and supports to notify content change to listeners when set/insert action
 * happens.
 *
 * @since 2.5
 */

public class CTextContentAdapter extends TextContentAdapter {

	/** The listeners list. */
	private List<IContentChangeListener> listeners = new ArrayList<>(2);

	/**
	 * Constructor.
	 */
	CTextContentAdapter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.jface.fieldassist.TextContentAdapter#setControlContents(org
	 * .eclipse.swt.widgets.Control, java.lang.String, int)
	 */
	@Override
	public void setControlContents(Control control, String text, int cursorPosition) {
		String oldValue = ((Text) control).getText();
		((Text) control).setText(text);
		((Text) control).setSelection(cursorPosition, cursorPosition);
		notifyContentChanged(control, ((Text) control).getText(), oldValue);
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
		String oldValue = ((Text) control).getText();
		Point selection = ((Text) control).getSelection();
		((Text) control).insert(text);
		// Insert will leave the cursor at the end of the inserted text. If this
		// is not what we wanted, reset the selection.
		if (cursorPosition < text.length()) {
			((Text) control).setSelection(selection.x + cursorPosition, selection.x + cursorPosition);
		}
		notifyContentChanged(control, ((Text) control).getText(), oldValue);
	}

	/**
	 * Add content change listeners.
	 *
	 * @param listener
	 */
	public void addContentChangeListener(IContentChangeListener listener) {
		if (!listeners.contains(listener)) {
			listeners.add(0, listener);
		}
	}

	/**
	 * Remove content change listener.
	 *
	 * @param listener
	 */
	public void removeContentChangeListener(IContentChangeListener listener) {
		listeners.remove(listener);
	}

	/**
	 * Notify content is changed to all listeners.
	 *
	 * @param control  the control whose content is changed.
	 * @param newValue the new content.
	 * @param oldValue the old content.
	 */
	public void notifyContentChanged(Control control, Object newValue, Object oldValue) {
		Object[] s = listeners.toArray();
		for (int i = 0; i < s.length; i++) {
			((IContentChangeListener) s[i]).contentChanged(control, newValue, oldValue);
		}
	}
}
