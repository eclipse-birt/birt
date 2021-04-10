/***********************************************************************
 * Copyright (c) 2013 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.model.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.chart.model.Chart;
import org.eclipse.birt.chart.model.ChartWithAxes;
import org.eclipse.birt.chart.model.ChartWithoutAxes;
import org.eclipse.birt.chart.model.DialChart;
import org.eclipse.birt.chart.model.IChartObject;
import org.eclipse.birt.chart.model.attribute.Angle3D;
import org.eclipse.birt.chart.model.attribute.AxisOrigin;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.ColorDefinition;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.DataPoint;
import org.eclipse.birt.chart.model.attribute.DataPointComponent;
import org.eclipse.birt.chart.model.attribute.DataPointComponentType;
import org.eclipse.birt.chart.model.attribute.EmbeddedImage;
import org.eclipse.birt.chart.model.attribute.ExtendedProperty;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.FontDefinition;
import org.eclipse.birt.chart.model.attribute.FormatSpecifier;
import org.eclipse.birt.chart.model.attribute.Gradient;
import org.eclipse.birt.chart.model.attribute.Image;
import org.eclipse.birt.chart.model.attribute.Insets;
import org.eclipse.birt.chart.model.attribute.Interactivity;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.attribute.Location3D;
import org.eclipse.birt.chart.model.attribute.Marker;
import org.eclipse.birt.chart.model.attribute.MultipleFill;
import org.eclipse.birt.chart.model.attribute.Palette;
import org.eclipse.birt.chart.model.attribute.PatternImage;
import org.eclipse.birt.chart.model.attribute.Rotation3D;
import org.eclipse.birt.chart.model.attribute.Size;
import org.eclipse.birt.chart.model.attribute.Style;
import org.eclipse.birt.chart.model.attribute.Text;
import org.eclipse.birt.chart.model.attribute.TextAlignment;
import org.eclipse.birt.chart.model.component.Axis;
import org.eclipse.birt.chart.model.component.ComponentPackage;
import org.eclipse.birt.chart.model.component.CurveFitting;
import org.eclipse.birt.chart.model.component.Dial;
import org.eclipse.birt.chart.model.component.DialRegion;
import org.eclipse.birt.chart.model.component.Grid;
import org.eclipse.birt.chart.model.component.Label;
import org.eclipse.birt.chart.model.component.MarkerLine;
import org.eclipse.birt.chart.model.component.MarkerRange;
import org.eclipse.birt.chart.model.component.Needle;
import org.eclipse.birt.chart.model.component.Scale;
import org.eclipse.birt.chart.model.component.Series;
import org.eclipse.birt.chart.model.data.SeriesDefinition;
import org.eclipse.birt.chart.model.layout.Block;
import org.eclipse.birt.chart.model.layout.ClientArea;
import org.eclipse.birt.chart.model.layout.LabelBlock;
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
import org.eclipse.emf.ecore.EObject;

/**
 * This class is auto generated code, don't edit it manually. This class
 * provides function to update chart element values with reference chart object
 * or default chart values, if current chart element isn't set or is null, then
 * use reference chart object to replace if the reference chart object is set or
 * not null, otherwise use default chart value to replace if it is set.
 * 
 * @generated
 */

public class BaseChartValueUpdater {

	protected ChartExtensionValueUpdater extUpdater = new ChartExtensionValueUpdater();

	/**
	 * Updates chart object.
	 *
	 * @param eObj    chart element object.
	 * @param eRefObj reference chart element object.
	 *
	 * @generated
	 */
	public void update(Chart eObj, Chart eRefObj) {
		if (eObj != null) {
			updateChart(eObj.eClass().getName(), null, eObj, eRefObj, true, true);
		}
	}

	/**
	 * Updates chart element Chart.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateChart(String name, EObject eParentObj, Chart eObj, Chart eRefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj == null) {
			return;
		}
		if (eObj instanceof DialChart) {
			updateDialChart(name, eParentObj, (DialChart) eObj, (DialChart) eRefObj,
					DefaultValueProvider.defDialChart(), eDefOverride, checkVisible);
		} else if (eObj instanceof ChartWithAxes) {
			updateChartWithAxes(name, eParentObj, (ChartWithAxes) eObj, (ChartWithAxes) eRefObj,
					DefaultValueProvider.defChartWithAxes(), eDefOverride, checkVisible);
		} else if (eObj instanceof ChartWithoutAxes) {
			updateChartWithoutAxes(name, eParentObj, (ChartWithoutAxes) eObj, (ChartWithoutAxes) eRefObj,
					DefaultValueProvider.defChartWithoutAxes(), eDefOverride, checkVisible);
		} else {
			updateChartImpl(name, eParentObj, eObj, eRefObj, null, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element Chart.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateChartImpl(String name, EObject eParentObj, Chart eObj, Chart eRefObj, Chart eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getType() == null) {
			if (eRefObj != null && eRefObj.getType() != null) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.getType() != null) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getSubType() == null) {
			if (eRefObj != null && eRefObj.getSubType() != null) {
				eObj.setSubType(eRefObj.getSubType());
			} else if (eDefObj != null && eDefObj.getSubType() != null) {
				eObj.setSubType(eDefObj.getSubType());
			}
		}

		if (!eObj.isSetDimension()) {
			if (eRefObj != null && eRefObj.isSetDimension()) {
				eObj.setDimension(eRefObj.getDimension());
			} else if (eDefObj != null && eDefObj.isSetDimension()) {
				eObj.setDimension(eDefObj.getDimension());
			}
		}

		if (eObj.getUnits() == null) {
			if (eRefObj != null && eRefObj.getUnits() != null) {
				eObj.setUnits(eRefObj.getUnits());
			} else if (eDefObj != null && eDefObj.getUnits() != null) {
				eObj.setUnits(eDefObj.getUnits());
			}
		}

		if (!eObj.isSetSeriesThickness()) {
			if (eRefObj != null && eRefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eRefObj.getSeriesThickness());
			} else if (eDefObj != null && eDefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eDefObj.getSeriesThickness());
			}
		}

		if (!eObj.isSetGridColumnCount()) {
			if (eRefObj != null && eRefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eRefObj.getGridColumnCount());
			} else if (eDefObj != null && eDefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eDefObj.getGridColumnCount());
			}
		}

		// list attributes

		// references
		updateBlock("block", eObj, eObj.getBlock(), eRefObj == null ? null : eRefObj.getBlock(),
				eDefObj == null ? null : eDefObj.getBlock(), eDefOverride, checkVisible);
		if (eObj.getExtendedProperties().size() == 0) {
			if (eRefObj != null && eRefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eRefObj.getExtendedProperties()));
			} else if (eDefObj != null && eDefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eDefObj.getExtendedProperties()));
			}
		}
		updateInteractivity("interactivity", eObj, eObj.getInteractivity(),
				eRefObj == null ? null : eRefObj.getInteractivity(),
				eDefObj == null ? null : eDefObj.getInteractivity(), eDefOverride, checkVisible);
		updateLabel("emptyMessage", eObj, eObj.getEmptyMessage(), eRefObj == null ? null : eRefObj.getEmptyMessage(),
				eDefObj == null ? null : eDefObj.getEmptyMessage(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element ChartWithAxes.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateChartWithAxes(String name, EObject eParentObj, ChartWithAxes eObj, ChartWithAxes eRefObj,
			ChartWithAxes eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetVersion()) {
			if (eRefObj != null && eRefObj.isSetVersion()) {
				eObj.setVersion(eRefObj.getVersion());
			} else if (eDefObj != null && eDefObj.isSetVersion()) {
				eObj.setVersion(eDefObj.getVersion());
			}
		}

		if (eObj.getType() == null) {
			if (eRefObj != null && eRefObj.getType() != null) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.getType() != null) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getSubType() == null) {
			if (eRefObj != null && eRefObj.getSubType() != null) {
				eObj.setSubType(eRefObj.getSubType());
			} else if (eDefObj != null && eDefObj.getSubType() != null) {
				eObj.setSubType(eDefObj.getSubType());
			}
		}

		if (!eObj.isSetDimension()) {
			if (eRefObj != null && eRefObj.isSetDimension()) {
				eObj.setDimension(eRefObj.getDimension());
			} else if (eDefObj != null && eDefObj.isSetDimension()) {
				eObj.setDimension(eDefObj.getDimension());
			}
		}

		if (eObj.getScript() == null) {
			if (eRefObj != null && eRefObj.getScript() != null) {
				eObj.setScript(eRefObj.getScript());
			} else if (eDefObj != null && eDefObj.getScript() != null) {
				eObj.setScript(eDefObj.getScript());
			}
		}

		if (eObj.getUnits() == null) {
			if (eRefObj != null && eRefObj.getUnits() != null) {
				eObj.setUnits(eRefObj.getUnits());
			} else if (eDefObj != null && eDefObj.getUnits() != null) {
				eObj.setUnits(eDefObj.getUnits());
			}
		}

		if (!eObj.isSetSeriesThickness()) {
			if (eRefObj != null && eRefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eRefObj.getSeriesThickness());
			} else if (eDefObj != null && eDefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eDefObj.getSeriesThickness());
			}
		}

		if (!eObj.isSetGridColumnCount()) {
			if (eRefObj != null && eRefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eRefObj.getGridColumnCount());
			} else if (eDefObj != null && eDefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eDefObj.getGridColumnCount());
			}
		}

		if (!eObj.isSetOrientation()) {
			if (eRefObj != null && eRefObj.isSetOrientation()) {
				eObj.setOrientation(eRefObj.getOrientation());
			} else if (eDefObj != null && eDefObj.isSetOrientation()) {
				eObj.setOrientation(eDefObj.getOrientation());
			}
		}

		if (!eObj.isSetUnitSpacing()) {
			if (eRefObj != null && eRefObj.isSetUnitSpacing()) {
				eObj.setUnitSpacing(eRefObj.getUnitSpacing());
			} else if (eDefObj != null && eDefObj.isSetUnitSpacing()) {
				eObj.setUnitSpacing(eDefObj.getUnitSpacing());
			}
		}

		if (!eObj.isSetReverseCategory()) {
			if (eRefObj != null && eRefObj.isSetReverseCategory()) {
				eObj.setReverseCategory(eRefObj.isReverseCategory());
			} else if (eDefObj != null && eDefObj.isSetReverseCategory()) {
				eObj.setReverseCategory(eDefObj.isReverseCategory());
			}
		}

		if (!eObj.isSetStudyLayout()) {
			if (eRefObj != null && eRefObj.isSetStudyLayout()) {
				eObj.setStudyLayout(eRefObj.isStudyLayout());
			} else if (eDefObj != null && eDefObj.isSetStudyLayout()) {
				eObj.setStudyLayout(eDefObj.isStudyLayout());
			}
		}

		// list attributes

		// references
		updateText("description", eObj, eObj.getDescription(), eRefObj == null ? null : eRefObj.getDescription(),
				eDefObj == null ? null : eDefObj.getDescription(), eDefOverride, checkVisible);
		updateBlock("block", eObj, eObj.getBlock(), eRefObj == null ? null : eRefObj.getBlock(),
				eDefObj == null ? null : eDefObj.getBlock(), eDefOverride, checkVisible);
		if (eObj.getExtendedProperties().size() == 0) {
			if (eRefObj != null && eRefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eRefObj.getExtendedProperties()));
			} else if (eDefObj != null && eDefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eDefObj.getExtendedProperties()));
			}
		}
		updateInteractivity("interactivity", eObj, eObj.getInteractivity(),
				eRefObj == null ? null : eRefObj.getInteractivity(),
				eDefObj == null ? null : eDefObj.getInteractivity(), eDefOverride, checkVisible);
		updateLabel("emptyMessage", eObj, eObj.getEmptyMessage(), eRefObj == null ? null : eRefObj.getEmptyMessage(),
				eDefObj == null ? null : eDefObj.getEmptyMessage(), eDefOverride, checkVisible);
		for (Axis element : eObj.getAxes()) {
			updateAxis("axes", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getAxes().size() > 0) ? eRefObj.getAxes().get(0) : null,
					DefaultValueProvider.defBaseAxis(), eDefOverride, checkVisible, 0);
		}

		updateFill("wallFill", eObj, eObj.getWallFill(), eRefObj == null ? null : eRefObj.getWallFill(),
				eDefObj == null ? null : eDefObj.getWallFill(), eDefOverride, checkVisible);
		updateFill("floorFill", eObj, eObj.getFloorFill(), eRefObj == null ? null : eRefObj.getFloorFill(),
				eDefObj == null ? null : eDefObj.getFloorFill(), eDefOverride, checkVisible);
		updateRotation3D("rotation", eObj, eObj.getRotation(), eRefObj == null ? null : eRefObj.getRotation(),
				eDefObj == null ? null : eDefObj.getRotation(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element ChartWithoutAxes.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateChartWithoutAxes(String name, EObject eParentObj, ChartWithoutAxes eObj, ChartWithoutAxes eRefObj,
			ChartWithoutAxes eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eObj instanceof DialChart) {
			updateDialChart(name, eParentObj, (DialChart) eObj, (DialChart) eRefObj,
					DefaultValueProvider.defDialChart(), eDefOverride, checkVisible);
		} else {
			updateChartWithoutAxesImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element ChartWithoutAxes.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateChartWithoutAxesImpl(String name, EObject eParentObj, ChartWithoutAxes eObj,
			ChartWithoutAxes eRefObj, ChartWithoutAxes eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetVersion()) {
			if (eRefObj != null && eRefObj.isSetVersion()) {
				eObj.setVersion(eRefObj.getVersion());
			} else if (eDefObj != null && eDefObj.isSetVersion()) {
				eObj.setVersion(eDefObj.getVersion());
			}
		}

		if (eObj.getType() == null) {
			if (eRefObj != null && eRefObj.getType() != null) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.getType() != null) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getSubType() == null) {
			if (eRefObj != null && eRefObj.getSubType() != null) {
				eObj.setSubType(eRefObj.getSubType());
			} else if (eDefObj != null && eDefObj.getSubType() != null) {
				eObj.setSubType(eDefObj.getSubType());
			}
		}

		if (!eObj.isSetDimension()) {
			if (eRefObj != null && eRefObj.isSetDimension()) {
				eObj.setDimension(eRefObj.getDimension());
			} else if (eDefObj != null && eDefObj.isSetDimension()) {
				eObj.setDimension(eDefObj.getDimension());
			}
		}

		if (eObj.getScript() == null) {
			if (eRefObj != null && eRefObj.getScript() != null) {
				eObj.setScript(eRefObj.getScript());
			} else if (eDefObj != null && eDefObj.getScript() != null) {
				eObj.setScript(eDefObj.getScript());
			}
		}

		if (eObj.getUnits() == null) {
			if (eRefObj != null && eRefObj.getUnits() != null) {
				eObj.setUnits(eRefObj.getUnits());
			} else if (eDefObj != null && eDefObj.getUnits() != null) {
				eObj.setUnits(eDefObj.getUnits());
			}
		}

		if (!eObj.isSetSeriesThickness()) {
			if (eRefObj != null && eRefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eRefObj.getSeriesThickness());
			} else if (eDefObj != null && eDefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eDefObj.getSeriesThickness());
			}
		}

		if (!eObj.isSetGridColumnCount()) {
			if (eRefObj != null && eRefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eRefObj.getGridColumnCount());
			} else if (eDefObj != null && eDefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eDefObj.getGridColumnCount());
			}
		}

		if (!eObj.isSetMinSlice()) {
			if (eRefObj != null && eRefObj.isSetMinSlice()) {
				eObj.setMinSlice(eRefObj.getMinSlice());
			} else if (eDefObj != null && eDefObj.isSetMinSlice()) {
				eObj.setMinSlice(eDefObj.getMinSlice());
			}
		}

		if (!eObj.isSetMinSlicePercent()) {
			if (eRefObj != null && eRefObj.isSetMinSlicePercent()) {
				eObj.setMinSlicePercent(eRefObj.isMinSlicePercent());
			} else if (eDefObj != null && eDefObj.isSetMinSlicePercent()) {
				eObj.setMinSlicePercent(eDefObj.isMinSlicePercent());
			}
		}

		if (eObj.getMinSliceLabel() == null) {
			if (eRefObj != null && eRefObj.getMinSliceLabel() != null) {
				eObj.setMinSliceLabel(eRefObj.getMinSliceLabel());
			} else if (eDefObj != null && eDefObj.getMinSliceLabel() != null) {
				eObj.setMinSliceLabel(eDefObj.getMinSliceLabel());
			}
		}

		if (!eObj.isSetCoverage()) {
			if (eRefObj != null && eRefObj.isSetCoverage()) {
				eObj.setCoverage(eRefObj.getCoverage());
			} else if (eDefObj != null && eDefObj.isSetCoverage()) {
				eObj.setCoverage(eDefObj.getCoverage());
			}
		}

		// list attributes

		// references
		updateText("description", eObj, eObj.getDescription(), eRefObj == null ? null : eRefObj.getDescription(),
				eDefObj == null ? null : eDefObj.getDescription(), eDefOverride, checkVisible);
		updateBlock("block", eObj, eObj.getBlock(), eRefObj == null ? null : eRefObj.getBlock(),
				eDefObj == null ? null : eDefObj.getBlock(), eDefOverride, checkVisible);
		if (eObj.getExtendedProperties().size() == 0) {
			if (eRefObj != null && eRefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eRefObj.getExtendedProperties()));
			} else if (eDefObj != null && eDefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eDefObj.getExtendedProperties()));
			}
		}
		updateInteractivity("interactivity", eObj, eObj.getInteractivity(),
				eRefObj == null ? null : eRefObj.getInteractivity(),
				eDefObj == null ? null : eDefObj.getInteractivity(), eDefOverride, checkVisible);
		updateLabel("emptyMessage", eObj, eObj.getEmptyMessage(), eRefObj == null ? null : eRefObj.getEmptyMessage(),
				eDefObj == null ? null : eDefObj.getEmptyMessage(), eDefOverride, checkVisible);
		int seriesDefIndex = 0;
		for (SeriesDefinition element : eObj.getSeriesDefinitions()) {
			updateSeriesDefinition("seriesDefinitions", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getSeriesDefinitions().size() > 0)
							? eRefObj.getSeriesDefinitions().get(0)
							: null,
					DefaultValueProvider.defSeriesDefinition(seriesDefIndex++), eDefOverride, checkVisible, 0, 0);
		}

	}

	/**
	 * Updates chart element DialChart.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateDialChart(String name, EObject eParentObj, DialChart eObj, DialChart eRefObj,
			DialChart eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetVersion()) {
			if (eRefObj != null && eRefObj.isSetVersion()) {
				eObj.setVersion(eRefObj.getVersion());
			} else if (eDefObj != null && eDefObj.isSetVersion()) {
				eObj.setVersion(eDefObj.getVersion());
			}
		}

		if (eObj.getType() == null) {
			if (eRefObj != null && eRefObj.getType() != null) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.getType() != null) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getSubType() == null) {
			if (eRefObj != null && eRefObj.getSubType() != null) {
				eObj.setSubType(eRefObj.getSubType());
			} else if (eDefObj != null && eDefObj.getSubType() != null) {
				eObj.setSubType(eDefObj.getSubType());
			}
		}

		if (!eObj.isSetDimension()) {
			if (eRefObj != null && eRefObj.isSetDimension()) {
				eObj.setDimension(eRefObj.getDimension());
			} else if (eDefObj != null && eDefObj.isSetDimension()) {
				eObj.setDimension(eDefObj.getDimension());
			}
		}

		if (eObj.getScript() == null) {
			if (eRefObj != null && eRefObj.getScript() != null) {
				eObj.setScript(eRefObj.getScript());
			} else if (eDefObj != null && eDefObj.getScript() != null) {
				eObj.setScript(eDefObj.getScript());
			}
		}

		if (eObj.getUnits() == null) {
			if (eRefObj != null && eRefObj.getUnits() != null) {
				eObj.setUnits(eRefObj.getUnits());
			} else if (eDefObj != null && eDefObj.getUnits() != null) {
				eObj.setUnits(eDefObj.getUnits());
			}
		}

		if (!eObj.isSetSeriesThickness()) {
			if (eRefObj != null && eRefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eRefObj.getSeriesThickness());
			} else if (eDefObj != null && eDefObj.isSetSeriesThickness()) {
				eObj.setSeriesThickness(eDefObj.getSeriesThickness());
			}
		}

		if (!eObj.isSetGridColumnCount()) {
			if (eRefObj != null && eRefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eRefObj.getGridColumnCount());
			} else if (eDefObj != null && eDefObj.isSetGridColumnCount()) {
				eObj.setGridColumnCount(eDefObj.getGridColumnCount());
			}
		}

		if (!eObj.isSetMinSlice()) {
			if (eRefObj != null && eRefObj.isSetMinSlice()) {
				eObj.setMinSlice(eRefObj.getMinSlice());
			} else if (eDefObj != null && eDefObj.isSetMinSlice()) {
				eObj.setMinSlice(eDefObj.getMinSlice());
			}
		}

		if (!eObj.isSetMinSlicePercent()) {
			if (eRefObj != null && eRefObj.isSetMinSlicePercent()) {
				eObj.setMinSlicePercent(eRefObj.isMinSlicePercent());
			} else if (eDefObj != null && eDefObj.isSetMinSlicePercent()) {
				eObj.setMinSlicePercent(eDefObj.isMinSlicePercent());
			}
		}

		if (eObj.getMinSliceLabel() == null) {
			if (eRefObj != null && eRefObj.getMinSliceLabel() != null) {
				eObj.setMinSliceLabel(eRefObj.getMinSliceLabel());
			} else if (eDefObj != null && eDefObj.getMinSliceLabel() != null) {
				eObj.setMinSliceLabel(eDefObj.getMinSliceLabel());
			}
		}

		if (!eObj.isSetCoverage()) {
			if (eRefObj != null && eRefObj.isSetCoverage()) {
				eObj.setCoverage(eRefObj.getCoverage());
			} else if (eDefObj != null && eDefObj.isSetCoverage()) {
				eObj.setCoverage(eDefObj.getCoverage());
			}
		}

		if (!eObj.isSetDialSuperimposition()) {
			if (eRefObj != null && eRefObj.isSetDialSuperimposition()) {
				eObj.setDialSuperimposition(eRefObj.isDialSuperimposition());
			} else if (eDefObj != null && eDefObj.isSetDialSuperimposition()) {
				eObj.setDialSuperimposition(eDefObj.isDialSuperimposition());
			}
		}

		// list attributes

		// references
		updateText("description", eObj, eObj.getDescription(), eRefObj == null ? null : eRefObj.getDescription(),
				eDefObj == null ? null : eDefObj.getDescription(), eDefOverride, checkVisible);
		updateBlock("block", eObj, eObj.getBlock(), eRefObj == null ? null : eRefObj.getBlock(),
				eDefObj == null ? null : eDefObj.getBlock(), eDefOverride, checkVisible);
		if (eObj.getExtendedProperties().size() == 0) {
			if (eRefObj != null && eRefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eRefObj.getExtendedProperties()));
			} else if (eDefObj != null && eDefObj.getExtendedProperties().size() > 0) {
				eObj.getExtendedProperties().addAll(ChartElementUtil.copyInstance(eDefObj.getExtendedProperties()));
			}
		}
		updateInteractivity("interactivity", eObj, eObj.getInteractivity(),
				eRefObj == null ? null : eRefObj.getInteractivity(),
				eDefObj == null ? null : eDefObj.getInteractivity(), eDefOverride, checkVisible);
		updateLabel("emptyMessage", eObj, eObj.getEmptyMessage(), eRefObj == null ? null : eRefObj.getEmptyMessage(),
				eDefObj == null ? null : eDefObj.getEmptyMessage(), eDefOverride, checkVisible);
		int seriesDefIndex = 0;
		for (SeriesDefinition element : eObj.getSeriesDefinitions()) {
			updateSeriesDefinition("seriesDefinitions", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getSeriesDefinitions().size() > 0)
							? eRefObj.getSeriesDefinitions().get(0)
							: null,
					DefaultValueProvider.defSeriesDefinition(seriesDefIndex++), eDefOverride, checkVisible, 0, 0);
		}

	}

	/**
	 * Updates chart element AreaSeries.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateAreaSeries(String name, EObject eParentObj, AreaSeries eObj, AreaSeries eRefObj,
			AreaSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		if (eObj instanceof DifferenceSeries) {
			updateDifferenceSeries(name, eParentObj, (DifferenceSeries) eObj, (DifferenceSeries) eRefObj,
					DefaultValueProvider.defDifferenceSeries(), eDefOverride, checkVisible);
		} else {
			updateAreaSeriesImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element AreaSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateAreaSeriesImpl(String name, EObject eParentObj, AreaSeries eObj, AreaSeries eRefObj,
			AreaSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		if (!eObj.isSetCurve()) {
			if (eRefObj != null && eRefObj.isSetCurve()) {
				eObj.setCurve(eRefObj.isCurve());
			} else if (eDefObj != null && eDefObj.isSetCurve()) {
				eObj.setCurve(eDefObj.isCurve());
			}
		}

		if (!eObj.isSetConnectMissingValue()) {
			if (eRefObj != null && eRefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eRefObj.isConnectMissingValue());
			} else if (eDefObj != null && eDefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eDefObj.isConnectMissingValue());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		int index_AreaSeries_markers = 0;
		for (Marker element : eObj.getMarkers()) {
			updateMarker("markers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markers", index_AreaSeries_markers, element),
					getValidIndexRef(eDefObj, "markers", index_AreaSeries_markers, element), eDefOverride,
					checkVisible);
			index_AreaSeries_markers++;
		}

		updateMarker("marker", eObj, eObj.getMarker(), eRefObj == null ? null : eRefObj.getMarker(),
				eDefObj == null ? null : eDefObj.getMarker(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element BarSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateBarSeries(String name, EObject eParentObj, BarSeries eObj, BarSeries eRefObj,
			BarSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetRiser()) {
			if (eRefObj != null && eRefObj.isSetRiser()) {
				eObj.setRiser(eRefObj.getRiser());
			} else if (eDefObj != null && eDefObj.isSetRiser()) {
				eObj.setRiser(eDefObj.getRiser());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateColorDefinition("riserOutline", eObj, eObj.getRiserOutline(),
				eRefObj == null ? null : eRefObj.getRiserOutline(), eDefObj == null ? null : eDefObj.getRiserOutline(),
				eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element BubbleSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateBubbleSeries(String name, EObject eParentObj, BubbleSeries eObj, BubbleSeries eRefObj,
			BubbleSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		if (!eObj.isSetCurve()) {
			if (eRefObj != null && eRefObj.isSetCurve()) {
				eObj.setCurve(eRefObj.isCurve());
			} else if (eDefObj != null && eDefObj.isSetCurve()) {
				eObj.setCurve(eDefObj.isCurve());
			}
		}

		if (!eObj.isSetConnectMissingValue()) {
			if (eRefObj != null && eRefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eRefObj.isConnectMissingValue());
			} else if (eDefObj != null && eDefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eDefObj.isConnectMissingValue());
			}
		}

		if (!eObj.isSetAccOrientation()) {
			if (eRefObj != null && eRefObj.isSetAccOrientation()) {
				eObj.setAccOrientation(eRefObj.getAccOrientation());
			} else if (eDefObj != null && eDefObj.isSetAccOrientation()) {
				eObj.setAccOrientation(eDefObj.getAccOrientation());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		int index_BubbleSeries_markers = 0;
		for (Marker element : eObj.getMarkers()) {
			updateMarker("markers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markers", index_BubbleSeries_markers, element),
					getValidIndexRef(eDefObj, "markers", index_BubbleSeries_markers, element), eDefOverride,
					checkVisible);
			index_BubbleSeries_markers++;
		}

		updateMarker("marker", eObj, eObj.getMarker(), eRefObj == null ? null : eRefObj.getMarker(),
				eDefObj == null ? null : eDefObj.getMarker(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);
		updateLineAttributes("accLineAttributes", eObj, eObj.getAccLineAttributes(),
				eRefObj == null ? null : eRefObj.getAccLineAttributes(),
				eDefObj == null ? null : eDefObj.getAccLineAttributes(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element DialSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateDialSeries(String name, EObject eParentObj, DialSeries eObj, DialSeries eRefObj,
			DialSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateDial("dial", eObj, eObj.getDial(), eRefObj == null ? null : eRefObj.getDial(),
				eDefObj == null ? null : eDefObj.getDial(), eDefOverride, checkVisible);
		updateNeedle("needle", eObj, eObj.getNeedle(), eRefObj == null ? null : eRefObj.getNeedle(),
				eDefObj == null ? null : eDefObj.getNeedle(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element DifferenceSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateDifferenceSeries(String name, EObject eParentObj, DifferenceSeries eObj,
			DifferenceSeries eRefObj, DifferenceSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		if (!eObj.isSetCurve()) {
			if (eRefObj != null && eRefObj.isSetCurve()) {
				eObj.setCurve(eRefObj.isCurve());
			} else if (eDefObj != null && eDefObj.isSetCurve()) {
				eObj.setCurve(eDefObj.isCurve());
			}
		}

		if (!eObj.isSetConnectMissingValue()) {
			if (eRefObj != null && eRefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eRefObj.isConnectMissingValue());
			} else if (eDefObj != null && eDefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eDefObj.isConnectMissingValue());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		int index_DifferenceSeries_markers = 0;
		for (Marker element : eObj.getMarkers()) {
			updateMarker("markers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markers", index_DifferenceSeries_markers, element),
					getValidIndexRef(eDefObj, "markers", index_DifferenceSeries_markers, element), eDefOverride,
					checkVisible);
			index_DifferenceSeries_markers++;
		}

		updateMarker("marker", eObj, eObj.getMarker(), eRefObj == null ? null : eRefObj.getMarker(),
				eDefObj == null ? null : eDefObj.getMarker(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);
		int index_DifferenceSeries_negativeMarkers = 0;
		for (Marker element : eObj.getNegativeMarkers()) {
			updateMarker("negativeMarkers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "negativeMarkers", index_DifferenceSeries_negativeMarkers, element),
					getValidIndexRef(eDefObj, "negativeMarkers", index_DifferenceSeries_negativeMarkers, element),
					eDefOverride, checkVisible);
			index_DifferenceSeries_negativeMarkers++;
		}

		updateLineAttributes("negativeLineAttributes", eObj, eObj.getNegativeLineAttributes(),
				eRefObj == null ? null : eRefObj.getNegativeLineAttributes(),
				eDefObj == null ? null : eDefObj.getNegativeLineAttributes(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element GanttSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateGanttSeries(String name, EObject eParentObj, GanttSeries eObj, GanttSeries eRefObj,
			GanttSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetStartMarkerPosition()) {
			if (eRefObj != null && eRefObj.isSetStartMarkerPosition()) {
				eObj.setStartMarkerPosition(eRefObj.getStartMarkerPosition());
			} else if (eDefObj != null && eDefObj.isSetStartMarkerPosition()) {
				eObj.setStartMarkerPosition(eDefObj.getStartMarkerPosition());
			}
		}

		if (!eObj.isSetEndMarkerPosition()) {
			if (eRefObj != null && eRefObj.isSetEndMarkerPosition()) {
				eObj.setEndMarkerPosition(eRefObj.getEndMarkerPosition());
			} else if (eDefObj != null && eDefObj.isSetEndMarkerPosition()) {
				eObj.setEndMarkerPosition(eDefObj.getEndMarkerPosition());
			}
		}

		if (!eObj.isSetUseDecorationLabelValue()) {
			if (eRefObj != null && eRefObj.isSetUseDecorationLabelValue()) {
				eObj.setUseDecorationLabelValue(eRefObj.isUseDecorationLabelValue());
			} else if (eDefObj != null && eDefObj.isSetUseDecorationLabelValue()) {
				eObj.setUseDecorationLabelValue(eDefObj.isUseDecorationLabelValue());
			}
		}

		if (!eObj.isSetDecorationLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetDecorationLabelPosition()) {
				eObj.setDecorationLabelPosition(eRefObj.getDecorationLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetDecorationLabelPosition()) {
				eObj.setDecorationLabelPosition(eDefObj.getDecorationLabelPosition());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateMarker("startMarker", eObj, eObj.getStartMarker(), eRefObj == null ? null : eRefObj.getStartMarker(),
				eDefObj == null ? null : eDefObj.getStartMarker(), eDefOverride, checkVisible);
		updateMarker("endMarker", eObj, eObj.getEndMarker(), eRefObj == null ? null : eRefObj.getEndMarker(),
				eDefObj == null ? null : eDefObj.getEndMarker(), eDefOverride, checkVisible);
		updateLineAttributes("connectionLine", eObj, eObj.getConnectionLine(),
				eRefObj == null ? null : eRefObj.getConnectionLine(),
				eDefObj == null ? null : eDefObj.getConnectionLine(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("outlineFill", eObj, eObj.getOutlineFill(), eRefObj == null ? null : eRefObj.getOutlineFill(),
				eDefObj == null ? null : eDefObj.getOutlineFill(), eDefOverride, checkVisible);
		updateLabel("decorationLabel", eObj, eObj.getDecorationLabel(),
				eRefObj == null ? null : eRefObj.getDecorationLabel(),
				eDefObj == null ? null : eDefObj.getDecorationLabel(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element LineSeries.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateLineSeries(String name, EObject eParentObj, LineSeries eObj, LineSeries eRefObj,
			LineSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		if (eObj instanceof BubbleSeries) {
			updateBubbleSeries(name, eParentObj, (BubbleSeries) eObj, (BubbleSeries) eRefObj,
					DefaultValueProvider.defBubbleSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof DifferenceSeries) {
			updateDifferenceSeries(name, eParentObj, (DifferenceSeries) eObj, (DifferenceSeries) eRefObj,
					DefaultValueProvider.defDifferenceSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof AreaSeries) {
			updateAreaSeries(name, eParentObj, (AreaSeries) eObj, (AreaSeries) eRefObj,
					DefaultValueProvider.defAreaSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof ScatterSeries) {
			updateScatterSeries(name, eParentObj, (ScatterSeries) eObj, (ScatterSeries) eRefObj,
					DefaultValueProvider.defScatterSeries(), eDefOverride, checkVisible);
		} else {
			updateLineSeriesImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element LineSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateLineSeriesImpl(String name, EObject eParentObj, LineSeries eObj, LineSeries eRefObj,
			LineSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		if (!eObj.isSetCurve()) {
			if (eRefObj != null && eRefObj.isSetCurve()) {
				eObj.setCurve(eRefObj.isCurve());
			} else if (eDefObj != null && eDefObj.isSetCurve()) {
				eObj.setCurve(eDefObj.isCurve());
			}
		}

		if (!eObj.isSetConnectMissingValue()) {
			if (eRefObj != null && eRefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eRefObj.isConnectMissingValue());
			} else if (eDefObj != null && eDefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eDefObj.isConnectMissingValue());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		int index_LineSeries_markers = 0;
		for (Marker element : eObj.getMarkers()) {
			updateMarker("markers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markers", index_LineSeries_markers, element),
					getValidIndexRef(eDefObj, "markers", index_LineSeries_markers, element), eDefOverride,
					checkVisible);
			index_LineSeries_markers++;
		}

		updateMarker("marker", eObj, eObj.getMarker(), eRefObj == null ? null : eRefObj.getMarker(),
				eDefObj == null ? null : eDefObj.getMarker(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element PieSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updatePieSeries(String name, EObject eParentObj, PieSeries eObj, PieSeries eRefObj,
			PieSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetExplosion()) {
			if (eRefObj != null && eRefObj.isSetExplosion()) {
				eObj.setExplosion(eRefObj.getExplosion());
			} else if (eDefObj != null && eDefObj.isSetExplosion()) {
				eObj.setExplosion(eDefObj.getExplosion());
			}
		}

		if (eObj.getExplosionExpression() == null) {
			if (eRefObj != null && eRefObj.getExplosionExpression() != null) {
				eObj.setExplosionExpression(eRefObj.getExplosionExpression());
			} else if (eDefObj != null && eDefObj.getExplosionExpression() != null) {
				eObj.setExplosionExpression(eDefObj.getExplosionExpression());
			}
		}

		if (!eObj.isSetTitlePosition()) {
			if (eRefObj != null && eRefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eRefObj.getTitlePosition());
			} else if (eDefObj != null && eDefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eDefObj.getTitlePosition());
			}
		}

		if (!eObj.isSetLeaderLineStyle()) {
			if (eRefObj != null && eRefObj.isSetLeaderLineStyle()) {
				eObj.setLeaderLineStyle(eRefObj.getLeaderLineStyle());
			} else if (eDefObj != null && eDefObj.isSetLeaderLineStyle()) {
				eObj.setLeaderLineStyle(eDefObj.getLeaderLineStyle());
			}
		}

		if (!eObj.isSetLeaderLineLength()) {
			if (eRefObj != null && eRefObj.isSetLeaderLineLength()) {
				eObj.setLeaderLineLength(eRefObj.getLeaderLineLength());
			} else if (eDefObj != null && eDefObj.isSetLeaderLineLength()) {
				eObj.setLeaderLineLength(eDefObj.getLeaderLineLength());
			}
		}

		if (!eObj.isSetRatio()) {
			if (eRefObj != null && eRefObj.isSetRatio()) {
				eObj.setRatio(eRefObj.getRatio());
			} else if (eDefObj != null && eDefObj.isSetRatio()) {
				eObj.setRatio(eDefObj.getRatio());
			}
		}

		if (!eObj.isSetRotation()) {
			if (eRefObj != null && eRefObj.isSetRotation()) {
				eObj.setRotation(eRefObj.getRotation());
			} else if (eDefObj != null && eDefObj.isSetRotation()) {
				eObj.setRotation(eDefObj.getRotation());
			}
		}

		if (!eObj.isSetClockwise()) {
			if (eRefObj != null && eRefObj.isSetClockwise()) {
				eObj.setClockwise(eRefObj.isClockwise());
			} else if (eDefObj != null && eDefObj.isSetClockwise()) {
				eObj.setClockwise(eDefObj.isClockwise());
			}
		}

		if (!eObj.isSetInnerRadius()) {
			if (eRefObj != null && eRefObj.isSetInnerRadius()) {
				eObj.setInnerRadius(eRefObj.getInnerRadius());
			} else if (eDefObj != null && eDefObj.isSetInnerRadius()) {
				eObj.setInnerRadius(eDefObj.getInnerRadius());
			}
		}

		if (!eObj.isSetInnerRadiusPercent()) {
			if (eRefObj != null && eRefObj.isSetInnerRadiusPercent()) {
				eObj.setInnerRadiusPercent(eRefObj.isInnerRadiusPercent());
			} else if (eDefObj != null && eDefObj.isSetInnerRadiusPercent()) {
				eObj.setInnerRadiusPercent(eDefObj.isInnerRadiusPercent());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateLabel("title", eObj, eObj.getTitle(), eRefObj == null ? null : eRefObj.getTitle(),
				eDefObj == null ? null : eDefObj.getTitle(), eDefOverride, checkVisible);
		updateLineAttributes("leaderLineAttributes", eObj, eObj.getLeaderLineAttributes(),
				eRefObj == null ? null : eRefObj.getLeaderLineAttributes(),
				eDefObj == null ? null : eDefObj.getLeaderLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("sliceOutline", eObj, eObj.getSliceOutline(),
				eRefObj == null ? null : eRefObj.getSliceOutline(), eDefObj == null ? null : eDefObj.getSliceOutline(),
				eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element ScatterSeries.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateScatterSeries(String name, EObject eParentObj, ScatterSeries eObj, ScatterSeries eRefObj,
			ScatterSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		if (eObj instanceof BubbleSeries) {
			updateBubbleSeries(name, eParentObj, (BubbleSeries) eObj, (BubbleSeries) eRefObj,
					DefaultValueProvider.defBubbleSeries(), eDefOverride, checkVisible);
		} else {
			updateScatterSeriesImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element ScatterSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateScatterSeriesImpl(String name, EObject eParentObj, ScatterSeries eObj, ScatterSeries eRefObj,
			ScatterSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetPaletteLineColor()) {
			if (eRefObj != null && eRefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eRefObj.isPaletteLineColor());
			} else if (eDefObj != null && eDefObj.isSetPaletteLineColor()) {
				eObj.setPaletteLineColor(eDefObj.isPaletteLineColor());
			}
		}

		if (!eObj.isSetCurve()) {
			if (eRefObj != null && eRefObj.isSetCurve()) {
				eObj.setCurve(eRefObj.isCurve());
			} else if (eDefObj != null && eDefObj.isSetCurve()) {
				eObj.setCurve(eDefObj.isCurve());
			}
		}

		if (!eObj.isSetConnectMissingValue()) {
			if (eRefObj != null && eRefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eRefObj.isConnectMissingValue());
			} else if (eDefObj != null && eDefObj.isSetConnectMissingValue()) {
				eObj.setConnectMissingValue(eDefObj.isConnectMissingValue());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		int index_ScatterSeries_markers = 0;
		for (Marker element : eObj.getMarkers()) {
			updateMarker("markers", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markers", index_ScatterSeries_markers, element),
					getValidIndexRef(eDefObj, "markers", index_ScatterSeries_markers, element), eDefOverride,
					checkVisible);
			index_ScatterSeries_markers++;
		}

		updateMarker("marker", eObj, eObj.getMarker(), eRefObj == null ? null : eRefObj.getMarker(),
				eDefObj == null ? null : eDefObj.getMarker(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element StockSeries.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateStockSeries(String name, EObject eParentObj, StockSeries eObj, StockSeries eRefObj,
			StockSeries eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		if (!eObj.isSetShowAsBarStick()) {
			if (eRefObj != null && eRefObj.isSetShowAsBarStick()) {
				eObj.setShowAsBarStick(eRefObj.isShowAsBarStick());
			} else if (eDefObj != null && eDefObj.isSetShowAsBarStick()) {
				eObj.setShowAsBarStick(eDefObj.isShowAsBarStick());
			}
		}

		if (!eObj.isSetStickLength()) {
			if (eRefObj != null && eRefObj.isSetStickLength()) {
				eObj.setStickLength(eRefObj.getStickLength());
			} else if (eDefObj != null && eDefObj.isSetStickLength()) {
				eObj.setStickLength(eDefObj.getStickLength());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateFill("fill", eObj, eObj.getFill(), eRefObj == null ? null : eRefObj.getFill(),
				eDefObj == null ? null : eDefObj.getFill(), eDefOverride, checkVisible);
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Axis.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 * @param axisIndex    index of axis.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateAxis(String name, EObject eParentObj, Axis eObj, Axis eRefObj, Axis eDefObj, boolean eDefOverride,
			boolean checkVisible, int axisIndex) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (!eObj.isSetTitlePosition()) {
			if (eRefObj != null && eRefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eRefObj.getTitlePosition());
			} else if (eDefObj != null && eDefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eDefObj.getTitlePosition());
			}
		}

		if (!eObj.isSetGapWidth()) {
			if (eRefObj != null && eRefObj.isSetGapWidth()) {
				eObj.setGapWidth(eRefObj.getGapWidth());
			} else if (eDefObj != null && eDefObj.isSetGapWidth()) {
				eObj.setGapWidth(eDefObj.getGapWidth());
			}
		}

		if (!eObj.isSetOrientation()) {
			if (eRefObj != null && eRefObj.isSetOrientation()) {
				eObj.setOrientation(eRefObj.getOrientation());
			} else if (eDefObj != null && eDefObj.isSetOrientation()) {
				eObj.setOrientation(eDefObj.getOrientation());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStaggered()) {
			if (eRefObj != null && eRefObj.isSetStaggered()) {
				eObj.setStaggered(eRefObj.isStaggered());
			} else if (eDefObj != null && eDefObj.isSetStaggered()) {
				eObj.setStaggered(eDefObj.isStaggered());
			}
		}

		if (!eObj.isSetInterval()) {
			if (eRefObj != null && eRefObj.isSetInterval()) {
				eObj.setInterval(eRefObj.getInterval());
			} else if (eDefObj != null && eDefObj.isSetInterval()) {
				eObj.setInterval(eDefObj.getInterval());
			}
		}

		if (!eObj.isSetPrimaryAxis()) {
			if (eRefObj != null && eRefObj.isSetPrimaryAxis()) {
				eObj.setPrimaryAxis(eRefObj.isPrimaryAxis());
			} else if (eDefObj != null && eDefObj.isSetPrimaryAxis()) {
				eObj.setPrimaryAxis(eDefObj.isPrimaryAxis());
			}
		}

		if (!eObj.isSetCategoryAxis()) {
			if (eRefObj != null && eRefObj.isSetCategoryAxis()) {
				eObj.setCategoryAxis(eRefObj.isCategoryAxis());
			} else if (eDefObj != null && eDefObj.isSetCategoryAxis()) {
				eObj.setCategoryAxis(eDefObj.isCategoryAxis());
			}
		}

		if (!eObj.isSetPercent()) {
			if (eRefObj != null && eRefObj.isSetPercent()) {
				eObj.setPercent(eRefObj.isPercent());
			} else if (eDefObj != null && eDefObj.isSetPercent()) {
				eObj.setPercent(eDefObj.isPercent());
			}
		}

		if (!eObj.isSetLabelWithinAxes()) {
			if (eRefObj != null && eRefObj.isSetLabelWithinAxes()) {
				eObj.setLabelWithinAxes(eRefObj.isLabelWithinAxes());
			} else if (eDefObj != null && eDefObj.isSetLabelWithinAxes()) {
				eObj.setLabelWithinAxes(eDefObj.isLabelWithinAxes());
			}
		}

		if (!eObj.isSetAligned()) {
			if (eRefObj != null && eRefObj.isSetAligned()) {
				eObj.setAligned(eRefObj.isAligned());
			} else if (eDefObj != null && eDefObj.isSetAligned()) {
				eObj.setAligned(eDefObj.isAligned());
			}
		}

		if (!eObj.isSetSideBySide()) {
			if (eRefObj != null && eRefObj.isSetSideBySide()) {
				eObj.setSideBySide(eRefObj.isSideBySide());
			} else if (eDefObj != null && eDefObj.isSetSideBySide()) {
				eObj.setSideBySide(eDefObj.isSideBySide());
			}
		}

		if (!eObj.isSetLabelSpan()) {
			if (eRefObj != null && eRefObj.isSetLabelSpan()) {
				eObj.setLabelSpan(eRefObj.getLabelSpan());
			} else if (eDefObj != null && eDefObj.isSetLabelSpan()) {
				eObj.setLabelSpan(eDefObj.getLabelSpan());
			}
		}

		if (!eObj.isSetAxisPercent()) {
			if (eRefObj != null && eRefObj.isSetAxisPercent()) {
				eObj.setAxisPercent(eRefObj.getAxisPercent());
			} else if (eDefObj != null && eDefObj.isSetAxisPercent()) {
				eObj.setAxisPercent(eDefObj.getAxisPercent());
			}
		}

		// list attributes

		// references
		updateLabel("title", eObj, eObj.getTitle(), eRefObj == null ? null : eRefObj.getTitle(),
				eDefObj == null ? null : eDefObj.getTitle(), eDefOverride, checkVisible);
		updateLabel("subTitle", eObj, eObj.getSubTitle(), eRefObj == null ? null : eRefObj.getSubTitle(),
				eDefObj == null ? null : eDefObj.getSubTitle(), eDefOverride, checkVisible);
		int orghAxisIndex = 0;
		for (Axis element : eObj.getAssociatedAxes()) {
			updateAxis("associatedAxes", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getAssociatedAxes().size() > 0) ? eRefObj.getAssociatedAxes().get(0)
							: null,
					DefaultValueProvider.defOrthogonalAxis(), eDefOverride, checkVisible, orghAxisIndex);
			orghAxisIndex++;
		}

		for (Axis element : eObj.getAncillaryAxes()) {
			updateAxis("ancillaryAxes", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getAncillaryAxes().size() > 0) ? eRefObj.getAncillaryAxes().get(0)
							: null,
					DefaultValueProvider.defAncillaryAxis(), eDefOverride, checkVisible, 0);
		}

		int seriesDefIndex = 0;
		for (SeriesDefinition element : eObj.getSeriesDefinitions()) {
			updateSeriesDefinition("seriesDefinitions", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getSeriesDefinitions().size() > 0)
							? eRefObj.getSeriesDefinitions().get(0)
							: null,
					DefaultValueProvider.defSeriesDefinition(seriesDefIndex), eDefOverride, checkVisible, axisIndex,
					seriesDefIndex);
			seriesDefIndex++;
		}

		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);
		int index_Axis_markerLines = 0;
		for (MarkerLine element : eObj.getMarkerLines()) {
			updateMarkerLine("markerLines", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markerLines", index_Axis_markerLines, element),
					getValidIndexRef(eDefObj, "markerLines", index_Axis_markerLines, element), eDefOverride,
					checkVisible);
			index_Axis_markerLines++;
		}

		int index_Axis_markerRanges = 0;
		for (MarkerRange element : eObj.getMarkerRanges()) {
			updateMarkerRange("markerRanges", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "markerRanges", index_Axis_markerRanges, element),
					getValidIndexRef(eDefObj, "markerRanges", index_Axis_markerRanges, element), eDefOverride,
					checkVisible);
			index_Axis_markerRanges++;
		}

		updateGrid("majorGrid", eObj, eObj.getMajorGrid(), eRefObj == null ? null : eRefObj.getMajorGrid(),
				eDefObj == null ? null : eDefObj.getMajorGrid(), eDefOverride, checkVisible);
		updateGrid("minorGrid", eObj, eObj.getMinorGrid(), eRefObj == null ? null : eRefObj.getMinorGrid(),
				eDefObj == null ? null : eDefObj.getMinorGrid(), eDefOverride, checkVisible);
		updateScale("scale", eObj, eObj.getScale(), eRefObj == null ? null : eRefObj.getScale(),
				eDefObj == null ? null : eDefObj.getScale(), eDefOverride, checkVisible);
		updateAxisOrigin("origin", eObj, eObj.getOrigin(), eRefObj == null ? null : eRefObj.getOrigin(),
				eDefObj == null ? null : eDefObj.getOrigin(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element CurveFitting.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateCurveFitting(String name, EObject eParentObj, CurveFitting eObj, CurveFitting eRefObj,
			CurveFitting eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetLabelAnchor()) {
			if (eRefObj != null && eRefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eRefObj.getLabelAnchor());
			} else if (eDefObj != null && eDefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eDefObj.getLabelAnchor());
			}
		}

		// list attributes

		// references
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Dial.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateDial(String name, EObject eParentObj, Dial eObj, Dial eRefObj, Dial eDefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetStartAngle()) {
			if (eRefObj != null && eRefObj.isSetStartAngle()) {
				eObj.setStartAngle(eRefObj.getStartAngle());
			} else if (eDefObj != null && eDefObj.isSetStartAngle()) {
				eObj.setStartAngle(eDefObj.getStartAngle());
			}
		}

		if (!eObj.isSetStopAngle()) {
			if (eRefObj != null && eRefObj.isSetStopAngle()) {
				eObj.setStopAngle(eRefObj.getStopAngle());
			} else if (eDefObj != null && eDefObj.isSetStopAngle()) {
				eObj.setStopAngle(eDefObj.getStopAngle());
			}
		}

		if (!eObj.isSetRadius()) {
			if (eRefObj != null && eRefObj.isSetRadius()) {
				eObj.setRadius(eRefObj.getRadius());
			} else if (eDefObj != null && eDefObj.isSetRadius()) {
				eObj.setRadius(eDefObj.getRadius());
			}
		}

		if (!eObj.isSetInverseScale()) {
			if (eRefObj != null && eRefObj.isSetInverseScale()) {
				eObj.setInverseScale(eRefObj.isInverseScale());
			} else if (eDefObj != null && eDefObj.isSetInverseScale()) {
				eObj.setInverseScale(eDefObj.isInverseScale());
			}
		}

		// list attributes

		// references
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateFill("fill", eObj, eObj.getFill(), eRefObj == null ? null : eRefObj.getFill(),
				eDefObj == null ? null : eDefObj.getFill(), eDefOverride, checkVisible);
		int index_Dial_dialRegions = 0;
		for (DialRegion element : eObj.getDialRegions()) {
			updateDialRegion("dialRegions", eObj, element, //$NON-NLS-1$
					getValidIndexRef(eRefObj, "dialRegions", index_Dial_dialRegions, element),
					getValidIndexRef(eDefObj, "dialRegions", index_Dial_dialRegions, element), eDefOverride,
					checkVisible);
			index_Dial_dialRegions++;
		}

		updateGrid("majorGrid", eObj, eObj.getMajorGrid(), eRefObj == null ? null : eRefObj.getMajorGrid(),
				eDefObj == null ? null : eDefObj.getMajorGrid(), eDefOverride, checkVisible);
		updateGrid("minorGrid", eObj, eObj.getMinorGrid(), eRefObj == null ? null : eRefObj.getMinorGrid(),
				eDefObj == null ? null : eDefObj.getMinorGrid(), eDefOverride, checkVisible);
		updateScale("scale", eObj, eObj.getScale(), eRefObj == null ? null : eRefObj.getScale(),
				eDefObj == null ? null : eDefObj.getScale(), eDefOverride, checkVisible);
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element DialRegion.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateDialRegion(String name, EObject eParentObj, DialRegion eObj, DialRegion eRefObj,
			DialRegion eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetLabelAnchor()) {
			if (eRefObj != null && eRefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eRefObj.getLabelAnchor());
			} else if (eDefObj != null && eDefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eDefObj.getLabelAnchor());
			}
		}

		if (!eObj.isSetInnerRadius()) {
			if (eRefObj != null && eRefObj.isSetInnerRadius()) {
				eObj.setInnerRadius(eRefObj.getInnerRadius());
			} else if (eDefObj != null && eDefObj.isSetInnerRadius()) {
				eObj.setInnerRadius(eDefObj.getInnerRadius());
			}
		}

		if (!eObj.isSetOuterRadius()) {
			if (eRefObj != null && eRefObj.isSetOuterRadius()) {
				eObj.setOuterRadius(eRefObj.getOuterRadius());
			} else if (eDefObj != null && eDefObj.isSetOuterRadius()) {
				eObj.setOuterRadius(eDefObj.getOuterRadius());
			}
		}

		// list attributes

		// references
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("fill", eObj, eObj.getFill(), eRefObj == null ? null : eRefObj.getFill(),
				eDefObj == null ? null : eDefObj.getFill(), eDefOverride, checkVisible);
		if (eObj.getStartValue() == null) {
			if (eRefObj != null && eRefObj.getStartValue() != null) {
				eObj.setStartValue(eRefObj.getStartValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getStartValue() != null) {
				eObj.setStartValue(eDefObj.getStartValue().copyInstance());
			}
		}
		if (eObj.getEndValue() == null) {
			if (eRefObj != null && eRefObj.getEndValue() != null) {
				eObj.setEndValue(eRefObj.getEndValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getEndValue() != null) {
				eObj.setEndValue(eDefObj.getEndValue().copyInstance());
			}
		}
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Grid.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateGrid(String name, EObject eParentObj, Grid eObj, Grid eRefObj, Grid eDefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetTickStyle()) {
			if (eRefObj != null && eRefObj.isSetTickStyle()) {
				eObj.setTickStyle(eRefObj.getTickStyle());
			} else if (eDefObj != null && eDefObj.isSetTickStyle()) {
				eObj.setTickStyle(eDefObj.getTickStyle());
			}
		}

		if (!eObj.isSetTickSize()) {
			if (eRefObj != null && eRefObj.isSetTickSize()) {
				eObj.setTickSize(eRefObj.getTickSize());
			} else if (eDefObj != null && eDefObj.isSetTickSize()) {
				eObj.setTickSize(eDefObj.getTickSize());
			}
		}

		if (!eObj.isSetTickCount()) {
			if (eRefObj != null && eRefObj.isSetTickCount()) {
				eObj.setTickCount(eRefObj.getTickCount());
			} else if (eDefObj != null && eDefObj.isSetTickCount()) {
				eObj.setTickCount(eDefObj.getTickCount());
			}
		}

		// list attributes

		// references
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		updateLineAttributes("tickAttributes", eObj, eObj.getTickAttributes(),
				eRefObj == null ? null : eRefObj.getTickAttributes(),
				eDefObj == null ? null : eDefObj.getTickAttributes(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Label.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateLabel(String name, EObject eParentObj, Label eObj, Label eRefObj, Label eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetEllipsis()) {
			if (eRefObj != null && eRefObj.isSetEllipsis()) {
				eObj.setEllipsis(eRefObj.getEllipsis());
			} else if (eDefObj != null && eDefObj.isSetEllipsis()) {
				eObj.setEllipsis(eDefObj.getEllipsis());
			}
		}

		// list attributes

		// references
		updateText("caption", eObj, eObj.getCaption(), eRefObj == null ? null : eRefObj.getCaption(),
				eDefObj == null ? null : eDefObj.getCaption(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element MarkerLine.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateMarkerLine(String name, EObject eParentObj, MarkerLine eObj, MarkerLine eRefObj,
			MarkerLine eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetLabelAnchor()) {
			if (eRefObj != null && eRefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eRefObj.getLabelAnchor());
			} else if (eDefObj != null && eDefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eDefObj.getLabelAnchor());
			}
		}

		// list attributes

		// references
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);
		if (eObj.getValue() == null) {
			if (eRefObj != null && eRefObj.getValue() != null) {
				eObj.setValue(eRefObj.getValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getValue() != null) {
				eObj.setValue(eDefObj.getValue().copyInstance());
			}
		}
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element MarkerRange.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateMarkerRange(String name, EObject eParentObj, MarkerRange eObj, MarkerRange eRefObj,
			MarkerRange eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eObj instanceof DialRegion) {
			updateDialRegion(name, eParentObj, (DialRegion) eObj, (DialRegion) eRefObj,
					eDefObj instanceof DialRegion ? (DialRegion) eDefObj : null, eDefOverride, checkVisible);
		} else {
			updateMarkerRangeImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element MarkerRange.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateMarkerRangeImpl(String name, EObject eParentObj, MarkerRange eObj, MarkerRange eRefObj,
			MarkerRange eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetLabelAnchor()) {
			if (eRefObj != null && eRefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eRefObj.getLabelAnchor());
			} else if (eDefObj != null && eDefObj.isSetLabelAnchor()) {
				eObj.setLabelAnchor(eDefObj.getLabelAnchor());
			}
		}

		// list attributes

		// references
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("fill", eObj, eObj.getFill(), eRefObj == null ? null : eRefObj.getFill(),
				eDefObj == null ? null : eDefObj.getFill(), eDefOverride, checkVisible);
		if (eObj.getStartValue() == null) {
			if (eRefObj != null && eRefObj.getStartValue() != null) {
				eObj.setStartValue(eRefObj.getStartValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getStartValue() != null) {
				eObj.setStartValue(eDefObj.getStartValue().copyInstance());
			}
		}
		if (eObj.getEndValue() == null) {
			if (eRefObj != null && eRefObj.getEndValue() != null) {
				eObj.setEndValue(eRefObj.getEndValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getEndValue() != null) {
				eObj.setEndValue(eDefObj.getEndValue().copyInstance());
			}
		}
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Needle.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateNeedle(String name, EObject eParentObj, Needle eObj, Needle eRefObj, Needle eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetDecorator()) {
			if (eRefObj != null && eRefObj.isSetDecorator()) {
				eObj.setDecorator(eRefObj.getDecorator());
			} else if (eDefObj != null && eDefObj.isSetDecorator()) {
				eObj.setDecorator(eDefObj.getDecorator());
			}
		}

		// list attributes

		// references
		updateLineAttributes("lineAttributes", eObj, eObj.getLineAttributes(),
				eRefObj == null ? null : eRefObj.getLineAttributes(),
				eDefObj == null ? null : eDefObj.getLineAttributes(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Scale.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateScale(String name, EObject eParentObj, Scale eObj, Scale eRefObj, Scale eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetStep()) {
			if (eRefObj != null && eRefObj.isSetStep()) {
				eObj.setStep(eRefObj.getStep());
			} else if (eDefObj != null && eDefObj.isSetStep()) {
				eObj.setStep(eDefObj.getStep());
			}
		}

		if (!eObj.isSetUnit()) {
			if (eRefObj != null && eRefObj.isSetUnit()) {
				eObj.setUnit(eRefObj.getUnit());
			} else if (eDefObj != null && eDefObj.isSetUnit()) {
				eObj.setUnit(eDefObj.getUnit());
			}
		}

		if (!eObj.isSetMinorGridsPerUnit()) {
			if (eRefObj != null && eRefObj.isSetMinorGridsPerUnit()) {
				eObj.setMinorGridsPerUnit(eRefObj.getMinorGridsPerUnit());
			} else if (eDefObj != null && eDefObj.isSetMinorGridsPerUnit()) {
				eObj.setMinorGridsPerUnit(eDefObj.getMinorGridsPerUnit());
			}
		}

		if (!eObj.isSetStepNumber()) {
			if (eRefObj != null && eRefObj.isSetStepNumber()) {
				eObj.setStepNumber(eRefObj.getStepNumber());
			} else if (eDefObj != null && eDefObj.isSetStepNumber()) {
				eObj.setStepNumber(eDefObj.getStepNumber());
			}
		}

		if (!eObj.isSetShowOutside()) {
			if (eRefObj != null && eRefObj.isSetShowOutside()) {
				eObj.setShowOutside(eRefObj.isShowOutside());
			} else if (eDefObj != null && eDefObj.isSetShowOutside()) {
				eObj.setShowOutside(eDefObj.isShowOutside());
			}
		}

		if (!eObj.isSetTickBetweenCategories()) {
			if (eRefObj != null && eRefObj.isSetTickBetweenCategories()) {
				eObj.setTickBetweenCategories(eRefObj.isTickBetweenCategories());
			} else if (eDefObj != null && eDefObj.isSetTickBetweenCategories()) {
				eObj.setTickBetweenCategories(eDefObj.isTickBetweenCategories());
			}
		}

		if (!eObj.isSetAutoExpand()) {
			if (eRefObj != null && eRefObj.isSetAutoExpand()) {
				eObj.setAutoExpand(eRefObj.isAutoExpand());
			} else if (eDefObj != null && eDefObj.isSetAutoExpand()) {
				eObj.setAutoExpand(eDefObj.isAutoExpand());
			}
		}

		if (!eObj.isSetMajorGridsStepNumber()) {
			if (eRefObj != null && eRefObj.isSetMajorGridsStepNumber()) {
				eObj.setMajorGridsStepNumber(eRefObj.getMajorGridsStepNumber());
			} else if (eDefObj != null && eDefObj.isSetMajorGridsStepNumber()) {
				eObj.setMajorGridsStepNumber(eDefObj.getMajorGridsStepNumber());
			}
		}

		if (!eObj.isSetFactor()) {
			if (eRefObj != null && eRefObj.isSetFactor()) {
				eObj.setFactor(eRefObj.getFactor());
			} else if (eDefObj != null && eDefObj.isSetFactor()) {
				eObj.setFactor(eDefObj.getFactor());
			}
		}

		// list attributes

		// references
		if (eObj.getMin() == null) {
			if (eRefObj != null && eRefObj.getMin() != null) {
				eObj.setMin(eRefObj.getMin().copyInstance());
			} else if (eDefObj != null && eDefObj.getMin() != null) {
				eObj.setMin(eDefObj.getMin().copyInstance());
			}
		}
		if (eObj.getMax() == null) {
			if (eRefObj != null && eRefObj.getMax() != null) {
				eObj.setMax(eRefObj.getMax().copyInstance());
			} else if (eDefObj != null && eDefObj.getMax() != null) {
				eObj.setMax(eDefObj.getMax().copyInstance());
			}
		}

	}

	/**
	 * Updates chart element Series.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      map of series objects.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateSeries(String name, EObject eParentObj, Series eObj, Map<String, Series> eRefObj, Series eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}
		if (ChartDynamicExtension.isExtended(eObj)) {
			EObject seriesDefault = extUpdater.getDefault(ComponentPackage.eINSTANCE.getSeries(), name, eObj);
			String key = getSeriesID(eObj);
			extUpdater.update(ComponentPackage.eINSTANCE.getSeries(), name, eParentObj, eObj, eRefObj.get(key),
					seriesDefault);
			return;
		}

		if (eObj instanceof BubbleSeries) {
			updateBubbleSeries(name, eParentObj, (BubbleSeries) eObj, (BubbleSeries) eRefObj.get("BubbleSeries"),
					DefaultValueProvider.defBubbleSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof DifferenceSeries) {
			updateDifferenceSeries(name, eParentObj, (DifferenceSeries) eObj,
					(DifferenceSeries) eRefObj.get("DifferenceSeries"), DefaultValueProvider.defDifferenceSeries(),
					eDefOverride, checkVisible);
		} else if (eObj instanceof AreaSeries) {
			updateAreaSeries(name, eParentObj, (AreaSeries) eObj, (AreaSeries) eRefObj.get("AreaSeries"),
					DefaultValueProvider.defAreaSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof ScatterSeries) {
			updateScatterSeries(name, eParentObj, (ScatterSeries) eObj, (ScatterSeries) eRefObj.get("ScatterSeries"),
					DefaultValueProvider.defScatterSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof BarSeries) {
			updateBarSeries(name, eParentObj, (BarSeries) eObj, (BarSeries) eRefObj.get("BarSeries"),
					DefaultValueProvider.defBarSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof DialSeries) {
			updateDialSeries(name, eParentObj, (DialSeries) eObj, (DialSeries) eRefObj.get("DialSeries"),
					DefaultValueProvider.defDialSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof GanttSeries) {
			updateGanttSeries(name, eParentObj, (GanttSeries) eObj, (GanttSeries) eRefObj.get("GanttSeries"),
					DefaultValueProvider.defGanttSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof LineSeries) {
			updateLineSeries(name, eParentObj, (LineSeries) eObj, (LineSeries) eRefObj.get("LineSeries"),
					DefaultValueProvider.defLineSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof PieSeries) {
			updatePieSeries(name, eParentObj, (PieSeries) eObj, (PieSeries) eRefObj.get("PieSeries"),
					DefaultValueProvider.defPieSeries(), eDefOverride, checkVisible);
		} else if (eObj instanceof StockSeries) {
			updateStockSeries(name, eParentObj, (StockSeries) eObj, (StockSeries) eRefObj.get("StockSeries"),
					DefaultValueProvider.defStockSeries(), eDefOverride, checkVisible);
		} else {
			updateSeriesImpl(name, eParentObj, eObj, eRefObj.size() > 0 ? eRefObj.get(0) : null, eDefObj, eDefOverride,
					checkVisible);
		}
	}

	/**
	 * Updates chart element Series.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateSeriesImpl(String name, EObject eParentObj, Series eObj, Series eRefObj, Series eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (eObj.getSeriesIdentifier() == null) {
			if (eRefObj != null && eRefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eRefObj.getSeriesIdentifier());
			} else if (eDefObj != null && eDefObj.getSeriesIdentifier() != null) {
				eObj.setSeriesIdentifier(eDefObj.getSeriesIdentifier());
			}
		}

		if (!eObj.isSetLabelPosition()) {
			if (eRefObj != null && eRefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eRefObj.getLabelPosition());
			} else if (eDefObj != null && eDefObj.isSetLabelPosition()) {
				eObj.setLabelPosition(eDefObj.getLabelPosition());
			}
		}

		if (!eObj.isSetStacked()) {
			if (eRefObj != null && eRefObj.isSetStacked()) {
				eObj.setStacked(eRefObj.isStacked());
			} else if (eDefObj != null && eDefObj.isSetStacked()) {
				eObj.setStacked(eDefObj.isStacked());
			}
		}

		if (!eObj.isSetTranslucent()) {
			if (eRefObj != null && eRefObj.isSetTranslucent()) {
				eObj.setTranslucent(eRefObj.isTranslucent());
			} else if (eDefObj != null && eDefObj.isSetTranslucent()) {
				eObj.setTranslucent(eDefObj.isTranslucent());
			}
		}

		// list attributes

		// references
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);
		updateDataPoint("dataPoint", eObj, eObj.getDataPoint(), eRefObj == null ? null : eRefObj.getDataPoint(),
				eDefObj == null ? null : eDefObj.getDataPoint(), eDefOverride, checkVisible);
		updateCurveFitting("curveFitting", eObj, eObj.getCurveFitting(),
				eRefObj == null ? null : eRefObj.getCurveFitting(), eDefObj == null ? null : eDefObj.getCurveFitting(),
				eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element SeriesDefinition.
	 *
	 * @param eObj           chart element object.
	 * @param eRefObj        reference chart element object.
	 * @param eDefObj        default chart element object.
	 * @param eDefOverride   indicates if using default object to override target
	 *                       object if target is null.
	 * @param checkVisible   indicates if still checking visible of chart element
	 *                       before updating properties of chart element.
	 * @param axisIndex      index of axis.
	 * @param seriesDefIndex index of series definition.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateSeriesDefinition(String name, EObject eParentObj, SeriesDefinition eObj, SeriesDefinition eRefObj,
			SeriesDefinition eDefObj, boolean eDefOverride, boolean checkVisible, int axisIndex, int seriesDefIndex) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetZOrder()) {
			if (eRefObj != null && eRefObj.isSetZOrder()) {
				eObj.setZOrder(eRefObj.getZOrder());
			} else if (eDefObj != null && eDefObj.isSetZOrder()) {
				eObj.setZOrder(eDefObj.getZOrder());
			}
		}

		// list attributes

		// references
		updatePalette("seriesPalette", eObj, eObj.getSeriesPalette(),
				eRefObj == null ? null : eRefObj.getSeriesPalette(),
				eDefObj == null ? null : eDefObj.getSeriesPalette(), eDefOverride, checkVisible, axisIndex,
				seriesDefIndex);
		int orthSeriesDefIndex = 0;
		for (SeriesDefinition element : eObj.getSeriesDefinitions()) {
			updateSeriesDefinition("seriesDefinitions", eObj, element, //$NON-NLS-1$
					(eRefObj != null && eRefObj.getSeriesDefinitions().size() > 0)
							? eRefObj.getSeriesDefinitions().get(0)
							: null,
					DefaultValueProvider.defSeriesDefinition(orthSeriesDefIndex), eDefOverride, checkVisible, 0,
					orthSeriesDefIndex);
			orthSeriesDefIndex++;
		}

		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);
		Map<String, Series> seriesRefMap = new HashMap<String, Series>();
		;
		if (eRefObj != null) {
			for (Series series : eRefObj.getSeries()) {
				seriesRefMap.put(getSeriesID(series), series);
			}
		}
		for (Series element : eObj.getSeries()) {
			updateSeries("series", eObj, element, seriesRefMap, DefaultValueProvider.defSeries(), eDefOverride, //$NON-NLS-1$
					checkVisible);
		}

	}

	/**
	 * Updates chart element Block.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateBlock(String name, EObject eParentObj, Block eObj, Block eRefObj, Block eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		if (eObj instanceof TitleBlock) {
			updateTitleBlock(name, eParentObj, (TitleBlock) eObj, (TitleBlock) eRefObj,
					DefaultValueProvider.defTitleBlock(), eDefOverride, checkVisible);
		} else if (eObj instanceof LabelBlock) {
			updateLabelBlock(name, eParentObj, (LabelBlock) eObj, (LabelBlock) eRefObj,
					eDefObj instanceof LabelBlock ? (LabelBlock) eDefObj : null, eDefOverride, checkVisible);
		} else if (eObj instanceof Legend) {
			updateLegend(name, eParentObj, (Legend) eObj, (Legend) eRefObj, DefaultValueProvider.defLegend(),
					eDefOverride, checkVisible);
		} else if (eObj instanceof Plot) {
			updatePlot(name, eParentObj, (Plot) eObj, (Plot) eRefObj, DefaultValueProvider.defPlot(), eDefOverride,
					checkVisible);
		} else {
			updateBlockImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element Block.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateBlockImpl(String name, EObject eParentObj, Block eObj, Block eRefObj, Block eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetAnchor()) {
			if (eRefObj != null && eRefObj.isSetAnchor()) {
				eObj.setAnchor(eRefObj.getAnchor());
			} else if (eDefObj != null && eDefObj.isSetAnchor()) {
				eObj.setAnchor(eDefObj.getAnchor());
			}
		}

		if (!eObj.isSetStretch()) {
			if (eRefObj != null && eRefObj.isSetStretch()) {
				eObj.setStretch(eRefObj.getStretch());
			} else if (eDefObj != null && eDefObj.isSetStretch()) {
				eObj.setStretch(eDefObj.getStretch());
			}
		}

		if (!eObj.isSetRow()) {
			if (eRefObj != null && eRefObj.isSetRow()) {
				eObj.setRow(eRefObj.getRow());
			} else if (eDefObj != null && eDefObj.isSetRow()) {
				eObj.setRow(eDefObj.getRow());
			}
		}

		if (!eObj.isSetColumn()) {
			if (eRefObj != null && eRefObj.isSetColumn()) {
				eObj.setColumn(eRefObj.getColumn());
			} else if (eDefObj != null && eDefObj.isSetColumn()) {
				eObj.setColumn(eDefObj.getColumn());
			}
		}

		if (!eObj.isSetRowspan()) {
			if (eRefObj != null && eRefObj.isSetRowspan()) {
				eObj.setRowspan(eRefObj.getRowspan());
			} else if (eDefObj != null && eDefObj.isSetRowspan()) {
				eObj.setRowspan(eDefObj.getRowspan());
			}
		}

		if (!eObj.isSetColumnspan()) {
			if (eRefObj != null && eRefObj.isSetColumnspan()) {
				eObj.setColumnspan(eRefObj.getColumnspan());
			} else if (eDefObj != null && eDefObj.isSetColumnspan()) {
				eObj.setColumnspan(eDefObj.getColumnspan());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetWidthHint()) {
			if (eRefObj != null && eRefObj.isSetWidthHint()) {
				eObj.setWidthHint(eRefObj.getWidthHint());
			} else if (eDefObj != null && eDefObj.isSetWidthHint()) {
				eObj.setWidthHint(eDefObj.getWidthHint());
			}
		}

		if (!eObj.isSetHeightHint()) {
			if (eRefObj != null && eRefObj.isSetHeightHint()) {
				eObj.setHeightHint(eRefObj.getHeightHint());
			} else if (eDefObj != null && eDefObj.isSetHeightHint()) {
				eObj.setHeightHint(eDefObj.getHeightHint());
			}
		}

		// list attributes

		// references
		int index_Block_children = 0;
		for (Block element : eObj.getChildren()) {
			updateBlock("children", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getChildren().size() <= index_Block_children) ? null
							: eRefObj.getChildren().get(index_Block_children),
					(eDefObj == null || eDefObj.getChildren().size() <= index_Block_children) ? null
							: eDefObj.getChildren().get(index_Block_children),
					eDefOverride, checkVisible);
			index_Block_children++;
		}

		updateBounds("bounds", eObj, eObj.getBounds(), eRefObj == null ? null : eRefObj.getBounds(),
				eDefObj == null ? null : eDefObj.getBounds(), eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);
		updateSize("minSize", eObj, eObj.getMinSize(), eRefObj == null ? null : eRefObj.getMinSize(),
				eDefObj == null ? null : eDefObj.getMinSize(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element ClientArea.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateClientArea(String name, EObject eParentObj, ClientArea eObj, ClientArea eRefObj,
			ClientArea eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		// list attributes

		// references
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateColorDefinition("shadowColor", eObj, eObj.getShadowColor(),
				eRefObj == null ? null : eRefObj.getShadowColor(), eDefObj == null ? null : eDefObj.getShadowColor(),
				eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element LabelBlock.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateLabelBlock(String name, EObject eParentObj, LabelBlock eObj, LabelBlock eRefObj,
			LabelBlock eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		if (eObj instanceof TitleBlock) {
			updateTitleBlock(name, eParentObj, (TitleBlock) eObj, (TitleBlock) eRefObj,
					DefaultValueProvider.defTitleBlock(), eDefOverride, checkVisible);
		} else {
			updateLabelBlockImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element LabelBlock.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateLabelBlockImpl(String name, EObject eParentObj, LabelBlock eObj, LabelBlock eRefObj,
			LabelBlock eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetAnchor()) {
			if (eRefObj != null && eRefObj.isSetAnchor()) {
				eObj.setAnchor(eRefObj.getAnchor());
			} else if (eDefObj != null && eDefObj.isSetAnchor()) {
				eObj.setAnchor(eDefObj.getAnchor());
			}
		}

		if (!eObj.isSetStretch()) {
			if (eRefObj != null && eRefObj.isSetStretch()) {
				eObj.setStretch(eRefObj.getStretch());
			} else if (eDefObj != null && eDefObj.isSetStretch()) {
				eObj.setStretch(eDefObj.getStretch());
			}
		}

		if (!eObj.isSetRow()) {
			if (eRefObj != null && eRefObj.isSetRow()) {
				eObj.setRow(eRefObj.getRow());
			} else if (eDefObj != null && eDefObj.isSetRow()) {
				eObj.setRow(eDefObj.getRow());
			}
		}

		if (!eObj.isSetColumn()) {
			if (eRefObj != null && eRefObj.isSetColumn()) {
				eObj.setColumn(eRefObj.getColumn());
			} else if (eDefObj != null && eDefObj.isSetColumn()) {
				eObj.setColumn(eDefObj.getColumn());
			}
		}

		if (!eObj.isSetRowspan()) {
			if (eRefObj != null && eRefObj.isSetRowspan()) {
				eObj.setRowspan(eRefObj.getRowspan());
			} else if (eDefObj != null && eDefObj.isSetRowspan()) {
				eObj.setRowspan(eDefObj.getRowspan());
			}
		}

		if (!eObj.isSetColumnspan()) {
			if (eRefObj != null && eRefObj.isSetColumnspan()) {
				eObj.setColumnspan(eRefObj.getColumnspan());
			} else if (eDefObj != null && eDefObj.isSetColumnspan()) {
				eObj.setColumnspan(eDefObj.getColumnspan());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetWidthHint()) {
			if (eRefObj != null && eRefObj.isSetWidthHint()) {
				eObj.setWidthHint(eRefObj.getWidthHint());
			} else if (eDefObj != null && eDefObj.isSetWidthHint()) {
				eObj.setWidthHint(eDefObj.getWidthHint());
			}
		}

		if (!eObj.isSetHeightHint()) {
			if (eRefObj != null && eRefObj.isSetHeightHint()) {
				eObj.setHeightHint(eRefObj.getHeightHint());
			} else if (eDefObj != null && eDefObj.isSetHeightHint()) {
				eObj.setHeightHint(eDefObj.getHeightHint());
			}
		}

		// list attributes

		// references
		int index_LabelBlock_children = 0;
		for (Block element : eObj.getChildren()) {
			updateBlock("children", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getChildren().size() <= index_LabelBlock_children) ? null
							: eRefObj.getChildren().get(index_LabelBlock_children),
					(eDefObj == null || eDefObj.getChildren().size() <= index_LabelBlock_children) ? null
							: eDefObj.getChildren().get(index_LabelBlock_children),
					eDefOverride, checkVisible);
			index_LabelBlock_children++;
		}

		updateBounds("bounds", eObj, eObj.getBounds(), eRefObj == null ? null : eRefObj.getBounds(),
				eDefObj == null ? null : eDefObj.getBounds(), eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);
		updateSize("minSize", eObj, eObj.getMinSize(), eRefObj == null ? null : eRefObj.getMinSize(),
				eDefObj == null ? null : eDefObj.getMinSize(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Legend.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateLegend(String name, EObject eParentObj, Legend eObj, Legend eRefObj, Legend eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetAnchor()) {
			if (eRefObj != null && eRefObj.isSetAnchor()) {
				eObj.setAnchor(eRefObj.getAnchor());
			} else if (eDefObj != null && eDefObj.isSetAnchor()) {
				eObj.setAnchor(eDefObj.getAnchor());
			}
		}

		if (!eObj.isSetStretch()) {
			if (eRefObj != null && eRefObj.isSetStretch()) {
				eObj.setStretch(eRefObj.getStretch());
			} else if (eDefObj != null && eDefObj.isSetStretch()) {
				eObj.setStretch(eDefObj.getStretch());
			}
		}

		if (!eObj.isSetRow()) {
			if (eRefObj != null && eRefObj.isSetRow()) {
				eObj.setRow(eRefObj.getRow());
			} else if (eDefObj != null && eDefObj.isSetRow()) {
				eObj.setRow(eDefObj.getRow());
			}
		}

		if (!eObj.isSetColumn()) {
			if (eRefObj != null && eRefObj.isSetColumn()) {
				eObj.setColumn(eRefObj.getColumn());
			} else if (eDefObj != null && eDefObj.isSetColumn()) {
				eObj.setColumn(eDefObj.getColumn());
			}
		}

		if (!eObj.isSetRowspan()) {
			if (eRefObj != null && eRefObj.isSetRowspan()) {
				eObj.setRowspan(eRefObj.getRowspan());
			} else if (eDefObj != null && eDefObj.isSetRowspan()) {
				eObj.setRowspan(eDefObj.getRowspan());
			}
		}

		if (!eObj.isSetColumnspan()) {
			if (eRefObj != null && eRefObj.isSetColumnspan()) {
				eObj.setColumnspan(eRefObj.getColumnspan());
			} else if (eDefObj != null && eDefObj.isSetColumnspan()) {
				eObj.setColumnspan(eDefObj.getColumnspan());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetWidthHint()) {
			if (eRefObj != null && eRefObj.isSetWidthHint()) {
				eObj.setWidthHint(eRefObj.getWidthHint());
			} else if (eDefObj != null && eDefObj.isSetWidthHint()) {
				eObj.setWidthHint(eDefObj.getWidthHint());
			}
		}

		if (!eObj.isSetHeightHint()) {
			if (eRefObj != null && eRefObj.isSetHeightHint()) {
				eObj.setHeightHint(eRefObj.getHeightHint());
			} else if (eDefObj != null && eDefObj.isSetHeightHint()) {
				eObj.setHeightHint(eDefObj.getHeightHint());
			}
		}

		if (!eObj.isSetHorizontalSpacing()) {
			if (eRefObj != null && eRefObj.isSetHorizontalSpacing()) {
				eObj.setHorizontalSpacing(eRefObj.getHorizontalSpacing());
			} else if (eDefObj != null && eDefObj.isSetHorizontalSpacing()) {
				eObj.setHorizontalSpacing(eDefObj.getHorizontalSpacing());
			}
		}

		if (!eObj.isSetVerticalSpacing()) {
			if (eRefObj != null && eRefObj.isSetVerticalSpacing()) {
				eObj.setVerticalSpacing(eRefObj.getVerticalSpacing());
			} else if (eDefObj != null && eDefObj.isSetVerticalSpacing()) {
				eObj.setVerticalSpacing(eDefObj.getVerticalSpacing());
			}
		}

		if (!eObj.isSetOrientation()) {
			if (eRefObj != null && eRefObj.isSetOrientation()) {
				eObj.setOrientation(eRefObj.getOrientation());
			} else if (eDefObj != null && eDefObj.isSetOrientation()) {
				eObj.setOrientation(eDefObj.getOrientation());
			}
		}

		if (!eObj.isSetDirection()) {
			if (eRefObj != null && eRefObj.isSetDirection()) {
				eObj.setDirection(eRefObj.getDirection());
			} else if (eDefObj != null && eDefObj.isSetDirection()) {
				eObj.setDirection(eDefObj.getDirection());
			}
		}

		if (!eObj.isSetPosition()) {
			if (eRefObj != null && eRefObj.isSetPosition()) {
				eObj.setPosition(eRefObj.getPosition());
			} else if (eDefObj != null && eDefObj.isSetPosition()) {
				eObj.setPosition(eDefObj.getPosition());
			}
		}

		if (!eObj.isSetItemType()) {
			if (eRefObj != null && eRefObj.isSetItemType()) {
				eObj.setItemType(eRefObj.getItemType());
			} else if (eDefObj != null && eDefObj.isSetItemType()) {
				eObj.setItemType(eDefObj.getItemType());
			}
		}

		if (!eObj.isSetTitlePosition()) {
			if (eRefObj != null && eRefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eRefObj.getTitlePosition());
			} else if (eDefObj != null && eDefObj.isSetTitlePosition()) {
				eObj.setTitlePosition(eDefObj.getTitlePosition());
			}
		}

		if (!eObj.isSetShowValue()) {
			if (eRefObj != null && eRefObj.isSetShowValue()) {
				eObj.setShowValue(eRefObj.isShowValue());
			} else if (eDefObj != null && eDefObj.isSetShowValue()) {
				eObj.setShowValue(eDefObj.isShowValue());
			}
		}

		if (!eObj.isSetShowPercent()) {
			if (eRefObj != null && eRefObj.isSetShowPercent()) {
				eObj.setShowPercent(eRefObj.isShowPercent());
			} else if (eDefObj != null && eDefObj.isSetShowPercent()) {
				eObj.setShowPercent(eDefObj.isShowPercent());
			}
		}

		if (!eObj.isSetShowTotal()) {
			if (eRefObj != null && eRefObj.isSetShowTotal()) {
				eObj.setShowTotal(eRefObj.isShowTotal());
			} else if (eDefObj != null && eDefObj.isSetShowTotal()) {
				eObj.setShowTotal(eDefObj.isShowTotal());
			}
		}

		if (!eObj.isSetWrappingSize()) {
			if (eRefObj != null && eRefObj.isSetWrappingSize()) {
				eObj.setWrappingSize(eRefObj.getWrappingSize());
			} else if (eDefObj != null && eDefObj.isSetWrappingSize()) {
				eObj.setWrappingSize(eDefObj.getWrappingSize());
			}
		}

		if (!eObj.isSetMaxPercent()) {
			if (eRefObj != null && eRefObj.isSetMaxPercent()) {
				eObj.setMaxPercent(eRefObj.getMaxPercent());
			} else if (eDefObj != null && eDefObj.isSetMaxPercent()) {
				eObj.setMaxPercent(eDefObj.getMaxPercent());
			}
		}

		if (!eObj.isSetTitlePercent()) {
			if (eRefObj != null && eRefObj.isSetTitlePercent()) {
				eObj.setTitlePercent(eRefObj.getTitlePercent());
			} else if (eDefObj != null && eDefObj.isSetTitlePercent()) {
				eObj.setTitlePercent(eDefObj.getTitlePercent());
			}
		}

		if (!eObj.isSetEllipsis()) {
			if (eRefObj != null && eRefObj.isSetEllipsis()) {
				eObj.setEllipsis(eRefObj.getEllipsis());
			} else if (eDefObj != null && eDefObj.isSetEllipsis()) {
				eObj.setEllipsis(eDefObj.getEllipsis());
			}
		}

		// list attributes

		// references
		int index_Legend_children = 0;
		for (Block element : eObj.getChildren()) {
			updateBlock("children", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getChildren().size() <= index_Legend_children) ? null
							: eRefObj.getChildren().get(index_Legend_children),
					(eDefObj == null || eDefObj.getChildren().size() <= index_Legend_children) ? null
							: eDefObj.getChildren().get(index_Legend_children),
					eDefOverride, checkVisible);
			index_Legend_children++;
		}

		updateBounds("bounds", eObj, eObj.getBounds(), eRefObj == null ? null : eRefObj.getBounds(),
				eDefObj == null ? null : eDefObj.getBounds(), eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);
		updateSize("minSize", eObj, eObj.getMinSize(), eRefObj == null ? null : eRefObj.getMinSize(),
				eDefObj == null ? null : eDefObj.getMinSize(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateClientArea("clientArea", eObj, eObj.getClientArea(), eRefObj == null ? null : eRefObj.getClientArea(),
				eDefObj == null ? null : eDefObj.getClientArea(), eDefOverride, checkVisible);
		updateText("text", eObj, eObj.getText(), eRefObj == null ? null : eRefObj.getText(),
				eDefObj == null ? null : eDefObj.getText(), eDefOverride, checkVisible);
		updateLineAttributes("separator", eObj, eObj.getSeparator(), eRefObj == null ? null : eRefObj.getSeparator(),
				eDefObj == null ? null : eDefObj.getSeparator(), eDefOverride, checkVisible);
		updateLabel("title", eObj, eObj.getTitle(), eRefObj == null ? null : eRefObj.getTitle(),
				eDefObj == null ? null : eDefObj.getTitle(), eDefOverride, checkVisible);
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Plot.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updatePlot(String name, EObject eParentObj, Plot eObj, Plot eRefObj, Plot eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetAnchor()) {
			if (eRefObj != null && eRefObj.isSetAnchor()) {
				eObj.setAnchor(eRefObj.getAnchor());
			} else if (eDefObj != null && eDefObj.isSetAnchor()) {
				eObj.setAnchor(eDefObj.getAnchor());
			}
		}

		if (!eObj.isSetStretch()) {
			if (eRefObj != null && eRefObj.isSetStretch()) {
				eObj.setStretch(eRefObj.getStretch());
			} else if (eDefObj != null && eDefObj.isSetStretch()) {
				eObj.setStretch(eDefObj.getStretch());
			}
		}

		if (!eObj.isSetRow()) {
			if (eRefObj != null && eRefObj.isSetRow()) {
				eObj.setRow(eRefObj.getRow());
			} else if (eDefObj != null && eDefObj.isSetRow()) {
				eObj.setRow(eDefObj.getRow());
			}
		}

		if (!eObj.isSetColumn()) {
			if (eRefObj != null && eRefObj.isSetColumn()) {
				eObj.setColumn(eRefObj.getColumn());
			} else if (eDefObj != null && eDefObj.isSetColumn()) {
				eObj.setColumn(eDefObj.getColumn());
			}
		}

		if (!eObj.isSetRowspan()) {
			if (eRefObj != null && eRefObj.isSetRowspan()) {
				eObj.setRowspan(eRefObj.getRowspan());
			} else if (eDefObj != null && eDefObj.isSetRowspan()) {
				eObj.setRowspan(eDefObj.getRowspan());
			}
		}

		if (!eObj.isSetColumnspan()) {
			if (eRefObj != null && eRefObj.isSetColumnspan()) {
				eObj.setColumnspan(eRefObj.getColumnspan());
			} else if (eDefObj != null && eDefObj.isSetColumnspan()) {
				eObj.setColumnspan(eDefObj.getColumnspan());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetWidthHint()) {
			if (eRefObj != null && eRefObj.isSetWidthHint()) {
				eObj.setWidthHint(eRefObj.getWidthHint());
			} else if (eDefObj != null && eDefObj.isSetWidthHint()) {
				eObj.setWidthHint(eDefObj.getWidthHint());
			}
		}

		if (!eObj.isSetHeightHint()) {
			if (eRefObj != null && eRefObj.isSetHeightHint()) {
				eObj.setHeightHint(eRefObj.getHeightHint());
			} else if (eDefObj != null && eDefObj.isSetHeightHint()) {
				eObj.setHeightHint(eDefObj.getHeightHint());
			}
		}

		if (!eObj.isSetHorizontalSpacing()) {
			if (eRefObj != null && eRefObj.isSetHorizontalSpacing()) {
				eObj.setHorizontalSpacing(eRefObj.getHorizontalSpacing());
			} else if (eDefObj != null && eDefObj.isSetHorizontalSpacing()) {
				eObj.setHorizontalSpacing(eDefObj.getHorizontalSpacing());
			}
		}

		if (!eObj.isSetVerticalSpacing()) {
			if (eRefObj != null && eRefObj.isSetVerticalSpacing()) {
				eObj.setVerticalSpacing(eRefObj.getVerticalSpacing());
			} else if (eDefObj != null && eDefObj.isSetVerticalSpacing()) {
				eObj.setVerticalSpacing(eDefObj.getVerticalSpacing());
			}
		}

		// list attributes

		// references
		int index_Plot_children = 0;
		for (Block element : eObj.getChildren()) {
			updateBlock("children", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getChildren().size() <= index_Plot_children) ? null
							: eRefObj.getChildren().get(index_Plot_children),
					(eDefObj == null || eDefObj.getChildren().size() <= index_Plot_children) ? null
							: eDefObj.getChildren().get(index_Plot_children),
					eDefOverride, checkVisible);
			index_Plot_children++;
		}

		updateBounds("bounds", eObj, eObj.getBounds(), eRefObj == null ? null : eRefObj.getBounds(),
				eDefObj == null ? null : eDefObj.getBounds(), eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);
		updateSize("minSize", eObj, eObj.getMinSize(), eRefObj == null ? null : eRefObj.getMinSize(),
				eDefObj == null ? null : eDefObj.getMinSize(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateClientArea("clientArea", eObj, eObj.getClientArea(), eRefObj == null ? null : eRefObj.getClientArea(),
				eDefObj == null ? null : eDefObj.getClientArea(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element TitleBlock.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateTitleBlock(String name, EObject eParentObj, TitleBlock eObj, TitleBlock eRefObj,
			TitleBlock eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetAnchor()) {
			if (eRefObj != null && eRefObj.isSetAnchor()) {
				eObj.setAnchor(eRefObj.getAnchor());
			} else if (eDefObj != null && eDefObj.isSetAnchor()) {
				eObj.setAnchor(eDefObj.getAnchor());
			}
		}

		if (!eObj.isSetStretch()) {
			if (eRefObj != null && eRefObj.isSetStretch()) {
				eObj.setStretch(eRefObj.getStretch());
			} else if (eDefObj != null && eDefObj.isSetStretch()) {
				eObj.setStretch(eDefObj.getStretch());
			}
		}

		if (!eObj.isSetRow()) {
			if (eRefObj != null && eRefObj.isSetRow()) {
				eObj.setRow(eRefObj.getRow());
			} else if (eDefObj != null && eDefObj.isSetRow()) {
				eObj.setRow(eDefObj.getRow());
			}
		}

		if (!eObj.isSetColumn()) {
			if (eRefObj != null && eRefObj.isSetColumn()) {
				eObj.setColumn(eRefObj.getColumn());
			} else if (eDefObj != null && eDefObj.isSetColumn()) {
				eObj.setColumn(eDefObj.getColumn());
			}
		}

		if (!eObj.isSetRowspan()) {
			if (eRefObj != null && eRefObj.isSetRowspan()) {
				eObj.setRowspan(eRefObj.getRowspan());
			} else if (eDefObj != null && eDefObj.isSetRowspan()) {
				eObj.setRowspan(eDefObj.getRowspan());
			}
		}

		if (!eObj.isSetColumnspan()) {
			if (eRefObj != null && eRefObj.isSetColumnspan()) {
				eObj.setColumnspan(eRefObj.getColumnspan());
			} else if (eDefObj != null && eDefObj.isSetColumnspan()) {
				eObj.setColumnspan(eDefObj.getColumnspan());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		if (!eObj.isSetWidthHint()) {
			if (eRefObj != null && eRefObj.isSetWidthHint()) {
				eObj.setWidthHint(eRefObj.getWidthHint());
			} else if (eDefObj != null && eDefObj.isSetWidthHint()) {
				eObj.setWidthHint(eDefObj.getWidthHint());
			}
		}

		if (!eObj.isSetHeightHint()) {
			if (eRefObj != null && eRefObj.isSetHeightHint()) {
				eObj.setHeightHint(eRefObj.getHeightHint());
			} else if (eDefObj != null && eDefObj.isSetHeightHint()) {
				eObj.setHeightHint(eDefObj.getHeightHint());
			}
		}

		if (!eObj.isSetAuto()) {
			if (eRefObj != null && eRefObj.isSetAuto()) {
				eObj.setAuto(eRefObj.isAuto());
			} else if (eDefObj != null && eDefObj.isSetAuto()) {
				eObj.setAuto(eDefObj.isAuto());
			}
		}

		// list attributes

		// references
		int index_TitleBlock_children = 0;
		for (Block element : eObj.getChildren()) {
			updateBlock("children", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getChildren().size() <= index_TitleBlock_children) ? null
							: eRefObj.getChildren().get(index_TitleBlock_children),
					(eDefObj == null || eDefObj.getChildren().size() <= index_TitleBlock_children) ? null
							: eDefObj.getChildren().get(index_TitleBlock_children),
					eDefOverride, checkVisible);
			index_TitleBlock_children++;
		}

		updateBounds("bounds", eObj, eObj.getBounds(), eRefObj == null ? null : eRefObj.getBounds(),
				eDefObj == null ? null : eDefObj.getBounds(), eDefOverride, checkVisible);
		updateInsets("insets", eObj, eObj.getInsets(), eRefObj == null ? null : eRefObj.getInsets(),
				eDefObj == null ? null : eDefObj.getInsets(), eDefOverride, checkVisible);
		updateSize("minSize", eObj, eObj.getMinSize(), eRefObj == null ? null : eRefObj.getMinSize(),
				eDefObj == null ? null : eDefObj.getMinSize(), eDefOverride, checkVisible);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);
		updateFill("background", eObj, eObj.getBackground(), eRefObj == null ? null : eRefObj.getBackground(),
				eDefObj == null ? null : eDefObj.getBackground(), eDefOverride, checkVisible);
		updateCursor("cursor", eObj, eObj.getCursor(), eRefObj == null ? null : eRefObj.getCursor(),
				eDefObj == null ? null : eDefObj.getCursor(), eDefOverride, checkVisible);
		updateLabel("label", eObj, eObj.getLabel(), eRefObj == null ? null : eRefObj.getLabel(),
				eDefObj == null ? null : eDefObj.getLabel(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Angle3D.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateAngle3D(String name, EObject eParentObj, Angle3D eObj, Angle3D eRefObj, Angle3D eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetXAngle()) {
			if (eRefObj != null && eRefObj.isSetXAngle()) {
				eObj.setXAngle(eRefObj.getXAngle());
			} else if (eDefObj != null && eDefObj.isSetXAngle()) {
				eObj.setXAngle(eDefObj.getXAngle());
			}
		}

		if (!eObj.isSetYAngle()) {
			if (eRefObj != null && eRefObj.isSetYAngle()) {
				eObj.setYAngle(eRefObj.getYAngle());
			} else if (eDefObj != null && eDefObj.isSetYAngle()) {
				eObj.setYAngle(eDefObj.getYAngle());
			}
		}

		if (!eObj.isSetZAngle()) {
			if (eRefObj != null && eRefObj.isSetZAngle()) {
				eObj.setZAngle(eRefObj.getZAngle());
			} else if (eDefObj != null && eDefObj.isSetZAngle()) {
				eObj.setZAngle(eDefObj.getZAngle());
			}
		}

		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element AxisOrigin.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateAxisOrigin(String name, EObject eParentObj, AxisOrigin eObj, AxisOrigin eRefObj,
			AxisOrigin eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		// list attributes

		// references
		if (eObj.getValue() == null) {
			if (eRefObj != null && eRefObj.getValue() != null) {
				eObj.setValue(eRefObj.getValue().copyInstance());
			} else if (eDefObj != null && eDefObj.getValue() != null) {
				eObj.setValue(eDefObj.getValue().copyInstance());
			}
		}

	}

	/**
	 * Updates chart element Bounds.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateBounds(String name, EObject eParentObj, Bounds eObj, Bounds eRefObj, Bounds eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetLeft()) {
			if (eRefObj != null && eRefObj.isSetLeft()) {
				eObj.setLeft(eRefObj.getLeft());
			} else if (eDefObj != null && eDefObj.isSetLeft()) {
				eObj.setLeft(eDefObj.getLeft());
			}
		}

		if (!eObj.isSetTop()) {
			if (eRefObj != null && eRefObj.isSetTop()) {
				eObj.setTop(eRefObj.getTop());
			} else if (eDefObj != null && eDefObj.isSetTop()) {
				eObj.setTop(eDefObj.getTop());
			}
		}

		if (!eObj.isSetWidth()) {
			if (eRefObj != null && eRefObj.isSetWidth()) {
				eObj.setWidth(eRefObj.getWidth());
			} else if (eDefObj != null && eDefObj.isSetWidth()) {
				eObj.setWidth(eDefObj.getWidth());
			}
		}

		if (!eObj.isSetHeight()) {
			if (eRefObj != null && eRefObj.isSetHeight()) {
				eObj.setHeight(eRefObj.getHeight());
			} else if (eDefObj != null && eDefObj.isSetHeight()) {
				eObj.setHeight(eDefObj.getHeight());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element ColorDefinition.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateColorDefinition(String name, EObject eParentObj, ColorDefinition eObj, ColorDefinition eRefObj,
			ColorDefinition eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (!eObj.isSetTransparency()) {
			if (eRefObj != null && eRefObj.isSetTransparency()) {
				eObj.setTransparency(eRefObj.getTransparency());
			} else if (eDefObj != null && eDefObj.isSetTransparency()) {
				eObj.setTransparency(eDefObj.getTransparency());
			}
		}

		if (!eObj.isSetRed()) {
			if (eRefObj != null && eRefObj.isSetRed()) {
				eObj.setRed(eRefObj.getRed());
			} else if (eDefObj != null && eDefObj.isSetRed()) {
				eObj.setRed(eDefObj.getRed());
			}
		}

		if (!eObj.isSetGreen()) {
			if (eRefObj != null && eRefObj.isSetGreen()) {
				eObj.setGreen(eRefObj.getGreen());
			} else if (eDefObj != null && eDefObj.isSetGreen()) {
				eObj.setGreen(eDefObj.getGreen());
			}
		}

		if (!eObj.isSetBlue()) {
			if (eRefObj != null && eRefObj.isSetBlue()) {
				eObj.setBlue(eRefObj.getBlue());
			} else if (eDefObj != null && eDefObj.isSetBlue()) {
				eObj.setBlue(eDefObj.getBlue());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Cursor.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateCursor(String name, EObject eParentObj, Cursor eObj, Cursor eRefObj, Cursor eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		// list attributes

		// references
		int index_Cursor_image = 0;
		for (Image element : eObj.getImage()) {
			updateImage("image", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getImage().size() <= index_Cursor_image) ? null
							: eRefObj.getImage().get(index_Cursor_image),
					(eDefObj == null || eDefObj.getImage().size() <= index_Cursor_image) ? null
							: eDefObj.getImage().get(index_Cursor_image),
					eDefOverride, checkVisible);
			index_Cursor_image++;
		}

	}

	/**
	 * Updates chart element DataPoint.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateDataPoint(String name, EObject eParentObj, DataPoint eObj, DataPoint eRefObj, DataPoint eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getPrefix() == null) {
			if (eRefObj != null && eRefObj.getPrefix() != null) {
				eObj.setPrefix(eRefObj.getPrefix());
			} else if (eDefObj != null && eDefObj.getPrefix() != null) {
				eObj.setPrefix(eDefObj.getPrefix());
			}
		}

		if (eObj.getSuffix() == null) {
			if (eRefObj != null && eRefObj.getSuffix() != null) {
				eObj.setSuffix(eRefObj.getSuffix());
			} else if (eDefObj != null && eDefObj.getSuffix() != null) {
				eObj.setSuffix(eDefObj.getSuffix());
			}
		}

		if (eObj.getSeparator() == null) {
			if (eRefObj != null && eRefObj.getSeparator() != null) {
				eObj.setSeparator(eRefObj.getSeparator());
			} else if (eDefObj != null && eDefObj.getSeparator() != null) {
				eObj.setSeparator(eDefObj.getSeparator());
			}
		}

		// list attributes

		// references
		if (eObj.getComponents().size() == 0) {
			if (eRefObj != null && eRefObj.getComponents().size() > 0) {
				eObj.getComponents().addAll(ChartElementUtil.copyInstance(eRefObj.getComponents()));
			} else if (eDefObj != null && eDefObj.getComponents().size() > 0) {
				eObj.getComponents().addAll(ChartElementUtil.copyInstance(eDefObj.getComponents()));
			}
		} else {
			for (DataPointComponent dpc : eObj.getComponents()) {
				DataPointComponentType type = dpc.getType();
				DataPointComponent subRef = null;
				DataPointComponent subDef = null;
				if (eRefObj != null) {
					for (DataPointComponent dpcRef : eRefObj.getComponents()) {
						if (type == dpcRef.getType()) {
							subRef = dpcRef;
							break;
						}
					}
				}
				if (eDefObj != null) {
					for (DataPointComponent dpcDef : eDefObj.getComponents()) {
						if (type == dpcDef.getType()) {
							subDef = dpcDef;
							break;
						}
					}
				}
				updateDataPointComponent("components", eObj, dpc, subRef, subDef, eDefOverride, checkVisible); //$NON-NLS-1$
			}
		}

	}

	/**
	 * Updates chart element DataPointComponent.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateDataPointComponent(String name, EObject eParentObj, DataPointComponent eObj,
			DataPointComponent eRefObj, DataPointComponent eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (!eObj.isSetOrthogonalType()) {
			if (eRefObj != null && eRefObj.isSetOrthogonalType()) {
				eObj.setOrthogonalType(eRefObj.getOrthogonalType());
			} else if (eDefObj != null && eDefObj.isSetOrthogonalType()) {
				eObj.setOrthogonalType(eDefObj.getOrthogonalType());
			}
		}

		// list attributes

		// references
		updateFormatSpecifier("formatSpecifier", eObj, eObj.getFormatSpecifier(),
				eRefObj == null ? null : eRefObj.getFormatSpecifier(),
				eDefObj == null ? null : eDefObj.getFormatSpecifier(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element EmbeddedImage.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateEmbeddedImage(String name, EObject eParentObj, EmbeddedImage eObj, EmbeddedImage eRefObj,
			EmbeddedImage eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getURL() == null) {
			if (eRefObj != null && eRefObj.getURL() != null) {
				eObj.setURL(eRefObj.getURL());
			} else if (eDefObj != null && eDefObj.getURL() != null) {
				eObj.setURL(eDefObj.getURL());
			}
		}

		if (!eObj.isSetSource()) {
			if (eRefObj != null && eRefObj.isSetSource()) {
				eObj.setSource(eRefObj.getSource());
			} else if (eDefObj != null && eDefObj.isSetSource()) {
				eObj.setSource(eDefObj.getSource());
			}
		}

		if (eObj.getData() == null) {
			if (eRefObj != null && eRefObj.getData() != null) {
				eObj.setData(eRefObj.getData());
			} else if (eDefObj != null && eDefObj.getData() != null) {
				eObj.setData(eDefObj.getData());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element ExtendedProperty.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateExtendedProperty(String name, EObject eParentObj, ExtendedProperty eObj, ExtendedProperty eRefObj,
			ExtendedProperty eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getName() == null) {
			if (eRefObj != null && eRefObj.getName() != null) {
				eObj.setName(eRefObj.getName());
			} else if (eDefObj != null && eDefObj.getName() != null) {
				eObj.setName(eDefObj.getName());
			}
		}

		if (eObj.getValue() == null) {
			if (eRefObj != null && eRefObj.getValue() != null) {
				eObj.setValue(eRefObj.getValue());
			} else if (eDefObj != null && eDefObj.getValue() != null) {
				eObj.setValue(eDefObj.getValue());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Fill.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateFill(String name, EObject eParentObj, Fill eObj, Fill eRefObj, Fill eDefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj != null || (eRefObj == null && eDefObj == null)) {
			return;
		} else {
			updateFillImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element Fill.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateFillImpl(String name, EObject eParentObj, Fill eObj, Fill eRefObj, Fill eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj != null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eRefObj != null) {
			Object v = eRefObj;
			if (eRefObj instanceof IChartObject) {
				v = eRefObj.copyInstance();
			}
			ChartElementUtil.setEObjectAttribute(eParentObj, name, v, false);
		} else if (eDefObj != null) {
			Object v = eDefObj;
			if (eDefObj instanceof IChartObject) {
				v = eDefObj.copyInstance();
			}
			ChartElementUtil.setEObjectAttribute(eParentObj, name, v, false);
		}
	}

	/**
	 * Updates chart element FontDefinition.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateFontDefinition(String name, EObject eParentObj, FontDefinition eObj, FontDefinition eRefObj,
			FontDefinition eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getName() == null) {
			if (eRefObj != null && eRefObj.getName() != null) {
				eObj.setName(eRefObj.getName());
			} else if (eDefObj != null && eDefObj.getName() != null) {
				eObj.setName(eDefObj.getName());
			}
		}

		if (!eObj.isSetSize()) {
			if (eRefObj != null && eRefObj.isSetSize()) {
				eObj.setSize(eRefObj.getSize());
			} else if (eDefObj != null && eDefObj.isSetSize()) {
				eObj.setSize(eDefObj.getSize());
			}
		}

		if (!eObj.isSetBold()) {
			if (eRefObj != null && eRefObj.isSetBold()) {
				eObj.setBold(eRefObj.isBold());
			} else if (eDefObj != null && eDefObj.isSetBold()) {
				eObj.setBold(eDefObj.isBold());
			}
		}

		if (!eObj.isSetItalic()) {
			if (eRefObj != null && eRefObj.isSetItalic()) {
				eObj.setItalic(eRefObj.isItalic());
			} else if (eDefObj != null && eDefObj.isSetItalic()) {
				eObj.setItalic(eDefObj.isItalic());
			}
		}

		if (!eObj.isSetStrikethrough()) {
			if (eRefObj != null && eRefObj.isSetStrikethrough()) {
				eObj.setStrikethrough(eRefObj.isStrikethrough());
			} else if (eDefObj != null && eDefObj.isSetStrikethrough()) {
				eObj.setStrikethrough(eDefObj.isStrikethrough());
			}
		}

		if (!eObj.isSetUnderline()) {
			if (eRefObj != null && eRefObj.isSetUnderline()) {
				eObj.setUnderline(eRefObj.isUnderline());
			} else if (eDefObj != null && eDefObj.isSetUnderline()) {
				eObj.setUnderline(eDefObj.isUnderline());
			}
		}

		if (!eObj.isSetWordWrap()) {
			if (eRefObj != null && eRefObj.isSetWordWrap()) {
				eObj.setWordWrap(eRefObj.isWordWrap());
			} else if (eDefObj != null && eDefObj.isSetWordWrap()) {
				eObj.setWordWrap(eDefObj.isWordWrap());
			}
		}

		if (!eObj.isSetRotation()) {
			if (eRefObj != null && eRefObj.isSetRotation()) {
				eObj.setRotation(eRefObj.getRotation());
			} else if (eDefObj != null && eDefObj.isSetRotation()) {
				eObj.setRotation(eDefObj.getRotation());
			}
		}

		// list attributes

		// references
		updateTextAlignment("alignment", eObj, eObj.getAlignment(), eRefObj == null ? null : eRefObj.getAlignment(),
				eDefObj == null ? null : eDefObj.getAlignment(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element FormatSpecifier.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateFormatSpecifier(String name, EObject eParentObj, FormatSpecifier eObj, FormatSpecifier eRefObj,
			FormatSpecifier eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj != null || (eRefObj == null && eDefObj == null)) {
			return;
		} else {
			updateFormatSpecifierImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element FormatSpecifier.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateFormatSpecifierImpl(String name, EObject eParentObj, FormatSpecifier eObj,
			FormatSpecifier eRefObj, FormatSpecifier eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj != null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eRefObj != null) {
			Object v = eRefObj;
			if (eRefObj instanceof IChartObject) {
				v = eRefObj.copyInstance();
			}
			ChartElementUtil.setEObjectAttribute(eParentObj, name, v, false);
		} else if (eDefObj != null) {
			Object v = eDefObj;
			if (eDefObj instanceof IChartObject) {
				v = eDefObj.copyInstance();
			}
			ChartElementUtil.setEObjectAttribute(eParentObj, name, v, false);
		}
	}

	/**
	 * Updates chart element Gradient.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateGradient(String name, EObject eParentObj, Gradient eObj, Gradient eRefObj, Gradient eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (!eObj.isSetDirection()) {
			if (eRefObj != null && eRefObj.isSetDirection()) {
				eObj.setDirection(eRefObj.getDirection());
			} else if (eDefObj != null && eDefObj.isSetDirection()) {
				eObj.setDirection(eDefObj.getDirection());
			}
		}

		if (!eObj.isSetCyclic()) {
			if (eRefObj != null && eRefObj.isSetCyclic()) {
				eObj.setCyclic(eRefObj.isCyclic());
			} else if (eDefObj != null && eDefObj.isSetCyclic()) {
				eObj.setCyclic(eDefObj.isCyclic());
			}
		}

		if (!eObj.isSetTransparency()) {
			if (eRefObj != null && eRefObj.isSetTransparency()) {
				eObj.setTransparency(eRefObj.getTransparency());
			} else if (eDefObj != null && eDefObj.isSetTransparency()) {
				eObj.setTransparency(eDefObj.getTransparency());
			}
		}

		// list attributes

		// references
		updateColorDefinition("startColor", eObj, eObj.getStartColor(),
				eRefObj == null ? null : eRefObj.getStartColor(), eDefObj == null ? null : eDefObj.getStartColor(),
				eDefOverride, checkVisible);
		updateColorDefinition("endColor", eObj, eObj.getEndColor(), eRefObj == null ? null : eRefObj.getEndColor(),
				eDefObj == null ? null : eDefObj.getEndColor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Image.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateImage(String name, EObject eParentObj, Image eObj, Image eRefObj, Image eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eObj instanceof EmbeddedImage) {
			updateEmbeddedImage(name, eParentObj, (EmbeddedImage) eObj, (EmbeddedImage) eRefObj,
					eDefObj instanceof EmbeddedImage ? (EmbeddedImage) eDefObj : null, eDefOverride, checkVisible);
		} else if (eObj instanceof PatternImage) {
			updatePatternImage(name, eParentObj, (PatternImage) eObj, (PatternImage) eRefObj,
					eDefObj instanceof PatternImage ? (PatternImage) eDefObj : null, eDefOverride, checkVisible);
		} else {
			updateImageImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element Image.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateImageImpl(String name, EObject eParentObj, Image eObj, Image eRefObj, Image eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getURL() == null) {
			if (eRefObj != null && eRefObj.getURL() != null) {
				eObj.setURL(eRefObj.getURL());
			} else if (eDefObj != null && eDefObj.getURL() != null) {
				eObj.setURL(eDefObj.getURL());
			}
		}

		if (!eObj.isSetSource()) {
			if (eRefObj != null && eRefObj.isSetSource()) {
				eObj.setSource(eRefObj.getSource());
			} else if (eDefObj != null && eDefObj.isSetSource()) {
				eObj.setSource(eDefObj.getSource());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Insets.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateInsets(String name, EObject eParentObj, Insets eObj, Insets eRefObj, Insets eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetTop()) {
			if (eRefObj != null && eRefObj.isSetTop()) {
				eObj.setTop(eRefObj.getTop());
			} else if (eDefObj != null && eDefObj.isSetTop()) {
				eObj.setTop(eDefObj.getTop());
			}
		}

		if (!eObj.isSetLeft()) {
			if (eRefObj != null && eRefObj.isSetLeft()) {
				eObj.setLeft(eRefObj.getLeft());
			} else if (eDefObj != null && eDefObj.isSetLeft()) {
				eObj.setLeft(eDefObj.getLeft());
			}
		}

		if (!eObj.isSetBottom()) {
			if (eRefObj != null && eRefObj.isSetBottom()) {
				eObj.setBottom(eRefObj.getBottom());
			} else if (eDefObj != null && eDefObj.isSetBottom()) {
				eObj.setBottom(eDefObj.getBottom());
			}
		}

		if (!eObj.isSetRight()) {
			if (eRefObj != null && eRefObj.isSetRight()) {
				eObj.setRight(eRefObj.getRight());
			} else if (eDefObj != null && eDefObj.isSetRight()) {
				eObj.setRight(eDefObj.getRight());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Interactivity.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateInteractivity(String name, EObject eParentObj, Interactivity eObj, Interactivity eRefObj,
			Interactivity eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetEnable()) {
			if (eRefObj != null && eRefObj.isSetEnable()) {
				eObj.setEnable(eRefObj.isEnable());
			} else if (eDefObj != null && eDefObj.isSetEnable()) {
				eObj.setEnable(eDefObj.isEnable());
			}
		}

		if (!eObj.isSetLegendBehavior()) {
			if (eRefObj != null && eRefObj.isSetLegendBehavior()) {
				eObj.setLegendBehavior(eRefObj.getLegendBehavior());
			} else if (eDefObj != null && eDefObj.isSetLegendBehavior()) {
				eObj.setLegendBehavior(eDefObj.getLegendBehavior());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element LineAttributes.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateLineAttributes(String name, EObject eParentObj, LineAttributes eObj, LineAttributes eRefObj,
			LineAttributes eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetStyle()) {
			if (eRefObj != null && eRefObj.isSetStyle()) {
				eObj.setStyle(eRefObj.getStyle());
			} else if (eDefObj != null && eDefObj.isSetStyle()) {
				eObj.setStyle(eDefObj.getStyle());
			}
		}

		if (!eObj.isSetThickness()) {
			if (eRefObj != null && eRefObj.isSetThickness()) {
				eObj.setThickness(eRefObj.getThickness());
			} else if (eDefObj != null && eDefObj.isSetThickness()) {
				eObj.setThickness(eDefObj.getThickness());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		// list attributes

		// references
		updateColorDefinition("color", eObj, eObj.getColor(), eRefObj == null ? null : eRefObj.getColor(),
				eDefObj == null ? null : eDefObj.getColor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Location.
	 *
	 * @param name         name chart element type.
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated
	 */
	public void updateLocation(String name, EObject eParentObj, Location eObj, Location eRefObj, Location eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		if (eObj instanceof Location3D) {
			updateLocation3D(name, eParentObj, (Location3D) eObj, (Location3D) eRefObj,
					eDefObj instanceof Location3D ? (Location3D) eDefObj : null, eDefOverride, checkVisible);
		} else {
			updateLocationImpl(name, eParentObj, eObj, eRefObj, eDefObj, eDefOverride, checkVisible);
		}
	}

	/**
	 * Updates chart element Location.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateLocationImpl(String name, EObject eParentObj, Location eObj, Location eRefObj,
			Location eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetX()) {
			if (eRefObj != null && eRefObj.isSetX()) {
				eObj.setX(eRefObj.getX());
			} else if (eDefObj != null && eDefObj.isSetX()) {
				eObj.setX(eDefObj.getX());
			}
		}

		if (!eObj.isSetY()) {
			if (eRefObj != null && eRefObj.isSetY()) {
				eObj.setY(eRefObj.getY());
			} else if (eDefObj != null && eDefObj.isSetY()) {
				eObj.setY(eDefObj.getY());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Location3D.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateLocation3D(String name, EObject eParentObj, Location3D eObj, Location3D eRefObj,
			Location3D eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetX()) {
			if (eRefObj != null && eRefObj.isSetX()) {
				eObj.setX(eRefObj.getX());
			} else if (eDefObj != null && eDefObj.isSetX()) {
				eObj.setX(eDefObj.getX());
			}
		}

		if (!eObj.isSetY()) {
			if (eRefObj != null && eRefObj.isSetY()) {
				eObj.setY(eRefObj.getY());
			} else if (eDefObj != null && eDefObj.isSetY()) {
				eObj.setY(eDefObj.getY());
			}
		}

		if (!eObj.isSetZ()) {
			if (eRefObj != null && eRefObj.isSetZ()) {
				eObj.setZ(eRefObj.getZ());
			} else if (eDefObj != null && eDefObj.isSetZ()) {
				eObj.setZ(eDefObj.getZ());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Marker.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateMarker(String name, EObject eParentObj, Marker eObj, Marker eRefObj, Marker eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// Pro-process 'visible' case, if current element is invisible, no need to
		// update other attributes.;
		if (checkVisible) {
			if (eObj.isSetVisible()) {
				if (!eObj.isVisible()) {
					// If the visible attribute of reference obj is false, directly return, no need
					// to udpate other attributes.;
					return;
				}
			} else if (eRefObj != null && eRefObj.isSetVisible()) {
				if (!eRefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				if (!eDefObj.isVisible()) {
					eObj.setVisible(false);
					return;
				}
			}
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (!eObj.isSetSize()) {
			if (eRefObj != null && eRefObj.isSetSize()) {
				eObj.setSize(eRefObj.getSize());
			} else if (eDefObj != null && eDefObj.isSetSize()) {
				eObj.setSize(eDefObj.getSize());
			}
		}

		if (!eObj.isSetVisible()) {
			if (eRefObj != null && eRefObj.isSetVisible()) {
				eObj.setVisible(eRefObj.isVisible());
			} else if (eDefObj != null && eDefObj.isSetVisible()) {
				eObj.setVisible(eDefObj.isVisible());
			}
		}

		// list attributes

		// references
		updateFill("fill", eObj, eObj.getFill(), eRefObj == null ? null : eRefObj.getFill(),
				eDefObj == null ? null : eDefObj.getFill(), eDefOverride, checkVisible);
		updatePalette("iconPalette", eObj, eObj.getIconPalette(), eRefObj == null ? null : eRefObj.getIconPalette(),
				eDefObj == null ? null : eDefObj.getIconPalette(), eDefOverride, checkVisible, 0, 0);
		updateLineAttributes("outline", eObj, eObj.getOutline(), eRefObj == null ? null : eRefObj.getOutline(),
				eDefObj == null ? null : eDefObj.getOutline(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element MultipleFill.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updateMultipleFill(String name, EObject eParentObj, MultipleFill eObj, MultipleFill eRefObj,
			MultipleFill eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		// list attributes

		// references
		int index_MultipleFill_fills = 0;
		for (Fill element : eObj.getFills()) {
			updateFill("fills", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getFills().size() <= index_MultipleFill_fills) ? null
							: eRefObj.getFills().get(index_MultipleFill_fills),
					(eDefObj == null || eDefObj.getFills().size() <= index_MultipleFill_fills) ? null
							: eDefObj.getFills().get(index_MultipleFill_fills),
					eDefOverride, checkVisible);
			index_MultipleFill_fills++;
		}

	}

	/**
	 * Updates chart element Palette.
	 *
	 * @param eObj           chart element object.
	 * @param eRefObj        reference chart element object.
	 * @param eDefObj        default chart element object.
	 * @param eDefOverride   indicates if using default object to override target
	 *                       object if target is null.
	 * @param checkVisible   indicates if still checking visible of chart element
	 *                       before updating properties of chart element.
	 * @param axisIndex      index of axis.
	 * @param seriesDefIndex index of series definition.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updatePalette(String name, EObject eParentObj, Palette eObj, Palette eRefObj, Palette eDefObj,
			boolean eDefOverride, boolean checkVisible, int axisIndex, int seriesDefIndex) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getName() == null) {
			if (eRefObj != null && eRefObj.getName() != null) {
				eObj.setName(eRefObj.getName());
			} else if (eDefObj != null && eDefObj.getName() != null) {
				eObj.setName(eDefObj.getName());
			}
		}

		// list attributes

		// references
		if (eObj.getEntries().size() > 0) {
			return;
		}
		if (eRefObj != null && eRefObj.getEntries().size() > 0) {
			Palette p = eRefObj.copyInstance();
			ChartDefaultValueUtil.shiftPaletteColors(p, (axisIndex + seriesDefIndex) * -1);
			eObj.getEntries().addAll(p.getEntries());
		} else if (eDefObj != null) {
			Palette p = eDefObj.copyInstance();
			ChartDefaultValueUtil.shiftPaletteColors(p, -axisIndex);
			eObj.getEntries().addAll(p.getEntries());
		}

	}

	/**
	 * Updates chart element PatternImage.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	protected void updatePatternImage(String name, EObject eParentObj, PatternImage eObj, PatternImage eRefObj,
			PatternImage eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetType()) {
			if (eRefObj != null && eRefObj.isSetType()) {
				eObj.setType(eRefObj.getType());
			} else if (eDefObj != null && eDefObj.isSetType()) {
				eObj.setType(eDefObj.getType());
			}
		}

		if (eObj.getURL() == null) {
			if (eRefObj != null && eRefObj.getURL() != null) {
				eObj.setURL(eRefObj.getURL());
			} else if (eDefObj != null && eDefObj.getURL() != null) {
				eObj.setURL(eDefObj.getURL());
			}
		}

		if (!eObj.isSetSource()) {
			if (eRefObj != null && eRefObj.isSetSource()) {
				eObj.setSource(eRefObj.getSource());
			} else if (eDefObj != null && eDefObj.isSetSource()) {
				eObj.setSource(eDefObj.getSource());
			}
		}

		if (!eObj.isSetBitmap()) {
			if (eRefObj != null && eRefObj.isSetBitmap()) {
				eObj.setBitmap(eRefObj.getBitmap());
			} else if (eDefObj != null && eDefObj.isSetBitmap()) {
				eObj.setBitmap(eDefObj.getBitmap());
			}
		}

		// list attributes

		// references
		updateColorDefinition("foreColor", eObj, eObj.getForeColor(), eRefObj == null ? null : eRefObj.getForeColor(),
				eDefObj == null ? null : eDefObj.getForeColor(), eDefOverride, checkVisible);
		updateColorDefinition("backColor", eObj, eObj.getBackColor(), eRefObj == null ? null : eRefObj.getBackColor(),
				eDefObj == null ? null : eDefObj.getBackColor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Rotation3D.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateRotation3D(String name, EObject eParentObj, Rotation3D eObj, Rotation3D eRefObj,
			Rotation3D eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		// list attributes

		// references
		int index_Rotation3D_angles = 0;
		for (Angle3D element : eObj.getAngles()) {
			updateAngle3D("angles", eObj, element, //$NON-NLS-1$
					(eRefObj == null || eRefObj.getAngles().size() <= index_Rotation3D_angles) ? null
							: eRefObj.getAngles().get(index_Rotation3D_angles),
					(eDefObj == null || eDefObj.getAngles().size() <= index_Rotation3D_angles) ? null
							: eDefObj.getAngles().get(index_Rotation3D_angles),
					eDefOverride, checkVisible);
			index_Rotation3D_angles++;
		}

	}

	/**
	 * Updates chart element Size.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateSize(String name, EObject eParentObj, Size eObj, Size eRefObj, Size eDefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetHeight()) {
			if (eRefObj != null && eRefObj.isSetHeight()) {
				eObj.setHeight(eRefObj.getHeight());
			} else if (eDefObj != null && eDefObj.isSetHeight()) {
				eObj.setHeight(eDefObj.getHeight());
			}
		}

		if (!eObj.isSetWidth()) {
			if (eRefObj != null && eRefObj.isSetWidth()) {
				eObj.setWidth(eRefObj.getWidth());
			} else if (eDefObj != null && eDefObj.isSetWidth()) {
				eObj.setWidth(eDefObj.getWidth());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Updates chart element Style.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateStyle(String name, EObject eParentObj, Style eObj, Style eRefObj, Style eDefObj,
			boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		// list attributes

		// references
		updateFontDefinition("font", eObj, eObj.getFont(), eRefObj == null ? null : eRefObj.getFont(),
				eDefObj == null ? null : eDefObj.getFont(), eDefOverride, checkVisible);
		updateColorDefinition("color", eObj, eObj.getColor(), eRefObj == null ? null : eRefObj.getColor(),
				eDefObj == null ? null : eDefObj.getColor(), eDefOverride, checkVisible);
		updateColorDefinition("backgroundColor", eObj, eObj.getBackgroundColor(),
				eRefObj == null ? null : eRefObj.getBackgroundColor(),
				eDefObj == null ? null : eDefObj.getBackgroundColor(), eDefOverride, checkVisible);
		updateImage("backgroundImage", eObj, eObj.getBackgroundImage(),
				eRefObj == null ? null : eRefObj.getBackgroundImage(),
				eDefObj == null ? null : eDefObj.getBackgroundImage(), eDefOverride, checkVisible);
		updateInsets("padding", eObj, eObj.getPadding(), eRefObj == null ? null : eRefObj.getPadding(),
				eDefObj == null ? null : eDefObj.getPadding(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element Text.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateText(String name, EObject eParentObj, Text eObj, Text eRefObj, Text eDefObj, boolean eDefOverride,
			boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (eObj.getValue() == null) {
			if (eRefObj != null && eRefObj.getValue() != null) {
				eObj.setValue(eRefObj.getValue());
			} else if (eDefObj != null && eDefObj.getValue() != null) {
				eObj.setValue(eDefObj.getValue());
			}
		}

		// list attributes

		// references
		updateFontDefinition("font", eObj, eObj.getFont(), eRefObj == null ? null : eRefObj.getFont(),
				eDefObj == null ? null : eDefObj.getFont(), eDefOverride, checkVisible);
		updateColorDefinition("color", eObj, eObj.getColor(), eRefObj == null ? null : eRefObj.getColor(),
				eDefObj == null ? null : eDefObj.getColor(), eDefOverride, checkVisible);

	}

	/**
	 * Updates chart element TextAlignment.
	 *
	 * @param eObj         chart element object.
	 * @param eRefObj      reference chart element object.
	 * @param eDefObj      default chart element object.
	 * @param eDefOverride indicates if using default object to override target
	 *                     object if target is null.
	 * @param checkVisible indicates if still checking visible of chart element
	 *                     before updating properties of chart element.
	 *
	 * @generated Don't change this method manually.
	 */
	public void updateTextAlignment(String name, EObject eParentObj, TextAlignment eObj, TextAlignment eRefObj,
			TextAlignment eDefObj, boolean eDefOverride, boolean checkVisible) {
		if (eObj == null) {
			if (eRefObj != null) {
				eObj = eRefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
			} else if (eDefOverride && eDefObj != null) {
				eObj = eDefObj.copyInstance();
				ChartElementUtil.setEObjectAttribute(eParentObj, name, eObj, false);
				return;
			}
		}
		if (eObj == null || (eRefObj == null && eDefObj == null)) {
			return;
		}

		// attributes
		if (!eObj.isSetHorizontalAlignment()) {
			if (eRefObj != null && eRefObj.isSetHorizontalAlignment()) {
				eObj.setHorizontalAlignment(eRefObj.getHorizontalAlignment());
			} else if (eDefObj != null && eDefObj.isSetHorizontalAlignment()) {
				eObj.setHorizontalAlignment(eDefObj.getHorizontalAlignment());
			}
		}

		if (!eObj.isSetVerticalAlignment()) {
			if (eRefObj != null && eRefObj.isSetVerticalAlignment()) {
				eObj.setVerticalAlignment(eRefObj.getVerticalAlignment());
			} else if (eDefObj != null && eDefObj.isSetVerticalAlignment()) {
				eObj.setVerticalAlignment(eDefObj.getVerticalAlignment());
			}
		}

		// list attributes

		// references

	}

	/**
	 * Generates series ID according to specified series.
	 *
	 * @param series chart series object.
	 *
	 * @generated
	 */
	protected String getSeriesID(Series series) {
		String simpleName = series.getClass().getSimpleName();
		if (simpleName.endsWith("Impl")) {
			simpleName = simpleName.substring(0, simpleName.indexOf("Impl"));
		}
		return simpleName;
	}

	/**
	 * Get valid index reference.
	 *
	 * @param obj       the parent object which contains list of specific object.
	 * @param attribute attribute name of specific object.
	 * @param index     the index of specific object.
	 * @param element   the target object.
	 *
	 * @generated
	 */
	private <T> T getValidIndexRef(EObject obj, String attribute, int index, T element) {
		if (obj == null) {
			return null;
		}

		try {
			String methodName = "get" + attribute.substring(0, 1).toUpperCase() + attribute.substring(1);
			Method m = obj.getClass().getMethod(methodName);
			List<T> mList = (List<T>) m.invoke(obj);
			if ((mList.size() - 1) >= index) {
				return mList.get(index);
			} else if (mList.size() == 1) {
				return mList.get(0);
			}
		} catch (Exception e) {
			// Do nothing.;
		}
		return null;
	}

}
