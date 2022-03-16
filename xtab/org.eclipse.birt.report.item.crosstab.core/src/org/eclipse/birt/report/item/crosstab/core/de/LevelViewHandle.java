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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.internal.LevelViewTask;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * LevelViewHandle.
 */
public class LevelViewHandle extends AbstractCrosstabItemHandle implements ILevelViewConstants, ICrosstabConstants {

	/**
	 *
	 * @param handle
	 */
	LevelViewHandle(DesignElementHandle handle) {
		super(handle);
	}

	/**
	 * Gets the referred OLAP level handle.
	 *
	 * @return the referred OLAP level handle.
	 */
	public LevelHandle getCubeLevel() {
		LevelHandle cubeLevel = (LevelHandle) handle.getElementProperty(LEVEL_PROP);
		if (cubeLevel == null) {
			String fullLevelName = this.getCubeLevelName();
			CubeHandle cubeHandle = this.getCrosstab().getCube();
			cubeLevel = findLevelHandle(fullLevelName);
		}

		return cubeLevel;
	}

	/**
	 * Gets name of the referred OLAP level handle.
	 *
	 * @return qualified name of the referred OLAP level handle
	 */
	public String getCubeLevelName() {
		return handle.getStringProperty(LEVEL_PROP);
	}

	/**
	 * Returns the iterator for filter list defined on this level view. The element
	 * in the iterator is the corresponding <code>DesignElementHandle</code> that
	 * deal with a <code>FilterConditionElementHandle</code> in the list.
	 *
	 * @return the iterator for <code>FilterConditionElementHandle</code> element
	 *         list
	 */

	public Iterator filtersIterator() {
		PropertyHandle propHandle = handle.getPropertyHandle(FILTER_PROP);
		if (propHandle == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return propHandle.getListValue().iterator();
	}

	/**
	 * Return the sort type.
	 *
	 * @return the sort type.
	 */

	public String getSortType() {
		return handle.getStringProperty(SORT_TYPE_PROP);
	}

	/**
	 * Returns the sort direction of this level view. The return value is defined in
	 * <code>DesignChoiceConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>SORT_DIRECTION_ASC</code>
	 * <li><code>SORT_DIRECTION_DESC</code>
	 *
	 * </ul>
	 *
	 * @return the sort direction of this level view
	 */

	public String getSortDirection() {
		return handle.getStringProperty(SORT_DIRECTION_PROP);
	}

	/**
	 * Gets the display key for this level view.
	 *
	 * @return the display key for this level view
	 */
	public String getDisplayField() {
		return handle.getStringProperty(DISPLAY_FIELD_PROP);
	}

	/**
	 * Returns the iterator for Sort list defined on this level. The element in the
	 * iterator is the corresponding <code>SortElementHandle</code>.
	 *
	 * @return the iterator for <code>SortElementHandle</code> element list
	 */

	public Iterator sortsIterator() {
		PropertyHandle propHandle = handle.getPropertyHandle(SORT_PROP);
		if (propHandle == null) {
			return Collections.EMPTY_LIST.iterator();
		}
		return propHandle.getListValue().iterator();
	}

	/**
	 * Gets page break before property value of this level view.
	 *
	 * @return page break before property value of this level view
	 */

	public String getPageBreakBefore() {
		return handle.getStringProperty(PAGE_BREAK_BEFORE_PROP);
	}

	/**
	 * Gets page break after property value of this level.
	 *
	 * @return page break after property value of this level
	 */

	public String getPageBreakAfter() {
		return handle.getStringProperty(PAGE_BREAK_AFTER_PROP);
	}

	/**
	 * Gets page break inside property value of this level.
	 *
	 * @return page break inside property value of this level
	 */

	public String getPageBreakInside() {
		return handle.getStringProperty(PAGE_BREAK_INSIDE_PROP);
	}

	/**
	 * Gets page break interval property value of this level.
	 *
	 * @return page break interval property value of this level
	 */
	public int getPageBreakInterval() {
		return handle.getIntProperty(PAGE_BREAK_INTERVAL_PROP);
	}

	/**
	 * Sets page break before property value of this level
	 *
	 * @param value the page break before option to set
	 * @throws SemanticException
	 */
	public void setPageBreakBefore(String value) throws SemanticException {
		handle.setStringProperty(PAGE_BREAK_BEFORE_PROP, value);
	}

	/**
	 * Sets page break after property value of this level.
	 *
	 * @param value the page break after option to set
	 * @throws SemanticException
	 *
	 */
	public void setPageBreakAfter(String value) throws SemanticException {
		handle.setStringProperty(PAGE_BREAK_AFTER_PROP, value);
	}

	/**
	 * Sets page break inside property value of this level.
	 *
	 * @param value the page break inside option to set
	 * @throws SemanticException
	 *
	 */
	public void setPageBreakInside(String value) throws SemanticException {
		handle.setStringProperty(PAGE_BREAK_INSIDE_PROP, value);
	}

	/**
	 * Sets page break interval property value of this level.
	 *
	 * @param value the page break interval to set
	 * @throws SemanticException
	 */
	public void setPageBreakInterval(int value) throws SemanticException {
		handle.setIntProperty(PAGE_BREAK_INTERVAL_PROP, value);
	}

	/**
	 * Returns the aggregation header location of this level view. The return value
	 * is defined in <code>ICrosstabConstants</code> and can be one of:
	 *
	 * <ul>
	 * <li><code>AGGREGATION_HEADER_LOCATION_BEFORE</code>
	 * <li><code>AGGREGATION_HEADER_LOCATION_AFTER</code>
	 * </ul>
	 *
	 * @return the aggregation header location of this level view
	 */

	public String getAggregationHeaderLocation() {
		return handle.getStringProperty(AGGREGATION_HEADER_LOCATION_PROP);
	}

	/**
	 * Sets the aggregation header location of this level view. The input value can
	 * be one of:
	 * <ul>
	 * <li><code>AGGREGATION_HEADER_LOCATION_BEFORE</code>
	 * <li><code>AGGREGATION_HEADER_LOCATION_AFTER</code>
	 * </ul>
	 *
	 * @param value the aggregation header location to set
	 * @throws SemanticException
	 */
	public void setAggregationHeaderLocation(String value) throws SemanticException {
		handle.setStringProperty(AGGREGATION_HEADER_LOCATION_PROP, value);
	}

	/**
	 * Gets the member property handle of this level view.
	 *
	 * @return the member property handle
	 */
	public PropertyHandle getMemberProperty() {
		return handle.getPropertyHandle(MEMBER_PROP);
	}

	/**
	 * Gets the aggregation header property handle of this level view.
	 *
	 * @return the aggregation header property handle
	 */
	public PropertyHandle getAggregationHeaderProperty() {
		return handle.getPropertyHandle(AGGREGATION_HEADER_PROP);
	}

	/**
	 * Gets the detail cell of this level view.
	 *
	 * @return the detail cell of this level view if set, otherwise null
	 */
	public CrosstabCellHandle getCell() {
		PropertyHandle propHandle = getMemberProperty();
		return propHandle.getContentCount() == 0 ? null
				: (CrosstabCellHandle) CrosstabUtil.getReportItem(propHandle.getContent(0),
						CROSSTAB_CELL_EXTENSION_NAME);
	}

	/**
	 * Gets the aggregation header cell of this level view.
	 *
	 * @return aggregation header cell if set, otherwise null
	 */
	public CrosstabCellHandle getAggregationHeader() {
		PropertyHandle propHandle = getAggregationHeaderProperty();
		return propHandle.getContentCount() == 0 ? null
				: (CrosstabCellHandle) CrosstabUtil.getReportItem(propHandle.getContent(0),
						CROSSTAB_CELL_EXTENSION_NAME);
	}

	/**
	 * Adds a aggregation header to the level if it is empty. This method only adds
	 * the aggreation header cell in this level. It does not adjust any measure
	 * aggreations automatically. User himself needs to add measure aggregations
	 * manually.
	 *
	 * @deprecated For internal test only, user addSubTotal() instead
	 */
	@Deprecated
	public void addAggregationHeader() throws SemanticException {
		if (getAggregationHeaderProperty().getContentCount() != 0) {
			logger.log(Level.INFO, "the aggregation header is set"); //$NON-NLS-1$
			return;
		}

		// can not add aggregation if this level is innermost
		if (isInnerMost()) {
			logger.log(Level.WARNING,
					"This level: [" + handle.getName() + "] can not add aggregation for it is innermost"); //$NON-NLS-1$//$NON-NLS-2$
			return;
		}

		getAggregationHeaderProperty().add(CrosstabExtendedItemFactory.createCrosstabCell(moduleHandle));
	}

	/**
	 * Removes the aggregation header cell if it is not empty, otherwise do nothing.
	 * This method will not adjust the measure aggregations automatically. Use needs
	 * to remove all the related aggregations manually.
	 *
	 * @deprecated For internal test only, user removeSubTotal() instead
	 */
	@Deprecated
	public void removeAggregationHeader() throws SemanticException {
		if (getAggregationHeaderProperty().getContentCount() > 0) {
			getAggregationHeaderProperty().drop(0);
		}
	}

	/**
	 * Adds a sub-total for this level. This method will add a aggregation header
	 * cell in this level and adjust the measure aggregations to ensure the
	 * validation of the whole crosstab.
	 *
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addSubTotal(List measureList, List functionList) throws SemanticException {
		return new LevelViewTask(this).addSubTotal(measureList, functionList);
	}

	/**
	 * Removes the aggregation header cell if it is not empty, otherwise do nothing.
	 * This method will remove the aggregation header cell in this level and adjust
	 * the measure aggregations to ensure the validation of the whole crosstab.
	 */
	public void removeSubTotal() throws SemanticException {
		new LevelViewTask(this).removeSubTotal();
	}

	/**
	 * Removes the aggregation header cell if it is not empty on particular measure,
	 * otherwise do nothing. This method will remove the aggregation header cell in
	 * this level and adjust the measure aggregations to ensure the validation of
	 * the whole crosstab.
	 */
	public void removeSubTotal(int measureIndex) throws SemanticException {
		new LevelViewTask(this).removeSubTotal(measureIndex);
	}

	/**
	 * Gets the position index where this level lies in the dimension view
	 * container. The returned value is a 0-based integer if this level is in the
	 * design tree. Otherwise return -1.
	 *
	 * @return position index if found, otherwise -1
	 */
	public int getIndex() {
		return handle.getIndex();
	}

	/**
	 * Justifies whether this level view is inner most in the crosstab. True if and
	 * only if it is the last one in its container dimension view and its container
	 * dimension view is the last one in crosstab view.
	 *
	 * @return true if
	 */
	public boolean isInnerMost() {
		DimensionViewHandle dimensionView = (DimensionViewHandle) getContainer();

		// a level view is 'innerMost' if and only if it is the last one in its
		// container dimension view and its container dimension view is the last
		// one in crosstab view
		if (dimensionView != null) {
			CrosstabViewHandle container = (CrosstabViewHandle) dimensionView.getContainer();
			if (container != null && dimensionView.getIndex() == container.getDimensionCount() - 1
					&& getIndex() == dimensionView.getLevelCount() - 1) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Gets the axis type of this level view in the crosstab. If this level lies in
	 * the design tree, the returned value is either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. Otherwise return
	 * <code>ICrosstabConstants.NO_AXIS_TYPE</code>.
	 *
	 * @return the axis type if this level resides in design tree, otherwise -1;
	 */
	public int getAxisType() {
		DimensionViewHandle dimensionView = (DimensionViewHandle) CrosstabUtil.getReportItem(handle.getContainer(),
				DIMENSION_VIEW_EXTENSION_NAME);
		return dimensionView == null ? NO_AXIS_TYPE : dimensionView.getAxisType();

	}

	/**
	 * Gets the aggregation function of this level view applying on the specified
	 * measure view.
	 *
	 * @param measureView
	 * @return
	 */
	public String getAggregationFunction(MeasureViewHandle measureView) {
		return new LevelViewTask(this).getAggregationFunction(measureView);
	}

	/**
	 * Gets the measure view list that define aggregations for the given level view.
	 * Each item in the list is instance of <code>MeasureViewHandle</code> .
	 *
	 * @param levelView
	 * @return
	 */
	public List getAggregationMeasures() {
		return new LevelViewTask(this).getAggregationMeasures();
	}

	/**
	 * Gets the aggregation function for the level view sub-total. If the level view
	 * is null or not define any sub-total, return null.
	 *
	 * @param measureView
	 * @param function
	 * @return
	 * @throws SemanticException
	 */
	public void setAggregationFunction(MeasureViewHandle measureView, String function) throws SemanticException {
		new LevelViewTask(this).setAggregationFunction(measureView, function);
	}
}
