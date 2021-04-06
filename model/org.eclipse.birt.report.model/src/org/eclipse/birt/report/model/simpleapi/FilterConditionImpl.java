/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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

	public String getOperator() {
		return condition.getOperator();
	}

	public String getValue1() {
		return condition.getValue1();
	}

	public String getValue2() {
		return condition.getValue2();
	}

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

	public void setValue1(String value1) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.VALUE1_MEMBER, value1);
			return;
		}

		condition.setValue1(value1);
	}

	public void setValue2(String value2) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.VALUE2_MEMBER, value2);
			return;
		}

		condition.setValue2(value2);
	}

	public IStructure getStructure() {
		return condition;
	}

	public String getExpr() {
		return condition.getExpr();
	}

	public void setExpr(String expr) throws SemanticException {
		if (structureHandle != null) {
			setProperty(FilterCondition.EXPR_MEMBER, expr);
			return;
		}

		condition.setExpr(expr);
	}

}
