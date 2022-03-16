/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

package org.eclipse.birt.chart.examples.builder;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.ui.swt.ColumnBindingInfo;
import org.eclipse.birt.chart.ui.swt.CustomPreviewTable;
import org.eclipse.birt.chart.ui.swt.DefaultChartDataSheet;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartAdapter;
import org.eclipse.birt.chart.ui.swt.wizard.data.SelectDataDynamicArea;
import org.eclipse.birt.chart.ui.util.ChartUIUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.ui.frameworks.taskwizard.interfaces.ITask;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;

/**
 *
 */

public class SampleStandardDataSheet extends DefaultChartDataSheet {

	private CustomPreviewTable tablePreview = null;

	@Override
	public Composite createDataDragSource(Composite parent) {
		Composite composite = ChartUIUtil.createCompositeWrapper(parent);
		{
			composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		}
		Label label = new Label(composite, SWT.NONE);
		{
			label.setText("Data Preview");
			label.setFont(JFaceResources.getBannerFont());
		}
		Label description = new Label(composite, SWT.WRAP);
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			description.setLayoutData(gd);
			description.setText("Sample Data");
		}

		tablePreview = new CustomPreviewTable(composite,
				SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION) {

			@Override
			protected void addDragListenerToHeaderButton(Button button) {
				// do nothing
			}
		};
		{
			GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.widthHint = 400;
			gridData.heightHint = 160;
			tablePreview.setLayoutData(gridData);
			tablePreview.setHeaderAlignment(SWT.LEFT);

			refreshSampleDataPreiview();
		}
		return composite;
	}

	public void refreshSampleDataPreiview() {
		ChartAdapter.beginIgnoreNotifications();
		context.getModel().createSampleRuntimeSeries();
		List<SeriesDefinition> valueSd = ChartUtil.getAllOrthogonalSeriesDefinitions(context.getModel());
		List<ColumnBindingInfo> lcb = new ArrayList<>();
		lcb.add(new ColumnBindingInfo("Category", null, "Category", null));
		for (SeriesDefinition sd : valueSd) {
			int index = valueSd.indexOf(sd) + 1;
			lcb.add(new ColumnBindingInfo("Value " + index, null, "Value " + index, null));
		}
		tablePreview.setColumns(lcb.toArray(new ColumnBindingInfo[0]));
		Object values = ChartUtil.getBaseSeriesDefinitions(context.getModel()).get(0).getRunTimeSeries().get(0)
				.getDataSet().getValues();
		List<List<?>> allValues = new ArrayList<>();
		if (values instanceof List<?>) {
			allValues.add((List<?>) values);
		}
		for (SeriesDefinition sd : valueSd) {
			values = sd.getRunTimeSeries().get(0).getDataSet().getValues();
			if (values instanceof List<?>) {
				allValues.add((List<?>) values);
			}
		}
		for (int i = 0; i < allValues.size(); i++) {
			for (Object o : allValues.get(i)) {
				if (o instanceof Calendar) {

					tablePreview.addEntry(DateFormat.getInstance().format(o), i);
				} else {
					tablePreview.addEntry(o.toString(), i);
				}

			}
		}
		tablePreview.layout();
		ChartAdapter.endIgnoreNotifications();
	}

	@Override
	public ISelectDataCustomizeUI createCustomizeUI(ITask task) {
		return new SelectDataDynamicArea(task);
	}
}
