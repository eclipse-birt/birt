/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.reportitem;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.IDataRowExpressionEvaluator;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.extension.IBaseResultSet;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.extension.IReportItem;

/**
 * Presentation implementation for Chart Axis in Cross tab
 */
public final class ChartReportItemPresentationAxisImpl extends ChartReportItemPresentationBase {

	private static IDataRowExpressionEvaluator DUMMY_AXIS_CHART_EVALUATOR = new IDataRowExpressionEvaluator() {

		public void close() {

		}

		public Object evaluate(String expression) {
			// Always return null since shared scale will be used to render axis
			// chart
			return null;
		}

		@SuppressWarnings("deprecation")
		public Object evaluateGlobal(String expression) {
			return null;
		}

		public boolean first() {
			// Only one row of null data
			return true;
		}

		public boolean next() {
			return false;
		}
	};

	@Override
	public void setModelObject(ExtendedItemHandle eih) {
		// Get the host chart handle from host chart
		modelHandle = (ExtendedItemHandle) eih.getElementProperty(PROPERTY_HOST_CHART);
		IReportItem item = getReportItem(modelHandle);
		if (item == null) {
			return;
		}
		cm = (Chart) ((ChartReportItemImpl) item).getProperty(PROPERTY_CHART);
		// Add lock to avoid concurrent exception from EMF. IReportItem has one
		// design time chart model that could be shared by multiple
		// presentation instance, but only allows one copy per item
		// concurrently.
		synchronized (item) {
			// Must copy model here to generate runtime data later
			if (cm != null) {
				cm = cm.copyInstance();
			}
		}
		setChartModelObject(item);
	}

	protected Bounds computeBounds() throws ChartException {
		final Bounds originalBounds = cm.getBlock().getBounds();

		// we must copy the bounds to avoid that setting it on one object
		// unsets it on its precedent container

		Bounds bounds = originalBounds.copyInstance();
		if (cm instanceof ChartWithAxes) {
			try {
				// Set the dynamic size with zero, which will be replaced by the
				// real value after computation when building chart
				ChartWithAxes chart = (ChartWithAxes) cm;
				AggregationCellHandle xtabCell = ChartCubeUtil.getXtabContainerCell(modelHandle);
				if (chart.isTransposed()) {
					bounds.setHeight(0);

					// If user specifies column cell width manually, set the
					// width to chart model
					double dWidth = ChartReportItemPresentationPlotImpl.getColumnCellWidth(xtabCell.getCrosstab(),
							renderDpi);
					if (!ChartUtil.mathEqual(dWidth, 0)
							&& !ChartUtil.mathEqual(dWidth, ChartCubeUtil.DEFAULT_COLUMN_WIDTH.getMeasure())) {
						bounds.setWidth(dWidth);
					} else if (!bounds.isSetWidth() || ChartUtil.mathEqual(bounds.getWidth(), 0)) {
						bounds.setWidth(ChartCubeUtil.DEFAULT_COLUMN_WIDTH.getMeasure());
					}
				} else {
					bounds.setWidth(0);

					// If user specifies row cell height manually, set the
					// height to chart model
					double dHeight = ChartReportItemPresentationPlotImpl.getRowCellHeight(xtabCell.getCrosstab(),
							renderDpi);
					if (!ChartUtil.mathEqual(dHeight, 0)
							&& !ChartUtil.mathEqual(dHeight, ChartCubeUtil.DEFAULT_ROW_HEIGHT.getMeasure())) {
						bounds.setHeight(dHeight);
					} else if (!bounds.isSetHeight() || ChartUtil.mathEqual(bounds.getHeight(), 0)) {
						bounds.setHeight(ChartCubeUtil.DEFAULT_ROW_HEIGHT.getMeasure());
					}
				}
			} catch (BirtException e) {
				throw new ChartException(ChartReportItemConstants.ID, ChartException.GENERATION, e);
			}
		}
		return bounds;
	}

	protected void updateChartModel() {
		super.updateChartModel();

		// Update runtime model to render axis only
		ChartCubeUtil.updateModelToRenderAxis(cm, rtc.isRightToLeft());
	}

	@Override
	protected IDataRowExpressionEvaluator createEvaluator(IBaseResultSet set) throws ChartException {
		// Return a dummy data set since axis chart can render without data
		return DUMMY_AXIS_CHART_EVALUATOR;
	}
}
