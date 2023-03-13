/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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

package org.eclipse.birt.chart.reportitem.ui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.reportitem.ui.dialogs.ChartExpressionProvider;
import org.eclipse.birt.chart.ui.swt.interfaces.IUIServiceProvider;
import org.eclipse.birt.chart.util.ChartUtil;
import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.figures.ReportElementFigure;
import org.eclipse.birt.report.designer.internal.ui.util.DataUtil;
import org.eclipse.birt.report.designer.internal.ui.util.ExpressionUtility;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.util.ColorManager;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.ImageManager;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.extension.ExtendedElementException;
import org.eclipse.birt.report.model.api.extension.IReportItem;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.api.util.ColorUtil;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;

/**
 * ChartReportItemUIUtil
 *
 * @since 2.5.3
 */

public class ChartReportItemUIUtil {

	/**
	 * Creates chart filter factory instance according to specified item handle.
	 *
	 * @param item
	 * @return filter factory
	 * @throws ExtendedElementException
	 */
	public static ChartFilterFactory createChartFilterFactory(Object item) throws ExtendedElementException {
		if (item instanceof ExtendedItemHandle) {
			return getChartFilterFactory(((ExtendedItemHandle) item).getReportItem());
		} else if (item instanceof IReportItem) {
			return createChartFilterFactory(item);
		}
		return new ChartFilterFactory();
	}

	private static ChartFilterFactory getChartFilterFactory(IReportItem adaptableObj) {
		ChartFilterFactory factory = ChartUtil.getAdapter(adaptableObj, ChartFilterFactory.class);
		if (factory != null) {
			return factory;
		}

		return new ChartFilterFactory();
	}

	/**
	 * Returns the categories list in BIRT chart expression builder
	 *
	 * @param builderCommand
	 * @return category style
	 */
	public static int getExpressionBuilderStyle(int builderCommand) {
		if (builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_DATA_BINDINGS) {
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_CHART_DATAPOINTS) {
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_SCRIPT_DATAPOINTS) {
			// Script doesn't support column binding expression.
			return ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		} else if (builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_TRIGGERS_SIMPLE) {
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TRIGGERS_SIMPLE for un-dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT;
		} else if (builderCommand == IUIServiceProvider.COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS) {
			// Bugzilla#202386: Tooltips never support chart
			// variables. Use COMMAND_EXPRESSION_TOOLTIPS_DATAPOINTS for dp
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_CUBE_EXPRESSION_TOOLTIPS_DATAPOINTS) {
			return ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_HYPERLINK) {
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS) {
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_COLUMN_BINDINGS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_HYPERLINK_DATAPOINTS_SIMPLE) {
			// Used for data cube case, no column bindings allowed
			return ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_DATA_POINTS;
		} else if (builderCommand == IUIServiceProvider.COMMAND_HYPERLINK_LEGEND) {
			// Add Legend item variables and remove column bindings
			return ChartExpressionProvider.CATEGORY_WITH_LEGEND_ITEMS
					| ChartExpressionProvider.CATEGORY_WITH_REPORT_PARAMS
					| ChartExpressionProvider.CATEGORY_WITH_JAVASCRIPT
					| ChartExpressionProvider.CATEGORY_WITH_BIRT_VARIABLES;
		}
		return ChartExpressionProvider.CATEGORY_BASE;
	}

	/**
	 * Get background image setting from design element handle.
	 *
	 * @param handle The handle of design element.
	 * @return background image
	 */
	public static String getBackgroundImage(DesignElementHandle handle) {
		return handle.getStringProperty(IStyleModel.BACKGROUND_IMAGE_PROP);
	}

	/**
	 * Get background position settings from design element handle.
	 *
	 * @param handle The handle of design element.
	 * @return background position
	 */
	public static Object[] getBackgroundPosition(DesignElementHandle handle) {
		Object x = null;
		Object y = null;

		if (handle != null) {
			Object px = handle.getProperty(IStyleModel.BACKGROUND_POSITION_X_PROP);
			Object py = handle.getProperty(IStyleModel.BACKGROUND_POSITION_Y_PROP);

			if (px instanceof String) {
				x = px;
			} else if (px instanceof DimensionValue) {
				// {0%,0%}
				if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(((DimensionValue) px).getUnits())) {
					x = px;
				} else {
					// {1cm,1cm}
					x = Integer.valueOf((int) DEUtil.convertoToPixel(px));
				}
			}

			if (py instanceof String) {
				y = py;
			} else if (py instanceof DimensionValue) {
				// {0%,0%}
				if (DesignChoiceConstants.UNITS_PERCENTAGE.equals(((DimensionValue) py).getUnits())) {
					y = py;
				} else {
					// {1cm,1cm}
					y = Integer.valueOf((int) DEUtil.convertoToPixel(py));
				}
			}
		}
		return new Object[] { x, y };
	}

	/**
	 * Get background repeat property from design element handle.
	 *
	 * @param handle The handle of design element.
	 * @return background repeat property
	 */
	public static int getBackgroundRepeat(DesignElementHandle handle) {
		return getRepeat(handle.getStringProperty(IStyleModel.BACKGROUND_REPEAT_PROP));
	}

	/**
	 * Get repeat identifier according to its value
	 *
	 * @param repeat Given string
	 * @return The repeat value
	 */
	public static int getRepeat(String repeat) {
		if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_X.equals(repeat)) {
			return 1;
		} else if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT_Y.equals(repeat)) {
			return 2;
		} else if (DesignChoiceConstants.BACKGROUND_REPEAT_REPEAT.equals(repeat)) {
			return 3;
		}
		return 0;
	}

	/**
	 * Generate computed columns for the given report item with the closest data set
	 * available.
	 *
	 * @param handle
	 * @param dataSetHandle Data Set. No aggregation created.
	 *
	 * @return true if succeed,or fail if no column generated.
	 * @throws SemanticException
	 * @see DataUtil#generateComputedColumns(ReportItemHandle)
	 *
	 */
	@SuppressWarnings("unchecked")
	public static List<ComputedColumn> generateComputedColumns(ReportItemHandle handle, DataSetHandle dataSetHandle)
			throws SemanticException {
		if (dataSetHandle != null) {
			List<ResultSetColumnHandle> resultSetColumnList = DataUtil.getColumnList(dataSetHandle);
			List<ComputedColumn> columnList = new ArrayList<>();
			for (ResultSetColumnHandle resultSetColumn : resultSetColumnList) {
				ComputedColumn column = StructureFactory.newComputedColumn(handle, resultSetColumn.getColumnName());
				column.setDataType(resultSetColumn.getDataType());
				ExpressionUtility.setBindingColumnExpression(resultSetColumn, column);

				column.setDisplayName(UIUtil.getColumnDisplayName(resultSetColumn));
				String displayKey = UIUtil.getColumnDisplayNameKey(resultSetColumn);
				if (displayKey != null) {
					column.setDisplayNameID(displayKey);
				}

				columnList.add(column);
			}
			return columnList;
		}
		return Collections.emptyList();
	}

	/**
	 * Refresh background including color and image.
	 *
	 * @param handle Item handle
	 * @param figure Element figure
	 */
	public static void refreshBackground(ExtendedItemHandle handle, ReportElementFigure figure) {
		refreshBackgroundColor(handle, figure);
		refreshBackgroundImage(handle, figure);
	}

	/**
	 * Refresh background image.
	 *
	 * @param handle Item handle
	 * @param figure Element figure
	 */
	public static void refreshBackgroundImage(ExtendedItemHandle handle, ReportElementFigure figure) {
		String backGroundImage = ChartReportItemUIUtil.getBackgroundImage(handle);

		if (backGroundImage == null) {
			figure.setImage(null);
		} else {
			Image image = null;
			String imageSourceType = DesignChoiceConstants.IMAGE_REF_TYPE_EMBED;
			Object obj = handle.getProperty(IStyleModel.BACKGROUND_IMAGE_TYPE_PROP);
			if (obj instanceof String) {
				imageSourceType = obj.toString();
			}
			try {
				if (imageSourceType.equalsIgnoreCase(DesignChoiceConstants.IMAGE_REF_TYPE_EMBED)) {
					// embedded image
					image = ImageManager.getInstance().getEmbeddedImage(handle.getModuleHandle(), backGroundImage);
				} else {
					// URL image
					image = ImageManager.getInstance().getImage(handle.getModuleHandle(), backGroundImage);
				}
			} catch (SWTException e) {
				// Should not be ExceptionHandler.handle(e), see SCR#73730
				image = null;
			}

			if (image == null) {
				figure.setImage(null);
				return;
			}

			figure.setImage(image);

			Object[] backGroundPosition = ChartReportItemUIUtil.getBackgroundPosition(handle);
			int backGroundRepeat = ChartReportItemUIUtil.getBackgroundRepeat(handle);

			figure.setRepeat(backGroundRepeat);

			Object xPosition = backGroundPosition[0];
			Object yPosition = backGroundPosition[1];
			Rectangle area = figure.getClientArea();
			org.eclipse.swt.graphics.Rectangle imageArea = image.getBounds();
			Point position = new Point(-1, -1);
			int alignment = 0;

			if (xPosition instanceof Integer) {
				position.x = ((Integer) xPosition).intValue();
			} else if (xPosition instanceof DimensionValue) {
				int percentX = (int) ((DimensionValue) xPosition).getMeasure();

				position.x = (area.width - imageArea.width) * percentX / 100;
			} else if (xPosition instanceof String) {
				alignment |= DesignElementHandleAdapter.getPosition((String) xPosition);
			}

			if (yPosition instanceof Integer) {
				position.y = ((Integer) yPosition).intValue();
			} else if (yPosition instanceof DimensionValue) {
				int percentY = (int) ((DimensionValue) yPosition).getMeasure();

				position.y = (area.width - imageArea.width) * percentY / 100;
			} else if (yPosition instanceof String) {
				alignment |= DesignElementHandleAdapter.getPosition((String) yPosition);
			}

			figure.setAlignment(alignment);
			figure.setPosition(position);
		}
	}

	/**
	 * Refresh background color.
	 *
	 * @param handle Item handle
	 * @param figure Figure
	 */
	public static void refreshBackgroundColor(ExtendedItemHandle handle, IFigure figure) {
		Object obj = handle.getProperty(IStyleModel.BACKGROUND_COLOR_PROP);

		figure.setOpaque(false);

		if (obj != null) {
			int color = 0xFFFFFF;
			if (obj instanceof String) {
				color = ColorUtil.parseColor((String) obj);
			} else {
				color = ((Integer) obj).intValue();
			}
			figure.setBackgroundColor(ColorManager.getColor(color));
			figure.setOpaque(true);
		}
	}
}
