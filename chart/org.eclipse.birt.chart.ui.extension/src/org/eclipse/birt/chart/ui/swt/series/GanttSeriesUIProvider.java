/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.ui.swt.series;

import java.util.ArrayList;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.AxisType;
import org.eclipse.birt.chart.model.attribute.DataType;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.Query;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.data.SeriesGrouping;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.ui.i18n.Messages;
import org.eclipse.birt.chart.ui.plugin.ChartUIExtensionPlugin;
import org.eclipse.birt.chart.ui.swt.DefaultSelectDataComponent;
import org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataComponent;
import org.eclipse.birt.chart.ui.swt.interfaces.ISelectDataCustomizeUI;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizard;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.birt.chart.ui.swt.wizard.data.BaseDataDefinitionComponent;
import org.eclipse.birt.chart.ui.swt.wizard.data.YOptionalDataDefinitionComponent;
import org.eclipse.birt.chart.ui.util.ChartUIConstants;
import org.eclipse.birt.chart.util.ChartExpressionUtil.ExpressionCodec;
import org.eclipse.emf.common.util.EList;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;

/**
 * @author Actuate Corporation
 * 
 */
public class GanttSeriesUIProvider extends DefaultSeriesUIProvider {

	private static final String SERIES_CLASS = "org.eclipse.birt.chart.model.type.impl.GanttSeriesImpl"; //$NON-NLS-1$

	public GanttSeriesUIProvider() {
		super();
	}

	public Composite getSeriesAttributeSheet(Composite parent, Series series, ChartWizardContext context) {
		return new GanttSeriesAttributeComposite(parent, SWT.NONE, context, series);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#getSeriesClass()
	 */
	public String getSeriesClass() {
		return SERIES_CLASS;
	}

	public ISelectDataComponent getSeriesDataComponent(int seriesType, SeriesDefinition seriesDefn,
			ChartWizardContext context, String sTitle) {
		if (seriesType == ISelectDataCustomizeUI.ORTHOGONAL_SERIES) {
			return new GanttDataDefinitionComponent(seriesDefn, context, sTitle);
		} else if (seriesType == ISelectDataCustomizeUI.GROUPING_SERIES) {
			BaseDataDefinitionComponent ddc = new YOptionalDataDefinitionComponent(
					BaseDataDefinitionComponent.BUTTON_GROUP, ChartUIConstants.QUERY_OPTIONAL, seriesDefn,
					seriesDefn.getQuery(), context, sTitle);
			return ddc;
		}
		return new DefaultSelectDataComponent();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#getCompatibleAxisType(
	 * org.eclipse.birt.chart.model.component.Series )
	 */
	public AxisType[] getCompatibleAxisType(Series series) {
		return new AxisType[] { AxisType.DATE_TIME_LITERAL };
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#
	 * validateSeriesBindingType(org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.ui.swt.interfaces.IDataServiceProvider)
	 */
	public void validateSeriesBindingType(Series series, IDataServiceProvider idsp) throws ChartException {
		ArrayList<Query> al = new ArrayList<Query>();
		al.addAll(series.getDataDefinition());
		for (int i = 0; i < al.size(); i++) {
			Query query = al.get(i);
			DataType dataType = idsp.getDataType(query.getDefinition());

			if ((i != 2) && (dataType == DataType.TEXT_LITERAL || dataType == DataType.NUMERIC_LITERAL)) {
				final ExpressionCodec codec = ChartModelHelper.instance().createExpressionCodec();
				codec.decode(query.getDefinition());
				throw new ChartException(ChartUIExtensionPlugin.ID, ChartException.DATA_BINDING, codec.getExpression());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.birt.chart.ui.swt.interfaces.ISeriesUIProvider#
	 * validateAggregationType (org.eclipse.birt.chart.model.component.Series,
	 * org.eclipse.birt.chart.model.data.SeriesDefinition,
	 * org.eclipse.birt.chart.model.data.SeriesDefinition)
	 */
	public boolean isValidAggregationType(Series series, SeriesDefinition orthSD, SeriesDefinition baseSD) {
		boolean isValidAgg = true;
		EList<Query> queries = series.getDataDefinition();
		for (int i : validationIndex(series)) {
			if (!isValidAgg) {
				break;
			}
			Query query = queries.get(i);
			boolean checked = false;
			String id = ChartWizard.Gatt_aggCheck_ID + series.eContainer().hashCode()
					+ series.getDataDefinition().indexOf(query);
			ChartWizard.removeException(id);
			if (query.getGrouping() != null && query.getGrouping().isEnabled()) {
				checked = true;
				isValidAgg = isValidAggregation(query.getGrouping(), false, id);
			}

			if (!checked && orthSD.getGrouping() != null && orthSD.getGrouping().isEnabled()) {
				checked = true;
				isValidAgg = isValidAggregation(orthSD.getGrouping(), false, id);
			}

			if (!checked) {

				if (baseSD.getGrouping() != null && baseSD.getGrouping().isEnabled()) {
					isValidAgg = isValidAggregation(baseSD.getGrouping(), true, id);
				}
			}
		}
		return isValidAgg;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.ui.swt.DefaultSeriesUIProvider#validationIndex(org.
	 * eclipse.birt.chart.model.component.Series)
	 */
	public int[] validationIndex(Series series) {
		return new int[] { 0, 1 };
	}

	/**
	 * Check if default aggregation and value series aggregation are valid for Gantt
	 * chart and show warning message in UI.
	 * 
	 * @param grouping
	 * @param isCategoryGrouping
	 * @param id                 error id for log
	 * @return <code>true</code> if aggregation is valid.
	 * @since 2.3
	 * 
	 */
	private boolean isValidAggregation(SeriesGrouping grouping, boolean isCategoryGrouping, String id) {
		if (grouping == null || !grouping.isEnabled()) {
			return true;
		}

		String aggName = grouping.getAggregateExpression();
		// Gantt chart only allow First, Last, Min and Max aggregations.
		if (!("First".equalsIgnoreCase(aggName) //$NON-NLS-1$
				|| "Last".equalsIgnoreCase(aggName) //$NON-NLS-1$
				|| "Min".equalsIgnoreCase(aggName) //$NON-NLS-1$
				|| "Max".equalsIgnoreCase(aggName))) //$NON-NLS-1$
		{
			String aggPlace = ""; //$NON-NLS-1$
			if (isCategoryGrouping) {
				aggPlace = Messages.getString("ChartUIUtil.TaskSelectData.Warning.CheckAgg.DefaultAggregate"); //$NON-NLS-1$
			} else {
				aggPlace = Messages.getString("ChartUIUtil.TaskSelectData.Warning.CheckAgg.ValueSeriesAggregate"); //$NON-NLS-1$
			}
			ChartWizard.showException(id, Messages.getString("ChartUIUtil.TaskSelectData.Warning.CheckAgg.GanttChart")//$NON-NLS-1$
					+ aggName + Messages.getString("ChartUIUtil.TaskSelectData.Warning.CheckAggAs")//$NON-NLS-1$
					+ aggPlace + Messages.getString("ChartUIUtil.TaskSelectData.Warning.CheckAgg.Aggregation"));//$NON-NLS-1$

			return false;
		}

		return true;
	}

}