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

import java.util.List;
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;

/**
 * CrosstabViewTask
 */
public class CrosstabViewTask extends AbstractCrosstabModelTask {

	protected CrosstabViewHandle crosstabView = null;

	/**
	 * 
	 * @param focus
	 */
	public CrosstabViewTask(CrosstabViewHandle focus) {
		super(focus);
		crosstabView = focus;
	}

	/**
	 * 
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addGrandTotal(List<MeasureViewHandle> measureList, List<String> functionList)
			throws SemanticException {
		return addGrandTotal(measureList, functionList, true);
	}

	CrosstabCellHandle addGrandTotal(List<MeasureViewHandle> measureList, List<String> functionList,
			boolean needTransaction) throws SemanticException {
		if (!isValidParameters(functionList, measureList))
			return null;

		verifyTotalMeasureFunctions(crosstabView.getAxisType(), functionList, measureList);

		PropertyHandle propHandle = crosstabView.getGrandTotalProperty();

		CommandStack stack = null;

		if (needTransaction) {
			stack = crosstabView.getCommandStack();
			stack.startTrans(Messages.getString("CrosstabViewTask.msg.add.grandtotal")); //$NON-NLS-1$
		}

		CrosstabCellHandle totalCell = null;

		try {
			ExtendedItemHandle grandTotal = null;
			if (propHandle.getContentCount() <= 0) {
				grandTotal = CrosstabExtendedItemFactory.createCrosstabCell(crosstabView.getModuleHandle());
				propHandle.add(grandTotal);
			}

			// adjust the measure aggregations
			CrosstabReportItemHandle crosstab = crosstabView.getCrosstab();
			if (crosstab != null && measureList != null) {
				// if measure direction is parallel with the target axis, we
				// need set the flag to check available aggreagtions on counter
				// axis.
				// !!! Note this check logic tightly adhere with the logic in
				// <code>validateCrosstab</code>, should be careful in case it
				// need be changed.
				boolean isVerticalMeasure = MEASURE_DIRECTION_VERTICAL.equals(crosstab.getMeasureDirection());
				boolean needCheckCounterAxis = ((isVerticalMeasure && crosstabView.getAxisType() == COLUMN_AXIS_TYPE)
						|| (!isVerticalMeasure && crosstabView.getAxisType() == ROW_AXIS_TYPE));

				addMeasureAggregations(crosstabView.getAxisType(), measureList, functionList, needCheckCounterAxis);

				// adjust measure header
				addTotalMeasureHeader(crosstabView.getAxisType(), null, measureList);
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

			totalCell = (CrosstabCellHandle) CrosstabUtil.getReportItem(grandTotal);
		} catch (SemanticException e) {
			crosstabView.getLogger().log(Level.INFO, e.getMessage(), e);

			if (needTransaction) {
				stack.rollback();
			}

			throw e;
		}

		if (needTransaction) {
			stack.commit();
		}

		return totalCell;
	}

	private void setAggregationFunction(MeasureViewHandle measureView, String function) throws SemanticException {
		if (crosstabView.getGrandTotal() == null || measureView == null)
			return;

		// if crosstab is not found, or level and measure not reside in the same
		// one then return null
		if (crosstab == null || crosstab != measureView.getCrosstab()) {
			return;
		}

		int axisType = crosstabView.getAxisType();
		String propName = CrosstabModelUtil.getAggregationOnPropName(axisType);

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.set.aggregate.function")); //$NON-NLS-1$

		try {
			// retrieve all aggregations for the measure
			for (int j = 0; j < measureView.getAggregationCount(); j++) {
				AggregationCellHandle cell = measureView.getAggregationCell(j);
				if (cell.getModelHandle().getStringProperty(propName) == null) {
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

	/**
	 * Removes grand total from crosstab if it is not empty, otherwise do nothing.
	 */
	public void removeGrandTotal() throws SemanticException {
		removeGrandTotal(true);
	}

	/**
	 * Remove grand total on particular measure
	 */
	public void removeGrandTotal(int measureIndex) throws SemanticException {
		removeGrandTotal(measureIndex, true);
	}

	void removeGrandTotal(int measureIndex, boolean needTransaction) throws SemanticException {
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty();

		if (propHandle.getContentCount() > 0) {
			CommandStack stack = null;

			if (needTransaction) {
				stack = crosstabView.getCommandStack();
				stack.startTrans(Messages.getString("CrosstabViewTask.msg.remove.grandtotal")); //$NON-NLS-1$
			}

			try {
				removeTotalMeasureHeader(crosstabView.getAxisType(), null, measureIndex);

				removeMeasureAggregations(crosstabView.getAxisType(), measureIndex);

				if (new CrosstabReportItemTask(crosstab).getAggregationMeasures(crosstabView.getAxisType())
						.size() == 0) {
					// remove grandtotal header if no grandtotal aggregations on
					// all measures
					propHandle.drop(0);
				}
			} catch (SemanticException e) {
				crosstabView.getLogger().log(Level.INFO, e.getMessage(), e);

				if (needTransaction) {
					stack.rollback();
				}

				throw e;
			}

			if (needTransaction) {
				stack.commit();
			}
		}
	}

	/**
	 * Removes grand total from crosstab if it is not empty, otherwise do nothing.
	 */
	void removeGrandTotal(boolean needTransaction) throws SemanticException {
		PropertyHandle propHandle = crosstabView.getGrandTotalProperty();

		if (propHandle.getContentCount() > 0) {
			CommandStack stack = null;

			if (needTransaction) {
				stack = crosstabView.getCommandStack();
				stack.startTrans(Messages.getString("CrosstabViewTask.msg.remove.grandtotal")); //$NON-NLS-1$
			}

			try {
				// adjust the measure aggregations before remove the grand-total
				// cell, for some adjustment action should depend on the
				// grand-total information; if there is no level in this axis,
				// then we need do nothing about the aggregations
				if (crosstab != null)
				// && CrosstabModelUtil.getAllLevelCount( crosstab,
				// crosstabView.getAxisType( ) ) > 0 )
				{
					removeTotalMeasureHeader(crosstabView.getAxisType(), null);

					removeMeasureAggregations(crosstabView.getAxisType());
				}

				propHandle.drop(0);
			} catch (SemanticException e) {
				crosstabView.getLogger().log(Level.INFO, e.getMessage(), e);

				if (needTransaction) {
					stack.rollback();
				}

				throw e;
			}

			if (needTransaction) {
				stack.commit();
			}
		}
	}

	/**
	 * Removes a dimension view that refers a cube dimension name with the given
	 * name from the design tree.
	 * 
	 * @param name name of the dimension view to remove
	 * @throws SemanticException
	 */
	public void removeDimension(String name) throws SemanticException {
		DimensionViewHandle dimensionView = crosstabView.getDimension(name);

		if (dimensionView == null) {
			crosstabView.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name);
			throw new CrosstabException(crosstabView.getModelHandle().getElement(),
					Messages.getString(MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name));
		}

		removeDimension(dimensionView, true);
	}

	/**
	 * Removes a dimension view in the given position. Index is 0-based integer.
	 * 
	 * @param index the position index of the dimension to remove, 0-based integer
	 * @throws SemanticException
	 */
	public void removeDimension(int index) throws SemanticException {
		DimensionViewHandle dimensionView = crosstabView.getDimension(index);
		if (dimensionView == null) {
			crosstabView.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					String.valueOf(index));
			return;
		}

		removeDimension(dimensionView, true);
	}

	public void removeDimension(DimensionViewHandle dimensionView) throws SemanticException {
		removeDimension(dimensionView, true);
	}

	void removeDimension(DimensionViewHandle dimensionView, boolean needTransaction) throws SemanticException {
		if (dimensionView == null || dimensionView.getContainer() != crosstabView)
			return;

		CommandStack stack = null;

		if (needTransaction) {
			stack = crosstabView.getCommandStack();
			stack.startTrans(Messages.getString("CrosstabViewTask.msg.remove.dimension")); //$NON-NLS-1$
		}

		int count = dimensionView.getLevelCount();

		try {
			// check if this is the last dimension in current view, then we need
			// remove the grand total on this axis after the removing.
			// !!! Note this operatin must be before the removing of the
			// dimension view, as during removing dimension view, it will
			// automatically adjust the grandtotal aggregations according to
			// counter axis state.
			if (crosstabView.getDimensionCount() == 1) {
				removeGrandTotal(false);
			}

			// adjust measure aggregations and then remove dimension view from
			// the design tree, the order can not be reversed
			if (crosstab != null) {
				DimensionViewTask dimTask = new DimensionViewTask(dimensionView);

				for (int i = 0; i < count; i++) {
					LevelViewHandle lv = dimensionView.getLevel(0);

					if (lv != null) {
						dimTask.removeLevel(lv, false);
					}
				}
			}

			dimensionView.getModelHandle().drop();
		} catch (SemanticException e) {
			if (needTransaction) {
				stack.rollback();
			}

			throw e;
		}

		if (needTransaction) {
			stack.commit();
		}
	}

	/**
	 * Add dimension view into crosstab view.
	 * 
	 * @param dimensionView
	 * @param targetIndex
	 * @param needTransaction
	 * @throws SemanticException
	 */
	public void addDimension(DimensionViewHandle dimensionView, int targetIndex, boolean needTransaction)
			throws SemanticException {
		if (dimensionView == null) {
			return;
		}

		CommandStack stack = null;

		if (needTransaction) {
			stack = crosstabView.getCommandStack();
			stack.startTrans(Messages.getString("CrosstabViewTask.msg.add.dimension")); //$NON-NLS-1$
		}

		try {
			// Get old inner most level view handle
			int axisType = crosstabView.getAxisType();
			LevelViewHandle oldLevelView = CrosstabModelUtil.getInnerMostLevel(crosstab, axisType);

			// Add dimension into crosstab view
			int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
			crosstabView.getViewsProperty().add(dimensionView.getModelHandle(), targetIndex);

			if (crosstab.getGrandTotal(counterAxisType) != null
					|| CrosstabModelUtil.getAllLevelCount(crosstab, counterAxisType) == 0) {
				LevelViewHandle lastLevelView = dimensionView.getLevel(dimensionView.getLevelCount() - 1);
				if (lastLevelView != null && lastLevelView.isInnerMost()) {
					// After add dimension, the innerMost level view is changed
					// Should update grand-total
					List measureList = crosstab.getAggregationMeasures(counterAxisType);
					AggregationInfo oldInfo = getAggregationInfo(oldLevelView, null);
					AggregationInfo info = getAggregationInfo(lastLevelView, null);
					for (int i = 0; i < measureList.size(); i++) {
						MeasureViewHandle measureView = (MeasureViewHandle) measureList.get(i);

						// Remove old grand-total aggregation cell
						AggregationCellHandle cell = measureView.getAggregationCell(oldInfo.getRowDimension(),
								oldInfo.getRowLevel(), oldInfo.getColDimension(), oldInfo.getColLevel());
						if (cell != null) {
							// Drop it since inner most level is changed.
							cell.getModelHandle().drop();
						}

						// Update aggregation cell
						String function = crosstab.getAggregationFunction(counterAxisType, measureView);
						validateSingleMeasureAggregation(crosstab, measureView, function, info.getRowDimension(),
								info.getRowLevel(), info.getColDimension(), info.getColLevel());
					}
				}
			}
		} catch (SemanticException e) {
			if (needTransaction) {
				stack.rollback();
			}

			throw e;
		}

		if (needTransaction) {
			stack.commit();
		}
	}
}
