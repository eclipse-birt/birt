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

package org.eclipse.birt.report.data.adapter.api;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.IBinding;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSetDesign;
import org.eclipse.birt.data.engine.api.querydefn.BaseDataSourceDesign;
import org.eclipse.birt.data.engine.api.querydefn.ColumnDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.GroupDefinition;
import org.eclipse.birt.data.engine.api.querydefn.InputParameterBinding;
import org.eclipse.birt.data.engine.api.querydefn.ParameterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ScriptExpression;
import org.eclipse.birt.data.engine.api.querydefn.SortDefinition;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DataSetParameterHandle;
import org.eclipse.birt.report.model.api.DataSourceHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.FilterConditionHandle;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ParamBindingHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.SortKeyHandle;

/**
 *
 */

public interface IModelAdapter {
	public enum ExpressionLocation {
		TABLE, CUBE
	}

	/**
	 * Adapts a Model data source handle to an equivalent BaseDataSourceDesign.
	 */
	BaseDataSourceDesign adaptDataSource(DataSourceHandle handle) throws BirtException;

	/**
	 * Adapts a Model data set handle to an equivalent BaseDataSetDesign.
	 */
	BaseDataSetDesign adaptDataSet(DataSetHandle handle) throws BirtException;

	/**
	 * Adapts a Model parameter definition
	 */
	ParameterDefinition adaptParameter(DataSetParameterHandle paramHandle);

	/**
	 * Adapts a Model input parameter binding definition
	 */
	InputParameterBinding adaptInputParamBinding(ParamBindingHandle modelHandle);

	/**
	 * Adapts a Model column definition
	 */
	ColumnDefinition ColumnAdaptor(ResultSetColumnHandle modelColumn);

	/**
	 * Adapts a Model computed column definition
	 *
	 * @throws AdapterException
	 *
	 */
	ComputedColumn adaptComputedColumn(ComputedColumnHandle modelHandle) throws AdapterException;

	/**
	 * Adapts a Model condition
	 *
	 * @param mainExpr Main expression; must not be null
	 * @param operator Operator
	 */
	ConditionalExpression adaptConditionalExpression(String mainExpr, String operator, String operand1,
			String operand2);

	ConditionalExpression adaptConditionalExpression(Expression mainExpr, String operator, Expression operand1,
			Expression operand2);

	ScriptExpression adaptExpression(Expression expr, ExpressionLocation el);

	ScriptExpression adaptExpression(Expression expr);

	/**
	 * Constructs an expression with provided text and return data type Data type is
	 * defined as a Model data type string
	 */

	ScriptExpression adaptExpression(Expression expr, String dataType);

	ScriptExpression adaptExpression(String jsExpr, String dataType);

	ScriptExpression adaptJSExpression(String jsExpr, String dataType);

	/*	*//**
			 * Adapts an expression based on Model computed column handle
			 *//*
				 * public ScriptExpression adaptExpression( ComputedColumnHandle ccHandle );
				 */

	/**
	 * Adapts a model filter handle
	 *
	 * @throws AdapterException
	 */
	FilterDefinition adaptFilter(FilterConditionHandle modelFilter);

	/**
	 * Adapts a model Group handle
	 *
	 * @throws AdapterException
	 */
	GroupDefinition adaptGroup(GroupHandle groupHandle);

	/**
	 * Adapts a model Sort handle
	 *
	 * @throws AdapterException
	 */
	SortDefinition adaptSort(SortKeyHandle sortHandle);

	/**
	 * Adapts a model Sort based on a sort key expression and a Model sort direction
	 * string
	 *
	 * @throws AdapterException
	 */
	SortDefinition adaptSort(Expression expr, String direction);

	/**
	 * Adapt a model computed column handle to an IBinding instance.
	 *
	 * @param handle
	 * @return
	 * @throws AdapterException
	 */
	IBinding adaptBinding(ComputedColumnHandle handle) throws AdapterException;

	/**
	 *
	 * Adapt a model computed column handle to an IBinding instance based on
	 * ExpressionLocation, which could be TABLE, CUBE.
	 *
	 * @param handle
	 * @param expressionLocation
	 * @return
	 * @throws AdapterException
	 */
	IBinding adaptBinding(ComputedColumnHandle handle, ExpressionLocation expressionLocation) throws AdapterException;
}
