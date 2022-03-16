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

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import com.ibm.icu.text.DecimalFormatSymbols;

/**
 * The class extends {@link TextAssistField} and processes for numeric value.
 *
 * @since 2.5
 */

public class TextNumberEditorAssistField extends TextAssistField {

	/**
	 * Constructor.
	 *
	 * @param text      the text to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 */
	public TextNumberEditorAssistField(Text text, Composite composite) {
		this(text, composite, null);
	}

	/**
	 * Constructor.
	 *
	 * @param text      the text to be decorated.
	 * @param composite The SWT composite within which the decoration should be
	 *                  rendered. The decoration will be clipped to this composite,
	 *                  but it may be rendered on a child of the composite. The
	 *                  decoration will not be visible if the specified composite or
	 *                  its child composites are not visible in the space relative
	 *                  to the control, where the decoration is to be rendered. If
	 *                  this value is null, then the decoration will be rendered on
	 *                  whichever composite (or composites) are located in the
	 *                  specified position.
	 * @param values    the available contents.
	 */
	public TextNumberEditorAssistField(Text text, Composite composite, String[] values) {
		super(text, composite, values);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.SmartField#isValid()
	 */
	@Override
	public boolean isValid() {
		String contents = getContents();
		if (contents == null || "".equals(contents.trim())) //$NON-NLS-1$
		{
			return true;
		}
		char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
		int length = contents.length();
		for (int i = 0; i < length;) {
			char ch = contents.charAt(i++);
			if (!Character.isDigit(ch) && ch != '.' && ch != groupingSeparator) {
				return false;
			}
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.AssistField#hasQuickFix()
	 */
	@Override
	public boolean hasQuickFix() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.ui.swt.fieldassist.AssistField#quickFix()
	 */
	@Override
	public void quickFix() {
		String contents = getContents();
		StringBuilder numbersOnly = new StringBuilder();
		int length = contents.length();
		char groupingSeparator = DecimalFormatSymbols.getInstance().getGroupingSeparator();
		for (int i = 0; i < length;) {
			char ch = contents.charAt(i++);
			if (Character.isDigit(ch) || ch == '.' || ch == groupingSeparator) {
				numbersOnly.append(ch);
			}
		}
		setContents(numbersOnly.toString());
	}
}
