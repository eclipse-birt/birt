/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class BubbleDataDefinitionComponent extends DefaultSelectDataComponent {

	public static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.BubbleSeriesImpl"; //$NON-NLS-1$

	private transient ISelectDataComponent[] dataComArray;

	private transient Composite cmpSeries = null;

	private transient SeriesDefinition seriesDefn = null;

	private transient String sTitle = null;

	private transient ChartWizardContext context = null;

	public BubbleDataDefinitionComponent(SeriesDefinition seriesDefn, ChartWizardContext context, String sTitle) {
		super();
		this.seriesDefn = seriesDefn;
		this.context = context;
		this.sTitle = sTitle;
		init();
	}

	private void init() {
		dataComArray = new ISelectDataComponent[2];

		// Value
		dataComArray[0] = new BaseDataDefinitionComponent(BaseDataDefinitionComponent.BUTTON_AGGREGATION,
				ChartUIConstants.QUERY_VALUE, seriesDefn, ChartUIUtil.getDataQuery(seriesDefn, 0), context, sTitle);
		// Size
		dataComArray[1] = new BaseDataDefinitionComponent(BaseDataDefinitionComponent.BUTTON_AGGREGATION,
				ChartUIConstants.QUERY_VALUE, seriesDefn, ChartUIUtil.getDataQuery(seriesDefn, 1), context, sTitle);
	}

	@Override
	public Composite createArea(Composite parent) {
		cmpSeries = new Composite(parent, SWT.NONE);
		{
			GridData gridData = new GridData(GridData.FILL_BOTH);
			cmpSeries.setLayoutData(gridData);

			GridLayout gridLayout = new GridLayout(2, false);
			gridLayout.marginWidth = 0;
			gridLayout.marginHeight = 0;
			cmpSeries.setLayout(gridLayout);
		}

		for (int i = 0; i < dataComArray.length; i++) {
			Label labelArray = new Label(cmpSeries, SWT.NONE);
			labelArray.setText(ChartUIUtil.getBubbleTitle(i) + "*"); //$NON-NLS-1$
			Composite cmpData = dataComArray[i].createArea(cmpSeries);
			cmpData.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
			dataComArray[i].bindAssociatedName(ChartUIUtil.getBubbleTitle(i));
		}
		return cmpSeries;
	}

	@Override
	public void selectArea(boolean selected, Object data) {
		if (data instanceof Integer) {
			int queryIndex = ((Integer) data).intValue();
			dataComArray[queryIndex].selectArea(selected, data);
		} else if (data instanceof Object[]) {
			Object[] array = (Object[]) data;
			SeriesDefinition seriesdefinition = (SeriesDefinition) array[0];
			for (int i = 0; i < dataComArray.length; i++) {
				dataComArray[i].selectArea(selected,
						new Object[] { seriesdefinition, ChartUIUtil.getDataQuery(seriesdefinition, i) });
			}
		} else {
			for (int i = 0; i < dataComArray.length; i++) {
				dataComArray[i].selectArea(selected, null);
			}
		}
	}

	@Override
	public void dispose() {
		for (int i = 0; i < dataComArray.length; i++) {
			dataComArray[i].dispose();
		}
		super.dispose();
	}

}
