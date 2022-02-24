/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.chart;

import org.eclipse.birt.chart.ui.swt.composites.ExtendedPropertyEditorComposite;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

/**
 * CustomPropertiesSheet
 */

public class CustomPropertiesSheet extends AbstractPopupSheet {

	public CustomPropertiesSheet(String title, ChartWizardContext context) {
		super(title, context, false);
	}

	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_CHART_CUSTOM);

		Composite cmpContent = new Composite(parent, SWT.NONE);
		{
			GridLayout glContent = new GridLayout();
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout(glContent);
		}

		Composite cmpExtenedComposite = new ExtendedPropertyEditorComposite(cmpContent, SWT.NONE,
				getChart().getExtendedProperties(), getContext());
		{
			GridData gd = new GridData();
			gd.heightHint = 300;
			cmpExtenedComposite.setLayoutData(gd);
		}

		return cmpContent;
	}
}
