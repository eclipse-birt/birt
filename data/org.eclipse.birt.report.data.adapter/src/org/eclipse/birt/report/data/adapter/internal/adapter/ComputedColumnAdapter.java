/*
 *************************************************************************
 * Copyright (c) 2006 Actuate Corporation.
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
 *  
 *************************************************************************
 */
package org.eclipse.birt.report.data.adapter.internal.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.api.aggregation.IAggrFunction;
import org.eclipse.birt.data.engine.api.aggregation.IParameterDefn;
import org.eclipse.birt.data.engine.api.querydefn.ComputedColumn;
import org.eclipse.birt.data.engine.core.DataException;
import org.eclipse.birt.report.data.adapter.api.AdapterException;
import org.eclipse.birt.report.data.adapter.api.IModelAdapter;
import org.eclipse.birt.report.model.api.AggregationArgumentHandle;
import org.eclipse.birt.report.model.api.ComputedColumnHandle;
import org.eclipse.birt.report.model.api.elements.structures.AggregationArgument;

/**
 * Adapts a Model computed column
 */
public class ComputedColumnAdapter extends ComputedColumn {

	public ComputedColumnAdapter(IModelAdapter adapter, ComputedColumnHandle modelCmptdColumn) throws AdapterException {

		super(modelCmptdColumn.getName(),
				adapter.adaptExpression(DataAdapterUtil.getExpression(modelCmptdColumn.getExpressionProperty(
						org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.EXPRESSION_MEMBER))),
				org.eclipse.birt.report.data.adapter.api.DataAdapterUtil
						.adaptModelDataType(modelCmptdColumn.getDataType()),
				modelCmptdColumn.getAggregateFunction(),
				modelCmptdColumn.getFilterExpression() == null ? null
						: adapter.adaptExpression(DataAdapterUtil.getExpression(modelCmptdColumn.getExpressionProperty(
								org.eclipse.birt.report.model.api.elements.structures.ComputedColumn.FILTER_MEMBER))),
				populateArgument(adapter, modelCmptdColumn));
	}

	/**
	 * Populate the arguments to a List by the order of the IAggrFunction saved
	 * 
	 * @param modelCmptdColumn
	 * @return
	 * @throws AdapterException
	 */
	private static List populateArgument(IModelAdapter adapter, ComputedColumnHandle modelCmptdColumn)
			throws AdapterException {
		Map argumentList = new HashMap();
		Iterator argumentIter = modelCmptdColumn.argumentsIterator();
		while (argumentIter.hasNext()) {
			AggregationArgumentHandle handle = (AggregationArgumentHandle) argumentIter.next();
			argumentList.put(handle.getName(), adapter.adaptExpression(
					DataAdapterUtil.getExpression(handle.getExpressionProperty(AggregationArgument.VALUE_MEMBER))));
		}

		List orderedArgument = new ArrayList();
		if (modelCmptdColumn.getAggregateFunction() != null) {
			IAggrFunction info = null;
			try {
				info = AggregationManager.getInstance().getAggregation(modelCmptdColumn.getAggregateFunction());
			} catch (DataException e) {
				e.printStackTrace();
			}
			if (info != null) {
				IParameterDefn[] parameters = info.getParameterDefn();

				if (parameters != null) {
					for (int i = 0; i < parameters.length; i++) {
						IParameterDefn pInfo = parameters[i];
						if (argumentList.get(pInfo.getName()) != null) {
							orderedArgument.add(argumentList.get(pInfo.getName()));
						}
					}
				}
			}
		}
		return orderedArgument;

	}

}
