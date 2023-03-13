/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.item.crosstab.core.de;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * AggregationCellHandle.
 */
public class AggregationCellHandle extends CrosstabCellHandle implements IAggregationCellConstants, ICrosstabConstants {

	/**
	 *
	 * @param handle
	 */
	AggregationCellHandle(DesignElementHandle handle) {
		super(handle);
	}

	/**
	 * Gets the referred row cube level of this aggregation applied on.
	 *
	 * @return the referred row cube level
	 */
	public LevelHandle getAggregationOnRow() {
		LevelHandle levelHandle = (LevelHandle) handle.getElementProperty(AGGREGATION_ON_ROW_PROP);
		if (levelHandle == null) {
			String fullLevelName = handle.getStringProperty(AGGREGATION_ON_ROW_PROP);
			levelHandle = findLevelHandle(fullLevelName);
		}

		return levelHandle;
	}

	/**
	 * Gets the referred column cube level of this aggregation applied on.
	 *
	 * @return the referred column cube level
	 */
	public LevelHandle getAggregationOnColumn() {
		LevelHandle levelHandle = (LevelHandle) handle.getElementProperty(AGGREGATION_ON_COLUMN_PROP);
		if (levelHandle == null) {
			String fullLevelName = handle.getStringProperty(AGGREGATION_ON_COLUMN_PROP);
			levelHandle = findLevelHandle(fullLevelName);
		}

		return levelHandle;
	}

	/**
	 * Gets the referred row cube level of this aggregation cell to span over.
	 *
	 * @return the referred row cube level
	 */
	public LevelHandle getSpanOverOnRow() {
		LevelHandle levelHandle = (LevelHandle) handle.getElementProperty(SPAN_OVER_ON_ROW_PROP);
		if (levelHandle == null) {
			String fullLevelName = handle.getStringProperty(SPAN_OVER_ON_ROW_PROP);
			levelHandle = findLevelHandle(fullLevelName);
		}

		return levelHandle;
	}

	/**
	 * Gets the referred column cube level of this aggregation cell to span over.
	 *
	 * @return the referred column cube level
	 */
	public LevelHandle getSpanOverOnColumn() {
		LevelHandle levelHandle = (LevelHandle) handle.getElementProperty(SPAN_OVER_ON_COLUMN_PROP);
		if (levelHandle == null) {
			String fullLevelName = handle.getStringProperty(SPAN_OVER_ON_COLUMN_PROP);
			levelHandle = findLevelHandle(fullLevelName);
		}

		return levelHandle;
	}

	/**
	 * Set the referred row cube level of this aggregation cell to span over.
	 *
	 * @return the referred row cube level
	 */
	public void setSpanOverOnRow(LevelHandle level) throws SemanticException {
		handle.setProperty(SPAN_OVER_ON_ROW_PROP, level);
	}

	/**
	 * Set the referred column cube level of this aggregation cell to span over.
	 *
	 * @return the referred column cube level
	 */
	public void setSpanOverOnColumn(LevelHandle level) throws SemanticException {
		handle.setProperty(SPAN_OVER_ON_COLUMN_PROP, level);
	}

	/**
	 * Set the referred row cube level of this aggregation applied on.
	 *
	 * @return the referred row cube level
	 */
	public void setAggregationOnRow(LevelHandle level) throws SemanticException {
		handle.setProperty(AGGREGATION_ON_ROW_PROP, level);
	}

	/**
	 * Set the referred column cube level of this aggregation applied on.
	 *
	 * @return the referred column cube level
	 */
	public void setAggregationOnColumn(LevelHandle level) throws SemanticException {
		handle.setProperty(AGGREGATION_ON_COLUMN_PROP, level);
	}

	/**
	 * Gets row/column cube level. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 *
	 * @param axisType row/column axis type
	 * @return row/column level if found, otherwise null
	 */
	protected LevelHandle getLevel(int axisType) {
		switch (axisType) {
		case ROW_AXIS_TYPE:
			return getAggregationOnRow();
		case COLUMN_AXIS_TYPE:
			return getAggregationOnColumn();
		default:
			return null;
		}
	}

	/**
	 * Gets the name of the cube dimension where the referred level lying. The axis
	 * type can be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 *
	 * @param axisType row/column axis type
	 * @return name of the cube dimension
	 */
	public String getDimensionName(int axisType) {
		LevelHandle cubeLevel = getLevel(axisType);
		if (cubeLevel == null) {
			return null;
		}
		DesignElementHandle hierarchy = cubeLevel.getContainer();
		DesignElementHandle dimension = hierarchy == null ? null : hierarchy.getContainer();
		return dimension == null ? null : dimension.getQualifiedName();

	}

	/**
	 * Gets name of the referred cube level. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 *
	 * @param axisType row/column axis type
	 *
	 * @return name of the cube level
	 */
	public String getLevelName(int axisType) {
		switch (axisType) {
		case ROW_AXIS_TYPE:
			return handle.getStringProperty(AGGREGATION_ON_ROW_PROP);
		case COLUMN_AXIS_TYPE:
			return handle.getStringProperty(AGGREGATION_ON_COLUMN_PROP);
		default:
			return null;
		}
	}

	/**
	 * Gets the position index of the referred row/column dimension in crosstab.The
	 * axis type can be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Returned value is 0-based
	 * integer.
	 *
	 * @param axisType row/column axis type
	 * @return position index of the referred row/column dimension if found,
	 *         otherwise -1
	 */
	public int getDimensionViewIndex(int axisType) {
		DimensionViewHandle dimensionView = getDimensionView(axisType);
		return dimensionView == null ? -1 : dimensionView.getIndex();
	}

	/**
	 * Gets the referred row/column dimension view.The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 *
	 * @param axisType row/column axis type
	 * @return dimension view if found, otherwise null;
	 */
	public DimensionViewHandle getDimensionView(int axisType) {
		ExtendedItemHandle crosstab = (ExtendedItemHandle) getCrosstabHandle();
		if (crosstab == null) {
			return null;
		}
		CrosstabReportItemHandle crosstabItem = (CrosstabReportItemHandle) CrosstabUtil.getReportItem(crosstab);
		if (crosstabItem == null) {
			return null;
		}
		DimensionViewHandle dimensionView = crosstabItem.getDimension(getDimensionName(axisType));
		return dimensionView;
	}

	/**
	 * Gets the position index of the referred row/column level in parent dimension
	 * view.The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Returned value is 0-based
	 * integer.
	 *
	 * @param axisType row/column axis type
	 * @return position index of the referred row/column level if found, otherwise
	 *         -1
	 */
	public int getLevelViewIndex(int axisType) {
		DimensionViewHandle dimensionView = getDimensionView(axisType);
		if (dimensionView == null) {
			return -1;
		}
		LevelViewHandle levelView = dimensionView.getLevel(getLevelName(axisType));
		return levelView == null ? -1 : levelView.getIndex();
	}

	/**
	 * Gets the referred row/column level view.The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 *
	 * @param axisType row/column axis type
	 * @return level view if found, otherwise null;
	 */
	public LevelViewHandle getLevelView(int axisType) {
		DimensionViewHandle dimensionView = getDimensionView(axisType);
		if (dimensionView == null) {
			return null;
		}
		return dimensionView.getLevel(getLevelName(axisType));
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#getPredefinedStyles ()
	 */
	@Override
	public List getPredefinedStyles() {
		AbstractCrosstabItemHandle container = getContainer();
		if (container == null) {
			return Collections.EMPTY_LIST;
		}

		List styles = new ArrayList();

		if (container instanceof MeasureViewHandle) {
			String propName = handle.getContainerPropertyHandle().getDefn().getName();
			if (IMeasureViewConstants.AGGREGATIONS_PROP.equals(propName)) {
				CrosstabReportItemHandle crosstab = getCrosstab();
				LevelHandle column = getAggregationOnColumn();
				LevelHandle row = getAggregationOnRow();
				if (row == null) {
					if (crosstab.getDimensionCount(ROW_AXIS_TYPE) > 0) {
						styles.add(CROSSTAB_ROW_GRAND_TOTAL_SELECTOR);
					}
				} else {
					DimensionViewHandle dimensionView = getDimensionView(ROW_AXIS_TYPE);
					LevelViewHandle levelView = getLevelView(ROW_AXIS_TYPE);
					if (dimensionView != null && levelView != null) {
						int index = dimensionView.getIndex();
						if (!(index == crosstab.getDimensionCount(ROW_AXIS_TYPE) - 1
								&& levelView.getIndex() == dimensionView.getLevelCount() - 1)) {
							styles.add(CROSSTAB_ROW_SUB_TOTAL_SELECTOR);
						}
					}
				}

				if (column == null) {
					if (crosstab.getDimensionCount(COLUMN_AXIS_TYPE) > 0) {
						styles.add(CROSSTAB_COLUMN_GRAND_TOTAL_SELECTOR);
					}
				} else {
					DimensionViewHandle dimensionView = getDimensionView(COLUMN_AXIS_TYPE);
					LevelViewHandle levelView = getLevelView(COLUMN_AXIS_TYPE);
					if (dimensionView != null && levelView != null) {
						int index = dimensionView.getIndex();
						if (!(index == crosstab.getDimensionCount(COLUMN_AXIS_TYPE) - 1
								&& levelView.getIndex() == dimensionView.getLevelCount() - 1)) {
							styles.add(CROSSTAB_COLUMN_SUB_TOTAL_SELECTOR);
						}
					}
				}
			}
		}

		styles.addAll(super.getPredefinedStyles());
		return styles;
	}
}
