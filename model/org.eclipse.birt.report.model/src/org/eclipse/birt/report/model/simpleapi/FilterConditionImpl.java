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

package org.eclipse.birt.report.model.simpleapi;

import org.eclipse.birt.report.model.activity.ActivityStack;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.FilterCondition;
import org.eclipse.birt.report.model.api.simpleapi.IFilterCondition;

/**
 * Implements of FilterCondition.
 *
 */

public class FilterConditionImpl extends Structure implements IFilterCondition {

	private FilterCondition condition;

	/**
	 * Constructor
	 *
	 * @param condition
	 */

	public FilterConditionImpl() {
		super(null);
		condition = createFilterCondition();
	}

	/**
	 * Constructor
	 *
	 * @param condition
	 */

	public FilterConditionImpl(FilterCondition condition) {
		super(null);
		if (condition == null) {
			this.condition = createFilterCondition();
		} else {

			this.condition = condition;
		}
	}

	/**
	 * Constructor
	 *
	 * @param conditionHandle
	 */

	public FilterConditionImpl(FilterConditionHandle conditionHandle) {
		super(conditionHandle);
		if (conditionHandle == null) {
			condition = createFilterCondition();
		} else {
			structureHandle = conditionHandle;
			condition = (FilterCondition) conditionHandle.getStructure();
		}
	}

	private FilterCondition createFilterCondition() {
		FilterCondition f = new FilterCondition();
		return f;
	}

	@Override
	public String getOperator() {
		return condition.getOperator();
	}

	@Override
	public String getValue1() {
		return condition.getValue1();
	}

	@Override
	public String getValue2() {
		return condition.getValue2();
	}

	@Override
	public void setOperator(String operator) throws SemanticException {
		if (structureHandle != null) {
			ActivityStack cmdStack = structureHandle.getModule().getActivityStack();

			cmdStack.startNonUndoableTrans(null);
			try {
				((FilterConditionHandle) structureHandle).setOperator(operator);
			} catch (SemanticException e) {
				cmdStack.rollback();
				throw e;
			}

			cmdStack.commit();
			return;
		}

		condition.setOperator(operator);
	}

	@Override
	public void setValue1(String value1) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.VALUE1_MEMBER, value1);
			return;
		}

		condition.setValue1(value1);
	}

	@Override
	public void setValue2(String value2) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.VALUE2_MEMBER, value2);
			return;
		}

		condition.setValue2(value2);
	}

	@Override
	public IStructure getStructure() {
		return condition;
	}

	@Override
	public String getExpr() {
		return condition.getExpr();
	}

	@Override
	public void setExpr(String expr) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.EXPR_MEMBER, expr);
			return;
		}

		condition.setExpr(expr);
	}

}
