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

import java.util.List;
import java.util.logging.Logger;

import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.data.engine.odi.IResultIterator;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

/**
 * JS object to retrieve aggregation value.
 */
public class JSAggrValueObject extends ScriptableObject {
	private int aggrCount;
	private List aggrExprInfoList;
	private IResultIterator odiResult;
	private List[] aggrValues;

	/** serialVersionUID = 1L; */
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger(JSAggrValueObject.class.getName());

	/**
	 * @param aggrExprInfoList
	 * @param odiResult
	 * @param aggrValues
	 * @param hasOdiResultDataRows
	 */
	JSAggrValueObject(List aggrExprInfoList, IResultIterator odiResult, List[] aggrValues) {
		Object[] params = { aggrExprInfoList, odiResult, aggrValues };
		logger.entering(JSAggrValueObject.class.getName(), "JSAggrValueObject", params);
		this.aggrExprInfoList = aggrExprInfoList;
		this.odiResult = odiResult;
		this.aggrValues = aggrValues;

		this.aggrCount = aggrExprInfoList.size();
		logger.exiting(JSAggrValueObject.class.getName(), "JSAggrValueObject");
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#getClassName()
	 */
	public String getClassName() {
		return "_RESERVED_AGGR_VALUE";
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#has(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	public boolean has(int index, Scriptable start) {
		return index > 0 && index < this.aggrCount;
	}

	/*
	 * @see org.mozilla.javascript.Scriptable#get(int,
	 * org.mozilla.javascript.Scriptable)
	 */
	public Object get(int index, Scriptable start) {
		if (index < 0 || index >= this.aggrCount) {
			// Should never get here
			return null;
		}

		try {
			return getAggregateValue(index);
		} catch (DataException e) {
			throw Context.reportRuntimeError(e.getLocalizedMessage());
		}
	}

	/**
	 * Get the aggregate value
	 * 
	 * @param aggrIndex
	 * @return
	 * @throws DataException
	 */
	private Object getAggregateValue(int aggrIndex) throws DataException {
		AggrExprInfo aggrInfo = getAggrInfo(aggrIndex);

		if (this.odiResult.getRowCount() == 0) {
			return aggrInfo.aggregation.getDefaultValue();
		}

		try {
			int groupIndex;

			if (aggrInfo.aggregation.getType() == IAggrFunction.SUMMARY_AGGR) {
				// Aggregate on the whole list: there is only one group
				if (aggrInfo.groupLevel == 0)
					groupIndex = 0;
				else
					groupIndex = this.odiResult.getCurrentGroupIndex(aggrInfo.groupLevel);
			} else {
				groupIndex = this.odiResult.getCurrentResultIndex();
			}

			return this.aggrValues[aggrIndex].get(groupIndex);

		} catch (DataException e) {
			throw e;
		}
	}

	/**
	 * Gets information about one aggregate expression in the table, given its index
	 */
	private AggrExprInfo getAggrInfo(int i) {
		return (AggrExprInfo) this.aggrExprInfoList.get(i);
	}

	/**
	 * Get aggregation's count
	 * 
	 * @return
	 */
	int getAggrCount() {
		return this.aggrExprInfoList.size();
	}

	/**
	 * Get the aggregate value list.
	 * 
	 * @param i
	 * @return
	 */
	public List getAggregateValues(int i) {
		if (i < this.aggrCount)
			return aggrValues[i];
		else
			return null;
	}

}
