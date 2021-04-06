/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.data.engine.expression;

import org.eclipse.birt.core.script.ScriptContext;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.executor.BaseQuery;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateRegistry;
import org.eclipse.birt.data.engine.impl.aggregation.AggregateTable;

/**
 * This class is used to populate an AggregationTable.
 */
final class AggregationTablePopulator {
	/**
	 * No instance
	 */
	private AggregationTablePopulator() {
	}

	/**
	 * close the context
	 */
	public static void close() {
	}

	/**
	 * Populate the AggregateTable instance using given AggregateObject.
	 * 
	 * @param table
	 * @param aggreObjList
	 * @param groupLvl
	 * @param aftergroup
	 * @return
	 * @throws DataException
	 */
	public static int populateAggregationTable(AggregateTable table, AggregateObject aggreObj, int groupLvl,
			int calculationLvl, boolean aftergroup, boolean isDetailedRow, ScriptContext cx) throws DataException {
		try {
			AggregateRegistry reg = table.getAggrRegistry(groupLvl, calculationLvl, isDetailedRow, cx);

			return reg.register(aggreObj.getAggregateExpr());
		} catch (DataException e) {
			throw e;
		}
	}

	/**
	 * create an aggregate table for base query.
	 * 
	 * @param query
	 * @return
	 */
	public static AggregateTable createAggregateTable(String tempDir, BaseQuery query) {
		return new AggregateTable(tempDir, query);
	}
}