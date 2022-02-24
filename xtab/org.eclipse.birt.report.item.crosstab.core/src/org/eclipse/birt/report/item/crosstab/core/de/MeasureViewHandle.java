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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.IAggregationCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabCellConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.IMeasureViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.internal.CrosstabModelUtil;
import org.eclipse.birt.report.item.crosstab.core.de.internal.MeasureViewTask;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.extension.CompatibilityStatus;
import org.eclipse.birt.report.model.api.extension.IllegalContentInfo;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.MeasureHandle;
import org.eclipse.birt.report.model.elements.interfaces.IDesignElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IExtendedItemModel;

/**
 * MeasureViewHandle.
 */
public class MeasureViewHandle extends AbstractCrosstabItemHandle implements IMeasureViewConstants, ICrosstabConstants {

	/**
	 * 
	 * @param handle
	 */
	MeasureViewHandle(DesignElementHandle handle) {
		super(handle);
	}

	/**
	 * Gets the referred OLAP measure handle of this measure view.
	 * 
	 * @return the referred OLAP measure handle
	 */
	public MeasureHandle getCubeMeasure() {
		MeasureHandle cubeMeasure = (MeasureHandle) handle.getElementProperty(MEASURE_PROP);
		CubeHandle cube = this.getCrosstab().getCube();
		// Check if using the same cube
		if (cubeMeasure != null) {
			if (cubeMeasure.getContainer() != null && cubeMeasure.getContainer().getContainer() != cube) {
				cubeMeasure = null;
			}
		}
		if (cubeMeasure == null) {
			String measureName = this.getCubeMeasureName();
			if (measureName != null && cube != null) {
				cubeMeasure = cube.getMeasure(measureName);
			}
		}

		return cubeMeasure;
	}

	/**
	 * Gets name of the referred OLAP measure handle in this measure view.
	 * 
	 * @return name of the referred OLAP measure handle
	 */
	public String getCubeMeasureName() {
		return handle.getStringProperty(MEASURE_PROP);
	}

	/**
	 * Gets the data type of this measure view. It is identical with the data type
	 * set in the referred cube measure element. If linked data set, should get data
	 * type from binding.
	 * 
	 * @return
	 */
	public String getDataType() {
		String dataType = null;
		CrosstabReportItemHandle crosstabItem = getCrosstab();
		if (CrosstabUtil.isBoundToLinkedDataSet(crosstabItem)) {
			String linkedColumnName = getCubeMeasureName();
			CrosstabCellHandle cell = getCell();
			if (cell != null) {
				List contents = cell.getContents();
				for (Object obj : contents) {
					if (obj != null && obj instanceof DataItemHandle) {
						String bindingName = ((DataItemHandle) obj).getResultSetColumn();
						ComputedColumnHandle column = CrosstabUtil.getColumnHandle(crosstabItem, bindingName);
						dataType = (column != null) ? column.getDataType() : null;
						if (CrosstabUtil.validateBinding(column, linkedColumnName)) {
							break;
						}
					}
				}
			}
		}

		if (dataType == null) {
			MeasureHandle cubeMeasure = getCubeMeasure();
			dataType = cubeMeasure == null ? null : cubeMeasure.getDataType();
		}

		return dataType;
	}

	/**
	 * Gets the aggregations property handle of this measure view.
	 * 
	 * @return the aggregations property handle
	 */
	PropertyHandle getAggregationsProperty() {
		return handle.getPropertyHandle(AGGREGATIONS_PROP);
	}

	/**
	 * Gets the detail slot handle of this measure view.
	 * 
	 * @return the detail slot handle
	 */
	PropertyHandle getDetailProperty() {
		return handle.getPropertyHandle(DETAIL_PROP);
	}

	/**
	 * Gets the header slot handle of this measure view.
	 * 
	 * @return the header slot handle
	 */
	public PropertyHandle getHeaderProperty() {
		return handle.getPropertyHandle(HEADER_PROP);
	}

	/**
	 * Gets the detail cell of this measure view.
	 * 
	 * @return the detail cell of this measure view if set, otherwise null
	 */
	public AggregationCellHandle getCell() {
		PropertyHandle propHandle = getDetailProperty();
		return propHandle.getContentCount() == 0 ? null
				: (AggregationCellHandle) CrosstabUtil.getReportItem(propHandle.getContent(0),
						AGGREGATION_CELL_EXTENSION_NAME);
	}

	/**
	 * Adds an aggregation cell with the specific row/column dimension and level.
	 * 
	 * @param rowDimension qualified name of the row dimension
	 * @param rowLevel     qualified name of the row level
	 * @param colDimension qualified name of the column dimension
	 * @param colLevel     qualifed name of the column level
	 * @return the added aggregation cell if succeed, otherwise null
	 * @throws SemanticException
	 */
	public AggregationCellHandle addAggregation(String rowDimension, String rowLevel, String colDimension,
			String colLevel) throws SemanticException {
		AggregationCellHandle aggregation = getAggregationCell(rowDimension, rowLevel, colDimension, colLevel);
		if (aggregation != null) {
			logger.log(Level.INFO, Messages.getString("MeasureViewHandle.info.aggregation.already.exist")); //$NON-NLS-1$
			return aggregation;
		}
		ExtendedItemHandle aggregationCell = CrosstabExtendedItemFactory.createAggregationCell(moduleHandle);
		if (aggregationCell != null) {
			CommandStack stack = getCommandStack();
			stack.startTrans(Messages.getString("MeasureViewHandle.msg.add.aggregation")); //$NON-NLS-1$

			try {
				aggregationCell.setProperty(IAggregationCellConstants.AGGREGATION_ON_ROW_PROP, rowLevel);
				aggregationCell.setProperty(IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP, colLevel);
				getAggregationsProperty().add(aggregationCell);
			} catch (SemanticException e) {
				stack.rollback();
				throw e;
			}
			stack.commit();
		}
		return (AggregationCellHandle) CrosstabUtil.getReportItem(aggregationCell);
	}

	/**
	 * 
	 * @param rowDimension qualified name of the row dimension
	 * @param rowLevel     qualified name of the row level
	 * @param colDimension qualified name of the column dimension
	 * @param colLevel     qualifed name of the column level
	 * @throws SemanticException
	 */
	public void removeAggregation(String rowDimension, String rowLevel, String colDimension, String colLevel)
			throws SemanticException {
		AggregationCellHandle cell = getAggregationCell(rowDimension, rowLevel, colDimension, colLevel);
		if (cell != null) {
			cell.handle.drop();
		}
	}

	/**
	 * Finds an aggregation cell which uses the given row/column dimension and
	 * level.
	 * 
	 * @param rowDimension qualified name of the row dimension
	 * @param rowLevel     qualified name of the row level
	 * @param colDimension qualified name of the column dimension
	 * @param colLevel     qualifed name of the column level
	 * @return the aggregation cell handle if found, otherwise null
	 */
	public AggregationCellHandle getAggregationCell(String rowDimension, String rowLevel, String colDimension,
			String colLevel) {
		int count = getAggregationCount();
		if (count == 0)
			return null;
		DesignElementHandle found = null;
		for (int i = 0; i < count; i++) {
			DesignElementHandle element = getAggregationsProperty().getContent(i);
			String row = element.getStringProperty(IAggregationCellConstants.AGGREGATION_ON_ROW_PROP);
			String column = element.getStringProperty(IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP);
			if ((rowLevel != null && rowLevel.equals(row)) || (rowLevel == null && row == null)) {
				if ((colLevel != null && colLevel.equals(column)) || (colLevel == null && column == null)) {
					found = element;
					break;
				}
			}
		}
		return (AggregationCellHandle) CrosstabUtil.getReportItem(found, AGGREGATION_CELL_EXTENSION_NAME);
	}

	/**
	 * Gets the aggregation cell count for this measure.
	 * 
	 * @return count of aggregation cell for this measure
	 */
	public int getAggregationCount() {
		return getAggregationsProperty().getContentCount();
	}

	/**
	 * Gets the aggregation cell with the given index. Position index is 0-based
	 * integer.
	 * 
	 * @param index a 0-based integer of the aggregation cell position
	 * @return the aggregation cell handle if found, otherwise null
	 */
	public AggregationCellHandle getAggregationCell(int index) {
		DesignElementHandle element = getAggregationsProperty().getContent(index);
		return (AggregationCellHandle) CrosstabUtil.getReportItem(element, AGGREGATION_CELL_EXTENSION_NAME);
	}

	/**
	 * Removes aggregation cell at the given position. The position index is 0-based
	 * integer.
	 * 
	 * @param index the position index of the aggregation cell to remove
	 * @throws SemanticException
	 */
	public void removeAggregation(int index) throws SemanticException {
		getAggregationsProperty().drop(index);
	}

	/**
	 * Gets the position index where this measure lies in the crosstab container.
	 * The returned value is a 0-based integer if this level is in the design tree.
	 * Otherwise return -1.
	 * 
	 * @return position index if found, otherwise -1
	 */
	public int getIndex() {
		return handle.getIndex();
	}

	/**
	 * Gets the first header cell for this measure.
	 * 
	 * @return the header cell
	 */
	public CrosstabCellHandle getHeader() {
		return getHeader(0);
	}

	/**
	 * Returns the header associated with given level view. If the level view is
	 * *null*, it returns the header associated with the grandtotal; if the level
	 * view is the inner most, it returns the header associated with the detail
	 * area; otherwise, it returns the header associated with the subtotal. The
	 * result may be null if the given level view doesn't yield a grandtotal or
	 * subtotal, or is in the wrong axis.
	 * 
	 * @param lv
	 * @return
	 * 
	 * @since 2.5
	 */
	public CrosstabCellHandle getHeader(LevelViewHandle levelView) {
		CrosstabReportItemHandle crosstab = getCrosstab();

		int targetAxis = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection()) ? ROW_AXIS_TYPE
				: COLUMN_AXIS_TYPE;

		if (levelView == null) {
			// this is grandtotal, return the last header if available
			if (CrosstabModelUtil.isAggregationOn(this, null, targetAxis)) {
				return getHeader(getHeaderCount() - 1);
			}
		} else if (levelView.getAxisType() == targetAxis) {
			// this is subtotal or detial
			LevelViewHandle innerMost = CrosstabModelUtil.getInnerMostLevel(crosstab, targetAxis);

			if (levelView == innerMost) {
				// return first header
				return getHeader();
			} else {
				List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels(crosstab, targetAxis);

				// we need the reversed order here to count from inner most to
				// outer most
				Collections.reverse(levels);

				int realIndex = 0;

				for (int i = 0; i < levels.size(); i++) {
					LevelViewHandle lv = levels.get(i);

					if (lv == innerMost || CrosstabModelUtil.isAggregationOn(this, lv.getCubeLevelName(), targetAxis)) {
						// find the real header index
						if (levelView == lv) {
							return getHeader(realIndex);
						}

						realIndex++;
					}
				}
			}
		}

		return null;
	}

	/**
	 * Gets header cell for this measure by given index.
	 * 
	 * @param index the header index
	 * @return the header cell which refers the given dimension and level
	 * 
	 * @since 2.5
	 */
	public CrosstabCellHandle getHeader(int index) {
		DesignElementHandle headerCell = getHeaderCell(index);
		return (CrosstabCellHandle) (headerCell == null ? null
				: CrosstabUtil.getReportItem(headerCell, CROSSTAB_CELL_EXTENSION_NAME));
	}

	/**
	 * @return Returns the header cell count for this measure.
	 * 
	 * @since 2.5
	 */
	public int getHeaderCount() {
		return getHeaderProperty().getContentCount();
	}

	/**
	 * Gets measure header cell.
	 * 
	 * @return the design element handle for the header cell if found, otherwise
	 *         null
	 */
	private DesignElementHandle getHeaderCell(int index) {
		PropertyHandle propHandle = getHeaderProperty();
		if (index < 0 || propHandle.getContentCount() <= index)
			return null;
		return propHandle.getContent(index);
	}

	/**
	 * Removes header cell for current measure.
	 * 
	 * @throws SemanticException
	 */
	public void removeHeader() throws SemanticException {
		new MeasureViewTask(this).removeHeader();
	}

	/**
	 * Adds header cell for current measure. If header cell already exists, this
	 * method just does nothing.
	 * 
	 * @throws SemanticException
	 */
	public void addHeader() throws SemanticException {
		new MeasureViewTask(this).addHeader();
	}

	/**
	 * Returns the iterator for filter list defined on this measure view. The
	 * element in the iterator is the corresponding <code>DesignElementHandle</code>
	 * that deal with a <code>FilterConditionElementHandle</code> in the list.
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

	private LevelHandle getInnerestLevel(CrosstabReportItemHandle crosstab, int axisType) {
		int dimCount = crosstab.getDimensionCount(axisType);
		if (dimCount > 0) {
			// TODO check visibility?
			DimensionViewHandle dv = crosstab.getDimension(axisType, dimCount - 1);

			LevelViewHandle lv = dv.getLevel(dv.getLevelCount() - 1);

			return lv.getCubeLevel();
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.api.extension.ReportItem#checkCompatibility ()
	 */
	public CompatibilityStatus checkCompatibility() {
		// update old version
		CrosstabReportItemHandle crosstab = getCrosstab();

		if (crosstab.compStatus < 0) {
			CompatibilityStatus status = null;
			List errorList = null;

			ExtendedItemHandle exhandle = (ExtendedItemHandle) getModelHandle();

			Map illegalContents = exhandle.getIllegalContents();

			// do compatibility for "detail" property since 2.3, update old
			// "crosstabCell" to new "aggregationCell" (? -> 2.3.0)
			if (illegalContents.containsKey(IMeasureViewConstants.DETAIL_PROP)) {
				List detailInfoList = (List) illegalContents.get(IMeasureViewConstants.DETAIL_PROP);

				if (detailInfoList.size() > 0) {
					IllegalContentInfo detailInfo = (IllegalContentInfo) detailInfoList.get(0);

					ExtendedItemHandle oldDetail = (ExtendedItemHandle) detailInfo.getContent();

					if (oldDetail != null) {
						status = new CompatibilityStatus();
						status.setStatusType(CompatibilityStatus.CONVERT_COMPATIBILITY_TYPE);

						try {
							ExtendedItemHandle newDetail = CrosstabExtendedItemFactory
									.createAggregationCell(getModuleHandle());

							handle.getPropertyHandle(DETAIL_PROP).setValue(newDetail);

							// copy old local properties
							for (Iterator itr = oldDetail.getPropertyIterator(); itr.hasNext();) {
								PropertyHandle propHandle = (PropertyHandle) itr.next();

								String propName = propHandle.getPropertyDefn().getName();

								if (!propHandle.isLocal() || IDesignElementModel.NAME_PROP.equals(propName)
										|| IDesignElementModel.EXTENDS_PROP.equals(propName)
										|| IExtendedItemModel.EXTENSION_NAME_PROP.equals(propName)
										|| IExtendedItemModel.EXTENSION_VERSION_PROP.equals(propName)
										|| ICrosstabCellConstants.CONTENT_PROP.equals(propName)) {
									continue;
								}

								try {
									oldDetail.copyPropertyTo(propName, newDetail);
								} catch (Exception e) {
									logger.log(Level.WARNING, "The old property [" //$NON-NLS-1$
											+ propName + "] is not converted properly to the new Crosstab model."); //$NON-NLS-1$
								}
							}

							// set new aggregateOn properties
							LevelHandle rowLevel = getInnerestLevel(crosstab, ROW_AXIS_TYPE);
							LevelHandle columnLevel = getInnerestLevel(crosstab, COLUMN_AXIS_TYPE);

							if (rowLevel != null) {
								newDetail.setProperty(IAggregationCellConstants.AGGREGATION_ON_ROW_PROP, rowLevel);
							}

							if (columnLevel != null) {
								newDetail.setProperty(IAggregationCellConstants.AGGREGATION_ON_COLUMN_PROP,
										columnLevel);
							}

							List contents = oldDetail.getContents(ICrosstabCellConstants.CONTENT_PROP);

							for (int i = 0; i < contents.size(); i++) {
								((DesignElementHandle) contents.get(i)).moveTo(newDetail,
										IAggregationCellConstants.CONTENT_PROP);
							}
						} catch (SemanticException e) {
							if (errorList == null) {
								errorList = new ArrayList(1);
							}
							errorList.add(e);
						}
					}
				}
			}

			// do compatibility for measure header property since 2.5.0, now
			// use different header instance for subtotal/grandtotal/detail
			// header (? -> 2.5.0)
			int expectHeaders = CrosstabModelUtil.computeAllMeasureHeaderCount(crosstab, this);
			int availableHeaders = getHeaderCount();

			if (availableHeaders < expectHeaders) {
				// upgrade the model to multi-header state
				PropertyHandle propHandle = getHeaderProperty();
				DesignElementHandle oldHeader = getHeaderCell(0);

				try {
					for (int i = 0; i < expectHeaders - availableHeaders; i++) {
						DesignElementHandle newHeader;

						if (oldHeader == null) {
							newHeader = CrosstabExtendedItemFactory.createCrosstabCell(getModuleHandle());
						} else {
							newHeader = oldHeader.copy().getHandle(getModuleHandle().getModule());
						}

						propHandle.add(newHeader);
					}
				} catch (SemanticException e) {
					if (errorList == null) {
						errorList = new ArrayList(1);
					}
					errorList.add(e);
				}

			}

			if (status != null) {
				if (errorList != null) {
					status.setErrors(errorList);
				}

				return status;
			}
		}

		return COMP_OK_STATUS;
	}
}
