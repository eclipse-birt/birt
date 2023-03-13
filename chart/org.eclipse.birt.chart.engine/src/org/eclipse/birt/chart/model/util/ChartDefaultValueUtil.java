/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.chart.model.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.Orientation;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.impl.DataPointComponentImpl;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.Legend;
import org.eclipse.birt.chart.model.layout.Plot;
import org.eclipse.birt.chart.model.layout.TitleBlock;
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
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.util.EList;

/**
 * This class provides methods to set default value into chart elements.
 *
 */

public class ChartDefaultValueUtil extends ChartElementUtil {

	/**
	 * Check if current chart use auto series palette.
	 *
	 * @param chart
	 * @return true if series palette is not set.
	 */
	public static boolean isAutoSeriesPalette(Chart chart) {
		SeriesDefinition sd = ChartUtil.getCategorySeriesDefinition(chart);
		return (sd.getSeriesPalette().getEntries().size() == 0);
	}

	/**
	 * Removes all series palettes.
	 *
	 * @param chart
	 */
	public static void removeSerlesPalettes(Chart chart) {
		ChartUtil.getCategorySeriesDefinition(chart).getSeriesPalette().getEntries().clear();
		for (SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions(chart)) {
			sd.getSeriesPalette().getEntries().clear();
		}
	}

	/**
	 * Updates series palettes to default values.
	 *
	 * @param chart
	 */
	public static void updateSeriesPalettes(Chart chart) {
		updateSeriesPalettes(chart, null);
	}

	/**
	 * Updates series palettes to default values.
	 *
	 * @param chart
	 * @param adapters
	 */
	public static void updateSeriesPalettes(Chart chart, Collection<? extends Adapter> adapters) {
		// Set series palette for category series definition.
		ChartUtil.getCategorySeriesDefinition(chart)
				.setSeriesPalette(DefaultValueProvider.defSeriesDefinition(0).getSeriesPalette().copyInstance());
		if (adapters != null) {
			ChartUtil.getCategorySeriesDefinition(chart).getSeriesPalette().eAdapters().addAll(adapters);
		}
		// Set series palettes for value series definitions.
		if (ChartUtil.hasMultipleYAxes(chart)) {
			int axesNum = ChartUtil.getOrthogonalAxisNumber(chart);
			for (int i = 0; i < axesNum; i++) {
				int pos = i;
				SeriesDefinition[] seriesDefns = ChartUtil.getOrthogonalSeriesDefinitions(chart, i)
						.toArray(new SeriesDefinition[] {});
				for (int j = 0; j < seriesDefns.length; j++) {
					pos += j;
					seriesDefns[j].setSeriesPalette(
							DefaultValueProvider.defSeriesDefinition(pos).getSeriesPalette().copyInstance());
					if (adapters != null) {
						seriesDefns[j].getSeriesPalette().eAdapters().addAll(adapters);
					}
				}
			}
		} else {
			int i = 0;
			for (SeriesDefinition sd : ChartUtil.getValueSeriesDefinitions(chart)) {
				sd.setSeriesPalette(DefaultValueProvider.defSeriesDefinition(i).getSeriesPalette().copyInstance());
				if (adapters != null) {
					sd.getSeriesPalette().eAdapters().addAll(adapters);
				}
				i++;
			}
		}
	}

	/**
	 * Returns default values of specified series object.
	 *
	 * @param runtimeSeries specified series object.
	 * @return series object with default value.
	 */
	public static Series getDefaultSeries(Series runtimeSeries) {
		if (runtimeSeries instanceof BarSeries) {
			return DefaultValueProvider.defBarSeries();
		} else if (runtimeSeries instanceof BubbleSeries) {
			return DefaultValueProvider.defBubbleSeries();
		} else if (runtimeSeries instanceof ScatterSeries) {
			return DefaultValueProvider.defScatterSeries();
		} else if (runtimeSeries instanceof DifferenceSeries) {
			return DefaultValueProvider.defDifferenceSeries();
		} else if (runtimeSeries instanceof AreaSeries) {
			return DefaultValueProvider.defAreaSeries();
		} else if (runtimeSeries instanceof LineSeries) {
			return DefaultValueProvider.defLineSeries();
		} else if (runtimeSeries instanceof GanttSeries) {
			return DefaultValueProvider.defGanttSeries();
		} else if (runtimeSeries instanceof DialSeries) {
			return DefaultValueProvider.defDialSeries();
		} else if (runtimeSeries instanceof PieSeries) {
			return DefaultValueProvider.defPieSeries();
		} else if (runtimeSeries instanceof StockSeries) {
			return DefaultValueProvider.defStockSeries();
		} else if (ChartDynamicExtension.isExtended(runtimeSeries)) {
			return (Series) new ChartExtensionValueUpdater().getDefault(ComponentPackage.eINSTANCE.getSeries(),
					"series", //$NON-NLS-1$
					runtimeSeries);
		}
		return null;
	}

	/**
	 * Returns default value chart instance according to specified chart instance.
	 *
	 * @param cm
	 * @return default value chart instance according to specified chart instance.
	 */
	public static Chart getDefaultValueChart(Chart cm) {
		Chart instance = null;
		if (cm instanceof DialChart) {
			instance = DefaultValueProvider.defDialChart();
		} else if (cm instanceof ChartWithoutAxes) {
			instance = DefaultValueProvider.defChartWithoutAxes();
		} else {
			instance = DefaultValueProvider.defChartWithAxes();
		}

		return instance;
	}

	/**
	 * Returns default chart orientation according to specified chart instance.
	 *
	 * @param cm
	 * @return default orientation of chart according to specified chart instance.
	 */
	public static Orientation getDefaultOrientation(Chart cm) {
		Chart chart = getDefaultValueChart(cm);
		if (chart instanceof ChartWithAxes) {
			((ChartWithAxes) chart).getOrientation();
		}
		return null;
	}

	/**
	 * Returns default chart block according specified chart instance.
	 *
	 * @param cm
	 * @return default chart block according specified chart instance.
	 */
	public static Block getDefaultBlock(Chart cm) {
		return ChartDefaultValueUtil.getDefaultValueChart(cm).getBlock();
	}

	/**
	 * Returns default chart legend according specified chart instance.
	 *
	 * @param cm
	 * @return default chart legend according specified chart instance.
	 */
	public static Legend getDefaultLegend(Chart cm) {
		return ChartDefaultValueUtil.getDefaultValueChart(cm).getLegend();
	}

	/**
	 * Returns default chart plot according specified chart instance.
	 *
	 * @param cm
	 * @return default chart plot according specified chart instance.
	 */
	public static Plot getDefaultPlot(Chart cm) {
		return ChartDefaultValueUtil.getDefaultValueChart(cm).getPlot();
	}

	/**
	 * Returns default chart title block according to specified chart instance.
	 *
	 * @param cm
	 * @return default chart title block according to specified chart instance.
	 */
	public static TitleBlock getDefaultTitle(Chart cm) {
		return ChartDefaultValueUtil.getDefaultValueChart(cm).getTitle();
	}

	/**
	 * Creates instance of default value chart according to specified chart type.
	 *
	 * @param cm
	 * @return chart instance with default values.
	 */
	public static Chart createDefaultValueChartInstance(Chart cm) {
		Chart instance = getDefaultValueChart(cm).copyInstance();

		// Add all different series instances.
		SeriesDefinition sd = ChartUtil.getOrthogonalSeriesDefinitions(instance, 0).get(0);
		List<Series> seriesList = sd.getSeries();
		seriesList.clear();
		if (instance instanceof ChartWithAxes) {
			seriesList.add(DefaultValueProvider.defBarSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defBubbleSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defScatterSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defDifferenceSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defAreaSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defLineSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defGanttSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defStockSeries().copyInstance());
		} else {
			seriesList.add(DefaultValueProvider.defDialSeries().copyInstance());
			seriesList.add(DefaultValueProvider.defPieSeries().copyInstance());
		}

		// Get remaining default series objects.
		Set<String> seriesNameSet = new HashSet<>();
		for (Series s : seriesList) {
			seriesNameSet.add(s.getClass().getName());
		}
		Set<Series> dtSeries = new HashSet<>();
		for (SeriesDefinition sdef : ChartUtil.getAllOrthogonalSeriesDefinitions(cm)) {
			for (Series s : sdef.getSeries()) {
				dtSeries.add(s);
			}
		}
		for (Iterator<Series> iter = dtSeries.iterator(); iter.hasNext();) {
			Series s = iter.next();
			if (!seriesNameSet.contains(s.getClass().getName())) {
				seriesList.add(getDefaultSeries(s));
			}
		}

		// Set default chart title according to chart type.
		instance.getTitle().getLabel().getCaption().setValue(ChartUtil.getDefaultChartTitle(cm));

		return instance;
	}

	/**
	 *
	 * Shifts the colors in palette with the offset.
	 *
	 * @param offset moving offset to rotate the color. If the offset is zero or the
	 *               absolute value is greater than the size of list, do nothing.
	 *               Negative value means moving to the left side, and positive
	 *               value is to the right side.
	 */
	public static void shiftPaletteColors(Palette p, int offset) {
		if (p.getEntries().size() == 0) {
			p.shift(offset);
			return;
		}

		final EList<Fill> el = p.getEntries();
		int size = el.size();

		if (offset == 0 || Math.abs(offset) >= size) {
			// Do nothing
			offset = 0;
			return;
		}

		List<Fill> colorList = new ArrayList<>(el);
		el.clear();
		if (offset < 0) {
			// Move to the left side
			offset = -offset;
		} else if (offset > 0) {
			// Move to the right side
			offset = size - offset;
		}

		for (int i = offset; i < size; i++) {
			el.add((colorList.get(i)));
		}
		for (int i = 0; i < offset; i++) {
			el.add((colorList.get(i)));
		}
	}

	/**
	 * Returns default units value of chart.
	 *
	 * @param cm reference chart model.
	 * @return default units value of chart.
	 */
	public static String getDefaultUnits(Chart cm) {
		if (cm instanceof ChartWithAxes) {
			return DefaultValueProvider.defChartWithAxes().getUnits();
		} else if (cm instanceof DialChart) {
			return DefaultValueProvider.defDialChart().getUnits();
		} else if (cm instanceof ChartWithoutAxes) {
			return DefaultValueProvider.defChartWithoutAxes().getUnits();
		}

		return null;
	}

	/**
	 * Returns the default DataPointComponent of Percentile Value Literal with
	 * according formatter.
	 *
	 * @param eObj
	 * @param eDefObj
	 * @return default DataPointComponent of Percentile Value Literal
	 */
	public static DataPointComponent getPercentileDataPointDefObj(DataPointComponent eObj, DataPointComponent eDefObj) {
		// Set default format for percentile value.
		DataPointComponent eTmpDefObj = eDefObj;
		if (eObj != null && eObj.getType() == DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL
				&& (eDefObj == null || eDefObj.getFormatSpecifier() == null)) {
			if (eDefObj == null) {
				eTmpDefObj = DataPointComponentImpl.create(DataPointComponentType.PERCENTILE_ORTHOGONAL_VALUE_LITERAL,
						DefaultValueProvider.defPercentileValueFormatSpecifier());
			} else if (eDefObj.getFormatSpecifier() == null) {
				eTmpDefObj = eDefObj.copyInstance();
				eTmpDefObj.setFormatSpecifier(DefaultValueProvider.defPercentileValueFormatSpecifier());
			}
		}
		return eTmpDefObj;
	}
}
