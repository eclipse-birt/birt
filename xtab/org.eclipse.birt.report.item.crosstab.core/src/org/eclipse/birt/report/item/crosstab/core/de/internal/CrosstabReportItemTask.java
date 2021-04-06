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
import java.util.logging.Level;

import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.AggregationCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.DimensionHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;

/**
 * CrosstabReportItemTask
 */
public class CrosstabReportItemTask extends AbstractCrosstabModelTask implements ICrosstabReportItemConstants {

	/**
	 * 
	 * @param focus
	 */
	public CrosstabReportItemTask(CrosstabReportItemHandle focus) {
		super(focus);
		this.crosstab = focus;
	}

	/**
	 * Adds the grandtotal on given axis for the specified measures. Depending on
	 * the measure direction, it will automatically decide if need process other
	 * measures even if it's not in the given list to resovle crosstab model
	 * consistency.
	 * 
	 * @param axisType
	 * @param measureList
	 * @param functionList
	 * @return
	 * @throws SemanticException
	 */
	public CrosstabCellHandle addGrandTotal(int axisType, List<MeasureViewHandle> measureList,
			List<String> functionList) throws SemanticException {
		if (crosstab == null || !CrosstabModelUtil.isValidAxisType(axisType))
			return null;

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.add.grandtotal")); //$NON-NLS-1$

		CrosstabCellHandle grandTotal = null;

		try {
			CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axisType);

			if (crosstabView == null) {
				crosstabView = crosstab.addCrosstabView(axisType);
			}

			grandTotal = new CrosstabViewTask(crosstabView).addGrandTotal(measureList, functionList, false);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();

		return grandTotal;
	}

	/**
	 * Removes row/column grand total from crosstab if it is not empty, otherwise do
	 * nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @throws SemanticException
	 */
	public void removeGrandTotal(int axisType) throws SemanticException {
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axisType);

		if (crosstabView != null) {
			crosstabView.removeGrandTotal();
		}
	}

	/**
	 * Removes row/column grand total from crosstab on particular measure, otherwise
	 * do nothing. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>.
	 * 
	 * @param axisType
	 * @param measureIndex
	 * @throws SemanticException
	 */
	public void removeGrandTotal(int axisType, int measureIndex) throws SemanticException {
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axisType);

		if (crosstabView != null) {
			crosstabView.removeGrandTotal(measureIndex);
		}
	}

	/**
	 * Gets the measure view list that has defined aggregations for the row/column
	 * grand total in the crosstab. Each item in the list is an instance of
	 * <code>MeasureViewHandle</code>.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @return
	 */
	public List<MeasureViewHandle> getAggregationMeasures(int axisType) {
		// if crosstab is null or has no grand total, then return empty
		if (crosstab == null || crosstab.getGrandTotal(axisType) == null)
			return Collections.emptyList();

		List<MeasureViewHandle> measures = new ArrayList<MeasureViewHandle>();
		for (int i = 0; i < crosstab.getMeasureCount(); i++) {
			MeasureViewHandle measureView = crosstab.getMeasure(i);
			if (measures.contains(measureView))
				continue;
			if (CrosstabModelUtil.isAggregationOn(measureView, null, axisType))
				measures.add(measureView);
		}
		return measures;
	}

	/**
	 * Gets the aggregation function for the row/column grand total in the crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @return
	 */
	public String getAggregationFunction(int axisType, MeasureViewHandle measureView) {
		// if crosstab is null or not define any grand total, then return null
		if (crosstab == null || crosstab.getGrandTotal(axisType) == null || measureView == null
				|| crosstab != measureView.getCrosstab())
			return null;

		for (int j = 0; j < measureView.getAggregationCount(); j++) {
			AggregationCellHandle cell = measureView.getAggregationCell(j);
			if ((axisType == COLUMN_AXIS_TYPE && cell.getAggregationOnColumn() == null)
					|| (axisType == ROW_AXIS_TYPE && cell.getAggregationOnRow() == null)) {
				String function = CrosstabModelUtil.getAggregationFunction(crosstab, cell);
				if (function != null)
					return function;
			}
		}
		return null;
	}

	/**
	 * Sets the aggregation function for the row/column grand total in the crosstab.
	 * 
	 * @param crosstab
	 * @param axisType
	 * @param measureView
	 * @param function
	 * @throws SemanticException
	 */
	public void setAggregationFunction(int axisType, MeasureViewHandle measureView, String function)
			throws SemanticException {
		// if crosstab is null or not define any grand total, then return null
		if (crosstab == null || crosstab.getGrandTotal(axisType) == null || measureView == null
				|| crosstab != measureView.getCrosstab())
			return;

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.set.aggregate.function")); //$NON-NLS-1$

		try {
			for (int j = 0; j < measureView.getAggregationCount(); j++) {
				AggregationCellHandle cell = measureView.getAggregationCell(j);
				if ((axisType == COLUMN_AXIS_TYPE && cell.getAggregationOnColumn() == null)
						|| (axisType == ROW_AXIS_TYPE && cell.getAggregationOnRow() == null)) {
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
	 * Swaps the crosstab row area and column area. Note this call is not equivalent
	 * to calling <code>pivotDimension</code> to interchange dimensions from row
	 * area to column area. Specifically this call will retain all the original
	 * subtotal and grandtotal info in both area after the swapping, while
	 * <code>pivotDimension</code> may remove the grandtotal or recreate some cells
	 * during the processing.
	 * 
	 * @throws SemanticException
	 */
	public void pivotCrosstab() throws SemanticException {
		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.pivot.crosstab")); //$NON-NLS-1$

		try {
			// swap the dimension views
			int transfered = transferDimensions(ROW_AXIS_TYPE, 0);
			transferDimensions(COLUMN_AXIS_TYPE, transfered);

			// swap the grandtotals
			CrosstabCellHandle replaced = transferGrandTotal(ROW_AXIS_TYPE, null, true);
			transferGrandTotal(COLUMN_AXIS_TYPE, replaced, false);

			// swap the aggregationOn property on measure aggregation cells
			for (int i = 0; i < crosstab.getMeasureCount(); i++) {
				MeasureViewHandle mv = crosstab.getMeasure(i);

				swapAggregateOn(mv.getCell());

				for (int j = 0; j < mv.getAggregationCount(); j++) {
					swapAggregateOn(mv.getAggregationCell(j));
				}
			}

			// swap other view properties
			swapViewProperties();

			// swap the measure direction
			String oldDirction = crosstab.getMeasureDirection();
			String newDirection = MEASURE_DIRECTION_HORIZONTAL.equals(oldDirction) ? MEASURE_DIRECTION_VERTICAL
					: MEASURE_DIRECTION_HORIZONTAL;
			crosstab.getModelHandle().setStringProperty(MEASURE_DIRECTION_PROP, newDirection);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	private void swapViewProperties() throws SemanticException {
		String rowGrandTotalLocation = null, colGrandTotalLocation = null;
		LevelHandle rowMirrorStartLevel = null, colMirrorStartLevel = null;
		List rowMembers = null, colMembers = null;

		CrosstabViewHandle rowView = crosstab.getCrosstabView(ROW_AXIS_TYPE);
		if (rowView != null) {
			rowMembers = rowView.getMembers();
			rowMirrorStartLevel = rowView.getMirroredStartingLevel();
			rowGrandTotalLocation = rowView.getGrandTotalLocation();
		}

		CrosstabViewHandle colView = crosstab.getCrosstabView(COLUMN_AXIS_TYPE);
		if (colView != null) {
			colMembers = colView.getMembers();
			colMirrorStartLevel = colView.getMirroredStartingLevel();
			colGrandTotalLocation = colView.getGrandTotalLocation();
		}

		if (rowView != null) {
			transferViewProperties(rowView, colGrandTotalLocation, colMirrorStartLevel, colMembers);
		}

		if (colView != null) {
			transferViewProperties(colView, rowGrandTotalLocation, rowMirrorStartLevel, rowMembers);
		}
	}

	private void transferViewProperties(CrosstabViewHandle view, String grandTotalLocation,
			LevelHandle mirrorStartLevel, List members) throws SemanticException {
		view.setGrandTotalLocation(grandTotalLocation);
		view.setMirroredStartingLevel(mirrorStartLevel);

		view.getModelHandle().getPropertyHandle(ICrosstabViewConstants.MEMBERS_PROP).clearValue();

		if (members != null) {
			for (Object mv : members) {
				view.addMember((MemberValueHandle) ((MemberValueHandle) mv).copy()
						.getHandle(view.getModuleHandle().getModule()));
			}
		}

	}

	private void swapAggregateOn(AggregationCellHandle aggCell) throws SemanticException {
		if (aggCell == null) {
			return;
		}

		LevelHandle rowLevel = aggCell.getAggregationOnRow();
		LevelHandle colLevel = aggCell.getAggregationOnColumn();

		aggCell.setAggregationOnColumn(rowLevel);
		aggCell.setAggregationOnRow(colLevel);
	}

	private int transferDimensions(int srcAxis, int offset) throws SemanticException {
		int transfered = 0;

		int dimCount = crosstab.getDimensionCount(srcAxis) - offset;

		if (dimCount > 0) {
			int targetAxis = CrosstabModelUtil.getOppositeAxisType(srcAxis);

			CrosstabViewHandle targetCrosstabView = crosstab.getCrosstabView(targetAxis);
			if (targetCrosstabView == null) {
				targetCrosstabView = crosstab.addCrosstabView(targetAxis);
			}

			DesignElementHandle targetViewHandle = targetCrosstabView.getModelHandle();

			for (int i = 0; i < dimCount; i++) {
				DimensionViewHandle dv = crosstab.getDimension(srcAxis, offset);

				dv.getModelHandle().moveTo(targetViewHandle, ICrosstabViewConstants.VIEWS_PROP, i);

				transfered++;
			}
		}

		return transfered;
	}

	private CrosstabCellHandle transferGrandTotal(int srcAxis, CrosstabCellHandle oldGT, boolean firstMove)
			throws SemanticException {
		CrosstabCellHandle srcGT = null;

		if (firstMove) {
			CrosstabViewHandle srcView = crosstab.getCrosstabView(srcAxis);
			srcGT = srcView != null ? srcView.getGrandTotal() : null;
		} else {
			srcGT = oldGT;
		}

		CrosstabCellHandle targetReplaced = null;

		int targetAxis = CrosstabModelUtil.getOppositeAxisType(srcAxis);
		CrosstabViewHandle targetView = crosstab.getCrosstabView(targetAxis);

		if (firstMove) {
			targetReplaced = targetView == null ? null : targetView.getGrandTotal();

			if (srcGT != null) {
				// clear grandtotal on source view
				if (srcGT.getModelHandle().getContainer() != null) {
					CrosstabCellHandle srcClone = (CrosstabCellHandle) CrosstabUtil.getReportItem(
							srcGT.getModelHandle().copy().getHandle(crosstab.getModuleHandle().getModule()));

					srcGT.getModelHandle().drop();

					srcGT = srcClone;
				}
			}

			if (targetReplaced != null) {
				CrosstabCellHandle targetClone = (CrosstabCellHandle) CrosstabUtil.getReportItem(
						targetReplaced.getModelHandle().copy().getHandle(crosstab.getModuleHandle().getModule()));

				targetReplaced.getModelHandle().drop();

				targetReplaced = targetClone;
			}
		}

		if (srcGT != null) {
			// add grandtotal to target view
			if (targetView == null) {
				targetView = crosstab.addCrosstabView(targetAxis);
			}

			PropertyHandle targetPropertyHandle = targetView.getGrandTotalProperty();

			if (targetPropertyHandle.getContentCount() <= 0) {
				targetPropertyHandle.add(srcGT.getModelHandle());
			}

		}

		return targetReplaced;
	}

	/**
	 * Moves the dimension view with the given name to the target index in the
	 * target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param name           name of the dimension view to move
	 * @param targetAxisType row/column axis type of the move target
	 * @param targetIndex    the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension(String name, int targetAxisType, int targetIndex) throws SemanticException {
		DimensionViewHandle dimensionView = crosstab.getDimension(name);
		if (dimensionView == null) {
			crosstab.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name);
			throw new CrosstabException(crosstab.getModelHandle().getElement(),
					Messages.getString(MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name));
		}
		moveDimension(dimensionView, targetAxisType, targetIndex);
	}

	/**
	 * Moves the dimension view with the given name to the target index in the
	 * target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param extendedItem   the dimension view extended item to move
	 * @param targetAxisType row/column axis type of the move target
	 * @param targetIndex    the position index of the move target
	 * @throws SemanticException
	 */
	private void moveDimension(DimensionViewHandle dimensionView, int targetAxisType, int targetIndex)
			throws SemanticException {
		assert dimensionView != null;

		// record existing subtotal aggregation info from source dimension
		Map<String, List<String>> functionListMap = new HashMap<String, List<String>>();
		Map<String, List<MeasureViewHandle>> measureListMap = new HashMap<String, List<MeasureViewHandle>>();
		for (int i = 0; i < dimensionView.getLevelCount(); i++) {
			LevelViewHandle levelView = dimensionView.getLevel(i);
			String name = levelView.getCubeLevelName();
			if (name == null)
				continue;

			List measureList = levelView.getAggregationMeasures();
			List<String> functionList = new ArrayList<String>();
			for (int j = 0; j < measureList.size(); j++) {
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get(j);
				String function = levelView.getAggregationFunction(measureView);
				if (function == null) {
					functionList.add(CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView));
				} else {
					functionList.add(function);
				}
			}
			functionListMap.put(name, functionList);
			measureListMap.put(name, measureList);
		}

		// record existing grandtotal aggregation info on target view, we need
		// to keep the grandtotal, but when remove dimension on source view, it
		// could be removed
		List<MeasureViewHandle> grandMeasureList = getAggregationMeasures(targetAxisType);
		List<String> grandFunctionList = new ArrayList<String>();
		for (int j = 0; j < grandMeasureList.size(); j++) {
			MeasureViewHandle measureView = grandMeasureList.get(j);
			String function = getAggregationFunction(targetAxisType, measureView);
			if (function == null) {
				grandFunctionList.add(CrosstabModelUtil.getDefaultMeasureAggregationFunction(measureView));
			} else {
				grandFunctionList.add(function);
			}
		}

		// have a copy for source dimension
		DimensionViewHandle clonedDimensionView = (DimensionViewHandle) CrosstabUtil.getReportItem(
				dimensionView.getModelHandle().copy().getHandle(dimensionView.getModelHandle().getModule()));

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.pivot.dimension")); //$NON-NLS-1$

		try {
			CrosstabViewHandle srcCrosstabView = (CrosstabViewHandle) dimensionView.getContainer();
			new CrosstabViewTask(srcCrosstabView).removeDimension(dimensionView, false);

			// if target crosstab view is null, add it first
			CrosstabViewHandle targetCrosstabView = crosstab.getCrosstabView(targetAxisType);
			if (targetCrosstabView == null) {
				targetCrosstabView = crosstab.addCrosstabView(targetAxisType);
			}

			List<MeasureViewHandle> transferMeasureList = new ArrayList<MeasureViewHandle>();
			List<String> transferFunctionList = new ArrayList<String>();

			// check if target view is empty and no grandtotal defined, then
			// remove dummy grandtotal from original view
			if (targetCrosstabView.getDimensionCount() == 0 && targetCrosstabView.getGrandTotal() == null) {
				// remove dummy grandtotal cells on remained subtotal from
				// source view
				for (int i = 0; i < srcCrosstabView.getDimensionCount(); i++) {
					DimensionViewHandle dv = srcCrosstabView.getDimension(i);

					for (int j = 0; j < dv.getLevelCount(); j++) {
						LevelViewHandle lv = dv.getLevel(j);

						if (lv.getAggregationHeader() != null) {
							for (int k = 0; k < crosstab.getMeasureCount(); k++) {
								MeasureViewHandle mv = crosstab.getMeasure(k);

								String rowDimension = null;
								String rowLevel = null;
								String colDimension = dv.getCubeDimensionName();
								String colLevel = lv.getCubeLevelName();

								if (srcCrosstabView.getAxisType() == ROW_AXIS_TYPE) {
									rowDimension = colDimension;
									rowLevel = colLevel;
									colDimension = null;
									colLevel = null;
								}

								AggregationCellHandle aggCell = mv.getAggregationCell(rowDimension, rowLevel,
										colDimension, colLevel);

								if (aggCell != null) {
									aggCell.getModelHandle().drop();
								}
							}
						}
					}
				}

				// transfer dummy grandtotal cells on source grandtotal to
				// target view
				if (srcCrosstabView.getGrandTotal() != null) {
					for (int i = 0; i < crosstab.getMeasureCount(); i++) {
						MeasureViewHandle mv = crosstab.getMeasure(i);

						AggregationCellHandle aggCell = mv.getAggregationCell(null, null, null, null);

						if (aggCell != null) {
							String function = getAggregationFunction(srcCrosstabView.getAxisType(), mv);

							aggCell.getModelHandle().drop();

							// record the grandtotal cell need to be transfered
							transferMeasureList.add(mv);
							transferFunctionList.add(function);
						}
					}
				}
			}

			new CrosstabViewTask(targetCrosstabView).addDimension(clonedDimensionView, targetIndex, false);

			// transfer pervious recorded grandtotal for target view
			if (transferMeasureList.size() > 0 && clonedDimensionView.getLevelCount() > 0) {
				addMeasureAggregations(clonedDimensionView.getLevel(clonedDimensionView.getLevelCount() - 1),
						transferMeasureList, transferFunctionList, false);
			}

			// add all the level aggregations
			for (int i = 0; i < clonedDimensionView.getLevelCount(); i++) {
				LevelViewHandle levelView = clonedDimensionView.getLevel(i);
				String levelName = levelView.getCubeLevelName();
				if (levelName == null) {
					continue;
				}

				if (levelView.isInnerMost()) {
					// remove aggregatio header on new innermost level if
					// existed
					if (levelView.getAggregationHeaderProperty().getContentCount() > 0) {
						levelView.getAggregationHeaderProperty().drop(0);
					}
				} else {
					// try restore original subtotal
					List<MeasureViewHandle> measureList = measureListMap.get(levelName);
					List<String> functionList = functionListMap.get(levelName);
					new LevelViewTask(levelView).addSubTotal(measureList, functionList, false);
				}
			}

			// restore all grandtotal aggregations on target view
			if (grandMeasureList.size() > 0) {
				addMeasureAggregations(targetAxisType, grandMeasureList, grandFunctionList, false);
			}

			validateCrosstab();
			int pos = CrosstabModelUtil.findPriorLevelCount(clonedDimensionView);
			for (int i = 0; i < clonedDimensionView.getLevelCount(); i++) {
				CrosstabModelUtil.updateHeaderCell(clonedDimensionView.getCrosstab(), pos + i,
						clonedDimensionView.getAxisType(), true, clonedDimensionView.getLevelCount() - i - 1);
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}

		stack.commit();
	}

	/**
	 * Moves the dimension view in the source position of source row/column to the
	 * target index in the target row/column. The axis type can be either
	 * <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param srcAxisType    the source row/column axis type
	 * @param srcIndex       the source position index
	 * @param targetAxisType row/column axis type of the move target
	 * @param targetIndex    the position index of the move target
	 * @throws SemanticException
	 */
	public void pivotDimension(int srcAxisType, int srcIndex, int targetAxisType, int targetIndex)
			throws SemanticException {
		DimensionViewHandle dimensionView = crosstab.getDimension(srcAxisType, srcIndex);
		if (dimensionView == null) {
			crosstab.getLogger().log(Level.INFO, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND,
					String.valueOf(srcAxisType));
			return;
		}

		moveDimension(dimensionView, targetAxisType, targetIndex);
	}

	/**
	 * Inserts a row/column dimension into the given position. The axis type can be
	 * either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. The index is 0-based
	 * integer.
	 * 
	 * @param dimensionHandle the OLAP dimension handle to use
	 * @param axisType        row/column axis type
	 * @param index           insert position, a 0-based integer
	 * @return
	 * @throws SemanticException
	 */
	public DimensionViewHandle insertDimension(DimensionHandle dimensionHandle, int axisType, int index)
			throws SemanticException {
		// if this dimension handle has referred by an existing dimension view,
		// then log error and do nothing
		if (dimensionHandle != null && crosstab.getDimension(dimensionHandle.getQualifiedName()) != null) {
			crosstab.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION,
					dimensionHandle.getQualifiedName());
			throw new CrosstabException(crosstab.getModelHandle().getElement(), Messages.getString(
					MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_DIMENSION, dimensionHandle.getQualifiedName()));
		}

		DimensionViewHandle dimensionView = null;

		CommandStack stack = crosstab.getCommandStack();
		stack.startTrans(Messages.getString("CrosstabReportItemTask.msg.insert.dimension")); //$NON-NLS-1$

		try {
			CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axisType);

			if (crosstabView == null) {
				// if the crosstab view is null, then create and add a crosstab
				// view
				// first, and then add the dimension to it second;
				crosstabView = crosstab.addCrosstabView(axisType);
			}

			// add the dimension to crosstab view directly
			dimensionView = crosstabView.insertDimension(dimensionHandle, index);
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();

		return dimensionView;
	}

	/**
	 * Removes a dimension view that refers a cube dimension name with the given
	 * name from the design tree.
	 * 
	 * @param name name of the dimension view to remove
	 * @throws SemanticException
	 */
	public void removeDimension(String name) throws SemanticException {
		DimensionViewHandle dimensionView = crosstab.getDimension(name);
		if (dimensionView == null) {
			crosstab.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name);
			throw new CrosstabException(crosstab.getModelHandle().getElement(),
					Messages.getString(MessageConstants.CROSSTAB_EXCEPTION_DIMENSION_NOT_FOUND, name));
		}

		removeDimension(dimensionView.getAxisType(), dimensionView.getIndex());
	}

	/**
	 * Removes a row/column dimension view in the given position. The axis type can
	 * be either <code>ICrosstabConstants.ROW_AXIS_TYPE</code> or
	 * <code>ICrosstabConstants.COLUMN_AXIS_TYPE</code>. And index is 0-based
	 * integer.
	 * 
	 * @param axisType row/column axis type
	 * @param index    the position index of the dimension to remove, 0-based
	 *                 integer
	 * @throws SemanticException
	 */
	public void removeDimension(int axisType, int index) throws SemanticException {
		CrosstabViewHandle crosstabView = crosstab.getCrosstabView(axisType);
		if (crosstabView != null) {
			crosstabView.removeDimension(index);
		}
	}
}
