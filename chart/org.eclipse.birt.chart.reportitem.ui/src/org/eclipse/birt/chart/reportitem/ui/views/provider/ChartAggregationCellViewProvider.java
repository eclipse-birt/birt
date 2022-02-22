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

package org.eclipse.birt.chart.reportitem.ui.views.provider;

import java.util.List;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.SeriesImpl;
import org.eclipse.birt.chart.model.data.BaseSampleData;
import org.eclipse.birt.chart.model.data.DataFactory;
import org.eclipse.birt.chart.model.data.OrthogonalSampleData;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SampleData;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.impl.QueryImpl;
import org.eclipse.birt.chart.model.data.impl.SeriesDefinitionImpl;
import org.eclipse.birt.chart.model.impl.ChartWithAxesImpl;
import org.eclipse.birt.chart.model.type.impl.BarSeriesImpl;
import org.eclipse.birt.chart.reportitem.ChartReportItemImpl;
import org.eclipse.birt.chart.reportitem.api.ChartCubeUtil;
import org.eclipse.birt.chart.reportitem.api.ChartInXTabStatusManager;
import org.eclipse.birt.chart.reportitem.api.ChartItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.birt.chart.reportitem.ui.ChartXTabUIUtil;
import org.eclipse.birt.chart.reportitem.ui.i18n.Messages;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.util.CrosstabUIHelper;
import org.eclipse.birt.report.item.crosstab.ui.extension.AggregationCellViewAdapter;
import org.eclipse.birt.report.item.crosstab.ui.extension.SwitchCellInfo;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;

/**
 * Provider for conversion between chart and text in cross tab
 */
public class ChartAggregationCellViewProvider extends AggregationCellViewAdapter {

	@Override
	public String getViewName() {
		return ChartReportItemConstants.CHART_EXTENSION_NAME;
	}

	@Override
	public String getViewDisplayName() {
		return Messages.getString("ChartAggregationCellViewProvider.Chart.DisplayName"); //$NON-NLS-1$
	}

	@Override
	public boolean matchView(AggregationCellHandle cell) {
		ExtendedItemHandle handle = getChartHandle(cell);
		if (handle != null) {
			// Only return true for plot chart
			return ChartCubeUtil.isPlotChart(handle);
		}
		return false;
	}

	@Override
	public void switchView(SwitchCellInfo info) {
		AggregationCellHandle cell = info.getAggregationCell();
		try {
			ChartWithAxes cm = createDefaultChart(info);

			// Get the measure binding expression and drop the DataItemHandle
			Object content = ChartCubeUtil.getFirstContent(cell);
			if (content instanceof DesignElementHandle) {
				((DesignElementHandle) content).dropAndClear();
			}

			// Create the ExtendedItemHandle with default chart model
			ExtendedItemHandle chartHandle = ChartCubeUtil.createChartHandle(cell.getModelHandle(),
					ChartReportItemConstants.TYPE_PLOT_CHART, null);
			ChartReportItemImpl reportItem = (ChartReportItemImpl) chartHandle.getReportItem();
			reportItem.setModel(cm);
			cell.addContent(chartHandle, 0);

			// Set default bounds for chart model and handle
			Bounds bounds = ChartItemUtil.createDefaultChartBounds(chartHandle, cm);
			cm.getBlock().setBounds(bounds);
			chartHandle.setWidth(bounds.getWidth() + "pt"); //$NON-NLS-1$
			chartHandle.setHeight(bounds.getHeight() + "pt"); //$NON-NLS-1$

			// If adding chart in total cell and there are charts in other
			// total cells, do not update direction.
			if (ChartCubeUtil.isAggregationCell(cell) && !checkChartInAllTotalCells(cell, cm.isTransposed())) {
				// Update xtab direction for multiple measure case
				ChartCubeUtil.updateXTabDirection(cell.getCrosstab(), cm.isTransposed());
			}

			// Set span and add axis cell
			ChartCubeUtil.addAxisChartInXTab(cell, cm, chartHandle, info.isNew());

			ChartInXTabStatusManager.updateGrandItemStatus(cell);

			// In fixed layout, need to set width for other cells
			if (cell.getCrosstab().getModuleHandle() instanceof ReportDesignHandle
					&& DesignChoiceConstants.REPORT_LAYOUT_PREFERENCE_FIXED_LAYOUT.equals(
							((ReportDesignHandle) cell.getCrosstab().getModuleHandle()).getLayoutPreference())) {
				CrosstabUIHelper.validateFixedColumnWidth((ExtendedItemHandle) cell.getCrosstabHandle());
			}
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		}
	}

	@Override
	public void restoreView(AggregationCellHandle cell) {
		try {
			ExtendedItemHandle chartHandle = getChartHandle(cell);
			Chart cm = ChartItemUtil.getChartFromHandle(chartHandle);

			// If it's axis chart, only remove axis in chart model
			if (ChartCubeUtil.isAxisChart(chartHandle)) {
				Axis yAxis = ((ChartWithAxes) cm).getAxes().get(0).getAssociatedAxes().get(0);
				yAxis.getLineAttributes().setVisible(false);
				yAxis.getLabel().setVisible(false);
				yAxis.getMajorGrid().getTickAttributes().setVisible(false);
				return;
			}

			// Set null size back
			CrosstabCellHandle levelCell = ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(),
					ICrosstabConstants.ROW_AXIS_TYPE);
			if (levelCell != null) {
				cell.getCrosstab().setRowHeight(levelCell, null);
			}
			levelCell = ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(), ICrosstabConstants.COLUMN_AXIS_TYPE);
			if (levelCell != null) {
				cell.getCrosstab().setColumnWidth(levelCell, null);
			}

			// Remove axis chart
			ChartCubeUtil.removeAxisChartInXTab(cell, ChartXTabUIUtil.isTransposedChartWithAxes(cm), true);
			// Plot chart will be removed by designer itself
		} catch (BirtException e) {
			ExceptionHandler.handle(e);
		}
	}

	private ChartWithAxes createDefaultChart(SwitchCellInfo info) {
		AggregationCellHandle cell = info.getAggregationCell();

		// Get data type of measure
		boolean bDateTypeMeasure = false;
		if (info.getMeasureInfo() != null) {
			MeasureHandle measure = info.getCrosstab().getCube().getMeasure(info.getMeasureInfo().getMeasureName());
			String dataType = measure.getDataType();
			String function = measure.getFunction();
			bDateTypeMeasure = !(DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equals(function)
					|| DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equals(function))
					&& (DesignChoiceConstants.COLUMN_DATA_TYPE_DATE.equals(dataType)
							|| DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME.equals(dataType));
		}

		ChartWithAxes cm = ChartWithAxesImpl.createDefault();
		cm.setType("Bar Chart");//$NON-NLS-1$
		cm.setSubType("Side-by-side");//$NON-NLS-1$
		cm.setUnits("Points"); //$NON-NLS-1$
		cm.setUnitSpacing(50);
		cm.getLegend().setVisible(false);
		cm.getTitle().setVisible(false);

		String exprMeasure = ChartCubeUtil.generateComputedColumnName(cell,
				ChartReportItemHelper.instance().getMeasureExprIndicator(cell.getCrosstab().getCube()));
		String exprCategory = null;

		// Compute the correct chart direction according to xtab
		if (checkTransposed(cell)) {
			cm.setTransposed(true);
			cm.setReverseCategory(true);

			// Get the row dimension binding name as Category expression
			Object content = ChartCubeUtil.getFirstContent(
					ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(), ICrosstabConstants.ROW_AXIS_TYPE));
			if (content instanceof DataItemHandle) {
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn();
			}
		} else {
			// Get the column dimension binding name as Category expression
			Object content = ChartCubeUtil.getFirstContent(
					ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(), ICrosstabConstants.COLUMN_AXIS_TYPE));
			if (content instanceof DataItemHandle) {
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn();
			}
		}

		// Add base series
		Axis xAxis = cm.getBaseAxes()[0];
		SeriesDefinition sdBase = SeriesDefinitionImpl.createDefault();
		Series series = SeriesImpl.createDefault();
		sdBase.getSeries().add(series);
		xAxis.setCategoryAxis(true);
		xAxis.getSeriesDefinitions().add(sdBase);
		if (exprCategory != null) {
			Query query = QueryImpl.create(ExpressionUtil.createJSDataExpression(exprCategory));
			series.getDataDefinition().add(query);
		}

		// Add orthogonal series
		Axis yAxis = cm.getOrthogonalAxes(xAxis, true)[0];
		SeriesDefinition sdOrth = SeriesDefinitionImpl.createDefault();
		series = BarSeriesImpl.createDefault();
		sdOrth.getSeries().add(series);
		yAxis.getSeriesDefinitions().add(sdOrth);
		if (bDateTypeMeasure) {
			yAxis.setType(AxisType.DATE_TIME_LITERAL);
		}
		if (exprMeasure != null) {
			Query query = QueryImpl.create(ExpressionUtil.createJSDataExpression(exprMeasure));
			series.getDataDefinition().add(query);
		}

		// Add sample data
		SampleData sampleData = DataFactory.eINSTANCE.createSampleData();
		sampleData.getBaseSampleData().clear();
		sampleData.getOrthogonalSampleData().clear();
		// Create Base Sample Data
		BaseSampleData sampleDataBase = DataFactory.eINSTANCE.createBaseSampleData();
		sampleDataBase.setDataSetRepresentation(ChartUtil.getNewSampleData(xAxis.getType(), 0));
		sampleData.getBaseSampleData().add(sampleDataBase);
		// Create Orthogonal Sample Data (with simulation count of 2)
		OrthogonalSampleData sampleDataOrth = DataFactory.eINSTANCE.createOrthogonalSampleData();
		sampleDataOrth.setDataSetRepresentation(ChartUtil.getNewSampleData(yAxis.getType(), 0));
		sampleDataOrth.setSeriesDefinitionIndex(0);
		sampleData.getOrthogonalSampleData().add(sampleDataOrth);
		cm.setSampleData(sampleData);

		return cm;
	}

	private void updateChartQueries(ChartWithAxes cm, AggregationCellHandle cell) {
		// Replace the query expression in chart model
		String exprMeasure = ChartCubeUtil.generateComputedColumnName(cell,
				ChartReportItemHelper.instance().getMeasureExprIndicator(cell.getCrosstab().getCube()));
		String exprCategory = null;

		if (cm.isTransposed()) {
			// Get the row dimension binding name as Category
			// expression
			Object content = ChartCubeUtil.getFirstContent(
					ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(), ICrosstabConstants.ROW_AXIS_TYPE));
			if (content instanceof DataItemHandle) {
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn();
			}
		} else {
			// Get the column dimension binding name as Category
			// expression
			Object content = ChartCubeUtil.getFirstContent(
					ChartCubeUtil.getInnermostLevelCell(cell.getCrosstab(), ICrosstabConstants.COLUMN_AXIS_TYPE));
			if (content instanceof DataItemHandle) {
				DataItemHandle dataItemHandle = (DataItemHandle) content;
				exprCategory = dataItemHandle.getResultSetColumn();
			}
		}

		if (exprCategory != null) {
			SeriesDefinition sdCategory = cm.getBaseAxes()[0].getSeriesDefinitions().get(0);
			Query queryCategory = sdCategory.getDesignTimeSeries().getDataDefinition().get(0);
			queryCategory.setDefinition(ExpressionUtil.createJSDataExpression(exprCategory));
		}

		if (exprMeasure != null) {
			SeriesDefinition sdValue = cm.getOrthogonalAxes(cm.getBaseAxes()[0], true)[0].getSeriesDefinitions().get(0);
			Query queryValue = sdValue.getDesignTimeSeries().getDataDefinition().get(0);
			queryValue.setDefinition(ExpressionUtil.createJSDataExpression(exprMeasure));
		}
	}

	private ExtendedItemHandle getChartHandle(CrosstabCellHandle cell) {
		Object content = ChartCubeUtil.getFirstContent(cell);
		if (ChartCubeUtil.isChartHandle(content)) {
			return (ExtendedItemHandle) content;
		}
		return null;
	}

	private boolean checkTransposed(AggregationCellHandle cell) {
		if (ChartCubeUtil.isDetailCell(cell)) {
			// If no column area, transpose chart.
			if (cell.getAggregationOnColumn() == null) {
				return true;
			}
			// If no row area, chart must be horizontal.
			if (cell.getAggregationOnRow() == null) {
				return false;
			}

			// If column grand/sub total cell already has chart, transpose
			// current chart to keep the same direction
			MeasureViewHandle mv = (MeasureViewHandle) cell.getContainer();
			for (int i = 0; i < mv.getAggregationCount(); i++) {
				AggregationCellHandle otherCell = mv.getAggregationCell(i);
				if (cell.getDimensionView(ICrosstabConstants.ROW_AXIS_TYPE) == otherCell
						.getDimensionView(ICrosstabConstants.ROW_AXIS_TYPE)
						&& cell.getLevelView(ICrosstabConstants.ROW_AXIS_TYPE) == otherCell
								.getLevelView(ICrosstabConstants.ROW_AXIS_TYPE)) {
					Object content = ChartCubeUtil.getFirstContent(otherCell);
					if (ChartCubeUtil.isPlotChart((DesignElementHandle) content)) {
						return true;
					}
				}
			}

			// If chart in measure cell, use the original direction
			Object content = ChartCubeUtil.getFirstContent(cell);
			if (ChartCubeUtil.isPlotChart((DesignElementHandle) content)) {
				return ((ChartWithAxes) ChartCubeUtil.getChartFromHandle((ExtendedItemHandle) content)).isTransposed();
			}
		}
		if (ChartCubeUtil.isAggregationCell(cell)) {
			LevelHandle levelRow = cell.getAggregationOnRow();
			LevelHandle levelColumn = cell.getAggregationOnColumn();
			// If in column grand total, transpose chart
			if (levelRow != null && levelColumn == null) {
				return true;
			}
			// If in row grand total, non-transpose chart
			if (levelRow == null && levelColumn != null) {
				return false;
			}

			// If in sub total
			if (levelRow != null && levelColumn != null) {
				// If column area's subtotal, transpose the chart
				return isInSubtotal(cell, ICrosstabConstants.COLUMN_AXIS_TYPE);
			}
			return false;
		}

		// Use the direction of first chart in multiple measure case
		List<ExtendedItemHandle> chartInOtherMeasure = ChartCubeUtil.findChartInOtherMeasures(cell, true);
		if (!chartInOtherMeasure.isEmpty()) {
			return ((ChartWithAxes) ChartCubeUtil.getChartFromHandle(chartInOtherMeasure.get(0))).isTransposed();
		}

		// Default chart direction is the same with xtab's
		return cell.getCrosstab().getMeasureDirection().equals(ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL);
	}

	private boolean isInSubtotal(AggregationCellHandle cell, int axisType) {
		int levelCount = ChartCubeUtil.getLevelCount(cell.getCrosstab(), axisType);
		if (levelCount > 1) {
			LevelViewHandle currentLevel = cell.getLevelView(axisType);
			for (int i = 0; i < levelCount; i++) {
				LevelViewHandle level = ChartCubeUtil.getLevel(cell.getCrosstab(), axisType, i);
				if (level == currentLevel) {
					// If not last level, it's subtotal
					return i < levelCount - 1;
				}
			}
		}
		return false;
	}

	@Override
	public void updateView(AggregationCellHandle cell, int type) {
		Object contentItem = ChartCubeUtil.getFirstContent(cell);

		if (contentItem instanceof ExtendedItemHandle) {
			ExtendedItemHandle handle = (ExtendedItemHandle) contentItem;

			try {
				if (ChartCubeUtil.isPlotChart(handle)) {
					// Update plot chart
					// Reset query expressions
					ChartReportItemImpl reportItem = (ChartReportItemImpl) handle.getReportItem();
					ChartWithAxes cm = (ChartWithAxes) reportItem.getProperty(ChartReportItemConstants.PROPERTY_CHART);
					ChartWithAxes cmNew;
					if (cm == null) {
						return;
					}

					cmNew = cm.copyInstance();
					if (type == CHANGE_ORIENTATION_TYPE && cell.getAggregationOnColumn() != null
							&& cell.getAggregationOnRow() != null && !ChartCubeUtil.isAggregationCell(cell)) {
						// If event is from xtab direction and xtab has two
						// aggregations and not in total cell, change chart's
						// direction. Otherwise, change xtab's direction
						cmNew.setTransposed(cell.getCrosstab().getMeasureDirection()
								.equals(ICrosstabConstants.MEASURE_DIRECTION_HORIZONTAL));
						cmNew.setReverseCategory(cmNew.isTransposed());
					}
					updateChartQueries(cmNew, cell);

					reportItem.executeSetModelCommand(handle, cm, cmNew);

					// Reset cell span
					if (cmNew.isTransposed()) {

						cell.setSpanOverOnRow(cell.getAggregationOnRow());
						cell.setSpanOverOnColumn(null);
					} else {
						cell.setSpanOverOnColumn(cell.getAggregationOnColumn());
						cell.setSpanOverOnRow(null);
					}

					if (type == CHANGE_ORIENTATION_TYPE) {
						ChartCubeUtil.updateXTabForAxis(cell, handle, cm.isTransposed(), cmNew);
					} else {
						// Replace date item with axis chart
						ChartCubeUtil.updateAxisChart(cell, cmNew, handle);

						// Update xtab direction for multiple measure case
						ChartCubeUtil.updateXTabDirection(cell.getCrosstab(), cmNew.isTransposed());
					}

					ChartInXTabStatusManager.updateGrandItemStatus(cell);
				} else if (ChartCubeUtil.isAxisChart(handle)) {
					// Remove axis chart if host chart does not exist
					ExtendedItemHandle hostChartHandle = (ExtendedItemHandle) handle
							.getElementProperty(ChartReportItemConstants.PROPERTY_HOST_CHART);
					if (hostChartHandle == null) {
						handle.dropAndClear();
						return;
					}

					if (type != CHANGE_ORIENTATION_TYPE) {
						ChartReportItemImpl reportItem = (ChartReportItemImpl) handle.getReportItem();
						ChartWithAxes cm = (ChartWithAxes) reportItem
								.getProperty(ChartReportItemConstants.PROPERTY_CHART);

						// Update xtab direction for multiple measure case
						ChartCubeUtil.updateXTabDirection(cell.getCrosstab(), cm.isTransposed());
					}
				}
			} catch (BirtException e) {
				ExceptionHandler.handle(e);
			}
		}
	}

	@Override
	public boolean canSwitch(SwitchCellInfo info) {
		AggregationCellHandle cell = info.getAggregationCell();
		if (cell != null) {
			// Do not allow switching to Chart for no aggregation case
			if (cell.getAggregationOnRow() == null && cell.getAggregationOnColumn() == null) {
				return false;
			}
		}
		CrosstabReportItemHandle xtab = info.getCrosstab();
		// Do not allow switching to Chart if neither row or column is defined
		if (xtab.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE) == 0
				&& xtab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) == 0) {
			return false;
		}

		if (info.getType() == SwitchCellInfo.GRAND_TOTAL || info.getType() == SwitchCellInfo.SUB_TOTAL) {
			// Do not allow switching to Chart for no dimension case in total
			// cell
			// If axis chart in total cell, don't allow to switch it to Chart
			// view.
			if (xtab.getDimensionCount(ICrosstabConstants.ROW_AXIS_TYPE) == 0
					|| xtab.getDimensionCount(ICrosstabConstants.COLUMN_AXIS_TYPE) == 0 || (ChartCubeUtil.findAxisChartInCell(cell) != null)) {
				return false;
			}
		}

		// Not allow to switch string measure to chart
		if (info.getCrosstab().getCube() != null && info.getMeasureInfo() != null) {
			if (info.getMeasureInfo().getMeasureName() == null) {
				return false;
			}

			MeasureHandle measureHandle = info.getCrosstab().getCube()
					.getMeasure(info.getMeasureInfo().getMeasureName());

			if (measureHandle == null) {
				return false;
			}

			String dataType = measureHandle.getDataType();
			return !DesignChoiceConstants.COLUMN_DATA_TYPE_STRING.equals(dataType);
		}

		return true;
	}

	private boolean checkChartInAllTotalCells(AggregationCellHandle cell, boolean bTransposed) {
		MeasureViewHandle mv = (MeasureViewHandle) cell.getContainer();
		int count = mv.getAggregationCount();
		if (count <= 1) {
			return false;
		}
		for (int i = 0; i < count; i++) {
			AggregationCellHandle totalCell = mv.getAggregationCell(i);
			if (totalCell != null) {
				Object content = ChartCubeUtil.getFirstContent(totalCell);
				if (ChartCubeUtil.isChartHandle(content)) {
					return true;
				}
			}
		}
		return false;
	}
}
