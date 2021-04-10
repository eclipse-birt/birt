/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.data.engine.api.IScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.data.adapter.i18n.ResourceConstants;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.filterExtension.interfaces.IFilterExprDefinition;

/**
 * A report filter
 */
public class FilterAdapter extends FilterDefinition {
	protected IModelAdapter adapter;

	/**
	 * Construct a filter based on a Model filter handle
	 * 
	 * @throws AdapterException
	 */
	public FilterAdapter(IModelAdapter adapter, FilterConditionHandle modelFilter) throws AdapterException {
		super(null);
		adatperBuildInFilter(adapter, modelFilter);
	}

	private void adatperBuildInFilter(IModelAdapter adapter, FilterConditionHandle modelFilter)
			throws AdapterException {
		this.adapter = adapter;
		String filterTarget = modelFilter.getFilterTarget();
		if (DesignChoiceConstants.FILTER_TARGET_DATA_SET.equals(filterTarget)) {
			setFilterTarget(FilterTarget.DATASET);
		} else if (DesignChoiceConstants.FILTER_TARGET_RESULT_SET.equals(filterTarget)) {
			setFilterTarget(FilterTarget.RESULTSET);
		}

		String filterExpr = modelFilter.getExpr();
		if (filterExpr != null) {
			// convert to DtE exprFilter if there is no operator
			String filterOpr = modelFilter.getOperator();
			if (filterOpr == null || filterOpr.length() == 0) {
				// Standalone expression; data type must be boolean
				setExpression(adapter.adaptExpression(
						DataAdapterUtil.getExpression(modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER))));
			} else {
				// Condition filter with operator and operands
				if (!filterOpr.equals(DesignChoiceConstants.FILTER_OPERATOR_IN)
						&& !filterOpr.equals(DesignChoiceConstants.FILTER_OPERATOR_NOT_IN)) {
					String operand1 = modelFilter.getValue1();
//					String operand2 = modelFilter.getValue2( );

					setExpression(adapter.adaptConditionalExpression(
							DataAdapterUtil.getExpression(
									modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER)),
							filterOpr,
							operand1 == null ? null : modelFilter.getValue1ExpressionList().getListValue().get(0),
							DataAdapterUtil
									.getExpression(modelFilter.getExpressionProperty(FilterCondition.VALUE2_MEMBER))));
				} else {
					List<Expression> operands = modelFilter.getValue1ExpressionList().getListValue();
					if (operands == null) {
						throw new AdapterException(ResourceConstants.INVALID_FILTER_OPERANDS);
					}
					List<IScriptExpression> adaptedExpressions = new ArrayList<IScriptExpression>();

					for (Expression expr : operands) {
						adaptedExpressions.add(adapter.adaptExpression(expr));
					}
					setExpression(new ConditionAdapter(
							adapter.adaptExpression(DataAdapterUtil
									.getExpression(modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER))),
							filterOpr, adaptedExpressions));
				}
				this.setUpdateAggregation(modelFilter.updateAggregation());
			}
		}
	}

	/**
	 * Construct a filter based on a Model filter handle and its filter definition
	 * 
	 * @throws AdapterException
	 */
	public FilterAdapter(IModelAdapter adapter, FilterConditionHandle modelFilter, IFilterExprDefinition filterDefn)
			throws AdapterException {
		super(null);
		if (filterDefn == null) {
			adatperBuildInFilter(adapter, modelFilter);
			return;
		}
		this.adapter = adapter;

		String filterTarget = modelFilter.getFilterTarget();
		if (DesignChoiceConstants.FILTER_TARGET_DATA_SET.equals(filterTarget)) {
			setFilterTarget(FilterTarget.DATASET);
		} else if (DesignChoiceConstants.FILTER_TARGET_RESULT_SET.equals(filterTarget)) {
			setFilterTarget(FilterTarget.RESULTSET);
		}

		String filterExpr = modelFilter.getExpr();
		if (filterExpr != null) {
			String filterOpr = null;
			// convert to DtE exprFilter if there is no operator
			if (filterDefn != null) {
				if (filterDefn.getBirtFilterExprId() != null) {
					filterOpr = modelFilter.getOperator();
				} else if (filterDefn.getExtFilterExprId() != null) {
					filterOpr = filterDefn.getExtFilterExprId();
				}
			}
			if (filterOpr == null) {
				filterOpr = modelFilter.getOperator();
			}
			if (filterOpr == null || filterOpr.length() == 0) {
				// Standalone expression; data type must be boolean
				setExpression(adapter.adaptExpression(
						DataAdapterUtil.getExpression(modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER))));
			} else {
				// Condition filter with operator and operands
				if (filterDefn.getMaxArguments() != null && filterDefn.getMaxArguments() <= 2) {
					String operand1 = modelFilter.getValue1();

					setExpression(adapter.adaptConditionalExpression(
							DataAdapterUtil.getExpression(
									modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER)),
							filterOpr,
							operand1 == null ? null : modelFilter.getValue1ExpressionList().getListValue().get(0),
							DataAdapterUtil
									.getExpression(modelFilter.getExpressionProperty(FilterCondition.VALUE2_MEMBER))));
				} else {
					List<Expression> operands = modelFilter.getValue1ExpressionList().getListValue();
					if (operands == null) {
						throw new AdapterException(ResourceConstants.INVALID_FILTER_OPERANDS);
					}
					List<IScriptExpression> adaptedExpressions = new ArrayList<IScriptExpression>();

					for (Expression expr : operands) {
						adaptedExpressions.add(adapter.adaptExpression(expr));
					}
					setExpression(new ConditionAdapter(
							adapter.adaptExpression(DataAdapterUtil
									.getExpression(modelFilter.getExpressionProperty(FilterCondition.EXPR_MEMBER))),
							filterOpr, adaptedExpressions));
				}
				this.setUpdateAggregation(modelFilter.updateAggregation());
			}
		}
	}

	/**
	 * Construct a filter with provided expression text
	 * 
	 * @throws AdapterException
	 */
	public FilterAdapter(IModelAdapter adapter, ExpressionHandle handle) throws AdapterException {
		super(adapter.adaptExpression(DataAdapterUtil.getExpression(handle)));
		this.adapter = adapter;
	}

}
