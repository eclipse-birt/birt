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
import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * LevelViewTask
 */
public class LevelViewTask extends AbstractCrosstabModelTask {

	protected LevelViewHandle focus = null;

	/**
	 * 
	 * @param theCrosstab
	 * @param levelView
	 */
	public LevelViewTask(LevelViewHandle levelView) {
		super(levelView);
		this.focus = levelView;
	}

	/**
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addSubTotal(List<MeasureViewHandle> measureList, List<String> functionList)
			throws SemanticException {
		return addSubTotal(measureList, functionList, true);
	}

	/**
	 * 
	 * @param measureList
	 * @param functionList
	 * @param levelView
	 * @return
	 * @throws SemanticException
	 */
	CrosstabCellHandle addSubTotal(List<MeasureViewHandle> measureList, List<String> functionList,
			boolean needTransaction) throws SemanticException {
		if (focus == null || !isValidParameters(functionList, measureList))
			return null;

		verifyTotalMeasureFunctions(focus.getAxisType(), functionList, measureList);

		// can not add aggregation if this level is innermost
		if (focus.isInnerMost()) {
			focus.getLogger().log(Level.WARNING, "This level: [" + focus.getModelHandle().getName() //$NON-NLS-1$
					+ "] can not add aggregation for it is innermost"); //$NON-NLS-1$
			return null;
		}
		if (focus.getAggregationHeader() != null) {
			focus.getLogger().log(Level.INFO, "the aggregation header is set"); //$NON-NLS-1$
		}

		CommandStack stack = null;
		if (needTransaction) {
			stack = focus.getCommandStack();
			stack.startTrans(Messages.getString("LevelViewTask.msg.add.subtotal")); //$NON-NLS-1$
		}

		try {
			if (focus.getAggregationHeader() == null)
				focus.getAggregationHeaderProperty()
						.add(CrosstabExtendedItemFactory.createCrosstabCell(focus.getModuleHandle()));

			// adjust the measure aggregations
			if (crosstab != null && measureList != null) {
				// if measure direction is parallel with the target axis, we
				// need set the flag to check available aggreagtions on counter
				// axis.
				// !!! Note this check logic tightly adhere with the logic in
				// <code>validateCrosstab</code>, should be careful in case it
				// need be changed.
				boolean isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection());
				boolean needCheckCounterAxis = ((isVerticalMeasure && focus.getAxisType() == COLUMN_AXIS_TYPE)
						|| (!isVerticalMeasure && focus.getAxisType() == ROW_AXIS_TYPE));

				addMeasureAggregations(focus, measureList, functionList, needCheckCounterAxis);

				addTotalMeasureHeader(focus.getAxisType(), focus, measureList);
			}

			validateCrosstab();

			// revalidate the aggregation functions, the function may be ignored
			// in previous processing.
			if (measureList != null && functionList != null) {
				for (int i = 0; i < measureList.size(); i++) {
					MeasureViewHandle mv = measureList.get(i);
					String func = functionList.get(i);

					setAggregationFunction(mv, func);
				}
			}
		} catch (SemanticException e) {
			focus.getLogger().log(Level.WARNING, e.getMessage(), e);

			if (needTransaction) {
				stack.rollback();
			}

			throw e;
		}

		if (needTransaction) {
			stack.commit();
		}

		return focus.getAggregationHeader();
	}

	/**
	 * Removes the sub-total.
	 * 
	 * @throws SemanticException
	 */
	public void removeSubTotal() throws SemanticException {
		if (focus.getAggregationHeader() != null) {
			CommandStack stack = focus.getCommandStack();
			stack.startTrans(Messages.getString("LevelViewTask.msg.remove.subtotal")); //$NON-NLS-1$

			try {
				// adjust the aggregations in measure elements first, then
				// remove the aggregation; for the adjustment actions should
				// depend on the aggregation information set in this level view
				if (crosstab != null) {
					removeTotalMeasureHeader(focus.getAxisType(), focus);

					removeMeasureAggregations(focus);
				}

				focus.getAggregationHeaderProperty().drop(0);
			} catch (SemanticException e) {
				focus.getLogger().log(Level.WARNING, e.getMessage(), e);
				stack.rollback();
				throw e;
			}

			stack.commit();
		}
	}

	/**
	 * Removes the sub-total on particular measure.
	 * 
	 * @throws SemanticException
	 */
	public void removeSubTotal(int measureIndex) throws SemanticException {
		if (focus.getAggregationHeader() != null) {
			CommandStack stack = focus.getCommandStack();
			stack.startTrans(Messages.getString("LevelViewTask.msg.remove.subtotal")); //$NON-NLS-1$

			try {
				// adjust the aggregations in measure elements first, then
				// remove the aggregation; for the adjustment actions should
				// depend on the aggregation information set in this level view
				if (crosstab != null) {
					removeTotalMeasureHeader(focus.getAxisType(), focus, measureIndex);

					removeMeasureAggregations(focus, measureIndex);
				}

				if (getAggregationMeasures().size() == 0) {
					// remove subtotal header if no subtotal aggregations on
					// all measures
					focus.getAggregationHeaderProperty().drop(0);
				}
			} catch (SemanticException e) {
				focus.getLogger().log(Level.WARNING, e.getMessage(), e);
				stack.rollback();
				throw e;
			}

			stack.commit();
		}
	}

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	public void validateLevelView() throws SemanticException {
		if (crosstab == null)
			return;

		String levelName = focus.getCubeLevelName();
		if (levelName == null || levelName.length() == 0)
			return;
		int axisType = focus.getAxisType();
		String measureDirection = crosstab.getMeasureDirection();
		boolean isInnerMost = focus.isInnerMost();

		if (needValidate(axisType, isInnerMost, measureDirection)) {
			CommandStack stack = crosstab.getCommandStack();
			stack.startTrans("Validate Level"); //$NON-NLS-1$
			try {
				doValidateAggregations(axisType);
			} catch (SemanticException e) {
				stack.rollback();
				throw e;
			}

			stack.commit();
		}
	}

	/**
	 * Checks whether to do validation for the level view.
	 * 
	 * @param axisType
	 * @param isInnerMost
	 * @param measureDirection
	 * @return
	 */
	private boolean needValidate(int axisType, boolean isInnerMost, String measureDirection) {
		// if the leve view is innermost, we need to validate aggregations
		// whether the measure direction is horizontal or vertical
		if (isInnerMost)
			return true;

		// if measure direction is horizontal, then we only validate levels in
		// column axis
		if (MEASURE_DIRECTION_HORIZONTAL.equals(measureDirection) && axisType == COLUMN_AXIS_TYPE)
			return true;

		// if measure direction is vertical, then we only validate levels in row
		// axis
		if (MEASURE_DIRECTION_VERTICAL.equals(measureDirection) && axisType == ROW_AXIS_TYPE)
			return true;
		return false;
	}

	/**
	 * 
	 * @param aggregationLevelList
	 * @throws SemanticException
	 */
	private void doValidateAggregations(int axisType) throws SemanticException {
		List<LevelViewHandle> aggregationLevelList = CrosstabModelUtil.getAllAggregationLevels(crosstab,
				CrosstabModelUtil.getOppositeAxisType(axisType));
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			validateMeasure(measureView, focus, axisType, aggregationLevelList);
		}
	}

	/**
	 * Gets the aggregation function of this level view applying on the specified
	 * measure view.
	 * 
	 * @param measureView
	 * @return
	 */
	public String getAggregationFunction(MeasureViewHandle measureView) {
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if (focus == null)
			return null;
		String levelName = focus.getCubeLevelName();
		if (focus.getAggregationHeader() == null || levelName == null || levelName.length() <= 0 || measureView == null)
			return null;

		// if crosstab is not found, or level and measure not reside in the same
		// one then return null
		if (crosstab == null || crosstab != measureView.getCrosstab())
			return null;

		if (focus.isInnerMost()) {
			String function = CrosstabModelUtil.getAggregationFunction(crosstab, measureView.getCell());

			if (function != null) {
				return function;
			}
		}

		int axisType = focus.getAxisType();
		String propName = CrosstabModelUtil.getAggregationOnPropName(axisType);

		// retrieve all aggregations for the measure a
		for (int j = 0; j < measureView.getAggregationCount(); j++) {
			AggregationCellHandle cell = measureView.getAggregationCell(j);
			if (levelName.equals(cell.getModelHandle().getStringProperty(propName))) {
				String function = CrosstabModelUtil.getAggregationFunction(crosstab, cell);
				if (function != null)
					return function;
			}
		}

		return null;
	}

	/**
	 * Gets the measure view list that define aggregations for the given level view.
	 * Each item in the list is an instance of <code>MeasureViewHandle</code> .
	 * 
	 * @param levelView
	 * @return
	 */
	public List<MeasureViewHandle> getAggregationMeasures() {
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if (focus.getAggregationHeader() == null || focus.getCubeLevelName() == null
				|| focus.getCubeLevelName().length() <= 0)
			return Collections.emptyList();
		if (crosstab == null)
			return Collections.emptyList();

		int axisType = focus.getAxisType();
		String levelName = focus.getCubeLevelName();
		List<MeasureViewHandle> measures = new ArrayList<MeasureViewHandle>();
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			if (measures.contains(measureView))
				continue;
			if (CrosstabModelUtil.isAggregationOn(measureView, levelName, axisType))
				measures.add(measureView);
		}
		return measures;
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
		// if level view is null, or aggregation header is not set, or cube
		// level is not set, then return empty
		if (focus.getAggregationHeader() == null || focus.getCubeLevelName() == null
				|| focus.getCubeLevelName().length() <= 0 || measureView == null)
			return;

		// if crosstab is not found, or level and measure not reside in the same
		// one then return null
		if (crosstab == null || crosstab != measureView.getCrosstab()) {
			return;
		}

		String levelName = focus.getCubeLevelName();
		int axisType = focus.getAxisType();
		String propName = CrosstabModelUtil.getAggregationOnPropName(axisType);

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.set.aggregate.function")); //$NON-NLS-1$

		try {
			// retrieve all aggregations for the measure
			for (int j = 0; j < measureView.getAggregationCount(); j++) {
				AggregationCellHandle cell = measureView.getAggregationCell(j);
				if (levelName.equals(cell.getModelHandle().getStringProperty(propName))) {
					// TODO: now we will change the function in the referred
					// column binding, need we generate a new computed column
					// rather thann write the old one, for the old one may be
					// used by other elements
					CrosstabModelUtil.setAggregationFunction(crosstab, cell, function);
				}
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

}
