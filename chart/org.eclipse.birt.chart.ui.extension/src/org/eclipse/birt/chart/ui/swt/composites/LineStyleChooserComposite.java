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

import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.ui.swt.AbstractLineStyleChooserComposite;
import org.eclipse.birt.chart.ui.util.ChartUIExtensionUtil;
import org.eclipse.birt.core.ui.swt.custom.ICustomChoice;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * LineStyleChooserComposite
 */
public class LineStyleChooserComposite extends AbstractLineStyleChooserComposite {

	static class LineStyleChoice extends LineCanvas implements ICustomChoice {

		LineStyleChoice(Composite parent, int iStyle, int iLineStyle) {
			super(parent, iStyle, iLineStyle, 1);
		}

		public Object getValue() {
			return Integer.valueOf(getLineStyle());
		}

		public void setValue(Object value) {
			if (value != null) {
				setLineStyle(((Integer) value).intValue());
			}
		}

	}

	public LineStyleChooserComposite(Composite parent, int style, int iLineStyle) {
		this(parent, style, iLineStyle,
				new Integer[] { SWT.NONE, SWT.LINE_SOLID, SWT.LINE_DASH, SWT.LINE_DASHDOT, SWT.LINE_DOT });
	}

	public LineStyleChooserComposite(Composite parent, int style, int iLineStyle, Integer[] lineStyleItems) {
		super(parent, style, Integer.valueOf(iLineStyle));
		setItems(lineStyleItems);
	}

	protected ICustomChoice createChoice(Composite parent, Object choiceValue) {
		if (choiceValue == null) {
			choiceValue = Integer.valueOf(0);
		}
		return new LineStyleChoice(parent, SWT.NONE, ((Integer) choiceValue).intValue());
	}

	/**
	 * Returns the current selected line style as an integer corresponding to the
	 * appropriate SWT constants.
	 * 
	 */
	public int getLineStyle() {
		return ((Integer) getChoiceValue()).intValue();
	}

	public void setLineStyle(int iStyle) {
		setChoiceValue(Integer.valueOf(iStyle));
	}

	@Override
	public void setLineStyle(LineStyle style, EObject eParent) {
		setLineStyle(ChartUIExtensionUtil.getSWTLineStyle(style));
	}

}
