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
package org.eclipse.birt.data.engine.impl.aggregation;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Scriptable;

/**
 * A registry of aggregate expressions. Stores all aggregate expressions that
 * appears in a report query or subquery
 */
public final class AggregateTable {
	/** Array of AggrExprInfo objects to record all aggregates */
	private List aggrExprInfoList;

	/** */
	private List groupDefns;
	private Scriptable scope;

	/** the base query contains aggregate */
	private BaseQuery baseQuery;

	private String tempDir;

	private static Logger logger = Logger.getLogger(AggregateTable.class.getName());

	/**
	 * Used for de-serialization
	 */
	public AggregateTable(String tempDir) {
		logger.entering(AggregateTable.class.getName(), "AggregateTable");
		this.aggrExprInfoList = new ArrayList();
		this.tempDir = tempDir;
		logger.exiting(AggregateTable.class.getName(), "AggregateTable");
	}

	/**
	 * construct the aggregateTable from preparedQuery
	 * 
	 * @param query
	 */
	public AggregateTable(String tempDir, Scriptable scope, List groupDefns) {
		this(tempDir);
		Object[] params = { tempDir, scope, groupDefns };
		logger.entering(AggregateTable.class.getName(), "AggregateTable", params);

		this.groupDefns = groupDefns;
		this.scope = scope;
		logger.exiting(AggregateTable.class.getName(), "AggregateTable");
	}

	/**
	 * construct the aggregateTable from baseQuery
	 * 
	 * @param query
	 */
	public AggregateTable(String tempDir, BaseQuery query) {
		this(tempDir);
		logger.entering(AggregateTable.class.getName(), "AggregateTable", query);

		this.baseQuery = query;
		logger.exiting(AggregateTable.class.getName(), "AggregateTable");
	}

	// --------------registration of aggregation ------------------------------

	/**
	 * Returns an implementation of the AggregateRegistry interface used by
	 * ExpressionCompiler, to register aggregate expressions at the specified
	 * grouping level
	 * 
	 * @param groupLevel
	 * @param afterGroup
	 * @param cx
	 * @return
	 * @throws DataException
	 */
	public AggregateRegistry getAggrRegistry(int groupLevel, int calculationLevel, boolean isDetailedRow,
			ScriptContext cx) throws DataException {
		AggrRegistry aggrRegistry = new AggrRegistry(groupLevel, calculationLevel, isDetailedRow, cx);
		aggrRegistry.prepare(groupDefns, scope, baseQuery, aggrExprInfoList);
		return aggrRegistry;
	}

	// --------------calculation of aggregation ------------------------------

	private AggregateCalculator currentCalculator;

	/**
	 * @param odiResult
	 * @param scope
	 * @throws DataException
	 */
	public void calculate(IResultIterator odiResult, Scriptable scope, ScriptContext cx) throws DataException {
		currentCalculator = new AggregateCalculator(tempDir, aggrExprInfoList, odiResult);
		currentCalculator.calculate(scope, cx);
	}

	/**
	 * @param odiResult
	 * @param scope
	 * @param aggrValue
	 * @throws DataException
	 */
	public void calculate(IResultIterator odiResult, Scriptable scope, ScriptContext cx, JSAggrValueObject aggrValue)
			throws DataException {
		currentCalculator = new AggregateCalculator(tempDir, aggrExprInfoList, odiResult);
		currentCalculator.calculate(scope, cx);
	}

	/**
	 * @return
	 */
	public Scriptable getJSAggrValueObject() {
		if (currentCalculator == null)
			return null;

		return currentCalculator.getJSAggrValueObject();
	}

}
