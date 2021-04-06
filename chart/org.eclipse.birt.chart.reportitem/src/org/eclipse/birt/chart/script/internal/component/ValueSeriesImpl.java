/*******************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.script.internal.component;

import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.ActionType;
import org.eclipse.birt.chart.model.attribute.URLValue;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.data.Trigger;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.BarSeries;
import org.eclipse.birt.chart.model.type.BubbleSeries;
import org.eclipse.birt.chart.model.type.DialSeries;
import org.eclipse.birt.chart.model.type.DifferenceSeries;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.LineSeries;
import org.eclipse.birt.chart.model.type.PieSeries;
import org.eclipse.birt.chart.model.type.ScatterSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.script.api.component.IValueSeries;
import org.eclipse.birt.chart.script.internal.series.AreaImpl;
import org.eclipse.birt.chart.script.internal.series.BarImpl;
import org.eclipse.birt.chart.script.internal.series.BubbleImpl;
import org.eclipse.birt.chart.script.internal.series.DifferenceImpl;
import org.eclipse.birt.chart.script.internal.series.GanttImpl;
import org.eclipse.birt.chart.script.internal.series.LineImpl;
import org.eclipse.birt.chart.script.internal.series.MeterImpl;
import org.eclipse.birt.chart.script.internal.series.PieImpl;
import org.eclipse.birt.chart.script.internal.series.ScatterImpl;
import org.eclipse.birt.chart.script.internal.series.StockImpl;
import org.eclipse.birt.report.model.api.ActionHandle;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.simpleapi.IAction;
import org.eclipse.emf.common.util.EList;

/**
 * 
 */

public abstract class ValueSeriesImpl extends SeriesImpl implements IValueSeries {

	protected ValueSeriesImpl(SeriesDefinition sd, Chart cm) {
		super(sd, cm);
	}

	public String getTitle() {
		return (String) series.getSeriesIdentifier();
	}

	public boolean isVisible() {
		return series.isVisible();
	}

	public void setTitle(String title) {
		series.setSeriesIdentifier(title);
	}

	public void setVisible(boolean visible) {
		series.setVisible(visible);
	}

	public IAction getAction() {
		EList<Trigger> triggers = series.getTriggers();
		Action chartAction = null;
		for (int i = 0; i < triggers.size(); i++) {
			chartAction = triggers.get(i).getAction();
			if (ActionType.URL_REDIRECT_LITERAL.equals(chartAction.getType())) {
				URLValue uv = (URLValue) chartAction.getValue();
				String sa = uv.getBaseUrl();
				final ActionHandle action;
				try {
					action = ModuleUtil.deserializeAction(sa);
				} catch (DesignFileException e) {
					Logger.getLogger("org.eclipse.birt.chart.reportitem/trace").log(e); //$NON-NLS-1$
					return null;
				}
				return new IAction() {

					public String getFormatType() {
						return action.getFormatType();
					}

					public String getLinkType() {
						return action.getLinkType();
					}

					public String getReportName() {
						return action.getReportName();
					}

					public String getTargetBookmark() {
						return action.getTargetBookmark();
					}

					public String getTargetWindow() {
						return action.getTargetWindow();
					}

					public String getURI() {
						return action.getURI();
					}

					public void setFormatType(String type) throws SemanticException {
						action.setFormatType(type);
					}

					public void setLinkType(String type) throws SemanticException {
						action.setLinkType(type);
					}

					public void setReportName(String reportName) throws SemanticException {
						action.setReportName(reportName);
					}

					public void setTargetBookmark(String bookmark) throws SemanticException {
						action.setTargetBookmark(bookmark);
					}

					public void setTargetWindow(String window) throws SemanticException {
						action.setTargetWindow(window);
					}

					public void setURI(String uri) throws SemanticException {
						action.setURI(uri);
					}

					public IStructure getStructure() {
						return action.getStructure();
					}

				};
			}
		}
		return null;
	}

	protected SeriesGrouping getBaseGrouping() {
		SeriesGrouping grouping = null;
		if (cm instanceof ChartWithAxes) {
			Axis bAxis = ((ChartWithAxes) cm).getAxes().get(0);
			SeriesDefinition bSd = bAxis.getSeriesDefinitions().get(0);
			grouping = bSd.getGrouping();
		}
		if (cm instanceof ChartWithoutAxes) {
			SeriesDefinition bSd = ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0);
			grouping = bSd.getGrouping();
		}
		return grouping;
	}

	public String getAggregateExpr() {
		String expr = ""; //$NON-NLS-1$
		SeriesGrouping baseGrouping = getBaseGrouping();
		if (baseGrouping.isEnabled()) {
			if (sd.getGrouping().isEnabled()) {
				expr = sd.getGrouping().getAggregateExpression();
			}
			if (expr.trim().length() == 0) {
				return baseGrouping.getAggregateExpression();
			}
		}
		return expr;
	}

	public void setAggregateExpr(String aggregateExpr) {
		SeriesGrouping baseGrouping = getBaseGrouping();
		if (aggregateExpr == null) {
			sd.getGrouping().setEnabled(false);
			sd.getGrouping().setAggregateExpression(""); //$NON-NLS-1$
			baseGrouping.setEnabled(false);
		} else {
			sd.getGrouping().setEnabled(true);
			sd.getGrouping().setAggregateExpression(aggregateExpr);
			baseGrouping.setEnabled(true);
		}
	}

	protected Axis getAxis() {
		if (cm instanceof ChartWithAxes && sd.eContainer() instanceof Axis) {
			return (Axis) sd.eContainer();
		}
		return null;
	}

	public boolean isPercent() {
		Axis axis = getAxis();
		if (axis != null) {
			return axis.isPercent();
		}
		return false;
	}

	public void setPercent(boolean percent) {
		Axis axis = getAxis();
		if (axis != null) {
			axis.setPercent(percent);
		}
	}

	public static IValueSeries createValueSeries(SeriesDefinition sd, Chart cm) {
		Series series = sd.getDesignTimeSeries();
		if (series instanceof BarSeries) {
			return new BarImpl(sd, cm);
		}

		// Note the order since LineSeries is extended by other Series.
		if (series instanceof DifferenceSeries) {
			return new DifferenceImpl(sd, cm);
		}
		if (series instanceof BubbleSeries) {
			return new BubbleImpl(sd, cm);
		}
		if (series instanceof AreaSeries) {
			return new AreaImpl(sd, cm);
		}
		if (series instanceof ScatterSeries) {
			return new ScatterImpl(sd, cm);
		}
		if (series instanceof LineSeries) {
			return new LineImpl(sd, cm);
		}

		if (series instanceof StockSeries) {
			return new StockImpl(sd, cm);
		}
		if (series instanceof PieSeries) {
			return new PieImpl(sd, cm);
		}
		if (series instanceof DialSeries) {
			return new MeterImpl(sd, cm);
		}
		if (series instanceof GanttSeries) {
			return new GanttImpl(sd, cm);
		}

		return null;
	}

}
