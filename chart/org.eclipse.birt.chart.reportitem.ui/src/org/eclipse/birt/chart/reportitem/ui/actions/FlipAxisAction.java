/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.ui.actions;

import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.ui.frameworks.taskwizard.WizardBase;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.jface.action.Action;

/**
 * 
 */

public class FlipAxisAction extends Action {

	private ExtendedItemHandle eih;
	private final ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();

	public FlipAxisAction(ExtendedItemHandle eih) {
		super(Messages.getString("FlipAxisAction.Text.FlipAxis"), //$NON-NLS-1$
				Action.AS_CHECK_BOX);
		this.eih = eih;
		init();
	}

	private void init() {
		Chart cm = ChartCubeUtil.getChartFromHandle(eih);
		if (cm instanceof ChartWithAxes) {
			this.setChecked(((ChartWithAxes) cm).isTransposed());
			this.setEnabled(checkEnabled());
		} else {
			this.setEnabled(false);
		}
	}

	private boolean checkEnabled() {
		try {
			if (ChartCubeUtil.isAxisChart(eih)) {
				// Get plot chart if it's axis chart
				eih = (ExtendedItemHandle) eih.getElementProperty(ChartReportItemConstants.PROPERTY_HOST_CHART);
			}
			if (ChartCubeUtil.isPlotChart(eih)) {
				AggregationCellHandle containerCell = ChartCubeUtil.getXtabContainerCell(eih);
				if (containerCell != null) {
					if (DEUtil.isLinkedElement(containerCell.getCrosstabHandle())) {
						// Not allowed to flip axis if xtab is extended from
						// library
						return false;
					}
					List<String> exprs = ChartCubeUtil.getAllLevelsBindingName(containerCell.getCrosstab());
					// Grand total always supports only one direction
					return exprs.size() == 2 && !ChartCubeUtil.isAggregationCell(containerCell);
				}
			}
		} catch (BirtException e) {
			WizardBase.displayException(e);
		}
		return false;
	}

	@Override
	public void run() {
		try {
			AggregationCellHandle containerCell = ChartCubeUtil.getXtabContainerCell(eih);
			if (containerCell != null) {
				ChartReportItemImpl reportItem = (ChartReportItemImpl) eih.getReportItem();
				ChartWithAxes cmOld = (ChartWithAxes) reportItem.getProperty(ChartReportItemConstants.PROPERTY_CHART);
				ChartWithAxes cmNew = cmOld.copyInstance();
				List<String> exprs = ChartCubeUtil.getAllLevelsBindingName(containerCell.getCrosstab());
				Query query = cmNew.getAxes().get(0).getSeriesDefinitions().get(0).getDesignTimeSeries()
						.getDataDefinition().get(0);
				eih.getRoot().getCommandStack().startTrans(getText());
				exprCodec.setType(UIUtil.getDefaultScriptType());

				if (cmNew.isTransposed()) {
					cmNew.setTransposed(false);
					// To resolve potential wrong axis type issue when flipping
					// axes
					cmNew.getBaseAxes()[0].setType(AxisType.TEXT_LITERAL);
					exprCodec.setBindingName(exprs.get(0), true);
					query.setDefinition(exprCodec.encode());
					ChartCubeUtil.updateXTabForAxis(containerCell, eih, true, cmNew);
				} else {
					cmNew.setTransposed(true);
					// To resolve potential wrong axis type issue when flipping
					// axes
					cmNew.getBaseAxes()[0].setType(AxisType.TEXT_LITERAL);
					exprCodec.setBindingName(exprs.get(1), true);
					query.setDefinition(exprCodec.encode());
					ChartCubeUtil.updateXTabForAxis(containerCell, eih, false, cmNew);
				}
				cmNew.setReverseCategory(cmNew.isTransposed());
				reportItem.executeSetModelCommand(eih, cmOld, cmNew);
			}
			eih.getRoot().getCommandStack().commit();
		} catch (BirtException e) {
			WizardBase.displayException(e);
			eih.getRoot().getCommandStack().rollback();
		}
	}
}
