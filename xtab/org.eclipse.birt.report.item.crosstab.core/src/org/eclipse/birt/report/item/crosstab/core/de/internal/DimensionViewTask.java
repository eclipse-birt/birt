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

package org.eclipse.birt.report.item.crosstab.core.de.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.eclipse.birt.report.data.adapter.api.IDimensionLevel;
import org.eclipse.birt.report.item.crosstab.core.CrosstabException;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabConstants;
import org.eclipse.birt.report.item.crosstab.core.ILevelViewConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabReportItemHandle;
import org.eclipse.birt.report.item.crosstab.core.de.DimensionViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.LevelViewHandle;
import org.eclipse.birt.report.item.crosstab.core.de.MeasureViewHandle;
import org.eclipse.birt.report.item.crosstab.core.i18n.MessageConstants;
import org.eclipse.birt.report.item.crosstab.core.i18n.Messages;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ElementFactory;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.MemberValueHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.olap.CubeHandle;
import org.eclipse.birt.report.model.api.olap.LevelHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IFilterConditionElementModel;
import org.eclipse.birt.report.model.elements.interfaces.ISortElementModel;

/**
 * DimensionViewTask
 */
public class DimensionViewTask extends AbstractCrosstabModelTask {

	protected DimensionViewHandle dimensionView = null;
	protected int axisType;

	/**
	 * 
	 * @param focus
	 */
	public DimensionViewTask(DimensionViewHandle focus) {
		super(focus);
		this.dimensionView = focus;
		this.axisType = dimensionView.getAxisType();
	}

	/**
	 * Inserts a level handle into a dimension view. This method will add the
	 * aggregations and data-item automatically.
	 * 
	 * @param dimensionView
	 * @param levelHandle
	 * @param index
	 * @return
	 * @throws SemanticException
	 */
	public LevelViewHandle insertLevel(LevelHandle levelHandle, int index) throws SemanticException {
		if (levelHandle != null) {
			// if cube dimension container of this cube level element is not
			// what is referred by this dimension view, then the insertion is
			// forbidden
			if (!levelHandle.getContainer().getContainer().getQualifiedName()
					.equals(dimensionView.getCubeDimensionName())) {
				// TODO: throw exception
				dimensionView.getLogger().log(Level.WARNING, ""); //$NON-NLS-1$
				return null;
			}

			// if this level handle has referred by an existing level view,
			// then log error and do nothing
			if (dimensionView.getLevel(levelHandle.getQualifiedName()) != null) {
				dimensionView.getLogger().log(Level.SEVERE, MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL,
						levelHandle.getQualifiedName());
				throw new CrosstabException(dimensionView.getModelHandle().getElement(), Messages.getString(
						MessageConstants.CROSSTAB_EXCEPTION_DUPLICATE_LEVEL, levelHandle.getQualifiedName()));
			}
		}

		CommandStack stack = dimensionView.getCommandStack();
		stack.startTrans(Messages.getString("DimensionViewTask.msg.insert.level")); //$NON-NLS-1$

		LevelViewHandle levelView = null;

		try {
			ExtendedItemHandle extendedItemHandle = CrosstabExtendedItemFactory
					.createLevelView(dimensionView.getModuleHandle(), levelHandle);
			if (extendedItemHandle != null) {
				dimensionView.getLevelsProperty().add(extendedItemHandle, index);

				levelView = (LevelViewHandle) CrosstabUtil.getReportItem(extendedItemHandle, LEVEL_VIEW_EXTENSION_NAME);

				// if level handle is specified, do some post work after adding
				if (levelHandle != null && crosstab != null) {
					doPostInsert(levelView);

					CrosstabModelUtil.updateHeaderCell(dimensionView.getCrosstab(),
							CrosstabModelUtil.findPriorLevelCount(dimensionView) + index, dimensionView.getAxisType());
				}
			}
		} catch (SemanticException e) {
			stack.rollback();
			throw e;
		}
		stack.commit();

		return levelView;
	}

	private void doPostInsert(LevelViewHandle levelView) throws SemanticException {
		if (levelView.isInnerMost()) {
			// if originally there is no levels and grand total,
			// then remove the aggregations for the axis type and
			// the counter axis level aggregations
			if (CrosstabModelUtil.getAllLevelCount(crosstab, axisType) <= 1) {
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel(levelView, axisType);

				if (crosstab.getGrandTotal(axisType) == null) {
					removeMeasureAggregations(axisType);
				}
			} else {
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel(levelView, axisType);

				// add one aggregation: the original innermost level
				// before this level is added and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = CrosstabModelUtil.getPrecedingLevel(levelView);
				int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
				assert precedingLevel != null;
				LevelViewHandle innerMostLevelView = CrosstabModelUtil.getInnerMostLevel(crosstab, counterAxisType);
				if (precedingLevel.getAggregationHeader() != null) {
					if (innerMostLevelView != null) {
						String dimensionName = ((DimensionViewHandle) innerMostLevelView.getContainer())
								.getCubeDimensionName();

						String levelName = innerMostLevelView.getCubeLevelName();
						List measureList = precedingLevel.getAggregationMeasures();
						List<String> functionList = new ArrayList<String>();
						for (int i = 0; i < measureList.size(); i++) {
							MeasureViewHandle measureView = (MeasureViewHandle) measureList.get(i);
							String function = precedingLevel.getAggregationFunction(measureView);
							functionList.add(function);
						}

						// add the data-item
						addMeasureAggregations(crosstab, dimensionName, levelName, counterAxisType,
								((DimensionViewHandle) precedingLevel.getContainer()).getCubeDimensionName(),
								precedingLevel.getCubeLevelName(), measureList, functionList);
					} else {
						// TODO add dummy grandtotal???
					}
				} else {
					// orginally, the preceding one is the innermost, we add
					// some aggregations for this innermost, even though it has
					// no sub-total; however, now, it is not innermost and
					// neither has sub-total, therefore, we should remove
					// aggregations about this
					removeMeasureAggregations(precedingLevel);
				}
			}

			// valid aggregation on measure detail cell
			LevelViewHandle innerestRowLevel = CrosstabModelUtil.getInnerMostLevel(crosstab, ROW_AXIS_TYPE);
			LevelViewHandle innerestColLevel = CrosstabModelUtil.getInnerMostLevel(crosstab, COLUMN_AXIS_TYPE);

			validateMeasureDetails(innerestRowLevel, innerestColLevel);
		} else {
			// if the added level view is not innermost and has
			// aggregation header, then add aggregations for this
			// level view and all counterpart axis levels and grand
			// total
			if (levelView.getAggregationHeader() != null) {
				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				addAggregationForLevel(levelView, axisType);

				// handle measure header
				addTotalMeasureHeader(axisType, levelView);
			}
		}
	}

	/**
	 * Adjust measure aggregations for the given two level views.
	 * 
	 * @param crosstab       the crosstab where the leve views reside
	 * @param leftDimension  the first dimension name
	 * @param leftLevel      the first level name
	 * @param axisType       the row/column axis type for the first level view
	 * @param rightDimension the second dimension name
	 * @param rightLevel     the second level name
	 * @param measures
	 * @param functions
	 * @param isAdd          true if add aggregation, otherwise false
	 * @throws SemanticException
	 */
	private void addMeasureAggregations(CrosstabReportItemHandle crosstab, String leftDimension, String leftLevel,
			int axisType, String rightDimension, String rightLevel, List<MeasureViewHandle> measures,
			List<String> functions) throws SemanticException {
		if (crosstab == null || !CrosstabModelUtil.isValidAxisType(axisType) || measures == null)
			return;
		if (functions == null || functions.size() != measures.size())
			return;

		String rowDimension = null;
		String rowLevel = null;
		String colDimension = null;
		String colLevel = null;

		if (axisType == ROW_AXIS_TYPE) {
			rowDimension = leftDimension;
			rowLevel = leftLevel;

			colDimension = rightDimension;
			colLevel = rightLevel;
		} else if (axisType == COLUMN_AXIS_TYPE) {
			rowDimension = rightDimension;
			rowLevel = rightLevel;

			colDimension = leftDimension;
			colLevel = leftLevel;
		}
		for (int i = 0; i < measures.size(); i++) {
			MeasureViewHandle measureView = measures.get(i);
			if (measureView.getCrosstab() != crosstab)
				continue;

			validateSingleMeasureAggregation(crosstab, measureView, functions.get(i), rowDimension, rowLevel,
					colDimension, colLevel);
		}
	}

	/**
	 * 
	 * @param levelView
	 * @param axisType
	 * @throws SemanticException
	 */
	private void addAggregationForLevel(LevelViewHandle levelView, int axisType) throws SemanticException {
		assert CrosstabModelUtil.isValidAxisType(axisType);
		if (levelView != null && levelView.getAxisType() != axisType)
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);

		// first add all aggregation for the added level view, for it is
		// innermost
		for (int dimension = 0; dimension < crosstab.getDimensionCount(counterAxisType); dimension++) {
			DimensionViewHandle tempDimensionView = crosstab.getDimension(counterAxisType, dimension);
			for (int level = 0; level < tempDimensionView.getLevelCount(); level++) {
				LevelViewHandle tempLevelView = tempDimensionView.getLevel(level);

				// if level view is not null, that is not grand-total
				if (levelView != null) {
					boolean isInnerMost = levelView.isInnerMost();
					if (isInnerMost && tempLevelView.isInnerMost())
						continue;
				}
				// if this level has no sub-total, do nothing
				if (tempLevelView.getAggregationHeader() == null)
					continue;
				List measureList = tempLevelView.getAggregationMeasures();
				AggregationInfo infor = getAggregationInfo(levelView, tempLevelView);
				for (int i = 0; i < measureList.size(); i++) {
					MeasureViewHandle measureView = (MeasureViewHandle) measureList.get(i);
					String function = tempLevelView.getAggregationFunction(measureView);

					validateSingleMeasureAggregation(crosstab, measureView, function, infor.getRowDimension(),
							infor.getRowLevel(), infor.getColDimension(), infor.getColLevel());
				}
			}
		}

		// handle for grand-total
		if (crosstab.getGrandTotal(counterAxisType) != null
				|| CrosstabModelUtil.getAllLevelCount(crosstab, counterAxisType) == 0) {
			List measureList = crosstab.getAggregationMeasures(counterAxisType);
			AggregationInfo infor = getAggregationInfo(levelView, null);
			for (int i = 0; i < measureList.size(); i++) {
				MeasureViewHandle measureView = (MeasureViewHandle) measureList.get(i);
				String function = crosstab.getAggregationFunction(counterAxisType, measureView);

				validateSingleMeasureAggregation(crosstab, measureView, function, infor.getRowDimension(),
						infor.getRowLevel(), infor.getColDimension(), infor.getColLevel());
			}
		}

	}

	/**
	 * Removes a level view that refers a cube level element with the given name.
	 * 
	 * @param name name of the cube level element to remove
	 * @throws SemanticException
	 */
	public void removeLevel(String name) throws SemanticException {
		LevelViewHandle levelView = dimensionView.getLevel(name);
		if (levelView != null) {
			removeLevel(levelView, true);
		}
	}

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	void removeLevel(LevelViewHandle levelView, boolean needTransaction) throws SemanticException {
		assert levelView != null;

		CommandStack stack = null;

		if (needTransaction) {
			stack = dimensionView.getCommandStack();
			stack.startTrans(Messages.getString("DimensionViewTask.msg.remove.level")); //$NON-NLS-1$
		}

		try {
			// adjust measure aggregations and then remove level view from
			// the design tree, the order can not reversed
			if (crosstab != null) {
				doPreRemove(levelView);
			}
			int count = CrosstabModelUtil.findPriorLevelCount(dimensionView);
//			for (int i=0; i<dimensionView.getLevelCount( ); i++)
//			{
//				if (dimensionView.getLevel( i ) == levelView)
//				{
//					break;
//				}
//				count = count + 1;
//			}
			count = count + levelView.getIndex();
			levelView.getModelHandle().drop();
			CrosstabModelUtil.updateHeaderCell(dimensionView.getCrosstab(), count, dimensionView.getAxisType(), true);
			CrosstabModelUtil.updateRPTMeasureAggregation(dimensionView.getCrosstab());
			if (crosstab != null) {
				validateFilterCondition();
				validateSort();
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

	/**
	 * Removes a level view at the given position. The position index is 0-based
	 * integer.
	 * 
	 * @param index the position index of the level view to remove
	 * @throws SemanticException
	 */
	public void removeLevel(int index) throws SemanticException {
		LevelViewHandle levelView = dimensionView.getLevel(index);
		if (levelView != null) {
			removeLevel(levelView, true);
		}
	}

	/**
	 * 
	 * @param levelView
	 * @throws SemanticException
	 */
	private void doPreRemove(LevelViewHandle levelView) throws SemanticException {
		if (crosstab == null)
			return;

		int axisType = dimensionView.getAxisType();

		if (levelView.isInnerMost()) {
			if (CrosstabModelUtil.getAllLevelCount(crosstab, axisType) <= 1) {
				// no level exists when this level is removed: if no
				// grand-total, then we should add aggregations for the empty
				// axis
				if (crosstab.getGrandTotal(axisType) == null) {
					addAggregationForLevel(null, axisType);
				}

				// remove aggregations related with the level view
				removeMeasureAggregations(levelView);

				// valid aggregation on measure detail cell
				int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
				LevelViewHandle innerestCounterAxisLevel = CrosstabModelUtil.getInnerMostLevel(crosstab,
						counterAxisType);

				if (axisType == ICrosstabConstants.ROW_AXIS_TYPE) {
					validateMeasureDetails(null, innerestCounterAxisLevel);
				} else {
					validateMeasureDetails(innerestCounterAxisLevel, null);
				}
			} else {
				// remove one aggregation: the original second innermost level
				// before this level is removed and the innermost
				// level in the counter axis if the orginal
				// innermost has aggregation header
				LevelViewHandle precedingLevel = CrosstabModelUtil.getPrecedingLevel(levelView);
				int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);

				assert precedingLevel != null;
				if (precedingLevel.getAggregationHeader() != null) {
					LevelViewHandle innerMostLevelView = CrosstabModelUtil.getInnerMostLevel(crosstab, counterAxisType);
					if (innerMostLevelView != null) {
						removeTotalMeasureHeader(axisType, precedingLevel);

						// remove aggregation with innermost level on counter
						// axis, since this will become the detail cell
						removeMeasureAggregation(precedingLevel, innerMostLevelView);

						// remove the aggregation header cell since now it
						// becomes innermost
						precedingLevel.getAggregationHeaderProperty().drop(0);

						// we still need to check missing aggreagtions for
						// the precedingLevel with count-axis levels, in case
						// the subtotal in not for all measures. If the
						// subtotal is for all measures, then this call is
						// supposed to do nothing.
						addAggregationForLevel(precedingLevel, axisType);
					} else {
						// no levels on counter axis, we should remove subtotal
						// for the new innermost level
						precedingLevel.removeSubTotal();
					}
				} else {
					// orginally, the preceding one is the second innermost and
					// now becomes the innermost, so we should add aggregations
					// even if it has no sub-total
					addAggregationForLevel(precedingLevel, axisType);
				}

				// add aggregations for this level and all counter
				// axis type levels except the innermost one
				removeMeasureAggregations(levelView);

				// valid aggregation on measure detail cell
				LevelViewHandle innerestCounterAxisLevel = CrosstabModelUtil.getInnerMostLevel(crosstab,
						counterAxisType);

				if (axisType == ICrosstabConstants.ROW_AXIS_TYPE) {
					validateMeasureDetails(precedingLevel, innerestCounterAxisLevel);
				} else {
					validateMeasureDetails(innerestCounterAxisLevel, precedingLevel);
				}
			}
		} else {
			// if the added level view is not innermost and has
			// aggregation header, then remove aggregations for this
			// level view and all counterpart axis levels and grand
			// total
			if (levelView.getAggregationHeader() != null) {
				removeTotalMeasureHeader(axisType, levelView);

				removeMeasureAggregations(levelView);
			}
		}

		// validate mirror starting level setting
		LevelHandle mirrorLevel = crosstab.getCrosstabView(axisType).getMirroredStartingLevel();

		if (mirrorLevel != null && levelView.getCubeLevel() == mirrorLevel) {
			crosstab.getCrosstabView(axisType).setMirroredStartingLevel(null);
		}
	}

	/**
	 * Validates the filter condition(member value list) for all the levels in the
	 * counter axis type of this dimension.
	 */
	private void validateFilterCondition() throws SemanticException {
		validateProperty(ILevelViewConstants.FILTER_PROP, IFilterConditionElementModel.EXPR_PROP,
				IFilterConditionElementModel.MEMBER_PROP);
	}

	/**
	 * Validates the sort(member value list) for all the levels in the counter axis
	 * type of this dimension.
	 */
	private void validateSort() throws SemanticException {
		validateProperty(ILevelViewConstants.SORT_PROP, ISortElementModel.KEY_PROP, ISortElementModel.MEMBER_PROP);
	}

	/**
	 * 
	 * @param propName
	 */
	private void validateProperty(String propName, String exprePropName, String memberValuePropName)
			throws SemanticException {
		assert crosstab != null;
		CubeHandle cube = crosstab.getCube();
		if (cube == null)
			return;

		int counterAxisType = CrosstabModelUtil.getOppositeAxisType(axisType);
		for (int dimension = 0; dimension < crosstab.getDimensionCount(counterAxisType); dimension++) {
			DimensionViewHandle dimensionView = crosstab.getDimension(counterAxisType, dimension);
			for (int level = 0; level < dimensionView.getLevelCount(); level++) {
				LevelViewHandle levelView = dimensionView.getLevel(level);
				DesignElementHandle levelHandle = levelView.getModelHandle();
				int count = levelHandle.getContentCount(propName);
				for (int i = 0; i < count; i++) {
					DesignElementHandle item = levelHandle.getContent(propName, i);
					String expression = item.getStringProperty(exprePropName);
					validateMemberValue(cube, levelView, item, expression, memberValuePropName);
				}

			}
		}
	}

	private void validateMemberValue(CubeHandle cube, LevelViewHandle levelView, DesignElementHandle item,
			String expression, String memberValuePropName) throws SemanticException {
		assert crosstab != null;
		assert cube != null;

		// expression is empty or null, then do nothing
		if ((!(cube instanceof TabularCubeHandle)) || expression == null || expression.length() == 0) {
			return;
		}

		List<IDimensionLevel> validatedLevelList = CrosstabUtil.getReferencedLevels(levelView, expression);

		MemberValueHandle oldMemberValue = (MemberValueHandle) item.getContent(memberValuePropName, 0);

		// if member value is not null originally, then build the
		// levelName/levelValue pair from it
		Map<String, Object> levelValueMap = new HashMap<String, Object>();
		MemberValueHandle tempMember = oldMemberValue;
		while (tempMember != null) {
			String levelName = tempMember.getCubeLevelName();
			String levelValue = tempMember.getValue();
			levelValueMap.put(levelName, levelValue);
			tempMember = (MemberValueHandle) tempMember.getContent(MemberValueHandle.MEMBER_VALUES_PROP, 0);
		}

		ElementFactory factory = dimensionView.getModuleHandle().getElementFactory();
		MemberValueHandle newMemberValue = factory.newMemberValue();
		tempMember = newMemberValue;
		MemberValueHandle parentMember = null;
		if (validatedLevelList != null) {
			for (int i = 0; i < validatedLevelList.size(); i++) {
				IDimensionLevel levelDefn = validatedLevelList.get(i);
				String levelName = getLevelHandle(levelDefn);
				tempMember.setStringProperty(MemberValueHandle.LEVEL_PROP, levelName);
				Object levelValue = levelValueMap.get(levelName);
				if (levelValue == null)
					continue;
				tempMember.setProperty(MemberValueHandle.VALUE_PROP, levelValue);
				if (parentMember != null) {
					parentMember.add(MemberValueHandle.MEMBER_VALUES_PROP, tempMember, 0);
				}

				parentMember = tempMember;
				tempMember = factory.newMemberValue();
			}
		}

		// clear the old member value and add the new one
		item.clearProperty(memberValuePropName);
		if (!isEmptyMemberValue(newMemberValue))
			item.add(memberValuePropName, newMemberValue);
	}

	private boolean isEmptyMemberValue(MemberValueHandle memberValue) {
		assert memberValue != null;

		String levelName = memberValue.getStringProperty(MemberValueHandle.LEVEL_PROP);
		if (levelName != null && levelName.length() > 0)
			return false;
		MemberValueHandle tempMember = (MemberValueHandle) memberValue.getContent(MemberValueHandle.MEMBER_VALUES_PROP,
				0);
		if (tempMember != null && !isEmptyMemberValue(tempMember))
			return false;
		return true;

	}

	private String getLevelHandle(IDimensionLevel levelDefn) {
		assert crosstab != null;

		String levelName = levelDefn.getLevelName();
		String dimensionName = levelDefn.getDimensionName();

		DimensionViewHandle dimension = crosstab.getDimension(dimensionName);
		assert dimension != null;
		LevelViewHandle level = dimension.findLevel(levelName);
		return level.getCubeLevelName();
	}
}
