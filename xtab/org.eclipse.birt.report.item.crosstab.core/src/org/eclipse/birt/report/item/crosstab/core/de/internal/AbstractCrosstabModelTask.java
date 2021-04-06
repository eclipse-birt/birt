/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AbstractCrosstabItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.ComputedMeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.item.crosstab.core.util.ICrosstabUpdateListener;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * AbstractCrosstabModelTask
 */
public class AbstractCrosstabModelTask implements ICrosstabConstants {

	protected CrosstabReportItemHandle crosstab = null;

	/**
	 * 
	 * @param focus
	 */
	public AbstractCrosstabModelTask(AbstractCrosstabItemHandle focus) {
		if (focus == null)
			throw new IllegalArgumentException("The focus for the task can not be null"); //$NON-NLS-1$
		this.crosstab = focus.getCrosstab();
	}

	protected AggregationInfo getAggregationInfo(LevelViewHandle leftLevelView, LevelViewHandle rightLevelView) {
		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;
		if (leftLevelView == null) {
			if (rightLevelView != null) {
				if (rightLevelView.getCrosstab() != crosstab || rightLevelView.getCubeLevelName() == null
						|| rightLevelView.getCubeLevelName().length() == 0)
					return null;
				int axisType = rightLevelView.getAxisType();
				if (axisType == COLUMN_AXIS_TYPE) {
					colDimension = ((DimensionViewHandle) rightLevelView.getContainer()).getCubeDimensionName();
					colLevel = rightLevelView.getCubeLevelName();
				} else {
					rowDimension = ((DimensionViewHandle) rightLevelView.getContainer()).getCubeDimensionName();
					rowLevel = rightLevelView.getCubeLevelName();
				}
			}
		} else {
			if (leftLevelView.getCrosstab() != crosstab || leftLevelView.getCubeLevelName() == null
					|| leftLevelView.getCubeLevelName().length() == 0)
				return null;
			if (rightLevelView == null) {
				int axisType = leftLevelView.getAxisType();
				if (axisType == COLUMN_AXIS_TYPE) {
					colDimension = ((DimensionViewHandle) leftLevelView.getContainer()).getCubeDimensionName();
					colLevel = leftLevelView.getCubeLevelName();
				} else {
					rowDimension = ((DimensionViewHandle) leftLevelView.getContainer()).getCubeDimensionName();
					rowLevel = leftLevelView.getCubeLevelName();
				}
			} else {
				if (rightLevelView.getCrosstab() != crosstab || rightLevelView.getCubeLevelName() == null
						|| rightLevelView.getCubeLevelName().length() == 0)
					return null;

				int axisType = leftLevelView.getAxisType();
				if (rightLevelView.getAxisType() != CrosstabModelUtil.getOppositeAxisType(axisType))
					return null;
				if (axisType == COLUMN_AXIS_TYPE) {
					colDimension = ((DimensionViewHandle) leftLevelView.getContainer()).getCubeDimensionName();
					colLevel = leftLevelView.getCubeLevelName();
					rowDimension = ((DimensionViewHandle) rightLevelView.getContainer()).getCubeDimensionName();
					rowLevel = rightLevelView.getCubeLevelName();
				} else {
					rowDimension = ((DimensionViewHandle) leftLevelView.getContainer()).getCubeDimensionName();
					rowLevel = leftLevelView.getCubeLevelName();
					colDimension = ((DimensionViewHandle) rightLevelView.getContainer()).getCubeDimensionName();
					colLevel = rightLevelView.getCubeLevelName();
				}
			}
		}
		return new AggregationInfo(rowDimension, rowLevel, colDimension, colLevel);
	}

	/**
	 * 
	 * @param functions
	 * @param measures
	 * @return
	 */
	protected boolean isValidParameters(List<String> functions, List<MeasureViewHandle> measures) {
		if (functions == null || measures == null) {
			return false;
		}
		if (measures.size() == 0 || functions.size() == 0) {
			return false;
		}
		if (measures.size() != functions.size()) {
			return false;
		}
		return true;
	}

	protected void verifyTotalMeasureFunctions(int axisType, List<String> functions, List<MeasureViewHandle> measures) {
		if (functions == null || measures == null || functions.size() == 0 || measures.size() == 0) {
			return;
		}

		// use all measures if the total direction is oppsite to the measure
		// direction to avoid hole
		boolean isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection());

		if ((isVerticalMeasure && axisType == COLUMN_AXIS_TYPE) || (!isVerticalMeasure && axisType == ROW_AXIS_TYPE)) {
			String firstFunc = functions.get(0);
			String firstDataType = (measures.get(0) != null) ? measures.get(0).getDataType() : null;
			for (int i = 0; i < crosstab.getMeasureCount(); i++) {
				MeasureViewHandle mv = crosstab.getMeasure(i);
				if (!measures.contains(mv)) {
					measures.add(mv);
					String dataType = mv.getDataType();
					if (dataType != null && dataType.equalsIgnoreCase(firstDataType)) {
						functions.add(firstFunc);
					} else {
						functions.add(CrosstabModelUtil.getDefaultMeasureAggregationFunction(mv));
					}
				}
			}
		}
	}

	/**
	 * This method checks and adds the missing aggregations for given measures on
	 * the specific level view. Note if <code>checkCounterAxis</code> is
	 * <code>true</code>, in certain cases, the check in this method will be
	 * skipped, and the remaining work is expected to be done in subsequent
	 * <code>validateCrosstab</code>.
	 * 
	 * @param theLevelView
	 * @param measureList
	 * @param functionList
	 * @param isAdd
	 * @param checkCounterAxis
	 * @throws SemanticException
	 */
	protected void addMeasureAggregations(LevelViewHandle theLevelView, List<MeasureViewHandle> measureList,
			List<String> functionList, boolean checkCounterAxis) throws SemanticException {
		if (crosstab == null || theLevelView.getCrosstab() != crosstab)
			return;
		if (measureList == null || measureList.isEmpty())
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType(theLevelView.getAxisType());

		// if the level view not specifies a cube level, then do nothing
		String dimensionName = ((DimensionViewHandle) theLevelView.getContainer()).getCubeDimensionName();
		String levelName = theLevelView.getCubeLevelName();
		if (levelName == null || dimensionName == null)
			return;

		// status identify this level is innermost or not
		boolean isInnerMost = theLevelView.isInnerMost();

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for (int dimension = 0; dimension < crosstab.getDimensionCount(counterAxisType); dimension++) {
			DimensionViewHandle dimensionView = crosstab.getDimension(counterAxisType, dimension);
			for (int level = 0; level < dimensionView.getLevelCount(); level++) {
				// one level exists in this crosstab, then set
				// isCounterAxisEmpty to false
				isCounterAxisEmpty = false;

				LevelViewHandle levelView = dimensionView.getLevel(level);
				String rowDimension = null;
				String rowLevel = null;
				String colDimension = null;
				String colLevel = null;
				if (counterAxisType == ROW_AXIS_TYPE) {
					rowDimension = dimensionView.getCubeDimensionName();
					rowLevel = levelView.getCubeLevelName();
					colDimension = dimensionName;
					colLevel = levelName;
				} else if (counterAxisType == COLUMN_AXIS_TYPE) {
					rowDimension = dimensionName;
					rowLevel = levelName;
					colDimension = dimensionView.getCubeDimensionName();
					colLevel = levelView.getCubeLevelName();
				}

				// if 'isLevelInnerMost' is true, then add aggregation for
				// those not innermost and has aggregation levels in counter
				// axis; otherwise 'isLevelInnerMost' is false, then add
				// aggregation for those is innermost or has aggregation
				// levels in counter axis
				if ((isInnerMost && !levelView.isInnerMost() && levelView.getAggregationHeader() != null)
						|| (!isInnerMost && theLevelView.getAggregationHeader() != null
								&& (levelView.isInnerMost() || levelView.getAggregationHeader() != null))) {
					for (int i = 0; i < measureList.size(); i++) {
						MeasureViewHandle measureView = measureList.get(i);
						if (measureView.getCrosstab() != crosstab)
							continue;

						String function = functionList == null
								? CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView)
								: (String) functionList.get(i);
						// if checkCounterMeasureList is true, then we need to
						// check the counter level view is aggregated on the
						// measure, otherwise do nothing
						if (checkCounterAxis && !CrosstabModelUtil.isAggregationOn(measureView,
								levelView.getCubeLevelName(), counterAxisType))
							continue;

						validateSingleMeasureAggregation(crosstab, measureView, function, rowDimension, rowLevel,
								colDimension, colLevel);
					}
				}
			}
		}

		// add aggregation for crosstab grand total; or there is no levels
		// and no grand total, we still need to add one aggregation
		if (crosstab.getGrandTotal(counterAxisType) != null
				|| (isCounterAxisEmpty && theLevelView.getAggregationHeader() != null)) {
			String rowDimension = null;
			String rowLevel = null;
			String colDimension = null;
			String colLevel = null;
			if (counterAxisType == ROW_AXIS_TYPE) {
				colDimension = dimensionName;
				colLevel = levelName;
			} else if (counterAxisType == COLUMN_AXIS_TYPE) {
				rowDimension = dimensionName;
				rowLevel = levelName;
			}

			for (int i = 0; i < measureList.size(); i++) {
				MeasureViewHandle measureView = measureList.get(i);
				if (measureView.getCrosstab() != crosstab)
					continue;
				String function = functionList == null
						? CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView)
						: (String) functionList.get(i);
				// if checkCounterMeasureList is true, then we need to
				// check the counter level view is aggregated on the
				// measure, otherwise do nothing
				if (checkCounterAxis && !CrosstabModelUtil.isAggregationOn(measureView, null, counterAxisType))
					continue;

				validateSingleMeasureAggregation(crosstab, measureView, function, rowDimension, rowLevel, colDimension,
						colLevel);

			}
		}
	}

	/**
	 * This method checks and adds the missing aggregations for given measures on
	 * the specific axis(grand total). Note if <code>checkCounterAxis</code> is
	 * <code>true</code>, in certain cases, the check in this method will be
	 * skipped, and the remaining work is expected to be done in subsequent
	 * <code>validateCrosstab</code>.
	 * 
	 * @param axisType
	 * @param measureList
	 * @param functionList
	 * @param isAdd
	 * @param checkCounterAxis
	 * @throws SemanticException
	 */
	protected void addMeasureAggregations(int axisType, List<MeasureViewHandle> measureList, List<String> functionList,
			boolean checkCounterAxis) throws SemanticException {
		if (crosstab == null || measureList == null || measureList.isEmpty()
				|| crosstab.getGrandTotal(axisType) == null)
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
		String dimensionName = null;
		String levelName = null;

		// justifies whether the counterAxis has no level and grand total
		boolean isCounterAxisEmpty = true;

		// add aggregations for all level views
		for (int dimension = 0; dimension < crosstab.getDimensionCount(counterAxisType); dimension++) {
			DimensionViewHandle dimensionView = crosstab.getDimension(counterAxisType, dimension);
			for (int level = 0; level < dimensionView.getLevelCount(); level++) {
				// one level exists in this crosstab, then set
				// isCounterAxisEmpty to false
				isCounterAxisEmpty = false;

				LevelViewHandle levelView = dimensionView.getLevel(level);
				String rowDimension = null;
				String rowLevel = null;
				String colDimension = null;
				String colLevel = null;
				if (counterAxisType == ROW_AXIS_TYPE) {
					rowDimension = dimensionView.getCubeDimensionName();
					rowLevel = levelView.getCubeLevelName();
					colDimension = dimensionName;
					colLevel = levelName;
				} else if (counterAxisType == COLUMN_AXIS_TYPE) {
					rowDimension = dimensionName;
					rowLevel = levelName;
					colDimension = dimensionView.getCubeDimensionName();
					colLevel = levelView.getCubeLevelName();
				}

				// if 'isLevelInnerMost' is true, then add aggregation for
				// those not innermost and has aggregation levels in counter
				// axis; otherwise 'isLevelInnerMost' is false, then add
				// aggregation for those is innermost or has aggregation
				// levels in counter axis
				if (levelView.isInnerMost() || levelView.getAggregationHeader() != null) {
					for (int i = 0; i < measureList.size(); i++) {
						MeasureViewHandle measureView = measureList.get(i);
						if (measureView.getCrosstab() != crosstab)
							continue;

						String function = functionList == null
								? CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView)
								: (String) functionList.get(i);
						// if checkCounterMeasureList is true, then we
						// need to check the counter level view is
						// aggregated on
						// the measure, otherwise do nothing
						if (checkCounterAxis && !CrosstabModelUtil.isAggregationOn(measureView,
								levelView.getCubeLevelName(), counterAxisType))
							continue;

						validateSingleMeasureAggregation(crosstab, measureView, function, rowDimension, rowLevel,
								colDimension, colLevel);
					}
				}
			}
		}

		// add aggregation for crosstab grand total; or there is no levels
		// and no grand total, we still need to add one aggregation
		if (crosstab.getGrandTotal(counterAxisType) != null || isCounterAxisEmpty) {
			String rowDimension = null;
			String rowLevel = null;
			String colDimension = null;
			String colLevel = null;

			for (int i = 0; i < measureList.size(); i++) {
				MeasureViewHandle measureView = measureList.get(i);
				if (measureView.getCrosstab() != crosstab)
					continue;

				String function = functionList == null
						? CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView)
						: (String) functionList.get(i);
				// if checkCounterMeasureList is true, then we need to
				// check the counter level view is aggregated on the
				// measure, otherwise do nothing
				if (checkCounterAxis && !CrosstabModelUtil.isAggregationOn(measureView, null, counterAxisType))
					continue;

				validateSingleMeasureAggregation(crosstab, measureView, function, rowDimension, rowLevel, colDimension,
						colLevel);

			}
		}
	}

	/**
	 * Removes all the aggregations related with the level view on all measures.
	 * 
	 * @param levelView
	 */
	protected void removeMeasureAggregations(LevelViewHandle levelView) throws SemanticException {
		if (levelView == null || levelView.getCrosstab() != crosstab)
			return;
		String dimensionName = ((DimensionViewHandle) levelView.getContainer()).getCubeDimensionName();
		String levelName = levelView.getCubeLevelName();
		if (dimensionName == null || levelName == null)
			return;

		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			removeMeasureAggregations(dimensionName, levelName, levelView.getAxisType(), i);
		}
	}

	/**
	 * Removes all the aggregations related with the level view on particular
	 * measure.
	 * 
	 * @param levelView
	 */
	protected void removeMeasureAggregations(LevelViewHandle levelView, int measureIndex) throws SemanticException {
		if (levelView == null || levelView.getCrosstab() != crosstab)
			return;
		String dimensionName = ((DimensionViewHandle) levelView.getContainer()).getCubeDimensionName();
		String levelName = levelView.getCubeLevelName();
		if (dimensionName == null || levelName == null)
			return;

		if (measureIndex >= 0 && measureIndex < crosstab.getMeasureCount()) {
			removeMeasureAggregations(dimensionName, levelName, levelView.getAxisType(), measureIndex);
		}
	}

	/**
	 * Removes all the aggregations related with the grand-total in the specified
	 * axis type on all measures.
	 * 
	 * @param axisType
	 */
	protected void removeMeasureAggregations(int axisType) throws SemanticException {
		if (crosstab == null || !CrosstabModelUtil.isValidAxisType(axisType))
			return;

		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			removeMeasureAggregations(null, null, axisType, i);
		}
	}

	/**
	 * Removes all the aggregations related with the grand-total in the specified
	 * axis type on particular measure.
	 * 
	 * @param axisType
	 * @param measureIndex
	 * @throws SemanticException
	 */
	protected void removeMeasureAggregations(int axisType, int measureIndex) throws SemanticException {
		if (crosstab == null || !CrosstabModelUtil.isValidAxisType(axisType))
			return;

		if (measureIndex >= 0 && measureIndex < crosstab.getMeasureCount()) {
			removeMeasureAggregations(null, null, axisType, measureIndex);
		}
	}

	/**
	 * 
	 * @param dimensionName
	 * @param levelName
	 * @param axisType
	 */
	private void removeMeasureAggregations(String dimensionName, String levelName, int axisType, int measureIndex)
			throws SemanticException {
		List<AggregationCellHandle> dropList = new ArrayList<AggregationCellHandle>();

		MeasureViewHandle measureView = crosstab.getMeasure(measureIndex);

		for (int j = 0; j < measureView.getAggregationCount(); j++) {
			AggregationCellHandle aggregationCell = measureView.getAggregationCell(j);
			String propName = CrosstabModelUtil.getAggregationOnPropName(axisType);
			String value = aggregationCell.getModelHandle().getStringProperty(propName);
			if ((value == null && levelName == null) || (value != null && value.equals(levelName))) {
				dropList.add(aggregationCell);
			}
		}

		// batch remove all un-used cells
		for (int i = 0; i < dropList.size(); i++) {
			dropList.get(i).getModelHandle().drop();
		}
	}

	/**
	 * Returns if aggregation is needed on given level on specific axis, this is
	 * mainly to check if there's any existing aggregation cell on given level
	 * subtotal or grantotal(if given level view is null). One special case is the
	 * given axis area is blank(so level view is also null), in this case, we need
	 * check subtotal and grandtotal on couter axis.
	 * 
	 * @param measureView
	 * @param levelView
	 * @param axisType
	 * @return
	 */
	private boolean isAggregationNeeded(MeasureViewHandle measureView, LevelViewHandle levelView, int axisType,
			List<LevelViewHandle> counterAggregationLevels) {
		if (measureView != null) {
			String checkDimensionName = null;
			String checkLevelName = null;

			if (levelView != null) {
				checkDimensionName = ((DimensionViewHandle) levelView.getContainer()).getCubeDimensionName();
				checkLevelName = levelView.getCubeLevelName();
			}

			// if ( checkDimensionName == null && checkLevelName == null )
			// {
			// // this is grand total
			// int totalDims = crosstab.getDimensionCount( axisType );
			//
			// // no dimension on axis, ignore grand total checks
			// if ( totalDims == 0 )
			// {
			// return false;
			// }
			// }

			int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);

			boolean isInnerMost = levelView != null ? levelView.isInnerMost() : false;

			int totalDimensions = crosstab.getDimensionCount(axisType);

			if (isInnerMost || totalDimensions == 0) {
				// for innerest level or blank area, whether the aggregation is
				// needed depends on levels subtotal and grandtotal existance on
				// counter axis

				// check subtotal/grandtotal aggregation on couter axis except
				// innermost level
				if (counterAggregationLevels.size() > 0) {
					return true;
				}

				if (crosstab.getGrandTotal(counterAxisType) != null) {
					return true;
				}
			}

			int totalCounterDimensions = crosstab.getDimensionCount(counterAxisType);

			if (totalCounterDimensions > 0) {
				// check subtotal
				for (int i = 0; i < totalCounterDimensions; i++) {
					DimensionViewHandle dv = crosstab.getDimension(counterAxisType, i);

					int totalLevels = dv.getLevelCount();

					for (int j = 0; j < totalLevels; j++) {
						LevelViewHandle lv = dv.getLevel(j);

						if ((i == totalCounterDimensions - 1 && j == totalLevels - 1)
								|| lv.getAggregationHeader() != null) {
							AggregationCellHandle cell = null;

							if (axisType == ROW_AXIS_TYPE) {
								cell = measureView.getAggregationCell(checkDimensionName, checkLevelName,
										dv.getCubeDimensionName(), lv.getCubeLevelName());
							} else {
								cell = measureView.getAggregationCell(dv.getCubeDimensionName(), lv.getCubeLevelName(),
										checkDimensionName, checkLevelName);

							}

							if (cell != null) {
								return true;
							}
						}
					}
				}
			}

			// check grandtotal
			if (totalCounterDimensions == 0 || crosstab.getGrandTotal(counterAxisType) != null) {
				AggregationCellHandle cell = null;

				if (axisType == ROW_AXIS_TYPE) {
					cell = measureView.getAggregationCell(checkDimensionName, checkLevelName, null, null);
				} else {
					cell = measureView.getAggregationCell(null, null, checkDimensionName, checkLevelName);

				}

				if (cell != null) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * 
	 * @param crosstab
	 * @param measureView
	 * @param toValidateLevelView
	 * @param aggregationLevels
	 * @throws SemanticException
	 */
	protected void validateMeasure(MeasureViewHandle measureView, LevelViewHandle toValidateLevelView,
			int toValidateAxisType, List<LevelViewHandle> aggregationLevels) throws SemanticException {
		if (measureView == null || aggregationLevels == null
				|| (toValidateLevelView != null && measureView.getCrosstab() != toValidateLevelView.getCrosstab()))
			return;
		if (toValidateLevelView != null && toValidateLevelView.getCubeLevelName() == null)
			return;
		if (toValidateLevelView != null && toValidateAxisType != toValidateLevelView.getAxisType())
			return;

		boolean isInnerMost = toValidateLevelView == null ? false : toValidateLevelView.isInnerMost();
		List<LevelViewHandle> unAggregationLevels = new ArrayList<LevelViewHandle>();
		int unAggregationCount = 0;

		boolean needAggregation = isAggregationNeeded(measureView, toValidateLevelView, toValidateAxisType,
				aggregationLevels);

		int toValidataDimCount = crosstab.getDimensionCount(toValidateAxisType);

		if (aggregationLevels.size() > 0 && needAggregation) {
			for (int i = 0; i < aggregationLevels.size(); i++) {
				LevelViewHandle levelView = aggregationLevels.get(i);
				if (isInnerMost) {
					// if the toValidate is innermost, then no aggregation is
					// generated with this and counter innermost one in the
					// couter
					// axis
					if (!levelView.isInnerMost()) {
						assert levelView.getAggregationHeader() != null;
						if (getAggregation(measureView, toValidateLevelView, levelView) == null) {
							unAggregationLevels.add(levelView);
							unAggregationCount++;
						}
					}
				} else {
					// if the validate axis is blank, we should skip the measure
					// detail areas.
					if (toValidataDimCount > 0 || !levelView.isInnerMost()) {
						if (getAggregation(measureView, toValidateLevelView, levelView) == null) {
							unAggregationLevels.add(levelView);
							unAggregationCount++;
						}
					}
				}
			}
		}

		int maxAggregationCount = aggregationLevels.size();
		// if the counter axis has grand-total, then consider the aggregation
		// with it
		if (needAggregation && (maxAggregationCount == 0
				|| crosstab.getGrandTotal(CrosstabModelUtil.getOppositeAxisType(toValidateAxisType)) != null)) {
			if (getAggregation(measureView, toValidateLevelView, null) == null) {
				maxAggregationCount++;
				unAggregationCount++;
			}
		}

		// then do checks about the unAggregationLevels the aggregation count is
		// valid: 1) 0 or the max count -1 if toValidate is innermost; 2) 0 or
		// max count if toValidate is not innermost
		// if ( ( isInnerMost && unAggregationCount != 0 && unAggregationCount
		// != maxAggregationCount - 1 )
		// || ( !isInnerMost && unAggregationCount != 0 && unAggregationCount !=
		// maxAggregationCount ) )

		if (unAggregationCount > 0) {
			for (int i = 0; i < unAggregationLevels.size() && unAggregationCount > 0; i++) {
				LevelViewHandle levelView = unAggregationLevels.get(i);
				String function = getAggregationFunction(measureView, levelView,
						CrosstabModelUtil.getOppositeAxisType(toValidateAxisType), toValidateLevelView,
						toValidateAxisType);

				addAggregation(measureView, toValidateLevelView, levelView, function);
				unAggregationCount--;
			}

			// if unaggregationCount not equals 0, it means that the grand-total
			// is not validated, then handle this
			if (unAggregationCount != 0) {
				String function = getAggregationFunction(measureView, null,
						CrosstabModelUtil.getOppositeAxisType(toValidateAxisType), toValidateLevelView,
						toValidateAxisType);
				addAggregation(measureView, toValidateLevelView, null, function);
			}
		}

		// check any redundant aggreations on "toValidateAxis"
		boolean needCheckRedundantAggregations = false;

		// so far we only check for computed measures
		if (measureView instanceof ComputedMeasureViewHandle
				&& !CrosstabUtil.isLinkedDataModelMeasureView(measureView)) {
			// for computed measure, aggregation is not needed in following
			// cases:
			// 1. targetAxis==Row, measureDirection==Vertical
			// 1. targetAxis==Column, measureDirection==Horizontal

			int bypassAxis = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection()) ? ROW_AXIS_TYPE
					: COLUMN_AXIS_TYPE;

			if (toValidateAxisType == bypassAxis) {
				needCheckRedundantAggregations = true;
			}
		}

		if (needCheckRedundantAggregations) {
			// try remove unnecessary subtoal aggregations
			for (int i = 0; i < aggregationLevels.size(); i++) {
				LevelViewHandle levelView = aggregationLevels.get(i);

				// redundant aggreagtions only happen on non-innermost levels
				if (!isInnerMost) {
					// if the validate axis is blank, we should skip the measure
					// detail areas.
					if (toValidataDimCount > 0) {
						AggregationCellHandle aggCell = getAggregation(measureView, toValidateLevelView, levelView);

						if (aggCell != null) {
							aggCell.getModelHandle().drop();
						}
					}
				}
			}

			// try remove unnecessary grandtotal aggregations
			if (maxAggregationCount == 0
					|| crosstab.getGrandTotal(CrosstabModelUtil.getOppositeAxisType(toValidateAxisType)) != null) {
				// redundant aggreagtions only happen on non-innermost levels
				if (!isInnerMost) {
					// if the validate axis is blank, we should skip the measure
					// detail areas.
					if (toValidataDimCount > 0) {
						AggregationCellHandle aggCell = getAggregation(measureView, toValidateLevelView, null);

						if (aggCell != null) {
							aggCell.getModelHandle().drop();
						}
					}
				}
			}
		}

	}

	/**
	 * Gets the aggregation cell for the given two level views.
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param rightLevelView
	 * @return
	 */
	private AggregationCellHandle getAggregation(MeasureViewHandle measureView, LevelViewHandle leftLevelView,
			LevelViewHandle rightLevelView) {
		if (measureView == null || measureView.getCrosstab() != crosstab)
			return null;

		AggregationInfo infor = getAggregationInfo(leftLevelView, rightLevelView);
		if (infor == null)
			return null;

		return measureView.getAggregationCell(infor.getRowDimension(), infor.getRowLevel(), infor.getColDimension(),
				infor.getColLevel());
	}

	/**
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param rightLevelView
	 * @param function
	 * @throws SemanticException
	 */
	private void addAggregation(MeasureViewHandle measureView, LevelViewHandle leftLevelView,
			LevelViewHandle rightLevelView, String function) throws SemanticException {
		if (measureView == null || measureView.getCrosstab() != crosstab)
			return;
		AggregationInfo infor = getAggregationInfo(leftLevelView, rightLevelView);
		if (infor == null)
			return;

		validateSingleMeasureAggregation(measureView.getCrosstab(), measureView, function, infor.getRowDimension(),
				infor.getRowLevel(), infor.getColDimension(), infor.getColLevel());
	}

	/**
	 * 
	 * @param measureView
	 * @param leftLevelView
	 * @param leftAxisType
	 * @param rightLevelView
	 * @param rightAxisType
	 * @return
	 */
	private String getAggregationFunction(MeasureViewHandle measureView, LevelViewHandle leftLevelView,
			int leftAxisType, LevelViewHandle rightLevelView, int rightAxisType) {
		if (measureView == null || measureView.getCrosstab() != crosstab)
			return null;
		if (!CrosstabModelUtil.isValidAxisType(leftAxisType) || !CrosstabModelUtil.isValidAxisType(rightAxisType))
			return null;
		if (leftAxisType != CrosstabModelUtil.getOppositeAxisType(rightAxisType))
			return null;

		// search the column first, then the row
		if (COLUMN_AXIS_TYPE == leftAxisType) {
			String function = null;
			function = getAggregationFunction(measureView, leftLevelView, leftAxisType);
			if (function != null)
				return function;
			return getAggregationFunction(measureView, rightLevelView, rightAxisType);
		}
		String function = null;
		function = getAggregationFunction(measureView, rightLevelView, rightAxisType);
		if (function != null)
			return function;
		return getAggregationFunction(measureView, leftLevelView, leftAxisType);

	}

	/**
	 * Gets the aggregation function by the level view.
	 * 
	 * @param measureView
	 * @param levelView
	 * @param axisType
	 * @return
	 */
	private String getAggregationFunction(MeasureViewHandle measureView, LevelViewHandle levelView, int axisType) {
		// grand-total
		if (levelView == null)
			return crosstab.getAggregationFunction(axisType, measureView);
		// sub-total
		return levelView.getAggregationFunction(measureView);
	}

	/**
	 * 
	 * @param leftLevelView
	 * @param rightLevelView
	 * @throws SemanticException
	 */
	protected void removeMeasureAggregation(LevelViewHandle leftLevelView, LevelViewHandle rightLevelView)
			throws SemanticException {
		AggregationInfo infor = getAggregationInfo(leftLevelView, rightLevelView);
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			measureView.removeAggregation(infor.getRowDimension(), infor.getRowLevel(), infor.getColDimension(),
					infor.getColLevel());
		}
	}

	/**
	 * Validates the entire crosstab model.
	 * 
	 * @param crosstab
	 * @throws SemanticException
	 */
	public void validateCrosstab() throws SemanticException {
		if (crosstab == null)
			return;

		String measureDirection = crosstab.getMeasureDirection();
		int axisType = COLUMN_AXIS_TYPE;
		if (MEASURE_DIRECTION_HORIZONTAL.equals(measureDirection)) {
			// if measure is hotizontal, then do the validation according to the
			// column levels and grand-total
			axisType = COLUMN_AXIS_TYPE;
		} else {
			// if measure is vertical, then do the validtion according to the
			// row levels and grand-total
			axisType = ROW_AXIS_TYPE;
		}

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
		// all the levels that may need add cells to be aggregated on, each in
		// the list may be an innermost in the axis type or has sub-total
		List<LevelViewHandle> counterAxisAggregationLevels = CrosstabModelUtil.getAllAggregationLevels(crosstab,
				counterAxisType);
		List<LevelViewHandle> toValidateLevelViews = CrosstabModelUtil.getAllAggregationLevels(crosstab, axisType);

		// validate the aggregations for sub-total
		int count = toValidateLevelViews.size();
		for (int i = 0; i < count; i++) {
			LevelViewHandle levelView = toValidateLevelViews.get(i);

			// if the level is innermost or has sub-total, we should validate
			// the aggregations for it, otherwise need do nothing
			assert levelView.isInnerMost() || levelView.getAggregationHeader() != null;

			for (int j = 0; j < crosstab.getMeasureCount(); j++) {
				MeasureViewHandle measureView = crosstab.getMeasure(j);
				validateMeasure(measureView, levelView, axisType, counterAxisAggregationLevels);
			}
		}

		// validate aggregations for grand-total, if target area is blank, we
		// still need to verify grand total
		if (crosstab.getGrandTotal(axisType) != null || crosstab.getDimensionCount(axisType) == 0) {
			for (int j = 0; j < crosstab.getMeasureCount(); j++) {
				MeasureViewHandle measureView = crosstab.getMeasure(j);
				validateMeasure(measureView, null, axisType, counterAxisAggregationLevels);
			}

		}

		// validate aggregation on measure detail cell
		LevelViewHandle innerestRowLevel = CrosstabModelUtil.getInnerMostLevel(crosstab, ROW_AXIS_TYPE);
		LevelViewHandle innerestColLevel = CrosstabModelUtil.getInnerMostLevel(crosstab, COLUMN_AXIS_TYPE);

		validateMeasureDetails(innerestRowLevel, innerestColLevel);

		validateMeasureHeaders();
	}

	protected void validateMeasureDetails(LevelViewHandle innerestRowLevel, LevelViewHandle innerestColLevel)
			throws SemanticException {
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			validateSingleMeasureDetail(measureView, innerestRowLevel, innerestColLevel);
		}
	}

	private void validateSingleMeasureDetail(MeasureViewHandle measureView, LevelViewHandle rowLevelView,
			LevelViewHandle colLevelView) throws SemanticException {
		AggregationCellHandle detailCell = measureView.getCell();

		LevelHandle rowLevel = detailCell.getAggregationOnRow();
		LevelHandle colLevel = detailCell.getAggregationOnColumn();

		// update cell aggregateOn properties.

		if (rowLevelView == null) {
			detailCell.setAggregationOnRow(null);
		} else if (rowLevel == null || !rowLevel.equals(rowLevelView.getCubeLevel())) {
			detailCell.setAggregationOnRow(rowLevelView.getCubeLevel());
		}

		if (colLevelView == null) {
			detailCell.setAggregationOnColumn(null);
		} else if (colLevel == null || !colLevel.equals(colLevelView.getCubeLevel())) {
			detailCell.setAggregationOnColumn(colLevelView.getCubeLevel());
		}

		String function = CrosstabModelUtil.getAggregationFunction(crosstab, detailCell);

		Map<String, Object> extras = null;

		if (function != null) {
			extras = new HashMap<String, Object>();
			extras.put(ICrosstabUpdateListener.EXTRA_FUNCTION_HINT, function);
		}

		CrosstabModelUtil.notifyValidate(ICrosstabUpdateListener.MEASURE_DETAIL, detailCell, extras);
	}

	/**
	 * Add measure header for subtotal or grandtotal
	 * 
	 * @param axisType
	 * @param levelView
	 * @throws SemanticException
	 */
	protected void addTotalMeasureHeader(int axisType, LevelViewHandle levelView) throws SemanticException {
		if (crosstab == null || (levelView != null && axisType != levelView.getAxisType())) {
			return;
		}

		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			addTotalMeasureHeader(axisType, levelView, crosstab.getMeasure(i));
		}
	}

	/**
	 * Add measure header for subtotal or grandtotal
	 * 
	 * @param axisType
	 * @param levelView
	 * @param measureList
	 * @throws SemanticException
	 */
	protected void addTotalMeasureHeader(int axisType, LevelViewHandle levelView, List<MeasureViewHandle> measureList)
			throws SemanticException {
		if (crosstab == null || measureList == null || measureList.size() == 0
				|| (levelView != null && axisType != levelView.getAxisType())) {
			return;
		}

		for (int i = 0; i < measureList.size(); i++) {
			addTotalMeasureHeader(axisType, levelView, measureList.get(i));
		}
	}

	private void addTotalMeasureHeader(int axisType, LevelViewHandle levelView, MeasureViewHandle mv)
			throws SemanticException {
		if (mv == null) {
			return;
		}

		int targetAxis = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection()) ? ROW_AXIS_TYPE
				: COLUMN_AXIS_TYPE;

		if (targetAxis != axisType) {
			return;
		}

		if (levelView == null) {
			// header on grandtotal, should always be the last one
			ExtendedItemHandle newHeader = CrosstabExtendedItemFactory.createCrosstabCell(mv.getModuleHandle());

			mv.getHeaderProperty().add(newHeader);

			CrosstabModelUtil.notifyCreation(ICrosstabUpdateListener.MEASURE_HEADER,
					CrosstabUtil.getReportItem(newHeader), null);
		} else {
			List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels(crosstab, targetAxis);

			// we need the reversed order here to count from inner most to
			// outer most
			Collections.reverse(levels);

			for (int i = 0; i < levels.size(); i++) {
				// find the matching header index
				if (levelView == levels.get(i)) {
					ExtendedItemHandle newHeader = CrosstabExtendedItemFactory.createCrosstabCell(mv.getModuleHandle());

					mv.getHeaderProperty().add(newHeader, i);

					CrosstabModelUtil.notifyCreation(ICrosstabUpdateListener.MEASURE_HEADER,
							CrosstabUtil.getReportItem(newHeader), null);

					break;
				}
			}
		}
	}

	/**
	 * Removes the measure header associated with subtotal or grandtotal
	 * 
	 * @param axisType
	 * @param levelView
	 * @throws SemanticException
	 */
	protected void removeTotalMeasureHeader(int axisType, LevelViewHandle levelView) throws SemanticException {
		if (crosstab == null || (levelView != null && axisType != levelView.getAxisType())) {
			return;
		}

		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			removeTotalMeasureHeader(axisType, levelView, i);
		}
	}

	/**
	 * Removes the measure header associated with subtotal or grandtotal
	 * 
	 * @param axisType
	 * @param levelView
	 * @param measureIndex
	 * @throws SemanticException
	 */
	protected void removeTotalMeasureHeader(int axisType, LevelViewHandle levelView, int measureIndex)
			throws SemanticException {
		if (crosstab == null || (levelView != null && axisType != levelView.getAxisType())) {
			return;
		}

		MeasureViewHandle mv = crosstab.getMeasure(measureIndex);
		int targetAxis = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection()) ? ROW_AXIS_TYPE
				: COLUMN_AXIS_TYPE;

		if (targetAxis != axisType || mv == null) {
			return;
		}

		if (levelView == null) {
			// header on grandtotal should always be the last one if it exists
			if (CrosstabModelUtil.isAggregationOn(mv, null, targetAxis)) {
				CrosstabCellHandle header = mv.getHeader(mv.getHeaderCount() - 1);

				if (header != null) {
					header.getModelHandle().drop();
				}
			}
		} else {
			LevelViewHandle innerMost = CrosstabModelUtil.getInnerMostLevel(crosstab, targetAxis);

			if (levelView == innerMost) {
				// should not reach here, otherwise, it may be a code logic
				// error.
				assert false;
			} else {
				List<LevelViewHandle> levels = CrosstabModelUtil.getAllAggregationLevels(crosstab, targetAxis);

				// we need the reversed order here to count from inner most to
				// outer most
				Collections.reverse(levels);

				int realIndex = 0;

				for (int i = 0; i < levels.size(); i++) {
					LevelViewHandle lv = levels.get(i);

					if (lv == innerMost || CrosstabModelUtil.isAggregationOn(mv, lv.getCubeLevelName(), targetAxis)) {
						// find the real header index
						if (levelView == lv) {
							CrosstabCellHandle header = mv.getHeader(realIndex);

							if (header != null) {
								header.getModelHandle().drop();
							}

							break;
						}

						realIndex++;
					}
				}
			}
		}
	}

	private void validateMeasureHeaders() throws SemanticException {
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			validateSingleMeasureHeader(measureView);
		}
	}

	private void validateSingleMeasureHeader(MeasureViewHandle measureView) throws SemanticException {
		if (measureView == null) {
			return;
		}

		// check expected measure header count
		int expectHeaders = CrosstabModelUtil.computeAllMeasureHeaderCount(crosstab, measureView);
		int availableHeaders = measureView.getHeaderCount();

		if (availableHeaders < expectHeaders) {
			// add missing header cells
			PropertyHandle propHandle = measureView.getHeaderProperty();

			for (int i = 0; i < expectHeaders - availableHeaders; i++) {
				ExtendedItemHandle headerCell = CrosstabExtendedItemFactory
						.createCrosstabCell(measureView.getModuleHandle());
				propHandle.add(headerCell);

				CrosstabModelUtil.notifyCreation(ICrosstabUpdateListener.MEASURE_HEADER,
						CrosstabUtil.getReportItem(headerCell), null);
			}
		} else if (availableHeaders > expectHeaders) {
			// remove redundant header cells
			PropertyHandle propHandle = measureView.getHeaderProperty();
			List contents = propHandle.getContents();

			for (int i = 0; i < availableHeaders - expectHeaders; i++) {
				((DesignElementHandle) contents.get(contents.size() - i - 1)).drop();
			}
		}
	}

	protected void validateSingleMeasureAggregation(CrosstabReportItemHandle crosstab, MeasureViewHandle measureView,
			String function, String rowDimension, String rowLevel, String colDimension, String colLevel)
			throws SemanticException {
		if (crosstab == null || measureView == null)
			return;

		AggregationCellHandle cell = measureView.getAggregationCell(rowDimension, rowLevel, colDimension, colLevel);

		if (cell == null) {
			cell = measureView.addAggregation(rowDimension, rowLevel, colDimension, colLevel);
		}

		Map<String, Object> extras = null;

		if (function != null) {
			extras = new HashMap<String, Object>();
			extras.put(ICrosstabUpdateListener.EXTRA_FUNCTION_HINT, function);
		}

		CrosstabModelUtil.notifyValidate(ICrosstabUpdateListener.MEASURE_AGGREGATION, cell, extras);
	}

	/**
	 * AggregationInfo
	 */
	static class AggregationInfo {

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		/**
		 * 
		 * @param rowDimension
		 * @param rowLevel
		 * @param colDimension
		 * @param colLevel
		 */
		public AggregationInfo(String rowDimension, String rowLevel, String colDimension, String colLevel) {
			this.rowDimension = rowDimension;
			this.rowLevel = rowLevel;
			this.colDimension = colDimension;
			this.colLevel = colLevel;
		}

		/**
		 * @return the rowDimension
		 */
		public String getRowDimension() {
			return rowDimension;
		}

		/**
		 * @return the rowLevel
		 */
		public String getRowLevel() {
			return rowLevel;
		}

		/**
		 * @return the colDimension
		 */
		public String getColDimension() {
			return colDimension;
		}

		/**
		 * @return the colLevel
		 */
		public String getColLevel() {
			return colLevel;
		}
	}

}
