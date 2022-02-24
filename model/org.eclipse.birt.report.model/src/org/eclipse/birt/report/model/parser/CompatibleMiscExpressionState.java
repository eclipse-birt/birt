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

package org.eclipse.birt.report.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.core.data.ExpressionUtil;
import org.eclipse.birt.core.data.IColumnBinding;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.model.api.StructureFactory;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.api.elements.structures.ComputedColumn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.GroupElement;
import org.eclipse.birt.report.model.elements.ScalarParameter;
import org.eclipse.birt.report.model.elements.interfaces.IAbstractScalarParameterModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.BoundDataColumnUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.xml.sax.SAXException;

/**
 * Parses expression values from BIRT 2.1M5 to BIRT 2.1 RC0. The rule is that if
 * any expression contains the DtE authorized string, creates the corresponding
 * bound data columns.
 * <p>
 * This is a part of backward compatibility work from BIRT 2.1M5 to BIRT 2.1.0.
 */

class CompatibleMiscExpressionState extends CompatibleExpressionState {

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler the handler to parse the design file.
	 * @param element    the data item
	 */

	CompatibleMiscExpressionState(ModuleParserHandler theHandler, DesignElement element) {
		super(theHandler, element);
	}

	/**
	 * Constructs a compatible state.
	 * 
	 * @param theHandler the handler to parse the design file.
	 * @param element    the data item
	 * @param propDefn
	 * @param struct
	 */

	CompatibleMiscExpressionState(ModuleParserHandler theHandler, DesignElement element, PropertyDefn propDefn,
			IStructure struct) {
		super(theHandler, element, propDefn, struct);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */

	public void end() throws SAXException {
		String value = text.toString();

		if (value == null)
			return;

		DesignElement target = BoundDataColumnUtil.findTargetOfBoundColumns(element, handler.module);

		// if the value is on elements like data set and data source. Not
		// require to create bound columns.

		if (target != null)
			setupBoundDataColumns(target, value, handler.versionNumber < VersionUtil.VERSION_3_2_0);

		// keep the expression as same.

		doEnd(value);
	}

	/**
	 * Changes rows[index] expression to the new format row._outer.
	 * 
	 * @see org.eclipse.birt.report.model.parser.PropertyState#doEnd(java.lang.String)
	 */

	protected void doEnd(Object value) {
		doEnd(value, false);
	}

	/**
	 * Sets the property value. This method changes rows[index] expression to the
	 * new format row._outer.
	 * 
	 * @param value               the value to set. Can contains rows[index]
	 *                            expression or not.
	 * @param isParamBindingValue <code>true</code> means the value is from
	 *                            parameter binding. Hence, it is not required to
	 *                            change from rows to row._outer.
	 * 
	 */

	protected void doEnd(Object value, boolean isParamBindingValue) {
		String newValue = (String) value;

		if (StringUtil.trimString((String) value) != null) {
			newValue = ExpressionUtil.updateParentQueryReferenceExpression((String) value, isParamBindingValue);
		}

		if (handler.versionNumber < VersionUtil.VERSION_3_2_19 && element instanceof ScalarParameter
				&& IAbstractScalarParameterModel.DEFAULT_VALUE_PROP.equalsIgnoreCase(name)) {
			CompatiblePropToExprState.handleDefaultValueList(handler.module, element, propDefn, nameValue,
					handler.versionNumber, newValue);
		}

		else
			super.doEnd(newValue);
	}

	/**
	 * Creates bound columns.
	 * 
	 * @param target             the target to add bound columns
	 * @param value              the expression value
	 * @param createLocalColumns <code>true</code> if create local data bound
	 *                           columns. Otherwise, only create data bound columns
	 *                           on the outer data container.
	 */

	protected void setupBoundDataColumns(DesignElement target, String value, boolean createLocalColumns) {
		if (value == null)
			return;

		List newExprs = null;

		try {
			newExprs = ExpressionUtil.extractColumnExpressions(value);
		} catch (BirtException e) {
			// do nothing
		}

		if (newExprs == null || newExprs.isEmpty())
			return;

		List<IColumnBinding> outerColumns = new ArrayList<IColumnBinding>();
		List<IColumnBinding> localColumns = new ArrayList<IColumnBinding>();
		for (int i = 0; i < newExprs.size(); i++) {
			IColumnBinding boundColumn = (IColumnBinding) newExprs.get(i);
			if (boundColumn.getOuterLevel() < 1)
				localColumns.add(boundColumn);
			else
				outerColumns.add(boundColumn);
		}

		if (!outerColumns.isEmpty()) {
			DesignElement tmpTarget = BoundDataColumnUtil.findTargetOfBoundColumns(target, handler.module, 1);
			addBoundColumnsToTarget(tmpTarget, outerColumns);
		}

		if (createLocalColumns)
			addBoundColumnsToTarget(target, localColumns);
	}

	/**
	 * Adds column bindings to the target element.
	 * 
	 * @param target   the target element
	 * @param newExprs a list containing column bindings
	 */

	protected void addBoundColumnsToTarget(DesignElement target, List<IColumnBinding> newExprs) {
		if (newExprs.isEmpty())
			return;

		if (target instanceof GroupElement) {
			appendBoundColumnsToGroup((GroupElement) target, newExprs);
			return;
		}

		for (int i = 0; i < newExprs.size(); i++) {
			IColumnBinding boundColumn = newExprs.get(i);
			String newExpression = boundColumn.getBoundExpression();
			if (newExpression == null)
				continue;

			BoundDataColumnUtil.createBoundDataColumn(target, boundColumn.getResultSetColumnName(), newExpression,
					handler.module);
		}
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target   the group element
	 * @param newExprs bound columns returned by ExpressionUtil
	 */

	private void appendBoundColumnsToGroup(GroupElement target, List<IColumnBinding> newExprs) {
		List<ComputedColumn> newColumns = new ArrayList<ComputedColumn>();
		for (int i = 0; i < newExprs.size(); i++) {
			ComputedColumn column = StructureFactory.createComputedColumn();
			IColumnBinding boundColumn = newExprs.get(i);
			String newExpression = boundColumn.getBoundExpression();
			if (newExpression == null)
				continue;

			column.setName(boundColumn.getResultSetColumnName());
			column.setExpression(boundColumn.getBoundExpression());
			if (!newColumns.contains(column))
				newColumns.add(column);
		}

		appendBoundColumnsToCachedGroup(target, newColumns);
	}

	/**
	 * Appends to the cached group bound columns. Becuase of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target   the group element
	 * @param newExprs bound columns returned by ExpressionUtil
	 */

	private void appendBoundColumnsToCachedGroup(GroupElement target, List<ComputedColumn> newColumns) {
		List boundColumns = (List) handler.tempValue.get(target);
		if (boundColumns == null) {
			handler.tempValue.put(target, newColumns);
			return;
		}

		for (int i = 0; i < newColumns.size(); i++) {
			ComputedColumn column = newColumns.get(i);
			if (!boundColumns.contains(column))
				boundColumns.add(column);
		}
	}

	/**
	 * Appends to the cached group bound columns. Because of "aggregateOn" property
	 * on bound columns, has to add bound columns at end() function of
	 * ListingElementState.
	 * 
	 * @param target     the group element
	 * @param boundName  the bound column name
	 * @param expression the bound column expression
	 * @return the return bound name
	 */

	protected String appendBoundColumnsToCachedGroup(GroupElement target, String boundName, String expression) {
		ComputedColumn column = StructureFactory.createComputedColumn();
		column.setName(boundName);
		column.setExpression(expression);

		List boundColumns = (List) handler.tempValue.get(target);
		if (boundColumns == null) {
			List newColumns = new ArrayList();
			newColumns.add(column);

			handler.tempValue.put(target, newColumns);
			return boundName;
		}

		if (!boundColumns.contains(column))
			boundColumns.add(column);

		return boundName;
	}
}
