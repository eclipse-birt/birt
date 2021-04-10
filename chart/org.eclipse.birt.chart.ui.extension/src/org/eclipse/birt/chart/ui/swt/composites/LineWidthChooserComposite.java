/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.ui.swt.AbstractLineWidthChooserComposite;
import org.eclipse.birt.core.ui.swt.custom.ICustomChoice;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * LineWidthChooserComposite
 */
public class LineWidthChooserComposite extends AbstractLineWidthChooserComposite {

	private static final Integer[] iLineWidths = new Integer[] { Integer.valueOf(1), Integer.valueOf(2),
			Integer.valueOf(3), Integer.valueOf(4) };

	static class LineWidthChoice extends LineCanvas implements ICustomChoice {

		LineWidthChoice(Composite parent, int iStyle, int iLineWidth) {
			super(parent, iStyle, SWT.LINE_SOLID, iLineWidth);
		}

		public Object getValue() {
			return Integer.valueOf(getLineWidth());
		}

		public void setValue(Object value) {
			if (value != null) {
				setLineWidth(((Integer) value).intValue());
			}
		}

	}

	public LineWidthChooserComposite(Composite parent, int style, int iWidth) {
		super(parent, style, Integer.valueOf(iWidth));
		setItems(iLineWidths);
	}

	public LineWidthChooserComposite(Composite parent, int style, int iWidth, Integer[] lineWidths) {
		super(parent, style, Integer.valueOf(iWidth));
		setItems(lineWidths);
	}

	protected ICustomChoice createChoice(Composite parent, Object choiceValue) {
		if (choiceValue == null) {
			choiceValue = Integer.valueOf(0);
		}
		return new LineWidthChoice(parent, SWT.NONE, ((Integer) choiceValue).intValue());
	}

	/**
	 * Returns the currently selected line width
	 * 
	 * @return currently selected line width
	 */
	public int getLineWidth() {
		return ((Integer) getChoiceValue()).intValue();
	}

	public void setLineWidth(int iWidth) {
		setChoiceValue(Integer.valueOf(iWidth));
	}

	@Override
	public void setLineWidth(int iWidth, EObject eParent) {
		setLineWidth(iWidth);
	}

}