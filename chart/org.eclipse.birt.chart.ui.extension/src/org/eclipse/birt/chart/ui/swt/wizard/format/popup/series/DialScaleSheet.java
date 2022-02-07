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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.util.DefaultValueProvider;
import org.eclipse.birt.chart.ui.swt.composites.TextEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractScaleSheet;

/**
 * DialScaleSheet
 */

public class DialScaleSheet extends AbstractScaleSheet {

	private DialSeries series;

	public DialScaleSheet(String title, ChartWizardContext context, DialSeries series) {
		super(title, context);
		this.series = series;
	}

	protected Scale getScale() {
		return series.getDial().getScale();
	}

	protected int getValueType() {
		return TextEditorComposite.TYPE_NUMBERIC;
	}

	protected void setState() {
		super.setState();

		if (btnShowOutside != null) {
			// Hide invalid attributes.
			btnShowOutside.setVisible(false);
		}
	}

	@Override
	protected Scale getDefaultVauleScale() {
		return DefaultValueProvider.defDialSeries().getDial().getScale();
	}

}
