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
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.simpleapi.IDataBinding;

/**
 * Implements of DataBinding.
 *
 */

public class DataBindingImpl extends Structure implements IDataBinding {

	private ComputedColumn column;

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public DataBindingImpl() {
		super(null);
		column = createComputedColumn();
	}

	/**
	 * Constructor
	 *
	 * @param columnHandle
	 */

	public DataBindingImpl(ComputedColumnHandle columnHandle) {
		super(columnHandle);
		if (columnHandle == null) {
			column = createComputedColumn();
		} else {
			structureHandle = columnHandle;

			column = (ComputedColumn) columnHandle.getStructure();
		}
	}

	/**
	 * Constructor
	 *
	 * @param column
	 */

	public DataBindingImpl(ComputedColumn column) {
		super(null);
		if (column == null) {
			this.column = createComputedColumn();
		} else {

			this.column = column;
		}
	}

	/**
	 * Create computed column.
	 *
	 * @return instance of <code>ComputedColumn</code>
	 */
	private ComputedColumn createComputedColumn() {
		ComputedColumn c = new ComputedColumn();
		return c;
	}

	@Override
	public String getAggregateOn() {
		return column.getAggregateOn();
	}

	@Override
	public String getDataType() {
		return column.getDataType();
	}

	@Override
	public String getExpression() {
		return column.getExpression();
	}

	@Override
	public String getName() {
		return column.getName();
	}

	@Override
	public void setAggregateOn(String on) throws SemanticException {
		if (structureHandle != null) {
			ActivityStack cmdStack = structureHandle.getModule().getActivityStack();

			cmdStack.startNonUndoableTrans(null);
			((ComputedColumnHandle) structureHandle).setAggregateOn(on);

			cmdStack.commit();
			return;
		}
		column.setAggregateOn(on);
	}

	@Override
	public void setDataType(String dataType) throws SemanticException {
		if (structureHandle != null) {
			setProperty(ComputedColumn.DATA_TYPE_MEMBER, dataType);
			return;
		}

		column.setDataType(dataType);
	}

	@Override
	public void setExpression(String expression) throws SemanticException {
		if (structureHandle != null) {
			setProperty(ComputedColumn.EXPRESSION_MEMBER, expression);
			return;
		}

		// expression is required.
		column.setExpression(expression);
	}

	@Override
	public void setName(String name) throws SemanticException {
		if (structureHandle != null) {
			setProperty(ComputedColumn.NAME_MEMBER, name);
			return;
		}

		// name is required.
		column.setName(name);
	}

	@Override
	public IStructure getStructure() {
		return column;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDataBinding#getExpressionType ()
	 */
	@Override
	public String getExpressionType() {
		Expression expression = column.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);
		if (expression == null) {
			return null;
		}

		return expression.getType();

	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.api.simpleapi.IDataBinding#setExpressionType
	 * (java.lang.String)
	 */
	@Override
	public void setExpressionType(String type) throws SemanticException {
		if (structureHandle != null) {
			ExpressionHandle handle = structureHandle.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);
			if (handle != null) {
				handle.setType(type);
			} else {
				Expression newExpression = new Expression(null, type);
				structureHandle.setExpressionProperty(ComputedColumn.EXPRESSION_MEMBER, newExpression);
			}

			return;
		}

		Expression expression = column.getExpressionProperty(ComputedColumn.EXPRESSION_MEMBER);

		Expression newValue = null;
		if (expression != null) {
			newValue = new Expression(expression.getExpression(), type);
		} else if (type != null) {
			newValue = new Expression(null, type);
		}

		column.setExpressionProperty(ComputedColumn.EXPRESSION_MEMBER, newValue);
	}

}
