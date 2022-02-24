/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.composites;

import org.eclipse.birt.chart.computation.ValueFormatter;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.DateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.FractionNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaDateFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.JavaNumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.NumberFormatSpecifier;
import org.eclipse.birt.chart.model.attribute.StringFormatSpecifier;
import org.eclipse.birt.chart.ui.extension.i18n.Messages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;

/**
 *
 */

public class FormatSpecifierPreview extends Composite {

	private final static String SAMPLE_NONE = Messages.getString("FormatSpecifierPreview.Lbl.NoFormat"); //$NON-NLS-1$
	private final static String SAMPLE_NA = Messages.getString("FormatSpecifierPreview.Lbl.NotAvailable"); //$NON-NLS-1$
	private final static Double SAMPLE_NUMBER = new Double(0.12345678);
	private final static Calendar SAMPLE_DATETIME = Calendar.getInstance();
	private final static String SAMPLE_STRING = "My String"; //$NON-NLS-1$

	private Label lblPreview;

	private String dataType;

	private boolean isInEditor = true;

	public FormatSpecifierPreview(Composite parent, int style, boolean isInEditor) {
		super(parent, style);
		this.isInEditor = isInEditor;

		setLayout(new FillLayout());
		lblPreview = new Label(this, SWT.NONE);
		lblPreview.setBackground(parent.getBackground());
	}

	public void updatePreview(FormatSpecifier fs) {
		if (fs == null) {
			lblPreview.setText(SAMPLE_NONE);
			// Re-layout the composite to prevent label truncated
			getParent().layout();
			return;
		}

		if (!isInEditor || dataType == null) {
			if (fs instanceof DateFormatSpecifier || fs instanceof JavaDateFormatSpecifier) {
				dataType = FormatSpecifierComposite.DATA_TYPE_DATETIME;
			} else if (fs instanceof NumberFormatSpecifier || fs instanceof JavaNumberFormatSpecifier
					|| fs instanceof FractionNumberFormatSpecifier) {
				dataType = FormatSpecifierComposite.DATA_TYPE_NUMBER;
			} else if (fs instanceof StringFormatSpecifier) {
				dataType = FormatSpecifierComposite.DATA_TYPE_STRING;
			} else {
				dataType = FormatSpecifierComposite.DATA_TYPE_NONE;
			}
		}

		if (FormatSpecifierComposite.DATA_TYPE_NONE.equals(dataType)) {
			lblPreview.setText(SAMPLE_NA);
		} else if (FormatSpecifierComposite.DATA_TYPE_NUMBER.equals(dataType)) {

			try {
				lblPreview.setText(ValueFormatter.format(SAMPLE_NUMBER, fs, ULocale.getDefault(), null));
			} catch (ChartException e) {
				e.printStackTrace();
			}
		} else if (FormatSpecifierComposite.DATA_TYPE_DATETIME.equals(dataType)) {
			try {
				lblPreview.setText(ValueFormatter.format(SAMPLE_DATETIME, fs, ULocale.getDefault(), null));
			} catch (ChartException e) {
				e.printStackTrace();
			}
		} else if (FormatSpecifierComposite.DATA_TYPE_STRING.equals(dataType)) {
			try {
				lblPreview.setText(ValueFormatter.format(SAMPLE_STRING, fs, ULocale.getDefault(), null));
			} catch (ChartException e) {
				e.printStackTrace();
			}
		}

		// Re-layout the composite to prevent label truncated
		getParent().layout();
	}

	/**
	 * @param dataType the dataType to set
	 */
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public void setEnabled(boolean enabled) {
		lblPreview.setEnabled(enabled);
		super.setEnabled(enabled);
	}

}
