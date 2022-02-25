/***********************************************************************
 * Copyright (c) 2011 Actuate Corporation.
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

package org.eclipse.birt.chart.model.util;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.TitleBlock;
import org.eclipse.birt.chart.model.type.GanttSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import com.ibm.icu.util.ULocale;

/**
 * This class override base chart value updater, it overrides super methods.
 */

public class ChartValueUpdater extends BaseChartValueUpdater {
	protected Chart chart;
	private ULocale uLocale;

	protected ULocale getULocale() {
		return uLocale;
	}

	public void setULocale(ULocale uLocale) {
		this.uLocale = uLocale;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.util.BaseChartValueUpdater#update(org.eclipse.
	 * birt.chart.model.Chart, org.eclipse.birt.chart.model.Chart)
	 */
	@Override
	public void update(Chart eObj, Chart eRefObj) {
		chart = eObj;

		beforeUpdate(eObj, eRefObj, true);

		super.update(eObj, eRefObj);

		revise(eObj, eRefObj, true);
		chart = null;
	}

	/**
	 * @param eObj
	 * @param eRefObj
	 * @param checkVisible
	 */
	public void update(Chart eObj, Chart eRefObj, boolean checkVisible) {
		chart = eObj;
		if (eObj != null) {
			beforeUpdate(eObj, eRefObj, checkVisible);

			updateChart(eObj.eClass().getName(), null, eObj, eRefObj, true, checkVisible);
		}

		revise(eObj, eRefObj, checkVisible);
		chart = null;
	}

	protected void beforeUpdate(Chart eObj, Chart eRefObj, boolean checkVisible) {
		// If chart message is not set or is blank string, just set it with null
		// to make sure the default chart message is used while updating chart
		// model with default chart values.
		if (eObj.getEmptyMessage() != null && ChartUtil.isEmpty(eObj.getEmptyMessage().getCaption().getValue())) {
			eObj.getEmptyMessage().getCaption().setValue(null);
		}
	}

	/**
	 * This method revise property values according to limit of different chart
	 * types.
	 *
	 * @param eObj
	 * @param eRefObj
	 */
	protected void revise(Chart eObj, Chart eRefObj, boolean checkVisible) {
		// This hasSeries array indicates if specific series type is contained in chart.
		boolean[] hasSeries = hasSpecificSeries(eObj);
		if (hasSeries[0]) // Value of index 0 indicates Stock series.
		{
			// Stock chart just supports default vertical orientation.
			((ChartWithAxes) eObj).unsetOrientation();
		}
		if (hasSeries[1]) // Value of index 1 indicates gantt series.
		{
			// Gantt chart just supports horizontal orientation.
			((ChartWithAxes) eObj).setOrientation(Orientation.HORIZONTAL_LITERAL);
		}

		// Revise axis title rotation.
		if (eObj != null) {
			if (eObj instanceof ChartWithAxes && eObj.getDimension() != ChartDimension.THREE_DIMENSIONAL_LITERAL) {
				rotateAxesTitle((ChartWithAxes) eObj);
			}
		}
	}

	/**
	 * This method iterate chart series to check if it contains specific series
	 * type.
	 *
	 * @param eObj
	 * @return
	 */
	protected boolean[] hasSpecificSeries(Chart eObj) {
		boolean[] hasSeries = { false, false };
		if (eObj instanceof ChartWithAxes) {
			ChartWithAxes cwa = (ChartWithAxes) eObj;
			for (Axis axis : cwa.getAxes().get(0).getAssociatedAxes()) {
				for (SeriesDefinition sd : axis.getSeriesDefinitions()) {
					for (Series s : sd.getSeries()) {
						// Since it is forbidden that a chart contains both stock
						// series and gantt series, so only one condition is meet,
						// we will return directly.
						if (!hasSeries[0] && s instanceof StockSeries) {
							hasSeries[0] = true;
							return hasSeries;
						} else if (!hasSeries[1] && s instanceof GanttSeries) {
							hasSeries[1] = true;
							return hasSeries;
						}
					}
				}
			}
		}
		return hasSeries;
	}

	protected void rotateAxesTitle(ChartWithAxes cwa) {
		boolean isHorizontal = (cwa.getOrientation() == Orientation.HORIZONTAL_LITERAL);
		Axis aX = cwa.getAxes().get(0);
		if (aX.getTitle().isSetVisible() && aX.getTitle().isVisible()
				&& !aX.getTitle().getCaption().getFont().isSetRotation()) {
			if (isHorizontal) {
				aX.getTitle().getCaption().getFont().setRotation(90);
			} else {
				aX.getTitle().getCaption().getFont().setRotation(0);
			}
		}

		EList<Axis> aYs = aX.getAssociatedAxes();
		for (int i = 0; i < aYs.size(); i++) {

			Axis aY = aYs.get(i);
			if (aY.getTitle().isSetVisible() && aY.getTitle().isVisible()
					&& !aY.getTitle().getCaption().getFont().isSetRotation()) {
				if (isHorizontal) {
					aY.getTitle().getCaption().getFont().setRotation(0);
				} else {
					aY.getTitle().getCaption().getFont().setRotation(90);
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateCurveFitting(
	 * java.lang.String, org.eclipse.emf.ecore.EObject,
	 * org.eclipse.birt.chart.model.component.CurveFitting,
	 * org.eclipse.birt.chart.model.component.CurveFitting,
	 * org.eclipse.birt.chart.model.component.CurveFitting, boolean, boolean)
	 */
	@Override
	public void updateCurveFitting(String name, EObject eParentObj, CurveFitting eObj, CurveFitting eRefObj,
			CurveFitting eDefObj, boolean eDefOverride, boolean checkVisible) {
		// As default, curve fitting is different from other chart element, if
		// current curve fitting is null, just use non-null reference curve
		// fitting to override, not use non-null default curve fitting to
		// override. And for properties in curve fitting, it can use values in
		// default curve fitting to override if those values in current curve
		// fitting are 'auto'

		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				eRefObj = null;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		super.updateCurveFitting(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateMarkerLine(java
	 * .lang.String, org.eclipse.emf.ecore.EObject,
	 * org.eclipse.birt.chart.model.component.MarkerLine,
	 * org.eclipse.birt.chart.model.component.MarkerLine,
	 * org.eclipse.birt.chart.model.component.MarkerLine, boolean, boolean)
	 */
	@Override
	public void updateMarkerLine(String name, EObject eParentObj, MarkerLine eObj, MarkerLine eRefObj,
			MarkerLine eDefObj, boolean eDefOverride, boolean checkVisible) {
		// Same reason with updateCurveFitting method.
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				eRefObj = null;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		super.updateMarkerLine(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.model.util.BaseChartValueUpdater#updateMarkerRange(
	 * java.lang.String, org.eclipse.emf.ecore.EObject,
	 * org.eclipse.birt.chart.model.component.MarkerRange,
	 * org.eclipse.birt.chart.model.component.MarkerRange,
	 * org.eclipse.birt.chart.model.component.MarkerRange, boolean, boolean)
	 */
	@Override
	public void updateMarkerRange(String name, EObject eParentObj, MarkerRange eObj, MarkerRange eRefObj,
			MarkerRange eDefObj, boolean eDefOverride, boolean checkVisible) {
		// Same reason with updateCurveFitting method.
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				eRefObj = null;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		super.updateMarkerRange(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
	}

	/**
	 * Updates chart element DialRegion.
	 *
	 * @param eObj    chart element object.
	 * @param eRefObj reference chart element object.
	 * @param eDefObj default chart element object.
	 *
	 * @generated Don't change this method manually.
	 */
	@Override
	protected void updateDialRegion(String name, EObject eParentObj, DialRegion eObj, DialRegion eRefObj,
			DialRegion eDefObj, boolean eDefOverride, boolean checkVisible) {
		// Same reason with updateCurveFitting method.
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				eRefObj = null;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		super.updateDialRegion(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);

	}

	@Override
	protected void updateMarkerRangeImpl(String name, EObject eParentObj, MarkerRange eObj, MarkerRange eRefObj,
			MarkerRange eDefObj, boolean eDefOverride, boolean checkVisible) {
		// Same reason with updateCurveFitting method.
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				eRefObj = null;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		super.updateMarkerRangeImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
	}

	@Override
	protected void updateTitleBlock(String name, EObject eParentObj, TitleBlock eObj, TitleBlock eRefObj,
			TitleBlock eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj != null && eObj.eContainer() != null && eObj.eContainer().eContainer() instanceof Chart) {
			// It is chart title block, if chart title is null or blank, we use chart type.
			if (ChartUtil.isEmpty(eObj.getLabel().getCaption().getValue())) {
				eObj.getLabel().getCaption().setValue(ChartUtil.getDefaultChartTitle(chart, getULocale()));
			}
		}
		super.updateTitleBlock(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
	}

	@Override
	public void updateDataPointComponent(String name, EObject eParentObj, DataPointComponent eObj,
			DataPointComponent eRefObj, DataPointComponent eDefObj, boolean eDefOverride, boolean checkVisible) {
		super.updateDataPointComponent(name, eParentObj, eObj, eRefObj,
				ChartDefaultValueUtil.getPercentileDataPointDefObj(eObj, eDefObj), eDefOverride, checkVisible);
	}
}
