/***********************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.chart.ui.swt.wizard.format.popup.series;

import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.ui.swt.composites.NeedleComposite;
import org.eclipse.birt.chart.ui.swt.composites.NeedleComposite.NeedleAttributesContext;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.format.popup.AbstractPopupSheet;
import org.eclipse.birt.chart.ui.util.ChartHelpContextIds;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

public class NeedleSheet extends AbstractPopupSheet {

	private transient SeriesDefinition seriesDefn;
	private Series series;

	/**
	 * @param title
	 * @param context
	 * @param seriesDefn
	 *
	 * @deprecated since 3.7
	 */
	@Deprecated
	public NeedleSheet(String title, ChartWizardContext context, SeriesDefinition seriesDefn) {
		super(title, context, true);
		this.seriesDefn = seriesDefn;
	}

	public NeedleSheet(String title, ChartWizardContext context, Series series) {
		super(title, context, true);
		this.series = series;
	}

	@Override
	protected Composite getComponent(Composite parent) {
		ChartUIUtil.bindHelp(parent, ChartHelpContextIds.POPUP_DIAL_NEEDLES);
		// Sheet content composite
		Composite cmpContent = new Composite(parent, SWT.NONE);
		{
			// Layout for the content composite
			GridLayout glContent = new GridLayout();
			glContent.marginHeight = 7;
			glContent.marginWidth = 7;
			cmpContent.setLayout(glContent);
		}

		DialSeries dialSeries = (series != null) ? (DialSeries) series : (DialSeries) seriesDefn.getDesignTimeSeries();

		new NeedleComposite(cmpContent, getContext(), dialSeries, getNeedleAttributesContext());

		return cmpContent;
	}

	protected NeedleAttributesContext getNeedleAttributesContext() {
		return new NeedleAttributesContext();
	}
}
