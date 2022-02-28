/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.script.internal.element;

import org.eclipse.birt.report.engine.api.script.ScriptException;
import org.eclipse.birt.report.engine.api.script.element.IFilterCondition;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.SimpleElementFactory;

/**
 * Implements of FilterCondition.
 */

public class FilterConditionImpl implements IFilterCondition {

	private org.eclipse.birt.report.model.api.simpleapi.IFilterCondition filterConditionImpl;

	/**
	 * Constructor
	 *
	 * @param condition
	 */

	public FilterConditionImpl() {
		filterConditionImpl = SimpleElementFactory.getInstance().createFilterCondition();
	}

	/**
	 * Constructor
	 *
	 * @param condition
	 */

	public FilterConditionImpl(FilterCondition condition) {
		filterConditionImpl = SimpleElementFactory.getInstance().createFilterCondition(condition);
	}

	/**
	 * Constructor
	 *
	 * @param conditionHandle
	 */

	public FilterConditionImpl(FilterConditionHandle conditionHandle) {
		filterConditionImpl = SimpleElementFactory.getInstance().createFilterCondition(conditionHandle);
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public FilterConditionImpl(org.eclipse.birt.report.model.api.simpleapi.IFilterCondition condition) {
		filterConditionImpl = condition;
	}

	@Override
	public String getOperator() {
		return filterConditionImpl.getOperator();
	}

	@Override
	public String getValue1() {
		return filterConditionImpl.getValue1();
	}

	@Override
	public String getValue2() {
		return filterConditionImpl.getValue2();
	}

	@Override
	public void setOperator(String operator) throws ScriptException {
		try {

			filterConditionImpl.setOperator(operator);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setValue1(String value1) throws ScriptException {
		try {

			filterConditionImpl.setValue1(value1);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public void setValue2(String value2) throws ScriptException {
		try {

			filterConditionImpl.setValue2(value2);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

	@Override
	public IStructure getStructure() {
		return filterConditionImpl.getStructure();
	}

	@Override
	public String getExpr() {
		return filterConditionImpl.getExpr();
	}

	@Override
	public void setExpr(String expr) throws ScriptException {
		try {

			filterConditionImpl.setExpr(expr);
		} catch (SemanticException e) {
			throw new ScriptException(e.getLocalizedMessage());
		}

	}

}
