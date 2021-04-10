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

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.core.data.DataType;
import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.IGroupDefinition;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.BaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ColumnReferenceExpression;
import org.eclipse.birt.data.engine.expression.CompiledExpression;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.odi.IQuery;

/**
 * 
 */
public final class QueryExecutorUtil {

	/**
	 * NO instance
	 */
	private QueryExecutorUtil() {
	}

	/**
	 * Convert IGroupDefn to IQuery.GroupSpec
	 * 
	 * @param cx
	 * @param src
	 * @return
	 * @throws DataException
	 */
	static IQuery.GroupSpec groupDefnToSpec(ScriptContext cx, IGroupDefinition src, String expr, String columnName,
			int index, int dataType, boolean doSortBeforeGrouping) throws DataException {
		ColumnInfo groupKeyInfo = new ColumnInfo(index, columnName);
		int groupIndex = groupKeyInfo.getColumnIndex();
		String groupKey = groupKeyInfo.getColumnName();
		boolean isComplexExpression = true;

		IQuery.GroupSpec dest = new IQuery.GroupSpec(groupIndex, groupKey);
		dest.setName(src.getName());
		dest.setInterval(src.getInterval());
		dest.setIntervalRange(src.getIntervalRange());
		dest.setIntervalStart(src.getIntervalStart());
		dest.setSortDirection(doSortBeforeGrouping ? src.getSortDirection() : IGroupDefinition.NO_SORT);
		dest.setDataType(dataType);
		dest.setFilters(src.getFilters());
		if (src.getSorts().size() != 0) {
			dest.setSorts(src.getSorts());
		}
		dest.setIsComplexExpression(isComplexExpression);
		return dest;
	}

	/**
	 * @param groupSpecs
	 * @param i
	 */
	static int getTempComputedColumnType(int i) {
		int interval = i;
		if (interval == IGroupDefinition.DAY_INTERVAL || interval == IGroupDefinition.HOUR_INTERVAL
				|| interval == IGroupDefinition.MINUTE_INTERVAL || interval == IGroupDefinition.SECOND_INTERVAL
				|| interval == IGroupDefinition.MONTH_INTERVAL || interval == IGroupDefinition.QUARTER_INTERVAL
				|| interval == IGroupDefinition.YEAR_INTERVAL || interval == IGroupDefinition.WEEK_INTERVAL
				|| interval == IGroupDefinition.NUMERIC_INTERVAL)
			interval = DataType.DOUBLE_TYPE;
		else if (interval == IGroupDefinition.STRING_PREFIX_INTERVAL)
			interval = DataType.STRING_TYPE;
		else
			interval = DataType.ANY_TYPE;
		return interval;
	}

	/**
	 * Common code to extract the name of a column from a JS expression which is in
	 * the form of "row.col". If expression is not in expected format, returns null
	 * 
	 * @param cx
	 * @param expr
	 * @return
	 */
	public static ColumnInfo getColInfoFromJSExpr(ScriptContext cx, String expr) {
		int colIndex = -1;
		String colName = null;
		CompiledExpression ce = ExpressionCompilerUtil.compile(expr, cx);
		if (ce instanceof ColumnReferenceExpression) {
			ColumnReferenceExpression cre = ((ColumnReferenceExpression) ce);
			colIndex = cre.getColumnindex();
			colName = cre.getColumnName();
		}
		return new ColumnInfo(colIndex, colName);
	}

	/**
	 * 
	 * @param filter
	 * @return
	 * @throws DataException
	 */
	static boolean isAggrFilter(IFilterDefinition filter, Map<String, IBinding> bindings) throws DataException {
		assert filter != null;
		return isAggrExpr(filter.getExpression(), bindings);

	}

	/**
	 * Detect whether an expression contains, or refer to aggregation.
	 * 
	 * @param expr
	 * @param bindings
	 * @return
	 * @throws DataException
	 */
	public static boolean isAggrExpr(IBaseExpression expr, Map<String, IBinding> bindings) throws DataException {
		try {
			Set<String> nameSet = getBindingNamesFromExpr(expr);
			Iterator<String> it = nameSet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				IBinding binding = bindings.get(key);
				if (binding == null)
					continue;

				Map<String, IBinding> newBindingMap = new HashMap<String, IBinding>(bindings);
				// Remove the current binding from binding list in order not to
				// leads to infinite loop.
				newBindingMap.remove(key);
				if (binding.getAggrFunction() != null || isAggrExpr(binding.getExpression(), newBindingMap)) {
					return true;
				}
			}
			return false;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	public static boolean isValidFilterExpression(IBaseExpression expr, Map<String, IBinding> bindings,
			ScriptContext context) throws DataException {
		try {
			if (!ExpressionCompilerUtil.isValidExpressionInQueryFilter(expr, context))
				return false;

			Set<String> nameSet = getBindingNamesFromExpr(expr);
			Iterator<String> it = nameSet.iterator();
			while (it.hasNext()) {
				String key = it.next();
				IBinding binding = bindings.get(key);
				if (binding == null)
					continue;

				if (binding.getAggrFunction() == null
						&& !ExpressionCompilerUtil.isValidExpressionInQueryFilter(binding.getExpression(), context))
					return false;

				Map<String, IBinding> newBindingMap = new HashMap<String, IBinding>(bindings);
				newBindingMap.remove(key);
				if (!isValidFilterExpression(binding.getExpression(), bindings, context))
					return false;
			}
			return true;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/**
	 * 
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	private static Set<String> getBindingNamesFromExpr(IBaseExpression expr) throws DataException {
		if (expr instanceof IConditionalExpression)
			return getBindingNamesFromConditionalExpr((IConditionalExpression) expr);
		else if (expr instanceof IScriptExpression)
			return getBindingNamesFromScriptExpr((IScriptExpression) expr);
		else
			return new HashSet<String>();
	}

	/**
	 * 
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	private static Set<String> getBindingNamesFromConditionalExpr(IConditionalExpression expr) throws DataException {
		Set<String> nameFromExpr = getBindingNamesFromScriptExpr(expr.getExpression());
		Set<String> nameFromOp1 = getBindingNamesFromExpr(expr.getOperand1());
		Set<String> nameFromOp2 = getBindingNamesFromExpr(expr.getOperand2());
		Set<String> result = new HashSet<String>();
		result.addAll(nameFromExpr);
		result.addAll(nameFromOp1);
		result.addAll(nameFromOp2);
		return result;
	}

	/**
	 * 
	 * @param expr
	 * @return
	 * @throws DataException
	 */
	private static Set<String> getBindingNamesFromScriptExpr(IScriptExpression expr) throws DataException {
		if (BaseExpression.constantId.equals(expr.getScriptId()))
			return Collections.EMPTY_SET;
		try {
			List<IColumnBinding> referedList = ExpressionUtil.extractColumnExpressions(expr.getText());
			Set<String> newList = new HashSet<String>();
			for (int j = 0; j < referedList.size(); j++) {
				IColumnBinding binding = referedList.get(j);
				String name = binding.getResultSetColumnName();
				newList.add(name);
			}

			return newList;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}
}
