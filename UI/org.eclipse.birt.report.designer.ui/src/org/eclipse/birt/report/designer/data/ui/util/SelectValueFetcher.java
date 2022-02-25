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

package org.eclipse.birt.report.designer.data.ui.util;

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.data.engine.api.DataEngineContext.DataEngineFlowMode;
import org.eclipse.birt.report.designer.internal.ui.data.DataService;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.Expression;
import org.eclipse.birt.report.model.api.ExpressionType;
import org.eclipse.birt.report.model.api.ModuleHandle;

/**
 * Utility class to fetch all available value for filter use.
 *
 */
public class SelectValueFetcher {
	/**
	 * private constructor
	 */
	private SelectValueFetcher() {
	}

	/**
	 * @deprecated
	 */
	@Deprecated
	public static List getSelectValueList(String expression, DataSetHandle dataSetHandle, boolean useDataSetFilter)
			throws BirtException {
		Expression jsExpression = new Expression(expression, ExpressionType.JAVASCRIPT);
		return getSelectValueList(jsExpression, dataSetHandle, useDataSetFilter);
	}

	/**
	 * @deprecated
	 * @param jsExpression
	 * @param dataSetHandle
	 * @param binding
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	@Deprecated
	public static List getSelectValueFromBinding(String expression, DataSetHandle dataSetHandle, Iterator binding,
			boolean useDataSetFilter) throws BirtException {
		Expression jsExpression = new Expression(expression, ExpressionType.JAVASCRIPT);
		return getSelectValueFromBinding(jsExpression, dataSetHandle, binding, useDataSetFilter);
	}

	/**
	 * Used in the filter select value dialog in dataset editor
	 *
	 * @param expression
	 * @param dataSetHandle
	 * @param binding
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList(Expression expression, DataSetHandle dataSetHandle, boolean useDataSetFilter)
			throws BirtException {
		return DataService.getInstance().getSelectValueList(expression, dataSetHandle, useDataSetFilter);
	}

	public static List getSelectValueList(Expression expression, DataSetHandle dataSetHandle,
			DataEngineFlowMode flowMode) throws BirtException {
		return DataService.getInstance().getSelectValueList(expression, dataSetHandle, flowMode);
	}

	public static List getSelectValueList(Expression expression, ModuleHandle moduleHandle, DataSetHandle dataSetHandle,
			boolean useDataSetFilter) throws BirtException {
		return DataService.getInstance().getSelectValueList(expression, moduleHandle, dataSetHandle, useDataSetFilter);
	}

	/**
	 * Used in filter select value dialog in layout without group definition.
	 *
	 * @param expression
	 * @param dataSetHandle
	 * @param binding
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueFromBinding(Expression expression, DataSetHandle dataSetHandle, Iterator binding,
			boolean useDataSetFilter) throws BirtException {
		return getSelectValueFromBinding(expression, dataSetHandle, binding, null, useDataSetFilter);
	}

	/**
	 * Used in filter select value dialog in layout with group definition.
	 *
	 * @param expression
	 * @param dataSetHandle
	 * @param binding          The iterator of ComputedColumnHandle
	 * @param groupIterator    The iterator of GroupHandle
	 * @param useDataSetFilter
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueFromBinding(Expression expression, DataSetHandle dataSetHandle, Iterator binding,
			Iterator groupIterator, boolean useDataSetFilter) throws BirtException {
		return DataService.getInstance().getSelectValueFromBinding(expression, dataSetHandle, binding, groupIterator,
				useDataSetFilter);
	}

	public static List getSelectValueFromBinding(Expression expression, ModuleHandle moduleHandle,
			DataSetHandle dataSetHandle, Iterator binding, Iterator groupIterator, boolean useDataSetFilter)
			throws BirtException {
		return DataService.getInstance().getSelectValueFromBinding(expression, moduleHandle, dataSetHandle, binding,
				groupIterator, useDataSetFilter);
	}

	/**
	 *
	 * @param selectValueExpression
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList(String expression, DataSetHandle dataSetHandle) throws BirtException {
		return getSelectValueList(expression, dataSetHandle, true);
	}

	/**
	 *
	 * @param selectValueExpression
	 * @param dataSetHandle
	 * @return
	 * @throws BirtException
	 */
	public static List getSelectValueList(Expression expression, DataSetHandle dataSetHandle) throws BirtException {
		return getSelectValueList(expression, dataSetHandle, true);
	}
}
