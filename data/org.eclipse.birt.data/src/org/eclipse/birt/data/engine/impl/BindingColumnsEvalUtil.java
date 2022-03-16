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

package org.eclipse.birt.data.engine.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Logger;

import org.eclipse.birt.core.data.DataTypeUtil;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.JavascriptEvalUtil;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.api.IBaseExpression;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.expression.ExprEvaluateUtil;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.impl.ResultIterator.RDSaveHelper;
import org.eclipse.birt.data.engine.impl.document.viewing.ExprMetaUtil;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * Evaluate a row of bound columns and meantime do something related with saving
 * the value of bound columns.
 *
 * There is different behavior when query is running on dataset or report
 * document. In the latter case, original binding column name is reserved and
 * only the binding column can be added, not allowed for delete or change. So it
 * is reasonable to assume that if the binding name is the same as one of
 * original binding columns, the binding expression is also the same as that of
 * the original one.
 */
public class BindingColumnsEvalUtil {
	//
	private IResultIterator odiResult;
	private Scriptable scope;
	private RDSaveHelper saveHelper;

	private Map<String, BindingColumn> allManualBindingExprs;
	private List allAutoBindingExprs;

	private ScriptContext cx;
	public final static int MANUAL_BINDING = 1;
	public final static int AUTO_BINDING = 2;

	private static Logger logger = Logger.getLogger(BindingColumnsEvalUtil.class.getName());

	/**
	 * @param ri
	 * @param scope
	 * @param saveUtil
	 * @param serviceForResultSet
	 * @throws DataException
	 */
	BindingColumnsEvalUtil(IResultIterator ri, Scriptable scope, ScriptContext cx, RDSaveHelper saveUtil,
			List manualBindingExprs, Map autoBindingExprs) throws DataException {
		Object[] params = { ri, scope, saveUtil, manualBindingExprs, autoBindingExprs };
		logger.entering(BindingColumnsEvalUtil.class.getName(), "BindingColumnsEvalUtil", params);

		this.odiResult = ri;
		this.scope = scope;
		this.saveHelper = saveUtil;
		this.cx = cx;

		this.initBindingColumns(manualBindingExprs, autoBindingExprs);
		logger.exiting(BindingColumnsEvalUtil.class.getName(), "BindingColumnsEvalUtil");
	}

	/**
	 * @param serviceForResultSet
	 * @throws DataException
	 */
	private void initBindingColumns(List manualBindingExprs, Map autoBindingExprs) throws DataException {
		// put the expressions of array into a list
		int size = manualBindingExprs.size();
		GroupBindingColumn[] groupBindingColumns = new GroupBindingColumn[size];
		Iterator itr = manualBindingExprs.iterator();
		while (itr.hasNext()) {
			GroupBindingColumn temp = (GroupBindingColumn) itr.next();
			groupBindingColumns[temp.getGroupLevel()] = temp;
		}

		allManualBindingExprs = new HashMap<>();
		for (int i = 0; i < size; i++) {
			List groupBindingExprs = new ArrayList();
			itr = groupBindingColumns[i].getColumnNames().iterator();
			while (itr.hasNext()) {
				String exprName = (String) itr.next();
				IBaseExpression baseExpr = groupBindingColumns[i].getExpression(exprName);
				allManualBindingExprs.put(exprName,
						new BindingColumn(exprName, baseExpr,
								groupBindingColumns[i].getBinding(exprName).getAggrFunction() != null,
								groupBindingColumns[i].getBinding(exprName).getDataType()));
			}
		}

		// put the auto binding expressions into a list
		allAutoBindingExprs = new ArrayList();
		itr = autoBindingExprs.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry entry = (Entry) itr.next();
			String exprName = (String) entry.getKey();
			IBaseExpression baseExpr = (IBaseExpression) entry.getValue();

			allAutoBindingExprs.add(new BindingColumn(exprName, baseExpr, false, baseExpr.getDataType()));
		}
	}

	/**
	 * @return
	 * @throws DataException save error
	 */
	void getColumnsValue(Map valueMap, boolean includeAggregation) throws DataException {
		Iterator itr = this.allAutoBindingExprs.iterator();
		while (itr.hasNext()) {
			BindingColumn bindingColumn = (BindingColumn) itr.next();
			if (valueMap.containsKey(bindingColumn.columnName)) {
				continue;
			}
			Object exprValue = evaluateValue(bindingColumn, AUTO_BINDING);
			if (valueMap.get(bindingColumn.columnName) == null) {
				valueMap.put(bindingColumn.columnName, exprValue);
			}
		}

		for (BindingColumn bindingColumn : allManualBindingExprs.values()) {
			if (valueMap.containsKey(bindingColumn.columnName) || (bindingColumn.isAggregation && !includeAggregation)) {
				continue;
			}
			Object exprValue = evaluateValue(bindingColumn, MANUAL_BINDING);

			valueMap.put(bindingColumn.columnName, exprValue);
		}

		if (ExprMetaUtil.isBasedOnRD(this.odiResult.getResultClass()) && !saveHelper.isSummaryQuery()) {
			if (this.odiResult.getCurrentResult() != null) {
				valueMap.put(ExprMetaUtil.POS_NAME,
						this.odiResult.getCurrentResult().getFieldValue(ExprMetaUtil.POS_NAME));
			} else {
				// For dummy query case.
				valueMap.put(ExprMetaUtil.POS_NAME, 0);
			}
		}
		saveHelper.doSaveExpr(valueMap);
	}

	/**
	 * @param baseExpr
	 * @param exprType
	 * @param valueMap
	 * @throws DataException
	 */
	private Object evaluateValue(BindingColumn bindingColumn, int exprType) throws DataException {
		Object exprValue = null;
		try {
			if (exprType == MANUAL_BINDING) {
				if (bindingColumn.isAggregation) {
					exprValue = this.odiResult.getAggrValue(bindingColumn.columnName);
				} else {
					exprValue = ExprEvaluateUtil.evaluateExpression(bindingColumn.baseExpr, odiResult, scope, cx);
				}
			} else {
				exprValue = ExprEvaluateUtil.evaluateRawExpression(bindingColumn.baseExpr, scope, cx);
			}

			if (exprValue != null && !(exprValue instanceof Exception)) {
				exprValue = DataTypeUtil.convert(JavascriptEvalUtil.convertJavascriptValue(exprValue),
						bindingColumn.type);
			}
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
		return exprValue;
	}

	/**
	 * Evaluate the specified binding column in MANUAL_BINDING mode.
	 *
	 * @param baseExpr
	 * @param exprType
	 * @param valueMap
	 * @throws DataException
	 */
	Object evaluateValue(String bindingName) throws DataException {
		BindingColumn binding = this.getBindingFromManualBinding(bindingName);
		if (binding == null) {
			throw new DataException(ResourceConstants.INVALID_BOUND_COLUMN_NAME, bindingName);
		}

		return this.evaluateValue(binding, MANUAL_BINDING);
	}

	/**
	 * Get BindingColumn object with specified name.
	 *
	 * @param name
	 * @return
	 * @throws DataException there is no BindingColumn in manualBindingExprs
	 */
	private BindingColumn getBindingFromManualBinding(String name) throws DataException {
		return this.allManualBindingExprs.get(name);
	}

	boolean isValidBindingName(String name) throws DataException {
		return this.getBindingFromManualBinding(name) != null;
	}

}
