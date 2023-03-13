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

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Custom text combo
 */
public class TextCombo extends CustomChooserComposite {

	private Font fontBold;

	private HashMap<String, Boolean> choiceMarkerMap = new HashMap<>(10);

	private class TextComboChoice extends TextCanvas implements ICustomChoice {

		TextComboChoice(Composite parent, int iStyle, String comboText) {
			super(parent, iStyle, comboText);
		}

		@Override
		public Object getValue() {
			return super.getText();
		}

		@Override
		public void setValue(Object value) {
			super.setText((String) value);

			if (choiceMarkerMap.containsKey(value)) {
				setTextFont(fontBold);
			} else {
				setTextFont(null);
			}
		}

	}

	public TextCombo(Composite parent, int style) {
		super(parent, style);

		GC gc = new GC(this);
		itemHeight = gc.getFontMetrics().getHeight() + 2;

		if (gc.getFont().getFontData() == null || gc.getFont().getFontData().length == 0) {
			fontBold = new Font(Display.getCurrent(), "arial", //$NON-NLS-1$
					9, SWT.BOLD);
		} else {
			FontData fd = gc.getFont().getFontData()[0];
			fontBold = new Font(gc.getDevice(), fd.getName(), fd.getHeight(), fd.getStyle() | SWT.BOLD);
		}
		addDisposeListener(new DisposeListener() {

			@Override
			public void widgetDisposed(DisposeEvent e) {
				fontBold.dispose();
				choiceMarkerMap.clear();
			}
		});
	}

	@Override
	protected ICustomChoice createChoice(Composite parent, Object choiceValue) {
		if (choiceValue == null) {
			choiceValue = ""; //$NON-NLS-1$
		}
		TextComboChoice choice = new TextComboChoice(parent, SWT.NONE, (String) choiceValue);
		if (choiceMarkerMap.containsKey(choiceValue)) {
			choice.setTextFont(fontBold);
		} else {
			choice.setTextFont(null);
		}
		return choice;
	}

	public void setText(String text) {
		setChoiceValue(text);
	}

	public String getText() {
		return (String) getChoiceValue();
	}

	public void deselectAll() {
		choiceMarkerMap.clear();
	}

	public void markSelection(String text) {
		choiceMarkerMap.put(text, Boolean.TRUE);
	}

	public void unmarkSelection(String text) {
		choiceMarkerMap.remove(text);
	}
}
