
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.olap.util.filter;

import java.util.Set;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.api.IConditionalExpression;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.i18n.ResourceConstants;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.util.CompareUtil;
import org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionCompiler;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 *
 */

public class TopBottomDimensionFilterEvalHelper extends BaseDimensionFilterEvalHelper
		implements IJSTopBottomFilterHelper {
	private double N;
	private int filterType;
	private boolean isTop;
	private boolean isPercent;

	/**
	 * @param parentScope
	 * @param queryDefn
	 * @param cubeFilter
	 * @throws DataException
	 */
	public TopBottomDimensionFilterEvalHelper(IBaseQueryResults outResults, Scriptable parentScope,
			ICubeQueryDefinition queryDefn, IFilterDefinition cubeFilter, ScriptContext cx) throws DataException {
		assert cubeFilter != null;
		initialize(outResults, parentScope, queryDefn, cubeFilter, cx);
		populateN(cx);
		popualteFilterType();
		argumentCheck();
	}

	/**
	 *
	 * @throws DataException
	 */
	private void argumentCheck() throws DataException {
		if (isPercent) {
			if (this.N < 0 || this.N > 100) {
				throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_PERCENT_ARGUMENT);
			}
		} else if (this.N < 0) {
			throw new DataException(ResourceConstants.INVALID_TOP_BOTTOM_N_ARGUMENT);
		}

	}

	/**
	 *
	 * @param cx
	 * @throws DataException
	 */
	private void populateN(ScriptContext cx) throws DataException {
		Object o = ScriptEvalUtil.evalExpr(((IConditionalExpression) expr).getOperand1(), cx.newContext(scope),
				ScriptExpression.defaultID, 0);
		this.N = Double.parseDouble(o.toString());
	}

	/**
	 *
	 */
	private void popualteFilterType() {
		int type = ((IConditionalExpression) this.expr).getOperator();
		switch (type) {
		case IConditionalExpression.OP_TOP_N:
			this.filterType = IJSTopBottomFilterHelper.TOP_N;
			isTop = true;
			isPercent = false;
			break;
		case IConditionalExpression.OP_TOP_PERCENT:
			this.filterType = IJSTopBottomFilterHelper.TOP_PERCENT;
			isTop = true;
			isPercent = true;
			break;
		case IConditionalExpression.OP_BOTTOM_N:
			this.filterType = IJSTopBottomFilterHelper.BOTTOM_N;
			isTop = false;
			isPercent = false;
			break;
		case IConditionalExpression.OP_BOTTOM_PERCENT:
			this.filterType = IJSTopBottomFilterHelper.BOTTOM_PERCENT;
			isTop = false;
			isPercent = true;
			break;
		default:
			assert false;
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#
	 * evaluateFilterExpr(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	@Override
	public Object evaluateFilterExpr(IResultRow resultRow) throws DataException {
		super.setData(resultRow);

		try {
			Object result = ScriptEvalUtil.evalExpr(((IConditionalExpression) expr).getExpression(),
					cx.newContext(scope), ScriptExpression.defaultID, 0);
			return result;
		} catch (IJSObjectPopulator.InMatchDimensionIndicator e) {
			throw new DataException(e.getMessage());
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	@Override
	public DimLevel getTargetLevel() throws DataException {
		Set set = OlapExpressionCompiler.getReferencedDimLevel(this.expr, queryDefn.getBindings());
		if (set.size() != 1) {
			throw new DataException(ResourceConstants.REFERENCED_DIM_LEVEL_SET_ERROR);
		}
		DimLevel result = (DimLevel) set.iterator().next();
		return result;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#
	 * getFilterType()
	 */
	@Override
	public int getFilterType() {
		return this.filterType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#getN()
	 */
	@Override
	public double getN() {
		return this.N;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#
	 * isQualifiedRow(org.eclipse.birt.data.engine.olap.util.filter.IResultRow)
	 */
	@Override
	public boolean isQualifiedRow(IResultRow resultRow) throws DataException {
		if (this.isAxisFilter) {
			for (int i = 0; i < axisLevels.length; i++) {
				DimLevel level = new DimLevel(axisLevels[i]);
				if (CompareUtil.compare(resultRow.getFieldValue(level.toString()), axisValues[i]) != 0) {
					return false;
				}
			}
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#
	 * isPercentFilter()
	 */
	@Override
	public boolean isPercent() {
		return isPercent;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.data.engine.olap.util.filter.IJSTopBottomFilterHelper#
	 * isTopFilter()
	 */
	@Override
	public boolean isTop() {
		return isTop;
	}
}
