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
import org.eclipse.birt.chart.ui.swt.AbstractHeadStyleChooserComposite;
import org.eclipse.birt.core.ui.swt.custom.ICustomChoice;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * Choose the Line decorator of needle
 */
public class HeadStyleChooserComposite extends AbstractHeadStyleChooserComposite {
	private static final Integer[] iLineDecorators = new Integer[] { Integer.valueOf(LineDecorator.ARROW),
			Integer.valueOf(LineDecorator.NONE), Integer.valueOf(LineDecorator.CIRCLE) };

	static class HeaderStyleChoice extends HeadStyleCanvas implements ICustomChoice {

		HeaderStyleChoice(Composite parent, int iStyle, int iLineDecorator) {
			super(parent, iStyle, iLineDecorator);
		}

		public Object getValue() {
			return Integer.valueOf(getHeadStyle());
		}

		public void setValue(Object value) {
			if (value != null) {
				setHeadStyle(((Integer) value).intValue());
			}
		}

	}

	public HeadStyleChooserComposite(Composite parent, int style, int iLineDecorator) {
		super(parent, style, Integer.valueOf(iLineDecorator));
		setItems(iLineDecorators);
	}

	protected ICustomChoice createChoice(Composite parent, Object choiceValue) {
		if (choiceValue == null) {
			choiceValue = Integer.valueOf(0);
		}
		return new HeaderStyleChoice(parent, SWT.NONE, ((Integer) choiceValue).intValue());
	}

	/**
	 * Returns the current selected head style as an integer.
	 * 
	 */
	public int getHeadStyle() {
		return ((Integer) getChoiceValue()).intValue();
	}

	public void setHeadStyle(int iStyle) {
		setChoiceValue(Integer.valueOf(iStyle));
	}
}
