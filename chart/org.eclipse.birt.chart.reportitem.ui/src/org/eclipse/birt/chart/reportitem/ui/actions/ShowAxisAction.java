/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui.actions;

import java.util.Iterator;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class ShowAxisAction extends Action {

	private ExtendedItemHandle eih;

	public ShowAxisAction(ExtendedItemHandle eih) {
		super(Messages.getString("ShowAxisAction.Text.ShowValueAxis"), //$NON-NLS-1$
				Action.AS_CHECK_BOX);
		this.eih = eih;
		init();
	}

	private void init() {
		Chart cm = ChartCubeUtil.getChartFromHandle(eih);
		if (cm instanceof ChartWithAxes) {
			this.setChecked(hasAxisChart());
			try {
				// Not allowed to show/hide axis if xtab is extended from
				// library
				AggregationCellHandle containerCell = ChartCubeUtil.getXtabContainerCell(eih);
				if (containerCell != null) {
					if (DEUtil.isLinkedElement(containerCell.getCrosstabHandle())) {
						this.setEnabled(false);
					}
				}
			} catch (BirtException e) {
				WizardBase.displayException(e);
			}
		} else {
			this.setEnabled(false);
		}
	}

	@SuppressWarnings("unchecked")
	private boolean hasAxisChart() {
		// Check if axis chart is existent
		if (ChartCubeUtil.isPlotChart(eih)) {
			for (Iterator<DesignElementHandle> iterator = eih.clientsIterator(); iterator.hasNext();) {
				DesignElementHandle client = iterator.next();
				if (ChartCubeUtil.isAxisChart(client)) {
					return true;
				}
			}
			return false;
		}
		if (ChartCubeUtil.isAxisChart(eih)) {
			return true;
		}
		return false;
	}

	@Override
	public void run() {
		ModuleHandle mh = eih.getRoot();
		try {
			mh.getCommandStack().startTrans(getText());

			// Update chart model for axis visibility
			ExtendedItemHandle plotChart = eih;
			if (ChartCubeUtil.isAxisChart(eih)) {
				plotChart = (ExtendedItemHandle) eih.getElementProperty(ChartReportItemConstants.PROPERTY_HOST_CHART);
			}
			ChartReportItemImpl reportItem = (ChartReportItemImpl) plotChart.getReportItem();
			ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty(ChartReportItemConstants.PROPERTY_CHART);
			ChartWithAxes cmNew = cmOld.copyInstance();
			Axis yAxis = cmNew.getAxes().get(0).getAssociatedAxes().get(0);
			if (yAxis != null) {
				yAxis.getLineAttributes().setVisible(isChecked());
				yAxis.getLabel().setVisible(isChecked());
				yAxis.getMajorGrid().getTickAttributes().setVisible(isChecked());
				reportItem.executeSetModelCommand(plotChart, cmOld, cmNew);
			}

			// Update axis chart in xtab
			AggregationCellHandle containerCell = ChartCubeUtil.getXtabContainerCell(eih);
			if (containerCell != null) {
				if (isChecked()) {
					// Add axis chart
					ChartXTabUIUtil.addAxisChartInXTab(containerCell, cmNew, eih);
				} else {
					// Delete axis chart
					ChartXTabUIUtil.removeAxisChartInXTab(containerCell,
							ChartXTabUIUtil.isTransposedChartWithAxes(cmNew), false);
				}
			}

			mh.getCommandStack().commit();
		} catch (BirtException e) {
			WizardBase.displayException(e);
			mh.getCommandStack().rollback();
		}
	}
}
