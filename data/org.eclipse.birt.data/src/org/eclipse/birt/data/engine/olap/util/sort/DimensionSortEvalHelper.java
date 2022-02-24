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

package org.eclipse.birt.data.engine.olap.util.sort;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.core.script.ScriptExpression;
import org.eclipse.birt.data.engine.api.IBaseQueryResults;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.olap.api.query.ICubeQueryDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ICubeSortDefinition;
import org.eclipse.birt.data.engine.olap.api.query.ILevelDefinition;
import org.eclipse.birt.data.engine.olap.data.api.DimLevel;
import org.eclipse.birt.data.engine.olap.data.impl.aggregation.filter.AggregationRowAccessor;
import org.eclipse.birt.data.engine.olap.util.DataJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper;
import org.eclipse.birt.data.engine.olap.util.IJSObjectPopulator;
import org.eclipse.birt.data.engine.olap.util.OlapExpressionUtil;
import org.eclipse.birt.data.engine.olap.util.filter.IResultRow;
import org.eclipse.birt.data.engine.script.ScriptEvalUtil;
import org.mozilla.javascript.Scriptable;

/**
 * 
 */

public class DimensionSortEvalHelper extends DimensionJSEvalHelper implements IJSSortHelper {

	protected ICubeSortDefinition sortDefinition;
	private DimLevel targetLevel;
	private Map<String, Object> axisDimValueMap;

	public DimensionSortEvalHelper(IBaseQueryResults outResults, Scriptable parentScope, ICubeQueryDefinition queryDefn,
			ICubeSortDefinition sortDefinition, ScriptContext cx) throws DataException {
		assert sortDefinition != null;
		initialize(outResults, parentScope, queryDefn, sortDefinition, cx);
	}

	/**
	 * 
	 * @param parentScope
	 * @param queryDefn
	 * @param sortDefinition
	 * @param cx
	 * @throws DataException
	 */
	protected void initialize(IBaseQueryResults outResults, Scriptable parentScope, ICubeQueryDefinition queryDefn,
			ICubeSortDefinition sortDefinition, ScriptContext cx) throws DataException {
		super.init(outResults, parentScope, queryDefn, cx, sortDefinition.getExpression());
		this.sortDefinition = sortDefinition;
		this.axisDimValueMap = new HashMap<String, Object>();
		for (int i = 0; i < this.sortDefinition.getAxisQualifierLevels().length; i++) {
			ILevelDefinition lvl = this.sortDefinition.getAxisQualifierLevels()[i];
			String lvlName = OlapExpressionUtil.getAttrReference(lvl.getHierarchy().getDimension().getName(),
					lvl.getName(), lvl.getName());
			this.axisDimValueMap.put(lvlName, this.sortDefinition.getAxisQualifierValues()[i]);
		}
	}

	/**
	 * 
	 */
	public Object evaluate(IResultRow resultRow) throws DataException {
		super.setData(resultRow);
		if (resultRow instanceof AggregationRowAccessor) {
			((AggregationRowAccessor) resultRow).setCurrentAxisValue(this.axisDimValueMap);
		}
		try {
			return ScriptEvalUtil.evalExpr(expr, cx.newContext(scope), ScriptExpression.defaultID, 0);
		} catch (IJSObjectPopulator.InMatchDimensionIndicator e) {
			return null;
		} catch (BirtException e) {
			throw DataException.wrap(e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.util.sort.IJSSortHelper#getTargetLevel()
	 */
	public DimLevel getTargetLevel() {
		if (targetLevel == null) {
			targetLevel = new DimLevel(this.sortDefinition.getTargetLevel());
		}
		return targetLevel;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.data.engine.olap.data.impl.aggregation.sort.ITargetSort#
	 * getSortDirection()
	 */
	public int getSortDirection() {
		return sortDefinition.getSortDirection();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.data.engine.olap.util.DimensionJSEvalHelper#
	 * registerJSObjectPopulators()
	 */
	@Override
	protected void registerJSObjectPopulators() throws DataException {
		super.registerJSObjectPopulators();
		// support data expressions that reference to dimension expressions
		// without aggregation levels
		register(new DataJSObjectPopulator(this.outResults, scope, queryDefn.getBindings(), false, cx));
	}
}
