/*******************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.ui.util;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;

import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.attribute.ChartDimension;
import org.eclipse.birt.chart.model.attribute.LineStyle;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.component.impl.CurveFittingImpl;
import org.eclipse.birt.chart.model.type.AreaSeries;
import org.eclipse.birt.chart.model.type.StockSeries;
import org.eclipse.birt.chart.model.util.ChartElementUtil;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartType;
import org.eclipse.birt.chart.ui.swt.interfaces.IChartUIHelper;
import org.eclipse.birt.chart.ui.swt.wizard.ChartWizardContext;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;

/**
 * This class defines common chart UI methods.
 */

public class ChartUIExtensionUtil {

	public static int PROPERTY_UPDATE = ChartElementUtil.PROPERTY_UPDATE;
	public static int PROPERTY_UNSET = ChartElementUtil.PROPERTY_UNSET;

	/**
	 * Populates series type list.
	 * 
	 * @param htSeriesNames
	 * @param cmbTypes
	 * @param context
	 * @param allChartType
	 * @param currentSeries
	 */
	public static void populateSeriesTypesList(Hashtable<String, Series> htSeriesNames, Combo cmbTypes,
			ChartWizardContext context, Collection<IChartType> allChartType, Series currentSeries) {
		IChartUIHelper helper = context.getUIFactory().createUIHelper();
		IChartType currentChartType = ChartUIUtil.getChartType(context.getModel().getType());

		// Populate Series Types List
		cmbTypes.removeAll();
		if (helper.canCombine(currentChartType, context)) {
			Orientation orientation = ((ChartWithAxes) context.getModel()).getOrientation();
			Iterator<IChartType> iterTypes = allChartType.iterator();
			while (iterTypes.hasNext()) {
				IChartType type = iterTypes.next();
				Series newSeries = type.getSeries(false);

				if (helper.canCombine(type, context)) {
					if (newSeries instanceof AreaSeries
							&& context.getModel().getDimension() == ChartDimension.TWO_DIMENSIONAL_WITH_DEPTH_LITERAL) {
						continue;
					}
					if (!(newSeries instanceof StockSeries) || (orientation.getValue() == Orientation.VERTICAL)) {
						String sDisplayName = newSeries.getDisplayName();
						htSeriesNames.put(sDisplayName, newSeries);
						cmbTypes.add(sDisplayName);
					}

					// Select the same series type
					if (type.getName().equals(context.getModel().getType())) {
						cmbTypes.select(cmbTypes.getItemCount() - 1);
					}
				}
			}
			String sDisplayName = currentSeries.getDisplayName();
			cmbTypes.setText(sDisplayName);
		} else {
			String seriesName = currentSeries.getDisplayName();
			cmbTypes.add(seriesName);
			cmbTypes.select(0);
		}

	}

	/**
	 * Create an instance of CurveFitting according to current context.
	 * 
	 * @param context
	 * @return an instance of CurveFitting
	 */
	public static CurveFitting createCurveFitting(ChartWizardContext context) {
		return context.getUIFactory().supportAutoUI() ? CurveFittingImpl.createDefault() : CurveFittingImpl.create();
	}

	/**
	 * Converts the specified model line style to an appropriate SWT line style
	 * constant
	 */
	public static int getSWTLineStyle(LineStyle style) {
		if (LineStyle.DASHED_LITERAL.equals(style)) {
			return SWT.LINE_DASH;
		} else if (LineStyle.DASH_DOTTED_LITERAL.equals(style)) {
			return SWT.LINE_DASHDOT;
		} else if (LineStyle.DOTTED_LITERAL.equals(style)) {
			return SWT.LINE_DOT;
		} else {
			return SWT.LINE_SOLID;
		}
	}
}
