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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.DataEngineContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IGroupInstanceInfo;
import org.eclipse.birt.data.engine.api.IInputParameterBinding;
import org.eclipse.birt.data.engine.api.IQueryDefinition;
import org.eclipse.birt.data.engine.api.IQueryExecutionHints;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.ISortDefinition;
import org.eclipse.birt.data.engine.api.ISubqueryDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.impl.document.ExprUtil;
import org.eclipse.birt.data.engine.impl.document.FilterDefnUtil;
import org.eclipse.birt.data.engine.impl.document.GroupDefnUtil;

/**
 * 
 * @author Administrator
 *
 */
public class QueryCompUtil {
	/**
	 * 
	 * @param qd1
	 * @param qd2
	 * @return
	 * @throws DataException
	 */
	public static boolean isIVQueryDefnEqual(int mode, IBaseQueryDefinition originalQuery,
			IBaseQueryDefinition newQuery) throws DataException {
		if (originalQuery == newQuery)
			return true;
		if (originalQuery == null || newQuery == null)
			return false;
		if (!(originalQuery instanceof IQueryDefinition && newQuery instanceof IQueryDefinition))
			return false;
		if (((IQueryDefinition) newQuery).getDataSetName() == null) {
			return false;
		}
		return isQueryDefnEqual(mode, originalQuery, newQuery, true, true);
	}

	/**
	 * 
	 * @param qd1
	 * @param qd2
	 * @param onIVMode
	 * @return
	 * @throws DataException
	 */
	public static boolean isQueryDefnEqual(int mode, IBaseQueryDefinition qd1, IBaseQueryDefinition qd2,
			boolean onIVMode, boolean isSubQueryNameCared) throws DataException {
		if (qd1 == qd2)
			return true;
		if (qd1 == null || qd2 == null)
			return false;

		if (!isEqualSorts(qd1.getSorts(), qd2.getSorts()))
			return false;
		if (!isEqualFilters(qd1.getFilters(), qd2.getFilters()))
			return false;
		if (!isEqualBindings(qd1.getBindings(), qd2.getBindings()))
			return false;
		if (!isEqualGroups(qd1.getGroups(), qd2.getGroups(), onIVMode))
			return false;
		if (qd1.usesDetails() != qd2.usesDetails())
			return false;
		// max row with 0 is same with negative number
		if (qd1.getMaxRows() >= 0 && qd2.getMaxRows() >= 0 && qd1.getMaxRows() != qd2.getMaxRows())
			return false;
		if ((qd1.getMaxRows() > 0 && qd2.getMaxRows() < 0) || (qd1.getMaxRows() < 0 && qd2.getMaxRows() > 0))
			return false;

		// We should never need to compare there parent query for that will not have
		// impact
		// on the query results.
		/*
		 * if ( !isQueryDefnEqual( qd1.getParentQuery( ), qd2.getParentQuery( ),
		 * onIVMode ) ) return false;
		 */
		if (mode == DataEngineContext.MODE_PRESENTATION && qd1.cacheQueryResults() != qd2.cacheQueryResults())
			return false;
		if (!isSubQueryEquals(qd1.getSubqueries(), qd2.getSubqueries(), onIVMode))
			return false;

		// For IV use, some properties of IQueryDefinition is not significant.

		if (qd1 instanceof IQueryDefinition && qd2 instanceof IQueryDefinition) {
			IQueryDefinition queryDefn1 = (IQueryDefinition) qd1;
			IQueryDefinition queryDefn2 = (IQueryDefinition) qd2;
			if (queryDefn1.isSummaryQuery() != queryDefn2.isSummaryQuery()) {
				return false;
			}
			if (!onIVMode) {

				if (!isEqualString(queryDefn1.getDataSetName(), queryDefn2.getDataSetName()))
					return false;
				if (!isInputParameterBindingEquals(queryDefn1.getInputParamBindings(),
						queryDefn2.getInputParamBindings()))
					return false;
				if (!isColumnProjectionEquals(queryDefn1.getColumnProjection(), queryDefn2.getColumnProjection()))
					return false;
			}

			IQueryExecutionHints hint1 = queryDefn1.getQueryExecutionHints();
			IQueryExecutionHints hint2 = queryDefn2.getQueryExecutionHints();

			if (!compareHints(hint1, hint2))
				return false;

		}

		if (qd1 instanceof ISubqueryDefinition && qd2 instanceof ISubqueryDefinition) {
			ISubqueryDefinition subQueryDefn1 = (ISubqueryDefinition) qd1;
			ISubqueryDefinition subQueryDefn2 = (ISubqueryDefinition) qd2;
			if (isSubQueryNameCared && !isEqualString(subQueryDefn1.getName(), subQueryDefn2.getName()))
				return false;
			if (subQueryDefn1.applyOnGroup() != subQueryDefn2.applyOnGroup())
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param hint1
	 * @param hint2
	 * @return
	 */
	private static boolean compareHints(IQueryExecutionHints hint1, IQueryExecutionHints hint2) {
		if (hint1 == hint2)
			return true;
		if (hint1 == null || hint2 == null)
			return false;

		if (hint1.doSortBeforeGrouping() != hint2.doSortBeforeGrouping())
			return false;
		if (hint1.getTargetGroupInstances().size() != hint2.getTargetGroupInstances().size())
			return false;
		for (int i = 0; i < hint1.getTargetGroupInstances().size(); i++) {
			IGroupInstanceInfo info1 = hint1.getTargetGroupInstances().get(i);
			IGroupInstanceInfo info2 = hint2.getTargetGroupInstances().get(i);
			if (info1.getGroupLevel() != info2.getGroupLevel())
				return false;
			if (info1.getRowId() != info2.getRowId())
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param columnProjection1
	 * @param columnProjection2
	 * @return
	 */
	private static boolean isColumnProjectionEquals(String[] columnProjection1, String[] columnProjection2) {
		if (columnProjection1 == columnProjection2)
			return true;

		if (columnProjection1 == null || columnProjection2 == null)
			return false;

		if (columnProjection1.length != columnProjection2.length)
			return false;

		for (int i = 0; i < columnProjection1.length; i++) {
			if (!isEqualString(columnProjection1[i], columnProjection2[i]))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param inputParamBindings1
	 * @param inputParamBindings2
	 * @return
	 */
	private static boolean isInputParameterBindingEquals(Collection inputParamBindings1,
			Collection inputParamBindings2) {
		if (inputParamBindings1 == inputParamBindings2)
			return true;

		if (inputParamBindings1 == null || inputParamBindings2 == null)
			return false;

		if (inputParamBindings1.size() != inputParamBindings2.size())
			return false;

		Iterator it1 = inputParamBindings1.iterator();
		Iterator it2 = inputParamBindings2.iterator();
		while (it1.hasNext()) {
			IInputParameterBinding binding1 = (IInputParameterBinding) it1.next();
			IInputParameterBinding binding2 = (IInputParameterBinding) it2.next();

			if (!isTwoExpressionEqual(binding1.getExpr(), binding2.getExpr(), false))
				return false;
			if (!isEqualString(binding1.getName(), binding2.getName()))
				return false;
			if (binding1.getPosition() != binding2.getPosition())
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param group1
	 * @param group2
	 * @param onIVMode
	 * @return
	 * @throws DataException
	 */
	static boolean isEqualGroups(List group1, List group2, boolean onIVMode) throws DataException {
		if (group1 == group2)
			return true;
		if (group1 == null || group2 == null)
			return false;
		if (group1.size() != group2.size())
			return false;
		for (int i = 0; i < group1.size(); i++) {
			IGroupDefinition groupDefn1 = (IGroupDefinition) group1.get(i);
			IGroupDefinition groupDefn2 = (IGroupDefinition) group2.get(i);
			if (!isEqualString(groupDefn1.getName(), groupDefn2.getName()))
				return false;
			if (!isEqualFilters(groupDefn1.getFilters(), groupDefn2.getFilters()))
				return false;
			if (groupDefn1.getSortDirection() != groupDefn2.getSortDirection())
				return false;
			if (!isEqualSorts(groupDefn1.getSorts(), groupDefn2.getSorts()))
				return false;
			if (groupDefn1.getInterval() != groupDefn2.getInterval())
				return false;
			if (groupDefn1.getIntervalRange() != groupDefn2.getIntervalRange())
				return false;
			if (!isEqualString(groupDefn1.getIntervalStart(), groupDefn2.getIntervalStart()))
				return false;
			if (!isEqualString(groupDefn1.getKeyColumn(), groupDefn2.getKeyColumn()))
				return false;
			if (!isEqualString(groupDefn1.getKeyExpression(), groupDefn2.getKeyExpression()))
				return false;
			if (groupDefn1.getSortDirection() != groupDefn2.getSortDirection())
				return false;
			if (!isSubQueryEquals(groupDefn1.getSubqueries(), groupDefn2.getSubqueries(), onIVMode))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param subQueries1
	 * @param subQueries2
	 * @param onIVMode
	 * @return
	 * @throws DataException
	 */
	private static boolean isSubQueryEquals(Collection subQueries1, Collection subQueries2, boolean onIVMode)
			throws DataException {
		if (subQueries1 == subQueries2)
			return true;
		if (subQueries1 == null || subQueries2 == null)
			return false;
		if (subQueries1.size() != subQueries2.size())
			return false;
		Iterator it1 = subQueries1.iterator();
		Iterator it2 = subQueries2.iterator();
		while (it1.hasNext()) {
			ISubqueryDefinition sub1 = (ISubqueryDefinition) it1.next();
			ISubqueryDefinition sub2 = (ISubqueryDefinition) it2.next();
			if (!isQueryDefnEqual(-1, sub1, sub2, onIVMode, true))
				return false;
		}
		return true;
	}

	/**
	 * 
	 * @param rs1
	 * @param rs2
	 * @return
	 * @throws DataException
	 */
	static boolean isEqualBindings(Map rs1, Map rs2) throws DataException {
		if (rs1 == rs2)
			return true;

		if (rs1 == null || rs2 == null)
			return false;

		if (rs1.size() != rs2.size())
			return false;

		Iterator it = rs1.keySet().iterator();
		while (it.hasNext()) {
			Object key = it.next();
			Object oldObj = rs1.get(key);
			Object newObj = rs2.get(key);
			if (oldObj != null && newObj != null) {
				if (!QueryCompUtil.isTwoBindingEqual((IBinding) newObj, (IBinding) oldObj))
					return false;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * 
	 * @param b1
	 * @param b2
	 * @return
	 * @throws DataException
	 */
	public static boolean isTwoBindingEqual(IBinding b1, IBinding b2) throws DataException {
		if (b1.getDataType() != b2.getDataType())
			return false;

		if (!isEqualString(b1.getAggrFunction(), b2.getAggrFunction()))
			return false;

		if (b1.getAggrFunction() != null && b2.getAggrFunction() != null) {
			if (b1.getAggregatOns().size() != b2.getAggregatOns().size())
				return false;
			for (int i = 0; i < b1.getAggregatOns().size(); i++) {
				if (!isEqualString(b1.getAggregatOns().get(i).toString(), b2.getAggregatOns().get(i).toString()))
					return false;
			}

			List b1Arguments = new ArrayList(b1.getArguments());
			if (b1.getExpression() != null) {
				b1Arguments.add(0, b1.getExpression());
			}

			List b2Arguments = new ArrayList(b2.getArguments());
			if (b2.getExpression() != null) {
				b2Arguments.add(0, b2.getExpression());
			}

			if (b1Arguments.size() != b2Arguments.size())
				return false;
			for (int i = 0; i < b1Arguments.size(); i++) {
				if (!isTwoExpressionEqual((IBaseExpression) b1Arguments.get(i), (IBaseExpression) b2Arguments.get(i),
						true))
					return false;
			}

		} else {
			if (!isTwoExpressionEqual(b1.getExpression(), b2.getExpression(), true))
				return false;
		}

		if (!isTwoExpressionEqual(b1.getFilter(), b2.getFilter(), true))
			return false;

		return true;
	}

	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public static boolean isTwoExpressionEqual(IBaseExpression obj1, IBaseExpression obj2, boolean ignoreDataType) {
		if (obj1 == null && obj2 != null)
			return false;
		if (obj1 != null && obj2 == null)
			return false;
		if (obj1 == null && obj2 == null)
			return true;

		if (obj1 instanceof IScriptExpression) {
			return isTwoExpressionEqual((IScriptExpression) obj1, (IScriptExpression) obj2, ignoreDataType);
		} else if (obj1 instanceof IConditionalExpression) {
			return isTwoExpressionEqual((IConditionalExpression) obj1, (IConditionalExpression) obj2, ignoreDataType);
		}
		return false;
	}

	/**
	 * Return whether two IScriptExpression instance equals.
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static boolean isTwoExpressionEqual(IScriptExpression obj1, IScriptExpression obj2,
			boolean ignoreDataType) {
		if (obj1 == null && obj2 != null)
			return false;
		if (obj1 != null && obj2 == null)
			return false;
		if (obj1 == null && obj2 == null)
			return true;
		if (ignoreDataType) {
			return isEqualString(obj1.getText(), obj2.getText())
					&& isEqualString(obj1.getGroupName(), obj2.getGroupName())
					&& isEqualString(obj1.getText(), obj2.getText());

		} else {
			return isEqualString(obj1.getText(), obj2.getText())
					&& isEqualString(obj1.getGroupName(), obj2.getGroupName())
					&& isEqualString(obj1.getText(), obj2.getText()) && ((obj1.getDataType() == obj2.getDataType())
							|| isUnknowOrAny(obj1, obj2) || isUnknowOrAny(obj2, obj1));
		}
	}

	private static boolean isUnknowOrAny(IScriptExpression obj1, IScriptExpression obj2) {
		return (obj1.getDataType() == -1 && obj2.getDataType() == 0);
	}

	/**
	 * 
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	private static boolean isTwoExpressionEqual(IConditionalExpression obj1, IConditionalExpression obj2,
			boolean ignoreDataTypes) {
		if (obj1.getOperator() != obj2.getOperator())
			return false;

		return isEqualString(obj1.getGroupName(), obj2.getGroupName())
				&& isTwoExpressionEqual(obj1.getExpression(), obj2.getExpression(), ignoreDataTypes)
				&& isTwoExpressionEqual(obj1.getOperand1(), obj2.getOperand1(), ignoreDataTypes)
				&& isTwoExpressionEqual(obj1.getOperand2(), obj2.getOperand2(), ignoreDataTypes);
	}

	/**
	 * @param oldSubQueryDefns
	 * @param newSubQueryDefns
	 * @return
	 */
	public static boolean isCompatibleSQs(Collection oldSubQueryDefns, Collection newSubQueryDefns) {
		if (oldSubQueryDefns == newSubQueryDefns)
			return true;
		else if (oldSubQueryDefns == null)
			return newSubQueryDefns.size() == 0;
		else if (newSubQueryDefns == null)
			return true;

		if (oldSubQueryDefns.size() < newSubQueryDefns.size())
			return false;

		Iterator oldIt = oldSubQueryDefns.iterator();
		Iterator newIt = newSubQueryDefns.iterator();
		while (newIt.hasNext()) {
			ISubqueryDefinition oldSub = (ISubqueryDefinition) oldIt.next();
			ISubqueryDefinition newSub = (ISubqueryDefinition) newIt.next();

			if (isEqualFilters(oldSub.getFilters(), newSub.getFilters()) == false)
				return false;

			if (isEqualSorts(oldSub.getSorts(), newSub.getSorts()) == false)
				return false;

			if (GroupDefnUtil.isEqualGroups(oldSub.getGroups(), newSub.getGroups()) == false)
				return false;

			if (isCompatibleExprMap(oldSub.getBindings(), newSub.getBindings()) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param oldFilter
	 * @param newFilter
	 * @return
	 */
	private static boolean isEqualFilters(List oldFilter, List newFilter) {
		if (oldFilter == newFilter)
			return true;

		if (oldFilter.size() != newFilter.size())
			return false;

		Iterator oldIt = oldFilter.iterator();
		Iterator newIt = newFilter.iterator();
		while (oldIt.hasNext()) {
			IFilterDefinition oldDefn = (IFilterDefinition) oldIt.next();
			IFilterDefinition newDefn = (IFilterDefinition) newIt.next();
			if (FilterDefnUtil.isEqualFilter(oldDefn, newDefn) == false)
				return false;
		}

		return true;
	}

	/**
	 * @param oldSorts
	 * @param newSorts
	 * @return
	 */
	public static boolean isEqualSorts(List oldSorts, List newSorts) {
		if (oldSorts == newSorts)
			return true;

		if (oldSorts.size() != newSorts.size())
			return false;

		Iterator oldIt = oldSorts.iterator();
		Iterator newIt = newSorts.iterator();
		while (oldIt.hasNext()) {
			ISortDefinition oldDefn = (ISortDefinition) oldIt.next();
			ISortDefinition newDefn = (ISortDefinition) newIt.next();

			if (isEqualString(oldDefn.getColumn(), newDefn.getColumn()) == false
					|| ExprUtil.isEqualExpression(oldDefn.getExpression(), newDefn.getExpression()) == false
					|| oldDefn.getSortDirection() != newDefn.getSortDirection()
					|| oldDefn.getSortStrength() != newDefn.getSortStrength() || isLocaleDeferent(oldDefn, newDefn))
				return false;
		}

		return true;
	}

	private static boolean isLocaleDeferent(ISortDefinition oldDefn, ISortDefinition newDefn) {
		if (oldDefn.getSortLocale() == newDefn.getSortLocale())
			return false;

		if (oldDefn.getSortLocale() == null || newDefn.getSortLocale() == null)
			return true;

		return !oldDefn.getSortLocale().equals(newDefn.getSortLocale());
	}

	/**
	 * @param oldExprMap
	 * @param newExprMap
	 * @return
	 */
	private static boolean isCompatibleExprMap(Map oldExprMap, Map newExprMap) {
		if (oldExprMap == newExprMap)
			return true;
		else if (oldExprMap == null)
			return newExprMap.size() == 0;
		else if (newExprMap == null)
			return oldExprMap.size() == 0;

		return oldExprMap.size() >= newExprMap.size();
	}

	/**
	 * @param ob1
	 * @param ob2
	 * @return
	 */
	public static boolean isEqualString(Object ob1, Object ob2) {
		if (ob1 == ob2)
			return true;
		else if (ob1 == null || ob2 == null)
			return false;

		return ob1.equals(ob2);
	}

}
