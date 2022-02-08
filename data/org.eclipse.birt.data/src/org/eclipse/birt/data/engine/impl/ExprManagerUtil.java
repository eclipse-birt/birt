/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryDefinition;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.IComputedColumn;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExpressionCompilerUtil;
import org.eclipse.birt.data.engine.expression.NamedExpression;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.script.ScriptConstants;

/**
 * This is a utility class which is used to validate the colum bindings defined
 * in ExprManager instance.
 */
public class ExprManagerUtil {
	private ExprManager exprManager;

	private static Logger logger = Logger.getLogger(ExprManagerUtil.class.getName());
	private ScriptContext cx;

	/**
	 * No external instance
	 */
	private ExprManagerUtil(ExprManager em, ScriptContext cx) {
		logger.entering(ExprManagerUtil.class.getName(), "ExprManagerUtil", em);
		this.exprManager = em;
		this.cx = cx;
		logger.exiting(ExprManagerUtil.class.getName(), "ExprManagerUtil");
	}

	/**
	 * This method tests whether column bindings in ExprManager is valid or not.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	public static void validateColumnBinding(ExprManager exprManager, IBaseQueryDefinition baseQueryDefn,
			ScriptContext cx) throws DataException {
		ExprManagerUtil util = new ExprManagerUtil(exprManager, cx);

		util.checkColumnBindingExpression(baseQueryDefn);
		util.checkDependencyCycle();
		util.checkGroupNameValidation();
	}

	/**
	 * Test whether high level group keys are depended on low level group keys.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private void checkGroupNameValidation() throws DataException {
		HashMap map = this.getGroupKeys();
		Iterator it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			Integer level = (Integer) entry.getKey();
			exprManager.setEntryGroupLevel(level.intValue());

			if (!ExpressionCompilerUtil.hasColumnRow(entry.getValue().toString(), exprManager, cx)) {
				exprManager.setEntryGroupLevel(ExprManager.OVERALL_GROUP);
				try {
					throw new DataException(ResourceConstants.INVALID_GROUP_KEY_COLUMN,
							new Object[] { ExpressionUtil.getColumnBindingName(entry.getValue().toString()), level });
				} catch (BirtException e) {
					throw DataException.wrap(e);
				}
			}
		}
		exprManager.setEntryGroupLevel(ExprManager.OVERALL_GROUP);
	}

	/**
	 * 
	 * @param columnName
	 * @return
	 * @throws DataException
	 */
	private boolean isColumnBindingExist(String columnName) throws DataException {
		List bindings = exprManager.getBindingExprs();

		for (int i = 0; i < bindings.size(); i++) {
			GroupBindingColumn gbc = (GroupBindingColumn) bindings.get(i);
			if (gbc.getExpression(columnName) != null)
				return true;
		}
		return false;
	}

	/**
	 * Test whether there are dependency cycles in exprManager.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private void checkDependencyCycle() throws DataException {
		Iterator it = this.getColumnNames().iterator();

		Set<NamedExpression> namedExpressions = new HashSet<NamedExpression>();
		while (it.hasNext()) {
			String name = it.next().toString();
			IBaseExpression expr = exprManager.getExpr(name);
			namedExpressions.add(new NamedExpression(name, expr));
		}
		String nameInvolvedInCycle = ExpressionCompilerUtil.getFirstFoundNameInCycle(namedExpressions,
				ExpressionUtil.ROW_INDICATOR);
		if (nameInvolvedInCycle != null) {
			throw new DataException(ResourceConstants.COLUMN_BINDING_CYCLE, nameInvolvedInCycle);
		}
	}

	/**
	 * Check whether the expression of all the column bindings is valid.
	 * 
	 * @param exprManager
	 * @return
	 * @throws DataException
	 */
	private void checkColumnBindingExpression(IBaseQueryDefinition baseQueryDefn) throws DataException {
		List list = this.getColumnNames();
		for (int i = 0; i < list.size(); i++) {
			String name = list.get(i).toString();
			IBaseExpression expr = exprManager.getExpr(name);
			if (expr != null) {
				if (!(expr instanceof IScriptExpression || expr instanceof IConditionalExpression)) {
					throw new DataException(ResourceConstants.BAD_DATA_EXPRESSION);
				}

				List l = null;
				try {
					l = ExpressionCompilerUtil.extractColumnExpression(expr, ExpressionUtil.ROW_INDICATOR);
				} catch (DataException e) {
					// Do nothing.The mal-formatted expression should not
					// prevent
					// other correct expression from being evaluated and
					// displayed.
				}

				if (l != null) {
					for (int j = 0; j < l.size(); j++) {
						checkColumnBindingExist(name, l.get(j).toString(), list, baseQueryDefn);
					}
				}
				List usedBindings = null;

				if (expr instanceof IScriptExpression) {
					try {
						usedBindings = ExpressionUtil.extractColumnExpressions(((IScriptExpression) expr).getText(),
								ExpressionUtil.ROW_INDICATOR);
					} catch (BirtException e) {
						continue;
					}
					validateReferredColumnBinding(name, usedBindings, baseQueryDefn);
				}
			}
		}
	}

	/**
	 * Test whether all the column bindings exist.
	 * 
	 * @param bindingName
	 * @param binding
	 * @throws DataException
	 */
	private void checkColumnBindingExist(String bindingName, String referName, List binding,
			IBaseQueryDefinition baseQueryDefn) throws DataException {
		if (ScriptConstants.ROW_NUM_KEYWORD.equals(referName)
				|| ScriptConstants.OUTER_RESULT_KEYWORD.equals(referName)) {
			return;
		}
		for (int i = 0; i < binding.size(); i++) {
			if (referName.equals(binding.get(i).toString()))
				return;
		}

		this.validateInParentQuery(bindingName, baseQueryDefn, referName);
	}

	/**
	 * 
	 * @param map
	 * @param usedBindings
	 * @throws DataException
	 */
	private void validateReferredColumnBinding(String bindingName, List usedBindings,
			IBaseQueryDefinition baseQueryDefn) throws DataException {
		List nameList = this.getColumnNames();
		for (int i = 0; i < usedBindings.size(); i++) {
			IColumnBinding cb = (IColumnBinding) usedBindings.get(i);
			if (useDefinedKeyWord(cb))
				continue;

			String name = ((IColumnBinding) usedBindings.get(i)).getResultSetColumnName();
			if (!nameList.contains(name) && baseQueryDefn != null) {
				validateInParentQuery(bindingName, baseQueryDefn, name);
			}
		}
	}

	private void validateInParentQuery(String bindingName, IBaseQueryDefinition baseQueryDefn, String name)
			throws DataException {
		if (baseQueryDefn == null)
			throw new DataException(ResourceConstants.COLUMN_BINDING_REFER_TO_INEXIST_BINDING,
					new Object[] { bindingName, name });
		String expr = findExpression(bindingName, name, baseQueryDefn.getParentQuery());
		if (expr == null) {
			throw new DataException(ResourceConstants.COLUMN_BINDING_REFER_TO_INEXIST_BINDING,
					new Object[] { bindingName, name });
		} else if (ExpressionUtil.hasAggregation(expr)) {
			throw new DataException(
					ResourceConstants.COLUMN_BINDING_REFER_TO_AGGREGATION_COLUMN_BINDING_IN_PARENT_QUERY, bindingName);
		}
	}

	/**
	 * 
	 * @param columnBindingName
	 * @param queryDefn
	 * @return
	 * @throws DataException
	 */
	private String findExpression(String bindingName, String referName, IBaseQueryDefinition queryDefn)
			throws DataException {
		if (queryDefn == null) {
			return null;
		}

		if (queryDefn.getBindings().get(referName) == null) {
			return findExpression(bindingName, referName, queryDefn.getParentQuery());
		}

		IBinding binding = (IBinding) queryDefn.getBindings().get(referName);
		if (binding.getAggrFunction() != null)
			throw new DataException(
					ResourceConstants.COLUMN_BINDING_REFER_TO_AGGREGATION_COLUMN_BINDING_IN_PARENT_QUERY, bindingName);
		IBaseExpression expr = binding.getExpression();
		if (expr instanceof IScriptExpression)
			return ((IScriptExpression) expr).getText();
		else
			return null;
	}

	/**
	 * 
	 * @param cb
	 * @return
	 */
	private boolean useDefinedKeyWord(IColumnBinding cb) {
		return cb.getOuterLevel() > 0 || cb.getResultSetColumnName().equals(ScriptConstants.ROW_NUM_KEYWORD)
				|| cb.getResultSetColumnName().equals("_rowPosition");
	}

	/**
	 * 
	 * @return
	 */
	private List getColumnNames() {
		List bindingExprs = exprManager.getBindingExprs();
		Map autoBindingExprMap = exprManager.getAutoBindingExprMap();

		List l = new ArrayList();
		l.addAll(autoBindingExprMap.keySet());
		for (int i = 0; i < bindingExprs.size(); i++) {
			l.addAll(((GroupBindingColumn) bindingExprs.get(i)).getColumnNames());
		}
		return l;
	}

	/**
	 * 
	 * @return
	 */
	private HashMap getGroupKeys() {
		List bindingExprs = exprManager.getBindingExprs();

		HashMap l = new HashMap();
		for (int i = 0; i < bindingExprs.size(); i++) {
			String key = ((GroupBindingColumn) bindingExprs.get(i)).getGroupKey();
			Integer groupLevel = Integer.valueOf(((GroupBindingColumn) bindingExprs.get(i)).getGroupLevel());
			if (key != null)
				l.put(groupLevel, key);
		}
		return l;
	}

	/**
	 * 
	 * @param computedColumn
	 * @param allComputes
	 * @return
	 * @throws DataException
	 */
	public static boolean parseAggregation(IComputedColumn computedColumn, List allComputes) throws DataException {

		IBaseExpression expr = computedColumn.getExpression();

		// check if it's an old style aggregation
		if (expr instanceof IScriptExpression) {
			if (ExpressionUtil.hasAggregation(((IScriptExpression) expr).getText())) {
				return true;
			}
		}

		// check if itself is an aggregation
		if (computedColumn.getAggregateFunction() != null) {
			return true;
		}

		// check if it refers to some aggregation bindings
		List<String> referencedBindings = ExpressionCompilerUtil.extractColumnExpression(computedColumn.getExpression(),
				ExpressionUtil.ROW_INDICATOR);
		for (int i = 0; i < referencedBindings.size(); i++) {
			IComputedColumn b = null;
			for (int j = 0; j < allComputes.size(); j++) {
				IComputedColumn com = (IComputedColumn) allComputes.get(j);
				if (com.getName().equals(referencedBindings.get(i))) {
					b = com;
				}
			}
			if (b != null) {
				boolean isAggr = parseAggregation(b, allComputes);
				if (isAggr) {
					return true;
				}
			}
		}

		// not an aggregation
		return false;
	}

}
