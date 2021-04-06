/*******************************************************************************
 * Copyright (c) 2007, 2008, 2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.reportitem.api;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.birt.chart.aggregate.IAggregateFunction;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.GroupingUnitType;
import org.eclipse.birt.chart.model.attribute.SortOption;
import org.eclipse.birt.chart.model.attribute.impl.BoundsImpl;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.i18n.Messages;
import org.eclipse.birt.chart.util.ChartExpressionUtil;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.chart.util.SecurityUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.aggregation.api.IBuildInAggregation;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.engine.api.impl.ReportEngineHelper;
import org.eclipse.birt.report.engine.api.impl.ReportRunnable;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DimensionHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.FilterConditionElementHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ListGroupHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ListingHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ModuleUtil;
import org.eclipse.birt.report.model.api.MultiViewsHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.util.DimensionUtil;
import org.eclipse.birt.report.model.elements.interfaces.IGroupElementModel;
import org.eclipse.emf.common.util.EList;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.text.NumberFormat;
import com.ibm.icu.util.ULocale;

/**
 * Utility class for Chart integration as report item
 */

/**
 * ChartItemUtil
 */
public class ChartItemUtil extends ChartExpressionUtil implements ChartReportItemConstants {

	protected static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.reportitem/trace"); //$NON-NLS-1$

	public static final String BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT = "BIRT_CHART_CONVERT_TO_IMAGE_TIME_OUT"; //$NON-NLS-1$

	private final static String DATA_BASE64 = "data:;base64,"; //$NON-NLS-1$

	/**
	 * Returns the element handle which can save binding columns the given element
	 * 
	 * @param handle the handle of the element which needs binding columns
	 * @return the holder for the element,or itself if no holder available
	 */
	public static ReportItemHandle getBindingHolder(DesignElementHandle handle) {
		if (handle instanceof ReportElementHandle) {

			if (handle instanceof ReportItemHandle) {
				if (((ReportItemHandle) handle).getDataBindingReference() != null
						|| ((ReportItemHandle) handle).getCube() != null
						|| ((ReportItemHandle) handle).getDataSet() != null) {
					return (ReportItemHandle) handle;
				}
			}

			return getBindingHolder(handle.getContainer());
		} else if (handle instanceof MultiViewsHandle) {
			return getBindingHolder(handle.getContainer());
		}
		return null;
	}

	/**
	 * Returns the binding data set if the element or its container has data set
	 * binding or the reference to the data set
	 * 
	 * @param element element handle
	 * @return the binding data set or null
	 * @since 2.5.2
	 */
	public static DataSetHandle getBindingDataSet(DesignElementHandle element) {
		if (element == null) {
			return null;
		}
		if (element instanceof ReportItemHandle) {
			DataSetHandle dataSet = ((ReportItemHandle) element).getDataSet();
			if (((ReportItemHandle) element)
					.getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_REPORT_ITEM_REF) {
				return getBindingDataSet(((ReportItemHandle) element).getDataBindingReference());
			} else if (dataSet != null) {
				return dataSet;
			} else if (((ReportItemHandle) element).getDataBindingType() == ReportItemHandle.DATABINDING_TYPE_DATA) {
				return null;
			}
		}
		if (element.getContainer() != null) {
			return getBindingDataSet(element.getContainer());
		}
		return null;
	}

	/**
	 * @return Returns if current eclipse environment is RtL.
	 */
	public static boolean isRtl() {
		// get -dir rtl option
		boolean rtl = false;
		String eclipseCommands = SecurityUtil.getSysProp("eclipse.commands"); //$NON-NLS-1$
		if (eclipseCommands != null) {
			String[] options = eclipseCommands.split("-"); //$NON-NLS-1$
			String regex = "[\\s]*[dD][iI][rR][\\s]*[rR][tT][lL][\\s]*"; //$NON-NLS-1$
			Pattern pattern = Pattern.compile(regex);
			for (int i = 0; i < options.length; i++) {
				String option = options[i];
				if (pattern.matcher(option).matches()) {
					rtl = true;
					break;
				}
			}
		}
		return rtl;
	}

	/**
	 * Gets all column bindings from handle and its container
	 * 
	 * @param itemHandle handle
	 * @return Iterator of all bindings
	 */
	public static Iterator<ComputedColumnHandle> getColumnDataBindings(ReportItemHandle itemHandle) {
		return getColumnDataBindings(itemHandle, false);
	}

	/**
	 * Gets all column bindings from handle and its container.
	 * 
	 * @param itemHandle
	 * @param unique     <code>true</code> will ignore the binding of container if
	 *                   it is duplicate between handle and its container.
	 * @return ComputedColumnHandle iterator
	 * @since 2.3.2
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<ComputedColumnHandle> getColumnDataBindings(ReportItemHandle itemHandle, boolean unique) {
		if (itemHandle.getDataSet() != null || itemHandle.getCube() != null) {
			return itemHandle.columnBindingsIterator();
		} else if (itemHandle.getContainer() instanceof MultiViewsHandle) {
			return itemHandle.columnBindingsIterator();
		}

		ReportItemHandle handle = getBindingHolder(itemHandle);
		if (handle == null) {
			return null;
		}

		Map<String, ComputedColumnHandle> bindingMap = new LinkedHashMap<String, ComputedColumnHandle>();
		ArrayList<ComputedColumnHandle> list = new ArrayList<ComputedColumnHandle>();
		Iterator<ComputedColumnHandle> i = handle.columnBindingsIterator();
		while (i.hasNext()) {
			ComputedColumnHandle cch = i.next();
			list.add(cch);
			bindingMap.put(cch.getName(), cch);
		}
		if (handle != itemHandle) {
			// Do not add same handle twice
			i = itemHandle.columnBindingsIterator();
			while (i.hasNext()) {
				ComputedColumnHandle cch = i.next();
				list.add(cch);
				bindingMap.put(cch.getName(), cch);
			}
		}
		if (unique) {
			return bindingMap.values().iterator();
		}
		return list.iterator();

	}

	/**
	 * Convert group unit type from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static int convertToDtEGroupUnit(DataType dataType, GroupingUnitType groupUnitType, double intervalRange) {
		if (dataType == DataType.NUMERIC_LITERAL) {
			if (intervalRange == 0) {
				return IGroupDefinition.NO_INTERVAL;
			}

			return IGroupDefinition.NUMERIC_INTERVAL;
		} else if (dataType == DataType.DATE_TIME_LITERAL) {
			switch (groupUnitType.getValue()) {
			case GroupingUnitType.SECONDS:
				return IGroupDefinition.SECOND_INTERVAL;

			case GroupingUnitType.MINUTES:
				return IGroupDefinition.MINUTE_INTERVAL;

			case GroupingUnitType.HOURS:
				return IGroupDefinition.HOUR_INTERVAL;

			case GroupingUnitType.DAYS:
				return IGroupDefinition.DAY_INTERVAL;

			case GroupingUnitType.WEEKS:
				return IGroupDefinition.WEEK_INTERVAL;

			case GroupingUnitType.MONTHS:
				return IGroupDefinition.MONTH_INTERVAL;

			case GroupingUnitType.QUARTERS:
				return IGroupDefinition.QUARTER_INTERVAL;

			case GroupingUnitType.YEARS:
				return IGroupDefinition.YEAR_INTERVAL;
			}
		} else if (dataType == DataType.TEXT_LITERAL) {
			switch (groupUnitType.getValue()) {
			case GroupingUnitType.STRING_PREFIX:
				return IGroupDefinition.STRING_PREFIX_INTERVAL;
			}

			return IGroupDefinition.NO_INTERVAL;
		}

		return IGroupDefinition.NO_INTERVAL;
	}

	/**
	 * Converts the group unit from DtE's to Chart's.
	 * 
	 * @param groupInterval
	 * @return unit defined in {@link GroupingUnitType} or -1 if no interval
	 * @since 2.6
	 */
	public static int convertToChartGroupUnit(int groupInterval) {
		switch (groupInterval) {
		case IGroupDefinition.NO_INTERVAL:
		case IGroupDefinition.NUMERIC_INTERVAL:
			return -1;
		case IGroupDefinition.SECOND_INTERVAL:
			return GroupingUnitType.SECONDS;
		case IGroupDefinition.MINUTE_INTERVAL:
			return GroupingUnitType.MINUTES;
		case IGroupDefinition.HOUR_INTERVAL:
			return GroupingUnitType.HOURS;
		case IGroupDefinition.DAY_INTERVAL:
			return GroupingUnitType.DAYS;
		case IGroupDefinition.WEEK_INTERVAL:
			return GroupingUnitType.WEEKS;
		case IGroupDefinition.MONTH_INTERVAL:
			return GroupingUnitType.MONTHS;
		case IGroupDefinition.QUARTER_INTERVAL:
			return GroupingUnitType.QUARTERS;
		case IGroupDefinition.YEAR_INTERVAL:
			return GroupingUnitType.YEARS;
		case IGroupDefinition.STRING_PREFIX_INTERVAL:
			return GroupingUnitType.STRING_PREFIX;
		}
		return -1;
	}

	/**
	 * Convert interval range from Chart's to DtE's.
	 * 
	 * @param dataType
	 * @param groupUnitType
	 * @param intervalRange
	 * @since BIRT 2.3
	 */
	public static double convertToDtEIntervalRange(DataType dataType, GroupingUnitType groupUnitType,
			double intervalRange) {
		double range = intervalRange;
		if (Double.isNaN(intervalRange)) {
			range = 0;
		}

		if (dataType == DataType.DATE_TIME_LITERAL && range <= 0) {
			range = 1;
		} else if (dataType == DataType.TEXT_LITERAL) {
			return (long) range;
		}

		return range;
	}

	/**
	 * Convert sort direction from Chart's to DtE's.
	 * 
	 * @param sortOption
	 * @since BIRT 2.3
	 */
	public static int convertToDtESortDirection(SortOption sortOption) {
		if (sortOption == SortOption.ASCENDING_LITERAL) {
			return IGroupDefinition.SORT_ASC;
		} else if (sortOption == SortOption.DESCENDING_LITERAL) {
			return IGroupDefinition.SORT_DESC;
		}
		return IGroupDefinition.NO_SORT;
	}

	/**
	 * Convert aggregation name from Chart's to DtE's.
	 * 
	 * @param agg
	 * @since BIRT 2.3
	 */
	public static String convertToDtEAggFunction(String agg) {
		if (PluginSettings.DefaultAggregations.SUM.equals(agg)) {
			return IBuildInAggregation.TOTAL_SUM_FUNC;

		} else if (PluginSettings.DefaultAggregations.AVERAGE.equals(agg)) {
			return IBuildInAggregation.TOTAL_AVE_FUNC;

		} else if (PluginSettings.DefaultAggregations.COUNT.equals(agg)) {
			return IBuildInAggregation.TOTAL_COUNT_FUNC;

		} else if (PluginSettings.DefaultAggregations.DISTINCT_COUNT.equals(agg)) {
			return IBuildInAggregation.TOTAL_COUNTDISTINCT_FUNC;

		} else if (PluginSettings.DefaultAggregations.FIRST.equals(agg)) {
			return IBuildInAggregation.TOTAL_FIRST_FUNC;

		} else if (PluginSettings.DefaultAggregations.LAST.equals(agg)) {
			return IBuildInAggregation.TOTAL_LAST_FUNC;

		} else if (PluginSettings.DefaultAggregations.MIN.equals(agg)) {
			return IBuildInAggregation.TOTAL_MIN_FUNC;

		} else if (PluginSettings.DefaultAggregations.MAX.equals(agg)) {
			return IBuildInAggregation.TOTAL_MAX_FUNC;
		} else if (PluginSettings.DefaultAggregations.WEIGHTED_AVERAGE.equals(agg)) {
			return IBuildInAggregation.TOTAL_WEIGHTEDAVE_FUNC;
		} else if (PluginSettings.DefaultAggregations.MEDIAN.equals(agg)) {
			return IBuildInAggregation.TOTAL_MEDIAN_FUNC;
		} else if (PluginSettings.DefaultAggregations.MODE.equals(agg)) {
			return IBuildInAggregation.TOTAL_MODE_FUNC;
		} else if (PluginSettings.DefaultAggregations.STDDEV.equals(agg)) {
			return IBuildInAggregation.TOTAL_STDDEV_FUNC;
		} else if (PluginSettings.DefaultAggregations.VARIANCE.equals(agg)) {
			return IBuildInAggregation.TOTAL_VARIANCE_FUNC;
		} else if (PluginSettings.DefaultAggregations.IRR.equals(agg)) {
			return IBuildInAggregation.TOTAL_IRR_FUNC;
		} else if (PluginSettings.DefaultAggregations.MIRR.equals(agg)) {
			return IBuildInAggregation.TOTAL_MIRR_FUNC;
		} else if (PluginSettings.DefaultAggregations.NPV.equals(agg)) {
			return IBuildInAggregation.TOTAL_NPV_FUNC;
		} else if (PluginSettings.DefaultAggregations.PERCENTILE.equals(agg)) {
			return IBuildInAggregation.TOTAL_PERCENTILE_FUNC;
		} else if (PluginSettings.DefaultAggregations.QUARTILE.equals(agg)) {
			return IBuildInAggregation.TOTAL_QUARTILE_FUNC;
		} else if (PluginSettings.DefaultAggregations.MOVING_AVERAGE.equals(agg)) {
			return IBuildInAggregation.TOTAL_MOVINGAVE_FUNC;
		} else if (PluginSettings.DefaultAggregations.RUNNING_SUM.equals(agg)) {
			return IBuildInAggregation.TOTAL_RUNNINGSUM_FUNC;
		} else if (PluginSettings.DefaultAggregations.RUNNING_NPV.equals(agg)) {
			return IBuildInAggregation.TOTAL_RUNNINGNPV_FUNC;
		} else if (PluginSettings.DefaultAggregations.RANK.equals(agg)) {
			return IBuildInAggregation.TOTAL_RANK_FUNC;
		} else if (PluginSettings.DefaultAggregations.TOP.equals(agg)) {
			return IBuildInAggregation.TOTAL_TOP_N_FUNC;
		} else if (PluginSettings.DefaultAggregations.TOP_PERCENT.equals(agg)) {
			return IBuildInAggregation.TOTAL_TOP_PERCENT_FUNC;
		} else if (PluginSettings.DefaultAggregations.BOTTOM.equals(agg)) {
			return IBuildInAggregation.TOTAL_BOTTOM_N_FUNC;
		} else if (PluginSettings.DefaultAggregations.BOTTOM_PERCENT.equals(agg)) {
			return IBuildInAggregation.TOTAL_BOTTOM_PERCENT_FUNC;
		} else if (PluginSettings.DefaultAggregations.PERCENT_RANK.equals(agg)) {
			return IBuildInAggregation.TOTAL_PERCENT_RANK_FUNC;
		} else if (PluginSettings.DefaultAggregations.PERCENT_SUM.equals(agg)) {
			return IBuildInAggregation.TOTAL_PERCENTSUM_FUNC;
		} else if (PluginSettings.DefaultAggregations.RUNNING_COUNT.equals(agg)) {
			return IBuildInAggregation.TOTAL_RUNNINGCOUNT_FUNC;
		} else if (PluginSettings.DefaultAggregations.RANGE.equals(agg)) {
			return IBuildInAggregation.TOTAL_RANGE_FUNC;
		}
		return null;
	}

	/**
	 * Check if Y grouping is defined.
	 * 
	 * @param orthSeriesDefinition
	 * @since BIRT 2.3
	 */
	public static boolean isYGroupingDefined(SeriesDefinition orthSeriesDefinition) {
		if (orthSeriesDefinition == null) {
			return false;
		}
		String yGroupExpr = null;
		if (orthSeriesDefinition.getQuery() != null) {
			yGroupExpr = orthSeriesDefinition.getQuery().getDefinition();
		}

		return yGroupExpr != null && !"".equals(yGroupExpr); //$NON-NLS-1$
	}

	/**
	 * Check if base series grouping is defined.
	 * 
	 * @param baseSD
	 * @since BIRT 2.3
	 */
	public static boolean isBaseGroupingDefined(SeriesDefinition baseSD) {
		if (baseSD != null && !baseSD.getDesignTimeSeries().getDataDefinition().isEmpty()
				&& baseSD.getGrouping() != null && baseSD.getGrouping().isEnabled()) {
			return true;
		}

		return false;
	}

	/**
	 * Check if current chart has defined grouping.
	 * 
	 * @param cm
	 * @since BIRT 2.3
	 */
	public static boolean isGroupingDefined(Chart cm) {
		SeriesDefinition baseSD = null;
		SeriesDefinition orthSD = null;
		Object[] orthAxisArray = null;
		if (cm instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes()[0].getSeriesDefinitions().get(0);

			orthAxisArray = cwa.getOrthogonalAxes(cwa.getBaseAxes()[0], true);
			orthSD = ((Axis) orthAxisArray[0]).getSeriesDefinitions().get(0);
		} else if (cm instanceof ChartWithoutAxes) {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions().get(0);
			orthSD = baseSD.getSeriesDefinitions().get(0);
		}

		if (isBaseGroupingDefined(baseSD) || isYGroupingDefined(orthSD)) {
			return true;
		}

		return false;
	}

	public static boolean isBaseGroupingDefined(Chart cm) {
		SeriesDefinition baseSD = null;
		if (cm instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes()[0].getSeriesDefinitions().get(0);
		} else if (cm instanceof ChartWithoutAxes) {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions().get(0);
		}

		if (isBaseGroupingDefined(baseSD)) {
			return true;
		}

		return false;
	}

	/**
	 * Check if running aggregates are set on chart.
	 * 
	 * @param cm
	 * @return set or not
	 * @throws ChartException
	 * @since 2.3.1
	 */
	public static boolean isSetRunningAggregation(Chart cm) throws ChartException {
		SeriesDefinition baseSD = ChartUtil.getBaseSeriesDefinitions(cm).get(0);
		for (SeriesDefinition orthoSD : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
			for (Query query : orthoSD.getDesignTimeSeries().getDataDefinition()) {
				String aggrFunc = ChartUtil.getAggregateFuncExpr(orthoSD, baseSD, query);
				if (aggrFunc == null) {
					continue;
				}

				IAggregateFunction aFunc = PluginSettings.instance().getAggregateFunction(aggrFunc);
				if (aFunc != null && aFunc.getType() == IAggregateFunction.RUNNING_AGGR) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check if summary aggregates are set on chart.
	 * 
	 * @param cm
	 * @return set or not
	 * @throws ChartException
	 */
	public static boolean isSetSummaryAggregation(Chart cm) throws ChartException {
		SeriesDefinition baseSD = ChartUtil.getBaseSeriesDefinitions(cm).get(0);
		for (SeriesDefinition orthoSD : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
			for (Query query : orthoSD.getDesignTimeSeries().getDataDefinition()) {
				String aggrFunc = ChartUtil.getAggregateFuncExpr(orthoSD, baseSD, query);
				if (aggrFunc == null) {
					continue;
				}
				IAggregateFunction aFunc = PluginSettings.instance().getAggregateFunction(aggrFunc);
				if (aFunc != null && aFunc.getType() == IAggregateFunction.SUMMARY_AGGR) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Check if chart has aggregation.
	 * 
	 * @param cm
	 */
	public static boolean hasAggregation(Chart cm) {
		SeriesDefinition baseSD = null;
		if (cm instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) cm;
			baseSD = cwa.getBaseAxes()[0].getSeriesDefinitions().get(0);
		} else if (cm instanceof ChartWithoutAxes) {
			ChartWithoutAxes cwoa = (ChartWithoutAxes) cm;
			baseSD = cwoa.getSeriesDefinitions().get(0);
		}

		// Check base is set aggregation.
		if (isBaseGroupingDefined(baseSD) && !ChartUtil.isEmpty(baseSD.getGrouping().getAggregateExpression())) {
			return true;
		}

		// Check if aggregation is just set on value series.
		try {
			if (cm instanceof ChartWithAxes) {
				EList<Axis> axisList = ((ChartWithAxes) cm).getAxes().get(0).getAssociatedAxes();
				for (Axis a : axisList) {
					for (SeriesDefinition orthSD : a.getSeriesDefinitions()) {
						for (Query query : orthSD.getDesignTimeSeries().getDataDefinition()) {
							if (ChartUtil.getAggregateFuncExpr(orthSD, baseSD, query) != null) {
								return true;
							}
						}
					}
				}
			} else if (cm instanceof ChartWithoutAxes) {
				for (SeriesDefinition orthSD : ((ChartWithoutAxes) cm).getSeriesDefinitions().get(0)
						.getSeriesDefinitions()) {
					for (Query query : orthSD.getDesignTimeSeries().getDataDefinition()) {
						if (ChartUtil.getAggregateFuncExpr(orthSD, baseSD, query) != null) {
							return true;
						}
					}
				}
			}
		} catch (ChartException e) {
			logger.log(e);
		}

		return false;
	}

	/**
	 * Finds chart report item from handle
	 * 
	 * @param eih extended item handle with chart
	 * @since 2.3
	 */
	public static IReportItem getChartReportItemFromHandle(ExtendedItemHandle eih) {
		IReportItem item = null;
		if (!isChartHandle(eih)) {
			return null;
		}
		try {
			item = eih.getReportItem();
		} catch (ExtendedElementException e) {
			logger.log(e);
		}
		if (item == null) {
			try {
				eih.loadExtendedElement();
				item = eih.getReportItem();
			} catch (ExtendedElementException eeex) {
				logger.log(eeex);
			}
			if (item == null) {
				logger.log(ILogger.ERROR,
						Messages.getString("ChartReportItemPresentationImpl.log.UnableToLocateWrapper")); //$NON-NLS-1$
			}
		}
		return item;
	}

	/**
	 * Checks if the object is handle with Chart model
	 * 
	 * @param content the object to check
	 * @since 2.3
	 */
	public static boolean isChartHandle(Object content) {
		return content instanceof ExtendedItemHandle
				&& CHART_EXTENSION_NAME.equals(((ExtendedItemHandle) content).getExtensionName());
	}

	/**
	 * Finds Chart model from handle
	 * 
	 * @param handle the handle with chart
	 * @since 2.3
	 */
	public static Chart getChartFromHandle(ExtendedItemHandle handle) {
		IReportItem item = getChartReportItemFromHandle(handle);
		if (item == null) {
			return null;
		}
		return (Chart) (item).getProperty(PROPERTY_CHART);
	}

	/**
	 * Gets all column bindings. If the handle's contain has column bindings, will
	 * combine the bindings with the handle's.
	 * 
	 * @param itemHandle handle
	 * @return the iterator of all column bindings
	 * @since 2.3
	 */
	@SuppressWarnings("unchecked")
	public static Iterator<ComputedColumnHandle> getAllColumnBindingsIterator(ReportItemHandle itemHandle) {
		ReportItemHandle container = getBindingHolder(itemHandle);
		if (container != null && container != itemHandle) {
			// Add all bindings to an iterator
			List<ComputedColumnHandle> allBindings = new ArrayList<ComputedColumnHandle>();
			for (Iterator<ComputedColumnHandle> ownBindings = itemHandle.columnBindingsIterator(); ownBindings
					.hasNext();) {
				allBindings.add(ownBindings.next());
			}
			for (Iterator<ComputedColumnHandle> containerBindings = container
					.columnBindingsIterator(); containerBindings.hasNext();) {
				allBindings.add(containerBindings.next());
			}
			return allBindings.iterator();
		}
		return itemHandle.columnBindingsIterator();
	}

	/**
	 * Transforms dimension value to points.
	 * 
	 * @param handle
	 * @param dpi    to convert px unit
	 * 
	 * @return the dimension value with measure of points
	 * @since 2.3
	 */
	public static double convertToPoints(org.eclipse.birt.report.model.api.DimensionHandle handle, int dpi) {
		double retValue = 0.0;

		if (handle.getMeasure() > 0 && handle.getUnits().trim().length() > 0) {
			if (DesignChoiceConstants.UNITS_PT.equalsIgnoreCase(handle.getUnits())) {
				retValue = handle.getMeasure();
			} else if (DesignChoiceConstants.UNITS_PX.equalsIgnoreCase(handle.getUnits())) {
				retValue = (handle.getMeasure() * 72d) / dpi;
			} else {
				retValue = DimensionUtil
						.convertTo(handle.getMeasure(), handle.getUnits(), DesignChoiceConstants.UNITS_PT).getMeasure();
			}
		}
		return retValue;
	}

	/**
	 * Convert model/engine aggregate expression to chart.
	 * 
	 * @param agg
	 * @since 2.3
	 */
	public static String convertToChartAggExpression(String agg) {

		if (DesignChoiceConstants.AGGREGATION_FUNCTION_SUM.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.SUM;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_AVERAGE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.AVERAGE;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_COUNT.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.COUNT;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_COUNTDISTINCT.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.DISTINCT_COUNT;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_FIRST.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.FIRST;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_LAST.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.LAST;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MIN.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MIN;

		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MAX.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MAX;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_WEIGHTEDAVG.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.WEIGHTED_AVERAGE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MEDIAN.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MEDIAN;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MODE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MODE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_STDDEV.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.STDDEV;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_VARIANCE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.VARIANCE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_IRR.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.IRR;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MIRR.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MIRR;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_NPV.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.NPV;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENTILE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.PERCENTILE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_TOP_QUARTILE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.QUARTILE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_MOVINGAVE.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.MOVING_AVERAGE;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGSUM.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.RUNNING_SUM;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGNPV.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.RUNNING_NPV;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_RANK.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.RANK;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.TOP;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_IS_TOP_N_PERCENT.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.TOP_PERCENT;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.BOTTOM;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_IS_BOTTOM_N_PERCENT.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.BOTTOM_PERCENT;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_RANK.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.PERCENT_RANK;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_PERCENT_SUM.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.PERCENT_SUM;
		} else if (DesignChoiceConstants.AGGREGATION_FUNCTION_RUNNINGCOUNT.equalsIgnoreCase(agg)) {
			return PluginSettings.DefaultAggregations.RUNNING_COUNT;
		}

		return null;
	}

	/**
	 * Check if chart is child of multi-views handle.
	 * 
	 * @param handle
	 * @since 2.3
	 */
	public static boolean isChildOfMultiViewsHandle(DesignElementHandle handle) {
		if (handle != null && handle.getContainer() instanceof MultiViewsHandle) {
			return true;
		}
		return false;
	}

	/**
	 * Returns report item reference of specified item handle.
	 * 
	 * @param itemHandle
	 * @since 2.3
	 */
	public static ReportItemHandle getReportItemReference(ReportItemHandle itemHandle) {
		return getReportItemReferenceImpl(itemHandle, itemHandle);
	}

	/**
	 * Returns item handle reference.
	 * 
	 * @param currentItemHandle
	 * @param itemHandle
	 * @return
	 * @since 2.3
	 */
	private static ReportItemHandle getReportItemReferenceImpl(final ReportItemHandle currentItemHandle,
			final ReportItemHandle itemHandle) {
		ReportItemHandle handle = currentItemHandle.getDataBindingReference();
		if (handle == null) {
			if (currentItemHandle.getContainer() instanceof MultiViewsHandle) {
				return getReportItemReferenceImpl((ReportItemHandle) currentItemHandle.getContainer().getContainer(),
						itemHandle);
			} else if (currentItemHandle == itemHandle) {
				return null;
			}

			return currentItemHandle;
		}

		return getReportItemReferenceImpl(handle, itemHandle);
	}

	/**
	 * Check if specified report item handle is related to chart.
	 * 
	 * @param handle
	 * @since 2.3
	 */
	public static boolean isChartReportItemHandle(ReportItemHandle handle) {
		if (handle instanceof ExtendedItemHandle && getChartFromHandle((ExtendedItemHandle) handle) != null) {
			return true;
		}
		return false;
	}

	/**
	 * Checks if chart inherits groupings and aggregations from container
	 * 
	 * @param handle chart handle
	 * @return inherits groupings or not
	 * @since 2.5
	 */
	public static boolean isChartInheritGroups(ReportItemHandle handle) {
		return handle.getDataSet() == null && isContainerInheritable(handle)
				&& !handle.getBooleanProperty(ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS);
	}

	/**
	 * Check if chart inherits columns only from container.
	 * 
	 * @param handle
	 * @return true means inherit columns only
	 * @since 2.5.3
	 */
	public static boolean isChartInheritColumnsOnly(ReportItemHandle handle) {
		return handle.getDataSet() == null && isContainerInheritable(handle)
				&& handle.getBooleanProperty(ChartReportItemConstants.PROPERTY_INHERIT_COLUMNS);
	}

	/**
	 * Check if chart inherits cube from container.
	 * 
	 * @param handle
	 * @return true if chart inherits cube from container.
	 */
	public static boolean isChartInhertCube(ReportItemHandle handle) {
		boolean isChartInheritCube = (handle.getDataSet() == null) && (handle.getCube() == null);
		if (!isChartInheritCube) {
			return false;
		}

		CubeHandle cube = null;
		DesignElementHandle container = handle.getContainer();
		while (container != null) {
			if (container instanceof ReportItemHandle) {
				cube = ((ReportItemHandle) container).getCube();
				if (cube != null) {
					break;
				}
			}
			container = container.getContainer();
		}

		if (cube == null) {
			return false;
		}
		if (ChartReportItemHelper.instance().getBindingCubeHandle(handle) == null) {
			return false;
		}

		return true;
	}

	/**
	 * Checks if the item's container is inheritable. Usually only Table and List
	 * can support inheritance.
	 * 
	 * @param itemHandle item
	 * @return true means inheritable
	 * @since 2.5
	 */
	public static boolean isContainerInheritable(ReportItemHandle itemHandle) {
		DesignElementHandle container = itemHandle.getContainer();
		if (container instanceof CellHandle || container instanceof ListHandle || container instanceof ListGroupHandle
				|| (container instanceof ExtendedItemHandle
						&& "CrosstabCell".equals(((ExtendedItemHandle) container).getExtensionName()))) //$NON-NLS-1$
		{
			while (container != null) {
				if (container instanceof ListingHandle) {
					return true;
				} else if (container instanceof GridHandle) {
					return true;
				} else if (container instanceof ExtendedItemHandle
						&& "Crosstab".equals(((ExtendedItemHandle) container).getExtensionName())) //$NON-NLS-1$
				{
					return true;
				}
				container = container.getContainer();
			}
		}
		return false;
	}

	public static boolean isContainerGridHandle(ReportItemHandle itemHandle) {
		DesignElementHandle container = itemHandle.getContainer();
		if (container instanceof CellHandle) {
			while (container != null) {
				if (container instanceof GridHandle) {
					return true;
				}
				container = container.getContainer();
			}
		}
		return false;
	}

	/**
	 * Get the inherited handle
	 * 
	 * @param itemHandle
	 * @return ListHandle or TabHandle or GridHandle
	 */
	public static ReportItemHandle getInheritedHandle(ReportItemHandle itemHandle) {
		if (itemHandle.getDataSet() == null && itemHandle.getCube() == null && isContainerInheritable(itemHandle)) {
			DesignElementHandle handle = itemHandle.getContainer();
			while (handle != null && !(handle instanceof ListingHandle || handle instanceof GridHandle
					|| (handle instanceof ExtendedItemHandle
							&& "Crosstab".equals(((ExtendedItemHandle) handle).getExtensionName())))) //$NON-NLS-1$
			{
				handle = handle.getContainer();
			}
			if (handle instanceof TableHandle) {
				return (TableHandle) handle;
			} else if (handle instanceof ListHandle) {
				return (ListHandle) handle;
			} else if (handle instanceof GridHandle) {
				return (GridHandle) handle;
			} else if (handle instanceof ExtendedItemHandle
					&& "Crosstab".equals(((ExtendedItemHandle) handle).getExtensionName())) //$NON-NLS-1$
			{
				return (ExtendedItemHandle) handle;
			}
		}
		return null;
	}

	/**
	 * Returns report item handle that is a chart handle and is referred by other
	 * chart recursively.
	 * 
	 * @param handle
	 * @return referenced chart handle
	 * @since 2.5
	 */
	public static ExtendedItemHandle getChartReferenceItemHandle(ReportItemHandle handle) {
		ReportItemHandle refHandle = handle.getDataBindingReference();
		if (refHandle == null || !isChartHandle(refHandle)) {
			return null;
		}

		return getChartReferenceItemHandleImpl((ExtendedItemHandle) refHandle);
	}

	private static ExtendedItemHandle getChartReferenceItemHandleImpl(ExtendedItemHandle chartHandle) {
		ReportItemHandle refHandle = chartHandle.getDataBindingReference();
		if (refHandle != null && isChartHandle(refHandle)) {
			return getChartReferenceItemHandleImpl((ExtendedItemHandle) chartHandle.getDataBindingReference());
		}
		return chartHandle;
	}

	/**
	 * Checks if chart model has bound queries completely.
	 * 
	 * @param cm chart model
	 * @return true complete
	 * @since 2.5
	 */
	public static boolean checkChartBindingComplete(Chart cm) {
		Series bs = ChartUtil.getBaseSeriesDefinitions(cm).get(0).getDesignTimeSeries();
		if (bs.getDataDefinition().size() == 0 || ChartUtil.isEmpty(bs.getDataDefinition().get(0).getDefinition())) {
			return false;
		}
		for (SeriesDefinition vsd : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
			Series vs = vsd.getDesignTimeSeries();
			if (vs.getDataDefinition().size() == 0
					|| ChartUtil.isEmpty(vs.getDataDefinition().get(0).getDefinition())) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates the default bounds for chart model.
	 * 
	 * @param eih chart handle
	 * @param cm  chart model
	 * @return default bounds
	 * @since 2.3
	 */
	public static Bounds createDefaultChartBounds(ExtendedItemHandle eih, Chart cm) {
		// Axis chart case
		if (ChartCubeUtil.isAxisChart(eih)) {
			// Axis chart must be ChartWithAxes
			ChartWithAxes cmWA = (ChartWithAxes) cm;
			if (cmWA.isTransposed()) {
				return BoundsImpl.create(0, 0, DEFAULT_CHART_BLOCK_WIDTH, DEFAULT_AXIS_CHART_BLOCK_SIZE);
			}
			return BoundsImpl.create(0, 0, DEFAULT_AXIS_CHART_BLOCK_SIZE, DEFAULT_CHART_BLOCK_HEIGHT);
		}
		// Plot or ordinary chart case
		else if (ChartCubeUtil.isPlotChart(eih)) {
			return BoundsImpl.create(0, 0, ChartCubeUtil.DEFAULT_COLUMN_WIDTH.getMeasure(),
					ChartCubeUtil.DEFAULT_ROW_HEIGHT.getMeasure());
		} else {
			return BoundsImpl.create(0, 0, DEFAULT_CHART_BLOCK_WIDTH, DEFAULT_CHART_BLOCK_HEIGHT);
		}
	}

	/**
	 * Computes bound size according to handle and dpi
	 * 
	 * @param eih chart handle
	 * @param dpi dpi
	 * @return bound size
	 * @since 4.0.0
	 */
	public static Bounds computeChartBounds(ExtendedItemHandle eih, int dpi) {
		final DimensionHandle dhHeight;
		final DimensionHandle dhWidth;
		final boolean bAxisChart = ChartCubeUtil.isAxisChart(eih);
		final Chart cm;
		if (bAxisChart) {
			ExtendedItemHandle hostChart = (ExtendedItemHandle) eih
					.getElementProperty(ChartReportItemConstants.PROPERTY_HOST_CHART);
			cm = ChartCubeUtil.getChartFromHandle(hostChart);
			if (cm == null) {
				return null;
			}
			// Use plot chart's size as axis chart's. Even if model sizes
			// are different, the output size are same
			if (((ChartWithAxes) cm).isTransposed()) {
				dhHeight = eih.getHeight();
				dhWidth = hostChart.getWidth();
			} else {
				dhHeight = hostChart.getHeight();
				dhWidth = eih.getWidth();
			}
		} else {
			cm = ChartCubeUtil.getChartFromHandle(eih);
			if (cm == null) {
				return null;
			}
			dhHeight = eih.getHeight();
			dhWidth = eih.getWidth();
		}

		Bounds cmBounds = (cm.getBlock() != null) ? cm.getBlock().getBounds() : null;
		Bounds defaultBounds = ChartItemUtil.createDefaultChartBounds(eih, cm);
		if (cmBounds != null && cmBounds.getWidth() > 0) {
			defaultBounds.setWidth(cmBounds.getWidth());
		}
		if (cmBounds != null && cmBounds.getHeight() > 0) {
			defaultBounds.setHeight(cmBounds.getHeight());
		}

		return computeBounds(dhWidth, dhHeight, dpi, defaultBounds);
	}

	private static Bounds computeBounds(DimensionHandle dhWidth, DimensionHandle dhHeight, int dpi,
			Bounds defaultBounds) {
		double dOriginalHeight = dhHeight.getMeasure();
		String sHeightUnits = dhHeight.getUnits();

		double dOriginalWidth = dhWidth.getMeasure();
		String sWidthUnits = dhWidth.getUnits();

		double dHeightInPoints = defaultBounds.getHeight();
		double dWidthInPoints = defaultBounds.getWidth();

		try {
			if (sHeightUnits != null) {
				// Convert from pixels to points first...since DimensionUtil
				// does not provide conversion services to and from Pixels
				if (sHeightUnits == DesignChoiceConstants.UNITS_PX) {
					dOriginalHeight = (dOriginalHeight * 72d) / dpi;
					sHeightUnits = DesignChoiceConstants.UNITS_PT;
				}
				dHeightInPoints = DimensionUtil.convertTo(dOriginalHeight, sHeightUnits, DesignChoiceConstants.UNITS_PT)
						.getMeasure();
			}

			if (sWidthUnits != null) {
				// Convert from pixels to points first...since DimensionUtil
				// does not provide conversion services to and from Pixels
				if (sWidthUnits == DesignChoiceConstants.UNITS_PX) {
					dOriginalWidth = (dOriginalWidth * 72d) / dpi;
					sWidthUnits = DesignChoiceConstants.UNITS_PT;
				}
				dWidthInPoints = DimensionUtil.convertTo(dOriginalWidth, sWidthUnits, DesignChoiceConstants.UNITS_PT)
						.getMeasure();
			}
		} catch (IllegalArgumentException e) {
			// Catch exception here to avoid invalid units
			logger.log(e);
		}

		return BoundsImpl.create(0, 0, dWidthInPoints, dHeightInPoints);
	}

	public static Bounds computeBounds(ExtendedItemHandle eih, int dpi, Bounds defaultBounds) {
		return computeBounds(eih.getWidth(), eih.getHeight(), dpi, defaultBounds);
	}

	/**
	 * Checks if chart model in handle uses the bindings in the list.
	 * 
	 * @param handle       item handle that contains chart model
	 * @param bindingNames binding list
	 * @return true if chart contains one or more bindings in list.
	 */
	public static boolean checkBindingsUsed(ExtendedItemHandle handle, List<String> bindingNames) {
		if (bindingNames != null && !bindingNames.isEmpty()) {
			Chart cm = getChartFromHandle(handle);
			if (cm == null) {
				return false;
			}
			Set<String> usedBindings = new HashSet<String>();
			SeriesDefinition bsd = ChartUtil.getBaseSeriesDefinitions(cm).get(0);

			// Add X series
			Series xSeries = bsd.getDesignTimeSeries();
			usedBindings.add(xSeries.getDataDefinition().get(0).getDefinition());

			// Add Y series
			for (SeriesDefinition vsd : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
				usedBindings.add(vsd.getQuery().getDefinition());
				Series vs = vsd.getDesignTimeSeries();
				for (Query query : vs.getDataDefinition()) {
					usedBindings.add(query.getDefinition());
				}
			}

			ExpressionCodec ec = ChartModelHelper.instance().createExpressionCodec();
			for (String strQuery : usedBindings) {
				if (strQuery.trim().length() > 0) {
					ec.decode(strQuery);
					Collection<String> names = ec.getRowBindingNameSet();
					if (names.isEmpty()) {
						names = ec.getCubeBindingNameList();
					}
					if (names.isEmpty()) {
						continue;
					}

					for (String bindingName : bindingNames) {
						if (names.contains(bindingName)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * Loads the expression from a ComputedColumnHandle into the ExpressionCodec.
	 * 
	 * @param exprCodec
	 * @param handle
	 * @return True if succeeds.
	 */
	public static boolean loadExpression(ExpressionCodec exprCodec, ComputedColumnHandle cch) {
		if (exprCodec != null) {
			ExpressionHandle eh = getAggregationExpression(cch);
			return loadExpressionFromHandle(exprCodec, eh);
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private static ExpressionHandle getAggregationExpression(ComputedColumnHandle bindingColumn) {
		if (bindingColumn.getExpression() != null) {
			return bindingColumn.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);
		}
		String functionName = bindingColumn.getAggregateFunction();
		try {
			IAggrFunction function = AggregationManager.getInstance().getAggregation(functionName);
			for (IParameterDefn param : function.getParameterDefn()) {
				if (param.isDataField()) {
					for (Iterator<AggregationArgumentHandle> iterator = bindingColumn.argumentsIterator(); iterator
							.hasNext();) {
						AggregationArgumentHandle arg = iterator.next();
						if (arg.getName().equals(param.getName())) {
							return arg.getExpressionProperty(AggregationArgument.VALUE_MEMBER);
						}
					}
				}
			}
		} catch (BirtException e) {
			logger.log(e);
		}
		return null;

	}

	public static boolean loadExpressionFromHandle(ExpressionCodec exprCodec, Expression expression) {
		if (expression != null && expression.getStringExpression() != null) {
			exprCodec.setExpression(expression.getStringExpression());
			exprCodec.setType(expression.getType());
			return true;
		} else {
			exprCodec.setExpression(null);
			return false;
		}
	}

	protected static boolean loadExpressionFromHandle(ExpressionCodec exprCodec, ExpressionHandle eh) {
		if (eh != null && eh.getValue() != null) {
			Expression expression = (Expression) eh.getValue();
			exprCodec.setExpression(expression.getStringExpression());
			exprCodec.setType(expression.getType());
			return true;
		} else {
			exprCodec.setExpression(null);
			return false;
		}
	}

	public static boolean loadExpression(ExpressionCodec exprCodec, FilterConditionElementHandle fceh) {
		if (exprCodec != null) {
			ExpressionHandle eh = fceh.getExpressionProperty(FilterCondition.EXPR_MEMBER);
			return loadExpressionFromHandle(exprCodec, eh);
		}
		return false;
	}

	public static boolean loadExpression(ExpressionCodec exprCodec, FilterCondition fceh) {
		if (exprCodec != null) {
			Expression eh = fceh.getExpressionProperty(FilterCondition.EXPR_MEMBER);
			return loadExpressionFromHandle(exprCodec, eh);
		}
		return false;
	}

	public static boolean loadExpression(ExpressionCodec exprCodec, FilterConditionHandle fceh) {
		if (exprCodec != null) {
			ExpressionHandle eh = fceh.getExpressionProperty(FilterCondition.EXPR_MEMBER);
			return loadExpressionFromHandle(exprCodec, eh);
		}
		return false;
	}

	/**
	 * Loads the expression from a ComputedColumnHandle into the ExpressionCodec.
	 * 
	 * @param exprCodec
	 * @param handle
	 */
	public static void loadExpression(ExpressionCodec exprCodec, GroupHandle gh) {
		if (exprCodec != null) {
			ExpressionHandle eh = gh.getExpressionProperty(IGroupElementModel.KEY_EXPR_PROP);
			loadExpressionFromHandle(exprCodec, eh);
		}
	}

	/**
	 * Converts data type defined in {@link DesignChoiceConstants} to
	 * {@link DataType}
	 * 
	 * @param dataType data type in design engine
	 * @return data type in chart model. Value may be null if data type is null or
	 *         equal to {@link DesignChoiceConstants#COLUMN_DATA_TYPE_ANY}
	 */
	@SuppressWarnings("deprecation")
	public static DataType convertToDataType(String dataType) {
		if (dataType == null) {
			return null;
		}
		if (dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_STRING)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_BLOB)) {
			return DataType.TEXT_LITERAL;
		}
		if (dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DECIMAL)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_FLOAT)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_INTEGER)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_BOOLEAN)) {
			return DataType.NUMERIC_LITERAL;
		}
		if (dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATETIME)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_DATE)
				|| dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_TIME)) {
			return DataType.DATE_TIME_LITERAL;
		} else if (dataType.equals(DesignChoiceConstants.COLUMN_DATA_TYPE_ANY)) {
			return null;
		}
		return DataType.TEXT_LITERAL;
	}

	/**
	 * Converts data type defined in {@link DesignChoiceConstants} to axis type in
	 * {@link AxisType}
	 * 
	 * @param dataType data type in design engine
	 * @return axis type in chart model
	 */
	public static AxisType convertToAxisType(String dataType) {
		DataType type = convertToDataType(dataType);
		if (type == DataType.NUMERIC_LITERAL) {
			return AxisType.LINEAR_LITERAL;
		}
		if (type == DataType.DATE_TIME_LITERAL) {
			return AxisType.DATE_TIME_LITERAL;
		}
		return AxisType.TEXT_LITERAL;
	}

	/**
	 * Checks if current bindings of chart's refer to other item.
	 * 
	 * @param itemHandle
	 * @return if current bindings of chart's refer to other item
	 * @since 2.6.1
	 */
	public static boolean isReportItemReference(ReportItemHandle itemHandle) {
		return getReportItemReference(itemHandle) != null;
	}

	/**
	 * Checks if chart is in multiple view.
	 * 
	 * @param itemHandle
	 * @return true if chart is in multiple view.
	 * @since 4.2.2
	 */
	public static boolean isInMultiViews(ReportItemHandle itemHandle) {
		return itemHandle.getContainer() instanceof MultiViewsHandle;
	}

	/**
	 * Checks if current bindings of chart's refer to the data set or cube directly.
	 * 
	 * @param itemHandle
	 * @return if current bindings of chart's refer to the data set or cube directly
	 * @since 2.6.1
	 */
	public static boolean isDirectBinding(ReportItemHandle itemHandle) {
		return (itemHandle.getDataSet() != null || itemHandle.getCube() != null) && !isReportItemReference(itemHandle);
	}

	/**
	 * Creates an runnable class used in report engine. Similar to
	 * {@link ReportEngineHelper#openReportDesign(org.eclipse.birt.report.model.api.ReportDesignHandle)}
	 * 
	 * @param reportEngine report engine
	 * @param moduleHandle module handle including report and library
	 * @return runnable class
	 * @throws BirtException
	 */
	public static IReportRunnable openReportDesign(ReportEngine reportEngine, ModuleHandle moduleHandle)
			throws BirtException {
		ReportRunnable ret = new ReportRunnable(reportEngine, moduleHandle);
		ret.setReportName(moduleHandle.getFileName());
		return ret;
	}

	/**
	 * Returns the data type of specified expression.
	 * 
	 * @param expression
	 * @param itemHandle
	 * @return data type of specified expression.
	 */
	public static DataType getExpressionDataType(String expression, ReportItemHandle itemHandle) {
		if (expression == null || expression.trim().length() == 0) {
			return null;
		}

		// Find data types from self column bindings first
		Object[] returnObj = findDataType(expression, itemHandle);
		if (((Boolean) returnObj[0]).booleanValue()) {
			return (DataType) returnObj[1];
		}

		// Find data types from its container column bindings.
		ReportItemHandle parentHandle = getBindingHolder(itemHandle);
		if (parentHandle != null) {
			returnObj = findDataType(expression, parentHandle);
			if (((Boolean) returnObj[0]).booleanValue()) {
				return (DataType) returnObj[1];
			}
		}

		// Try to parse with number format
		try {
			NumberFormat.getInstance().parse(expression);
			return DataType.NUMERIC_LITERAL;
		} catch (ParseException e) {
		}

		// Try to parse with date format
		try {
			DateFormat.getInstance().parse(expression);
			return DataType.DATE_TIME_LITERAL;
		} catch (ParseException e) {
		}

		// Return null for unknown data type.
		return null;
	}

	/**
	 * Find data type of expression from specified item handle.
	 * 
	 * @param expression expression.
	 * @param itemHandle specified item handle.
	 * @return an object array, size is two, the first element is a boolean object,
	 *         if its value is <code>true</code> then means the data type is found
	 *         and the second element of array stores the data type; if its value is
	 *         <code>false</code> then means that data type is not found.
	 */
	private static Object[] findDataType(String expression, ReportItemHandle itemHandle) {
		ExpressionCodec exprCodec = ChartModelHelper.instance().createExpressionCodec();
		// Below calling 'ChartReportItemUtil.checkStringInExpression' exists
		// problem, it just check special case, like row["a"]+"Q"+row["b"].
		// In fact, if expression is a script, it should be no way to get the
		// return type.
		// Now the only thing we can do is to try to consider more situations
		// and avoid wrong check, we will just check the single line expression.
		// If it is not a single line expression, the data type will no be
		// checked. Of course even if we just check single line expression, it
		// still will get wrong result for valid expression, but it will avoid
		// error check in many situations.
		// The ChartReportItemUtil.checkStringInExpression will be refactored.

		boolean complexScripts = isMultiLineExpression(expression);

		// Checks if expression contains string
		if (!complexScripts && ChartExpressionUtil.checkStringInExpression(expression)) {
			return new Object[] { true, DataType.TEXT_LITERAL };
		}

		exprCodec.decode(expression);

		// simple means one binding expression without js function
		if (!exprCodec.isBinding(false)) {
			return new Object[] { false, null };
		}

		Object[] returnObj = new Object[2];
		returnObj[0] = Boolean.FALSE;
		String columnName = exprCodec.getBindingName();

		Iterator<ComputedColumnHandle> iterator = ChartItemUtil.getAllColumnBindingsIterator(itemHandle);
		while (iterator.hasNext()) {
			ComputedColumnHandle cc = iterator.next();
			if (cc.getName().equalsIgnoreCase(columnName)) {
				String dataType = cc.getDataType();
				if (dataType == null) {
					continue;
				}
				returnObj[0] = Boolean.TRUE;
				returnObj[1] = ChartItemUtil.convertToDataType(dataType);
			}
		}
		return returnObj;
	}

	/**
	 * Check if a expression has multiple lines.
	 * 
	 * @param expression
	 * @return true if a expression has multiple lines.
	 */
	public static boolean isMultiLineExpression(String expression) {
		if (expression == null) {
			return false;
		}
		boolean complexScripts = false;
		for (int i = 0; i < expression.length(); i++) {
			if (expression.charAt(i) == '\n' && i != (expression.length() - 1)) {
				complexScripts = true;
				break;
			}
		}
		return complexScripts;
	}

	/**
	 * Returns the boolean value from extended property.
	 * 
	 * @param cm                   chart model
	 * @param extendedPropertyName property name
	 * @param defaultValue         default value when it's null
	 * @return boolean property value
	 */
	public static boolean getBooleanProperty(Chart cm, String extendedPropertyName, boolean defaultValue) {
		if (cm == null) {
			return defaultValue;
		}
		ExtendedProperty property = ChartUtil.getExtendedProperty(cm, extendedPropertyName);
		if (property == null) {
			return defaultValue;
		}
		return Boolean.valueOf(property.getValue());
	}

	/**
	 * Returns if cube hierarchy should be kept on category
	 * 
	 * @param cm chart model
	 * @return result
	 */
	public static boolean isKeepCubeHierarchyOnCategory(Chart cm) {
		return getBooleanProperty(cm, EXTENDED_PROPERTY_HIERARCHY_CATEGORY,
				ChartUtil.compareVersion(cm.getVersion(), "2.5.3") > 0); //$NON-NLS-1$
	}

	/**
	 * Returns if cube hierarchy should be kept on series
	 * 
	 * @param cm chart model
	 * @return result
	 */
	public static boolean isKeepCubeHierarchyOnSeries(Chart cm) {
		return getBooleanProperty(cm, EXTENDED_PROPERTY_HIERARCHY_SERIES,
				ChartUtil.compareVersion(cm.getVersion(), "2.5.3") > 0); //$NON-NLS-1$
	}

	/**
	 * Sets the property to keep cube hierarchy on category
	 * 
	 * @param cm    chart model
	 * @param value state
	 */
	public static void setKeepCubeHierarchyOnCategory(Chart cm, boolean value) {
		ChartUtil.setExtendedProperty(cm, EXTENDED_PROPERTY_HIERARCHY_CATEGORY, String.valueOf(value));
	}

	/**
	 * Sets the property to keep cube hierarchy on series.
	 * 
	 * @param cm    chart model
	 * @param value state
	 */
	public static void setKeepCubeHierarchyOnSeries(Chart cm, boolean value) {
		ChartUtil.setExtendedProperty(cm, EXTENDED_PROPERTY_HIERARCHY_SERIES, String.valueOf(value));
	}

	/**
	 * Get externalized message.
	 * 
	 * @param handle        design element handle
	 * @param sKey          the key of the externalized message
	 * @param sDefaultValue default value
	 * @param locale        locale information
	 * 
	 * @return message the externalized message.
	 */
	public static String externalizedMessage(DesignElementHandle handle, String sKey, String sDefaultValue,
			ULocale locale) {
		return ModuleUtil.getExternalizedValue(handle, sKey, sDefaultValue, locale);
	}

	/**
	 * Returns chart output format name, null if not find.
	 * 
	 * @param chartHandle extended item handle contains chart.
	 * @return chart output format name, null if not find.
	 */
	public static String getChartOutputFormat(ExtendedItemHandle chartHandle) {
		Object output = chartHandle.getProperty(PROPERTY_OUTPUT);
		if (output instanceof String) {
			return (String) output;
		}
		return null;
	}
}
